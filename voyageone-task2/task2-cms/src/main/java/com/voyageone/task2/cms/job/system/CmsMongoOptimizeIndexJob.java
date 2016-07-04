package com.voyageone.task2.cms.job.system;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.system.CmsMongoOptimizeIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 优化Mongodb索引
 *
 * @author jindong.yang
 */
@Component("CmsMongoOptimizeIndexJob")
public class CmsMongoOptimizeIndexJob extends BaseTaskJob {

    @Autowired
    private CmsMongoOptimizeIndexService cmsMongoOptimizeIndexService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsMongoOptimizeIndexService;
    }


}

