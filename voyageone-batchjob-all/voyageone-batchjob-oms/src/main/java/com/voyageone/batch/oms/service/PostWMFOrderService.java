package com.voyageone.batch.oms.service;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.jcraft.jsch.ChannelSftp;
import com.voyageone.batch.core.Constants;
import com.voyageone.batch.oms.OmsConstants;
import com.voyageone.batch.oms.dao.OrderDao;
import com.voyageone.batch.oms.formbean.InFormFile;
import com.voyageone.batch.oms.formbean.OutFormOrderDetailOrderDetail;
import com.voyageone.batch.oms.formbean.OutFormOrderdetailOrders;
import com.voyageone.batch.oms.modelbean.OrderExtend;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.FtpBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.magento.api.MagentoConstants;
import com.voyageone.common.magento.api.service.MagentoApiServiceImpl;
import com.voyageone.common.mail.Mail;
import com.voyageone.common.util.*;
import magento.SalesOrderCancelResponseParam;
import magento.SalesOrderEntity;
import magento.SalesOrderInfoResponseParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PostWMFOrderService {
	
	private static Log logger = LogFactory.getLog(PostWMFOrderService.class);
	
	@Autowired
	private OrderDao orderDao;

	@Autowired
	MagentoApiServiceImpl magentoApiServiceImpl;
	
	@Autowired
	IssueLog issueLog;
	
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	private DefaultTransactionDefinition def = new DefaultTransactionDefinition();

	// taskName
	// 正常订单推送
	private final static String POST_WMF_NOWMFAL_ORDER = "PostWMFNormalOrder";

	// 准备取消订单推送
	private final static String POST_WMF_CANCEL_ORDER = "PostWMFPendingCancelOrder";

	// 皇马渠道ID
	private String orderChannelID = "008";


	/**
	 * 向WMF正常订单推送
	 *
	 */
	public boolean postWMFNormalOrder() {
		boolean isSuccess = true;

		return isSuccess;
	}

	/**
	 * 向WMF取消订单推送(upload)
	 *
	 */
	public boolean postWMFPendingCancelOrder() throws Exception {
		boolean isSuccess = true;

		// 订单数据抽出
		List<OrderExtend> pushOrderList = orderDao.getPendingCancelOrdersInfo(orderChannelID);

		logger.info("-----取消订单件数：" + pushOrderList.size());

		magentoApiServiceImpl.setOrderChannelId(orderChannelID);

		List<OrderExtend> cancelOrderList = new ArrayList<>();

		isSuccess = updateOrdersInfo(POST_WMF_CANCEL_ORDER, pushOrderList);

		for (OrderExtend order :pushOrderList) {
			logger.info("-------Order_Number：" + order.getOrderNumber() + "，Client_order_id：" + order.getClientOrderId());

			//品牌方订单号未设定的场合，说明还没向品牌方推送过订单，直接取消
			if (StringUtils.isNullOrBlank2(order.getClientOrderId())) {
				cancelOrderList.add(order);
				continue;
			}

			//调用Magento服务的API 获取订单信息
			SalesOrderInfoResponseParam response = magentoApiServiceImpl.getSalesOrderInfo(order.getClientOrderId());

			if (response != null) {
				SalesOrderEntity salesOrderEntity = response.getResult();
				//已经取消的场合，直接更新标志位
				if (MagentoConstants.WMF_Status.Cancelled.equals(salesOrderEntity.getStatus())) {
					cancelOrderList.add(order);
					continue;
				}
			} else{
				issueLog.log(POST_WMF_CANCEL_ORDER,
						"WMF订单信息取得失败，Order_Number：" + order.getOrderNumber() + "，Client_order_id：" + order.getClientOrderId() ,
						ErrorType.BatchJob,
						SubSystem.OMS);
				continue;
			}

			//调用Magento服务的API 取消订单
			SalesOrderCancelResponseParam cancelResponse = magentoApiServiceImpl.cancelSalesOrder(order.getClientOrderId());

			if (cancelResponse.getResult() == 0 ) {
				cancelOrderList.add(order);
				continue;
			} else{
				issueLog.log(POST_WMF_CANCEL_ORDER,
						"WMF订单取消失败，Order_Number：" + order.getOrderNumber() + "，Client_order_id：" + order.getClientOrderId() ,
						ErrorType.BatchJob,
						SubSystem.OMS);
				continue;
			}

		}

		if (cancelOrderList.size() > 0) {
			isSuccess = updateOrdersInfo(POST_WMF_CANCEL_ORDER, cancelOrderList);
		}

		return isSuccess;
	}

	/**
	 * DB更新
	 *
	 */
	private boolean updateOrdersInfo(String taskName, List<OrderExtend> pushOrderList) {
		boolean isSuccess = true;

		TransactionStatus status=transactionManager.getTransaction(def);
		try {

			// 正常订单推送
			if (POST_WMF_NOWMFAL_ORDER.equals(taskName)) {
				isSuccess = orderDao.updateOrdersSendInfo(POST_WMF_NOWMFAL_ORDER, getSelectOrderNumberList(pushOrderList));
			// 取消订单推送
			} else if (POST_WMF_CANCEL_ORDER.equals(taskName)) {
				isSuccess = orderDao.updatePendingCancelOrdersSendInfo(POST_WMF_CANCEL_ORDER, getSelectOrderNumberList(pushOrderList));
			}

			if (isSuccess) {
				String notesStr = getBatchNoteSqlData(pushOrderList, taskName);

				isSuccess = orderDao.insertNotesBatchData(notesStr, getSelectOrderNumberList(pushOrderList).size());
			}


			// 执行结果判定
			if (isSuccess) {
				transactionManager.commit(status);

			} else {
				transactionManager.rollback(status);

				issueLog.log("updateOrdersInfo",
						"updateOrdersInfo error;task name = " + taskName,
						ErrorType.BatchJob,
						SubSystem.OMS);
			}
		} catch (Exception ex) {
			logger.error("updateOrdersSendInfo", ex);

			isSuccess = false;

			transactionManager.rollback(status);
		}

		return isSuccess;
	}

	/**
	 * 取得订单列表（仅订单号）
	 */
	private List<String> getSelectOrderNumberList(List<OrderExtend> pushOrderList) {
		List<String> ret = new ArrayList<String>();

		for (int i = 0; i < pushOrderList.size(); i++) {
			OrderExtend orderInfo = pushOrderList.get(i);

			if (!ret.contains(orderInfo.getOrderNumber())) {
				ret.add(orderInfo.getOrderNumber());
			}
		}

		return ret;
	}

	/**
	 * 批处理Notes信息所需数据拼装
	 *
	 * @param orderInfoList 订单列表（含明细）
	 * @param taskName
	 * @return
	 */
	private String getBatchNoteSqlData(List<OrderExtend> orderInfoList, String taskName) {
		StringBuilder sqlBuffer = new StringBuilder();
		ArrayList<String> orderNumberList = new ArrayList<String>();

		int size = orderInfoList.size();
		for (int i = 0; i < size; i++) {
			// 订单信息
			OrderExtend orderInfo = orderInfoList.get(i);

			//	相同订单号只出一条Notes
			if (orderNumberList.contains(orderInfo.getOrderNumber())) {
				continue;
			} else {
				orderNumberList.add(orderInfo.getOrderNumber());
			}

			// 拼装SQL values 部分
			String entryDateTime = DateTimeUtil.getNow(DateTimeUtil.DEFAULT_DATETIME_FORMAT);

			if (StringUtils.isEmpty(sqlBuffer.toString())) {
				sqlBuffer.append(prepareNotesData(orderInfo, entryDateTime, entryDateTime, taskName));
			} else {
				sqlBuffer.append(Constants.COMMA_CHAR);
				sqlBuffer.append(prepareNotesData(orderInfo, entryDateTime, entryDateTime, taskName));
			}
		}

		return sqlBuffer.toString();

	}

	/**
	 * 一条订单的插入notes表语句values部分
	 *
	 * @param orderInfo
	 * @param entryDate
	 * @param entryTime
	 * @param taskName
	 * @return
	 */
	private String prepareNotesData(OrderExtend orderInfo, String entryDate, String entryTime, String taskName) {
		StringBuilder sqlValueBuffer = new StringBuilder();

		sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);

		// type
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.NOTES_SYSTEM);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// numeric_key
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(orderInfo.getOrderNumber());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// item_number
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.ZERO_CHAR);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// entry_date
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(entryDate);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// entry_time
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(entryTime);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// notes
		String note = "";
		// 正常订单推送
		if (POST_WMF_NOWMFAL_ORDER.equals(taskName)) {
			note = "Push order to WMF";
		// 取消订单推送
		} else if (POST_WMF_CANCEL_ORDER.equals(taskName)) {
			note = "Push cancel order to WMF";
		}

		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(note);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// entered_by
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(taskName);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// parent_type
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.NOTES_SYSTEM);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// parent_key
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(orderInfo.getOrderNumber());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// source_order_id
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(orderInfo.getSourceOrderId());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// creater
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(taskName);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// created
		sqlValueBuffer.append(Constants.NOW_MYSQL);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// modifier
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(taskName);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// modified
		sqlValueBuffer.append(Constants.NOW_MYSQL);

		sqlValueBuffer.append(Constants.RIGHT_BRACKET_CHAR);

		return sqlValueBuffer.toString();
	}

}
