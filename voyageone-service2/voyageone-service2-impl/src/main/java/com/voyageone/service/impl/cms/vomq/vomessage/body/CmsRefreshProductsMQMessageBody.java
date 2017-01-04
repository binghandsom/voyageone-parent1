package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

/**
 * Created by james on 2017/1/4.
 */
@VOMQQueue(value =  CmsMqRoutingKey.CMS_TASK_REFRESH_PRODUCTS)
public class CmsRefreshProductsMQMessageBody extends BaseMQMessageBody  {

    Integer taskId;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if(taskId == null){
            throw new MQMessageRuleException("参数不对");
        }
    }
}
