#!/bin/sh
# -----------------------------------------------------------------------------------------------------------------
# Script to removed all existing cron jobs
# for the user and creating new ones!
# Parameters (Optional):
# $1 : slave
# If a parameter is passed to this script, the parameter
# can only be 'slave' which indicates that the slave cluster
# is ACTING AS the MASTER CLUSTER.
# If no parameter is passed, the script by uses the default
# MASTER AND SLAVE cluster configurations from the configuration files
# -----------------------------------------------------------------------------------------------------------------

# -------------
# Initial setup
# -------------
_SCRIPT_NAME=`basename $0`
_LOG_FILE=./logs/installation.log

_HOST_NAME=`hostname`
_PATH_TO_CURRENT_RELEASE=`pwd`

_NEW_CRON_JOBFILE=./newCron.tmp

#Time (HR of day) in HH format to start the data population!
DATA_POPULATION_TIME=05

# -------------------------------------------------
# Get configuration values from configuration files
# -------------------------------------------------
source $(dirname $0)/env.sh
source $(dirname $0)/$ENV/settings.sh

# ----------------------------------
# Identify Master and Slave Clusters
# ----------------------------------
if [[ -z $1 ]]; then
    _MASTER_CLUSTER=$MASTER_HOST
    _POPULATE="./populate.sh"
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "No parameter is passed to the script, so will create default crons!"
else
    _MASTER_CLUSTER=$SLAVE_HOST
    _POPULATE="./populate.sh slave"
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Since [$1] has been passed to the script, [slave] cluster will be treated as [master] and crons will be created based on this!"
fi

./logger.sh $_SCRIPT_NAME $_LOG_FILE "-------------------------- Setting Up Crons --------------------------"

# ---------------------
# Remove Existing Crons
# ---------------------
crontab -r 2>/dev/null
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Deleted existing cronjobs!"

# -------------------------------
# Create Crons for Master Cluster
# -------------------------------
echo "#!/bin/sh" > $_NEW_CRON_JOBFILE

if [[ "$_HOST_NAME" == "$_MASTER_CLUSTER" ]]; then
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Creating crons for Master cluster [$_HOST_NAME]"
    echo "# Populate JDBM 3 cache data at [$DATA_POPULATION_TIME] HRS every day." >> $_NEW_CRON_JOBFILE
    echo "00 $DATA_POPULATION_TIME * * * . ~/.bash_profile; cd $_PATH_TO_CURRENT_RELEASE; $_POPULATE" >> $_NEW_CRON_JOBFILE
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Data Population cron job created. Will run [$_POPULATE] everyday @ [$DATA_POPULATION_TIME] HRS"
fi

# ------------------------------
# Create Crons for Slave Cluster
# If no paramter is passed
# (Non failover situations)
# ------------------------------
if [[ -z $1 ]]; then
    if [[ "$_HOST_NAME" == "$SLAVE_HOST" ]]; then
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Creating crons for Slave cluster [$_HOST_NAME]"
        echo "# Monitor the running [Master] cluster every [minute]" >> $_NEW_CRON_JOBFILE
        echo "*/1 * * * * . ~/.bash_profile; cd $_PATH_TO_CURRENT_RELEASE; ./failover.sh" >> $_NEW_CRON_JOBFILE
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Messaging fail over cron job created. Will run [failover.sh] every [minute]"
    fi
fi

# Add to Cron Tab
crontab $_NEW_CRON_JOBFILE

# Remove tmp file
rm -f $_NEW_CRON_JOBFILE

./logger.sh $_SCRIPT_NAME $_LOG_FILE "-------------------- Completed Setting Up of Crons --------------------"