package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.GiltAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Jonas, 2/3/16.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component("GiltAnalysisJob")
public class GiltAnalysisJob extends BaseTaskJob {

    @Autowired
    private GiltAnalysisService giltAnalysisService;

    @Override
    protected BaseTaskService getTaskService() {
        return giltAnalysisService;
    }
}
