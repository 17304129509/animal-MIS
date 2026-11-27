$ErrorActionPreference = 'Stop'
$project = Split-Path -Parent $PSScriptRoot
$frontend = Join-Path $project 'frontend'
$static = Join-Path $project 'src/main/resources/static'
$assets = Join-Path $static 'assets'

Push-Location $project
try {
    & npm.cmd --prefix $frontend ci --no-audit --no-fund
    if ($LASTEXITCODE -ne 0) { throw 'Installing frontend build dependencies failed.' }
    & npm.cmd --prefix $frontend run build
    if ($LASTEXITCODE -ne 0) { throw 'Frontend build failed.' }

    if (Test-Path -LiteralPath $assets) {
        $physicalAssets = (Resolve-Path -LiteralPath $assets).Path
        $physicalProject = (Resolve-Path -LiteralPath $project).Path
        $expected = Join-Path $physicalProject 'src/main/resources/static/assets'
        if ($physicalAssets -ne $expected -or
            ((Get-Item -LiteralPath $physicalAssets).Attributes -band [IO.FileAttributes]::ReparsePoint)) {
            throw 'Cannot replace the generated frontend assets at an unexpected path.'
        }
        Remove-Item -LiteralPath $physicalAssets -Recurse -Force
    }
    Copy-Item -LiteralPath (Join-Path $frontend 'dist/assets') -Destination $assets -Recurse
    Copy-Item -LiteralPath (Join-Path $frontend 'dist/index.html') -Destination $static -Force

    & mvn.cmd -B clean verify
    if ($LASTEXITCODE -ne 0) { throw 'Maven build failed.' }
    $package = Join-Path $project 'package'
    New-Item -ItemType Directory -Path $package -Force | Out-Null
    Copy-Item -LiteralPath (Join-Path $project 'target/animal-rescue-1.0.0.jar') `
        -Destination (Join-Path $package 'animal-rescue.jar') -Force
} finally {
    Pop-Location
}
