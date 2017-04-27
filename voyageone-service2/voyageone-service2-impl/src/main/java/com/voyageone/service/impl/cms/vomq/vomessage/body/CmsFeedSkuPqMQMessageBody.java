package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.namesub.IMQMessageSubBeanName;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;

import java.util.List;

/**
 * @description VMS的价格和库存改变时更新CMS=》feedInfo信息
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_FEED_SKU_PQ_MQ_JOB)
public class CmsFeedSkuPqMQMessageBody extends BaseMQMessageBody implements IMQMessageSubBeanName {

    private List<CmsBtFeedInfoModel_Sku> skuList;

    public List<CmsBtFeedInfoModel_Sku> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<CmsBtFeedInfoModel_Sku> skuList) {
        this.skuList = skuList;
    }


    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtil.isEmpty(super.getChannelId())) {
            throw new MQMessageRuleException("缺少参数：channelId");
        }

        if (ListUtils.isNull(skuList)) {
            throw new MQMessageRuleException("缺少参数：skuInfo");
        }
    }

    @Override
    public String getSubBeanName() {
        return getChannelId();
    }
}
