package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsBuildPlatformProductUploadJdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 京东平台商品上新Job
 *
 * @author desmond on 2016/6/16.
 * @version 2.1.0
 * @since 2.1.0
 */
@Component("CmsBuildPlatformProductUploadJdJob")
public class CmsBuildPlatformProductUploadJdJob extends BaseTaskJob {

    @Autowired
    private CmsBuildPlatformProductUploadJdService cmsBuildPlatformProductUploadJdService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsBuildPlatformProductUploadJdService;
    }

}

