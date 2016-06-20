package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsBuildPlatformProductUploadJMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 聚美平台商品上新Job
 *
 * @author ethan on 2016/6/19.
 * @version 2.1.0
 * @since 2.1.0
 */
@Component("CmsBuildPlatformProductUploadJMJob")
public class CmsBuildPlatformProductUploadJMJob extends BaseTaskJob {

    @Autowired
    private CmsBuildPlatformProductUploadJMService cmsBuildPlatformProductUploadJMService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsBuildPlatformProductUploadJMService;
    }

}

