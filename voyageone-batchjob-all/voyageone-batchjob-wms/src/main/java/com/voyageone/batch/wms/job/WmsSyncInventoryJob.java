package com.voyageone.batch.wms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.wms.service.inventory.sync.WmsSyncInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 库存同步任务，同步库存变动到独立域名，和其他第三方
 *
 * Created by jonas on 15/6/1.
 */
@Component("wmsSyncInventoryJobTask")
public class WmsSyncInventoryJob extends BaseTaskJob {

    @Autowired
    private WmsSyncInventoryService wmsSyncInventoryService;

    @Override
    protected BaseTaskService getTaskService() {
        return wmsSyncInventoryService;
    }
}
