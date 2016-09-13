package com.voyageone.task2.cms.service;

import com.voyageone.common.util.MapUtil;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtBrandBlockModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by jonas on 9/12/16.
 *
 * @version 2.6.0
 * @since 2.6.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_TASK_BRANDBLOCKJOB)
public class CmsBrandBlockJobService extends BaseMQCmsService {

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {

        boolean blocking = (boolean) messageMap.get("blocking");
        @SuppressWarnings("unchecked") Map<String, Object> brandBlockMap = (Map<String, Object>) messageMap.get("data");
        CmsBtBrandBlockModel brandBlockModel = MapUtil.toModel(brandBlockMap, CmsBtBrandBlockModel.class);

    }
}
