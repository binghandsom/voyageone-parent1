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
import com.voyageone.batch.oms.OmsConstants;
import com.voyageone.batch.oms.modelbean.TradeInfoBean;
import com.voyageone.batch.oms.service.OrderInfoImportService;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;

public class ChangedOrderJsonImportJob {
	
	private static Log logger = LogFactory.getLog(ChangedOrderJsonImportJob.class);
	
	@Autowired
	TaskDao taskDao;
	
	@Autowired
	OrderInfoImportService orderInfoImportService;
	
	@Autowired
	IssueLog issueLog;
	
	public final static String taskCheck = "ChangedOrderJsonImport";
	
	/**
	 * 批量处理json状态变化订单信息到history表中
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
		
		// 总存储集合
		List<TradeInfoBean> orderInfoTotalList = new ArrayList<TradeInfoBean>();
		// 总存储集合订单数
		int totalSize = 0;
		// json表主键信息
		List<String> jsonIdList = new ArrayList<String>();
		List<String> targetList = new ArrayList<String>();
		
		boolean isSuccess = true;
		
		try {
			while (true) {
				// 从json串状态变化订单表中抽出单条记录批量插入
				List<Map<String, String>> changedOrderJsonList = orderInfoImportService.getChangedOrderInfoFromJson();
				
				// 本次取出新订单要处理的话
				if (changedOrderJsonList != null && changedOrderJsonList.size() > 0) {
					for (Map<String, String> changedOrderJson : changedOrderJsonList) {
						
						String jsonId = String.valueOf(changedOrderJson.get("id"));
						String target = String.valueOf(changedOrderJson.get("target"));
						String jsonData = String.valueOf(changedOrderJson.get("jsonData"));
						
						if (StringUtils.isNullOrBlank2(jsonId) || StringUtils.isNullOrBlank2(jsonData) || StringUtils.isNullOrBlank2(target)) {
							continue;
						}
						
						// json字符串转BeanList
						List<TradeInfoBean> tradeInfoList = null;
						try {
							tradeInfoList = JsonUtil.jsonToBeanList(jsonData, TradeInfoBean.class);
						} catch (Exception ex) {
							logger.error(ex.getMessage(), ex);
							
							isSuccess = false;
							// 状态变化订单json字符串转TradeInfoBean列表出错
							issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS, "状态变化订单json字符串转TradeInfoBean列表出错");
						}
						
						// 插入新付款订单信息
						if (tradeInfoList != null && tradeInfoList.size() > 0) {
							int size = tradeInfoList.size();
							totalSize += size;
							
							orderInfoTotalList.addAll(tradeInfoList);
							jsonIdList.add(jsonId);
							targetList.add(target);
							
							// 超过300一次性批量插入
							if (totalSize >= OmsConstants.DATACOUNT_300) {
								break;
							}
						}
					}
					
					// 只取一次数据库
					break;
					
				// 没有新订单继续取出
				} else {
					break;
					
				}
			}
			
			// 有要处理的订单信息，批量处理
			if (totalSize > 0 && jsonIdList.size() > 0) {
				isSuccess = orderInfoImportService.importChangedOrderFromJsonToHistory(orderInfoTotalList, jsonIdList, targetList, taskCheck);
			} else {
				logger.info("没有要批量处理json状态变化订单信息");
			}
			
			totalSize = 0;
			
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

