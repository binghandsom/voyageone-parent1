package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.CmsStatisticsReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by james.li on 2015/10/20.
 */
@Component("cmsStatisticsReportTask")
public class CmsStatisticsReportJob extends BaseTaskJob {

    @Autowired
    private CmsStatisticsReportService cmsStatisticsReportService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsStatisticsReportService;
    }
}
