package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.BhfoAnalysisService;
import com.voyageone.task2.cms.service.feed.VtmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author james
 * @version 0.0.1, 16/4/29
 */
@Component("CmsBhfoAnalySisJob")
public class CmsBhfoAnalysisJob extends BaseTaskJob {
    @Autowired
    private BhfoAnalysisService bhfoAnalysisService;

    @Override
    protected BaseTaskService getTaskService() {
        return bhfoAnalysisService;
    }
}
