package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.AgreementMember;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.agreementmember.AgreementMemberNewEvent;
import com.telenor.cos.messaging.producers.xpath.AgreementMemberInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Component
public class AgreementMemberNewProducer extends AbstractAgreementProducer {
    
    @Autowired
    private AgreementMemberInsertXpathExtractor agreementMemberInsertXpathExtractor;

    @Override
    public List<Event> produceMessage(Node message) {
        Long agreementMemberId = XPathLong.getValue(agreementMemberInsertXpathExtractor.getAgreementMemberId(message));
        Long masterId = XPathLong.getValue(agreementMemberInsertXpathExtractor.getMasterId(message));
        Long agreementId = XPathLong.getValue(agreementMemberInsertXpathExtractor.getAgreementId(message));
        Long customerUnitNumber = XPathLong.getValue(agreementMemberInsertXpathExtractor.getCustomerUnitNumber(message));
        if (masterId == null) {
            masterId = getMasterIdFromCacheAndValidateOrgNumber(customerUnitNumber,agreementId,agreementMemberId);
        }
        AgreementMember agreementMember = createAgreementMember(agreementId,agreementMemberId, masterId);
        List<Event> eventsList = new ArrayList<Event>();
        eventsList.add(new AgreementMemberNewEvent(agreementMember));
        return eventsList;
    }

    @Override
    public Boolean isApplicable(Node message) {
        return agreementMemberInsertXpathExtractor.getAgreementMemberId(message) != null;
    }

}
