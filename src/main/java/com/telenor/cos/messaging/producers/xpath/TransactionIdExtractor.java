package com.telenor.cos.messaging.producers.xpath;

import javax.annotation.PostConstruct;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

@Component
public class TransactionIdExtractor {

    private static final XPath xPath = XPathFactory.newInstance().newXPath();

    private static final String TRANSACTION_ID_PATH_EXPR = "string(//tran/@eventId)";


    private XPathExpression transactionIdExpression;

    /**
     * Initializes the XpathExpressions based on xpath.properties
     *
     * @throws XPathExpressionException if the expression cannot be compiled
     */
    @PostConstruct
    public void initializeXpathCompile() throws XPathExpressionException {
        transactionIdExpression = xPath.compile(TRANSACTION_ID_PATH_EXPR);
    }

    /**
     * Evaluates the node and return the transactionId
     *
     * @param message the message
     * @return transactionId
     * @throws javax.xml.xpath.XPathExpressionException
     *          if message can't be parsed
     */
    public String getTransactionId(Node message) throws XPathExpressionException {
        return transactionIdExpression.evaluate(message);
    }

    @Override
    public String toString() {
        return "TransactionIdExtractor{" +
                "transactionIdPath='" + TRANSACTION_ID_PATH_EXPR + '\'' +
                '}';
    }
}
