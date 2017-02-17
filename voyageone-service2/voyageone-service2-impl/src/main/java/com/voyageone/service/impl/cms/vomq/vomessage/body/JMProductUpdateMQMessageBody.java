package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

/**
 * JMProductUpdateMQMessageBody   聚美平台上传更新
 *
 * @author peitao 2016/12/30.
 * @version 2.0.0
 * @since 2.0.0
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_JM_PROMOTION_PRODUCT_UPDATE)
public class JMProductUpdateMQMessageBody extends BaseMQMessageBody {

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
            throw new MQMessageRuleException("聚美活动-平台上传更新MQ发送异常, 参数cmsBtJmPromotionId为空或者0.");
        }
        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("聚美活动-平台上传更新MQ发送异常, 发送者为空.");
        }
    }
}
