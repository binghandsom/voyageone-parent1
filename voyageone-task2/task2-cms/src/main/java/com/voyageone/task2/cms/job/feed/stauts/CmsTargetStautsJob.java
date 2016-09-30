package com.voyageone.task2.cms.job.feed.stauts;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.ShoeZooAnalysisService;
import com.voyageone.task2.cms.service.feed.status.TargetSkuStatusCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author james
 * @version 0.0.1, 16/4/29
 */
@Component("CmsTargetStautsJob")
public class CmsTargetStautsJob extends BaseTaskJob {
    @Autowired
    private TargetSkuStatusCheckService targetSkuStatusCheckService;
    @Override
    protected BaseTaskService getTaskService() {
        return targetSkuStatusCheckService;
    }
}
