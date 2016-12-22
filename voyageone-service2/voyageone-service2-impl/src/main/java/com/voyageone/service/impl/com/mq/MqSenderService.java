package com.voyageone.service.impl.com.mq;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.mq.config.MQConfigUtils;
import com.voyageone.common.mq.exception.MQMessageRuleException;
import com.voyageone.common.spring.SpringContext;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.com.mq.config.VoRabbitMqLocalConfig;
import com.voyageone.common.mq.config.IMQMessageBody;
import com.voyageone.common.mq.config.VOMQQueue;
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
public class MqSenderService extends BaseService {

    private static final String CONSUMER_RETRY_KEY = "$consumer_retry_times$";

    @Autowired
    private MqBackMessageService mqBackMessageService;

    public  void  sendMessage(IMQMessageBody message) throws MQMessageRuleException {
        final VOMQQueue voQueue = AnnotationUtils.findAnnotation(message.getClass(), VOMQQueue.class);
        if (voQueue == null)
            throw new BusinessException(message.getClass().getName() + "未加VOQueue注解");

        message.check();

        sendMessage(null, voQueue.queues()[0], message, true, isLocal(), true, 0);
    }

//    /**
//     * 发送消息
//     *
//     * @param routingKey 消息KEY
//     * @param message 消息内容
//     */
//    public void sendMessage(String routingKey, Object message) {
//        sendMessage(routingKey, message, true, 0);
//    }
//
//    public void sendMessage(String routingKey, Object message, long delaySecond) {
//        sendMessage(routingKey, message, true, delaySecond);
//    }
//
//    /**
//     * 发送消息
//     *
//     * @param routingKey    消息KEY
//     * @param message    消息内容
//     * @param isBackMessage 出错时是否把消息保存在数据库中，以后会自动发送到消息中 [true: try catch; false:throw exception]
//     * @param delaySecond    延迟发送时间 秒
//     */
//    public void sendMessage(String routingKey, Object message, boolean isBackMessage, long delaySecond) {
//        sendMessage(null, routingKey, message, isBackMessage, isLocal(), true, delaySecond);
//    }
//
//
//    public void sendMessage(String exchange, String routingKey, Object messageMap,
//                            boolean isBackMessage, boolean isLoad, boolean isDeclareQueue) {
//        sendMessage(exchange, routingKey, messageMap, isBackMessage, isLoad, isDeclareQueue, 0);
//    }

    /**
     * 发送消息
     *
     * @param exchange       交换机名
     * @param routingKey     消息KEY
     * @param message     消息内容
     * @param isBackMessage  出错时是否把消息保存在数据库中，以后会自动发送到消息中 [true: try catch; false:throw exception]
     * @param isLoad         是否为开发环境
     * @param isDeclareQueue 是否检测消息定义存在
     * @param delaySecond    延迟发送时间 秒
     */
    public void sendMessage(String exchange, String routingKey, Object message,
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

//            if (messageMap == null) {
//                messageMap = new HashMap<>();
//            }

//            int retryTimes = 0;
//            if (messageMap.containsKey(CONSUMER_RETRY_KEY)) {
//                retryTimes = (int) messageMap.get(CONSUMER_RETRY_KEY);
//                messageMap.remove(CONSUMER_RETRY_KEY); //从body移动COMSUMER_RETRY_KEY到header
//            }

           // final int finalRetryTimes = retryTimes;
            Message m = new Message(JacksonUtil.bean2Json(message).getBytes(StandardCharsets.UTF_8), new MessageProperties() {{
                //setHeader(CONSUMER_RETRY_KEY, finalRetryTimes);

            }});


            AmqpTemplate amqpTemplate = SpringContext.getBean(AmqpTemplate.class);
            if (amqpTemplate == null) {
                throw new RuntimeException("AmqpTemplate not found");
            }
            if (exchange == null) {
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
            $error(e.getMessage(), e);
            if (isBackMessage) {
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
