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
import com.voyageone.batch.oms.utils.WebServiceUtil;
import com.voyageone.common.components.baidu.translate.BaiduTranslateUtil;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.sears.bean.OrderLookupItemSub;
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
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
//	private String postBCBGDailySalesFileName = "SALES_%s_%s.dat"; //"SALES_YYYYMMDD_HHMMSS.dat";
	private String postBCBGDailySalesFileName = "DailySales_VO_BMX_%s_%s.dat"; //"DailySales_VO_BBB_YYYYMMDD_HHMMSS.dat";
	// 未发货订单推送（Not Shipped）
//	private String postBCBGDemandFileName = "DEMAND_%s_%s.dat";//"DEMAND_YYYYMMDD_HHMMSS.dat";
	private String postBCBGDemandFileName = "OrderStatus_VO_BMX_%s_%s.dat";//"OrderStatus_VO_BBB_YYYYMMDD_HHMMSS.dat";

	// 输出文件编码
	private final String outputFileEncoding = "UTF-8";

	private final String FREIGHT = "FREIGHT";
	private final String StoreNumber = "868";
	private final String Carrier = "99999999";
	private final String Country = "CHN";
	private final String PayType = "ALIPAY";
	private final String Discount = "Discount";
	private final String BAIDU_TRANSLATE_CONFIG = "BAIDU_TRANSLATE_CONFIG";

	private final String DummyDate = "00000000";

	// Task DB 更新区分
	// Shipped 订单
	private final int DAILY_SALES_FOR_SHIPPED = 1;
	// Returned 订单
	private final int DAILY_SALES_FOR_RETURNED = 2;

	// Approved 订单
	private final int DEMAND_FOR_APPROVED = 1;
	// Cancelled 订单
	private final int DEMAND_FOR_CANCELLED = 2;
	/**
	 * 向RM正常订单推送(upload)
	 *
	 */
	public boolean postBCBGDailySales() {

		boolean isSuccess = true;
		// 渠道时区
//		int orderChannelTimeZone = Integer.valueOf(ChannelConfigs.getVal1(orderChannelID, ChannelConfigEnums.Name.channel_time_zone));
		int orderChannelTimeZone = 8; // 以北京时间计算
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
		upLoadFtpBean.setUpload_filename(fileName);

		// 每天生成一个文件
		if (FileUtils.isFileExist(fileName, upLoadFtpBean.getUpload_local_bak_path(), 10)) {
			return isSuccess;
		}

		// 订单数据抽出（正常订单 Shipped）
		logger.info("getPushDailySalesList");
		List<OrderExtend> pushDailySalesListList = getPushDailySalesList(orderChannelID, orderChannelTimeZone, exchangeRate);

		// 订单数据抽出（正常订单 Return）
		logger.info("getPushDailySalesListForReturn");
		List<Object> pushDailySalesListListForReturn = getPushDailySalesListForReturn(orderChannelID, orderChannelTimeZone, exchangeRate);
		boolean retReturn = (boolean)pushDailySalesListListForReturn.get(0);
		List<OrderExtend> retReturnForOutputList = (List<OrderExtend>)pushDailySalesListListForReturn.get(1);
		List<OrderExtend> retReturnForUpdateList = (List<OrderExtend>)pushDailySalesListListForReturn.get(2);

		// 正常订单有数据的场合 && Return订单有数据的场合
		if (pushDailySalesListList != null &&	retReturn &&
				(pushDailySalesListList.size() > 0 || retReturnForOutputList.size() > 0)) {

			// CSV文件做成（Shipped Order）
			if (isSuccess) {
				if (pushDailySalesListList.size() > 0) {
					logger.info("createUploadFile Daily Sales shipped record count = " + pushDailySalesListList.size());
					isSuccess = createUploadFileForDailySales(upLoadFtpBean, fileName, pushDailySalesListList, createdDateTime, DAILY_SALES_FOR_SHIPPED);
				}
			}
			// CSV文件做成（Returned Order）
			if (isSuccess && retReturnForOutputList.size() > 0) {
				logger.info("createUploadFile Daily Sales returned record count = " + retReturnForOutputList.size());
				isSuccess = createUploadFileForDailySales(upLoadFtpBean, fileName, retReturnForOutputList, createdDateTime, DAILY_SALES_FOR_RETURNED);
			}

			// FTP目录夹Copy推送
			if (isSuccess) {
				logger.info("uploadOrderFile");
				isSuccess = uploadOrderFile(upLoadFtpBean, fileName);
			}

			// 推送标志更新
			if (isSuccess) {
				if (pushDailySalesListList.size() > 0) {
					logger.info("updateDailySalesSendInfo Shipped Order");
					isSuccess = updateOrdersInfo(POST_BCBG_DAILY_SALES, pushDailySalesListList, DAILY_SALES_FOR_SHIPPED);
				}

				if (retReturnForUpdateList.size() > 0) {
					logger.info("updateDailySalesSendInfo Returned Order");
					isSuccess = updateOrdersInfo(POST_BCBG_DAILY_SALES, retReturnForUpdateList, DAILY_SALES_FOR_RETURNED);
				}

//
				if (isSuccess) {
					// 源文件
					String srcFile = upLoadFtpBean.getUpload_localpath() + "/" + upLoadFtpBean.getUpload_filename();
					// 目标文件
					String destFile = upLoadFtpBean.getUpload_local_bak_path() + "/" + upLoadFtpBean.getUpload_filename();
					logger.info("moveFile = " + srcFile + " " + destFile);
					FileUtils.moveFile(srcFile, destFile);

				} else {
					logger.info("updateDailySalesSendInfo error");
					issueLog.log("postBCBGDailySales.updateDailySalesSendInfo",
							"updateDailySalesSendInfo error;Push order file = " + fileName,
							ErrorType.BatchJob,
							SubSystem.OMS);
				}
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
	 * 取得Daily Sales文件用数据（Shipped）
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

//			String shipDate = "";
			for (int i = 0; i < pushOrderList.size(); i++) {
				OrderExtend orderExtendInfo = pushOrderList.get(i);
				// SKU金额取得
				if (!orderNumber.equals(orderExtendInfo.getOrderNumber())) {
					orderPriceDatas = new ArrayList<SetPriceBean>();
					orderPriceDatas = SetPriceUtils.setPrice(orderExtendInfo.getOrderNumber(), orderExtendInfo.getOrderChannelId(), orderExtendInfo.getCartId(), 4);

					orderNumber = orderExtendInfo.getOrderNumber();
					lineNumber = 1;
//					shipDate = "";
				}
				SetPriceBean skuPriceInfo = getSKUPriceInfo(orderExtendInfo.getSku(), orderPriceDatas);

				// LineNumber
				orderExtendInfo.setLineNumber(String.valueOf(lineNumber));

				String orderDateTime = orderExtendInfo.getOrderDateTime();
				// OrderDateTime
				orderExtendInfo.setOrderDateTime(getLocalDateTime(orderDateTime, timeZone));
				// OrderDate
				orderExtendInfo.setOrderDate(getLocalDate(orderDateTime, timeZone));

				if ("Shipping".equals(orderExtendInfo.getSku())){
					// 运费的场合
					orderExtendInfo.setLongItemNumber(FREIGHT);

					// ShipDate（用物品的ShipDate设定 -> 已发货的订单 运费含ship_date）
					String shipDate = getLocalDate(orderExtendInfo.getShipDate(), timeZone);
					orderExtendInfo.setShipDate(shipDate);

					orderExtendInfo.setQuantityOrdered("1");
					orderExtendInfo.setQuantityShipped("1");

					float price = div2Digits(Float.valueOf(orderExtendInfo.getPricePerUnit()), rate);
					orderExtendInfo.setPrice(String.valueOf(price));

					// SalePrice
					orderExtendInfo.setPricePerUnit("0");

					// Promo Discount
					orderExtendInfo.setOrderDiscount("0");
				} else {
					// 物品的场合
					orderExtendInfo.setLongItemNumber("");

					// ShipDate
					String shipDate = getLocalDate(orderExtendInfo.getShipDate(), timeZone);
					orderExtendInfo.setShipDate(shipDate);

					// 售价含折扣 = （数量 * 单价）
					float price = div2Digits(Float.valueOf(skuPriceInfo.getPrice()), rate);
					orderExtendInfo.setPrice(String.valueOf(price));

					// 售价不含折扣
					String pricePerUnit = orderExtendInfo.getPricePerUnit();
					float unitPrice = div2Digits(Float.valueOf(pricePerUnit), rate);
					orderExtendInfo.setPricePerUnit(String.valueOf(unitPrice));

					// 订单折扣（明细折扣 + 订单折扣 = 原始价格 - 销售价格）
					float salePrice = mult2Digits(Float.valueOf(pricePerUnit), Float.valueOf(orderExtendInfo.getQuantityOrdered()));
					float orderDiscount = sub2Digits(salePrice, Float.valueOf(skuPriceInfo.getPrice()));
//					orderDiscount = mult2Digits(Float.valueOf(skuPriceInfo.getShipping_price()), -1);
					orderDiscount = mult2Digits(orderDiscount, -1);
					orderDiscount = div2Digits(orderDiscount, rate);
					orderExtendInfo.setOrderDiscount(String.valueOf(orderDiscount));
				}

				// 中文翻译
				translateOrderExtend(orderExtendInfo);

				lineNumber = lineNumber + 1;
			}

			ret = pushOrderList;
		} catch(Exception e) {
			ret = null;

			logger.error("getPushDailySalesList error", e);
			issueLog.log("PostBCBGOrderService.getPushDailySalesList",
					"getPushDailySalesList error",
					ErrorType.BatchJob,
					SubSystem.OMS);
		}
		return ret;
	}

	/**
	 * 取得Daily Sales文件用数据（Returned）
	 *
	 * @param orderChannelID 订单渠道
	 * @param timeZone 渠道时区
	 * @param exchangeRate 汇率
	 * @return		return[0]	执行结果
	 * 				return[1]	ReturnOrderDetail 数据 文件输出用
	 *				return[2]	ReturnOrderDetail 数据 发送标志更新用
	 */
	private ArrayList<Object> getPushDailySalesListForReturn(String orderChannelID, int timeZone, String exchangeRate) {
		ArrayList<Object> retArr = new ArrayList<Object>();

		// 执行结果
		boolean retRun = true;
		// CancelOrderDetail 数据 文件输出用
		List<OrderExtend> retForOutput = null;
		// CancelOrderDetail 数据 发送标志更新用
		List<OrderExtend> retForUpdate = null;

		ArrayList<String> searchDateList = getSearchDate(timeZone);

		// 检索日期取得
		// 开始检索日
		String beginSearchDate = searchDateList.get(0);
		// 终了检索日
		String endSearchDate = searchDateList.get(1);
			logger.info("getPushDailySalesListForReturn searchDate = " + beginSearchDate + " " + endSearchDate);

		try {
			// 订单数据取得 发送标志更新用
			retForUpdate = orderDao.getPushBCBGDailySalesInfoForReturnItems(orderChannelID, beginSearchDate, endSearchDate);

			// 订单数据取得
			List<OrderExtend> pushOrderList = orderDao.getPushBCBGDailySalesInfoForReturn(orderChannelID, beginSearchDate, endSearchDate);

			Float rate = Float.valueOf(exchangeRate);
			// 订单号
			String orderNumber = "";
			// 订单明细价格
			List<SetPriceBean> orderPriceDatas = new ArrayList<SetPriceBean>();
			// 订单单位连番
			int lineNumber = 0;

//			String shipDate = "";
			for (int i = 0; i < pushOrderList.size(); i++) {
				OrderExtend orderExtendInfo = pushOrderList.get(i);
				// SKU金额取得
				if (!orderNumber.equals(orderExtendInfo.getOrderNumber())) {
					orderPriceDatas = new ArrayList<SetPriceBean>();
					orderPriceDatas = SetPriceUtils.setPrice(orderExtendInfo.getOrderNumber(), orderExtendInfo.getOrderChannelId(), orderExtendInfo.getCartId(), 5);

					orderNumber = orderExtendInfo.getOrderNumber();
					lineNumber = 1;
//					shipDate = "";
				}
				SetPriceBean skuPriceInfo = getSKUPriceInfo(orderExtendInfo.getSku(), orderPriceDatas);

				// LineNumber
				orderExtendInfo.setLineNumber(String.valueOf(lineNumber));

				String orderDateTime = orderExtendInfo.getOrderDateTime();
				// OrderDateTime
				orderExtendInfo.setOrderDateTime(getLocalDateTime(orderDateTime, timeZone));
				// OrderDate
				orderExtendInfo.setOrderDate(getLocalDate(orderDateTime, timeZone));

				if ("Shipping".equals(orderExtendInfo.getSku())){
					// 运费的场合
					orderExtendInfo.setLongItemNumber(FREIGHT);

					// ShipDate（用物品的ShipDate设定 -> 已发货的订单 运费含ship_date）
					String shipDate = getLocalDate(orderExtendInfo.getShipDate(), timeZone);
					orderExtendInfo.setShipDate(shipDate);

					orderExtendInfo.setQuantityOrdered("-1");
					orderExtendInfo.setQuantityShipped("-1");

					float price = div2Digits(Float.valueOf(orderExtendInfo.getPricePerUnit()), rate);
					price = mult2Digits(price, -1);
					orderExtendInfo.setPrice(String.valueOf(price));

					// SalePrice
					orderExtendInfo.setPricePerUnit("0");

					// Promo Discount
					orderExtendInfo.setOrderDiscount("0");
				} else {
					// 物品的场合
					orderExtendInfo.setLongItemNumber("");

					// ShipDate
					String shipDate = getLocalDate(orderExtendInfo.getShipDate(), timeZone);
					orderExtendInfo.setShipDate(shipDate);

					orderExtendInfo.setQuantityOrdered("-" + orderExtendInfo.getQuantityOrdered());
					orderExtendInfo.setQuantityShipped("-" + orderExtendInfo.getQuantityShipped());

					// 售价含折扣 = （数量 * 单价） ItemAmount
					float price = div2Digits(Float.valueOf(skuPriceInfo.getPrice()), rate);
					price = mult2Digits(price, -1);
					orderExtendInfo.setPrice(String.valueOf(price));

					// 售价不含折扣
					String pricePerUnit = orderExtendInfo.getPricePerUnit();
					float unitPrice = div2Digits(Float.valueOf(pricePerUnit), rate);
					orderExtendInfo.setPricePerUnit(String.valueOf(unitPrice));

					// 订单折扣（明细折扣 + 订单折扣 = 原始价格 - 销售价格）
					float salePrice = mult2Digits(Float.valueOf(pricePerUnit), Float.valueOf(orderExtendInfo.getQuantityOrdered()));
					float orderDiscount = sub2Digits(salePrice, Float.valueOf("-" + skuPriceInfo.getPrice()));
					orderDiscount = mult2Digits(orderDiscount, -1);
					orderDiscount = div2Digits(orderDiscount, rate);
					orderExtendInfo.setOrderDiscount(String.valueOf(orderDiscount));
				}

				// 中文翻译
				translateOrderExtend(orderExtendInfo);

				lineNumber = lineNumber + 1;
			}

			retForOutput = pushOrderList;
		} catch(Exception e) {
			retRun = false;
			retForOutput = null;
			retForUpdate = null;

			logger.error("getPushDailySalesListForReturn error", e);
			issueLog.log("PostBCBGOrderService.getPushDailySalesListForReturn",
					"getPushDailySalesListForReturn error",
					ErrorType.BatchJob,
					SubSystem.OMS);
		}

		retArr.add(retRun);
		retArr.add(retForOutput);
		retArr.add(retForUpdate);

		return retArr;
	}

	/**
	 * 发货订单推送（Shipped）
	 *
	 * @param fileName 待上传文件名
	 */
	private boolean createUploadFileForDailySales(FtpBean ftpBean, String fileName, List<OrderExtend> pushOrderList, String createdDateTime, int kind) {
		boolean isSuccess = true;

		// 本地文件生成路径
		String uploadLocalPath = ftpBean.getUpload_localpath();
		try {

			File file = new File(uploadLocalPath + "/" + fileName);
			FileOutputStream  fop = new FileOutputStream(file, true);

			FileWriteUtils fileWriter = new FileWriteUtils(fop,  Charset.forName(outputFileEncoding), "A","S");

			// Body输出
			createUploadFileBodyForDailySales(fileWriter, pushOrderList, createdDateTime, kind);

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
	private void createUploadFileBodyForDailySales(FileWriteUtils fileWriter, List<OrderExtend> pushOrderList, String createdDateTime, int kind) throws IOException {

		for (int i = 0 ; i < pushOrderList.size(); i++) {
			OrderExtend orderInfo = pushOrderList.get(i);

			fileWriter.write(StoreNumber, DailySalesFileFormat.StoreNumber);
			fileWriter.write("", DailySalesFileFormat.CustomerID);
			fileWriter.write("", DailySalesFileFormat.OrderNumber);
			fileWriter.write(orderInfo.getSourceOrderId(), DailySalesFileFormat.WebOrderNumber);

			if (DAILY_SALES_FOR_SHIPPED == kind) {
				fileWriter.write("SO", DailySalesFileFormat.OrderType);
			} else {
				fileWriter.write("SR", DailySalesFileFormat.OrderType);
			}

			fileWriter.write(orderInfo.getOrderDate(), DailySalesFileFormat.OrderDate);
			fileWriter.write(orderInfo.getShipDate(), DailySalesFileFormat.ShipDate);
			fileWriter.write(orderInfo.getLineNumber(), DailySalesFileFormat.LineNumber);
			fileWriter.write(orderInfo.getLongItemNumber(), DailySalesFileFormat.LongItemNumber);
			fileWriter.write(orderInfo.getUPC(), DailySalesFileFormat.UPC);
			fileWriter.write(orderInfo.getStyle(), DailySalesFileFormat.Style);
			fileWriter.write(orderInfo.getColor(), DailySalesFileFormat.Color);
			fileWriter.write(orderInfo.getSize(), DailySalesFileFormat.Size);
			fileWriter.write(orderInfo.getQuantityOrdered(), DailySalesFileFormat.QuantityOrdered);
			fileWriter.write(orderInfo.getQuantityShipped(), DailySalesFileFormat.QuantityShipped);
			fileWriter.write(orderInfo.getMSRP(), DailySalesFileFormat.ListPrice);

			if ("Shipping".equals(orderInfo.getSku())){
				fileWriter.write("", DailySalesFileFormat.UnitPrice);
			} else {
				float unitPrice = div2Digits(Float.valueOf(orderInfo.getPrice()), Float.valueOf(orderInfo.getQuantityOrdered()));
				fileWriter.write(String.valueOf(unitPrice), DailySalesFileFormat.UnitPrice);
			}

			fileWriter.write(orderInfo.getPrice(), DailySalesFileFormat.ItemAmount);
			fileWriter.write("0.0", DailySalesFileFormat.TaxAmount);
			fileWriter.write(Carrier, DailySalesFileFormat.Carrier);

			if (DAILY_SALES_FOR_SHIPPED == kind) {
				fileWriter.write("", DailySalesFileFormat.ReasonCode);
			} else {
				if ("Shipping".equals(orderInfo.getSku())){
					fileWriter.write("", DailySalesFileFormat.ReasonCode);
				} else {
					fileWriter.write("105", DailySalesFileFormat.ReasonCode);
				}
			}

			fileWriter.write("", DailySalesFileFormat.ShipToAddressID);
			fileWriter.write(orderInfo.getShipName(), DailySalesFileFormat.ShipToName);

			ArrayList<String> shipAddressList = getBCBGAddress(orderInfo.getShipAddress(), orderInfo.getShipAddress2());
			fileWriter.write(shipAddressList.get(0), DailySalesFileFormat.ShipToAddress1);
			fileWriter.write(shipAddressList.get(1), DailySalesFileFormat.ShipToAddress2);

			fileWriter.write(orderInfo.getShipCity(), DailySalesFileFormat.ShipToCity);
			fileWriter.write(orderInfo.getShipState(), DailySalesFileFormat.ShipToState);
			fileWriter.write(orderInfo.getShipZip(), DailySalesFileFormat.ShipToZip);
			fileWriter.write(Country, DailySalesFileFormat.ShipToCountry);
			fileWriter.write(orderInfo.getShipPhone(), DailySalesFileFormat.ShipToPhone);
			fileWriter.write("", DailySalesFileFormat.BillToAddressID);
			fileWriter.write(orderInfo.getName(), DailySalesFileFormat.BillToName);

			ArrayList<String> billAddressList = getBCBGAddress(orderInfo.getAddress(), orderInfo.getAddress2());

			fileWriter.write(billAddressList.get(0), DailySalesFileFormat.BillToAddress1);
			fileWriter.write(billAddressList.get(1), DailySalesFileFormat.BillToAddress2);
			fileWriter.write(orderInfo.getCity(), DailySalesFileFormat.BillToCity);
			fileWriter.write(orderInfo.getState(), DailySalesFileFormat.BillToState);
			fileWriter.write(orderInfo.getZip(), DailySalesFileFormat.BillToZip);
			fileWriter.write(Country, DailySalesFileFormat.BillToCountry);
			fileWriter.write(orderInfo.getPhone(), DailySalesFileFormat.BillToPhone);
			fileWriter.write(PayType, DailySalesFileFormat.PaymentType);
			fileWriter.write(orderInfo.getPricePerUnit(), DailySalesFileFormat.SalePrice);

			float unitOrderDiscount = div2Digits(Float.valueOf(orderInfo.getOrderDiscount()), Float.valueOf(orderInfo.getQuantityOrdered()));
			if (DAILY_SALES_FOR_RETURNED == kind) {
				unitOrderDiscount = mult2Digits(unitOrderDiscount, -1);
			}

			if (unitOrderDiscount == 0) {
				fileWriter.write("", DailySalesFileFormat.Promo1);
			} else {
				fileWriter.write(Discount, DailySalesFileFormat.Promo1);
			}
			fileWriter.write(String.valueOf(unitOrderDiscount), DailySalesFileFormat.PromoDiscountAmount1);

			if (unitOrderDiscount == 0) {
				fileWriter.write("", DailySalesFileFormat.PromoDiscountDescription1);
			} else {
				fileWriter.write(Discount, DailySalesFileFormat.PromoDiscountDescription1);
			}

			fileWriter.write("", DailySalesFileFormat.Promo2);
			fileWriter.write("0.0", DailySalesFileFormat.PromoDiscountAmount2);
			fileWriter.write("", DailySalesFileFormat.PromoDiscountDescription2);
			fileWriter.write("", DailySalesFileFormat.Promo3);
			fileWriter.write("0.0", DailySalesFileFormat.PromoDiscountAmount3);
			fileWriter.write("", DailySalesFileFormat.PromoDiscountDescription3);
			fileWriter.write("", DailySalesFileFormat.Promo4);
			fileWriter.write("0.0", DailySalesFileFormat.PromoDiscountAmount4);
			fileWriter.write("", DailySalesFileFormat.PromoDescountDescription4);
			fileWriter.write("", DailySalesFileFormat.Promo5);
			fileWriter.write("0.0", DailySalesFileFormat.PromoDiscountAmount5);
			fileWriter.write("", DailySalesFileFormat.PromoDiscountDescription5);

			if (DAILY_SALES_FOR_SHIPPED == kind) {
				// Sellable
				fileWriter.write("45010", DailySalesFileFormat.InventoryCondition);
			} else {
				// Non-Sellable
				fileWriter.write("45015", DailySalesFileFormat.InventoryCondition);
			}

			fileWriter.write("", DailySalesFileFormat.AgentIDChannel);
			fileWriter.write(orderInfo.getTrackingNo(), DailySalesFileFormat.TrackingNo);
			fileWriter.write("", DailySalesFileFormat.OriginalOrderNumber);

			if (DAILY_SALES_FOR_SHIPPED == kind) {
				fileWriter.write("", DailySalesFileFormat.ReasonCodeDescription);
			} else {
				if ("Shipping".equals(orderInfo.getSku())){
					fileWriter.write("", DailySalesFileFormat.ReasonCodeDescription);
				} else {
					fileWriter.write("Do not like style", DailySalesFileFormat.ReasonCodeDescription);
				}
			}

			fileWriter.write(orderInfo.getOrderDateTime(), DailySalesFileFormat.WebOrderDateTime);
			fileWriter.write(createdDateTime, DailySalesFileFormat.DateTimeFileCreated);
			fileWriter.write("", DailySalesFileFormat.AOSStoreID);
			fileWriter.write("", DailySalesFileFormat.AOSEmployeeEIN);
			fileWriter.write("", DailySalesFileFormat.AOSEmployeeName);
			fileWriter.write("", DailySalesFileFormat.Source);
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
		if (allAddress.length() <= 40) {
			retAddress1 = allAddress;
		} else if (allAddress.length() > 40 && allAddress.length() <= 80) {
			retAddress1 = allAddress.substring(0,40);
			retAddress2 = allAddress.substring(40,allAddress.length());
		} else {
			retAddress1 = allAddress.substring(0,40);
			retAddress2 = allAddress.substring(40,80);
		}

		retAddress.add(retAddress1);
		retAddress.add(retAddress2);
		return retAddress;
	}

	/**
	 * DB更新
	 *
	 */
	private boolean updateOrdersInfo(String taskName, List<OrderExtend> pushOrderList, int updateKind) {
		boolean isSuccess = true;

		TransactionStatus status=transactionManager.getTransaction(def);
		try {
			String noteContent = "";

			// Shiped订单推送
			if (POST_BCBG_DAILY_SALES.equals(taskName)) {
				if (DAILY_SALES_FOR_SHIPPED == updateKind) {
					// Shipped Order的场合
					isSuccess = orderDao.updateOrderExtFlg1(POST_BCBG_DAILY_SALES, getSelectOrderNumberList(pushOrderList));
				} else {
					// Returned Order的场合
					isSuccess = updateReturnOrdersSendInfo(POST_BCBG_DAILY_SALES, pushOrderList);
				}
			// Not Shipped订单推送
			} else if (POST_BCBG_DEMAND.equals(taskName)) {
				if (DEMAND_FOR_APPROVED == updateKind) {
					// Approved Order的场合
					isSuccess = orderDao.updateOrdersSendInfo(POST_BCBG_DEMAND, getSelectOrderNumberList(pushOrderList));
				} else {
					// Cancel Order的场合
					isSuccess = updateExtFlag1OrdersSendInfo(POST_BCBG_DEMAND, pushOrderList);
				}
			}

			if (isSuccess) {
				String notesStr = getBatchNoteSqlData(pushOrderList, taskName, updateKind);

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

	private boolean updateExtFlag1OrdersSendInfo(String taskName, List<OrderExtend> pushOrderList) {
		boolean ret = true;

		for (int i = 0; i < pushOrderList.size(); i++) {
			OrderExtend orderExtend = pushOrderList.get(i);
			ret = orderDao.updateExtFlag1OrdersSendInfo(taskName, orderExtend.getOrderNumber(), orderExtend.getItemNumber());

			if (!ret) {
				break;
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
	private String getBatchNoteSqlData(List<OrderExtend> orderInfoList, String taskName, int updateKind) {
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
				sqlBuffer.append(prepareNotesData(orderInfo, entryDateTime, entryDateTime, taskName, updateKind));
			} else {
				sqlBuffer.append(Constants.COMMA_CHAR);
				sqlBuffer.append(prepareNotesData(orderInfo, entryDateTime, entryDateTime, taskName, updateKind));
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
	private String prepareNotesData(OrderExtend orderInfo, String entryDate, String entryTime, String taskName, int updateKind) {
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
			if (DAILY_SALES_FOR_SHIPPED == updateKind) {
				note = "Push Daily Sales File to BCBG(Shipped Order)";
			} else {
				note = "Push Daily Sales File to BCBG(Returned Order)";
			}
		// Not Shipped Order 推送
		} else if (POST_BCBG_DEMAND.equals(taskName)) {
			if (DEMAND_FOR_APPROVED == updateKind) {
				note = "Push Demand File to BCBG(Approved Order)";
			} else {
				note = "Push Demand File to BCBG(Cancelled Order)";
			}
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
//		int orderChannelTimeZone = Integer.valueOf(ChannelConfigs.getVal1(orderChannelID, ChannelConfigEnums.Name.channel_time_zone));
		int orderChannelTimeZone = 8;
		// 汇率
		String exchangeRate = Codes.getCodeName("MONEY_CHG_RMB", "USD");
		// 做成时间
		String createdDateTimeTmp = DateTimeUtil.getLocalTime(DateTimeUtil.getGMTTime(), orderChannelTimeZone);
		String createdDateTime = DateTimeUtil.format(DateTimeUtil.parse(createdDateTimeTmp), DateTimeUtil.DATE_TIME_FORMAT_2);

		// 推送文件名取得
		String fileName = createPostFileNameForDemands(orderChannelTimeZone);
		logger.info("createPostFileName Demands File = " + fileName);

		// 路径配置信息读取
		FtpBean upLoadFtpBean = formatDemandUploadFtpBean();
		upLoadFtpBean.setUpload_filename(fileName);

		// 每天生成一个文件
		if (FileUtils.isFileExist(fileName, upLoadFtpBean.getUpload_local_bak_path(), 10)) {
			return isSuccess;
		}

		// 订单数据抽出（正常订单 Approved）
		List<OrderExtend> pushDemandList = getPushDemandList(orderChannelID, orderChannelTimeZone, exchangeRate);

		// 订单数据抽出（正常订单 Canceled）
		List<Object> getPushDemandListForCancel = getPushDemandListForCancel(orderChannelID, orderChannelTimeZone, exchangeRate);
		boolean retCancel = (boolean)getPushDemandListForCancel.get(0);
		List<OrderExtend> retCancelForOutputList = (List<OrderExtend>)getPushDemandListForCancel.get(1);
		List<OrderExtend> retCancelForUpdateList = (List<OrderExtend>)getPushDemandListForCancel.get(2);

		// 正常订单有数据的场合 && Cancel订单有数据的场合
		if (pushDemandList != null &&	retCancel &&
				(pushDemandList.size() > 0 || retCancelForOutputList.size() > 0)) {
			// CSV文件做成 Approved
			if (isSuccess && pushDemandList.size() > 0) {
				logger.info("createUploadFile Demands Approved record count = " + pushDemandList.size());
				isSuccess = createUploadFileForDemands(upLoadFtpBean, fileName, pushDemandList, createdDateTime, DEMAND_FOR_APPROVED);
			}
			// CSV文件做成 Canceled
			if (isSuccess && retCancelForOutputList.size() > 0) {
				logger.info("createUploadFile Demands Cancel record count = " + retCancelForOutputList.size());
				isSuccess = createUploadFileForDemands(upLoadFtpBean, fileName, retCancelForOutputList, createdDateTime, DEMAND_FOR_CANCELLED);
			}

			// FTP目录夹Copy推送
			if (isSuccess) {
				logger.info("uploadOrderFile");
				isSuccess = uploadOrderFile(upLoadFtpBean, fileName);
			}

			// 推送标志更新
			if (isSuccess) {

				if (pushDemandList.size() > 0) {
					logger.info("updateDemandSendInfo Approved Order");
					isSuccess = updateOrdersInfo(POST_BCBG_DEMAND, pushDemandList, DEMAND_FOR_APPROVED);
				}

				if (isSuccess && retCancelForUpdateList.size() > 0) {
					logger.info("updateDemandSendInfo Cancelled Order");
					isSuccess = updateOrdersInfo(POST_BCBG_DEMAND, retCancelForUpdateList, DEMAND_FOR_CANCELLED);
				}

				if (isSuccess) {
					// 源文件
					String srcFile = upLoadFtpBean.getUpload_localpath() + "/" + upLoadFtpBean.getUpload_filename();
					// 目标文件
					String destFile = upLoadFtpBean.getUpload_local_bak_path() + "/" + upLoadFtpBean.getUpload_filename();
					logger.info("moveFile = " + srcFile + " " + destFile);
					FileUtils.moveFile(srcFile, destFile);

				} else {
					logger.info("updateDemandSendInfo error");
					issueLog.log("postBCBGDemands.updateDemandSendInfo",
							"updateDemandSendInfo error;Push order file = " + fileName,
							ErrorType.BatchJob,
							SubSystem.OMS);
				}
			} else {
				logger.info("uploadOrderFile error;Push demand file = " + fileName);
				issueLog.log("postBCBGDemand.uploadOrderFile",
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

//				// OrderDate
//				orderExtendInfo.setOrderDate(getLocalDate(orderExtendInfo.getOrderDateTime(), timeZone));
				orderExtendInfo.setOrderDateTime(getLocalDateTime(orderExtendInfo.getOrderDateTime(), timeZone));

				if ("Shipping".equals(orderExtendInfo.getSku())) {
					// 运费的场合
					orderExtendInfo.setLongItemNumber(FREIGHT);

					orderExtendInfo.setQuantityOrdered("1");
					// 未发货
					orderExtendInfo.setQuantityShipped("0");

					float price = div2Digits(Float.valueOf(orderExtendInfo.getPricePerUnit()), rate);
					orderExtendInfo.setPrice(String.valueOf(price));

					// SalePrice
					orderExtendInfo.setPricePerUnit("0");

					// Promo Discount
					orderExtendInfo.setOrderDiscount("0");
				} else {
					// 物品的场合
					orderExtendInfo.setLongItemNumber("");
					// 未发货
					orderExtendInfo.setQuantityShipped("0");

					// 售价含折扣
					float price = div2Digits(Float.valueOf(skuPriceInfo.getPrice()), rate);
					orderExtendInfo.setPrice(String.valueOf(price));

					// 售价不含折扣
					String pricePerUnit = orderExtendInfo.getPricePerUnit();
					float unitPrice = div2Digits(Float.valueOf(pricePerUnit), rate);
					orderExtendInfo.setPricePerUnit(String.valueOf(unitPrice));

					// 订单折扣
//					float orderDiscount = mult2Digits(Float.valueOf(skuPriceInfo.getShipping_price()), -1);
//					orderDiscount = div2Digits(orderDiscount, rate);
//					orderExtendInfo.setOrderDiscount(String.valueOf(orderDiscount));

					// 订单折扣（明细折扣 + 订单折扣 = 原始价格 - 销售价格）
					float salePrice = mult2Digits(Float.valueOf(pricePerUnit), Float.valueOf(orderExtendInfo.getQuantityOrdered()));
					float orderDiscount = sub2Digits(salePrice, Float.valueOf(skuPriceInfo.getPrice()));
					orderDiscount = mult2Digits(orderDiscount, -1);
					orderDiscount = div2Digits(orderDiscount, rate);
					orderExtendInfo.setOrderDiscount(String.valueOf(orderDiscount));
				}

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
	 * 取得Demand文件用数据（Cancel Order）
	 *
	 * @param orderChannelID 订单渠道
	 * @param timeZone 渠道时区
	 * @param exchangeRate 汇率
	 * @return		return[0]	执行结果
	 * 				return[1]	CancelOrderDetail 数据 文件输出用
	 *				return[2]	CancelOrderDetail 数据 发送标志更新用
	 */
	private ArrayList<Object> getPushDemandListForCancel(String orderChannelID, int timeZone, String exchangeRate) {
		ArrayList<Object> retArr = new ArrayList<Object>();

		// 执行结果
		boolean retRun = true;
		// CancelOrderDetail 数据 文件输出用
		List<OrderExtend> retForOutput = null;
		// CancelOrderDetail 数据 发送标志更新用
		List<OrderExtend> retForUpdate = null;

		ArrayList<String> searchDateList = getSearchDate(timeZone);

		// 检索日期取得
		// 开始检索日
		String beginSearchDate = searchDateList.get(0);
		// 终了检索日
		String endSearchDate = searchDateList.get(1);
		logger.info("getPushDemandListForCancel searchDate = " + beginSearchDate + " " + endSearchDate);

		try {
			// 订单数据取得 发送标志更新用
			retForUpdate = orderDao.getPushBCBGDemandsInfoForCancelItems(orderChannelID, beginSearchDate, endSearchDate);

			// 订单数据取得 文件输出用
			List<OrderExtend> pushOrderList = orderDao.getPushBCBGDemandsInfoForCancel(orderChannelID, beginSearchDate, endSearchDate);

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
					orderPriceDatas = SetPriceUtils.setPrice(orderExtendInfo.getOrderNumber(), orderExtendInfo.getOrderChannelId(), orderExtendInfo.getCartId(), 5);

					orderNumber = orderExtendInfo.getOrderNumber();

					lineNumber = 1;
				}
				SetPriceBean skuPriceInfo = getSKUPriceInfo(orderExtendInfo.getSku(), orderPriceDatas);

				// LineNumber
				orderExtendInfo.setLineNumber(String.valueOf(lineNumber));

//				// OrderDate
//				orderExtendInfo.setOrderDate(getLocalDate(orderExtendInfo.getOrderDateTime(), timeZone));
				orderExtendInfo.setOrderDateTime(getLocalDateTime(orderExtendInfo.getOrderDateTime(), timeZone));

				if ("Shipping".equals(orderExtendInfo.getSku())) {
					// 运费的场合
					orderExtendInfo.setLongItemNumber(FREIGHT);

					orderExtendInfo.setQuantityOrdered("-1");
					// 未发货
					orderExtendInfo.setQuantityShipped("0");

					float price = div2Digits(Float.valueOf(orderExtendInfo.getPricePerUnit()), rate);
//					price = mult2Digits(price, -1);
					orderExtendInfo.setPrice(String.valueOf(price));

					// SalePrice
					orderExtendInfo.setPricePerUnit("0");

					// Promo Discount
					orderExtendInfo.setOrderDiscount("0");
				} else {
					// 物品的场合
					orderExtendInfo.setLongItemNumber("");

					orderExtendInfo.setQuantityOrdered("-" + orderExtendInfo.getQuantityOrdered());
					// 未发货
					orderExtendInfo.setQuantityShipped("0");

					// 售价含折扣 ItemAmount
					float price = div2Digits(Float.valueOf(skuPriceInfo.getPrice()), rate);
					price = mult2Digits(price, -1);
					orderExtendInfo.setPrice(String.valueOf(price));

					// 售价不含折扣 SalePrice
					String pricePerUnit = orderExtendInfo.getPricePerUnit();
					float unitPrice = div2Digits(Float.valueOf(pricePerUnit), rate);
					orderExtendInfo.setPricePerUnit(String.valueOf(unitPrice));

					// 订单折扣（明细折扣 + 订单折扣 = 原始价格 - 销售价格）
					// Promo Dicscount Amount 1
					float salePrice = mult2Digits(Float.valueOf(pricePerUnit), Float.valueOf(orderExtendInfo.getQuantityOrdered()));
					float orderDiscount = sub2Digits(salePrice, Float.valueOf("-" + skuPriceInfo.getPrice()));
					orderDiscount = mult2Digits(orderDiscount, -1);
					orderDiscount = div2Digits(orderDiscount, rate);
					orderExtendInfo.setOrderDiscount(String.valueOf(orderDiscount));
				}

				lineNumber = lineNumber + 1;
			}

			retForOutput = pushOrderList;
		} catch(Exception e) {
			retRun = false;
			retForUpdate = null;
			retForOutput = null;

			logger.error("getPushDemandListForCancel error", e);
			issueLog.log("postBCBGDemands.getPushDemandListForCancel",
					"getPushDemandListForCancel error",
					ErrorType.BatchJob,
					SubSystem.OMS);
		}

		retArr.add(retRun);
		retArr.add(retForOutput);
		retArr.add(retForUpdate);

		return retArr;
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
	 * 检索时间（北京时间 当天24点，前七天的数据，通过定时器设定24点启动）     //检索时间（美国时间，前一天的数据）
	 *
	 * @param timeZone 本地时区
	 */
	private ArrayList<String> getSearchDate(int timeZone) {
		ArrayList<String> retSearchDate = new ArrayList<String>();

//		String localTime = DateTimeUtil.getLocalTime(DateTimeUtil.getNow(), timeZone);
//		Date searchDate = DateTimeUtil.addDays(DateTimeUtil.parse(localTime), -1);
//		String searchDateString =  DateTimeUtil.format(searchDate, DateTimeUtil.DEFAULT_DATE_FORMAT);
//		String startSearchDate = searchDateString + " 00:00:00";
//		String endSearchDate = searchDateString + " 23:59:59";
//
//		String startSearchDateGMT = DateTimeUtil.getGMTTime(startSearchDate, timeZone);
//		String endSearchDateGMT = DateTimeUtil.getGMTTime(endSearchDate, timeZone);
//
//		retSearchDate.add(startSearchDateGMT);
//		retSearchDate.add(endSearchDateGMT);


		String startSearchDateGMT = "";
		String endSearchDateGMT = "";
		String now = "";

		now = DateTimeUtil.getNow();

		// 前七天
		Date startDate = DateTimeUtil.addDays(DateTimeUtil.parse(now), -7);
		String startDateString =  DateTimeUtil.format(startDate, DateTimeUtil.DEFAULT_DATE_FORMAT);
		startSearchDateGMT = startDateString + " 00:00:00";
		endSearchDateGMT = now;

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
	private boolean createUploadFileForDemands(FtpBean ftpBean, String fileName, List<OrderExtend> pushOrderList, String createdDateTime, int kind) {
		boolean isSuccess = true;

		// 本地文件生成路径
		String uploadLocalPath = ftpBean.getUpload_localpath();
		try {

			File file = new File(uploadLocalPath + "/" + fileName);
			FileOutputStream  fop = new FileOutputStream(file, true);

			FileWriteUtils fileWriter = new FileWriteUtils(fop,  Charset.forName(outputFileEncoding), "A","S");

			// Body输出
			createUploadFileBodyForDemands(fileWriter, pushOrderList, createdDateTime, kind);

			fileWriter.flush();
			fileWriter.close();

		} catch (Exception e) {
			isSuccess = false;

			logger.error("createUploadFileForDemands", e);

			issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);
		}

		return isSuccess;
	}

	/**
	 * 未发货订单做成
	 *
	 * @param fileWriter
	 * @param pushOrderList
	 * @param createdDateTime
	 */
	private void createUploadFileBodyForDemands(FileWriteUtils fileWriter, List<OrderExtend> pushOrderList, String createdDateTime, int kind) throws IOException {

		for (int i = 0 ; i < pushOrderList.size(); i++) {
			OrderExtend orderInfo = pushOrderList.get(i);
			fileWriter.write(StoreNumber, DemandsFileFormat.StoreNumber);
			fileWriter.write("", DemandsFileFormat.OrderNumber);
			fileWriter.write(orderInfo.getLineNumber(), DemandsFileFormat.LineNumber);
			fileWriter.write(orderInfo.getOrderDateTime(), DemandsFileFormat.OrderDate);
			fileWriter.write(orderInfo.getSourceOrderId(), DemandsFileFormat.WebOrderNumber);
			fileWriter.write("Customer", DemandsFileFormat.AgentIDChannel);
			fileWriter.write(orderInfo.getLongItemNumber(), DemandsFileFormat.LongItemNumber);
			fileWriter.write(orderInfo.getUPC(), DemandsFileFormat.UPC);
			fileWriter.write(orderInfo.getPricePerUnit(), DemandsFileFormat.SalePrice);
			fileWriter.write(orderInfo.getQuantityOrdered(), DemandsFileFormat.QuantityOrdered);
			fileWriter.write(orderInfo.getQuantityShipped(), DemandsFileFormat.QuantityShipped);
			fileWriter.write("", DemandsFileFormat.POBOQuantity);
			fileWriter.write(DummyDate, DemandsFileFormat.PromiseDate);

			if ("Shipping".equals(orderInfo.getSku())) {
				fileWriter.write(orderInfo.getPrice(), DemandsFileFormat.UnitPrice);
			} else {
				float unitPrice = div2Digits(Float.valueOf(orderInfo.getPrice()), Float.valueOf(orderInfo.getQuantityOrdered()));
				fileWriter.write(String.valueOf(unitPrice), DemandsFileFormat.UnitPrice);
//				fileWriter.write(orderInfo.getPrice(), DemandsFileFormat.UnitPrice);
			}

			fileWriter.write("0.0", DemandsFileFormat.TaxAmount);
			float unitOrderDiscount = div2Digits(Float.valueOf(orderInfo.getOrderDiscount()), Float.valueOf(orderInfo.getQuantityOrdered()));
			if (DEMAND_FOR_CANCELLED == kind) {
				unitOrderDiscount = mult2Digits(unitOrderDiscount, -1);
			}

			if (unitOrderDiscount == 0) {
				fileWriter.write("", DemandsFileFormat.Promo1);
			} else {
				fileWriter.write(Discount, DemandsFileFormat.Promo1);
			}
			fileWriter.write(String.valueOf(unitOrderDiscount), DemandsFileFormat.PromoDiscountAmount1);

			if (unitOrderDiscount == 0) {
				fileWriter.write("", DemandsFileFormat.PromoDiscountDescription1);
			} else {
				fileWriter.write(Discount, DemandsFileFormat.PromoDiscountDescription1);
			}

			fileWriter.write("", DemandsFileFormat.Promo2);
			fileWriter.write("0.0", DemandsFileFormat.PromoDiscountAmount2);
			fileWriter.write("", DemandsFileFormat.PromoDiscountDescription2);
			fileWriter.write("", DemandsFileFormat.Promo3);
			fileWriter.write("0.0", DemandsFileFormat.PromoDiscountAmount3);
			fileWriter.write("", DemandsFileFormat.PromoDiscountDescription3);
			fileWriter.write("", DemandsFileFormat.Promo4);
			fileWriter.write("0.0", DemandsFileFormat.PromoDiscountAmount4);
			fileWriter.write("", DemandsFileFormat.PromoDiscountDescription4);
			fileWriter.write("", DemandsFileFormat.Promo5);
			fileWriter.write("0.0", DemandsFileFormat.PromoDiscountAmount5);
			fileWriter.write("", DemandsFileFormat.PromoDiscountDescription5);
			fileWriter.write("IP", DemandsFileFormat.HoldCode);
			fileWriter.write("In Process", DemandsFileFormat.HoldCodeDescription);
			fileWriter.write(createdDateTime, DemandsFileFormat.DateTimeFileCreated);
			fileWriter.write(orderInfo.getSourceOrderId(), DemandsFileFormat.BorderFreeOrderNO);
			fileWriter.write("", DemandsFileFormat.AOSEINNO);
			fileWriter.write("", DemandsFileFormat.AOSStoreNO);
			fileWriter.write("", DemandsFileFormat.CustomerEmailAddress);

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
		private static final String StoreNumber = "S,5";
		private static final String OrderNumber = "S,8";
		private static final String LineNumber = "S,6";
		private static final String OrderDate = "A,14";
		private static final String WebOrderNumber = "A,25";
		private static final String AgentIDChannel = "A,10";
		private static final String LongItemNumber = "A,25";
		private static final String UPC = "A,14";
		private static final String SalePrice = "S,15.2";
		private static final String QuantityOrdered = "S,15";
		private static final String QuantityShipped = "S,15";
		private static final String POBOQuantity = "S,15";
		private static final String PromiseDate = "S,8";
		private static final String UnitPrice = "S,15.2";
		private static final String TaxAmount = "S,15.2";
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
		private static final String PromoDiscountDescription4 = "A,20";
		private static final String Promo5 = "A,12";
		private static final String PromoDiscountAmount5 = "S,15.2";
		private static final String PromoDiscountDescription5 = "A,20";
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

	private void translateOrderExtend(OrderExtend orderInfo) throws Exception {
		ArrayList<String> translateContent = new ArrayList<String>();

		translateContent.add(orderInfo.getShipName());
		translateContent.add(orderInfo.getShipAddress());
		translateContent.add(orderInfo.getShipAddress2());
		translateContent.add(orderInfo.getShipCity());
		translateContent.add(orderInfo.getShipState());

		translateContent.add(orderInfo.getName());
		translateContent.add(orderInfo.getAddress());
		translateContent.add(orderInfo.getAddress2());
		translateContent.add(orderInfo.getCity());
		translateContent.add(orderInfo.getState());

//		List<String> afterTranslateContent = translate(translateContent, 1);
		List<String> afterTranslateContent = BaiduTranslateUtil.translate(translateContent);

		orderInfo.setShipName(afterTranslateContent.get(0));
		orderInfo.setShipAddress(afterTranslateContent.get(1));
		orderInfo.setShipAddress2(afterTranslateContent.get(2));
		orderInfo.setShipCity(afterTranslateContent.get(3));
		orderInfo.setShipState(afterTranslateContent.get(4));

		orderInfo.setName(afterTranslateContent.get(5));
		orderInfo.setAddress(afterTranslateContent.get(6));
		orderInfo.setAddress2(afterTranslateContent.get(7));
		orderInfo.setCity(afterTranslateContent.get(8));
		orderInfo.setState(afterTranslateContent.get(9));
	}

	private List<String> translate(List<String> beforeStringList, int threadNo) throws Exception {
		List<String> resultStrList = new ArrayList<String>();

		StringBuilder url = new StringBuilder(Codes.getCodeName(BAIDU_TRANSLATE_CONFIG, "Url"));
		String apiKey = Codes.getCodeName(BAIDU_TRANSLATE_CONFIG, "ApiKey" + threadNo);
		String from = Codes.getCodeName(BAIDU_TRANSLATE_CONFIG, "From");
		String to = Codes.getCodeName(BAIDU_TRANSLATE_CONFIG, "To");

		StringBuilder q = new StringBuilder("q=");
		if (beforeStringList != null && beforeStringList.size() > 0) {
			String qSplit = URLEncoder.encode("\n", "utf-8");
			int qSize = beforeStringList.size();
			for (int i = 0; i < qSize; i++) {
				if (i == 0) {
					q.append(URLEncoder.encode(beforeStringList.get(i), "utf-8"));
				} else {
					q.append(qSplit);
					q.append(URLEncoder.encode(beforeStringList.get(i), "utf-8"));
				}
			}
		} else {
			return resultStrList;
		}

//		url.append("?");
//		url.append("client_id=");
//		url.append(apiKey);
//		url.append("&");
//		url.append(q.toString());
//		url.append("&");
//		url.append("from=");
//		url.append(from);
//		url.append("&");
//		url.append("to=");
//		url.append(to);

		StringBuilder urlPara = new StringBuilder();
		urlPara.append("client_id=");
		urlPara.append(apiKey);
		urlPara.append("&");
		urlPara.append(q.toString());
		urlPara.append("&");
		urlPara.append("from=");
		urlPara.append(from);
		urlPara.append("&");
		urlPara.append("to=");
		urlPara.append(to);

		logger.info(url.toString());

//		String transResult = WebServiceUtil.getByUrl(url.toString());
		String transResult = HttpUtils.get(url.toString(),urlPara.toString());

		Map<String, Object> jsonToMap = JsonUtil.jsonToMap(transResult);
		// 百度翻译API服务发生错误
		if (jsonToMap.containsKey("error_code")) {
			Object error_code = jsonToMap.get("error_code");
			Object error_msg = jsonToMap.get("error_msg");
			issueLog.log("PostBCBGOrderService.translate",
					"百度翻译API服务发生错误。error_code：" + error_code + " error_msg：" + error_msg,
					ErrorType.BatchJob,
					SubSystem.OMS);

		} else {
			Object trans_result = jsonToMap.get("trans_result");
			List<Map> mapList = (List<Map>) trans_result;
			if (mapList != null && mapList.size() > 0) {
				for (int i = 0; i < mapList.size(); i++) {
					Map<String, String> map = mapList.get(i);
					String src = map.get("src");
					String dst = map.get("dst");
					logger.info("src:" + src + " -> dst:" + dst);
					resultStrList.add(dst);
				}
			}
		}

		return resultStrList;
	}

//	public static void main(String[] args) throws Exception {
////		String temp = "中国人1";
////
////		System.out.println(temp.length());
////		System.out.println(temp.getBytes().length);
////		Charset charset = Charset.forName("gbk");
////		System.out.println(temp.getBytes(charset).length);
//
////		float a = Float.valueOf("6.2");
////		float price = div2Digits(Float.valueOf("1049.00"), a);
////		System.out.println(price);
//
//		String inputString = "1.2";
//		String numericLength = "10";
//		String outputString = "";
//
//		if (inputString.contains(".")) {
//			double numericDouble = Double.parseDouble(inputString);
//			DecimalFormat df = new DecimalFormat("#.00");
//			outputString = StringUtils.lPad(df.format(numericDouble), "0", Integer.valueOf(numericLength));
//		} else {
//			outputString = StringUtils.lPad(inputString, "0", Integer.valueOf(numericLength));
//		}
//
//		System.out.println(outputString);

//		String localTime = DateTimeUtil.getLocalTime("2015-11-11 16:00:00", 8);
//		Date localTimeForDate = DateTimeUtil.parse(localTime);
//
//		String date = DateTimeUtil.format(localTimeForDate, DateTimeUtil.DATE_TIME_FORMAT_3);
//		String time = DateTimeUtil.format(localTimeForDate, DateTimeUtil.DATE_TIME_FORMAT_4);
//
//		System.out.println(String.format("DailySales_VO_BMX_%s_%s.dat", date, time));
//	}
}
