package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import org.apache.commons.lang.StringUtils;

/**
 * Created by james on 2017/1/3.
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_ADD_PLATFORM_CART)
public class CmsCartAddMQMessageBody extends BaseMQMessageBody {

    private String channelId;
    private Integer cartId;
    // 是否按照一个code一个group来生成数据
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
        if (StringUtils.isBlank(channelId)) {
            throw new MQMessageRuleException("追加一个新cart自动添加product.platforms处理MQ发送异常, 参数channelId为空.");
        }
        if (cartId == null) {
            throw new MQMessageRuleException("追加一个新cart自动添加product.platforms处理MQ发送异常, 参数cartId为空.");
        }
    }
}
