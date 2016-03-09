package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.OmsPostGiltCancelOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jerry on 2016/02/16.
 */
@Component("OmsPostGiltCancelOrderJob")
public class OmsPostGiltCancelOrderJob extends BaseTaskJob {
	@Autowired
	private OmsPostGiltCancelOrderService omsPostGiltCancelOrderService;

	@Override
	protected BaseTaskService getTaskService() {
		return omsPostGiltCancelOrderService;
	}
}


