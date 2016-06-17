@ECHO OFF

SETLOCAL ENABLEDELAYEDEXPANSION

SET JAVAVM=java.exe

SET TMF_XP= 
SET LIB=..\lib
SET DIST=..\dist

for %%i in (%LIB%\sirius-base*.jar) do set TMF_XP=!TMF_XP!;%%i
for %%i in (%DIST%\I36*.jar) do set TMF_XP=!TMF_XP!;%%i
for %%i in (%LIB%\commons-logging*.jar) do set TMF_XP=!TMF_XP!;%%i
for %%i in (%LIB%\log4j*.jar) do set TMF_XP=!TMF_XP!;%%i

rem SET TMF_XP=.;../LIB;TMF_UM%;TMF_CL%;TMF_LJ%

echo %TMF_XP%

%JAVAVM% -Dlog4j.configuration=file:../CONF/log4j.xml -classpath %TMF_XP% com.marconi.fusion.base.asn1.msg.FileMsgBerReader com.marconi.fusion.X36.X36MessageFactory %* 


:END

ENDLOCAL

