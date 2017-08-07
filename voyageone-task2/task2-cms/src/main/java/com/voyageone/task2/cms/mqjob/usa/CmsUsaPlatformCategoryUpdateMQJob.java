package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsBtProductUpdatePriceMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsUsaPlatformCategoryUpdateMQMessageBody;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Created by dell on 2017/8/7.
 */
@Service
@RabbitListener()
public class CmsUsaPlatformCategoryUpdateMQJob extends TBaseMQCmsService<CmsUsaPlatformCategoryUpdateMQMessageBody> {
    @Override
    public void onStartup(CmsUsaPlatformCategoryUpdateMQMessageBody messageBody) throws Exception {

    }
}
