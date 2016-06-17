#!/bin/sh

Success="0"
Failure="1"
ExitVal="${Failure}"

typeset -i menu_mode=0
FULLNAME=""
AGENT_DIR=""
AGENT_USER_HOME=""
AGENT_USER_HOME_DEF=""
ENV_FILE=""
postfix=""
curr_host=$(hostname | cut -d '.' -f 1)
home_inst=$(pwd)
chmod +w "${home_inst}" > /dev/null 2>&1
JRE=0
REM_PATCH=""

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
  JAVA_DEF="/opt/java1.5/"
  com_size="ls -s --block-size=512"
  ext_os="${curr_os}"
  if [ -e "/etc/SuSE-release" ]
  then
    LIN_VER=$(cat "/etc/SuSE-release" | grep VERSION | cut -d "=" -f 2)
    if [ "${LIN_VER}" -ge "10" ];
    then
      ext_os="${curr_os}_10"
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

    fi
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
  ${my_o} "######################################"
  ${my_o} ""
  ${my_o} "         ${VENDOR}"
  ${my_o} ""
  ${my_o} "      ${AGENT_TITLE}"
  ${my_o} "                 ${AGENT_TYPE}"
  ${my_o} ""
  ${my_o} "######################################"
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

CheckAgentEnv()
{
  ENV_FILE="${AGENT_USER_HOME}"/${USER_ENV_DIR}/${AGENT_VER}.env
  if [ ! -e $"{ENV_FILE}" ];
  then
    ${my_o} "The environment file ${ENV_FILE}"
    ${my_o} "doesn't exist"
    ${my_o} "ERROR"
    ${my_o} "${Exiting}"
    exit 1
  fi

  if [ -r ${ENV_FILE} ];
  then
    . ${ENV_FILE}
  else
    ${my_o} "ERROR: missing or unreadable environment file ${ENV_FILE}"
    ${my_o} "Check your istallation"
    ${my_o} "${ExitSetUp}"
    exit 1
  fi

  #
  # Calcolo la home directory dell' AGENT_USER
  #
  user_home_agent=`fgrep "${AGENT_USER}:" /etc/passwd | awk 'BEGIN { FS = ":"} {print $6}'`
  if [ "${user_home_agent}" = "" ];
  then
    ${my_o} ""
    ${my_o} "Cannot find home directory for ${AGENT_USER} Agent user"
    ${my_o} "${ExitSetUp}"
    exit 1
  fi

  #
  # Esporto la home directory dell'Agent User
  #
  export user_home_agent

  return
}

CheckInstEnvFile()
{
  ENV_FILE="${AGENT_USER_HOME}"/${USER_ENV_DIR}/${AGENT_VER}.env
  if [ ! -e "${ENV_FILE}" ];
  then
    ${my_o} "The environment file ${ENV_FILE}"
    ${my_o} "doesn't exist"
    ${my_o} "ERROR"
    ${my_o} "${Exiting}"
    exit 1
  fi

  if [ -r ${ENV_FILE} ];
  then
    . ${ENV_FILE}
  else
    ${my_o} "ERROR: missing or unreadable environment file ${ENV_FILE}"
    ${my_o} "Check your istallation"
    ${my_o} "${ExitSetUp}"
    exit 1
  fi

  return
}

CheckTarInst()
{
  old_dir=$(pwd)

  cd ${home_inst}

  ${my_o} ""
  ${my_o} "Checking ${PATCH_AGENT_TAR}.gz....."
  if [ -e ${PATCH_AGENT_TAR}.gz ];
  then
    UNCOMP_SIZE=$(${MY_ZIP} -l ${PATCH_AGENT_TAR}.gz | grep ${PATCH_AGENT_TAR} | awk '{print $2}')
    tmp_dir=$(pwd)
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
    else
      ${my_o} "Free space is enough"
    fi
    ${my_o} "\nUncompressing ${PATCH_AGENT_TAR}.gz, wait please..."
    ${MY_ZIP} -d ${PATCH_AGENT_TAR}.gz > /dev/null 2>&1
    if [ $? != 0 ];
    then
      ${my_o} "..ERROR"
      ${my_o} "${Contact}"
      ${my_o} "${Exiting}"
      exit 1
    fi
  else
    ${my_o} "The file ${PATCH_AGENT_TAR}.gz is not found"
    ${my_o} "Check your installation package"
    ${my_o} "${Exiting}"
    exit 1
  fi

  AGENT_TOBEPATCHED_DIR=${AGENT_DIR}

  TAR_SIZE=$(${com_size} ${PATCH_AGENT_TAR} | awk '{print $1}')
  let "TAR_SIZE=${TAR_SIZE}/2"
  let "TAR_SIZE=${TAR_SIZE} + ${RESTART_DB_SIZE}"
  FREE_SIZE=$(df -l -k -P "${AGENT_DIR}" | grep -vi "filesystem" | awk '{if(NF > 5) print $4; else print $3}')
  if [ ${FREE_SIZE} -lt ${TAR_SIZE} ];
  then
    ${my_o} "You have not enough space to extract the tar file ${AGENT_TAR}"
    ${my_o} "You need ${TAR_SIZE} Kb of free space in ${AGENT_TOBEPATCHED_DIR} repository"
    ${my_o} "but you have only ${FREE_SIZE} Kb of free space in this repository"
    ${my_o} "${Contact}"
    ${my_o} "${Exiting}"
    exit 1
  fi

  ${my_o} "..OK"

  cd ${old_dir}

  return 0
}

CheckPatchToBeRemoved()
{
  cd "${AGENT_DIR}"

  inst_list=""
  typeset -i j=0

  if [ -d "./PATCH" ];
  then
    cd "${AGENT_DIR}"/PATCH
  fi

  temp_inst_list=$(find . -type d -name "SAVED_FOR_P*" -exec basename {} \; | cut -c 12- |sort -r)
  typeset -i tot_inst=0
  for item in ${temp_inst_list};
  do
    inst_list[${tot_inst}]=${item}
    let "tot_inst=$tot_inst + 1"
  done

    if [ ${tot_inst} = 0 ];
    then
      ${my_o} "There isn't ${AGENT_TITLE} patch installed"
      ${my_o} "Exiting..."
      exit 0
    fi
    if [ ${tot_inst} = 1 ];
    then
      rem_inst=${inst_list}
    else
      ${my_o} "=================================================================="
      ${my_o} " List of the ${AGENT_TITLE} patch installed"
      ${my_o} "=================================================================="
      ${my_o} ""

      typeset -i inst_count=0
      typeset -i idx=1
#      ${my_o} "  0. Exit"
      while [ ${inst_count} -lt ${tot_inst} ];
      do
        new_item=${inst_list[${inst_count}]}
        ${my_o} "  ${idx}. PATCH ${new_item}"
        let "inst_count=${inst_count} + 1"
        let "idx=${idx} + 1"
      done
      ${my_o} ""
      ${my_o} "=================================================================="
      ${my_o} " List of the removable Patch"
      ${my_o} "=================================================================="
      ${my_o} ""
      ${my_o} "  1. PATCH ${inst_list[0]}"
      ${my_o} "  2. Exit"
      ${my_o} ""

      typeset -i sel_inst=0
      while [ "${sel_inst}" -lt 1 -o "${sel_inst}" -gt 2 ]
      do
        ${my_o} "Make your selection, please [1..2][Default = 2]: \c"
        read l_var

        if [ "${l_var}" = "" ];
        then
          l_var=2
        fi
        if [ "${l_var}" = "2" ];
	then
	    ${my_o} "${Exiting}"
	    exit 0
	fi
        if isNumeric "${l_var}"
        then
          sel_inst=${l_var}
        fi
      done
      let "sel_inst=${sel_inst} - 1"
      rem_inst=${inst_list[${sel_inst}]}
    fi
    new_item=${rem_inst}

  answer=""
  while [ "${answer}" != "y" -a "${answer}" != "n" ]
  do
    ${my_o} ""
    ${my_o} "You have selected the patch ${new_item}"
    ${my_o} "Are you sure to remove this patch? [y/n] [default: y]: \c"
    read answer
    ${my_o} ""
    if [ "${answer}" = "" ];
    then
      answer="y"
    fi
    if [ "${answer}" = "n" ];
    then
      CheckPatchToBeRemoved
    fi
  done

  REM_PATCH="${new_item}"
  export REM_PATCH

  cd "${home_inst}"

  return 0
}

RemoveP()
{
  if [ "${REM_PATCH}" = "" ];
  then
    return 0
  fi

  cd "${AGENT_DIR}"
  PATCH_NUMBER=${REM_PATCH}

  PATCH_ROOT_DIR="PATCH/SAVED_FOR_P${PATCH_NUMBER}"

  if [ ! -d ./${PATCH_ROOT_DIR} ]
  then
    return 0
  fi

  ${my_o} ""
  ${my_o} "Removing patch ${PATCH_NUMBER} ..."

  rm -f ${AGENT_DIR}/LIB/*
  rm -f ${AGENT_DIR}/CONF/*
  rm -f ${AGENT_DIR}/SCRIPT/Q_Backup
  rm -f ${AGENT_DIR}/SCRIPT/Q_Restore
  rm -f ${AGENT_DIR}/SCRIPT/SetupUser854.sh

  cp -f -p -R ./${PATCH_ROOT_DIR}/* ${AGENT_DIR}

  cd "${AGENT_DIR}"/PATCH
  rm -fR ./SAVED_FOR_P${PATCH_NUMBER} > /dev/null 2>&1
  cd "${AGENT_DIR}"
  ${my_o} "Removed patch ${PATCH_NUMBER}"
  ${my_o} ""

  dd=$(date "+%m/%d/%y %H:%M:%S")
  ${my_o} "${PATCH_NUMBER} removed on ${dd}" >> ./INFO/.patch_installed
  cat /dev/null > ./INFO/.patch
  rm -f ./INFO/PATCH.README.*

  cd "${home_inst}"
  ${my_o} "Done successfully"

  return 0
}

PatchRemove()
{
  ${my_o} ""
  ${my_o} "Checking for ${AGENT_TYPE} installed..."
  ${my_o} ""

  SelectAgentToBePatched "remove"
  #CheckAgentEnv
  CheckInstEnvFile
  CheckPatchToBeRemoved
  CheckRunningApp ${AGENT_VER}
  RemoveP

  return 0
}

PatchInstall()
{
  ${my_o} ""
  ${my_o} "Checking for ${AGENT_TYPE} installed..."
  ${my_o} ""

  SelectAgentToBePatched "install"
  #CheckAgentEnv
  CheckInstEnvFile
  CheckPatchAlreadyInstalled
  CheckRunningApp ${AGENT_VER}
  Install
  #CheckRSIFileUFF
  #CheckSGFileUFF

  UpdateConf
  UpdateRights

  return 0
}

SelectAgentToBePatched()
{
  mode_sel="$1"

  ${my_o} ""

  id -u ${AGENT_USER} > /dev/null 2>&1
  if [ $? = 0 ];
  then
    AGENT_USER_HOME=$(fgrep "${AGENT_USER}:" /etc/passwd | awk 'BEGIN { FS = ":"} {print $6}')
    if [ "${AGENT_USER_HOME}" = "" ];
    then
      exit 1
    fi

    tmp_list=$(ls "${AGENT_USER_HOME}/${USER_ENV_DIR}/"*.env 2>/dev/null)

    inst_list=""

    for item in ${tmp_list}
    do
      agent_type=$(fgrep "AGENT_TYPE=" ${item} | awk 'BEGIN {FS = "="} {print $2}')

      if [ "${agent_type}" = "\"${AGENT_TYPE}\"" -o "${agent_type}" = "${AGENT_TYPE}" ];
      then
        mydir=$(fgrep "AGENT_DIR=" ${item} |cut -d '=' -f 2 | cut -d '"' -f 2)
        if [ -r "${mydir}/INFO/.release" ];
        then
          cur_ver=$(cat "${mydir}/INFO/.release")
          if [ "${cur_ver}" = "${VERSION}" ];
          then
            tmp_item=$(basename ${item} .env)
            tmp_inst_list="${tmp_inst_list} ${item}"
          fi
        fi
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
      ${my_o} "There isn't ${AGENT_TITLE} application installed"
      return 0
    fi
    if [ ${tot_inst} = 1 ];
    then
      rem_inst=${inst_list}
    else
      ${my_o} "=================================================================="
      ${my_o} " List of the ${AGENT_TITLE} applications installed"
      ${my_o} "=================================================================="
      ${my_o} ""

      typeset -i inst_count=0
      typeset -i idx=1
      ${my_o} "  0. Exit"
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
        if [ "${l_var}" = "0" ];
	then
	    ${my_o} "${Exiting}"
	    exit 0
	fi

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
    ${my_o} "There isn't ${AGENT_TITLE} version ${VERSION} application installed"
    return 0
  fi

  answer=""
  while [ "${answer}" != "y" -a "${answer}" != "n" ]
  do
    ${my_o} ""
    ${my_o} "You have selected the ${new_item} application"
    ${my_o} "Are you sure to ${mode_sel} patch for it? [y/n] [default: y]: \c"
    read answer
    ${my_o} ""
    if [ "${answer}" = "" ];
    then
      answer="y"
    fi
    if [ "${answer}" = "n" ];
    then
      SelectAgentToBePatched "${mode_sel}"
    fi
  done

  AGENT_VER="${new_item}"

  export AGENT_VER

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

  if [ -x ${home_inst}/ckproc ];
  then

    for item in ${PROC_LIST_STOP};
    do

      ${my_o} "\nEvaluating $item for ${AGENT_DIR}...\n"

      res=$(${home_inst}/ckproc -f ${AGENT_DIR}/${item})

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

CheckPatchAlreadyInstalled()
{
  cd "${AGENT_DIR}"
  PATCH_ROOT_DIR="PATCH/SAVED_FOR_P${PATCH_NUMBER}"

  if [ -d ./${PATCH_ROOT_DIR} ]
  then
    ${my_o} ""
    ${my_o} "Warning: Patch ${PATCH_NUMBER} already installed\n"
    ${my_o} "${Exiting}"
    ${my_o} ""
    cd "${home_inst}"
    exit 1
  fi

  if [ -d "./PATCH" ];
  then
    cd "${AGENT_DIR}"/PATCH
  fi

  file_list=$(find . -type d -name "SAVED_FOR_P*" -print)

  cd "${AGENT_DIR}"

  for item in ${file_list};
  do
    new_item=$(basename ${item} | cut -c 12- )
    if [ "${new_item}" -ge "${PATCH_NUMBER}" ];
    then
      ${my_o} ""
      if [ "${new_item}" -eq "${PATCH_NUMBER}" ];
      then
        ${my_o} "WARNING: Patch ${PATCH_NUMBER} is already installed\n"
      else
        ${my_o} "WARNING: There is a newer Patch ${new_item} already installed\n"
      fi
      ${my_o} "${Exiting}"
      ${my_o} ""
      cd "${home_inst}"
      exit 1
    fi
  done

  cd "${home_inst}"

  return 0
}

Install()
{
    cd "${AGENT_DIR}"

##    AGENT_TOBEPATCHED_DIR="~${AGENT_USER}/${AGENT_VER}"
##    export AGENT_TOBEPATCHED_DIR

##    PWD=$(pwd)
##    ${my_o} $PWD
###
# In base al contenuto della patch salvo le
# directories necessarie.
##
    if [ ! -e ${home_inst}/.patch_contents ]
    then
	${my_o} "\nCannot find patch contents, exiting patch installation procedure."
	exit 1
    fi

    ${my_o} "Backup: Start....\n"

    PATCH_ROOT_DIR="PATCH/SAVED_FOR_P${PATCH_NUMBER}"
    PATCH_CONTENTS="${home_inst}/.patch_contents"

    if [ ! -d ./${PATCH_ROOT_DIR} ]
    then
      mkdir -p ./${PATCH_ROOT_DIR}
      chown -R ${AGENT_USER}:${AGENT_GROUP} "${PATCH_ROOT_DIR}" > /dev/null 2>&1
      chmod -R 755 "${PATCH_ROOT_DIR}"/* > /dev/null 2>&1
    fi

    chown  ${AGENT_USER}:${AGENT_GROUP} "${AGENT_DIR}"/PATCH > /dev/null 2>&1
    chmod  755 "${AGENT_DIR}"/PATCH > /dev/null 2>&1

##    ${my_o} $DIR_APPENDIX
##    ${my_o} $PATCH_CONTENTS

    if [ -d "./LIB" ];
    then
      cp -p -R ./LIB ./${PATCH_ROOT_DIR}
    fi

	if [ -d "./CONF" ];
    then
      cp -p -R ./CONF ./${PATCH_ROOT_DIR}
    fi

    if [ -e "./abcd.$$" ];
    then
      rm -f ./abcd.$$ > /dev/null 2>&1
    fi

    cat ${PATCH_CONTENTS} | awk '{print length($1), $1}' | sort -r | cut -d ' ' -f 2- > ./abcd.$$

    #CONTENTS=$(cat ${PATCH_CONTENTS})
    CONTENTS=$(cat ./abcd.$$)
##    ${my_o} $CONTENTS
    for item in ${CONTENTS}; do
	DIR_NAME=`dirname ${item}`
	FILE_NAME=`basename ${item}`

	BACKUP_DIR_NAME=${PATCH_ROOT_DIR}/${DIR_NAME}

##	  ${my_o} $DIR_NAME
##	  ${my_o} $FILE_NAME
##	  ${my_o} $BACKUP_DIR_NAME
##	  ${my_o} $item

	if [ ! -d ./$BACKUP_DIR_NAME ]
	then
	  mkdir -p ./$BACKUP_DIR_NAME
          chown -R ${AGENT_USER}:${AGENT_GROUP} "${BACKUP_DIR_NAME}" > /dev/null 2>&1
          chmod -R 755 "${BACKUP_DIR_NAME}"/* > /dev/null 2>&1
	fi

	${my_o} "Backup of $item in $BACKUP_DIR_NAME"
	if [ -e "./${item}" ];
        then
	  mv ./${item} ./${BACKUP_DIR_NAME}
        else
          suff=$(echo ${item} | awk 'BEGIN { FS = "-"} {print $NF}')
          pref=$(basename ${item} ${suff})
          mv ./${DIR_NAME}/${pref}* ./$BACKUP_DIR_NAME > /dev/null 2>&1

##        mi serve per copiare anche i file che erano .uff
	  new_item_uff=$(basename ${item} .uff)
	  if [ -e "./${DIR_NAME}/${new_item_uff}" ];
	  then
	    ${my_o} "Backup of ${DIR_NAME}/${new_item_uff} in $BACKUP_DIR_NAME"
	    cp -p ./${DIR_NAME}/${new_item_uff} ./$BACKUP_DIR_NAME
          fi
        fi
    done

    if [ -e "./abcd.$$" ];
    then
      rm -f ./abcd.$$ > /dev/null 2>&1
    fi

    if [ -d "./CONF" ];
    then
      cp -p -R ./CONF ./${PATCH_ROOT_DIR}
    fi

    ### TR HO63496
       BACKUP_DIR_NAME=${PATCH_ROOT_DIR}/SCRIPT
       echo "Backup of Q_Backup and Q_Restore to " ${BACKUP_DIR_NAME}
       if [ ! -d ./$BACKUP_DIR_NAME ]
	     then
	       mkdir -p ./$BACKUP_DIR_NAME
          chown -R ${AGENT_USER}:${AGENT_GROUP} "${BACKUP_DIR_NAME}" > /dev/null 2>&1
          chmod -R 755 "${BACKUP_DIR_NAME}" > /dev/null 2>&1
	   fi

         cp -p -R ./SCRIPT/Q_Backup ./$BACKUP_DIR_NAME
         cp -p -R ./SCRIPT/Q_Restore ./$BACKUP_DIR_NAME
         grep -iv cur_dir ./SCRIPT/Q_Backup > ./SCRIPT/Q_Backup_new
         grep -iv cur_dir ./SCRIPT/Q_Restore > ./SCRIPT/Q_Restore_new
         mv ./SCRIPT/Q_Backup_new ./SCRIPT/Q_Backup
         mv ./SCRIPT/Q_Restore_new ./SCRIPT/Q_Restore
         updatePermisions

         echo "backup done of" ${AGENT_DIR} "/Q_Backup" "Q_Restore"

    ${my_o} "\nBackup: Done."

    CheckTarInst

    ${my_o} "\nExtracting files from archive ${PATCH_AGENT_TAR}, wait please..."

    tar -xf "${home_inst}/${PATCH_AGENT_TAR}"
    if [ $? != 0 ];
    then
      ${my_o} "..ERROR"
      ${my_o} "${Contact}"
      ${my_o} "${Exiting}"
      exit 1
    fi
    rm -f ${home_inst}/${PATCH_AGENT_TAR}

    dd=$(date "+%m/%d/%y %H:%M:%S")
    ${my_o} "${PATCH_NUMBER} on ${dd}" >> ./INFO/.patch_installed
    chown  ${AGENT_USER}:${AGENT_GROUP} ./INFO/.patch_installed > /dev/null 2>&1
    chmod  644 ./INFO/.patch_installed > /dev/null 2>&1
##cat $home_inst/.patch >> INFO/.patch_installed
    mv -f $home_inst/.patch INFO/.patch
    chown  ${AGENT_USER}:${AGENT_GROUP} ./INFO/.patch > /dev/null 2>&1
    chmod  644 ./INFO/.patch > /dev/null 2>&1
    mv -f ${PATCH_CONTENTS} INFO/.patch_contents
    chown  ${AGENT_USER}:${AGENT_GROUP} ./INFO/.patch_contents > /dev/null 2>&1
    chmod  644 ./INFO/.patch_contents > /dev/null 2>&1
    mv -f ${home_inst}/PATCH.README.* ./INFO
    chown  ${AGENT_USER}:${AGENT_GROUP} ./INFO/PATCH.README.* > /dev/null 2>&1
    chmod  644 ./INFO/PATCH.README.* > /dev/null 2>&1

    cd "${home_inst}"
    ${my_o} "Done successfully"

    return 0
}

CheckRSIFileUFF()
{
  if [ -e "${AGENT_DIR}/SCRIPT/RSI_Setup" ];
  then
    UpdateRSIFileUFF
    RenameRSIFileUFF
  else
    RemoveRSIScriptFile
  fi

  return 0
}

RemoveRSIScriptFile()
{
  rsi_file_list=$(find ${AGENT_DIR}/SCRIPT -type f -name "RSI_*" -print)

  for item in ${rsi_file_list};
  do
    rm -f "${item}" > /dev/null  2>&1
  done

  return 0
}

UpdateRSIFileUFF()
{
  rsi_file_list=$(find ${AGENT_DIR} -type f -name "RSI*.uff" -print)
  export rsi_file_list

  ENV_FILE="${user_home_agent}"/${USER_ENV_DIR}/${AGENT_VER}.env
  ENV_FILE1=$(${my_o} ${ENV_FILE} | sed -e "s?\/?\\\/?g")

  FULLNAME="${AGENT_VER}"

  for item in ${rsi_file_list};
  do
    ${my_o} ""
    ${my_o} "Updating template ${item}"

    ex << ++ ${item} > /dev/null
      g/FULLNAME/s//${FULLNAME}/g
      g/ENV_FILE/s//${ENV_FILE1}/g
      g/AGENT_USER/s//${AGENT_USER}/g
    w!
++
    ${my_o} "Successfully done"
  done

  ${my_o} ""

  return 0
}

RenameRSIFileUFF()
{
  for item in ${rsi_file_list};
  do
    new_dir=$(dirname ${item})
    new_item=$(basename ${item} .uff)
    mv ${item} ${new_dir}/${new_item} > /dev/null  2>&1
  done

  return 0
}

CheckSGFileUFF()
{
  if [ -e "${AGENT_DIR}/SCRIPT/updateSG.uff" ];
  then
    UpdateSGFileUFF
    RenameSGFileUFF
  fi

  return 0
}

UpdateSGFileUFF()
{
  sg_file_list=$(find ${AGENT_DIR} -type f -name "updateSG.uff" -print)
  export sg_file_list

  ENV_FILE="${user_home_agent}"/${USER_ENV_DIR}/${AGENT_VER}.env
  ENV_FILE1=$(${my_o} ${ENV_FILE} | sed -e "s?\/?\\\/?g")

  FULLNAME="${AGENT_VER}"

  for item in ${sg_file_list};
  do
    ${my_o} ""
    ${my_o} "Updating template ${item}"

    ex << ++ ${item} > /dev/null
      g/FULLNAME/s//${FULLNAME}/g
      g/ENV_FILE/s//${ENV_FILE1}/g
      g/AGENT_USER/s//${AGENT_USER}/g
      g/AGENT_GROUP/s//${AGENT_GROUP}/g
      g/AGENT_UID/s//${AGENT_UID}/g
      g/AGENT_GID/s//${AGENT_GID}/g
      g/USER_ENV_DIR/s//${USER_ENV_DIR}/g
      g/USER_CONF_DIR/s//${USER_CONF_DIR}/g
      g/USER_CONF_PORT_FILE/s//${USER_CONF_PORT_FILE}/g
    w!
++
    ${my_o} "Successfully done"
  done

  ${my_o} ""

  return 0
}

RenameSGFileUFF()
{
  for item in ${sg_file_list};
  do
    new_dir=$(dirname ${item})
    new_item=$(basename ${item} .uff)
    mv ${item} ${new_dir}/${new_item} > /dev/null  2>&1
  done

  return 0
}

RemoveConfLine()
{
  CONF_FILE="$1"
  exten_key="$2"

  if [ -r "${CONF_FILE}" ];
  then
      if [ -e "${CONF_FILE}".$$ ];
      then
        rm -f "${CONF_FILE}".$$
      fi

      cat "${CONF_FILE}" | while
        read LINE;
        do
        tmp_str=$(echo "${LINE}" |grep "${exten_key}")
          if [ "${tmp_str}" = "" ];
          then
                        echo $LINE >> ${CONF_FILE}.$$
          fi
      done
      cp ${CONF_FILE}.$$ ${CONF_FILE}
      rm -f ${CONF_FILE}.$$
    fi

  return 0
}


AddConfLine()
{
  CONF_FILE="$1"
  exten_key="$2"
  exten_val="$3"
  exten_pre="$4"
  add_bline="$5"

  exten_orig="${exten_key}=${exten_val}"

  if [ -r "${CONF_FILE}" ];
  then
    exten=$(grep "${exten_key}=" ${CONF_FILE})

    if [ "${exten}" = "" ];
    then
      exten=$(cat ${CONF_FILE})

      if [ -e "${CONF_FILE}".$$ ];
      then
        rm -f "${CONF_FILE}".$$
      fi

      last_ext="0"
      found_ext="0"
      cat "${CONF_FILE}" | while
        read LINE;
        do
        tmp_str=$(echo "${LINE}" |grep "${exten_pre}")
        if [ "${found_ext}" = "0" ];
        then
          if [ ! "${tmp_str}" = "" ];
          then
            found_ext="1"
          fi
        else
          if [ "${last_ext}" = "0" ];
          then
            if [ "${tmp_str}" = "" ];
            then
              last_ext="1"
              if [ "${add_bline}" = "1" ];
              then
                echo " " >> ${CONF_FILE}.$$
              fi
              echo "${exten_orig}" >> ${CONF_FILE}.$$
            fi
          fi
        fi
        echo $LINE >> ${CONF_FILE}.$$
      done

      exten=$(grep "${exten_key}=" ${CONF_FILE}.$$)
      if [ "${exten}" = "" ];
      then
        if [ "${add_bline}" = "1" ];
        then
          echo " " >> ${CONF_FILE}.$$
        fi
        echo "${exten_orig}" >> ${CONF_FILE}.$$
      fi

      cp ${CONF_FILE}.$$ ${CONF_FILE}

      rm -f ${CONF_FILE}.$$
    fi
  fi

  return 0
}

UpdateConf()
{
  ${my_o} ""
  ${my_o} "Updating config ..."

CONF_FILE="${AGENT_DIR}/CONF/Application.properties"
trueLicFile=$(echo "${AGENT_DIR}/CONF/License.xml" | sed -e "s?\/?\\\/?g")

ex << ++ "${CONF_FILE}" > /dev/null
g/^LicenseManager.licenseFile/s/LicenseManager.licenseFile.*/LicenseManager.licenseFile = ${trueLicFile}/
w!
++


CONF_FILE="${AGENT_DIR}/SCRIPT/Q_Start"

ex << ++ ${CONF_FILE} > /dev/null
g/{cleanNewFile}/s/{cleanNewFile}/{trueLicFile}/
w!
++

CONF_FILE="${AGENT_DIR}/SCRIPT/Update_Lic"
ex << ++ "${CONF_FILE}" > /dev/null
g/{cleanNewFile}/s/{cleanNewFile}/{trueLicFile}/
w!
++


CONF_FILE="${AGENT_DIR}/CONF"
file_list=$(ls -1d "${CONF_FILE}/"*)
for item in ${file_list};
do
if [ -e "${item}".$$ ];
then
rm -f "${item}".$$
fi
cat ${item} | tr -d "\r" > ${item}.$$
cat ${item}.$$ > "$item"
rm -f "${item}".$$
done

#WSON properties


#CR 16 WSON Alarms
AddConfLine "${AGENT_DIR}/CONF/I38Plugin.properties" "PlugIn.MLRA.LinkUpDown" "alarm : { notificationId '', alarmIdentifier '',  objectName emsDn : { ems '' }, nativeEMSName '', objectType ot_aid, emsTime '',  neTime '', isClearable true, perceivedSeverity ps_warning, assignedSeverity as_warning, layerRate 1, acknowledgeIndication ai_event_unacknowledged, correlatedAlarmIdentifiers {},  probableCause 'EMS',  nativeProbableCause '', probableCauseQualifier 'MLRA Communication Loss', serviceAffecting sa_non_service_affecting, affectedTPList {}, additionalText '', x733Info {}, rcaiIndicator false  } " "com.marconi.fusion.tmf.i38PlugIn.sbi.NMAlarmProcessor.threshold" "0"
AddConfLine "${AGENT_DIR}/CONF/I38Plugin.properties" "PlugIn.MLRA.ControlChannel.CCUpDown" "alarm : { notificationId '', alarmIdentifier '', objectName emsDn : { ems '' }, nativeEMSName '', objectType ot_aid,emsTime '', neTime '', isClearable true, perceivedSeverity ps_warning, assignedSeverity as_warning, layerRate 1, acknowledgeIndication ai_event_unacknowledged,  correlatedAlarmIdentifiers {}, probableCause 'TCF', nativeProbableCause '', probableCauseQualifier 'CC Communication Loss', serviceAffecting sa_non_service_affecting, affectedTPList {}, additionalText '',  x733Info {}, rcaiIndicator false } " "com.marconi.fusion.tmf.i38PlugIn.sbi.NMAlarmProcessor.threshold" "7"
AddConfLine "${AGENT_DIR}/CONF/I38Plugin.properties" "PlugIn.MLRA.LinkCluster.CCUpDown" "alarm : {  notificationId '',  alarmIdentifier '',  objectName emsDn : { ems '' },  nativeEMSName '', objectType ot_aid, emsTime '', neTime '', isClearable true, perceivedSeverity ps_warning, assignedSeverity as_warning, layerRate 1, acknowledgeIndication ai_event_unacknowledged, correlatedAlarmIdentifiers {}, probableCause 'TCF',  nativeProbableCause '',  probableCauseQualifier 'LK Communication Loss of Resilence',  serviceAffecting sa_non_service_affecting,  affectedTPList {}, additionalText '',  x733Info {},  rcaiIndicator false  } " "com.marconi.fusion.tmf.i38PlugIn.sbi.NMAlarmProcessor.threshold" "13"

AddConfLine "${AGENT_DIR}/CONF/I36Plugin.properties" "PlugIn.meGranularity" "100" "PlugIn.realignment.fastRealignment" "0"
AddConfLine "${AGENT_DIR}/CONF/I36Plugin.properties" "PlugIn.SBI.setAlarmTpIndex" "false" "PlugIn.realignment.berHome" "0"

# Fix for TR HO56956,HO58289 - Removing the exsiting property(RemoveConfLine) and adding again the same property.
RemoveConfLine "${AGENT_DIR}/CONF/I36Plugin.properties" "PlugIn.provisioning"
AddConfLine "${AGENT_DIR}/CONF/I36Plugin.properties" "PlugIn.provisioning" "{portLabel,linkState,modifiedNe,protectionChange,alarmResourceEnableState,modifiedNeAddress,modifiedNeName}" "PlugIn.provisioning.alarmRetrialInterval" "0"




# start P4

AddConfLine "${AGENT_DIR}/CONF/I38Profile.properties" "PlugIn.SBI.trailtraceAlwaysOnEM" "false" "PlugIn.I36.getTPFromDB" "0"
AddConfLine "${AGENT_DIR}/CONF/I36Plugin.properties" "PlugIn.performance.timeOffset24 " "0" "PlugIn.performance.forceLocation" "0"
AddConfLine "${AGENT_DIR}/CONF/I36Plugin.properties" "PlugIn.performance.timeOffset15 " "0" "PlugIn.performance.forceLocation" "0"

#end P4
# start P4 for trailtrace get set always on EM

 AddConfLine "${AGENT_DIR}/CONF/I38Profile.properties" "PlugIn.SBI.trailtraceAlwaysOnEM" "false" "PlugIn.I36.getTPFromDB" "0"

# end P4

		${my_o} "OK"
		${my_o} ""
    return 0
}

UpdateEndPointHttp()
{
  FILE="$1"
  SOURCE_COMMON="$2"
  DEST_COMMON="$3"

  if [ "${curr_os}" = "HP-UX" ];
  then
    sed "s|${SOURCE_COMMON}|${DEST_COMMON}|" $FILE > temp.txt
    mv temp.txt $FILE
  else
    sed  -i "s/${SOURCE_COMMON}/${DEST_COMMON}/g" $FILE
  fi

}

UpdateRights()
{
  cd ${AGENT_DIR}

  ${my_o} ""
  ${my_o} "Updating access rights for ${AGENT_DIR}..."

  chown -R ${AGENT_USER}:${AGENT_GROUP} ${AGENT_DIR}/LIB
  chmod -R 755 ${AGENT_DIR}/LIB/ > /dev/null 2>&1

  chown -R ${AGENT_USER}:${AGENT_GROUP} ${AGENT_DIR}/CONF
  chmod -R 755 ${AGENT_DIR}/CONF/ > /dev/null 2>&1

  find "${AGENT_DIR}/CONF" -type f -name "*.properties" -exec chown ${AGENT_USER}:${AGENT_GROUP} {} \;
  find "${AGENT_DIR}/CONF" -type f -name "*.xml" -exec chown ${AGENT_USER}:${AGENT_GROUP} {} \;
  find "${AGENT_DIR}/CONF" -type f -name "*.properties" -exec chmod 644 {} \;
  find "${AGENT_DIR}/CONF" -type f -name "*.xml" -exec chmod 755 {} \;

  #Provides correct permissions and group as these are changed during the installation of patch001
  chown root:${AGENT_GROUP} "${AGENT_DIR}/SCRIPT/Q_ResetDB"
  chmod -R 755 "${AGENT_DIR}/SCRIPT/Q_ResetDB"

  chown root:${AGENT_GROUP} "${AGENT_DIR}/SCRIPT/Q_CheckNMSDB"
  chmod -R 755 "${AGENT_DIR}/SCRIPT/Q_CheckNMSDB"

  chown root:${AGENT_GROUP} "${AGENT_DIR}/SCRIPT/installfd"
  chmod -R 755 "${AGENT_DIR}/SCRIPT/installfd"

  if [ ${AGENT_MODE} = "EM" ];
  then
  chown ${AGENT_USER}:${AGENT_GROUP} "${AGENT_DIR}/SCRIPT/SetupUser854.sh"
  chmod -R 755 "${AGENT_DIR}/SCRIPT/SetupUser854.sh"
  fi

  if [ ${AGENT_MODE} = "NM" ];
  then
   if [ -e "${AGENT_DIR}/SCRIPT/AsyncManager.sh" ];
    then
      chgrp nmc "${AGENT_DIR}/SCRIPT/AsyncManager.sh" > /dev/null 2>&1
      chmod g+s "${AGENT_DIR}/SCRIPT/AsyncManager.sh" > /dev/null 2>&1
    fi
  fi

  ${my_o} "Successfully done"

  ${my_o} "\nPatch $PATCH_NUMBER installed for agent $AGENT_TYPE"
  ${my_o} "$Exiting"

  cd "${home_inst}"

  return 0
}
 ###TR HO63496 updatePermissions added
updatePermisions()
{
  cd ${AGENT_DIR}
  echo "Updating access rights for ${AGENT_DIR}/SCRIPT/Q_Backup and Q_Restore..."
  chown -R ${AGENT_USER}:${AGENT_GROUP} ${AGENT_DIR}/SCRIPT/Q_Backup  ${AGENT_DIR}/SCRIPT/Q_Restore
  chmod -R 755 ${AGENT_DIR}/SCRIPT/Q_Backup  ${AGENT_DIR}/SCRIPT/Q_Restore  > /dev/null 2>&1
  return 0
}

MainMenu()
{
  if [ ! -e "${home_inst}"/.release ]
  then
      ${my_o} "\nCannot find version, exiting patch installation procedure."
      exit 1
  fi
  VERSION=$(cat "${home_inst}"/.release)

  if [ ! -e "${home_inst}"/.patch ]
  then
      ${my_o} "\nCannot find patch, exiting patch installation procedure."
      exit 1
  fi
  PATCH_NUMBER=$(cat "${home_inst}"/.patch)

  export VERSION
  export PATCH_NUMBER

  ${my_o} ""
  ${my_o} "================  M E N U  =================\n"
  ${my_o} " 1. Install patch ${PATCH_NUMBER} for ${AGENT_TYPE} ${VERSION}\n"
  ${my_o} " 2. Remove patch ${PATCH_NUMBER} for ${AGENT_TYPE} ${VERSION}\n"
  ${my_o} " 3. Exit\n"
  ${my_o} "============================================\n"

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
       PatchInstall
       ;;

    '2')
       PatchRemove
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
