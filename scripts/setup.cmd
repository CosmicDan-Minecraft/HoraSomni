@echo off
SETLOCAL
pushd %~dp0\..
call gradlew.bat genSources
echo.
echo.
echo ---------------------------------------
echo.
echo.
echo Done. You can now import/refresh build.gradle in IntelliJ.
echo.
pause
