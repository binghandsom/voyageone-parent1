package com.voyageone.batch.bi.etl.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.voyageone.batch.bi.etl.service.BaseBiKettleService;
import com.voyageone.batch.bi.etl.service.VmBrandService;
import com.voyageone.batch.bi.etl.service.VmSkuSyncService;

@Component("biVmMasterSyncJob")
public class BiVmMasterSyncJob extends BaseBiTaskJob {
	
	@Autowired
	VmBrandService vmBrandSyncService;
	@Autowired
	VmSkuSyncService vmSkuSyncService;
	
    @Override
    protected BaseBiKettleService[] getTaskServices() {
        return new BaseBiKettleService[]{vmBrandSyncService, vmSkuSyncService};
    }

}
