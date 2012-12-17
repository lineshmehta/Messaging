package com.telenor.cos.messaging.producers.xpath;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.util.TestHelper;
import com.telenor.cos.test.category.ServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/contextWithoutCamel.xml")
@Category(ServiceTest.class)
@DirtiesContext
public class AgreementMemberInsertXpathExtractorTest extends AbstractXpathUnitTest{

    @Autowired
    private AgreementMemberInsertXpathExtractor extractor;

    private static final String NEW_XML="dataset/agreementMember_new.xml";

    private Node message;

    @Before
    public void setUp() throws Exception {
        message = new TestHelper().fileToDom(NEW_XML);
    }

    @Test
    public void getAgreementMemberId() {
        assertEquals("Unexpected agreement member id", Long.valueOf(666), extractor.getAgreementMemberId(message).getValue());
    }

    @Test
    public void getAgreementId() {
        assertEquals("Unexpected agreement id", Long.valueOf(1), extractor.getAgreementId(message).getValue());
    }

    @Test
    public void getCustomerUnitNumber() {
        assertEquals("Unexpected customer unit number", Long.valueOf(121212), extractor.getCustomerUnitNumber(message).getValue());
    }

    @Test
    public void getMasterId() throws Exception {
        assertEquals("Unexpected master id", Long.valueOf(555), extractor.getMasterId(message).getValue());
    }

}
