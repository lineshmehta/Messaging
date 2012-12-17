package com.telenor.cos.messaging.web.controller.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CachableResource;
import com.telenor.cos.messaging.jdbm.CustomerCache;
import com.telenor.cos.messaging.jdbm.KurtIdCache;
import com.telenor.cos.messaging.jdbm.MasterCustomerCache;
import com.telenor.cos.messaging.jdbm.ResourceCache;
import com.telenor.cos.messaging.jdbm.SubscriptionTypeCache;
import com.telenor.cos.messaging.jdbm.UserResourceCache;
import com.telenor.cos.messaging.web.form.CacheForm;
import com.telenor.cos.test.suite.UnitTests;

@Category(UnitTests.class)
public class CacheControllerTest {

    private static final String ID = "123";

    private CacheController cacheController;

    private Model model;

    @Mock
    private CustomerCache customerCache;

    @Mock
    private SubscriptionTypeCache subscriptionTypeCache;

    @Mock 
    private MasterCustomerCache masterCustomerCache;

    @Mock
    private KurtIdCache kurtIdCache;

    @Mock
    private ResourceCache resourceCache;

    @Mock
    private UserResourceCache userResourceCache;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        cacheController = new CacheController();
        ReflectionTestUtils.setField(cacheController, "customerCache", customerCache);
        ReflectionTestUtils.setField(cacheController, "subscriptionTypeCache", subscriptionTypeCache);
        ReflectionTestUtils.setField(cacheController, "masterCustomerCache", masterCustomerCache);
        ReflectionTestUtils.setField(cacheController, "kurtIdCache", kurtIdCache);
        ReflectionTestUtils.setField(cacheController, "resourceCache", resourceCache);
        ReflectionTestUtils.setField(cacheController, "userResourceCache", userResourceCache);
    }

    @Test
    public void findForm() {
        cacheController.testCacheForm(model);
        assertTrue("Model did not contain attribute cacheForm",model.containsAttribute("cacheForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:verifyCacheForm'", "redirect:verifyCacheForm",cacheController.testCache());
    }

    @Test
    public void testPostForCustomerCache() {
        CacheForm cacheForm = createCacheForm("customerCache");
        CachableCustomer cachableCustomer = new CachableCustomer(123L);
        when(customerCache.get(any(Long.class))).thenReturn(cachableCustomer);
        cacheController.testCache(cacheForm, model);
        verify(customerCache).get(any(Long.class));
        assertTrue("Model did not contain attribute result",model.containsAttribute("result"));
    }

    @Test
    public void testPostForSubscriptionTypeCache() {
        CacheForm cacheForm = createCacheForm("subscriptionTypeCache");
        when(subscriptionTypeCache.get(any(String.class))).thenReturn(ID);
        cacheController.testCache(cacheForm, model);
        verify(subscriptionTypeCache).get(any(String.class));
        assertTrue("Model did not contain attribute result",model.containsAttribute("result"));
    }

    @Test
    public void testPostForResourceCache() {
        CacheForm cacheForm = createCacheForm("resourceCache");
        CachableResource cachableResource = new CachableResource(Long.valueOf(ID));
        when(resourceCache.get(any(Long.class))).thenReturn(cachableResource);
        cacheController.testCache(cacheForm, model);
        verify(resourceCache).get(any(Long.class));
        assertTrue("Model did not contain attribute result",model.containsAttribute("result"));
    }

    @Test
    public void testPostForUserResourceCache() {
        CacheForm cacheForm = createCacheForm("userResourceCache");
        List<String> csUserIdsList = new ArrayList<String>();
        when(userResourceCache.get(any(Long.class))).thenReturn(csUserIdsList);
        cacheController.testCache(cacheForm, model);
        verify(userResourceCache).get(any(Long.class));
        assertTrue("Model did not contain attribute result",model.containsAttribute("result"));
    }

    @Test
    public void testPostForMasterCustomerCache() {
        CacheForm cacheForm = createCacheForm("masterCustomerCache");
        when(masterCustomerCache.get(any(Long.class))).thenReturn(Long.valueOf(ID));
        cacheController.testCache(cacheForm, model);
        verify(masterCustomerCache).get(any(Long.class));
        assertTrue("Model did not contain attribute result",model.containsAttribute("result"));
    }

    @Test
    public void testPostKurtIdCache() {
        CacheForm cacheForm = createCacheForm("kurtIdCache");
        when(kurtIdCache.get(any(Long.class))).thenReturn(Long.valueOf(ID));
        cacheController.testCache(cacheForm, model);
        verify(kurtIdCache).get(any(Long.class));
        assertTrue("Model did not contain attribute result",model.containsAttribute("result"));
    }
    private CacheForm createCacheForm(String cacheType) {
        CacheForm cacheForm = new CacheForm();
        cacheForm.setId(ID);
        cacheForm.setCacheType(cacheType);
        return cacheForm;
    }
}
