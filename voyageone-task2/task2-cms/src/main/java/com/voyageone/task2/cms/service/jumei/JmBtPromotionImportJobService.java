package com.voyageone.task2.cms.service.jumei;

import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionImportTaskService;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionImportTask3Service;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.task2.base.BaseMQCmsService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * JmBtPromotion ImportJob Service
 *
 * @author peitao 2016/4/8.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_JmBtPromotionImportTask)
public class JmBtPromotionImportJobService extends BaseMQCmsService {
//    @Autowired
//    private CmsBtJmPromotionImportTaskService service;
     @Autowired
     CmsBtJmPromotionImportTask3Service service;
     @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        $debug("JmBtPromotionImportJobService收到消息：" + JacksonUtil.bean2Json(messageMap));
        TaskControlBean taskControlBean = getTaskControlBean(taskControlList, "cms.jm.import.path");
        if (taskControlBean == null) {
            $error("JmBtPromotionImportJobService", "请配置cms.jm.import.path");
            return;
        }
        String importPath = taskControlBean.getCfg_val1();
        FileUtils.mkdirPath(importPath);
        int id = (int) Double.parseDouble(messageMap.get("id").toString());
        service.importFile(id, importPath);
        $info("JmBtPromotionImportJobService", "end");
    }
}
