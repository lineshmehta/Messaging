package com.telenor.cos.messaging.producers.xpath;

public abstract class AbstractRelSubscriptionXpathExtractorTest extends AbstractXpathUnitTest {
    
    protected final static String INSERT_XML = 
         "<dbStream xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://dhttm12:8000/RepraWebApp/dtds/dbeventstream.xsd\" environment=\"REP_connector.RepCondb\">"
            + "<tran eventId=\"190:00000008627bd0ad012c824c0005012c824b00080000a04d00d34fbe0000000000010003\">"
            + "<insert schema=\"REL_SUBSCRIPTION\">"
            + "<values>"
            + "<cell name=\"subscr_id_owner\" type=\"NUMERIC\">30870128</cell>"
            + "<cell name=\"subscr_id_member\" type=\"NUMERIC\">30870131</cell>"
            + "<cell name=\"rel_subscr_type\" type=\"CHAR\">DK</cell>"
            + "<cell name=\"info_reg_date\" type=\"DATETIME\">20120510 12:49:22:143</cell>"
            + "<cell name=\"info_reg_appl_name\" type=\"CHAR\">S212RT    </cell>"
            + "<cell name=\"info_reg_user_name\" type=\"CHAR\">NULL</cell>"
            + "<cell name=\"info_chg_date\" type=\"DATETIME\">20120510 12:49:22:143</cell>"
            + "<cell name=\"info_chg_appl_name\" type=\"CHAR\">S212RT    </cell>"
            + "<cell name=\"info_chg_user_name\" type=\"CHAR\">NULL</cell>"
            + "<cell name=\"info_is_deleted\" type=\"CHAR\">N</cell>"
            + "</values>" + "</insert>" + "</tran>" + "</dbStream>";

}
