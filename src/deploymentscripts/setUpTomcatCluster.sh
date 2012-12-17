#!/bin/sh
# ---------------------------------------------------------------------------------------------------------------------------------------
# Script to sets up the Tomcat Cluster using the base installation at TOMCAT_HOME
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

source $(dirname $0)/env.sh

./logger.sh $_SCRIPT_NAME $_LOG_FILE "Setting up tomcat at [$_PATH_TO_TOMCAT] for port [$_PORT] ..."

# -----------------------
# Delete existing Cluster
# -----------------------
if [ -d $_PATH_TO_TOMCAT ];
    then
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Deleting the existing tomcat cluster at [$_PATH_TO_TOMCAT] ..."
        rm -rf $_PATH_TO_TOMCAT
        deleteTomcatStatus=$?
        if [ "0" != "$deleteTomcatStatus" ]; then
            ./logger.sh $_SCRIPT_NAME $_LOG_FILE "FATAL ERROR: Couldn't delete existing tomcat installation at [$_PATH_TO_TOMCAT]. Delete exited with status [$deleteTomcatStatus]. Aborting Installation!"
            exit 1
        fi
        ./logger.sh $_SCRIPT_NAME $_LOG_FILE "Existing tomcat cluster deleted successfully!"
fi

# ---------------------------
# Create the required folders
# ---------------------------
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Creating the tomcat folders at [$_PATH_TO_TOMCAT] ..."

mkdir -p $_PATH_TO_TOMCAT/conf/
mkdir -p $_PATH_TO_TOMCAT/bin
mkdir -p $_PATH_TO_TOMCAT/logs
mkdir -p $_PATH_TO_TOMCAT/temp
mkdir -p $_PATH_TO_TOMCAT/conf/Catalina/localhost/

./logger.sh $_SCRIPT_NAME $_LOG_FILE "Successfully created the tomcat folders at [$_PATH_TO_TOMCAT]"

# ---------------------
# Copy the Tomcat Files
# ---------------------
cp -r $TOMCAT_HOME/conf/ $_PATH_TO_TOMCAT/
cp -r $TOMCAT_HOME/webapps/ $_PATH_TO_TOMCAT/
cp server.xml $_PATH_TO_TOMCAT/conf/
cp -r $TOMCAT_HOME/bin/ $_PATH_TO_TOMCAT/
./copyTomcatUsersXml.sh $ENV $_PATH_TO_TOMCAT

./logger.sh $_SCRIPT_NAME $_LOG_FILE "Copied all the tomcat files from [$TOMCAT_HOME]!"

# ------------------------
# Modify the default ports
# ------------------------
sed -i 's/8080/'${SERVER_PORT}'/' $_PATH_TO_TOMCAT/conf/server.xml
sed -i 's/8005/'${SHUTDOWN_PORT}'/' $_PATH_TO_TOMCAT/conf/server.xml

./logger.sh $_SCRIPT_NAME $_LOG_FILE "Modified the tomcat ports! New server port is [$SERVER_PORT]"

# ------------------------
# Create the context files
# ------------------------
if [[ $OSTYPE == 'cygwin' ]]; then
    DEPLOYMENT_DIR=`cygpath -w $DEPLOYMENT_DIR`
fi

echo "<?xml version=\"1.0\" encoding=\"utf-8\"?><Context path=\"/messaging\" docBase=\"$DEPLOYMENT_DIR/messaging\"/>" > $_PATH_TO_TOMCAT/conf/Catalina/localhost/messaging.xml

echo "<?xml version=\"1.0\" encoding=\"utf-8\"?><Context path=\"/amqconsole\" docBase=\"$DEPLOYMENT_DIR/activemq-web-console-secure-5.7.0\"/>" > $_PATH_TO_TOMCAT/conf/Catalina/localhost/amqconsole.xml

./logger.sh $_SCRIPT_NAME $_LOG_FILE "Created the context files!"

# ------------------------------------
# Set Environment Variables for Tomcat
# ------------------------------------

if [[ $OSTYPE == 'cygwin' ]]; then
    CATALINA_BASE=`cygpath -u $_PATH_TO_TOMCAT`
fi

TOMCAT_PID_FILE=$_PATH_TO_TOMCAT/$_PORT.pid

if [[ $OSTYPE == 'cygwin' ]]; then
    CATALINA_PID=`cygpath -u $TOMCAT_PID_FILE`
fi

echo "#!/bin/sh" > $_PATH_TO_TOMCAT/bin/setenv.sh
echo "export TOMCAT_HOME=$TOMCAT_HOME"                                  | tee -a $_PATH_TO_TOMCAT/bin/setenv.sh $_LOG_FILE
echo "export CATALINA_PID=$TOMCAT_PID_FILE"                             | tee -a $_PATH_TO_TOMCAT/bin/setenv.sh $_LOG_FILE
echo "export CATALINA_HOME=$TOMCAT_HOME"                                | tee -a $_PATH_TO_TOMCAT/bin/setenv.sh $_LOG_FILE
echo "export CATALINA_BASE=$_PATH_TO_TOMCAT"                            | tee -a $_PATH_TO_TOMCAT/bin/setenv.sh $_LOG_FILE
echo "export CATALINA_OPTS=\"$CATALINA_OPTS -Denv=$ENV -Dport=$_PORT\"" | tee -a $_PATH_TO_TOMCAT/bin/setenv.sh $_LOG_FILE

setEnvFileAsString=`cat $_PATH_TO_TOMCAT/bin/setenv.sh`
./logger.sh $_SCRIPT_NAME $_LOG_FILE "-------------------- Values Set in [$_PATH_TO_TOMCAT/bin/setenv.sh] : \n$setEnvFileAsString"
./logger.sh $_SCRIPT_NAME $_LOG_FILE "Environment Variables have been set!"

./logger.sh $_SCRIPT_NAME $_LOG_FILE "Successfully COMPLETED set up tomcat at [$_PATH_TO_TOMCAT] for port [$_PORT]"