package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import java.util.Map;

/**
 * 重新计算指导价Job消息实体
 *
 * @Author rex
 * @Create 2016-12-30 16:09
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_ADV_SEARCH_REFRESH_RETAIL_PRICE)
public class AdvSearchRefreshRetailPriceMQMessageBody extends BaseMQMessageBody {

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
            throw new MQMessageRuleException("批量重新计算指导价MQ参数为空");
        }
    }

}
