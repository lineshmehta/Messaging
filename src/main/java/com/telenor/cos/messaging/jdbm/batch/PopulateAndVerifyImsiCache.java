package com.telenor.cos.messaging.jdbm.batch;

import org.apache.commons.lang.StringUtils;

import com.telenor.cos.messaging.CosMessagingException;

public class PopulateAndVerifyImsiCache extends PopulateAndVerifyCache<Long, String> {

    /**
     * Constructor
     */
    public PopulateAndVerifyImsiCache() {
        setCommand(new ImsiCacheCommand());
    }

    private class ImsiCacheCommand extends Command<Long, String> {

        @Override
        public Long parse(String line) {
            String[] subsIdAndImsi = StringUtils.splitPreserveAllTokens(line,'|');
            if (subsIdAndImsi == null){
                return null;
            }

            Long subscriptionId = Long.valueOf(subsIdAndImsi[0]);
            getMap().put(subscriptionId, subsIdAndImsi[1]);
            return subscriptionId;
        }

        @Override
        public void verify(Long subscriptionId, long lineNumber) {
            if (subscriptionId == null) {
                throw new CosMessagingException("No subscriptionId for line: " + lineNumber, null);
            }

            String imsi = getCache().get(subscriptionId);
            if (imsi == null) {
                throw new CosMessagingException("Imsi with ID [" + subscriptionId +  "] was not inserted in JDBM Cache, but exists in the data dump (csv file)", null);
            }
        }
    };

}
