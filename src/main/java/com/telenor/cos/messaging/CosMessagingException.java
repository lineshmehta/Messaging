package com.telenor.cos.messaging;

import com.telenor.cos.cosmash.exception.CosException;

@SuppressWarnings("serial")
public class CosMessagingException extends CosException{

    /**
     * Constructor
     *
     * @param message error message
     * @param e root cause
     */
    public CosMessagingException(String message, Throwable e) {
        super(800, message, e);
    }

}