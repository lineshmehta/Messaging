package com.telenor.cos.messaging.producers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.CosMessagingException;
import com.telenor.cos.messaging.CosMessagingInvalidDataException;
import com.telenor.cos.messaging.event.AgreementMember;
import com.telenor.cos.messaging.jdbm.MasterCustomerCache;

@Component
public abstract class AbstractAgreementProducer extends AbstractProducer{

    @Autowired
    private MasterCustomerCache masterCustomerCache;

    /**
     * Gets MasterId from Cache and Validates OrganisationNumber.
     * @param customerUnitNumber custUnitNumber.
     * @param agreementId agreementId.
     * @param agreementMemberId agreementMemberId.
     * @return masterId.
     */
    protected Long getMasterIdFromCacheAndValidateOrgNumber(Long customerUnitNumber, Long agreementId, Long agreementMemberId) {
        if (customerUnitNumber == null) {
            throw new CosMessagingInvalidDataException("CustomerUnitNumber is [null].\nCosMessagingInvalidDataException for Agreement Id [" + agreementId + "] and Agreement MemberId [" + agreementMemberId + "]", null);
        }
        Long masterId = masterCustomerCache.get(customerUnitNumber);
        if (masterId == null) {
            throw new CosMessagingException("No Master Customer with Customer Unit Number [" + customerUnitNumber + "] was not found in MasterCustomerCache."
                    + "Hence AgreementMember with Id [" + agreementMemberId + "] is failed to create for AgreementId [" + agreementId + "]" , null );
        }
        return masterId;
    }

    /**
     * creates AgreementMember.
     * @param agreementId agreementId.
     * @param agreementMemberId agreementMemberId.
     * @param masterId masterId.
     * @return AgreementMember.
     */
    protected AgreementMember createAgreementMember(Long agreementId,Long agreementMemberId, Long masterId) {
        AgreementMember agreementMember = new AgreementMember();
        agreementMember.setAgreementId(agreementId);
        agreementMember.setAgreementMemberId(agreementMemberId);
        agreementMember.setMasterId(masterId);
        return agreementMember;
    }
}
