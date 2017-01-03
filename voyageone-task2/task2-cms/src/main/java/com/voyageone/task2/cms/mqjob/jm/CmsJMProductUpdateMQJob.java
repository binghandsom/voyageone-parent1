package com.voyageone.task2.cms.mqjob.jm;
import com.voyageone.service.impl.cms.jumei2.JuMeiProductPlatform3Service;
import com.voyageone.service.impl.cms.vomq.vomessage.body.jm.JMProductUpdateMQMessageBody;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CmsJMProductUpdateMQJob  聚美平台上传更新
 *
 * @author peitao 2016/12/30.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
@RabbitListener()
public class CmsJMProductUpdateMQJob extends TBaseMQCmsService<JMProductUpdateMQMessageBody> {
    @Autowired
    private JuMeiProductPlatform3Service service;

    @Override
    public void onStartup(JMProductUpdateMQMessageBody messageBody) throws Exception {
        int id = messageBody.getCmsBtJmPromotionId();
        service.updateJmByPromotionId(id);
    }
}
