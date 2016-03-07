package com.voyageone.task2.cms.service.promotion.beat;

import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.task2.base.modelbean.TaskControlBean;

import java.util.List;

/**
 * Created by jonasvlag on 16/3/3.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public class BeatService extends BaseTaskService {

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBeatJob2";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {



    }
}
