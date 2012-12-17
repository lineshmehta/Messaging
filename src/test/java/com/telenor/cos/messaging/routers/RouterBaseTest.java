package com.telenor.cos.messaging.routers;

import org.apache.camel.test.junit4.CamelSpringTestSupport;

/**
 * Class that all router tests should extend
 */
public abstract class RouterBaseTest extends CamelSpringTestSupport {

    @Override
    public String isMockEndpoints() {
        return "*"; // Mocks all end points
    }

    /**
     *  we override this method and return true, to tell Camel test-kit that
     *  it should only create CamelContext once (per class), so we will
     *  re-use the CamelContext between each test method in this class
     */
    @Override
    public boolean isCreateCamelContextPerClass() {
        return true;
    }

}