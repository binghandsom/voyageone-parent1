package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import java.util.List;

/**
 * Created by james on 2017/1/16.
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_TM_TEJIABAO_DEL)
public class CmsTeJiaBaoDelMQMessageBody extends BaseMQMessageBody {

    private String channelId;
    private Long tejiabaoId;
    private Integer cartId;
    private List<String> numIId;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Long getTejiabaoId() {
        return tejiabaoId;
    }

    public void setTejiabaoId(Long tejiabaoId) {
        this.tejiabaoId = tejiabaoId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public List<String> getNumIId() {
        return numIId;
    }

    public void setNumIId(List<String> numIId) {
        this.numIId = numIId;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if(StringUtil.isEmpty(channelId) || tejiabaoId == null || cartId == null || ListUtils.isNull(numIId)){
            throw new MQMessageRuleException("参数不对");
        }
    }
}
