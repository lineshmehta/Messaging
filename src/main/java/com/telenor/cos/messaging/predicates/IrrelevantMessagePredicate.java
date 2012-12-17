package com.telenor.cos.messaging.predicates;

import static javax.xml.xpath.XPathConstants.NODESET;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Component
public class IrrelevantMessagePredicate implements Predicate {

    private XPath xPath = XPathFactory.newInstance().newXPath();

    private static Logger log = LoggerFactory.getLogger(IrrelevantMessagePredicate.class);
    private String regex = "INFO_CHG.*|INFO_TIMESTAMP|LOGON_DATE_SUCCESSFUL";
    private String valuesXpath = "//values/cell";
    private String oldValuesXpath = "//oldValues/cell";
    private String deleteXpath = "//delete";

    @Override
    public boolean matches(Exchange exchange) {
        try {
            boolean isMessageWithoutRelevantChanges = doNodesOnlyContainUninterestingUpdates(exchange);
            logMessage(exchange, isMessageWithoutRelevantChanges);
            return isMessageWithoutRelevantChanges;
        } catch (XPathExpressionException e) {
            log.error("Problem evaluating the Value nodes in incoming message: " + e.getMessage());
            return false;
        } catch (IOException ioe) {
            log.error("Problem reading properties from file: " + ioe.getMessage());
            return false;
        }
    }

    private void logMessage(Exchange exchange, boolean isMessageWithoutRelevantChanges) {
        if (isMessageWithoutRelevantChanges) {
            log.info("Message is NOT relevant. Will NOT process this message further: \n" + exchange.getIn(Message.class).getBody());
        } else if (log.isDebugEnabled()) {
            log.debug("Message is relevant. Message will be further processed: \n" + exchange.getIn(Message.class).getBody());
        }
    }

    private boolean doNodesOnlyContainUninterestingUpdates(Exchange exchange) throws IOException, XPathExpressionException {
        NodeList valueNodes = extractNodesFromXmlMessage(exchange, valuesXpath);
        boolean isDeleteEvent = isDeleteEvent(exchange);
        List<Node> nodesWithPotentialUpdates = removeUninterestingNodes(valueNodes);
        if (nodesWithPotentialUpdates.size() == 0 && !isDeleteEvent) {
            return true;
        }
        NodeList oldValueNodes = extractNodesFromXmlMessage(exchange, oldValuesXpath);
        List<Node> emptyValueNodes = findNodesWithEmptyFields(nodesWithPotentialUpdates);
        return !emptyValueNodes.isEmpty() && nodesWithPotentialUpdates.size() == emptyValueNodes.size() && oldValuesContainsSameEmptyNodes(emptyValueNodes, oldValueNodes);
    }

    private boolean isDeleteEvent(Exchange exchange) throws XPathExpressionException {
        NodeList nodeList = extractNodesFromXmlMessage(exchange, deleteXpath);
        return nodeList != null && nodeList.getLength() > 0;
    }

    private List<Node> findNodesWithEmptyFields(List<Node> nodes) {
        List<Node> emptyNodes = new ArrayList<Node>();
        for (Node node : nodes) {
            String nodeValue = node.getTextContent();
            if (isEmptyOrNull(nodeValue)) {
                emptyNodes.add(node);
            }
        }
        return emptyNodes;
    }

    private boolean isEmptyOrNull(String nodeValue) {
        return (nodeValue.isEmpty() || nodeValue.matches("[ \\t]*") || nodeValue.equalsIgnoreCase("NULL"));
    }

    private boolean oldValuesContainsSameEmptyNodes(List<Node> emptyValueNodes, NodeList oldValueNodes) throws XPathExpressionException {
        List<Node> emptyOldValueNodes = findNodesWithEmptyFields(nodeListToList(oldValueNodes));
        return !emptyOldValueNodes.isEmpty() && emptyOldValueNodes.containsAll(emptyValueNodes);
    }

    private List<Node> nodeListToList(NodeList nodeList) {
        List<Node> nodes = new ArrayList<Node>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            nodes.add(nodeList.item(i));
        }
        return nodes;
    }

    private List<Node> removeUninterestingNodes(NodeList nodes) {
        Pattern pattern = Pattern.compile(regex);
        List<Node> interestingNodes = new ArrayList<Node>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Matcher matcher = pattern.matcher(nodes.item(i).getAttributes().getNamedItem("name").getNodeValue());
            boolean match = matcher.matches();
            if (!match) {
                interestingNodes.add(nodes.item(i));
            }
        }
        return interestingNodes;
    }

    private NodeList extractNodesFromXmlMessage(Exchange exchange, String xPathString) throws XPathExpressionException {
        Message message = exchange.getIn(Message.class);
        XPathExpression xPathExpression = xPath.compile(xPathString);
        Object result = xPathExpression.evaluate(message.getBody(Node.class), NODESET);
        return (NodeList) result;
    }
}
