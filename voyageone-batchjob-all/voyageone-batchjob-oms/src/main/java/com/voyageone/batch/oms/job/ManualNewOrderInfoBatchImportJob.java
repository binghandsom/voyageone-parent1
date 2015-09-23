package com.voyageone.batch.oms.job;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.dao.TaskDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.oms.modelbean.NewOrderInfo4Log;
import com.voyageone.batch.oms.service.OrderInfoImportService;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;

public class ManualNewOrderInfoBatchImportJob {
	
	private static Log logger = LogFactory.getLog(ManualNewOrderInfoBatchImportJob.class);
	
	@Autowired
	TaskDao taskDao;
	
	@Autowired
	OrderInfoImportService orderInfoImportService;
	
	@Autowired
	IssueLog issueLog;
	
	private final static String taskCheck = "ManualNewOrderBatchImport";
	
	/**
	 * 批量一次性导入手工新订单
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
			// 获取新订单信息
			List<NewOrderInfo4Log> newOrderInfoList = orderInfoImportService.getManualNewOrderInfo4BatchFromLog();
			
			// 有新订单信息的话
			if (newOrderInfoList != null && newOrderInfoList.size() > 0) {
				int size = newOrderInfoList.size();
				logger.info("新订单信息数：" + size);
				
				// 批量插入顾客表所需数据拼装
				String customerSqlValue = orderInfoImportService.getBatchCustomerSqlData(newOrderInfoList, taskCheck);
				// 批量插入顾客表
				isSuccess = orderInfoImportService.insertCustomerBatchData(customerSqlValue);
				if (isSuccess) {
					logger.info("顾客表批量插入正常结束");
				
					// 一次性取出要插入的订单对应的customerId列表
					List<String> orderCustomerIdList = orderInfoImportService.getOrderCustomerIdList(newOrderInfoList);
					// customerId列表和新订单数量匹配
					if (orderCustomerIdList != null && size == orderCustomerIdList.size()) {
						// 批处理订单表所需数据拼装
						String ordersBatchStr = orderInfoImportService.getBatchOrdersSqlData(newOrderInfoList, orderCustomerIdList, taskCheck);
						
						// 批处理订单详细表所需数据拼装
						String[] orderDetailsStrAndCount = orderInfoImportService.getBatchOrdersDetailSqlData(newOrderInfoList, taskCheck);
						
						// 批处理Notes表所需数据拼装
						String notesStr = orderInfoImportService.getBatchNoteSqlData(newOrderInfoList, taskCheck);
						
						// 批处理交易明细表所需数据拼装
						Map<String, Object> transactionMap = orderInfoImportService.getBatchTransactionsSqlData(newOrderInfoList, taskCheck);
						String transactionsStr = (String)transactionMap.get("sqlData");
						int countNoGap = Integer.valueOf(String.valueOf(transactionMap.get("count")));
						
						// 批处理oms_bt_group_orders表所需数据拼装
//						String ordersGroupBatchStr = orderInfoImportService.getBatchGroupSqlData(newOrderInfoList, taskCheck);
						
						// 独立域名订单导入时钱已经支付到公司账户
						String[] paymentBatch = orderInfoImportService.getBatchPaymentSqlData(newOrderInfoList, taskCheck);
						String paymentBatchStr = paymentBatch[0];
						int paymentSize = Integer.valueOf(paymentBatch[1]);
						
						// 扩展orders表插入拼装（目前只是皇马，以后可能有别的第三方伙伴）
						String[] extOrdersBatch = orderInfoImportService.getBatchExtOrdersSqlData(newOrderInfoList, taskCheck);
						String extOrdersBatchStr = extOrdersBatch[0];
						int extOrdersSize = Integer.valueOf(extOrdersBatch[1]);
						
						// 开启事物完成 订单表 订单详细表 Notes表 导入并且同时完成发送置位操作
						isSuccess = orderInfoImportService.importNewManualOrders(ordersBatchStr, orderDetailsStrAndCount, notesStr, transactionsStr, countNoGap,
								newOrderInfoList, paymentBatchStr, paymentSize, extOrdersBatchStr, extOrdersSize, taskCheck);
						
						// 成功
						if (isSuccess) {
							
							logger.info("批量一次性导入新订单成功");
							
							// 检查收件人信息完整性检查
							//orderInfoImportService.checkReceiverInfo(newOrderInfoList);
							
						// 失败
						} else {
							logger.info("批量一次性导入新订单失败");
							
							issueLog.log("ManualNewOrderInfoBatchImportJob", 
									"批量一次性导入新订单失败", 
									ErrorType.BatchJob, SubSystem.OMS, "可能是某条订单数据主键冲突导致批处理插入新订单失败，后面会交给单条新订单插入任务来处理");
							
							// 将历史表发送标志置成2，且记录预分配订单号（由单条导入服务来处理）
							orderInfoImportService.resetManualHistoryOrdersForeach(newOrderInfoList, size, taskCheck);
						}
					} else {
						isSuccess = false;
						
						logger.info("customerId列表和新订单数量不匹配，新订单有" + size + " customerId列表有" + orderCustomerIdList.size());
						logger.info("批量一次性导入新订单异常结束");
						
						issueLog.log("ManualNewOrderInfoBatchImportJob", 
								"customerId列表和新订单数量不匹配，新订单有" + size + " customerId列表有" + orderCustomerIdList.size(), 
								ErrorType.BatchJob, SubSystem.OMS);
					}
				} else {
					logger.info("顾客表批量插入异常，批量导入新订单异常结束");
					
					issueLog.log("ManualNewOrderInfoBatchImportJob", 
							"顾客表批量插入异常，批量导入新订单异常结束", 
							ErrorType.BatchJob, SubSystem.OMS);
				}
			} else {
				logger.info("没有需要批量导入的新订单");
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

