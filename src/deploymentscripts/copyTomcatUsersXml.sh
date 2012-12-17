#!/bin/bash
# ---------------------------------------------------------------------------------------------------------------------------------------
# Script to copy tomcat-users.xml based on Environment.
# FOR PRODUCTION : copy this file FROM $TOMCAT_HOME/conf/ to $_PATH_TO_TOMCAT/conf
# FOR OTHER ENVs : copy this file FROM COS MESSAGING DISTRIBUTION to $_PATH_TO_TOMCAT/conf
# Parameters:
# $1 - The Environment
# $2 - Path to Tomcat
# Return:
# 0 if copy is successful
# 1 for all other cases
# ---------------------------------------------------------------------------------------------------------------------------------------

# ---------------------------
# Variables And Initial Setup
# ---------------------------
_LOG_FILE=./logs/installation.log
_SCRIPT_NAME=`basename $0`
ENV=$1
_PATH_TO_TOMCAT=$2

# ---------
# File Copy
# ---------

if [[ $ENV == "production" || $ENV == "prod" ]]; then
    if [[ -r "$TOMCAT_HOME/conf/tomcat-users.xml" ]]; then
        cp $TOMCAT_HOME/conf/tomcat-users.xml $_PATH_TO_TOMCAT/conf
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "[tomcat-users.xml] successfully copied from TOMCAT_HOME @ [$TOMCAT_HOME/conf/]"
        exit 0
    else
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Either [tomcat-users.xml] was not found in the tomcat home [$TOMCAT_HOME/conf/] or there was no Read Permission!"
        exit 1
    fi
else
    if [[ -r "tomcat-users.xml" ]]; then
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "copying [tomcat-users.xml] from the Distribution"
        cp tomcat-users.xml $_PATH_TO_TOMCAT/conf
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "[tomcat-users.xml] successfully copied from the Distribution"
        exit 0
    else
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Either [tomcat-users.xml] was not found in the Distribution [$DEPLOYMENT_DIR] or there was no Read Permission!"
        exit 1
    fi
fi