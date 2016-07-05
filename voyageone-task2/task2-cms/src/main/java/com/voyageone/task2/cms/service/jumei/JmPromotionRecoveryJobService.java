package com.voyageone.task2.cms.service.jumei;

/**
 * Created by dell on 2016/7/4.
 */

import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
//@RabbitListener(queues = MqRoutingKey.CMS_BATCH_JmPromotionRecovery)
public class JmPromotionRecoveryJobService {


}
