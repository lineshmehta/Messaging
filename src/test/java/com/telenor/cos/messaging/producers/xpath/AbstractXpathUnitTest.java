package com.telenor.cos.messaging.producers.xpath;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Node;

import com.telenor.cos.messaging.util.TestHelper;

public class AbstractXpathUnitTest {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS");

    public Node stringToDom(String xmlSource) throws Exception {
        return  new TestHelper().stringToDom(xmlSource);
    }
    
    public Date createDate(String date) {
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException("Unable to parse date: " + date);
        }
    }
}