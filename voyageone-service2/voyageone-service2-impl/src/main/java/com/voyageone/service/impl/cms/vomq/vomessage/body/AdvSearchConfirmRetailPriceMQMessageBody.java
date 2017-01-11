package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 高级检索-确认指导价变更Job消息实体
 *
 * @Author rex
 * @Create 2016-12-30 17:08
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_ADV_SEARCH_CONFIRM_RETAIL_PRICE)
public class AdvSearchConfirmRetailPriceMQMessageBody extends BaseMQMessageBody {

    private String channelId;
    private List<Integer> cartList;
    private List<String> codeList;
    private String userName;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public List<Integer> getCartList() {
        return cartList;
    }

    public void setCartList(List<Integer> cartList) {
        this.cartList = cartList;
    }

    public List<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isBlank(channelId)) {
            throw new MQMessageRuleException("高级检索-确认指导价变更MQ发送异常, 参数channelId为空.");
        }
        if (CollectionUtils.isEmpty(cartList)) {
            throw new MQMessageRuleException("高级检索-确认指导价变更MQ发送异常, 参数cartList为空.");
        }
        if (CollectionUtils.isEmpty(codeList)) {
            throw new MQMessageRuleException("高级检索-确认指导价变更MQ发送异常, 参数codeList为空.");
        }
        if (StringUtils.isBlank(userName)) {
            throw new MQMessageRuleException("高级检索-确认指导价变更MQ发送异常, 参数userName为空.");
        }
        if (StringUtils.isBlank(getSender())) {
            throw new MQMessageRuleException("sender(发送者)不能为空");
        }
    }
}
