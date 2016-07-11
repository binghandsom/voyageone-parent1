package com.voyageone.service.impl.com.mq;

import com.voyageone.common.mq.config.MQConfigUtils;
import com.voyageone.common.spring.SpringContext;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.com.mq.config.VoRabbitMqLocalConfig;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aooer 2016/2/29.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class MqSender extends BaseService {

    private static final String CONSUMER_RETRY_KEY = "$consumer_retry_times$";

    @Autowired
    private MqBackMessageService mqBackMessageService;

    /**
     * 发送消息
     *
     * @param routingKey 消息KEY
     * @param messageMap 消息内容
     */
    public void sendMessage(String routingKey, Map<String, Object> messageMap) {
        sendMessage(routingKey, messageMap, true);
    }

    /**
     * 发送消息
     *
     * @param routingKey 消息KEY
     * @param messageMap 消息内容
     * @param isBackMessage  出错时是否把消息保存在数据库中，以后会自动发送到消息中 [true: try catch; false:throw exception]
     */
    public void sendMessage(String routingKey, Map<String, Object> messageMap, boolean isBackMessage) {
        sendMessage(null, routingKey, messageMap, isBackMessage, isLocal(), true);
    }

    /**
     * 发送消息
     *
     * @param exchange        交换机名
     * @param routingKey      消息KEY
     * @param messageMap       消息内容
     * @param isBackMessage  出错时是否把消息保存在数据库中，以后会自动发送到消息中 [true: try catch; false:throw exception]
     * @param isLoad          是否为开发环境
     * @param isDeclareQueue 是否检测消息定义存在
     */
    public void sendMessage(String exchange, String routingKey, Map<String, Object> messageMap,
                            boolean isBackMessage, boolean isLoad, boolean isDeclareQueue) {
        try {
            // isload add ipaddress to routingKey
            if (isLoad && !routingKey.endsWith(MQConfigUtils.EXISTS_IP)) {
                routingKey = MQConfigUtils.getAddStrQueneName(routingKey);
            }

            //declareQueue
            if (isDeclareQueue) {
                AmqpAdmin amqpAdmin = SpringContext.getBean(AmqpAdmin.class);
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
            Message message = new Message(JacksonUtil.bean2Json(messageMap).getBytes(), new MessageProperties() {{
                setHeader(CONSUMER_RETRY_KEY, finalRetryTimes);
            }});

            AmqpTemplate amqpTemplate = SpringContext.getBean(AmqpTemplate.class);
            if (amqpTemplate == null) {
                throw new RuntimeException("AmqpTemplate not found");
            }
            if (exchange == null) {
                amqpTemplate.send(routingKey, message);
            } else {
                amqpTemplate.send(exchange, routingKey, message);
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

}
