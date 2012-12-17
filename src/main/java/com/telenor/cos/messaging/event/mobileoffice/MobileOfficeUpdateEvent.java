package com.telenor.cos.messaging.event.mobileoffice;

import com.telenor.cos.messaging.event.Event;

public class MobileOfficeUpdateEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = 7624220076113434038L;
    private String directoryNumber;
    private String extensionNumber;
    private String oldExtentionNumber;   

    /**
     * Constructor
     *                   git
     * @param directoryNumber
     *            the directoryNumber (msisdn)
     * @param extensionNumber
     *            the extensionNumber (shortNumber)
     * @param oldExtentionNumber
     *            the previous extensionNumber (shortNumber)
     */
    public MobileOfficeUpdateEvent(String directoryNumber, String extensionNumber, String oldExtentionNumber) {
        super(null, ACTION.UPDATED, TYPE.MOBILE_OFFER);
        this.directoryNumber = directoryNumber;
        this.extensionNumber = extensionNumber;
        this.oldExtentionNumber = oldExtentionNumber;
    }

    public String getDirectoryNumber() {
        return directoryNumber;
    }

    public String getExtensionNumber() {
        return extensionNumber;
    }

    public String getOldExtentionNumber() {
        return oldExtentionNumber;
    }
}
