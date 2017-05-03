package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.namesub.IMQMessageSubBeanName;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2017/3/28.
 */
@VOMQQueue(value = CmsMqRoutingKey.EWMS_MQ_GROUP_SKU)
public class EwmsMQUpdateProductMessageBody  extends BaseMQMessageBody implements IMQMessageSubBeanName {

//    private String channelId;
    private Integer cartId;
    private String groupSku;
    private Double groupPrice;
    private List<Map<String, BigDecimal>> sku;
    private String groupKind;
    private String numIid;
    private String userName;
    private String type;

//    public String getChannelId() {
//        return channelId;
//    }
//
//    public void setChannelId(String channelId) {
//        this.channelId = channelId;
//    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getGroupSku() {
        return groupSku;
    }

    public void setGroupSku(String groupSku) {
        this.groupSku = groupSku;
    }

    public Double getGroupPrice() {
        return groupPrice;
    }

    public void setGroupPrice(Double groupPrice) {
        this.groupPrice = groupPrice;
    }

    public List<Map<String, BigDecimal>> getSku() {
        return sku;
    }

    public void setSku(List<Map<String, BigDecimal>> sku) {
        this.sku = sku;
    }

    public String getGroupKind() {
        return groupKind;
    }

    public void setGroupKind(String groupKind) {
        this.groupKind = groupKind;
    }

    public String getNumIid() {
        return numIid;
    }

    public void setNumIid(String numIid) {
        this.numIid = numIid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isBlank(getChannelId())) {
            throw new MQMessageRuleException("组合商品推送MQ发送异常, 参数channelId为空.");
        }
        if (cartId == null) {
            throw new MQMessageRuleException("组合商品推送MQ发送异常, 参数cartId为空.");
        }
        if (StringUtils.isBlank(groupSku)) {
            throw new MQMessageRuleException("组合商品推送MQ发送异常, 参数groupSku为空.");
        }
        if (CollectionUtils.isEmpty(sku)) {
            throw new MQMessageRuleException("组合商品推送MQ发送异常, 参数sku为空.");
        }
    }

    @Override
    public String getSubBeanName() {
        return getChannelId();
    }
}
