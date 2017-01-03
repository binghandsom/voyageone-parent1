package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import java.util.Map;

/**
 * 批量更新商品Job消息实体
 *
 * @Author rex
 * @Create 2017-01-03 13:59
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_BATCH_UPDATE_PRODUCT)
public class BatchUpdateProductMQMessageBody extends BaseMQMessageBody {

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
            throw new MQMessageRuleException("批量更新商品MQ参数为空");
        }
    }
}
