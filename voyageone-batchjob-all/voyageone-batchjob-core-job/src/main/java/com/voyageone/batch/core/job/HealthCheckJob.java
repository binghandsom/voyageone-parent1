package com.voyageone.batch.core.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.service.HealthCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 心跳检查
 *
 * @author Jacky
 */
@Component("healthCheckTask")
public class HealthCheckJob extends BaseTaskJob {

    @Autowired
    HealthCheckService healthCheckService;

    @Override
    protected BaseTaskService getTaskService() {
        return healthCheckService;
    }

}
