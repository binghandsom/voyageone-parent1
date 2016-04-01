package com.voyageone.common.mq;

import com.voyageone.common.mq.dao.MqMsgBackDao;
import com.voyageone.common.mq.enums.MqRoutingKey;
import com.voyageone.common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public void sendMessage(MqRoutingKey routingKey, Map<String, Object> messageMap){
        try{
            amqpAdmin.declareQueue(new Queue(routingKey.getValue()));
            if (messageMap == null) {
                messageMap = new HashMap<>();
            }
            amqpTemplate.convertAndSend(routingKey.getValue(), JsonUtil.getJsonString(messageMap));
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            msgBackDao.insertBatchMessage(routingKey.toString(),messageMap);
        }
    }

}
