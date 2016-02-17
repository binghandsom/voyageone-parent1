package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.CmsPromotrionService;
import org.springframework.stereotype.Component;

/**
 * @author james.li on 2016/2/16.
 * @version 2.0.0
 */
@Component("CmsPromotionJob")
public class CmsPromotionJob extends BaseTaskJob {
    private CmsPromotrionService cmsPromotrionService;
    @Override
    protected BaseTaskService getTaskService() {
        return null;
    }
}
