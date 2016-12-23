package com.voyageone.task2.cms.service.jumei;

import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionExportTask3Service;
import com.voyageone.service.impl.cms.vomessage.body.JmExportMQMessageBody;
import com.voyageone.task2.base.TBaseMQCmsService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RabbitListener()
public  class JmBtPromotionExportJobService extends TBaseMQCmsService<JmExportMQMessageBody> {

    @Autowired
    private CmsBtJmPromotionExportTask3Service service;


    @Override
    public void onStartup(JmExportMQMessageBody messageBody) throws Exception {
        $debug("JmBtPromotionExportJobService收到消息：" + JacksonUtil.bean2Json(messageBody));
        TaskControlBean taskControlBean = getTaskControlBean(taskControlList, "cms.jm.export.path");
        $info("JmBtPromotionExportJobService", "begin");
        if (taskControlBean == null) {
            $error("JmBtPromotionExportJobService", "请配置cms.jm.export.path");
            return;
        }

        String exportPath = taskControlBean.getCfg_val1();
        FileUtils.mkdirPath(exportPath);
        int id = messageBody.getJmBtPromotionExportTaskId();
        service.export(id, exportPath);
        $info("JmBtPromotionExportJobService", "end");
    }
}