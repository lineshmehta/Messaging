package com.telenor.cos.samlstub;

import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;
import java.util.Set;
import java.util.TreeSet;

/**
 * For fitnesse tests, its required to bypass the actual
 * SAML validation and hence we use this add a dummy SAMLAssertion
 * instead of making a call to the TSS.
 *
 * @author t798435
 * @since 23 11 2011
 */
@Component
public class SoapHandlerStub implements SOAPHandler<SOAPMessageContext> {

    /*
     * This method adds a dummy saml assertion string
     * for all outgoing requests. this is done so that the called service
     * can authenticate the request and createEvent response.
     */
    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean isOutboundMessage = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        SoapHandlerStubHelper stubHelper = new SoapHandlerStubHelper();
        if (isOutboundMessage) {
            SOAPMessage soapMessage = context.getMessage();
            try {
                soapMessage = stubHelper.insertDummySAMLAssertion(soapMessage);
                context.setMessage(soapMessage);
                return true;
            } catch (Exception e) {
                handleException(e);
            }
        }
        return true;//even if exception is thrown
    }

    @Override
    public void close(MessageContext context) {
    }

    @Override
    public Set<QName> getHeaders() {
        return new TreeSet<QName>();
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;//faults should always be handled
    }

    /**
     * The client should always get back a {@link javax.xml.ws.soap.SOAPFaultException}
     * This method tries and converts all exceptions to
     * {@link javax.xml.ws.soap.SOAPFaultException} type. If it is not able to do so
     * then it throws back a {@ TelenorSecurityServiceSoapHandlerException}
     *
     * @param e - the obtained exception
     */
    private void handleException(Exception e) {
        SOAPFault soapFault = null;
        try {
            soapFault = createSOAPFault(e);
            throw new SOAPFaultException(soapFault);
        } catch (SOAPException exception) {
            throw new SoapHandlerException("Error in creating SOAPFaultException from the Exception : " + e, exception);
        }
    }

    /**
     * @param e the exception which has to be converted to SOAPFault
     * @return new SOAPFault Exception
     * @throws javax.xml.soap.SOAPException
     */
    private SOAPFault createSOAPFault(Exception e) throws SOAPException {
        SOAPFactory soapFactory = SOAPFactory.newInstance();
        SOAPFault soapFault = soapFactory.createFault();
        soapFault.setFaultString(e.getMessage());
        return soapFault;
    }
}