package com.voyageone.service.bean.com;

import com.voyageone.service.model.com.TmTaskControlModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/18
 */
public class TmTaskControlBean extends TmTaskControlModel {
	
	private String taskName;
	
	@Override
	public void setCfgVal2(String cfgVal2) {
		if (cfgVal2 == null) {
			super.setCfgVal2("");
		} else {
			super.setCfgVal2(cfgVal2);
		}
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
}
