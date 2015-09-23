package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.CmsGetSuperFeedService;
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

