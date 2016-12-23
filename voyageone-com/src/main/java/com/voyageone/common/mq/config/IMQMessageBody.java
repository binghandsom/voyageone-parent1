package com.voyageone.common.mq.config;

import com.voyageone.common.mq.exception.MQMessageRuleException;

/**
 * Created by dell on 2016/12/20.
 */
public  interface IMQMessageBody {

     int getConsumerRetryTimes();

     void check() throws MQMessageRuleException;

}
