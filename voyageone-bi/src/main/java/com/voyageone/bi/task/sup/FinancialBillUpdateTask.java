package com.voyageone.bi.task.sup;

import com.voyageone.batch.bi.etl.service.BaseBiKettleService;
import org.springframework.stereotype.Service;

@Service
public class FinancialBillUpdateTask extends BaseBiKettleService {

	private final static String TASK_NAME = "tax_transpotation";
	private final static String KBJ_FILE_NAME = "tax_transpotation";

	public FinancialBillUpdateTask() {
		super.taskName = TASK_NAME;
		super.jobXmlFileName = KBJ_FILE_NAME;
	}


}
