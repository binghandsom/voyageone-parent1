package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsBuildPlatformProductUploadCnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 独立域名产品上新Job
 *
 * @author morse.lu on 2016/09/23.
 * @version 2.6.0
 * @since 2.6.0
 */
@Component("CmsBuildPlatformProductUploadCnJob")
public class CmsBuildPlatformProductUploadCnJob extends BaseTaskJob {

    @Autowired
    private CmsBuildPlatformProductUploadCnService cmsBuildPlatformProductUploadCnService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsBuildPlatformProductUploadCnService;
    }

}

