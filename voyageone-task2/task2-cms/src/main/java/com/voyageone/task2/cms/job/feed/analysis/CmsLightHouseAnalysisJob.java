package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.LightHouseAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gjl on 2016/7/8.
 */
@Component("CmsLightHouseAnalysisJob")
public class CmsLightHouseAnalysisJob extends BaseTaskJob {
    @Autowired
    private LightHouseAnalysisService lightHouseAnalysisService;
    @Override
    protected BaseTaskService getTaskService() {
        return lightHouseAnalysisService;
    }
}
