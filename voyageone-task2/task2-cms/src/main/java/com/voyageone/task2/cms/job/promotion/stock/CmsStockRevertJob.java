package com.voyageone.task2.cms.job.promotion.stock;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.promotion.stock.StockRevertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author morse.lu
 * @version 0.0.1, 16/3/29
 */
@Component("CmsStockRevertJob")
public class CmsStockRevertJob extends BaseTaskJob {
    @Autowired
    private StockRevertService stockRevertService;

    @Override
    protected BaseTaskService getTaskService() {
        return stockRevertService;
    }
}
