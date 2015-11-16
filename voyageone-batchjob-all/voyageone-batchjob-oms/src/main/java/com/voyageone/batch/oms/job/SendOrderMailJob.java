package com.voyageone.batch.oms.job;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.dao.TaskDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.oms.service.SendOrderMailService;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;

	public class SendOrderMailJob {
		private static Log logger = LogFactory.getLog(SendOrderMailJob.class);
		
		@Autowired
		private TaskDao taskDao;
		
		@Autowired
		private SendOrderMailService sendOrderMailService;
		
		@Autowired
		IssueLog issueLog;
		
		public final static String taskCheck = "SendOrderMailJob";
		
		public final static String taskCheckTopSpendingRanking = "SpendingTopRanking";
			
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

			boolean isSuccess = sendOrderMailService.sendOrderMail();

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
		
		/**
		 * 统计消费前多少名顾客
		 */
		public void sendTopSpendingRanking() {
			String taskNameReal = taskCheckTopSpendingRanking;

			List<TaskControlBean> taskControlList = taskDao.getTaskControlList(taskNameReal);
			// 是否可以运行的判断
			if (TaskControlUtils.isRunnable(taskControlList, taskNameReal) == false) {
				return;
			}

			String taskID = TaskControlUtils.getTaskId(taskControlList);
			logger.info(taskNameReal + "任务开始");
			// 任务监控历史记录添加:启动
			taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());

			// 允许运行的订单渠道取得
			List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

			boolean isSuccess = true;

			// 需要发邮件的渠道
			for (String orderChannelId : orderChannelIdList) {
				try {
					String topCountStr =  TaskControlUtils.getVal2(taskControlList, TaskControlEnums.Name.order_channel_id, orderChannelId);
					isSuccess = sendOrderMailService.sendTopSpendingRankingMail(orderChannelId, Integer.valueOf(topCountStr));

				} catch (Exception ex) {
					isSuccess = false;

					logger.error(ex.getMessage(), ex);
					issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
				}
			}

			// 任务监控历史记录添加:结束
			String result = "";
			if (isSuccess) {
				result = TaskControlEnums.Status.SUCCESS.getIs();
			} else {
				result = TaskControlEnums.Status.ERROR.getIs();
			}
			taskDao.insertTaskHistory(taskID, result);

			logger.info(taskNameReal + "任务结束");
		}
	}
