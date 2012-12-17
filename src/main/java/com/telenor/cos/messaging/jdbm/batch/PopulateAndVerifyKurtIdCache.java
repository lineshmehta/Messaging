package com.telenor.cos.messaging.jdbm.batch;

import org.apache.commons.lang.StringUtils;

import com.telenor.cos.messaging.CosMessagingException;

public class PopulateAndVerifyKurtIdCache extends PopulateAndVerifyCache<Long, Long> {

    /**
     * KurtIdCacheCommand.
     */
    public PopulateAndVerifyKurtIdCache() {
        setCommand(new KurtIdCacheCommand());
    }

    private class KurtIdCacheCommand extends Command<Long, Long> {

        @Override
        Long parse(String line) {
            String[] kurtAndMasterCustId = StringUtils.splitPreserveAllTokens(line,'|');
            if (kurtAndMasterCustId[0] != null) {
                getMap().put(Long.valueOf(kurtAndMasterCustId[0]), Long.valueOf(kurtAndMasterCustId[1]));
            }
            return kurtAndMasterCustId[0] != null ? Long.valueOf(kurtAndMasterCustId[0]): null;
        }

        @Override
        void verify(Long kurtId, long lineNumber) {
            if(kurtId != null) {
                Long masterCustomerId = getCache().get(kurtId);
                if (masterCustomerId == null) {
                    throw new CosMessagingException("MasterCustomer with KurtId ["+ kurtId +"] was not inserted in JDBM Cache, but exists in the data dump (csv file)", null);
                }
            } else {
                throw new CosMessagingException(("Row number [" + lineNumber + "] has no master Id "), null);
            }
        }
    }
}
