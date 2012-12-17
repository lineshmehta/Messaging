package com.telenor.cos.messaging;

/**
 * Used for integration test of endpoints
 */
public final class ExternalEndpoints {
    private ExternalEndpoints() {
    }

    private static final String TEST_VIRTUAL_TOPIC = "Consumer.TEST.VirtualTopic.";
    public final static String DEAD_LETTER = "deadLetter";
    public final static String INCOMING = "incoming.";

    public final static String INCOMING_REP_SERVER = "incoming.repserver";
    public final static String INCOMING_EVENT = "incoming.event";

    public final static String INCOMING_SUBSCRIPTIONS = "incoming.subscription";
    public final static String NEW_SUBSCRIPTIONS = TEST_VIRTUAL_TOPIC + "Subscriptions.New";
    public final static String UPDATED_SUBSCRIPTIONS = TEST_VIRTUAL_TOPIC + "Subscriptions.Updated";
    public final static String DELETED_SUBSCRIPTIONS = TEST_VIRTUAL_TOPIC + "Subscriptions.LogicalDeleted";
    public final static String UPDATED_SUBSCRIPTION_STATUS = TEST_VIRTUAL_TOPIC + "Subscriptions.ChangeStatus";
    public final static String SECRET_NUMBER_UPADATE = TEST_VIRTUAL_TOPIC + "Subscriptions.SecretNumber";
    public final static String EXPIRED_SUBSCRIPTIONS = TEST_VIRTUAL_TOPIC + "Subscriptions.Expired";
    public final static String SECOND_EXPIRED_SUBSCRIPTIONS_CONSUMER = "Consumer.TEST2.VirtualTopic.Subscriptions.Expired";

    public final static String CHANGE_USER_SUBSCRIPTIONS = TEST_VIRTUAL_TOPIC + "Subscriptions.ChangeUser";
    public final static String CHANGE_ACCOUNT_SUBSCRIPTIONS = TEST_VIRTUAL_TOPIC + "Subscriptions.ChangeAccount";
    public final static String CHANGE_TYPE_SUBSCRIPTIONS = TEST_VIRTUAL_TOPIC + "Subscriptions.ChangeType";

    public final static String SUBSCRIPTION_EQUPMENT_NEW_TOPIC = TEST_VIRTUAL_TOPIC + "SubscriptionEquipment.New";
    public final static String SUBSCRIPTION_EQUPMENT_UPDATE_TOPIC = TEST_VIRTUAL_TOPIC + "SubscriptionEquipment.Update";
    public final static String SUBSCRIPTION_EQUPMENT_DELETE_TOPIC = TEST_VIRTUAL_TOPIC + "SubscriptionEquipment.Delete";

    public final static String INCOMING_CUSTOMERS = "incoming.customer";
    public final static String CUSTOMER_NEW = TEST_VIRTUAL_TOPIC + "Customers.New";
    public final static String CUSTOMER_NAME_CHANGE = TEST_VIRTUAL_TOPIC + "Customers.NameChange";
    public final static String CUSTOMER_LOGICAL_DELETE = TEST_VIRTUAL_TOPIC + "Customers.LogicalDeleted";
    public final static String CUSTOMER_ADRESS_CHANGE = TEST_VIRTUAL_TOPIC + "Customers.AddressChange";

    public final static String INCOMING_ACCOUNTS = "incoming.account";
    public final static String ACCOUNT_NEW = TEST_VIRTUAL_TOPIC + "Accounts.New";
    public final static String ACCOUNT_LOGICAL_DELETE = TEST_VIRTUAL_TOPIC + "Accounts.LogicalDeleted";
    public final static String ACCOUNT_NAME_CHANGE = TEST_VIRTUAL_TOPIC + "Accounts.NameChange";
    public final static String ACCOUNT_STATUS_CHANGE = TEST_VIRTUAL_TOPIC + "Accounts.StatusChange";
    public final static String ACCOUNT_INVOICE_FORMAT_CHANGE = TEST_VIRTUAL_TOPIC + "Accounts.InvoiceFormatChange";
    public final static String ACCOUNT_PAYER_CHANGE = TEST_VIRTUAL_TOPIC + "Accounts.PayerChange";
    public final static String ACCOUNT_OWNER_CHANGE = TEST_VIRTUAL_TOPIC + "Accounts.OwnerChange";
    public final static String ACCOUNT_PAYMENTSTATUS_CHANGE = TEST_VIRTUAL_TOPIC + "Accounts.PaymentStatusChange";
    public final static String ACCOUNT_TYPE_CHANGE = TEST_VIRTUAL_TOPIC + "Accounts.TypeChange";

    public final static String INCOMING_AGREEMENT = INCOMING + "agreement";
    public final static String AGREEMENT_NEW = TEST_VIRTUAL_TOPIC + "Agreement.New";
    public final static String AGREEMENT_LOGICAL_DELETE = TEST_VIRTUAL_TOPIC + "Agreement.LogicalDelete";

    public final static String INCOMING_MASTERSTRUCTURE = "incoming.masterstructure";
    public final static String MASTERSTRUCTURE_DELETE = TEST_VIRTUAL_TOPIC + "MasterStructure.LogicalDeleted";
    public final static String MASTERSTRUCTURE_UPDATE = TEST_VIRTUAL_TOPIC + "MasterStructure.Updated";
    public final static String MASTERSTRUCTURE_NEW = TEST_VIRTUAL_TOPIC + "MasterStructure.New";

    public final static String INCOMING_MASTERCUSTOMERS = "incoming.mastercustomer";
    public final static String MASTERCUSTOMER_LOGICAL_DELETE = TEST_VIRTUAL_TOPIC + "MasterCustomer.LogicalDeleted";
    public final static String MASTERCUSTOMER_NEW = TEST_VIRTUAL_TOPIC + "MasterCustomer.New";
    public final static String MASTERCUSTOMER_NAME_CHANGE = TEST_VIRTUAL_TOPIC + "MasterCustomer.NameChange";

    public final static String TNUIDUSERMAPPING_INCOMING_QUEUE = "incoming.tnuIdUserMapping";
    public final static String TNUIDUSERMAPPING_NEW_TOPIC = TEST_VIRTUAL_TOPIC + "TnuIdUserMapping.New";
    public final static String TNUIDUSERMAPPING_DELETE_TOPIC = TEST_VIRTUAL_TOPIC + "TnuIdUserMapping.Delete";

    public final static String USERRESOURCE_INCOMING_QUEUE = "incoming.userResource";
    public final static String USERRESOURCE_NEW_TOPIC = TEST_VIRTUAL_TOPIC + "UserResource.New";
    public final static String USERRESOURCE_DELETE_TOPIC = TEST_VIRTUAL_TOPIC + "UserResource.Delete";
    public final static String USERRESOURCE_RESOURCE_ID_UPDATE_TOPIC = TEST_VIRTUAL_TOPIC + "UserResource.ResourceIdUpdate";
    public final static String USERRESOURCE_CSUSER_ID_UPDATE_TOPIC = TEST_VIRTUAL_TOPIC + "UserResource.CsUserIdUpdate";

    public final static String RESOURCE_INCOMING_QUEUE = "incoming.resource";
    public final static String RESOURCE_NEW_TOPIC = TEST_VIRTUAL_TOPIC + "Resource.New";
    public final static String RESOURCE_LOGICAL_DELETE_TOPIC = TEST_VIRTUAL_TOPIC + "Resource.LogicalDeleted";
    public final static String RESOURCE_TYPE_ID_UPDATE_TOPIC = TEST_VIRTUAL_TOPIC + "Resource.TypeIdChange";
    public final static String RESOURCE_TYPE_ID_KEY_UPDATE_TOPIC = TEST_VIRTUAL_TOPIC + "Resource.TypeIdKeyChange";
    public final static String RESOURCE_CONTENT_INHERIT_UPDATE_TOPIC = TEST_VIRTUAL_TOPIC + "Resource.ContentInheritChange";
    public final static String RESOURCE_STRUCTURE_INHERIT_UPDATE_TOPIC = TEST_VIRTUAL_TOPIC + "Resource.StructureInheritChange";

    public final static String MOBILE_OFFICE_INCOMING_QUEUE = "incoming.mobileOffice";
    public final static String MOBILE_OFFICE_NEW_TOPIC = TEST_VIRTUAL_TOPIC + "MobileOffice.New";
    public final static String MOBILE_OFFICE_UPDATE_TOPIC = TEST_VIRTUAL_TOPIC + "MobileOffice.Update";
    public final static String MOBILE_OFFICE_DELETE_TOPIC = TEST_VIRTUAL_TOPIC + "MobileOffice.Delete";

    public final static String USER_REFERENCE_INCOMING_QUEUE = "jms:queue:incoming.userReference";
    public final static String USER_REFERENCE_NEW_TOPIC = TEST_VIRTUAL_TOPIC + "UserReference.New";
    public final static String USER_REFERENCE_INVOICE_CHANGE_TOPIC = TEST_VIRTUAL_TOPIC + "UserReference.InvoiceChange";
    public final static String USER_REFERENCE_DESCR_CHANGE_TOPIC = TEST_VIRTUAL_TOPIC + "UserReference.DescrChange";
    public final static String USER_REFERENCE_LOGICAL_DELETE_TOPIC = TEST_VIRTUAL_TOPIC + "UserReference.LogicalDeleted";

    public final static String AGREEMENT_MEMBER_INCOMING_QUEUE = "incoming.agreementMember";
    public final static String AGREEMENT_MEMBER_NEW_TOPIC = TEST_VIRTUAL_TOPIC + "AgreementMember.New";
    public final static String AGREEMENT_MEMBER_LOGICAL_DELETE_TOPIC = TEST_VIRTUAL_TOPIC + "AgreementMember.LogicalDelete";

    public final static String AGREEMENT_OWNER_INCOMING_QUEUE = "incoming.agreementOwner";
    public final static String AGREEMENT_OWNER_NEW_TOPIC = TEST_VIRTUAL_TOPIC + "AgreementOwner.New";
    public final static String AGREEMENT_OWNER_DELETE_TOPIC = TEST_VIRTUAL_TOPIC + "AgreementOwner.Delete";
}
