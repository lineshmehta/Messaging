package com.telenor.cos.messaging.producers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.AgreementOwner;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.agreementowner.AgreementOwnerNewEvent;
import com.telenor.cos.messaging.producers.xpath.AgreementOwnerInsertXpathExtractor;

@Component
public class AgreementOwnerNewProducer extends AbstractProducer {

    @Autowired
    private AgreementOwnerInsertXpathExtractor agreementOwnerInsertXpathExtractor;

    @Override
    public List<Event> produceMessage(Node message) {
        Long agreementId = agreementOwnerInsertXpathExtractor.getAgreementId(message).getValue();
        Long masterId = agreementOwnerInsertXpathExtractor.getMasterId(message).getValue();
        AgreementOwner agreementOwner = new AgreementOwner();
        agreementOwner.setAgreementId(agreementId);
        agreementOwner.setMasterId(masterId);
        Event event = new AgreementOwnerNewEvent(agreementOwner);
        List<Event> eventList = new ArrayList<Event>();
        eventList.add(event);
        return eventList;
    }

    @Override
    public Boolean isApplicable(Node message) {
        return agreementOwnerInsertXpathExtractor.getAgreementOwnerId(message) != null;
    }

}
