package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsSynInventoryToCmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Neil on 27/11/15.
 */
@Component("CmsSynInventoryToCmsJob")
public class CmsSynInventoryToCmsJob extends BaseTaskJob {
    @Autowired
    private CmsSynInventoryToCmsService cmsSynInventoryToCmsService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsSynInventoryToCmsService;
    }
}
