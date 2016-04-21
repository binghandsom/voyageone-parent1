package com.voyageone.task2.cms.job.promotion.stock;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.promotion.stock.StockSeparateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author morse.lu
 * @version 0.0.1, 16/3/29
 */
@Component("CmsStockSeparateJob")
public class CmsStockSeparateJob extends BaseTaskJob {
    @Autowired
    private StockSeparateService stockSeparateService;

    @Override
    protected BaseTaskService getTaskService() {
        return stockSeparateService;
    }
}
