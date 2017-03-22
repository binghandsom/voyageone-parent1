package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedSkuPqModel;

import java.util.List;

/**
 * @description VMS的价格和库存改变时更新CMS=》feedInfo信息
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_FEED_SKU_PQ_MQ_JOB)
public class CmsFeedSkuPqMQMessageBody extends BaseMQMessageBody {

    String channelId;
    String modifier;
    List<CmsBtFeedSkuPqModel> skuInfo;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public List<CmsBtFeedSkuPqModel> getSkuInfo() {
        return skuInfo;
    }

    public void setSkuInfo(List<CmsBtFeedSkuPqModel> skuInfo) {
        this.skuInfo = skuInfo;
    }


    @Override
    public void check() throws MQMessageRuleException {
        if(StringUtil.isEmpty(channelId)){
            throw new MQMessageRuleException("缺少参数：channelId");
        }

        if( ListUtils.isNull(skuInfo)){
            throw new MQMessageRuleException("缺少参数：skuInfo");
        }
    }
}
