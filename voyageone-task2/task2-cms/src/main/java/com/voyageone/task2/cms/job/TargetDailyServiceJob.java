package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.TargetDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author james.li on 2016/9/26.
 * @version 2.0.0
 */
@Component("TargetDailyService")
public class TargetDailyServiceJob extends BaseTaskJob {

    @Autowired
    private TargetDailyService targetDailyService;

    @Override
    protected BaseTaskService getTaskService() {
        return targetDailyService;
    }
}
