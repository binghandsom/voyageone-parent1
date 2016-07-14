package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.EdcSkincareAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gjl on 2016/7/12.
 */
@Component("CmsEdcSkincareAnalysisJob")
public class CmsEdcSkincareAnalysisJob extends BaseTaskJob {
    @Autowired
    private EdcSkincareAnalysisService edcSkincareAnalysisService;
    @Override
    protected BaseTaskService getTaskService() {
        return edcSkincareAnalysisService;
    }
}
