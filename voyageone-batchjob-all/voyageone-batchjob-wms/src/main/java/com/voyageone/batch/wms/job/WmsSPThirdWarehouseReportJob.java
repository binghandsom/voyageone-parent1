package com.voyageone.batch.wms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.wms.service.WmsSPThirdWarehouseReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 生成给斯伯丁第三方仓库发货日报
 *
 * @author Fred
 */
@Component("wmsSPThirdWarehouseReportTask")
public class WmsSPThirdWarehouseReportJob  extends BaseTaskJob {

    @Autowired
    WmsSPThirdWarehouseReportService wmsSPThirdWarehouseReportService;

    @Override
    protected BaseTaskService getTaskService() {
        return wmsSPThirdWarehouseReportService;
    }
}
