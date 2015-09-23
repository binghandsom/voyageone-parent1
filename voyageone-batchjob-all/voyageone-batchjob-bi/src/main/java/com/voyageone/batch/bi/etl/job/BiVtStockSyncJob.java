package com.voyageone.batch.bi.etl.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.voyageone.batch.bi.etl.service.BaseBiKettleService;
import com.voyageone.batch.bi.etl.service.VtStockService;

@Component("biVtStockSyncJob")
public class BiVtStockSyncJob extends BaseBiTaskJob {
	
	@Autowired
	VtStockService vtStockService;
	
    @Override
    protected BaseBiKettleService[] getTaskServices() {
		return new BaseBiKettleService[] { vtStockService };
    }
}
