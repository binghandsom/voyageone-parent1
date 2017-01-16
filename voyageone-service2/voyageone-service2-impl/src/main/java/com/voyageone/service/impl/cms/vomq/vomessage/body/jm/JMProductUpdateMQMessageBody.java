package com.voyageone.service.impl.cms.vomq.vomessage.body.jm;

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
@VOMQQueue(value = CmsMqRoutingKey.CMS_JM_PRODUCT_UPDATE)
public class JMProductUpdateMQMessageBody extends BaseMQMessageBody {

    // 表cms_bt_jm_promotion的id
    private int cmsBtJmPromotionId;

    public int getCmsBtJmPromotionId() {
        return cmsBtJmPromotionId;
    }

    public void setCmsBtJmPromotionId(int cmsBtJmPromotionId) {
        this.cmsBtJmPromotionId = cmsBtJmPromotionId;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (cmsBtJmPromotionId == 0) {
            throw new MQMessageRuleException("cmsBtJmPromotionId不能等于0");
        }
        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("sender(发送者)不能为空");
        }
    }
}
