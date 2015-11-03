package com.voyageone.batch.oms.job;

import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.dao.TaskDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.oms.service.PostBCBGOrderService;
import com.voyageone.batch.oms.service.PostRMOrderService;
import com.voyageone.common.components.issueLog.IssueLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PostBCBGOrderJob {
	
	private static Log logger = LogFactory.getLog(PostBCBGOrderJob.class);
	
	@Autowired
	TaskDao taskDao;
	
	@Autowired
	PostBCBGOrderService postBCBGOrderService;
	
	@Autowired
	IssueLog issueLog;

	// taskName
	// 正常订单推送（Shipped）
	private final static String POST_BCBG_DAILY_SALES = "PostBCBGDailySales";

	// 未发货订单推送（Not Shipped）
	private final static String POST_BCBG_DEMAND = "PostBCBGDemand";

	/**
	 * 正常订单推送（Shipped）
	 */
	public void postBCBGDailySales() {
		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(POST_BCBG_DAILY_SALES);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, POST_BCBG_DAILY_SALES) == false) {
			return;
		}
		String taskID =  TaskControlUtils.getTaskId(taskControlList);
		logger.info(POST_BCBG_DAILY_SALES + "任务开始");
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());
		
		boolean isSuccess = true;

		isSuccess = postBCBGOrderService.postBCBGDailySales();
		
		// 任务监控历史记录添加:结束
		String result = "";
		if (isSuccess) {
			result = TaskControlEnums.Status.SUCCESS.getIs();
		} else {
			result = TaskControlEnums.Status.ERROR.getIs();
		}
		taskDao.insertTaskHistory(taskID, result);

		logger.info(POST_BCBG_DAILY_SALES + "任务结束");
	}

	/**
	 * 未发货订单推送（Not Shipped）
	 */
	public void postBCBGDemand() {

		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(POST_BCBG_DEMAND);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, POST_BCBG_DEMAND) == false) {
			return;
		}
		String taskID =  TaskControlUtils.getTaskId(taskControlList);
		logger.info(POST_BCBG_DEMAND + "任务开始");
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());

		boolean isSuccess = true;

		isSuccess = postBCBGOrderService.postBCBGDemand();

		// 任务监控历史记录添加:结束
		String result = "";
		if (isSuccess) {
			result = TaskControlEnums.Status.SUCCESS.getIs();
		} else {
			result = TaskControlEnums.Status.ERROR.getIs();
		}
		taskDao.insertTaskHistory(taskID, result);

		logger.info(POST_BCBG_DEMAND + "任务结束");
	}
}

