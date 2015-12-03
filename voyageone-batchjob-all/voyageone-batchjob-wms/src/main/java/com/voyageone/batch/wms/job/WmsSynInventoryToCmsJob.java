package com.voyageone.batch.wms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.wms.service.WmsSynInventoryToCmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Neil on 27/11/15.
 */
@Component("wmsSynInventoryToCmsJob")
public class WmsSynInventoryToCmsJob extends BaseTaskJob {
    @Autowired
    private WmsSynInventoryToCmsService wmsSynInventoryToCmsService;

    @Override
    protected BaseTaskService getTaskService() {
        return wmsSynInventoryToCmsService;
    }
}
