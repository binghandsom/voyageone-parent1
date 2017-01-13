package com.voyageone.task2.cms.mqjob;

import com.voyageone.service.impl.cms.vomq.vomessage.body.JmPromotionImportMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.UpdateProductSalePriceMQMessageBody;
import com.voyageone.service.impl.cms.vomqjobservice.CmsUpdateProductSalePriceService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 修改商品saleprice Job
 *
 * @Author dell
 * @Create 2017-01-09 14:08
 */
@Service
@RabbitListener()
public class CmsUpdateProductSalePriceMQJob extends TBaseMQCmsService<UpdateProductSalePriceMQMessageBody> {

    @Autowired
    private CmsUpdateProductSalePriceService cmsUpdateProductSalePriceService;

    @Override
    public void onStartup(UpdateProductSalePriceMQMessageBody messageBody) throws Exception {
        long threadNo =  Thread.currentThread().getId();
        cmsUpdateProductSalePriceService.process(messageBody);
        $info("threadNo="+threadNo+" finish");
    }
}
