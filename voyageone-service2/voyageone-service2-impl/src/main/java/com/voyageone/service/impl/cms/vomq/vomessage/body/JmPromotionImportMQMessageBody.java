package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

/**
 * JmExportMQMessageBody   聚美导入Job 消息体类
 *
 * @author peitao 2016/12/26.
 * @version 2.0.0
 * @since 2.0.0
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_JM_PROMOTION_IMPORT)
public class JmPromotionImportMQMessageBody extends BaseMQMessageBody {

    // 是cms_bt_jm_promotion_import_task的id
    private int jmBtPromotionImportTaskId;

    public int getJmBtPromotionImportTaskId() {
        return jmBtPromotionImportTaskId;
    }

    public void setJmBtPromotionImportTaskId(int jmBtPromotionImportTaskId) {
        this.jmBtPromotionImportTaskId = jmBtPromotionImportTaskId;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (jmBtPromotionImportTaskId == 0) {
            throw new MQMessageRuleException("jmBtPromotionImportTaskId");
        }
    }
}
