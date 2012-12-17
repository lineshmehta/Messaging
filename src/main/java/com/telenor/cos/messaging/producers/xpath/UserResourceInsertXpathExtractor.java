package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;

/**
 * Implementation class for {@link UserResourceXpathExtractor}
 *
 * @author Babaprakash D
 */
@Component
public class UserResourceInsertXpathExtractor extends UserResourceXpathExtractor {

    private static final String CS_USER_ID_EXPR = "//insert[@schema='USER_RESOURCE']//values//cell[@name='CS_USER_ID']";
    private static final String RESOURCE_ID_EXPR = "//insert[@schema='USER_RESOURCE']//values//cell[@name='RESOURCE_ID']";

    @Override
    public String getCsUserIdExpr() {
        return CS_USER_ID_EXPR;
    }

    @Override
    public String getResourceIdExpr() {
        return RESOURCE_ID_EXPR;
    }
}
