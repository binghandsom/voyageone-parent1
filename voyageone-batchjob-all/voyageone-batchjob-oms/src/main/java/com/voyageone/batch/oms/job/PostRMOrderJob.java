package com.voyageone.batch.oms.job;

import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.dao.TaskDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.oms.service.PostRMOrderService;
import com.voyageone.common.components.issueLog.IssueLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PostRMOrderJob {
	
	private static Log logger = LogFactory.getLog(PostRMOrderJob.class);
	
	@Autowired
	TaskDao taskDao;
	
	@Autowired
	PostRMOrderService postRMOrderService;
	
	@Autowired
	IssueLog issueLog;

	// 正常订单推送
	// 正常订单推送
	private final static String POST_RM_NORMAL_ORDER = "PostRMNormalOrder";

	// 准备取消订单推送
	private final static String POST_RM_CANCEL_ORDER = "PostRMPendingCancelOrder";

	// 退货订单推送
	private final static String POST_RM_RETURN_ORDER = "PostRMReturnOrder";

	// 取消订单下载
	private final static String DOWNLOAD_RM_CANCEL_ORDER = "DownloadRMCancelOrder";

	// 更新订单下载
	private final static String DOWNLOAD_RM_UPDATE_ORDER = "DownloadRMUpdateOrder";

	/**
	 * 正常订单推送
	 */
	public void postRMNormalOrder() {
		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(POST_RM_NORMAL_ORDER);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, POST_RM_NORMAL_ORDER) == false) {
			return;
		}
		String taskID =  TaskControlUtils.getTaskId(taskControlList);
		logger.info(POST_RM_NORMAL_ORDER + "任务开始");
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());
		
		boolean isSuccess = true;

		isSuccess = postRMOrderService.postRMNormalOrder();
		
		// 任务监控历史记录添加:结束
		String result = "";
		if (isSuccess) {
			result = TaskControlEnums.Status.SUCCESS.getIs();
		} else {
			result = TaskControlEnums.Status.ERROR.getIs();
		}
		taskDao.insertTaskHistory(taskID, result);

		logger.info(POST_RM_NORMAL_ORDER + "任务结束");
	}

	/**
	 * 准备取消订单推送
	 */
	public void postRMPendingCancelOrder() {

		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(POST_RM_CANCEL_ORDER);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, POST_RM_CANCEL_ORDER) == false) {
			return;
		}
		String taskID =  TaskControlUtils.getTaskId(taskControlList);
		logger.info(POST_RM_CANCEL_ORDER + "任务开始");
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());

		boolean isSuccess = true;

		isSuccess = postRMOrderService.postRMPendingCancelOrder();

		// 任务监控历史记录添加:结束
		String result = "";
		if (isSuccess) {
			result = TaskControlEnums.Status.SUCCESS.getIs();
		} else {
			result = TaskControlEnums.Status.ERROR.getIs();
		}
		taskDao.insertTaskHistory(taskID, result);

		logger.info(POST_RM_CANCEL_ORDER + "任务结束");
	}

	/**
	 * 退货订单推送
	 */
	public void postRMReturnOrder() {

		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(POST_RM_RETURN_ORDER);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, POST_RM_RETURN_ORDER) == false) {
			return;
		}
		String taskID =  TaskControlUtils.getTaskId(taskControlList);
		logger.info(POST_RM_RETURN_ORDER + "任务开始");
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());

		boolean isSuccess = true;

		isSuccess = postRMOrderService.postRMReturnOrder();

		// 任务监控历史记录添加:结束
		String result = "";
		if (isSuccess) {
			result = TaskControlEnums.Status.SUCCESS.getIs();
		} else {
			result = TaskControlEnums.Status.ERROR.getIs();
		}
		taskDao.insertTaskHistory(taskID, result);

		logger.info(POST_RM_RETURN_ORDER + "任务结束");
	}

	/**
	 * 取消订单下载
	 */
	public void downloadRMCancelOrder() {

		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(DOWNLOAD_RM_CANCEL_ORDER);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, DOWNLOAD_RM_CANCEL_ORDER) == false) {
			return;
		}
		String taskID =  TaskControlUtils.getTaskId(taskControlList);
		logger.info(DOWNLOAD_RM_CANCEL_ORDER + "任务开始");
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());

		boolean isSuccess = true;

		isSuccess = postRMOrderService.downloadRMOrderCancelledOrder();

		// 任务监控历史记录添加:结束
		String result = "";
		if (isSuccess) {
			result = TaskControlEnums.Status.SUCCESS.getIs();
		} else {
			result = TaskControlEnums.Status.ERROR.getIs();
		}
		taskDao.insertTaskHistory(taskID, result);

		logger.info(DOWNLOAD_RM_CANCEL_ORDER + "任务结束");
	}

	/**
	 * 更新订单下载（取消订单回复）
	 */
	public void downloadRMUpdateOrder() {

		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(DOWNLOAD_RM_UPDATE_ORDER);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, DOWNLOAD_RM_UPDATE_ORDER) == false) {
			return;
		}
		String taskID =  TaskControlUtils.getTaskId(taskControlList);
		logger.info(DOWNLOAD_RM_UPDATE_ORDER + "任务开始");
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());

		boolean isSuccess = true;

		isSuccess = postRMOrderService.downloadRMOrderUpdateOrder();

		// 任务监控历史记录添加:结束
		String result = "";
		if (isSuccess) {
			result = TaskControlEnums.Status.SUCCESS.getIs();
		} else {
			result = TaskControlEnums.Status.ERROR.getIs();
		}
		taskDao.insertTaskHistory(taskID, result);

		logger.info(DOWNLOAD_RM_UPDATE_ORDER + "任务结束");
	}
}

