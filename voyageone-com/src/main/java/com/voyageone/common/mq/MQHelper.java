package com.voyageone.common.mq;

import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author aooer 2016/5/5.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component
public class MQHelper {

    private static RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @Autowired
    private void setRabbitListenerEndpointRegistry(RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry) {
        MQHelper.rabbitListenerEndpointRegistry = rabbitListenerEndpointRegistry;
    }

    public static void start(String beanName){
        if(!isRunning(beanName))
        rabbitListenerEndpointRegistry.getListenerContainer(beanName).start();
    }

    public static void stop(String beanName){
        if(isRunning(beanName))
        rabbitListenerEndpointRegistry.getListenerContainer(beanName).stop();
    }

    public static boolean isRunning(String beanName){
        return rabbitListenerEndpointRegistry.getListenerContainer(beanName).isRunning();
    }

}
