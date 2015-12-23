package com.voyageone.batch.oms.job;

import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.dao.TaskDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.oms.service.PostBCBGOrderService;
import com.voyageone.batch.oms.service.PostSearsOrderService;
import com.voyageone.common.components.issueLog.IssueLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PostSearsOrderJob {
	
	private static Log logger = LogFactory.getLog(PostSearsOrderJob.class);
	
	@Autowired
	TaskDao taskDao;
	
	@Autowired
	PostSearsOrderService postSearsOrderService;
	
	@Autowired
	IssueLog issueLog;

	// taskName
	// 正常订单推送（Create Order）
	private final static String POST_SEARS_CREATE_ORDER = "PostSearsCreateOrder";
	// 订单信息取得（Order Lookup）
	private final static String GET_SEARS_ORDER_LOOKUP = "GetSearsOrderLookup";

	/**
	 * 正常订单推送（Create Order）
	 */
	public void postPostSearsCreateOrder() {
		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(POST_SEARS_CREATE_ORDER);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, POST_SEARS_CREATE_ORDER) == false) {
			return;
		}
		String taskID =  TaskControlUtils.getTaskId(taskControlList);
		logger.info(POST_SEARS_CREATE_ORDER + "任务开始");
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());
		
		boolean isSuccess = true;

		isSuccess = postSearsOrderService.postSearsCreateOrder();
		
		// 任务监控历史记录添加:结束
		String result = "";
		if (isSuccess) {
			result = TaskControlEnums.Status.SUCCESS.getIs();
		} else {
			result = TaskControlEnums.Status.ERROR.getIs();
		}
		taskDao.insertTaskHistory(taskID, result);

		logger.info(POST_SEARS_CREATE_ORDER + "任务结束");
	}

	/**
	 * 订单信息取得（Order Lookup）
	 */
	public void getSearsOrderLookup() {
		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(GET_SEARS_ORDER_LOOKUP);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, GET_SEARS_ORDER_LOOKUP) == false) {
			return;
		}
		String taskID =  TaskControlUtils.getTaskId(taskControlList);
		logger.info(GET_SEARS_ORDER_LOOKUP + "任务开始");
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());

		boolean isSuccess = true;

		isSuccess = postSearsOrderService.getSearsOrderLookup();

		// 任务监控历史记录添加:结束
		String result = "";
		if (isSuccess) {
			result = TaskControlEnums.Status.SUCCESS.getIs();
		} else {
			result = TaskControlEnums.Status.ERROR.getIs();
		}
		taskDao.insertTaskHistory(taskID, result);

		logger.info(GET_SEARS_ORDER_LOOKUP + "任务结束");
	}
}

