package com.voyageone.service.impl.com.mq.message;

/**
 * Created by dell on 2016/12/20.
 */
public  abstract class BaseMQMessageBody {
     abstract void check() throws MQMessageRuleException;
}
