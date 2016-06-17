#!/bin/sh
. _DEBUG.sh
Success="0"
Failure="1"
ExitVal="${Failure}"
appVersion=""
appMode=""
#MINOR_UPGRADE_VER=3.1
#MAJOR_UPGRADE_VER=3.5
#MAJOR_UPGRADE_VER_1=3.4
typeset -i menu_mode=0
FULLNAME=""
AGENT_DIR=""
#UPGRADE_AGENT_DIR=""
AGENT_USER_HOME=""
AGENT_USER_HOME_DEF=""
ENV_FILE=""
TMF814_APP_TYPE="SONM"
SG_READ="0"
POSTFIX_READ="0"
PLUGIN_INSTALLATION_TYPE="UNDEFINED"
postfix=""
curr_host=$(hostname | cut -d '.' -f 1)
home_inst=$(pwd)
chmod +w "${home_inst}" > /dev/null 2>&1
JRE=0

inst_step="0"
rem_step="0"

curr_os=$(uname -s)
ext_os=""
if [ "${curr_os}" = "HP-UX" ];
then
  MY_ZIP="/usr/contrib/bin/gzip"
  boot_path="/sbin"
  JAVA_DEF="/opt/java"
  com_size="ls -s"
  my_o="echo"
  HP_ARCH="pa11"
  if [ -x /usr/bin/hp-pa ];
  then
    $(/usr/bin/hp-pa)
    if [ $? -ne 0 ];
    then
      HP_ARCH="ia64"
      OS_SUBVER=$(uname -r | awk -F. '{print $3}')
      if [ "${OS_SUBVER}" -ge "31" ];
      then
        HP_ARCH="ia64_31"
      fi
    fi
  fi
  ext_os="${curr_os}.${HP_ARCH}"
elif [ "${curr_os}" = "Linux" ];
then
  my_o="echo -e"
  MY_ZIP="/bin/gzip"
  boot_path="/etc/rc.d"
  JAVA_DEF="/opt/java1.6/"
  com_size="ls -s --block-size=512"
  ext_os="${curr_os}"
  if [ -e "/etc/SuSE-release" ]
  then
    LIN_VER=$(cat "/etc/SuSE-release" | grep VERSION | cut -d "=" -f 2)
     if [ "${LIN_VER}" -eq "10" ];
    then
      ext_os="${curr_os}_10"
    elif [ "${LIN_VER}" -eq "11" ];
    then
      ext_os="${curr_os}_11"
    fi
  fi
else
  tmp_os=$(echo ${curr_os} |grep -i cygwin)
  if [ "${tmp_os}" != "" ];
  then
    curr_os="Cygwin"
    my_o="echo -e"
    MY_ZIP="/bin/gzip"
    boot_path="/etc/rc.d"
    #JAVA_DEF="C:/Program Files/Java/j2re1.4.2_04"
    JAVA_DEF="/machine/SOFTWARE/JavaSoft/Java Runtime Environment"
    AGENT_USER_HOME_DEF="C:/FUSION"
    com_size="ls -s --block-size=512"
    ext_os="${curr_os}"
  else
    ${my_o} "Unknown OS: ${curr_os}"
    ${my_o} "Exiting..."
    exit 1
  fi
fi


#################### Error exit
ErrExit()
{
  rval=$1
  rmsg=$2
  if [ ${rval} -ne 0 ];
  then
    ${my_o}  ${rmsg}
  	${my_o} " \n Upgrade procedure aborted ..... "
    ${my_o} ""
    exit ${rval}
  fi
}

######################


Cleaner()
{
  if [ "${ExitVal}" = "${Failure}" ];
  then
    ${my_o} ""
    ${my_o} "Abort installation ..."
    ${my_o} ""
    if [ "${inst_step}" = "1" ];
    then
      ${my_o} ""
      ${my_o} "Starting Cleanup of failed installation..."
      ${my_o} ""

      new_item="${FULLNAME}"

      if [ "${curr_os}" = "Cygwin" ];
      then
        AGENT_USER_HOME="${AGENT_USER_HOME_DEF}"
      else
        AGENT_USER_HOME=$(fgrep "${AGENT_USER}:" /etc/passwd | awk 'BEGIN { FS = ":"} {print $6}')
      fi

      if [ "${AGENT_USER_HOME}" != "" ];
      then
        if [ -r "${AGENT_USER_HOME}/${USER_ENV_DIR}/${new_item}.env" ];
        then
          . "${AGENT_USER_HOME}/${USER_ENV_DIR}/${new_item}.env"
        fi
      fi

      RemoveRebootFile  "${new_item}"
      RemoveAttilaFile  "${new_item}"
      RemovePrivateFile "${new_item}"
      RemoveCORBA       "${new_item}"
      RemoveConfFile    "${new_item}"
      RemoveSGFile      "${new_item}"
      RemoveApplication "${new_item}"
      RemoveTmpFile

      ${my_o} "Finished Cleanup"
      ${my_o} ""
    fi
    ${my_o} "${Exiting}"
    ${my_o} ""
  else
    ${my_o} ""
  fi

  return 0
}

trap Cleaner EXIT

if [ -r "./AgentEnv.mode" ];
then
  . "./AgentEnv.mode"
else
  ${my_o} "ERROR: missing or unreadable the environment file AgentEnv.mode"
  ${my_o} "Check your media distribution"
  ${my_o} "Exiting.."
  exit 1
fi

if [ ${AGENT_MODE} = "EM" ];
then
    if [ -e "./AgentEnv_EM.${ext_os}" ] && [ -r "./AgentEnv_EM.${ext_os}" ];
	then
	  mv AgentEnv_EM.${ext_os} AgentEnv.${ext_os}
	else
	  echo "Missing environment file ./AgentEnv_EM.${ext_os}"
	  echo "Exiting..."
	  exit 1
	fi
else
	if [ -e "./AgentEnv_NM.${ext_os}" ];
	then
	  mv AgentEnv_NM.${ext_os} AgentEnv.${ext_os}
	else
	  echo "Missing environment file ./AgentEnv_NM.${ext_os}"
	  echo "Exiting..."
	  exit 1
	fi
fi
# lettura variabili di ambiente ufficiali
if [ -r "./AgentEnv.${ext_os}" ];
then
  . "./AgentEnv.${ext_os}"
else
  ${my_o} "ERROR: missing or unreadable the environment file AgentEnv.${ext_os}"
  ${my_o} "Check your media distribution"
  ${my_o} "Exiting.."
  exit 1
fi

# lettura variabili di ambiente customizzate
if [ -r "./tmf.cfg" ];
then
  . "./tmf.cfg"
fi

Welcome()
{
  clear
  ${my_o} ""
  ${my_o} "####################################################################"
  ${my_o} ""
  ${my_o} "         ${VENDOR}"
  ${my_o} ""
  ${my_o} "            ${AGENT_TITLE} ${AGENT_TYPE}"
  ${my_o} ""
  ${my_o} "####################################################################"
  ${my_o} ""

  return 0
}

CheckRootUser()
{
  if [ "${curr_os}" = "Cygwin" ];
  then
      return 0
  fi

  if [[ -x /usr/bin/whoami ]]; then
    if [[ $(/usr/bin/whoami) != "root" ]]; then
      ${my_o} ""
      ${my_o} "Sorry, you must be root to run this script!"
      ${my_o} "${Exiting}"
      exit 0
    fi
  fi

  return 0
}

isNumeric()
{
   # This routine will return true (0) if the string contains all numeric
   # characters.

   expr "$1" + 1 > /dev/null 2>&1
   if [ $? -ge 2 ]; then
      return 1
   fi
   return 0
}

CheckOS()
{
  #OS_NAME=$(uname -s)
  if [ "${curr_os}" = "${SUPPORTED_OS}" ];
  then
    if [ "${curr_os}" != "HP-UX" ];
    then
      return 0
    fi
    OS_VER=$(uname -r | awk -F. '{print $2}')
    if [ ${OS_VER} -lt ${SUPPORTED_OSVER} ];
    then
      ${my_o} "OS_VER $OS_VER is not compatible with this product"
      ${my_o} "You need OS_VER ${SUPPORTED_OSVER}"
      ${my_o} "${Exiting}"
      exit 1
    fi
    OS_SUBVER=$(uname -r | awk -F. '{print $3}')
    if [ ${OS_SUBVER} -lt ${SUPPORTED_OSSUBVER} ];
    then
      ${my_o} "OS_MINOR_VERSION $OS_SUBVER is not compatible with this product"
      ${my_o} "You need OS_SUBVERSION ${SUPPORTED_OSSUBVER}"
      ${my_o} "${Exiting}"
      exit 1
    fi
  else
    ${my_o} ""
    ${my_o} "ERROR: unknown os ${OS_NAME}, you need ${SUPPORTED_OS}"
    ${my_o} "${Exiting}"
    exit 1
  fi

  return 0
}

CheckNMSDB()
{
  ${my_o} "Checking NMSDB installation...."

  #OBJDIR="/opt/object/hprisc"
  OBJDIR="${OBJ_DIR_DEF}"
  #if [ ! -d "${OBJDIR}" ];
  #then
  #  ${my_o} "..ERROR\n"
  #  ${my_o} "The directory ${OBJDIR} is missing"
  #  ${my_o} "Check NMSDB installation, please"
  #  ${my_o} "Remember that SO_BASE required version is ${NMSDB_VER} or higher"
  #  ${my_o} "${Exiting}"
  #  exit 1
  #fi

  #${my_o} "..OK\n"
  #${my_o} "Checking NMSDB environment....\c"

  ######## Inserimento directory di OBJECTIVITY
  ${my_o} "\nInsert name of the NMSDB Ver. ${NMSDB_VER}  directory:"
  ${my_o} "[default ${OBJ_DIR_DEF}]: \c"
  read OBJDIR
  if [ "${OBJDIR}" = "" ];
  then
    OBJDIR="${OBJ_DIR_DEF}"
  fi

  ######## Checks if Objectivity is already installed
  if [ -d "${OBJDIR}/bin" ];
  then
    object_path="${OBJDIR}/bin"
    SHLIB_PATH=${OBJDIR}/lib
    export SHLIB_PATH
    LD_LIBRARY_PATH=${OBJDIR}/lib
    export LD_LIBRARY_PATH
  else
    ${my_o} "..ERROR\n"
    ${my_o} " NMSDB environment not correctly setted!"
    ${my_o} " ${OBJDIR}/bin doesn't exist."
    ${my_o} "$Exiting"
    exit 1
  fi

  ######## Checks if Objectivity lib is already installed
  if [ "${curr_os}" = "HP-UX" ];
  then
    if [ "${HP_ARCH}" = "pa11" ];
    then
      if [ ! -e "${OBJDIR}/lib/liboo.${OBJ_VER}.sl" ];
      then
        ${my_o} "..ERROR\n"
        ${my_o} " NMSDB library not properly setted!"
        ${my_o} " ${OBJDIR}/lib/liboo.${OBJ_VER}.sl doesn't exist."
        ${my_o} "$Exiting"
        exit 1
      fi
    else
      if [ ! -e "${OBJDIR}/lib/liboo.${OBJ_VER}.so" ];
      then
        ${my_o} "..ERROR\n"
        ${my_o} " NMSDB library not properly setted!"
        ${my_o} " ${OBJDIR}/lib/liboo.${OBJ_VER}.so doesn't exist."
        ${my_o} "$Exiting"
        exit 1
      fi
    fi
  fi

  if [ "${curr_os}" = "Linux" ];
  then
    if [ ! -e "${OBJDIR}/lib/liboo.so.${OBJ_VER}" ];
    then
      ${my_o} "..ERROR\n"
      ${my_o} " NMSDB library not properly setted!"
      ${my_o} " ${OBJDIR}/lib/liboo.so.${OBJ_VER} doesn't exist."
      ${my_o} "$Exiting"
      exit 1
    fi
  fi

  if [ ! -e "${OBJDIR}/java/lib/oojava.jar" ];
  then
    ${my_o} "..ERROR\n"
    ${my_o} " NMSDB java environment not properly setted!"
    ${my_o} " ${OBJDIR}/java/lib/oojava.jar doesn't exist."
    ${my_o} "$Exiting"
    exit 1
  fi

	${my_o} "..OK\n"
	${my_o} "Checking if NMSDB server is running....\c"

  ######## Checks if Objectivity processes are running
  if [ -x "${object_path}/oolockserver" ];
  then
    ps -el | fgrep -v fgrep | fgrep ools > /dev/null
    if [ $? != 0 ];
    then
      ${my_o} "..WARNING\n"
      ${my_o} "\nNMSDB server is not running..."
      ${my_o} "Starting NMSDB server..."
        if [ -x /sbin/init.d/nmsdb ];
      then
        /sbin/init.d/nmsdb start > /dev/null 2>&1
      else
        if [ -x /etc/init.d/nmsdb ];
        then
          /etc/init.d/nmsdb start > /dev/null 2>&1
        else
          ${nmsdb_path}/oolockserver -notitle
	fi
      fi
    fi
    ${object_path}/oocheckls $curr_host -notitle | fgrep -v fgrep | fgrep -e "Server version: ${OBJ_VER}" -e "Server version: 10.1" -e "Server version: 9.4" -e "Server version: 9.3" -e "Server version: 9.2" -e "Server version: 8.0.7" -e "Server version: 7.1" -e "Server version: 5.2" > /dev/null
    if [ $? = 0 ];
    then
      ${object_path}/oocheckls $curr_host -notitle | fgrep -v fgrep | fgrep "The Lock Server is running" > /dev/null
      if [ $? != 0 ];
      then
        ${my_o} "..ERROR\n"
        ${my_o} "\nNMSDB Server is NOT running..."
        ${my_o} "$Exiting"
        exit 1
      fi
    else
      ${my_o} "..ERROR\n"
      ${my_o} "\nNMSDB Version is NOT correct, need Ver. ${NMSDB_VER} or higher"
      ${my_o} "$Exiting"
      exit 1
   fi
  else
    ${my_o} "..ERROR\n"
    ${my_o} "\nNMSDB server process ${object_path}/oolockserver isn't present"
    ${my_o} "$Exiting"
    exit 1
  fi

  ${my_o} "..OK\n"

  return 0
}

CheckJver()
{
  jver=$1

  #JAVA_VER_SUPP
  #MAJOR_JVER_SUPP=1
  #MINOR_JVER_SUPP=4
  #REVIS_JVER_SUPP=2
  #PATCH_JVER_SUPP=04

  jver1=$(${my_o} "${jver}" | cut -d '"' -f 2)
  major_jver=$(${my_o} "${jver1}" | cut -d '.' -f 1)

  #Fixed as part of TR HN42354
  if [ ${major_jver} -ne ${MAJOR_JVER_SUPP} ];
  then
    return 1
  fi

  minor_jver=$(${my_o} "${jver1}" | cut -d '.' -f 2)
  if [ ${minor_jver} -ne ${MINOR_JVER_SUPP} ];
  then
    return 1
  fi

  if [ "${curr_os}" = "HP-UX" ];
  then
    revis_jver=$(${my_o} "${jver1}" | cut -d '.' -f 3)
  else
    revis_jver=$(${my_o} "${jver1}" | cut -d '.' -f 3 | cut -d '_' -f 1)
  fi
  if [ ${revis_jver} -gt ${REVIS_JVER_SUPP} ];
  then
    return 0
  fi
  if [ ${revis_jver} -lt ${REVIS_JVER_SUPP} ];
  then
    return 1
  fi

  if [ "${curr_os}" = "HP-UX" ];
  then
    patch_jver=$(${my_o} "${jver1}" | cut -d '.' -f 4)
  else
    patch_jver=$(${my_o} "${jver1}" | cut -d '.' -f 3 | cut -d '_' -f 2)
  fi
  if [ ${patch_jver} -gt ${PATCH_JVER_SUPP} ];
  then
    return 0
  fi
  if [ ${patch_jver} -lt ${PATCH_JVER_SUPP} ];
  then
    return 1
  fi

  return 0
}

GetLatestJavaPath()
{
  TPATH_DUMP=
  TLIST_DUMP="Version\t\tType\tSupport\tPath"
  TLIST=

  if [ "${curr_os}" = "Cygwin" ];
  then
    #java_defi="/machine/SOFTWARE/JavaSoft/Java Runtime Environment"
    jre_list=$(regtool -k list "${JAVA_DEF}")
    for i in $jre_list;
    do
      j_home=$(regtool get "${JAVA_DEF}/$i/JavaHome")
      my_list="${j_home}\n${my_list}"
      if [ -x "$j_home/bin/java" ];
      then
        vers=$("$j_home/bin/java" -version 2>&1 | grep -i "java version" | awk '{print $NF}')
        CheckJver "${vers}"
        if [ $? -eq 0 ];
        then
          jsupp=Yes
          TPATH="${vers}\t1\t$j_home"
          TLIST="${TPATH}\n${TLIST}"
        else
          jsupp=No
        fi
        TPATH_DUMP="${vers}\tjre\t$jsupp\t$j_home"
        TLIST_DUMP="${TPATH_DUMP}\n${TLIST_DUMP}"
      elif [ -x "$j_home/jre/bin/java" ];
      then
        vers=$("$j_home/jre/bin/java" -version 2>&1 | grep -i "java version" | awk '{print $NF}')
        CheckJver "${vers}"
        if [ $? -eq 0 ];
        then
          jsupp=Yes
          TPATH="${vers}\t1\t$j_home"
          TLIST="${TPATH}\n${TLIST}"
        else
          jsupp=No
        fi
        TPATH_DUMP="${vers}\tjre\t$jsupp\t$j_home"
        TLIST_DUMP="${TPATH_DUMP}\n${TLIST_DUMP}"
      fi
    done
  else
    jlist=$((ls -1d "${JAVA_DEF}"*) 2>/dev/null)
    for i in $jlist;
    do
      if [ -x $i/jre/bin/java ];
      then
        vers=$($i/jre/bin/java -version 2>&1 | grep -i "java version" | awk '{print $NF}')
        CheckJver "${vers}"
        if [ $? -eq 0 ];
        then
          jsupp=Yes
          TPATH="${vers}\t1\t$i"
          TLIST="${TPATH}\n${TLIST}"
          TPATH_DUMP="${vers}\tjre\t$jsupp\t$i"
          TLIST_DUMP="${TPATH_DUMP}\n${TLIST_DUMP}"
        else
          jsupp=No
        fi

      fi
      if [ -x $i/bin/java ];
      then
        vers=$($i/bin/java -version 2>&1 | grep -i "java version" | awk '{print $NF}')
        CheckJver "${vers}"
        if [ $? -eq 0 ];
        then
          jsupp=Yes
          TPATH="${vers}\t2\t$i"
          TLIST="${TPATH}\n${TLIST}"
          TPATH_DUMP="${vers}\tjdk\t$jsupp\t$i"
          TLIST_DUMP="${TPATH_DUMP}\n${TLIST_DUMP}"
        else
          jsupp=No
        fi

      fi
    done
  fi

  if [ "${curr_os}" = "HP-UX" ];
  then
    #We prefer  to use j2re vs. jdk by reverse ordering the first field
    LATEST_JAVA_PATH=`echo "${TLIST}" | \
                      sort -k1,1r | \
                      head -1 | \
                      awk 'BEGIN { FS = "\t"} {print $3}'`
  else
    #We prefer  to use j2re vs. jdk by reverse ordering the first field
    LATEST_JAVA_PATH=`echo -e "${TLIST}" | \
                      sort -k1,1r | \
                      head -1 | \
                      awk 'BEGIN { FS = "\t"} {print $3}'`
  fi

  #${my_o} "Latest java path = \n${TLIST}"
  #${my_o} "Latest java = ${LATEST_JAVA_PATH}"
  if [ "${LATEST_JAVA_PATH}" != "" ];
  then
    ${my_o} ""
    ${my_o} "Founded following Java pkgs in default path:"
    ${my_o} "${TLIST_DUMP}" | sort -k1,1r
    ${my_o} ""
    if [ -d "${LATEST_JAVA_PATH}" ];
    then
      JAVA_DIR="${LATEST_JAVA_PATH}"
      if [ -x $i/jre/bin/java ];
      then
        JRE=1
      fi
    fi
  fi

  return 0
}

CheckJava()
{
  ${my_o} "Checking Java installation....\c"

  GetLatestJavaPath

  ######## Inserimento directory di Java
  ${my_o} ""
  ${my_o} "Insert directory of Java installation"
  ${my_o} "[default ${JAVA_DIR}]: \c"
  read tmp_java_dir
  if [ "${tmp_java_dir}" != "" ];
  then
    JAVA_DIR="${tmp_java_dir}"
  fi

  JRE=0
  if [ -x "${JAVA_DIR}/jre/bin/java" ];
  then
    JRE=1
  fi

  if [ ! -d "${JAVA_DIR}" ];
  then
    ${my_o} "..ERROR\n"
    ${my_o} " Java environment not correctly setted!"
    ${my_o} " ${JAVA_DIR} doesn't exist."
    ${my_o} "$Exiting"
    exit 1
  fi

  if [ "${JRE}" = "0" ];
  then
    if [ ! -d "${JAVA_DIR}/bin" ];
    then
      ${my_o} "..ERROR\n"
      ${my_o} " Java environment not correctly setted!"
      ${my_o} " ${JAVA_DIR}/bin doesn't exist."
      ${my_o} "$Exiting"
      exit 1
    fi

    if [ ! -x "${JAVA_DIR}/bin/java" ];
    then
      ${my_o} "..ERROR\n"
      ${my_o} " Java environment not correctly setted!"
      ${my_o} " ${JAVA_DIR}/bin/java doesn't exist."
      ${my_o} "$Exiting"
      exit 1
    fi

    vers=$("${JAVA_DIR}/bin/java" -version 2>&1 | grep -i "java version" | awk '{print $NF}')
    CheckJver "${vers}"
    if [ $? -eq 1 ];
    then
      ${my_o} "..ERROR\n"
      ${my_o} " Java environment not correctly setted!"
      ${my_o} " Java version selected is not supported"
      ${my_o} " Version is $vers, Supported starting from $JAVA_VER_SUPP"
      ${my_o} "$Exiting"
      exit 1
    fi
  else
    if [ ! -d "${JAVA_DIR}/jre" ];
    then
      ${my_o} "..ERROR\n"
      ${my_o} " Java environment not correctly setted!"
      ${my_o} " ${JAVA_DIR}/jre doesn't exist."
      ${my_o} "$Exiting"
      exit 1
    fi

    if [ ! -d "${JAVA_DIR}/jre/bin" ];
    then
      ${my_o} "..ERROR\n"
      ${my_o} " Java environment not correctly setted!"
      ${my_o} " ${JAVA_DIR}/jre/bin doesn't exist."
      ${my_o} "$Exiting"
      exit 1
    fi

    if [ ! -x "${JAVA_DIR}/jre/bin/java" ];
    then
      ${my_o} "..ERROR\n"
      ${my_o} " Java environment not correctly setted!"
      ${my_o} " ${JAVA_DIR}/jre/bin/java doesn't exist."
      ${my_o} "$Exiting"
      exit 1
    fi

    vers=$("${JAVA_DIR}/jre/bin/java" -version 2>&1 | grep -i "java version" | awk '{print $NF}')
    CheckJver "${vers}"
    if [ $? -eq 1 ];
    then
      ${my_o} "..ERROR\n"
      ${my_o} " Java environment not correctly setted!"
      ${my_o} " Java version selected is not supported"
      ${my_o} " Version is $vers, Supported starting from $JAVA_VER_SUPP"
      ${my_o} "$Exiting"
      exit 1
    fi
  fi

  ${my_o} "..OK\n"

  PATH="${JAVA_DIR}"/bin:"${JAVA_DIR}"/jre/bin:${PATH}
  export PATH
  CLASSPATH=.:${CLASSPATH}
  export CLASSPATH

  return 0
}

CheckSG()
{
  if [ "${SG_READ}" != "0" ];
  then
    return 0
  fi

  SG_READ="1"
  SG="0"
  export SG

  if [ "${curr_os}" = "HP-UX" ];
  then
    ${my_o} "\nDo you want install with Service Guard? (y/n, default No) \c"
    read tmp_SG
    ${my_o} ""
  else
    tmp_SG="n"
  fi
  if [ "${tmp_SG}" = "y" ];
  then
    SG="1"
    export SG

    ${my_o} "Remember that before install this application you must have"
    ${my_o} "executed the Service Guard command:"
    ${my_o} "    ./manual.sh preinst"
    ${my_o} "located in the directory /etc/cmcluster/pkg_tmfxxxx"
    ${my_o} "Remember also that the floating ip-address"
    ${my_o} "should be already set."
    ${my_o} ""

    answer=""
    while [ "${answer}" != "y" -a "${answer}" != "n" ];
    do
      ${my_o} "Do you want continue installation? [y/n] [default: y]: \c"
      read answer
      if [ "${answer}" = "" ];
      then
        answer="y"
      fi
    done
    if [ "${answer}" = "n" ];
    then
      ${my_o} "${Exiting}"
      exit 0
    fi

    ${my_o} ""

    if [ ! -d ${SGDIR} ];
    then
      ${my_o} " Service Guard Environment not correctly configured!"
      ${my_o} " directory $SGDIR doesn't exists."
      ${my_o} " ${Exiting}"
      exit 1
    fi
    set -A pkgs $(ls -d ${SGDIR}/${SGPACK_PREFIX}* 2> /dev/null )
    typeset -i pkg_end=${#pkgs[*]}
    if [ ${pkg_end} = 0 ];
    then
      ${my_o} " There are no packages with prefix ${SGPACK_PREFIX} in ${SGDIR}!"
      ${my_o} " ${Exiting}"
      exit 1
    fi
    if [ ${pkg_end} = 1 ];
    then
      pack=${pkgs}
    else
      ${my_o} "Choose the Service Guard package for this installation:\n"
      typeset -i pkg_count=0
      typeset -i idx=1
      while [ ${pkg_count} -lt ${pkg_end} ];
      do
        ${my_o} "  ${idx}. ${pkgs[${pkg_count}]}\n"
        let "pkg_count=${pkg_count} + 1"
        let "idx=${idx} + 1"
      done
      typeset -i sel_pack=0
      while [ "${sel_pack}" -lt 1 -o "${sel_pack}" -gt ${pkg_end} ]
      do
        ${my_o} "Make your selection, please [1..${pkg_end}][Default = 1]: \c"
        read l_var
        if [ "${l_var}" = "" ];
        then
          l_var=1
        fi
        if isNumeric "${l_var}"
        then
          sel_pack=${l_var}
        fi
      done
      let "sel_pack=${sel_pack} - 1"
      pack=${pkgs[${sel_pack}]}
    fi
    ${my_o} ""
    ${my_o} "You have selected the package ${pack}\n"
    if [ ! -e ${pack}/VAR.d ];
    then
      ${my_o} "The environment file ${pack}/VAR.d isn't present"
      ${my_o} "${Exiting}"
      exit 1
    fi
    if [ ! -e ${pack}/rcontrol.sh ];
    then
      ${my_o} "The environment file ${pack}/rcontrol.sh isn't present"
      ${my_o} "${Exiting}"
      exit 1
    fi
    WORKFS=$(grep "FS\[0\]"  "${pack}"/rcontrol.sh | grep -v grep | grep -v "^#" | cut -d ";" -f 2 | cut -d = -f 2 | cut -d '"' -f 2)
    if [ "${WORKFS}" = "" ];
    then
      ${my_o} "The field WORKFS is missing in the environment file ${pack}/rcontrol.sh"
      ${my_o} "${Exiting}"
      exit 1
    fi
    df | grep ${WORKFS} > /dev/null
    if [ $? != 0 ];
    then
      ${my_o} "No file system found for the ${pack} package(s)"
      ${my_o} "You must be on working node or you must mount"
      ${my_o} "the file system ${WORKFS}"
      ${my_o} "${Exiting}"
      exit 1
    fi

    SGHOST=$(grep "^WHOST" "${pack}"/VAR.d | grep -v grep | cut -d = -f 2 | cut -d '"' -f 2)
    if [ "${SGHOST}" = "" ];
    then
      ${my_o} "The field WHOST is missing in the environment file ${pack}/VAR.d"
      ${my_o} "${Exiting}"
      exit 1
    fi

    #if [ ! -d ${pack}/nms.d ];
    #then
    #  ${my_o} "The environment directory ${pack}/nms.d isn't present"
    #  ${my_o} "${Exiting}"
    #  exit 1
    #fi
    #if [ ! -e ${pack}/nms.d/NFS.d ];
    #then
    #  ${my_o} "The environment file ${pack}/nms.d/NFS.d isn't present"
    #  ${my_o} "${Exiting}"
    #  exit 1
    #fi

    #if [ "${AGENT_MODE}" = "NM" ];
    #then
    #  TMFA_MOUNT_POINT=$(grep "SNFS\[0\]"  "${pack}"/nms.d/NFS.d | grep -v grep | grep -v "^#" | cut -d ";" -f 2 | cut -d = -f 2 | cut -d '"' -f 2)
    #  if [ "${TMFA_MOUNT_POINT}" = "" ];
    #  then
    #    ${my_o} "The field SNFS[0] is missing in the environment file ${pack}/nms.d/NFS.d"
    #    ${my_o} "${Exiting}"
    #    exit 1
    #  fi
    #  if [ ! -d "${TMFA_MOUNT_POINT}" ];
    #  then
    #    ${my_o} "No mount directory point found for the ${pack} package(s)"
    #    ${my_o} "please check your configuration and create it"
    #    ${my_o} "with the specified value ${TMFA_MOUNT_POINT}"
    #    ${my_o} "${Exiting}"
    #    exit 1
    #  fi
    #else
    ## this is a generic default, but in this case it is unused
    TMFA_MOUNT_POINT="/opt/mv38"
    #fi

    SGPACK=${pack}
    SGFS=${WORKFS}
    SGTMFA_MOUNT_POINT=${TMFA_MOUNT_POINT}
    export SGPACK
    export SGFS
    export SGTMFA_MOUNT_POINT

    ${my_o} "Installing ${AGENT_TITLE} ${AGENT_TYPE} on Service Guard:"
    ${my_o} " package:     ${SGPACK}"
    ${my_o} " filesystem:  ${SGFS}\n"
    answer=""
    while [ "${answer}" != "y" -a "${answer}" != "n" ];
    do
      ${my_o} "Do you want continue? [y/n][default: y]: \c"
      read answer
      if [ "${answer}" = "" ];
      then
        answer="y"
      fi
    done
    if [ "${answer}" = "n" ];
    then
      ${my_o} "${Exiting}"
      exit 0
    fi
    postfix=$(basename ${SGPACK} | cut -d _ -f 2)
  fi

  return 0
}

CheckPostfix()
{
if [ "${POSTFIX_READ}" != "0" ];
  then
  return 0
fi

POSTFIX_READ="1"
len_ctrl="1"
while [ "${len_ctrl}" = "1" ];
do
  if [ "${SG}" = "0" ];
  then
    postfix="${curr_host}"

    VERSION=$(cat ./.version | tr '[.]' '[_]')

    tmp_version=$(echo ${VERSION} | cut -d _ -f1-2)
    ############ CR377 code
    appVersion=${tmp_version}
	appMode=${AGENT_MODE}
	#########
	MODE_STRING="IPTNMS"
	if [ "${AGENT_MODE}" = "EM" ];
	then
	   MODE_STRING="EM"
	fi

    tmp_fullname="${PREFIX}v${tmp_version}_${MODE_STRING}_${postfix}"
    FULLNAME=$(echo ${tmp_fullname} | tr '[.]' '[_]')

    #tmp_fullname="${PREFIX}v${VERSION}_${postfix}"
    #FULLNAME=$(echo ${tmp_fullname} | tr '[.]' '[_]')

    #FULLNAME="${PREFIX}v${VERSION}_${postfix}"

    ${my_o} ""
    ${my_o} "The installation name will be ${FULLNAME}"
    ${my_o} "Do you want modify? [y/n][default:n]: \c"
    read answer
    if [ "${answer}" = "" ]
    then
      answer="n"
    fi
    if [ "${answer}" = "y" ]
    then
      custom_ctrl=""
      while [ "${custom_ctrl}" = "" ];
      do
        ${my_o} ""
        ${my_o} "Please, insert new name: \c"
        read custom_name
        if [ "${custom_name}" = "" ];
        then
          ${my_o} ""
          ${my_o} "You have inserted a not valid name, please retry"
          ${my_o} ""
        else
          answer1=""
          while [ "${answer1}" != "y" -a "${answer1}" != "n" ];
          do
            ${my_o} ""
            ${my_o} "The installation name will be ${custom_name}"
            ${my_o} "Are you sure? [y/n][default: y]: \c"
            read answer1
            if [ "${answer1}" = "" ];
            then
              answer1="y"
            fi
          done
          if [ "${answer1}" = "y" ];
          then
            custom_ctrl="y"
            tmp_fullname="${custom_name}"
            FULLNAME=$(echo ${tmp_fullname} | tr '[.]' '[_]')
            #FULLNAME="${custom_name}"
          fi
        fi
      done
    fi
  else
    VERSION=$(cat ./.version | tr '[.]' '[_]')

    tmp_version=$(echo ${VERSION} | cut -d _ -f1-2)
	############ CR377 code
    appVersion=${tmp_version}
	appMode=${AGENT_MODE}
	#########
    tmp_fullname="${PREFIX}v${tmp_version}_${AGENT_MODE}_${postfix}"
    FULLNAME=$(echo ${tmp_fullname} | tr '[.]' '[_]')
    #FULLNAME="${PREFIX}v${VERSION}_${postfix}"

    ${my_o} "In Service Guard, you can customize only a part of the Installation name"
    ${my_o} "The Installation name must be like this: ${PREFIX}xxxxx_${postfix}"
    ${my_o} "The part of name indicated with xxxxx is customizable"
    ${my_o} "xxxxx could have any length, equal or greater than one character"

    ${my_o} ""
    ${my_o} "The installation name will be ${FULLNAME}"
    ${my_o} "Do you want modify the part named v${tmp_version}? [y/n][default:n]: \c"
    read answer
    if [ "${answer}" = "" ]
    then
      answer="n"
    fi
    if [ "${answer}" = "y" ]
    then
      custom_ctrl=""
      while [ "${custom_ctrl}" = "" ];
      do
        ${my_o} ""
        ${my_o} "Please, insert new customized part of name: \c"
        read custom_name
        if [ "${custom_name}" = "" ];
        then
          ${my_o} ""
          ${my_o} "You have inserted a not valid name, please retry"
          ${my_o} ""
        else
          answer1=""
          while [ "${answer1}" != "y" -a "${answer1}" != "n" ];
          do
            ${my_o} ""
            ${my_o} "The installation name will be ${PREFIX}${custom_name}_${postfix}"
            ${my_o} "Are you sure? [y/n][default: y]: \c"
            read answer1
            if [ "${answer1}" = "" ];
            then
              answer1="y"
            fi
          done
          if [ "${answer1}" = "y" ];
          then
            custom_ctrl="y"
            tmp_fullname="${PREFIX}${custom_name}_${postfix}"
            FULLNAME=$(echo ${tmp_fullname} | tr '[.]' '[_]')
            #FULLNAME="${PREFIX}${custom_name}_${postfix}"
          fi
        fi
      done
    fi
  fi
  len_count=$(echo "${FULLNAME}" | awk '{print length($1)}')
  if [ ${len_count} -gt 24 -o ${len_count} -lt 2 ];
  then
    ${my_o} ""
    ${my_o} "You have inserted a not valid name, please retry"
    ${my_o} "The name length must be in range between 2 and 24 characters"
    ${my_o} "The lenght of selected name ${FULLNAME} is ${len_count}"
    ${my_o} ""
   else
     len_ctrl="0"
   fi
done

  ${my_o} "\nStarting installation for ${FULLNAME}\n"

  return 0
}

CheckAlreadyInstalled()
{
  if [ "${curr_os}" != "Cygwin" ];
  then
    id -u ${AGENT_USER} > /dev/null 2>&1
    if [ $? = 0 ];
    then
      AGENT_USER_HOME=$(fgrep "${AGENT_USER}:" /etc/passwd | awk 'BEGIN { FS = ":"} {print $6}')
    fi
  else
    if [ "${AGENT_USER_HOME}" = "" ];
    then
      AGENT_USER_HOME="${AGENT_USER_HOME_DEF}"
    fi
  fi

  if [ "${AGENT_USER_HOME}" != "" ];
  then
    if [ -e "${AGENT_USER_HOME}/${USER_ENV_DIR}/${FULLNAME}.env" ];
    then
      ${my_o} "Installation ${FULLNAME} already exist"
      ${my_o} "If you want use this installation name it is necessary"
      ${my_o} "remove the previous and then reinstall this!"
      ${my_o} "${Exiting}"
      exit 0
    fi
  fi

  return 0
}

CreateAgentUser()
{
  ${my_o} "Check file system..."
  PS3="Choose the file system for the user ${AGENT_USER}: "
  if [ "${curr_os}" = "Linux" ];
  then
    fs_list=$(df -l -P -x tmpfs -x nfs -x cdfs | grep -vi "filesystem" | awk '{ if(NF > 1) print $NF}')
  else
    fs_list=$(df -l -P | grep -vi "filesystem" | awk '{ if(NF > 1) print $NF}')
  fi

  typeset -i fs_tot=0
  for i in ${fs_list};
  do
    let "fs_tot=${fs_tot} + 1"
  done
  if [ ${fs_tot} = 0 ];
  then
    ${my_o} " There are no file system!, system panic"
    ${my_o} " ${Contact}"
    ${my_o} " ${Exiting}"
    exit 1
  fi

  if [ ${fs_tot} = 1 ];
  then
    fs_sel=${fs_list}
  else
    select fs_sel in ${fs_list};
    do
      if [ "${fs_sel}" != "" ];
      then
        break
      else
        ${my_o} "Your choice is not valid"
      fi
    done
  fi

  ${my_o} "You have selected file system ${fs_sel}"
  if [ "${fs_sel}" = "/" ];
  then
    agentuser_home="/${AGENT_USER}"
  else
    agentuser_home="${fs_sel}/${AGENT_USER}"
  fi

  export agentuser_home

  agentuser_prehome=$(dirname "${agentuser_home}")
  if [ ! -d "${agentuser_prehome}" ];
  then
    mkdir -p "${agentuser_prehome}"
    chgrp ${AGENT_GROUP} "${agentuser_prehome}"
    chmod 777 "${agentuser_prehome}"
  fi

  user_uid=$(cat /etc/passwd | awk 'BEGIN { FS = ":"} {print $3}' | grep ":${AGENT_UID}}:")
  new_uid="-u ${AGENT_UID}"
  if [ "${user_uid}" != "" ];
  then
    new_uid=""
  fi

  /usr/sbin/useradd ${new_uid} -g ${AGENT_GROUP} -d ${agentuser_home} -s /bin/sh -c NBI_Manager! -m ${AGENT_USER}
  if [ $? != 0 ];
  then
    ${my_o} "ERROR creating ${AGENT_USER} user"
    ${my_o} "${Contact}"
    ${my_o} "${Exiting}"
    exit 1
  fi

  ${my_o} "Insert the password for ${AGENT_USER} user:"
  passwd ${AGENT_USER}

  return 0
}

CheckUserSG()
{
  if [ "${SG}" = "0" ];
  then
    return 0
  fi

  ${my_o} ""
  ${my_o} "Checking user ${AGENT_USER} on Service Guard hosts..."

  SG_POSTFIX=$(basename ${SGPACK})
  nodenames=$(grep NODE_NAME ${SGPACK}/${SG_POSTFIX}.ascii | grep -v "^#" |  awk '{print $2}')

  for item in ${nodenames};
  do
    if [ "${item}" = "${curr_host}" ];
    then
      continue
    fi
    user_group=$(remsh ${item} -n fgrep "${AGENT_GROUP}:" /etc/group | awk 'BEGIN { FS = ":"} {print $1}')
    if [ "${user_group}" = "" ];
    then
      ${my_o} "ERROR: group ${AGENT_GROUP} is not present on host ${item}"
      ${my_o} "       Remember that this group must have gid ${AGENT_GID}"
      ${my_o} "You need to create it before install this application"
      ${my_o} "${Contact}"
      ${my_o} "${Exiting}"
      exit 1
    fi

    user_gid=$(remsh ${item} -n fgrep ":${AGENT_GID}:" /etc/group | awk 'BEGIN { FS = ":"} {print $1}')
    if [ "${user_gid}" != "" ];
    then
      if [ "${user_gid}" != "${AGENT_GROUP}" ];
      then
        ${my_o} "ERROR: group ${AGENT_GROUP} on host ${item} has wrong gid"
        ${my_o} "       the right gid must be ${AGENT_GID}"
        ${my_o} "You need to correct it before install this application"
        ${my_o} "${Contact}"
        ${my_o} "${Exiting}"
        exit 1
      fi
    else
      ${my_o} "ERROR: group ${AGENT_GROUP} on host ${item} has wrong gid"
      ${my_o} "       the right gid must be ${AGENT_GID}"
      ${my_o} "You need to correct it before install this application"
      ${my_o} "${Contact}"
      ${my_o} "${Exiting}"
      exit 1
    fi

    user_uid=$(remsh ${item} -n cat /etc/passwd | awk 'BEGIN { FS = ":"} {print ":"$1":"$3}' | grep -v grep | grep ":${AGENT_USER}:" | awk 'BEGIN { FS = ":"} {print $3}')
    if [ "${user_uid}" = "" ];
    then
      ${my_o} "ERROR: user ${AGENT_USER} is not present on host ${item}"
      ${my_o} "You need to create it before install this application"
        print "This user must have the following parameters on host ${item}:"
        print "     user:  AGENT_USER"
        print "      uid:  AGENT_UID"
        print "    group:  AGENT_GROUP"
        print "      gid:  AGENT_GID"
        print " home dir:  ${AGENT_USER_HOME}"
        print "    shell:  sh"
      ${my_o} "${Contact}"
      ${my_o} "${Exiting}"
      exit 1
    fi
    if [ "${user_uid}" != "${AGENT_UID}" ];
    then
      ${my_o} "ERROR: user ${AGENT_USER} on host ${item} has wrong uid"
      ${my_o} "       the uid is ${user_uid} instead of ${AGENT_UID}"
      ${my_o} "You need to correct it before install this application"
      ${my_o} "${Contact}"
      ${my_o} "${Exiting}"
      exit 1
    fi

    user_home=$(remsh ${item} -n fgrep "${AGENT_USER}:" /etc/passwd | awk 'BEGIN { FS = ":"} {print $6}')
    if [ "${user_home}" != "${AGENT_USER_HOME}" ];
    then
      ${my_o} "ERROR: user ${AGENT_USER} on host ${item} has wrong home directory"
      ${my_o} "       the right home directory is ${AGENT_USER_HOME}"
      ${my_o} "You need to correct it before install this application"
      ${my_o} "${Contact}"
      ${my_o} "${Exiting}"
      exit 1
    fi

  done

  ${my_o} "Successfully done"
  ${my_o} ""

  return 0
}

CheckUser()
{
  if [ "${curr_os}" = "Cygwin" ];
  then
    AGENT_USER_HOME="${AGENT_USER_HOME_DEF}"
    if [ ! -d "${AGENT_USER_HOME}/${USER_ENV_DIR}" ];
    then
      mkdir -p "${AGENT_USER_HOME}/${USER_ENV_DIR}"
    fi

    chmod 755 "${AGENT_USER_HOME}/${USER_ENV_DIR}"
    chown ${AGENT_USER}:${AGENT_GROUP} "${AGENT_USER_HOME}/${USER_ENV_DIR}" > /dev/null 2>&1

    return 0
  fi

  user_group=$(fgrep "${AGENT_GROUP}:" /etc/group | awk 'BEGIN { FS = ":"} {print $1}')
  user_gid=$(fgrep ":${AGENT_GID}:" /etc/group | awk 'BEGIN { FS = ":"} {print $1}')
  new_gid="-g ${AGENT_GID}"
  if [ "${user_gid}" != "" ];
  then
    if [ "${user_gid}" != "${AGENT_GROUP}" ];
    then
      new_gid=""
    fi
  fi

  if [ "${user_group}" = "" ];
  then
    /usr/sbin/groupadd ${new_gid} ${AGENT_GROUP}
    if [ $? != 0 ];
    then
      ${my_o} "ERROR adding ${AGENT_GROUP} group"
      ${my_o} "${Contact}"
      ${my_o} "${Exiting}"
      exit 1
    fi
  else
    if [ "${new_gid}" != "" ];
    then
      mod_gid=$(fgrep "${AGENT_GROUP}:" /etc/group | awk 'BEGIN { FS = ":"} {print $3}')
      if [ "${mod_gid}" != "${AGENT_GID}" ];
      then
        /usr/sbin/groupmod -g ${AGENT_GID} ${AGENT_GROUP}
	if [ $? != 0 ];
        then
          ${my_o} "WARNING: failure in modify id ${AGENT_GID} of ${AGENT_GROUP} group"
          ${my_o} "Please check manually this id after installation"
          ${my_o} ""
        fi
      fi
    fi
  fi

  user_uid=$(cat /etc/passwd | awk 'BEGIN { FS = ":"} {print ":"$1":"$3}' | grep -v grep | grep ":${AGENT_USER}:" | awk 'BEGIN { FS = ":"} {print $3}')
  if [ "${user_uid}" = "" ];
  then
    CreateAgentUser
  else
    if [ "${user_uid}" != "${AGENT_UID}" ];
    then
      user_mod=$(cat /etc/passwd | awk 'BEGIN { FS = ":"} {print ":"$1":"$3}' | grep -v grep | grep ":${AGENT_UID}:" | awk 'BEGIN { FS = ":"} {print $2}')
      if [ "${user_mod}" = "" ];
      then
        /usr/sbin/usermod -u ${AGENT_UID} ${AGENT_USER}
	if [ $? != 0 ];
        then
          ${my_o} "WARNING: failure in modify id ${AGENT_UID} of ${AGENT_USER} user"
          ${my_o} "Please check manually this id after installation"
          ${my_o} ""
        fi
      fi
    fi
    user_group=$(fgrep "${AGENT_GROUP}:" /etc/group | awk 'BEGIN { FS = ":"} {print $3}')
    user_gid=$(cat /etc/passwd | awk 'BEGIN { FS = ":"} {print ":"$1":"$4}' | grep -v grep | grep ":${AGENT_USER}:" | awk 'BEGIN { FS = ":"} {print $3}')
    if [ ! "${user_group}" = "${user_gid}" ];
    then
      /usr/sbin/usermod -g ${AGENT_GROUP} ${AGENT_USER}
      if [ $? != 0 ];
      then
        ${my_o} "WARNING: failure in modify group ${AGENT_GROUP} of ${AGENT_USER} user"
        ${my_o} "Please check manually this id after installation"
        ${my_o} ""
      fi
    fi
  fi

  AGENT_USER_HOME=$(fgrep "${AGENT_USER}:" /etc/passwd | awk 'BEGIN { FS = ":"} {print $6}')
  if [ "${AGENT_USER_HOME}" = "" ];
  then
    ${my_o} "ERROR: user ${AGENT_USER} has wrong home directory"
    ${my_o} "You need to correct it before install this application"
    ${my_o} "${Contact}"
    ${my_o} "${Exiting}"
    exit 1
  fi

  chown ${AGENT_USER}:${AGENT_GROUP} "${AGENT_USER_HOME}"    > /dev/null 2>&1
  chown ${AGENT_USER}:${AGENT_GROUP} "${AGENT_USER_HOME}/."* > /dev/null 2>&1

  if [ ! -d "${AGENT_USER_HOME}/${USER_ENV_DIR}" ];
  then
    mkdir "${AGENT_USER_HOME}/${USER_ENV_DIR}"
    chmod 755 "${AGENT_USER_HOME}/${USER_ENV_DIR}"
    chown ${AGENT_USER}:${AGENT_GROUP} "${AGENT_USER_HOME}/${USER_ENV_DIR}" > /dev/null 2>&1
  else
    chown ${AGENT_USER}:${AGENT_GROUP} "${AGENT_USER_HOME}/${USER_ENV_DIR}" > /dev/null 2>&1
  fi

  if [ ! -d "${AGENT_USER_HOME}/${USER_CONF_DIR}" ];
  then
    mkdir "${AGENT_USER_HOME}/${USER_CONF_DIR}"
    chmod 755 "${AGENT_USER_HOME}/${USER_CONF_DIR}"
    chown ${AGENT_USER}:${AGENT_GROUP} "${AGENT_USER_HOME}/${USER_CONF_DIR}" > /dev/null 2>&1
  else
    chown -R ${AGENT_USER}:${AGENT_GROUP} "${AGENT_USER_HOME}/${USER_CONF_DIR}" > /dev/null 2>&1
  fi

  port_file="${AGENT_USER_HOME}/${USER_CONF_DIR}/${USER_CONF_PORT_FILE}"
  if [ ! -e "${port_file}" ];
  then
    ${my_o} "43210 0 free" > "${port_file}"
    chmod 644 "${port_file}"
  fi

  chown ${AGENT_USER}:${AGENT_GROUP} "${port_file}" > /dev/null 2>&1

  CheckUserSG

  return 0
}

CheckOtherUser()
{
  if [ "${curr_os}" = "Cygwin" ];
  then
    return 0
  fi

  TMP_LINE=$(set | grep "^EXTRA_USER" | awk 'BEGIN { FS = "="} {print $1}')
  if [ "${#TMP_LINE[*]}" = "0" ];
  then
    return 0
  fi

  for LINE in ${TMP_LINE};
  do
    BUF=$(set | grep "^${LINE}" | cut -d "=" -f 2- )
    BUF1_user=$(echo "${BUF}" |cut -d " " -f 2 )
    BUF2_group=$(echo "${BUF}" |cut -d " " -f 3 )
    BUF3_shell=$(echo "${BUF}" |cut -d " " -f 4 )
    BUF4_gid=$(echo "${BUF}" |cut -d " " -f 5 )

    user_group=$(fgrep "${BUF2_group}" /etc/group | awk '{print $1}')
    if [ "${user_group}" = "" ];
    then
      ${my_o} "Adding group ${BUF2_group}...\c"
      /usr/sbin/groupadd -g ${BUF4_gid} ${BUF2_group}
      if [ $? != 0 ];
      then
        ${my_o} "..ERROR"
        ${my_o} "${Contact}"
        ${my_o} "${Exiting}"
        exit 1
      fi
      ${my_o} "..OK"
    fi

    user_home=$(fgrep "${BUF1_user}:" /etc/passwd | awk 'BEGIN { FS = ":"} {print $6}')
    if [ "${user_home}" = "" ];
    then
      home_tmp=$(fgrep "${AGENT_USER}" /etc/passwd | awk 'BEGIN { FS = ":"} {print $6}')
      home_def=$(dirname ${home_tmp})/${BUF1_user}
      ${my_o} "Choose the home directory for the user ${BUF1_user}: [${home_def}] "
      read home_to_add
      if [ "${home_to_add}" = "" ];
      then
        home_to_add=${home_def}
      fi

      ${my_o} ""
      ${my_o} "Adding user ${BUF1_user}...\c"
      /usr/sbin/useradd -g ${BUF2_group} -d ${home_to_add} -s ${BUF3_shell} -c Extra_User_for_TMF_NBI! -m ${BUF1_user}
      if [ $? != 0 ];
      then
        ${my_o} "..ERROR"
        ${my_o} "${Contact}"
        ${my_o} "${Exiting}"
        exit 1
      fi
      ${my_o} "..OK\n"

      ${my_o} "Insert the password for ${BUF1_user} user"
      passwd ${BUF1_user}

    fi

  done

  return 0
}

CheckExtraGroup4User()
{
  #for AsyncManager.java version

  if [ "${curr_os}" = "Cygwin" ];
  then
    return 0
  fi

  if [ "${AGENT_MODE}" != "NM" ];
  then
    return 0
  fi

  # check if Extra Group nmc is already added to fusion user
  user_extra_group="nmc"
  user_extra_gid="7654"

  user_Egroup=$(fgrep "${user_extra_group}:" /etc/group | awk 'BEGIN { FS = ":"} {print $1}')
  if [ "${user_Egroup}" = "" ];
  then
    /usr/sbin/groupadd -g ${user_extra_gid} ${user_extra_group}
    if [ $? != 0 ];
    then
      ${my_o} "ERROR adding ${user_extra_group} group"
      ${my_o} "${Contact}"
      ${my_o} "${Exiting}"
      exit 1
    fi
  fi

  ueg=$(grep "^${user_extra_group}:" /etc/group | grep -v grep | grep "${AGENT_USER}")
  if [ "${ueg}" = "" ];
  then
    /usr/sbin/usermod -G nmc ${AGENT_USER} > /dev/null 2>&1
    if [ $? != 0 ];
    then
      ${my_o} ""
      ${my_o} "Cannot add extra group nmc to ${AGENT_USER} user"
      ${my_o} "Please logout any ${AGENT_USER} session on this server"
      ${my_o} "or stop any application belonging to this user"
      ${my_o} "and then restart this installation procedure."
      ${my_o} ""
      ${my_o} "${Exiting}"
      exit 1
    fi
  fi

  return 0
}

CheckTarInst()
{
  ${my_o} ""
  if [ -e "${AGENT_TAR}.gz" ];
  then
    UNCOMP_SIZE=$(${MY_ZIP} -l ${AGENT_TAR}.gz | grep ${AGENT_TAR} | awk '{print $2}')
    tmp_dir=$(pwd)
    #FREE_SIZE=$(df -lk ${tmp_dir} | grep free | awk '{print $1}')
    FREE_SIZE=$(df -l -k -P ${tmp_dir} | grep -vi "filesystem" | awk '{if(NF > 5) print $4; else print $3}')
    let "UNCOMP_SIZE=${UNCOMP_SIZE}/1024"
    if [ ${FREE_SIZE} -lt ${UNCOMP_SIZE} ];
    then
      ${my_o} "You have not enough space to unzip the file ${AGENT_TAR}"
      ${my_o} "You need ${UNCOMP_SIZE} Kb of free space"
      ${my_o} "but you have only ${FREE_SIZE} Kb of free space in this repository"
      ${my_o} "${Contact}"
      ${my_o} "${Exiting}"
      exit 1
    fi
    ${my_o} "Uncompressing ${AGENT_TAR}, wait please...\c"
    ${MY_ZIP} -d ${AGENT_TAR}.gz > /dev/null 2>&1
    if [ $? != 0 ];
    then
      ${my_o} "..ERROR"
      ${my_o} "${Contact}"
      ${my_o} "${Exiting}"
      exit 1
    fi
    ${my_o} "..OK"
  fi

  if [ ! -e ${AGENT_TAR} ];
  then
    ${my_o} "The file ${AGENT_TAR} is not found"
    ${my_o} "Check your installation package"
    ${my_o} "${Exiting}"
    exit 1
  fi

  TAR_SIZE=$(${com_size} ${AGENT_TAR} | awk '{print $1}')
  let "TAR_SIZE=${TAR_SIZE}/2"
  #let "TAR_SIZE=${TAR_SIZE}/1024" lo faccio in K non M
  let "TAR_SIZE=${TAR_SIZE} + ${RESTART_DB_SIZE}"
  FREE_SIZE=$(df -l -k -P "${AGENT_DIR}" | grep -vi "filesystem" | awk '{if(NF > 5) print $4; else print $3}')
  #FREE_SIZE=$(df -lk ${AGENT_DIR} | grep free | awk '{print $1}')
  if [ ${FREE_SIZE} -lt ${TAR_SIZE} ];
  then
    ${my_o} "You have not enough space to extract the tar file ${AGENT_TAR}"
    ${my_o} "You need ${TAR_SIZE} Kb of free space in ${AGENT_DIR} repository"
    ${my_o} "but you have only ${FREE_SIZE} Kb of free space in this repository"
    ${my_o} "${Contact}"
    ${my_o} "${Exiting}"
    exit 1
  fi
  ${my_o} "Extracting files from archive  ${AGENT_TAR}, wait please..."
  cd "${AGENT_DIR}"
  tar -xf "${home_inst}/${AGENT_TAR}"
  if [ $? != 0 ];
  then
    ${my_o} "..ERROR"
    ${my_o} "${Contact}"
    ${my_o} "${Exiting}"
    exit 1
  fi

  ${my_o} "Done successfully"
  ${my_o} ""

  cd "${home_inst}"

  return 0
}

CheckAgentInst()
{
  if [ "${curr_os}" = "Cygwin" ];
  then
    if [ "${AGENT_USER_HOME}" = "" ];
    then
      AGENT_USER_HOME="${AGENT_USER_HOME_DEF}"
    fi
  else
    if [ -r /etc/passwd ];
    then
      AGENT_USER_HOME=$(fgrep "${AGENT_USER}:" /etc/passwd | awk 'BEGIN { FS = ":"} {print $6}')
    fi
  fi

  if [ "${SG}" = "0" ];
  then
#    AGENT_DIR_DEF=${AGENT_USER_HOME}/${FULLNAME}
    AGENT_DIR_DEF=${AGENT_USER_HOME}
  else
#    AGENT_DIR_DEF=${SGFS}/${FULLNAME}
    AGENT_DIR_DEF=${SGFS}
  fi
  answer=""
  while [ "${answer}" != "y" -a "${answer}" != "n" ];
  do
    ${my_o} "Insert directory name for installing ${AGENT_TITLE} ${AGENT_TYPE}"
    ${my_o} "[default ${AGENT_DIR_DEF}]"
    ${my_o} "Do you want modify? [y/n][default: n]: \c"
    read answer
    if [ "${answer}" = "" ];
    then
      answer="n"
    fi
  done
  if [ "${answer}" = "n" ];
  then
#    AGENT_DIR=${AGENT_DIR_DEF}
    AGENT_DIR="${AGENT_DIR_DEF}/${FULLNAME}"
  else
    answer1=""
    while [ "${answer1}" = "" ];
    do
      ${my_o} "Insert new directory name (full path): \c"
      read answer1
      if [ "${answer1}" != "" ];
      then
        AGENT_DIR="${answer1}/${FULLNAME}"
        if [ -d "${AGENT_DIR}" ];
        then
          ${my_o} "Sorry, the directory ${AGENT_DIR} is already present"
          answer1=""
        fi
      fi
    done
  fi

  ${my_o} "Creating and checking for directory ${AGENT_DIR}"

  if [ "${curr_os}" = "Cygwin" ];
  then
    AGENT_DIR_TMP=$(cygpath -m "${AGENT_DIR}")
    if [ "${AGENT_DIR_TMP}" != "" ];
    then
      AGENT_DIR="${AGENT_DIR_TMP}"
    fi
  fi

  if [ ! -d "${AGENT_DIR}" ];
  then
    mkdir -p "${AGENT_DIR}"
    (chown ${AGENT_USER}:${AGENT_GROUP} "${AGENT_DIR}") >/dev/null 2>&1
    chmod 755 "${AGENT_DIR}"
  fi

  inst_step="1"

  return 0
}

CheckEnvFile()
{
  ENV_FILE="${AGENT_USER_HOME}"/${USER_ENV_DIR}/${FULLNAME}.env
  MODE_STRING="IPT NMS Circuit"
  if [ -e $"{ENV_FILE}" ];
  then
    ${my_o} "The environment file ${ENV_FILE}"
    ${my_o} "already exist"
    ${my_o} "ERROR"
    ${my_o} "${Exiting}"
    exit 1
  fi
   if [ "${AGENT_MODE}" = "EM" ];
  then
    TMF814_APP_TYPE="SOEM"
    MODE_STRING="EM"
  fi
  PLUGIN_INSTALLATION_TYPE="Collocated"
  tmpans=""
  while [ "${tmpans}" != "y" -a "${tmpans}" != "n" ];
  do
      ${my_o} ""
      ${my_o} "Has the ${MODE_STRING} TMF application collocated with ${MODE_STRING} or Dislocated in the server?\c"
      ${my_o} "[y/n] [y=Dislocated][n=collocated with EM/IPT NMS Circuit] [default: n]: \c"
      read tmpans
      if [ "${tmpans}" = "" ];
      then
        tmpans="n"
      fi
  done
  if [ "${tmpans}" = "y" ];
  then
   	if [ "${AGENT_MODE}" = "EM" ];
  	then
    	TMF814_APP_TYPE="TMFEM"
  	else
  		TMF814_APP_TYPE="TMFNM"
  	fi
   	PLUGIN_INSTALLATION_TYPE="Dislocated"
  fi

  if [ ! -d "${AGENT_USER_HOME}/${USER_ENV_DIR}" ];
  then
    mkdir "${AGENT_USER_HOME}/${USER_ENV_DIR}"
    chmod 755 "${AGENT_USER_HOME}/${USER_ENV_DIR}"
    chown ${AGENT_USER}:${AGENT_GROUP} "${AGENT_USER_HOME}/${USER_ENV_DIR}" > /dev/null 2>&1
  else
    chown ${AGENT_USER}:${AGENT_GROUP} "${AGENT_USER_HOME}/${USER_ENV_DIR}" > /dev/null 2>&1
  fi

  if [ -e "${AGENT_DIR}/SCRIPT/Q_Env.skl" ];
  then
    #mv -f ${AGENT_DIR}/SCRIPT/Q_Env.skl ${ENV_FILE} > /dev/null 2>&1
    mv -f "${AGENT_DIR}/SCRIPT/Q_Env.skl" "${ENV_FILE}"

    AGENT_DIR1=$(${my_o} "${AGENT_DIR}" | sed -e "s?\/?\\\/?g")
    AGENT_TITLE1=$(${my_o} "${AGENT_TITLE}" | sed -e "s?\/?\\\/?g")
    AGENT_TYPE1=$(${my_o} "${AGENT_TYPE}" | sed -e "s?\/?\\\/?g")
    OBJDIR1=$(${my_o} ${OBJDIR} | sed -e "s?\/?\\\/?g")
    OBJ_VER1=$(${my_o} ${OBJ_VER} | sed -e "s?\/?\\\/?g")
    NMSDB_VER1=$(${my_o} ${NMSDB_VER} | sed -e "s?\/?\\\/?g")
    if [ "${curr_os}" = "Cygwin" ];
    then
      JAVA_DIR_TMP=$(cygpath -m "${JAVA_DIR}")
      JAVA_DIR1=$(${my_o} "${JAVA_DIR_TMP}" | sed -e "s?\/?\\\/?g")
    else
      JAVA_DIR1=$(${my_o} "${JAVA_DIR}" | sed -e "s?\/?\\\/?g")
    fi
    SGPACK1=$(${my_o} "${SGPACK}" | sed -e "s?\/?\\\/?g")

    ex << ++ "${ENV_FILE}" > /dev/null
      g/__AGENT_DIR__/s//"${AGENT_DIR1}"/g
      g/__AGENT_TITLE__/s//"${AGENT_TITLE1}"/g
      g/__AGENT_TYPE__/s//"${AGENT_TYPE1}"/g
      g/__AGENT_MODE__/s//"${AGENT_MODE}"/g
      g/__TMF814_APP_TYPE__/s//"${TMF814_APP_TYPE}"/g
      g/__OBJDIR__/s//${OBJDIR1}/g
      g/__OBJ_VER__/s//"${OBJ_VER1}"/g
      g/__NMSDB_VER__/s//"${NMSDB_VER1}"/g
      g/__JAVADIR__/s//"${JAVA_DIR1}"/g
      g/__SG__/s//${SG}/g
      g/__SGPACK__/s//"${SGPACK1}"/g
    w!
++
   else
    ${my_o} '#!/bin/sh\n'                                   > "${ENV_FILE}"
    ${my_o} 'if [ ${SHLIB_PATH} ];'                        >> "${ENV_FILE}"
    ${my_o} "then"                                         >> "${ENV_FILE}"
    ${my_o} "  SHLIB_PATH="'${OBJDIR}/lib:${AGENT_DIR}/LIB:${SHLIB_PATH}'"" >> "${ENV_FILE}"
    ${my_o} "else"                                         >> "${ENV_FILE}"
    ${my_o} "  SHLIB_PATH=${OBJDIR}/lib:${AGENT_DIR}/LIB"  >> "${ENV_FILE}"
    ${my_o} "fi\n"                                         >> "${ENV_FILE}"
    ${my_o} 'if [ ${LD_LIBRARY_PATH} ];'                   >> "${ENV_FILE}"
    ${my_o} "then"                                         >> "${ENV_FILE}"
    ${my_o} "  LD_LIBRARY_PATH="'${OBJDIR}/lib:${AGENT_DIR}/LIB:${LD_LIBRARY_PATH}'"" >> "${ENV_FILE}"
    ${my_o} "else"                                         >> "${ENV_FILE}"
    ${my_o} "  LD_LIBRARY_PATH=${OBJDIR}/lib:${AGENT_DIR}/LIB"  >> "${ENV_FILE}"
    ${my_o} "fi\n"
    ${my_o} "AGENT_DIR=${AGENT_DIR}"                       >> "${ENV_FILE}"
    ${my_o} "AGENT_TITLE=\"${AGENT_TITLE}\""               >> "${ENV_FILE}"
    ${my_o} "AGENT_TYPE=\"${AGENT_TYPE}\""                 >> "${ENV_FILE}"
    ${my_o} "AGENT_MODE=\"${AGENT_MODE}\""                 >> "${ENV_FILE}"
    ${my_o} "TMF814_APP_TYPE=\"${TMF814_APP_TYPE}\""       >> "${ENV_FILE}"
    ${my_o} "OBJDIR=${OBJDIR}"                             >> "${ENV_FILE}"
    ${my_o} "OBJ_VER=${OBJ_VER}"                           >> "${ENV_FILE}"
    ${my_o} "NMSDB_VER=${NMSDB_VER}"                       >> "${ENV_FILE}"
    ${my_o} "JAVA_HOME=\"${JAVA_DIR}\""                    >> "${ENV_FILE}"
    ${my_o} "CLASSPATH=."                                  >> "${ENV_FILE}"
    ${my_o} "SG=\"${SG}\""                                 >> "${ENV_FILE}"
    if [ "${SG}" = "1" ];
    then
      ${my_o} "SGPACK=${SGPACK}"                           >> "${ENV_FILE}"
    fi
    ${my_o} "PATH="'${PATH}:${AGENT_DIR}/BIN:/usr/vue/bin:'"${OBJDIR}"'/bin:'"${JAVA_DIR}"'/bin:'"${JAVA_DIR}"'/jre/bin:/usr/softbench/bin:'"." >> "${ENV_FILE}"
    ${my_o} ""                                             >> "${ENV_FILE}"
    ${my_o} "export AGENT_DIR"                             >> "${ENV_FILE}"
    ${my_o} "export AGENT_MODE"                            >> "${ENV_FILE}"
    ${my_o} "export TMF814_APP_TYPE"                       >> "${ENV_FILE}"
    ${my_o} "export OBJDIR"                                >> "${ENV_FILE}"
    ${my_o} "export OBJ_VER"                               >> "${ENV_FILE}"
    ${my_o} "export NMSDB_VER"                             >> "${ENV_FILE}"
    ${my_o} "export JAVA_HOME"                             >> "${ENV_FILE}"
    ${my_o} "export PATH"                                  >> "${ENV_FILE}"
    ${my_o} "export SHLIB_PATH"                            >> "${ENV_FILE}"
    ${my_o} "export LD_LIBRARY_PATH"                       >> "${ENV_FILE}"
    ${my_o} "export CLASSPATH"                             >> "${ENV_FILE}"
    ${my_o} ""                                             >> "${ENV_FILE}"
  fi

  chown ${AGENT_USER}:${AGENT_GROUP} "${ENV_FILE}" > /dev/null 2>&1
  chmod 444 "${ENV_FILE}"

  return 0
}

FreePortFile()
{
  name=$1

  port_file="${AGENT_USER_HOME}"/${USER_CONF_DIR}/${USER_CONF_PORT_FILE}
  if [ ! -e "${port_file}" ];
  then
    return 0
  fi

  found="0"

  port_file_tmp1="${AGENT_USER_HOME}"/${USER_CONF_DIR}/.port.$RANDOM
  if [ -e "${port_file_tmp1}" ];
  then
    rm -f "${port_file_tmp1}" > /dev/null 2>&1
  fi

  cat "${port_file}" | while
  read LINE;
  do
    if [ "${found}" = "0" ];
    then
      typeset -i tmp_i=0
      for tmp_item in ${LINE};
      do
        param[${tmp_i}]=${tmp_item}
        let "tmp_i=${tmp_i} + 1"
      done
      #set -A param ${LINE}
      if [ "${param[2]}" = "${name}" ];
      then
        ${my_o} "${param[0]} ${param[1]} free" >> ${port_file_tmp1}
        found="1"
      else
        ${my_o} "${LINE}" >> ${port_file_tmp1}
      fi
    else
      ${my_o} "${LINE}" >> ${port_file_tmp1}
    fi
  done

  if [ -e "${port_file_tmp1}" ];
  then
    mv -f "${port_file_tmp1}" "${port_file}" > /dev/null 2>&1
    chmod 644 "${port_file}"
    chown ${AGENT_USER}:${AGENT_GROUP} "${port_file}" > /dev/null 2>&1
  fi

  return 0
}

UpdatePortFile()
{
  name=$1
  req=$2

  port_file="${AGENT_USER_HOME}"/${USER_CONF_DIR}/${USER_CONF_PORT_FILE}
  if [ ! -e "${port_file}" ];
  then
    ${my_o} "The config file ${port_file}"
    ${my_o} "don't exist"
    ${my_o} "ERROR"
    ${my_o} "${Exiting}"
    exit 1
  fi

  found="0"

  port_file_tmp1="${AGENT_USER_HOME}"/${USER_CONF_DIR}/.port.$RANDOM
  if [ -e "${port_file_tmp1}" ];
  then
    rm -f "${port_file_tmp1}" > /dev/null 2>&1
  fi

  port_file_tmp2="${AGENT_USER_HOME}"/${USER_CONF_DIR}/.port.$RANDOM
  if [ -e "${port_file_tmp2}" ];
  then
    rm -f "${port_file_tmp2}" > /dev/null 2>&1
  fi

  sort "${port_file}" > "${port_file_tmp1}"

  cat "${port_file_tmp1}" | while
  read LINE;
  do
    if [ "${found}" = "0" ];
    then
      set -A param ${LINE}
      if [ "${param[2]}" = "free" ];
      then
        typeset -i num=${param[1]}
        if [ "${num}" -eq 0 ];
        then
          ${my_o} "${param[0]} ${req} ${name}" >> "${port_file_tmp2}"
      	  typeset -i max_port=0
  	  let "max_port=${param[0]} + ${req}"
          ${my_o} "${max_port} 0 free" >> "${port_file_tmp2}"
          found="1"
	  break
        fi
        if [ "${req}" -eq "${num}" ];
        then
          ${my_o} "${param[0]} ${num} ${name}" >> "${port_file_tmp2}"
          found="1"
          continue
        else
          ${my_o} "${LINE}" >> "${port_file_tmp2}"
        fi
      else
        ${my_o} "${LINE}" >> "${port_file_tmp2}"
      fi
    else
      ${my_o} "${LINE}" >> "${port_file_tmp2}"
    fi
  done

  if [ -e "${port_file_tmp2}" ];
  then
    mv -f "${port_file_tmp2}" "${port_file}" > /dev/null 2>&1
    chmod 644 "${port_file}"
    chown ${AGENT_USER}:${AGENT_GROUP} "${port_file}" > /dev/null 2>&1
  fi

  if [ -e "${port_file_tmp1}" ];
  then
    rm -f "${port_file_tmp1}" > /dev/null 2>&1
  fi

  return 0
}

CheckConfFile()
{
  UpdatePortFile "${FULLNAME}" ${NUM_PORT}
}

CheckNMName()
{
  #
  # Setup NMName. Per default propongo hostname corrente
  #
  NMNAME=${curr_host}
  MODE_STRING="IPT NMS Circuit"
  if [ "${AGENT_MODE}" = "EM" ];
  then
     MODE_STRING="EM"
  fi

  answer_nmname=""
  while [ "${answer_nmname}" != "y" ]; ##### Fix for the TR HO45097
  do
    ${my_o} ""
    ${my_o} "Insert the ${MODE_STRING} Manager Name for this application, please"
    ${my_o} "[default: ${NMNAME}]: \c"
    read nmname_tmp
    if [ "${nmname_tmp}" != "" ];
    then
      NMNAME="${nmname_tmp}"
    fi
    ${my_o} ""
    ${my_o} "The ${MODE_STRING} Manager Name ${NMNAME} is correct?"
    ${my_o} "[y/n] [default: y]: \c"
    read answer_nmname
    if [ "${answer_nmname}" = "" ];
    then
      answer_nmname="y"
    else
      NMNAME=${curr_host}
    fi
  done

  return 0
}

CheckMV38Hostname()
{
  #
  # Setup MV38 hostname. Per default propongo hostname corrente
  #
  MV38_HOST=${curr_host}
  MODE_STRING="IPT NMS Circuit"
  if [ "${AGENT_MODE}" = "EM" ];
  then
     MODE_STRING="EM"
  fi

  answer_mv38_sg=""
  while [ "${answer_mv38_sg}" != "y" -a "${answer_mv38_sg}" != "n" ];
  do
    ${my_o} ""
    ${my_o} "Has the ${MODE_STRING} Manager installed in SERVICE GUARD? [y/n] [default: n]: \c"
    read answer_mv38_sg
    if [ "${answer_mv38_sg}" = "" ];
    then
      answer_mv38_sg="n"
    fi
  done

  if [ "${answer_mv38_sg}" = "y" ];
  then
    #
    # Bisogna inserire il nome flottante della macchina su cui e' installato il Network Manager
    #
    ${my_o} ""
    ${my_o} "Insert the floating name for host where the ${MODE_STRING} Manager has installed, please : [${MV38_HOST}] \c"
    read mv38_floating_name

    if [ "${mv38_floating_name}" != "" ];
    then
      MV38_HOST=${mv38_floating_name}
    fi
  else
    #
    # MV38 non e' in SG , bisogna inserire l'hostname su cui e' installato il Network Manager
    #
    ${my_o} ""
    ${my_o} "Insert the hostname where the ${MODE_STRING} Manager has installed, please [${MV38_HOST}]: \c"
    read mv38_host_name

    if [ "${mv38_host_name}" != "" ];
    then
      MV38_HOST=${mv38_host_name}
    fi
  fi

  #
  # Esporto hostname di MV38
  #
  export MV38_HOST

  return 0
}

CheckCorba()
{
  CORBA_NEED_INST="0"

  #answer_need=""
  #while [ "${answer_need}" != "y" -a "${answer_need}" != "n" ];
  #do
  #  ${my_o} ""
  #  ${my_o} "Do you want install Corba Services inside this application?"
  #  ${my_o} "NOTE: if you have already installed Corba Service, please choose n"
  #  ${my_o} "      otherwise choose y, in this case you will be prompted to"
  #  ${my_o} "      provide valid license file"
  #  ${my_o} "[y/n] [default: y]: \c"
  #  read answer_need
  #  if [ "${answer_need}" = "" ];
  #  then
  #    answer_need="y"
  #  fi
  #done
#
#  if [ "${answer_need}" = "y" ];
#  then
#    CORBA_NEED_INST="1"
#  fi

  export CORBA_NEED_INST

#  ${my_o} ""

  return 0
}

CheckCorbaHostname()
{
  #
  # Setup Corba hostname. Per default propongo hostname corrente
  #
  CORBA_HOST=${curr_host}

  answer_mv38_sg=""
  while [ "${answer_mv38_sg}" != "y" -a "${answer_mv38_sg}" != "n" ];
  do
    ${my_o} ""
    ${my_o} "Has the Corba Services installed in SERVICE GUARD? [y/n] [default: n]: \c"
    read answer_mv38_sg
    if [ "${answer_mv38_sg}" = "" ];
    then
      answer_mv38_sg="n"
    fi
  done

  if [ "${answer_mv38_sg}" = "y" ];
  then
    #
    # Bisogna inserire il nome flottante della macchina su cui e' installato il Corba Services
    #
    ${my_o} ""
    ${my_o} "Insert the floating name for host where the Corba Services has installed, please : [${CORBA_HOST}] \c"
    read mv38_floating_name

    if [ "${mv38_floating_name}" != "" ];
    then
      CORBA_HOST=${mv38_floating_name}
    fi
  else
    #
    # CORBA_HOST non e' in SG , bisogna inserire l'hostname su cui e' installato il Corba Services
    #
    ${my_o} ""
    ${my_o} "Insert the hostname where the Corba Services has installed, please [${CORBA_HOST}]: \c"
    read mv38_host_name

    if [ "${mv38_host_name}" != "" ];
    then
      CORBA_HOST=${mv38_host_name}
    fi

    #provo a farmi dare ip
    CORBA_HOST_TMP=${CORBA_HOST}
    if [ -x /bin/getip ];
    then
      CORBA_HOST_IP=$(/bin/getip ${CORBA_HOST_TMP})
      if [ $? -eq 0 ];
      then
        if [ "${CORBA_HOST_IP}" != "" ];
        then
          CORBA_HOST="${CORBA_HOST_IP}"
        fi
      fi
    fi
  fi

  #
  # Esporto hostname di Corba Services
  #
  export CORBA_HOST

  return 0
}

CheckHostname()
{
  CheckNMName
  CheckMV38Hostname
  CheckCorba
  CheckCorbaHostname

  return 0
}

InstallCORBA()
{
  Name_Port="27888"
  ${my_o} ""
  ${my_o} "Insert the communication port of NameService [$Name_Port]: "
  read tmp_port
  if [ "${tmp_port}" != "" ];
  then
    Name_Port=${tmp_port}
    tmp_port=""
  fi

  Notif_Port="27889"
  ${my_o} ""
  ${my_o} "Insert the communication port of NotificationService [$Notif_Port]: "
  read tmp_port
  if [ "${tmp_port}" != "" ];
  then
    Notif_Port=${tmp_port}
    tmp_port=""
  fi

  if [ "${CORBA_NEED_INST}" = "1" ];
  then
    tmp_curr=$(pwd)
    cd "${tmp_curr}"/CORBA
    ./ptinstall.sh "${AGENT_DIR}" "${Name_Port}" "${Notif_Port}"
    if [ $? -ne  0 ];
    then
      ${my_o} "There is a problem Installing OpenFusion"
      ${my_o} ""
      cd "${tmp_curr}"
      exit 1
    fi

    cd "${tmp_curr}"
  else
    ${my_o} ""
    ${my_o} "The ORBInitRef of already installed CORBA Services will be set to"
    ${my_o} "corbaloc, if you want change this, please modify later the config"
    ${my_o} "file ORB.properties located in dir CONF of this application"
    ${my_o} "${AGENT_DIR}"
    ${my_o} "By default OpenORB is used, but it's possible to customize this"
    ${my_o} "application with any other compliance ORB"
    ${my_o} ""
    ${my_o} "Press return key to continue...\c"
    read tmp_continue
  fi

  export Name_Port
  export Notif_Port

  return 0
}

UpdateFileUFF()
{
  if [ -e "${AGENT_DIR}/SCRIPT/AsyncManager.sh.ORIG" ];
  then
    cp -f "${AGENT_DIR}/SCRIPT/AsyncManager.sh.ORIG" "${AGENT_DIR}/SCRIPT/AsyncManager.sh"
  fi

  file_list=$(find "${AGENT_DIR}" -type f \( -name "*.properties" -o -name "*.uff" -o -name "log4j*.xml" \) -print)
  export file_list

  if [ "${SG}" = "1" ];
  then
    NUMERR=1
    TMF_ASYNC_MOUNT_POINT="${SGTMFA_MOUNT_POINT}"
  else
    NUMERR=0
    TMF_ASYNC_MOUNT_POINT="/opt/mv38"
  fi

  DEF_HOST=${curr_host}
  PLUGIN_ASYNC_DEF_HOST=${MV38_HOST}

  if [ "${SG}" = "1" ];
  then
    if [ "${SGHOST}" != "" ];
    then
      DEF_HOST=${SGHOST}
      
    fi
  fi

  ENV_FILE1=$(${my_o} "${ENV_FILE}" | sed -e "s?\/?\\\/?g")

  NMNAME_FULL=$(${my_o} "${NMNAME}" | sed -e "s?\/?\\\/?g")

  TMF_ASYNC_MOUNT_POINT1=$(${my_o} "${TMF_ASYNC_MOUNT_POINT}" | sed -e "s?\/?\\\/?g")

  TMP_CORBA_ENABLE="DISABLE"
  if [ "${CORBA_NEED_INST}" = "1" ];
  then
    TMP_CORBA_ENABLE="ENABLE"
  fi

  PLUG_TYPE="38"
  TMP_TMFASYNC_ENABLE="DISABLE"
  if [ "${curr_os}" != "Cygwin" ];
  then
    TMP_TMFASYNC_ENABLE="ENABLE"
  fi
  PDM_ENABLE="no"
  if [ "${AGENT_MODE}" = "EM" ];
  then
  	TMP_TMFASYNC_ENABLE="DISABLE"
  	PLUG_TYPE="36"
        PDM_ENABLE="yes"
  fi

  if [ "${JRE}" = "0" ];
  then
    JBIN=$(${my_o} "/bin" | sed -e "s?\/?\\\/?g")
  else
    JBIN=$(${my_o} "/jre/bin" | sed -e "s?\/?\\\/?g")
  fi

  AGENT_SHLIB_TMP="${OBJDIR}/lib:${AGENT_DIR}/LIB:${AGENT_DIR}/BIN"
  AGENT_SHLIB_TMP1=$(${my_o} ${AGENT_SHLIB_TMP} | sed -e "s?\/?\\\/?g")

  for item in ${file_list};
  do
    ${my_o} ""
    ${my_o} "Updating template ${item}"

    ex << ++ "${item}" > /dev/null
      g/FULLNAME/s//${FULLNAME}/g
      g/AGENT_FULL_PATH/s//${AGENT_DIR1}/g
      g/AGENT_FULL_PC_PATH/s//${AGENT_DIR1}/g
      g/AGENT_USER/s//${AGENT_USER}/g
      g/AGENT_GROUP/s//${AGENT_GROUP}/g
      g/AGENT_UID/s//${AGENT_UID}/g
      g/AGENT_GID/s//${AGENT_GID}/g
      g/USER_ENV_DIR/s//${USER_ENV_DIR}/g
      g/USER_CONF_DIR/s//${USER_CONF_DIR}/g
      g/USER_CONF_PORT_FILE/s//${USER_CONF_PORT_FILE}/g
      g/ENV_FILE/s//${ENV_FILE1}/g
      g/AGENT_SHLPATH/s//${AGENT_SHLIB_TMP1}/g
      g/AGENT_LDLPATH/s//${AGENT_SHLIB_TMP1}/g
      g/^MAX_NUM_RETRY/s/MAX_NUM_RETRY = .*/MAX_NUM_RETRY = ${NUMERR}/
      g/TMF_CURR_HOST/s//${DEF_HOST}/g
      g/^CheckNMDir/s/CheckNMDir = .*/CheckNMDir = ${TMF_ASYNC_MOUNT_POINT1}/
      g/^TMFA.Async.Host/s/TMFA.Async.Host.*/TMFA.Async.Host=${DEF_HOST}/
      g/^TMFA.CHECK.CheckNMDir/s/TMFA.CHECK.CheckNMDir = .*/TMFA.CHECK.CheckNMDir = ${TMF_ASYNC_MOUNT_POINT1}/
      g/__JAVADIR__/s//${JAVA_DIR1}/g
      g/^JAVA_BIN_DIR =/s/JAVA_BIN_DIR =.*/JAVA_BIN_DIR = \$\(JAVA_DIR\)${JBIN}/
      g/__CORBA_ENABLE__/s//${TMP_CORBA_ENABLE}/g
      g/__TMFASYNC_ENABLE__/s//${TMP_TMFASYNC_ENABLE}/g
      g/^Framework.home/s/Framework.home.*/Framework.home=${AGENT_DIR1}/
      g/^Plugin.InstallationType/s/Plugin.InstallationType.*/Plugin.InstallationType=${PLUGIN_INSTALLATION_TYPE}/
      g/^EMS1=/s/EMS1=.*/EMS1=${FULLNAME}/
      g/^Framework.IDL.version/s/Framework.IDL.version=.*/#Framework.IDL.version=3.0/
      g/^PlugIn.I36.Enabled/s/PlugIn.I36.Enabled=.*/PlugIn.I36.Enabled=true/
      g/^PlugIn.NMName/s/PlugIn.NMName=.*/PlugIn.NMName=${NMNAME_FULL}/
      g/^EMS.nmName/s/EMS.nmName.*/EMS.nmName=${NMNAME_FULL}/
      g/^PlugIn.SBI.Host/s/PlugIn.SBI.Host=.*/PlugIn.SBI.Host=${MV38_HOST}/
      g/^PlugIn.SBI.host/s/PlugIn.SBI.host=.*/PlugIn.SBI.host=${MV38_HOST}/
      g/^PlugIn.SBI.PDMSupported/s/PlugIn.SBI.PDMSupported.*/PlugIn.SBI.PDMSupported=${PDM_ENABLE}/
      g/^com.marconi.fusion.tmf.plugIn.IPlugIn/s/com.marconi.fusion.tmf.plugIn.IPlugIn=.*/com.marconi.fusion.tmf.plugIn.IPlugIn=com.marconi.fusion.tmf.i${PLUG_TYPE}PlugIn.I${PLUG_TYPE}PlugIn/
      g/^com.marconi.fusion.tmf.plugIn.IPlugIn.properties/s/com.marconi.fusion.tmf.plugIn.IPlugIn.properties=.*/com.marconi.fusion.tmf.plugIn.IPlugIn.properties=\$\{Framework.conf\}\/I${PLUG_TYPE}Plugin.properties/
      g/^PlugIn.Async.Host/s/PlugIn.Async.Host=.*/PlugIn.Async.Host=${PLUGIN_ASYNC_DEF_HOST}/
      g/^PlugIn.GCT.rexec.HostName/s/PlugIn.GCT.rexec.HostName=.*/PlugIn.GCT.rexec.HostName=${MV38_HOST}/
      g/^ORG.Properties/s/ORG.PropertiesDir=.*/ORG.PropertiesDir=${AGENT_DIR1}\/CONF\/jacorb.properties/
      g/^EventChannel.IOR/s/EventChannel.IOR=.*/EventChannel.IOR=${AGENT_DIR1}\/CONF\/EventChannel.ior/
      g/^jacorb.logfile=/s/jacorb.logfile=.*/jacorb.logfile=${AGENT_DIR1}\/LOG\/jacorb.log/
      g/@LOGDIR@/s/@LOGDIR@/${AGENT_DIR1}\/LOG/g
      g/^ORBInitRef=-ORBInitRef NameService=/s/ORBInitRef=-ORBInitRef NameService=.*/ORBInitRef=-ORBInitRef NameService=corbaloc\:\:${CORBA_HOST}\:${Name_Port}\/NameService -ORBInitRef NotificationService=corbaloc\:\:${CORBA_HOST}\:${Notif_Port}\/NotificationService/
      g/^NameService.host =/s/NameService.host =.*/NameService.host = ${CORBA_HOST}/
      g/^NameService.port =/s/NameService.port =.*/NameService.port = ${Name_Port}/
      g/^NotificationService.host =/s/NotificationService.host =.*/NotificationService.host = ${CORBA_HOST}/
      g/^NotificationService.port =/s/NotificationService.port =.*/NotificationService.port = ${Notif_Port}/
    w!
++
    ${my_o} "Successfully done"
  done

  tmp_conf_file="${AGENT_DIR}/CONF/TMFFramework.properties"
  if [ -e "${tmp_conf_file}" ];
  then
  	ex << ++ "${tmp_conf_file}" > /dev/null
      g/^EMS =/s/EMS =.*/EMS =${FULLNAME}/
      g/^EMS.type =/s/EMS.type =.*/EMS.type =${AGENT_MODE}/
    w!
++
  fi

  tmp_conf_file="${AGENT_DIR}/CONF/I36Plugin.properties"
  if [ -e "${tmp_conf_file}" ];
  then
    ex << ++ "${tmp_conf_file}" > /dev/null
      g/^PlugIn.realignment.collection/s/PlugIn.realignment.collection.*/PlugIn.realignment.collection = no/
    w!
++
  fi

#      g/^ORBInitRef.NameService/s/ORBInitRef.NameService=.*/ORBInitRef.NameService=corbaloc\:\:${CORBA_HOST}\:${Name_Port}\/NameService/
#      g/^ORBInitRef.NotificationService/s/ORBInitRef.NotificationService=.*/ORBInitRef.NotificationService=corbaloc\:\:${CORBA_HOST}\:${Notif_Port}\/NotificationService/

#      g/^ORBInitRef.NameService/s/ORBInitRef.NameService=.*/ORBInitRef.NameService=http\:\/\/${CORBA_HOST}\:8080\/NameSingleton.ior/
#      g/^ORBInitRef.NotificationService/s/ORBInitRef.NotificationService=.*/ORBInitRef.NotificationService=http\:\/\/${CORBA_HOST}\:8080\/NotificationSingleton.ior/

  ${my_o} ""

  return 0
}

UpdateLinuxFile()
{
  if [ "${curr_os}" != "Linux" ];
  then
    return 0
  fi

  #metto a posto il Q_Reboot
  ex << ++ "${AGENT_DIR}/SCRIPT/Q_Reboot" > /dev/null
    g/^#!\/sbin\/sh/s/#!\/sbin\/.*/#!\/bin\/sh/
    w!
++


  if [ -e "${AGENT_DIR}/LIB/oojava.jar" ];
  then
    rm -f "${AGENT_DIR}/LIB/oojava.jar"            > /dev/null 2>&1
  fi

  if [ -e "${AGENT_DIR}/LIB/oojava-9.3.jar" ];
  then
    rm -f "${AGENT_DIR}/LIB/oojava-9.3.jar"            > /dev/null 2>&1
  fi

  return 0
}

UpdateWinFile()
{
  if [ "${curr_os}" != "Cygwin" ];
  then
    return 0
  fi

  bat_list=$(find "${AGENT_DIR}/SCRIPT" -type f -name "*.bat" -print)
  for item in ${bat_list};
  do
    if [ -e "${item}.tmp" ];
    then
      rm -f "${item}.tmp" > /dev/null 2>&1
    fi

    cygpath -w -f "${item}" > "${item}.tmp"

    if [ -e "${item}.tmp" ];
    then
      rm -f "${item}"            > /dev/null 2>&1
      mv "${item}.tmp" "${item}" > /dev/null 2>&1
    fi
  done

  #metto a posto il pre_start
  ex << ++ "${AGENT_DIR}/SCRIPT/pre_start" > /dev/null
    g/^#!\/bin\/sh/s/#!\/bin\/.*/#!\/bin\/bash/
    w!
++

  #metto a posto l'iconetta
  if [ -e "${AGENT_USER_HOME}/${USER_ENV_DIR}/Q_Manager.bat" ];
  then
    rm -f "${AGENT_USER_HOME}/${USER_ENV_DIR}/Q_Manager.bat" > /dev/null 2>&1
  fi
  cp "${AGENT_DIR}/SCRIPT/Q_Manager.bat" "${AGENT_USER_HOME}/${USER_ENV_DIR}/Q_Manager.bat"
  chmod 755 "${AGENT_USER_HOME}/${USER_ENV_DIR}/Q_Manager.bat"

  if [ -e "${AGENT_USER_HOME}/${USER_ENV_DIR}/sof.ico" ];
  then
    rm -f "${AGENT_USER_HOME}/${USER_ENV_DIR}/sof.ico" > /dev/null 2>&1
  fi
  cp "${AGENT_DIR}/SCRIPT/sof.ico" "${AGENT_USER_HOME}/${USER_ENV_DIR}/sof.ico"
  chmod 755 "${AGENT_USER_HOME}/${USER_ENV_DIR}/sof.ico"

  return 0
}

RenameFileUFF()
{
  for item in ${file_list};
  do
    new_dir=$(dirname "${item}")
    new_item=$(basename "${item}" .uff)
    mv "${item}" "${new_dir}"/"${new_item}" > /dev/null  2>&1
  done

  UpdateWinFile
  UpdateLinuxFile

  return 0
}

RenameExportFileConf()
{
 if [ "${AGENT_MODE}" = "EM" ];
  then
  	if [ -e "${AGENT_DIR}/CONF/CustomerData_EM.properties" ];
  		then
    	mv -f "${AGENT_DIR}/CONF/CustomerData_EM.properties" "${AGENT_DIR}/CONF/CustomerData.properties"
    	if [ -e "${AGENT_DIR}/CONF/CustomerData_NM.properties" ];
    	then
    		rm -f "${AGENT_DIR}/CONF/CustomerData_NM.properties"
    	fi
  	fi
  	if [ -e "${AGENT_DIR}/CONF/OrchestratorPlugin_EM.properties" ];
  		then
    	mv -f "${AGENT_DIR}/CONF/OrchestratorPlugin_EM.properties" "${AGENT_DIR}/CONF/OrchestratorPlugin.properties"
    	if [ -e "${AGENT_DIR}/CONF/OrchestratorPlugin_NM.properties" ];
    	then
    		rm -f "${AGENT_DIR}/CONF/OrchestratorPlugin_NM.properties"
    	fi
  	fi
  	if [ -e "${AGENT_DIR}/CONF/OrchestratorManager_EM.properties" ];
  		then
    	mv -f "${AGENT_DIR}/CONF/OrchestratorManager_EM.properties" "${AGENT_DIR}/CONF/OrchestratorManager.properties"
    	if [ -e "${AGENT_DIR}/CONF/OrchestratorManager_NM.properties" ];
    	then
    		rm -f "${AGENT_DIR}/CONF/OrchestratorManager_NM.properties"
    	fi
  	fi
  	
  fi

  if [ "${AGENT_MODE}" = "NM" ];
  then
  	if [ -e "${AGENT_DIR}/CONF/CustomerData_NM.properties" ];
  		then
    	mv -f "${AGENT_DIR}/CONF/CustomerData_NM.properties" "${AGENT_DIR}/CONF/CustomerData.properties"
    	if [ -e "${AGENT_DIR}/CONF/CustomerData_EM.properties" ];
    	then
    		rm -f "${AGENT_DIR}/CONF/CustomerData_EM.properties"
    	fi
  	fi
  	if [ -e "${AGENT_DIR}/CONF/OrchestratorPlugin_NM.properties" ];
  		then
    	mv -f "${AGENT_DIR}/CONF/OrchestratorPlugin_NM.properties" "${AGENT_DIR}/CONF/OrchestratorManager.properties"
    	if [ -e "${AGENT_DIR}/CONF/OrchestratorPlugin_EM.properties" ];
    	then
    		rm -f "${AGENT_DIR}/CONF/OrchestratorPlugin_EM.properties"
    	fi
  	fi
  	if [ -e "${AGENT_DIR}/CONF/OrchestratorManager_NM.properties" ];
  		then
    	mv -f "${AGENT_DIR}/CONF/OrchestratorManager_NM.properties" "${AGENT_DIR}/CONF/OrchestratorPlugin.properties"
    	if [ -e "${AGENT_DIR}/CONF/OrchestratorManager_EM.properties" ];
    	then
    		rm -f "${AGENT_DIR}/CONF/OrchestratorManager_EM.properties"
    	fi
  	fi
  	
  fi

}

UpdateReboot()
{
  if [ "${curr_os}" = "Cygwin" ];
  then
    return 0
  fi

  answer=""
  if [ "${SG}" = "0" ];
  then
    while [ "${answer}" != "y" -a "${answer}" != "n" ];
    do
      ${my_o} ""
      ${my_o} "Do you want start ${FULLNAME} at host Reboot?"
      ${my_o} "[y/n] [default: y]: \c"
      read answer
      if [ "${answer}" = "" ];
      then
        answer="y"
      fi
    done
    ${my_o} ""
  else
    answer="y"
  fi

  if [ "${answer}" = "y" ];
  then
    chmod 555 ${AGENT_DIR}/SCRIPT/Q_Reboot
    if [ "${SG}" = "0" ];
    then
      if [ "${curr_os}" = "Linux" ];
      then
        ${my_o} "Configuring Linux shutdown/reboot management..."
        ln -f -s ${AGENT_DIR}/SCRIPT/Q_Reboot "${boot_path}"/rc3.d/S98${FULLNAME}
        ln -f -s ${AGENT_DIR}/SCRIPT/Q_Reboot "${boot_path}"/rc5.d/S98${FULLNAME}
        ln -f -s ${AGENT_DIR}/SCRIPT/Q_Reboot "${boot_path}"/rc0.d/K01${FULLNAME}
        ln -f -s ${AGENT_DIR}/SCRIPT/Q_Reboot "${boot_path}"/rc1.d/K01${FULLNAME}
        ln -f -s ${AGENT_DIR}/SCRIPT/Q_Reboot "${boot_path}"/rc3.d/K01${FULLNAME}
        ln -f -s ${AGENT_DIR}/SCRIPT/Q_Reboot "${boot_path}"/rc5.d/K01${FULLNAME}
        ln -f -s ${AGENT_DIR}/SCRIPT/Q_Reboot "${boot_path}"/rc6.d/K01${FULLNAME}
        ln -f -s ${AGENT_DIR}/SCRIPT/Q_Reboot "${boot_path}"/chk${FULLNAME}
   		/sbin/chkconfig chk${FULLNAME} on
      else
        ${my_o} "Configuring HP shutdown/reboot management..."
        ln -f -s ${AGENT_DIR}/SCRIPT/Q_Reboot "${boot_path}"/rc3.d/S940${FULLNAME}
        ln -f -s ${AGENT_DIR}/SCRIPT/Q_Reboot "${boot_path}"/rc2.d/K905${FULLNAME}
      fi
    else
      ${my_o} "Configuring Service Guard shutdown/reboot management..."
      ln -f -s ${AGENT_DIR}/SCRIPT/Q_Reboot "${boot_path}"/rc3.d/s940${FULLNAME}
      ln -f -s ${AGENT_DIR}/SCRIPT/Q_Reboot "${boot_path}"/rc2.d/k905${FULLNAME}

      ${my_o} ""
      SG_POSTFIX=$(basename ${SGPACK})
      nodenames=$(grep NODE_NAME ${SGPACK}/${SG_POSTFIX}.ascii | grep -v "^#" |  awk '{print $2}')
      rcmd="rm -f ${boot_path}/rc3.d/s940${FULLNAME} ${boot_path}/rc2.d/k905${FULLNAME}; ln -f -s ${AGENT_DIR}/SCRIPT/Q_Reboot ${boot_path}/rc3.d/s940${FULLNAME}; ln -f -s ${AGENT_DIR}/SCRIPT/Q_Reboot ${boot_path}/rc2.d/k905${FULLNAME};"
      Home_IsOnFS=$(echo "${AGENT_USER_HOME}" | awk -vhomefs="${AGENT_USER_HOME}" -vtestfs="${SGFS}/" '{print index(homefs, testfs)}')
      for tnod in ${nodenames};
      do
        if [ "${tnod}" != "${curr_host}" ];
        then
          ${my_o} " Updating ${tnod} SG node"
          remsh ${tnod} -n ${rcmd}
          if [ $? -ne  0 ];
          then
            ${my_o} "There is a problem configuring host ${tnod}"
            ${my_o} "Check manually the configuration for Boot files"
            ${my_o} ""
          fi
	  sg_user_home=""
	  if [ "${Home_IsOnFS}" != "1" ];
          then
	    sg_user_home=$(remsh ${tnod} -n fgrep "${AGENT_USER}:" /etc/passwd | awk 'BEGIN { FS = ":"} {print $6}')
          fi
          if [ "${sg_user_home}" != "" ];
          then
            new_cmd="mkdir -p ${sg_user_home}/${USER_ENV_DIR}; chmod 755 ${sg_user_home}/${USER_ENV_DIR}; chown ${AGENT_USER}:${AGENT_GROUP} ${sg_user_home}/${USER_ENV_DIR}"
            remsh ${tnod} -n ${new_cmd}
            if [ $? -ne  0 ];
            then
              ${my_o} "There is a problem configuring host ${tnod}"
              ${my_o} "Check manually the configuration for ENV directory"
              ${my_o} ""
            fi

            sg_env_file=$(basename ${ENV_FILE})
	    rcp ${ENV_FILE} ${tnod}:${sg_user_home}/${USER_ENV_DIR}/${sg_env_file}
            if [ $? -ne  0 ];
            then
              ${my_o} "There is a problem configuring host ${tnod}"
              ${my_o} "Check manually the configuration for ENV file"
              ${my_o} ""
            fi
            new_cmd="chmod 444 ${sg_user_home}/${USER_ENV_DIR}/${sg_env_file}"
            remsh ${tnod} -n ${new_cmd}
            if [ $? -ne  0 ];
            then
              ${my_o} "There is a problem configuring host ${tnod}"
              ${my_o} "Check manually the configuration for rigth of ENV file"
              ${my_o} ""
            fi

            new_cmd="mkdir -p ${sg_user_home}/${USER_CONF_DIR}; chmod 755 ${sg_user_home}/${USER_CONF_DIR}; chown ${AGENT_USER}:${AGENT_GROUP} ${sg_user_home}/${USER_CONF_DIR}"
            remsh ${tnod} -n ${new_cmd}
            if [ $? -ne  0 ];
            then
              ${my_o} "There is a problem configuring host ${tnod}"
              ${my_o} "Check manually the configuration for CONF directory"
              ${my_o} ""
            fi

#	    rcp ${AGENT_USER_HOME}/${USER_CONF_DIR}/${USER_CONF_PORT_FILE} ${tnod}:${sg_user_home}/${USER_CONF_DIR}/${USER_CONF_PORT_FILE}
#            if [ $? -ne  0 ];
#            then
#              ${my_o} "There is a problem configuring host ${tnod}"
#              ${my_o} "Check manually the configuration for CONF file"
#              ${my_o} ""
#            fi
#            new_cmd="chmod 444 ${sg_user_home}/${USER_CONF_DIR}/${USER_CONF_PORT_FILE}"
#            remsh ${tnod} -n ${new_cmd}
#            if [ $? -ne  0 ];
#            then
#              ${my_o} "There is a problem configuring host ${tnod}"
#              ${my_o} "Check manually the configuration for rigth of CONF file"
#              ${my_o} ""
#            fi
          fi
        fi
      done
    fi
    ${my_o} "Successfully done"
  fi

  return 0
}

UpdateAttila()
{
  if [ "${SG}" = "1" ];
  then
    return 0
  fi

  if [ "${curr_os}" = "Cygwin" ];
  then
    if [ -e "${AGENT_DIR}/SCRIPT/SetAttila.sh" ];
    then
      rm -f "${AGENT_DIR}/SCRIPT/SetAttila.sh" > /dev/null 2>&1
    fi
    if [ -d "${AGENT_DIR}/SCRIPT/.ATTILA_HELP" ];
    then
      rm -fR "${AGENT_DIR}/SCRIPT/.ATTILA_HELP" > /dev/null 2>&1
    fi
    if [ -d "${AGENT_DIR}/SCRIPT/.ATTILA_IMG" ];
    then
      rm -fR "${AGENT_DIR}/SCRIPT/.ATTILA_IMG" > /dev/null 2>&1
    fi
    return 0
  fi

  ${my_o} ""
  ${my_o} "Updating ATTILA utilities..."
  if [ -x "${AGENT_DIR}/SCRIPT/SetAttila.sh" ];
  then
    "${AGENT_DIR}/SCRIPT/SetAttila.sh"
    if [ $? -ne 0 ];
    then
      ${my_o} "WARNING: ATTILA utilities NOT configured"
    fi
  fi

  ${my_o} "Successfully done"

  return 0
}

UpdateRights()
{
  ${my_o} ""
  ${my_o} "Updating access rights ..."

  if [ ! -d "${AGENT_DIR}/LOG" ];
  then
    mkdir "${AGENT_DIR}/LOG"
  fi

  if [ ! -d "${AGENT_DIR}/USERS" ];
  then
    mkdir "${AGENT_DIR}/USERS"
  fi

  if [ "${curr_os}" = "HP-UX" ];
  then
    if [ "${SG}" = "1" ];
    then
      if [ -e "${AGENT_DIR}/BIN/monitorapp" ];
      then
        rm -f "${AGENT_DIR}/BIN/monitorapp" > /dev/null 2>&1
      fi
      ln -f -s "${AGENT_DIR}/BIN/monitorapp.${curr_os}.${HP_ARCH}" "${AGENT_DIR}/BIN/monitorapp"
      rm -f "${AGENT_DIR}/BIN/monitorapp.Lin"* > /dev/null 2>&1
      rm -f "${AGENT_DIR}/BIN/monitorapp.Cyg"* > /dev/null 2>&1

      if [ -e "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.boot" ];
      then
        rm -f "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.boot" > /dev/null 2>&1
      fi
      ln -f -s "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.boot.${curr_os}.${HP_ARCH}" "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.boot"
      rm -f "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.boot.Lin"* > /dev/null 2>&1
      rm -f "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.boot.Cyg"* > /dev/null 2>&1

      if [ -e "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.fdb" ];
      then
        rm -f "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.fdb" > /dev/null 2>&1
      fi
      ln -f -s "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.fdb.${curr_os}.${HP_ARCH}" "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.fdb"
      rm -f "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.fdb.Lin"* > /dev/null 2>&1
      rm -f "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.fdb.Cyg"* > /dev/null 2>&1

      if [ -e "${AGENT_DIR}/LIB/oojava.jar" ];
      then
        rm -f "${AGENT_DIR}/LIB/oojava.jar" > /dev/null 2>&1
      fi
      #ln -f -s "${AGENT_DIR}/LIB/oojava.jar.${curr_os}.${HP_ARCH}" "${AGENT_DIR}/LIB/oojava.jar"
      rm -f "${AGENT_DIR}/LIB/oojava.jar.Lin"* > /dev/null 2>&1
      rm -f "${AGENT_DIR}/LIB/oojava.jar.Cyg"* > /dev/null 2>&1

      if [ -e "${AGENT_DIR}/LIB/liboojava.sl" ];
      then
        rm -f "${AGENT_DIR}/LIB/liboojava.sl" > /dev/null 2>&1
      fi
      #ln -f -s "${AGENT_DIR}/LIB/liboojava.sl.${curr_os}.${HP_ARCH}" "${AGENT_DIR}/LIB/liboojava.sl"
      if [ -e "${AGENT_DIR}/LIB/liboojava.so" ];
      then
        rm -f "${AGENT_DIR}/LIB/liboojava.so" > /dev/null 2>&1
      fi
      #ln -f -s "${AGENT_DIR}/LIB/liboojava.so.${curr_os}.${HP_ARCH}" "${AGENT_DIR}/LIB/liboojava.so"
      rm -f "${AGENT_DIR}/LIB/liboojava.so.Lin"* > /dev/null 2>&1

      if [ -e "${AGENT_DIR}/BIN/TMFA_Man" ];
      then
        rm -f "${AGENT_DIR}/BIN/TMFA_Man" > /dev/null 2>&1
      fi
      if [ -e "${AGENT_DIR}/BIN/TMFA_Man.${curr_os}.${HP_ARCH}" ];
      then
        ln -f -s "${AGENT_DIR}/BIN/TMFA_Man.${curr_os}.${HP_ARCH}" "${AGENT_DIR}/BIN/TMFA_Man"
      fi
      rm -f "${AGENT_DIR}/BIN/TMFA_Man.Lin"*   > /dev/null 2>&1
      rm -f "${AGENT_DIR}/BIN/TMFA_Man.Cyg"*   > /dev/null 2>&1

      if [ -e "${AGENT_DIR}/CONF/app.ini" ];
      then
        rm -f "${AGENT_DIR}/CONF/app.ini" > /dev/null 2>&1
      fi
      rm -f "${AGENT_DIR}/CONF/app.ini.Lin"* > /dev/null 2>&1
      rm -f "${AGENT_DIR}/CONF/app.ini.Cyg"* > /dev/null 2>&1
      if [ ${AGENT_MODE} = "EM" ];
	  then
		find "${AGENT_DIR}/CONF" -type f \( -name "app.ini.NM.*" \) -exec rm "{}" \;
		file_list=$(find "${AGENT_DIR}/CONF" -type f \( -name "app.ini.EM.*" \) -print)
	  else
		find "${AGENT_DIR}/CONF" -type f \( -name "app.ini.EM.*" \) -exec rm "{}" \;
		file_list=$(find "${AGENT_DIR}/CONF" -type f \( -name "app.ini.NM.*" \) -print)
	  fi
	  for item in ${file_list};
	  do
		dirname=`dirname ${item}`
		new_filename=$( basename ${item} | awk '{ print substr( $0, 0, 8 ) substr( $0, 12, length($0)) }' )
		mv ${item} ${dirname}/${new_filename}
	  done
      ln -f -s "${AGENT_DIR}/CONF/app.ini.${curr_os}.${HP_ARCH}" "${AGENT_DIR}/CONF/app.ini"
    else
      mv "${AGENT_DIR}/BIN/monitorapp.${curr_os}.${HP_ARCH}" "${AGENT_DIR}/BIN/monitorapp"
      mv -f "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.boot.${curr_os}.${HP_ARCH}" "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.boot"
      mv -f "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.fdb.${curr_os}.${HP_ARCH}" "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.fdb"
      #mv -f "${AGENT_DIR}/LIB/oojava.jar.${curr_os}.${HP_ARCH}" "${AGENT_DIR}/LIB/oojava.jar"
      #if [ "${HP_ARCH}" = "pa11" ];
      #then
      #  mv -f "${AGENT_DIR}/LIB/liboojava.sl.${curr_os}.${HP_ARCH}" "${AGENT_DIR}/LIB/liboojava.sl"
      #else
      #  mv -f "${AGENT_DIR}/LIB/liboojava.so.${curr_os}.${HP_ARCH}" "${AGENT_DIR}/LIB/liboojava.so"
      #fi
      if [ -e "${AGENT_DIR}/BIN/TMFA_Man.${curr_os}.${HP_ARCH}" ];
      then
        mv "${AGENT_DIR}/BIN/TMFA_Man.${curr_os}.${HP_ARCH}" "${AGENT_DIR}/BIN/TMFA_Man"
      fi
      mv "${AGENT_DIR}/CONF/app.ini.${AGENT_MODE}.${curr_os}.${HP_ARCH}" "${AGENT_DIR}/CONF/app.ini"
    fi
  else
    mv "${AGENT_DIR}/BIN/monitorapp.${ext_os}" "${AGENT_DIR}/BIN/monitorapp"
    mv "${AGENT_DIR}/CONF/app.ini.${AGENT_MODE}.${ext_os}" "${AGENT_DIR}/CONF/app.ini"
    if [ "${curr_os}" = "Linux" ];
    then
      #mv "${AGENT_DIR}/LIB/liboojava.so.${curr_os}" "${AGENT_DIR}/LIB/liboojava.so"
      if [ -e "${AGENT_DIR}/BIN/TMFA_Man.${ext_os}" ];
      then
        mv "${AGENT_DIR}/BIN/TMFA_Man.${ext_os}" "${AGENT_DIR}/BIN/TMFA_Man"
      fi
    fi
    mv "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.boot.${ext_os}" "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.boot"
    mv "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.fdb.${ext_os}" "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.fdb"
    #mv "${AGENT_DIR}/LIB/oojava.jar.${ext_os}" "${AGENT_DIR}/LIB/oojava.jar"
  fi

  if [ "${SG}" = "0" ];
  then
    rm -f "${AGENT_DIR}/BIN/monitorapp."*            > /dev/null 2>&1
    rm -f "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.boot."* > /dev/null 2>&1
    rm -f "${AGENT_DIR}/DBRESTORE/SOO_TMF_DB.fdb."*  > /dev/null 2>&1
    rm -f "${AGENT_DIR}/LIB/oojava.jar."*            > /dev/null 2>&1
    rm -f "${AGENT_DIR}/LIB/liboojava.sl."*          > /dev/null 2>&1
    rm -f "${AGENT_DIR}/LIB/liboojava.so."*          > /dev/null 2>&1
    rm -f "${AGENT_DIR}/BIN/TMFA_Man."*              > /dev/null 2>&1
    rm -f "${AGENT_DIR}/CONF/app.ini."*              > /dev/null 2>&1
  fi

  if [ "${AGENT_MODE}" = "EM" ];
  then
    if [ -e "${AGENT_DIR}/BIN/TMFA_Man" ];
    then
  	  rm -f "${AGENT_DIR}/BIN/TMFA_Man"              > /dev/null 2>&1
    fi
    if [ -e "${AGENT_DIR}/SCRIPT/AsyncManager.sh" ];
    then
  	  rm -f "${AGENT_DIR}/SCRIPT/AsyncManager.sh"    > /dev/null 2>&1
    fi
  fi

  if [ "${curr_os}" = "Cygwin" ];
  then
    if [  -d "${AGENT_DIR}/BIN" ];
    then
      if [  -d "${home_inst}/BIN_WIN" ];
      then
        cp "${home_inst}"/BIN_WIN/* "${AGENT_DIR}/BIN"
      fi
    fi
  fi

  chown -R ${AGENT_USER}:${AGENT_GROUP} "${AGENT_DIR}" > /dev/null 2>&1
  chmod -R 755 "${AGENT_DIR}"/* > /dev/null 2>&1

  find "${AGENT_DIR}" -type f -name "*.ini" -exec chmod 644 {} \;
  find "${AGENT_DIR}" -type f -name "*.cnf" -exec chmod 644 {} \;
  find "${AGENT_DIR}" -type f -name "*.cfg" -exec chmod 644 {} \;
  find "${AGENT_DIR}" -type f -name "*.dat" -exec chmod 644 {} \;
  find "${AGENT_DIR}" -type f -name "*.properties" -exec chmod 644 {} \;
  find "${AGENT_DIR}" -type f -name "*.profile" -exec chmod 644 {} \;

  chmod 777 "${AGENT_DIR}"/LOG
  chmod 666 "${AGENT_DIR}"/LOG/* > /dev/null 2>&1

  if [ -d "${AGENT_DIR}/SOCKETS" ];
  then
    chmod 777 "${AGENT_DIR}/SOCKETS"
  fi

  if [ -d "${AGENT_DIR}/LANG" ];
  then
    chmod 555 "${AGENT_DIR}"/LANG
    chmod 444 "${AGENT_DIR}"/LANG/* > /dev/null 2>&1
  fi

  if [ -d "${AGENT_DIR}/HELP" ];
  then
    find "${AGENT_DIR}/HELP" -type f -name "*" -exec chmod 444 {} \;
    find "${AGENT_DIR}/HELP" -type d -name "*" -exec chmod 755 {} \;
  fi

  if [ -d "${AGENT_DIR}/DATA" ];
  then
    chmod 644 "${AGENT_DIR}"/DATA/* > /dev/null 2>&1
  fi

  if [ -d "${AGENT_DIR}/DBRESTORE" ];
  then
    chmod 644 "${AGENT_DIR}"/DBRESTORE/* > /dev/null 2>&1
  fi

  chmod u+s "${AGENT_DIR}/SCRIPT/Q_Backup"  > /dev/null 2>&1
  chmod u+s "${AGENT_DIR}/SCRIPT/Q_Restore" > /dev/null 2>&1

  chown root:${AGENT_GROUP} "${AGENT_DIR}/SCRIPT/Q_ResetDB"
  chmod u+s "${AGENT_DIR}/SCRIPT/Q_ResetDB"

  chown root:${AGENT_GROUP} "${AGENT_DIR}/SCRIPT/Q_CheckNMSDB"
  chmod u+s "${AGENT_DIR}/SCRIPT/Q_CheckNMSDB"

  chown root:${AGENT_GROUP} "${AGENT_DIR}/SCRIPT/installfd"
  chmod u+s "${AGENT_DIR}/SCRIPT/installfd"

  chmod 755 "${AGENT_DIR}"/SCRIPT/* > /dev/null 2>&1

  if [ "${curr_os}" != "Cygwin" ];
  then
    find "${AGENT_DIR}/SCRIPT" -type f -name "*.bat" -exec rm -f {} \;
    find "${AGENT_DIR}/SCRIPT" -type f -name "*.bat.*" -exec rm -f {} \;
    find "${AGENT_DIR}/SCRIPT" -type f -name "*.ico" -exec rm -f {} \;
  fi

  if [ "${curr_os}" != "Cygwin" ];
  then
    if [ -e "${AGENT_DIR}/BIN/TMFA_Man" ];
    then
      chgrp nmc "${AGENT_DIR}/BIN/TMFA_Man" > /dev/null 2>&1
      chmod 755 "${AGENT_DIR}/BIN/TMFA_Man" > /dev/null 2>&1
    fi
    if [ -e "${AGENT_DIR}/SCRIPT/AsyncManager.sh" ];
    then
      chgrp nmc "${AGENT_DIR}/SCRIPT/AsyncManager.sh" > /dev/null 2>&1
      chmod 755 "${AGENT_DIR}/SCRIPT/AsyncManager.sh" > /dev/null 2>&1
    fi
    find "${AGENT_DIR}/SCRIPT" -type f -name "*.ORIG" -exec rm -f {} \;
  fi

  if [ -e "${AGENT_DIR}/LIB/oojava.jar" ];
  then
    rm -f "${AGENT_DIR}/LIB/oojava.jar"            > /dev/null 2>&1
  fi

  if [ -e "${AGENT_DIR}/LIB/oojava-9.3.jar" ];
  then
    rm -f "${AGENT_DIR}/LIB/oojava-9.3.jar"        > /dev/null 2>&1
  fi

  rm -f "${AGENT_DIR}/LIB/liboojava.s"*            > /dev/null 2>&1

  ${my_o} "Successfully done"

  return 0
}

UpdateLicense()
{
  if [ -x "${AGENT_DIR}/SCRIPT/Update_Lic" ];
  then
    "${AGENT_DIR}/SCRIPT/Update_Lic"
    if [ $? -ne 0 ];
    then
      ${my_o} "WARNING: license NOT configured"
    fi
  fi

  return 0
}

UpdateUsers()
{
  ${my_o} ""
  ${my_o} "Remember to setup the users for tmf, inventory or performance"
  ${my_o} "before run the application."
  ${my_o} "To do this, please execute the script ${AGENT_DIR}/SCRIPT/SetupUser.sh"
  ${my_o} "logged as user ${AGENT_USER}"
  ${my_o} ""

  return 0
}

StartAgent()
{
  answer="y"
  ${my_o} ""
  ${my_o} "Do you want start application now? (y/n) [default: ${answer}]: \c"
  read tmp
  if [ "${tmp}" != "" ];
  then
     answer=${tmp}
  fi
  if [ "${answer}" = "y" ];
  then
    ${my_o} ""
    ${my_o} "Application is starting: please wait...\c"
    su ${AGENT_USER} -c "${AGENT_DIR}"/SCRIPT/Q_Start
  fi

 return 0
}

AgentInstall()
{
  ${my_o} ""
  ${my_o} "Starting installation, some preliminary check will be executed"
  ${my_o} ""

  CheckNMSDB
  CheckJava
  CheckSG
  CheckPostfix
  CheckAlreadyInstalled
  CheckUser
  # fix for the TR HO60165 --  "tmfman" user should not be created during TMF over EM installation 
  if [ "${AGENT_MODE}" = "NM" ];
    then
   CheckOtherUser
      fi
  CheckExtraGroup4User
  CheckAgentInst
  CheckTarInst
  CheckEnvFile
  #CheckConfFile
  CheckHostname
  InstallCORBA
  UpdateFileUFF
  RenameFileUFF
  RenameExportFileConf
  UpdateReboot
  UpdateAttila
  UpdateRights
  "${AGENT_DIR}"/SCRIPT/Q_ResetDB force
  UpdateLicense
  UpdateUsers
  #StartAgent

  ${my_o} ""
  ${my_o} "Installation successfully executed!"
  ${my_o} ""

  return 0
}

CheckRunningApp()
{
  ${my_o} ""
  ${my_o} "Check if application $1 is running..."
  if [ -x "${AGENT_DIR}/BIN/monitorapp" ];
  then
    tmp_res=$("${AGENT_DIR}"/BIN/monitorapp -p "${AGENT_DIR}"/CONF/app.ini -t)
    if [ "${tmp_res}" -gt 0 ];
    then
      ${my_o} "WARNING: you have to stop $1 application first.\n"
      ${my_o} "${Exiting}"
      exit 1
    fi
  fi

  if [ -x "${home_inst}/ckproc" ];
  then
    for item in ${PROC_LIST_STOP};
    do
      res=$("${home_inst}"/ckproc -f "${AGENT_DIR}/${item}")
      if [ "${res}" -gt 0 ];
      then
        ${my_o} "WARNING: the ${AGENT_DIR}/${item} process is running."
        ${my_o} "You have to stop this process before removing application."
        ${my_o} "${Exiting}"
        exit 1
      fi
    done
  fi

  ${my_o} "OK, application $1 is NOT running"
  ${my_o} ""

  return 0
}

RemoveRebootFile()
{
  file_postfix=$1

  ${my_o} "Removing Boot files...\c"

  if [ "${curr_os}" = "Linux" ];
  then
    if [ -e "${boot_path}"/rc3.d/S98${file_postfix} ];
    then
      rm -f "${boot_path}"/rc3.d/S98${file_postfix} > /dev/null 2>&1
    fi

    ${my_o} "..\c"

    if [ -e "${boot_path}"/rc5.d/S98${file_postfix} ];
    then
      rm -f "${boot_path}"/rc5.d/S98${file_postfix} > /dev/null 2>&1
    fi

    ${my_o} "..\c"

    if [ -e "${boot_path}"/rc0.d/K01${file_postfix} ];
    then
      rm -f "${boot_path}"/rc0.d/K01${file_postfix} > /dev/null 2>&1
    fi

    ${my_o} "..\c"

    if [ -e "${boot_path}"/rc1.d/K01${file_postfix} ];
    then
      rm -f "${boot_path}"/rc1.d/K01${file_postfix} > /dev/null 2>&1
    fi

    ${my_o} "..\c"

    if [ -e "${boot_path}"/rc3.d/K01${file_postfix} ];
    then
      rm -f "${boot_path}"/rc3.d/K01${file_postfix} > /dev/null 2>&1
    fi

    ${my_o} "..\c"

    if [ -e "${boot_path}"/rc5.d/K01${file_postfix} ];
    then
      rm -f "${boot_path}"/rc5.d/K01${file_postfix} > /dev/null 2>&1
    fi

    ${my_o} "..\c"

    if [ -e "${boot_path}"/rc6.d/K01${file_postfix} ];
    then
      rm -f "${boot_path}"/rc6.d/K01${file_postfix} > /dev/null 2>&1
    fi
    
    if [ -e "${boot_path}"/chk${file_postfix} ];
    then
	    /sbin/chkconfig chk${FULLNAME} off
    	rm -f "${boot_path}"/chk${file_postfix} > /dev/null 2>&1
    fi
    
  else
    if [ -e "${boot_path}"/rc3.d/S940${file_postfix} ];
    then
      rm -f "${boot_path}"/rc3.d/S940${file_postfix} > /dev/null 2>&1
    fi

    ${my_o} "..\c"

    if [ -e "${boot_path}"/rc2.d/K905${file_postfix} ];
    then
      rm -f "${boot_path}"/rc2.d/K905${file_postfix} > /dev/null 2>&1
    fi

    ${my_o} "..\c"

    if [ -e "${boot_path}"/rc3.d/s940${file_postfix} ];
    then
      rm -f "${boot_path}"/rc3.d/s940${file_postfix} > /dev/null 2>&1
    fi

    ${my_o} "..\c"

    if [ -e "${boot_path}"/rc2.d/k905${file_postfix} ];
    then
      rm -f "${boot_path}"/rc2.d/k905${file_postfix} > /dev/null 2>&1
    fi
  fi

  ${my_o} "..OK"
  ${my_o} ""

  return 0
}

RemoveSGFile()
{
  file_postfix=$1

  if [ "${SG}" = "0" ];
  then
    return 0
  fi

  ${my_o} "Removing configuration files of Service Guard..."
  ${my_o} ""

  SG_POSTFIX=$(basename ${SGPACK})
  nodenames=$(grep NODE_NAME ${SGPACK}/${SG_POSTFIX}.ascii | grep -v "^#" |  awk '{print $2}')
  rcmd="rm -f ${boot_path}/rc3.d/s940${file_postfix} ${boot_path}/rc2.d/k905${file_postfix};"

  Home_IsOnFS="0"
  pack="${SGPACK}"
  if [ -e "${pack}"/rcontrol.sh ];
  then
    WORKFS=""
    WORKFS=$(grep "FS\[0\]"  "${pack}"/rcontrol.sh | grep -v grep | grep -v "^#" | cut -d ";" -f 2 | cut -d = -f 2 | cut -d '"' -f 2)
    if [ "${WORKFS}" = "" ];
    then
      ${my_o} "The field WORKFS is missing in the environment file ${pack}/rcontrol.sh"
    else
      Home_IsOnFS=$(echo "${AGENT_USER_HOME}" | awk -vhomefs="${AGENT_USER_HOME}" -vtestfs="${WORKFS}/" '{print index(homefs, testfs)}')
    fi
  fi

  for tnod in ${nodenames};
  do
    if [ "${tnod}" != "${curr_host}" ];
    then
      ${my_o} " Updating ${tnod} SG node"
      remsh ${tnod} -n ${rcmd}
      if [ $? -ne  0 ];
      then
        ${my_o} "There is a problem removing configuration from host ${tnod}"
        ${my_o} "Check manually the configuration"
        ${my_o} ""
      fi

      sg_user_home=""
      if [ "${Home_IsOnFS}" != "1" ];
      then
        sg_user_home=$(remsh ${tnod} -n fgrep "${AGENT_USER}:" /etc/passwd | awk 'BEGIN { FS = ":"} {print $6}')
      fi
      if [ "${sg_user_home}" != "" ];
      then
        new_cmd="rm -f ${sg_user_home}/${file_postfix}.env"
        remsh ${tnod} -n ${new_cmd}
        if [ $? -ne  0 ];
        then
          ${my_o} "There is a problem removing configuration from host ${tnod}"
          ${my_o} "Check manually the configuration for ENV file"
          ${my_o} ""
        fi

        new_cmd="mkdir -p ${sg_user_home}/${USER_CONF_DIR}; chmod 755 ${sg_user_home}/${USER_CONF_DIR}; chown ${AGENT_USER}:${AGENT_GROUP} ${sg_user_home}/${USER_CONF_DIR}"
        remsh ${tnod} -n ${new_cmd}
        if [ $? -ne  0 ];
        then
          ${my_o} "There is a problem removing configuration from host ${tnod}"
          ${my_o} "Check manually the configuration for CONF directory"
          ${my_o} ""
        fi

#	rcp ${AGENT_USER_HOME}/${USER_CONF_DIR}/${USER_CONF_PORT_FILE} ${tnod}:${sg_user_home}/${USER_CONF_DIR}/${USER_CONF_PORT_FILE}
#        if [ $? -ne  0 ];
#        then
#          ${my_o} "There is a problem removing configuration from host ${tnod}"
#          ${my_o} "Check manually the configuration for CONF file"
#          ${my_o} ""
#        fi

#        new_cmd="chmod 444 ${sg_user_home}/${USER_CONF_DIR}/${USER_CONF_PORT_FILE}"
#        remsh ${tnod} -n ${new_cmd}
#        if [ $? -ne  0 ];
#        then
#          ${my_o} "There is a problem removing configuration from host ${tnod}"
#          ${my_o} "Check manually the configuration for rigth of CONF file"
#          ${my_o} ""
#        fi
      fi
    fi
  done

  ${my_o} "Successfully done"
  ${my_o} ""

  return 0
}

RemoveAttilaFile()
{
  file_postfix=$1

  ATTILA_USER_HOME=$(fgrep "attila:" /etc/passwd | awk 'BEGIN { FS = ":"} {print $6}')
  if [ "${ATTILA_USER_HOME}" != "" ];
  then
    if [ -d "${ATTILA_USER_HOME}/.nms" ];
    then
      if [ -d "${ATTILA_USER_HOME}/.nms/NBIMAN" ];
      then
        if [ -d "${ATTILA_USER_HOME}/.nms/NBIMAN/${file_postfix}" ];
        then
          ${my_o} ""
          ${my_o} "Removing configuration files of Attila user...\c"
          rm -fR "${ATTILA_USER_HOME}/.nms/NBIMAN/${file_postfix}" > /dev/null 2>&1
          ${my_o} "..OK"
        fi

        dir_count=$(ls "${ATTILA_USER_HOME}/.nms/NBIMAN/"* | grep ':$' | sed -e "s/://")
        if [ "${dir_count}" = "" ];
        then
          rm -fR "${ATTILA_USER_HOME}/.nms/NBIMAN" > /dev/null 2>&1
        fi
      fi
    fi
  fi

  return 0
}

RemovePrivateFile()
{
  file_postfix=$1

  ${my_o} ""
  ${my_o} "Removing Private file of $1"

  if [ -x "${home_inst}"/PrivateRemove.sh ];
  then
    "${home_inst}"/PrivateRemove.sh ${file_postfix}
  fi


  if [ "${curr_os}" = "Cygwin" ];
  then
    #tolgo l'iconetta
    if [ ! -e "${AGENT_USER_HOME}/${USER_ENV_DIR}/Q_Manager.bat" ];
    then
      return 0
    fi

    if [ ! -e "${AGENT_DIR}/${SCRIPT}/Q_Manager.bat" ];
    then
      return 0
    fi

    diff "${AGENT_DIR}/${SCRIPT}/Q_Manager.bat" "${AGENT_USER_HOME}/${USER_ENV_DIR}/Q_Manager.bat" > /dev/null

    if [ $? -eq 0 ];
    then
      rm -f "${AGENT_USER_HOME}/${USER_ENV_DIR}/Q_Manager.bat" > /dev/null 2>&1
      if [  -e "${AGENT_USER_HOME}/${USER_ENV_DIR}/sof.ico" ];
      then
        rm -f "${AGENT_USER_HOME}/${USER_ENV_DIR}/sof.ico" > /dev/null 2>&1
      fi
    fi
  fi

  return 0
}

RemoveCORBA()
{
  file_postfix=$1
  ${my_o} ""
  ${my_o} "Uninstalling CORBA services of $1"
  if [ -x "${AGENT_DIR}"/CORBA/bin/uninstall ];
  then
    "${AGENT_DIR}"/CORBA/bin/uninstall
  else
    ${my_o} "CORBA not found in ${AGENT_DIR}"
  fi

  return 0
}

RemoveConfFile()
{
  ${my_o} "Removing conf files"
  if [ "${curr_os}" = "Cygwin" ];
  then
    AGENT_USER_HOME="${AGENT_USER_HOME_DEF}"
  else
    AGENT_USER_HOME=$(fgrep "${AGENT_USER}:" /etc/passwd | awk 'BEGIN { FS = ":"} {print $6}')
  fi

  FreePortFile $1

  return 0
}

RemoveApplication()
{
  file_postfix=$1

  ${my_o} ""
  ${my_o} "Choose if you want delete or keep a copy of ${file_postfix} application"
  ${my_o} ""
  ${my_o} " 1. Delete ${file_postfix} application"
  ${my_o} " 2. Keep a copy of ${file_postfix} application"
  ${my_o} ""

  typeset -i opt_menu=0
  while [ "$opt_menu" -lt 1 -o "$opt_menu" -gt 2 ]
  do
    ${my_o} "Please, enter your selection [1..2][default 1]: \c"
    read l_var
    if [ "${l_var}" = "" ];
    then
      l_var="1"
    fi
    if isNumeric "${l_var}"
    then
      opt_menu=${l_var}
    fi
  done

  ${my_o} ""

  if [ ${opt_menu} -eq 1 ];
  then
    ${my_o} "Deleting ${file_postfix} application..."
    ${my_o} "Removing ${AGENT_DIR} Structure..."
    rm -fR "${AGENT_DIR}"                                           > /dev/null 2>&1
    rm -fR "${AGENT_USER_HOME}/${USER_ENV_DIR}/${file_postfix}.env" > /dev/null 2>&1
    ${my_o} "Successfully done"
  else
    TMP_DATE=$(date '+%y_%m_%d_%H_%M')
    new_name="${AGENT_DIR}_${TMP_DATE}_OLD"
    ${my_o} "Moving ${file_postfix} application in ${new_name}"
    mv -f "${AGENT_DIR}" "${new_name}" > /dev/null 2>&1
    mv -f "${AGENT_USER_HOME}/${USER_ENV_DIR}/${file_postfix}.env" "${new_name}/${file_postfix}.env" >/dev/null 2>&1
    ${my_o} "Successfully done"
  fi

  ${my_o} ""

  return 0
}

RemoveTmpFile()
{
  if [ -d "${AGENT_USER_HOME}/${USER_CONF_DIR}" ];
  then
    rm -f "${AGENT_USER_HOME}/${USER_CONF_DIR}"/.port.* > /dev/null 2>&1
  fi

  return 0
}

AgentRemove()
{
  ${my_o} ""

  if [ "${curr_os}" = "Cygwin" ];
  then
    AGENT_USER_HOME="${AGENT_USER_HOME_DEF}"
  else
    id -u ${AGENT_USER} > /dev/null 2>&1
    if [ $? = 0 ];
    then
      AGENT_USER_HOME=$(fgrep "${AGENT_USER}:" /etc/passwd | awk 'BEGIN { FS = ":"} {print $6}')
    fi
  fi

  if [ "${AGENT_USER_HOME}" != "" ];
  then
    #tmp_list=$(ls "${AGENT_USER_HOME}/${USER_ENV_DIR}/"*.env | xargs grep -E -e "AGENT_TYPE=\"${AGENT_TYPE}\"" -e "AGENT_TYPE=${AGENT_TYPE}" )
    tmp_list=$(ls "${AGENT_USER_HOME}/${USER_ENV_DIR}/"*.env 2>/dev/null)

    inst_list=""
    for item in ${tmp_list};
    do
      agent_type=$(fgrep "AGENT_TYPE=" ${item} | awk 'BEGIN {FS = "="} {print $2}')
      if [ "${agent_type}" = "\"${AGENT_TYPE}\"" -o "${agent_type}" = "${AGENT_TYPE}" ];
      then
        tmp_item=$(basename ${item} .env)
        tmp_inst_list="${tmp_inst_list} ${item}"
      fi
    done

    typeset -i tot_inst=0
    for item in ${tmp_inst_list};
    do
      inst_list[${tot_inst}]=${item}
      let "tot_inst=${tot_inst} + 1"
    done
    if [ ${tot_inst} = 0 ];
    then
      ${my_o} "There isn't ${AGENT_TITLE} application installed on this server"
      return 0
    fi

    if [ ${tot_inst} = 1 ];
    then
      rem_inst=${inst_list}
    else
      ${my_o} ""
      ${my_o} "=================================================================="
      ${my_o} " List of the ${AGENT_TITLE} applications installed"
      ${my_o} "=================================================================="
      ${my_o} ""

      typeset -i inst_count=0
      typeset -i idx=1
      while [ ${inst_count} -lt ${tot_inst} ];
      do
        new_item=$(basename ${inst_list[${inst_count}]} .env)
        ${my_o} "  ${idx}. ${new_item}"
        let "inst_count=${inst_count} + 1"
        let "idx=${idx} + 1"
      done
      ${my_o} ""
      typeset -i sel_inst=0
      while [ "${sel_inst}" -lt 1 -o "${sel_inst}" -gt ${tot_inst} ]
      do
        ${my_o} "Make your selection, please [1..${tot_inst}][Default = 1]: \c"
        read l_var
        if [ "${l_var}" = "" ];
        then
          l_var=1
        fi
        if isNumeric "${l_var}"
        then
          sel_inst=${l_var}
        fi
      done
      let "sel_inst=${sel_inst} - 1"
      rem_inst=${inst_list[${sel_inst}]}
    fi
    new_item=$(basename ${rem_inst} .env)
  else
    ${my_o} "There isn't ${AGENT_TITLE} application installed"
    return 0
  fi

  answer=""
  while [ "${answer}" != "y" -a "${answer}" != "n" ]
  do
    ${my_o} ""
    ${my_o} "You have selected the ${new_item} application"
    ${my_o} "Are you sure to remove it? [y/n] [default: y]: \c"
    read answer
    ${my_o} ""
    if [ "${answer}" = "" ];
    then
      answer="y"
    fi
    if [ "${answer}" = "n" ];
    then
      ${my_o} "${Exiting}"
      exit 0
    fi
  done

  . "${AGENT_USER_HOME}/${USER_ENV_DIR}/${new_item}.env"

  DeleteAgent ${new_item}
  ${my_o} ""
  ${my_o} "Installation ${new_item} successfully removed!"
  ${my_o} ""

  return 0
}

DeleteAgent()
{
 if [ $# -ne 1 ];
 then
   echo ""
   echo "Usage $0 <Name of teh Agent To be Deleted>"
   echo ""
   exit 1
 fi
 agent_remove=$1
 CheckRunningApp   ${agent_remove}
 RemoveRebootFile  ${agent_remove}
 RemoveAttilaFile  ${agent_remove}
 RemovePrivateFile ${agent_remove}
 RemoveCORBA       ${agent_remove}
 RemoveConfFile    ${agent_remove}
 RemoveSGFile      ${agent_remove}
 RemoveApplication ${agent_remove}

 return 0
}


AgentUpgrade()
{
  ${my_o} ""
  ${my_o} "Starting Ugrade"
  ${my_o} ""

  OLD_AGENT_USER_HOME=""

  CheckSG
  CheckPostfix

  ${my_o} ""

  if [ "${curr_os}" = "Cygwin" ];
  then
    AGENT_USER_HOME="${AGENT_USER_HOME_DEF}"
  else
    id -u ${AGENT_USER} > /dev/null 2>&1
    if [ $? = 0 ];
    then
      AGENT_USER_HOME=$(fgrep "${AGENT_USER}:" /etc/passwd | awk 'BEGIN { FS = ":"} {print $6}')
    fi
    id -u fusion > /dev/null 2>&1
    if [ $? = 0 ];
    then
      OLD_AGENT_USER_HOME=$(fgrep "fusion:" /etc/passwd | awk 'BEGIN { FS = ":"} {print $6}')
    fi
  fi


  if [ "${AGENT_USER_HOME}" != "" ];
  then
    #tmp_list=$(ls "${AGENT_USER_HOME}/${USER_ENV_DIR}/"*.env | xargs grep -E -e "AGENT_TYPE=\"${AGENT_TYPE}\"" -e "AGENT_TYPE=${AGENT_TYPE}" )
    tmp_list=$(ls "${AGENT_USER_HOME}/${USER_ENV_DIR}/"*.env 2>/dev/null)
    inst_list=""
    for item in ${tmp_list};
    do
      agent_type=$(fgrep "AGENT_TYPE=" ${item} | awk 'BEGIN {FS = "="} {print $2}' 2>/dev/null)
      ######## CR377 code HN42375
      tmp_agent_dir=$(grep "AGENT_DIR=" ${item} | cut -d"=" -f2 | cut -d"\"" -f2 2>/dev/null)
      tmp_agent_mode=$(grep "AGENT_MODE=" ${item} | cut -c 13-14 2>/dev/null)
 	  tmp_agent_version=$(grep "EMS.version=" "${tmp_agent_dir}/CONF/TMFFramework.properties" | cut -c 13- 2>/dev/null)
	  ### tmp_agent_version=$(echo ${tmp_agent_version} | tr "." "_")
	  ### echo "tmp_agent_version:::" ${tmp_agent_version}
	  ##########
 	  if [ "${agent_type}" = "\"${AGENT_TYPE}\"" -o "${agent_type}" = "${AGENT_TYPE}" ];
      then
        tmp_item=$(basename ${item} .env)
        ######### CR377 code HN42375
        if [ -n "${tmp_agent_mode}" -a -n "${tmp_agent_version}" ];
		    then
				if [  "${tmp_agent_mode}" = "${appMode}" ] && [ ${tmp_agent_version} \> ${MINOR_UPGRADE_VER} -a ${tmp_agent_version} \< ${MAJOR_UPGRADE_VER} ];
		        then
		            tmp_inst_list="${tmp_inst_list} ${item}"
		        fi
		fi
       ##########
    fi
    done

	if [ "${OLD_AGENT_USER_HOME}" != "" ];
  	then
  	###############  getting Tmf instances installed in Fusion direcotory and with User Fusion -- IPTNMS renaming
    tmp_list=$(ls "${OLD_AGENT_USER_HOME}/${USER_ENV_DIR}/"*.env 2>/dev/null)
    for item in ${tmp_list};
    do
      agent_type=$(fgrep "AGENT_TYPE=" ${item} | awk 'BEGIN {FS = "="} {print $2}' 2>/dev/null)
      ######## CR377 code HN42375
      tmp_agent_dir=$(grep "AGENT_DIR=" ${item} | cut -d"=" -f2 | cut -d"\"" -f2 2>/dev/null)
      tmp_agent_mode=$(grep "AGENT_MODE=" ${item} | cut -c 13-14 2>/dev/null)
 	  tmp_agent_version=$(grep "EMS.version=" "${tmp_agent_dir}/CONF/TMFFramework.properties" | cut -c 13- 2>/dev/null)
	  ### tmp_agent_version=$(echo ${tmp_agent_version} | tr "." "_")
	  echo "tmp_agent_version:::" ${tmp_agent_version}
	  ##########
 	  if [ "${agent_type}" = "\"${AGENT_TYPE}\"" -o "${agent_type}" = "${AGENT_TYPE}" ];
      then
        tmp_item=$(basename ${item} .env)
        ######### CR377 code HN42375
        if [ -n "${tmp_agent_mode}" -a -n "${tmp_agent_version}" ];
		    then
				if [  "${tmp_agent_mode}" = "${appMode}" ] && [ ${tmp_agent_version} \> ${MINOR_UPGRADE_VER} -a ${tmp_agent_version} \< ${MAJOR_UPGRADE_VER} ];
		        then
		            tmp_inst_list="${tmp_inst_list} ${item}"
		        fi
		fi
       ##########
    fi
    done
	###############  End of getting Tmf instances installed in Fusion direcotory and with User Fusion -- IPTNMS renaming
	fi

	echo tmp_inst_list

    typeset -i tot_inst=0
    for item in ${tmp_inst_list};
    do
      inst_list[${tot_inst}]=${item}
      let "tot_inst=${tot_inst} + 1"
    done

    if [ ${tot_inst} = 0 ];
    then
      ${my_o} "There isn't ${AGENT_TITLE} application installed on this server"
      return 0
    fi

	${my_o} "Select the Instance to be Upgraded: \c"

    if [ ${tot_inst} = 1 ];
    then
      rem_inst=${inst_list}
    else
      ${my_o} ""
      ${my_o} "=================================================================="
      ${my_o} " List of the ${AGENT_TITLE} applications installed"
      ${my_o} "=================================================================="
      ${my_o} ""

      typeset -i inst_count=0
      typeset -i idx=1
      while [ ${inst_count} -lt ${tot_inst} ];
      do
        new_item=$(basename ${inst_list[${inst_count}]} .env)
        ${my_o} "  ${idx}. ${new_item}"
        let "inst_count=${inst_count} + 1"
        let "idx=${idx} + 1"
      done
      ${my_o} ""
      typeset -i sel_inst=0
      while [ "${sel_inst}" -lt 1 -o "${sel_inst}" -gt ${tot_inst} ]
      do
        ${my_o} "Make your selection, please [1..${tot_inst}][Default = 1]: \c"
        read l_var
        if [ "${l_var}" = "" ];
        then
          l_var=1
        fi
        if isNumeric "${l_var}"
        then
          sel_inst=${l_var}
        fi
      done
      let "sel_inst=${sel_inst} - 1"
      rem_inst=${inst_list[${sel_inst}]}
    fi
    new_item=$(basename ${rem_inst} .env)
  else
    ${my_o} "There isn't ${AGENT_TITLE} application installed"
    return 0
  fi

  answer=""
  while [ "${answer}" != "y" -a "${answer}" != "n" ]
  do
    ${my_o} ""
    ${my_o} "You have selected the ${new_item} application"
    ${my_o} "Are you sure to upgrade it? [y/n] [default: y]: \c"
    read answer
    ${my_o} ""
    if [ "${answer}" = "" ];
    then
      answer="y"
    fi
    if [ "${answer}" = "n" ];
    then
      ${my_o} "${Exiting}"
      exit 0
    fi
  done
    ################# CR377 code HN42379
    . "${AGENT_USER_HOME}/${USER_ENV_DIR}/${new_item}.env"
    ### UPGRADE_AGENT_DIR="${AGENT_USER_HOME}/${new_item}"

    UPGRADE_AGENT_DIR="${AGENT_DIR}"
   agent_remove="${new_item}"
    ##########
   # Install the new Agent
	AgentInstall

     ########  CR377 HN42387 -- till now we placed the temporary file in UPGRADE_AGENT_DIR, So we delete the temporary files from UPGRADE_AGENT_DIR.
     ########  now we placing temporary file in installing directory so we can delete the temporary files without asking users


   ############### adding code to change the path of backup files from upgradeAgentDIR to AgentDIR and
   ############### remove backup files from Agent dir rather than upgradeAgentDIR -- TR HN42379
	### IPT-NMS -- Variable to hold the ems version of the upgraded TMF agent
	upgrade_agent_tmf_version=$(grep "EMS.version=" ${UPGRADE_AGENT_DIR}/CONF/TMFFramework.properties | cut -d"=" -f2 )

   #Take the DB Backup
   ${my_o} ""
    ${my_o} "DB Backup is being performed: please wait...\c"
    if [ ${upgrade_agent_tmf_version} \> MINOR_UPGRADE_VER -a ${upgrade_agent_tmf_version} \< MAJOR_UPGRADE_VER_1 ];
    then
    	su fusion -c "sh -c '${UPGRADE_AGENT_DIR}/SCRIPT/Q_Backup ${UPGRADE_AGENT_DIR} ${AGENT_DIR}'"
    	chown nbiman:nbiadmin ${AGENT_DIR}/TMF*.tar > /dev/null 2>&1
    else
    su ${AGENT_USER} -c "sh -c '${AGENT_DIR}/SCRIPT/Q_Backup ${UPGRADE_AGENT_DIR} ${AGENT_DIR}'"
    fi

    ### HN42391 - HN42393 - HN42395
	ErrExit $? "DBBackup Failed"

   ${my_o} ""
    ${my_o} "Taknig Bakup of Users: please wait...\c"
    if [ ${upgrade_agent_tmf_version} \> MINOR_UPGRADE_VER -a ${upgrade_agent_tmf_version} \< MAJOR_UPGRADE_VER_1 ];
    then
    	su fusion -c "sh -c '${UPGRADE_AGENT_DIR}/SCRIPT/Q_Userbackup ${UPGRADE_AGENT_DIR} ${AGENT_DIR}'"
    	chown nbiman:nbiadmin ${AGENT_DIR}/USERS*.tar > /dev/null 2>&1
    else
    su ${AGENT_USER} -c "sh -c '${AGENT_DIR}/SCRIPT/Q_Userbackup ${UPGRADE_AGENT_DIR} ${AGENT_DIR}'"
    fi
	### HN42391 - HN42393 - HN42395
	ErrExit $? "Userbackup Failed"

 #Restore DB from earlier version

  ${my_o} ""
    ${my_o} "Restoring Databse : please wait...\c"
    su ${AGENT_USER} -c "sh -c '${AGENT_DIR}/SCRIPT/Q_Restore ${AGENT_DIR}/TMF*.tar'"
	### HN42391 - HN42393 - HN42395
	ErrExit $? "Restoring Databse Failed"

  #Restore Users from earlier version
  ${my_o} ""
    ${my_o} "Restoring USERS : please wait...\c"
    su ${AGENT_USER} -c "sh -c '${AGENT_DIR}/SCRIPT/Q_Userrestore ${AGENT_DIR}/USERS*.tar'"
	### HN42391 - HN42393 - HN42395
	ErrExit $? "Restoring USERS Failed"
  ${my_o} "Removing existing backup files from ${AGENT_DIR}"
   rm ${AGENT_DIR}/*.tar

  #############  end of TR HN42379

 #Start the Agnet
 #${my_o} ""
   #${my_o} "Application is starting: please wait...\c"
   #su ${AGENT_USER} -c "sh -c '${AGENT_DIR}/SCRIPT/Q_Start'"

 AGENT_DIR=${UPGRADE_AGENT_DIR}
 ${my_o} ""
   ${my_o} "Agent Directory is ${AGENT_DIR}...\c"

 #Stop the old version of Agent
 #${my_o} ""
  ######## new script for CR377 HN42394
     answer=""
	  while [ "${answer}" != "y" -a "${answer}" != "n" ];
	  do
	    ${my_o} "Are you sure to stop OLD agent? [y/n][default: y]: \c"
	    read answer
	    if [ "${answer}" = "" ];
	    then
	      answer="y"
	    fi
	  done
	  if [ "${answer}" = "y" ];
  	  then
   ${my_o} "Stopping ${agent_remove} : please wait...\c"
   su ${AGENT_USER} -c "sh -c '${UPGRADE_AGENT_DIR}/SCRIPT/Q_Stop'"
      else
         ${my_o} "Stop old agent manually after installation"
  	  fi
     ############


 # Remove the old version of Agent
 #. "${AGENT_USER_HOME}/${USER_ENV_DIR}/${agent_remove}.env"
 #${my_o} "Removing ${agent_remove} : please wait...\c"
 #DeleteAgent ${agent_remove}
 ## HN42395
 ${my_o} "The upgrade has been done successfully"
 return 0
}

MainMenu()
{
# fix for the TR HO43323 -- now the Upgrade procedure is done manually, so the code related to upgrade procedure is commented
  ${my_o} ""
  ${my_o} "================  M E N U  ===============================\n"
  ${my_o} " 1. Install ${AGENT_TITLE} ${AGENT_TYPE}\n"
  # ${my_o} " 2. Upgrade ${AGENT_TITLE} ${AGENT_TYPE}\n"
  ${my_o} " 2. Remove  ${AGENT_TITLE} ${AGENT_TYPE}\n"
  ${my_o} " 3. Exit\n"
  ${my_o} "===========================================================\n"

  while [ "$menu_mode" -lt 1 -o "$menu_mode" -gt 3 ]
  do
    ${my_o} "Please, enter your selection [1..3][default 3]: \c"
    read l_var
    if [ "${l_var}" = "" ];
    then
      l_var="3"
    fi
    if isNumeric "${l_var}"
    then
      menu_mode=${l_var}
    fi
  done

  ${my_o} ""

  case ${menu_mode} in
    '1')
       AgentInstall
       ;;
	#'2')
	#	AgentUpgrade
	#	;;
	'2')
       AgentRemove
       ;;
    *)
       ${my_o} "${Exiting}"
       ExitVal="${Success}"
       exit 0
       ;;
   esac

  return 0
}

main()
{
  Welcome
  CheckRootUser
  CheckOS
  MainMenu

  return 0
}

main

ExitVal="${Success}"

exit 0
