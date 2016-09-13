package com.voyageone.task2.cms.service.feed;

import com.voyageone.service.dao.cms.mongo.CmsBtCAdProductDao;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.mongo.CmsBtCAdProudctModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2016/9/13.
 * @version 2.0.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_FeedExportJob)
public class CaAnalysisService extends BaseMQCmsService {

    @Autowired
    private CmsBtCAdProductDao cmsBtCAdProductDao;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        String channelId = messageMap.get("channelId").toString();
        List<String> sellerSKUs = (List<String>) messageMap.get("sellerSKUs");
        List<CmsBtCAdProudctModel> feedList = cmsBtCAdProductDao.getProduct(channelId, sellerSKUs);
    }


}
