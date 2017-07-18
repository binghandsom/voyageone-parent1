package com.voyageone.service.impl.cms.vomq.vomessage.body.usa;

import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by dell on 2017/7/18.
 */
public class CmsBtProductUpdateListDelistStatusMQMessageBody extends BaseMQMessageBody {
    private Integer cartId;
    private String activeStatus;
    private List<String> productCodes;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public List<String> getProductCodes() {
        return productCodes;
    }

    public void setProductCodes(List<String> productCodes) {
        this.productCodes = productCodes;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isBlank(super.getChannelId())) {
            throw new MQMessageRuleException("高级检索-批量设置商品上下架MQ发送异常, 参数channelId为空.");
        }
        if (CollectionUtils.isEmpty(productCodes)) {
            throw new MQMessageRuleException("高级检索-批量设置商品上下架MQ发送异常, 参数productCodes为空.");
        }
        if (cartId == null) {
            throw new MQMessageRuleException("高级检索-批量设置商品上下架MQ发送异常, 参数cartId为空.");
        }
        if (StringUtils.isBlank(activeStatus)) {
            throw new MQMessageRuleException("高级检索-批量设置商品上下架MQ发送异常, 参数activeStatus为空.");
        }
        if (StringUtils.isBlank(getSender())) {
            throw new MQMessageRuleException("高级检索-批量设置商品上下架MQ发送异常, 发送者为空.");
        }
    }
}
