package com.telenor.cos.messaging.producers.xpath;

/**
 * @author Babaprakash D
 *
 */
public class AbstractMasterCustomerXpathUnitTest extends AbstractXpathUnitTest {
    
    protected static final String NEW_MASTER_CUSTOMER_XML = "<insert schema=\"MASTER_CUSTOMER\">\n" 
                + "        <values>\n"
                + "            <cell name=\"MASTER_ID\" type=\"NUMERIC\">369</cell>\n"
                + "            <cell name=\"KURT_ID\" type=\"INT\">3978455</cell>\n"
                + "            <cell name=\"CUST_UNIT_NUMBER\" type=\"INT\">971261730</cell>\n"
                + "            <cell name=\"CUST_LAST_NAME\" type=\"VARCHAR\">TOLLAN</cell>\n"
                + "            <cell name=\"CUST_FIRST_NAME\" type=\"VARCHAR\">ELLEN ANNE</cell>\n"
                + "            <cell name=\"CUST_MIDDLE_NAME\" type=\"VARCHAR\"></cell>\n"
                + "            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">N</cell>\n"
                + "        </values>\n" + "    </insert>\n" ;
    
    protected static final String MASTER_CUSTOMER_NAME_CHANGE_XML = "<update schema=\"MASTER_CUSTOMER\">\n" 
            + "<values>"
            + "<cell name=\"CUST_FIRST_NAME\" type=\"CHAR\">Nils</cell>"
            + "<cell name=\"CUST_MIDDLE_NAME\" type=\"CHAR\">Lasse Basse</cell>"
            + "<cell name=\"CUST_LAST_NAME\" type=\"CHAR\">Olsen</cell>"
            + "</values>"
            + "<oldValues>"
            + "<cell name=\"MASTER_ID\" type=\"NUMERIC\">456</cell>"
            + "<cell name=\"CUST_FIRST_NAME\" type=\"CHAR\">Nils Kåre</cell>"
            + "<cell name=\"CUST_MIDDLE_NAME\" type=\"CHAR\">Lasse Basse</cell>"
            + "<cell name=\"CUST_LAST_NAME\" type=\"CHAR\">Olsen</cell>"
            + "</oldValues>"
            + "</update>";
}
