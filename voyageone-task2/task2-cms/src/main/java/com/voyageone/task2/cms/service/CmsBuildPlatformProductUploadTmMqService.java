package com.voyageone.task2.cms.service;

import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 天猫平台产品上新服务
 * Product表中产品不存在就向天猫平台新增商品，否则就更新商品
 *
 * @author desmond on 2016/5/11.
 * @version 2.0.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_PlatformProductUploadTmJob)
public class CmsBuildPlatformProductUploadTmMqService extends BaseMQCmsService {

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
    }

}
