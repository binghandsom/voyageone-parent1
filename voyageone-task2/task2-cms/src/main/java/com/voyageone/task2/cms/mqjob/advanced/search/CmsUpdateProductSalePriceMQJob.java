package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.service.impl.cms.vomq.vomessage.body.UpdateProductSalePriceMQMessageBody;
import com.voyageone.service.impl.cms.vomqjobservice.CmsUpdateProductSalePriceService;

import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        super.count = messageBody.getProductCodes().size();
        List<CmsBtOperationLogModel_Msg> failList = cmsUpdateProductSalePriceService.process(messageBody);
        if (failList.size() > 0) {
            String comment = String.format("处理总件数(%s), 处理失败件数(%s)", messageBody.getProductCodes().size(), failList.size());
            cmsSuccessIncludeFailLog(messageBody, comment, failList);
        }
    }
}
