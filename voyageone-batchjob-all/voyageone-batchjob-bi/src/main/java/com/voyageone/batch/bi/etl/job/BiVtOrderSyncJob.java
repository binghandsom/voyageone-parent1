package com.voyageone.batch.bi.etl.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.voyageone.batch.bi.etl.service.BaseBiKettleService;
import com.voyageone.batch.bi.etl.service.CleanCacheService;
import com.voyageone.batch.bi.etl.service.VsProductLiftCycleService;
import com.voyageone.batch.bi.etl.service.VsProductService;
import com.voyageone.batch.bi.etl.service.VtOrderService;

@Component("biVtOrderSyncJob")
public class BiVtOrderSyncJob extends BaseBiTaskJob {
	
	@Autowired
	VtOrderService vtOrderService;

	@Autowired
	VsProductService vsProductService;

	@Autowired
	VsProductLiftCycleService vsProductLiftCycleService;
	
	@Autowired
	CleanCacheService cleanCacheService;
	
    @Override
    protected BaseBiKettleService[] getTaskServices() {
		return new BaseBiKettleService[] { 
				vtOrderService, 
				vsProductService,
				vsProductLiftCycleService, 
				cleanCacheService };
    }
}
