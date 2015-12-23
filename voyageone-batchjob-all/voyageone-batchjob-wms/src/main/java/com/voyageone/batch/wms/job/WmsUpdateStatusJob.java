package com.voyageone.batch.wms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.wms.service.WmsUpdateStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 更新订单相关状态
 * 更新订单相关状态
 *
 * @author Jack
 */
@Component("wmsUpdateStatusTask")
public class WmsUpdateStatusJob extends BaseTaskJob {

    @Autowired
    WmsUpdateStatusService wmsUpdateStatusService;

    @Override
    protected BaseTaskService getTaskService() {
        return wmsUpdateStatusService;
    }

}
