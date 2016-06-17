
TAR_NAME="$1"
MENU_MODE="$2"

curr_dir=$(pwd)

SOURCE_DIR=$(dirname ${curr_dir})

##############################
#
# TARGETS
#
##############################

DISTRIB_DIR="${curr_dir}"
DEPLOY_DIR="${SOURCE_DIR}/DEPLOY"

##############################
#
# COMMAND
#
##############################

RM="/bin/rm -f"
RMDIR="/bin/rm -rf"
CP="/bin/cp -r"
MV="/bin/mv"
STRIP="strip"
FIND="/bin/find"

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

if [ ! -a "${FILE_VERSION}" ]
then
  echo "1" > "${FILE_VERSION}"
fi

VERSION=`cat ${FILE_VERSION}`

echo "\nVersion Number (default ${VERSION}): "
read TMP

if [ "${TMP}" <> "" ]
then
  VERSION=$TMP
fi

if [ ! -a "${FILE_DROP}" ]
then
  echo "a" > "${FILE_DROP}"
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
  DROP="${TMP}"
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

echo "${VERSION}" > "${FILE_VERSION}"
echo "${DROP}"    > "${FILE_DROP}"
echo "${RELEASE}" > "${FILE_RELEASE}"
echo "${INTERNALBUILD}" > "${FILE_INTERNALBUILD}"
