package com.voyageone.common.util;

import org.springframework.core.annotation.AnnotationUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * Created by dell on 2016/12/21.
 */
public class GenericSuperclassUtils {

    public static <T> Class<T> getGenericActualTypeClass(Object bean) {
        return getGenericActualTypeClass(bean,0);
    }

    public static <T> Class<T> getGenericActualTypeClass(Object bean, int index) {
        Class<T> aClass = (Class<T>) ((ParameterizedTypeImpl) bean.getClass().getGenericSuperclass()).getActualTypeArguments()[index];
        return aClass;
    }

}
