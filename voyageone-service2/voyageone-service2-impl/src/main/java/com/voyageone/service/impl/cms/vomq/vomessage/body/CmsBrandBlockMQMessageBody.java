package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.CmsBtBrandBlockModel;

/**
 * CmsBrandBlockMQMessageBody   执行品牌屏蔽（解除屏蔽）的额外操作。如查找品牌相关的商品，并下架锁定这些商品
 *
 * @author peitao 2016/12/30.
 * @version 2.0.0
 * @since 2.0.0
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_BRAND_BLOCK)
public class CmsBrandBlockMQMessageBody extends BaseMQMessageBody {

    CmsBtBrandBlockModel data;
    boolean blocking;

    public CmsBtBrandBlockModel getData() {
        return data;
    }

    public void setData(CmsBtBrandBlockModel data) {
        this.data = data;
    }

    public boolean isBlocking() {
        return blocking;
    }

    public void setBlocking(boolean blocking) {
        this.blocking = blocking;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isEmpty(super.getChannelId())) {
            throw new MQMessageRuleException("变更黑名单操作MQ发送异常, 参数channelId为空.");
        }
        if (data == null) {
            throw new MQMessageRuleException("变更黑名单操作MQ发送异常, 参数data为空.");
        }
        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("变更黑名单操作MQ发送异常, 发送者为空.");
        }
    }
}
