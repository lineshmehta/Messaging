package com.telenor.cos.messaging.producers.xpath;

import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import javax.annotation.PostConstruct;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Extracts csUserId and ResourceId from UserResource message.
 *
 * @author Babaprakash D
 */
@Component
public abstract class UserResourceXpathExtractor {

    protected static final XPath XPATH = XPathFactory.newInstance().newXPath();

    @Autowired
    private XPathHelper xPathHelper;
    private XPathExpression csUserIdXPathExpr;
    private XPathExpression resourceIdXPathExpr;

    /**
     * Initializes the XparhExpressions based on xpath.properties
     *
     * @throws XPathExpressionException if the expression cannot be compiled
     */
    @PostConstruct
    public void initializeXpathCompile() throws XPathExpressionException {
        csUserIdXPathExpr = XPATH.compile(getCsUserIdExpr());
        resourceIdXPathExpr = XPATH.compile(getResourceIdExpr());
    }

    /**
     * Extracts CS_USER_ID column data from the message received from repServer.
     *
     * @param message received from replication server.
     * @return csUserId.
     */
    public XPathString getCsUserId(Node message) {
        return xPathHelper.getString(csUserIdXPathExpr, message);
    }

    /**
     * Extracts RESOURCE_ID column data from the message received from repServer.
     *
     * @param message received from replication server.
     * @return resourceId.
     */
    public XPathLong getResourceId(Node message) {
        return xPathHelper.getLong(resourceIdXPathExpr, message);
    }

    /**
     * @return an XPath-expression
     */
    abstract public String getCsUserIdExpr();

    /**
     * @return an XPath-expression
     */
    abstract public String getResourceIdExpr();

}
