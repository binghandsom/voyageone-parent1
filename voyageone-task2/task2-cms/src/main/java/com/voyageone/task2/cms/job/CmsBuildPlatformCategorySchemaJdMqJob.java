package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseMQTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsBuildPlatformCategorySchemaJdMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author desmond 2016/4/13.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component
public class CmsBuildPlatformCategorySchemaJdMqJob extends BaseMQTaskJob {

    @Autowired
    private CmsBuildPlatformCategorySchemaJdMqService cmsBuildPlatformCategorySchemaJdMqService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsBuildPlatformCategorySchemaJdMqService;
    }
}
