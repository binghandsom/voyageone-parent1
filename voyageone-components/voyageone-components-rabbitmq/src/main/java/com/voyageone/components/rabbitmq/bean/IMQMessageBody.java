package com.voyageone.components.rabbitmq.bean;

import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;

public interface IMQMessageBody {

    int getConsumerRetryTimes();

    int getMqId();

    long getDelaySecond();

    String getSender();

    String getChannelId();

    void check() throws MQMessageRuleException;
}
