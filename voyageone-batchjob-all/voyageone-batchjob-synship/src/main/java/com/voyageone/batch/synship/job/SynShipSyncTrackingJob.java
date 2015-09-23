package com.voyageone.batch.synship.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.synship.service.tracking.sync.SynShipSyncTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 运单同步任务，同步运单到各平台
 *
 * Created by Jack on 15/8/1.
 */
@Component("synShipSyncTrackingTask")
public class SynShipSyncTrackingJob extends BaseTaskJob {

    @Autowired
    private SynShipSyncTrackingService synShipSyncTrackingService;

    @Override
    protected BaseTaskService getTaskService() {
        return synShipSyncTrackingService;
    }
}
