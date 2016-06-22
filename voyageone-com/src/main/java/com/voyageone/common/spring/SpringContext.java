package com.voyageone.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * SpringContext
 *
 * @author chuanyu.liang 2016/5/5.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component()
@Lazy(value = false)
public class SpringContext implements ApplicationContextAware {
    // Spring应用上下文环境
    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     *
     * @param applicationContext ApplicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContext.applicationContext = applicationContext;
    }

    /**
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取对象
     *
     * @param name String
     * @return Object
     * @throws BeansException
     */
    public static Object getBean(String name) throws BeansException {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(name);
    }

    /**
     * 获取对象
     *
     * @param cls Class
     * @return Object
     * @throws BeansException
     */
    public static <T> T getBean(Class<T> cls) throws BeansException {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(cls);
    }

    /**
     * 获取对象
     *
     * @param type Class
     * @param name String
     * @param <T>  Class
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type, String name) {
        if (applicationContext == null) {
            return null;
        }
        if (applicationContext.containsBean(name)) {
            Object bean = applicationContext.getBean(name);
            if (type.isInstance(bean)) {
                return (T) bean;
            }
        }
        return null;
    }

    /**
     * 获取对象 Map
     */
    public static Map<String, Object> getBeansWithAnnotationMap(Class<? extends Annotation> cls) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBeansWithAnnotation(cls);
    }
}