package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 重设置商品标题消息
 *
 * @Author dell
 * @Create 2017-06-13 11:03
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_RESET_PRODUCT_TITLE)
public class CmsResetProductTitleMQMessageBody extends BaseMQMessageBody {

    /**
     * 渠道ID
     */
    private String channelId;
    /**
     * 产品Code
     */
    private List<String> codes;

    @Override
    public String getChannelId() {
        return channelId;
    }

    @Override
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    @Override
    public void check() throws MQMessageRuleException {

        if (StringUtils.isBlank(channelId)) {
            throw new BusinessException("参数channelId为空");
        }

        if (CollectionUtils.isEmpty(codes)) {
            throw new BusinessException("参数产品Code为空");
        }

        if (StringUtils.isBlank(getSender())) {
            throw new BusinessException("消息发送人为空");
        }
    }
}
