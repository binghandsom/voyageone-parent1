package com.voyageone.task2.cms.mqjob;

import com.voyageone.service.impl.cms.vomq.vomessage.body.ProductVoRateUpdateMQMessageBody;
import com.voyageone.service.impl.cms.product.CmsProductVoRateUpdateService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
        Map<String, Object> params = new HashMap<>();
        params.put("channelId", messageBody.getChannelId());
        params.put("creater", messageBody.getCreater());
        params.put("codeList", messageBody.getCodeList());
        params.put("voRate", messageBody.getVoRate());
        cmsProductVoRateUpdateService.updateProductVoRate(params);
    }
}
