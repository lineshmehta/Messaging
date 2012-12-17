package com.telenor.cos.messaging.web.controller.subscriptionequipment;

import com.telenor.cos.messaging.web.form.SubscriptionEquipmentForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SubscriptionEquipmentUpdateController extends SubscriptionEquipmentCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionEquipmentUpdateController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/subscriptionEquipmentUpdateEvent", method = RequestMethod.GET)
    public String testSubscriptionEquipmentUpdateEvent() {
        LOG.info("called testSubscriptionEquipmentUpdateEvent() for GET");
        return "redirect:subscriptionEquipmentUpdateEventForm";
    }
    
    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/subscriptionEquipmentUpdateEventForm", method = RequestMethod.GET)
    public void testSubscriptionEquipmentUpdateEventForm(Model model) {
        SubscriptionEquipmentForm form = getSubscriptionEquipmentForm();
        model.addAttribute(form);
        LOG.info("called testSubscriptionEquipmentUpdateEventForm()");
    }
    
    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/subscriptionEquipmentUpdateEvent", method = RequestMethod.POST)
    public void testSubscriptionEquipmentUpdateEvent(@ModelAttribute SubscriptionEquipmentForm form, Model model) {
        LOG.info("called testSubscriptionEquipmentUpdateEvent() for POST. form = " + form.toString());
        String xml = form.toUpdateXML(SubscriptionEquipmentForm.EventType.UPDATE, SubscriptionEquipmentForm.SubscriptionEquipmentUpdateSubType.CHANGE_IMSI_NUMBER);
        LOG.info("called testSubscriptionEquipmentUpdateEvent() for POST. xml = " + xml);
        setUp();
        sendAndReceiveMessage(model, xml, UPDATE_SUBSCRIPTION_EQUIPMENT);
    }
}
