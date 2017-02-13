package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
/**
 * PromotionProductStockSyncMQMessageBody   库存同步
 *
 * @author peitao 2017/01/03.
 * @version 2.0.0
 * @since 2.0.0
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_JM_PROMOTION_PRODUCT_STOCK_SYNC)
public class JmPromotionProductStockSyncMQMessageBody extends BaseMQMessageBody {

    @Override
    public void check() throws MQMessageRuleException {

        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("sender(发送者)不能为空");
        }
    }

}
