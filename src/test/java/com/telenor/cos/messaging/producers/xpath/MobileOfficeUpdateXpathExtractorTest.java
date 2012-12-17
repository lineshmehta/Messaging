package com.telenor.cos.messaging.producers.xpath;

import com.telenor.cos.test.category.ServiceTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/contextWithoutCamel.xml")
@Category(ServiceTest.class)
@DirtiesContext
public class MobileOfficeUpdateXpathExtractorTest extends AbstractXpathUnitTest {

    public static final String EXTENSION_NUMBER = "45678944";
    public static final String EXTENSION_NUMBER_OLD = "45678945";
    public static final String DIR_NUMBER = "111222555444";
    private final static String OLD_INFO_IS_DELETED = "N";
    private final static String NEW_INFO_IS_DELETED = "Y";


    @Autowired
    private MobileOfficeUpdateXpathExtractor mobileOfficeUpdateXpathExtractor;

    private Node updateMessage;
    private Node deleteMessage;

    @Before
    public void setUp() throws Exception {
        updateMessage = stringToDom(getMobileOfficeUpdateXml());
        deleteMessage = stringToDom(getMobileOfficeDeleteXml());
    }

    @Test
    public void getExtensionNumberTest() {
        assertEquals(EXTENSION_NUMBER, mobileOfficeUpdateXpathExtractor.getExtensionNumber(updateMessage).getValue());
    }

    @Test
    public void getExtensionNumberOldTest() {
        assertEquals(EXTENSION_NUMBER_OLD, mobileOfficeUpdateXpathExtractor.getExtensionNumberOld(updateMessage).getValue());
    }

    @Test
    public void getDirectoryNumberIdOldTest() {
        assertEquals(DIR_NUMBER, mobileOfficeUpdateXpathExtractor.getDirectoryNumberOld(updateMessage).getValue());
    }

    @Test
    public void getInfoIsDeletedTest() {
        assertEquals(NEW_INFO_IS_DELETED, mobileOfficeUpdateXpathExtractor.getInfoIsDeleted(deleteMessage).getValue());
    }

    @Test
    public void getInfoIsDeletedOldTest() {
        assertEquals(OLD_INFO_IS_DELETED, mobileOfficeUpdateXpathExtractor.getInfoIsDeletedOld(deleteMessage).getValue());
    }

    private String getMobileOfficeUpdateXml() {
        return "<update schema=\"MOBILE_OFFICE_MEMBERS\">"
                + "<values>"
                    + "<cell name=\"EXTENSION_NUMBER\" type=\"VARCHAR\">" + EXTENSION_NUMBER + "</cell>"
                + "</values>"
                + "<oldValues>"
                    + "<cell name=\"EXTENSION_NUMBER\" type=\"VARCHAR\">" + EXTENSION_NUMBER_OLD + "</cell>"
                    + "<cell name=\"DIRECTORY_NUMBER_ID\" type=\"VARCHAR\">" + DIR_NUMBER + "</cell>"
                + "</oldValues>"
                + "</update>";
    }

    private String getMobileOfficeDeleteXml() {
        return "<update schema=\"MOBILE_OFFICE_MEMBERS\">"
                + "<values>"
                + "<cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + NEW_INFO_IS_DELETED + "</cell>"
                + "</values>"
                + "<oldValues>"
                + "<cell name=\"EXTENSION_NUMBER\" type=\"VARCHAR\">" + EXTENSION_NUMBER_OLD + "</cell>"
                + "<cell name=\"DIRECTORY_NUMBER_ID\" type=\"VARCHAR\">" + DIR_NUMBER + "</cell>"
                + "<cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + OLD_INFO_IS_DELETED + "</cell>"
                + "</oldValues>"
                + "</update>";
    }

}
