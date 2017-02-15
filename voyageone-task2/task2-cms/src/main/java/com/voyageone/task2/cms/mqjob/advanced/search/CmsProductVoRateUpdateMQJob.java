package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.product.CmsProductVoRateUpdateService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.ProductVoRateUpdateMQMessageBody;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

        try {
            List<Map<String, String>> failList = cmsProductVoRateUpdateService.updateProductVoRate(messageBody);
            if (CollectionUtils.isNotEmpty(failList)) {
                cmsSuccessIncludeFailLog(messageBody, JacksonUtil.bean2Json(failList));
            }
        } catch (Exception e) {
            cmsLog(messageBody, OperationLog_Type.unknownException, e.getMessage());
        }
    }
}