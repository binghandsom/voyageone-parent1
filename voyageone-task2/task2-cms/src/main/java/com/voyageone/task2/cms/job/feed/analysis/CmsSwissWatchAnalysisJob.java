package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.SwissWatchAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gjl on 2016/08/16.
 */
@Component("CmsSwissWatchAnalysisJob")
public class CmsSwissWatchAnalysisJob extends BaseTaskJob {
    @Autowired
    private SwissWatchAnalysisService swissWatchAnalysisService;
    @Override
    protected BaseTaskService getTaskService() {
        return swissWatchAnalysisService;
    }
}
