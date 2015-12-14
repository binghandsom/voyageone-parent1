package com.voyageone.batch.oms.service;

import com.google.gson.Gson;
import com.jcraft.jsch.ChannelSftp;
import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.Constants;
import com.voyageone.batch.core.modelbean.SetPriceBean;
import com.voyageone.batch.core.util.SetPriceUtils;
import com.voyageone.batch.oms.OmsConstants;
import com.voyageone.batch.oms.dao.OrderDao;
import com.voyageone.batch.oms.dao.ReservationDao;
import com.voyageone.batch.oms.formbean.InFormFile;
import com.voyageone.batch.oms.modelbean.OrderExtend;
import com.voyageone.common.components.baidu.translate.BaiduTranslateUtil;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.sears.SearsService;
import com.voyageone.common.components.sears.bean.*;
import com.voyageone.common.configs.*;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.beans.FtpBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.mail.Mail;
import com.voyageone.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PostSearsOrderService {
	
	private static Log logger = LogFactory.getLog(PostSearsOrderService.class);
	
	@Autowired
	private OrderDao orderDao;

	@Autowired
	private ReservationDao reservationDao;
	
	@Autowired
	IssueLog issueLog;

	@Autowired
	SearsService searsService;

	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	private DefaultTransactionDefinition def = new DefaultTransactionDefinition();

	// taskName
	// 正常订单推送
	private final static String POST_SEARS_CREATE_ORDER = "PostSearsCreateOrder";

	// Sears渠道ID
	private String orderChannelID = "013";
//	private String orderChannelID = "001";

	//	异常邮件区分
	//	订单推送异常
	private int MAIL_PUSH_ORDER_ERROR = 1;
	//	超卖异常
	private int MAIL_OUT_OF_STOCK_ERROR = 2;
	//	部分取消异常
	private int MAIL_DIFFERENT_STATUS_ERROR = 3;

	private String DUMMY_LAST_NAME = "name";
	private String SEARS_MAIL_ADDRESS = "sears@voyageone.cn";
	private String COMPANY_PHONE = "5626771997";
	/**
	 * 调用Sears(Create Order)
	 *
	 */
	public boolean postSearsCreateOrder() {
		logger.info("postSearsCreateOrder start");

		boolean isSuccess = true;
		int orderChannelTimeZone = 8;
		String exchangeRate = "6.2";

		// 推送异常订单一览
		List<OrderResponse> pushErrorOrderList = new ArrayList<OrderResponse>();

		logger.info("	getPushCreateList");
		List<OrderExtend> pushDailySalesListList = getPushCreateList(orderChannelID, orderChannelTimeZone, exchangeRate);

		for (OrderExtend orderExtendInfo:pushDailySalesListList) {
			OrderBean createOrderInfo = orderExtendInfo.getOrderOutputInfo();

//			for (OrderItem item : createOrderInfo.getItems()) {
//
//				if (item.getItemId().equals("04935666000")) {
//					item.setItemPrice((float)45.22);
//				}
//				else if (item.getItemId().equals("00603040000")) {
//					item.setItemPrice((float)11.89);
//				}
//				else if (item.getItemId().equals("00631715000")) {
//					item.setItemPrice((float)29.74);
//				}
//				else if (item.getItemId().equals("00627225000")) {
//					item.setItemPrice((float)23.79);
//				}
//				else if (item.getItemId().equals("00633733000")) {
//					item.setItemPrice((float)23.79);
//				}
//				else if (item.getItemId().equals("00823164000")) {
//					item.setItemPrice((float)23.79);
//				}
//				else if (item.getItemId().equals("00823307000")) {
//					item.setItemPrice((float)29.74);
//				}
//				else if (item.getItemId().equals("00856234000")) {
//					item.setItemPrice((float)35.69);
//				}
//				else if (item.getItemId().equals("00615643000")) {
//					item.setItemPrice((float)11.89);
//				}
//				else if (item.getItemId().equals("00826038000")) {
//					item.setItemPrice((float)154.63);
//				}
//				else if (item.getItemId().equals("04909847000")) {
//					item.setItemPrice((float)16.66);
//				}
//				else if (item.getItemId().equals("04918897000")) {
//					item.setItemPrice((float)190.39);
//				}
//
//			}

			// 请求XML缓存
			backupTheXmlFile(createOrderInfo.getOrderReference(), JaxbUtil.convertToXml(createOrderInfo), 0);

			try {

				OrderLookupsResponse orderLookupsResponse = null;
				OrderResponse orderResponse = new OrderResponse();

				// Sears Order Lookup接口请求
				logger.info("	searsService.OrderLookup sourceOrderId = " + createOrderInfo.getOrderReference() + ",orderNumber = " + orderExtendInfo.getOrderNumber());
				orderLookupsResponse = searsService.getOrderInfoByOrderReference(createOrderInfo.getOrderReference());

				// 相应XML缓存
				backupTheXmlFile(createOrderInfo.getOrderReference(), JaxbUtil.convertToXml(orderLookupsResponse), 2);

				if (orderLookupsResponse == null || orderLookupsResponse.getOrder() == null){
					// Sears Create Order接口请求
					logger.info("	searsService.CreateOrder sourceOrderId = " + createOrderInfo.getOrderReference() + ",orderNumber = " + orderExtendInfo.getOrderNumber());
					orderResponse = searsService.CreateOrder(createOrderInfo);

					// 相应XML缓存
					backupTheXmlFile(createOrderInfo.getOrderReference(), orderResponse.getMessage(), 1);
				} else {
					orderResponse.setMessage("Succeed");
					orderResponse.setOrderId(orderLookupsResponse.getOrder().get(0).getOrderId());
					logger.info("	searsService.OrderLookup 订单已经推送过 orderId = " + orderResponse.getOrderId());
				}

				//	第三方订单号更新
				if ("Succeed".equals(orderResponse.getMessage())) {
					logger.info("	searsService.CreateOrder 订单推送成功 orderId = " + orderResponse.getOrderId());
					isSuccess = updateOrder(orderExtendInfo.getOrderNumber(), orderResponse.getOrderId(), POST_SEARS_CREATE_ORDER);

					if (!isSuccess) {
						issueLog.log("postSearsCreateOrder.updateOrder",
								"UpdateOrder error ; OrderNumber = " + createOrderInfo.getOrderReference(),
								ErrorType.BatchJob,
								SubSystem.OMS);
					}
				} else {
					logger.info("	searsService.CreateOrder 订单推送失败：" + orderResponse.getMessage());
					// 异常的场合，订单号再设定
					orderResponse.setOrderId(createOrderInfo.getOrderReference());
					pushErrorOrderList.add(orderResponse);
				}
			} catch (Exception e) {
				isSuccess = false;
				logger.error("postSearsCreateOrder.CreateOrder error", e);

				issueLog.log("postSearsCreateOrder.CreateOrder",
						"CreateOrder error ; OrderNumber = " + createOrderInfo.getOrderReference(),
						ErrorType.BatchJob,
						SubSystem.OMS,e.toString());
			}
		}

		// 推送异常订单，客服邮件通知
		if (pushErrorOrderList.size() > 0) {
			// 由于会重复发送，感觉给客服不妥，暂时先记入IssueLog（在订单查询任务中再将取消、超卖邮件发给客服）
//			sendCustomerServiceMail(pushErrorOrderList, MAIL_PUSH_ORDER_ERROR);
			for (OrderResponse errorOrderResponse : pushErrorOrderList) {
				issueLog.log("postSearsCreateOrder.CreateOrder",
						"CreateOrder error ; SourceOrderID = " + errorOrderResponse.getOrderId(),
						ErrorType.BatchJob,
						SubSystem.OMS,errorOrderResponse.getMessage());
			}

		}

		logger.info("postSearsCreateOrder end");

		return isSuccess;
	}

	/**
	 * 推送异常订单客服邮件通知
	 * @param pushErrorOrderList 异常一览订单列表
	 * @param kind 异常种类
	 *
	 */
	private void sendCustomerServiceMail(List<OrderResponse> pushErrorOrderList, int kind) {

		try {
			if (pushErrorOrderList.size() > 0) {

				// 邮件标题取得
				String mailSubject = "";
				if (MAIL_PUSH_ORDER_ERROR == kind) {
					mailSubject = OmsConstants.SEARS_PUSH_ORDER_ERROR_SUBJECT;
				} else {
					if (MAIL_OUT_OF_STOCK_ERROR == kind) {
						mailSubject = OmsConstants.SEARS_OUT_OF_STOCK_CHECK_SUBJECT;
					} else {
						if (MAIL_DIFFERENT_STATUS_ERROR == kind) {
							mailSubject = OmsConstants.SEARS_PARTIAL_CANCELLED_CHECK_SUBJECT;
						}
					}
				}

				StringBuilder tbody = new StringBuilder();

				for (int i = 0; i < pushErrorOrderList.size(); i++) {
					OrderResponse orderResponse = pushErrorOrderList.get(i);
					// 邮件每行正文
					String mailTextLine =
							String.format(OmsConstants.PATERN_TABLE_REASON_ROW, orderResponse.getOrderId(), orderResponse.getMessage());
					tbody.append(mailTextLine);
				}

				// 拼接table
				String body = String.format(OmsConstants.PATERN_TABLE_REASON, mailSubject, tbody.toString());

				// 拼接邮件正文
				StringBuilder emailContent = new StringBuilder();
				emailContent.append(Constants.EMAIL_STYLE_STRING).append(body);

				// TODO 邮件组变更
//				Mail.sendAlert("ITOMS", OmsConstants.RM_OUT_OF_STOCK_CHECK_SUBJECT, getArrayListString(sourceOrderIdList), true);
//				Mail.sendAlert("CUSTOMER_SERVICE_REALMADRID", mailSubject, emailContent.toString(), true);
				logger.info("	Sears订单推送错误邮件发送");
				Mail.sendAlert(CodeConstants.EmailReceiver.NEED_SOLVE, mailSubject, emailContent.toString(), true);
			}
		} catch (Exception e) {

			logger.error("sendCustomerServiceMail", e);

			issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS, "postSearsCreateOrder.sendCustomerServiceMail error");
		}
	}

	/**
	 * 推送订单信息取得
	 *
	 */
	private List<OrderExtend> getPushCreateList(String orderChannelID, int timeZone, String exchangeRate) {
//		List<OrderBean> ret = new ArrayList<OrderBean>();

		List<OrderExtend> orderInfoFromDB = new ArrayList<>();
		try {
			// 订单信息取得
			orderInfoFromDB =orderDao.getPushThirdPartyOrderInfo(orderChannelID);
			for (OrderExtend orderExtendInfo: orderInfoFromDB) {

				// 信息翻译
				translateOrderInfo(orderExtendInfo);

				// 订单明细信息取得
				OrderBean orderOutputInfo = getPushOrder(orderChannelID, orderExtendInfo, timeZone, exchangeRate);

				// 明细税率不存在的订单跳过
				if (orderOutputInfo != null) {
					orderExtendInfo.setOrderOutputInfo(orderOutputInfo);
				}
			}
		} catch (Exception e) {
			logger.error("getPushCreateList error", e);
			issueLog.log(e, ErrorType.BatchJob,
					SubSystem.OMS, "getPushCreateList error");
		}
		return orderInfoFromDB;
	}

	/**
	 * 推送订单明细信息取得
	 *
	 */
	private OrderBean getPushOrder(String orderChannelID, OrderExtend orderExtendInfo, int timeZone, String exchangeRate) throws Exception {

		// 公司扣点
		float voCommission = Float.valueOf(ChannelConfigs.getVal1(orderChannelID, ChannelConfigEnums.Name.vo_commission));
		// 阿里扣点
		float alipayFee =  Float.valueOf(ChannelConfigs.getVal1(orderChannelID, ChannelConfigEnums.Name.alipay_fee));
		// 天猫扣点
		float tmallCommission = Float.valueOf(ChannelConfigs.getVal1(orderChannelID, ChannelConfigEnums.Name.tmall_commission));

		// 汇率
		Float rate = Float.valueOf(exchangeRate);

		OrderBean ret = new OrderBean();

		// customerReference
		ret.setCustomerReference(orderExtendInfo.getName());
		// orderReference
		String orderReference = "";
		if (StringUtils.isEmpty(orderExtendInfo.getSubSourceOrderId())) {
			orderReference = orderExtendInfo.getSourceOrderId();
		} else {
			orderReference = orderExtendInfo.getSubSourceOrderId();
		}
		ret.setOrderReference(orderReference);
		// orderTimestamp
		String orderDateTime = orderExtendInfo.getOrderDateTime();
		ret.setOrderTimestamp(DateTimeUtil.getLocalTime(DateTimeUtil.parse(orderDateTime), timeZone));
		// transactionId
		ret.setTransactionId(orderExtendInfo.getPayNo());
		// exchangeRate
		ret.setExchangeRate(Double.valueOf(exchangeRate));

		List<OrderItem> itemList = new ArrayList<OrderItem>();
		// 订单明细价格
		List<SetPriceBean> orderPriceDatas = SetPriceUtils.setPrice(orderExtendInfo.getOrderNumber(), orderExtendInfo.getOrderChannelId(), orderExtendInfo.getCartId(), SetPriceUtils.APPROVED_ORDER_INCLUDE_SHIPPING);

		// 订单明细税率检查
		if (!chkOrderDetailData(orderPriceDatas)) {
			return null;
		}

		for (int i = 0; i < orderPriceDatas.size(); i++) {
			SetPriceBean orderPriceInfo = orderPriceDatas.get(i);

			OrderItem orderItem = new OrderItem();
			orderItem.setItemId(orderPriceInfo.getClient_sku());
			orderItem.setQuantity(Integer.valueOf(orderPriceInfo.getQuantity_ordered()));

			float price = div2Digits(Float.valueOf(orderPriceInfo.getPrice()), rate);
			orderItem.setItemPrice(price);

			float shippingFee = div2Digits(Float.valueOf(orderPriceInfo.getShipping_fee()), rate);
			orderItem.setShipCharge(shippingFee);

			// 关税
			orderItem.setCustomsDuty(mult2Digits(price, Float.valueOf(orderPriceInfo.getDuty_rate())));

			OrderFeesBean orderFeesInfo = new OrderFeesBean();
			// Ali 扣点
			orderFeesInfo.setAlipayFee(mult2Digits(price + shippingFee, alipayFee));
			// Tmall 扣点
			orderFeesInfo.setTmallCommission(mult2Digits(price + shippingFee, tmallCommission));
			// 公司 扣点
			orderFeesInfo.setVoCommission(mult2Digits(price + shippingFee, voCommission));
			orderItem.setFees(orderFeesInfo);

			itemList.add(orderItem);
		}
		ret.setItems(itemList);

		// shippingAddress
		OrderShippingBean orderShippingInfo = new OrderShippingBean();

//		ArrayList<String> shippingAddressList = getSearsAddress(orderExtendInfo.getShipAddress(), orderExtendInfo.getShipAddress2());
		ArrayList<String> shippingAddressList = getSearsAddress(ChannelConfigs.getChannel(orderChannelID).getSend_address(),"");

		orderShippingInfo.setAddressLine1(shippingAddressList.get(0));
		orderShippingInfo.setAddressLine2(shippingAddressList.get(1));
		orderShippingInfo.setAddressLine3(shippingAddressList.get(2));
		orderShippingInfo.setZipCode(orderExtendInfo.getShipZip());
		orderShippingInfo.setZipCode(ChannelConfigs.getChannel(orderChannelID).getSend_zip());
		orderShippingInfo.setFirstName(orderExtendInfo.getShipName());
		orderShippingInfo.setLastName(DUMMY_LAST_NAME);
		orderShippingInfo.setEmail(SEARS_MAIL_ADDRESS);
		orderShippingInfo.setCity(orderExtendInfo.getShipCity());
		orderShippingInfo.setDayPhone(COMPANY_PHONE);
		// TODO 修正预定
		orderShippingInfo.setState("IL");
		orderShippingInfo.setAddressType("S");
		orderShippingInfo.setCountryCode("USA");

		ret.setShippingAddress(orderShippingInfo);

		return ret;
	}

	/**
	 * 订单明细税率检查
	 *
	 */
	private boolean chkOrderDetailData(List<SetPriceBean> orderPriceDatas) {
		boolean ret = true;

		for (int i = 0; i < orderPriceDatas.size(); i++) {
			if (StringUtils.isEmpty(orderPriceDatas.get(i).getDuty_rate())) {
				ret = false;
				break;
			}
		}

		return ret;
	}

	/**
	 * 地址固定长度对应
	 *
	 */
	private ArrayList<String> getSearsAddress(String address1, String address2) {
		ArrayList<String> retAddress = new ArrayList<String>();

		String retAddress1 = "";
		String retAddress2 = "";
		String retAddress3 = "";

		String allAddress = address1 + address2;
		if (allAddress.length() <= 45) {
			retAddress1 = allAddress;
		} else if (allAddress.length() > 45 && allAddress.length() <= 90) {
			retAddress1 = allAddress.substring(0,45);
			retAddress2 = allAddress.substring(45,allAddress.length());
		} else if (allAddress.length() > 90 && allAddress.length() <= 135) {
			retAddress1 = allAddress.substring(0,45);
			retAddress2 = allAddress.substring(45,90);
			retAddress3 = allAddress.substring(90,allAddress.length());
		} else {
			retAddress1 = allAddress.substring(0,45);
			retAddress2 = allAddress.substring(45,90);
			retAddress3 = allAddress.substring(90,135);
		}

		retAddress.add(retAddress1);
		retAddress.add(retAddress2);
		retAddress.add(retAddress3);

		return retAddress;
	}

	/**
	 * 翻译对应
	 *
	 */
	private void translateOrderInfo(OrderExtend orderExtendInfo) throws Exception {
		ArrayList<String> translateContent = new ArrayList<String>();
//		translateContent.add(orderExtendInfo.getName());
//		translateContent.add(orderExtendInfo.getShipAddress());
//		translateContent.add(orderExtendInfo.getShipAddress2());
//		translateContent.add(orderExtendInfo.getShipName());
//		translateContent.add(orderExtendInfo.getShipCity());
		//地址用仓库地址，所以不需要翻译
		translateContent.add(orderExtendInfo.getName());
		translateContent.add(orderExtendInfo.getShipName());

		List<String> translateList = BaiduTranslateUtil.translate(translateContent);

//		orderExtendInfo.setName(translateList.get(0));
//		orderExtendInfo.setShipAddress(translateList.get(1));
//		orderExtendInfo.setShipAddress2(translateList.get(2));
//		orderExtendInfo.setShipName(translateList.get(3));
//		orderExtendInfo.setShipCity(translateList.get(4));
		//地址用仓库地址，所以不需要翻译
		orderExtendInfo.setName(translateList.get(0));
		orderExtendInfo.setShipName(translateList.get(1));
	}

	/**
	 * 第三方订单号，回写本地DB
	 *
	 */
	private boolean updateOrder(String orderNumber, String clientOrderId, String taskName) {
		boolean ret = true;

		ret = orderDao.updateOrdersClientOrderIdInfo(orderNumber, clientOrderId, taskName);

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

	/** 接口XML文件保存
	 *
	 * @param strXML
	 * @param type
	 */
	private void backupTheXmlFile(String orderNumber, String strXML, int type) {

		String strFolder = Properties.readValue("PostBackup") + File.separator + this.getClass().getName();
		File file = new File(strFolder);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		java.util.Date date = new java.util.Date();
		String fileName = orderNumber + "_" + sdf.format(date);
		FileWriter fs = null;
		try {
			if (type == 0) {
				fs = new FileWriter(strFolder + File.separator + "post_sears_" + fileName + ".xml");
			} else if (type == 1) {
				fs = new FileWriter(strFolder + File.separator + "ret_sears_" + fileName + ".xml");
			} else {
				fs = new FileWriter(strFolder + File.separator + "ret_sears_lookup" + fileName + ".xml");
			}
			fs.write(strXML);
			fs.flush();
		} catch (Exception ex) {
			logger.error(ex.toString());
			issueLog.log(ex,ErrorType.BatchJob, SubSystem.OMS);
		} finally {
			try {
				fs.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				issueLog.log(e,ErrorType.BatchJob, SubSystem.OMS);
			}
		}
	}

	/**
	 * 调用Sears(Order Lookup)
	 *
	 */
	public boolean getSearsOrderLookup() {
		logger.info("getSearsOrderLookup start");

		boolean isSuccess = true;

		// 超卖订单一览
		List<OrderResponse> searOutOfStockOrderList = new ArrayList<OrderResponse>();
		// 部分取消订单一览
		List<OrderResponse> searPartialCancelOrderList = new ArrayList<OrderResponse>();
		// 品牌方取消订单一览
		List<OrderExtend> searsCancelOrderList = new ArrayList<OrderExtend>();

		logger.info("	getOrderDetailForBlankTrackingNo");
		List<OrderExtend> orderDetailListForBlankTrackingNo = getOrderDetailForBlankTrackingNo(orderChannelID);

		if (orderDetailListForBlankTrackingNo != null) {
			try {

				String clientOrderId = "";
				boolean isNeedSetTrackingNumber = false;
				// 相同订单号只取一次
				OrderLookupResponse orderLookupResponse = null;

				// Sears Tracking Info 取得
				for (int i = 0; i < orderDetailListForBlankTrackingNo.size(); i++) {
					OrderExtend orderDetailInfo = orderDetailListForBlankTrackingNo.get(i);

					if (!clientOrderId.equals(orderDetailInfo.getClientOrderId())) {
						isNeedSetTrackingNumber = false;
						clientOrderId = orderDetailInfo.getClientOrderId();

						// Sears Order Lookup接口请求
						logger.info("	searsService.OrderLookup clientOrderId = " + orderDetailInfo.getClientOrderId());
						orderLookupResponse = searsService.getOrderInfo(orderDetailInfo.getClientOrderId());

						// orderLookupResponse 再设定（根据trackingNumbers 设定 trackingNumberList）
						resetOrderLoopupResponse(orderLookupResponse);

						// 相应XML缓存
						backupTheXmlFile(orderDetailInfo.getOrderNumber(), JaxbUtil.convertToXml(orderLookupResponse), 2);

						// Sears 订单信息返回值检查
						List<Object> chkResult = chkOrderLoopupResponse(orderLookupResponse);
						boolean retResult = (boolean)chkResult.get(0);
						String retStatus = (String)chkResult.get(1);
						String statusCode = (String)chkResult.get(2);
						String retCancelReasonCode = (String)chkResult.get(3);

						if (retResult) {
							// Cancel 以外的场合，TrackingNumber需要设定
							if (StringUtils.isEmpty(retStatus)) {
								isNeedSetTrackingNumber = true;
							} else {
								// 超卖的场合
								if (!StringUtils.isEmpty(retCancelReasonCode)) {
									OrderResponse outOfStockOrder = new OrderResponse();
									outOfStockOrder.setOrderId(orderDetailInfo.getSourceOrderId());
									outOfStockOrder.setMessage(OmsConstants.SEARS_OUT_OF_STOCK_MESSAGE);

									searOutOfStockOrderList.add(outOfStockOrder);
								}
							}
						} else {
							// 部分取消的场合
							OrderResponse partialCancelOrder = new OrderResponse();
							partialCancelOrder.setOrderId(orderDetailInfo.getSourceOrderId());
							partialCancelOrder.setMessage(OmsConstants.SEARS_PARTIAL_CANCELLED_MESSAGE);

							searPartialCancelOrderList.add(partialCancelOrder);
						}
					}

					if (isNeedSetTrackingNumber) {
						// TrackingNumber 为空的明细
						if (StringUtils.isEmpty(orderDetailInfo.getTrackingNumber())) {
							setTrackingInfo(orderDetailInfo, orderLookupResponse);
						}
					}

					// Status 更新
					setClientStatusInfo(orderDetailInfo, orderLookupResponse);

					// 如果是品牌方取消的话，则记入取消列表
					if (orderDetailInfo.getClientStatus().equals(OmsConstants.SearsOrderItemStatus.Cancelled) && StringUtils.isNullOrBlank2(orderDetailInfo.getTrackingNumber())) {
						searsCancelOrderList.add(orderDetailInfo);
					}
				}

				// Sears Tracking Info 更新
				isSuccess = updateSearsTrackingInfo(orderDetailListForBlankTrackingNo,searsCancelOrderList);

//				// 推送异常订单，客服邮件通知
//				if (searOutOfStockOrderList.size() > 0) {
//					sendCustomerServiceMail(searOutOfStockOrderList, MAIL_OUT_OF_STOCK_ERROR);
//				}
//
//				// 部分取消，客服邮件通知
//				if (searPartialCancelOrderList.size() > 0) {
//					sendCustomerServiceMail(searPartialCancelOrderList, MAIL_DIFFERENT_STATUS_ERROR);
//				}

				String errorMail = sendErrorMail(searsCancelOrderList);

				if (!StringUtils.isNullOrBlank2(errorMail)) {
					logger.info("错误邮件出力");
					String subject = String.format(OmsConstants.EmailPostSearsOrder.SUBJECT, "Sears");
					Mail.sendAlert(CodeConstants.EmailReceiver.NEED_SOLVE, subject, errorMail, true);
				}

			} catch (Exception e) {
				isSuccess = false;
				logger.error("getSearsOrderLookup error", e);

				issueLog.log(e,
						ErrorType.BatchJob,
						SubSystem.OMS,
						"getSearsOrderLookup error");
			}
		}

		logger.info("getSearsOrderLookup end");

		return isSuccess;
	}

	/**
	 * Order lookup 返回值检查（目前只处理Cancel状态）
	 * @param orderLookupResponse Order lookup 返回值
	 * @return ret ret[0] 检查结果
	 * 				ret[1] status
	 * 				ret[2] statusCode
	 * 				ret[3] cancelReasonCode
	 */
	private List<Object> chkOrderLoopupResponse(OrderLookupResponse orderLookupResponse) {
		List<Object> ret = new ArrayList<Object>();
		boolean retResult = true;
		String retStatus = "";
		String retStatusCode = "";
		String retCancelReasonCode = "";

		List<OrderLookupItem> orderLookupResponseItems = orderLookupResponse.getItems();
		for (int i = 0; i < orderLookupResponseItems.size(); i++) {
			OrderLookupItem orderLookupItem = orderLookupResponseItems.get(i);

			// 取消的场合
			if (OmsConstants.SearsOrderItemStatus.Cancelled.equals(orderLookupItem.getStatus())) {
				retStatus = orderLookupItem.getStatus();
				retStatusCode = orderLookupItem.getStatusCode();

				// 超卖原因Code设定
				if (OmsConstants.SearsOrderItemCancelReasonCode.OutOfStock.equals(StringUtils.isNullOrBlank2(orderLookupItem.getCancelReasonCode()))) {
					retCancelReasonCode = orderLookupItem.getCancelReasonCode();
				}
			}

			if (!StringUtils.isEmpty(retStatus)) {
				// 明细部分Cancel的场合
				if (!retStatus.equals(orderLookupItem.getStatus())) {
					retResult = false;
					break;
				}
			}
		}

		ret.add(retResult);
		ret.add(retStatus);
		ret.add(retStatusCode);
		ret.add(retCancelReasonCode);

		return ret;
	}

	/**
	 * 待处理订单取得（trackingNumbers 为空的订单）
	 * @param orderChannelID 订单渠道
	 *
	 */
	private List<OrderExtend> getOrderDetailForBlankTrackingNo(String orderChannelID) {
		List<OrderExtend> ret = orderDao.getSearsOrderDetailBlankTrackingInfo(orderChannelID);

		return ret;
	}

	/** Order lookup 返回值再设定（trackingNumberList 设定）
	 * @param orderLookupResponse Order lookup 返回值
	 *
	 */
	private void resetOrderLoopupResponse(OrderLookupResponse orderLookupResponse) {
		List<OrderLookupItem> responseItems = orderLookupResponse.getItems();

		for (int i = 0; i < responseItems.size(); i++) {
			OrderLookupItem item = responseItems.get(i);

			if (!StringUtils.isEmpty(item.getTrackingNumbers())) {
				List<OrderLookupItemSub> trackingNumberList = getOrderLookupItemSubList(item.getTrackingNumbers());
				item.setTrackingNumberList(trackingNumberList);
			} else {
				item.setTrackingNumberList(new ArrayList<OrderLookupItemSub>());
			}
		}
	}

	/** Order lookup 返回值再设定（根据trackingNumbers，取得trackingNumberList）
	 * @param trackingNumbers
	 * @return trackingNumberList
	 */
	private List<OrderLookupItemSub> getOrderLookupItemSubList(String trackingNumbers) {
		List<OrderLookupItemSub> ret = new ArrayList<OrderLookupItemSub>();

		String[] trackingNumArr = trackingNumbers.split(",");
		for (int i = 0; i < trackingNumArr.length; i++) {
			OrderLookupItemSub orderLookupItemSub = new OrderLookupItemSub();
			orderLookupItemSub.setTrackingNumber(trackingNumArr[i]);

			ret.add(orderLookupItemSub);
		}

		return ret;
	}


	/**
	 * 根据Sears 订单信息，Tracking信息设置
	 * @param orderDetailInfo 待处理订单明细
	 * @param orderLookupResponse Sears返回订单信息
	 *
	 */
	private void setTrackingInfo(OrderExtend orderDetailInfo, OrderLookupResponse orderLookupResponse) {
		List<OrderLookupItem> orderLookupItemList = orderLookupResponse.getItems();

		for (int i = 0; i < orderLookupItemList.size(); i++) {
			OrderLookupItem orderLookupItem = orderLookupItemList.get(i);

			// Sears 侧TrackingNumbers 未产生的场合，不处理
			if(StringUtils.isEmpty(orderLookupItem.getTrackingNumbers())) {
				continue;
			}

			// 找到本地SKU对应的，Sears明细
			if(orderDetailInfo.getClientSku().equals(orderLookupItem.getItemId())) {

				// 订单明细 TrackingNumber 设定
				List<OrderLookupItemSub> trackingNumberList = orderLookupItem.getTrackingNumberList();
				for (int j = 0; j < trackingNumberList.size(); j++) {
					OrderLookupItemSub orderLookupItemSub = trackingNumberList.get(j);
					// 未分配的场合
					if (!orderLookupItemSub.isAllocated()) {
						orderDetailInfo.setTrackingNumber(orderLookupItemSub.getTrackingNumber());
						// 分配标志设定
						orderLookupItemSub.setIsAllocated(true);
						break;
					}
				}

				// 订单明细 SalesCheckNumber 设定
				orderDetailInfo.setSalesCheckNumber(orderLookupItem.getSalesCheckNumber());
				// 本地更新标志
				orderDetailInfo.setIsNeedUpdateFlag(true);

				break;
			}
		}
	}

	/**
	 * 根据Sears 订单信息，明细扩展表client_status信息设置
	 * @param orderDetailInfo 待处理订单明细
	 * @param orderLookupResponse Sears返回订单信息
	 *
	 */
	private void setClientStatusInfo(OrderExtend orderDetailInfo, OrderLookupResponse orderLookupResponse) {
		List<OrderLookupItem> orderLookupItemList = orderLookupResponse.getItems();

		for (int i = 0; i < orderLookupItemList.size(); i++) {
			OrderLookupItem orderLookupItem = orderLookupItemList.get(i);

			// 找到本地SKU对应的，Sears明细
			if(orderDetailInfo.getClientSku().equals(orderLookupItem.getItemId())) {

				// client SKU 更新
				orderDetailInfo.setClientStatus(StringUtils.null2Space2(orderLookupItem.getStatus()));

				// ErrorMessage 设置
				orderDetailInfo.setErrorMessage(StringUtils.null2Space2(orderLookupItem.getErrorMessage()));

				break;
			}
		}
	}

	/**
	 * 本地TrackingNumber信息更新主函数（TrackingNumber同本公司ResId）
	 * @param orderDetailList 待处理订单明细一览
	 *
	 */
	private boolean updateSearsTrackingInfo(List<OrderExtend> orderDetailList, List<OrderExtend> cancelDetailList) {
		boolean ret = true;

		List<Object> updateSql = getUpdateSql(orderDetailList);

		String updateSqlStr = (String)updateSql.get(0);
		int size = (int)updateSql.get(1);

		if (size > 0) {

			TransactionStatus status=transactionManager.getTransaction(def);

			try {
				ret = orderDao.updateSearsTrackingInfo(updateSqlStr, size);

				// 取消记录更新
				for (OrderExtend orderExtend : cancelDetailList) {

					if (!StringUtils.isNullOrBlank2(orderExtend.getResId())){
						List<Long> reservationList = new ArrayList<>();
						reservationList.add(Long.valueOf(orderExtend.getResId()));
						reservationDao.updateReservationStatus(Long.valueOf(orderExtend.getOrderNumber()),reservationList,CodeConstants.Reservation_Status.Cancelled,POST_SEARS_CREATE_ORDER);
						reservationDao.insertReservationLog(reservationList,"Item cancelled by the Sears：" + orderExtend.getErrorMessage(),POST_SEARS_CREATE_ORDER);
					}

				}

				transactionManager.commit(status);

//				// 执行结果判定
//				if (ret) {
//					transactionManager.commit(status);
//
//				} else {
//					transactionManager.rollback(status);
//
//					issueLog.log("updateSearsTrackingInfo",
//							"updateSearsTrackingInfo error;size = " + size,
//							ErrorType.BatchJob,
//							SubSystem.OMS);
//				}
			}catch (Exception e){
				transactionManager.rollback(status);

				logger.info("updateSearsTrackingInfo error;size = " + e.toString());

				issueLog.log("updateSearsTrackingInfo",
						"updateSearsTrackingInfo error;size = " + size,
						ErrorType.BatchJob,
						SubSystem.OMS,e.toString());
			}


		}

		return ret;
	}

	/**
	 * 本地TrackingNumber信息更新Sql文取得
	 * @param orderDetailList 待处理订单明细一览
	 * @return ret ret[0] 更新用Sql文
	 *				ret[1] 更新记录件数
	 *
	 */
	private List<Object> getUpdateSql(List<OrderExtend> orderDetailList) {
		List<Object> ret = new ArrayList<>();

		// 更新用Sql文
		StringBuffer updateSql = new StringBuffer();
		// 更新记录件数
		int size = 0;

		String updateSqlSub = "select '%s' order_number, '%s' item_number,'%s' tracking_number, '%s' sales_check_number, '%s' client_status";

		for (int i = 0; i < orderDetailList.size(); i++) {
			OrderExtend orderDetailInfo = orderDetailList.get(i);

//			if (orderDetailInfo.isNeedUpdateFlag()) {
				size = size + 1;

				String updateRecSql = String.format(updateSqlSub, orderDetailInfo.getOrderNumber(), orderDetailInfo.getItemNumber(), StringUtils.null2Space2(orderDetailInfo.getTrackingNumber()), StringUtils.null2Space2(orderDetailInfo.getSalesCheckNumber()), StringUtils.null2Space2(orderDetailInfo.getClientStatus()));

				if (StringUtils.isEmpty(updateSql.toString())) {
					updateSql.append(updateRecSql);
				} else {
					updateSql.append(" union all ");
					updateSql.append(updateRecSql);
				}
//			}
		}

		ret.add(updateSql.toString());
		ret.add(size);

		return ret;
	}

	/**
	 * 错误邮件出力
	 * @param errorList 错误SKU一览
	 * @return 错误邮件内容
	 */
	private String sendErrorMail(List<OrderExtend> errorList) {

		StringBuilder builderContent = new StringBuilder();

		if (errorList.size() > 0) {

			StringBuilder builderDetail = new StringBuilder();

			int index = 0;
			for (OrderExtend error : errorList) {

				index = index + 1;
				builderDetail.append(String.format(OmsConstants.EmailPostSearsOrder.ROW,
						index,
						ShopConfigs.getShopNameDis(error.getOrderChannelId(), error.getCartId()),
						error.getOrderNumber(),
						error.getSourceOrderId(),
						error.getClientOrderId(),
						error.getResId(),
						error.getSku(),
						error.getClientSku(),
						error.getErrorMessage()));
			}

			String count = String.format(OmsConstants.EmailPostSearsOrder.COUNT, errorList.size());

			String detail = String.format(OmsConstants.EmailPostSearsOrder.TABLE, count, builderDetail.toString());

			builderContent
					.append(Constants.EMAIL_STYLE_STRING)
					.append(OmsConstants.EmailPostSearsOrder.HEAD)
					.append(detail);


		}

		return builderContent.toString();
	}


}
