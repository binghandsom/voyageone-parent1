package com.voyageone.task2.cms.mqjob;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.product.search.CmsAdvSearchProductApprovalService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchProductApprovalMQMessageBody;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 高级检索-商品审批
 *
 * @Author dell
 * @Create 2017-01-13 13:06
 */
@Service
@RabbitListener()
public class CmsAdvSearchProductApprovalMQJob extends TBaseMQCmsService<AdvSearchProductApprovalMQMessageBody> {

    @Autowired
    protected CmsAdvSearchProductApprovalService cmsAdvSearchProductApprovalService;

    @Override
    public void onStartup(AdvSearchProductApprovalMQMessageBody messageBody) throws Exception {

        try {
            cmsAdvSearchProductApprovalService.approval(messageBody);
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                cmsLog(messageBody, OperationLog_Type.businessException, e.getMessage());
            } else {
                cmsLog(messageBody, OperationLog_Type.unknownException, e.getMessage());
            }
        }
    }
}
