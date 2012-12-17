package com.telenor.cos.messaging.web.controller.usermapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.usermapping.TnuIdUserMappingNewEvent;
import com.telenor.cos.messaging.web.form.TnuUserIdMappingForm;

/**
 * WebApp Controller For UserMapping New Events.
 * @author Babaprakash D
 *
 */
@Controller
public class TnuUserIdMappingNewController extends TnuUserIdMappingCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(TnuUserIdMappingNewController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/tnuIdUserMappingNewEvent", method = RequestMethod.GET)
    public String testTnuIdUserMappingNewEvent() {
        LOG.info("called testTnuIdUserMappingNewEvent() for GET");
        return "redirect:tnuIdUserMappingNewEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/tnuIdUserMappingNewEventForm", method = RequestMethod.GET)
    public void testTnuIdUserMappingNewEventForm(Model model) {
        TnuUserIdMappingForm form = getUserMappingForm();
        model.addAttribute(form);
        LOG.info("called testTnuIdUserMappingNewEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/tnuIdUserMappingNewEvent", method = RequestMethod.POST)
    public void testTnuIdUserMappingNewEvent(@ModelAttribute TnuUserIdMappingForm form, Model model) {
        LOG.info("called testTnuIdUserMappingNewEvent() for POST. form = " + form.toString());
        TnuUserIdMappingForm userMappingForm = getUserMappingForm();
        userMappingForm.setApplicationId(form.getApplicationId());
        userMappingForm.setTnuId(form.getTnuId());
        userMappingForm.setCsUserId(form.getCsUserId());
        String xml = userMappingForm.toNewEventXML(TnuUserIdMappingForm.EventType.INSERT);
        LOG.info("called testTnuIdUserMappingNewEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        TnuIdUserMappingNewEvent consumedEvent = (TnuIdUserMappingNewEvent) getJms().receive(NEW_TNUID_USERMAPPING, correlationId);
        model.addAttribute("result", consumedEvent);
        LOG.info("received event = " + consumedEvent);
    }
}
