package com.voyageone.task2.cms.mqjob.test;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.namesub.IMQMessageSubBeanName;
import com.voyageone.service.model.cms.CmsBtBrandBlockModel;

/**
 * CmsBrandBlockMQMessageBody   执行品牌屏蔽（解除屏蔽）的额外操作。如查找品牌相关的商品，并下架锁定这些商品
 *
 * @author peitao 2016/12/30.
 * @version 2.0.0
 * @since 2.0.0
 */
@VOMQQueue(value = "SubChannelMQMessage")
public class MqSubChannelMQMessageBody extends BaseMQMessageBody implements IMQMessageSubBeanName {

    private CmsBtBrandBlockModel data;

    public CmsBtBrandBlockModel getData() {
        return data;
    }

    public void setData(CmsBtBrandBlockModel data) {
        this.data = data;
    }

    @Override
    public void check() throws MQMessageRuleException {
    }

    @Override
    public String getSubBeanName() {
        return data.getChannelId();
    }

}
