#!/bin/sh
# ---------------------------------------------------------------------------------------------------------------------------------------
# This script shuts down and restarts tomcat instance for
# the current release. It is run as cron every week for
# ensuring a continued optimal performance of the server.
# Parameters (Optional):
# $1 : slave
# If a parameter is passed to this script, the parameter
# can only be 'slave' which indicates that the slave cluster
# should be restarted.
# If no parameter is passed, the script by default restarts
# the master cluster
# ---------------------------------------------------------------------------------------------------------------------------------------

# -------------
# Initial Setup
# -------------
startTime=$SECONDS
_LOG_FILE=./logs/dataPopulation.log
_SCRIPT_NAME=`basename $0`
_DATE=`date`
_LOCK_FILE=./restart.lock

# Get the Catalina Base and Port Information from the configuration files
source $(dirname $0)/env.sh
source $(dirname $0)/$ENV/settings.sh

./logger.sh $_SCRIPT_NAME $_LOG_FILE "Restarting Messaging [$DEPLOYMENT_DIR] on [$_DATE]"

# ------------------------------------------
# Identify which cluster should be restarted
# ------------------------------------------
if [[ -z $1 ]]; then
    _PATH_TO_TOMCAT=$CATALINA_BASE_MASTER
    _TOMCAT_PORT=$MASTER_PORT
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "No parameter is passed to the script, so will restart [master] cluster!"
else
    _PATH_TO_TOMCAT=$CATALINA_BASE_SLAVE
    _TOMCAT_PORT=$SLAVE_PORT
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Since [$1] has been passed to the script, [slave] cluster will be restarted!"
fi

# -----------------------------------------------
# Function To Delete the Lock File Before Exiting
# -----------------------------------------------
fnDeleteLockFile()
{
    rm -f $_LOCK_FILE
    _EXIT_STATUS=$?
    if [ $_EXIT_STATUS != 0 ]; then
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR : Couldn't delete [$_LOCK_FILE]. Something has gone haywire!"
        exit 1
    fi
}

# --------------------------------
# Ensure that there is no existing
# restart job going on...
# --------------------------------
if [ -f $_LOCK_FILE ] ; then
    ./checkProcessIsRunning.sh `cat $_LOCK_FILE`
    if [ $? = 1 ]; then
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR: A restart is already in progress. Please try again after some time. Aborting MESSAGING RESTART!"
        exit 1
    else
       ./logger.sh $_SCRIPT_NAME $_LOG_FILE "WARNING : Found an orphan lock file at [$_LOCK_FILE]. Will delete the file and continue with restart..."
        fnDeleteLockFile
    fi
fi

# ----------------
# Create Lock File
# ----------------
echo $$ > $_LOCK_FILE
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Created Restart Lock File at [$_LOCK_FILE]"

# ----------------------
# Invoke Shutdown Script
# ----------------------
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Running [./shutDownTomcat.sh $_PATH_TO_TOMCAT $_TOMCAT_PORT] ..."
./shutDownTomcat.sh $_PATH_TO_TOMCAT $_TOMCAT_PORT
tomcatShutDownStatus=$?
if [ "0" != "$tomcatShutDownStatus" ]; then
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR: Unable to Stop Tomcat on port [$_TOMCAT_PORT]. Shutdown exited with status [$tomcatShutDownStatus]. Aborting Restart! Messaging may be down!"
    fnDeleteLockFile
    exit 1
fi

# --------------------------------------
# Clear KahaDB Logs and Recreate Folders
# --------------------------------------
rm -rf $KAHADB_MASTER_DIR
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Cleared KahaDB [Master] Logs by running [rm -rf $KAHADB_MASTER_DIR]"

rm -rf $KAHADB_SLAVE_DIR
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Cleared KahaDB [Slave] Logs by running [rm -rf $KAHADB_SLAVE_DIR]"

mkdir -p $KAHADB_MASTER_DIR
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Recreated KahaDB [Master] Logs Folder Structure by running [mkdir -p $KAHADB_MASTER_DIR]"

mkdir -p $KAHADB_SLAVE_DIR
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Recreated KahaDB [Slave] Logs Folder Structure by running [mkdir -p $KAHADB_SLAVE_DIR]"

# ---------------------
# Invoke Startup Script
# ---------------------
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Running [./startTomcat.sh $_PATH_TO_TOMCAT $_TOMCAT_PORT] ..."
./startTomcat.sh $_PATH_TO_TOMCAT $_TOMCAT_PORT
tomcatStartUpStatus=$?
if [ "0" != "$tomcatStartUpStatus" ]; then
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR: Unable to Start Tomcat on port [$_TOMCAT_PORT]. Startup exited with status [$tomcatStartUpStatus]. Messaging is Down!"
    fnDeleteLockFile
    exit 1
fi

# -------------
# Log and Exit!
# -------------
fnDeleteLockFile
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Lock file [$_LOCK_FILE] deleted!"
endTime=$SECONDS
totalTime=$((endTime - startTime))
./logger.sh $_SCRIPT_NAME $_LOG_FILE "TIME_TO_RESTART=\"$totalTime\""
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Messaging was successfully restarted in [$totalTime] seconds!"