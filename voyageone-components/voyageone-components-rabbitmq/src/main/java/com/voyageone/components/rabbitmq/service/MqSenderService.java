package com.voyageone.components.rabbitmq.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.spring.SpringContext;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.ComponentBase;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.IMQMessageBody;
import com.voyageone.components.rabbitmq.config.VoRabbitMqLocalConfig;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.namesub.IMQMessageSubBeanName;
import com.voyageone.components.rabbitmq.utils.MQConfigUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
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
public class MqSenderService extends ComponentBase {

    @Autowired
    private IMqBackupMessage mqBackMessageService;

    public void sendMessage(IMQMessageBody message) throws MQMessageRuleException {
        sendMessage(message, isLocal());
    }

    public void sendMessage(IMQMessageBody message, boolean isLoad) throws MQMessageRuleException {
        final VOMQQueue voQueue = AnnotationUtils.findAnnotation(message.getClass(), VOMQQueue.class);
        if (voQueue == null)
            throw new BusinessException(message.getClass().getName() + "未加VOQueue注解");

        message.check();

        sendMessage(voQueue.exchange(), voQueue.value(), message, voQueue.isBackMessage(), isLoad, voQueue.isDeclareQueue(), message.getDelaySecond());
    }

    /**
     * 发送消息
     *
     * @param routingKey 消息KEY
     * @param messageMap 消息内容
     */
    public void sendMessage(String routingKey, Map<String, Object> messageMap) {
        sendMessage(null, routingKey, messageMap, true, isLocal(), true, 0);
    }

    /**
     * 发送消息
     *
     * @param exchange        交换机名
     * @param routingKey      消息KEY
     * @param message         消息内容
     * @param isBackupMessage 出错时是否把消息保存在数据库中，以后会自动发送到消息中 [true: try catch; false:throw exception]
     * @param isLoad          是否为开发环境
     * @param isDeclareQueue  是否检测消息定义存在
     * @param delaySecond     延迟发送时间 秒
     */
    public void sendMessage(String exchange, String routingKey, Object message,
                            boolean isBackupMessage, boolean isLoad, boolean isDeclareQueue, long delaySecond) {
        try {

            if (!routingKey.endsWith(MQConfigUtils.EXISTS_IP)) {
                // add subBeanName
                if (message instanceof IMQMessageSubBeanName) {
                    String subBeanName = ((IMQMessageSubBeanName)message).getSubBeanName();
                    if (!StringUtils.isBlank(subBeanName)) {
                        routingKey = MQConfigUtils.getNewBeanName(routingKey, subBeanName);
                    }
                }
                // isload add ipaddress to routingKey
                if (isLoad) {
                    routingKey = MQConfigUtils.getAddStrQueneName(routingKey);
                }
            }

            AmqpAdmin amqpAdmin = SpringContext.getBean(AmqpAdmin.class);
            //declareQueue
            if (isDeclareQueue) {
                if (amqpAdmin == null) {
                    throw new RuntimeException("AmqpAdmin not found");
                }
                amqpAdmin.declareQueue(new Queue(routingKey, true, false, isLoad));
            }

            if (message == null) {
                message = new HashMap<>();
            }

            Message m = new Message(JacksonUtil.bean2Json(message).getBytes(StandardCharsets.UTF_8), new MessageProperties());

            AmqpTemplate amqpTemplate = SpringContext.getBean(AmqpTemplate.class);
            if (amqpTemplate == null) {
                throw new RuntimeException("AmqpTemplate not found");
            }
            if (exchange == null || exchange.trim().length() == 0) {
                if (delaySecond > 0L) {
                    sendDelayQueue(amqpAdmin, amqpTemplate, "", routingKey, delaySecond, m);
                } else {
                    amqpTemplate.send(routingKey, m);
                }
            } else {
                if (delaySecond > 0L) {
                    sendDelayQueue(amqpAdmin, amqpTemplate, exchange, routingKey, delaySecond, m);
                } else {
                    amqpTemplate.send(exchange, routingKey, m);
                }
            }

        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            if (isBackupMessage) {
                try {
                    mqBackMessageService.addBackMessage(routingKey, message);
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
    @SuppressWarnings("Duplicates")
    private void sendDelayQueue(AmqpAdmin amqpAdmin, AmqpTemplate amqpTemplate, String exchange, String redirectRoutingKey, long delaySecond, Message message) {
        Map<String, Object> queueMap = new HashMap<>();
        queueMap.put("x-expires", 60000);
        queueMap.put("x-dead-letter-exchange", exchange);
        queueMap.put("x-dead-letter-routing-key", redirectRoutingKey);
        queueMap.put("x-message-ttl", TimeUnit.SECONDS.toMillis(delaySecond));
        String tempQueue = redirectRoutingKey + "-temp";
        amqpAdmin.declareQueue(new Queue(tempQueue, true, false, true, queueMap));
        amqpTemplate.send(tempQueue, message);
    }

}
