package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.ChampionAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gjl on 2016/12/9.
 */
@Component("CmsChampionAnalysisJob")
public class CmsChampionAnalysisJob extends BaseTaskJob {
    @Autowired
    private ChampionAnalysisService championAnalysisService;
    @Override
    protected BaseTaskService getTaskService() {
        return championAnalysisService;
    }
}
