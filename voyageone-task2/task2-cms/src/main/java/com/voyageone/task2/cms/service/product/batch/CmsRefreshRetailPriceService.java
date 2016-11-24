package com.voyageone.task2.cms.service.product.batch;

import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by james on 2016/11/24.
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_TASK_AdvSearch_RefreshRetailPriceServiceJob)
public class CmsRefreshRetailPriceService extends BaseMQCmsService {
    @Autowired
    private CmsRefreshRetailPriceTask refreshRetailPriceService;

    @Override
    protected void onStartup(Map<String, Object> messageMap) throws Exception {
        String serviceName = (String) messageMap.get("_taskName");
        if ("refreshRetailPrice".equals(serviceName)) {
            refreshRetailPriceService.onStartup(messageMap);
        }
    }
}
