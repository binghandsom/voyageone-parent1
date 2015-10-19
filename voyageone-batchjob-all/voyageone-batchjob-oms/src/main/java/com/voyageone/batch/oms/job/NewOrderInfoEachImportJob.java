package com.voyageone.batch.oms.job;

import java.util.ArrayList;
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

public class NewOrderInfoEachImportJob {
	
	private static Log logger = LogFactory.getLog(NewOrderInfoEachImportJob.class);
	
	@Autowired
	TaskDao taskDao;
	
	@Autowired
	OrderInfoImportService orderInfoImportService;
	
	@Autowired
	IssueLog issueLog;
	
	public final static String taskCheck = "NewOrderEachImport";
	
	/**
	 * 循环每次一条导入新订单
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
			List<NewOrderInfo4Log> newOrderInfoList = orderInfoImportService.getNewOrderInfo4EachFromLog();
			// 有新订单信息的话
			if (newOrderInfoList != null && newOrderInfoList.size() > 0) {
				int size = newOrderInfoList.size();
				logger.info("需每条导入的新订单信息数：" + size);
				
				/******************** 初始化赠品设定相关内容 START ********************/
				// 买就送赠品设定
				orderInfoImportService.getBuyThanGiftSetting();
				// 满就送赠品例外设定
				orderInfoImportService.getPriceThanGiftExceptSetting();
				// 满就送赠品设定
				orderInfoImportService.getPriceThanGiftSetting();
				// 老顾客送赠品设定
				orderInfoImportService.getRegularCustomerGiftSetting();
				// 优先下单前多少名可获赠品设定
				orderInfoImportService.getPriorCountGiftSetting();
				// 已送赠品顾客信息获得
				orderInfoImportService.getHavingGiftedCustomerInfo();
				// 赠品类型信息设置获得
				orderInfoImportService.getGiftedPropertySetting();
				/******************** 初始化赠品设定相关内容 END ********************/

				// 套装设定内容获得
				orderInfoImportService.getSuitSkuSetting();
				
				for (NewOrderInfo4Log newOrderInfo : newOrderInfoList) {
					logger.info("tid：" + newOrderInfo.getTid() + " 导入开始");
					
					List<NewOrderInfo4Log> newOrderInfoEachList = new ArrayList<NewOrderInfo4Log>();
					newOrderInfoEachList.add(newOrderInfo);
					
					// 插入顾客表所需数据拼装
					String customerSqlValue = orderInfoImportService.getBatchCustomerSqlData(newOrderInfoEachList, taskCheck);
					
					// 订单属于老顾客标记
					orderInfoImportService.getRegularCustomer(newOrderInfoEachList);
					
					// 插入顾客表
					isSuccess = orderInfoImportService.insertCustomerBatchData(customerSqlValue);
					if (isSuccess) {
					
						// 一次性取出要插入的订单对应的customerId列表
						List<String> orderCustomerIdList = orderInfoImportService.getOrderCustomerIdList(newOrderInfoEachList);
						// 找到customerId
						if (orderCustomerIdList != null && 1 == orderCustomerIdList.size()) {
							// 订单表所需数据拼装
							String ordersStr = orderInfoImportService.getEachOrdersSqlData(newOrderInfoEachList, orderCustomerIdList, taskCheck);
							
							// 订单详细表所需数据拼装
							String[] orderDetailsStrAndCount = orderInfoImportService.getBatchOrdersDetailSqlData(newOrderInfoEachList, taskCheck);
							
							// Notes表所需数据拼装
							String notesStr = orderInfoImportService.getBatchNoteSqlData(newOrderInfoEachList, taskCheck);
							
							// 批处理交易明细表所需数据拼装
							Map<String, Object> transactionMap = orderInfoImportService.getBatchTransactionsSqlData(newOrderInfoEachList, taskCheck);
							String transactionsStr = (String)transactionMap.get("sqlData");
							int countNoGap = Integer.valueOf(String.valueOf(transactionMap.get("count")));
							
							// 批处理oms_bt_group_orders表所需数据拼装
							String ordersGroupStr = orderInfoImportService.getBatchGroupSqlData(newOrderInfoEachList, taskCheck);
							
							// 独立域名订单导入时钱已经支付到公司账户
							String[] paymentBatch = orderInfoImportService.getBatchPaymentSqlData(newOrderInfoEachList, taskCheck);
							String paymentBatchStr = paymentBatch[0];
							int paymentSize = Integer.valueOf(paymentBatch[1]);
							
							// 扩展orders表插入拼装
							String[] extOrdersBatch = orderInfoImportService.getBatchExtOrdersSqlData(newOrderInfoEachList, taskCheck);
							String extOrdersBatchStr = extOrdersBatch[0];
							int extOrdersSize = Integer.valueOf(extOrdersBatch[1]);
							
							// 扩展orderDetails表插入拼装
							String[] extOrderDetailsBatch = orderInfoImportService.getBatchExtOrderDetailsSqlData(newOrderInfoEachList, taskCheck);
							String extOrderDetailsBatchStr = extOrderDetailsBatch[0];
							int extOrderDetailsSize = Integer.valueOf(extOrderDetailsBatch[1]);
							
							// 开启事物完成 订单表 订单详细表 Notes表 导入并且同时完成发送置位操作
							isSuccess = orderInfoImportService.importNewOrders(ordersStr, orderDetailsStrAndCount, notesStr, 
									transactionsStr, countNoGap, ordersGroupStr, newOrderInfoEachList, paymentBatchStr, paymentSize, 
									extOrdersBatchStr, extOrdersSize, extOrderDetailsBatchStr, extOrderDetailsSize, taskCheck);
							
							// 成功
							if (isSuccess) {
								logger.info("tid：" + newOrderInfo.getTid() + " 导入成功");
								
								// 本轮订单插入结束之后已送赠品顾客信息记录
								orderInfoImportService.recordHavingGiftedCustomerInfo();
								
								// 检查没有自动approved的订单
								orderInfoImportService.checkNotApprovedInfo(newOrderInfoEachList);
								
							// 失败
							} else {
								logger.info("tid：" + newOrderInfo.getTid() + " 导入失败");
								
								issueLog.log("NewOrderInfoEachImportJob", 
										"单条导入新订单失败  tid：" + newOrderInfo.getTid(), 
										ErrorType.BatchJob, SubSystem.OMS);
								
								// 将历史表发送标志置成3
								orderInfoImportService.resetHistoryOrdersForManual(newOrderInfoEachList, taskCheck);
							}
						} else {
							isSuccess = false;
							
							logger.info("没找到customerId, 导入新订单异常结束");
							
							issueLog.log("NewOrderInfoEachImportJob", 
									"没找到customerId, 导入新订单异常结束  tid：" + newOrderInfo.getTid(), 
									ErrorType.BatchJob, SubSystem.OMS);
						}
					} else {
						logger.info("顾客表插入异常，导入新订单异常结束");
						
						issueLog.log("NewOrderInfoEachImportJob", 
								"顾客表插入异常，导入新订单异常结束  tid：" + newOrderInfo.getTid(), 
								ErrorType.BatchJob, SubSystem.OMS);
					}
				}
			} else {
				logger.info("没有需要导入的新订单");
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

