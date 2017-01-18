package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.product.CmsProductPriceUpdateService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchRefreshRetailPriceMQMessageBody;

import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 高级检索-重新计算指导价Job消息实体
 *
 * @Author rex
 * @Create 2016-12-30 16:17
 */
@Service
@RabbitListener()
public class CmsAdvSearchRefreshRetailPriceMQJob extends TBaseMQCmsService<AdvSearchRefreshRetailPriceMQMessageBody> {

    @Autowired
    private CmsProductPriceUpdateService cmsProductPriceUpdateService;

    @Override
    public void onStartup(AdvSearchRefreshRetailPriceMQMessageBody messageBody) throws Exception {

        try {
            List<Map<String, String>> failList = cmsProductPriceUpdateService.updateProductRetailPrice(messageBody);
            if (CollectionUtils.isNotEmpty(failList)) {
                cmsSuccessIncludeFailLog(messageBody, JacksonUtil.bean2Json(failList));
            }
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                cmsBusinessExLog(messageBody, e.getMessage());
            } else {
                cmsLog(messageBody, OperationLog_Type.unknownException, e.getMessage());
            }
        }
    }
}
