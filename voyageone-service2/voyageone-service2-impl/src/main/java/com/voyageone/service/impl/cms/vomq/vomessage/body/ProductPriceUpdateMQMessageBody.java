package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 更新product和group的价格Job
 *
 * @Author rex
 * @Create 2017-01-03 16:03
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_PRODUCT_PRICE_UPDATE)
public class ProductPriceUpdateMQMessageBody extends BaseMQMessageBody {

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
            throw new MQMessageRuleException("product和group的价格刷新(VOCmsProductPriceUpdateQueue)参数为空");
        }
        if (StringUtils.isBlank(getSender())) {
            throw new MQMessageRuleException("sender(发送者)不能为空");
        }
    }
}
