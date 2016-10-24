package com.voyageone.task2.cms.job.search;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.search.CmsProductTotalImportToDistSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Cms Product Data 全量导入 Search Server JOB
 *
 * @author chuanyu.liang 2016/9/30.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component("CmsProductTotalImportToDistSearchJob")
public class CmsProductTotalImportToDistSearchJob extends BaseTaskJob {
    @Autowired
    private CmsProductTotalImportToDistSearchService cmsProductTotalImportToDistSearchService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsProductTotalImportToDistSearchService;
    }
}
