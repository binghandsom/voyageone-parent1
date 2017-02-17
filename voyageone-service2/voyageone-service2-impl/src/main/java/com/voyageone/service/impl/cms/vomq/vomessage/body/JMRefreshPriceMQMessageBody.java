package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;


/**
 * JMRefreshPriceMQMessageBody   聚美刷新参考价
 *
 * @author peitao 2016/12/30.
 * @version 2.0.0
 * @since 2.0.0
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_JM_PROMOTION_REFRESH_PRICE)
public class JMRefreshPriceMQMessageBody extends BaseMQMessageBody {

    // 表cms_bt_jm_promotion的id
    private Integer cmsBtJmPromotionId;

    public Integer getCmsBtJmPromotionId() {
        return cmsBtJmPromotionId;
    }

    public void setCmsBtJmPromotionId(Integer cmsBtJmPromotionId) {
        this.cmsBtJmPromotionId = cmsBtJmPromotionId;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (cmsBtJmPromotionId == null || cmsBtJmPromotionId == 0) {
            throw new MQMessageRuleException("聚美活动-获取聚美日常销售的参考价MQ发送异常, 参数cmsBtJmPromotionId为空或者0.");
        }
        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("聚美活动-获取聚美日常销售的参考价MQ发送异常, 发送者为空.");
        }
    }
}
