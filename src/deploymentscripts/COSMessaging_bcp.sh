#!/bin/sh
##############
# SYBASE nasse
##############
#export DSQUERY=KM_Nasse
. $HOME/.profile

NUMBER_OF_EXPECTED_ARGS=2
E_WRONG_ARGS=16
E_FILES_NOT_FOUND=2

#PATHS/ENV
PASSWD_FILE=/home/cosadmin/ENV/passwd.txt
OUT_DIR=/home/cosadmin/OUT_MESSAGING
LOG=/home/cosadmin/LOG

# Set connection info --> Create password.txt file when user is decided (or created)
SERVER=KM_Nasse; export SERVER
USERNAME=cosindex; export USERNAME
PASSWD=`cat $PASSWD_FILE | grep "^$SERVER" | grep "$USERNAME" | cut -f3`; export PASSWD

NOW=`date +%Y%m%d_%H%M%S`
LOCK_FILE=/home/cosadmin/LOG/COSMessaging_bcp.lock
MAIL_LIST=Giao-The.Cung@telenor.com

# Files names
CUSTOMER_DETAILS=$OUT_DIR/customer_details_view.csv
SUBSCRIPTION_PRODUCT=$OUT_DIR/subscription_product_view.csv
RESOURCE_DETAILS=$OUT_DIR/resource_details_view.csv
USER_RESOURCE_DETAILS=$OUT_DIR/user_resource_details_view.csv
MASTER_CUSTOMER_DETAILS=$OUT_DIR/master_customer_details_view.csv
MASTER_CUSTOMER_KURT_ID_DETAILS=$OUT_DIR/kurt_master_id_details_view.csv

if [ -f $LOCK_FILE ] 
then
{
	mailx -s "COSMessaging bcp extraction in progress or previous run failed" $MAIL_LIST < $LOG/COSMessaging_bcp.err
	exit 1
}
fi
touch $LOCK_FILE

# Remove old files
rm $OUT_DIR/*.csv 2>/dev/null
rm $LOG/COSMessaging_bcp.log 2>/dev/null
rm $LOG/COSMessaging_bcp.err 2>/dev/null

echo
echo "#####################################" | tee -a $LOG/COSMessaging_bcp.log
echo "##  Starting COSMessaging bcp extraction" | tee -a $LOG/COSMessaging_bcp.log
echo "##  Server: $SERVER" | tee -a $LOG/COSMessaging_bcp.log
echo "##    User: $USERNAME" | tee -a $LOG/COSMessaging_bcp.log
echo "##    Time: $NOW" | tee -a $LOG/COSMessaging_bcp.log
echo "#####################################" | tee -a $LOG/COSMessaging_bcp.log
echo

if [ ! -d $OUT_DIR ]; then mkdir $OUT_DIR; fi
if [ ! -d $OUT_DIR ]; then exit 1; fi

cd $OUT_DIR

echo "Running BCP" | tee -a $LOG/COSMessaging_bcp.log

# BCP prepare
SERVER_CREDENTIALS="-S$SERVER -U$USERNAME"
CUSTOMER_DETAILS_CMD="CustomerMaster..COSmsg_customer_details_view out $CUSTOMER_DETAILS $SERVER_CREDENTIALS"
SUBSCRIPTION_PRODUCT_CMD="ProductCatalog_new..COSmsg_subscription_product_view out $SUBSCRIPTION_PRODUCT $SERVER_CREDENTIALS"
RESOURCE_DETAILS_CMD="CosSecurity..COSmsg_resource_details_view out $RESOURCE_DETAILS $SERVER_CREDENTIALS"
USER_RESOURCE_DETAILS_CMD="CosSecurity..COSmsg_user_resource_details_view out $USER_RESOURCE_DETAILS $SERVER_CREDENTIALS"
MASTER_CUSTOMER_DETAILS_CMD="CMMaster..COSmsg_master_customer_details_view out $MASTER_CUSTOMER_DETAILS $SERVER_CREDENTIALS"
MASTER_CUSTOMER_KURT_ID_DETAILS_CMD="CMMaster..COSmsg_master_customer_kurt_id_details_view out $MASTER_CUSTOMER_KURT_ID_DETAILS $SERVER_CREDENTIALS"

# BCP execution
#bcp $CUSTOMER_DETAILS_CMD -c -t";" -J"iso_1" << EOF > $LOG/customer_details.log
bcp $CUSTOMER_DETAILS_CMD -c -t"|" -J"utf8" << EOF > $LOG/customer_details.log
$PASSWD
EOF
bcp $SUBSCRIPTION_PRODUCT_CMD -c -t"|" -J"utf8" << EOF > $LOG/subscription_product_details.log
$PASSWD
EOF
bcp $RESOURCE_DETAILS_CMD -c -t"|" -J"utf8" << EOF > $LOG/resource_details.log
$PASSWD
EOF
bcp $USER_RESOURCE_DETAILS_CMD -c -t"|" -J"utf8" << EOF > $LOG/user_resource_details.log
$PASSWD
EOF
bcp $MASTER_CUSTOMER_DETAILS_CMD -c -t"|" -J"utf8" << EOF > $LOG/master_customer_details.log
$PASSWD
EOF
bcp $MASTER_CUSTOMER_KURT_ID_DETAILS_CMD -c -t"|" -J"utf8" << EOF > $LOG/master_customer_kurt_id_details.log
$PASSWD
EOF

# Remove lock file, ready for re-execution
rm $LOCK_FILE 2>/dev/null 

echo BCP Output Folder=$OUT_DIR | tee -a $LOG/COSMessaging_bcp.log

#error control
grep -i "error"  $LOG/customer_details.log $LOG/subscription_product_details.log $LOG/resource_details.log $LOG/user_resource_details.log $LOG/master_customer_details.log $LOG/master_customer_kurt_id_details.log > $LOG/COSMessaging_bcp.err

if [ -s $LOG/COSMessaging_bcp.err ]
then
{
	echo "There was an error and not all csv files were generated" | tee -a $LOG/COSMessaging_bcp.log
	exit $E_FILES_NOT_FOUND
}
fi

if [ -f $CUSTOMER_DETAILS ] && [ -f $SUBSCRIPTION_PRODUCT ] && [ -f $RESOURCE_DETAILS ] && [ -f $USER_RESOURCE_DETAILS ] && [ -f $MASTER_CUSTOMER_DETAILS ] && [ -f $MASTER_CUSTOMER_KURT_ID_DETAILS ]; then
	echo "CSV files generated successfully" > $LOG/COSMessaging_bcp.log
else
	echo "There was an error and not all csv files were generated" | tee -a $LOG/COSMessaging_bcp.log
	exit $E_FILES_NOT_FOUND
fi

exit 0