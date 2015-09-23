package com.voyageone.batch.bi.etl.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.voyageone.batch.bi.etl.service.BaseBiKettleService;
import com.voyageone.batch.bi.etl.service.VsProductIIDService;

@Component("biVsProductIIDSyncJob")
public class BiVsProductIIDSyncJob extends BaseBiTaskJob {
	
	@Autowired
	VsProductIIDService vsProductIIDService;

    @Override
    protected BaseBiKettleService[] getTaskServices() {
    	return new BaseBiKettleService[]{vsProductIIDService};
    }
}
