package com.voyageone.task2.cms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 取所有平台tree和schema
 *
 * @author morse on 2016/9/12
 * @version 2.6.0
 */
@Service
public class GetAllPlatformsInfoService extends BaseTaskService {

    private final static String JOB_NAME = "CmsGetAllPlatformsInfoJob";

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return JOB_NAME;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

    }

}
