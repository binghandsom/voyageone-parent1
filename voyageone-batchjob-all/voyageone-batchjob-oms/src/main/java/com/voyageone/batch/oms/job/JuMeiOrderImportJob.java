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
import com.voyageone.batch.oms.service.JuMeiOrderInfoImportService;
import com.voyageone.batch.oms.service.OrderInfoImportService;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.mail.Mail;

public class JuMeiOrderImportJob {
	
	private static Log logger = LogFactory.getLog(JuMeiOrderImportJob.class);
	
	/**
	 * 取提前量60分钟之前开始的聚美订单信息
	 */
	private final static int pre_minute = 60;
	
	/**
	 * jumei批量处理新订单信息到history表任务
	 */
	public final static String TASKCHECK_JUMEI_NEW_ORDER = "JumeiNewOrderImport";

	/**
	/**
	 * 聚美短链接推送任务
	 */
	public final static String TASKCHECK_JUMEI_SHORTURL_GENERATE = "JumeiShortUrlGenerate";
	
	@Autowired
	TaskDao taskDao;
	
	@Autowired
	OrderInfoImportService orderInfoImportService;
	
	@Autowired
	IssueLog issueLog;
	
	@Autowired
	JuMeiOrderInfoImportService juMeiOrderInfoImportService;
	
	/**
	 * 聚美bhfo批量处理新订单信息到history表中
	 */
	public void jmNewOrderImport() {
		
		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(TASKCHECK_JUMEI_NEW_ORDER);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, TASKCHECK_JUMEI_NEW_ORDER) == false) {
			return;
		}
		String taskID =  TaskControlUtils.getTaskId(taskControlList);
		logger.info(TASKCHECK_JUMEI_NEW_ORDER + "任务开始");
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());
		
		boolean isSuccess = true;
		
		try {
			// 取得新订单history表中最新订单信息的时间, 以此时间作为向聚美请求的开始时间
			String startOrderTime = orderInfoImportService.getLastHistoryNewOrderTime4Jumei(pre_minute, OmsConstants.CART_JM);
			
			// 调用聚美API获得新订单
			List<TaobaoTradeBean> jmNewOrderList = juMeiOrderInfoImportService.getJumeiNewOrder(startOrderTime,
					OmsConstants.CART_JM, OmsConstants.TITLE_JM);
			// 插入临时表
			if (jmNewOrderList != null && jmNewOrderList.size() > 0) {
				isSuccess = juMeiOrderInfoImportService.importNewOrderToHistory(jmNewOrderList, TASKCHECK_JUMEI_NEW_ORDER);
			} else {
				logger.info("本次没有要处理的聚美新订单数据");
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

		logger.info(TASKCHECK_JUMEI_NEW_ORDER + "任务结束");
	}
	
	/**
	 * 聚美短链接推送
	 */
	public void shortUrlGenerate() {
		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(TASKCHECK_JUMEI_SHORTURL_GENERATE);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, TASKCHECK_JUMEI_SHORTURL_GENERATE) == false) {
			return;
		}
		String taskID =  TaskControlUtils.getTaskId(taskControlList);
		logger.info(TASKCHECK_JUMEI_SHORTURL_GENERATE + "任务开始");
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());
		
		boolean isSuccess = true;
		
		List<TaobaoTradeBean> jumeiOrderList = null;
		try {
			// 获得聚美没有推送短链接的新订单信息
			List<String> cartIdList = new ArrayList<String>();
			cartIdList.add(OmsConstants.CART_JM);
			
			jumeiOrderList = juMeiOrderInfoImportService.getNeedShortUrlJmNewOrder(cartIdList);
			
		} catch (Exception ex) {
			isSuccess = false;
			logger.error(ex.getMessage(), ex);
			
			issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
			
			return;
		}
		
		// 有需要处理的订单
		if (jumeiOrderList != null && jumeiOrderList.size() > 0) {
			List<TaobaoTradeBean> errorList = new ArrayList<TaobaoTradeBean>();
			List<String> errorInfoList = new ArrayList<String>();
			
			for (TaobaoTradeBean trade : jumeiOrderList) {
				try {
					List<String> errorMessage = new ArrayList<String>();
					boolean isPushortSuccess = juMeiOrderInfoImportService.pushShortUrl(trade, errorMessage, TASKCHECK_JUMEI_SHORTURL_GENERATE);
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
					logger.info("OMS中有聚美新订单信息推送到Synship的短链接表失败");
					
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
			logger.info("本次没有要处理的聚美国际推送短链接的新订单信息");
		}
		
		// 任务监控历史记录添加:结束
		String result = "";
		if (isSuccess) {
			result = TaskControlEnums.Status.SUCCESS.getIs();
		} else {
			result = TaskControlEnums.Status.ERROR.getIs();
		}
		taskDao.insertTaskHistory(taskID, result);

		logger.info(TASKCHECK_JUMEI_SHORTURL_GENERATE + "任务结束");
	}
	
}

