package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsJmMallPriceReductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by james on 2016/10/18.
 */
@Component("CmsJmMallPriceReductionJob")
public class CmsJmMallPriceReductionJob extends BaseTaskJob {

    @Autowired
    CmsJmMallPriceReductionService cmsJmMallPriceReductionService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsJmMallPriceReductionService;
    }
}
