package com.voyageone.batch.cms.service.promotion.beat;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;

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
