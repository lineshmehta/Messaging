package com.telenor.cos.messaging.routers.aggregation;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.CosMessagingException;


/**
 * Simple bean merger
 */
@Component
public class BeanMerger  {

    /**
     * Set properties/values that are not null from the newValues object into the target object.
     * 
     * @param <T> the type  
     * @param target the object to merge new values into
     * @param newValues the object the get the new values from
     * @return the merged object
     */
    public <T> T mergeNotNullValues(T target, T newValues) {
        BeanInfo beanInfo = getBeanInfo(target);
        return mergeNotNullValues(target, newValues, beanInfo);
    }
    
    private <T> BeanInfo getBeanInfo(T target) {
        try {
             return Introspector.getBeanInfo(target.getClass());
        } catch (IntrospectionException e) {
           throw new CosMessagingException("Unexpected error when trying to get bean info for: " + target, e);
        }
    }

    private <T> T mergeNotNullValues(T target, T newValues, BeanInfo beanInfo) {
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
            if (descriptor.getWriteMethod() != null) {
                try {
                    Object value = descriptor.getReadMethod().invoke(newValues);
                    if (value != null) {
                        descriptor.getWriteMethod().invoke(target, value);
                    }
                } catch (IllegalArgumentException e) {
                    throw new CosMessagingException("Unexpected error when trying to merge the beans: " + target + " and :" + newValues, e);
                } catch (IllegalAccessException e) {
                    throw new CosMessagingException("Unexpected error when trying to merge the beans: " + target + " and :" + newValues, e);
                } catch (InvocationTargetException e) {
                    throw new CosMessagingException("Unexpected error when trying to merge the beans: " + target + " and :" + newValues, e);
                }

            }
        }
        
        return target;
    }

}
