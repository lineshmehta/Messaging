package com.telenor.cos.messaging.acceptancetest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telenor.cos.graph.service.AccountDto;
import com.telenor.cos.graph.service.CustomerDto;
import com.telenor.cos.graph.service.MasterCustomerDto;
import com.telenor.cos.graph.service.ResourceReadServiceImpl;
import com.telenor.cos.graph.service.ResourceUpdateServiceImpl;
import com.telenor.cos.graph.service.SubscriptionDto;
import com.telenor.cos.graph.service.UserMappingDto;
import com.telenor.cos.graph.service.UserResourceAccessDto;
import com.telenor.cos.messaging.util.TestHelper;
import com.telenor.cos.stub.service.MessageTestProducerEndPointWS;
import com.telenor.cosread.service.search.SortOrder;
import com.telenor.cosread.service.search.SubscriptionClauseDto;
import com.telenor.cosread.service.search.SubscriptionSearchFields;
import com.telenor.cosread.service.search.SubscriptionSearchResultDto;
import com.telenor.cosread.service.search.SubscriptionSearchServiceEndpoint;
import com.telenor.cosread.service.search.SubscriptionSortFields;
import com.telenor.cosread.service.search.SubscriptionViewFilterDto;
import com.telenor.cosread.service.search.UserDto;
import com.telenor.cosread.service.search.ViewFilterPreferencesDto;
import com.telenor.cosread.service.update.UpdateServiceEndpoint;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/webservices.xml")
public class CommonSubscriptionTest {
    
    @Autowired
    private ApplicationContext applicationContext;
    
    private MessageTestProducerEndPointWS testMessageProducer;
    
    private SubscriptionSearchServiceEndpoint subscriptionSearchService;
    
    private ResourceReadServiceImpl graphResourceReadServiceMaster;
    
    private ResourceReadServiceImpl graphResourceReadServiceSlave;
    
    private UpdateServiceEndpoint cosReadUpdateService;
    
    private ResourceUpdateServiceImpl resourceUpdateServiceMaster;
    
    private ResourceUpdateServiceImpl resourceUpdateServiceSlave;
    
    private static final String END_POINT_URI = "jms:queue:incoming";
    
    private static final String masterCustomerParentId = "999999001";
    
    private static final String masterCustomerId = "999999003";
    
    private static final String customerId = "999999004";
    
    private static final String accountId = "999999005";
    
    private static final String tnuId = "ScroogeMcDuck";
    
    private static final String csUserId = "ScroogeMcDuck_cs";
    
    private TestHelper testHelper = new TestHelper();
    
    public void setUp() throws Exception {
        testMessageProducer = getRepServerEndpoint();
        subscriptionSearchService = getSearchService();
        graphResourceReadServiceMaster =  getResourceReadServiceMaster();
        graphResourceReadServiceSlave = getResourceReadServiceSlave();
        cosReadUpdateService = getCosReadUpdateServiceMaster();
        resourceUpdateServiceMaster=getResourceUpdateServiceMaster();
        resourceUpdateServiceSlave=getResourceUpdateServiceSlave();
        insertTestDatainGraphMaster();
        insertTestDatainGraphSlave();
    }
    
    public String prepareInputXML(String fileName) throws Exception {
        return testHelper.fileToString(fileName);
    }
    
    public void sendMsgsToRepServer(String newSubscriptionInputXML) {
        List<Object> inputXML = new ArrayList<Object>();
        inputXML.add(newSubscriptionInputXML);
        testMessageProducer.sendMessages(END_POINT_URI, inputXML);
    }
    public SubscriptionSearchResultDto searchInCosRead(UserDto userdto,SubscriptionClauseDto clauseDto) {
        return subscriptionSearchService.findSubscriptions(userdto, clauseDto, createViewFilterDto());
    }
    
    public SubscriptionDto searchInCosGraphMaster(String subscriptionId) {
        return graphResourceReadServiceMaster.readSubscription(subscriptionId);
    }
    
    public SubscriptionDto searchInCosGraphSlave(String subscriptionId) {
        return graphResourceReadServiceSlave.readSubscription(subscriptionId);
    }
    
    public void deleteSubscrDataInCosRead(String subscriptionId) {
        List<String> subscriptionIds=new ArrayList<String>();
        subscriptionIds.add(subscriptionId);
        cosReadUpdateService.deleteSubscriptionsById(subscriptionIds);
    }
    
    public UserDto createUserDto(int applicationId,String userId) {
        UserDto userDto= new UserDto();
        userDto.setApplicationId(applicationId);
        userDto.setUserId(userId);
        return userDto;
    }
    
    public SubscriptionClauseDto createClauseDto(String valueToSearchFor) {
        SubscriptionClauseDto clauseDto = new SubscriptionClauseDto();
        clauseDto.setValueToSearchFor(valueToSearchFor);
        
        Collection<SubscriptionSearchFields> fields = new ArrayList<SubscriptionSearchFields>();
        fields.add(SubscriptionSearchFields.SUBSCRIPTION_ID);
        clauseDto.getFields().addAll(fields);
        return clauseDto;
    }
    public SubscriptionViewFilterDto createViewFilterDto() {
        SubscriptionViewFilterDto viewFileterDto = new SubscriptionViewFilterDto();
        ViewFilterPreferencesDto viewFilterPreferencesDto = new ViewFilterPreferencesDto();
        
        viewFilterPreferencesDto.setFromPage(0);
        viewFilterPreferencesDto.setPageSize(10);
        viewFileterDto.setSortField(SubscriptionSortFields.SUBSCRIPTION_ID);
        viewFilterPreferencesDto.setSortOrder(SortOrder.ASCENDING);
        return viewFileterDto;
    }
    private MessageTestProducerEndPointWS getRepServerEndpoint() {
        return applicationContext.getBean("repServer", MessageTestProducerEndPointWS.class);
    }
    
    private SubscriptionSearchServiceEndpoint getSearchService() {
        return applicationContext.getBean("subscriptionSearchService", SubscriptionSearchServiceEndpoint.class);
    }
    
    private ResourceReadServiceImpl getResourceReadServiceMaster() {
        return applicationContext.getBean("graphResourceReadServiceMaster", ResourceReadServiceImpl.class);
    }
    
    private ResourceReadServiceImpl getResourceReadServiceSlave() {
        return applicationContext.getBean("graphResourceReadServiceSlave", ResourceReadServiceImpl.class);
    }
    
    private ResourceUpdateServiceImpl getResourceUpdateServiceMaster() {
        return applicationContext.getBean("resourceUpdateServiceMaster", ResourceUpdateServiceImpl.class);
    }
    
    private ResourceUpdateServiceImpl getResourceUpdateServiceSlave() {
        return applicationContext.getBean("resourceUpdateServiceSlave", ResourceUpdateServiceImpl.class);
    }
    
    private UpdateServiceEndpoint getCosReadUpdateServiceMaster() {
        return applicationContext.getBean("cosReadUpdateService", UpdateServiceEndpoint.class);
    }
    private void insertTestDatainGraphMaster() throws Exception {
        resourceUpdateServiceMaster.updateMasterCustomer(createMasterCustomerDto());
        resourceUpdateServiceMaster.updateCustomer(createCustomerDto());
        resourceUpdateServiceMaster.updateAccount(createAccountDto());
        resourceUpdateServiceMaster.updateUserMapping(createUserMappingDto());
        resourceUpdateServiceMaster.updateUserResource(createUserResourceAccessDto());
    }
    private void insertTestDatainGraphSlave() throws Exception {
        resourceUpdateServiceSlave.updateMasterCustomer(createMasterCustomerDto());
        resourceUpdateServiceSlave.updateCustomer(createCustomerDto());
        resourceUpdateServiceSlave.updateAccount(createAccountDto());
        resourceUpdateServiceSlave.updateUserMapping(createUserMappingDto());
        resourceUpdateServiceSlave.updateUserResource(createUserResourceAccessDto());
    }
    private MasterCustomerDto createMasterCustomerDto() {
        MasterCustomerDto masterCustomerDto = new MasterCustomerDto();
        masterCustomerDto.setId(masterCustomerId);
        masterCustomerDto.setParentId(masterCustomerParentId);
        return masterCustomerDto;
    }
    private CustomerDto createCustomerDto() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customerId);
        customerDto.setParentId(masterCustomerId);
        return customerDto;
    }
    private AccountDto createAccountDto() {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(accountId);
        accountDto.setOwnerCustomerId(customerId);
        accountDto.setPayerCustomerId(customerId);
        return accountDto;
    }
    private UserMappingDto createUserMappingDto() {
        UserMappingDto userMappingDto = new UserMappingDto();
        userMappingDto.setApplicationId(62);
        userMappingDto.setTelenorUserId(tnuId);
        userMappingDto.setId(csUserId);
        return userMappingDto;
    }
    private UserResourceAccessDto createUserResourceAccessDto() {
        UserResourceAccessDto userResourceAccessDto = new UserResourceAccessDto();
        userResourceAccessDto.setInheritContent(true);
        userResourceAccessDto.setInheritStructure(true);
        userResourceAccessDto.setResourceId(masterCustomerParentId);
        userResourceAccessDto.setUserId(csUserId);
        userResourceAccessDto.setResourceType("Master");
        return userResourceAccessDto;
    }
}
