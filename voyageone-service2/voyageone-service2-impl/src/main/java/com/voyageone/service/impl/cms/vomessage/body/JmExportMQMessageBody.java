package com.voyageone.service.impl.cms.vomessage.body;

import com.voyageone.common.mq.config.VOMQQueue;
import com.voyageone.common.mq.exception.MQMessageRuleException;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.impl.com.mq.BaseMQMessageBody;

/**
 * Created by dell on 2016/12/20.
 * 聚美导出消息类
 */
@VOMQQueue(queues= MqRoutingKey.CMS_BATCH_JmBtPromotionExportTask)
public class JmExportMQMessageBody extends BaseMQMessageBody {
    public int getJmBtPromotionExportTaskId() {
        return jmBtPromotionExportTaskId;
    }

    public void setJmBtPromotionExportTaskId(int jmBtPromotionExportTaskId) {
        this.jmBtPromotionExportTaskId = jmBtPromotionExportTaskId;
    }
    //表 Jm_Bt_Promotion_ExportTask
     int jmBtPromotionExportTaskId;

    @Override
    public void check() throws MQMessageRuleException {
        if (getJmBtPromotionExportTaskId() == 0) {
            throw new MQMessageRuleException("jmBtPromotionExportTaskId不能等于0");
        }
    }
}
