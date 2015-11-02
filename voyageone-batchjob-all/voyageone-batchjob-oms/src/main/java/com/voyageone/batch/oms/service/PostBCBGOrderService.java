package com.voyageone.batch.oms.service;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.jcraft.jsch.ChannelSftp;
import com.voyageone.batch.core.Constants;
import com.voyageone.batch.core.modelbean.SetPriceBean;
import com.voyageone.batch.core.util.SetPriceUtils;
import com.voyageone.batch.oms.OmsConstants;
import com.voyageone.batch.oms.dao.OrderDao;
import com.voyageone.batch.oms.formbean.InFormFile;
import com.voyageone.batch.oms.formbean.OutFormOrderDetailOrderDetail;
import com.voyageone.batch.oms.formbean.OutFormOrderdetailOrders;
import com.voyageone.batch.oms.modelbean.OrderExtend;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.FtpBean;
import com.voyageone.common.configs.beans.OrderChannelConfigBean;
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

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PostBCBGOrderService {
	
	private static Log logger = LogFactory.getLog(PostBCBGOrderService.class);
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	IssueLog issueLog;
	
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	private DefaultTransactionDefinition def = new DefaultTransactionDefinition();

	// taskName
	// 正常订单推送（Shipped）
	private final static String POST_BCBG_DAILY_SALES = "PostBCBGDailySales";

	// 未发货订单推送（Not Shipped）
	private final static String POST_BCBG_DEMAND = "PostBCBGDemand";

	// ftp上传文件路径设置
	//		正常订单推送（Shipped）
	private final String oms_upload_daily_sales_file_path = "oms_upload_daily_sales_file_path";
	//		未发货订单推送（Not Shipped）
	private final String oms_upload_demands_file_path = "oms_upload_demands_file_path";

	// BCBG渠道ID
	private String orderChannelID = "012";
//	private String orderChannelID = "001";

	// 正常订单推送（Shipped）
	private String postBCBGDailySalesFileName = "SALES_%s_%s.dat"; //"SALES_YYYYMMDD_HHMMSS.dat";
	// 未发货订单推送（Not Shipped）
	private String postBCBGDemandFileName = "DEMAND_%s_%s.dat";//"DEMAND_YYYYMMDD_HHMMSS.dat";

	// 输出文件编码
	private final String outputFileEncoding = "UTF-8";

	/**
	 * 向RM正常订单推送(upload)
	 *
	 */
	public boolean postBCBGDailySales() {

		boolean isSuccess = true;
		// 渠道时区
		int orderChannelTimeZone = Integer.valueOf(ChannelConfigs.getVal1(orderChannelID, ChannelConfigEnums.Name.channel_time_zone));
		// 汇率
		String exchangeRate = Codes.getCodeName("MONEY_CHG_RMB", "USD");
		// 做成时间
		String createdDateTimeTmp = DateTimeUtil.getLocalTime(DateTimeUtil.getGMTTime(), orderChannelTimeZone);
		String createdDateTime = DateTimeUtil.format(DateTimeUtil.parse(createdDateTimeTmp), DateTimeUtil.DATE_TIME_FORMAT_2);

		// 推送文件名取得
		String fileName = createPostFileNameForDailySales(orderChannelTimeZone);
		logger.info("createPostFileName DailySales File = " + fileName);

		// 路径配置信息读取
		FtpBean upLoadFtpBean = formatDailySalesUploadFtpBean();

		// 每天生成一个文件
		if (FileUtils.isFileExist(fileName, upLoadFtpBean.getUpload_local_bak_path(), 10)) {
			return isSuccess;
		}

		// 订单数据抽出
		logger.info("getPushDailySalesList");
		List<OrderExtend> pushDailySalesListList = getPushDailySalesList(orderChannelID, orderChannelTimeZone, exchangeRate);

		// 有数据的场合
		if (pushDailySalesListList != null && pushDailySalesListList.size() > 0) {
			// CSV文件做成
			if (isSuccess) {
				logger.info("createUploadFile Daily Sales record count = " + pushDailySalesListList.size());
				isSuccess = createUploadFileForDailySales(upLoadFtpBean, fileName, pushDailySalesListList, createdDateTime);
			}

			// FTP目录夹Copy推送
			if (isSuccess) {
				logger.info("uploadOrderFile");
				isSuccess = uploadOrderFile(upLoadFtpBean, fileName);
			}

			// 推送标志更新
			if (isSuccess) {
//				logger.info("updateDailySalesSendInfo");
//				isSuccess = updateOrdersInfo(POST_BCBG_DEMAND, pushDailySalesListList);
//
//				if (isSuccess) {
					// 源文件
					String srcFile = upLoadFtpBean.getUpload_localpath() + "/" + upLoadFtpBean.getUpload_filename();
					// 目标文件
					String destFile = upLoadFtpBean.getUpload_local_bak_path() + "/" + upLoadFtpBean.getUpload_filename();
					logger.info("moveFile = " + srcFile + " " + destFile);
					FileUtils.moveFile(srcFile, destFile);

//				} else {
//					logger.info("updateDailySalesSendInfo error");
//					issueLog.log("postBCBGDemands.updateDailySalesSendInfo",
//							"updateDailySalesSendInfo error;Push order file = " + fileName,
//							ErrorType.BatchJob,
//							SubSystem.OMS);
//				}
			} else {
				logger.info("uploadOrderFile error;Push Daily Sales file = " + fileName);
				issueLog.log("postBCBGDailySales.uploadOrderFile",
						"uploadOrderFile error;Push Daily Sales file = " + fileName,
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
	 * 取得Daily Sales文件用数据
	 *
	 * @param orderChannelID 订单渠道
	 * @param timeZone 渠道时区
	 * @param exchangeRate 汇率
	 */
	private List<OrderExtend> getPushDailySalesList(String orderChannelID, int timeZone, String exchangeRate) {
		List<OrderExtend> ret = null;
		ArrayList<String> searchDateList = getSearchDate(timeZone);

		// 检索日期取得
		// 开始检索日
		String beginSearchDate = searchDateList.get(0);
		// 终了检索日
		String endSearchDate = searchDateList.get(1);
		logger.info("getPushDailySalesList searchDate = " + beginSearchDate + " " + endSearchDate);

		try {
			// 订单数据取得
			List<OrderExtend> pushOrderList = orderDao.getPushBCBGDailySalesInfo(orderChannelID, beginSearchDate, endSearchDate);

			Float rate = Float.valueOf(exchangeRate);
			// 订单号
			String orderNumber = "";
			// 订单明细价格
			List<SetPriceBean> orderPriceDatas = new ArrayList<SetPriceBean>();
			// 订单单位连番
			int lineNumber = 0;

			for (int i = 0; i < pushOrderList.size(); i++) {
				OrderExtend orderExtendInfo = pushOrderList.get(i);
				// SKU金额取得
				if (!orderNumber.equals(orderExtendInfo.getOrderNumber())) {
					orderPriceDatas = new ArrayList<SetPriceBean>();
					orderPriceDatas = SetPriceUtils.setPrice(orderExtendInfo.getOrderNumber(), orderExtendInfo.getOrderChannelId(), orderExtendInfo.getCartId(), 4);

					orderNumber = orderExtendInfo.getOrderNumber();
					lineNumber = 1;
				}
				SetPriceBean skuPriceInfo = getSKUPriceInfo(orderExtendInfo.getSku(), orderPriceDatas);

				// LineNumber
				orderExtendInfo.setLineNumber(String.valueOf(lineNumber));

				// OrderDateTime
				orderExtendInfo.setOrderDateTime(getLocalDateTime(orderExtendInfo.getOrderDateTime(), timeZone));

				// OrderDate
				orderExtendInfo.setOrderDate(getLocalDate(orderExtendInfo.getOrderDateTime(), timeZone));

				// ShipDate
				orderExtendInfo.setShipDate(getLocalDate(orderExtendInfo.getShipDate(), timeZone));

				// 售价含折扣
				float price = div2Digits(Float.valueOf(skuPriceInfo.getPrice()), rate);
				orderExtendInfo.setPrice(String.valueOf(price));

				// 售价不含折扣
				float unitPrice = div2Digits(Float.valueOf(orderExtendInfo.getPricePerUnit()), rate);
				orderExtendInfo.setPricePerUnit(String.valueOf(unitPrice));

				// 订单折扣
				float orderDiscount = div2Digits(Float.valueOf(skuPriceInfo.getShipping_price()), rate);
				orderExtendInfo.setOrderDiscount(String.valueOf(orderDiscount));

				lineNumber = lineNumber + 1;
			}

			ret = pushOrderList;
		} catch(Exception e) {
			ret = null;

			logger.error("getPushDemandList error", e);
			issueLog.log("postBCBGDemands.getPushDemandList",
					"getPushDemandList error",
					ErrorType.BatchJob,
					SubSystem.OMS);
		}
		return ret;
	}

	/**
	 * 发货订单推送（Shipped）
	 *
	 * @param fileName 待上传文件名
	 */
	private boolean createUploadFileForDailySales(FtpBean ftpBean, String fileName, List<OrderExtend> pushOrderList, String createdDateTime) {
		boolean isSuccess = true;

		// 本地文件生成路径
		String uploadLocalPath = ftpBean.getUpload_localpath();
		try {

			File file = new File(uploadLocalPath + "/" + fileName);
			FileOutputStream  fop = new FileOutputStream(file);

			FileWriteUtils fileWriter = new FileWriteUtils(fop,  Charset.forName(outputFileEncoding), "A","S");

			// Body输出
			createUploadFileBodyForDailySales(fileWriter, pushOrderList, createdDateTime);

			fileWriter.flush();
			fileWriter.close();

		} catch (Exception e) {
			isSuccess = false;

			logger.error("createUploadFileForDailySales", e);

			issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);
		}

		return isSuccess;
	}

	/**
	 * DailySales文件生成
	 *
	 * @param fileWriter 待上传文件名
	 * @param pushOrderList 订单数据
	 * @param createdDateTime 处理时间
	 */
	private void createUploadFileBodyForDailySales(FileWriteUtils fileWriter, List<OrderExtend> pushOrderList, String createdDateTime) throws IOException {

		for (int i = 0 ; i < pushOrderList.size(); i++) {
			OrderExtend orderInfo = pushOrderList.get(i);

			fileWriter.write(" ", DailySalesFileFormat.StoreNumber);
			fileWriter.write(" ", DailySalesFileFormat.CustomerID);
			fileWriter.write(" ", DailySalesFileFormat.OrderNumber);
			fileWriter.write(orderInfo.getSourceOrderId(), DailySalesFileFormat.WebOrderNumber);
			fileWriter.write("SO", DailySalesFileFormat.OrderType);
			fileWriter.write(orderInfo.getOrderDate(), DailySalesFileFormat.OrderDate);
			fileWriter.write(orderInfo.getShipDate(), DailySalesFileFormat.ShipDate);
			fileWriter.write(orderInfo.getLineNumber(), DailySalesFileFormat.LineNumber);
			fileWriter.write(" ", DailySalesFileFormat.LongItemNumber);
			fileWriter.write(orderInfo.getUPC(), DailySalesFileFormat.UPC);
			fileWriter.write(orderInfo.getStyle(), DailySalesFileFormat.Style);
			fileWriter.write(orderInfo.getColor(), DailySalesFileFormat.Color);
			fileWriter.write(orderInfo.getSize(), DailySalesFileFormat.Size);
			fileWriter.write(orderInfo.getQuantityOrdered(), DailySalesFileFormat.QuantityOrdered);
			fileWriter.write(orderInfo.getQuantityShipped(), DailySalesFileFormat.QuantityShipped);
			fileWriter.write(orderInfo.getMSRP(), DailySalesFileFormat.ListPrice);

			float unitPrice = div2Digits(Float.valueOf(orderInfo.getPrice()), Float.valueOf(orderInfo.getQuantityOrdered()));
			fileWriter.write(String.valueOf(unitPrice), DailySalesFileFormat.UnitPrice);

			fileWriter.write(orderInfo.getPrice(), DailySalesFileFormat.ItemAmount);
			fileWriter.write(" ", DailySalesFileFormat.TaxAmount);
			fileWriter.write("EMS", DailySalesFileFormat.Carrier);
			fileWriter.write(" ", DailySalesFileFormat.ReasonCode);
			fileWriter.write(" ", DailySalesFileFormat.ShipToAddressID);
			fileWriter.write(orderInfo.getShipName(), DailySalesFileFormat.ShipToName);

			ArrayList<String> shipAddressList = getBCBGAddress(orderInfo.getShipAddress(), orderInfo.getShipAddress2());
			fileWriter.write(shipAddressList.get(0), DailySalesFileFormat.ShipToAddress1);
			fileWriter.write(shipAddressList.get(1), DailySalesFileFormat.ShipToAddress2);

			fileWriter.write(orderInfo.getShipCity(), DailySalesFileFormat.ShipToCity);
			fileWriter.write(orderInfo.getShipState(), DailySalesFileFormat.ShipToState);
			fileWriter.write(orderInfo.getShipZip(), DailySalesFileFormat.ShipToZip);
			fileWriter.write(orderInfo.getShipCountry(), DailySalesFileFormat.ShipToCountry);
			fileWriter.write(orderInfo.getShipPhone(), DailySalesFileFormat.ShipToPhone);
			fileWriter.write(" ", DailySalesFileFormat.BillToAddressID);
			fileWriter.write(orderInfo.getName(), DailySalesFileFormat.BillToName);

			ArrayList<String> billAddressList = getBCBGAddress(orderInfo.getAddress(), orderInfo.getAddress2());

			fileWriter.write(billAddressList.get(0), DailySalesFileFormat.BillToAddress1);
			fileWriter.write(billAddressList.get(1), DailySalesFileFormat.BillToAddress2);
			fileWriter.write(orderInfo.getCity(), DailySalesFileFormat.BillToCity);
			fileWriter.write(orderInfo.getState(), DailySalesFileFormat.BillToState);
			fileWriter.write(orderInfo.getZip(), DailySalesFileFormat.BillToZip);
			fileWriter.write(orderInfo.getCountry(), DailySalesFileFormat.BillToCountry);
			fileWriter.write(orderInfo.getPhone(), DailySalesFileFormat.BillToPhone);
			fileWriter.write("ALIPAY", DailySalesFileFormat.PaymentType);
			fileWriter.write(orderInfo.getPricePerUnit(), DailySalesFileFormat.SalePrice);
			fileWriter.write("Discount", DailySalesFileFormat.Promo1);
			fileWriter.write(orderInfo.getOrderDiscount(), DailySalesFileFormat.PromoDiscountAmount1);
			fileWriter.write("Discount", DailySalesFileFormat.PromoDiscountDescription1);
			fileWriter.write(" ", DailySalesFileFormat.Promo2);
			fileWriter.write(" ", DailySalesFileFormat.PromoDiscountAmount2);
			fileWriter.write(" ", DailySalesFileFormat.PromoDiscountDescription2);
			fileWriter.write(" ", DailySalesFileFormat.Promo3);
			fileWriter.write(" ", DailySalesFileFormat.PromoDiscountAmount3);
			fileWriter.write(" ", DailySalesFileFormat.PromoDiscountDescription3);
			fileWriter.write(" ", DailySalesFileFormat.Promo4);
			fileWriter.write(" ", DailySalesFileFormat.PromoDiscountAmount4);
			fileWriter.write(" ", DailySalesFileFormat.PromoDescountDescription4);
			fileWriter.write(" ", DailySalesFileFormat.Promo5);
			fileWriter.write(" ", DailySalesFileFormat.PromoDiscountAmount5);
			fileWriter.write(" ", DailySalesFileFormat.PromoDiscountDescription5);
			fileWriter.write("45010", DailySalesFileFormat.InventoryCondition);
			fileWriter.write(" ", DailySalesFileFormat.AgentIDChannel);
			fileWriter.write(orderInfo.getTrackingNo(), DailySalesFileFormat.TrackingNo);
			fileWriter.write(" ", DailySalesFileFormat.OriginalOrderNumber);
			fileWriter.write(" ", DailySalesFileFormat.ReasonCodeDescription);
			fileWriter.write(orderInfo.getOrderDateTime(), DailySalesFileFormat.WebOrderDateTime);
			fileWriter.write(createdDateTime, DailySalesFileFormat.DateTimeFileCreated);
			fileWriter.write(" ", DailySalesFileFormat.AOSStoreID);
			fileWriter.write(" ", DailySalesFileFormat.AOSEmployeeEIN);
			fileWriter.write(" ", DailySalesFileFormat.AOSEmployeeName);
			fileWriter.write(" ", DailySalesFileFormat.Source);
			fileWriter.write(orderInfo.getSourceOrderId(), DailySalesFileFormat.BorderfreeOrderNO);
			fileWriter.write(orderInfo.getEmail(), DailySalesFileFormat.CustomerEmailAddress);

			fileWriter.write(System.getProperty("line.separator"));
		}
	}

	/**
	 * 本地时间取得 yyyyMMdd
	 *
	 * @param dateTime 待处理时间
	 * @param timeZone 本地时区
	 *
	 */
	private String getLocalDate(String dateTime, int timeZone) {
		String localDateTime = DateTimeUtil.getLocalTime(DateTimeUtil.parse(dateTime), timeZone);
		String localDate = DateTimeUtil.format(DateTimeUtil.parse(localDateTime, DateTimeUtil.DEFAULT_DATETIME_FORMAT), DateTimeUtil.DATE_TIME_FORMAT_3);

		return localDate;
	}

	/**
	 * 本地时间取得 yyyyMMddHHmmss
	 *
	 * @param dateTime 待处理时间
	 * @param timeZone 本地时区
	 *
	 */
	private String getLocalDateTime(String dateTime, int timeZone) {
		String localDateTime = DateTimeUtil.getLocalTime(DateTimeUtil.parse(dateTime), timeZone);
		String localDate = DateTimeUtil.format(DateTimeUtil.parse(localDateTime, DateTimeUtil.DEFAULT_DATETIME_FORMAT), DateTimeUtil.DATE_TIME_FORMAT_2);

		return localDate;
	}

	/**
	 * BCBG地址取得
	 *
	 * @param address1 订单地址1
	 * @param address2 订单地址2
	 *
	 */
	private ArrayList<String> getBCBGAddress(String address1, String address2) {
		ArrayList<String> retAddress = new ArrayList<String>();

		String retAddress1 = "";
		String retAddress2 = "";

		String allAddress = address1 + address2;
		if (allAddress.length() <= 20) {
			retAddress1 = allAddress;
		} else if (allAddress.length() > 20 && allAddress.length() <= 40) {
			retAddress1 = allAddress.substring(0,20);
			retAddress2 = allAddress.substring(20,allAddress.length());
		} else {
			retAddress1 = allAddress.substring(0,20);
			retAddress2 = allAddress.substring(20,40);
		}

		retAddress.add(retAddress1);
		retAddress.add(retAddress2);
		return retAddress;
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

			// Shiped订单推送
			if (POST_BCBG_DAILY_SALES.equals(taskName)) {
				isSuccess = orderDao.updateOrderExtFlg1(POST_BCBG_DAILY_SALES, getSelectOrderNumberList(pushOrderList));
			// Not Shipped订单推送
			} else if (POST_BCBG_DEMAND.equals(taskName)) {
				isSuccess = orderDao.updateOrdersSendInfo(POST_BCBG_DEMAND, getSelectOrderNumberList(pushOrderList));
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
		// Shipped Order 推送
		if (POST_BCBG_DAILY_SALES.equals(taskName)) {
			note = "Push Daily Sales File to BCBG";
		// Not Shipped Order 推送
		} else if (POST_BCBG_DEMAND.equals(taskName)) {
			note = "Push cancel order to RM";
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
	 * 向BCBG推送Demand订单(upload)
	 *
	 */
	public boolean postBCBGDemand() {
		boolean isSuccess = true;
		// 渠道时区
		int orderChannelTimeZone = Integer.valueOf(ChannelConfigs.getVal1(orderChannelID, ChannelConfigEnums.Name.channel_time_zone));
		// 汇率
		String exchangeRate = Codes.getCodeName("MONEY_CHG_RMB", "USD");

		// 推送文件名取得
		String fileName = createPostFileNameForDemands(orderChannelTimeZone);
		logger.info("createPostFileName Demands File = " + fileName);

		// 路径配置信息读取
		FtpBean upLoadFtpBean = formatDemandUploadFtpBean();

		// 每天生成一个文件
		if (FileUtils.isFileExist(fileName, upLoadFtpBean.getUpload_local_bak_path(), 10)) {
			return isSuccess;
		}

		// 订单数据抽出
		List<OrderExtend> pushDemandList = getPushDemandList(orderChannelID, orderChannelTimeZone, exchangeRate);

		// 有数据的场合
		if (pushDemandList != null && pushDemandList.size() > 0) {
			// CSV文件做成
			if (isSuccess) {
				logger.info("createUploadFile Demands record count = " + pushDemandList.size());
				isSuccess = createUploadFileForDemands(upLoadFtpBean, fileName, pushDemandList);
			}

			// FTP目录夹Copy推送
			if (isSuccess) {
				logger.info("uploadOrderFile");
				isSuccess = uploadOrderFile(upLoadFtpBean, fileName);
			}

			// 推送标志更新
			if (isSuccess) {
//				logger.info("updateDemandSendInfo");
//				isSuccess = updateOrdersInfo(POST_BCBG_DEMAND, pushDemandList);
//
//				if (isSuccess) {
					// 源文件
					String srcFile = upLoadFtpBean.getUpload_localpath() + "/" + upLoadFtpBean.getUpload_filename();
					// 目标文件
					String destFile = upLoadFtpBean.getUpload_local_bak_path() + "/" + upLoadFtpBean.getUpload_filename();
					logger.info("moveFile = " + srcFile + " " + destFile);
					FileUtils.moveFile(srcFile, destFile);

//				} else {
//					logger.info("updateDemandSendInfo error");
//					issueLog.log("postBCBGDemands.updateDemandSendInfo",
//							"updateDemandSendInfo error;Push order file = " + fileName,
//							ErrorType.BatchJob,
//							SubSystem.OMS);
//				}
			} else {
				logger.info("uploadOrderFile error;Push demand file = " + fileName);
				issueLog.log("postRMNormalOrder.uploadOrderFile",
						"uploadOrderFile error;Push demand file = " + fileName,
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
	 * 取得Demand文件用数据
	 *
	 * @param orderChannelID 订单渠道
	 * @param timeZone 渠道时区
	 * @param exchangeRate 汇率
	 */
	private List<OrderExtend> getPushDemandList(String orderChannelID, int timeZone, String exchangeRate) {
		List<OrderExtend> ret = null;
		ArrayList<String> searchDateList = getSearchDate(timeZone);

		// 检索日期取得
		// 开始检索日
		String beginSearchDate = searchDateList.get(0);
		// 终了检索日
		String endSearchDate = searchDateList.get(1);
		logger.info("getPushDemandList searchDate = " + beginSearchDate + " " + endSearchDate);

		try {
			// 订单数据取得
			List<OrderExtend> pushOrderList = orderDao.getPushBCBGDemandsInfo(orderChannelID, beginSearchDate, endSearchDate);

			Float rate = Float.valueOf(exchangeRate);
			// 订单号
			String orderNumber = "";
			// 订单明细价格
			List<SetPriceBean> orderPriceDatas = new ArrayList<SetPriceBean>();
			// 订单单位连番
			int lineNumber = 0;

			for (int i = 0; i < pushOrderList.size(); i++) {
				OrderExtend orderExtendInfo = pushOrderList.get(i);
				// SKU金额取得
				if (!orderNumber.equals(orderExtendInfo.getOrderNumber())) {
					orderPriceDatas = new ArrayList<SetPriceBean>();
					orderPriceDatas = SetPriceUtils.setPrice(orderExtendInfo.getOrderNumber(), orderExtendInfo.getOrderChannelId(), orderExtendInfo.getCartId(), 4);

					orderNumber = orderExtendInfo.getOrderNumber();

					lineNumber = 1;
				}
				SetPriceBean skuPriceInfo = getSKUPriceInfo(orderExtendInfo.getSku(), orderPriceDatas);

				// LineNumber
				orderExtendInfo.setLineNumber(String.valueOf(lineNumber));

				// OrderDate
				orderExtendInfo.setOrderDate(getLocalDate(orderExtendInfo.getOrderDateTime(), timeZone));

				// 售价含折扣
				float price = div2Digits(Float.valueOf(skuPriceInfo.getPrice()), rate);
				orderExtendInfo.setPrice(String.valueOf(price));

				// 售价不含折扣
				float unitPrice = div2Digits(Float.valueOf(orderExtendInfo.getPricePerUnit()), rate);
				orderExtendInfo.setPricePerUnit(String.valueOf(unitPrice));

				lineNumber = lineNumber + 1;
			}

			ret = pushOrderList;
		} catch(Exception e) {
			ret = null;

			logger.error("getPushDemandList error", e);
			issueLog.log("postBCBGDemands.getPushDemandList",
					"getPushDemandList error",
					ErrorType.BatchJob,
					SubSystem.OMS);
		}
		return ret;
	}

	private SetPriceBean getSKUPriceInfo(String SKU, List<SetPriceBean> orderPriceDatas) {
		SetPriceBean ret = null;

		for (int i = 0; i < orderPriceDatas.size(); i++) {
			SetPriceBean skuPriceInfo = orderPriceDatas.get(i);

			if (skuPriceInfo.getSku().equals(SKU)) {
				ret = skuPriceInfo;
				break;
			}
		}

		return ret;
	}

	/**
	 * 检索时间（美国时间，前一天的数据）
	 *
	 * @param timeZone 本地时区
	 */
	private ArrayList<String> getSearchDate(int timeZone) {
		ArrayList<String> retSearchDate = new ArrayList<String>();

		String localTime = DateTimeUtil.getLocalTime(DateTimeUtil.getNow(), timeZone);
		Date searchDate = DateTimeUtil.addDays(DateTimeUtil.parse(localTime), -1);
		String searchDateString =  DateTimeUtil.format(searchDate, DateTimeUtil.DEFAULT_DATE_FORMAT);
		String startSearchDate = searchDateString + " 00:00:00";
		String endSearchDate = searchDateString + " 23:59:59";

		String startSearchDateGMT = DateTimeUtil.getGMTTime(startSearchDate, timeZone);
		String endSearchDateGMT = DateTimeUtil.getGMTTime(endSearchDate, timeZone);

		retSearchDate.add(startSearchDateGMT);
		retSearchDate.add(endSearchDateGMT);

		return retSearchDate;
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
		try {
			String localFile = ftpBean.getUpload_localpath() + File.separator + ftpBean.getUpload_filename();
			String remoteFile = ftpBean.getUpload_path() + File.separator + ftpBean.getUpload_filename();

			FileUtils.copyFile(localFile, remoteFile);

		} catch (Exception e) {

			isSuccess = false;

			logger.error("uploadOrderFile", e);

			issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);

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
	 * 推送文件名生成（SALES_YYYYMMDD_HHMMSS.dat）
	 *
	 */
	private String createPostFileNameForDailySales(int timeZone) {
		String localTime = DateTimeUtil.getLocalTime(DateTimeUtil.getNow(), timeZone);
		Date localTimeForDate = DateTimeUtil.parse(localTime);

		String date = DateTimeUtil.format(localTimeForDate, DateTimeUtil.DATE_TIME_FORMAT_3);
		String time = DateTimeUtil.format(localTimeForDate, DateTimeUtil.DATE_TIME_FORMAT_4);

		return String.format(postBCBGDailySalesFileName, date, time);
	}

	/**
	 * 推送文件名生成（DEMAND_YYYYMMDD_HHMMSS.dat）
	 *
	 */
	private String createPostFileNameForDemands(int timeZone) {
		String localTime = DateTimeUtil.getLocalTime(DateTimeUtil.getNow(), timeZone);
		Date localTimeForDate = DateTimeUtil.parse(localTime);

		String date = DateTimeUtil.format(localTimeForDate, DateTimeUtil.DATE_TIME_FORMAT_3);
		String time = DateTimeUtil.format(localTimeForDate, DateTimeUtil.DATE_TIME_FORMAT_4);

		return String.format(postBCBGDemandFileName, date, time);
	}

	/**
	 * 未发货订单推送（Not Shipped）
	 *
	 * @param fileName 待上传文件名
	 */
	private boolean createUploadFileForDemands(FtpBean ftpBean, String fileName, List<OrderExtend> pushOrderList) {
		boolean isSuccess = true;

		// 本地文件生成路径
		String uploadLocalPath = ftpBean.getUpload_localpath();
		try {

			File file = new File(uploadLocalPath + "/" + fileName);
			FileOutputStream  fop = new FileOutputStream(file);

			FileWriteUtils fileWriter = new FileWriteUtils(fop,  Charset.forName(outputFileEncoding), "A","S");

			// Body输出
			createUploadFileBodyForDemands(fileWriter, pushOrderList);

			fileWriter.flush();
			fileWriter.close();

		} catch (Exception e) {
			isSuccess = false;

			logger.error("createUploadFileForDemands", e);

			issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);
		}

		return isSuccess;
	}

	private void createUploadFileBodyForDemands(FileWriteUtils fileWriter, List<OrderExtend> pushOrderList) throws IOException {

		for (int i = 0 ; i < pushOrderList.size(); i++) {
			OrderExtend orderInfo = pushOrderList.get(i);
			fileWriter.write(" ", DemandsFileFormat.StoreNumber);
			fileWriter.write(" ", DemandsFileFormat.OrderNumber);
			fileWriter.write(orderInfo.getLineNumber(), DemandsFileFormat.LineNumber);
			fileWriter.write(orderInfo.getOrderDate(), DemandsFileFormat.OrderDate);
			fileWriter.write(orderInfo.getSourceOrderId(), DemandsFileFormat.WebOrderNumber);
			fileWriter.write(" ", DemandsFileFormat.AgentIDChannel);
			fileWriter.write(" ", DemandsFileFormat.LongItemNumber);
			fileWriter.write(orderInfo.getUPC(), DemandsFileFormat.UPC);
			fileWriter.write(orderInfo.getPricePerUnit(), DemandsFileFormat.SalePrice);
			fileWriter.write(orderInfo.getQuantityOrdered(), DemandsFileFormat.QuantityOrdered);
			fileWriter.write(orderInfo.getQuantityShipped(), DemandsFileFormat.QuantityShipped);
			fileWriter.write(" ", DemandsFileFormat.POBOQuantity);
			fileWriter.write(" ", DemandsFileFormat.PromiseDate);
			fileWriter.write(orderInfo.getPrice(), DemandsFileFormat.UnitPrice);
			fileWriter.write("0", DemandsFileFormat.TaxAmount);
			fileWriter.write(" ", DemandsFileFormat.Promo1);
			fileWriter.write("0", DemandsFileFormat.PromoDiscountAmount1);
			fileWriter.write(" ", DemandsFileFormat.PromoDiscountDescription1);
			fileWriter.write(" ", DemandsFileFormat.Promo2);
			fileWriter.write("0", DemandsFileFormat.PromoDiscountAmount2);
			fileWriter.write(" ", DemandsFileFormat.PromoDiscountDescription2);
			fileWriter.write(" ", DemandsFileFormat.Promo3);
			fileWriter.write("0", DemandsFileFormat.PromoDiscountAmount3);
			fileWriter.write(" ", DemandsFileFormat.PromoDiscountDescription3);
			fileWriter.write(" ", DemandsFileFormat.Promo4);
			fileWriter.write("0", DemandsFileFormat.PromoDiscountAmount4);
			fileWriter.write(" ", DemandsFileFormat.PromoDiscountDescription4);
			fileWriter.write(" ", DemandsFileFormat.Promo5);
			fileWriter.write("0", DemandsFileFormat.PromoDiscountAmount5);
			fileWriter.write(" ", DemandsFileFormat.PromoDiscountDescription5);
			fileWriter.write("IP", DemandsFileFormat.HoldCode);
			fileWriter.write("In Process", DemandsFileFormat.HoldCodeDescription);
			fileWriter.write(DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_2), DemandsFileFormat.DateTimeFileCreated);
			fileWriter.write(orderInfo.getSourceOrderId(), DemandsFileFormat.BorderFreeOrderNO);
			fileWriter.write(" ", DemandsFileFormat.AOSEINNO);
			fileWriter.write(" ", DemandsFileFormat.AOSStoreNO);
			fileWriter.write(" ", DemandsFileFormat.CustomerEmailAddress);

			fileWriter.write(System.getProperty("line.separator"));
		}
	}

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
	 * 上传Bean生成
	 *
	 */
	private FtpBean formatDemandUploadFtpBean() {
		FtpBean ftpBean = new FtpBean();

		// 配置信息读取
		List<ThirdPartyConfigBean> ftpFilePaths = ThirdPartyConfigs.getThirdPartyConfigList(orderChannelID, oms_upload_demands_file_path);

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
	private FtpBean formatDailySalesUploadFtpBean() {
		FtpBean ftpBean = new FtpBean();

		// 配置信息读取
		List<ThirdPartyConfigBean> ftpFilePaths = ThirdPartyConfigs.getThirdPartyConfigList(orderChannelID, oms_upload_daily_sales_file_path);

		// 上传文件路径
		ftpBean.setUpload_path(ftpFilePaths.get(0).getProp_val1());
		// 本地文件路径
		ftpBean.setUpload_localpath(ftpFilePaths.get(0).getProp_val2());
		// 本地文件备份路径
		ftpBean.setUpload_local_bak_path(ftpFilePaths.get(0).getProp_val3());

		return ftpBean;
	}

	/**
	 * SALES_YYYYMMDD_HHMMSS.dat 文件格式（upload： /opt/ftp-shared/clients-ftp/voyageone-bcbg-sftp/orders/sales）
	 */
	private class DailySalesFileFormat {
		private static final String StoreNumber = "A,5";
		private static final String CustomerID = "S,8";
		private static final String OrderNumber = "S,8";
		private static final String WebOrderNumber = "A,25";
		private static final String OrderType = "A,2";
		private static final String OrderDate = "S,8";
		private static final String ShipDate = "S,8";
		private static final String LineNumber = "S,6";
		private static final String LongItemNumber = "A,25";
		private static final String UPC = "A,25";
		private static final String Style = "A,20";
		private static final String Color = "A,5";
		private static final String Size = "A,20";
		private static final String QuantityOrdered = "S,15";
		private static final String QuantityShipped = "S,15";
		private static final String ListPrice = "S,15.2";
		private static final String UnitPrice = "S,15.2";
		private static final String ItemAmount = "S,15.2";
		private static final String TaxAmount = "S,15.2";
		private static final String Carrier = "S,8";
		private static final String ReasonCode = "A,4";
		private static final String ShipToAddressID = "S,8";
		private static final String ShipToName = "A,40";
		private static final String ShipToAddress1 = "A,40";
		private static final String ShipToAddress2 = "A,40";
		private static final String ShipToCity = "A,25";
		private static final String ShipToState = "A,3";
		private static final String ShipToZip = "A,12";
		private static final String ShipToCountry = "A,3";
		private static final String ShipToPhone = "A,40";
		private static final String BillToAddressID = "S,8";
		private static final String BillToName = "A,40";
		private static final String BillToAddress1 = "A,40";
		private static final String BillToAddress2 = "A,40";
		private static final String BillToCity = "A,25";
		private static final String BillToState = "A,3";
		private static final String BillToZip = "A,12";
		private static final String BillToCountry = "A,3";
		private static final String BillToPhone = "A,40";
		private static final String PaymentType = "A,20";
		private static final String SalePrice = "S,15.2";
		private static final String Promo1 = "A,12";
		private static final String PromoDiscountAmount1 = "S,15.2";
		private static final String PromoDiscountDescription1 = "A,20";
		private static final String Promo2 = "A,12";
		private static final String PromoDiscountAmount2 = "S,15.2";
		private static final String PromoDiscountDescription2 = "A,20";
		private static final String Promo3 = "A,12";
		private static final String PromoDiscountAmount3 = "S,15.2";
		private static final String PromoDiscountDescription3 = "A,20";
		private static final String Promo4 = "A,12";
		private static final String PromoDiscountAmount4 = "S,15.2";
		private static final String PromoDescountDescription4 = "A,20";
		private static final String Promo5 = "A,12";
		private static final String PromoDiscountAmount5 = "S,15.2";
		private static final String PromoDiscountDescription5 = "A,20";
		private static final String InventoryCondition = "A,12";
		private static final String AgentIDChannel = "A,10";
		private static final String TrackingNo = "A,25";
		private static final String OriginalOrderNumber = "A,8";
		private static final String ReasonCodeDescription = "A,40";
		private static final String WebOrderDateTime = "A,14";
		private static final String DateTimeFileCreated = "A,14";
		private static final String AOSStoreID = "A,10";
		private static final String AOSEmployeeEIN = "A,10";
		private static final String AOSEmployeeName = "A,40";
		private static final String Source = "A,10";
		private static final String BorderfreeOrderNO = "A,25";
		private static final String CustomerEmailAddress = "A,40";
	}

	/**
	 * DEMAND_YYYYMMDD_HHMMSS.dat 文件格式（upload： /opt/ftp-shared/clients-ftp/voyageone-bcbg-sftp/orders/demands）
	 */
	private class DemandsFileFormat {
		private static final String StoreNumber = "A,5";
		private static final String OrderNumber = "S,8";
		private static final String LineNumber = "S,6";
		private static final String OrderDate = "A,14";
		private static final String WebOrderNumber = "A,25";
		private static final String AgentIDChannel = "S,10";
		private static final String LongItemNumber = "S,25";
		private static final String UPC = "S,14";
		private static final String SalePrice = "S,15.2";
		private static final String QuantityOrdered = "A,15";
		private static final String QuantityShipped = "A,15";
		private static final String POBOQuantity = "A,15";
		private static final String PromiseDate = "A,8";
		private static final String UnitPrice = "S,15.2";
		private static final String TaxAmount = "S,15.2";
		private static final String Promo1 = "S,12";
		private static final String PromoDiscountAmount1 = "S,15.2";
		private static final String PromoDiscountDescription1 = "S,20";
		private static final String Promo2 = "S,12";
		private static final String PromoDiscountAmount2 = "S,15.2";
		private static final String PromoDiscountDescription2 = "S,20";
		private static final String Promo3 = "A,12";
		private static final String PromoDiscountAmount3 = "S,15.2";
		private static final String PromoDiscountDescription3 = "A,20";
		private static final String Promo4 = "A,12";
		private static final String PromoDiscountAmount4 = "S,15.2";
		private static final String PromoDiscountDescription4 = "A,20";
		private static final String Promo5 = "A,12";
		private static final String PromoDiscountAmount5 = "S,15.2";
		private static final String PromoDiscountDescription5 = "S,20";
		private static final String HoldCode = "A,3";
		private static final String HoldCodeDescription = "A,40";
		private static final String DateTimeFileCreated = "A,14";
		private static final String BorderFreeOrderNO = "A,25";
		private static final String AOSEINNO = "A,10";
		private static final String AOSStoreNO = "A,10";
		private static final String CustomerEmailAddress = "A,40";
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

	/**
	 * 除法 保留两位小数
	 *
	 * @param addPara1
	 * @param addPara2
	 * @return ret
	 */
	private float div2Digits(float addPara1, float addPara2) {
		float ret = 0f;

		BigDecimal bd1 = new BigDecimal(addPara1);
		BigDecimal bd2 = new BigDecimal(addPara2);

//		ret = bd1.divide(bd2).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		BigDecimal bd3 = bd1.divide(bd2, 2, BigDecimal.ROUND_HALF_UP);
		ret = bd3.floatValue();

		return ret;
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
//		String temp = "中国人1";
//
//		System.out.println(temp.length());
//		System.out.println(temp.getBytes().length);
//		Charset charset = Charset.forName("gbk");
//		System.out.println(temp.getBytes(charset).length);

//		float a = Float.valueOf("6.2");
//		float price = div2Digits(Float.valueOf("1049.00"), a);
//		System.out.println(price);

		String inputString = "1.2";
		String numericLength = "10";
		String outputString = "";

		if (inputString.contains(".")) {
			double numericDouble = Double.parseDouble(inputString);
			DecimalFormat df = new DecimalFormat("#.00");
			outputString = StringUtils.lPad(df.format(numericDouble), "0", Integer.valueOf(numericLength));
		} else {
			outputString = StringUtils.lPad(inputString, "0", Integer.valueOf(numericLength));
		}

		System.out.println(outputString);
	}
}
