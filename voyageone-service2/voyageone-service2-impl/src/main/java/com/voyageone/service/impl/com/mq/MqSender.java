package com.voyageone.service.impl.com.mq;

import com.voyageone.service.impl.com.mq.config.AnnotationProcessorByIP;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.BaseService;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
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

//    public void sendMessage(MqRoutingKey routingKey, Map<String, Object> messageMap) {
//        sendMessage(routingKey.getValue(), messageMap);
//    }


    public void sendMessage(String routingKey, Map<String, Object> messageMap) {
        try {
            // isload add ipaddress to routingKey
            if (annotationProcessorByIP.isLocal() && !routingKey.endsWith(EXISTS_IP)) {
                routingKey += InetAddress.getLocalHost().toString().replace("/", "_") + EXISTS_IP;
            }

            //declareQueue
            amqpAdmin.declareQueue(new Queue(routingKey));
            if (messageMap == null) {
                messageMap = new HashMap<>();
            }

            int retryTimes = 0;
            if (messageMap.containsKey(CONSUMER_RETRY_KEY)) {
                retryTimes = (int)messageMap.get(CONSUMER_RETRY_KEY);
                messageMap.remove(CONSUMER_RETRY_KEY); //从body移动COMSUMER_RETRY_KEY到header
            }

            final int finalRetryTimes = retryTimes;
            amqpTemplate.send(routingKey, new Message(JacksonUtil.bean2Json(messageMap).getBytes(), new MessageProperties() {{
                setHeader(CONSUMER_RETRY_KEY, finalRetryTimes);
            }}));

        } catch (Exception e) {
            $error(e.getMessage(), e);
            try {
                mqBackMessageService.addBackMessage(routingKey, messageMap);
            } catch (Exception ignored) {
            }
        }
    }

}