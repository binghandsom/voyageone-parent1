package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

/**
 * CmsProcductBIDataMQMessageBody    从bi基础数据表取得产品的bi信息，并保存 (浏览量 访客数 加购件数 收藏人数)
 *
 * @author peitao 2016/12/30.
 * @version 2.0.0
 * @since 2.0.0
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_BATCH_GET_PRODUCT_BI_DATA)
public class CmsProductBIDataMQMessageBody extends BaseMQMessageBody {
    Integer cartId;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isEmpty(super.getChannelId())) {
            throw new MQMessageRuleException("定时任务-取得产品的bi信息MQ发送异常, 参数channelId为空.");
        }
        if (cartId == null || cartId == 0) {
            throw new MQMessageRuleException("定时任务-取得产品的bi信息MQ发送异常, 参数cartId为空或者0.");
        }
        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("定时任务-取得产品的bi信息MQ发送异常, 发送者为空.");
        }
    }
}
