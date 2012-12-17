package com.telenor.cos.messaging.jdbm.batch;

import org.apache.commons.lang.StringUtils;

import com.telenor.cos.messaging.CosMessagingException;


public class PopulateAndVerifySubscriptionTypeCache extends PopulateAndVerifyCache<String, String> {

    /**
     * setSubscriptionTypeCacheCommand.
     */
    public PopulateAndVerifySubscriptionTypeCache() {
        setCommand(new SubscriptionTypeCacheCommand());
    }

    private class SubscriptionTypeCacheCommand extends Command<String, String> {

        @Override
        String parse(String data) {
            String[] stringArray = new String[2];
            if (data != null) {
                stringArray = StringUtils.splitPreserveAllTokens(data, "|");
                getMap().put(stringArray[0], stringArray[1]);
            }
            return stringArray[0] != null ? stringArray[0]:null;
        }

        @Override
        void verify(String s212ProductId, long lineNumber) {
            String subscriptionType = getCache().get(s212ProductId);
            if (subscriptionType == null) {
                throw new CosMessagingException("SubscriptionType with Type ["+s212ProductId+"] was not inserted in JDBM Cache, but exists in the data dump (csv file)", null);
            }
        }
    }
}