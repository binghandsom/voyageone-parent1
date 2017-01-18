package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.CmsBtExportTaskService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchExportMQMessageBody;
import com.voyageone.service.model.cms.CmsBtExportTaskModel;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import com.voyageone.task2.cms.service.product.batch.CmsAdvSearchExportFileService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 高级检索文件导出job
 *
 * @Author rex
 * @Create 2016-12-30 15:25
 */
@Service
@RabbitListener()
public class CmsAdvSearchExportMQJob extends TBaseMQCmsService<AdvSearchExportMQMessageBody> {

    @Autowired
    private CmsBtExportTaskService cmsBtExportTaskService;
    @Autowired
    private CmsAdvSearchExportFileService cmsAdvSearchExportFileService;

    @Override
    public void onStartup(AdvSearchExportMQMessageBody messageBody) throws Exception {
        Integer cmsBtExportTaskId = messageBody.getCmsBtExportTaskId();
        if (cmsBtExportTaskId == null) {
            this.cmsLog(messageBody, OperationLog_Type.parameterException, "cms.bt.export.task.id不存在");
            return;
        }
        CmsBtExportTaskModel exportTaskModel = cmsBtExportTaskService.getExportById(cmsBtExportTaskId);
        if (exportTaskModel == null) {
            this.cmsLog(messageBody, OperationLog_Type.parameterException, String.format("cms.bt.export.task(id=%s)不存在", cmsBtExportTaskId));
            return;
        }
        try {
            List<Map<String, String>> failList = cmsAdvSearchExportFileService.export(messageBody);
            if (CollectionUtils.isNotEmpty(failList)) {
                cmsLog(messageBody, OperationLog_Type.successIncludeFail, JacksonUtil.bean2Json(failList));
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
