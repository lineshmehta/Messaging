package com.telenor.cos.messaging.producers.xpath;

public class AbstractUserResourceXpathUnitTest extends AbstractXpathUnitTest {
    
    protected static final String NEW_USER_RESOURCE = "<insert schema=\"USER_RESOURCE\">"
        + "<values>"
        +    "<cell name=\"CS_USER_ID\" type=\"VARCHAR\">cosmaster</cell>"
        +    "<cell name=\"RESOURCE_ID\" type=\"NUMERIC\">1</cell>"
        + "</values>"
    + "</insert>";
    
    protected static final String DELETE_USER_RESOURCE = "<delete schema=\"USER_RESOURCE\">"
       + "<oldValues>"
       +    "<cell name=\"CS_USER_ID\" type=\"VARCHAR\">cosmaster</cell>"
       +    "<cell name=\"RESOURCE_ID\" type=\"NUMERIC\">1</cell>"
       + "</oldValues>"
    + "</delete>";
    
    protected static final String UPDATE_RESOURCEID_USER_RESOURCE = "<update schema=\"USER_RESOURCE\">"
            + "<values>"
            +    "<cell name=\"RESOURCE_ID\" type=\"NUMERIC\">2</cell>"
            + "</values>"
            + "<oldValues>"
            +    "<cell name=\"CS_USER_ID\" type=\"VARCHAR\">cosmaster</cell>"
            +    "<cell name=\"RESOURCE_ID\" type=\"NUMERIC\">1</cell>"
            + "</oldValues>"
         + "</update>";
    
    protected static final String UPDATE_CSUSERID_USER_RESOURCE = "<update schema=\"USER_RESOURCE\">"
            + "<values>"
            +    "<cell name=\"CS_USER_ID\" type=\"VARCHAR\">cosmaster1</cell>"
            + "</values>"
            + "<oldValues>"
            +    "<cell name=\"CS_USER_ID\" type=\"VARCHAR\">cosmaster</cell>"
            +    "<cell name=\"RESOURCE_ID\" type=\"NUMERIC\">1</cell>"
            + "</oldValues>"
         + "</update>";
}
