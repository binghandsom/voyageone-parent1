package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.feed.GiltAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Jonas, 2/3/16.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component
public class GiltAnalysisJob extends BaseTaskJob {

    @Autowired
    private GiltAnalysisService giltAnalysisService;

    @Override
    protected BaseTaskService getTaskService() {
        return giltAnalysisService;
    }
}
