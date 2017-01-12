package com.voyageone.task2.cms.mqjob;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.vomq.vomessage.body.BatchUpdateProductMQMessageBody;
import com.voyageone.service.impl.cms.vomqjobservice.CmsBacthUpdateService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 批量更新商品Job
 *
 * @Author rex
 * @Create 2017-01-03 14:03
 */
@Service
@RabbitListener()
public class CmsBatchUpdateProductMQJob extends TBaseMQCmsService<BatchUpdateProductMQMessageBody> {

    @Autowired
    private CmsBacthUpdateService bacthUpdateService;

    @Override
    public void onStartup(BatchUpdateProductMQMessageBody messageBody) throws Exception {
        messageBody.getParams().put("productIds", messageBody.getProductCodes());
        messageBody.getParams().put("_channleId", messageBody.getChannelId());
        messageBody.getParams().put("_userName", messageBody.getUserNmme());
        try {
            Map<String, String> failMap = bacthUpdateService.onStartup(messageBody.getParams());
            if (failMap != null && failMap.size() > 0) {
                cmsLog(messageBody, OperationLog_Type.successIncludeFail, JacksonUtil.bean2Json(failMap));
            }
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                cmsLog(messageBody, OperationLog_Type.businessException, e.getMessage());
            } else {
                cmsLog(messageBody, OperationLog_Type.unknownException, e.getMessage());
            }
        }
    }
}
