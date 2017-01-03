package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import java.util.HashMap;
import java.util.Map;

/**
 * 高级检索-确认指导价变更Job消息实体
 *
 * @Author rex
 * @Create 2016-12-30 17:08
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_ADV_SEARCH_CONFIRM_RETAIL_PRICE)
public class AdvSearchConfirmRetailPriceMQMessageBody extends BaseMQMessageBody {

    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (params == null || params.size() <= 0) {
            throw new MQMessageRuleException("高级检索-确认指定价变更MQ参数为空");
        }
    }
}
