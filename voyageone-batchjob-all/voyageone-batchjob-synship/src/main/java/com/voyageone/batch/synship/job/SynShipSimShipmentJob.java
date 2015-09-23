package com.voyageone.batch.synship.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.synship.service.SynShipSimShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 模拟第三方渠道的发货（主要是针对第三方仓库自己发货的）
 *
 * Created by Jack on 15/8/1.
 */
@Component("synShipSimShipmentTask")
public class SynShipSimShipmentJob extends BaseTaskJob {

    @Autowired
    private SynShipSimShipmentService synShipSimShipmentService;

    @Override
    protected BaseTaskService getTaskService() {
        return synShipSimShipmentService;
    }
}
