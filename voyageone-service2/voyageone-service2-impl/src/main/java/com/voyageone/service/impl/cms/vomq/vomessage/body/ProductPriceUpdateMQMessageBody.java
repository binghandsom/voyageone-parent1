package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.namesub.IMQMessageSubBeanName;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import org.apache.commons.lang.StringUtils;

/**
 * 更新product和group的价格Job
 *
 * @Author rex
 * @Create 2017-01-03 16:03VOCmsProductPriceUpdateQueue
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_BATCH_COUNT_PRODUCT_PRICE)
public class ProductPriceUpdateMQMessageBody extends BaseMQMessageBody  implements IMQMessageSubBeanName {

    private Long prodId;
    private Integer cartId;

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    @Override
    public void check() throws MQMessageRuleException {

        if (StringUtils.isBlank(super.getChannelId())) {
            throw new MQMessageRuleException("定时任务-同步产品sku的价格至code的group价格范围MQ发送异常, 参数channelId为空.");
        }
        if (prodId == null) {
            throw new MQMessageRuleException("定时任务-同步产品sku的价格至code的group价格范围MQ发送异常, 参数prodId为空.");
        }
        if (cartId == null) {
            throw new MQMessageRuleException("定时任务-同步产品sku的价格至code的group价格范围MQ发送异常, 参数cartId为空.");
        }
        if (StringUtils.isBlank(getSender())) {
            throw new MQMessageRuleException("定时任务-同步产品sku的价格至code的group价格范围MQ发送异常, 发送者为空.");
        }
    }

    @Override
    public String getSubBeanName() {
        return getChannelId();
    }
}
