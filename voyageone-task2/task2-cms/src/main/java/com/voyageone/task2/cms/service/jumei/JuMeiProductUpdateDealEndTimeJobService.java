package com.voyageone.task2.cms.service.jumei;

import com.voyageone.service.impl.cms.jumei.platefrom.JuMeiDealService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * JuMei Product UpdateDeal EndTimeJob Service
 *
 * @author peitao 2016/4/8.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class JuMeiProductUpdateDealEndTimeJobService extends BaseMQCmsService {

    @Autowired
    private JuMeiDealService service;

    @RabbitListener(queues = MqRoutingKey.CMS_BATCH_JuMeiProductUpdateDealEndTimeJob)
    protected void onMessage(Message message) {
        super.onMessage(message);
    }

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        int id = (int) Double.parseDouble(messageMap.get("id").toString());
        service.updateDealEndTime(id);
    }
}
