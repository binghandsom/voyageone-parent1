package com.voyageone.task2.cms.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author aooer 2016/4/18.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
@RabbitListener(queues = "voyageone_mq_error_handle_testing")
public class AnnotationMQService extends BaseMQCmsService {

    static long start=System.currentTimeMillis();

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        System.out.println("间隔时间："+(System.currentTimeMillis()-start));
        start=System.currentTimeMillis();
        System.out.println(JacksonUtil.bean2Json(taskControlList));
    }

}
