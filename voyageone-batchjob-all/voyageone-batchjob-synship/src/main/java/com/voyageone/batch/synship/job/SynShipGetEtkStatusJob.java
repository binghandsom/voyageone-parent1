package com.voyageone.batch.synship.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.synship.service.SynShipGetEtkStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 轮询获得ＥＴＫ面单的状态
 *
 * Created by jack on 9/27/15.
 */
@Component("synShipGetEtkStatusTask")
public class SynShipGetEtkStatusJob extends BaseTaskJob {

    @Autowired
    private SynShipGetEtkStatusService service;

    @Override
    protected BaseTaskService getTaskService() {
        return service;
    }
}
