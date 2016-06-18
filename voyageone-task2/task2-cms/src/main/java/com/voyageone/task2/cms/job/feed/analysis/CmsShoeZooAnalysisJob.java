package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.ShoeZooAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author james
 * @version 0.0.1, 16/4/29
 */
@Component("CmsShoeZooAnalysisJob")
public class CmsShoeZooAnalysisJob extends BaseTaskJob {
    @Autowired
    private ShoeZooAnalysisService shoeZooAnalysisService;
    @Override
    protected BaseTaskService getTaskService() {
        return shoeZooAnalysisService;
    }
}
