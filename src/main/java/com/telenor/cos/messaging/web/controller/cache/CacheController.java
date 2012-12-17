package com.telenor.cos.messaging.web.controller.cache;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CachableResource;
import com.telenor.cos.messaging.jdbm.CustomerCache;
import com.telenor.cos.messaging.jdbm.KurtIdCache;
import com.telenor.cos.messaging.jdbm.MasterCustomerCache;
import com.telenor.cos.messaging.jdbm.ResourceCache;
import com.telenor.cos.messaging.jdbm.SubscriptionTypeCache;
import com.telenor.cos.messaging.jdbm.UserResourceCache;
import com.telenor.cos.messaging.web.form.CacheForm;

@Controller
public class CacheController {

    private static final Logger LOG = LoggerFactory.getLogger(CacheController.class);

    @Autowired
    private CustomerCache customerCache;

    @Autowired 
    private SubscriptionTypeCache subscriptionTypeCache;

    @Autowired
    private ResourceCache resourceCache;

    @Autowired
    private UserResourceCache userResourceCache;

    @Autowired
    private MasterCustomerCache masterCustomerCache;

    @Autowired
    private KurtIdCache kurtIdCache;


    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/verifyCache", method = RequestMethod.GET)
    public String testCache() {
        LOG.info("called testCache() for GET");
        return "redirect:verifyCacheForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/verifyCacheForm", method = RequestMethod.GET)
    public void testCacheForm(Model model) {
        LOG.info("called testCacheForm()");
        CacheForm cacheForm = new CacheForm();
        model.addAttribute(cacheForm);
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param cacheForm form
     * @param model model
     */
    @RequestMapping(value = "/verifyCache", method = RequestMethod.POST)
    public void testCache(@ModelAttribute CacheForm cacheForm, Model model) {
        LOG.info("called testCache()");
        findCacheDataAndAddToModel(model, cacheForm.getId(), cacheForm.getCacheType());
    }

    /**
     * @param model
     * @param id
     * @param cacheType
     */
    private void findCacheDataAndAddToModel(Model model, String id,String cacheType) {

        if("customerCache".equals(cacheType)) {
            CachableCustomer cachableCustomer = customerCache.get(Long.valueOf(id));
            model.addAttribute("result", cachableCustomer);
        } else if ("subscriptionTypeCache".equals(cacheType)) {
            String productId = subscriptionTypeCache.get(id);
            model.addAttribute("result", "ProductId In Cache For S212ProductId Entered is ["+ productId +"]");
        } else if ("resourceCache".equals(cacheType)) {
            CachableResource cachableResource = resourceCache.get(Long.valueOf(id));
            model.addAttribute("result", cachableResource);
        } else if ("userResourceCache".equals(cacheType)) {
            List<String> csUserIdsListForResourceId = userResourceCache.get(Long.valueOf(id));
            model.addAttribute("result", "List of csUserIds using ResourceId are"+csUserIdsListForResourceId);
        } else if ("masterCustomerCache".equals(cacheType)) {
            Long masterId = masterCustomerCache.get(Long.valueOf(id));
            model.addAttribute("result", "masterCustomerId In Cache For CustUnitNumber Entered Is ["+ masterId +"]");
        } else if("kurtIdCache".equals(cacheType)) {
            Long masterCustId = kurtIdCache.get(Long.valueOf(id));
            model.addAttribute("result", "masterCustomerId In Cache For KurtId Entered Is ["+ masterCustId +"]");
        }
    }
}
