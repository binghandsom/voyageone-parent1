package com.voyageone.batch.bi.etl.service;

import org.springframework.stereotype.Service;

@Service
public class VtOrderService extends BaseBiKettleService {
	
	private final static String TASK_NAME = "vt_order";
	private final static String KBJ_FILE_NAME = "vt_order";
	
	public VtOrderService() {
		super.taskName = TASK_NAME;
		super.jobXmlFileName = KBJ_FILE_NAME;
	}

}
