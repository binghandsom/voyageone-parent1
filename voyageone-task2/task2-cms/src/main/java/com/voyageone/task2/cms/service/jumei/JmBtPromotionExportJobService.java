package com.voyageone.task2.cms.service.jumei;

import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionExportTaskService;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionExportTask3Service;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.task2.base.BaseMQCmsService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * JmBtPromotion ExportJob Service
 *
 * @author peitao 2016/4/8.
 * @version 2.0.0
 * @since 2.0.0
 */

@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_JmBtPromotionExportTask)
public class JmBtPromotionExportJobService extends BaseMQCmsService {
//    @Autowired
//    private CmsBtJmPromotionExportTaskService service;
    @Autowired
    private CmsBtJmPromotionExportTask3Service service;
    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        $debug("JmBtPromotionExportJobService收到消息：" + JacksonUtil.bean2Json(messageMap));
        TaskControlBean taskControlBean = getTaskControlBean(taskControlList, "cms.jm.export.path");
        $info("JmBtPromotionExportJobService", "begin");
        if (taskControlBean == null) {
            $error("JmBtPromotionExportJobService", "请配置cms.jm.export.path");
            return;
        }
        String exportPath = taskControlBean.getCfg_val1();
        FileUtils.mkdirPath(exportPath);
        int id = (int) Double.parseDouble(messageMap.get("id").toString());
        service.export(id, exportPath);
        $info("JmBtPromotionExportJobService", "end");
    }
}
