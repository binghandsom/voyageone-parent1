package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.namesub.IMQMessageSubBeanName;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

/**
 * Created by james on 2017/1/4.
 */
@VOMQQueue(value =  CmsMqRoutingKey.CMS_REFRESH_PRODUCT_PLATFORM_FIELDS)
public class CmsRefreshProductsMQMessageBody extends BaseMQMessageBody  implements IMQMessageSubBeanName {

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
            throw new MQMessageRuleException("平台属性默认设置-强制对某商品或某类目进行属性的重新计算赋值和上新MQ发送异常, 参数taskId为空.");
        }
    }

    @Override
    public String getSubBeanName() {
        return getChannelId();
    }
}
