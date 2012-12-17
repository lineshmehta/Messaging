package com.telenor.cos.messaging.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.telenor.cos.messaging.CosCorrelationJmsTemplate;

/**
 * Common controller to be subclassed.
 */
public abstract class CommonController {

    @Autowired
    private CosCorrelationJmsTemplate jms;

    public final static String INCOMING = "incoming.repserver";

    /**
     * @return the jms template to be used for testing.
     */
    public CosCorrelationJmsTemplate getJms() {
        return jms;
    }

    /**
     * Creates a usable date format
     *
     * @param binder binder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
