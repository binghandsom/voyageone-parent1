package com.voyageone.batch.bi.etl.service;

import org.springframework.stereotype.Service;

@Service
public class VfShipmentPackageService extends BaseBiKettleService {

	private final static String TASK_NAME = "shipment_package";
	private final static String KBJ_FILE_NAME = "shipment_package";

	public VfShipmentPackageService() {
		super.taskName = TASK_NAME;
		super.jobXmlFileName = KBJ_FILE_NAME;
	}


}
