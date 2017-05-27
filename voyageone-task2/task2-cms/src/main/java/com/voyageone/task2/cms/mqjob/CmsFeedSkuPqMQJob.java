package com.voyageone.task2.cms.mqjob;


import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.service.impl.cms.feed.FeedToCmsService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsFeedSkuPqMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @description VMS的价格和库存改变时请求MQ
 *              更新CMS=》feedInfo信息
 *              该MQ只更新    priceNet:美金成本价
 *                           priceClientRetail:美金指导价
 *                           priceClientMsrp:美金专柜价
 *                           qty:库存
 *                           isSale:是否在卖
 * @author  piao
 */
@Service
@VOSubRabbitListener
public class CmsFeedSkuPqMQJob extends TBaseMQCmsSubService<CmsFeedSkuPqMQMessageBody> {

    @Autowired
    FeedToCmsService feedToCmsService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void onStartup(CmsFeedSkuPqMQMessageBody messageBody) throws Exception {

        Map<String, List<CmsBtOperationLogModel_Msg>> result = feedToCmsService.updateFeedSkuPrice(messageBody.getChannelId(), messageBody.getSkuList(), messageBody.getSender());

        List<CmsBtOperationLogModel_Msg> success = result.get("success"),
                failed = result.get("failed");

        /** 写入错误信息日志 */
        if (result.get("failed").size() > 0) {
            String comment = String.format("处理总数(%s), 处理成功数(%s),处理失败数(%s)",
                    messageBody.getSkuList().size(),
                    success.size(),
                    failed.size());

            cmsSuccessIncludeFailLog(messageBody, comment, failed);
        }

    }

    @Override
    public String[] getAllSubBeanName() {
        List<String> orderChannelIdList = Shops.getShopList().stream().map(ShopBean::getOrder_channel_id).distinct().collect(Collectors.toList());
        String[] strings = new String[orderChannelIdList.size()];
        orderChannelIdList.toArray(strings);
        return strings;
    }
}
