package com.voyageone.task2.cms.job.product;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsDataAmountService;
import com.voyageone.task2.cms.service.product.PlatformForcedInStockProduct_AutoOnSaleJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by dell on 2016/7/5.
 */
@Component
public class PlatformForcedInStockProduct_AutoOnSaleJob extends BaseTaskJob {

    @Autowired
    PlatformForcedInStockProduct_AutoOnSaleJobService service;

    @Override
    protected BaseTaskService getTaskService() {
        return service;
    }
}
