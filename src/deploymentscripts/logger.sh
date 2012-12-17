#!/bin/sh
# ---------------------------------------------------------------------------------------------------------------------------------------
# Script for logging in a format similar to Logback.
# Parameters:
# $1 - Name of the script file
# $2 - Log file
# $3 - Data to be logged
# ---------------------------------------------------------------------------------------------------------------------------------------

_SCRIPT_NAME=$1
_LOG=$2
_MESSAGE=$3

echo -e "$(date +%Y-%m-%d' '%T) SCRIPT=\"$_SCRIPT_NAME\" - $_MESSAGE" | tee -a $_LOG