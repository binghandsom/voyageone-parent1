package com.voyageone.task2.cms.job.sneakerhead;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.sneakerhead.CmsUsPlatformStatusSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by vantis on 2016/12/2.
 * 闲舟江流夕照晚 =。=
 */
@Component
public class CmsUsPlatformStatusSyncJob extends BaseTaskJob {

    private final CmsUsPlatformStatusSyncService cmsUsPlatformStatusSyncService;

    @Autowired
    public CmsUsPlatformStatusSyncJob(CmsUsPlatformStatusSyncService cmsUsPlatformStatusSyncService) {
        this.cmsUsPlatformStatusSyncService = cmsUsPlatformStatusSyncService;
    }

    @Override
    protected BaseTaskService getTaskService() {
        return cmsUsPlatformStatusSyncService;
    }
}
