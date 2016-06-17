@echo off

echo Stopping the processes, Waiting ...

SETLOCAL

PATH C:\TEST_MON\BIN;%PATH%

C:\MON_TEST\BIN\monitorapp -p C:\MON_TEST\CONF\app.ini -s

IF ERRORLEVEL 1 goto RESNOTOK

echo Processes stopped with success!
goto END

:RESNOTOK
echo Processes NOT stopped!

:END
ENDLOCAL
