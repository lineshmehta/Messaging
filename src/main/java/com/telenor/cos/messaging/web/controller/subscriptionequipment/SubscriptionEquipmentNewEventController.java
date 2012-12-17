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
public class SubscriptionEquipmentNewEventController extends SubscriptionEquipmentCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionEquipmentNewEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/subscriptionEquipmentNewEvent", method = RequestMethod.GET)
    public String testSubscriptionEquipmentNewEvent() {
        LOG.info("called testSubscriptionEquipmentNewEvent() for GET");
        return "redirect:subscriptionEquipmentNewEventForm";
    }
    
    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/subscriptionEquipmentNewEventForm", method = RequestMethod.GET)
    public void testSubscriptionEquipmentNewEventForm(Model model) {
        SubscriptionEquipmentForm form = getSubscriptionEquipmentForm();
        model.addAttribute(form);
        LOG.info("called testSubscriptionEquipmentNewEventForm()");
    }
    
    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/subscriptionEquipmentNewEvent", method = RequestMethod.POST)
    public void testSubscriptionEquipmentNewEvent(@ModelAttribute SubscriptionEquipmentForm form, Model model) {
        LOG.info("called testSubscriptionEquipmentNewEvent() for POST. form = " + form.toString());
        String xml = form.toNewXML(SubscriptionEquipmentForm.EventType.INSERT);
        LOG.info("called testSubscriptionEquipmentNewEvent() for POST. xml = " + xml);
        setUp();
        sendAndReceiveMessage(model, xml, NEW_SUBSCRIPTION_EQUIPMENT);
    }
}
