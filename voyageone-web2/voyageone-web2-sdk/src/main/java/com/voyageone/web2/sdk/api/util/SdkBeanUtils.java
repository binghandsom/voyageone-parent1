package com.voyageone.web2.sdk.api.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Map;

/**
 * SdkBeanUtils
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since 2.0.0
 */
public class SdkBeanUtils {

    /**
     * copyProperties
     * @param source map
     * @param target bean
     */
    public static void copyProperties(Map<String, Object> source, Object target) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(target);
        beanWrapper.setPropertyValues(source);
    }

    /**
     * copyProperties
     * @param source bean
     * @param target map
     */
    public static void copyProperties(Object source, Map<String, Object> target) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] descriptor = beanWrapper.getPropertyDescriptors();
        for (PropertyDescriptor aDescriptor : descriptor) {
            String name = aDescriptor.getName();
            target.put(name, beanWrapper.getPropertyValue(name));
        }
    }
}
