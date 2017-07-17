package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsBtProductUpdatePriceMQMessageBody;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Created by dell on 2017/7/17.
 */
@Service
@RabbitListener()
public class CmsBtProductUpdatePriceMQJob extends TBaseMQCmsService<CmsBtProductUpdatePriceMQMessageBody> {

    @Override
    public void onStartup(CmsBtProductUpdatePriceMQMessageBody messageBody) throws Exception {


    }
}
