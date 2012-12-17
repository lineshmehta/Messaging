package com.telenor.cos.messaging.producers;

import org.w3c.dom.Node;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public final class XmlStringPrinter {

    private  XmlStringPrinter() {
    }

    /**
     * returns xml as string
     * @param node the xml
     * @return the string
     */
    public static String getXmlContent(Node node) {
        try {
            return getXmlAsString(node, setUpTransformer());
        } catch (TransformerException e) {
            return e.getMessage();
        }
    }

    private static String getXmlAsString(Node node, Transformer trans) throws TransformerException {
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(node);
        trans.transform(source, result);
        return sw.toString();
    }

    private static Transformer setUpTransformer() throws TransformerConfigurationException {
        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        return trans;
    }
}