package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.OverStockPriceAnalysisService;
import com.voyageone.task2.cms.service.feed.WmfPriceAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author james.li on 2016/8/8.
 * @version 2.0.0
 */
@Component("WmfPriceAnalysisJob")
public class WmfPriceAnalysisJob extends BaseTaskJob {

    @Autowired
    private WmfPriceAnalysisService wmfPriceAnalysisService;

    @Override
    protected BaseTaskService getTaskService() {
        return wmfPriceAnalysisService;
    }
}
