package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.TargetAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author james.li on 2016/5/10.
 * @version 2.0.0
 */
@Component("CmsTargetAnalySisJob")
public class TargetAnalysisJob extends BaseTaskJob {
    @Autowired
    private TargetAnalysisService targetAnalysisService;
    @Override
    protected BaseTaskService getTaskService() {
        return targetAnalysisService;
    }
}
