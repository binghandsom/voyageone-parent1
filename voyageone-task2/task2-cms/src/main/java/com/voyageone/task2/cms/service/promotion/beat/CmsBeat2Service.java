package com.voyageone.task2.cms.service.promotion.beat;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;

import java.util.List;

/**
 * Created by james on 2017/6/20.
 */
public class CmsBeat2Service extends BaseCronTaskService {
    @Override
    protected String getTaskName() {
        return "CmsBeat2Job";
    }

    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

    }
}
