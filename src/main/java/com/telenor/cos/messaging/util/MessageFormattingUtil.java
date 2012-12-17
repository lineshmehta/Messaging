package com.telenor.cos.messaging.util;

import java.text.MessageFormat;

public final class MessageFormattingUtil {

    private static final MessageFormat POSTCODE_MESSAGE_FORMAT = new MessageFormat("{0,number,0000}");

    private MessageFormattingUtil() {

    }

    /**
     * Formats PostCodeIdMain to fourDigits.
     * @param postCodeIdToFormat postCodeIdMain to format.
     * @return formatted String.
     */
    public static String getFormattedPostCodeIdMain(Long postCodeIdToFormat) {
        return postCodeIdToFormat == null ? null : POSTCODE_MESSAGE_FORMAT.format(new Object[]{postCodeIdToFormat});
    }
}
