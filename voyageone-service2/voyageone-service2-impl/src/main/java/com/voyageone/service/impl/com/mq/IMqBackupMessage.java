package com.voyageone.service.impl.com.mq;

/**
 * Created by dell on 2016/12/23.
 */
public interface IMqBackupMessage {
     void addBackMessage(String routingKey, Object messageMap);
}
