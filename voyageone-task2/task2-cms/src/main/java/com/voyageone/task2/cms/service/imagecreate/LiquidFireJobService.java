package com.voyageone.task2.cms.service.imagecreate;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.service.impl.cms.imagecreate.LiquidFireImageService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.task2.base.BaseMQCmsService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/4/26.
 */
//@Service
//@RabbitListener(queues = MqRoutingKey.CMS_BATCH_LiquidFireJob)
public class LiquidFireJobService extends BaseMQCmsService {
    private static final Logger LOG = LoggerFactory.getLogger(LiquidFireJobService.class);
    @Autowired
    LiquidFireImageService service;
    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {

    }
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }
    @Override
    public String getTaskName() {
      return "LiquidFireJobService";
    }
}
