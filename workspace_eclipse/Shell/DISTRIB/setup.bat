@echo off

echo Starting installation procedure, Wait please...

SETLOCAL

PATH %CD%\BIN_WIN;%CD%;%PATH%

COLOR F0

%CD%\BIN_WIN\bash.exe -c ". ./AgentInst.sh"

IF ERRORLEVEL 1 goto RESNOTOK

echo Installation procedure executed with success!
goto END

:RESNOTOK
echo Installation procedure NOT executed!

:END
PAUSE
ENDLOCAL
