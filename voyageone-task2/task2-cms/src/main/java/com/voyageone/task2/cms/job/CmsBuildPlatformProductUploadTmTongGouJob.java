package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsBuildPlatformProductUploadTmTongGouService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 天猫官网同购商品上新Job
 *
 * @author desmond on 2016/09/12.
 * @version 2.5.0
 * @since 2.5.0
 */
@Component("CmsBuildPlatformProductUploadTmTongGouJob")
public class CmsBuildPlatformProductUploadTmTongGouJob extends BaseTaskJob {

    @Autowired
    private CmsBuildPlatformProductUploadTmTongGouService cmsBuildPlatformProductUploadTmTongGouService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsBuildPlatformProductUploadTmTongGouService;
    }

}

