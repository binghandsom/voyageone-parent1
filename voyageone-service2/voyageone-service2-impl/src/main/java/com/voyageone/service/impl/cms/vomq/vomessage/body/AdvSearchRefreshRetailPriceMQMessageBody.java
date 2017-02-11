package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * 重新计算指导价Job消息实体
 *
 * @Author rex
 * @Create 2016-12-30 16:09
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_REFRESH_PLATFORM_RETAIL_PRICE)
public class AdvSearchRefreshRetailPriceMQMessageBody extends BaseMQMessageBody {

    private String channelId;
    private String userName;
    private List<String> codeList;
    private List<Integer> cartList;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
    }

    public List<Integer> getCartList() {
        return cartList;
    }

    public void setCartList(List<Integer> cartList) {
        this.cartList = cartList;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isBlank(channelId)) {
            throw new MQMessageRuleException(String.format("高级检索->重新计算指导价MQ发送异常, 参数channelId为空."));
        }
        if (CollectionUtils.isEmpty(codeList)) {
            throw new MQMessageRuleException(String.format("高级检索->重新计算指导价MQ发送异常, 参数codeList为空."));
        }
        if (CollectionUtils.isEmpty(cartList)) {
            throw new MQMessageRuleException(String.format("高级检索->重新计算指导价MQ发送异常, 参数cartList为空."));
        }
        if (StringUtils.isBlank(userName)) {
            throw new MQMessageRuleException(String.format("高级检索->重新计算指导价MQ发送异常, 参数userName为空."));
        }
        if (StringUtils.isBlank(getSender())) {
            throw new MQMessageRuleException("sender(发送者)不能为空");
        }
    }

}
