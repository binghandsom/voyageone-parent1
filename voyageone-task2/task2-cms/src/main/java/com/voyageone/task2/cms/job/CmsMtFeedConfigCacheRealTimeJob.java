package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsMtFeedConfigCacheRealTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author aooer 2016/3/8.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component
public class CmsMtFeedConfigCacheRealTimeJob extends BaseTaskJob {

    @Autowired
    private CmsMtFeedConfigCacheRealTimeService cmsMtFeedConfigCacheRealTimeService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsMtFeedConfigCacheRealTimeService;
    }
}
