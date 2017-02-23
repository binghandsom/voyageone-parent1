package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.ProductVoRateUpdateMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Product VoRate Update Job
 *
 * @Author rex
 * @Create 2016-12-30 18:10
 */
@Service
@RabbitListener()
public class CmsProductVoRateUpdateMQJob extends TBaseMQCmsService<ProductVoRateUpdateMQMessageBody> {

    @Autowired
    private ProductService productService;

    @Override
    public void onStartup(ProductVoRateUpdateMQMessageBody messageBody) throws Exception {

        super.count = messageBody.getCodeList().size();
        List<CmsBtOperationLogModel_Msg> failList = productService.updateProductVoRate(messageBody);
        if (failList.size() > 0) {
            String comment = String.format("处理总件数(%s), 处理失败件数(%s)", messageBody.getCodeList().size(), failList.size());
            cmsSuccessIncludeFailLog(messageBody, comment, failList);
        }
    }
}
