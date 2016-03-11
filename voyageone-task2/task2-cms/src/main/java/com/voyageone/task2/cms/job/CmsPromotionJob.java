package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsPromotrionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author james.li on 2016/2/16.
 * @version 2.0.0
 */
@Component("CmsPromotionJob")
public class CmsPromotionJob extends BaseTaskJob {
    @Autowired
    private CmsPromotrionService cmsPromotrionService;
    @Override
    protected BaseTaskService getTaskService() {
        return cmsPromotrionService;
    }
}
