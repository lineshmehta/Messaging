#!/bin/sh
# ---------------------------------------------------------------------------------------------------------------------------------------
# Script to Start Tomcat
# Parameters:
# $1 --> The Path to the Tomcat Installation
# $2 --> The Port on which this Installation will run
# ---------------------------------------------------------------------------------------------------------------------------------------

# -------------
# Initial Setup
# -------------
_LOG_FILE=./logs/installation.log
_SCRIPT_NAME=`basename $0`

_PATH_TO_TOMCAT=$1
_PORT=$2

_PATH_TO_CATALINA_LOG="$_PATH_TO_TOMCAT/logs/catalina.out"

# Tomcat should ideally startup in less than 10 minutes or 600 seconds
_MAX_TIME_FOR_STARTUP=600

# ---------------------
# Invoke Startup Script
# ---------------------
./logger.sh $_SCRIPT_NAME $_LOG_FILE "-------------------------------- STARTING TOMCAT [$_PATH_TO_TOMCAT/bin/startup.sh]"
$_PATH_TO_TOMCAT/bin/startup.sh

./logger.sh $_SCRIPT_NAME $_LOG_FILE "Tomcat is currently starting ..."

# -----------------------------------------
# Verify If Tomcat has started by Scanning
# catalina.out for 'Server Startup' Message
# -----------------------------------------
counter=0
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Grepping logs at [$_PATH_TO_CATALINA_LOG] to verify startup (This might take upto [$_MAX_TIME_FOR_STARTUP] seconds) ..."

# Set the variable to blank before testing...
started=
while [[ $started = '' && $counter -lt _MAX_TIME_FOR_STARTUP ]];
    do
       echo -n ".."
       if [ -f  $_PATH_TO_CATALINA_LOG ]
       then
           started=`grep "Server startup" $_PATH_TO_CATALINA_LOG`
       fi
       counter=`expr $counter + 1`
       sleep 1
done
echo -e ".."

if [[ $started = '' ]]; then
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR : Unable to start tomcat at [$_PATH_TO_TOMCAT] in [$_MAX_TIME_FOR_STARTUP] seconds. Aborting Installation!"
    exit 1
fi

# -----------------------------------
# Ping and Check Application Response
# -----------------------------------
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Pinging Cosmessaging on [curl http://localhost:$_PORT]"
./pingCosmessaging.sh localhost $_PORT
_EXIT_STATUS=$?
if [ $_EXIT_STATUS != 0 ]; then
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR : Cosmessaging Startup has failed! Aborting Installation!\nPing Response was [$_EXIT_STATUS]"
    exit 1
else
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Cosmessaging is now running!"
fi