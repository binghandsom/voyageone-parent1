package com.voyageone.batch.synship.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.synship.service.SynShipPreShipChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 预处理发货渠道，当发货渠道发生变化时，重新设置发货渠道
 *
 * Created by jack on 11/5/15.
 */
@Component("synShipPreShipChannelTask")
public class SynShipPreShipChannelJob extends BaseTaskJob {

    @Autowired
    private SynShipPreShipChannelService service;

    @Override
    protected BaseTaskService getTaskService() {
        return service;
    }
}
