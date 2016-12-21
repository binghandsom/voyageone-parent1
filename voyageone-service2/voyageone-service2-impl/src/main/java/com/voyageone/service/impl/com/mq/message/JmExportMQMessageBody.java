package com.voyageone.service.impl.com.mq.message;

import com.voyageone.common.mq.config.VOQueue;
import com.voyageone.common.mq.exception.MQMessageRuleException;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;

/**
 * Created by dell on 2016/12/20.
 * 聚美导出消息类
 */
@VOQueue(queues= MqRoutingKey.CMS_BATCH_JmBtPromotionExportTask)
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
            // votodo 错误码规范定义 待实现
            throw new MQMessageRuleException("6001", "jmBtPromotionExportTaskId不能等于0");
        }
    }
}
