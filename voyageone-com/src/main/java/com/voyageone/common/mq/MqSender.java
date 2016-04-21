package com.voyageone.common.mq;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.mq.config.RabbitListenerAnnotationBeanPostSplitIPProcessor;
import com.voyageone.common.mq.dao.MqMsgBackDao;
import com.voyageone.common.mq.enums.MqRoutingKey;
import com.voyageone.common.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aooer 2016/2/29.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component
public class MqSender {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private MqMsgBackDao msgBackDao;

    @Autowired
    private RabbitListenerAnnotationBeanPostSplitIPProcessor rabbitListenerAnnotationBeanPostSplitIPProcessor;

    private static final String CONSUMER_RETRY_KEY= "$consumer_retry_times$";

    private static final String EXISTS_IP="$EXISTS_IP$";

    public void sendMessage(MqRoutingKey routingKey, Map<String, Object> messageMap){
        sendMessage(routingKey.getValue(),messageMap);
    }

    public void sendMessage(String routingKey, Map<String, Object> messageMap){
        try{
            // isload add ipaddress to routingKey
            if (rabbitListenerAnnotationBeanPostSplitIPProcessor.isLocal()&&!routingKey.endsWith(EXISTS_IP)) {
                routingKey+=InetAddress.getLocalHost().toString().replace("/","_")+EXISTS_IP;
            }

            amqpAdmin.declareQueue(new Queue(routingKey));
            if (messageMap == null) {
                messageMap = new HashMap<>();
            }
            Object $retryTimes$=messageMap.get(CONSUMER_RETRY_KEY);
            if(StringUtils.isEmpty($retryTimes$)) {
                amqpTemplate.convertAndSend(routingKey, JacksonUtil.bean2Json(messageMap));
            }else {
                messageMap.remove(CONSUMER_RETRY_KEY); //从body移动COMSUMER_RETRY_KEY到header
                amqpTemplate.send(routingKey, new Message(JacksonUtil.bean2Json(messageMap).getBytes(), new MessageProperties() {{setHeader(CONSUMER_RETRY_KEY,Integer.parseInt(String.valueOf($retryTimes$)));}}));
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            msgBackDao.insertBatchMessage(routingKey,messageMap);
        }
    }

}
