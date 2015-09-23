package com.voyageone.batch.bi.etl.service;

import org.springframework.stereotype.Service;

@Service
public class VmBrandService extends BaseBiKettleService {
	
	private final static String TASK_NAME = "vm_brand";
	private final static String KBJ_FILE_NAME = "vm_brand";
	
	public VmBrandService() {
		super.taskName = TASK_NAME;
		super.jobXmlFileName = KBJ_FILE_NAME;
	}
}
