package com.voyageone.batch.wms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.wms.service.WmsBCBGInventoryReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 生成BCBG日报（以SKU集计物理库存）
 *
 * @author Fred
 */
@Component("wmsBCBGInventoryReportingTask")
public class WmsBCBGInventoryReportingJob  extends BaseTaskJob {

    @Autowired
    WmsBCBGInventoryReportingService wmsBCBGInventoryReportingService;

    @Override
    protected BaseTaskService getTaskService() {
        return wmsBCBGInventoryReportingService;
    }
}
