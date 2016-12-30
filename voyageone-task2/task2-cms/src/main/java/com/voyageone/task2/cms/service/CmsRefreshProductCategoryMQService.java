package com.voyageone.task2.cms.service;

import com.voyageone.common.util.CommonUtil;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by james on 2016/12/30.
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_CmsBatchRefreshMainCategoryJob)
public class CmsRefreshProductCategoryMQService extends BaseMQCmsService  {
    @Override
    protected void onStartup(Map<String, Object> messageMap) throws Exception {
//        requestMap.put("userName",userInfo.getUserName());
//        requestMap.put("channelId",userInfo.getSelChannelId());
//        requestMap.put("prodIds",codes)

    }
}
