package com.telenor.cos.messaging.web.controller;

import com.telenor.cos.messaging.web.form.TestForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Per Jørgen Walstrøm
 */

@Controller
public class TestController extends CommonController {

    @RequestMapping(method = RequestMethod.GET)
    public void findTestForm(Model model) {
        model.addAttribute(new TestForm());
    }
}
