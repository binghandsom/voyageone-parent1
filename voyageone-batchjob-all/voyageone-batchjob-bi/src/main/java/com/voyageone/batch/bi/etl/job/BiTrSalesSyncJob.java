package com.voyageone.batch.bi.etl.job;

import com.voyageone.batch.bi.etl.service.BaseBiKettleService;
import com.voyageone.batch.bi.etl.service.TrSalesService;
import com.voyageone.batch.bi.etl.service.VtStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("biTrSalesSyncJob")
public class BiTrSalesSyncJob extends BaseBiTaskJob {
	
	@Autowired
    TrSalesService trSalesService;
	
    @Override
    protected BaseBiKettleService[] getTaskServices() {
		return new BaseBiKettleService[] { trSalesService };
    }
}
