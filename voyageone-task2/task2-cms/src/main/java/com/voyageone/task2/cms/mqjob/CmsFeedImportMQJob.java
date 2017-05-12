package com.voyageone.task2.cms.mqjob;

import com.voyageone.common.configs.Shops;
import com.voyageone.common.util.ListUtils;
import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.service.impl.cms.feed.FeedToCmsService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsFeedImportMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by james on 2017/3/9.
 */
@Service
@VOSubRabbitListener
public class CmsFeedImportMQJob extends TBaseMQCmsSubService<CmsFeedImportMQMessageBody> {

    @Autowired
    FeedToCmsService feedToCmsService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void onStartup(CmsFeedImportMQMessageBody messageBody) throws Exception {
        messageBody.check();
        $info(String.format("feed导入 channelId=%s feedModel条数=%d", messageBody.getChannelId(), messageBody.getCmsBtFeedInfoModels().size()));
        Map<String, List<CmsBtFeedInfoModel>> response = feedToCmsService.updateProduct(messageBody.getChannelId(), messageBody.getCmsBtFeedInfoModels(), messageBody.getSender());
        if(!ListUtils.isNull(response.get("fail"))){
            List<CmsBtFeedInfoModel> fails = response.get("fail");
            List<CmsBtOperationLogModel_Msg> msg = new ArrayList<>(fails.size());
            fails.forEach(feed->{
                CmsBtOperationLogModel_Msg item = new CmsBtOperationLogModel_Msg();
                item.setSkuCode(feed.getCode());
                item.setMsg(feed.getUpdMessage());
                msg.add(item);
            });
            cmsSuccessIncludeFailLog(messageBody, "feed导入失败", msg);
        }
    }

    @Override
    public String[] getAllSubBeanName() {
        List<String> orderChannelIdList = Shops.getShopList().stream().map(item -> item.getCart_id()).distinct().collect(Collectors.toList());
        String[] strings = new String[orderChannelIdList.size()];
        orderChannelIdList.toArray(strings);
        return strings;
    }
}
