package com.voyageone.task2.cms.mqjob.jm;

import com.voyageone.common.util.FileUtils;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionExportTask3Service;
import com.voyageone.service.impl.cms.vomq.vomessage.body.JmPromotionExportMQMessageBody;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * JmBtPromotion ImportJob Service  聚美活动文件生成导出
 *
 * @author peitao 2016/12/26.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
@RabbitListener()
public class CmsJmPromotionExportMQJob extends TBaseMQCmsService<JmPromotionExportMQMessageBody> {

    @Autowired
    private CmsBtJmPromotionExportTask3Service service;

    @Override
    public void onStartup(JmPromotionExportMQMessageBody messageBody) {

        if(taskControlList == null) initControls();
        // 获取Mq的配置信息
        TaskControlBean taskControlBean = getTaskControlBean(taskControlList, "cms.jm.export.path");
        if (taskControlBean == null) {
            cmsConfigExLog(messageBody, "请在tm_task_control中确认配置cms.jm.export.path");
            return;
        }

        // 生成excel文件
        String exportPath = taskControlBean.getCfg_val1();
        FileUtils.mkdirPath(exportPath);
        service.export(messageBody.getJmBtPromotionExportTaskId(), exportPath);
    }
}