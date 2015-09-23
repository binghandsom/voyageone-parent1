package com.voyageone.batch.wms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.wms.service.WmsGetClientInvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 获取第三方库存Job
 * @author sky
 *
 */
@Component("wmsGetClientInvTask")
public class WmsGetClientInvJob extends BaseTaskJob {

	@Autowired
	WmsGetClientInvService wmsGetClientInvService;

	@Override
	protected BaseTaskService getTaskService() {
		return wmsGetClientInvService;
	}

}
