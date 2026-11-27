@echo off
setlocal
powershell.exe -NoLogo -NoProfile -ExecutionPolicy Bypass -File "%~dp0scripts\start-demo.ps1" %*
set "exit_code=%errorlevel%"
if not "%exit_code%"=="0" pause
exit /b %exit_code%
