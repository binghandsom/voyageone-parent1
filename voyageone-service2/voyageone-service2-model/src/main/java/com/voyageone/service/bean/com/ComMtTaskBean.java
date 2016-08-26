package com.voyageone.service.bean.com;

import java.util.List;

import com.voyageone.service.model.com.ComMtTaskModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/18
 */
public class ComMtTaskBean extends ComMtTaskModel {
	
	private String runFlg;
	
	private List<TmTaskControlBean> taskConfig;

	public String getRunFlg() {
		return runFlg;
	}

	public void setRunFlg(String runFlg) {
		this.runFlg = runFlg;
	}

	public List<TmTaskControlBean> getTaskConfig() {
		return taskConfig;
	}

	public void setTaskConfig(List<TmTaskControlBean> taskConfig) {
		this.taskConfig = taskConfig;
	}
	
}
