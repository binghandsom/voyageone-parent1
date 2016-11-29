package com.voyageone.task2.cms.job.uscategory;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.uscategory.CmsSneakerheadUsCategorySyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by vantis on 2016/11/28.
 * 闲舟江流夕照晚 =。=
 */
@Component
public class CmsUsCategorySyncJob extends BaseTaskJob {

    private final CmsSneakerheadUsCategorySyncService cmsSneakerheadUsCategorySyncService;

    @Autowired
    public CmsUsCategorySyncJob(CmsSneakerheadUsCategorySyncService cmsSneakerheadUsCategorySyncService) {
        this.cmsSneakerheadUsCategorySyncService = cmsSneakerheadUsCategorySyncService;
    }

    @Override
    protected BaseTaskService getTaskService() {
        return this.cmsSneakerheadUsCategorySyncService;
    }
}
