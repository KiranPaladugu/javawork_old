@ECHO OFF

SETLOCAL

SET JAVAVM=java.exe

for %%i in (..\LIB\TMFUser-Management*.jar) do set TMF_UM=%%i
for %%i in (..\LIB\commons-logging*.jar) do set TMF_CL=%%i
for %%i in (..\LIB\log4j*.jar) do set TMF_LJ=%%i

SET TMF_XP=.;../LIB;TMF_UM%;TMF_CL%;TMF_LJ%

%JAVAVM% -Dlog4j.configuration=file:../CONF/log4j.xml -classpath %TMF_XP% com.marconi.fusion.tmf.usermgt.UserPasswd

IF ERRORLEVEL 1 goto RESNOTOK

goto END

:RESNOTOK
echo UserPasswd application NOT executed!

:END
ENDLOCAL


