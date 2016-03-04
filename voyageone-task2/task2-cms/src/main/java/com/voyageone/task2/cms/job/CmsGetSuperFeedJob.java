package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsGetSuperFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 产品导入
 *
 * @author Zero
 */
@Component("cmsGetSuperFeedTask")
public class CmsGetSuperFeedJob extends BaseTaskJob {

    @Autowired
    private CmsGetSuperFeedService cmsGetSuperFeedService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsGetSuperFeedService;
    }
}

