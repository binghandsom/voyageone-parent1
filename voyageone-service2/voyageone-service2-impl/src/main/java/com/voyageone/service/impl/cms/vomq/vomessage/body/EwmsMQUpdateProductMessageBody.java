package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by gjl on 2017/3/28.
 */
@VOMQQueue(value = CmsMqRoutingKey.EWMS_MQ_GROUP_SKU)
public class EwmsMQUpdateProductMessageBody  extends BaseMQMessageBody {

    private String channelId;
    private Integer cartId;
    private String groupSku;
    private List<String> sku;
    private String groupKind;
    private String numIid;
    private String userName;
    private String type;

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

    public String getGroupSku() {
        return groupSku;
    }

    public void setGroupSku(String groupSku) {
        this.groupSku = groupSku;
    }

    public List<String> getSku() {
        return sku;
    }

    public void setSku(List<String> sku) {
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
        if (StringUtils.isBlank(channelId)) {
            throw new MQMessageRuleException("高级检索-组合商品推送MQ发送异常, 参数channelId为空.");
        }
        if (cartId== null) {
            throw new MQMessageRuleException("高级检索-组合商品推送MQ发送异常, 参数cartId为空.");
        }
        if (StringUtils.isBlank(groupSku)) {
            throw new MQMessageRuleException("高级检索-组合商品推送MQ发送异常, 参数groupSku为空.");
        }
        if (sku== null) {
            throw new MQMessageRuleException("高级检索-组合商品推送MQ发送异常, 参数sku为空.");
        }
    }
}
