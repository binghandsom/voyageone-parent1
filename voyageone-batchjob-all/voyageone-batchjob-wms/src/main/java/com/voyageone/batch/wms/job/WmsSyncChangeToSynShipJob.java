package com.voyageone.batch.wms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.wms.service.WmsSyncChangeToSynShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 盘点后进行库存盘点
 * 盘点之后进行库存比较，有差异出现的情况下，将差异值记入比较表以供仓库人员进行操作
 *
 * @author Jack
 */
@Component("wmsSyncChangeToSynShipTask")
public class WmsSyncChangeToSynShipJob   extends BaseTaskJob {

    @Autowired
    WmsSyncChangeToSynShipService wmsSyncChangeToSynShipService;

    @Override
    protected BaseTaskService getTaskService() {
        return wmsSyncChangeToSynShipService;
    }
}
