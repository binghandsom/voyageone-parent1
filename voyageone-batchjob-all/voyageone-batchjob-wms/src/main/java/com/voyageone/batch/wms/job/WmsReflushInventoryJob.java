package com.voyageone.batch.wms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.wms.service.inventory.WmsReFlushInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jonas on 15/6/8.
 */
@Component("wmsReflushInventoryJobTask")
public class WmsReflushInventoryJob extends BaseTaskJob {

    @Autowired
    private WmsReFlushInventoryService wmsReFlushInventoryService;

    @Override
    protected BaseTaskService getTaskService() {
        return wmsReFlushInventoryService;
    }
}
