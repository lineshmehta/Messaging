package com.telenor.cos.messaging.predicates;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.util.TestHelper;

public class IrrelevantMessagePredicateTest {

    private IrrelevantMessagePredicate irrelevantMessagePredicate;

    private TestHelper testHelper;

    @Mock
    private XPath xPathMock;

    @Mock
    private Logger loggerMock;

    @Mock
    private Exchange exchangeMock;

    @Mock
    private Message messageMock;

    private static final String XML_ONLY_EMPTY_CHANGE = "dataset/empty_change.xml";
    private static final String XML_ONLY_INFO_CHG = "dataset/info_change.xml";
    private static final String XML_MIXED_UNINTERESTING = "dataset/uninteresting_change_mixed.xml";
    private static final String XML_MIXED_UNINTERESTING_AND_INTERESTING = "dataset/uninteresting_and_interesting_change.xml";
    private static final String XML_TRAILING_INFO = "dataset/whitespace_update.xml";
    private static final String XML_DELETE = "dataset/delete_event.xml";

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        testHelper = new TestHelper();
        irrelevantMessagePredicate = new IrrelevantMessagePredicate();
        ReflectionTestUtils.setField(irrelevantMessagePredicate, "regex", "INFO_CHG.*");
        ReflectionTestUtils.setField(irrelevantMessagePredicate, "deleteXpath", "//delete");
        ReflectionTestUtils.setField(irrelevantMessagePredicate, "valuesXpath", "//values/cell");
        ReflectionTestUtils.setField(irrelevantMessagePredicate, "oldValuesXpath", "//oldValues/cell");
    }

    @Test
    public void testCatchExceptionAndWtiteToLog() throws Exception {
        ReflectionTestUtils.setField(irrelevantMessagePredicate, "xPath", xPathMock);
        when(xPathMock.compile(anyString())).thenThrow(new XPathExpressionException("Test Exception"));
        ReflectionTestUtils.setField(irrelevantMessagePredicate, "log", loggerMock);
        irrelevantMessagePredicate.matches(exchangeMock);
        verify(loggerMock).error("Problem evaluating the Value nodes in incoming message: Test Exception");
    }

    /**
     * Should be possible to change a value to an empty value
     *
     * @throws Exception
     */
    @Test
    public void testMatchesWithEmptyChangeShouldReturnFalse() throws Exception {
        String xml = new TestHelper().fileToString(XML_ONLY_EMPTY_CHANGE);
        boolean isUninterestingMessage = mockAndTestAndDoStuffAndShit(xml);
        assertFalse(isUninterestingMessage);
    }

    @Test
    public void testMatchesWithOnlyInfoChangeShouldReturnTrue() throws Exception {
        String xml = new TestHelper().fileToString(XML_ONLY_INFO_CHG);
        boolean isUninterestingMessage = mockAndTestAndDoStuffAndShit(xml);
        assertTrue(isUninterestingMessage);
    }

    @Test
    public void testMatchesWithMixedUninterestingNodesShouldReturnFalse() throws Exception {
        String xml = new TestHelper().fileToString(XML_MIXED_UNINTERESTING);
        boolean isUninterestingMessage = mockAndTestAndDoStuffAndShit(xml);
        assertFalse(isUninterestingMessage);
    }

    @Test
    public void testMatchesWithMixedUninterestingNodesAndInterestingNodeShouldReturnFalse() throws Exception {
        String xml = new TestHelper().fileToString(XML_MIXED_UNINTERESTING_AND_INTERESTING);
        boolean isUninterestingMessage = mockAndTestAndDoStuffAndShit(xml);
        assertFalse(isUninterestingMessage);
    }

    @Test
    public void testMatchesWithNodeWithWhiteSpaceAndTrailingInfoShouldReturnFalse() throws Exception {
        String xml = new TestHelper().fileToString(XML_TRAILING_INFO);
        boolean isUninterestingMessage = mockAndTestAndDoStuffAndShit(xml);
        assertFalse(isUninterestingMessage);
    }

    @Test
    public void testDeleteWithOnlyOldValuesShouldReturnFalse() throws Exception {
        String xml = new TestHelper().fileToString(XML_DELETE);
        boolean isUninterestingMessage = mockAndTestAndDoStuffAndShit(xml);
        assertFalse(isUninterestingMessage);
    }

    private boolean mockAndTestAndDoStuffAndShit(String xml) throws Exception {
        Node rootNode = testHelper.stringToDom(xml);
        when(messageMock.getBody(Node.class)).thenReturn(rootNode);
        when(exchangeMock.getIn(Message.class)).thenReturn(messageMock).thenReturn(messageMock);
        return irrelevantMessagePredicate.matches(exchangeMock);
    }
}
