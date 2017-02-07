package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.product.search.CmsAdvSearchProductApprovalService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchProductApprovalMQMessageBody;

import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

        Map<String,Object> errorCodeList = cmsAdvSearchProductApprovalService.approval(messageBody);
        if (errorCodeList.size() > 0)
        cmsSuccessIncludeFailLog(messageBody, JacksonUtil.bean2Json(errorCodeList));
        else
        cmsSuccessLog(messageBody, "高级检索 商品审批 正常结束!");
    }
}
