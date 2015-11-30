package com.voyageone.batch.oms.service;

import com.jcraft.jsch.ChannelSftp;
import com.voyageone.batch.core.Constants;
import com.voyageone.batch.core.modelbean.SetPriceBean;
import com.voyageone.batch.core.util.SetPriceUtils;
import com.voyageone.batch.oms.OmsConstants;
import com.voyageone.batch.oms.dao.OrderDao;
import com.voyageone.batch.oms.formbean.InFormFile;
import com.voyageone.batch.oms.modelbean.OrderExtend;
import com.voyageone.common.components.baidu.translate.BaiduTranslateUtil;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.sears.SearsService;
import com.voyageone.common.components.sears.bean.*;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.ThirdPartyConfigs;
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
//	private String orderChannelID = "013";
	private String orderChannelID = "001";

	/**
	 * 调用Sears(Create Order)
	 *
	 */
	public boolean postSearsCreateOrder() {
		logger.info("postSearsCreateOrder start");

		boolean isSuccess = true;
		int orderChannelTimeZone = 8;
		String exchangeRate = "6.2";

		logger.info("	getPushCreateList");
		List<OrderBean> pushDailySalesListList = getPushCreateList(orderChannelID, orderChannelTimeZone, exchangeRate);

		if (pushDailySalesListList != null) {
			for (int i = 0; i < pushDailySalesListList.size(); i++) {
				OrderBean createOrderInfo = pushDailySalesListList.get(i);

				// 请求XML缓存
				backupTheXmlFile(createOrderInfo.getOrderReference(), JaxbUtil.convertToXml(createOrderInfo), 0);

				try {
					// Sears Create Order接口请求
					logger.info("	searsService.CreateOrder sourceOrderId = " + createOrderInfo.getOrderReference());
					OrderResponse orderResponse = searsService.CreateOrder(createOrderInfo);

					// 相应XML缓存
					backupTheXmlFile(createOrderInfo.getOrderReference(), orderResponse.getMessage(), 1);

					//	第三方订单号更新
					if ("Succeed".equals(orderResponse.getMessage())) {
						logger.info("	updateOrder");
						isSuccess = updateOrder(createOrderInfo.getOrderReference(), orderResponse.getOrderId(), POST_SEARS_CREATE_ORDER);

						if (!isSuccess) {
							issueLog.log("postSearsCreateOrder.updateOrder",
									"UpdateOrder error ; OrderNumber = " + createOrderInfo.getOrderReference(),
									ErrorType.BatchJob,
									SubSystem.OMS);
						}
					} else {
						// TODO
						issueLog.log("postSearsCreateOrder.CreateOrder",
								"CreateOrder error ; Message = " + orderResponse.getMessage(),
								ErrorType.BatchJob,
								SubSystem.OMS);
					}
				} catch (Exception e) {
					isSuccess = false;
					logger.error("postSearsCreateOrder.CreateOrder error", e);

					issueLog.log("postSearsCreateOrder.CreateOrder",
							"CreateOrder error ; OrderNumber = " + createOrderInfo.getOrderReference(),
							ErrorType.BatchJob,
							SubSystem.OMS);
				}
			}
		}

		logger.info("postSearsCreateOrder end");

		return isSuccess;
	}

	/**
	 * 推送订单信息取得
	 *
	 */
	private List<OrderBean> getPushCreateList(String orderChannelID, int timeZone, String exchangeRate) {
		List<OrderBean> ret = new ArrayList<OrderBean>();

		try {
			// 订单信息取得
			List<OrderExtend> orderInfoFromDB =orderDao.getPushThirdPartyOrderInfo(orderChannelID);
			if (orderInfoFromDB.size() > 0) {
				for (int i = 0; i < orderInfoFromDB.size(); i++) {
					OrderExtend orderExtendInfo = orderInfoFromDB.get(i);

					// 信息翻译
					translateOrderInfo(orderExtendInfo);

					OrderBean orderOutputInfo = getPushOrder(orderExtendInfo, timeZone, exchangeRate);

					ret.add(orderOutputInfo);
				}
			}
		} catch (Exception e) {
			ret = null;

			logger.error("getPushCreateList error", e);
			issueLog.log(e, ErrorType.BatchJob,
					SubSystem.OMS, "getPushCreateList error");
		}


		return ret;
	}

	/**
	 * 推送订单明细信息取得
	 *
	 */
	private OrderBean getPushOrder(OrderExtend orderExtendInfo, int timeZone, String exchangeRate) throws Exception {
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
		for (int i = 0; i < orderPriceDatas.size(); i++) {
			SetPriceBean orderPriceInfo = orderPriceDatas.get(i);

			OrderItem orderItem = new OrderItem();
			orderItem.setItemId(orderPriceInfo.getClient_sku());
			orderItem.setQuantity(Integer.valueOf(orderPriceInfo.getQuantity_ordered()));

			float price = div2Digits(Float.valueOf(orderPriceInfo.getPrice()), rate);
			orderItem.setItemPrice(price);

			float shippingFee = div2Digits(Float.valueOf(orderPriceInfo.getShipping_fee()), rate);
			orderItem.setShipCharge(shippingFee);

			// TODO
			orderItem.setCustomsDuty(10);

			OrderFeesBean orderFeesInfo = new OrderFeesBean();
			orderFeesInfo.setAlipayFee(10);
			orderFeesInfo.setTmallCommission(10);
			orderFeesInfo.setVoCommission(10);
			orderItem.setFees(orderFeesInfo);

			itemList.add(orderItem);
		}
		ret.setItems(itemList);

		// shippingAddress
		OrderShippingBean orderShippingInfo = new OrderShippingBean();

		ArrayList<String> shippingAddressList = getSearsAddress(orderExtendInfo.getShipAddress(), orderExtendInfo.getShipAddress2());

		orderShippingInfo.setAddressLine1(shippingAddressList.get(0));
		orderShippingInfo.setAddressLine2(shippingAddressList.get(1));
		orderShippingInfo.setAddressLine3("");
		orderShippingInfo.setZipCode(orderExtendInfo.getShipZip());
		orderShippingInfo.setFirstName(orderExtendInfo.getShipName());
		orderShippingInfo.setLastName("");
		orderShippingInfo.setEmail(orderExtendInfo.getShipEmail());
		orderShippingInfo.setCity(orderExtendInfo.getShipCity());
		orderShippingInfo.setDayPhone(orderExtendInfo.getShipPhone());
		orderShippingInfo.setState("");
		orderShippingInfo.setAddressType("S");
		orderShippingInfo.setCountryCode("CN");

		ret.setShippingAddress(orderShippingInfo);

		return ret;
	}

	private ArrayList<String> getSearsAddress(String address1, String address2) {
		ArrayList<String> retAddress = new ArrayList<String>();

		String retAddress1 = "";
		String retAddress2 = "";

		String allAddress = address1 + address2;
		if (allAddress.length() <= 255) {
			retAddress1 = allAddress;
		} else if (allAddress.length() > 255 && allAddress.length() <= 510) {
			retAddress1 = allAddress.substring(0,255);
			retAddress2 = allAddress.substring(255,allAddress.length());
		} else {
			retAddress1 = allAddress.substring(0,255);
			retAddress2 = allAddress.substring(255,510);
		}

		retAddress.add(retAddress1);
		retAddress.add(retAddress2);
		return retAddress;
	}

	private void translateOrderInfo(OrderExtend orderExtendInfo) throws Exception {
		ArrayList<String> translateContent = new ArrayList<String>();
		translateContent.add(orderExtendInfo.getName());
		translateContent.add(orderExtendInfo.getShipAddress());
		translateContent.add(orderExtendInfo.getShipAddress2());
		translateContent.add(orderExtendInfo.getShipName());
		translateContent.add(orderExtendInfo.getShipCity());

		List<String> translateList = BaiduTranslateUtil.translate(translateContent);

		orderExtendInfo.setName(translateList.get(0));
		orderExtendInfo.setShipAddress(translateList.get(1));
		orderExtendInfo.setShipAddress2(translateList.get(2));
		orderExtendInfo.setShipName(translateList.get(3));
		orderExtendInfo.setShipCity(translateList.get(4));
	}

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
				fs = new FileWriter(strFolder + File.separator + "post_onestop_" + fileName + ".xml");
			} else {
				fs = new FileWriter(strFolder + File.separator + "ret_onestop_" + fileName + ".xml");
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
}
