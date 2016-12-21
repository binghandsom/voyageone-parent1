package com.voyageone.common.util;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * Created by dell on 2016/12/21.
 */
public class GenericSuperclassUtils {

    public static   <T> Class<T> getGenericActualTypeClass(Object bean) {
        Class<T> aClass = (Class<T>) ((ParameterizedTypeImpl) bean.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return aClass;
    }

    public static  <T> Class<T> getGenericActualTypeClass(Object bean, int index) {
        Class<T> aClass = (Class<T>) ((ParameterizedTypeImpl) bean.getClass().getGenericSuperclass()).getActualTypeArguments()[index];
        return aClass;
    }
}
