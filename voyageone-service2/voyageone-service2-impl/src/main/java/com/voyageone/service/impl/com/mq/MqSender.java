package com.voyageone.service.impl.com.mq;

import com.voyageone.components.rabbitmq.utils.MQConfigUtils;
import com.voyageone.common.spring.SpringContext;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.rabbitmq.service.MqBackupMessageService;
import com.voyageone.service.impl.BaseService;
import com.voyageone.components.rabbitmq.config.VoRabbitMqLocalConfig;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author aooer 2016/2/29.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class MqSender extends BaseService {

    private static final String CONSUMER_RETRY_KEY = "$consumer_retry_times$";

    @Autowired
    private MqBackupMessageService mqBackMessageService;

    /**
     * 发送消息
     *
     * @param routingKey 消息KEY
     * @param messageMap 消息内容
     */
    public void sendMessage(String routingKey, Map<String, Object> messageMap) {
        sendMessage(routingKey, messageMap, true, 0);
    }

    public void sendMessage(String routingKey, Map<String, Object> messageMap, long delaySecond) {
        sendMessage(routingKey, messageMap, true, delaySecond);
    }

    /**
     * 发送消息
     *
     * @param routingKey    消息KEY
     * @param messageMap    消息内容
     * @param isBackMessage 出错时是否把消息保存在数据库中，以后会自动发送到消息中 [true: try catch; false:throw exception]
     * @param delaySecond    延迟发送时间 秒
     */
    public void sendMessage(String routingKey, Map<String, Object> messageMap, boolean isBackMessage, long delaySecond) {
        sendMessage(null, routingKey, messageMap, isBackMessage, isLocal(), true, delaySecond);
    }


    public void sendMessage(String exchange, String routingKey, Map<String, Object> messageMap,
                            boolean isBackMessage, boolean isLoad, boolean isDeclareQueue) {
        sendMessage(exchange, routingKey, messageMap, isBackMessage, isLoad, isDeclareQueue, 0);
    }

    /**
     * 发送消息
     *
     * @param exchange       交换机名
     * @param routingKey     消息KEY
     * @param messageMap     消息内容
     * @param isBackMessage  出错时是否把消息保存在数据库中，以后会自动发送到消息中 [true: try catch; false:throw exception]
     * @param isLoad         是否为开发环境
     * @param isDeclareQueue 是否检测消息定义存在
     * @param delaySecond    延迟发送时间 秒
     */
    public void sendMessage(String exchange, String routingKey, Map<String, Object> messageMap,
                            boolean isBackMessage, boolean isLoad, boolean isDeclareQueue, long delaySecond) {
        try {
            // isload add ipaddress to routingKey
            if (isLoad && !routingKey.endsWith(MQConfigUtils.EXISTS_IP)) {
                routingKey = MQConfigUtils.getAddStrQueneName(routingKey);
            }

            AmqpAdmin amqpAdmin = SpringContext.getBean(AmqpAdmin.class);
            //declareQueue
            if (isDeclareQueue) {
                if (amqpAdmin == null) {
                    throw new RuntimeException("AmqpAdmin not found");
                }
                amqpAdmin.declareQueue(new Queue(routingKey, true, false, isLoad));
            }

            if (messageMap == null) {
                messageMap = new HashMap<>();
            }

            int retryTimes = 0;
            if (messageMap.containsKey(CONSUMER_RETRY_KEY)) {
                retryTimes = (int) messageMap.get(CONSUMER_RETRY_KEY);
                messageMap.remove(CONSUMER_RETRY_KEY); //从body移动COMSUMER_RETRY_KEY到header
            }

            final int finalRetryTimes = retryTimes;
            Message message = new Message(JacksonUtil.bean2Json(messageMap).getBytes(StandardCharsets.UTF_8), new MessageProperties() {{
                setHeader(CONSUMER_RETRY_KEY, finalRetryTimes);

            }});


            AmqpTemplate amqpTemplate = SpringContext.getBean(AmqpTemplate.class);
            if (amqpTemplate == null) {
                throw new RuntimeException("AmqpTemplate not found");
            }
            if (exchange == null) {
                if (delaySecond > 0L) {
                    sendDelayQueue(amqpAdmin, amqpTemplate, "", routingKey, delaySecond, message);
                } else {
                    amqpTemplate.send(routingKey, message);
                }
            } else {
                if (delaySecond > 0L) {
                    sendDelayQueue(amqpAdmin, amqpTemplate, exchange, routingKey, delaySecond, message);
                } else {
                    amqpTemplate.send(exchange, routingKey, message);
                }
            }

        } catch (RuntimeException e) {
            $error(e.getMessage(), e);
            if (isBackMessage) {
                try {
                    mqBackMessageService.addBackMessage(routingKey, messageMap);
                } catch (Exception ignored) {
                }
            } else {
                throw e;
            }
        }
    }

    private boolean isLocal() {
        VoRabbitMqLocalConfig config = SpringContext.getBean(VoRabbitMqLocalConfig.class);
        return config == null || config.isLocal();
    }

    //声明临时延迟队列
    private void sendDelayQueue(AmqpAdmin amqpAdmin, AmqpTemplate amqpTemplate, String exchange, String redirectRoutingKey, long delaySecond, Message message) {
        Map<String, Object> queueMap = new HashMap<>();
//        queueMap.put("x-expires", 60000);
        queueMap.put("x-dead-letter-exchange", exchange);
        queueMap.put("x-dead-letter-routing-key", redirectRoutingKey);
        queueMap.put("x-message-ttl", TimeUnit.SECONDS.toMillis(delaySecond));
        String tempQueue = redirectRoutingKey + "-temp";
        amqpAdmin.declareQueue(new Queue(tempQueue, true, false, true, queueMap));
        amqpTemplate.send(tempQueue, message);
    }

}
