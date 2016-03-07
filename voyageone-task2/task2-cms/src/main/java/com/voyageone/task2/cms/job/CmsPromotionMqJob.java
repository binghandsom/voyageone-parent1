package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseMQTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsPromotionMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author aooer 2016/3/4.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component
public class CmsPromotionMqJob extends BaseMQTaskJob {

    @Autowired
    private CmsPromotionMqService cmsPromotionMqService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsPromotionMqService;
    }
}
