package com.voyageone.batch.bi.etl.service;

import org.springframework.stereotype.Service;

@Service
public class VmSkuSyncService extends BaseBiKettleService {
	
	private final static String TASK_NAME = "vm_sku";
	private final static String KBJ_FILE_NAME = "vm_sku";
	
	public VmSkuSyncService() {
		super.taskName = TASK_NAME;
		super.jobXmlFileName = KBJ_FILE_NAME;
	}

}
