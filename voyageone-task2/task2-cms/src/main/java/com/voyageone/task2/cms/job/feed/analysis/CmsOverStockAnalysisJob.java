package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.OverStockAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author gjl
 * @version 0.0.1, 16/06/21
 */
@Component("CmsOverStockAnalysisJob")
public class CmsOverStockAnalysisJob extends BaseTaskJob {
    @Autowired
    private OverStockAnalysisService overStockAnalysisService;
    @Override
    protected BaseTaskService getTaskService() {
        return overStockAnalysisService;
    }
}
