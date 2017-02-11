package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import java.util.List;

/**
 * 把聚美活动价同步到商城价
 * Created by james on 2017/1/23.
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_JM_MALL_PROMOTION_PRICE_SYNC)
public class CmsJmMallPromotionPriceSyncMQMessageBody extends BaseMQMessageBody {

    private String  channelId;

    private Integer jmPromotionId;

    private List<String> productCodes;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getJmPromotionId() {
        return jmPromotionId;
    }

    public void setJmPromotionId(Integer jmPromotionId) {
        this.jmPromotionId = jmPromotionId;
    }

    public List<String> getProductCodes() {
        return productCodes;
    }

    public void setProductCodes(List<String> productCodes) {
        this.productCodes = productCodes;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if(StringUtil.isEmpty(this.channelId) || this.jmPromotionId == null || this.jmPromotionId <= 0){
            throw new MQMessageRuleException("channelId 或 活动ID 不正确");
        }
    }
}
