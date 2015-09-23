package com.voyageone.batch.oms.job;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.dao.TaskDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.oms.OmsConstants;
import com.voyageone.batch.oms.modelbean.TaobaoTradeBean;
import com.voyageone.batch.oms.modelbean.TradeInfoBean;
import com.voyageone.batch.oms.service.JingdongOrderInfoImportService;
import com.voyageone.batch.oms.service.OrderInfoImportService;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.mail.Mail;

public class JingdongOrderImportJob {
	
	private static Log logger = LogFactory.getLog(JingdongOrderImportJob.class);
	
	/**
	 * 取提前量60分钟之前开始的京东订单信息
	 */
	private final static int pre_minute = 60;
	
	/**
	 * 京东批量处理新订单信息到history表任务
	 */
	public final static String TASKCHECK_JD_NEW_ORDER = "JdNewOrderImport";
	/**
	 * 京东批量处理状态变化订单信息到history表任务
	 */
	public final static String TASKCHECK_JD_CHANGED_ORDER = "JdChangedOrderImport";
	/**
	 * 京东国际批量处理新订单信息到history表任务
	 */
	public final static String TASKCHECK_JD_GJ_NEW_ORDER = "JdGjNewOrderImport";
	/**
	 * 京东国际批量处理状态变化订单信息到history表任务
	 */
	public final static String TASKCHECK_JD_GJ_CHANGED_ORDER = "JdGjChangedOrderImport";
	/**
	 * 京东、京东国际短链接推送任务
	 */
	public final static String TASKCHECK_JD_SHORTURL_GENERATE = "JdShortUrlGenerate";
	
	@Autowired
	TaskDao taskDao;
	
	@Autowired
	OrderInfoImportService orderInfoImportService;
	
	@Autowired
	IssueLog issueLog;
	
	@Autowired
	JingdongOrderInfoImportService jdOrderInfoImportService;
	
	/**
	 * 京东批量处理新订单信息到history表中
	 */
	public void jdNewOrderImport() {
		
		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(TASKCHECK_JD_NEW_ORDER);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, TASKCHECK_JD_NEW_ORDER) == false) {
			return;
		}
		String taskID =  TaskControlUtils.getTaskId(taskControlList);
		logger.info(TASKCHECK_JD_NEW_ORDER + "任务开始");
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());
		
		boolean isSuccess = true;
		
		try {
			// 取得新订单history表中最新订单信息的时间, 以此时间作为向京东请求的开始时间
			String startOrderTime = orderInfoImportService.getLastHistoryNewOrderTime(pre_minute, 
					OmsConstants.CHANNEL_SNEAKERHEAD, OmsConstants.CART_JD);
			
			// 调用京东API获得新订单
			List<TaobaoTradeBean> jdNewOrderList = jdOrderInfoImportService.getJdNewOrder(startOrderTime,
					OmsConstants.CHANNEL_SNEAKERHEAD, OmsConstants.CART_JD, OmsConstants.TARGET_JD, OmsConstants.TITLE_JD);
			// 插入临时表
			if (jdNewOrderList != null && jdNewOrderList.size() > 0) {
				isSuccess = jdOrderInfoImportService.importNewOrderToHistory(jdNewOrderList, TASKCHECK_JD_NEW_ORDER);
			} else {
				logger.info("本次没有要处理的京东新订单数据");
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

		logger.info(TASKCHECK_JD_NEW_ORDER + "任务结束");
	}
	
	/**
	 * 京东状态变化订单信息到history表中
	 */
	public void jdChangedOrderImport() {
		
		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(TASKCHECK_JD_CHANGED_ORDER);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, TASKCHECK_JD_CHANGED_ORDER) == false) {
			return;
		}
		String taskID =  TaskControlUtils.getTaskId(taskControlList);
		logger.info(TASKCHECK_JD_CHANGED_ORDER + "任务开始");
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());
		
		boolean isSuccess = true;
		
		try {
			// 取得新订单history表中最新订单信息的时间, 以此时间作为向京东请求的开始时间
			String startOrderTime = orderInfoImportService.getLastHistoryChangedOrderTime(pre_minute, 
					OmsConstants.CHANNEL_SNEAKERHEAD, OmsConstants.CART_JD);
			
			// 调用京东API获得新订单
			List<TradeInfoBean> jdChangedOrderList = jdOrderInfoImportService.getJdChangedOrder(startOrderTime, 
					OmsConstants.CHANNEL_SNEAKERHEAD, OmsConstants.CART_JD, OmsConstants.TARGET_JD, OmsConstants.TITLE_JD);
			// 插入临时表
			if (jdChangedOrderList != null && jdChangedOrderList.size() > 0) {
				isSuccess = jdOrderInfoImportService.importChangedOrderToHistory(jdChangedOrderList, TASKCHECK_JD_CHANGED_ORDER);
			} else {
				logger.info("本次没有要处理的京东状态变化订单数据");
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

		logger.info(TASKCHECK_JD_CHANGED_ORDER + "任务结束");
	}
	
	/**
	 * 京东国际批量处理新订单信息到history表中
	 */
	public void jdInternationalNewOrderImport() {
		
		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(TASKCHECK_JD_GJ_NEW_ORDER);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, TASKCHECK_JD_GJ_NEW_ORDER) == false) {
			return;
		}
		String taskID =  TaskControlUtils.getTaskId(taskControlList);
		logger.info(TASKCHECK_JD_GJ_NEW_ORDER + "任务开始");
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());
		
		boolean isSuccess = true;
		
		try {
			// 取得新订单history表中最新订单信息的时间, 以此时间作为向京东国际请求的开始时间
			String startOrderTime = orderInfoImportService.getLastHistoryNewOrderTime(pre_minute, 
					OmsConstants.CHANNEL_SNEAKERHEAD, OmsConstants.CART_JDG);
			
			// 调用京东国际API获得新订单
			List<TaobaoTradeBean> jdNewOrderList = jdOrderInfoImportService.getJdNewOrder(startOrderTime,
					OmsConstants.CHANNEL_SNEAKERHEAD, OmsConstants.CART_JDG, OmsConstants.TARGET_JDG, OmsConstants.TITLE_JDG);
			// 插入临时表
			if (jdNewOrderList != null && jdNewOrderList.size() > 0) {
				isSuccess = jdOrderInfoImportService.importNewOrderToHistory(jdNewOrderList, TASKCHECK_JD_GJ_NEW_ORDER);
			} else {
				logger.info("本次没有要处理的京东国际新订单数据");
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

		logger.info(TASKCHECK_JD_GJ_NEW_ORDER + "任务结束");
	}
	
	/**
	 * 京东国际状态变化订单信息到history表中
	 */
	public void jdInternationalChangedOrderImport() {
		
		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(TASKCHECK_JD_GJ_CHANGED_ORDER);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, TASKCHECK_JD_GJ_CHANGED_ORDER) == false) {
			return;
		}
		String taskID =  TaskControlUtils.getTaskId(taskControlList);
		logger.info(TASKCHECK_JD_GJ_CHANGED_ORDER + "任务开始");
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());
		
		boolean isSuccess = true;
		
		try {
			// 取得新订单history表中最新订单信息的时间, 以此时间作为向京东请求的开始时间
			String startOrderTime = orderInfoImportService.getLastHistoryChangedOrderTime(pre_minute, 
					OmsConstants.CHANNEL_SNEAKERHEAD, OmsConstants.CART_JDG);
			
			// 调用京东国际API获得新订单
			List<TradeInfoBean> jdChangedOrderList = jdOrderInfoImportService.getJdChangedOrder(startOrderTime, 
					OmsConstants.CHANNEL_SNEAKERHEAD, OmsConstants.CART_JDG, OmsConstants.TARGET_JDG, OmsConstants.TITLE_JDG);
			logger.info("本次处理取得京东国际状态变化订单数据结束");
			
			// 插入临时表
			if (jdChangedOrderList != null && jdChangedOrderList.size() > 0) {
				isSuccess = jdOrderInfoImportService.importChangedOrderToHistory(jdChangedOrderList, TASKCHECK_JD_GJ_CHANGED_ORDER);
			} else {
				logger.info("本次没有要处理的京东国际状态变化订单数据");
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

		logger.info(TASKCHECK_JD_GJ_CHANGED_ORDER + "任务结束");
	}
	
	/**
	 * 京东、京东国际短链接推送
	 */
	public void shortUrlGenerate() {
		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(TASKCHECK_JD_SHORTURL_GENERATE);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, TASKCHECK_JD_SHORTURL_GENERATE) == false) {
			return;
		}
		String taskID =  TaskControlUtils.getTaskId(taskControlList);
		logger.info(TASKCHECK_JD_SHORTURL_GENERATE + "任务开始");
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());
		
		boolean isSuccess = true;
		
		List<TaobaoTradeBean> jdOrderList = null;
		try {
			// 获得京东和京东国际没有推送短链接的新订单信息
			List<String> cartIdList = new ArrayList<String>();
			cartIdList.add(OmsConstants.CART_JD);
			cartIdList.add(OmsConstants.CART_JDG);
			
			jdOrderList = jdOrderInfoImportService.getNeedShortUrlJdNewOrder(OmsConstants.CHANNEL_SNEAKERHEAD, cartIdList);
			
		} catch (Exception ex) {
			isSuccess = false;
			logger.error(ex.getMessage(), ex);
			
			issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
			
			return;
		}
		
		// 有需要处理的订单
		if (jdOrderList != null && jdOrderList.size() > 0) {
			List<TaobaoTradeBean> errorList = new ArrayList<TaobaoTradeBean>();
			List<String> errorInfoList = new ArrayList<String>();
			
			for (TaobaoTradeBean trade : jdOrderList) {
				try {
					List<String> errorMessage = new ArrayList<String>();
					boolean isPushortSuccess = jdOrderInfoImportService.pushShortUrl(trade, errorMessage, TASKCHECK_JD_SHORTURL_GENERATE);
					// 处理失败
					if (!isPushortSuccess) {
						errorList.add(trade);
						errorInfoList.add(errorMessage.get(0));
					}
				} catch (Exception ex) {
					isSuccess = false;
					logger.error(ex.getMessage(), ex);
					
					issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
				}
			}
			
			// 有处理失败需要发邮件
			if (errorList.size() > 0 && errorInfoList.size() > 0) {
				StringBuilder tbody = new StringBuilder();
				for (int i = 0; i < errorList.size(); i++) {
					TaobaoTradeBean trade = errorList.get(i);
					String errorMessage = errorInfoList.get(i);
					
					String orderChannelId = trade.getOrder_channel_id();
					String cartId = trade.getCartId();
					ShopBean shopBean = ShopConfigs.getShop(orderChannelId, cartId);
					// 邮件每行正文
					String mailTextLine = 
						String.format(OmsConstants.SHOT_URL_CHECK_FAILURE_ROW, shopBean.getShop_name(), 
								orderChannelId, cartId, trade.getTid(), trade.getReceiver_name(), trade.getReceiver_mobile(), errorMessage);
					tbody.append(mailTextLine);
				}
				
				if (tbody.length() > 0) {
					logger.info("OMS中有新订单信息推送到Synship的短链接表失败");
					
					// 拼接table
					String body = String.format(OmsConstants.SHOT_URL_CHECK_FAILURE_TABLE, OmsConstants.SHOT_URL_CHECK_FAILURE_HEAD, tbody.toString());
					
					// 拼接邮件正文
					StringBuilder emailContent = new StringBuilder();
					emailContent.append(com.voyageone.batch.core.Constants.EMAIL_STYLE_STRING).append(body);
					try {
						Mail.sendAlert("ITOMS", OmsConstants.SHOT_URL_CHECK_FAILURE_SUBJECT, emailContent.toString(), true);
						logger.info("邮件发送成功!");
						
					} catch (MessagingException e) {
						logger.info("邮件发送失败！" + e);
					}
				}
			}
		} else {
			logger.info("本次没有要处理的京东和京东国际推送短链接的新订单信息");
		}
		
		// 任务监控历史记录添加:结束
		String result = "";
		if (isSuccess) {
			result = TaskControlEnums.Status.SUCCESS.getIs();
		} else {
			result = TaskControlEnums.Status.ERROR.getIs();
		}
		taskDao.insertTaskHistory(taskID, result);

		logger.info(TASKCHECK_JD_SHORTURL_GENERATE + "任务结束");
	}
	
}

