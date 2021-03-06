package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.service.impl.cms.CmsBtExportTaskService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchExportMQMessageBody;
import com.voyageone.service.model.cms.CmsBtExportTaskModel;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import com.voyageone.task2.cms.service.product.batch.CmsAdvSearchExportFileService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        CmsBtExportTaskModel exportTaskModel = cmsBtExportTaskService.getExportById(cmsBtExportTaskId);
        if (exportTaskModel == null) {
            cmsConfigExLog(messageBody, String.format("cms.bt.export.task(id=%s)不存在", cmsBtExportTaskId));
            return;
        }

        List<CmsBtOperationLogModel_Msg> failList = cmsAdvSearchExportFileService.export(messageBody);
        if (CollectionUtils.isNotEmpty(failList)) {
            String comment = String.format("处理失败件数(%s)", failList.size());
            cmsSuccessIncludeFailLog(messageBody, comment, failList);
        }

    }
}
