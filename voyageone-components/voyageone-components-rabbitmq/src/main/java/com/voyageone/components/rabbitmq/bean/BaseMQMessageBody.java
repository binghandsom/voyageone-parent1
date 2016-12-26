package com.voyageone.components.rabbitmq.bean;

import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;

/**
 * Created by dell on 2016/12/20.
 */
public abstract class BaseMQMessageBody implements IMQMessageBody {
    //消费者重试次数
    protected int consumerRetryTimes = 0;

    public int getConsumerRetryTimes() {
        return consumerRetryTimes;
    }

    public void setConsumerRetryTimes(int consumerRetryTimes) {
        this.consumerRetryTimes = consumerRetryTimes;
    }

    public abstract void check() throws MQMessageRuleException;
}
