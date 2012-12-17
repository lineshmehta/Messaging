package com.telenor.cos.messaging.util;

import com.telenor.cos.messaging.event.UserReference;

public final class UserReferenceTestHelper {
    
    private static final String INVOICE_REF = "test";
    private static final String USER_REF_DESCR = "test";
    private static final String NUMBER_TYPE = "ES";
    
    public static final String USERREF_NEW_XML = "dataset/userReference_new_numberType_ES.xml";
    public static final String USERREF_DESC_UPDATE_XML = "dataset/userreference_descrupdate.xml";
    public static final String INVOICEREF_UPDATE_XML = "dataset/userReference_invoiceupdate.xml";
    public static final String NUMBER_TYPE_UPDATE_XML = "dataset/userreference_numberTypeupdate.xml";
    public static final String LOGICAL_DELETE_XML = "dataset/userreference_logicalDelete.xml";
    
    private UserReferenceTestHelper() {
        
    }
    
    public static UserReference createUserReference() {
        UserReference userReference = new UserReference();
        userReference.setInvoiceRef(INVOICE_REF);
        userReference.setNumberType(NUMBER_TYPE);
        userReference.setUserRefDescr(USER_REF_DESCR);
        return userReference;
    }

}
