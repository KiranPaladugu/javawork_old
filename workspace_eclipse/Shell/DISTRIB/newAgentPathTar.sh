#!/bin/sh

TAR_NAME="$1" 
MENU_MODE="$2"

curr_dir=$(`pwd`)

SOURCE_DIR=$(dirname ${curr_dir})

##############################
#
# TARGETS
#
##############################

DISTRIB_DIR="${curr_dir}"
echo DISTRIB_DIR=${DISTRIB_DIR}
DEPLOY_DIR="${SOURCE_DIR}/DEPLOY"

##############################
#
# COMMAND
#
##############################

RM="/bin/rm -f"
RMDIR="/bin/rm -rf"
CP="/bin/cp -r"
MV="/bin/mv -f"
STRIP="strip"
FIND="/bin/find"
MKDISTRIB="./copyFileSet"


UTIL_DIR="${SOURCE_DIR}/DISTRIB/UTIL"

##############################
#
# GENERAL
#
##############################

DATE="`/bin/date '+%y_%m_%d'`"
OS="created: `uname -sr`"
SIGNEXE="${SOURCE_DIR}/QNE_ETSI/KERNEL/sign -w" 
GETIDENT="sh -f ${UTIL_DIR}/getident"
MKDISTRIB="${UTIL_DIR}/copyFileSet"

###########################################
#
# CHIEDO IL NUMERO DI PATCH
#
###########################################


$GETTAG_EXE $REVISION_HOLDER_FILE



##############################
#
# TARGETS
#
##############################

TARGET="AGENT_ROOT"
TARGET_DIR="$DISTRIB_DIR/$TARGET"

echo "TARGET_DIR = $TARGET_DIR"

###############
#
# Get last release
#
############

GETTAG_EXE="${UTIL_DIR}/getTag"
REVISION_HOLDER_FILE="./RevisionHolder"

$GETTAG_EXE "${REVISION_HOLDER_FILE}"

FILE_VERSION="${DISTRIB_DIR}/.version"
FILE_DROP="${DISTRIB_DIR}/.drop"
FILE_RELEASE="${DISTRIB_DIR}/.release"
FILE_INTERNALBUILD="${DISTRIB_DIR}/.internalbuild"

if [ ! -a ${FILE_VERSION} ]
then
  echo "1" > ${FILE_VERSION}
fi

VERSION=`cat ${FILE_VERSION}`

echo "\nVersion Number (default ${VERSION}): \c"
read TMP

if [ "$TMP" <> "" ]
then
  VERSION=$TMP
fi

if [ ! -a ${FILE_DROP} ]
then
  echo "1" > ${FILE_DROP}
fi

DROP=`cat ${FILE_DROP}`

if [ "$DROP" = "" ]
then
  echo "\nDrop Number (0 for null): "
else
  echo "\nDrop Number (default ${DROP} - 0 for null): "
fi
read TMP

if [ "${TMP}" <> "" ]
then
  DROP=${TMP}
fi

if [ "${DROP}" = "0" ]
then
  DROP=""
fi

if [ "${DROP}" <> "" ]
then
  RELEASE="${VERSION}.${DROP}"
else
  RELEASE="${VERSION}"
fi

if [ ! -a "${FILE_INTERNALBUILD}" ]
then
  echo "1" > "${FILE_INTERNALBUILD}"
fi

INTERNALBUILD=`cat ${FILE_INTERNALBUILD}`

echo "Please specify internal build version if any : "
read TMP

  INTERNALBUILD=$TMP
  
  
echo "The release is <$RELEASE>"
echo "The version is <$VERSION>"
echo "The drop is <$DROP>"
echo "The patch is <${PATCH_NUMBER}>"

echo $RELEASE > $FILE_RELEASE
echo $VERSION > $FILE_VERSION
echo $DROP  > $FILE_DROP
echo ${PATCH_NUMBER}   > $FILE_PATCH

### File .version
echo ${VERSION} > .version

VERSION_FOR_NAME=$(cat ./.version | tr '[.]' '[_]')

DISTRIB_MARK="${VERSION} - Drop ${DROP} P${PATCH_NUMBER}"
echo "\nSignature name is $DISTRIB_MARK"

DISTRIB_MARK="${VERSION}"

if [ "${DROP}" = "" ]
then
  DISTRIB_MARK="${VERSION}"
else
  DISTRIB_MARK="${VERSION} - Drop ${DROP}"
fi

DISTRIB_NAME="TMFAgent_${RELEASE}"

echo DISTRIB_MARK = "${DISTRIB_MARK}"
echo DISTRIB_NAME = "${DISTRIB_NAME}"

echo DISTRIB_NAME = $DISTRIB_NAME

DISTRIB_VERSION="Distribuited: `/bin/date '+%y/%m/%d'`"

##################################################
#
# Copiatura albero
#
##################################################

#### Creo directories

echo "Creating directories tree......"

if [ ! -d "${DISTRIB_DIR}" ]
then
  mkdir -p "${DISTRIB_DIR}"
fi

if [ -d "${TARGET_DIR}" ]
then
  $RMDIR "${TARGET_DIR}"
fi

echo "\n*****************************************"
echo "Making directory $TARGET_DIR"
echo "*****************************************"

mkdir -p "${TARGET_DIR}"

### Copio i files

### EXE & LIBs

#echo $TARGET_DIR


echo "Copying exe & lib files....."
cd ${DISTRIB_DIR}



if [  "${MENU_MODE}" = "2" ]
then
echo "Copying files for NM"
FILE_SET_NAME="FilesPatchAgent_NM.set"
else
echo "Copying files for EM"
FILE_SET_NAME="FilesPatchAgent_EM.set"
fi

FILE_SET=$DISTRIB_DIR/$FILE_SET_NAME

if [ ! -a $FILE_SET ]
then
  echo "ERROR: file <$FILE_SET> doesn't exist"
  exit
fi

$MKDISTRIB $FILE_SET $TARGET_DIR

if [ $! <> 0 ]
then
    exit 1
fi

## 
# il file  prodotto da MKDISTRIB e' il contenuto della patch
#
mv ${FILE_SET}.copied $DISTRIB_DIR/.patch_contents

LOG_DIR=$TARGET_DIR/LOG
EXE_DIR=$TARGET_DIR/BIN
LIB_DIR=$TARGET_DIR/LIB
CONF_DIR=$TARGET_DIR/CONF
DATA_DIR=$TARGET_DIR/DATA
SCRIPT_DIR=$TARGET_DIR/SCRIPT
HTML_DIR=$TARGET_DIR/HTML
INFO_DIR=$TARGET_DIR/INFO


if [ -d $LOG_DIR ]
then
    $RM -R $LOG_DIR
fi

if [ $? -ne 0 ]
then
  exit 1
fi

if [ -r "${LIB_DIR}/Utilities.jar" ];
then
  rm -f "${LIB_DIR}/Utilities.jar"
fi

if [ -r "${LIB_DIR}/oojava.jar" ];
then
  rm -f "${LIB_DIR}/oojava.jar"
fi

if [ -r "${LIB_DIR}/oojava-"?"."?".jar" ];
then
  rm -f "${LIB_DIR}/oojava-"?"."?".jar"
fi

rm -f "${LIB_DIR}/oojava.jar."* > /dev/null 2>&1
rm -f "${LIB_DIR}/liboojava."*  > /dev/null 2>&1


### Creo una directory INFO in cui vado a mettere qualche file di utilita'.
if [ ! -d $TARGET_DIR/INFO ]
then
   mkdir -p $TARGET_DIR/INFO
fi

echo "\nCopying completed.\n"

### File .version (viene utilizzato per il file InfoRelease)
echo ${VERSION} > $INFO_DIR/.version

### File .drop (viene utilizzato per il file InfoRelease)
echo ${DROP} > $INFO_DIR/.drop

### File .drop (viene utilizzato per il file InfoRelease)
echo ${RELEASE} > $INFO_DIR/.release
echo ${PATCH_NUMBER} > $INFO_DIR/.patch

cp -f $DISTRIB_DIR/PATCH.README.$PATCH_NUMBER $INFO_DIR

echo "Marking exe & lib files...."

echo "Marking completed.\n"

echo "Archiving patch tar...."

cd "${TARGET_DIR}"

tar cf "${TAR_NAME}" *

mv "${TAR_NAME}" "${DISTRIB_DIR}/${TAR_NAME}"
cd "${DISTRIB_DIR}"

##if [ -d "${TARGET_DIR}" ]
##then
##  ${RMDIR} ${TARGET_DIR}
##fi

####echo "\nCompressing files ... please wait ..."
####
####if [ -a $TAR_NAME.gz ]
####then
####  $RM $TAR_NAME.gz
####fi
####gzip -9 $TAR_NAME
####

echo "...ended.\n"

exit 0
