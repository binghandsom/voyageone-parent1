package com.voyageone.batch.oms.job;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.voyageone.batch.core.Constants;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.dao.TaskDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.oms.OmsConstants;
import com.voyageone.batch.oms.modelbean.ChangedOrderInfo4Log;
import com.voyageone.batch.oms.service.OrderInfoImportService;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;

public class ChangedOrderInfoImportJob {
	
	private static Log logger = LogFactory.getLog(ChangedOrderInfoImportJob.class);
	
	@Autowired
	TaskDao taskDao;
	
	@Autowired
	OrderInfoImportService orderInfoImportService;
	
	@Autowired
	IssueLog issueLog;
	
	private final static String taskCheck = "ChangedOrderImport";
	
	/**
	 * 更新状态发生变化订单
	 */
	public void run() {
		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(taskCheck);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, taskCheck) == false) {
			return;
		}
		String taskID =  TaskControlUtils.getTaskId(taskControlList);
		logger.info(taskCheck + "任务开始");
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());
		
		boolean isSuccess = true;
		
		try {
			// 获取状态变化订单信息
			List<ChangedOrderInfo4Log> changedOrderInfoList = orderInfoImportService.getChangedOrderInfoFromLog();
			// 有状态变化订单信息的话
			if (changedOrderInfoList != null && changedOrderInfoList.size() > 0) {
				int size = changedOrderInfoList.size();
				logger.info("状态变化订单信息数：" + size);
				
				for (int i = 0; i < size; i++) {
					ChangedOrderInfo4Log changedOrderInfo = changedOrderInfoList.get(i);
					
					// 订单状态
					String status = changedOrderInfo.getStatus();
					// 退款阶段
					String refundPhase = changedOrderInfo.getRefundPhase();
					
					// 目前只处理交易成功、退款创建、买家修改退款协议、售后退款成功、退款关闭、交易关闭状态的更新
					// 京东锁定状态
					if (Constants.ALIBABA_STATUS_TRADE_SUCCESS.equals(status) || 
						Constants.ALIBABA_STATUS_REFUND_CREATED.equals(status) ||
						Constants.ALIBABA_STATUS_REFUND_BUYER_MODIFY_AGREEMENT.equals(status) ||
						(Constants.ALIBABA_STATUS_REFUND_SUCCESS.equals(status) && OmsConstants.REFUND_PHASE_AFTERSALE.equals(refundPhase)) ||
						Constants.ALIBABA_STATUS_REFUND_CLOSED.equals(status) ||
						Constants.ALIBABA_STATUS_TRADE_CLOSE.equals(status) ||
						Constants.JD_STATUS_LOCKED.equals(status)) {
						// 获取内部订单号及订单状态
						List<Map> orderInfoMapList = orderInfoImportService.getOrderNumberAndStatusByTid(changedOrderInfo);
						if (orderInfoMapList == null || orderInfoMapList.size() <= 0) {
							// 订单号不存在
							logger.info("tid:" + changedOrderInfo.getTid() + "在订单表里不存在, 等待下次执行");
							
							continue;
						}
						
						// 返回该订单应该赋予的状态及确认时间
						String[] orderStatusAndConfirmDate = orderInfoImportService.getOrderStatusAndConfirmDate(changedOrderInfo);
						String orderStatus = orderStatusAndConfirmDate[0];
						
						// 交易成功&交易关闭
						if (Constants.ALIBABA_STATUS_TRADE_SUCCESS.equals(status) || Constants.ALIBABA_STATUS_TRADE_CLOSE.equals(status)) {
							// 交易成功状态判断有没有重复记录
							if (Constants.ALIBABA_STATUS_TRADE_SUCCESS.equals(status)) {
								// 有重复
								boolean isRepeat = orderInfoImportService.isRepeatTradeSuccess(changedOrderInfo);
								if (isRepeat) {
									logger.info("orderChannelId:" + changedOrderInfo.getOrderChannelId() + 
												" cartId:" + changedOrderInfo.getCartId() + 
												" sourceOrderId:" + changedOrderInfo.getTid() +
												"'s TradeSuccess is Repeat, ignore!");
									// 置位oms_changed_orders_import_history表发送标志
									orderInfoImportService.resetChangedHistoryOrders(changedOrderInfo, taskCheck);
									continue;
								}
							}
							orderInfoImportService.tradeSuccessUpdate(changedOrderInfo, orderStatus, orderInfoMapList, taskCheck);
							
						// 退款创建、买家修改退款协议
						} else if (Constants.ALIBABA_STATUS_REFUND_CREATED.equals(status) || Constants.ALIBABA_STATUS_REFUND_BUYER_MODIFY_AGREEMENT.equals(status)) {
							orderInfoImportService.refundCreatedUpdate(changedOrderInfo, orderStatus, orderInfoMapList, taskCheck, status);
							
							// 退款创建
							if (Constants.ALIBABA_STATUS_REFUND_CREATED.equals(status)) {
								// 锁定 synship lock
								isSuccess = orderInfoImportService.lockSynShipOrders(orderInfoMapList, changedOrderInfo, taskCheck);
								if (isSuccess) {
									logger.info("锁定 synship lockSynShipOrders is success");
								} else {
									logger.info("锁定 synship lockSynShipOrders is failure");
									
//									issueLog.log("OrderInfoImportService.lockSynShipOrders", 
//											"锁定 synship lockSynShipOrders is failure, sourceOrderId:" + changedOrderInfo.getTid(), 
//											ErrorType.BatchJob, SubSystem.OMS, "可能是synship那边还没有这个订单的记录，下次同步时可以把该订单及锁定状态反映到synship");
								}
							}
							
						// 售后退款成功
						} else if (Constants.ALIBABA_STATUS_REFUND_SUCCESS.equals(status) && OmsConstants.REFUND_PHASE_AFTERSALE.equals(refundPhase)) {
							orderInfoImportService.refundSuccessUpdate(changedOrderInfo, orderStatus, orderInfoMapList, taskCheck);
							
						// 退款关闭
						} else if (Constants.ALIBABA_STATUS_REFUND_CLOSED.equals(status)) {
							orderInfoImportService.refundClosedUpdate(changedOrderInfo, orderStatus, orderInfoMapList, taskCheck);
							
						// 京东锁定
						} else if (Constants.JD_STATUS_LOCKED.equals(status)) {
							orderInfoImportService.lockFromJdUpdate(changedOrderInfo, orderInfoMapList, taskCheck, status);
							
							// 锁定 synship lock
							isSuccess = orderInfoImportService.lockSynShipOrders(orderInfoMapList, changedOrderInfo, taskCheck);
							if (isSuccess) {
								logger.info("锁定 synship lockSynShipOrders is success");
							} else {
								logger.info("锁定 synship lockSynShipOrders is failure");
								
//								issueLog.log("OrderInfoImportService.lockSynShipOrders", 
//										"锁定 synship lockSynShipOrders is failure, sourceOrderId:" + changedOrderInfo.getTid(), 
//										ErrorType.BatchJob, SubSystem.OMS, "可能是synship那边还没有这个订单的记录，下次同步时可以把该订单及锁定状态反映到synship");
							}
						}
					} else {
						// 置位oms_changed_orders_import_history表发送标志
						orderInfoImportService.resetChangedHistoryOrders(changedOrderInfo, taskCheck);
					}
				}
			} else {
				logger.info("没有需要更新状态变化的订单");
			}
		} catch (Exception ex) {
			isSuccess = false;
			
			logger.error(ex.getMessage(), ex);
			issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
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

