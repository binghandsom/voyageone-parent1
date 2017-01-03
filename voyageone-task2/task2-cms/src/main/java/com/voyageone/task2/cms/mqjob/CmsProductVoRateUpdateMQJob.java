package com.voyageone.task2.cms.mqjob;

import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchRefreshRetailPriceMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.ProductVoRateUpdateMQMessageBody;
import com.voyageone.task2.cms.service.product.CmsProductVoRateUpdateService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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
    private CmsProductVoRateUpdateService cmsProductVoRateUpdateService;

    @Override
    public void onStartup(ProductVoRateUpdateMQMessageBody messageBody) throws Exception {
        Map<String, Object> params = messageBody.getParams();
        if (params == null || params.size() <= 0) {
            this.cmsLog(messageBody, OperationLog_Type.parameterException, "Product VoRate Update MQ参数为空");
            return;
        }
        cmsProductVoRateUpdateService.onStartup(params);
    }
}
