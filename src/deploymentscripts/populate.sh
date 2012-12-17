#!/bin/sh
# ---------------------------------------------------------------------------------------------------------------------------------------
# This script populates the jdbm 3 cache with each
# installation and also runs as a cron job each night.
# After population, it restarts the cluster to
# reflect the new data.
# Parameters (Optional):
# $1 : slave
# If a parameter is passed to this script, the parameter
# can only be 'slave' which indicates that the slave jdbm
# cache should be populated.
# If no parameter is passed, the script by default populates
# the master jdbm cache only.
# ---------------------------------------------------------------------------------------------------------------------------------------

# ---------------------------
# Variables And Initial Setup
# ---------------------------
source $(dirname $0)/env.sh
source $(dirname $0)/$ENV/settings.sh

startTime=$SECONDS

_LOG_FILE=./logs/dataPopulation.log
_SCRIPT_NAME=`basename $0`

_USER=cosadmin
_PATH_TO_BCP_SCRIPT=SCRIPT/COSMessaging_bcp.sh
_BCP_LOG_FILE="/home/cosadmin/LOG/COSMessaging_bcp.log"

_PATH_TO_REMOTE_CSV="OUT_MESSAGING/*"
_TMP_CSV_FOLDER="`pwd`/tmp"

_JDBM_JAR=jdbm-batch.jar
_CHUNK_SIZE=5000

# ------------------------------------------------
# Identify for which cluster should population run
# ------------------------------------------------
if [[ -z $1 ]]; then
    _JDBM_LOCK=$JDBM_MASTER_DIR/jdbm3-data.lock
    _JDBM_CSV_FOLDER=$JDBM_MASTER_DIR/csv
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "No parameter is passed to the script, so will populate [master] cache!"
else
    _JDBM_LOCK=$JDBM_SLAVE_DIR/jdbm3-data.lock
    _JDBM_CSV_FOLDER=$JDBM_SLAVE_DIR/csv
    _JAVA_OPT="-Dmaster=slave"
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Since [$1] has been passed to the script, [slave] cache will be populated!"
fi

# --------------------------------------------
# Function to Delete Lock File Before Exiting!
# --------------------------------------------
fnDeleteLockFile()
{
    rm -f $_JDBM_LOCK 2>/dev/null
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Deleted the lock file [$_JDBM_LOCK]"
}

# ----------------------------------------------------------------------
# This flag is set to TRUE and used when due to some reason, data
# couldn't be retrieved from the FKM Server.
# In such case, the script will try to load data from a previous backup
# Note: Default value is "false"
# ----------------------------------------------------------------------
_FKM_JOB_FAILED="false"

./logger.sh $_SCRIPT_NAME $_LOG_FILE "******************************************  STARTING DATA POPULATION  ******************************************"

# -------------------------------
# Create Temp Folder for CSV Dump
# -------------------------------
rm -rf $_TMP_CSV_FOLDER 2>/dev/null
mkdir -p $_TMP_CSV_FOLDER 2>/dev/null
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Temporary Folder [$_TMP_CSV_FOLDER] was deleted and recreated."

# --------------------------------
# Ensure that there is no existing
# DB population job going on.
# --------------------------------
if [ -f $_JDBM_LOCK ] ; then
    ./checkProcessIsRunning.sh `cat $_JDBM_LOCK`
    if [ $? = 1 ]; then
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR: A Datapopulation process is already in progress. Please try again after some time. Aborting DATA POPULATION!"
        exit 1
    else
       ./logger.sh $_SCRIPT_NAME $_LOG_FILE "WARNING : Found an orphan lock file at [$_JDBM_LOCK]. Will delete the file and continue with data population..."
        rm -f $_LOCK_FILE
    fi
fi

# ------------------------------------------
# Execute the Remote BCP Job to generate the
# CSV (View Files) Dump
# ------------------------------------------
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Executing [$_PATH_TO_BCP_SCRIPT] on Remote Data Server [$DATABASE_SERVER] as [$_USER]"
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Command Executed --> [ssh $_USER@$DATABASE_SERVER $_PATH_TO_BCP_SCRIPT]\nThis will take several minutes..."

startTimeForBCP=$SECONDS
ssh $_USER@$DATABASE_SERVER $_PATH_TO_BCP_SCRIPT
_EXIT_STATUS="${PIPESTATUS[0]}"
endTimeForBCP=$SECONDS

if [ $_EXIT_STATUS != 0 ]; then
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR: Couldn't execute the Remote BCP Script!"
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Exit Status Returned was [$_EXIT_STATUS]"
    _FKM_JOB_FAILED="true"
else
    totalTimeForBCP=$((endTimeForBCP - startTimeForBCP))
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "TIME_FOR_BCP=\"$totalTimeForBCP\""
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Remote script was executed successfully. Exit Status Returned was [$_EXIT_STATUS]"
fi


# ----------------------------------------
# Verify the Remote BCP Job Logs to ensure
# that there were no Errors
# ----------------------------------------
if [ $_FKM_JOB_FAILED != "true" ]; then
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Verifying Remote BCP Job..."
    ./checkBCPLogsForErrors.sh $_USER $DATABASE_SERVER $_BCP_LOG_FILE $_LOG_FILE
    _EXIT_STATUS="${PIPESTATUS[0]}"
    if [ $_EXIT_STATUS != 0 ]; then
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR: Found some errors in BCP Log!"
        _FKM_JOB_FAILED="true"
    fi
fi

# ------------------------------------------
# Copy the Dump Files from the Remote Server
# ------------------------------------------
if [ $_FKM_JOB_FAILED != "true" ]; then
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Transferring CSV Files from server [$DATABASE_SERVER] in the path [$_PATH_TO_REMOTE_CSV] to temporary storage [$_TMP_CSV_FOLDER]"

    scp -r $_USER@$DATABASE_SERVER:$_PATH_TO_REMOTE_CSV $_TMP_CSV_FOLDER
    _EXIT_STATUS="${PIPESTATUS[0]}"

    if [ $_EXIT_STATUS != 0 ]; then
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR: Couldn't transfer the files from FKM server! Exit Status Returned was [$_EXIT_STATUS]"
        _FKM_JOB_FAILED="true"
    else
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Transferred the CSV Files successfully!"

        # ---------------------------------------
        # Create a Backup of the copied CSV Files
        # ---------------------------------------

        # Create a dynamic archive name in format: HourMinute-DayMonthYear
        ARCHIVE_NAME=`date '+%H%M-%d%h%Y'`
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Backing up the fkmDataDump files from [$_TMP_CSV_FOLDER] to [$BACKUP_DIR]"

        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Executing [zip $BACKUP_DIR/$ARCHIVE_NAME.zip *] inside [$_TMP_CSV_FOLDER] ..."
        (
            cd $_TMP_CSV_FOLDER;
            zip $BACKUP_DIR/$ARCHIVE_NAME.zip *;
        )
        _EXIT_STATUS=$?
        if [ $_EXIT_STATUS != 0 ]; then
            ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Error: Exit Status Returned by the zip command was [$_EXIT_STATUS]. Ignoring and continuing without backing up..."
        else
            ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Sucessfully completed Backing up of the fkmDataDump files!"
        fi
    fi
fi

# -------------------------------------------------
# Get the CSV files from Local Backup if FKM Failed
# -------------------------------------------------
if [ $_FKM_JOB_FAILED == "true" ]; then

    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Couldn't get files from FKM, so will attempt to populate using local backup ..."

    # ----------------------------------------------
    # Get the files from the latest available backup
    # ----------------------------------------------
    LATEST_ARCHIVE=`ls -tr $BACKUP_DIR | tail -1`
    if [[ -n $LATEST_ARCHIVE ]]; then
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Latest available archive file name is [$LATEST_ARCHIVE]"
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Extracting files using [unzip $BACKUP_DIR/$LATEST_ARCHIVE -d $_TMP_CSV_FOLDER]"
        unzip $BACKUP_DIR/$LATEST_ARCHIVE -d $_TMP_CSV_FOLDER
        _EXIT_STATUS=$?
        if [ $_EXIT_STATUS != 0 ]; then
            ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Error: Couldnt unzip the zip! No CSV Files available to continue Data Population! Aborting Data Population!"
            ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Exit Status Returned by the unzip command was [$_EXIT_STATUS]"
            fnDeleteLockFile
            exit 1
        else
            ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Successfully unziped the files!"
        fi
    else
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR : No archive available in [$BACKUP_DIR]. Aborting Data Population!"
        fnDeleteLockFile
        exit 1
    fi
fi

# ------------------------------------
# Clean up, and re-configure JDBM Home 
# ------------------------------------

# Clean up the old data cache and recreate the folder
rm -rf $JDBM_MASTER_DIR 2> /dev/null
mkdir $JDBM_MASTER_DIR
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Folder [$JDBM_MASTER_DIR] was deleted and recreated."

rm -rf $JDBM_SLAVE_DIR 2> /dev/null
mkdir $JDBM_SLAVE_DIR
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Folder [$JDBM_SLAVE_DIR] was deleted and recreated."

mkdir $_JDBM_CSV_FOLDER
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Folder [$_JDBM_CSV_FOLDER] was created."

# Move the files from the temporary storage area
mv $_TMP_CSV_FOLDER/* $_JDBM_CSV_FOLDER/
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Moved files [mv $_TMP_CSV_FOLDER/* $_JDBM_CSV_FOLDER/]"

# Delete the Temp Folder
rm -rf $_TMP_CSV_FOLDER
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Deleted [$_TMP_CSV_FOLDER]"

# Create the Lock File
echo $$ > $_JDBM_LOCK
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Created DB Lock File [$_JDBM_LOCK]"

# ------------------------------------------------------
# Ensure that we have some CSV files to start population
# ------------------------------------------------------
if [ "$(ls -A $_JDBM_CSV_FOLDER)" ]; then
  ./logger.sh $_SCRIPT_NAME $_LOG_FILE "[$_JDBM_CSV_FOLDER] has files so data population can be started ..."
else
  ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR : [$_JDBM_CSV_FOLDER] has NO files. Data population cannot continue. Aborting Data Population!"
  fnDeleteLockFile
  exit 1
fi

# -----------------
# POPULATE FKM DATA
# -----------------
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Jar file : [$_JDBM_JAR] & Path to CSV : [$_JDBM_CSV_FOLDER]"

./logger.sh $_SCRIPT_NAME $_LOG_FILE "Starting population of the FKM Data..................."
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Starting Data Population \nExecuting [java $_JAVA_OPT -jar $_JDBM_JAR $_JDBM_CSV_FOLDER $_CHUNK_SIZE] ..."
    startTimeForCache=$SECONDS
    java $_JAVA_OPT -jar $_JDBM_JAR $_JDBM_CSV_FOLDER $_CHUNK_SIZE | tee -a $_LOG_FILE
    _EXIT_STATUS=$?
    if [ $_EXIT_STATUS != 0 ]; then
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR: Couldn't insert data. Cache is NOT POPULATED!"
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Exit Status Returned by the java process was [$_EXIT_STATUS]"
    else
        endTimeForCache=$SECONDS
        totalTimeForCache=$((endTimeForCache - startTimeForCache))
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Completed Data Population for JDBM3 Cache in [$totalTimeForCache] seconds"
    fi
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Completed FKM Data population!"

# -------------------------------------------
# POPULATE TEST DATA in NON PROD Environments
# -------------------------------------------
if [[ ! "$ENV" =~ prod* ]]; then
    ./insertTestData.sh
    _EXIT_STATUS=$?
    if [ $_EXIT_STATUS != 0 ]; then
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR : Couldnt insert the test data. Ignoring and continuing without TEST DATA ..."
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Exit Status Returned was [$_EXIT_STATUS]"
    fi
fi

# ----------------
# Delete Lock File
# ----------------
fnDeleteLockFile

# -----------------------------------------
# Perform Housekeeping in the backup Folder
# Retain archives only from last 3 days.
# -----------------------------------------
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Doing housekeeping of [$BACKUP_DIR]..."
find $BACKUP_DIR/ -name "*" -mtime +3 -exec rm -f {} \;
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Completed housekeeping!"

# ---------------------------------------
# Restart Messaging so that the new Cache
# is loaded in to the application
# Parameter:
# $1 which will empty for Master Cluster
# and 'slave' for Slave Cluster.
# ---------------------------------------
./restart.sh $1
_EXIT_STATUS=$?
if [ $_EXIT_STATUS != 0 ]; then
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Restart failed! [restart.sh $1] aborted with exit status [$_EXIT_STATUS]"
fi

# -------------
# Log and Exit!
# -------------
endTime=$SECONDS
totalTime=$((endTime - startTime))
# Convert From Seconds to Minutes
totalTime=`echo "scale=2; $totalTime / 60" | bc`

# For Splunk
./logger.sh $_SCRIPT_NAME $_LOG_FILE "TIME_TO_POPULATE=\"$totalTime\""
./logger.sh $_SCRIPT_NAME $_LOG_FILE "************************* COMPLETED DATA POPULATION JOB in : [$totalTime] minutes *************************"