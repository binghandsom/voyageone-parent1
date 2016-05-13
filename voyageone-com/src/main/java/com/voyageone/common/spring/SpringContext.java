package com.voyageone.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by DELL on 2016/5/12.
 */
public class SpringContext implements ApplicationContextAware {
    // Spring应用上下文环境
    private static ApplicationContext applicationContext;
    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     *
     * @param applicationContext ApplicationContext
     */
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
        return applicationContext.getBean(cls);
    }
}