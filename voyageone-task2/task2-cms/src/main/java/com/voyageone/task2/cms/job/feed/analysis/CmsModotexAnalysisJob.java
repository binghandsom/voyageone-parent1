package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.ModotexAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author gjl
 * @version 0.0.1, 16/08/09
 */
@Component("CmsModotexAnalysisJob")
public class CmsModotexAnalysisJob extends BaseTaskJob {
    @Autowired
    private ModotexAnalysisService modotexAnalysisService;
    @Override
    protected BaseTaskService getTaskService() {
        return modotexAnalysisService;
    }
}
