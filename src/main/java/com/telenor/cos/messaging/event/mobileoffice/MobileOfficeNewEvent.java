package com.telenor.cos.messaging.event.mobileoffice;

import com.telenor.cos.messaging.event.Event;

public class MobileOfficeNewEvent extends Event {

    private String directoryNumber;
    private String extensionNumber;

    /**
     * 
     */
    private static final long serialVersionUID = 2507082794767003876L;

    /**
     * Constructor
     * 
     * @param directoryNumber
     *            the directoryNumber (msisdn)
     * @param extensionNumber
     *            the extensionNumber (shortNumber)
     */
    public MobileOfficeNewEvent(String directoryNumber, String extensionNumber) {
        super(null, ACTION.CREATED, TYPE.MOBILE_OFFER);
        this.directoryNumber = directoryNumber;
        this.extensionNumber = extensionNumber;
    }

    public String getDirectoryNumber() {
        return directoryNumber;
    }

    public String getExtensionNumber() {
        return extensionNumber;
    }

}
