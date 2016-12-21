package com.voyageone.service.impl.com.mq.message;

import com.voyageone.common.mq.config.IMQMessageBody;
import com.voyageone.common.mq.exception.MQMessageRuleException;

/**
 * Created by dell on 2016/12/20.
 */
public  abstract class BaseMQMessageBody implements IMQMessageBody {
  public    abstract void check() throws MQMessageRuleException;
}
