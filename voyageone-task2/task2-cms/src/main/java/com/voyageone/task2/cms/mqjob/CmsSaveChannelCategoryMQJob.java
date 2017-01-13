package com.voyageone.task2.cms.mqjob;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.vomq.vomessage.body.SaveChannelCategoryMQMessageBody;
import com.voyageone.service.impl.cms.vomqjobservice.CmsSaveChannelCategoryService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 保存店铺内分类Job
 *
 * @Author rex
 * @Create 2017-01-03 14:20
 */
@Service
@RabbitListener()
public class CmsSaveChannelCategoryMQJob extends TBaseMQCmsService<SaveChannelCategoryMQMessageBody> {

    @Autowired
    private CmsSaveChannelCategoryService saveChannelCategoryService;

    @Override
    public void onStartup(SaveChannelCategoryMQMessageBody messageBody) throws Exception {
        try {
            Map<String, String> errorMap = saveChannelCategoryService.onStartup(messageBody.getParams());
            if (errorMap != null && errorMap.size() > 0) {
                cmsLog(messageBody, OperationLog_Type.successIncludeFail, JacksonUtil.bean2Json(errorMap));
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
