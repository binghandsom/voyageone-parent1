package com.voyageone.task2.cms.mqjob;

import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchExportMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.BatchUpdateProductMQMessageBody;
import com.voyageone.task2.cms.service.product.batch.CmsBacthUpdateTask;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 批量更新商品Job
 *
 * @Author rex
 * @Create 2017-01-03 14:03
 */
@Service
@RabbitListener()
public class CmsBatchUpdateProductMQJob extends TBaseMQCmsService<BatchUpdateProductMQMessageBody> {

    @Autowired
    private CmsBacthUpdateTask bacthUpdateService;

    @Override
    public void onStartup(BatchUpdateProductMQMessageBody messageBody) throws Exception {
        bacthUpdateService.onStartup(messageBody.getParams());
    }
}
