package com.telenor.cos.messaging.util;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.StringReader;

public class TestHelper {

    public Node fileToDom(String fileName) throws Exception {
        DocumentBuilder builder = createDocumentBuilder();
        return builder.parse(getFullPath(fileName));
    }

    public String fileToString(String fileName) throws Exception {
        return FileUtils.readFileToString(new File(getFullPath(fileName)));
    }

    public Node stringToDom(String xmlSource) throws Exception {
        DocumentBuilder builder = createDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xmlSource)));
    }

    private DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        return factory.newDocumentBuilder();
    }
 
    public  String getFullPath(String shortName) {
        return getClass().getResource('/' + shortName).getPath();
    }
}
