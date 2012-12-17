package com.telenor.cos.messaging.web.controller.subscription;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.subscription.NewSubscriptionEvent;
import com.telenor.cos.messaging.web.form.RelSubscriptionForm;
import com.telenor.cos.messaging.web.form.SubscriptionForm;
import com.telenor.cos.messaging.web.form.SubscriptionForm.SubscriptionUpdateSubType;
import com.telenor.cos.messaging.web.form.UserReferenceForm;

/**
 * Controller for the SubscriptionNewEvent.
 */
@Controller
public class SubscriptionNewEventController extends SubscriptionCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionNewEventController.class);

    /**
     * 
     * The plain HTTP GET mapping, just forwarding the GET including the model
     * @return a forward
     * 
     */
    @RequestMapping(value = "/subscriptionNewEvent", method = RequestMethod.GET)
    public String testSubscriptionNewEvent() {
        LOG.info("called testSubscriptionNewEvent() for GET");
        return "redirect:subscriptionNewEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/subscriptionNewEventForm", method = RequestMethod.GET)
    public void testSubscriptionNewEventForm(Model model) {
        SubscriptionForm form = getSubscriptionForm();
        model.addAttribute(form);
        LOG.info("called testSubscriptionNewEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     * @throws InterruptedException InterruptedException.
     */
    @RequestMapping(value = "/subscriptionNewEvent", method = RequestMethod.POST)
    public void testSubscriptionNewEvent(@ModelAttribute SubscriptionForm form, Model model) throws InterruptedException {
        //Creating SubscriptionForm Default Values and UserEntered Values
        SubscriptionForm subscriptionForm = createSubscriptionForm(form);
        LOG.info("called testSubscriptionNewEvent() for POST. form = " + form.toString());
        String xml = subscriptionForm.toNewEventXML(SubscriptionForm.EventType.INSERT);
        String updateXml1 = subscriptionForm.toUpdateEventXML(SubscriptionForm.EventType.UPDATE,SubscriptionUpdateSubType.UPDATE_ONE);
        String updateXml2 = subscriptionForm.toUpdateEventXML(SubscriptionForm.EventType.UPDATE,SubscriptionUpdateSubType.UPDATE_TWO);
        LOG.info("called testSubscriptionNewEvent() for POST. Insert XML = " + xml + "\nFirst Update XML = " + updateXml1 + "\nSecond Update XML = " + updateXml2);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        sendXmlsToQueue(updateXml1,updateXml2);
        if(StringUtils.isNotEmpty(form.getUserReferenceForm().getNumberType())) {
            sendXmlsForUserReference(form);
        }
        //Following logic will be executed only when we need to add DataKort to new Subscription or existing subscription.
        if(StringUtils.isNotEmpty(form.getRelSubscriptionForm().getRelSubscrType())) {
            sendXmlsForRelSubscription(form);
        }
        NewSubscriptionEvent consumedSubscriptionEvent = (NewSubscriptionEvent) getJms().receive(SUBSCRIPTION_NEW_TOPIC,correlationId);
        model.addAttribute("result", consumedSubscriptionEvent);
        LOG.info("received event = " + consumedSubscriptionEvent);
    }

    private SubscriptionForm createSubscriptionForm(SubscriptionForm form) {
        //Gets SubscriptionForm with Default Values.
        SubscriptionForm subscriptionForm = getSubscriptionForm();

        //Setting form values entered by user.
        subscriptionForm.setSubscrId(form.getSubscrId());
        subscriptionForm.setContractId(form.getContractId());
        subscriptionForm.setS212ProductId(form.getS212ProductId());
        subscriptionForm.setDirectoryNumberId(form.getDirectoryNumberId());
        subscriptionForm.setCustIdResp(form.getCustIdResp());
        subscriptionForm.setCustIdPayer(form.getCustIdPayer());
        subscriptionForm.setCustIdUser(form.getCustIdUser());
        subscriptionForm.setSubscrValidFromDate(form.getSubscrValidFromDate());
        subscriptionForm.setSubscrHasSecretNumber(form.getSubscrHasSecretNumber());
        subscriptionForm.setAccId(form.getAccId());
        subscriptionForm.setSubscrStatusId(form.getSubscrStatusId());

        return subscriptionForm;
    }

    private void sendXmlsForUserReference(SubscriptionForm subscriptionForm) {
        Long ownerSubscriptionId = subscriptionForm.getSubscrId();
        Long msisdn = subscriptionForm.getDirectoryNumberId();
        Long contractId = subscriptionForm.getContractId();

        UserReferenceForm userReferenceForm = createUserReferenceForm();
        userReferenceForm.setSubscriptionId(ownerSubscriptionId);
        userReferenceForm.setDirectoryNumberId(msisdn);
        userReferenceForm.setContractId(contractId);
        userReferenceForm.setNumberType(subscriptionForm.getUserReferenceForm().getNumberType());
        userReferenceForm.setUserRefDescr(subscriptionForm.getUserReferenceForm().getUserRefDescr());
        userReferenceForm.seteInvoiceRef(subscriptionForm.getUserReferenceForm().geteInvoiceRef());

        //Create New UserReference XML.
        String userReferenceInsertXml = userReferenceForm.toNewEventXML(UserReferenceForm.EventType.INSERT);
        getJms().send(INCOMING, userReferenceInsertXml);
        //LOG.info("called testSubscriptionNewEvent() for POST. UserReference Insert XML = " + userReferenceInsertXml);
    }

    private void sendXmlsForRelSubscription(SubscriptionForm subscriptionForm) {
        Long subscriptionId = subscriptionForm.getSubscrId();
        Long relOwnerSubscriptionId = subscriptionForm.getRelSubscriptionForm().getSubscrIdOwner();
        Long memberSubscriptionId = subscriptionForm.getRelSubscriptionForm().getSubscrIdMember();
        String relSubscriptionType = subscriptionForm.getRelSubscriptionForm().getRelSubscrType();
        Long msisdn = subscriptionForm.getRelSubscriptionForm().getDirectoryNumberId();

        //Gets RelSubscriptionForm with Dafault Values.
        RelSubscriptionForm relSubscriptionForm = createRelSubscriptionForm();

        relSubscriptionForm.setSubscrIdMember(subscriptionId);
        relSubscriptionForm.setSubscrIdOwner(relOwnerSubscriptionId);
        relSubscriptionForm.setRelSubscrType(relSubscriptionType);

        //Following logic will be executed only when DataKort is added with NewSubscription.
        if(subscriptionId.equals(relOwnerSubscriptionId)) {
            //Create Main Subscription For DataKort,DataKort2 and Tvilling
            SubscriptionForm subscrForm = createSubscriptionForm(subscriptionForm);
            subscrForm.setSubscrId(memberSubscriptionId);
            subscrForm.setSubscrTypeId(relSubscriptionType);
            subscrForm.setDirectoryNumberId(msisdn);
            relSubscriptionForm.setSubscrIdMember(memberSubscriptionId);
            relSubscriptionForm.setSubscrIdOwner(subscriptionId);
            String relSubscrInsertXml = subscrForm.toNewEventXML(SubscriptionForm.EventType.INSERT);
            String relSubscrupdateXml1 = subscrForm.toUpdateEventXML(SubscriptionForm.EventType.UPDATE,SubscriptionUpdateSubType.UPDATE_ONE);
            String relSubscrupdateXml2 = subscrForm.toUpdateEventXML(SubscriptionForm.EventType.UPDATE,SubscriptionUpdateSubType.UPDATE_TWO);
            sendXmlsToQueue(relSubscrInsertXml,relSubscrupdateXml1,relSubscrupdateXml2);
            LOG.info("called testSubscriptionNewEvent() for POST. Insert XML = " + relSubscrInsertXml + "\nFirst Update XML = " + relSubscrupdateXml1 + "\nSecond Update XML = " + relSubscrupdateXml2);
        }

        //Create New RelSubscription XML.
        String relSubScrXml = relSubscriptionForm.toNewEventXML(RelSubscriptionForm.EventType.INSERT);
        sendXmlsToQueue(relSubScrXml);

        LOG.info("called testSubscriptionNewEvent() for POST. RelSubscription XML = "+ relSubScrXml);
    }

    private void sendXmlsToQueue(String...xmls) {
        for(int i =0;i<xmls.length;i++) {
            getJms().send(INCOMING, xmls[i]);
        }
    }
}
