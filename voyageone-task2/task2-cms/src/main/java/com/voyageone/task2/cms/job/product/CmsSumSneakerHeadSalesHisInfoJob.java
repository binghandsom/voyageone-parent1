package com.voyageone.task2.cms.job.product;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.product.sales.CmsSumSneakerHeadSalesService;
import org.springframework.stereotype.Component;

/**
 * Created by gjl on 2016/11/24.
 */
@Component("CmsSumSneakerHeadSalesHisInfoJob")
public class CmsSumSneakerHeadSalesHisInfoJob extends BaseTaskJob {

    private CmsSumSneakerHeadSalesService cmsSumSneakerHeadSalesService;
    @Override
    protected BaseTaskService getTaskService() {
        return cmsSumSneakerHeadSalesService;
    }
}
