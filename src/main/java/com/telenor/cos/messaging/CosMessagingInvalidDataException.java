package com.telenor.cos.messaging;


/**
 * This exception is thrown when input data is invalid.
 */
@SuppressWarnings("serial")
public class CosMessagingInvalidDataException extends CosMessagingException {
    
    /**
     * Constructor.
     * 
     * @param message the error message
     * @param e the root cause
     */
    public CosMessagingInvalidDataException(String message, Throwable e) {
        super(message, e);
    }

}
