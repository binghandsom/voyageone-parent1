package com.voyageone.service.impl.cms.vomq.vomessage.body.usa;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * Created by Charis on 2017/7/25.
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_USA_CATEGORY_RECEIVE)
public class CmsCategoryReceiveMQMessageBody extends BaseMQMessageBody {

    private String channelId;
    private String cartId;
    private List<String> fullCatIds;

    @Override
    public String getChannelId() {
        return channelId;
    }

    @Override
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public List<String> getFullCatIds() {
        return fullCatIds;
    }

    public void setFullCatIds(List<String> fullCatIds) {
        this.fullCatIds = fullCatIds;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isEmpty(channelId)) {
            throw new MQMessageRuleException("获取美国类目信息MQ发送异常, 参数channelId为空.");
        }
        if (StringUtils.isEmpty(cartId)) {
            throw new MQMessageRuleException("获取美国类目信息MQ发送异常, 参数cartId为空.");
        }
        if (CollectionUtils.isEmpty(fullCatIds)) {
            throw new MQMessageRuleException("获取美国类目信息MQ发送异常, 参数fullCatIds为空.");
        }
    }
}
