package com.voyageone.components.rabbitmq.utils;

import com.voyageone.common.spring.SpringContext;
import com.voyageone.components.rabbitmq.annotation.VOMQRunnable;
import com.voyageone.components.rabbitmq.annotation.VOMQStart;
import com.voyageone.components.rabbitmq.annotation.VOMQStop;
import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.components.rabbitmq.namesub.IMQSubBeanName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * MQConfigInit 专用配置访问类
 *
 * @author chuanyu.liang 2016/4/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class MQConfigInit {

    private final static Logger logger = LoggerFactory.getLogger(MQConfigInit.class);

    /**
     * init
     */
    public static void init() {
        initRabbitListener();
        initVOMultRabbitListener();
    }

    private static void initRabbitListener() {
        Map<String, Object> beansWithAnnotationMap = SpringContext.getBeansWithAnnotationMap(RabbitListener.class);
        initMqListener(beansWithAnnotationMap);
    }

    private static void initVOMultRabbitListener() {
        Map<String, Object> beansWithAnnotationMap = SpringContext.getBeansWithAnnotationMap(VOSubRabbitListener.class);
        initMqListener(beansWithAnnotationMap);
    }

    private static void initMqListener(Map<String, Object> beansWithAnnotationMap) {
        if (beansWithAnnotationMap != null) {
            for (Map.Entry<String, Object> entry : beansWithAnnotationMap.entrySet()) {
                Object bean = entry.getValue();
                if (checkMQRunnable(bean)) {
                    startMQ(bean);
                    if (bean instanceof IMQSubBeanName) {
                        logger.info(String.format("%s %s is start", bean.getClass().getSimpleName(), ((IMQSubBeanName)bean).getSubBeanName()));
                    } else {
                        logger.info(String.format("%s is start", bean.getClass().getSimpleName()));
                    }

                } else {
                    stopMQ(bean);
                }
            }
        }
    }

    /**
     * checkStartMq
     */
    public static boolean checkStartMq(Object bean) {
        return checkMQRunnable(bean) && startMQ(bean);
    }

    /**
     * checkMQRunnable
     */
    private static boolean checkMQRunnable(Object bean) {
        return runMethod(bean, VOMQRunnable.class);
    }

    /**
     * startMQ
     */
    private static boolean startMQ(Object bean) {
        return runMethod(bean, VOMQStart.class);
    }

    /**
     * stopMQ
     */
    public static boolean stopMQ(Object bean) {
        return runMethod(bean, VOMQStop.class);
    }

    /**
     * checkMQRunnable
     */
    private static boolean runMethod(Object bean, Class annotationType) {
        final boolean[] isVOMqAnnotation = {true};
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        ReflectionUtils.doWithMethods(targetClass, new ReflectionUtils.MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                Object annotationMethod = AnnotationUtils.findAnnotation(method, annotationType);
                if (annotationMethod != null) {
                    try {
                        isVOMqAnnotation[0] = (boolean) method.invoke(bean);
                    } catch (InvocationTargetException ignored) {
                    }
                }
            }
        });
        return isVOMqAnnotation[0];
    }

}