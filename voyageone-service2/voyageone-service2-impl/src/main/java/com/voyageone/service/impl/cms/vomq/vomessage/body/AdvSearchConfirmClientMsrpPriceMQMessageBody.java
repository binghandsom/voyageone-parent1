package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * 高级检索-确认客户成本价Job
 *
 * @Author rex
 * @Create 2016-12-30 17:08
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_CONFIRM_CLIENT_MSRP_PRICE)
public class AdvSearchConfirmClientMsrpPriceMQMessageBody extends BaseMQMessageBody {

    private List<String> codeList;

    public List<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isBlank(super.getChannelId())) {
            throw new MQMessageRuleException("高级检索-批量确认中国指导价变更MQ发送异常, 参数channelId为空.");
        }
        if (CollectionUtils.isEmpty(codeList)) {
            throw new MQMessageRuleException("高级检索-批量确认中国指导价变更MQ发送异常, 参数codeList为空.");
        }
        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("高级检索-批量确认中国指导价变更MQ发送异常, 发送者为空.");
        }
    }
}
