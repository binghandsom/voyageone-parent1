package com.voyageone.task2.cms.job.platform;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.platform.CmsGetPlatformStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 获取商品在平台的实际上下架状态
 * Created by jason.jiang on 2016/08/03
 */
@Component("CmsGetPlatformStatusJob")
public class CmsGetPlatformStatusJob extends BaseTaskJob {
	@Autowired
	private CmsGetPlatformStatusService cmsGetPlatformStatusService;

	@Override
	protected BaseTaskService getTaskService() {
		return cmsGetPlatformStatusService;
	}
}
