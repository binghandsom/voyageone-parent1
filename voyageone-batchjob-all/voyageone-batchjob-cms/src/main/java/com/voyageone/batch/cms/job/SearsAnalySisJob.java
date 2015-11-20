package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.feed.BcbgAnalysisService;
import com.voyageone.batch.cms.service.feed.SearsAnalysisService;
import com.voyageone.common.components.sears.base.SearsBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Jonas on 10/23/15.
 */
@Component("SearsAnalySisJob")
public class SearsAnalySisJob extends BaseTaskJob {
    @Autowired
    private SearsAnalysisService analysisService;

    @Override
    protected BaseTaskService getTaskService() {
        return analysisService;
    }
}
