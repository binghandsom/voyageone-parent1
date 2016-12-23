package com.voyageone.components.rabbitmq.bean;

import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;

/**
 * Created by dell on 2016/12/20.
 */
public  interface IMQMessageBody {

     int getConsumerRetryTimes();

     void check() throws MQMessageRuleException;

}
