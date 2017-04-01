package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.namesub.IMQMessageSubBeanName;
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
public class AdvSearchRefreshRetailPriceMQMessageBody extends BaseMQMessageBody  implements IMQMessageSubBeanName {

    private String channelId;
    private List<String> codeList;
    private Integer cartId;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public List<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
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
            throw new MQMessageRuleException("高级检索-批量重新计算中国指导价MQ发送异常, 参数channelId为空.");
        }
        if (CollectionUtils.isEmpty(codeList)) {
            throw new MQMessageRuleException("高级检索-批量重新计算中国指导价MQ发送异常, 参数codeList为空.");
        }
        if (cartId == null) {
            throw new MQMessageRuleException("高级检索-批量重新计算中国指导价MQ发送异常, 参数cartId为空.");
        }
        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("高级检索-批量重新计算中国指导价MQ发送异常, 发送者为空.");
        }
    }

    @Override
    public String getSubBeanName() {
        return getChannelId();
    }
}
