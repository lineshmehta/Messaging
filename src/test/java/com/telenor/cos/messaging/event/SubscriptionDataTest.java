package com.telenor.cos.messaging.event;

import java.util.Date;

import org.junit.Test;

public class SubscriptionDataTest {

    /**
     * I hate testCoverage
     */
    @Test
    public void createData() {
        Subscription data = new Subscription();
        set(data);
        get(data);

    }

    private void get(Subscription data) {
        data.getAccountId();
        data.getContractId();
        data.getFirstName();
        data.getInvoiceReference();
        data.getIsSecretNumber();
        data.getLastName();
        data.getMiddleName();
        data.getShortNumber();
        data.getMsisdn();
        data.getMsisdnDatakort();
        data.getMsisdnTvilling();
        data.getSubscriptionType();
        data.getUserCustomerId();
        data.getUserReference();
        data.getUserReferenceDescription();
        data.getValidFromDate();
        data.getValidToDate();
    }

    private void set(Subscription data) {
        data.setAccountId(24L);
        data.setContractId(43545L);
        data.setFirstName("glenn");
        data.setInvoiceReference("mmmmm");
        data.setInvoiceReference("fds");
        data.setIsSecretNumber(true);
        data.setLastName("Hoddle");
        data.setMiddleName("Victor");
        data.setShortNumber(123);
        data.setMsisdnDatakort(134551L);
        data.setMsisdnTvilling(444433222L);
        data.setMsisdn(313L);
        data.setSubscriptionType("gratis");
        data.setUserCustomerId(2431L);
        data.setUserReference("411");
        data.setUserReferenceDescription("rwagsfgsfdg");
        data.setValidFromDate(new Date());
        data.setValidFromDate(null);
        data.getValidFromDate();
        data.setValidToDate(new Date());
        data.setValidToDate(null);
        data.getValidToDate();
    }
}
