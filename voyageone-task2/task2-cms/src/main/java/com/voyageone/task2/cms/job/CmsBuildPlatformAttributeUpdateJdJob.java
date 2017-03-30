package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsBuildPlatformAttributeUpdateJdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Charis on 2017/3/29.
 *
 * 京东商品属性增量更新处理
 */

@Component("CmsBuildPlatformAttributeUpdateJdJob")
public class CmsBuildPlatformAttributeUpdateJdJob extends BaseTaskJob{

    @Autowired
    private CmsBuildPlatformAttributeUpdateJdService cmsBuildPlatformAttributeUpdateJdService;

    @Override
    protected BaseTaskService getTaskService() {return cmsBuildPlatformAttributeUpdateJdService;}
}
