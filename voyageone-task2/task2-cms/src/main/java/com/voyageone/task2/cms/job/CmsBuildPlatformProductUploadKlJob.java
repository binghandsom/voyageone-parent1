package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsBuildPlatformProductUploadKlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 考拉平台商品上新Job
 *
 * @author 2017/6/16.
 * @version 2.1.0
 * @since 2.1.0
 */
@Component("CmsBuildPlatformProductUploadKlJob")
public class CmsBuildPlatformProductUploadKlJob extends BaseTaskJob {

    @Autowired
    private CmsBuildPlatformProductUploadKlService cmsBuildPlatformProductUploadKlService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsBuildPlatformProductUploadKlService;
    }

}

