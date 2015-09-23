package com.voyageone.batch.bi.etl.service;

import org.springframework.stereotype.Service;

@Service
public class VfTrackOrdersService extends BaseBiKettleService {

	private final static String TASK_NAME = "track_orders";
	private final static String KBJ_FILE_NAME = "track_orders";

	public VfTrackOrdersService() {
		super.taskName = TASK_NAME;
		super.jobXmlFileName = KBJ_FILE_NAME;
	}

}
