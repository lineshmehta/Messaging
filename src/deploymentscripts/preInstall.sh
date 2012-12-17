#!/bin/sh
# ---------------------------------------------------------------------------------------------------------------------------------------
# This script moves the unzipped release folder to the
# release folder corresponding to this release.
# Then it invokes invokes the install.sh script
# located inside the moved release folder.
# After invoking the install script, this script deletes
# itself retains only last 3 distributions of messaging
# so that the releasebase folder is always clean.
# ---------------------------------------------------------------------------------------------------------------------------------------

startTime=$SECONDS

RELEASE_BASEDIR=~/releasebase/

VERSION=`ls -td */ | xargs -l basename`

MAJOR_VERSION=`echo $VERSION | awk -F '.' 'BEGIN{OFS=".";} {print $1,$2}'`

RELEASE_DIR=~/release_$MAJOR_VERSION

DEPLOYMENT_DIR=$RELEASE_DIR/$VERSION

TMP_INSTALL_DIR=`dirname $(readlink -f $0)`

BACKUP_DIR=$RELEASE_DIR/backup/

JDBM_MASTER_DIR=$RELEASE_DIR/jdbm3/master

JDBM_SLAVE_DIR=$RELEASE_DIR/jdbm3/slave

KAHADB_MASTER_DIR=$RELEASE_DIR/kahadb/master

KAHADB_SLAVE_DIR=$RELEASE_DIR/kahadb/slave

# ------------------------------------------------------
# Function to clean up the temporary installation folder
# ------------------------------------------------------
fnDeleteTmpInstallFolder() {
    echo "Deleting the temporary folders [$TMP_INSTALL_DIR] ..."
    rm -rf $TMP_INSTALL_DIR
    echo "Folder [$TMP_INSTALL_DIR] Deleted!"
}
# ------------------------------------------------------------------------------------
# Verify that the ENV variable exists or has been passed as a parameter to this script
# ------------------------------------------------------------------------------------
if [[ -z $ENV ]]; then
    # echo "ENV variable is not already set. So, it should be passed as a script parameter"
    if [[ -z $1 ]]; then
        echo "This script requires a parameter (which identifies the ENVIRONMENT) to be passed without which it cannot run!"
        echo "Usage Example : "
        echo "./preInstall.sh prod"
        echo "Note: For Production Environment, the parameter must be 'prod' only. Exact command to run this script is: /preInstall.sh prod"
        echo "Aborting Run. Please run again with the parameter"
        exit 1
    else
       export ENV=$1
    fi
fi

export RELEASE_DIR=$RELEASE_DIR
export DEPLOYMENT_DIR=$RELEASE_DIR/$VERSION
export TOMCAT_HOME=$TOMCAT_HOME
export JDBM_MASTER_DIR=$JDBM_MASTER_DIR
export JDBM_SLAVE_DIR=$JDBM_SLAVE_DIR
export KAHADB_MASTER_DIR=$KAHADB_MASTER_DIR
export KAHADB_SLAVE_DIR=$KAHADB_SLAVE_DIR
export BACKUP_DIR=$BACKUP_DIR

echo "Preparing for Installation of CosMessaging Version [$VERSION] in [$ENV] environment ..."
echo "RELEASE_BASEDIR      = $RELEASE_BASEDIR"
echo "RELEASE_DIR          = $RELEASE_DIR"
echo "VERSION              = $VERSION"
echo "MAJOR VERSION        = $MAJOR_VERSION"
echo "TMP_INSTALL_DIR      = $TMP_INSTALL_DIR"
echo "JDBM_MASTER_DIR      = $JDBM_MASTER_DIR"
echo "JDBM_SLAVE_DIR       = $JDBM_SLAVE_DIR"
echo "KAHADB_MASTER_DIR    = $KAHADB_MASTER_DIR"
echo "KAHADB_SLAVE_DIR     = $KAHADB_SLAVE_DIR"
echo "BACKUP_DIR           = $BACKUP_DIR"

# ---------------------------------------------
# Create release directory if it does not exist
# ---------------------------------------------
if [ ! -d $RELEASE_DIR ]; then
    mkdir -p $RELEASE_DIR;
    echo "Folder [$RELEASE_DIR] was not existing and hence was created."
fi

# ------------------------------------
# Check if Same Release already exists
# ------------------------------------
if [ -d $RELEASE_DIR/$VERSION ]; then
    echo "FATAL ERROR: The version you are trying to install i.e. [$VERSION] is already existing!"
    echo "Cannot reinstall the same version. Will Abort Installation post cleanup..."
    fnDeleteTmpInstallFolder
    exit 1
fi

# -----------------------------------------------
# Recreate Cache Folders if they are not existing
# -----------------------------------------------
if [ ! -d $JDBM_MASTER_DIR ]; then
    mkdir -p $JDBM_MASTER_DIR
    echo "Folder [$JDBM_MASTER_DIR] was not existing and hence was created."
fi

if [ ! -d $JDBM_SLAVE_DIR ]; then
    mkdir -p $JDBM_SLAVE_DIR
    echo "Folder [$JDBM_SLAVE_DIR] was not existing and hence was created."
fi

# ---------------------------------------------------
# Create Kaha DB Directories if they are not existing
# ---------------------------------------------------
if [ ! -d $KAHADB_MASTER_DIR ]; then
    mkdir -p $KAHADB_MASTER_DIR
    echo "Folder [$KAHADB_MASTER_DIR] was not existing and hence was created."
fi

if [ ! -d $KAHADB_SLAVE_DIR ]; then
    mkdir -p $KAHADB_SLAVE_DIR
    echo "Folder [$KAHADB_SLAVE_DIR] was not existing and hence was created."
fi

# ---------------------------------------------
# Create Backup Directory if it is not existing
# ---------------------------------------------
if [ ! -d $BACKUP_DIR ]; then
    mkdir -p $BACKUP_DIR
    echo "Folder [$BACKUP_DIR] was not existing and hence was created."
fi

# --------------------------------------------------
# Move the release version folder to current release
# --------------------------------------------------
echo "Moving version [$VERSION] files to the location [$RELEASE_DIR]"
mv $VERSION $RELEASE_DIR

# --------------------------------------------
# Give execute permission to all shell scripts
# --------------------------------------------
chmod -R 744 $RELEASE_DIR/$VERSION/*.sh
echo "Execute permission given to all shell scripts..."

# -------------------------------------------------------------
# Use SED to convert DOS generated shell scripts to UNIX format
# -------------------------------------------------------------
sed -i -e 's/\r//' $RELEASE_DIR/$VERSION/*.sh
echo "All DOS format files converted to UNIX format..."

(cd $RELEASE_DIR/$VERSION/;
./install.sh)

# -------------------------------------------
# Check if Installation was successful or not
# -------------------------------------------
installationStatus=$?
if [ "0" != $installationStatus ]; then
    echo "FATAL ERROR: Installation Failed! Script [$RELEASE_DIR/$VERSION/install.sh] exited with status [$installationStatus]. Check console logs and logs at [$RELEASE_DIR/$VERSION/logs/installation.log] for details. Aborting Installation!"
    fnDeleteTmpInstallFolder
    exit 1
fi

# ----------------------------------------------------------
# Retain only last 3 Distribution Zips of this MAJOR release
# ----------------------------------------------------------
ls -t1 $RELEASE_BASEDIR/*messaging*$MAJOR_VERSION*.zip | tail -n +3 | xargs rm -f
echo "Doing housekeeping of existing archives..."

# -------
# Cleanup
# -------
fnDeleteTmpInstallFolder

# ---------------------------------
# Calculate Total Installation Time
# ---------------------------------
endTime=$SECONDS
totalTime=$((endTime - startTime))

# Convert From Seconds to Minutes
totalTime=`echo "scale=2; $totalTime / 60" | bc`

echo "Installation completed successfully in [$totalTime] minutes!"