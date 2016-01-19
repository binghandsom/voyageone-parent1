package com.voyageone.web2.sdk.api.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Map;

/**
 * SdkBeanUtils
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class SdkBeanUtils {

    public static void copyProperties(Map source, Object target) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(target);
        beanWrapper.setPropertyValues(source);
    }

    public static void beanSetToMap() {

    }
}
