package com.voyageone.task2.cms.job.product;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.product.CmsImportOrdersHisInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jason.jiang on 2016/05/24
 */
@Component
public class CmsImportOrdersHisInfoJob extends BaseTaskJob {
	@Autowired
	private CmsImportOrdersHisInfoService cmsImportOrdersHisInfoService;

	@Override
	protected BaseTaskService getTaskService() {
		return cmsImportOrdersHisInfoService;
	}
}


