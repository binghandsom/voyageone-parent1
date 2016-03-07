package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.OmsPostGiltNormalOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jerry on 2016/02/16.
 */
@Component("OmsPostGiltNormalOrderJob")
public class OmsPostGiltNormalOrderJob extends BaseTaskJob {
	@Autowired
	private OmsPostGiltNormalOrderService omsPostGiltNormalOrderService;

	@Override
	protected BaseTaskService getTaskService() {
		return omsPostGiltNormalOrderService;
	}
}


