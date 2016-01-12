package com.voyageone.batch.bi.etl.service;

import org.springframework.stereotype.Service;

@Service
public class VfTaxTranspotationService extends BaseBiKettleService {

	private final static String TASK_NAME = "tax_transpotation";
	private final static String KBJ_FILE_NAME = "tax_transpotation";

	public VfTaxTranspotationService() {
		super.taskName = TASK_NAME;
		super.jobXmlFileName = KBJ_FILE_NAME;
	}

}
