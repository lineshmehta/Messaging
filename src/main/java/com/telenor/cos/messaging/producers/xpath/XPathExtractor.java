package com.telenor.cos.messaging.producers.xpath;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathDate;
import com.telenor.cos.messaging.producers.xpath.type.XPathInteger;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

import javax.annotation.PostConstruct;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Class which compiles the XPath expressions defined as private Stings on the subclass
 */
public class XPathExtractor {

    private static final XPath XPATH = XPathFactory.newInstance().newXPath();

    /**
     * The map of XPath expression to the compiled expressions
     */
    private Map<String, XPathExpression> map = new HashMap<String, XPathExpression>();

    @Autowired
    private XPathHelper xPathHelper;

    /**
     * Initializes the XparhExpressions based on xpath.properties
     * This is done by assuming that all private Strings are XPath expressions that should be compiled
     *
     * @throws XPathExpressionException if the expression cannot be compiled
     * @throws IllegalAccessException on error
     */
    @PostConstruct
    public void initializeXpathCompile() throws XPathExpressionException, IllegalAccessException {
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAccessible() && String.class.equals(field.getType())) {
                field.setAccessible(true);
                try {
                    String fieldValue = (String) field.get(this);
                    map.put(fieldValue, XPATH.compile(fieldValue));
                } catch (Exception e) {
                    throw new RuntimeException("Failed to load field for: " + getClass() + "." + field.getName(), e);
                } finally {
                    field.setAccessible(false);
                }
            }
        }
    }

    XPathLong getLong(String expression, Node message) {
        XPathExpression xPathExpression = map.get(expression);
        return xPathHelper.getLong(xPathExpression, message);
    }

    XPathString getString(String expression, Node message) {
        XPathExpression xPathExpression = map.get(expression);
        return xPathHelper.getString(xPathExpression, message);
    }

    XPathDate getDate(String expression, Node message) {
        XPathExpression xPathExpression = map.get(expression);
        return xPathHelper.getDate(xPathExpression, message);
    }

    XPathInteger getInteger(String expression, Node message) {
        XPathExpression xPathExpression = map.get(expression);
        return xPathHelper.getInteger(xPathExpression, message);
    }

}
