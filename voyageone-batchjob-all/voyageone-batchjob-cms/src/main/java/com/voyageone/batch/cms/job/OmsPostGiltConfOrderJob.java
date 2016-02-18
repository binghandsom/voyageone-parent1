package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.OmsPostGiltConfOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jerry on 2016/02/16.
 */
@Component("OmsPostGiltConfOrderJob")
public class OmsPostGiltConfOrderJob extends BaseTaskJob {
	@Autowired
	private OmsPostGiltConfOrderService omsPostGiltConfOrderService;

	@Override
	protected BaseTaskService getTaskService() {
		return omsPostGiltConfOrderService;
	}
}


