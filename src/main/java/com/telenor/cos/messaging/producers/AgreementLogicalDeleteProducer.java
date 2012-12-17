package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Agreement;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.agreement.AgreementLogicalDeletedEvent;
import com.telenor.cos.messaging.producers.xpath.AgreementUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

@Component
public class AgreementLogicalDeleteProducer extends AbstractProducer {

    @Autowired
    private AgreementUpdateXpathExtractor agreementUpdateXpathExtractor;
    
    @Override
    public List<Event> produceMessage(Node message) {
        return Collections.<Event>singletonList(getEvent(message));
    }

    @Override
    public Boolean isApplicable(Node message) {
        XPathString infoIsDeleted = agreementUpdateXpathExtractor.getInfoIsDeleted(message);
        return infoIsDeleted != null && "Y".equalsIgnoreCase(infoIsDeleted.getValue());
    }

    Event getEvent(Node message) {
        return new AgreementLogicalDeletedEvent(createAgreement(message));
    }

    Agreement createAgreement(Node message) {
        Agreement agreement = new Agreement();
        agreement.setAgreementId(XPathLong.getValue(getAgreementId(message)));
        return agreement;
    }

    XPathLong getAgreementId(Node message) {
        return agreementUpdateXpathExtractor.getAgreementId(message);
    }
}
