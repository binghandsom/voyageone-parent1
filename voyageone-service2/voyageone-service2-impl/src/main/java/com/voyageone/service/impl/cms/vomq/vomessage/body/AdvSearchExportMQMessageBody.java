package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import org.apache.commons.collections.CollectionUtils;

import java.util.Map;

/**
 * 高级检索文件导出Job消息实体
 *
 * @Author rex
 * @Create 2016-12-30 14:18
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_ADV_SEARCH_EXPORT)
public class AdvSearchExportMQMessageBody extends BaseMQMessageBody {

    private int cmsBtExportTaskId;
    private Map<String, Object> searchValue;

    public int getCmsBtExportTaskId() {
        return cmsBtExportTaskId;
    }

    public void setCmsBtExportTaskId(int cmsBtExportTaskId) {
        this.cmsBtExportTaskId = cmsBtExportTaskId;
    }

    public Map<String, Object> getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(Map<String, Object> searchValue) {
        this.searchValue = searchValue;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (cmsBtExportTaskId == 0) {
            throw new MQMessageRuleException("高级检索导出MQ发送异常,参数cmsBtExportTaskId为空.");
        }
        if (searchValue == null || searchValue.size() < 1) {
            throw new MQMessageRuleException("高级检索导出MQ发送异常,缺少参数.");
        }
        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("sender(发送者)不能为空");
        }
    }
}
