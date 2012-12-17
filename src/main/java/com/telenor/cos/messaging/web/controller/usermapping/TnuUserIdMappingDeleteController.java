package com.telenor.cos.messaging.web.controller.usermapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.usermapping.TnuidUserMappingDeleteEvent;
import com.telenor.cos.messaging.web.form.TnuUserIdMappingForm;

@Controller
public class TnuUserIdMappingDeleteController extends TnuUserIdMappingCommonController{

    private static final Logger LOG = LoggerFactory.getLogger(TnuUserIdMappingDeleteController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/tnuIdUserMappingDeleteEvent", method = RequestMethod.GET)
    public String testTnuIdUserMappingDeleteEvent() {
        LOG.info("called testTnuIdUserMappingDeleteEvent() for GET");
        return "redirect:tnuIdUserMappingDeleteEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/tnuIdUserMappingDeleteEventForm", method = RequestMethod.GET)
    public void testTnuIdUserMappingDeleteEventForm(Model model) {
        TnuUserIdMappingForm form = getUserMappingForm();
        model.addAttribute(form);
        LOG.info("called testTnuIdUserMappingDeleteEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/tnuIdUserMappingDeleteEvent", method = RequestMethod.POST)
    public void testTnuIdUserMappingDeleteEvent(@ModelAttribute TnuUserIdMappingForm form, Model model) {
        LOG.info("called testTnuIdUserMappingDeleteEvent() for POST. form = " + form.toString());
        TnuUserIdMappingForm userMappingForm = getUserMappingForm();
        userMappingForm.setApplicationId(form.getApplicationId());
        userMappingForm.setTnuId(form.getTnuId());
        String xml = userMappingForm.toDeleteEventXML(TnuUserIdMappingForm.EventType.DELETE);
        LOG.info("called testTnuIdUserMappingDeleteEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        TnuidUserMappingDeleteEvent consumedEvent = (TnuidUserMappingDeleteEvent) getJms().receive(DELETE_TNUID_USERMAPPING, correlationId);
        model.addAttribute("result", consumedEvent);
        LOG.info("received event = " + consumedEvent);
    }
    
}
