package com.voyageone.batch.oms.job;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.dao.TaskDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.oms.service.SendSameSourceOrderIdMailService;
/**
 * 
 * @author eric
 *
 */
public class SendSameOrderMailJob {
	
	  	private static Log logger = LogFactory.getLog(SendSameOrderMailJob.class);
	
		@Autowired
		private TaskDao taskDao;
		
		@Autowired
		private SendSameSourceOrderIdMailService sendSameSourceOrderIdMailService;
			
		public final static String taskCheck = "SendSameOrderMailJob";
		
		public void run(){
			
			List<TaskControlBean> taskControlList = taskDao.getTaskControlList(taskCheck);
			// 是否可以运行的判断
			if (TaskControlUtils.isRunnable(taskControlList, taskCheck) == false) {
				return;
			}
			
			String taskID =  TaskControlUtils.getTaskId(taskControlList);
			logger.info(taskCheck + "任务开始");
			
			// 任务监控历史记录添加:启动
			taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());
			
			boolean isSuccess = sendSameSourceOrderIdMailService.sendSameSourceOrderIdMail();
			
			// 任务监控历史记录添加:结束
			String result = "";
			if (isSuccess) {
				result = TaskControlEnums.Status.SUCCESS.getIs();
			} else {
				result = TaskControlEnums.Status.ERROR.getIs();
			}
			taskDao.insertTaskHistory(taskID, result);
	
			logger.info(taskCheck + "任务结束");
	}
}
