package com.voyageone.service.impl.com.mq.handler;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * @author chuanyu.liang 2016/5/29.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class MqAdminHandler {
    @Resource
    RabbitAdmin admin;

    public int getQueueCount(final String name) {
        int messageCount = 0;
        try {
            Properties props = admin.getQueueProperties(name);
            messageCount = Integer.parseInt(props.get("QUEUE_MESSAGE_COUNT").toString());
        } catch (Exception ignored) {
        }
        return messageCount;
    }
}
