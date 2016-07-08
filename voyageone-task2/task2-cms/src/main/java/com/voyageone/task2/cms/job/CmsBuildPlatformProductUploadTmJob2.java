package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsBuildPlatformProductUploadTmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 天猫平台商品上新Job
 *
 * @author morse.lu on 2016/07/08.
 * @version 2.1.0
 * @since 2.1.0
 */
@Component("CmsBuildPlatformProductUploadTmJob")
public class CmsBuildPlatformProductUploadTmJob2 extends BaseTaskJob {

    @Autowired
    private CmsBuildPlatformProductUploadTmService cmsBuildPlatformProductUploadTmService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsBuildPlatformProductUploadTmService;
    }

}

