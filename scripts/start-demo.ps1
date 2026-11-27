param(
    [ValidateRange(1, 65535)][int]$UserPort = 8081,
    [ValidateRange(1, 65535)][int]$AdminPort = 8082,
    [ValidateRange(1, 65535)][int]$DatabasePort = 33067,
    [switch]$NoBrowser,
    [switch]$DownloadRuntimes
)

$ErrorActionPreference = 'Stop'
$ProgressPreference = 'SilentlyContinue'
$project = Split-Path -Parent $PSScriptRoot
$runtime = Join-Path $project '.runtime'
$jar = Join-Path $project 'package/animal-rescue.jar'
$localData = Join-Path $runtime 'mysql-data'
$credentialFile = Join-Path $runtime 'credentials.json'
$seedMarker = Join-Path $runtime 'demo-seeded'
$dbPidFile = Join-Path $runtime 'mysql.pid'
$mysql = $null
$app = $null
$lock = $null
$clientBin = $null
$credentials = $null
$exitStatus = 0

function Read-Download([string]$Url, [string]$Target) {
    & curl.exe --fail --silent --show-error --location --retry 2 --ssl-revoke-best-effort $Url -o $Target
    if ($LASTEXITCODE -ne 0) { throw "Download failed: $Url" }
}

function Install-Zip([string]$Url, [string]$Sha256, [string]$ExpectedFile) {
    if (Test-Path -LiteralPath $ExpectedFile -PathType Leaf) { return }
    $archive = Join-Path $runtime ("download-" + [guid]::NewGuid().ToString('N') + ".zip")
    try {
        Write-Host "Downloading $Url"
        Read-Download $Url $archive
        if ((Get-FileHash -LiteralPath $archive -Algorithm SHA256).Hash -ne $Sha256) {
            throw 'Download checksum mismatch.'
        }
        & tar.exe -xf $archive -C $runtime
        if ($LASTEXITCODE -ne 0 -or !(Test-Path -LiteralPath $ExpectedFile -PathType Leaf)) {
            throw 'Runtime extraction failed.'
        }
    } finally {
        if (Test-Path -LiteralPath $archive) { Remove-Item -LiteralPath $archive -Force }
    }
}

function Find-Java {
    if (!$DownloadRuntimes) {
        $javaCommand = Get-Command java.exe -CommandType Application -ErrorAction SilentlyContinue |
            Select-Object -First 1
        if ($javaCommand) {
            $params = New-Object System.Diagnostics.ProcessStartInfo
            $params.FileName = $javaCommand.Source
            $params.Arguments = '-version'
            $params.UseShellExecute = $false
            $params.CreateNoWindow = $true
            $params.RedirectStandardError = $true
            $probe = [System.Diagnostics.Process]::Start($params)
            $version = $probe.StandardError.ReadToEnd()
            $probe.WaitForExit()
            if ($probe.ExitCode -eq 0 -and $version -match 'version "([0-9]+)') {
                if ([int]$Matches[1] -ge 17) { return $javaCommand.Source }
            }
        }
    }
    $folder = 'zulu17.70.201-ca-jre17.0.21.1-win_x64'
    $exe = Join-Path $runtime "$folder/bin/java.exe"
    Install-Zip "https://cdn.azul.com/zulu/bin/$folder.zip" `
        '7ac4b62ed405c724dfd5731442f77a655d0bfc8dd3e92921702292280aa75f55' $exe
    return $exe
}

function Find-MySql {
    if (!$DownloadRuntimes) {
        $mysqlCommand = Get-Command mysqld.exe -CommandType Application -ErrorAction SilentlyContinue |
            Select-Object -First 1
        if ($mysqlCommand) {
            $directory = Split-Path -Parent $mysqlCommand.Source
            $version = & $mysqlCommand.Source --no-defaults --version
            if ($LASTEXITCODE -eq 0 -and $version -match '\bVer 8\.' -and
                (Test-Path (Join-Path $directory 'mysql.exe')) -and
                (Test-Path (Join-Path $directory 'mysqladmin.exe'))) { return $directory }
        }
    }
    $folder = 'mysql-8.0.46-winx64'
    $exe = Join-Path $runtime "$folder/bin/mysqld.exe"
    Install-Zip "https://cdn.mysql.com/Downloads/MySQL-8.0/$folder.zip" `
        '28E9EDA019D88EFF4478D811EA2110B83F02A3966BE157FE91CC55DEF3AB0D4D' $exe
    return Split-Path -Parent $exe
}

function Assert-PortAvailable([int]$Port) {
    $probe = New-Object System.Net.Sockets.TcpListener([System.Net.IPAddress]::Loopback, $Port)
    try {
        $probe.Start()
    } catch {
        throw "Local port $Port is busy. Stop the other program or select another port."
    } finally {
        $probe.Stop()
    }
}

function Write-Utf8([string]$File, [string]$Content) {
    [System.IO.File]::WriteAllText($File, $Content, (New-Object System.Text.UTF8Encoding($false)))
}

function MySql-Query([string]$Sql) {
    $settings = New-Object System.Diagnostics.ProcessStartInfo
    $settings.FileName = Join-Path $clientBin 'mysql.exe'
    $settings.Arguments = "--no-defaults --protocol=TCP --host=127.0.0.1 --port=$DatabasePort --user=root --default-character-set=utf8mb4 --batch --skip-column-names"
    $settings.UseShellExecute = $false
    $settings.CreateNoWindow = $true
    $settings.RedirectStandardInput = $true
    $settings.RedirectStandardOutput = $true
    $settings.RedirectStandardError = $true
    $settings.EnvironmentVariables['MYSQL_PWD'] = $credentials.rootPassword
    $process = [System.Diagnostics.Process]::Start($settings)
    try {
        $stdout = $process.StandardOutput.ReadToEndAsync()
        $stderr = $process.StandardError.ReadToEndAsync()
        $payload = [Text.Encoding]::UTF8.GetBytes($Sql + "`n")
        $process.StandardInput.BaseStream.Write($payload, 0, $payload.Length)
        $process.StandardInput.Close()
        $process.WaitForExit()
        if ($process.ExitCode -ne 0) { throw "MySQL command failed: $($stderr.Result)" }
        return $stdout.Result.Trim()
    } finally {
        $process.Dispose()
    }
}

function Wait-Ready($Process, [string]$Name, [scriptblock]$Check) {
    for ($attempt = 0; $attempt -lt 90; $attempt++) {
        if ($Process.HasExited) { throw "$Name exited. Check the logs in $runtime" }
        try {
            if (& $Check) { return }
        } catch {}
        Start-Sleep -Seconds 1
    }
    throw "$Name startup timed out. Check the logs in $runtime"
}

try {
    if ($env:OS -ne 'Windows_NT' -or $env:PROCESSOR_ARCHITECTURE -ne 'AMD64') {
        throw 'This launcher supports Windows x64. Other platforms can use the README build commands.'
    }
    if (!(Test-Path -LiteralPath $jar -PathType Leaf)) {
        throw 'The application JAR is missing. Run scripts/build-package.ps1 or download the complete repository ZIP.'
    }
    if (@($UserPort, $AdminPort, $DatabasePort | Select-Object -Unique).Count -ne 3) {
        throw 'Choose three different ports.'
    }
    New-Item -ItemType Directory -Force -Path $runtime | Out-Null
    $lock = [IO.File]::Open((Join-Path $runtime 'demo.lock'), 'OpenOrCreate', 'ReadWrite', 'None')
    foreach ($port in @($UserPort, $AdminPort, $DatabasePort)) { Assert-PortAvailable $port }
    $java = Find-Java
    $clientBin = Find-MySql
    $server = Join-Path $clientBin 'mysqld.exe'
    $base = Split-Path -Parent $clientBin

    if (Test-Path -LiteralPath $credentialFile) {
        $credentials = Get-Content -LiteralPath $credentialFile -Raw | ConvertFrom-Json
    } else {
        $credentials = @{
            rootPassword = [guid]::NewGuid().ToString('N')
            appPassword = [guid]::NewGuid().ToString('N')
            jwtSecret = [guid]::NewGuid().ToString('N') + [guid]::NewGuid().ToString('N')
        }
        Write-Utf8 $credentialFile ($credentials | ConvertTo-Json)
    }
    $serverArgs = @('--no-defaults', "--basedir=`"$base`"", "--datadir=`"$localData`"")
    $readyFile = Join-Path $runtime 'mysql-initialized'
    if (!(Test-Path -LiteralPath $readyFile)) {
        Write-Host 'Initializing the local MySQL demo database...'
        $init = Start-Process $server -ArgumentList ($serverArgs + @('--initialize-insecure','--console')) `
            -WindowStyle Hidden -PassThru -Wait -RedirectStandardError (Join-Path $runtime 'mysql-initialize.log')
        if ($init.ExitCode -ne 0) { throw "MySQL initialization failed. See $runtime/mysql-initialize.log" }
        New-Item -ItemType File -Path $readyFile | Out-Null
    }
    $serverArgs += @('--console', '--bind-address=127.0.0.1', "--port=$DatabasePort", '--mysqlx=OFF',
        '--skip-log-bin', '--innodb-buffer-pool-size=128M', "--pid-file=`"$dbPidFile`"")
    $initSql = Join-Path $runtime 'mysql-first-start.sql'
    if (!(Test-Path -LiteralPath $seedMarker)) {
        Write-Utf8 $initSql ("ALTER USER 'root'@'localhost' IDENTIFIED BY '" + $credentials.rootPassword + "';`n")
        $serverArgs += "--init-file=`"$initSql`""
    }
    $mysql = Start-Process $server -ArgumentList $serverArgs -WindowStyle Hidden -PassThru `
        -RedirectStandardOutput (Join-Path $runtime 'mysql.stdout.log') `
        -RedirectStandardError (Join-Path $runtime 'mysql.stderr.log')
    Wait-Ready $mysql 'MySQL' {
        $tcp = New-Object System.Net.Sockets.TcpClient
        try { $tcp.Connect('127.0.0.1', $DatabasePort); return $tcp.Connected }
        finally { $tcp.Dispose() }
    }
    if (Test-Path -LiteralPath $initSql) { Remove-Item -LiteralPath $initSql -Force }
    if (!(Test-Path -LiteralPath $seedMarker)) {
        $schema = (Get-Content -LiteralPath (Join-Path $project 'database/schema.sql') -Raw -Encoding UTF8) `
            -replace 'CREATE TABLE ', 'CREATE TABLE IF NOT EXISTS '
        MySql-Query $schema | Out-Null
        if ((MySql-Query 'SELECT COUNT(*) FROM campus_animal.user;') -eq '0') {
            $seed = Get-Content -LiteralPath (Join-Path $project 'database/demo-data.sql') -Raw -Encoding UTF8
            MySql-Query "USE campus_animal; START TRANSACTION; $seed COMMIT;" | Out-Null
        }
        MySql-Query ("CREATE USER IF NOT EXISTS 'animal_demo'@'localhost' IDENTIFIED BY '" + $credentials.appPassword +
            "'; GRANT SELECT, INSERT, UPDATE, DELETE ON campus_animal.* TO 'animal_demo'@'localhost';") | Out-Null
        New-Item -ItemType File -Path $seedMarker | Out-Null
    }
    $env:DB_URL = "jdbc:mysql://127.0.0.1:$DatabasePort/campus_animal?connectionTimeZone=Asia/Shanghai&sslMode=DISABLED&allowPublicKeyRetrieval=true"
    $env:DB_USER = 'animal_demo'
    $env:DB_PASSWORD = $credentials.appPassword
    $env:JWT_SECRET = $credentials.jwtSecret
    $env:SERVER_ADDRESS = '127.0.0.1'
    $env:SERVER_PORT = $UserPort
    $env:ADMIN_PORT = $AdminPort
    $env:UPLOAD_DIR = Join-Path $runtime 'uploads'
    $app = Start-Process $java -ArgumentList @('-jar', "`"$jar`"") -WindowStyle Hidden -PassThru `
        -WorkingDirectory $project -RedirectStandardOutput (Join-Path $runtime 'app.stdout.log') `
        -RedirectStandardError (Join-Path $runtime 'app.stderr.log')
    Wait-Ready $app 'Animal rescue application' {
        $response = Invoke-WebRequest -UseBasicParsing -TimeoutSec 3 -Uri "http://127.0.0.1:$UserPort/api/animal?pageSize=1"
        return ($response.StatusCode -eq 200 -and ($response.Content | ConvertFrom-Json).code -eq 200)
    }
    Write-Host "User website  http://127.0.0.1:$UserPort/"
    Write-Host "Admin website http://127.0.0.1:$AdminPort/"
    Write-Host 'Demo users: stu001, vol001, admin001. Password: 123456'
    Write-Host 'Press Ctrl+C to stop the app and its private database.'
    if (!$NoBrowser) {
        Start-Process "http://127.0.0.1:$UserPort/"
        Start-Process "http://127.0.0.1:$AdminPort/"
    }
    while (!$app.WaitForExit(750)) {}
    $exitStatus = $app.ExitCode
} catch {
    [Console]::Error.WriteLine($_.Exception.Message)
    $exitStatus = 1
} finally {
    if ($app -and !$app.HasExited) { $app.Kill(); $app.WaitForExit() }
    if ($mysql -and !$mysql.HasExited) {
        $env:MYSQL_PWD = $credentials.rootPassword
        try {
            & (Join-Path $clientBin 'mysqladmin.exe') --no-defaults --protocol=TCP --host=127.0.0.1 `
                "--port=$DatabasePort" --user=root --connect-timeout=3 shutdown 2>$null
        } catch {}
        if (!$mysql.WaitForExit(10000)) { $mysql.Kill(); $mysql.WaitForExit() }
    }
    if ($lock) { $lock.Dispose() }
}
exit $exitStatus
