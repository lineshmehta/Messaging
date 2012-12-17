#!/bin/sh
# ---------------------------------------------------------------------------------------------------------------------------------------
# This script inserts the Test Data for Messaging
# ---------------------------------------------------------------------------------------------------------------------------------------

# ---------------------------
# Variables And Initial Setup
# ---------------------------
source $(dirname $0)/env.sh
source $(dirname $0)/$ENV/settings.sh

_LOG_FILE=./logs/dataPopulation.log
_SCRIPT_NAME=`basename $0`

_CONFLUENCE_LINK_TO_FILE=http://mw:8080/download/attachments/4653743/customer_details_view.csv
_CHUNK_SIZE=5
_JDBM_JAR=jdbm-batch.jar
_CUSTOMER_DATA="customer"

./logger.sh $_SCRIPT_NAME $_LOG_FILE "Inserting Test Data..."

# ------------------
# Create the folders
# ------------------
_JDBM_TEST_DATA_DIR="$JDBM_MASTER_DIR/../testdata"
mkdir -p $_JDBM_TEST_DATA_DIR 2> /dev/null

# -----------------------
# Download Test Data File
# -----------------------
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Downloading file [$_CONFLUENCE_LINK_TO_FILE] from confluence to [$_JDBM_TEST_DATA_DIR] ..."
wget -N $_CONFLUENCE_LINK_TO_FILE -P $_JDBM_TEST_DATA_DIR 2> /dev/null
if [ $? != 0 ]; then
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR: Couldn't download the file. Aborting Data Population!"
    exit 1
fi
./logger.sh $_SCRIPT_NAME $_LOG_FILE "File successfully downloaded to [$_JDBM_TEST_DATA_DIR]"

# ----------------------------------------------
# Populate the Data in Master and Slave Clusters
# ----------------------------------------------
for cluster in master slave
do
    java "-Dmaster=$cluster" -jar $_JDBM_JAR $_JDBM_TEST_DATA_DIR $_CUSTOMER_DATA $_CHUNK_SIZE | tee -a $_LOG_FILE
    _EXIT_STATUS=$?
    if [ $_EXIT_STATUS != 0 ]; then
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR : Couldn't insert test data in [$cluster] cluster. Ignoring and continuing..."
    else
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Completed Insertion of Test Data in [$cluster]!"
    fi
done