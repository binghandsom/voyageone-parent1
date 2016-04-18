package com.voyageone.task2.cms.service.jumei;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.jumei.CmsBtJmPromotionImportTaskService;
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
public class JmBtPromotionImportJobService extends BaseMQTaskService {

    @Autowired
    CmsBtJmPromotionImportTaskService service;
    private static final Logger LOG = LoggerFactory.getLogger(JmBtPromotionImportJobService.class);
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList, Map<String, Object> message) throws Exception {
        // cms.jm.import.path
        // cms.jm.export.path
        System.out.println("收到消息：" + JacksonUtil.bean2Json(message));
        // this.getControls();
        System.out.println("收到消息begin：" + JacksonUtil.bean2Json(message));
        TaskControlBean taskControlBean = get(taskControlList, "cms.jm.import.path");
        if (taskControlBean == null) {
            LOG.error("JmBtPromotionImportJobService", "请配置cms.jm.import.path");
            return;
        }
        String importPath = taskControlBean.getCfg_val1();
        FileUtils.mkdirPath(importPath);
        int id = (int) Double.parseDouble(message.get("id").toString());
        service.importFile(id,importPath);
        System.out.println("收到消息end：" + JacksonUtil.bean2Json(message));
    }


    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "JmBtPromotionImportJobService";
    }
}
