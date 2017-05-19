package com.voyageone.task2.cms.mqjob;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.CmsBtPromotionExportTaskService;
import com.voyageone.service.impl.cms.promotion.CmsPromotionExportService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsPromotionExportMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsShelvesImageUploadMQMessageBody;
import com.voyageone.service.model.cms.CmsBtExportTaskModel;
import com.voyageone.service.model.cms.CmsBtPromotionExportTaskModel;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 活动(非聚美)商品导出Job
 *
 * @Author rex.wu
 * @Create 2017-05-17 15:50
 */
@Service
@RabbitListener()
public class CmsPromotionExportMQJob extends TBaseMQCmsService<CmsPromotionExportMQMessageBody> {

    @Autowired
    private CmsBtPromotionExportTaskService cmsBtPromotionExportTaskService;
    @Autowired
    private CmsPromotionExportService cmsPromotionExportService;


    @Override
    public void onStartup(CmsPromotionExportMQMessageBody messageBody) throws Exception {

        try {
            cmsPromotionExportService.export(messageBody);
        } catch (Exception e) {
            String comment = "活动商品导出MQ Job消费异常";
            if (e instanceof BusinessException) {
                cmsBusinessExLog(messageBody, comment + e.getMessage());
            } else {
                cmsLog(messageBody, OperationLog_Type.unknownException, comment);
            }
        }

    }
}
