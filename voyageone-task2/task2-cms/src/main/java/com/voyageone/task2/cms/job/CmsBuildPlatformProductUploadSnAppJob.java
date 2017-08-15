package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsBuildPlatformProductUploadSnAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Charis on 2017/8/15.
 */
@Component("CmsBuildPlatformProductUploadSnAppJob")
public class CmsBuildPlatformProductUploadSnAppJob extends BaseTaskJob {

    @Autowired
    private CmsBuildPlatformProductUploadSnAppService cmsBuildPlatformProductUploadSnAppService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsBuildPlatformProductUploadSnAppService;
    }
}
