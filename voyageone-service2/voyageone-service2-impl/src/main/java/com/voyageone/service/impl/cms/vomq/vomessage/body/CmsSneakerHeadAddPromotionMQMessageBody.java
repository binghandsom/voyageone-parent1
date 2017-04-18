package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

/**
 * Created by james on 2017/3/28.
 *
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_SNEAKERHEAD_ADD_PROMOTION)
public class CmsSneakerHeadAddPromotionMQMessageBody extends BaseMQMessageBody {
    private Integer promotionId;

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if(promotionId == null || promotionId == 0){
            throw new MQMessageRuleException("promotionId不能为空");
        }
    }
}
