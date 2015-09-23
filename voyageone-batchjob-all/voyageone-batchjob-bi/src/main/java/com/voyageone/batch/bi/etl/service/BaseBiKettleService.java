package com.voyageone.batch.bi.etl.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.voyageone.batch.bi.util.FileUtils;
import com.voyageone.common.util.DateTimeUtil;

public class BaseBiKettleService {
	private static Log logger = LogFactory.getLog(BaseBiKettleService.class);
	
	private static final ThreadLocal<String> jobXmlPathLocal = new ThreadLocal<String>();
	
	protected String taskName= "";
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public static String getJobXmlPath() {
		return jobXmlPathLocal.get();
	}
	public static void setJobXmlPath(String jobXmlPath) {
		jobXmlPathLocal.remove();
		jobXmlPathLocal.set(jobXmlPath);
	}

	protected String jobXmlFileName= "";
	public String getJobXmlFileName() {
		return jobXmlFileName;
	}
	public void setJobXmlFileName(String jobXmlFileName) {
		this.jobXmlFileName = jobXmlFileName;
	}

	private Map<String, String> params;
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
	public Map<String, String> createParams(int pre) {
		Map<String, String> params = new HashMap<String, String>();
		Date date = DateTimeUtil.addDays(pre);
		String strDate = DateTimeUtil.format(date, "yyyy-MM-dd");
		params.put("start_date", strDate);
		//this.params = params;
		return params;
	}

	//@Autowired
	//protected TaskDao taskDao;
	
	@Autowired
	protected KettleExecuterService kettleExecuter;
	
	
	public void startup() {
		if (!checkTaskRunable(taskName)) {
			logger.info(taskName + "任务开始");
		}
		logger.info(taskName + "任务开始");
		insertTaskHistory(taskName);
		// 任务监控历史记录添加:启动

		boolean isSuccess = true;
		try {
			isSuccess = execute();
		} catch (Exception ex) {
			isSuccess = false;
			logger.error(ex.getMessage(), ex);
			//issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
		}
		
		if (isSuccess) {
			logger.info(taskName + "任务OK");
		} else {
			logger.info(taskName + "任务ERROR");
		}
		logger.info(taskName + "任务结束");
	}
	
	public boolean execute() throws Exception {
		return jobExecute(jobXmlFileName);
	}
	
	public boolean jobExecute(String jobfileName) throws Exception {
		logger.debug(taskName + "execute开始");
		boolean result = false;
		String fileName = getFilePath(jobfileName + ".kjb");
		if (params == null) {
			params = createParams(-7);
		}
		if (fileName != null) {
			result = kettleExecuter.jobExecute(fileName, params);
		} else {
			logger.error("ERROR:" + taskName + ":" + jobXmlPathLocal.get() + ":" + jobfileName +" not found");
		}
		
		params=  null;
		logger.debug(taskName + "execute结束");
		return result;
	}
	
	public boolean tranExecute(String jobfileName) throws Exception {
		logger.debug(taskName + "execute开始");
		
		String fileName = getFilePath(jobfileName + ".ktr");
		if (params == null) {
			params = createParams(-7);
		}
		
		kettleExecuter.transExecute(fileName, params);
		
		params=  null;
		logger.debug(taskName + "execute结束");
		return true;
	}

	
	public boolean checkTaskRunable(String taskName) {
//		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(taskName);
//		// 是否可以运行的判断
//		if (TaskControlUtils.isRunnable(taskControlList, taskName) == false) {
//			return false;
//		}
		return true;
	}
	
	public void insertTaskHistory(String taskName) {
//		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(taskName);
//		String taskID =  TaskControlUtils.getTaskId(taskControlList);
//		// 任务监控历史记录添加:启动
//		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());
	}
	
//	public Resource getResource(String fileName) throws IOException {
//		String ResourceName = "classpath:/kettle_xml/sneakerhead/" + fileName;
//		Resource resource = new ClassPathResource(ResourceName);
//		return resource;
//	}
	
	public String getFilePath(String fileName) throws IOException {
		String fileNameStr = FileUtils.getRootPath() + jobXmlPathLocal.get() + fileName;
		File f = new File(fileNameStr);
		String result = null;
		if (f.exists()) {
			result = f.getAbsolutePath();
		}
		return result;
	}
}
