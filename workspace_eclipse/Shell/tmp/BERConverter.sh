
RUN_FILE=com.marconi.tmp.tools.BERConverter

choice()
{
	CHOICE="no"
	echo "Do you want Convert BER ? [ yes/no default:yes]"
	read TMP
	if [ "$TMP" = "" ]
	then		
		CHOICE="yes"
	else		
		CHOICE=$TMP
	fi

	if [ "$CHOICE" = "yes" ]
	then		
		return 1;
	fi
	if [ "$CHOICE" = "no" ]
	then 
		return 0;
	fi
return -1
}

inputme(){
choice
VAL=$?
if [[ ${VAL} -eq 1 || ${VAL} -eq 0 ]]
	then
		if [ ${VAL} -eq 1 ]
		then
		echo " Plz Wait while starting BER Convertion .."
		process
		fi
	else
		echo "Please enter only yes / no"
		inputme
fi

}


echo "Starting.."

inputme

process(){

if [ -e "ENV_FILE" ];
then
  . "ENV_FILE"
else
  echo ""
  echo "Cannot find environment file ENV_FILE"
  echo "Exiting management procedure..."
  echo ""
  exit 1
fi

SCRIPT_DIR=$(pwd)


## ROOT_DIR=AGENT_FULL_PATH
LIB_DIR="${ROOT_DIR}"/LIB
CONF_DIR="${ROOT_DIR}"/CONF
BER_DIR="${ROOT_DIR}"/BER
BIN_DIR="${ROOT_DIR}"/BIN
USERS_DIR="${ROOT_DIR}"/USERS

if [[ -x /usr/bin/whoami ]]; then
  if [[ $(/usr/bin/whoami) != "AGENT_USER" ]]; then
    echo ""
    echo "Sorry, you must be AGENT_USER user to run this script!"
    echo "${Exiting}"
    ExitVal="${Success}"
    exit 1
  fi
fi

cur_dir=$(pwd)
cd "${SCRIPT_DIR}"

JOPTS="-server"

curr_os=$(uname -s)
if [ "${curr_os}" = "HP-UX" -o "${curr_os}" = "Linux" ];
then
  sep=":"
else
  sep=";"
fi

if [ "${curr_os}" = "HP-UX" ];
then
  JOPTS="${JOPTS} -V2"
fi

JAVA_EXE="/opt/java1.6/jre/bin/java"

if [ -x "${JAVA_HOME}/jre/bin/java" ];
then
  JAVA_EXE="${JAVA_HOME}/jre/bin/java"
elif [ -x "${JAVA_HOME}/bin/java" ];
then
  JAVA_EXE="${JAVA_HOME}/bin/java"
else
  echo ""
  echo "Cannot find java on your server!"
  echo "Please check your installation"
  echo "Exiting..."
  echo ""
  exit 1
fi

TMF_XP="."

file_list=$(find "${LIB_DIR}" \( -type f -o -type l \) -name "*.jar" -print)

for item in ${file_list};
do
  TMF_XP="${TMF_XP}${sep}${item}"
done

TMF_XP=${TMF_XP}${sep}$(find "${OBJDIR}/java" \( -type f -o -type l \) -name "oojava.jar" -print)
export TMF_XP

MEM="-server -Xms384m -Xmx1024m -XX:MaxPermSize=256m"

LOG4J="-Dlog4j.configuration=file:${CONF_DIR}/log4j.xml -Dlog4j.configuration.watchEnabled=yes -Dlog4j.configuration.watchDelay=120000 -Dlog4j.configuratorClass=com.marconi.fusion.base.logging.log4j.Configurator"

echo "BER Conversion in Progress. Please wait ….. "

if [ "${curr_os}" = "HP-UX" ];
then
"${JAVA_EXE}" -d64 ${MEM} ${LOG4J} -classpath "${TMF_XP}" "$RUN_FILE" "${CONF_DIR}" "${BER_DIR}" false true
else
 "${JAVA_EXE}" ${MEM} ${LOG4J} -classpath "${TMF_XP}" "$RUN_FILE" "${CONF_DIR}" "${BER_DIR}" false true
fi

res=$?

if [ ${res} -ne 0 ];
then
echo " BER Conversion could not be finished succussfully ..."
else
echo " BER Conversion has been completed succussfully"
fi

exit $?

}
	 
