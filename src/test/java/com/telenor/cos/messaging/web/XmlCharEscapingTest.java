package com.telenor.cos.messaging.web;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.telenor.cos.messaging.web.form.AccountForm;
import com.telenor.cos.messaging.web.form.CustomerForm;
import com.telenor.cos.messaging.web.form.MasterCustomerForm;
import com.telenor.cos.messaging.web.form.MobileOfficeForm;
import com.telenor.cos.messaging.web.form.RelSubscriptionForm;
import com.telenor.cos.messaging.web.form.ResourceForm;
import com.telenor.cos.messaging.web.form.SubscriptionForm;
import com.telenor.cos.messaging.web.form.TnuUserIdMappingForm;
import com.telenor.cos.messaging.web.form.UserResourceForm;

/**
 * @author t798435
 * This is to test that all forms have their string objects
 * escaped for XML special characters
 *
 */
public class XmlCharEscapingTest {

    private final static String stringWithXMLSpecialChar = "quot, amp, apos, lt, gt -- \",&,',<,>";
    private final static String expectedString = "quot, amp, apos, lt, gt -- &quot;,&amp;,&apos;,&lt;,&gt;";
    private final static String assertionMessage = "XML Special Chars have not been escaped!";

    @Test
    public void checkAccountForm() {
        AccountForm form = new AccountForm();
        form.setAccInvMedium(stringWithXMLSpecialChar);
        form.setAccountName(stringWithXMLSpecialChar);
        form.setAccStatusId(stringWithXMLSpecialChar);
        form.setAccStatusId2(stringWithXMLSpecialChar);
        form.setAccTypeId(stringWithXMLSpecialChar);
        form.setOldAccInvMedium(stringWithXMLSpecialChar);
        form.setOldAccountName(stringWithXMLSpecialChar);
        form.setOldAccountStatusId(stringWithXMLSpecialChar);
        form.setOldAccountStatusId2(stringWithXMLSpecialChar);
        form.setOldAccountTypeId(stringWithXMLSpecialChar);
        assertEquals(assertionMessage, expectedString, form.getAccInvMedium());
        assertEquals(assertionMessage, expectedString, form.getAccountName());
        assertEquals(assertionMessage, expectedString, form.getAccStatusId());
        assertEquals(assertionMessage, expectedString, form.getAccStatusId2());
        assertEquals(assertionMessage, expectedString, form.getAccTypeId());
        assertEquals(assertionMessage, expectedString, form.getOldAccInvMedium());
        assertEquals(assertionMessage, expectedString, form.getOldAccountName());
        assertEquals(assertionMessage, expectedString, form.getOldAccountStatusId());
        assertEquals(assertionMessage, expectedString, form.getOldAccountStatusId2());
        assertEquals(assertionMessage, expectedString, form.getOldAccountTypeId());
    }

    @Test
    public void checkCustomerForm() {
        CustomerForm form = new CustomerForm();
        form.setAddrCOName(stringWithXMLSpecialChar);
        form.setAddrLineMain(stringWithXMLSpecialChar);
        form.setAddrStreetName(stringWithXMLSpecialChar);
        form.setCustFirstName(stringWithXMLSpecialChar);
        form.setCustLastName(stringWithXMLSpecialChar);
        form.setCustMiddleName(stringWithXMLSpecialChar);
        form.setCustUnitNumber(stringWithXMLSpecialChar);
        form.setPostcodeNameMain(stringWithXMLSpecialChar);
        assertEquals(assertionMessage, expectedString, form.getAddrCOName());
        assertEquals(assertionMessage, expectedString, form.getAddrLineMain());
        assertEquals(assertionMessage, expectedString, form.getAddrStreetName());
        assertEquals(assertionMessage, expectedString, form.getCustFirstName());
        assertEquals(assertionMessage, expectedString, form.getCustLastName());
        assertEquals(assertionMessage, expectedString, form.getCustMiddleName());
        assertEquals(assertionMessage, expectedString, form.getCustUnitNumber());
        assertEquals(assertionMessage, expectedString, form.getPostcodeNameMain());
    }

    @Test
    public void checkMasterCustomerForm() {
        MasterCustomerForm form = new MasterCustomerForm();
        form.setCustFirstName(stringWithXMLSpecialChar);
        form.setCustLastName(stringWithXMLSpecialChar);
        form.setCustMiddleName(stringWithXMLSpecialChar);
        form.setOldFirstName(stringWithXMLSpecialChar);
        form.setOldLastName(stringWithXMLSpecialChar);
        form.setOldMiddleName(stringWithXMLSpecialChar);
        assertEquals(assertionMessage, expectedString, form.getCustFirstName());
        assertEquals(assertionMessage, expectedString, form.getCustLastName());
        assertEquals(assertionMessage, expectedString, form.getCustMiddleName());
        assertEquals(assertionMessage, expectedString, form.getOldFirstName());
        assertEquals(assertionMessage, expectedString, form.getOldLastName());
        assertEquals(assertionMessage, expectedString, form.getOldMiddleName());
    }

    @Test
    public void checkMobileOfficeForm() {
        MobileOfficeForm form = new MobileOfficeForm();
        form.setDirectoryNumber(stringWithXMLSpecialChar);
        form.setExtensionNumber(stringWithXMLSpecialChar);
        form.setExtensionNumberOld(stringWithXMLSpecialChar);
        assertEquals(assertionMessage, expectedString, form.getDirectoryNumber());
        assertEquals(assertionMessage, expectedString, form.getExtensionNumber());
        assertEquals(assertionMessage, expectedString, form.getExtensionNumberOld());
    }

    @Test
    public void checkRelSubscriptionForm() {
        RelSubscriptionForm form = new RelSubscriptionForm();
        form.setRelSubscrType(stringWithXMLSpecialChar);
        assertEquals(assertionMessage, expectedString, form.getRelSubscrType());
    }

    @Test
    public void checkResourceForm() {
        ResourceForm form = new ResourceForm();
        form.setOldResourceTypeIdKey(stringWithXMLSpecialChar);
        form.setResourceTypeIdKey(stringWithXMLSpecialChar);
        assertEquals(assertionMessage, expectedString, form.getOldResourceTypeIdKey());
        assertEquals(assertionMessage, expectedString, form.getResourceTypeIdKey());
    }

    @Test
    public void checkSubscriptionForm() {
        SubscriptionForm form = new SubscriptionForm();
        form.setOldS212ProductId(stringWithXMLSpecialChar);
        form.setOldSubscriptionStatusId(stringWithXMLSpecialChar);
        form.setS212ProductId(stringWithXMLSpecialChar);
        form.setSubscrStatusId(stringWithXMLSpecialChar);
        assertEquals(assertionMessage, expectedString, form.getOldS212ProductId());
        assertEquals(assertionMessage, expectedString, form.getOldSubscriptionStatusId());
        assertEquals(assertionMessage, expectedString, form.getS212ProductId());
        assertEquals(assertionMessage, expectedString, form.getSubscrStatusId());
    }

    @Test
    public void checkTnuUserIdMappingForm() {
        TnuUserIdMappingForm form = new TnuUserIdMappingForm();
        form.setCsUserId(stringWithXMLSpecialChar);
        form.setTnuId(stringWithXMLSpecialChar);
        assertEquals(assertionMessage, expectedString, form.getCsUserId());
        assertEquals(assertionMessage, expectedString, form.getTnuId());
    }

    @Test
    public void checkUserResourceForm() {
        UserResourceForm form = new UserResourceForm();
        form.setCsUserId(stringWithXMLSpecialChar);
        assertEquals(assertionMessage, expectedString, form.getCsUserId());
    }
}