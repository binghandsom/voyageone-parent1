package com.voyageone.task2.cms.job.product;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.product.CmsCheckProductIsRightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 每天check各个channel的product数据是否正确,主要用于检测产品的状态和sku数据
 *
 * @author edward.lin 2017/03/06
 * @version 2.15.0
 */
@Component("CmsCheckProductIsRightJob")
public class CmsCheckProductIsRightJob extends BaseTaskJob {

	@Autowired
	private CmsCheckProductIsRightService cmsCheckProductIsRightService;

	@Override
	protected BaseTaskService getTaskService() {
		return cmsCheckProductIsRightService;
	}
}
