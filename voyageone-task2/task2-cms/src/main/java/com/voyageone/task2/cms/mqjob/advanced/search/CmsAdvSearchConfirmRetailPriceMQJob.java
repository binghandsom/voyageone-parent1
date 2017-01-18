package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchConfirmRetailPriceMQMessageBody;
import com.voyageone.service.impl.cms.vomqjobservice.CmsConfirmRetailPriceService;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 高级检索-确认指定价更新Job
 *
 * @Author dell
 * @Create 2016-12-30 17:18
 */
@Service
@RabbitListener()
public class CmsAdvSearchConfirmRetailPriceMQJob extends TBaseMQCmsService<AdvSearchConfirmRetailPriceMQMessageBody> {

    @Autowired
    private CmsConfirmRetailPriceService confirmRetailPriceService;

    @Override
    public void onStartup(AdvSearchConfirmRetailPriceMQMessageBody messageBody) throws Exception {
        try {
            confirmRetailPriceService.onStartup(messageBody);
        } catch (Exception e) {
            cmsLog(messageBody, OperationLog_Type.unknownException, e.getMessage());
        }

    }
}
