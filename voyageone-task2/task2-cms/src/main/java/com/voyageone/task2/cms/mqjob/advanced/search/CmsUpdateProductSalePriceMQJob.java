package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.service.impl.cms.vomq.vomessage.body.UpdateProductSalePriceMQMessageBody;
import com.voyageone.service.impl.cms.vomqjobservice.CmsUpdateProductSalePriceService;

import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
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
        cmsUpdateProductSalePriceService.process(messageBody);
    }
}