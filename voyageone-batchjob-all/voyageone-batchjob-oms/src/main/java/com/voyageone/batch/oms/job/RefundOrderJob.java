package com.voyageone.batch.oms.job;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.dao.TaskDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.oms.OmsConstants;
import com.voyageone.batch.oms.modelbean.ChangedOrderInfo4Log;
import com.voyageone.batch.oms.service.RefundService;

public class RefundOrderJob {
	private static Log logger = LogFactory.getLog(RefundOrderJob.class);

	// private final static String[][] mChannelCartList = { { "001", "24", "4"
	// },
	// { "001", "26", "14" } };
	// private final static String[][] mChannelCartList = { { "001", "24", "4" }
	// };

	@Autowired
	TaskDao taskDao;

	@Autowired
	RefundService refundService;

	private final static String taskCheck = "RefundOrderJob";

	/**
	 * 取提前量60分钟之前开始的京东订单信息
	 */
	private final static int pre_minute = 60;

	public void run() {
		List<TaskControlBean> taskControlList = taskDao
				.getTaskControlList(taskCheck);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, taskCheck) == false) {
			return;
		}
		String taskID = TaskControlUtils.getTaskId(taskControlList);
		logger.info(taskCheck + "任务开始");
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());

		boolean isSuccess = true;

		try {
			// 取得新订单history表中最新订单信息的时间, 以此时间作为向京东请求的开始时间
//			String refundApplyStartDate = refundService.getJdLastHistoryRefundOrderTime(pre_minute);
			String refundApplyStartDate = "2015-01-15 08:43:46";
			// 从京东拉取新退款列表
			List<ChangedOrderInfo4Log> refundOrderList = refundService
					.doJDSearchRefundOrders(null, null, refundApplyStartDate,
							OmsConstants.CHANNEL_SNEAKERHEAD,
							OmsConstants.CART_JD, OmsConstants.TARGET_JD);

			List<String> unclosedRefundOrderIds = refundService
					.getUnclosedRefundId(OmsConstants.CHANNEL_SNEAKERHEAD,
							OmsConstants.CART_JD);
			List<ChangedOrderInfo4Log> unclosedRefundOrders = new ArrayList<ChangedOrderInfo4Log>();
			for (String unclosedRefundOrderId : unclosedRefundOrderIds) {
				// 从京东查看未关闭退款订单的最新状态
				List<ChangedOrderInfo4Log> unclosedRefundOrder = refundService
						.doJDSearchRefundOrders(unclosedRefundOrderId, 3l, "",
								OmsConstants.CHANNEL_SNEAKERHEAD,
								OmsConstants.CART_JD, OmsConstants.TARGET_JD);
				// ChangedOrderInfo4Log unclosedRefundOrder = refundService
				// .doJDRefundDetail(unclosedRefundOrderId,
				// OmsConstants.CHANNEL_SNEAKERHEAD,
				// OmsConstants.CART_JD, OmsConstants.TARGET_JD);
				unclosedRefundOrders.add(unclosedRefundOrder.get(0));
			}
			if (refundOrderList != null && refundOrderList.size() > 0) {
				isSuccess = refundService.insertRefundOrdersInfo(
						refundOrderList, taskCheck);
			}
			if (unclosedRefundOrders.size() > 0) {
				isSuccess = refundService.insertRefundOrdersInfo(
						unclosedRefundOrders, taskCheck);
			}
		} catch (Exception ex) {
			isSuccess = false;
			logger.error(ex.getMessage(), ex);
		}

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