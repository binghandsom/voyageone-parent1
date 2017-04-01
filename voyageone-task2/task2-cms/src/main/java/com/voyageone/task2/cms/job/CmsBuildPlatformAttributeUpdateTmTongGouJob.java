package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsBuildPlatformAttributeUpdateTmTongGouService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Charis on 2017/3/29.
 *
 *  官网同购商品属性增量更新处理
 */
@Component("CmsBuildPlatformAttributeUpdateTmTongGouJob")
public class CmsBuildPlatformAttributeUpdateTmTongGouJob extends BaseTaskJob {

    @Autowired
    private CmsBuildPlatformAttributeUpdateTmTongGouService cmsBuildPlatformAttributeUpdateTmTongGouService;

    @Override
    protected BaseTaskService getTaskService() {return cmsBuildPlatformAttributeUpdateTmTongGouService;}
}
