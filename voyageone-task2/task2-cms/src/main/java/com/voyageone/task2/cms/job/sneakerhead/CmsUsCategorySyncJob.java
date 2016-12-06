package com.voyageone.task2.cms.job.sneakerhead;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.sneakerhead.CmsUsCategorySyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by vantis on 2016/11/28.
 * 闲舟江流夕照晚 =。=
 */
@Component
public class CmsUsCategorySyncJob extends BaseTaskJob {

    private final CmsUsCategorySyncService cmsUsCategorySyncService;

    @Autowired
    public CmsUsCategorySyncJob(CmsUsCategorySyncService cmsUsCategorySyncService) {
        this.cmsUsCategorySyncService = cmsUsCategorySyncService;
    }

    @Override
    protected BaseTaskService getTaskService() {
        return this.cmsUsCategorySyncService;
    }
}
