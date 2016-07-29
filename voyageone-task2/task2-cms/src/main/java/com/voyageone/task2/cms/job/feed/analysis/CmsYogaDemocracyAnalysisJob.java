package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.YogaDemocracyAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author gjl
 * @version 0.0.1, 16/07/27
 */
@Component("CmsYogaDemocracyAnalysisJob")
public class CmsYogaDemocracyAnalysisJob extends BaseTaskJob {
    @Autowired
    private YogaDemocracyAnalysisService yogaDemocracyAnalysisService;
    @Override
    protected BaseTaskService getTaskService() {
        return yogaDemocracyAnalysisService;
    }
}
