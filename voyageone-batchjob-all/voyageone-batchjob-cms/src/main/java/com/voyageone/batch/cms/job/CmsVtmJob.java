package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.feed.VtmService;
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
