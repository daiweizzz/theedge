@echo off
setlocal EnableExtensions
cd /d "%~dp0"

set "DIR=%~dp0"
if "%DIR:~-1%"=="\" set "DIR=%DIR:~0,-1%"

echo.
echo ==========================================================
echo  Push the mod project to GitHub
echo ==========================================================
echo.
echo Fixing repository ownership...
git config --global --add safe.directory "%DIR%"

echo.
echo A browser may open to sign in to GitHub. Complete the sign-in.
echo.

git push -u origin main > "%~dp0push.log" 2>&1
set "PUSH_EXIT=%ERRORLEVEL%"
type "%~dp0push.log"

if not "%PUSH_EXIT%"=="0" (
  echo.
  echo Push did not finish. The error was saved to push.log.
) else (
  echo.
  echo Done! Open github.com/daiweizzz/theedge and click the Actions tab.
  echo Wait for the "build-mod" run to finish, then download the "theedge-mod" artifact.
  echo The playable jar is inside that downloaded zip.
)

echo.
pause
