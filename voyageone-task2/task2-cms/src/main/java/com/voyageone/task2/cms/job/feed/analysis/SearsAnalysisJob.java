package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.SearsAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Jonas on 10/23/15.
 */
@Component("SearsAnalysisJob")
public class SearsAnalysisJob extends BaseTaskJob {
    @Autowired
    private SearsAnalysisService analysisService;

    @Override
    protected BaseTaskService getTaskService() {
        return analysisService;
    }
}
