package com.telenor.cos.messaging.producers.xpath;


/**
 * Sets xpath expressions for unittests that tests translators/filter
 *
 * @author Eirik Bergande (Capgemini)
 */
public abstract class AbstractSubscriptionXpathUnitTest extends AbstractXpathUnitTest {

    protected final static String XML_NEW = "<insert schema=\"SUBSCRIPTION\">"
            + "<values>"
            + "<cell name=\"SUBSCR_ID\" type=\"NUMERIC\">32143317</cell>"
            + "<cell name=\"ACC_ID\" type=\"INT\">999999001</cell>"
            + "<cell name=\"CUST_ID_USER\" type=\"NUMERIC\">6935066</cell>"
            + "<cell name=\"SUBSCR_VALID_FROM_DATE\" type=\"DATETIME\">20120510 00:00:00:000</cell>"
            + "<cell name=\"SUBSCR_VALID_TO_DATE\" type=\"DATETIME\" />"
            + "<cell name=\"SUBSCR_HAS_SECRET_NUMBER\" type=\"CHAR\">N</cell>"
            + "<cell name=\"DIRECTORY_NUMBER_ID\" type=\"NUMERIC\">580000506010</cell>"
            + "<cell name=\"CONTRACT_ID\" type=\"INT\">12716621</cell>"
            + "<cell name=\"S212_PRODUCT_ID\" type=\"NUMERIC\">04713</cell>"
            + "<cell name=\"SUBSCR_STATUS_ID\" type=\"CHAR\"> </cell>"
            + "</values>"
            + "</insert>";

    protected final static String XML_UPDATE_VALUES = "<update schema=\"SUBSCRIPTION\">" + "<values>"
            + "<cell name=\"SUBSCR_ID\" type=\"NUMERIC\">32143317</cell>"
            + "<cell name=\"ACC_ID\" type=\"INT\">999999001</cell>"
            + "<cell name=\"CUST_ID_USER\" type=\"NUMERIC\">6935066</cell>"
            + "<cell name=\"SUBSCR_VALID_FROM_DATE\" type=\"DATETIME\">20120505 00:00:00:000</cell>"
            + "<cell name=\"SUBSCR_VALID_TO_DATE\" type=\"DATETIME\" />"
            + "<cell name=\"INFO_IS_DELETED\" type=\"CHAR\">Y</cell>"
            + "<cell name=\"SUBSCR_HAS_SECRET_NUMBER\" type=\"CHAR\" />" + "</values>";

    protected final static String XML_UPDATE_OLDVALUES = "<oldValues>"
            + "<cell name=\"SUBSCR_ID\" type=\"NUMERIC\">32143317</cell>"
            + "<cell name=\"ACC_ID\" type=\"INT\">999999001</cell>"
            + "<cell name=\"CUST_ID_USER\" type=\"NUMERIC\">6935066</cell>"
            + "<cell name=\"SUBSCR_VALID_FROM_DATE\" type=\"DATETIME\">20120505 00:00:00:000</cell>"
            + "<cell name=\"SUBSCR_VALID_TO_DATE\" type=\"DATETIME\" />"
            + "<cell name=\"SUBSCR_HAS_SECRET_NUMBER\" type=\"CHAR\" />" + "</oldValues>" + "</update>";

    protected final static String XML_UPDATE_OLDVALUES_ACCOUNT_NOT_SET = "<oldValues>"
            + "<cell name=\"SUBSCR_ID\" type=\"NUMERIC\">32143317</cell>"
            + "<cell name=\"CUST_ID_USER\" type=\"NUMERIC\">6935066</cell>"
            + "<cell name=\"SUBSCR_VALID_FROM_DATE\" type=\"DATETIME\">20120505 00:00:00:000</cell>"
            + "<cell name=\"SUBSCR_VALID_TO_DATE\" type=\"DATETIME\" />"
            + "<cell name=\"INFO_IS_DELETED\" type=\"CHAR\">Y</cell>"
            + "<cell name=\"SUBSCR_TYPE_ID\" type=\"CHAR\">TC</cell>"
            + "<cell name=\"SUBSCR_HAS_SECRET_NUMBER\" type=\"CHAR\" />" + "</oldValues>" + "</update>";

    protected final static String XML_FULL = "<tran eventId=\"102:00000000000029d5000014eb0001000014ea002900009c8d00930ee20000000000020001\">"
            + "<insert schema=\"Subscription\">"
            + "<values>"
            + "<cell name=\"subscr_id\" type=\"NUMERIC\">32143317</cell>"
            + "</values>"
            + "</insert></tran>";

    protected final static String XML_NEW_CUSTOMER = "<insert schema=\"Customer\">"
            + "<values>"
            + "<cell name=\"cust_id\" type=\"NUMERIC\">456</cell>"
            + "<cell name=\"name\" type=\"CHAR\">Nils</cell>"
            + "<cell name=\"middle_name\" type=\"CHAR\">Lasse Basse</cell>"
            + "<cell name=\"last_name\" type=\"CHAR\">Olsen</cell>"
            + "<cell name=\"cust_unit_number\" type=\"CHAR\">666999</cell>"
            + "</values>"
            + "</insert>";

    protected final static String XML_UPDATED_CUSTOMER = "<update schema=\"Customer\">"
            + "<values>"
            + "<cell name=\"cust_id\" type=\"NUMERIC\">456</cell>"
            + "<cell name=\"name\" type=\"CHAR\">Nils</cell>"
            + "<cell name=\"middle_name\" type=\"CHAR\">Lasse Basse</cell>"
            + "<cell name=\"last_name\" type=\"CHAR\">Olsen</cell>"
            + "<cell name=\"cust_unit_number\" type=\"CHAR\">666999</cell>"
            + "</values>"
            + "</update>";

    protected final static String XML_TYPE_CHANGE = "<update schema=\"SUBSCRIPTION\">"
            + "<values>"
            + "<cell name=\"cust_id\" type=\"NUMERIC\">456</cell>"
            + "<cell name=\"name\" type=\"CHAR\">Nils</cell>"
            + "<cell name=\"middle_name\" type=\"CHAR\">Lasse Basse</cell>"
            + "<cell name=\"last_name\" type=\"CHAR\">Olsen</cell>"
            + "<cell name=\"cust_unit_number\" type=\"CHAR\">666999</cell>"
            + "<cell name=\"S212_PRODUCT_ID\" type=\"CHAR\">788</cell>"
            + "</values>"
            + "<oldValues>"
            + "<cell name=\"SUBSCR_ID\" type=\"NUMERIC\">32143317</cell>"
            + "</oldValues>"
            + "</update>";

    protected final static String XML_NEW_ACCOUNT = "<insert schema=\"Account\">"
            + "<values>"
            + "<cell name=\"acc_id\" type=\"NUMERIC\">456</cell>"
            + "<cell name=\"acc_name\" type=\"CHAR\">Superaccount</cell>"
            + "<cell name=\"acc_type_id\" type=\"CHAR\">NSFO</cell>"
            + "<cell name=\"acc_status_id\" type=\"CHAR\">55</cell>"
            + "<cell name=\"acc_paym_type_id\" type=\"CHAR\">888</cell>"
            + "<cell name=\"cust_id_payer\" type=\"NUMERIC\">123</cell>"
            + "<cell name=\"cust_id_resp\" type=\"NUMERIC\">789</cell>"
            + "<cell name=\"e_invoice_address\" type=\"CHAR\">Area 52</cell>"
            + "<cell name=\"acc_inv_medium\" type=\"CHAR\">Letter</cell>"
            + "</values>"
            + "</insert>";

    protected final static String XML_UPDATE_ACCOUNT = "<update schema=\"Account\">"
            + "<values>"
            + "<cell name=\"acc_id\" type=\"NUMERIC\">456</cell>"
            + "<cell name=\"acc_name\" type=\"CHAR\">Superaccount</cell>"
            + "<cell name=\"acc_type_id\" type=\"CHAR\">NSFO</cell>"
            + "<cell name=\"acc_status_id\" type=\"CHAR\">55</cell>"
            + "<cell name=\"acc_paym_type_id\" type=\"CHAR\">888</cell>"
            + "<cell name=\"cust_id_payer\" type=\"NUMERIC\">123</cell>"
            + "<cell name=\"cust_id_resp\" type=\"NUMERIC\">789</cell>"
            + "<cell name=\"e_invoice_address\" type=\"CHAR\">Area 52</cell>"
            + "<cell name=\"acc_inv_medium\" type=\"CHAR\">Letter</cell>"
            + "</values>"
            + "<oldValues>"
            + "<cell name=\"acc_id\" type=\"NUMERIC\">123</cell>"
            + "<cell name=\"acc_name\" type=\"CHAR\">Superaccount33</cell>"
            + "<cell name=\"acc_type_id\" type=\"CHAR\">NS</cell>"
            + "<cell name=\"acc_status_id\" type=\"CHAR\">47</cell>"
            + "<cell name=\"acc_paym_type_id\" type=\"CHAR\">654</cell>"
            + "<cell name=\"cust_id_payer\" type=\"NUMERIC\">852</cell>"
            + "<cell name=\"cust_id_resp\" type=\"NUMERIC\">963</cell>"
            + "<cell name=\"e_invoice_address\" type=\"CHAR\">Area 64</cell>"
            + "<cell name=\"acc_inv_medium\" type=\"CHAR\">mail</cell>"
            + "</oldValues>"
            + "</update>";
}
