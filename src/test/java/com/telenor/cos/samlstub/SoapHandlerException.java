package com.telenor.cos.samlstub;

@SuppressWarnings("serial")
public class SoapHandlerException extends RuntimeException {
    /**
     * @param message - the error message that is to be passed on
     * @param e       - the exception message
     * @see Error code for this exception is set randomly to 92
     */
    public SoapHandlerException(String message, Throwable e) {
        super(message, e);
    }
}
