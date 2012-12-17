package com.telenor.cos.messaging.producers.xpath;

/**
 * @author t798435
 *
 */
public class AbstractMasterStructureXpathUnitTest extends AbstractXpathUnitTest {
    
    protected static final String NEW_MASTER_STRUCTURE_XML = "<insert schema=\"MAST_STRUCTURE\">\n" 
                + "        <values>\n"
                + "            <cell name=\"STRUCT_TYPE_ID\" type=\"SMALLINT\">1</cell>\n"
                + "            <cell name=\"MAST_ID_OWNER\" type=\"NUMERIC\">777</cell>\n"
                + "            <cell name=\"MAST_ID_MEMBER\" type=\"NUMERIC\">666</cell>\n"
                + "            <cell name=\"INFO_REG_DATE\" type=\"DATETIME\">17.10.1999 12:38:19</cell>\n"
                + "            <cell name=\"INFO_REG_APPL_NAME\" type=\"VARCHAR\">DBARTISAN</cell>\n"
                + "            <cell name=\"INFO_REG_USER_NAME\" type=\"VARCHAR\">aalaro</cell>\n"
                + "            <cell name=\"INFO_CHG_DATE\" type=\"DATETIME\">29.04.2011 18:23:10</cell>\n"
                + "            <cell name=\"INFO_CHG_APPL_NAME\" type=\"VARCHAR\">CCDW</cell>\n"
                + "            <cell name=\"INFO_CHG_USER_NAME\" type=\"VARCHAR\">CCDW</cell>\n"
                + "            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">N</cell>\n"
                + "            <cell name=\"INFO_QUALITY_INDEX\" type=\"CHAR\">2</cell>\n"
                + "            <cell name=\"INFO_TIMESTAMP\" type=\"TIMESTAMP\">000200002ad48d74</cell>\n"
                + "        </values>\n" 
                + "    </insert>\n" ;
    
    protected static final String UPDATE_MASTER_STRUCTURE_XML = "<update schema=\"MAST_STRUCTURE\">\n" 
            + "        <values>\n"
            + "            <cell name=\"MAST_ID_OWNER\" type=\"NUMERIC\">888</cell>\n"
            + "        </values>\n" 
            + "        <oldValues>\n"
            + "            <cell name=\"STRUCT_TYPE_ID\" type=\"SMALLINT\">1</cell>\n"
            + "            <cell name=\"MAST_ID_OWNER\" type=\"NUMERIC\">777</cell>\n"
            + "            <cell name=\"MAST_ID_MEMBER\" type=\"NUMERIC\">666</cell>\n"
            + "            <cell name=\"INFO_REG_DATE\" type=\"DATETIME\">17.10.1999 12:38:19</cell>\n"
            + "            <cell name=\"INFO_REG_APPL_NAME\" type=\"VARCHAR\">DBARTISAN</cell>\n"
            + "            <cell name=\"INFO_REG_USER_NAME\" type=\"VARCHAR\">aalaro</cell>\n"
            + "            <cell name=\"INFO_CHG_DATE\" type=\"DATETIME\">29.04.2011 18:23:10</cell>\n"
            + "            <cell name=\"INFO_CHG_APPL_NAME\" type=\"VARCHAR\">CCDW</cell>\n"
            + "            <cell name=\"INFO_CHG_USER_NAME\" type=\"VARCHAR\">CCDW</cell>\n"
            + "            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">N</cell>\n"
            + "            <cell name=\"INFO_QUALITY_INDEX\" type=\"CHAR\">2</cell>\n"
            + "            <cell name=\"INFO_TIMESTAMP\" type=\"TIMESTAMP\">000200002ad48d74</cell>\n"
            + "        </oldValues>\n" 
            + "    </update>\n" ;
    
    protected static final String DELETE_MASTER_STRUCTURE_XML = "<update schema=\"MAST_STRUCTURE\">\n" 
            + "        <values>\n"
            + "            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">Y</cell>\n"
            + "        </values>\n" 
            + "        <oldValues>\n"
            + "            <cell name=\"STRUCT_TYPE_ID\" type=\"SMALLINT\">1</cell>\n"
            + "            <cell name=\"MAST_ID_OWNER\" type=\"NUMERIC\">777</cell>\n"
            + "            <cell name=\"MAST_ID_MEMBER\" type=\"NUMERIC\">666</cell>\n"
            + "            <cell name=\"INFO_REG_DATE\" type=\"DATETIME\">17.10.1999 12:38:19</cell>\n"
            + "            <cell name=\"INFO_REG_APPL_NAME\" type=\"VARCHAR\">DBARTISAN</cell>\n"
            + "            <cell name=\"INFO_REG_USER_NAME\" type=\"VARCHAR\">aalaro</cell>\n"
            + "            <cell name=\"INFO_CHG_DATE\" type=\"DATETIME\">29.04.2011 18:23:10</cell>\n"
            + "            <cell name=\"INFO_CHG_APPL_NAME\" type=\"VARCHAR\">CCDW</cell>\n"
            + "            <cell name=\"INFO_CHG_USER_NAME\" type=\"VARCHAR\">CCDW</cell>\n"
            + "            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">N</cell>\n"
            + "            <cell name=\"INFO_QUALITY_INDEX\" type=\"CHAR\">2</cell>\n"
            + "            <cell name=\"INFO_TIMESTAMP\" type=\"TIMESTAMP\">000200002ad48d74</cell>\n"
            + "        </oldValues>\n" 
            + "    </update>\n" ;
    
}
