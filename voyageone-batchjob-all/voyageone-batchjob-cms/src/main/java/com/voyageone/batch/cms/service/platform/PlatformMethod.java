package com.voyageone.batch.cms.service.platform;

import com.voyageone.batch.cms.bean.platform.SxWorkLoadBean;

/**
 * Created by zhujiaye on 16/2/14.
 */
public abstract class PlatformMethod {

	/**
	 * 执行完一个workload之后, 更新workload表的状态信息
	 * 如果有错误的话, 更新错误信息
	 * @param workLoadBean 当前执行的workload
	 * @param publishStatus 执行结果状态
	 */
	protected void doComplete(SxWorkLoadBean workLoadBean, int publishStatus) {
		// TODO:更新workload信息

	}

}
