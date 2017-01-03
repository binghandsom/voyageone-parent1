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
@VOMQQueue(value = CmsMqRoutingKey.CMS_FEED_EXPORT)
public class FeedExportMQMessageBody extends BaseMQMessageBody {

    // cms_bt_export_task ID
    private int feedExportTaskId;

    public int getFeedExportTaskId() {
        return feedExportTaskId;
    }

    public void setFeedExportTaskId(int feedExportTaskId) {
        this.feedExportTaskId = feedExportTaskId;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (feedExportTaskId == 0) {
            throw new MQMessageRuleException("feedExportTaskId不能等于0");
        }
        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("sender(发送者)不能为空");
        }
    }
}
