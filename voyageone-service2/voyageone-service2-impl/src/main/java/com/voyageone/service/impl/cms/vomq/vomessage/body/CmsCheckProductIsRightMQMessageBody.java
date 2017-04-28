package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.namesub.IMQMessageSubBeanName;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

/**
 * Created by james on 2017/3/9.
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_CHECK_PRODUCT_IS_RIGHT)
public class CmsCheckProductIsRightMQMessageBody extends BaseMQMessageBody implements IMQMessageSubBeanName {

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public void check() throws MQMessageRuleException {
    }

    @Override
    public String getSubBeanName() {
        return getChannelId();
    }
}
