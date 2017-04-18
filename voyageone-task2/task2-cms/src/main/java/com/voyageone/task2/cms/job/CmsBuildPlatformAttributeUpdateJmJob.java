package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsBuildPlatformAttributeUpdateJmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Charis on 2017/3/29.
 *
 * 聚美商品标题增量更新处理
 */

@Component("CmsBuildPlatformAttributeUpdateJmJob")
public class CmsBuildPlatformAttributeUpdateJmJob extends BaseTaskJob{

    @Autowired
    private CmsBuildPlatformAttributeUpdateJmService cmsBuildPlatformAttributeUpdateJmService;

    @Override
    protected BaseTaskService getTaskService() {return cmsBuildPlatformAttributeUpdateJmService;}
}
