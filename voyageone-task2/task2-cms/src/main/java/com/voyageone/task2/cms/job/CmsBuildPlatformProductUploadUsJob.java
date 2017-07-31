package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsBuildPlatformProductUploadTmTongGouService;
import com.voyageone.task2.cms.service.CmsBuildPlatformProductUploadUsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Charis on 2017/7/20.
 */
@Component("CmsBuildPlatformProductUploadUsJob")
public class CmsBuildPlatformProductUploadUsJob extends BaseTaskJob{

    @Autowired
    private CmsBuildPlatformProductUploadUsService cmsBuildPlatformProductUploadUsService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsBuildPlatformProductUploadUsService;
    }
}
