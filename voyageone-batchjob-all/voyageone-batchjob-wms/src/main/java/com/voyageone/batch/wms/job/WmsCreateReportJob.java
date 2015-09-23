package com.voyageone.batch.wms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.wms.service.WmsCreateReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 生成第三方的销售退货日报
 *
 * @author fred on 2015/8/4.
 */
@Component("wmsCreateReportJobTask")
public class WmsCreateReportJob extends BaseTaskJob {
    @Autowired
    WmsCreateReportService wmsCreateReportService;

    @Override
    protected BaseTaskService getTaskService() {
        return wmsCreateReportService;
    }
}
