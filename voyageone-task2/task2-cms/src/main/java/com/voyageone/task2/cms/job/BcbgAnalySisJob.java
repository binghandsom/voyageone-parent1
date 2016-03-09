package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.BcbgAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Jonas on 10/23/15.
 */
@Component("BcbgAnalySisJob")
public class BcbgAnalySisJob extends BaseTaskJob {
    @Autowired
    private BcbgAnalysisService analysisService;

    @Override
    protected BaseTaskService getTaskService() {
        return analysisService;
    }
}
