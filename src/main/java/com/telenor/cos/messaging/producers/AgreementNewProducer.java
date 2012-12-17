package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Agreement;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.agreement.AgreementNewEvent;
import com.telenor.cos.messaging.jdbm.MasterCustomerCache;
import com.telenor.cos.messaging.producers.xpath.AgreementInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

@Component
public class AgreementNewProducer extends AbstractProducer{

    private static final Logger LOG = LoggerFactory.getLogger(AgreementNewProducer.class);

    @Autowired
    private AgreementInsertXpathExtractor agreementInsertXpathExtractor;

    @Autowired
    private MasterCustomerCache masterCustomerCache;

    @Override
    public List<Event> produceMessage(Node message) {
        return Collections.<Event>singletonList(getEvent(message));
    }

    @Override
    public Boolean isApplicable(Node message) {
        return agreementInsertXpathExtractor.getAgreementId(message) != null;
    }

    Event getEvent(Node message) {
        Agreement agreement = createAgreement(message);
        Long custUnitNumber = XPathLong.getValue(agreementInsertXpathExtractor.getCustUnitNumber(message));
        Long masterId = null;
        if (custUnitNumber != null) {
            masterId = masterCustomerCache.get(custUnitNumber);
            if (masterId == null) {
                LOG.warn("Master id with customer unit number [" + custUnitNumber + "] not found in MasterCustomerCache");
            }
        }
        agreement.setMasterCustomerId(masterId);
        return new AgreementNewEvent(agreement);
    }

    XPathLong getAgreementId(Node message) {
        return agreementInsertXpathExtractor.getAgreementId(message);
    }

    Agreement createAgreement(Node message) {
        Agreement agreement = new Agreement();
        agreement.setAgreementId(XPathLong.getValue(getAgreementId(message)));
        return agreement;
    }


}
