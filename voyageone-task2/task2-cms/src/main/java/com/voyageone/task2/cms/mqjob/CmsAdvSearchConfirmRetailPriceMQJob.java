package com.voyageone.task2.cms.mqjob;

import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchConfirmRetailPriceMQMessageBody;
import com.voyageone.service.impl.cms.vomqjobservice.CmsConfirmRetailPriceService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * 高级检索-确认指定价更新Job
 *
 * @Author dell
 * @Create 2016-12-30 17:18
 */
@RabbitListener()
public class CmsAdvSearchConfirmRetailPriceMQJob extends TBaseMQCmsService<AdvSearchConfirmRetailPriceMQMessageBody> {

    @Autowired
    private CmsConfirmRetailPriceService confirmRetailPriceService;

    @Override
    public void onStartup(AdvSearchConfirmRetailPriceMQMessageBody messageBody) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("productIds", messageBody.getCodeList());
        params.put("cartIds", messageBody.getCartList());
        params.put("_channleId", messageBody.getChannelId());
        params.put("_userName", messageBody.getUserName());

        try {
            confirmRetailPriceService.onStartup(params);
        } catch (Exception e) {
            cmsLog(messageBody, OperationLog_Type.unknownException, e.getMessage());
        }

    }
}
