package com.telenor.cos.messaging.util;

public final class SubscriptionTestXml {

    public static final String S212_PRODUCT_ID = "04713";
    public static final String PRODUCT_ID = "3995";
    public static final String DEALER_ID = "6662";
    public static final Long ACCOUNT_ID = Long.valueOf(999);
    public static final Long ID_OF_AGGREGATED_SUBSCRIPTION = Long.valueOf(1);
    public static final Long ID_OF_SINGLE_SUBSCRIPTION_INSERT = Long.valueOf(222);

    private SubscriptionTestXml() {
        // NOP
    }


    public final static String INSERT_XML =
            "<dbStream>"
                    + "<tran eventId=\"102:00000000000029d5000014eb0001000014ea002900009c8d00930ee20000000000020001\">"
                    + "<insert schema=\"SUBSCRIPTION\">" + "<values>"
                    + "<cell name=\"SUBSCR_ID\" type=\"NUMERIC\">" + ID_OF_AGGREGATED_SUBSCRIPTION + "</cell>"
                    + "<cell name=\"CUST_ID_USER\" type=\"NUMERIC\">6935066</cell>"
                    + "<cell name=\"SUBSCR_VALID_FROM_DATE\" type=\"DATETIME\">20120510 00:00:00:000</cell>"
                    + "<cell name=\"DIRECTORY_NUMBER_ID\" type=\"NUMERIC\">580000506010</cell>"
                    + "<cell name=\"SUBSCR_VALID_TO_DATE\" type=\"DATETIME\" />"
                    + "<cell name=\"SUBSCR_HAS_SECRET_NUMBER\" type=\"CHAR\" />"
                    + "<cell name=\"CONTRACT_ID\" type=\"INT\">12716621</cell>"
                    + "<cell name=\"S212_PRODUCT_ID\" type=\"NUMERIC\">NULL</cell>" + "</values>" + "</insert>"
                    + "</tran>"
                    + "</dbStream>";


    public final static String UPDATE_ONE_XML = "<dbStream>"
            + "<tran eventId=\"102:00000000000029d5000014eb0001000014ea002900009c8d00930ee20000000000020001\">"
            + "<update schema=\"SUBSCRIPTION\">" + "<values>"
            + "<cell name=\"PRODUCT_ID\" type=\"INT\">" + PRODUCT_ID + "</cell>"
            + "<cell name=\"SUBSCR_VALID_FROM_DATE\" type=\"DATETIME\">20120510 00:00:00:000</cell>"
            + "<cell name=\"DIRECTORY_NUMBER_ID\" type=\"NUMERIC\">580000506010</cell>"
            + "<cell name=\"SUBSCR_VALID_TO_DATE\" type=\"DATETIME\" />"
            + "<cell name=\"SUBSCR_HAS_SECRET_NUMBER\" type=\"CHAR\" />"
            + "<cell name=\"CONTRACT_ID\" type=\"INT\">12716621</cell>"
            + "<cell name=\"S212_PRODUCT_ID\" type=\"NUMERIC\">" + S212_PRODUCT_ID + "</cell>" + "</values>"  
            + "<oldValues>"
            + "<cell name=\"SUBSCR_ID\" type=\"NUMERIC\">" + ID_OF_AGGREGATED_SUBSCRIPTION + "</cell>"
            + "</oldValues>"
            + "</update>"
            + "</tran>"
            + "</dbStream>";


    public final static String UPDATE_TWO_XML = "<dbStream>"
            + "<tran eventId=\"102:00000000000029d5000014eb0001000014ea002900009c8d00930ee20000000000020001\">"
            + "<update schema=\"SUBSCRIPTION\">" + "<values>"
            + "<cell name=\"ACC_ID\" type=\"INT\">" + ACCOUNT_ID + "</cell>"
            + "<cell name=\"DEALER_ID\" type=\"INT\">" + DEALER_ID +"</cell>"
            + "<cell name=\"SUBSCR_VALID_FROM_DATE\" type=\"DATETIME\">20120510 00:00:00:000</cell>"
            + "<cell name=\"DIRECTORY_NUMBER_ID\" type=\"NUMERIC\">580000506010</cell>"
            + "<cell name=\"SUBSCR_VALID_TO_DATE\" type=\"DATETIME\" />"
            + "<cell name=\"SUBSCR_HAS_SECRET_NUMBER\" type=\"CHAR\" />"
            + "<cell name=\"CONTRACT_ID\" type=\"INT\">12716621</cell>"
            + "<cell name=\"S212_PRODUCT_ID\" type=\"NUMERIC\">04713</cell>"
            + "</values>"
            + "<oldValues>"
            + "<cell name=\"SUBSCR_ID\" type=\"NUMERIC\">" + ID_OF_AGGREGATED_SUBSCRIPTION + "</cell>"
            + "</oldValues>"
            + "</update>"
            + "</tran>"
            + "</dbStream>";

    public final static String NO_MATCH_ON_ID_XML = "<dbStream>"
            + "<tran eventId=\"102:00000000000029d5000014eb0001000014ea002900009c8d00930ee20000000000020001\">"
            + "<insert schema=\"SUBSCRIPTION\">" + "<values>"
            + "<cell name=\"SUBSCR_ID\" type=\"NUMERIC\">" + ID_OF_SINGLE_SUBSCRIPTION_INSERT + "</cell>"
            + "<cell name=\"ACC_ID\" type=\"INT\">999999001</cell>"
            + "<cell name=\"CUST_ID_USER\" type=\"NUMERIC\">6935066</cell>"
            + "<cell name=\"SUBSCR_VALID_FROM_DATE\" type=\"DATETIME\">20120510 00:00:00:000</cell>"
            + "<cell name=\"DIRECTORY_NUMBER_ID\" type=\"NUMERIC\">580000506010</cell>"
            + "<cell name=\"SUBSCR_VALID_TO_DATE\" type=\"DATETIME\" />"
            + "<cell name=\"SUBSCR_HAS_SECRET_NUMBER\" type=\"CHAR\" />"
            + "<cell name=\"CONTRACT_ID\" type=\"INT\">12716621</cell>"
            + "<cell name=\"S212_PRODUCT_ID\" type=\"NUMERIC\">04713</cell>" + "</values>" + "</insert>"
            + "</tran>"
            + "</dbStream>";

    public final static String SINGLE_UPDATE_WITH_NO_MATCH_ON_ID_XML = "<dbStream>"
            + "<tran eventId=\"102:00000000000029d5000014eb0001000014ea002900009c8d00930ee20000000000020001\">"
            + "<update schema=\"SUBSCRIPTION\">" + "<values>"
            + "<cell name=\"ACC_ID\" type=\"INT\">" + ACCOUNT_ID + "</cell>"
            + "<cell name=\"DEALER_ID\" type=\"INT\">" + DEALER_ID +"</cell>"
            + "<cell name=\"SUBSCR_VALID_FROM_DATE\" type=\"DATETIME\">20120510 00:00:00:000</cell>"
            + "<cell name=\"DIRECTORY_NUMBER_ID\" type=\"NUMERIC\">580000506010</cell>"
            + "<cell name=\"SUBSCR_VALID_TO_DATE\" type=\"DATETIME\"/>"
            + "<cell name=\"SUBSCR_HAS_SECRET_NUMBER\" type=\"CHAR\"/>"
            + "<cell name=\"CONTRACT_ID\" type=\"INT\">12716621</cell>"
            + "<cell name=\"S212_PRODUCT_ID\" type=\"NUMERIC\">04713</cell>" + "</values>"
            + "<oldValues>"
            + "<cell name=\"SUBSCR_ID\" type=\"NUMERIC\">12234</cell>"
            + "</oldValues>"
            + "</update>"
            + "</tran>"
            + "</dbStream>";

    public final static String SUBSCRIPTION_EXPIRED_XML = "<dbStream>"
            + "<tran eventId=\"102:00000000000029d5000014eb0001000014ea002900009c8d00930ee20000000000020001\">"
            + "<update schema=\"SUBSCRIPTION\">" 
            + "<values>"
            + "<cell name=\"SUBSCR_ID\" type=\"NUMERIC\">999</cell>"
            + "<cell name=\"CUST_ID_USER\" type=\"NUMERIC\">6935066</cell>"
            + "<cell name=\"DIRECTORY_NUMBER_ID\" type=\"NUMERIC\">580000506010</cell>"
            + "<cell name=\"SUBSCR_VALID_FROM_DATE\" type=\"DATETIME\">20120505 00:00:00:000</cell>"
            + "<cell name=\"SUBSCR_VALID_TO_DATE\" type=\"DATETIME\">20121010 00:00:00:000</cell>"
            + "<cell name=\"SUBSCR_HAS_SECRET_NUMBER\" type=\"CHAR\" />"
            + "<cell name=\"CONTRACT_ID\" type=\"INT\">12716621</cell>"
            + "<cell name=\"S212_PRODUCT_ID\" type=\"NUMERIC\">NULL</cell>" 
            + "</values>" 
            + "<oldValues>"
            + "<cell name=\"SUBSCR_ID\" type=\"NUMERIC\">999</cell>"
            + "<cell name=\"SUBSCR_VALID_FROM_DATE\" type=\"DATETIME\">20120510 00:00:00:000</cell>"
            + "<cell name=\"SUBSCR_VALID_TO_DATE\" type=\"DATETIME\">NULL</cell>"
            + "</oldValues>"
            + "</update>"
            + "</tran>"
            + "</dbStream>";

    public final static String SUBSCRIPTION_INSERT_XML = "<dbStream>"
            + "<tran eventId=\"102:00000000000029d5000014eb0001000014ea002900009c8d00930ee20000000000020001\">"
            + "<insert schema=\"SUBSCRIPTION\">" + "<values>"
            + "<cell name=\"SUBSCR_ID\" type=\"NUMERIC\">" + ID_OF_AGGREGATED_SUBSCRIPTION + "</cell>"
            + "<cell name=\"CUST_ID_USER\" type=\"NUMERIC\">6935066</cell>"
            + "<cell name=\"SUBSCR_VALID_FROM_DATE\" type=\"DATETIME\">20120510 00:00:00:000</cell>"
            + "<cell name=\"DIRECTORY_NUMBER_ID\" type=\"NUMERIC\">580000506010</cell>"
            + "<cell name=\"SUBSCR_VALID_TO_DATE\" type=\"DATETIME\" />"
            + "<cell name=\"SUBSCR_HAS_SECRET_NUMBER\" type=\"CHAR\">Y</cell>"
            + "<cell name=\"CONTRACT_ID\" type=\"INT\">12716621</cell>"
            + "<cell name=\"S212_PRODUCT_ID\" type=\"NUMERIC\">NULL</cell>" + "</values>" + "</insert>"
            + "</tran>"
            + "</dbStream>";

    public final static String SUBSCRIPTION_UPDATE_ONE_XML = "<dbStream>"
            + "<tran eventId=\"102:00000000000029d5000014eb0001000014ea002900009c8d00930ee20000000000020001\">"
            + "<update schema=\"SUBSCRIPTION\">" + "<values>"
            + "<cell name=\"PRODUCT_ID\" type=\"INT\">" + PRODUCT_ID + "</cell>"
            + "<cell name=\"S212_PRODUCT_ID\" type=\"NUMERIC\">" + S212_PRODUCT_ID + "</cell>"
            + "<oldValues>"
            + "<cell name=\"SUBSCR_ID\" type=\"NUMERIC\">" + ID_OF_AGGREGATED_SUBSCRIPTION + "</cell>"
            + "<cell name=\"SUBSCR_VALID_FROM_DATE\" type=\"DATETIME\">20120510 00:00:00:000</cell>"
            + "<cell name=\"DIRECTORY_NUMBER_ID\" type=\"NUMERIC\">580000506010</cell>"
            + "<cell name=\"SUBSCR_VALID_TO_DATE\" type=\"DATETIME\" />"
            + "<cell name=\"SUBSCR_HAS_SECRET_NUMBER\" type=\"CHAR\">Y</cell>"
            + "<cell name=\"CONTRACT_ID\" type=\"INT\">12716621</cell>"
            + "</values>"
            + "</oldValues>"
            + "</update>"
            + "</tran>"
            + "</dbStream>";

    public final static String SUBSCRIPTION_UPDATE_TWO_XML = "<dbStream>"
            + "<tran eventId=\"102:00000000000029d5000014eb0001000014ea002900009c8d00930ee20000000000020001\">"
            + "<update schema=\"SUBSCRIPTION\">" + "<values>"
            + "<cell name=\"DEALER_ID\" type=\"INT\">" + DEALER_ID +"</cell>"
            + "</values>"
            + "<oldValues>"
            + "<cell name=\"SUBSCR_ID\" type=\"NUMERIC\">" + ID_OF_AGGREGATED_SUBSCRIPTION + "</cell>"
            + "<cell name=\"ACC_ID\" type=\"INT\">" + ACCOUNT_ID + "</cell>"
            + "<cell name=\"SUBSCR_VALID_FROM_DATE\" type=\"DATETIME\">20120510 00:00:00:000</cell>"
            + "<cell name=\"DIRECTORY_NUMBER_ID\" type=\"NUMERIC\">580000506010</cell>"
            + "<cell name=\"SUBSCR_VALID_TO_DATE\" type=\"DATETIME\" />"
            + "<cell name=\"SUBSCR_HAS_SECRET_NUMBER\" type=\"CHAR\">Y</cell>"
            + "<cell name=\"CONTRACT_ID\" type=\"INT\">12716621</cell>"
            + "<cell name=\"S212_PRODUCT_ID\" type=\"NUMERIC\">04713</cell>"
            + "</oldValues>"
            + "</update>"
            + "</tran>"
            + "</dbStream>";

}
