package com.voyageone.batch.synship.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.synship.service.SynShipSetTakeFlgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 轮询获得订单的物流信息，如果无任何物流信息的话，则自动插入揽收标志
 *
 * Created by jack on 11/5/15.
 */
@Component("synShipSetTakeFlgTask")
public class SynShipSetTakeFlgJob extends BaseTaskJob {

    @Autowired
    private SynShipSetTakeFlgService service;

    @Override
    protected BaseTaskService getTaskService() {
        return service;
    }
}
