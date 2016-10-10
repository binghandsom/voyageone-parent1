package com.voyageone.service.bean.com;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyageone.service.model.com.ComMtTaskModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/18
 */
public class ComMtTaskBean extends ComMtTaskModel {
	
	private String runFlg;
	
	private List<TmTaskControlBean> taskConfig;
	
	@JsonIgnore
	public void setTaskId(String taskId) {
		if (StringUtils.startsWith(taskId, "X")) {
			super.setTaskId(null);
		} else {
			super.setTaskId(Integer.valueOf(taskId));
		}
	}

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
