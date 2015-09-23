package com.voyageone.batch.oms.service;

import com.csvreader.CsvReader;
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
import com.voyageone.common.mail.Mail;
import com.voyageone.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import com.csvreader.CsvWriter;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class PostRMOrderService {
	
	private static Log logger = LogFactory.getLog(PostRMOrderService.class);
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	IssueLog issueLog;
	
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	private DefaultTransactionDefinition def = new DefaultTransactionDefinition();

	// taskName
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

	//ftp连接URL
	private final String ftp_address = "ftp_address";
	// ftp连接port
	private final String ftp_port = "ftp_port";
	// ftp连接usernmae
	private final String ftp_usernmae = "ftp_uid";
	// ftp连接password
	private final String ftp_password = "ftp_pwd";
	// ftp连接password
	private final String ftp_max_next_time = "ftp_max_next_time";
	// ftp上传文件路径设置
	//		正式订单推送
	private final String oms_upload_sales_orders_file_path = "oms_upload_sales_orders_file_path";
	//		取消订单推送
	private final String oms_upload_cancel_order_file_path = "oms_upload_cancel_order_file_path";
	//		退货订单推送
	private final String oms_upload_return_order_file_path = "oms_upload_return_order_file_path";
	// ftp下载文件路径设置
	//		主动取消订单（超卖等）
	private final String oms_download_cancel_order_file_path = "oms_download_cancel_order_file_path";
	//		订单状态更新（取消订单应答）
	private final String oms_download_update_order_file_path = "oms_download_update_order_file_path";
	// 皇马渠道ID
	private String orderChannelID = "008";
//	private String orderChannelID = "005";

	// 推送文件名格式（正式订单）
	private String postFileName = "orders_%s_%s.csv";
	// 推送文件名格式（取消订单）
	private String postFileNameForPendingCancelOrder = "refunds_%s_%s.csv";
	// 推送文件名格式（取消订单）
	private String postFileNameForReturnOrder = "returned_orders_%s_%s.csv";
	// 推送文件名格式（发货订单）
	private String postFileNameForShippingOrder = "orderupdate_%s_%s.csv";

	// 下载文件返回值
	//		正常
	private final int download_ret_success = 2;
	//		文件不存在
	private final int download_ret_fileNotExist = 1;
	//		异常
	private final int download_ret_error = 0;

	// 正常订单
	private final String uploadFileKindNormal = "0";
	// 取消订单
	private final String uploadFileKindCancel = "1";

	// 输出文件编码
	private final String outputFileEncoding = "UTF-8";
	// bom文件头
	private final String bom = "BOM";

	/**
	 * 向RM正常订单推送(upload)
	 *
	 */
	public boolean postRMNormalOrder() {
		boolean isSuccess = true;

		// 推送文件名取得
		String fileName = createPostFileName();
		logger.info("createPostFileName push order = " + fileName);

		FtpBean upLoadFtpBean = formatSalesOrdersUploadFtpBean();

		// 订单数据抽出
//		List<OrderExtend> pushOrderList = orderDao.getPushOrdersInfo(orderChannelID);
		List<OrderExtend> pushOrderList = getPushOrderList(orderChannelID);

		// 有数据的场合
		if (pushOrderList.size() > 0) {
			// CSV文件做成
			if (isSuccess) {
				logger.info("createUploadFile push record count= " + pushOrderList.size());
				isSuccess = createUploadFile(upLoadFtpBean, fileName, pushOrderList, uploadFileKindNormal);
			}

			// bom 追加
			if (isSuccess) {
				logger.info("createUploadFile addBom fileName = " + fileName);
				isSuccess = addBom(upLoadFtpBean, fileName);
			}

			// FTP推送
			if (isSuccess) {
				logger.info("uploadOrderFile");
				isSuccess = uploadOrderFile(upLoadFtpBean, fileName);
			}

			// 推送标志更新
			if (isSuccess) {
				logger.info("updateOrdersSendInfo");
//				isSuccess = orderDao.updateOrdersSendInfo(POST_RM_NORMAL_ORDER, getSelectOrderNumberList(pushOrderList));
				isSuccess = updateOrdersInfo(POST_RM_NORMAL_ORDER, pushOrderList);

				if (isSuccess) {
					// 源文件
					String srcFile = upLoadFtpBean.getUpload_localpath() + "/" + upLoadFtpBean.getUpload_filename();
					// 目标文件
					String destFile = upLoadFtpBean.getUpload_local_bak_path() + "/" + upLoadFtpBean.getUpload_filename();
					logger.info("moveFile = " + srcFile + " " + destFile);
					FileUtils.moveFile(srcFile, destFile);

				} else {
					logger.info("updateOrdersSendInfo error");
					issueLog.log("postRMNormalOrder.updateOrdersSendInfo",
									"updateOrdersSendInfo error;Push order file = " + fileName,
									ErrorType.BatchJob,
									SubSystem.OMS);
				}
			} else {
				logger.info("uploadOrderFile error;Push order file = " + fileName);
				issueLog.log("postRMNormalOrder.uploadOrderFile",
						"uploadOrderFile error;Push order file = " + fileName,
						ErrorType.BatchJob,
						SubSystem.OMS);

				// 源文件
				String srcFile = upLoadFtpBean.getUpload_localpath() + "/" + upLoadFtpBean.getUpload_filename();
				logger.info("delFile = " + srcFile);
				FileUtils.delFile(srcFile);
			}
		} else {
			logger.info("createPostFileName push order rec = 0");
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
			String noteContent = "";

			// 正常订单推送
			if (POST_RM_NORMAL_ORDER.equals(taskName)) {
				isSuccess = orderDao.updateOrdersSendInfo(POST_RM_NORMAL_ORDER, getSelectOrderNumberList(pushOrderList));
			// 取消订单推送
			} else if (POST_RM_CANCEL_ORDER.equals(taskName)) {
				isSuccess = orderDao.updatePendingCancelOrdersSendInfo(POST_RM_CANCEL_ORDER, getSelectOrderNumberList(pushOrderList));
			// 退货订单推送
			} else if (POST_RM_RETURN_ORDER.equals(taskName)) {
				isSuccess = updateReturnOrdersSendInfo(POST_RM_RETURN_ORDER, pushOrderList);
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
		if (POST_RM_NORMAL_ORDER.equals(taskName)) {
			note = "Push order to RM";
		// 取消订单推送
		} else if (POST_RM_CANCEL_ORDER.equals(taskName)) {
			note = "Push cancel order to RM";
		// 退货订单推送
		} else if (POST_RM_RETURN_ORDER.equals(taskName)) {
			note = "Push return order to RM";
		// 皇马主动取消订单
		} else if (DOWNLOAD_RM_CANCEL_ORDER.equals(taskName)) {
			note = "RM cancelled order";
		// 皇马订单状态变为取消
		} else if (DOWNLOAD_RM_UPDATE_ORDER.equals(taskName)) {
			note = "RM change order status to cancelled";
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

	/**
	 * 推送订单数据抽取
	 *
	 */
	private List<OrderExtend> getPushOrderList(String orderChannelId) {
		List<OrderExtend> orderExtendList = new ArrayList<OrderExtend>();

		// 订单信息取得
		List<OutFormOrderdetailOrders> ordersList = orderDao.getOrdersListByOrderChannelId(orderChannelId);
		if (ordersList.size() == 0) {
			return orderExtendList;
		}

		// 订单详情取得
		List<OutFormOrderDetailOrderDetail> orderDetailsListForOrdersList = orderDao.getOrderDetailsInfo(orderChannelId, getOrderNumberList(ordersList));

		// 订单详细再设定（绑定折扣）
		for (int i = 0; i < ordersList.size(); i++) {
			OutFormOrderdetailOrders orderInfo = ordersList.get(i);
			//	订单明细信息取得
			List<OutFormOrderDetailOrderDetail> orderDetailsList = getOrderDetailsList(orderDetailsListForOrdersList, orderInfo.getOrderNumber());
			//	订单明细信息中，信息再设定
			setCustomOrderDetailFields(orderInfo, orderDetailsList);
			//	订单明细设定
			orderInfo.setOrderDetailsList(orderDetailsList);
		}

		// 订单折扣算出
		for (int i = 0; i < ordersList.size(); i++) {
			OutFormOrderdetailOrders orderInfo = ordersList.get(i);

			orderInfo.setRevisedOrderDiscount(orderInfo.getRevisedDiscount());

			List<OutFormOrderDetailOrderDetail> orderDetailsList = orderInfo.getOrderDetailsList();
			for (int j = 0; j < orderDetailsList.size(); j++) {
				OutFormOrderDetailOrderDetail orderDetailInfo = orderDetailsList.get(j);

				// 物品的场合
				if (!orderDetailInfo.isAdjustment()) {
					if (!StringUtils.isEmpty(orderDetailInfo.getDiscount())) {
						float orderDiscount = sub2Digits(Float.valueOf(orderInfo.getRevisedOrderDiscount()),Float.valueOf(orderDetailInfo.getDiscount()));
						orderInfo.setRevisedOrderDiscount(String.valueOf(orderDiscount));
					}
				}
			}
		}

		// 订单折扣算出
		for (int i = 0; i < ordersList.size(); i++) {
			OutFormOrderdetailOrders orderInfo = ordersList.get(i);

			List<OutFormOrderDetailOrderDetail> orderDetailsList = orderInfo.getOrderDetailsList();
			for (int j = 0; j < orderDetailsList.size(); j++) {

				OutFormOrderDetailOrderDetail orderDetailInfo = orderDetailsList.get(j);

				if (!orderDetailInfo.isAdjustment()) {
					OrderExtend orderExtendInfo = new OrderExtend();
					// 订单信息
					orderExtendInfo.setSourceOrderId(orderInfo.getSourceOrderId());
					orderExtendInfo.setOrderNumber(orderInfo.getOrderNumber());
					orderExtendInfo.setPayNo(orderInfo.getPoNumber());
					orderExtendInfo.setName(orderInfo.getName());
					orderExtendInfo.setEmail(orderInfo.getEmail());
					orderExtendInfo.setPhone(orderInfo.getPhone());
					orderExtendInfo.setCompany(orderInfo.getCompany());
					orderExtendInfo.setAddress(orderInfo.getAddress());
					orderExtendInfo.setAddress2(orderInfo.getAddress2());
					orderExtendInfo.setCity(orderInfo.getCity());
					orderExtendInfo.setZip(orderInfo.getZip());
					orderExtendInfo.setOrderDateTime(orderInfo.getOrderDateTime());
					orderExtendInfo.setShipName(orderInfo.getShipName());
					orderExtendInfo.setShipAddress(orderInfo.getShipAddress());
					orderExtendInfo.setShipAddress2(orderInfo.getShipAddress2());
					orderExtendInfo.setShipCity(orderInfo.getShipCity());
					orderExtendInfo.setShipZip(orderInfo.getShipZip());

					orderExtendInfo.setFinalShippingTotal(orderInfo.getFinalShippingTotal());
					orderExtendInfo.setFinalGrandTotal(orderInfo.getFinalGrandTotal());
					orderExtendInfo.setTaobaoLogisticsId(orderInfo.getTaobaoLogisticsId());
					// 订单折扣
					orderExtendInfo.setOrderDiscount(orderInfo.getRevisedOrderDiscount());

					// 订单明细
					orderExtendInfo.setClientSku(orderDetailInfo.getClientSku());
					orderExtendInfo.setProduct(orderDetailInfo.getProduct());
					orderExtendInfo.setQuantityOrdered(orderDetailInfo.getQuantityOrdered());
					//	扣除折扣后售价
					orderExtendInfo.setPrice(orderDetailInfo.getPrice());
					// 	物品折扣
					orderExtendInfo.setItemDiscount(orderDetailInfo.getDiscount());

					// 输出退避
					orderExtendList.add(orderExtendInfo);
				}
			}
		}

		return orderExtendList;
	}

	/**
	 * 获得订单明细信息，根据订单号
	 *
	 * @param orderDetailsListForOrdersList 根据原订单号获得的一组订单明细信息
	 * @param orderNumber 订单号
	 *
	 * @return
	 */
	private List<OutFormOrderDetailOrderDetail> getOrderDetailsList(List<OutFormOrderDetailOrderDetail> orderDetailsListForOrdersList, String orderNumber) {
		List<OutFormOrderDetailOrderDetail> orderDetailsList = new ArrayList<OutFormOrderDetailOrderDetail>();

		for (int i = 0; i < orderDetailsListForOrdersList.size(); i++) {
			OutFormOrderDetailOrderDetail orderDetailsInfo = orderDetailsListForOrdersList.get(i);

			if (orderNumber.equals(orderDetailsInfo.getOrderNumber())) {
				orderDetailsList.add(orderDetailsInfo);
			}
		}

		return orderDetailsList;
	}

	private List<String> getOrderNumberList(List<OutFormOrderdetailOrders> ordersList) {
		ArrayList<String> orderNumberList = new ArrayList<String>();

		for (int i = 0; i < ordersList.size(); i++) {
			orderNumberList.add(ordersList.get(i).getOrderNumber());
		}

		return orderNumberList;
	}

	/**
	 * 订单明细字段再设定
	 *
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * @return
	 */
	private void setCustomOrderDetailFields(OutFormOrderdetailOrders ordersInfo,
											List<OutFormOrderDetailOrderDetail> orderDetailsList) {
		if (orderDetailsList != null) {
			// 当前订单sku缓存
			List<String> skuList = new ArrayList<String>();
			// 订单运输渠道（根据订单明细取得）
			String shipChannel = "";

			// 初次设定
			for (int i = 0; i < orderDetailsList.size(); i++) {
				OutFormOrderDetailOrderDetail orderDetailsInfo = orderDetailsList.get(i);

				// 运输渠道设定
				if (!StringUtils.isEmpty(orderDetailsInfo.getShipChannel())) {
					shipChannel = orderDetailsInfo.getShipChannel();
				}

				// pricePerUnit
				orderDetailsInfo.setPricePerUnit(StringUtils.getFormatedMoney(orderDetailsInfo.getPricePerUnit()));

				if (OmsConstants.OrderDetailSkuDsp.PRODUCT_TITLE.endsWith(orderDetailsInfo.getSku())) {
					//  return 明细信息不显示（例：Returned (1) 11821313-6: Dr. Martens 1460 W 马丁大夫经典八孔女子真皮马丁靴 美国正品）
					orderDetailsInfo.setShowFlag(false);

				} else if (StringUtils.isEmpty(orderDetailsInfo.getSubItemNumber()) || "0".equals(orderDetailsInfo.getSubItemNumber())) {
					// 普通明细信息
					orderDetailsInfo.setShowFlag(true);
				} else {
					// 关联打折信息
					orderDetailsInfo.setShowFlag(false);
				}

				// 物品的场合
				if (!orderDetailsInfo.isAdjustment()) {
					// 打折信息加算
					float discount = 0;
					for (int j = 0; j < orderDetailsList.size(); j++) {
						OutFormOrderDetailOrderDetail orderDetailsDiscountInfo = orderDetailsList.get(j);
						if (orderDetailsInfo.getItemNumber().equals(orderDetailsDiscountInfo.getSubItemNumber())) {
							discount = add2Digits(discount, Float.valueOf(orderDetailsDiscountInfo.getPricePerUnit()));
						}
					}

					// 打折信息设定
					if (discount != 0) {
						orderDetailsInfo.setDiscount(String.valueOf(discount));
					}

					// 售价设定（该处discount 已经 *-1）
					float price = add2Digits(Float.valueOf(orderDetailsInfo.getPricePerUnit()), discount);
					orderDetailsInfo.setPrice(String.valueOf(price));
				}
			}
		}
	}

	/**
	 * 向RM取消订单推送(upload)
	 *
	 */
	public boolean postRMPendingCancelOrder() {
		boolean isSuccess = true;

		// 推送文件名取得
		String fileName = createPostFileNameForPendingCancelOrder();
		logger.info("createPostFileName pending cancel order = " + fileName);

		FtpBean upLoadFtpBean = formatCancelOrdersUploadFtpBean();

		// 订单数据抽出
		List<OrderExtend> pushOrderList = orderDao.getPendingCancelOrdersInfo(orderChannelID);

		// 有数据的场合
		if (pushOrderList.size() > 0) {
			// CSV文件做成
			if (isSuccess) {
				logger.info("createUploadFile pending cancel record count = " + pushOrderList.size());
				isSuccess = createUploadFileForPendingCancelOrder(upLoadFtpBean, fileName, pushOrderList, uploadFileKindCancel);
			}

			// bom 追加
			if (isSuccess) {
				logger.info("createUploadFile addBom fileName = " + fileName);
				isSuccess = addBom(upLoadFtpBean, fileName);
			}

			// FTP推送
			if (isSuccess) {
				logger.info("uploadOrderFile");
				isSuccess = uploadOrderFile(upLoadFtpBean, fileName);
			}

			// 推送标志更新
			if (isSuccess) {
				logger.info("updatePendingCancelOrdersSendInfo");
//				isSuccess = orderDao.updatePendingCancelOrdersSendInfo(POST_RM_CANCEL_ORDER, getSelectOrderNumberList(pushOrderList));
				isSuccess = updateOrdersInfo(POST_RM_CANCEL_ORDER, pushOrderList);

				if (isSuccess) {
					// 源文件
					String srcFile = upLoadFtpBean.getUpload_localpath() + "/" + upLoadFtpBean.getUpload_filename();
					// 目标文件
					String destFile = upLoadFtpBean.getUpload_local_bak_path() + "/" + upLoadFtpBean.getUpload_filename();
					logger.info("moveFile = " + srcFile + " " + destFile);
					FileUtils.moveFile(srcFile, destFile);

				} else {
					logger.info("updatePendingCancelOrdersSendInfo error");
					issueLog.log("postRMNormalOrder.updatePendingCancelOrdersSendInfo",
							"updatePendingCancelOrdersSendInfo error;Push order file = " + fileName,
							ErrorType.BatchJob,
							SubSystem.OMS);
				}
			} else {
				logger.info("uploadOrderFile error;Push pending cancel order file = " + fileName);
				issueLog.log("postRMNormalOrder.uploadOrderFile",
						"uploadOrderFile error;Push pending cancel order file = " + fileName,
						ErrorType.BatchJob,
						SubSystem.OMS);

				// 源文件
				String srcFile = upLoadFtpBean.getUpload_localpath() + "/" + upLoadFtpBean.getUpload_filename();
				logger.info("delFile = " + srcFile);
				FileUtils.delFile(srcFile);
			}
		}

		return isSuccess;
	}

	/**
	 * 向RM取消订单推送(upload)
	 *
	 */
	public boolean postRMReturnOrder() {
		boolean isSuccess = true;

		// 推送文件名取得
		String fileName = createPostFileNameForReturnOrder();
		logger.info("createPostFileName return order = " + fileName);

		FtpBean upLoadFtpBean = formatReturnOrdersUploadFtpBean();

		// 订单数据抽出
		List<OrderExtend> pushOrderList = orderDao.getPushReturnOrdersInfo(orderChannelID);

		// 有数据的场合
		if (pushOrderList.size() > 0) {
			// CSV文件做成
			if (isSuccess) {
				logger.info("createUploadFile return record count = " + pushOrderList.size());
				isSuccess = createUploadFileForReturnOrder(upLoadFtpBean, fileName, pushOrderList, uploadFileKindCancel);
			}

			// bom 追加
			if (isSuccess) {
				logger.info("createUploadFile addBom fileName = " + fileName);
				isSuccess = addBom(upLoadFtpBean, fileName);
			}

			// FTP推送
			if (isSuccess) {
				logger.info("uploadOrderFile");
				isSuccess = uploadOrderFile(upLoadFtpBean, fileName);
			}

			// 推送标志更新
			if (isSuccess) {
				logger.info("updatePendingCancelOrdersSendInfo");
//				isSuccess = updateReturnOrdersSendInfo(POST_RM_RETURN_ORDER, pushOrderList);
				isSuccess = updateOrdersInfo(POST_RM_RETURN_ORDER, pushOrderList);

				if (isSuccess) {
					// 源文件
					String srcFile = upLoadFtpBean.getUpload_localpath() + "/" + upLoadFtpBean.getUpload_filename();
					// 目标文件
					String destFile = upLoadFtpBean.getUpload_local_bak_path() + "/" + upLoadFtpBean.getUpload_filename();
					logger.info("moveFile = " + srcFile + " " + destFile);
					FileUtils.moveFile(srcFile, destFile);

				} else {
					logger.info("updateReturnOrdersSendInfo error");
					issueLog.log("postRMReturnOrder.updateReturnOrdersSendInfo",
							"updateReturnOrdersSendInfo error;Push order file = " + fileName,
							ErrorType.BatchJob,
							SubSystem.OMS);
				}
			} else {
				logger.info("uploadOrderFile error;Push return order file = " + fileName);
				issueLog.log("postRMReturnOrder.uploadOrderFile",
						"uploadOrderFile error;Push return order file = " + fileName,
						ErrorType.BatchJob,
						SubSystem.OMS);

				// 源文件
				String srcFile = upLoadFtpBean.getUpload_localpath() + "/" + upLoadFtpBean.getUpload_filename();
				logger.info("delFile = " + srcFile);
				FileUtils.delFile(srcFile);
			}
		}

		return isSuccess;
	}

	private boolean updateReturnOrdersSendInfo(String taskName, List<OrderExtend> pushOrderList) {
		boolean ret = true;

		for (int i = 0; i < pushOrderList.size(); i++) {
			OrderExtend orderExtend = pushOrderList.get(i);
			ret = orderDao.updateReturnOrdersSendInfo(taskName, orderExtend.getOrderNumber(), orderExtend.getItemNumber());

			if (!ret) {
				break;
			}
		}

		return ret;
	}

	/**
	 * 取RM订单(download)
	 */
	public boolean downloadRMOrderCancelledOrder() {
		boolean isSuccess = true;
		logger.info("downloadRMOrderCancelledOrder");

		FtpBean downloadFtpBean = formatCancelOrderDownloadFtpBean();

//		// CSV文件下载，下载后删除
//		logger.info("downloadRMOrderSub");
//		List<Object> ret = downloadRMOrderSub(downloadFtpBean);
//		// 下载返回结果
//		boolean downloadResult = (boolean)ret.get(0);
//		// 下载文件一览
//		List<String> downloadFileList = (List<String>) ret.get(1);
//
//		logger.info("downloadRMOrderSub ret = " + downloadResult);
//		logger.info("downloadRMOrderSub fileList = " + getArrayListString(downloadFileList));

		// 文件名列表取得
		boolean downloadResult = true;
		List<String> downloadFileList = FileUtils.getFileGroup(downloadFtpBean.getDown_filename(), downloadFtpBean.getDown_localpath());
		logger.info("downloadRMOrderCancelledOrder FileList = " + getArrayListString(downloadFileList));

		// CSV文件读取，客服邮件发送
		if (downloadResult && downloadFileList.size() > 0) {

			// CSV文件读取
			logger.info("getSourceOrderIdListFromDownLoadFile");
			List<Object> readDownloadFile = getSourceOrderIdListFromDownLoadFile(downloadFileList, downloadFtpBean);
			// 	下载返回结果
			boolean readDownloadFileResult = (boolean)readDownloadFile.get(0);
			// 	下载文件中，订单一览
			List<String> readDownloadFileSourceOrderId = (List<String>) readDownloadFile.get(1);

			logger.info("getSourceOrderIdListFromDownLoadFile SourceOrderIdList = " + getArrayListString(readDownloadFileSourceOrderId));

			//	DB更新，客服邮件发送
			if (readDownloadFileResult && readDownloadFileSourceOrderId.size() > 0) {

				// 客服邮件通知
				logger.info("sendCustomerServiceMail");
				sendCustomerServiceMail(readDownloadFileSourceOrderId);

				logger.info("updatePaternCancelOrdersInfo");
//				isSuccess = orderDao.updatePaternCancelOrdersInfo(DOWNLOAD_RM_CANCEL_ORDER, readDownloadFileSourceOrderId);
				isSuccess = updateOrdersInfoForDownLoad(DOWNLOAD_RM_CANCEL_ORDER, readDownloadFileSourceOrderId);

				if (isSuccess) {
					// 文件移动
					for (int i = 0 ; i < downloadFileList.size(); i++)	 {
						String downloadFileName = downloadFileList.get(i);
						// 源文件
						String srcFile = downloadFtpBean.getDown_localpath() + "/" + downloadFileName;
						// 目标文件
//						String destFile = downloadFtpBean.getDown_local_bak_path() + "/" + downloadFileName;
						String destFile = downloadFtpBean.getDown_local_bak_path() + "/" + addTimestampToFileName(downloadFileName);
						logger.info("moveFile = " + srcFile + " " + destFile);
						FileUtils.moveFile(srcFile, destFile);
					}
				} else {
					// DB更新异常
					logger.info("updatePaternCancelOrdersInfo error");
					issueLog.log("downloadRMOrderCancelledOrder.updatePaternCancelOrdersInfo",
							"updatePaternCancelOrdersInfo error SourceOrderIdList = " + getArrayListString(readDownloadFileSourceOrderId),
							ErrorType.BatchJob,
							SubSystem.OMS);
				}
			}
		}

		return isSuccess;
	}

	/**
	 * 文件名中timestamp 追加
	 *
	 */
	private String addTimestampToFileName(String fileNameStr) {
		String retFile = fileNameStr.substring(0, fileNameStr.lastIndexOf(".")) + "_" + DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_2) + fileNameStr.substring(fileNameStr.lastIndexOf("."));

		return retFile;
	}

	/**
	 * DB更新（下载用，根据下载的sourceOrderId）
	 *
	 */
	private boolean updateOrdersInfoForDownLoad(String taskName, List<String> sourceOrderIdList) {
		boolean isSuccess = true;

		TransactionStatus status=transactionManager.getTransaction(def);
		try {
			String noteContent = "";

			// 取消订单下载
			if (DOWNLOAD_RM_CANCEL_ORDER.equals(taskName)) {
				isSuccess = orderDao.updatePaternCancelOrdersInfo(DOWNLOAD_RM_CANCEL_ORDER, sourceOrderIdList);
			// 订单状态变更下载
			} else if (DOWNLOAD_RM_UPDATE_ORDER.equals(taskName)) {
				isSuccess = orderDao.updatePaternCancelOrdersInfo(DOWNLOAD_RM_UPDATE_ORDER, sourceOrderIdList);
			}

			if (isSuccess) {
				List<OrderExtend> orderList = orderDao.getOrdersInfo(sourceOrderIdList);

				String notesStr = getBatchNoteSqlData(orderList, taskName);

				isSuccess = orderDao.insertNotesBatchData(notesStr, orderList.size());
			}


			// 执行结果判定
			if (isSuccess) {
				transactionManager.commit(status);

			} else {
				transactionManager.rollback(status);

				issueLog.log("updateOrdersInfoForDownLoad",
						"updateOrdersInfoForDownLoad error;task name = " + taskName,
						ErrorType.BatchJob,
						SubSystem.OMS);
			}
		} catch (Exception ex) {
			logger.error("updateOrdersInfoForDownLoad", ex);

			isSuccess = false;

			transactionManager.rollback(status);

			issueLog.log("updateOrdersInfoForDownLoad",
					"updateOrdersInfoForDownLoad error;task name = " + taskName,
					ErrorType.BatchJob,
					SubSystem.OMS);
		}

		return isSuccess;
	}

	/**
	 * 取RM订单(download)
	 */
	public boolean downloadRMOrderUpdateOrder() {
		boolean isSuccess = true;
		logger.info("downloadRMOrderUpdateOrder");

		FtpBean downloadFtpBean = formatUpdateOrderDownloadFtpBean();

//		// CSV文件下载，下载后删除
//		logger.info("downloadRMOrderSub");
//		List<Object> ret = downloadRMOrderSub(downloadFtpBean);
//		// 下载返回结果
//		boolean downloadResult = (boolean)ret.get(0);
//		// 下载文件一览
//		List<String> downloadFileList = (List<String>) ret.get(1);
//
//		logger.info("downloadRMOrderSub ret = " + downloadResult);
//		logger.info("downloadRMOrderSub fileList = " + getArrayListString(downloadFileList));

		boolean downloadResult = true;
		List<String> downloadFileList = FileUtils.getFileGroup(downloadFtpBean.getDown_filename(), downloadFtpBean.getDown_localpath());
		logger.info("downloadRMOrderUpdateOrder FileList = " + getArrayListString(downloadFileList));

		// CSV文件读取，客服邮件发送
		if (downloadResult && downloadFileList.size() > 0) {

			logger.info("getSourceOrderIdListFromDownLoadFile");
			List<Object> readDownloadFile = getSourceOrderIdListFromDownLoadFileForUpdate(downloadFileList, downloadFtpBean);
			// 下载返回结果
			boolean readDownloadFileResult = (boolean)readDownloadFile.get(0);
			// 下载文件中，订单一览
			List<String> readDownloadFileCancelSourceOrderId = (List<String>) readDownloadFile.get(1);
			List<String> readDownloadFileErrorSourceOrderId = (List<String>) readDownloadFile.get(2);
			List<InFormFile> readDownloadFileShippingSourceOrderId = (List<InFormFile>) readDownloadFile.get(3);

			logger.info("getSourceOrderIdListFromDownLoadFile cancelSourceOrderIdList = " + getArrayListString(readDownloadFileCancelSourceOrderId));
			logger.info("getSourceOrderIdListFromDownLoadFile errorSourceOrderIdList = " + getArrayListString(readDownloadFileErrorSourceOrderId));
			logger.info("getSourceOrderIdListFromDownLoadFile shippingSourceOrderIdList = " + getArrayListStringForInFormFile(readDownloadFileShippingSourceOrderId));

			// 读文件正常的场合
			if (readDownloadFileResult) {

				// IT邮件通知
				if (readDownloadFileErrorSourceOrderId.size() > 0) {
					logger.info("sendITMail");
					sendITMail(readDownloadFileErrorSourceOrderId);
				}

				// 客服邮件通知
				if (readDownloadFileCancelSourceOrderId.size() > 0) {
					logger.info("sendCustomerServiceMail");
					sendCustomerServiceMail(readDownloadFileCancelSourceOrderId);
				}

				// Shipping 文件生成
				if (readDownloadFileShippingSourceOrderId.size() > 0) {
					// Shipping文件名取得
					String fileName = createDownloadFileNameForShippingOrder();
					logger.info("createDownloadFileNameForShippingOrder shipping order = " + fileName);

					logger.info("createDownloadFileForShipping");
					createDownloadFileForShipping(downloadFtpBean, fileName, readDownloadFileShippingSourceOrderId);
				}

				// 取消订单处理
				if (readDownloadFileCancelSourceOrderId.size() > 0) {
					logger.info("updatePaternCancelOrdersInfo");
//					isSuccess = orderDao.updatePaternCancelOrdersInfo(DOWNLOAD_RM_UPDATE_ORDER, readDownloadFileCancelSourceOrderId);
					isSuccess = updateOrdersInfoForDownLoad(DOWNLOAD_RM_UPDATE_ORDER, readDownloadFileCancelSourceOrderId);
				}

				if (isSuccess) {
					// 文件移动
					for (int i = 0 ; i < downloadFileList.size(); i++)	 {
						String downloadFileName = downloadFileList.get(i);
						// 源文件
						String srcFile = downloadFtpBean.getDown_localpath() + "/" + downloadFileName;
						// 目标文件
						String destFile = downloadFtpBean.getDown_local_bak_path() + "/" + addTimestampToFileName(downloadFileName);
						logger.info("moveFile = " + srcFile + " " + destFile);
						FileUtils.moveFile(srcFile, destFile);
					}
				} else {
					// DB更新异常
					logger.info("updatePaternCancelOrdersInfo error");
					issueLog.log("downloadRMOrderUpdateOrder.updatePaternCancelOrdersInfo",
							"updatePaternCancelOrdersInfo error SourceOrderIdList = " + getArrayListString(readDownloadFileCancelSourceOrderId),
							ErrorType.BatchJob,
							SubSystem.OMS);
				}
			}
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
	 * 从下载文件中取得订单列表（仅订单号）
	 *
	 * @param downloadFileList 下载文件列表
	 * @param ftpBean 下载文件用Bean
	 * @return list[0]	boolean 执行结果
	 * 			list[1] List<String>  下载文件中订单一览
	 */
	private List<Object> getSourceOrderIdListFromDownLoadFile(List<String> downloadFileList, FtpBean ftpBean) {
		List<Object> ret = new ArrayList<>();

		boolean isSuccess = true;
		List<String> cancelledSourceOrderIdList = new ArrayList<String>();

		try {
			for (int i = 0; i < downloadFileList.size(); i++) {
				CsvReader reader = new CsvReader(new FileInputStream(ftpBean.getDown_localpath() + "/" + downloadFileList.get(i)), ',', Charset.forName("GBK"));

				// Head读入
				reader.readHeaders();
				String[] headers = reader.getHeaders();

				// Body读入
				while (reader.readRecord()) {
					String sourceOrderId = reader.get(CancelledOrderFileFormat.MarketplaceOrderId);

					if (!cancelledSourceOrderIdList.contains(sourceOrderId)) {
						cancelledSourceOrderIdList.add(sourceOrderId);
					}
				}
				reader.close();
			}
		} catch (Exception e) {
			isSuccess = false;

			logger.error("getSourceOrderIdListFromDownLoadFile", e);

			issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);
		}

		ret.add(isSuccess);
		ret.add(cancelledSourceOrderIdList);

		return ret;
	}

	/**
	 * 从下载文件中取得订单列表（仅订单号）
	 *
	 * @param downloadFileList 下载文件列表
	 * @param ftpBean 下载文件用Bean
	 * @return list[0]	boolean 执行结果
	 * 			list[1] List<String>  下载文件中,取消订单一览
	 * 			list[2] List<String>  下载文件中,异常订单一览
	 * 			list[3] List<InFormFile>  下载文件中,发货订单一览
	 */
	private List<Object> getSourceOrderIdListFromDownLoadFileForUpdate(List<String> downloadFileList, FtpBean ftpBean) {
		List<Object> ret = new ArrayList<>();

		boolean isSuccess = true;
		// 取消订单一览
		List<String> cancelledSourceOrderIdList = new ArrayList<String>();
		// 异常订单一览
		List<String> errorSourceOrderIdList = new ArrayList<String>();
		// 发货订单一览
		List<InFormFile> shippingSourceOrderIdList = new ArrayList<InFormFile>();

		try {
			for (int i = 0; i < downloadFileList.size(); i++) {
				String fileName = downloadFileList.get(i);

				CsvReader reader = new CsvReader(new FileInputStream(ftpBean.getDown_localpath() + "/" + downloadFileList.get(i)), ',', Charset.forName("GBK"));

				// Head读入
				reader.readHeaders();
				String[] headers = reader.getHeaders();

				// Body读入
				while (reader.readRecord()) {
					String sourceOrderId = reader.get(UpdateOrderFileFormat.MarketplaceOrderId);
					String orderStatus = reader.get(UpdateOrderFileFormat.StatusReason);

					// 取消订单的场合
					if (OmsConstants.RMOrderStatus.PartiallyFulfillable.equalsIgnoreCase(orderStatus) ||
							OmsConstants.RMOrderStatus.TotallyUnfulfillable.equalsIgnoreCase(orderStatus) ||
							OmsConstants.RMOrderStatus.BackOrder.equalsIgnoreCase(orderStatus) ||
							OmsConstants.RMOrderStatus.Refunded.equalsIgnoreCase(orderStatus) ||
							OmsConstants.RMOrderStatus.Cancelled.equalsIgnoreCase(orderStatus)) {
						if (!cancelledSourceOrderIdList.contains(sourceOrderId)) {
							cancelledSourceOrderIdList.add(sourceOrderId);
						}

					// 异常订单的场合
					} else if (OmsConstants.RMOrderStatus.FailedValidation.equalsIgnoreCase(orderStatus) ||
							OmsConstants.RMOrderStatus.FailedOrderCreation.equalsIgnoreCase(orderStatus)) {
						if (!errorSourceOrderIdList.contains(sourceOrderId)) {
							errorSourceOrderIdList.add(sourceOrderId);
						}

					// 发货订单的场合
					} else if (OmsConstants.RMOrderStatus.Despatched.equalsIgnoreCase(orderStatus) ||
							OmsConstants.RMOrderStatus.DespatchedWithTracking.equalsIgnoreCase(orderStatus)) {

						InFormFile shippingOrder = new InFormFile();
						shippingOrder.setSourceOrderId(sourceOrderId);
						shippingOrder.setFileName(fileName);

						if (!shippingSourceOrderIdList.contains(shippingOrder)) {
							shippingSourceOrderIdList.add(shippingOrder);
						}
					}
				}
				reader.close();
			}
		} catch (Exception e) {
			isSuccess = false;

			logger.error("getSourceOrderIdListFromDownLoadFileForUpdate", e);

			issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);
		}

		ret.add(isSuccess);
		ret.add(cancelledSourceOrderIdList);
		ret.add(errorSourceOrderIdList);
		ret.add(shippingSourceOrderIdList);

		return ret;
	}

	/**
	 * 推送文件名生成（orders_%s_%s.csv）
	 *
	 */
	private String createPostFileName() {
		String date = DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_3);
		String time = DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_4);

		return String.format(postFileName, date, time);
	}

	/**
	 * 推送文件名生成（refunds_%s_%s.csv）
	 *
	 */
	private String createPostFileNameForPendingCancelOrder() {
		String date = DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_3);
		String time = DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_4);

		return String.format(postFileNameForPendingCancelOrder, date, time);
	}

	/**
	 * 推送文件名生成（returned_orders_%s_%s.csv）
	 *
	 */
	private String createPostFileNameForReturnOrder() {
		String date = DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_3);
		String time = DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_4);

		return String.format(postFileNameForReturnOrder, date, time);
	}

	/**
	 * 推送文件名生成（orderupdate_%s_%s.csv）
	 *
	 */
	private String createDownloadFileNameForShippingOrder() {
		String date = DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_3);
		String time = DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_4);

		return String.format(postFileNameForShippingOrder, date, time);
	}

	/**
	 * 取RM订单子函数(CSV文件下载，下载后删除)
	 *
	 * @param downloadFtpBean
	 * @return list[0]	boolean 执行结果
	 * 			list[1] List<String>  下载文件名列表
	 */
	private List<Object> downloadRMOrderSub(FtpBean downloadFtpBean) {
		List<Object> ret = new ArrayList<>();

		boolean isSuccess = true;
		List<String> downloadFileList = new ArrayList<String>();

		FTPClient ftpClient = new FTPClient();
		FtpUtil ftpUtil = new FtpUtil();

		try{
			//建立连接
			ftpClient = ftpUtil.linkFtp(downloadFtpBean);

			logger.info("getFolderFileNames");
			// 下载文件列表取得
			downloadFileList = ftpUtil.getFolderFileNames(downloadFtpBean, ftpClient, downloadFtpBean.getDown_remotepath());

			if (downloadFileList.size() > 0) {
				// 文件下载
				logger.info("downLoadCancelOrderFile");
				isSuccess = downLoadCancelOrderFile(downloadFtpBean, ftpClient, ftpUtil, downloadFileList);

//				// 服务器文件删除
//				if (isSuccess) {
//					logger.info("delCloudFile");
//					isSuccess = delCloudFile(downloadFtpBean, ftpClient, ftpUtil, downloadFileList);
//				}
				// 服务器文件备份
				if (isSuccess) {
					logger.info("removeCloudFile");
					isSuccess = removeCloudFile(downloadFtpBean, ftpClient, ftpUtil, downloadFileList);
				}
			}

		} catch (Exception e) {

			isSuccess = false;

			logger.error("downloadRMOrderSub", e);

			issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);

		} finally {
			try {
				//断开连接
				ftpUtil.disconnectFtp(ftpClient);

			} catch (Exception e) {
				isSuccess = false;

				logger.error("downloadRMOrderSub", e);

				issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);
			}
		}

		ret.add(isSuccess);
		ret.add(downloadFileList);

		return ret;
	}

	/**
	 * 目录夹文件取得
	 *
	 */
	private File[] getFileList(String dirPath) {
		File dir = new File(dirPath);
		File[] ret = dir.listFiles();

		return ret;
	}

	/**
	 * 上传文件做成
	 *
	 * @param fileName 待上传文件名
	 */
	private boolean createUploadFile(FtpBean ftpBean, String fileName, List<OrderExtend> pushOrderList, String fileKind) {
		boolean isSuccess = true;

		// 本地文件生成路径
		String uploadLocalPath = ftpBean.getUpload_localpath();

		try {
//			CsvWriter csvWriter = new CsvWriter(new FileWriter(uploadLocalPath + "/" + fileName), ',', Charset.forName("GB2312"));

//			CsvWriter csvWriter = new CsvWriter(new FileWriter(uploadLocalPath + "/" + fileName), ',');
			File file = new File(uploadLocalPath + "/" + fileName);
			FileOutputStream  fop = new FileOutputStream(file);
//			CsvWriter csvWriter = new CsvWriter(fop, ',', Charset.forName("GB2312"));
			CsvWriter csvWriter = new CsvWriter(fop, ',', Charset.forName(outputFileEncoding));


			// Head输出
			createUploadFileHead(csvWriter);

			// Body输出
			createUploadFileBody(csvWriter, pushOrderList, fileKind);

			csvWriter.flush();
			csvWriter.close();

		} catch (Exception e) {
			isSuccess = false;

			logger.error("createUploadFile", e);

			issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);
		}

		return isSuccess;
	}

	/**
	 * 上传文件做成
	 *
	 * @param csvWriter 上传文件Handler
	 */
	private void createUploadFileHead(CsvWriter csvWriter) throws IOException {
		csvWriter.write(bom + PushOrderFileFormat.LinnworksOrderId);
		csvWriter.write(PushOrderFileFormat.ReferenceNum);
		csvWriter.write(PushOrderFileFormat.AlipayTransactionId);
		csvWriter.write(PushOrderFileFormat.BuyerTitle);
		csvWriter.write(PushOrderFileFormat.BuyerFirstName);
		csvWriter.write(PushOrderFileFormat.BuyerLastName);
		csvWriter.write(PushOrderFileFormat.Email_Address);
		csvWriter.write(PushOrderFileFormat.BuyerPhone);
		csvWriter.write(PushOrderFileFormat.Company);
		csvWriter.write(PushOrderFileFormat.Address1);
		csvWriter.write(PushOrderFileFormat.Address2);
		csvWriter.write(PushOrderFileFormat.Address3);
		csvWriter.write(PushOrderFileFormat.Town);
		csvWriter.write(PushOrderFileFormat.Country);
		csvWriter.write(PushOrderFileFormat.Country_Code);
		csvWriter.write(PushOrderFileFormat.PostCode);
		csvWriter.write(PushOrderFileFormat.ReceivedDate);
		csvWriter.write(PushOrderFileFormat.Status);
		csvWriter.write(PushOrderFileFormat.Source);
		csvWriter.write(PushOrderFileFormat.Subsource);
		csvWriter.write(PushOrderFileFormat.SiteName);
		csvWriter.write(PushOrderFileFormat.SiteId);
		csvWriter.write(PushOrderFileFormat.Marketplace_Territory);
		csvWriter.write(PushOrderFileFormat.Shipping_Title);
		csvWriter.write(PushOrderFileFormat.Shipping_First_Name);
		csvWriter.write(PushOrderFileFormat.Shipping_Last_Name);
		csvWriter.write(PushOrderFileFormat.ShippingAddress1);
		csvWriter.write(PushOrderFileFormat.ShippingAddress2);
		csvWriter.write(PushOrderFileFormat.ShippingAddress3);
		csvWriter.write(PushOrderFileFormat.ShippingTown);
		csvWriter.write(PushOrderFileFormat.ShippingCountry);
		csvWriter.write(PushOrderFileFormat.ShippingCountryCode);
		csvWriter.write(PushOrderFileFormat.ShippingPostCode);
		csvWriter.write(PushOrderFileFormat.PostalService);
		csvWriter.write(PushOrderFileFormat.VariationId);
		csvWriter.write(PushOrderFileFormat.Title);
		csvWriter.write(PushOrderFileFormat.Quantity);
		csvWriter.write(PushOrderFileFormat.UnitCost);
		csvWriter.write(PushOrderFileFormat.PostageCost);
		csvWriter.write(PushOrderFileFormat.OrderTotal);
		csvWriter.write(PushOrderFileFormat.Currency);
		csvWriter.write(PushOrderFileFormat.ZebraId);
		csvWriter.write(PushOrderFileFormat.Item_Discount);
		csvWriter.write(PushOrderFileFormat.Order_Discount);
		csvWriter.endRecord();
	}

	private void createUploadFileBody(CsvWriter csvWriter, List<OrderExtend> pushOrderList, String fileKind) throws IOException {
		for (int i = 0 ; i < pushOrderList.size(); i++) {
			OrderExtend orderInfo = pushOrderList.get(i);

			// LinnworksOrderId
			csvWriter.write(orderInfo.getSourceOrderId());
			// ReferenceNum
			csvWriter.write(orderInfo.getOrderNumber());
			// AlipayTransactionId
			csvWriter.write(orderInfo.getPayNo());
			// BuyerTitle
			csvWriter.write("");

			// BuyerFirstName
			csvWriter.write(orderInfo.getName());
			// BuyerLastName
			csvWriter.write("");
			// Email_Address
			csvWriter.write(orderInfo.getEmail());
			// BuyerPhone
			csvWriter.write(orderInfo.getPhone());
			// Company
			csvWriter.write(orderInfo.getCompany());

			// 地址取得
			String address = orderInfo.getAddress();
			String address2 = "";

			if (StringUtils.isEmpty(address)) {
				address = orderInfo.getAddress2();
			} else {
				address2 = orderInfo.getAddress2();
			}

			ArrayList<String> rmAddressArr = getRMAddress(address, address2);
			// Address1
			csvWriter.write(rmAddressArr.get(0));
			// Address2
			csvWriter.write(rmAddressArr.get(1));
			// Address3
			csvWriter.write(rmAddressArr.get(2));
			// Town
			csvWriter.write(orderInfo.getCity());
			// Country
			csvWriter.write(OmsConstants.ExtOrderItem.COUNTRY);
			// Country_Code
			csvWriter.write(OmsConstants.ExtOrderItem.COUNTRY_CODE);
			// PostCode
			csvWriter.write(orderInfo.getZip());
			// ReceivedDate
			csvWriter.write(DateTimeUtil.changeDefautlDateTimeToENFormat(orderInfo.getOrderDateTime()));
			// Status
			//		正常订单的场合
			if (uploadFileKindNormal.equals(fileKind)) {
				csvWriter.write(OmsConstants.ExtOrderStatus.PAID);
			//		取消订单的场合
			} else {
				csvWriter.write(OmsConstants.ExtOrderStatus.CANCELLING);
			}

			// Source
			csvWriter.write(OmsConstants.ExtOrderItem.SOURCE);
			// Subsource
			csvWriter.write(OmsConstants.ExtOrderItem.SUBSOURCE);
			// SiteName
			csvWriter.write(OmsConstants.ExtOrderItem.SITENAME);
			// SiteId
			csvWriter.write(OmsConstants.ExtOrderItem.SITEID);
			// Marketplace_Territory
			csvWriter.write(OmsConstants.ExtOrderItem.MARKETPLACE);
			// Shipping_Title
			csvWriter.write("");
			// Shipping_First_Name
			csvWriter.write(orderInfo.getShipName());
			// Shipping_Last_Name
			csvWriter.write("");

			// 地址取得
			String shipAddress = orderInfo.getShipAddress();
			String shipAddress2 = "";

			if (StringUtils.isEmpty(shipAddress)) {
				shipAddress = orderInfo.getShipAddress2();
			} else {
				shipAddress2 = orderInfo.getShipAddress2();
			}
			ArrayList<String> rmShipAddressArr = getRMAddress(shipAddress, shipAddress2);
			// ShippingAddress1
			csvWriter.write(rmShipAddressArr.get(0));
			// ShippingAddress2
			csvWriter.write(rmShipAddressArr.get(1));
			// ShippingAddress3
			csvWriter.write(rmShipAddressArr.get(2));
			// ShippingTown
			csvWriter.write(orderInfo.getShipCity());
			// ShippingCountry
			csvWriter.write(OmsConstants.ExtOrderItem.COUNTRY);
			// ShippingCountryCode
			csvWriter.write(OmsConstants.ExtOrderItem.COUNTRY_CODE);
			// ShippingPostCode
			csvWriter.write(orderInfo.getShipZip());
			// PostalService
			csvWriter.write(OmsConstants.ExtOrderItem.POSTAL_SERVICE);
			// VariationId
			csvWriter.write(orderInfo.getClientSku());
			// Title
			csvWriter.write(orderInfo.getProduct());
			// Quantity
			csvWriter.write(orderInfo.getQuantityOrdered());
			// UnitCost
//			csvWriter.write(orderInfo.getPricePerUnit());
			csvWriter.write(orderInfo.getPrice());
			// PostageCost
			csvWriter.write(orderInfo.getFinalShippingTotal());
			// OrderTotal
			csvWriter.write(orderInfo.getFinalGrandTotal());
			// Currency
			csvWriter.write(OmsConstants.ExtOrderItem.CURRENCY);
			// ZebraId
			csvWriter.write(orderInfo.getTaobaoLogisticsId());
			//  Item Discount
			String itemDicount = "0";
			if (!StringUtils.isEmpty(orderInfo.getItemDiscount())) {
				itemDicount = String.valueOf(mult2Digits(Float.valueOf(orderInfo.getItemDiscount()), -1));
			}
			csvWriter.write(itemDicount);
			// Order Discount
			String orderDiscount = "0";
			if (!StringUtils.isEmpty(orderInfo.getItemDiscount())) {
				orderDiscount = String.valueOf(mult2Digits(Float.valueOf(orderInfo.getOrderDiscount()), -1));
			}
			csvWriter.write(orderDiscount);

			csvWriter.endRecord();
		}
	}

	/**
	 * 皇马地址转化
	 *
	 * @param address1 address2
	 * @return ArrayList[0] 地址1
	 * 			ArrayList[1] 地址2
	 * 			ArrayList[3] 地址3
	 */
	private static ArrayList<String> getRMAddress(String address1, String address2) {
		ArrayList<String> retAddress = new ArrayList<String>();

		String rmAddress1 = "";
		String rmAddress2 = "";
		String rmAddress3 = "";

		if (address1.length() > 15) {
			rmAddress1 = address1.substring(0,15);
			if (address1.length() > 30) {
				rmAddress2 = address1.substring(15,30);
			} else {
				rmAddress2 = address1.substring(15);
			}

			if (address2.length() > 15) {
				rmAddress3 = address2.substring(0,15);
			} else {
				rmAddress3 = address2;
			}
		} else {
			rmAddress1 = address1;
			if (address2.length() > 15) {
				rmAddress2 = address2.substring(0,15);

				if (address2.length() > 30) {
					rmAddress3 = address2.substring(15,30);
				} else {
					rmAddress3 = address2.substring(15);
				}
			} else {
				rmAddress2 = address2;
			}
		}

		retAddress.add(rmAddress1);
		retAddress.add(rmAddress2);
		retAddress.add(rmAddress3);

		return retAddress;
	}

	/**
	 * 上传文件做成（待取消订单）
	 *
	 * @param fileName 待上传文件名
	 */
	private boolean createUploadFileForPendingCancelOrder(FtpBean ftpBean, String fileName, List<OrderExtend> pushOrderList, String fileKind) {
		boolean isSuccess = true;

		// 本地文件生成路径
		String uploadLocalPath = ftpBean.getUpload_localpath();

		try {
//			CsvWriter csvWriter = new CsvWriter(new FileWriter(uploadLocalPath + "/" + fileName), ',', Charset.forName("GB2312"));

			File file = new File(uploadLocalPath + "/" + fileName);
			FileOutputStream  fop = new FileOutputStream(file);
			CsvWriter csvWriter = new CsvWriter(fop, ',', Charset.forName(outputFileEncoding));

			// Head输出
			createUploadFileHeadForPendingCancel(csvWriter);

			// Body输出
			createUploadFileBodyForPendingCancel(csvWriter, pushOrderList, fileKind);

			csvWriter.flush();
			csvWriter.close();

		} catch (Exception e) {
			isSuccess = false;

			logger.error("createUploadFileForPendingCancelOrder", e);

			issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);
		}

		return isSuccess;
	}

	/**
	 * 上传文件做成（待取消订单）
	 *
	 * @param fileName 待上传文件名
	 */
	private boolean createUploadFileForReturnOrder(FtpBean ftpBean, String fileName, List<OrderExtend> pushOrderList, String fileKind) {
		boolean isSuccess = true;

		// 本地文件生成路径
		String uploadLocalPath = ftpBean.getUpload_localpath();

		try {
//			CsvWriter csvWriter = new CsvWriter(new FileWriter(uploadLocalPath + "/" + fileName), ',', Charset.forName("GB2312"));

			File file = new File(uploadLocalPath + "/" + fileName);
			FileOutputStream  fop = new FileOutputStream(file);
			CsvWriter csvWriter = new CsvWriter(fop, ',', Charset.forName(outputFileEncoding));

			// Head输出
			createUploadFileHeadForReturn(csvWriter);

			// Body输出
			createUploadFileBodyForReturn(csvWriter, pushOrderList, fileKind);

			csvWriter.flush();
			csvWriter.close();

		} catch (Exception e) {
			isSuccess = false;

			logger.error("createUploadFileForReturnOrder", e);

			issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);
		}

		return isSuccess;
	}

	/**
	 * 上传文件做成
	 *
	 * @param csvWriter 上传文件Handler
	 */
	private void createUploadFileHeadForReturn(CsvWriter csvWriter) throws IOException {
		csvWriter.write(bom + ReturnOrderFileFormat.MarketplaceOrderId);
		csvWriter.write(ReturnOrderFileFormat.Quantity);
		csvWriter.write(ReturnOrderFileFormat.VariationId);
		csvWriter.write(ReturnOrderFileFormat.ReturnedReason);

		csvWriter.endRecord();
	}

	private void createUploadFileBodyForReturn(CsvWriter csvWriter, List<OrderExtend> pushOrderList, String fileKind) throws IOException {
		for (int i = 0 ; i < pushOrderList.size(); i++) {
			OrderExtend orderInfo = pushOrderList.get(i);

			// MarketplaceOrderId
			csvWriter.write(orderInfo.getSourceOrderId());
			// Quantity
			csvWriter.write(orderInfo.getQuantityOrdered());
			// VariationId
			csvWriter.write(orderInfo.getClientSku());
			// ReturnedReason
			csvWriter.write("Purchased In Error");

			csvWriter.endRecord();
		}
	}

	/**
	 * 上传文件做成
	 *
	 * @param csvWriter 上传文件Handler
	 */
	private void createUploadFileHeadForPendingCancel(CsvWriter csvWriter) throws IOException {
		csvWriter.write(bom + RefundsFileFormat.LinnworksOrderId);
		csvWriter.write(RefundsFileFormat.VariationId);
		csvWriter.write(RefundsFileFormat.Processed);

		csvWriter.endRecord();
	}

	private void createUploadFileBodyForPendingCancel(CsvWriter csvWriter, List<OrderExtend> pushOrderList, String fileKind) throws IOException {
		for (int i = 0 ; i < pushOrderList.size(); i++) {
			OrderExtend orderInfo = pushOrderList.get(i);

			// LinnworksOrderId
			csvWriter.write(orderInfo.getSourceOrderId());
			// ZebraId
			csvWriter.write(orderInfo.getClientSku());
			// ReferenceNum
			csvWriter.write("TRUE");

			csvWriter.endRecord();
		}
	}

	/**
	 * 订单文件上传
	 *
	 * @param fileName 待上传文件名
	 */
	private boolean uploadOrderFile(FtpBean ftpBean, String fileName) {
		boolean isSuccess = true;

		// 本地文件名称
		ftpBean.setUpload_filename(fileName);

		ChannelSftp ftpClient = new ChannelSftp();
		SFtpUtil ftpUtil = new SFtpUtil();
		try {
			//建立连接
			ftpClient = ftpUtil.linkFtp(ftpBean);

			//文件流取得
			ftpBean.setUpload_input(new FileInputStream(ftpBean.getUpload_localpath() + "/" + ftpBean.getUpload_filename()));

			isSuccess = ftpUtil.uploadFile(ftpBean, ftpClient);

		} catch (Exception e) {

			isSuccess = false;

			logger.error("uploadOrderFile", e);

			issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);

		} finally {
			try {
				//断开连接
				ftpUtil.disconnectFtp(ftpClient);

			} catch (Exception e) {
				isSuccess = false;

				logger.error("uploadOrderFile", e);

				issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);
			}
		}

		return isSuccess;
	}

	/**
	 * Cancel订单文件下载
	 *
	 */
	private boolean downLoadCancelOrderFile(FtpBean ftpBean, FTPClient ftpClient, FtpUtil ftpUtil, List<String> downloadFileList) throws IOException {
		boolean isSuccess = true;

		//建立连接
		ftpClient = ftpUtil.linkFtp(ftpBean);

		//文件下载
		int result = ftpUtil.downFiles(ftpBean, ftpClient, downloadFileList);

		if (result != download_ret_success && result != download_ret_fileNotExist) {
			issueLog.log(DOWNLOAD_RM_CANCEL_ORDER, "DownLoad RM File Error!", ErrorType.BatchJob, SubSystem.OMS);
		}

		return isSuccess;
	}

	/**
	 * 被下载文件删除
	 */
	private boolean delCloudFile(FtpBean ftpBean, FTPClient ftpClient, FtpUtil ftpUtil, List<String> downloadFileList) throws IOException {
		boolean isSuccess = true;

//		File[] fileList = getFileList(ftpBean.getDown_localpath());

		for (int i = 0; i < downloadFileList.size(); i++) {
//			String fileName = fileList[i].getName();
			String fileName = downloadFileList.get(i);
			ftpUtil.delOneFile(ftpBean, ftpClient, fileName);

		}

		return isSuccess;
	}

	/**
	 * 被下载文件移动
	 */
	private boolean removeCloudFile(FtpBean ftpBean, FTPClient ftpClient, FtpUtil ftpUtil, List<String> downloadFileList) throws IOException {
		boolean isSuccess = true;

//		File[] fileList = getFileList(ftpBean.getDown_localpath());

		for (int i = 0; i < downloadFileList.size(); i++) {
//			String fileName = fileList[i].getName();
			String fileName = downloadFileList.get(i);
			ftpUtil.removeOneFile(ftpBean, ftpClient, fileName);
		}

		return isSuccess;
	}

	/**
	 * 客服邮件发送
	 */
//	private boolean sendCustomerServiceMail(FtpBean ftpBean, List<String> downloadFileList) {
//		boolean isSuccess = true;
//
////		File[] fileList = getFileList(ftpBean.getDown_localpath());
//
//		try {
//
//			ArrayList<String> cancelledOrderNumberList = new ArrayList<String>();
//
////			for (int i = 0; i < downloadFileList.length; i++) {
////				CsvReader reader = new CsvReader(new FileInputStream(ftpBean.getDown_localpath() + "/" + fileList[i].getName()), ',', Charset.forName("GBK"));
//			for (int i = 0; i < downloadFileList.size(); i++) {
//				CsvReader reader = new CsvReader(new FileInputStream(ftpBean.getDown_localpath() + "/" + downloadFileList.get(i)), ',', Charset.forName("GBK"));
//
//				// Head读入
//				reader.readHeaders();
//				String[] headers = reader.getHeaders();
//
//				// Body读入
//				while (reader.readRecord()) {
//					String sourceOrderId = reader.get(CancelledOrderFileFormat.MarketplaceOrderId);
//
//					if (!cancelledOrderNumberList.contains(sourceOrderId)) {
//						cancelledOrderNumberList.add(sourceOrderId);
//					}
//				}
//				reader.close();
//			}
//
//			if (cancelledOrderNumberList.size() > 0) {
//				Mail.sendAlert("ITOMS", OmsConstants.RM_OUT_OF_STOCK_CHECK_SUBJECT, getArrayListString(cancelledOrderNumberList), true);
//			}
//		} catch (Exception e) {
//			isSuccess = false;
//
//			logger.error("sendCustomerServiceMail", e);
//
//			issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);
//		}
//
//		return isSuccess;
//	}
	private boolean sendCustomerServiceMail(List<String> sourceOrderIdList) {
		boolean ret = true;

		try {
			if (sourceOrderIdList.size() > 0) {

				StringBuilder tbody = new StringBuilder();

				for (int i = 0; i < sourceOrderIdList.size(); i++) {
					// 邮件每行正文
					String mailTextLine =
							String.format(OmsConstants.PATERN_TABLE_ROW, sourceOrderIdList.get(i));
					tbody.append(mailTextLine);
				}

				// 拼接table
				String body = String.format(OmsConstants.PATERN_TABLE, OmsConstants.RM_OUT_OF_STOCK_CHECK_SUBJECT, tbody.toString());

				// 拼接邮件正文
				StringBuilder emailContent = new StringBuilder();
				emailContent.append(Constants.EMAIL_STYLE_STRING).append(body);

//				Mail.sendAlert("ITOMS", OmsConstants.RM_OUT_OF_STOCK_CHECK_SUBJECT, getArrayListString(sourceOrderIdList), true);
				Mail.sendAlert("CUSTOMER_SERVICE_REALMADRID", OmsConstants.RM_OUT_OF_STOCK_CHECK_SUBJECT, emailContent.toString(), true);
			}
		} catch (Exception e) {
			ret = false;

			logger.error("sendCustomerServiceMail", e);

			issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);
		}

		return ret;
	}

	private boolean sendITMail(List<String> sourceOrderIdList) {
		boolean ret = true;

		try {
			if (sourceOrderIdList.size() > 0) {
				StringBuilder tbody = new StringBuilder();

				for (int i = 0; i < sourceOrderIdList.size(); i++) {
					// 邮件每行正文
					String mailTextLine =
							String.format(OmsConstants.PATERN_TABLE_ROW, sourceOrderIdList.get(i));
					tbody.append(mailTextLine);
				}

				// 拼接table
				String body = String.format(OmsConstants.PATERN_TABLE, OmsConstants.RM_ERROR_CHECK_SUBJECT, tbody.toString());

				// 拼接邮件正文
				StringBuilder emailContent = new StringBuilder();
				emailContent.append(Constants.EMAIL_STYLE_STRING).append(body);

//				Mail.sendAlert("ITOMS", OmsConstants.RM_ERROR_CHECK_SUBJECT, getArrayListString(sourceOrderIdList), true);
				Mail.sendAlert("ITOMS", OmsConstants.RM_ERROR_CHECK_SUBJECT, emailContent.toString(), true);
			}
		} catch (Exception e) {
			ret = false;

			logger.error("sendCustomerServiceMail", e);

			issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);
		}

		return ret;
	}

	private String getArrayListString(List<String> orderList) {
		StringBuffer ret = new StringBuffer();

		for (int i = 0; i < orderList.size(); i++) {
			if (StringUtils.isEmpty(ret.toString())) {
				ret.append(orderList.get(i));
			} else {
				ret.append(",");
				ret.append(orderList.get(i));
			}
		}
		return ret.toString();
	}

	private String getArrayListStringForInFormFile(List<InFormFile> orderList) {
		StringBuffer ret = new StringBuffer();

		for (int i = 0; i < orderList.size(); i++) {
			if (StringUtils.isEmpty(ret.toString())) {
				ret.append(orderList.get(i).getSourceOrderId());
			} else {
				ret.append(",");
				ret.append(orderList.get(i).getSourceOrderId());
			}
		}
		return ret.toString();
	}

	/**
	 * 配置信息
	 *
	 * @param orderChannelID    String
	 */
	private FtpBean formatFtpBean(String orderChannelID){

		String url = "";
		// ftp连接port
		String port = "";
		// ftp连接usernmae
		String username = "";
		// ftp连接password
		String password = "";

		// ftp连接地址
		url = ThirdPartyConfigs.getVal1(orderChannelID, ftp_address);
		// ftp连接port
		port = ThirdPartyConfigs.getVal1(orderChannelID, ftp_port);
		// ftp连接usernmae
		username = ThirdPartyConfigs.getVal1(orderChannelID, ftp_usernmae);
		// ftp连接password
		password = ThirdPartyConfigs.getVal1(orderChannelID, ftp_password);

		FtpBean ftpBean = new FtpBean();
		ftpBean.setPort(port);
		ftpBean.setUrl(url);
		ftpBean.setUsername(username);
		ftpBean.setPassword(password);
		ftpBean.setFile_coding("iso-8859-1");
		return ftpBean;
	}

	/**
	 * 下载Bean生成
	 *
	 */
	private FtpBean formatCancelOrderDownloadFtpBean() {
		FtpBean ftpBean = formatFtpBean(orderChannelID);

		// 配置信息读取
		List<ThirdPartyConfigBean> ftpFilePaths = ThirdPartyConfigs.getThirdPartyConfigList(orderChannelID, oms_download_cancel_order_file_path);

//		// 下载文件路径
//		ftpBean.setDown_remotepath(ftpFilePaths.get(0).getProp_val1());
//		// 本地文件路径
//		ftpBean.setDown_localpath(ftpFilePaths.get(0).getProp_val2());
//		// 本地文件备份路径
//		ftpBean.setDown_local_bak_path(ftpFilePaths.get(0).getProp_val3());
//		// 下载文件备份路径（远程）
//		ftpBean.setRemote_bak_path(ftpFilePaths.get(0).getProp_val4());

		// 本地文件路径
		ftpBean.setDown_localpath(ftpFilePaths.get(0).getProp_val1());
		// 本地文件备份路径
		ftpBean.setDown_local_bak_path(ftpFilePaths.get(0).getProp_val2());
		// 下载文件名
		ftpBean.setDown_filename(ftpFilePaths.get(0).getProp_val3());

		return ftpBean;
	}

	/**
	 * 下载Bean生成
	 *
	 */
	private FtpBean formatUpdateOrderDownloadFtpBean() {
		FtpBean ftpBean = formatFtpBean(orderChannelID);

		// 配置信息读取
		List<ThirdPartyConfigBean> ftpFilePaths = ThirdPartyConfigs.getThirdPartyConfigList(orderChannelID, oms_download_update_order_file_path);

//		// 下载文件路径
//		ftpBean.setDown_remotepath(ftpFilePaths.get(0).getProp_val1());
//		// 本地文件路径
//		ftpBean.setDown_localpath(ftpFilePaths.get(0).getProp_val2());
//		// 本地文件备份路径
//		ftpBean.setDown_local_bak_path(ftpFilePaths.get(0).getProp_val3());
//		// 下载文件备份路径（远程）
//		ftpBean.setRemote_bak_path(ftpFilePaths.get(0).getProp_val4());

		// 本地文件路径
		ftpBean.setDown_localpath(ftpFilePaths.get(0).getProp_val1());
		// 本地文件备份路径
		ftpBean.setDown_local_bak_path(ftpFilePaths.get(0).getProp_val2());
		// 下载文件名
		ftpBean.setDown_filename(ftpFilePaths.get(0).getProp_val3());
		// 生成Shipping文件名
		ftpBean.setUpload_localpath(ftpFilePaths.get(0).getProp_val4());

		return ftpBean;
	}

	/**
	 * 上传Bean生成
	 *
	 */
	private FtpBean formatSalesOrdersUploadFtpBean() {
		FtpBean ftpBean = formatFtpBean(orderChannelID);

		// 配置信息读取
		List<ThirdPartyConfigBean> ftpFilePaths = ThirdPartyConfigs.getThirdPartyConfigList(orderChannelID, oms_upload_sales_orders_file_path);

		// 上传文件路径
		ftpBean.setUpload_path(ftpFilePaths.get(0).getProp_val1());
		// 本地文件路径
		ftpBean.setUpload_localpath(ftpFilePaths.get(0).getProp_val2());
		// 本地文件备份路径
		ftpBean.setUpload_local_bak_path(ftpFilePaths.get(0).getProp_val3());

		return ftpBean;
	}

	/**
	 * 上传Bean生成
	 *
	 */
	private FtpBean formatCancelOrdersUploadFtpBean() {
		FtpBean ftpBean = formatFtpBean(orderChannelID);

		// 配置信息读取
		List<ThirdPartyConfigBean> ftpFilePaths = ThirdPartyConfigs.getThirdPartyConfigList(orderChannelID, oms_upload_cancel_order_file_path);

		// 上传文件路径
		ftpBean.setUpload_path(ftpFilePaths.get(0).getProp_val1());
		// 本地文件路径
		ftpBean.setUpload_localpath(ftpFilePaths.get(0).getProp_val2());
		// 本地文件备份路径
		ftpBean.setUpload_local_bak_path(ftpFilePaths.get(0).getProp_val3());

		return ftpBean;
	}

	/**
	 * 上传Bean生成
	 *
	 */
	private FtpBean formatReturnOrdersUploadFtpBean() {
		FtpBean ftpBean = formatFtpBean(orderChannelID);

		// 配置信息读取
		List<ThirdPartyConfigBean> ftpFilePaths = ThirdPartyConfigs.getThirdPartyConfigList(orderChannelID, oms_upload_return_order_file_path);

		// 上传文件路径
		ftpBean.setUpload_path(ftpFilePaths.get(0).getProp_val1());
		// 本地文件路径
		ftpBean.setUpload_localpath(ftpFilePaths.get(0).getProp_val2());
		// 本地文件备份路径
		ftpBean.setUpload_local_bak_path(ftpFilePaths.get(0).getProp_val3());

		return ftpBean;
	}


	/**
	 * orders_yyyyMMdd_HHmmss.csv 文件格式（upload： /SalesOrders）
	 */
	private class PushOrderFileFormat {
		private static final String LinnworksOrderId = "LinnworksOrderId";
		private static final String ReferenceNum = "ReferenceNum";
		private static final String AlipayTransactionId = "AlipayTransactionId";
		private static final String BuyerTitle = "BuyerTitle";
		private static final String BuyerFirstName = "BuyerFirstName";
		private static final String BuyerLastName = "BuyerLastName";
		private static final String Email_Address = "Email Address";
		private static final String BuyerPhone = "BuyerPhone";
		private static final String Company = "Company";
		private static final String Address1 = "Address1";
		private static final String Address2 = "Address2";
		private static final String Address3 = "Address3";
		private static final String Town = "Town";
		private static final String Country = "Country";
		private static final String Country_Code = "Country Code";
		private static final String PostCode = "PostCode";
		private static final String ReceivedDate = "ReceivedDate";
		private static final String Status = "Status";
		private static final String Source = "Source";
		private static final String Subsource = "Subsource";
		private static final String SiteName = "SiteName";
		private static final String SiteId = "SiteId";
		private static final String Marketplace_Territory = "Marketplace Territory";
		private static final String Shipping_Title = "Shipping Title";
		private static final String Shipping_First_Name = "Shipping First Name";
		private static final String Shipping_Last_Name = "Shipping Last Name";
		private static final String ShippingAddress1 = "ShippingAddress1";
		private static final String ShippingAddress2 = "ShippingAddress2";
		private static final String ShippingAddress3 = "ShippingAddress3";
		private static final String ShippingTown = "ShippingTown";
		private static final String ShippingCountry = "ShippingCountry";
		private static final String ShippingCountryCode = "ShippingCountryCode";
		private static final String ShippingPostCode = "ShippingPostCode";
		private static final String PostalService = "PostalService";
		private static final String VariationId = "VariationId";
		private static final String Title = "Title";
		private static final String Quantity = "Quantity";
		private static final String UnitCost = "UnitCost";
		private static final String PostageCost = "PostageCost";
		private static final String OrderTotal = "OrderTotal";
		private static final String Currency = "Currency";
		private static final String ZebraId = "ZebraId";
		private static final String Item_Discount = "Item Discount";
		private static final String Order_Discount = "Order Discount";
	}

	/**
	 * refunds_yyyyMMdd_HHmmss.csv 文件格式（upload： /Refunds）
	 */
	private class RefundsFileFormat {
		private static final String LinnworksOrderId = "LinnworksOrderId";
		private static final String VariationId = "VariationId";
		private static final String Processed = "Processed";
	}

	/**
	 * all_lines_out_of_stock_cancellations_yyyyMMdd_HHmmss.csv 文件格式（downupload： /Cancel）
	 */
	private class CancelledOrderFileFormat {
		private static final String MarketplaceOrderId = "MarketplaceOrderId";
		private static final String VariationId = "VariationId";
		private static final String FailureReason = "FailureReason";
	}

	/**
	 * order_updates_20150713_152100.csv 文件格式（downupload： /Updates）
	 */
	private class UpdateOrderFileFormat {
		private static final String MarketplaceOrderId = "MarketplaceOrderId";
		private static final String StatusReason = "StatusReason";
	}

	/**
	 * returned_orders_20150713_160800.csv 文件格式（upload： /Returns）
	 */
	private class ReturnOrderFileFormat {
		private static final String MarketplaceOrderId = "MarketplaceOrderId";
		private static final String Quantity = "Quantity";
		private static final String VariationId = "VariationId";
		private static final String ReturnedReason = "ReturnedReason";
	}

	/**
	 * orderupdate_20150707_094054.csv 文件格式（download： /Shipping）
	 */
	private class ShippingOrderFileFormat {
		private static final String MarketplaceOrderId = "MarketplaceOrderId";
		private static final String ShippedDate = "ShippedDate";
		private static final String CourierName = "CourierName";
		private static final String TrackingId = "TrackingId";
	}

	/**
	 * 加法 保留两位小数
	 *
	 * @param addPara1
	 * @param addPara2
	 * @return ret
	 */
	private float add2Digits(float addPara1, float addPara2) {
		float ret = 0f;

		BigDecimal bd1 = new BigDecimal(addPara1);
		BigDecimal bd2 = new BigDecimal(addPara2);

		ret = bd1.add(bd2).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

		return ret;
	}

	/**
	 * 减法 保留两位小数
	 *
	 * @param addPara1
	 * @param addPara2
	 * @return ret
	 */
	private float sub2Digits(float addPara1, float addPara2) {
		float ret = 0f;

		BigDecimal bd1 = new BigDecimal(addPara1);
		BigDecimal bd2 = new BigDecimal(addPara2);

		ret = bd1.subtract(bd2).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

		return ret;
	}

	/**
	 * 乘法 保留两位小数
	 *
	 * @param addPara1
	 * @param addPara2
	 * @return ret
	 */
	private float mult2Digits(float addPara1, float addPara2) {
		float ret = 0f;

		BigDecimal bd1 = new BigDecimal(addPara1);
		BigDecimal bd2 = new BigDecimal(addPara2);

		ret = bd1.multiply(bd2).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

		return ret;
	}

	private boolean addBom(FtpBean ftpBean, String fileName) {
		boolean isSuccess = true;

		// bom 头部
		byte[] bs={(byte)0xef,(byte)0xbb,(byte)0xbf};
		try {
			RandomAccessFile raf = new RandomAccessFile(ftpBean.getUpload_localpath() + "/" + fileName, "rw");
			// 将写文件指针移到文件头
			raf.seek(0);
			raf.write(bs);
			raf.close();

		} catch (Exception e) {
			isSuccess = false;

			issueLog.log("addBom",
							"addBom file = " + fileName,
							ErrorType.BatchJob,
							SubSystem.OMS);
		}

		return isSuccess;
	}

	/**
	 * 下载文件再做成（Shipping）
	 *
	 * @param fileName 待上传文件名
	 */
	private boolean createDownloadFileForShipping(FtpBean ftpBean, String fileName, List<InFormFile> shippingOrderList) {
		boolean isSuccess = true;

		// 本地文件生成路径（Shipping 目录夹删除对应 20150724 根据Upload文件生成shipping文件）
		String uploadLocalPath = ftpBean.getUpload_localpath();

		try {

			List<OrderExtend> pushOrderList = orderDao.getOrdersInfoForShipping(shippingOrderList);

			File file = new File(uploadLocalPath + "/" + fileName);
			FileOutputStream  fop = new FileOutputStream(file);
			CsvWriter csvWriter = new CsvWriter(fop, ',', Charset.forName(outputFileEncoding));

			// Head输出
			createShippingFileHead(csvWriter);

			// Body输出
			createShippingFileBody(csvWriter, pushOrderList, shippingOrderList);

			csvWriter.flush();
			csvWriter.close();

		} catch (Exception e) {
			isSuccess = false;

			logger.error("createDownloadFileForShipping", e);

			issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);
		}

		return isSuccess;
	}

	/**
	 * Shipping文件Head做成
	 *
	 * @param csvWriter Shippping文件Handler
	 */
	private void createShippingFileHead(CsvWriter csvWriter) throws IOException {
		csvWriter.write(ShippingOrderFileFormat.MarketplaceOrderId);
		csvWriter.write(ShippingOrderFileFormat.ShippedDate);
		csvWriter.write(ShippingOrderFileFormat.CourierName);
		csvWriter.write(ShippingOrderFileFormat.TrackingId);

		csvWriter.endRecord();
	}

	/**
	 * Shipping文件Body做成
	 *
	 * @param csvWriter Shippping文件Handler
	 */
	private void createShippingFileBody(CsvWriter csvWriter, List<OrderExtend> pushOrderList, List<InFormFile> shippingOrderList) throws IOException {
		for (int i = 0 ; i < pushOrderList.size(); i++) {
			OrderExtend orderInfo = pushOrderList.get(i);

			// LinnworksOrderId
			csvWriter.write(orderInfo.getSourceOrderId());
			// ShippedDate
			InFormFile inFormFile = getSelectedInFormFile(shippingOrderList, orderInfo.getSourceOrderId());
			csvWriter.write(getDateTimeByFileName(inFormFile.getFileName()));
			// CourierName
			csvWriter.write("RM");
			// TrackingId
			csvWriter.write(orderInfo.getTaobaoLogisticsId());

			csvWriter.endRecord();
		}
	}

	private InFormFile getSelectedInFormFile(List<InFormFile> shippingOrderList, String sourceOrderId) {
		InFormFile ret = null;

		for (int i = 0; i < shippingOrderList.size(); i++) {
			if (sourceOrderId.equals(shippingOrderList.get(i).getSourceOrderId())) {
				ret = shippingOrderList.get(i);

				break;
			}
		}

		return  ret;
	}

	/**
	 * 根据文件名取得时间（order_updates_20150713_152000.csv）
	 *
	 * @param fileName 文件名
	 */
	private String getDateTimeByFileName(String fileName) {

		fileName = fileName.substring(0, fileName.length() - 4);
		String[] filaNameArr = fileName.split("_");
		String date = filaNameArr[2];
		String time = filaNameArr[3];

		Date a = DateTimeUtil.parse(date + time, DateTimeUtil.DATE_TIME_FORMAT_2);
		String  ret = DateTimeUtil.format(a, DateTimeUtil.EN_DATE_FORMAT);

		return ret;
	}

	public static void main(String[] args) throws Exception {
//
//		CsvWriter csvWriter = new CsvWriter(new FileWriter("d://test4.text"), ',');
//
//		csvWriter.write( "name11");
//
//		csvWriter.write("company,1");
//
//		csvWriter.endRecord();
//
//		csvWriter.write("11");
//
//		csvWriter.write("12");
//
//		csvWriter.endRecord();
//
//		csvWriter.write("21");
//
//		csvWriter.write("22");
//
//		csvWriter.endRecord();
//
//		csvWriter.flush();
//
//		csvWriter.close();

//		String date = DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_3);
//		String time = DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_4);
//
//		String a = String.format("orders_%s_%s.csv", date, time);
//		System.out.println(a);

//		uploadOrderFile("test.text");

//		FtpUtil ftpUtil = new FtpUtil();
//		FTPClient ftpClient = new FTPClient();
//
//		FtpBean ftpBean = new FtpBean();
//		ftpBean.setPort("");
//		ftpBean.setUrl("121.41.58.229");
//		ftpBean.setUsername("app_tomcat");
//		ftpBean.setPassword("tomcat_voyageone");
//		ftpBean.setDown_remotepath("/usr/web/contents/oms/temp/image");
//		ftpBean.setDown_localpath("D:\\RMPostTest\\download");
//		ftpBean.setFile_coding("iso-8859-1");
//
//		try {
//			//建立连接
//			ftpClient = ftpUtil.linkFtp(ftpBean);
//			int result = ftpUtil.downFile(ftpBean, ftpClient);
//
//			System.out.println("result = " + result);
////			ftpUtil.delFileForSpecifiedFile(ftpBean, ftpClient, "D__RMPostTest_upload_test.text");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

//		File dir = new File("D:\\jiming\\work\\new SE\\test\\oms");
//		File[] ret = dir.listFiles();
//
//		System.out.println("fileName = " + ret[2].getName());
//		System.out.println("ret.length = " + ret.length);

		// 文件读入=============
//		CsvReader reader = new CsvReader(new FileInputStream("D:\\jiming\\联络\\收到\\20150703 RM\\bak\\all_lines_out_of_stock_cancellations_20150611_041641.csv"), ',', Charset.forName("GBK"));
//		reader.readHeaders();
//		String[] headers = reader.getHeaders();
//
//		while (reader.readRecord()) {
//			System.out.println(reader.get("VariationId"));
//		}
//
//		reader.close();


//		Date a = DateTimeUtil.parse("2014-08-30 07:09:00", DateTimeUtil.DEFAULT_DATETIME_FORMAT);
//		String  out = DateTimeUtil.format(a, DateTimeUtil.US_DATE_FORMAT);
//		System.out.println("out = " + out);

//		String ret = "";
//		String fileName = "all_lines_out_of_stock_cancellations_*.csv";
//		if (fileName.length() > 5) {
//			ret = fileName.substring(0, fileName.length() - 5);
//		}
//
//		System.out.println("out = " + ret);

//		int sIndex = 0;
//		String preFileName = "all_lines_out_of_stock_cancellations_*.csv";
//		sIndex = preFileName.indexOf("*");
//		String fileNameLike = preFileName.substring(0, sIndex);
//		System.out.println("out = " + fileNameLike);

//		FileUtils.moveFile("D:\\RMPostTest\\upload\\returned_orders_20150716_193618.csv", "D:/RMPostTest/uploadbackup/returned_orders_20150716_193618.csv");
//		FileUtils.delFile("D:\\RMPostTest\\upload/returned_orders_20150716_195923.csv");
//		FileUtils.delFile("D:\\RMPostTest\\upload/returned_orders_20150716_195923.csv");

//		byte[] bs={(byte)0xef,(byte)0xbb,(byte)0xbf};
////		String bomString=new String( bs ,"UTF-8");
//
//		RandomAccessFile raf = new RandomAccessFile("D:\\RMPostTest\\upload\\orders_20150717_103643.csv", "rw");
//		// 将写文件指针移到文件头
//		raf.seek(0);
////		raf.writeBytes(bs);
//		raf.write(bs);
////		raf.writeChars(bomString);
//		raf.close();

//
//			Date a = DateTimeUtil.parse("2015-7-2 15:44:30", DateTimeUtil.DEFAULT_DATETIME_FORMAT);
//			String  ret = DateTimeUtil.format(a, DateTimeUtil.US_DATE_FORMAT_2);
//
//		System.out.println("out = " + ret);

//		String fileName = "order_updates_20150713_152100.csv";
//		fileName = fileName.substring(0, fileName.length() - 4);
//		String[] filaNameArr = fileName.split("_");
//		String date = filaNameArr[2];
//		String time = filaNameArr[3];
//
//		Date a = DateTimeUtil.parse(date + time, DateTimeUtil.DATE_TIME_FORMAT_2);
//		String  ret = DateTimeUtil.format(a, DateTimeUtil.US_DATE_FORMAT_2);
//		System.out.println("out = " + ret);

//		ArrayList<String> a = getRMAddress("中中中中中国国国国国中中","");
//
//		System.out.println("a[0] = " + a.get(0));
//		System.out.println("a[1] = " + a.get(1));
//		System.out.println("a[2] = " + a.get(2));
	}
}
