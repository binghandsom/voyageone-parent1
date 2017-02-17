package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

/**
 * Created by james on 2017/1/3.
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_UPLOAD_SHELVES_IMAGE)
public class CmsShelvesImageUploadMQMessageBody extends BaseMQMessageBody {

    private Integer shelvesId;

    public Integer getShelvesId() {
        return shelvesId;
    }

    public void setShelvesId(Integer shelvesId) {
        this.shelvesId = shelvesId;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if(shelvesId == null || shelvesId == 0){
            throw new MQMessageRuleException("货架管理-货架监控MQ发送异常, 参数shelvesId为空或者0.");
        }
    }
}
