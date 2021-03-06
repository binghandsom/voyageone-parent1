package com.voyageone.task2.cms.mqjob;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.impl.cms.CmsBtExportTaskService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.FeedExportMQMessageBody;
import com.voyageone.service.model.cms.CmsBtExportTaskModel;
import com.voyageone.task2.cms.service.CmsFeedExportService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Feed导出Job
 *
 * @Author rex
 * @Create 2016-12-30 11:04
 */
@Service
@RabbitListener()
public class CmsFeedExportMQJob extends TBaseMQCmsService<FeedExportMQMessageBody> {

    @Autowired
    private CmsBtExportTaskService cmsBtExportTaskService;
    @Autowired
    private CmsFeedExportService cmsFeedExportService;

    @Override
    public void onStartup(FeedExportMQMessageBody messageBody) throws Exception {
        CmsBtExportTaskModel exportTaskModel = cmsBtExportTaskService.getExportById(messageBody.getCmsBtExportTaskId());
        if (exportTaskModel == null) {
            throw new BusinessException("cms.bt.export.task.id不存在");
        }

        cmsFeedExportService.export(exportTaskModel);
    }

}
