package com.voyageone.task2.cms.mqjob.jm;

import com.voyageone.common.util.FileUtils;

import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionImportTask3Service;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.impl.cms.vomq.vomessage.body.jm.JmPromotionImportMQMessageBody;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * JmBtPromotion ImportJob Service  聚美活动文件导入
 *
 * @author peitao 2016/12/29.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
@RabbitListener(queues = CmsMqRoutingKey.CMS_JM_PROMOTION_IMPORT)
public class CmsJmPromotionImportMQJob extends TBaseMQCmsService<JmPromotionImportMQMessageBody> {
    @Autowired
    CmsBtJmPromotionImportTask3Service service;

    @Override
    public void onStartup(JmPromotionImportMQMessageBody messageMap) throws Exception {
        TaskControlBean taskControlBean = getTaskControlBean(taskControlList, "cms.jm.import.path");
        if (taskControlBean == null) {
            this.cmsConfigExLog(messageMap, "请配置cms.jm.import.path");
            return;
        }
        String importPath = taskControlBean.getCfg_val1();
        FileUtils.mkdirPath(importPath);
        int id = messageMap.getJmBtPromotionImportTaskId();
        service.importFile(id, importPath);
    }
}
