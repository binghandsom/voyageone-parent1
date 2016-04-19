package com.voyageone.task2.cms.job.promotion.stock;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.promotion.stock.StockIncrementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jeff.duan
 * @version 0.0.1, 16/3/31
 */
@Component("CmsStockIncrementJob")
public class CmsStockIncrementJob extends BaseTaskJob {
    @Autowired
    private StockIncrementService stockIncrementService;

    @Override
    protected BaseTaskService getTaskService() {
        return stockIncrementService;
    }
}
