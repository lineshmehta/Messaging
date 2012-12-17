package com.telenor.cos.messaging.producers;

import java.util.ArrayList;
import java.util.List;

import com.telenor.cos.messaging.event.AgreementMember;
import com.telenor.cos.messaging.event.agreementmember.AgreementMemberLogicalDeleteEvent;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.producers.xpath.AgreementMemberUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

@Component
public class AgreementMemberLogicalDeleteProducer extends AbstractAgreementProducer {
    
    @Autowired
    private AgreementMemberUpdateXpathExtractor agreementMemberUpdateXPathExtractor;
    
    @Override
    public List<Event> produceMessage(Node message) {
        Long agreementId = agreementMemberUpdateXPathExtractor.getOldAgreementId(message).getValue();
        Long agreementMemberId = agreementMemberUpdateXPathExtractor.getOldAgreementMemberId(message).getValue();
        Long masterId = XPathLong.getValue(agreementMemberUpdateXPathExtractor.getOldAgreementMemberMasterId(message));
        Long customerUnitNumber = XPathLong.getValue(agreementMemberUpdateXPathExtractor.getOldAgreementMemberCustUnitNumber(message));
        if (masterId == null) {
            masterId = getMasterIdFromCacheAndValidateOrgNumber(customerUnitNumber,agreementId,agreementMemberId);
        }
        AgreementMember agreementMember = createAgreementMember(agreementId,agreementMemberId, masterId);
        List<Event> eventList = new ArrayList<Event>();
        eventList.add(new AgreementMemberLogicalDeleteEvent(agreementMember));
        return eventList;
    }

    @Override
    public Boolean isApplicable(Node message) {
        XPathString infoIsDeleted = agreementMemberUpdateXPathExtractor.getNewInfoIsDeleted(message);
        return infoIsDeleted != null && "Y".equalsIgnoreCase(infoIsDeleted.getValue());
    }
}
