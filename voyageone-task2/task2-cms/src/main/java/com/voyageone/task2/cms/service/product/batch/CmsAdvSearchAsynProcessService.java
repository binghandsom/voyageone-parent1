package com.voyageone.task2.cms.service.product.batch;

import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 高级检索业务的批量处理相关业务将来都要移到这里，都采用异步处理
 *
 * @author jiangjusheng on 2016/08/24
 * @version 2.0.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_TASK_AdvSearch_AsynProcessJob)
public class CmsAdvSearchAsynProcessService extends BaseMQCmsService {



    @Override
    public String getTaskName() {
        return null;
    }

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {

    }

}
