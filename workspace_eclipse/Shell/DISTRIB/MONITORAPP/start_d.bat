@echo off

echo Starting the processes, Waiting ...

SETLOCAL

PATH C:\TEST_MON\BIN;%PATH%

C:\MON_TEST\BIN\monitorapp -p C:\MON_TEST\CONF\app.ini

IF ERRORLEVEL 1 goto RESNOTOK

echo Processes started with success!
goto END

:RESNOTOK
echo Processes NOT started!

:END
ENDLOCAL
