package com.voyageone.service.impl.com.mq;

import com.voyageone.common.mq.config.MQConfigUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.com.mq.config.AnnotationProcessorByIP;
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

    @Autowired
    private MqBackMessageService mqBackMessageService;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private AnnotationProcessorByIP annotationProcessorByIP;

    private static final String CONSUMER_RETRY_KEY = "$consumer_retry_times$";

    private static final String EXISTS_IP = "$EXISTS_IP$";

    @Autowired
    private VoRabbitMqLocalConfig voRabbitMqLocalConfig;


    public void sendMessage(String routingKey, Map<String, Object> messageMap) {
        try {
            // isload add ipaddress to routingKey
            if (annotationProcessorByIP.isLocal() && !routingKey.endsWith(MQConfigUtils.EXISTS_IP)) {
                routingKey = MQConfigUtils.getAddStrQueneName(routingKey);
            }

            //declareQueue
            amqpAdmin.declareQueue(new Queue(routingKey, true, false, voRabbitMqLocalConfig.isLocal()));
            if (messageMap == null) {
                messageMap = new HashMap<>();
            }

            int retryTimes = 0;
            if (messageMap.containsKey(CONSUMER_RETRY_KEY)) {
                retryTimes = (int)messageMap.get(CONSUMER_RETRY_KEY);
                messageMap.remove(CONSUMER_RETRY_KEY); //从body移动COMSUMER_RETRY_KEY到header
            }

            final int finalRetryTimes = retryTimes;
            Message message = new Message(JacksonUtil.bean2Json(messageMap).getBytes(), new MessageProperties() {{
                setHeader(CONSUMER_RETRY_KEY, finalRetryTimes);
            }});
            amqpTemplate.send(routingKey, message);

        } catch (Exception e) {
            $error(e.getMessage(), e);
            try {
                mqBackMessageService.addBackMessage(routingKey, messageMap);
            } catch (Exception ignored) {
            }
        }
    }

}
