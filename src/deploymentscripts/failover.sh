#!/bin/sh
# ---------------------------------------------------------------------------------------------------------------------------------------
# This script is used to start a slave instance of Messaging
# When the master instance is not up and Running.
# It runs as a cron every minute and does the following:
# 1) Checks if Master Is Running.
# 2) If it is not running then:
#    a) Ensure Master Instance is Stopped
#    b) Populate Slave Cache
#    c) Start Slave Cluster
#    d) Modify Cronjobs
# ---------------------------------------------------------------------------------------------------------------------------------------

# ---------------------------
# Variables And Initial Setup
# ---------------------------
source $(dirname $0)/env.sh
source $(dirname $0)/$ENV/settings.sh

_CURRENT_FOLDER=`pwd`
_LOG_FILE=./logs/installation.log
_SCRIPT_NAME=`basename $0`
_LOCK_FILE=./failover.lock
_RESTART_LOCK=$_CURRENT_FOLDER/restart.lock
_MAX_TIME_FOR_RESTART=10

# -----------------------------------------------
# Function To Delete the Lock File Before Exiting
# -----------------------------------------------
fnDeleteLockFile()
{
    rm -f $_LOCK_FILE
    _EXIT_STATUS=$?
    if [[ $_EXIT_STATUS != 0 ]]; then
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR : Couldn't delete [$_LOCK_FILE]\n[rm -f $_LOCK_FILE] returned exit status [$_EXIT_STATUS]\nSomething has gone haywire!"
        fnDeleteLockFile
        exit 1
    fi
    echo "Deleted [$_LOCK_FILE]"
}

# --------------------------------
# Ensure that there is no existing
# failover tasks going on...
# --------------------------------
if [ -f $_LOCK_FILE ] ; then
    ./checkProcessIsRunning.sh `cat $_LOCK_FILE`
    if [[ $? = 1 ]]; then
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE " ******* WARNING ******* : A failover task is already in progress. Aborting... "
        exit 1
    else
       ./logger.sh $_SCRIPT_NAME $_LOG_FILE "WARNING : Found an orphan lock file at [$_LOCK_FILE]. Will delete the file and continue with failover..."
        fnDeleteLockFile
    fi
fi

# ----------------
# Create Lock File
# ----------------
echo $$ > $_LOCK_FILE
echo "Created Failover Lock File at [$_LOCK_FILE]"

# --------------------------
# Check if Master Is Running
# --------------------------
./pingCosmessaging.sh $MASTER_HOST $MASTER_PORT
_EXIT_STATUS=$?
if [[ $_EXIT_STATUS == 0 ]]; then
    fnDeleteLockFile
    exit 0
else

    # -------------------------------------------
    # Check if a scheduled RESTART is in progress
    # -------------------------------------------

    # test -f command returns zero if file is found
    _EXIT_STATUS=0
    counter=1
    while [[ $_EXIT_STATUS == 0 && $counter -lt $_MAX_TIME_FOR_RESTART ]];
    do
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Checking if any scheduled restart is in progress ...\nExecuting [ssh cosmessaging@$MASTER_HOST test -f $_RESTART_LOCK] ..."
        ssh cosmessaging@$MASTER_HOST test -f $_RESTART_LOCK
        _EXIT_STATUS=$?
        if [[ $_EXIT_STATUS != 0 ]]; then
           ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Lock file [$_RESTART_LOCK] does not exist, exit status [$_EXIT_STATUS] returned!"
           break
        else
           ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Lock file [$_RESTART_LOCK] exists, exit status [$_EXIT_STATUS] returned!\nA Scheduled restart may still be running! Sleeping for [10] seconds\nAttempt [$counter] of [$_MAX_TIME_FOR_RESTART] attempts ..."
           counter=`expr $counter + 1`
           sleep 10
       fi
   done
fi

# ---------------------------------------
# Maybe an Installation is in progress...
# ---------------------------------------
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Sleeping for [60] seconds to ensure running jobs (if any) are completed"
sleep 60

# ---------------------------
# Check if Master has Started
# ---------------------------
./pingCosmessaging.sh $MASTER_HOST $MASTER_PORT
_EXIT_STATUS=$?
if [ $_EXIT_STATUS == 0 ]; then
    fnDeleteLockFile
    exit 0
fi

# -----------------
# Initiate Failover
# -----------------
startTime=$SECONDS
./logger.sh $_SCRIPT_NAME $_LOG_FILE "************************* STARTING FAILOVER INSTANCE *************************"

# --------------------------------
# Delete Running Cronjobs on Slave
# --------------------------------
crontab -r
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Temporarily deleted cronjobs!"

# --------------------------------------------------
# Clear Cronjobs on Master To Avoid Race Conditions!
# --------------------------------------------------
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Executing [ssh cosmessaging@$MASTER_HOST crontab -r] to avoid race conditions ..."
ssh cosmessaging@$MASTER_HOST crontab -r
_EXIT_STATUS=$?
if [[ $_EXIT_STATUS != 0 ]]; then
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "WARNING : Couldn't delete crons on master. Got an non zero exit status [$_EXIT_STATUS]! Ignoring and continuing ..."
else
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Sucessfully deleted cronjobs (exit status [$_EXIT_STATUS]) on the master cluster at [$MASTER_HOST]!"
fi

# ------------------------------------
# Try and Kill Master Cluster Instance
# ------------------------------------
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Attempting to kill the Master Cluster Instance ..."
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Executing [ssh cosmessaging@$MASTER_HOST 'cat $CATALINA_BASE_MASTER/$MASTER_PORT.pid | xargs kill -9']"
ssh cosmessaging@$MASTER_HOST cat $CATALINA_BASE_MASTER/$MASTER_PORT.pid | xargs kill -9
_EXIT_STATUS=$?
if [[ $_EXIT_STATUS != 0  ]]; then
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Couldn't kill the process. Got an non zero exit status [$_EXIT_STATUS]! Perhaps the process was already dead!"
else
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Killed running process on master (got exit status [$_EXIT_STATUS])!"
fi

# ----------------------------
# Populate Data in Slave Cache
# ----------------------------
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Starting to populate data in slave cluster cache. Logs for this will be logged at [$DEPLOYMENT_DIR/logs/dataPopulation.log]"
./populate.sh slave
_EXIT_STATUS=$?
if [ $_EXIT_STATUS != 0 ]; then
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR : Couldn't populate data for the jdbm cache. Exit Status [$_EXIT_STATUS] returned. Will ignore this and try to start the server ..."
else
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Successfully populated data in slave cluster cache!"
fi

# -------------------
# Start Slave Cluster
# -------------------
./startTomcat.sh $CATALINA_BASE_SLAVE $SLAVE_PORT
_EXIT_STATUS=$?
if [ $_EXIT_STATUS != 0 ]; then
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR : Couldn't restart [slave] cluster\nBOTH MASTER AND SLAVE ARE DOWN! MAYDAY MAYDAY!!!"
    fnDeleteLockFile
    exit 1
fi

# -----------------------------------
# Create 'Modified' Cronjobs On Slave
# -----------------------------------
./createCronJob.sh slave

# ----------------
# Delete Lock File
# ----------------
fnDeleteLockFile

# -----
# Exit!
# -----
endTime=$SECONDS
totalTime=$((endTime - startTime))
# Convert From Seconds to Minutes
totalTime=`echo "scale=2; $totalTime / 60" | bc`
./logger.sh $_SCRIPT_NAME $_LOG_FILE "TIME_TO_RECOVER=\"$totalTime\""
./logger.sh $_SCRIPT_NAME $_LOG_FILE "************************* SUCCESSFULLY STARTED FAILOVER INSTANCE in : [$totalTime] minutes *************************"