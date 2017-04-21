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
@VOMQQueue(value = CmsMqRoutingKey.CMS_PART_APPROVAL)
public class AdvSearchPartApprovalMQMessageBody extends BaseMQMessageBody  implements IMQMessageSubBeanName  {

    private List<String> codeList;
    private Integer cartId;
    private List<String> platformWorkloadAttributes;

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

    public List<String> getPlatformWorkloadAttributes() {
        return platformWorkloadAttributes;
    }

    public void setPlatformWorkloadAttributes(List<String> platformWorkloadAttributes) {
        this.platformWorkloadAttributes = platformWorkloadAttributes;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isBlank(super.getChannelId())) {
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

        if (CollectionUtils.isEmpty(platformWorkloadAttributes)) {
            throw new MQMessageRuleException("高级检索-批量重新计算中国指导价MQ发送异常, 更新字段为空.");
        }
    }

    @Override
    public String getSubBeanName() {
        return getChannelId();
    }
}
