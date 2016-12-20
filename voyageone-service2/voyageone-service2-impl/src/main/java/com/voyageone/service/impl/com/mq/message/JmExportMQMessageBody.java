package com.voyageone.service.impl.com.mq.message;

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

    public int jmBtPromotionExportTaskId;

    @Override
    public void check() throws MQMessageRuleException {
        if (getJmBtPromotionExportTaskId() == 0) {
            // votodo 错误码定义 待实现
            throw new MQMessageRuleException("1001", "jmBtPromotionExportTaskId不能等于0");
        }
    }
}
