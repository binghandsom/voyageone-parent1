package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.OmsPostGiltCancelOrderService;
import com.voyageone.batch.cms.service.OmsPostGiltNormalOrderService;
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


