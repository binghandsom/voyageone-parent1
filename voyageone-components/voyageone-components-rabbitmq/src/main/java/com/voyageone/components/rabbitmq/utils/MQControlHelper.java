package com.voyageone.components.rabbitmq.utils;

import com.voyageone.common.spring.SpringContext;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.rabbitmq.config.VoRabbitMqLocalConfig;
import com.voyageone.components.rabbitmq.handler.VOExceptionStrategy;
import org.apache.commons.collections.MapUtils;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 消息队列 控制帮助类
 *
 * @author aooer 2016/5/5.
 * @version 2.0.0
 * @since 2.0.0
 */
public class MQControlHelper {

    private static <T> T getApplicationContextBean(Class<T> requiredType) {
        return SpringContext.getBean(requiredType);
    }

    public static void start(String endpointId) {
        if (!isRunning(endpointId)) {
            SimpleMessageListenerContainer simpleMessageListenerContainer = (SimpleMessageListenerContainer) getApplicationContextBean(RabbitListenerEndpointRegistry.class).getListenerContainer(endpointId);
            if (simpleMessageListenerContainer != null) {
                VoRabbitMqLocalConfig voRabbitMqLocalConfig = getApplicationContextBean(VoRabbitMqLocalConfig.class);
                if (voRabbitMqLocalConfig.isLocal()) {
                    getApplicationContextBean(AmqpAdmin.class).declareQueue(new Queue(simpleMessageListenerContainer.getQueueNames()[0], true, false, voRabbitMqLocalConfig.isLocal()));
                }
                simpleMessageListenerContainer.start();
            }
        }
    }

    public static void stop(String endpointId) {
        if (isRunning(endpointId)) {
            MessageListenerContainer messageListenerContainer = getApplicationContextBean(RabbitListenerEndpointRegistry.class).getListenerContainer(endpointId);
            if (messageListenerContainer != null) {
                messageListenerContainer.stop();
            }
        }
    }

    public static boolean isRunning(String endpointId) {
        MessageListenerContainer messageListenerContainer = getApplicationContextBean(RabbitListenerEndpointRegistry.class).getListenerContainer(endpointId);
        return messageListenerContainer != null && messageListenerContainer.isRunning();
    }

    public static int getConcurrentConsumers(String endpointId) {
        int result = 0;
        SimpleMessageListenerContainer simpleMessageListenerContainer = (SimpleMessageListenerContainer) getApplicationContextBean(RabbitListenerEndpointRegistry.class).getListenerContainer(endpointId);
        if (simpleMessageListenerContainer != null) {
            result = simpleMessageListenerContainer.getActiveConsumerCount();
        }
        return result;
    }

    public static void setConcurrentConsumers(String endpointId, int count) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = (SimpleMessageListenerContainer) getApplicationContextBean(RabbitListenerEndpointRegistry.class).getListenerContainer(endpointId);
        if (simpleMessageListenerContainer != null) {
            simpleMessageListenerContainer.setConcurrentConsumers(count);
        }
    }

    /**
     * 是否超过重试次数的判断
     */
    public static boolean isOutRetryTimes(Message message) {
        Map<String, Object> msgMap;
        try {
            msgMap = JacksonUtil.jsonToMap(new String(message.getBody(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return isOutRetryTimes(msgMap);
    }

    /**
     * 是否超过重试次数的判断
     */
    public static boolean isOutRetryTimes(Map<String, Object> msgBodyMap) {
        // RETRY>3 return
        return !MapUtils.isEmpty(msgBodyMap) && //headers非空
                !StringUtils.isEmpty(msgBodyMap.get(VOExceptionStrategy.CONSUMER_RETRY_KEY)) && //CONSUMER_RETRY_KEY非空
                (int) msgBodyMap.get(VOExceptionStrategy.CONSUMER_RETRY_KEY) >= VOExceptionStrategy.MAX_RETRY_TIMES;
    }
}
