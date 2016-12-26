package com.voyageone.service.impl.cms.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.service.impl.cms.vomessage.CmsMqRoutingKey;
import com.voyageone.service.impl.cms.vomessage.EnumCmsMqRoutingKey;

/**
 * Created by dell on 2016/12/20.
 * 聚美导出消息类
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_JM_PROMOTION_EXPORT)
public class JmExportMQMessageBody extends BaseMQMessageBody {

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
