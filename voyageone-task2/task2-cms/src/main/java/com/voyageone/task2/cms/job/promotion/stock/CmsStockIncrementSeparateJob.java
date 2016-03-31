package com.voyageone.task2.cms.job.promotion.stock;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.promotion.stock.StockIncrementSeparateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jeff.duan
 * @version 0.0.1, 16/3/31
 */
@Component("CmsStockIncrementSeparateJob")
public class CmsStockIncrementSeparateJob extends BaseTaskJob {
    @Autowired
    private StockIncrementSeparateService stockIncrementSeparateService;

    @Override
    protected BaseTaskService getTaskService() {
        return stockIncrementSeparateService;
    }
}
