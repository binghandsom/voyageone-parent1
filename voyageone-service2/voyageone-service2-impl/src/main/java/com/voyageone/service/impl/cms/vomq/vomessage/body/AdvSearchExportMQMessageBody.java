package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import java.util.Map;

/**
 * 高级检索文件导出Job消息实体
 *
 * @Author rex
 * @Create 2016-12-30 14:18
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_ADV_SEARCH_EXPORT)
public class AdvSearchExportMQMessageBody extends BaseMQMessageBody {

    // cms_bt_export_task ID
    private int advSearchExportTaskId;
    private Map<String, Object> searchValue;

    public int getAdvSearchExportTaskId() {
        return advSearchExportTaskId;
    }

    public void setAdvSearchExportTaskId(int advSearchExportTaskId) {
        this.advSearchExportTaskId = advSearchExportTaskId;
    }

    public Map<String, Object> getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(Map<String, Object> searchValue) {
        this.searchValue = searchValue;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (advSearchExportTaskId == 0) {
            throw new MQMessageRuleException("advSearchExportTaskId不能等于0");
        }
        if (searchValue == null || searchValue.size() < 1) {
            throw new MQMessageRuleException("高级检索检索条件为空");
        }
        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("sender(发送者)不能为空");
        }
    }
}
