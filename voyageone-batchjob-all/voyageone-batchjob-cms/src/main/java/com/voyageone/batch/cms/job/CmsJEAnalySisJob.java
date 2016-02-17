package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.feed.JewelryAnalysisService;
import com.voyageone.batch.cms.service.feed.SearsAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Jonas on 10/23/15.
 */
@Component("CmsJEAnalySisJob")
public class CmsJEAnalySisJob extends BaseTaskJob {
    @Autowired
    private JewelryAnalysisService jewelryAnalysisService;

    @Override
    protected BaseTaskService getTaskService() {
        return jewelryAnalysisService;
    }
}
