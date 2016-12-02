package com.voyageone.task2.cms.service.sneakerhead;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.components.sneakerhead.service.SneakerheadApiService;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by vantis on 2016/12/2.
 * 闲舟江流夕照晚 =。=
 */
public class CmsUsPlatformStatusSyncService extends BaseCronTaskService {

    private final SneakerheadApiService sneakerheadApiService;

    @Autowired
    public CmsUsPlatformStatusSyncService(SneakerheadApiService sneakerheadApiService) {
        this.sneakerheadApiService = sneakerheadApiService;
    }

    @Override
    protected String getTaskName() {
        return "cmsUsPlatformStatusSyncJob";
    }

    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        // TODO: 2016/12/2 美国平台状态获取 vantis
    }
}
