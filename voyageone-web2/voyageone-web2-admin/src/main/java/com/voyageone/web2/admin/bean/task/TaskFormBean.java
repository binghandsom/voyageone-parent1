package com.voyageone.web2.admin.bean.task;

import com.voyageone.web2.admin.bean.AdminFormBean;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/18
 */
public class TaskFormBean extends AdminFormBean {
	
	private Integer taskId;
	
	private String taskType;
	
	private String taskName;
	
	private String taskComment;
	
	private String taskFreq;
	
	private String runFlg;

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskComment() {
		return taskComment;
	}

	public void setTaskComment(String taskComment) {
		this.taskComment = taskComment;
	}

	public String getTaskFreq() {
		return taskFreq;
	}

	public void setTaskFreq(String taskFreq) {
		this.taskFreq = taskFreq;
	}

	public String getRunFlg() {
		return runFlg;
	}

	public void setRunFlg(String runFlg) {
		this.runFlg = runFlg;
	}
	
}
