package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseMQTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.MQConsumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author aooer 2016/2/29.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component("mqConsumJob")
public class MqConsumJob extends BaseMQTaskJob {

    @Autowired
    private MQConsumService consumService;

    @Override
    protected BaseTaskService getTaskService() {
        return consumService;
    }
}
