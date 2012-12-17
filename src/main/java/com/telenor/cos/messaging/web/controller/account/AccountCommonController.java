package com.telenor.cos.messaging.web.controller.account;

import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.web.controller.CommonController;
import com.telenor.cos.messaging.web.form.AccountForm;

/**
 * Common Controller for Account Events.
 * @author Babaprakash D
 *
 */
public class AccountCommonController extends CommonController {
    
    public final static String ACCOUNT_NEW = "Consumer.WTEST.VirtualTopic.Accounts.New";
    public final static String ACCOUNT_LOGICAL_DELETE = "Consumer.WTEST.VirtualTopic.Accounts.LogicalDeleted";
    public final static String ACCOUNT_NAME_CHANGE = "Consumer.WTEST.VirtualTopic.Accounts.NameChange";
    public final static String ACCOUNT_STATUS_CHANGE = "Consumer.WTEST.VirtualTopic.Accounts.StatusChange";
    public final static String ACCOUNT_INVOICE_FORMAT_CHANGE = "Consumer.WTEST.VirtualTopic.Accounts.InvoiceFormatChange";
    public final static String ACCOUNT_PAYER_CHANGE = "Consumer.WTEST.VirtualTopic.Accounts.PayerChange";
    public final static String ACCOUNT_OWNER_CHANGE = "Consumer.WTEST.VirtualTopic.Accounts.OwnerChange";
    public final static String ACCOUNT_PAYMENTSTATUS_CHANGE = "Consumer.WTEST.VirtualTopic.Accounts.PaymentStatusChange";
    public final static String ACCOUNT_TYPE_CHANGE = "Consumer.WTEST.VirtualTopic.Accounts.TypeChange";
    
    private boolean queuesSetUp = false;

    /**
     * Sets up the JMS-environment.
     */
    public void setUp() {
        if (!queuesSetUp) {
            queuesSetUp = true;
            getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
            getJms().receive(ACCOUNT_NEW);
            getJms().receive(ACCOUNT_LOGICAL_DELETE);
            getJms().receive(ACCOUNT_NAME_CHANGE);
            getJms().receive(ACCOUNT_STATUS_CHANGE);
            getJms().receive(ACCOUNT_INVOICE_FORMAT_CHANGE);
            getJms().receive(ACCOUNT_PAYER_CHANGE);
            getJms().receive(ACCOUNT_OWNER_CHANGE);
            getJms().receive(ACCOUNT_PAYMENTSTATUS_CHANGE);
            getJms().receive(ACCOUNT_TYPE_CHANGE);
            getJms().setReceiveTimeout(3000L);
        }
    }

    /**
     * @return a form with default values
     */
    protected AccountForm getAccountForm() {
        AccountForm accountForm = new AccountForm();
        accountForm.setCustIdPayer(Long.valueOf(999999051));
        accountForm.setCustIdResp(Long.valueOf(999999051));
        accountForm.setOldCustIdPayer(Long.valueOf(999999051));
        accountForm.setOldCustIdResp(Long.valueOf(999999051));
        accountForm.setAccountName("David");
        accountForm.setOldAccountName("David");
        accountForm.setOldAccountTypeId("NO");
        accountForm.setAccTypeId("NO");
        accountForm.setOldAccountStatusId2("PA");
        accountForm.setAccStatusId2("PA");
        return accountForm;
    }
    /**
     * Returns AccountForm
     * @param accountForm accountForm.
     * @return accountForm
     */
    protected AccountForm createAccountUpdateCommonForm(AccountForm accountForm) {
        AccountForm form = getAccountForm();
        form.setAccId(accountForm.getAccId());
        form.setCustIdResp(accountForm.getCustIdResp());
        form.setOldCustIdPayer(accountForm.getOldCustIdPayer());
        form.setOldCustIdResp(accountForm.getOldCustIdResp());
        form.setOldAccountName(accountForm.getOldAccountName());
        form.setOldAccountStatusId(accountForm.getOldAccountStatusId());
        form.setOldAccountTypeId(accountForm.getOldAccountTypeId());
        form.setOldAccountStatusId2(accountForm.getOldAccountStatusId2());
        form.setOldAccInvMedium(accountForm.getOldAccInvMedium());
        return form;
    }
}
