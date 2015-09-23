package com.voyageone.batch.bi.spider.job;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.common.components.issueLog.enums.SubSystem;

public abstract class BaseSpiderService extends BaseTaskService {

	@Override
	public SubSystem getSubSystem() {
		return null;
	}

    /**
     * 默认公开的启动入口
     */
	@Override
    public void startup() {
        try {
            onStartup(null);
        } catch (Exception ex) {
        	//e.printStackTrace();
        	logger.error(ex.getMessage(), ex);
        }
    }
	
}
