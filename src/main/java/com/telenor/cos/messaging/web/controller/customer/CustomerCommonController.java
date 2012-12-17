package com.telenor.cos.messaging.web.controller.customer;

import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.web.controller.CommonController;
import com.telenor.cos.messaging.web.form.CustomerForm;

public class CustomerCommonController extends CommonController {

    public final static String CUSTOMER_NEW = "Consumer.WTEST.VirtualTopic.Customers.New";
    public final static String CUSTOMER_LOGICAL_DELETE = "Consumer.WTEST.VirtualTopic.Customers.LogicalDeleted";
    public final static String CUSTOMER_ADRESS_CHANGE = "Consumer.WTEST.VirtualTopic.Customers.AddressChange";
    public final static String CUSTOMER_NAME_CHANGE = "Consumer.WTEST.VirtualTopic.Customers.NameChange";

    private boolean queuesSetUp = false;

    /**
     * Sets up the JMS-environment.
     */
    public void setUp() {
        if (!queuesSetUp) {
            queuesSetUp = true;
            getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
            getJms().receive(CUSTOMER_NEW);
            getJms().receive(CUSTOMER_LOGICAL_DELETE);
            getJms().receive(CUSTOMER_ADRESS_CHANGE);
            getJms().receive(CUSTOMER_NAME_CHANGE);
            getJms().setReceiveTimeout(3000L);
        }
    }

    /**
     * 
     * @return a form with default values
     */
    protected CustomerForm getCustomerForm() {
        CustomerForm form = new CustomerForm();
        form.setMasterId(55);
        form.setCustFirstName("Ole");
        form.setCustMiddleName("Dole");
        form.setCustLastName("Duck");
        form.setCustUnitNumber("987");
        form.setPostcodeIdMain(443);
        form.setPostcodeNameMain("hoksund");
        form.setAddrLineMain("rt56");
        form.setAddrCOName("7890");
        form.setAddrStreetName("overbakken");
        form.setAddrStreetNumber("5634");
        form.setInfoIsDeleted(false);
        return form;
    }
}
