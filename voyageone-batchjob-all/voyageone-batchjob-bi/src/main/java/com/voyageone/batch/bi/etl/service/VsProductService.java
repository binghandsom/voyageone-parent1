package com.voyageone.batch.bi.etl.service;

import org.springframework.stereotype.Service;

@Service
public class VsProductService extends BaseBiKettleService {
	
	private final static String TASK_NAME = "vs_product_v";
	private final static String KBJ_FILE_NAME = "vs_product_v";
	
	public VsProductService() {
		super.taskName = TASK_NAME;
		super.jobXmlFileName = KBJ_FILE_NAME;
	}

}
