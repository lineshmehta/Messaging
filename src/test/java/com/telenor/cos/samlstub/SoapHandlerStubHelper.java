package com.telenor.cos.samlstub;

import com.telenor.cos.test.category.IntegrationTest;
import org.junit.experimental.categories.Category;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

/**
 * This is a helper class to {@link SoapHandlerStub}
 * @author t798435
 * @since 23 11 2011
 */
@Category(IntegrationTest.class)
public class SoapHandlerStubHelper {

    private static final String dummySAMLAssertion = "This is a Dummy SAML for Fitnesse Tests";
    private static final String AssertionTag = "DummySAML";

    /**
     * This method is invoked when SAMLbypass has been enabled for Fitnesse tests.
     * In such case a dummy saml assertion is added to the soap header and the same is 
     * checked for by the saml handler to ensure that there is some security in the 
     * transaction and no arbitrary requests are passed
     * @param soapMessage which has to be processed
     * @return SOAPMessage with dummy saml in the header
     * @throws SoapHandlerException
     */
    public SOAPMessage insertDummySAMLAssertion(SOAPMessage soapMessage) {
        try {
            SOAPFactory factory = SOAPFactory.newInstance();
            SOAPElement tokenElement =  factory.createElement(AssertionTag, "", "");
            tokenElement.addTextNode(dummySAMLAssertion);
            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPHeader header =  soapPart.getEnvelope().getHeader();
            if (header == null){
                soapPart.getEnvelope().addHeader();
            }
            soapPart.getEnvelope().getHeader().addChildElement(tokenElement);
            return soapMessage;
        } catch (Exception e) {
            throw new SoapHandlerException("Exception while adding DUMMY SAML ASSERTION to SOAP Header", e);
        }
    }
}