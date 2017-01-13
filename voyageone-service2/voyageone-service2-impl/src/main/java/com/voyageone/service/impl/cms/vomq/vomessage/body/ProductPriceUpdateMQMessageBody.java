package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 更新product和group的价格Job
 *
 * @Author rex
 * @Create 2017-01-03 16:03VOCmsProductPriceUpdateQueue
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_PRODUCT_PRICE_UPDATE)
public class ProductPriceUpdateMQMessageBody extends BaseMQMessageBody {

    private String channelId;
    private Long prodId;
    private Integer cartId;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

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

        if (StringUtils.isBlank(channelId)) {
            throw new MQMessageRuleException("product和group的价格刷新MQ发送异常, 参数channelId为空.");
        }
        if (prodId == null) {
            throw new MQMessageRuleException("product和group的价格刷新MQ发送异常, 参数prodId为空.");
        }
        if (cartId == null) {
            throw new MQMessageRuleException("product和group的价格刷新MQ发送异常, 参数cartId为空.");
        }
        if (StringUtils.isBlank(getSender())) {
            throw new MQMessageRuleException("sender(发送者)不能为空");
        }
    }
}
