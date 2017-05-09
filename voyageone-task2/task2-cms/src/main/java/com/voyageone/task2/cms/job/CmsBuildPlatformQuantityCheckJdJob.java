package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsBuildPlatformQuantityCheckJdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Charis on 2017/4/19.
 */

@Component("CmsBuildPlatformQuantityCheckJdJob")
public class CmsBuildPlatformQuantityCheckJdJob extends BaseTaskJob{

    @Autowired
    private CmsBuildPlatformQuantityCheckJdService cmsBuildPlatformQuantityCheckJdService;
    @Override
    protected BaseTaskService getTaskService() {
        return cmsBuildPlatformQuantityCheckJdService;
    }
}
