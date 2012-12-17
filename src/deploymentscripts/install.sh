#!/bin/sh
# ---------------------------------------------------------------------------------------------------------------------------------------
# This script sets up
# 1) All the neccesary folders
# 2) Environment Configurations
# 3) Populate the JDBM3 Data Cache
# 4) Tomcat Server Base (Shutdown existing & recreate)
# 5) Symbolic Links
# It also does some housekeeping activities.
# ---------------------------------------------------------------------------------------------------------------------------------------

# ---------------------------
# Variables And Initial Setup
# ---------------------------
_LOG_DIRECTORY=logs
_LOG_FILE=./$_LOG_DIRECTORY/installation.log

_ENVIRONMENT_CONF_FILE=env.sh
_HOST_NAME=`hostname`
_SCRIPT_NAME=`basename $0`

# -----------------------------------------------------------
# Create the Log and DataBase Directories if they don't exist
# -----------------------------------------------------------
if [[ ! -d $_LOG_DIRECTORY ]]; then
    mkdir $_LOG_DIRECTORY
fi

./logger.sh $_SCRIPT_NAME $_LOG_FILE "******************************************  INSTALLING COSMESSAGING  *****************************************"

# ------------------------------------------
# Verify and Configure Environment Variables
# ------------------------------------------
if [ -z "$TOMCAT_HOME" ]; then
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR: TOMCAT_HOME is not set! Installation cannot continue without it. Aborting Installation!"
    exit 1
fi

./logger.sh $_SCRIPT_NAME $_LOG_FILE "TOMCAT_HOME = [$TOMCAT_HOME]"
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Path to Deployed Release's Tomcat = [$RELEASE_DIR/tomcat]"

# --------------------------------------------------
# Configuration to ensure script runs in cygwin also
# --------------------------------------------------
if [[ ! -r $_ENVIRONMENT_CONF_FILE ]]; then
    # Create a file to save the environment configuration so that installation can be re-run
    echo "#!/bin/sh" > $_ENVIRONMENT_CONF_FILE
    echo "export ENV=$ENV"                              | tee -a $_ENVIRONMENT_CONF_FILE $_LOG_FILE
    if [[ $OSTYPE == 'cygwin' ]]; then
        TOMCAT_HOME=`cygpath -u $TOMCAT_HOME`
        RELEASE_DIR=`cygpath -u $RELEASE_DIR`
        DEPLOYMENT_DIR=`cygpath -u $DEPLOYMENT_DIR`
        JDBM_MASTER_DIR=`cygpath -u $JDBM_MASTER_DIR`
        JDBM_SLAVE_DIR=`cygpath -u $JDBM_SLAVE_DIR`
        KAHADB_MASTER_DIR=`cygpath -u $KAHADB_MASTER_DIR`
        KAHADB_SLAVE_DIR=`cygpath -u $KAHADB_SLAVE_DIR`
        BACKUP_DIR=`cygpath -u $BACKUP_DIR`
    fi
    echo "export TOMCAT_HOME=$TOMCAT_HOME"              | tee -a $_ENVIRONMENT_CONF_FILE $_LOG_FILE
    echo "export RELEASE_DIR=$RELEASE_DIR"              | tee -a $_ENVIRONMENT_CONF_FILE $_LOG_FILE
    echo "export DEPLOYMENT_DIR=$DEPLOYMENT_DIR"        | tee -a $_ENVIRONMENT_CONF_FILE $_LOG_FILE
    echo "export JDBM_MASTER_DIR=$JDBM_MASTER_DIR"      | tee -a $_ENVIRONMENT_CONF_FILE $_LOG_FILE
    echo "export JDBM_SLAVE_DIR=$JDBM_SLAVE_DIR"        | tee -a $_ENVIRONMENT_CONF_FILE $_LOG_FILE
    echo "export KAHADB_MASTER_DIR=$KAHADB_MASTER_DIR"  | tee -a $_ENVIRONMENT_CONF_FILE $_LOG_FILE
    echo "export KAHADB_SLAVE_DIR=$KAHADB_SLAVE_DIR"  | tee -a $_ENVIRONMENT_CONF_FILE $_LOG_FILE
    echo "export BACKUP_DIR=$BACKUP_DIR"                | tee -a $_ENVIRONMENT_CONF_FILE $_LOG_FILE

    chmod 744 $_ENVIRONMENT_CONF_FILE
    sed -i -e 's/\r//' $_ENVIRONMENT_CONF_FILE
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Created the environment configuration file so that installation can be re-run!"
fi

source ./$_ENVIRONMENT_CONF_FILE

if [[ "$ENV" =~ local* ]]; then
    _HOST_NAME="localhost"
fi

./logger.sh $_SCRIPT_NAME $_LOG_FILE "Host: [$_HOST_NAME] ---> Environment: [$ENV]"

# ----------------------------
# Indentify Host and Ports for
# Master and Slave Clusters
# ----------------------------
(
    _CURRENT_DIR=`pwd`;
    cd $_CURRENT_DIR/$ENV;
    MASTER_HOST=`grep -ri "IS_MASTER=true" * | awk '{print $1}' | sed 's/.sh://g' | cut -d'/' -f1`;
    MASTER_PORT=`grep -ri "IS_MASTER=true" * | awk '{print $1}' | sed 's/.sh://g' | cut -d'/' -f2`;
    SLAVE_HOST=`grep -ri "IS_SLAVE=true" * | awk '{print $1}' | sed 's/.sh://g' | cut -d'/' -f1`;
    SLAVE_PORT=`grep -ri "IS_SLAVE=true" * | awk '{print $1}' | sed 's/.sh://g' | cut -d'/' -f2`;
    cd $_CURRENT_DIR;
    echo -e "\nexport MASTER_HOST=$MASTER_HOST"         | tee -a ./$ENV/settings.sh $_LOG_FILE
    echo "export MASTER_PORT=$MASTER_PORT"              | tee -a ./$ENV/settings.sh $_LOG_FILE
    echo "export SLAVE_HOST=$SLAVE_HOST"                | tee -a ./$ENV/settings.sh $_LOG_FILE
    echo "export SLAVE_PORT=$SLAVE_PORT"                | tee -a ./$ENV/settings.sh $_LOG_FILE
)

# ------------------------------------------------
# Shutdown, Install and Start Tomcat for each port
# ------------------------------------------------
for portFileName in $(ls ${ENV}/${_HOST_NAME}/[0-9]*.sh);
do
    portFileName=$(basename $portFileName)
    PORT=${portFileName%.*}

    export CATALINA_BASE="$RELEASE_DIR/tomcat/$PORT"
    export PORT=$PORT

    source $(dirname $0)/${ENV}/${_HOST_NAME}/${PORT}.sh

    if [[ "$IS_MASTER" == "true" ]]; then
        echo "export CATALINA_BASE_MASTER=$CATALINA_BASE"          | tee -a ./$ENV/settings.sh $_LOG_FILE
    else
        echo "export CATALINA_BASE_SLAVE=$CATALINA_BASE"           | tee -a ./$ENV/settings.sh $_LOG_FILE
    fi

    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "---------------------------------------------------------------------------- Setting up Tomcat for port [$PORT]"

    # Shutdown any tomcat instance running on this port
    ./shutDownTomcat.sh $CATALINA_BASE $PORT
    tomcatShutDownStatus=$?
    if [ "0" != "$tomcatShutDownStatus" ]; then
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR: Unable to Stop Tomcat on port [$PORT]. Shutdown exited with status [$tomcatShutDownStatus]. Aborting Installation!"
        exit 1
    fi

    # Set up a new tomcat for this port
    ./setUpTomcatCluster.sh $CATALINA_BASE $PORT
    tomcatInstallationStatus=$?
    if [ "0" != "$tomcatInstallationStatus" ]; then
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR: Couldn't install tomcat at [$CATALINA_BASE]. Installation exited with status [$tomcatInstallationStatus]. Aborting Installation!"
        exit 1
    fi

    # Start the server only for the master instance
    if [[ "$IS_MASTER" == "true" ]]; then
        ./startTomcat.sh $CATALINA_BASE $PORT
        tomcatStartUpStatus=$?
        if [ "0" != "$tomcatStartUpStatus" ]; then
            ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR: Unable to Start Tomcat on port [$PORT]. Startup exited with status [$tomcatStartUpStatus]. Aborting Installation!"
            exit 1
        fi
    else
        # lots TODO : DONT START. LATER CREATE CRON. CRON TO POLL MASTER INSTANCE ETC ETC
       ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Not starting server on port [$PORT] as this port is for high availability!"
    fi
    ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Completed Setup of Tomcat for port [$PORT]"
done

# -----------------------------
# Configure the symbolic links
# -----------------------------
if [[ -e "../latest" ]]; then
    rm ../previous 2>/dev/null
    mv ../latest ../previous
fi

shopt -s extglob

./logger.sh $_SCRIPT_NAME $_LOG_FILE "Creating Symbolic link for [$DEPLOYMENT_DIR] --> as --> [latest]"

(cd ..;
currentDir=`pwd`
echo "Temporarily moved to directory [$currentDir]"
ln -fs $DEPLOYMENT_DIR latest;

echo "Symbolic Link created successfully!"

# ---------------
# Do Housekeeping
# ---------------
echo "Cleaning up the release folder [$RELEASE_DIR]"

rm -r !("previous"|"latest"|`basename $(readlink previous)`|`basename $(readlink latest)`|"kahadb"|"tomcat"|"jdbm3"|"backup") 2>/dev/null)

./logger.sh $_SCRIPT_NAME $_LOG_FILE "Completed Housekeeping Activities!"

# ---------------
# Create Cronjobs
# ---------------
./createCronJob.sh

./logger.sh $_SCRIPT_NAME $_LOG_FILE "************************************  COMPLETED COSMESSAGING INSTALLATION  ***********************************"