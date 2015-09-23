package com.voyageone.batch.wms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.wms.service.WmsAllotInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 库存分配
 * 库存分配（CMS未上线时的临时任务）
 *
 * @author Jack
 */
@Component("wmsAllotInventoryTask")
public class WmsAllotInventoryJob extends BaseTaskJob {

    @Autowired
    WmsAllotInventoryService wmsAllotInventoryService;

    @Override
    protected BaseTaskService getTaskService() {
        return wmsAllotInventoryService;
    }

}
