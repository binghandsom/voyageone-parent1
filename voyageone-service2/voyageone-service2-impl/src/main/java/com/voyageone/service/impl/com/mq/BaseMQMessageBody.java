package com.voyageone.service.impl.com.mq;

import com.voyageone.common.mq.config.IMQMessageBody;
import com.voyageone.common.mq.exception.MQMessageRuleException;

/**
 * Created by dell on 2016/12/20.
 */
public abstract class BaseMQMessageBody implements IMQMessageBody {

    protected int consumerRetryTimes = 0;

    @Override
    public int getConsumerRetryTimes() {
        return consumerRetryTimes;
    }

    public void setConsumerRetryTimes(int consumerRetryTimes) {
        this.consumerRetryTimes = consumerRetryTimes;
    }

    public abstract void check() throws MQMessageRuleException;
}
