package com.telenor.cos.messaging.jdbm.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CachableResource;
import com.telenor.cos.messaging.util.MessageFormattingUtil;

/**
 * @author Babaprakash D
 *
 */
public final class JDBMUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBMUtils.class);

    public static final String CUSTOMER_DETAILS_CSV_FILENAME = "customer_details_view.csv";
    public static final String SUBSCRIPTION_IMSI_CSV_FILENAME = "subscription_equipment_view.csv";
    public static final String MASTER_CUSTOMER_DETAILS_CSV_FILENAME = "master_customer_details_view.csv";
    public static final String SUBSCRIPTION_TYPE_CSV_FILENAME = "subscription_product_view.csv";
    public static final String RESOURCE_DETAILS_CSV_FILENAME = "resource_details_view.csv";
    public static final String USERRESOURCE_DETAILS_CSV_FILENAME = "user_resource_details_view.csv";
    public static final String MASTER_CUSTOMER_KURT_DETAILS_CSV_FILENAME = "master_customer_kurt_details_view.csv";

    public static final String CHAR_SET = "UTF-8";

    private JDBMUtils(){

    }

    /**
     * @param csvFileAbsolutePathPath CSV file absolute path.
     * @return BufferedReader creating reader.
     * @throws IOException ioException will be thrown.
     */
    public static BufferedReader createBufferedReader(String csvFileAbsolutePathPath) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(csvFileAbsolutePathPath)), JDBMUtils.CHAR_SET));
        return bufferedReader;
    }

    /**
     * @param customerDetails customerData
     * @return Customer
     */
    public static CachableCustomer createCustomerObject(String customerDetails) {
        String[] customerData=StringUtils.splitPreserveAllTokens(customerDetails,"|");
        if (customerData.length < 12){
            LOGGER.error("Found Customer Data [" + customerDetails.toString() + "] with Incomplete Field Values. Expected [12]. Got [" + customerData.length + "]");
            LOGGER.info("Ignoring this record and continuing...");
            return null;
        }
        CachableCustomer customer = new CachableCustomer(Long.valueOf(removeQuotesIfrequired(customerData[0])));
        String masterCustomerId = removeQuotesIfrequired(customerData[1]);
        if(!StringUtils.isBlank(masterCustomerId)) {
            customer.setMasterCustomerId(Long.valueOf(masterCustomerId));
        }else {
            customer.setMasterCustomerId(null);
        }
        customer.setFirstName(removeQuotesIfrequired(customerData[2]));
        customer.setMiddleName(removeQuotesIfrequired(customerData[3]));
        customer.setLastName(removeQuotesIfrequired(customerData[4]));
        String customerUnitNumber=removeQuotesIfrequired(customerData[5]);
        if(StringUtils.isNotBlank(customerUnitNumber)) {
            customer.setCustUnitNumber(Long.valueOf(customerUnitNumber));
        }else {
            customer.setCustUnitNumber(null);
        }
        String custPostCodeIdMain =removeQuotesIfrequired(customerData[6]);
        if(StringUtils.isNotBlank(custPostCodeIdMain)) {
            String formattedPostCodeIdMain = MessageFormattingUtil.getFormattedPostCodeIdMain(Long.valueOf(custPostCodeIdMain));
            customer.setPostcodeIdMain(formattedPostCodeIdMain);
        }else {
            customer.setPostcodeIdMain(null);
        }
        customer.setPostcodeNameMain(removeQuotesIfrequired(customerData[7]));
        customer.setAddressLineMain(removeQuotesIfrequired(customerData[8]));
        customer.setAddressCOName(removeQuotesIfrequired(customerData[9]));
        customer.setAddressStreetName(removeQuotesIfrequired(customerData[10]));
        customer.setAddressStreetNumber(removeQuotesIfrequired(customerData[11]));
        return customer;
    }

    /**
     * Creates CachableResource Object.
     * @param resourceDetails resourceData
     * @return Resource
     */
    public static CachableResource createResourceObject(String resourceDetails) {
        String[] resourceData = StringUtils.splitPreserveAllTokens(resourceDetails,"|");
        if (resourceData.length < 5){
            LOGGER.error("Found Resource Data [" + resourceDetails.toString()+ "] with Incomplete Field Values. Expected [5]. Got [" + resourceData.length + "]");
            LOGGER.info("Ignoring this record and continuing...");
            return null;
        }
        CachableResource resource = new CachableResource(Long.valueOf(removeQuotesIfrequired(resourceData[0])));
        String resourceTypeId = removeQuotesIfrequired(resourceData[1]);
        String resourceTypeIdKey = removeQuotesIfrequired(resourceData[2]);
        String resourceHasContentInherit = removeQuotesIfrequired(resourceData[3]);
        String resourceHasStructureInherit = removeQuotesIfrequired(resourceData[4]);

        if(!StringUtils.isBlank(resourceTypeId)) {
            resource.setResourceTypeId(Integer.valueOf(resourceTypeId));
        }else {
            resource.setResourceTypeId(null);
        }
        if(!StringUtils.isBlank(resourceTypeIdKey)) {
            resource.setResourceTypeIdKey(resourceTypeIdKey);
        }else {
            resource.setResourceTypeIdKey(null);
        }
        if(!StringUtils.isBlank(resourceHasContentInherit)) {
            resource.setResourceHasContentInherit(resourceHasContentInherit);
        }else {
            resource.setResourceHasContentInherit(null);
        }
        if(!StringUtils.isBlank(resourceHasStructureInherit)) {
            resource.setResourceHasStructureInherit(resourceHasStructureInherit);
        }else {
            resource.setResourceHasStructureInherit(null);
        }
        return resource;
    }

    private static String removeQuotesIfrequired(String data) {
        return StringUtils.remove(data, "\"");
    }
}