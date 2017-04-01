package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.CmsConstants;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.namesub.IMQMessageSubBeanName;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * 记录上下架操作历史 Job
 *
 * @Author rex
 * @Create 2017-01-04 19:31
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_UPDATE_PRODUCT_PLATFORM_PSTATUS_OFF_OR_ON)
public class PlatformActiveLogMQMessageBody extends BaseMQMessageBody  implements IMQMessageSubBeanName {

    private String channelId;
    private Integer cartId;
    private String activeStatus;
    private List<String> productCodes;
    private String comment;
    private CmsConstants.PlatformActive statusVal;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CmsConstants.PlatformActive getStatusVal() {
        return statusVal;
    }

    public void setStatusVal(CmsConstants.PlatformActive statusVal) {
        this.statusVal = statusVal;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isBlank(channelId)) {
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
        if (StringUtils.isBlank(String.valueOf(statusVal))) {
            throw new MQMessageRuleException("高级检索-批量设置商品上下架MQ发送异常, 参数statusVal为空.");
        }
        if (StringUtils.isBlank(getSender())) {
            throw new MQMessageRuleException("高级检索-批量设置商品上下架MQ发送异常, 发送者为空.");
        }
    }

    @Override
    public String getSubBeanName() {
        return getChannelId();
    }
}
