package com.voyageone.task2.cms.mqjob;

import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.CmsBtExportTaskService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchExportMQMessageBody;
import com.voyageone.service.model.cms.CmsBtExportTaskModel;
import com.voyageone.task2.cms.service.product.batch.CmsAdvSearchExportFileService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        CmsBtExportTaskModel exportTaskModel = cmsBtExportTaskService.getExportById(messageBody.getCmsBtExportTaskId());
        if (exportTaskModel == null) {
            this.cmsLog(messageBody, OperationLog_Type.parameterException, "cms.bt.export.task.id不存在");
            return;
        }
        cmsAdvSearchExportFileService.export(messageBody);
    }
}
