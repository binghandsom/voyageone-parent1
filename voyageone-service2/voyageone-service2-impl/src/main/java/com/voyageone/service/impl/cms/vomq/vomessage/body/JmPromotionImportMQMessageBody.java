package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

/**
 * JmExportMQMessageBody   聚美导入Job 消息体类
 *
 * @author peitao 2016/12/30.
 * @version 2.0.0
 * @since 2.0.0
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_JM_PROMOTION_IMPORT)
public class JmPromotionImportMQMessageBody extends BaseMQMessageBody {

    // 是cms_bt_jm_promotion_import_task的id
    private Integer jmBtPromotionImportTaskId;

    public Integer getJmBtPromotionImportTaskId() {
        return jmBtPromotionImportTaskId;
    }

    public void setJmBtPromotionImportTaskId(Integer jmBtPromotionImportTaskId) {
        this.jmBtPromotionImportTaskId = jmBtPromotionImportTaskId;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (jmBtPromotionImportTaskId == null || jmBtPromotionImportTaskId == 0) {
            throw new MQMessageRuleException("聚美活动-活动商品文件导入MQ发送异常, 参数jmBtPromotionImportTaskId为空或者0.");
        }
        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("聚美活动-活动商品文件导入MQ发送异常, 发送者为空.");
        }
    }
}
