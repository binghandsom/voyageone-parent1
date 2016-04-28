package com.voyageone.task2.cms.service.imagecreate;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.service.impl.cms.imagecreate.AliYunOSSFileService;
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
//@RabbitListener(queues = MqRoutingKey.CMS_BATCH_AliYunOSSJob)
public class AliYunOSSJobService extends BaseMQCmsService {

    @Autowired
    AliYunOSSFileService service;
    private static final Logger LOG = LoggerFactory.getLogger(AliYunOSSJobService.class);
    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {

    }
    @Override
    public SubSystem getSubSystem() {
            return SubSystem.CMS;
    }
    @Override
    public String getTaskName() {
        return "AliYunOSSJobService";
    }
}
