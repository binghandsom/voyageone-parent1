package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.VtmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author morse.lu
 * @version 0.0.1, 16/3/4
 */
@Component("CmsVtmJob")
public class CmsVtmJob extends BaseTaskJob {
    @Autowired
    private VtmService vtmService;

    @Override
    protected BaseTaskService getTaskService() {
        return vtmService;
    }
}
