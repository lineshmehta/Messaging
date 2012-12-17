#!/bin/sh
# ---------------------------------------------------------------------------------------------------------------------------------------
# This script shuts down tomcat instance for 
# the given port by all possible means
# Parameters:
# $1 --> The Path to the Tomcat Installation
# $2 --> The Port to be shutdown
# ---------------------------------------------------------------------------------------------------------------------------------------

# -------------
# Initial Setup
# -------------
_LOG_FILE=./logs/installation.log
_SCRIPT_NAME=`basename $0`
_PATH_TO_TOMCAT=$1
_PORT=$2

# Tomcat should ideally shutdown in less than 30 seconds
_MAX_TIME_FOR_SHUTDOWN=30

# ---------------------------------------------
# Function to log that shutdown was successful,
# clean old logs and exit with Zero!
# ---------------------------------------------
fnLogCleanAndExit()
{
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Tomcat [$_PATH_TO_TOMCAT] has been shutdown successfully on port [$_PORT]!"
    rm -rf $_PATH_TO_TOMCAT/logs/*
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Cleared all old logs at [$_PATH_TO_TOMCAT/logs/*]"
    exit 0
}

# ------------
# Find the PID
# ------------

TOMCAT_PID_FILE=$_PATH_TO_TOMCAT/$_PORT.pid

if [[ -s $TOMCAT_PID_FILE ]]; then
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Found [$TOMCAT_PID_FILE] --> Will attempt to extract the PID"
    TOMCAT_PID=`cat "$TOMCAT_PID_FILE" | sed s/\r//`
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Using [$TOMCAT_PID] as Tomcat PID and checking if the process is running..."
    ./checkProcessIsRunning.sh $TOMCAT_PID
    currentTomcatStatus=$?
else
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE " ***** WARNING : Tomcat pid file not found! Expected [$TOMCAT_PID_FILE] to be present! *****"
fi

# -------------------------------------
# Kill Running Tomcat by graceful means
# -------------------------------------

if [[ $currentTomcatStatus == 1 ]]; then
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Tomcat is running! Invoking shutdown script as [$_PATH_TO_TOMCAT/bin/shutdown.sh -force]"
    $_PATH_TO_TOMCAT/bin/shutdown.sh -force

    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Tomcat is currently shutting down. Please wait ..."

    counter=0
    while [[ $currentTomcatStatus == 1 && $counter -lt $_MAX_TIME_FOR_SHUTDOWN ]];
        do
            echo ".."
            currentTomcatStatus=`./checkProcessIsRunning.sh $TOMCAT_PID`
            counter=`expr $counter + 1`
        done

    # ----------------------------------------------------------
    # If Tomcat didnt shut down in stipulated time, kill the PID
    # ----------------------------------------------------------

    if [[ $currentTomcatStatus == 1 ]]; then
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE -n "WARNING : Couldn't shutdown with script. Executing [kill $TOMCAT_PID] ..."
        kill $TOMCAT_PID
        sleep 5
    fi

else
    fnLogCleanAndExit
fi

# --------------------------------------------
# If Tomcat is still running, Kill it brutally
# --------------------------------------------

./logger.sh $_SCRIPT_NAME $_LOG_FILE "Using [$TOMCAT_PID] as Tomcat PID and checking if the process is STILL running..."

./checkProcessIsRunning.sh $TOMCAT_PID

currentTomcatStatus=$?

if [[ $currentTomcatStatus == 1 ]]; then
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "WARNING : Unable to stop Tomcat using graceful means. Executing [kill -9 "$TOMCAT_PID"] ..."
    kill -9 "$TOMCAT_PID"
    sleep 5
else
    fnLogCleanAndExit
fi

# ----------------------------------------------
# If Tomcat is still running, Give Up and Abort!
# ----------------------------------------------

./logger.sh $_SCRIPT_NAME $_LOG_FILE "Verifying if tomcat is STILL running..."

currentTomcatStatus=`./checkProcessIsRunning.sh $TOMCAT_PID`

if [[ $currentTomcatStatus == 1 ]]; then
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR: UNABLE TO STOP TOMCAT on port [$_PORT] in location [$_PATH_TO_TOMCAT] Aborting..."
    exit 1
else
    fnLogCleanAndExit
fi