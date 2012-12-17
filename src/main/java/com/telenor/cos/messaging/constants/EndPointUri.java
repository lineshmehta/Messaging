package com.telenor.cos.messaging.constants;


/**
 * Constants holding end point URIs.
 */
public final class EndPointUri {


    private EndPointUri() {
        // NOP
    }

    private static final String ACTIVEMQ_TOPIC_VIRTUAL_TOPIC = "activemq:topic:VirtualTopic.";

    public static final String DEAD_LETTER_QUEUE = "jms:queue:deadLetter";

    public static final String INVALID_MESSAGE_QUEUE = "jms:queue:invalidMessages";

    public final static String REPSERVER_QUEUE = "jms:queue:incoming.repserver";

    public final static String INCOMING_QUEUE = "jms:queue:incoming";

    public final static String INCOMING_EVENT_QUEUE = "jms:queue:incoming.event";

    public final static String SUBSCRIPTION_EQUPMENT_INCOMING_QUEUE = "jms:queue:incoming.subscriptionEquipment";
    public final static String SUBSCRIPTION_EQUPMENT_NEW_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "SubscriptionEquipment.New";
    public final static String SUBSCRIPTION_EQUPMENT_UPDATE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "SubscriptionEquipment.Update";
    public final static String SUBSCRIPTION_EQUPMENT_DELETE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "SubscriptionEquipment.Delete";


    public final static String SUBSCRIPTION_INCOMING_QUEUE = "jms:queue:incoming.subscription";

    public final static String SUBSCRIPTION_NEW_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Subscriptions.New";

    public final static String SUBSCRIPTION_UPDATED_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Subscriptions.Updated";

    public final static String SUBSCRIPTION_LOGICAL_DELETE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Subscriptions.LogicalDeleted";

    public final static String SUBSCRIPTION_EXPIRED_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Subscriptions.Expired";

    public final static String SUBSCRIPTION_CHANGE_USER_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Subscriptions.ChangeUser";

    public final static String SUBSCRIPTION_CHANGE_ACCOUNT_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Subscriptions.ChangeAccount";

    public final static String SUBSCRIPTION_CHANGE_TYPE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Subscriptions.ChangeType";

    public final static String SUBSCRIPTION_CHANGE_STATUS_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Subscriptions.ChangeStatus";

    public final static String SUBSCRIPTION_SECRET_NUMBER_UPDATE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Subscriptions.SecretNumber";

    public final static String SUBSCRIPTION_SHORT_NUMBER_UPDATE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Subscriptions.ShortNumber";


    public final static String CUSTOMER_INCOMING_QUEUE = "jms:queue:incoming.customer";

    public final static String CUSTOMER_NEW_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Customers.New";

    public final static String CUSTOMER_LOGICAL_DELETE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Customers.LogicalDeleted";

    public final static String CUSTOMER_NAME_CHANGE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Customers.NameChange";

    public final static String CUSTOMER_ADRESS_CHANGE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Customers.AddressChange";


    public final static String AGREEMENT_INCOMING_QUEUE = "jms:queue:incoming.agreement";

    public final static String AGREEMENT_NEW_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Agreement.New";

    public final static String AGREEMENT_LOGICAL_DELETE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Agreement.LogicalDelete";


    public final static String ACCOUNT_INCOMING_QUEUE = "jms:queue:incoming.account";

    public final static String ACCOUNT_NEW_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Accounts.New";

    public final static String ACCOUNT_LOGICAL_DELETE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Accounts.LogicalDeleted";

    public final static String ACCOUNT_NAME_CHANGE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Accounts.NameChange";

    public final static String ACCOUNT_STATUS_CHANGE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Accounts.StatusChange";

    public final static String ACCOUNT_INVOICE_FORMAT_CHANGE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Accounts.InvoiceFormatChange";

    public final static String ACCOUNT_PAYER_CHANGE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Accounts.PayerChange";

    public final static String ACCOUNT_OWNER_CHANGE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Accounts.OwnerChange";

    public final static String ACCOUNT_PAYMENTSTATUS_CHANGE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Accounts.PaymentStatusChange";

    public final static String ACCOUNT_TYPE_CHANGE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Accounts.TypeChange";


    public final static String MASTERCUSTOMER_INCOMING_QUEUE = "jms:queue:incoming.mastercustomer";

    public final static String MASTERCUSTOMER_NEW_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "MasterCustomer.New";

    public final static String MASTERCUSTOMER_NAME_CHANGE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "MasterCustomer.NameChange";

    public final static String MASTERCUSTOMER_LOGICAL_DELETE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "MasterCustomer.LogicalDeleted";


    public final static String MASTERSTRUCTURE_INCOMING_QUEUE = "jms:queue:incoming.masterstructure";

    public final static String MASTERSTRUCTURE_NEW_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "MasterStructure.New";

    public final static String MASTERSTRUCTURE_UPDATED_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "MasterStructure.Updated";

    public final static String MASTERSTRUCTURE_DELETE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "MasterStructure.LogicalDeleted";


    public final static String USERRESOURCE_INCOMING_QUEUE = "jms:queue:incoming.userResource";

    public final static String USERRESOURCE_NEW_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "UserResource.New";

    public final static String USERRESOURCE_RESOURCE_ID_UPDATE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "UserResource.ResourceIdUpdate";

    public final static String USERRESOURCE_CSUSER_ID_UPDATE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "UserResource.CsUserIdUpdate";

    public final static String USERRESOURCE_DELETE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "UserResource.Delete";


    public final static String TNUIDUSERMAPPING_INCOMING_QUEUE = "jms:queue:incoming.tnuIdUserMapping";

    public final static String TNUIDUSERMAPPING_NEW_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "TnuIdUserMapping.New";

    public final static String TNUIDUSERMAPPING_DELETE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "TnuIdUserMapping.Delete";


    public final static String RESOURCE_INCOMING_QUEUE = "jms:queue:incoming.resource";

    public final static String RESOURCE_NEW_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Resource.New";

    public final static String RESOURCE_TYPE_ID_UPDATE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Resource.TypeIdChange";

    public final static String RESOURCE_TYPE_ID_KEY_UPDATE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Resource.TypeIdKeyChange";

    public final static String RESOURCE_CONTENT_INHERIT_UPDATE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Resource.ContentInheritChange";

    public final static String RESOURCE_STRUCTURE_INHERIT_UPDATE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Resource.StructureInheritChange";

    public final static String RESOURCE_LOGICAL_DELETE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "Resource.LogicalDeleted";


    public final static String MOBILE_OFFICE_INCOMING_QUEUE = "jms:queue:incoming.mobileOffice";

    public final static String MOBILE_OFFICE_NEW_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "MobileOffice.New";

    public final static String MOBILE_OFFICE_UPADTE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "MobileOffice.Update";

    public final static String MOBILE_OFFICE_DELETE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "MobileOffice.Delete";



    public final static String USER_REFERENCE_INCOMING_QUEUE = "jms:queue:incoming.userReference";

    public final static String USER_REFERENCE_NEW_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "UserReference.New";

    public final static String USER_REFERENCE_INVOICE_CHANGE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "UserReference.InvoiceChange";

    public final static String USER_REFERENCE_DESCR_CHANGE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "UserReference.DescrChange";

    public final static String USER_REFERENCE_LOGICAL_DELETE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "UserReference.LogicalDeleted";


    public final static String AGREEMENT_MEMBER_INCOMING_QUEUE = "jms:queue:incoming.agreementMember";

    public final static String AGREEMENT_MEMBER_NEW_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "AgreementMember.New";

    public final static String AGREEMENT_MEMBER_LOGICAL_DELETE_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "AgreementMember.LogicalDelete";


    public final static String AGREEMENT_OWNER_INCOMING_QUEUE = "jms:queue:incoming.agreementOwner";

    public final static String AGREEMENT_OWNER_NEW_TOPIC = ACTIVEMQ_TOPIC_VIRTUAL_TOPIC + "AgreementOwner.New";

}
