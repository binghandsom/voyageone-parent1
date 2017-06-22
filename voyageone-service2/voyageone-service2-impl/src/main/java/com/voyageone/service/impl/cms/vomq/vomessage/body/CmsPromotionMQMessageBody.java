package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.namesub.IMQMessageSubBeanName;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import java.util.List;

/**
 * Created by james on 2017/4/28.
 * 刷新平台上的活动价格
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_PROMOTION)
public class CmsPromotionMQMessageBody extends BaseMQMessageBody implements IMQMessageSubBeanName {

    private Integer promotionId;

    private List<String> numIidList;

    private Long triggerTime;

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public List<String> getNumIidList() {
        return numIidList;
    }

    public void setNumIidList(List<String> numIidList) {
        this.numIidList = numIidList;
    }

    public Long getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Long triggerTime) {
        this.triggerTime = triggerTime;
    }

    @Override
    public String getSubBeanName() {
        return getChannelId();
    }

    @Override
    public void check() throws MQMessageRuleException {
        if(promotionId == null || promotionId == 0){
            throw new MQMessageRuleException("promotionId为空");
        }
    }
}
