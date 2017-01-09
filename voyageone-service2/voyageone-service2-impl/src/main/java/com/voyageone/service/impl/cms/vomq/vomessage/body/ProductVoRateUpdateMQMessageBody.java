package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Product VoRate Update Job
 *
 * @Author rex
 * @Create 2016-12-30 18:06
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_PRODUCT_VO_RATE_UPDATE)
public class ProductVoRateUpdateMQMessageBody extends BaseMQMessageBody {

    private String channelId;
    private String creater;
    private List<String> codeList;
    private String voRate;

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isBlank(channelId)) {
            throw new MQMessageRuleException("ProductVoRateUpdate参数channelId为空.");
        }
        if (CollectionUtils.isEmpty(codeList)) {
            throw new MQMessageRuleException("ProductVoRateUpdate参数codeList为空.");
        }
        if (StringUtils.isBlank(voRate)) {
            throw new MQMessageRuleException("ProductVoRateUpdate参数voRate为空.");
        }
        if (StringUtils.isBlank(creater)) {
            throw new MQMessageRuleException("ProductVoRateUpdate参数vcreater为空.");
        }
        if (StringUtils.isBlank(getSender())) {
            throw new MQMessageRuleException("sender(发送者)不能为空");
        }
    }

    public String getVoRate() {
        return voRate;
    }

    public void setVoRate(String voRate) {
        this.voRate = voRate;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public List<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
    }

}
