package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

/**
 * Created by james on 2017/1/3.
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_BATCH_CartAddJob)
public class CmsCartAddMQMessageBody extends BaseMQMessageBody {

    private String channelId;
    private Integer cartId;
    private Boolean single;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Boolean getSingle() {
        return single;
    }

    public void setSingle(Boolean single) {
        this.single = single;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if(StringUtil.isEmpty(channelId) || cartId == null){
            throw new MQMessageRuleException("参数不能为空");
        }
    }
}
