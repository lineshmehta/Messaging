package com.telenor.cos.messaging.event.mobileoffice;

import com.telenor.cos.messaging.event.Event;


public class MobileOfficeDeleteEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = -1L;
    private String directoryNumber;
    private String oldExtensionNumber;

    /**
     * Constructor
     *
     * @param directoryNumber the directoryNumber (msisdn)
     *
     * @param oldExtensionNumber the previous extensionNumber (short number)
     */
    public MobileOfficeDeleteEvent(String directoryNumber, String oldExtensionNumber) {
        super(null, ACTION.DELETE, TYPE.MOBILE_OFFER);
        this.directoryNumber = directoryNumber;
        this.oldExtensionNumber = oldExtensionNumber;
    }

    public String getDirectoryNumber() {
        return directoryNumber;
    }

    public String getOldExtensionNumber() {
        return oldExtensionNumber;
    }
}
