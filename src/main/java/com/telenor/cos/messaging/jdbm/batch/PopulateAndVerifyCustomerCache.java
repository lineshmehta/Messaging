package com.telenor.cos.messaging.jdbm.batch;

import com.telenor.cos.messaging.CosMessagingException;
import com.telenor.cos.messaging.jdbm.CachableCustomer;

public class PopulateAndVerifyCustomerCache extends PopulateAndVerifyCache<Long, CachableCustomer> {

    /**
     * setCustomerCacheCommand.
     */
    public PopulateAndVerifyCustomerCache() {
        setCommand(new CustomerCacheCommand());
    }

    private class CustomerCacheCommand extends Command<Long, CachableCustomer> {

        @Override
        public Long parse(String line) {
            CachableCustomer customer = JDBMUtils.createCustomerObject(line);
            if (customer == null){
                return null;
            }

            getMap().put(customer.getCustomerId(), customer);
            return customer.getCustomerId();
        }

        @Override
        public void verify(Long customerId, long lineNumber) {
            if(customerId == null) {
                return;
            }

            CachableCustomer customer = getCache().get(customerId);
            if (customer == null) {
                throw new CosMessagingException("Customer with ID ["+customerId+"] was not inserted in JDBM Cache, but exists in the data dump (csv file)", null);
            }
            validateCustomerId(customer, lineNumber);
        }

        /**
         * Validates the Customer object.
         *
         * @param customer customer object.
         * @param rowNum row number in file.
         */
        private void validateCustomerId(CachableCustomer customer, long rowNum) {
            if (customer.getCustomerId() == null) {
                throw new CosMessagingException(("Row number [" + rowNum + "] has no customer id "), null);
            }
        }
    };
}
