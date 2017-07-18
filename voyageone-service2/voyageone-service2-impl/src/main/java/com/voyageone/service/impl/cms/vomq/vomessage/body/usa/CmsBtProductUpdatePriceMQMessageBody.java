package com.voyageone.service.impl.cms.vomq.vomessage.body.usa;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.namesub.IMQMessageSubBeanName;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/7/17.
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_USA_PRODUCT_UPDATE_PRICE)
public class CmsBtProductUpdatePriceMQMessageBody extends BaseMQMessageBody implements IMQMessageSubBeanName {
    private List<String> productCodes;
    private Integer cartId;
    private Map<String, Object> params;

    public List<String> getProductCodes() {
        return productCodes;
    }

    public void setProductCodes(List<String> productCodes) {
        this.productCodes = productCodes;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isBlank(super.getChannelId())) {
            throw new MQMessageRuleException("高级检索-批量修改价格MQ发送异常, 参数channelId为空.");
        }
        if (cartId == null) {
            throw new MQMessageRuleException("高级检索-批量修改价格MQ发送异常, 参数cartId为空.");
        }
        if (CollectionUtils.isEmpty(productCodes)) {
            throw new MQMessageRuleException("高级检索-批量修改价格MQ发送异常, 参数productCodes为空.");
        }
        if (params == null || params.size() <= 0) {
            throw new MQMessageRuleException("高级检索-批量修改价格MQ发送异常, 参数params为空.");
        }
        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("高级检索-批量修改价格MQ发送异常, 发送者为空.");
        }
    }

    @Override
    public String getSubBeanName() {
            return getChannelId();
    }
}
