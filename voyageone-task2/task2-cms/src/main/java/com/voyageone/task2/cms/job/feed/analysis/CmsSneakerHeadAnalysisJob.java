package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.ShoeZooAnalysisService;
import com.voyageone.task2.cms.service.feed.SneakerHeadAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gjl on 2016/11/15.
 */
@Component("CmsSneakerHeadAnalysisJob")
public class CmsSneakerHeadAnalysisJob extends BaseTaskJob {
    @Autowired
    private SneakerHeadAnalysisService sneakerHeadAnalysisService;
    @Override
    protected BaseTaskService getTaskService() {
        return sneakerHeadAnalysisService;
    }
}
