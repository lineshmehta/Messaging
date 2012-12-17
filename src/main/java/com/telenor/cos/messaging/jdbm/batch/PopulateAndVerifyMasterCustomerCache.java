package com.telenor.cos.messaging.jdbm.batch;

import org.apache.commons.lang.StringUtils;

import com.telenor.cos.messaging.CosMessagingException;


public class PopulateAndVerifyMasterCustomerCache extends PopulateAndVerifyCache<Long, Long> {

    /**
     * setMasterCustomerCacheCommand.
     */
    public PopulateAndVerifyMasterCustomerCache() {
        setCommand(new MasterCustomerCacheCommand());
    }

    private class MasterCustomerCacheCommand extends Command<Long, Long> {

        @Override
        Long parse(String masterCustomerData) {
            String[] masterCustomer = StringUtils.splitPreserveAllTokens(masterCustomerData,'|');
            if (masterCustomer[0] != null) {
                getMap().put(Long.valueOf(masterCustomer[0]), Long.valueOf(masterCustomer[1]));
            }
            return masterCustomer[0] != null ? Long.valueOf(masterCustomer[0]) : null;
        }

        @Override
        void verify(Long custUnitNumber, long lineNumber) {
            if(custUnitNumber != null) {
                Long masterCustomerId = getCache().get(custUnitNumber);
                if (masterCustomerId == null) {
                    throw new CosMessagingException("MasterCustomer with CustUnitNumber ["+ custUnitNumber +"] was not inserted in JDBM Cache, but exists in the data dump (csv file)", null);
                }
            } else {
                throw new CosMessagingException(("Row number [" + lineNumber + "] has no master Id "), null);
            }
        }
    }
}
