package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsOverstockPriceEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author james.li on 2016/7/28.
 * @version 2.0.0
 */
@Component("CmsOverstockPriceEventJob")
public class CmsOverstockPriceEventJob extends BaseTaskJob {

    @Autowired
    private CmsOverstockPriceEventService cmsOverstockPriceEvent;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsOverstockPriceEvent;
    }
}
