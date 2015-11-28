package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.CmsSynInventoryService;
import com.voyageone.batch.cms.service.feed.SearsAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Neil on 27/11/15.
 */
@Component("cmsSynInventoryJob")
public class CmsSynInventoryJob extends BaseTaskJob {
    @Autowired
    private CmsSynInventoryService cmsSynInventoryService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsSynInventoryService;
    }
}
