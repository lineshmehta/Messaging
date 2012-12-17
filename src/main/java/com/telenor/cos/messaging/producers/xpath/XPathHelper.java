package com.telenor.cos.messaging.producers.xpath;

import com.telenor.cos.messaging.CosMessagingInvalidDataException;
import com.telenor.cos.messaging.producers.xpath.type.XPathDate;
import com.telenor.cos.messaging.producers.xpath.type.XPathInteger;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class XPathHelper {

    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS");

    /**
     * Evaluate XPath expression and return the result as a <code>String</code> from the given node.
     *
     * @param xPathExpression the compiled XPath expression
     * @param node the node to extract the result from
     * @return the value extracted from the node
     */
    public XPathString getString(XPathExpression xPathExpression, Node node) {
        String value = getStringValue(xPathExpression, node);
        if(value == null){
            return null;
        }

        if(value.equalsIgnoreCase("null")){
            value = "";
        }
        return XPathString.valueOf(value);
    }


    /**
     * Evaluate XPath expression and return the result as a <code>Long</code> from the given node.
     * 
     * @param xPathExpression the compiled XPath expression
     * @param node the node to extract the result from
     * @return the value extracted from the node
     */
    public XPathLong getLong(XPathExpression xPathExpression, Node node) {
        try {
            String extractedValue = getStringValue(xPathExpression, node);
            if(extractedValue == null){
                return null;
            }

            if(extractedValue.equalsIgnoreCase("null")){
                extractedValue = null;
            }
            
            Long longValue = null;
            if(StringUtils.isNotEmpty(extractedValue)){
                longValue = Long.valueOf(StringUtils.trim(extractedValue));
            }
            return XPathLong.valueOf(longValue);
        } catch (NumberFormatException e) {
            throw new CosMessagingInvalidDataException("Number format error using: " + xPathExpression, e);
        }
    }

    /**
     * Evaluate XPath expression and return the result as a <code>Date</code> from the given node.
     * 
     * @param xPathExpression the compiled XPath expression
     * @param node the node to extract the result from
     * @return the value extracted from the node
     */
    public XPathDate getDate(XPathExpression xPathExpression, Node node) {
        String extractedValue = getStringValue(xPathExpression, node);
        if(extractedValue == null){
            return null;
        }

        if(extractedValue.equalsIgnoreCase("null")){
            extractedValue = null;
        }

        Date date = null;
        if(StringUtils.isNotEmpty(extractedValue)){
            date = parseDate(extractedValue);
        }
        return new XPathDate(date);
    }

    /**
     * Evaluate XPath expression and return the result as a <code>Integer</code> from the given node.
     * 
     * @param xPathExpression the compiled XPath expression
     * @param node the node to extract the result from
     * @return the value extracted from the node
     */
    public XPathInteger getInteger(XPathExpression xPathExpression, Node node) {
        try {
            String extractedValue = getStringValue(xPathExpression, node);
            if(extractedValue == null){
                return null;
            }

            if(extractedValue.equalsIgnoreCase("null")){
                extractedValue = null;
            }

            Integer integerValue = null;
            if(StringUtils.isNotEmpty(extractedValue)){
                integerValue = Integer.valueOf(StringUtils.trim(extractedValue));
            }
            return XPathInteger.valueOf(integerValue);

        } catch (NumberFormatException e) {
            throw new CosMessagingInvalidDataException("Number format error using: " + xPathExpression, e);
        }
    }

    /**
     * Evaluate XPath expression and return the result as a <code>String</code> from the given node.
     * Returns null if no matching node can be found for the xPathExpression. If an empty node is
     * found en empty String will be returned.
     *
     * @param xPathExpression the compiled XPath expression
     * @param node the node to extract the result from
     * @return the value extracted from the node
     */
    private String getStringValue(XPathExpression xPathExpression, Node node) {
        try {
            Node extractedNode = (Node) xPathExpression.evaluate(node, XPathConstants.NODE);
            if(extractedNode == null){
                return null;
            }

            Node value = extractedNode.getFirstChild();
            String nodeValue =  value != null ? value.getTextContent() : null;

            if ( nodeValue != null) {
                return nodeValue;
            }else{
                return "";
            }

        } catch (XPathExpressionException e) {
            throw new CosMessagingInvalidDataException("Error when extracting value from xml using: " + xPathExpression, e);
        }
    }

    /**
     * Parse the given date <code>String</code> and returns a <code>Date</code>.
     *
     * @param date the date <code>String</code> to create a <code>Date</code> from
     * @return a date
     */
    private Date parseDate(String date) {
        try {
            return dateTimeFormat.parse(date);
        } catch (ParseException e) {
            throw new CosMessagingInvalidDataException("Date parse error parsing" + date + " using format " + dateTimeFormat, e);
        }
    }

}