package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Product VoRate Update Job
 *
 * @Author rex
 * @Create 2016-12-30 18:06
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_UPDATE_PRODUCT_VO_RATE)
public class ProductVoRateUpdateMQMessageBody extends BaseMQMessageBody {

    private List<String> codeList;
    private String voRate;

    public String getVoRate() {
        return voRate;
    }

    public void setVoRate(String voRate) {
        this.voRate = voRate;
    }

    public List<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isBlank(super.getChannelId())) {
            throw new MQMessageRuleException("高级检索-批量更新商品vo扣点MQ发送异常, 参数channelId为空.");
        }
        if (CollectionUtils.isEmpty(codeList)) {
            throw new MQMessageRuleException("高级检索-批量更新商品vo扣点MQ发送异常, 参数codeList为空.");
        }
        if (StringUtils.isBlank(getSender())) {
            throw new MQMessageRuleException("高级检索-批量更新商品vo扣点MQ发送异常, 发送者为空.");
        }
    }

}
