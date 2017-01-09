package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 修改商品saleprice 消息实体
 *
 * @Author rex
 * @Create 2017-01-09 13:57
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_UPDATE_PRODUCT_SALE_PRECE)
public class UpdateProductSalePriceMQMessageBody extends BaseMQMessageBody {

    private List<String> productCodes;
    private Integer cartId;
    private String channelId;
    private Map<String, Object> params;
    private Integer userId;

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

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (org.apache.commons.lang.StringUtils.isBlank(channelId)) {
            throw new MQMessageRuleException("批量修改商品价格MQ发送异常,参数channelId为空.");
        }
        if (cartId == null) {
            throw new MQMessageRuleException("批量修改商品价格MQ发送异常,参数cartId为空.");
        }
        if (CollectionUtils.isEmpty(productCodes)) {
            throw new MQMessageRuleException("批量修改商品价格MQ发送异常,参数productCodes为空.");
        }
        if (params == null || params.size() <= 0) {
            throw new MQMessageRuleException("批量修改商品价格MQ发送异常,修改参数为空.");
        }

        if (userId == null) {
            throw new MQMessageRuleException("批量修改商品价格MQ发送异常,参数userId为空.");
        }

        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("sender(发送者)不能为空");
        }
    }
}
