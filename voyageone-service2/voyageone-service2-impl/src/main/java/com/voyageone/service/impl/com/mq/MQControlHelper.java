package com.voyageone.service.impl.com.mq;

import com.voyageone.common.spring.SpringContext;
import com.voyageone.service.impl.com.mq.config.VoRabbitMqLocalConfig;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
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

    private static <T> T getApplicationContextBean(Class<T> requiredType) {
        return SpringContext.getBean(requiredType);
    }

    public static void start(String beanName) {
        if (!isRunning(beanName)) {
            SimpleMessageListenerContainer simpleMessageListenerContainer = (SimpleMessageListenerContainer) getApplicationContextBean(RabbitListenerEndpointRegistry.class).getListenerContainer(beanName);
            if (simpleMessageListenerContainer != null) {
                VoRabbitMqLocalConfig voRabbitMqLocalConfig = getApplicationContextBean(VoRabbitMqLocalConfig.class);
                if (voRabbitMqLocalConfig.isLocal()) {
                    getApplicationContextBean(AmqpAdmin.class).declareQueue(new Queue(simpleMessageListenerContainer.getQueueNames()[0], true, false, voRabbitMqLocalConfig.isLocal()));
                }
                simpleMessageListenerContainer.start();
            }
        }
    }

    public static void stop(String beanName) {
        if (isRunning(beanName)) {
            MessageListenerContainer messageListenerContainer = getApplicationContextBean(RabbitListenerEndpointRegistry.class).getListenerContainer(beanName);
            if (messageListenerContainer != null) {
                messageListenerContainer.stop();
            }
        }
    }

    public static boolean isRunning(String beanName) {
        MessageListenerContainer messageListenerContainer = getApplicationContextBean(RabbitListenerEndpointRegistry.class).getListenerContainer(beanName);
        return messageListenerContainer != null && messageListenerContainer.isRunning();
    }

    public static int getConcurrentConsumers(String beanName) {
        int result = 0;
        SimpleMessageListenerContainer simpleMessageListenerContainer = (SimpleMessageListenerContainer) getApplicationContextBean(RabbitListenerEndpointRegistry.class).getListenerContainer(beanName);
        if (simpleMessageListenerContainer != null) {
            result = simpleMessageListenerContainer.getActiveConsumerCount();
        }
        return result;
    }

    public static void setConcurrentConsumers(String beanName, int count) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = (SimpleMessageListenerContainer) getApplicationContextBean(RabbitListenerEndpointRegistry.class).getListenerContainer(beanName);
        if (simpleMessageListenerContainer != null) {
            simpleMessageListenerContainer.setConcurrentConsumers(count);
        }
    }
}
