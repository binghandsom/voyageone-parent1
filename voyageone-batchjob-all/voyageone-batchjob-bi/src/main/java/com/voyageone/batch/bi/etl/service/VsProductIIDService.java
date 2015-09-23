package com.voyageone.batch.bi.etl.service;

import org.springframework.stereotype.Service;

@Service
public class VsProductIIDService extends BaseBiKettleService {
	
	private final static String TASK_NAME = "vs_prodoct_iid";
	private final static String KBJ_FILE_NAME = "vs_prodoct_iid";
	
	public VsProductIIDService() {
		super.taskName = TASK_NAME;
		super.jobXmlFileName = KBJ_FILE_NAME;
	}

}
