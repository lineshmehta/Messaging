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
public class SubscriptionEquipmentDeleteEventController extends SubscriptionEquipmentCommonController {
    
    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionEquipmentDeleteEventController.class);

    /**
     * The plain HTTP GET mapping. just forwarding the GET including the model.
     * 
     * @return a forward
     */
    @RequestMapping(value = "/subscriptionEquipmentDeleteEvent", method = RequestMethod.GET )
    public String testSubscriptionEquipmentDeleteEvent() {
        LOG.info("Called testSubscriptionEquipmentDeleteEvent() for GET");
        return "redirect:subscriptionEquipmentDeleteEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/subscriptionEquipmentDeleteEventForm", method = RequestMethod.GET)
    public void testSubscriptionEquipmentDeleteEventForm(Model model) {
        SubscriptionEquipmentForm form = getSubscriptionEquipmentForm();
        model.addAttribute(form);
        LOG.info("called testSubscriptionEquipmentDeleteEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/subscriptionEquipmentDeleteEvent", method = RequestMethod.POST)
    public void testSubscriptionEquipmentDeleteEvent(@ModelAttribute SubscriptionEquipmentForm form, Model model) {
        LOG.info("called testSubscriptionEquipmentDeleteEvent() for POST. form = " + form.toString());
        String xml = form.toUpdateXML(SubscriptionEquipmentForm.EventType.UPDATE, SubscriptionEquipmentForm.SubscriptionEquipmentUpdateSubType.LOGICAL_DELETE);
        LOG.info("called testSubscriptionEquipmentDeleteEvent() for POST. xml = " + xml);
        setUp();
        sendAndReceiveMessage(model, xml, DELETE_SUBSCRIPTION_EQUIPMENT);
    }
}
