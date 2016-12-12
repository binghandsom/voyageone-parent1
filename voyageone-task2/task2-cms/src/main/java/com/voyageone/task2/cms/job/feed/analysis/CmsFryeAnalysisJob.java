package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.FragranceNetAnalysisService;
import com.voyageone.task2.cms.service.feed.FryeAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gjl on 2016/11/29
 */
@Component("CmsFryeAnalysisJob")
public class CmsFryeAnalysisJob extends BaseTaskJob {
    @Autowired
    private FryeAnalysisService fryeAnalysisService;
    @Override
    protected BaseTaskService getTaskService() {
        return fryeAnalysisService;
    }
}
