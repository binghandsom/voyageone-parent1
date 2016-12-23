package com.voyageone.service.impl.cms.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;

/**
 * Created by dell on 2016/12/20.
 * 聚美导出消息类
 */
@VOMQQueue(MqRoutingKey.CMS_BATCH_JmBtPromotionExportTask)
public class JmExportMQMessageBody extends BaseMQMessageBody {

    //表 Jm_Bt_Promotion_ExportTask
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
