package com.telenor.cos.messaging.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MessageFormattingUtilTest {

    @Test
    public void getFormattedPostCodeIdMainWithPostCodeIdNull() {
        String formattedPostCodeIdMain = MessageFormattingUtil.getFormattedPostCodeIdMain(null);
        assertEquals("Unexpected PostCodeIdMain",null,formattedPostCodeIdMain);
    }

    @Test
    public void getFormattedPostCodeIdMainWithThreeDigitPostCodeId() {
        String formattedPostCodeIdMain = MessageFormattingUtil.getFormattedPostCodeIdMain(Long.valueOf(300));
        assertEquals("Unexpected PostCodeIdMain","0300",formattedPostCodeIdMain);
    }

    @Test
    public void getFormattedPostCodeIdMainWithFourDigitPostCodeId() {
        String formattedPostCodeIdMain = MessageFormattingUtil.getFormattedPostCodeIdMain(Long.valueOf(4400));
        assertEquals("Unexpected PostCodeIdMain","4400",formattedPostCodeIdMain);
    }
}
