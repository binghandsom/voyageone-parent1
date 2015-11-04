package com.voyageone.batch.ims.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.ims.service.ImsPromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by james.li on 2015/10/29.
 */
@Component("ImsPromotionTask")
public class ImsPromotionJob extends BaseTaskJob {
    @Autowired
    private ImsPromotionService imsPromotionService;

    @Override
    protected BaseTaskService getTaskService() {
        return imsPromotionService;
    }
}
