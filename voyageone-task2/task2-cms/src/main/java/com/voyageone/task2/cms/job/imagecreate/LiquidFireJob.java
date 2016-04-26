package com.voyageone.task2.cms.job.imagecreate;

import com.voyageone.task2.base.BaseMQTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.imagecreate.LiquidFireJobService;
import org.springframework.stereotype.Service;

@Service
public class LiquidFireJob  extends BaseMQTaskJob {
    LiquidFireJobService service;
    @Override
    protected BaseTaskService getTaskService() {
        return service;
    }
}
