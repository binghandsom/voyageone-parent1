package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.FragranceNetAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gjl on 2016/7/5.
 */
@Component("CmsFragranceNetAnalysisJob")
public class CmsFragranceNetAnalysisJob extends BaseTaskJob {
    @Autowired
    private FragranceNetAnalysisService fragranceNetAnalysisService;
    @Override
    protected BaseTaskService getTaskService() {
        return fragranceNetAnalysisService;
    }
}
