package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsBuildPlatformAttributeUpdateTmServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Charis on 2017/3/29.
 *
 *  天猫商品属性增量更新处理
 */
@Component("CmsBuildPlatformAttributeUpdateTmJob")
public class CmsBuildPlatformAttributeUpdateTmJob extends BaseTaskJob{

    @Autowired
    private CmsBuildPlatformAttributeUpdateTmServcie cmsBuildPlatformAttributeUpdateTmServcie;

    @Override
    protected BaseTaskService getTaskService() {return cmsBuildPlatformAttributeUpdateTmServcie;}

}
