package com.voyageone.service.impl.com.mq;

import com.voyageone.common.spring.SpringContext;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * @author aooer 2016/5/5.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component
public class MQControlHelper {

    public static RabbitListenerEndpointRegistry getRabbitListenerEndpointRegistry() {
        return SpringContext.getBean(RabbitListenerEndpointRegistry.class);
    }

    public static void start(String beanName) {
        if (!isRunning(beanName)) {
            MessageListenerContainer messageListenerContainer = getRabbitListenerEndpointRegistry().getListenerContainer(beanName);
            if (messageListenerContainer != null) {
                messageListenerContainer.start();
            }
        }
    }

    public static void stop(String beanName) {
        if (isRunning(beanName)) {
            MessageListenerContainer messageListenerContainer = getRabbitListenerEndpointRegistry().getListenerContainer(beanName);
            if (messageListenerContainer != null) {
                messageListenerContainer.stop();
            }
        }
    }

    public static boolean isRunning(String beanName) {
        MessageListenerContainer messageListenerContainer = getRabbitListenerEndpointRegistry().getListenerContainer(beanName);
        return messageListenerContainer != null && messageListenerContainer.isRunning();
    }

    public static int getConcurrentConsumers(String beanName) {
        int result = 0;
        SimpleMessageListenerContainer simpleMessageListenerContainer = (SimpleMessageListenerContainer) getRabbitListenerEndpointRegistry().getListenerContainer(beanName);
        if (simpleMessageListenerContainer != null) {
            result = simpleMessageListenerContainer.getActiveConsumerCount();
        }
        return result;
    }

    public static void setConcurrentConsumers(String beanName, int count) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = (SimpleMessageListenerContainer) getRabbitListenerEndpointRegistry().getListenerContainer(beanName);
        if (simpleMessageListenerContainer != null) {
            simpleMessageListenerContainer.setConcurrentConsumers(count);
        }
    }
}
