package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.service.impl.cms.prices.PlatformPriceService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.UpdateProductSalePriceMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsSubService;
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
@VOSubRabbitListener
public class CmsUpdateProductSalePriceMQJob extends TBaseMQCmsSubService<UpdateProductSalePriceMQMessageBody> {

    @Autowired
    private PlatformPriceService platformPriceService;

    @Override
    public void onStartup(UpdateProductSalePriceMQMessageBody messageBody) throws Exception {
        super.count = messageBody.getProductCodes().size();
        List<CmsBtOperationLogModel_Msg> failList = platformPriceService.updateProductSalePrice(messageBody);
        if (failList.size() > 0) {
            String comment = String.format("处理总件数(%s), 处理失败件数(%s)", messageBody.getProductCodes().size(), failList.size());
            cmsSuccessIncludeFailLog(messageBody, comment, failList);
        }
    }
}
