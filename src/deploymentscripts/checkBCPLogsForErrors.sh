#!/bin/sh
# ---------------------------------------------------------------------------------------------------------------------------------------
# Script to check if the specified bcp log has the specified error message.
# Parameters:
# $1 - Remote Host User
# $2 - Remote Host
# $3 - Path to the BCP log file
# $4 - Path to the Logger File for logging this script's run
# Returns:
# 0 - if the BCP script has no occurence of the Error Message
# 9 - for all other cases
# ---------------------------------------------------------------------------------------------------------------------------------------


# ---------------------------
# Variables And Initial Setup
# ---------------------------
_SCRIPT_NAME=`basename $0`
USER=$1
SERVER=$2
BCP_LOG_FILE=$3
ERROR_MESSAGE="error"
_LOG_FILE=$4

# ----------------------------
# Check the BCP Log for Errors
# ----------------------------
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Entered Args are: \nUser = $1\nServer = $2\nBCP Log File Path = $3\nLocal Log File Path = $4"

ssh $USER@$SERVER "test -r $BCP_LOG_FILE"
FILE_EXISTS=$?
if [ $FILE_EXISTS != 0 ];
then
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "BCP Log File ($BCP_LOG_FILE) was not Found or isn't Readable. Since status of BCP is unknown, Terminating script with Failed Status."
    exit 1
fi

ERROR_COUNT=`ssh $USER@$SERVER grep -ic $ERROR_MESSAGE $BCP_LOG_FILE`

if [ $ERROR_COUNT != 0 ];
then 
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "BCP Run FAILED! '$ERROR_COUNT' Error(s) found in the BCP log file: $BCP_LOG_FILE."
    exit 1
else
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "BCP Run was SUCCESFUL!"
    exit 0
fi