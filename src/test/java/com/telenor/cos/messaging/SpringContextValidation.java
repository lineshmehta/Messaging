package com.telenor.cos.messaging;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telenor.cos.test.category.EmbeddedTest;

/**
 * @author Eirik Bergande (Capgemini)
 * 
 *         This test takes 35 seconds, if someone wants to improve it, then
 *         please go ahead.
 * 
 */
@Category(EmbeddedTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-embeddedTest.xml")
@DirtiesContext
public class SpringContextValidation {

    @Autowired
    private ApplicationContext context;

    /**
     * Test that context is valid and loadable
     */
    @Test
    public void contextIsLoadable() {
        context.getBean("masterCustomerLogicalDeleteProducer");
    }

}
