package com.voyageone.service.impl.cms.vomq.vomessage.body;

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
 * 高级检索-商品审批Job消息实体
 *
 * @Author rex
 * @Create 2017-01-13 11:29
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_UPDATE_PRODUCT_PLATFORM_STATUS_TO_APPROVE_BY_SMART)
public class AdvSearchProductApprovalBySmartMQMessageBody extends BaseMQMessageBody  implements IMQMessageSubBeanName{

    private Integer cartId;
    private List<String> productCodes;
    private Map<String, Object> cmsSessionParams;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public List<String> getProductCodes() {
        return productCodes;
    }

    public void setProductCodes(List<String> productCodes) {
        this.productCodes = productCodes;
    }

    public Map<String, Object> getCmsSessionParams() {
        return cmsSessionParams;
    }

    public void setCmsSessionParams(Map<String, Object> cmsSessionParams) {
        this.cmsSessionParams = cmsSessionParams;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isBlank(super.getChannelId())) {
            throw new MQMessageRuleException("高级检索-批量上新商品平台状态MQ发送异常, 参数channelId为空.");
        }
        if (cartId == null) {
            throw new MQMessageRuleException("高级检索-批量上新商品平台状态MQ发送异常, 参数cartList为空.");
        }
        if (CollectionUtils.isEmpty(productCodes)) {
            throw new MQMessageRuleException("高级检索-批量上新商品平台状态MQ发送异常, 参数productCodes为空.");
        }
        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("高级检索-商品上新MQ发送异常, 发送者为空.");
        }
    }

    @Override
    public String getSubBeanName() {
        return getChannelId();
    }
}
