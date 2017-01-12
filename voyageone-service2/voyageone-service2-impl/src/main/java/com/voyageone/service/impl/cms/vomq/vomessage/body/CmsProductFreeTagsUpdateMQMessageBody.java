package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;


/**
 * CmsProductFreeTagsUpdateMQMessageBody    高级搜索-设置自由标签
 *
 * @author peitao 2017/01/12
 * @version 2.0.0
 * @since 2.0.0
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_PPRODUCT_FREE_TAGS_UPDATE)
public class CmsProductFreeTagsUpdateMQMessageBody extends BaseMQMessageBody {
    String channelId;
    boolean isSelAll;


    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }



    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isEmpty(channelId)) {
            throw new MQMessageRuleException("channelId不能为空");
        }
        if (StringUtils.isEmpty(getSender())) {
            throw new MQMessageRuleException("sender(发送者)不能为空");
        }
    }
}
