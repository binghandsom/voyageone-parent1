package com.voyageone.task2.cms.service;

import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by james on 2016/12/30.
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_CmsBatchRefreshMainCategoryJob)
public class CmsRefreshProductCategoryMQService extends BaseMQCmsService  {
    @Override
    protected void onStartup(Map<String, Object> messageMap) throws Exception {

    }
}
