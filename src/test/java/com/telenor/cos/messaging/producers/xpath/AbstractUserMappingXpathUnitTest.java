package com.telenor.cos.messaging.producers.xpath;

/**
 * @author Babaprakash D
 *
 */
public class AbstractUserMappingXpathUnitTest extends AbstractXpathUnitTest {
    
    protected static final String NEW_TNUID_USERMAPPING = "<insert schema=\"tnuid_user_mapping\">\n" + "        <values>\n"
            + "            <cell name=\"tnu_id\" type=\"VARCHAR\">_1000030</cell>\n"
            + "            <cell name=\"application_id\" type=\"NUMERIC\">62</cell>\n"
            + "            <cell name=\"cs_user_id\" type=\"VARCHAR\">tn_9234</cell>\n"
            + "            <cell name=\"info_reg_date\" type=\"DATETIME\">28.03.2008 15:12:17</cell>\n"
            + "            <cell name=\"info_reg_appl_name\" type=\"VARCHAR\">sql-plugin</cell>\n"
            + "            <cell name=\"info_reg_user_name\" type=\"VARCHAR\">wina</cell>\n"
            + "            <cell name=\"info_chg_date\" type=\"DATETIME\">28.03.2008 15:12:17</cell>\n"
            + "            <cell name=\"info_chg_appl_name\" type=\"VARCHAR\">sql-plugin</cell>\n"
            + "            <cell name=\"info_chg_user_name\" type=\"VARCHAR\">wina</cell>\n"
            + "            <cell name=\"info_timestamp\" type=\"TIMESTAMP\">000300000a8cbd75</cell>\n"
            + "        </values>\n" + "    </insert>\n";
    
    protected static final String DELETE_TNUID_USERMAPPING = "<delete schema=\"tnuid_user_mapping\">\n" 
            + "        <values>\n"
            + "            <cell name=\"application_id\" type=\"NUMERIC\">62</cell>\n"
            + "            <cell name=\"cs_user_id\" type=\"VARCHAR\">tn_9234</cell>\n"
            + "        </values>\n"
            + "        <oldValues>\n"
            + "            <cell name=\"tnu_id\" type=\"VARCHAR\">_1000030</cell>\n"
            + "            <cell name=\"application_id\" type=\"NUMERIC\">62</cell>\n"
            + "            <cell name=\"cs_user_id\" type=\"VARCHAR\">tn_9234</cell>\n"
            + "            <cell name=\"info_reg_date\" type=\"DATETIME\">28.03.2008 15:12:17</cell>\n"
            + "            <cell name=\"info_reg_appl_name\" type=\"VARCHAR\">sql-plugin</cell>\n"
            + "            <cell name=\"info_reg_user_name\" type=\"VARCHAR\">wina</cell>\n"
            + "            <cell name=\"info_chg_date\" type=\"DATETIME\">28.03.2008 15:12:17</cell>\n"
            + "            <cell name=\"info_chg_appl_name\" type=\"VARCHAR\">sql-plugin</cell>\n"
            + "            <cell name=\"info_chg_user_name\" type=\"VARCHAR\">wina</cell>\n"
            + "            <cell name=\"info_timestamp\" type=\"TIMESTAMP\">000300000a8cbd75</cell>\n"
            + "        </oldValues>\n"
            + "    </delete>\n";
    
}
