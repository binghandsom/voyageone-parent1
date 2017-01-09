package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsBuildPlatformProductUploadDtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 分销平台商品上新Job
 *
 * @author desmond on 2017/01/06.
 * @version 2.11.0
 * @since 2.11.0
 */
@Component("CmsBuildPlatformProductUploadDtJob")
public class CmsBuildPlatformProductUploadDtJob extends BaseTaskJob {

    @Autowired
    private CmsBuildPlatformProductUploadDtService cmsBuildPlatformProductUploadDtService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsBuildPlatformProductUploadDtService;
    }

}

