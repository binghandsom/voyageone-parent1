package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.SEAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jonasvlag on 16/4/1.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@Component("ShoeCityAnalysisJob")
public class SEAnalysisJob extends BaseTaskJob {

    @Autowired
    private SEAnalysisService seAnalysisService;

    @Override
    protected BaseTaskService getTaskService() {
        return seAnalysisService;
    }
}
