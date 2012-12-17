package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.jdbm.MasterCustomerCache;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import org.mockito.Mock;

public class AgreementMemberCommonProducerTest {

    public final static XPathLong AGREEMENT_MEMBER_ID = XPathLong.valueOf(1);
    public final static XPathLong AGREEMENT_ID = XPathLong.valueOf(11);
    public final static XPathLong MASTER_ID = XPathLong.valueOf(3);
    public final static XPathLong CUSTOMER_UNIT_NUMBER = XPathLong.valueOf(4);

    @Mock
    private MasterCustomerCache mockMasterCustomerCache;

    public MasterCustomerCache getMockMasterCustomerCache() {
        return mockMasterCustomerCache;
    }
}
