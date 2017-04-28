package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.namesub.IMQMessageSubBeanName;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

/**
 * Created by james on 2017/3/9.
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_ADD_NEW_GROUP)
public class CmsAddNewGroupMQMessageBody extends BaseMQMessageBody implements IMQMessageSubBeanName {
    //@RequestParam("channelId") String channelId, @RequestParam("cartId") Integer cartId, @RequestParam("code") String productCode, @RequestParam("isSingle") Boolean isSingle

    private Integer cartId;

    private String code;

    private Boolean isSingle = true;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getIsSingle() {
        return isSingle;
    }

    public void setIsingle(Boolean isSingle) {
        isSingle = isSingle;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (cartId == null || cartId < 0) {
            throw new MQMessageRuleException("生成新的group, 参数cartId为空或小于0.");
        }
    }

    @Override
    public String getSubBeanName() {
        return getChannelId();
    }
}
