package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

/**
 * JmExportMQMessageBody   聚美导出Job 消息体类
 *
 * @author peitao 2016/12/26.
 * @version 2.0.0
 * @since 2.0.0
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_JM_PROMOTION_EXPORT)
public class JmExportMQMessageBody extends BaseMQMessageBody {
//后缀MQMessageBody
    // 是cms_bt_jm_promotion_export_task的id
    private int jmBtPromotionExportTaskId;

    public int getJmBtPromotionExportTaskId() {
        return jmBtPromotionExportTaskId;
    }

    public void setJmBtPromotionExportTaskId(int jmBtPromotionExportTaskId) {
        this.jmBtPromotionExportTaskId = jmBtPromotionExportTaskId;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (jmBtPromotionExportTaskId == 0) {
            throw new MQMessageRuleException("jmBtPromotionExportTaskId不能等于0");
        }
    }
}
