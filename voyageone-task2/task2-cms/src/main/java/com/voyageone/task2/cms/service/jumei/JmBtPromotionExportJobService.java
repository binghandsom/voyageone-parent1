package com.voyageone.task2.cms.service.jumei;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionExportTaskService;
import com.voyageone.task2.base.BaseMQTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/4/8.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class JmBtPromotionExportJobService extends BaseMQTaskService {

    @Autowired
    CmsBtJmPromotionExportTaskService service;
    private static final Logger LOG = LoggerFactory.getLogger(JmBtPromotionImportJobService.class);
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList, Map<String, Object> message) throws Exception {
        System.out.println("JmBtPromotionExportJobService收到消息：" + JacksonUtil.bean2Json(message));
        TaskControlBean taskControlBean = get(taskControlList, "cms.jm.export.path");
        LOG.info("JmBtPromotionExportJobService","begin");
        if (taskControlBean == null) {
            LOG.error("JmBtPromotionExportJobService", "请配置cms.jm.export.path");
            return;
        }
        String exportPath = taskControlBean.getCfg_val1();
        FileUtils.mkdirPath(exportPath);
        int id = (int) Double.parseDouble(message.get("id").toString());
        service.export(id,exportPath);
        LOG.info("JmBtPromotionExportJobService","end");
    }

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "JmBtPromotionExportJobService";
    }
}
