package com.voyageone.task2.cms.mqjob;

import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchConfirmRetailPriceMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchExportMQMessageBody;
import com.voyageone.task2.cms.service.product.batch.CmsAdvSearchAsynProcessService;
import com.voyageone.task2.cms.service.product.batch.CmsConfirmRetailPriceTask;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 高级检索-确认指定价更新Job
 *
 * @Author dell
 * @Create 2016-12-30 17:18
 */
@RabbitListener()
public class CmsAdvSearchConfirmRetailPriceMQJob extends TBaseMQCmsService<AdvSearchConfirmRetailPriceMQMessageBody> {

    @Autowired
    private CmsConfirmRetailPriceTask confirmRetailPriceService;

    @Override
    public void onStartup(AdvSearchConfirmRetailPriceMQMessageBody messageBody) throws Exception {

        if (messageBody.getParams() == null || messageBody.getParams().size() <= 0) {
            this.cmsLog(messageBody, OperationLog_Type.parameterException, "高级检索-确认指定价变更MQ参数为空");
            return;
        }
        confirmRetailPriceService.onStartup(messageBody.getParams());

    }
}
