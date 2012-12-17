#!/bin/sh
# ---------------------------------------------------------------------------------------------------------------------------------------
# This script pings Cosmessaging instance running on given Host and Port
# Parameters:
# $1 : Host
# $2 : Port
# This script then pings Cosmessaging and
# Returns:
# 0 : If Cosmessaging responds to the ping
# 1 : If ping fails
# ---------------------------------------------------------------------------------------------------------------------------------------

# ---------------------------
# Variables And Initial Setup
# ---------------------------
_SCRIPT_NAME=`basename $0`
_LOG_FILE=./logs/installation.log

# -----------------------
# Ping and Check Response
# -----------------------
RESULT=`curl http://$1:$2/messaging/`
if [[ $RESULT == "" || "$RESULT" =~ *connect* ]]; then
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Executed [curl http://$1:$2] and got [$RESULT] response !"
    exit 1
else
    echo "Cosmessaging cluster on [$1:$2] is running!"
    exit 0
fi