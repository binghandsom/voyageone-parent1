package com.voyageone.core.service;

import java.util.List;
import java.util.Map;

import com.voyageone.core.emum.UserEditEnum;
import com.voyageone.core.formbean.InFormUser;

public interface ManagerService {

	/**
	 * 获得用户列表
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getBatchJobList();
	
	public int updateJobRun(Map data);
	
	public int insertBatchJob(Map data);
}
