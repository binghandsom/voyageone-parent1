package com.voyageone.task2.cms.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.task2.base.BaseMQCmsService;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author aooer 2016/4/18.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
@RabbitListener(queues = "voyageone_mq_error_handle_testing")
public class AnnotationMQService extends BaseMQCmsService {

//    static long start=System.currentTimeMillis();

    @Override
    public boolean isRunnable() {
        return true;
    }

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
//        System.out.println("间隔时间："+(System.currentTimeMillis()-start));
//        start=System.currentTimeMillis();
//        System.out.println(JacksonUtil.bean2Json(taskControlList));
        int id = Integer.parseInt((String)messageMap.get("id"));
        //Thread.sleep(id%2*1000);
        System.out.println(id + " " + System.currentTimeMillis());

        TimeUnit.SECONDS.sleep(3);

        if (id == 9) {
            System.out.println(id + " AAA " + System.currentTimeMillis());
            //TimeUnit.SECONDS.sleep(300);
            throw new RuntimeException("AAA9");
        }

        System.out.println(id + " OK " + System.currentTimeMillis());
    }

}
