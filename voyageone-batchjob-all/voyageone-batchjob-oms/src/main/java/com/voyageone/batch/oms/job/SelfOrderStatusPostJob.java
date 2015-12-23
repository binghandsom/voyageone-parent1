package com.voyageone.batch.oms.job;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.dao.TaskDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.JaxbUtil;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.oms.OmsConstants;
import com.voyageone.batch.oms.modelbean.SelfOrderInfo4Post;
import com.voyageone.batch.oms.modelbean.SelfOrderInfo4PostXml;
import com.voyageone.batch.oms.service.OrderInfoImportService;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;

public class SelfOrderStatusPostJob {
	
	private static Log logger = LogFactory.getLog(SelfOrderStatusPostJob.class);
	
	@Autowired
	TaskDao taskDao;
	
	@Autowired
	OrderInfoImportService orderInfoImportService;
	
	@Autowired
	IssueLog issueLog;
	
	private final static String taskCheck = "SelfOrderStatusPost";
	
	/**
	 * 独立域名推送状态变化信息
	 */
	public void run(String orderChannelId) {
		
		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(taskCheck);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, taskCheck) == false) {
			return;
		}
		String taskID =  TaskControlUtils.getTaskId(taskControlList);
		logger.info(taskCheck + "任务开始");
		logger.info("oderChannelId:" + orderChannelId);
		
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());
		
		boolean isSuccess = true;
		
		// 独立域名
		String cartId = OmsConstants.CART_SELF;
		try {
			// 需要向独立域名推送shipped、canceled状态的订单信息
			List<SelfOrderInfo4Post> postOrderStatusInfoList = new ArrayList<SelfOrderInfo4Post>();
			
			// 获取独立域名已发货订单信息
			List<SelfOrderInfo4Post> selfShippedOrderInfoList = orderInfoImportService.getSelfShippedOrderInfo(orderChannelId, cartId);
			// 有已发货订单信息的话
			if (selfShippedOrderInfoList != null && selfShippedOrderInfoList.size() > 0) {
				int size = selfShippedOrderInfoList.size();
				logger.info("独立域名已发货订单数有" + size);
				
				postOrderStatusInfoList.addAll(selfShippedOrderInfoList);
			} else {
				logger.info("没有要向独立域名推送已发货订单信息");
			}
			
			// 获取独立域名已取消原始订单信息
			List<SelfOrderInfo4Post> selfCanceledOriginalOrderInfoList = orderInfoImportService.getSelfCanceledOriginalOrderInfo(orderChannelId, cartId);
			// 获取独立域名已取消原始订单对应 子订单没有取消信息
			List<SelfOrderInfo4Post> selfCanceledChildOrderInfoList = orderInfoImportService.getSelfCanceledChildOrderInfo(orderChannelId, cartId);
			// 有已取消订单信息的话
			if (selfCanceledOriginalOrderInfoList != null && selfCanceledOriginalOrderInfoList.size() > 0) {
				logger.info("未整理过已取消订单信息数:" + selfCanceledOriginalOrderInfoList.size());
				
				orderInfoImportService.getReallySelfCanceledOrderInfoList(selfCanceledOriginalOrderInfoList, selfCanceledChildOrderInfoList);
				
				int size = selfCanceledOriginalOrderInfoList.size();
				if (size <= 0) {
					logger.info("没有要向独立域名推送已取消订单信息");
				} else {
					logger.info("独立域名已取消订单数有" + size);
					postOrderStatusInfoList.addAll(selfCanceledOriginalOrderInfoList);
				}
			} else {
				logger.info("没有要向独立域名推送已取消订单信息");
			}
			
			int size = postOrderStatusInfoList.size();
			if (size > 0) {
				
				// 组装xml
				SelfOrderInfo4PostXml xmlBean = new SelfOrderInfo4PostXml();
				xmlBean.setOrders(postOrderStatusInfoList);
				String postXML = JaxbUtil.convertToXml(xmlBean);
				
				// 调用WebService
				logger.info("调用独立域名WebService：order_status_update_request.php开始");
				String postUrl = TaskControlUtils.getVal2(taskControlList, TaskControlEnums.Name.order_channel_id, orderChannelId);
				logger.info("postUrl:" + postUrl);

				// https
				System.setProperty("javax.net.ssl.trustStore", "/opt/app-shared/voyageone_web/contents/other/third_party/com/trustStore/trustStore");

				String response= orderInfoImportService.postOrder(postXML, postUrl);
				logger.info("返回结果：" + response);
				
				if (response != null) {
					if (response.indexOf("Success") > -1) {
						// 调用之后置位oms_bt_group_orders里的发送标志self_send_flag
						for (SelfOrderInfo4Post bean : postOrderStatusInfoList) {
							boolean isSuccessSet = orderInfoImportService.resetGroupSelfSendFlag(bean, taskCheck);
							
							if (!isSuccessSet) {
								logger.info(bean.getSourceOrderId() + " 置位oms_bt_group_orders里的发送标志self_send_flag失败");
							}
						}
					}
				}
				
			} else {
				logger.info("没有要向独立域名推送shipped、canceled状态订单信息");
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

