package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseMQTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsBuildPlatformProductUploadJdMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author desmond 2016/4/15.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component
public class CmsBuildPlatformProductUploadJdMqJob extends BaseMQTaskJob {

    @Autowired
    private CmsBuildPlatformProductUploadJdMqService cmsBuildPlatformProductUploadJdMqService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsBuildPlatformProductUploadJdMqService;
    }
}
