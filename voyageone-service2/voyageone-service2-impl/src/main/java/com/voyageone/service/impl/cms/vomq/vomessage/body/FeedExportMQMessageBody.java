package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

/**
 * Feed文件导出Job消息实体
 *
 * @Author rex
 * @Create 2016-12-30 10:38
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_EXPORT_FEED_INFO)
public class FeedExportMQMessageBody extends BaseMQMessageBody {

    private int cmsBtExportTaskId;

    public int getCmsBtExportTaskId() {
        return cmsBtExportTaskId;
    }

    public void setCmsBtExportTaskId(int cmsBtExportTaskId) {
        this.cmsBtExportTaskId = cmsBtExportTaskId;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (cmsBtExportTaskId == 0) {
            throw new MQMessageRuleException("Feed文件导出MQ发送异常,参数cmsBtExportTaskId为空.");
        }
        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("sender(发送者)不能为空");
        }
    }
}
