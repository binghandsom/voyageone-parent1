package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.ShoeMetroAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author gjl
 * @version 0.0.1, 16/07/27
 */
@Component("CmsShoeMetroAnalysisJob")
public class CmsShoeMetroAnalysisJob extends BaseTaskJob {
    @Autowired
    private ShoeMetroAnalysisService shoeMetroAnalysisService;
    @Override
    protected BaseTaskService getTaskService() {
        return shoeMetroAnalysisService;
    }
}
