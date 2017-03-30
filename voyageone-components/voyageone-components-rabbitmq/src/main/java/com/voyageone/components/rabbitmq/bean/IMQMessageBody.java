package com.voyageone.components.rabbitmq.bean;

import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;

public interface IMQMessageBody {

    int getConsumerRetryTimes();

    int getMqId();

    int getDelaySecond();

    String getSender();

    void check() throws MQMessageRuleException;
}
