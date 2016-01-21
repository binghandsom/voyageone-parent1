package com.voyageone.batch.oms.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import javax.mail.MessagingException;

import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.ShopConfigEnums;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.voyageone.batch.core.Constants;
import com.voyageone.batch.oms.OmsConstants;
import com.voyageone.batch.oms.dao.CustomerDao;
import com.voyageone.batch.oms.dao.OrderDao;
import com.voyageone.batch.oms.modelbean.ChangedOrderInfo4Log;
import com.voyageone.batch.oms.modelbean.NewOrderInfo4Log;
import com.voyageone.batch.oms.modelbean.SelfOrderDetailInfo4Post;
import com.voyageone.batch.oms.modelbean.SelfOrderInfo4Post;
import com.voyageone.batch.oms.modelbean.TaobaoOrderBean;
import com.voyageone.batch.oms.modelbean.TaobaoPromotionBean;
import com.voyageone.batch.oms.modelbean.TaobaoTradeBean;
import com.voyageone.batch.oms.modelbean.TradeInfoBean;
import com.voyageone.batch.oms.utils.CommonUtil;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.Type;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Name;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Sale;
import com.voyageone.common.configs.Enums.TypeConfigEnums.MastType;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.mail.Mail;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;

@Service
public class OrderInfoImportService {
	
	private static Log logger = LogFactory.getLog(OrderInfoImportService.class);
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	IssueLog issueLog;
	
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	private DefaultTransactionDefinition def = new DefaultTransactionDefinition();
	
	/**
	 * 买就送赠品设定
	 */
	private Map<String, List<Map<String, String>>> BUY_THAN_GIFT_SETTING;
	/**
	 * 买就送赠品设定(不减库存备份，给transaction表用)
	 */
	private Map<String, List<Map<String, String>>> BUY_THAN_GIFT_SETTING_TRANS;
	/**
	 * 买就送赠品设定(不减库存备份，给扩展表用)
	 */
	private Map<String, List<Map<String, String>>> BUY_THAN_GIFT_SETTING_EXT;
	/**
	 * 满就送赠品例外设定
	 */
	private Map<String, List<String>> PRICE_THAN_GIFT_EXCEPT_SETTING;
	/**
	 * 满就送赠品设定
	 */
	private Map<String, List<Map<String, String>>> PRICE_THAN_GIFT_SETTING;
	/**
	 * 满就送赠品设定(不减库存备份，给transaction表用)
	 */
	private Map<String, List<Map<String, String>>> PRICE_THAN_GIFT_SETTING_TRANS;
	/**
	 * 满就送赠品设定(不减库存备份，给扩展表用)
	 */
	private Map<String, List<Map<String, String>>> PRICE_THAN_GIFT_SETTING_EXT;
	/**
	 * 满就送金额设定
	 */
	private Map<String, List<Double>> PRICE_THAN_GIFT_PRICE_SETTING;
	/**
	 * 老顾客送赠品设定
	 */
	private Map<String, List<String>> REGULAR_CUSTOMER_GIFT_SETTING;
	/**
	 * 已获赠品顾客信息（历史）
	 */
	private Map<String, Object> HAVING_GIFTED_CUSTOMER_INFO;
	/**
	 * 已获赠品顾客信息（本轮）
	 */
	private Map<String, Object> HAVING_GIFTED_CUSTOMER_INFO_LOCAL;
	/**
	 * 前多少名赠品设定
	 */
	private Map<String, List<Map<String, String>>> PRIOR_COUNT_CUSTOMER_GIFT_SETTING;
	/**
	 * 前多少名赠品设定(不减库存备份，给transaction表用)
	 */
	private Map<String, List<Map<String, String>>> PRIOR_COUNT_CUSTOMER_GIFT_SETTING_TRANS;
	/**
	 * 前多少名赠品设定(不减库存备份，给扩展表用)
	 */
	private Map<String, List<Map<String, String>>> PRIOR_COUNT_CUSTOMER_GIFT_SETTING_EXT;
	/**
	 * 前多少名赠品例外sku设定
	 */
	private Map<String, List<String>> PRIOR_COUNT_CUSTOMER_GIFT_EXCEPT_SKU_SETTING;
	/**
	 * 赠品规则类型相关属性设定
	 */
	private Map<String, String> GIFT_PROPERTY_SETTING;

	/**
	 * 套装设定内容
	 */
	private Map<String, List<String>> SUIT_SKU_SETTING;
	
	/**
	 * 取出要处理的新订单json列表
	 * 
	 * @return
	 */
	public List<Map<String, String>> getNewOrderInfoFromJson() {
		return orderDao.getNewOrderInfoFromJson();
	}
	
	/**
	 * 批处理将新订单json信息转到history表中
	 * 
	 * @param orderInfoTotalList
	 * @param jsonIdList
	 * @param targetList
	 * @param taskName
	 */
	public boolean importNewOrderFromJsonToHistory(List<TaobaoTradeBean> orderInfoTotalList, List<String> jsonIdList, List<String> targetList, String taskName) {
		boolean isSuccess = true;
		
		if (orderInfoTotalList != null && orderInfoTotalList.size() > 0) {
			
			// 插入history表values部分准备
			String sqlBatch = getBatchNewOrderHistorySqlData(orderInfoTotalList, taskName);
			
			int jsonIdSize = jsonIdList.size();
//			// json表主键id拼接
//			StringBuilder sbJsonId = new StringBuilder(Constants.LEFT_BRACKET_CHAR);
//			for (int i = 0; i < jsonIdSize; i++) {
//				String jsonId = jsonIdList.get(i);
//				sbJsonId.append(jsonId);
//				
//				if (i < (jsonIdSize - 1)) {
//					sbJsonId.append(Constants.COMMA_CHAR);
//				}
//			}
//			sbJsonId.append(Constants.RIGHT_BRACKET_CHAR);
//			String jsonIdListStr = sbJsonId.toString();
//			logger.info("jsonId:" + jsonIdListStr);
			
			// 开启事物
			TransactionStatus status = transactionManager.getTransaction(def);
			try {
				// 新订单json数据批量插入history表
				isSuccess = orderDao.insertNewOrdersInfo(sqlBatch, orderInfoTotalList.size());
				// 插入history表成功
				if (isSuccess) {
					logger.info("新订单json数据批量插入history表成功");
					
					// 更新id列表对应的json信息表发送标志
					isSuccess = orderDao.updateNewSendFlag4Json(jsonIdList, targetList, jsonIdSize, taskName);
					if (isSuccess) {
						logger.info("置位新订单json信息表发送标志成功");
					} else {
						logger.info("置位新订单json信息表发送标志失败");
					}
				} else {
					logger.info("新订单json数据批量插入history表失败");
				}
				
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
				
				isSuccess = false;
				issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
				
			} finally {
				if (isSuccess) {
					transactionManager.commit(status);
					
					logger.info("importNewOrderFromJsonToHistory is commit");
					
				} else {
					transactionManager.rollback(status);
					
					logger.info("importNewOrderFromJsonToHistory is rollback");
					
					issueLog.log("OrderInfoImportService.importNewOrderFromJsonToHistory", 
							"importNewOrderFromJsonToHistory is rollback", 
							ErrorType.BatchJob, SubSystem.OMS);
					
					// 批处理异常时置位2
					orderDao.updateNewSendFlag4Json2(jsonIdList, targetList, jsonIdSize, taskName);
				}
			}
		}
		
		return isSuccess;
	}
	
	/**
	 * 新订单json信息转到history表中所需数据拼装
	 * 
	 * @return
	 */
	public String getBatchNewOrderHistorySqlData(List<TaobaoTradeBean> orderInfoTotalList, String taskName) {
		StringBuilder sqlBuffer = new StringBuilder();
		
		int size = orderInfoTotalList.size();
		for (int i = 0; i < size; i++) {
			sqlBuffer.append(dataPrepare4TaobaoTradeBean(orderInfoTotalList.get(i), taskName));
			
			if (i < (size - 1)) {
				sqlBuffer.append(Constants.COMMA_CHAR);
			}
		}
		
		return sqlBuffer.toString();
		
	}
	
	/**
	 * 新付款订单信息数据准备
	 * 
	 * @param bean
	 * @param taskName
	 * @return
	 */
	private String dataPrepare4TaobaoTradeBean(TaobaoTradeBean bean, String taskName) {
		StringBuilder sqlValueBuffer = new StringBuilder();
		
		sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
		
		// orderChannelId
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(bean.getChannelId());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// cartId
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(bean.getCartId());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// tid
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(bean.getTid());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// modified_time
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(bean.getModified());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// payTime
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(bean.getPay_time());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// marketName
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.EMPTY_STR);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// billingBuyerNick
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(bean.getBuyer_nick()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// billingBuyerCompany
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.EMPTY_STR);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// billingBuyerPhone
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.EMPTY_STR);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// billingBuyerEmail
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(bean.getBuyer_email()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// billingAddressStreet1
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(StringUtils.null2Space2(bean.getReceiver_district())));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// billingAddressStreet2
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(StringUtils.null2Space2(bean.getReceiver_address())));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// billingAddressCity
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(bean.getReceiver_city()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// billingAddressState
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(bean.getReceiver_state()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// billingAddressZip
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(bean.getReceiver_zip()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// billingAddressCountry
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(OmsConstants.ADDRESS_COUNTRY_CHINA);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// shippingReceiverName
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(bean.getReceiver_name()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// shippingReceiverCompany
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.EMPTY_STR);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// shippingReceiverPhone
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(StringUtils.isNullOrBlank2(bean.getReceiver_mobile()) ? transferStr(bean.getReceiver_phone()) : transferStr(bean.getReceiver_mobile()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// shippingAddressStreet1
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(StringUtils.null2Space2(bean.getReceiver_district())));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// shippingAddressStreet2
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(StringUtils.null2Space2(bean.getReceiver_address())));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// shippingAddressCity
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(bean.getReceiver_city()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// shippingAddressState
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(bean.getReceiver_state()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// shippingAddressZip
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(bean.getReceiver_zip()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// shippingAddressCountry
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(OmsConstants.ADDRESS_COUNTRY_CHINA);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		StringBuilder productSku = new StringBuilder();
		StringBuilder productName = new StringBuilder();
		StringBuilder productItemPrice = new StringBuilder();
		StringBuilder productQuantity = new StringBuilder();
		
		List<TaobaoOrderBean> orderList = bean.getOrder();
		if (orderList != null && orderList.size() > 0) {
			int orderSize = orderList.size();
			for (int i = 0; i < orderSize; i++) { 
				TaobaoOrderBean order = orderList.get(i);
				
				String outerIid = order.getOuter_iid();
				// 价差订单
				if (OmsConstants.PRICES_GAP.equalsIgnoreCase(outerIid)) {
					order.setOuter_sku_id(outerIid);
				}
				String sku = order.getOuter_sku_id();
				
				if (StringUtils.isNullOrBlank2(sku)) {
					sku = OmsConstants.NO_SKU_PRODUCT;
					order.setOuter_sku_id(sku);
					
					try {
						Mail.sendAlert("ITOMS", "订单中有sku是空！", 
								"订单中有sku是空，订单tid:" + bean.getTid() + " orderChannelId:" + bean.getChannelId() + " cartId:" + bean.getCartId(), true);
						logger.info("邮件发送成功!");
						
					} catch (MessagingException e) {
						logger.info("邮件发送失败！" + e);
					}
				}
				
				productSku.append(sku);
				
				productName.append(order.getTitle());
				
				productItemPrice.append(order.getPrice());
				
				productQuantity.append(order.getNum());
				
				if (i < (orderSize - 1)) {
					productSku.append(Constants.SPLIT_CHAR_ADD);
					productName.append(Constants.SPLIT_CHAR_ADD);
					productItemPrice.append(Constants.SPLIT_CHAR_ADD);
					productQuantity.append(Constants.SPLIT_CHAR_ADD);
				}
			}
		}
		
		// productSku
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(productSku.toString()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// productName
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(productName.toString()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// productItemPrice
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(productItemPrice.toString()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// productQuantity
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(productQuantity.toString()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// productSize
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.EMPTY_STR);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// productPrice
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.ZERO_CHAR);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// productExtraCurrency5
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.EMPTY_STR);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// paymentGeneric1Name
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(bean.getPaymentMethod());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// paymentGeneric1Description
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.EMPTY_STR);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// paymentGeneric1Field1
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(bean.getAlipay_no());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// paymentGeneric1Field2
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(bean.getBuyer_alipay_no());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// totalsTotalFee
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(bean.getTotal_fee());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// totalsPayment
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(bean.getPayment());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// totalsTaxAmount
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.ZERO_FLOAT_2);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		String[] postFeeAndType = getPostType(bean);
		// totalsPostFee
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(postFeeAndType[0]);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// totalsShippingType
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(postFeeAndType[1]));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		StringBuilder discountDescription = new StringBuilder();
		StringBuilder discountAmount = new StringBuilder();
		StringBuilder surchargeDescription = new StringBuilder();
		StringBuilder surchargeAmount = new StringBuilder();
		
		List<TaobaoPromotionBean> promotionList = bean.getPromotion();
		if (promotionList != null && promotionList.size() > 0) {
			
			int promotionSize = promotionList.size();
			for (int i = 0; i < promotionSize; i++) {
				TaobaoPromotionBean promotion = promotionList.get(i);
				
				double discount = promotion.getDiscount().doubleValue();
				if (discount >= 0) {
//					if (discount == 0) {
//						continue;
//					}
					
					// 折扣描述
					String promotionName = promotion.getName();
					if (!StringUtils.isEmpty(promotionName)) {
						if (promotionName.contains("$null")) {
							promotionName = promotionName.replace("$null", "$" + OmsConstants.NO_SKU_PRODUCT);
							promotion.setName(promotionName);
						}
					}
					discountDescription.append(promotionName);
					
					discountAmount.append(discount);
					
					if (i < (promotionSize - 1)) {
						discountDescription.append(Constants.SPLIT_CHAR_ADD);
						discountAmount.append(Constants.SPLIT_CHAR_ADD);
					}
				} else {
					surchargeDescription.append("Fix Discount Extra");
					surchargeAmount.append(Math.abs(discount));
					
					if (i < (promotionSize - 1)) {
						surchargeDescription.append(Constants.SPLIT_CHAR_ADD);
						surchargeAmount.append(Constants.SPLIT_CHAR_ADD);
					}
				}
			}
		}
		
		// discountDescription
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(discountDescription.toString()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// discountAmount
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(discountAmount.toString());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// surchargeDescription
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(surchargeDescription.toString()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// surchargeAmount
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(surchargeAmount.toString());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// otherInvoiceInfo
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(bean.getInvoice_info()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		String buyerMessage = StringUtils.null2Space(bean.getBuyer_message());
		buyerMessage = buyerMessage.replaceAll("\r", "");
		buyerMessage = buyerMessage.replaceAll("\n", "");
		// otherComments
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(buyerMessage));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// sent_flag
		sqlValueBuffer.append(Constants.ZERO_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// sent_time
		sqlValueBuffer.append(Constants.NULL_STR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// id_card
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(bean.getId_card()));
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
	 * 转换数据中的特殊字符
	 * 
	 * @param data
	 * @return
	 */
	public String transferStr(String data) {
		if (StringUtils.isNullOrBlank2(data)) {
			return Constants.EMPTY_STR;
		} else {
			return data.replace("'", "''").replace("\\", "\\\\").replace("\r\n", " ").replace("\n", " ").replace("\r", " ");
		}
	}
	
	/**
	 * 获得快递方式
	 * 
	 * @param bean
	 * @return
	 */
	private String[] getPostType(TaobaoTradeBean bean) {
		String postFee = bean.getPost_fee();
		String shippingType = bean.getShipping_type();
		
		String postType = "STO Standard";
		
		String orderChannelId = bean.getChannelId();
		// 斯伯丁
		if (OmsConstants.CHANNEL_SPALDING.equals(orderChannelId)) {
			postType = "SF Ecommerce";
		} else {
			if (StringUtils.isNullOrBlank2(postFee) || Constants.ZERO_FLOAT_2.equals(postFee)) {
				postFee = Constants.ZERO_FLOAT_2;
				postType = "STO Standard";
			} else {
				if ("post".equalsIgnoreCase(shippingType)) {
					postType = "STO Standard";
				} else if ("express".equalsIgnoreCase(shippingType)) {
					postType = getSFCompanyByState(bean);
				} else if ("ems".equalsIgnoreCase(shippingType)) {
					postType = "EMS Standard";
				} else {
					postType = getSFCompanyByState(bean);
				}
			}
		}
		
		String[] postFeeAndType = new String[2];
		postFeeAndType[0] = postFee;
		postFeeAndType[1] = postType;
		
		return postFeeAndType;
	}
	
	/**
	 * 顺丰快递方式
	 * 
	 * @param tradebean
	 * @return
	 */
	private String getSFCompanyByState(TaobaoTradeBean tradebean) {
		String strSFCompany = "SF Standard";
		String[] lstStateSF = {"江苏", "浙江", "上海", "安徽"};
		boolean blnFoundSF = false;
		
		for (int i = 0; i < lstStateSF.length; i++) {
			// receiver_state
			if (tradebean.getReceiver_state() != null && tradebean.getReceiver_state().indexOf(lstStateSF[i]) != -1) {
				blnFoundSF = true;
				break;
			}
			// receiver_city
			if (tradebean.getReceiver_city() != null && tradebean.getReceiver_city().indexOf(lstStateSF[i]) != -1) {
				blnFoundSF = true;
				break;
			}
			// receiver_district
			if (tradebean.getReceiver_district() != null && tradebean.getReceiver_district().indexOf(lstStateSF[i]) != -1) {
				blnFoundSF = true;
				break;
			}
		}
		// if blnFoundSF, then send by SF Ecommerce
		if (blnFoundSF) {
			strSFCompany = "SF Ecommerce";
		}
		return strSFCompany;
	}
	
	/**
	 * 取出要处理的状态变化订单json列表
	 * 
	 * @return
	 */
	public List<Map<String, String>> getChangedOrderInfoFromJson() {
		return orderDao.getChangedOrderInfoFromJson();
	}
	
	/**
	 * 批处理将状态变化订单json信息转到history表中
	 * 
	 * @param orderInfoTotalList
	 * @param jsonIdList
	 * @param targetList
	 * @param taskName
	 */
	public boolean importChangedOrderFromJsonToHistory(List<TradeInfoBean> orderInfoTotalList, List<String> jsonIdList, List<String> targetList, String taskName) {
		boolean isSuccess = true;
		
		if (orderInfoTotalList != null && orderInfoTotalList.size() > 0) {
			// 插入history表values部分准备
			String sqlBatch = getBatchChangedOrderHistorySqlData(orderInfoTotalList, taskName);
			
			int jsonIdSize = jsonIdList.size();
//			// 对应json信息表id值准备
//			StringBuilder sbJsonId = new StringBuilder(Constants.LEFT_BRACKET_CHAR);
//			for (int i = 0; i < jsonIdSize; i++) {
//				String jsonId = jsonIdList.get(i);
//				sbJsonId.append(jsonId);
//				
//				if (i < (jsonIdSize - 1)) {
//					sbJsonId.append(Constants.COMMA_CHAR);
//				}
//			}
//			sbJsonId.append(Constants.RIGHT_BRACKET_CHAR);
			
//			String jsonIdListStr = sbJsonId.toString();
//			logger.info("jsonId:" + jsonIdListStr);
			
			TransactionStatus status = transactionManager.getTransaction(def);
			
			try {
				// 状态变化订单json数据批量插入history表
				isSuccess = orderDao.insertChangedOrdersInfo(sqlBatch, orderInfoTotalList.size());
				// 插入history表成功
				if (isSuccess) {
					logger.info("状态变化订单json数据批量插入history表成功");
					
					// 更新id列表对应的json信息表发送标志
					isSuccess = orderDao.updateChangedSendFlag4Json(jsonIdList, targetList, jsonIdSize, taskName);
					if (isSuccess) {
						logger.info("置位状态变化json信息表发送标志成功");
					} else {
						logger.info("置位状态变化json信息表发送标志失败");
					}
				} else {
					logger.info("状态变化订单json数据批量插入history表失败");
				}
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
				
				isSuccess = false;
				
				issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
			} finally {
				if (isSuccess) {
					transactionManager.commit(status);
					
					logger.info("importChangedOrderFromJsonToHistory is commit");
					
				} else {
					transactionManager.rollback(status);
					
					logger.info("importChangedOrderFromJsonToHistory is rollback");
					
					issueLog.log("OrderInfoImportService.importChangedOrderFromJsonToHistory", 
							"importChangedOrderFromJsonToHistory is rollback", 
							ErrorType.BatchJob, SubSystem.OMS);
					
					// 批处理异常时置位2
					orderDao.updateChangedSendFlag4Json2(jsonIdList, targetList, jsonIdSize, taskName);
				}
			}
		}
		
		return isSuccess;
	}
	
	/**
	 * 状态变化新订单json信息转到history表中所需数据拼装
	 * 
	 * @return
	 */
	public String getBatchChangedOrderHistorySqlData(List<TradeInfoBean> orderInfoTotalList, String taskName) {
		StringBuilder sqlBuffer = new StringBuilder();
		
		int size = orderInfoTotalList.size();
		for (int i = 0; i < size; i++) {
			sqlBuffer.append(dataPrepare4TradeInfoBean(orderInfoTotalList.get(i), taskName));
			
			if (i < (size - 1)) {
				sqlBuffer.append(Constants.COMMA_CHAR);
			}
		}
		
		return sqlBuffer.toString();
		
	}
	
	/**
	 * 订单状态变化信息数据准备
	 * 
	 * @param bean
	 * @param taskName
	 * @return
	 */
	private String dataPrepare4TradeInfoBean(TradeInfoBean bean, String taskName) {
		StringBuilder sqlValueBuffer = new StringBuilder();
		
		sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
		
		// orderChannelId
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(bean.getChannelId());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// cartId
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(bean.getCartId());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// tid
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(bean.getTid());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// status
		String status = transferStr(bean.getStatus());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(status);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// modified
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(bean.getModified()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// buyerNick
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(bean.getBuyer_nick()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// oid
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(bean.getOid());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// sent_flag
		String sentFlag = Constants.ONE_CHAR;
		// TradeSuccess、RefundCreated、RefundBuyerModifyAgreement、RefundSuccess、RefundClosed、TradeClose
		// LOCKED
		// 这几种状态目前发送标志置为0推送更新，其余只接受但发送标志置为1不更新正式表
		if (Constants.ALIBABA_STATUS_TRADE_SUCCESS.equals(status) || 
			Constants.ALIBABA_STATUS_REFUND_CREATED.equals(status) ||
			Constants.ALIBABA_STATUS_REFUND_BUYER_MODIFY_AGREEMENT.equals(status) ||
			Constants.ALIBABA_STATUS_REFUND_SUCCESS.equals(status) ||
			Constants.ALIBABA_STATUS_REFUND_CLOSED.equals(status) ||
			Constants.ALIBABA_STATUS_TRADE_CLOSE.equals(status) ||
			Constants.JD_STATUS_LOCKED.equals(status)) {
			sentFlag = Constants.ZERO_CHAR;
		}
		
		sqlValueBuffer.append(sentFlag);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// sent_time
		sqlValueBuffer.append(Constants.NULL_STR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// rid
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(bean.getRid());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// refund_time
		sqlValueBuffer.append(Constants.NULL_STR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// refund_flag
		sqlValueBuffer.append(Constants.ZERO_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// target
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(bean.getTarget());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// fee
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(bean.getFee());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// refundPhase
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(StringUtils.null2Space2(bean.getRefundPhase()));
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
	 * 获取需批处理新订单信息
	 * 
	 * @return
	 */
	public List<NewOrderInfo4Log> getNewOrderInfo4BatchFromLog() {
		List<NewOrderInfo4Log> newOrderInfo = orderDao.getNewOrderInfo4BatchFromLog();
		return newOrderInfo;
	}
	
	/**
	 * 获取买就送赠品信息
	 * 
	 * @return
	 */
	public void getBuyThanGiftSetting() {
		if (BUY_THAN_GIFT_SETTING == null) {
			BUY_THAN_GIFT_SETTING = new HashMap<String, List<Map<String, String>>>();
		}
		if (!BUY_THAN_GIFT_SETTING.isEmpty()) {
			BUY_THAN_GIFT_SETTING.clear();
		}
		if (BUY_THAN_GIFT_SETTING_TRANS == null) {
			BUY_THAN_GIFT_SETTING_TRANS = new HashMap<String, List<Map<String, String>>>();
		}
		if (!BUY_THAN_GIFT_SETTING_TRANS.isEmpty()) {
			BUY_THAN_GIFT_SETTING_TRANS.clear();
		}
		if (BUY_THAN_GIFT_SETTING_EXT == null) {
			BUY_THAN_GIFT_SETTING_EXT = new HashMap<String, List<Map<String, String>>>();
		}
		if (!BUY_THAN_GIFT_SETTING_EXT.isEmpty()) {
			BUY_THAN_GIFT_SETTING_EXT.clear();
		}

		List<Map<String, String>> buyThanGiftList = orderDao.getBuyThanGiftSetting();
		if (buyThanGiftList != null && buyThanGiftList.size() > 0) {
			for (Map<String, String> buyThanGift : buyThanGiftList) {
				String orderChannelId = StringUtils.null2Space2(String.valueOf(buyThanGift.get("orderChannelId")));
				String cartId = StringUtils.null2Space2(String.valueOf(buyThanGift.get("cartId")));
				String sku = StringUtils.null2Space2(String.valueOf(buyThanGift.get("sku")));

				// 赠品sku
				String giftSku = StringUtils.null2Space2(String.valueOf(buyThanGift.get("giftSku")));
				giftSku = giftSku.toUpperCase();
				// 赠品sku指定顺序
				String giftSkuPrior = StringUtils.null2Space2(String.valueOf(buyThanGift.get("priorNum")));
				// 赠品sku库存
				String giftSkuInventory = StringUtils.null2Space2(String.valueOf(buyThanGift.get("inventory")));

				Map<String, String> giftSkuMap = new HashMap<String, String>();
				giftSkuMap.put("giftSku", giftSku);
				giftSkuMap.put("priorNum", giftSkuPrior);
				giftSkuMap.put("inventory", giftSkuInventory);
				
				String mapKey = orderChannelId + Constants.COMMA_CHAR + cartId + Constants.COMMA_CHAR + sku.toUpperCase();
				if (BUY_THAN_GIFT_SETTING.containsKey(mapKey)) {
					List<Map<String, String>> giftSkuList = BUY_THAN_GIFT_SETTING.get(mapKey);
					giftSkuList.add(giftSkuMap);
					
				} else {
					List<Map<String, String>> giftSkuList = new ArrayList<Map<String, String>>();
					giftSkuList.add(giftSkuMap);
					BUY_THAN_GIFT_SETTING.put(mapKey, giftSkuList);
				}

				Map<String, String> giftSkuMapTrans = new HashMap<String, String>();
				giftSkuMapTrans.put("giftSku", giftSku);
				giftSkuMapTrans.put("priorNum", giftSkuPrior);
				giftSkuMapTrans.put("inventory", giftSkuInventory);

				if (BUY_THAN_GIFT_SETTING_TRANS.containsKey(mapKey)) {
					List<Map<String, String>> giftSkuList = BUY_THAN_GIFT_SETTING_TRANS.get(mapKey);
					giftSkuList.add(giftSkuMapTrans);

				} else {
					List<Map<String, String>> giftSkuList = new ArrayList<Map<String, String>>();
					giftSkuList.add(giftSkuMapTrans);
					BUY_THAN_GIFT_SETTING_TRANS.put(mapKey, giftSkuList);
				}

				Map<String, String> giftSkuMapExt = new HashMap<String, String>();
				giftSkuMapExt.put("giftSku", giftSku);
				giftSkuMapExt.put("priorNum", giftSkuPrior);
				giftSkuMapExt.put("inventory", giftSkuInventory);

				if (BUY_THAN_GIFT_SETTING_EXT.containsKey(mapKey)) {
					List<Map<String, String>> giftSkuList = BUY_THAN_GIFT_SETTING_EXT.get(mapKey);
					giftSkuList.add(giftSkuMapExt);

				} else {
					List<Map<String, String>> giftSkuList = new ArrayList<Map<String, String>>();
					giftSkuList.add(giftSkuMapExt);
					BUY_THAN_GIFT_SETTING_EXT.put(mapKey, giftSkuList);
				}
				
			}
		}
	}
	
	/**
	 * 满就送赠品例外设定
	 * 
	 * @return
	 */
	public void getPriceThanGiftExceptSetting() {
		if (PRICE_THAN_GIFT_EXCEPT_SETTING == null) {
			PRICE_THAN_GIFT_EXCEPT_SETTING = new HashMap<String, List<String>>();
		}
		if (!PRICE_THAN_GIFT_EXCEPT_SETTING.isEmpty()) {
			PRICE_THAN_GIFT_EXCEPT_SETTING.clear();
		}
		
		List<Map<String, String>> priceThanGiftExceptList = orderDao.getPriceThanGiftExceptSetting();
		if (priceThanGiftExceptList != null && priceThanGiftExceptList.size() > 0) {
			for (Map<String, String> priceThanGiftExcept : priceThanGiftExceptList) {
				String orderChannelId = StringUtils.null2Space2(String.valueOf(priceThanGiftExcept.get("orderChannelId")));
				String cartId = StringUtils.null2Space2(String.valueOf(priceThanGiftExcept.get("cartId")));
				String exceptSku = StringUtils.null2Space2(String.valueOf(priceThanGiftExcept.get("exceptSku")));
				
				String mapKey = orderChannelId + cartId;
				if (PRICE_THAN_GIFT_EXCEPT_SETTING.containsKey(mapKey)) {
					List<String> exceptSkuList = PRICE_THAN_GIFT_EXCEPT_SETTING.get(mapKey);
					exceptSkuList.add(exceptSku.toUpperCase());
					
				} else {
					List<String> exceptSkuList = new ArrayList<String>();
					exceptSkuList.add(exceptSku.toUpperCase());
					PRICE_THAN_GIFT_EXCEPT_SETTING.put(mapKey, exceptSkuList);
				}
			}
		}
	}
	
	/**
	 * 满就送赠品设定
	 * 
	 * @return
	 */
	public void getPriceThanGiftSetting() {
		if (PRICE_THAN_GIFT_SETTING == null) {
			PRICE_THAN_GIFT_SETTING = new HashMap<String, List<Map<String, String>>>();
		}
		if (!PRICE_THAN_GIFT_SETTING.isEmpty()) {
			PRICE_THAN_GIFT_SETTING.clear();
		}
		if (PRICE_THAN_GIFT_SETTING_TRANS == null) {
			PRICE_THAN_GIFT_SETTING_TRANS = new HashMap<String, List<Map<String, String>>>();
		}
		if (!PRICE_THAN_GIFT_SETTING_TRANS.isEmpty()) {
			PRICE_THAN_GIFT_SETTING_TRANS.clear();
		}
		if (PRICE_THAN_GIFT_SETTING_EXT == null) {
			PRICE_THAN_GIFT_SETTING_EXT = new HashMap<String, List<Map<String, String>>>();
		}
		if (!PRICE_THAN_GIFT_SETTING_EXT.isEmpty()) {
			PRICE_THAN_GIFT_SETTING_EXT.clear();
		}
		if (PRICE_THAN_GIFT_PRICE_SETTING == null) {
			PRICE_THAN_GIFT_PRICE_SETTING = new HashMap<String, List<Double>>();
		}
		if (!PRICE_THAN_GIFT_PRICE_SETTING.isEmpty()) {
			PRICE_THAN_GIFT_PRICE_SETTING.clear();
		}

		List<Map<String, String>> priceThanGiftSettingList = orderDao.getPriceThanGiftSetting();
		if (priceThanGiftSettingList != null && priceThanGiftSettingList.size() > 0) {
			
			for (Map<String, String> priceThanGift : priceThanGiftSettingList) {
				String orderChannelId = StringUtils.null2Space2(String.valueOf(priceThanGift.get("orderChannelId")));
				String cartId = StringUtils.null2Space2(String.valueOf(priceThanGift.get("cartId")));
				
				String price = String.valueOf(priceThanGift.get("price"));
				double priceDouble = Double.parseDouble(price);
				DecimalFormat df = new DecimalFormat("#.00");
				price = df.format(priceDouble);

				// 赠品sku
				String giftSku = StringUtils.null2Space2(String.valueOf(priceThanGift.get("giftSku")));
				giftSku = giftSku.toUpperCase();
				// 赠品sku指定顺序
				String giftSkuPrior = StringUtils.null2Space2(String.valueOf(priceThanGift.get("priorNum")));
				// 赠品sku库存
				String giftSkuInventory = StringUtils.null2Space2(String.valueOf(priceThanGift.get("inventory")));
				Map<String, String> giftSkuMap = new HashMap<String, String>();
				giftSkuMap.put("giftSku", giftSku);
				giftSkuMap.put("priorNum", giftSkuPrior);
				giftSkuMap.put("inventory", giftSkuInventory);

				String mapKey = orderChannelId + Constants.COMMA_CHAR + cartId + Constants.COMMA_CHAR + price;
				if (PRICE_THAN_GIFT_SETTING.containsKey(mapKey)) {
					List<Map<String, String>> priceThanGiftSkuList = PRICE_THAN_GIFT_SETTING.get(mapKey);
					priceThanGiftSkuList.add(giftSkuMap);
					
				} else {
					List<Map<String, String>> priceThanGiftSkuList = new ArrayList<Map<String, String>>();
					priceThanGiftSkuList.add(giftSkuMap);
					PRICE_THAN_GIFT_SETTING.put(mapKey, priceThanGiftSkuList);

				}

				Map<String, String> giftSkuMapTrans = new HashMap<String, String>();
				giftSkuMapTrans.put("giftSku", giftSku);
				giftSkuMapTrans.put("priorNum", giftSkuPrior);
				giftSkuMapTrans.put("inventory", giftSkuInventory);
				if (PRICE_THAN_GIFT_SETTING_TRANS.containsKey(mapKey)) {
					List<Map<String, String>> priceThanGiftSkuList = PRICE_THAN_GIFT_SETTING_TRANS.get(mapKey);
					priceThanGiftSkuList.add(giftSkuMapTrans);

				} else {
					List<Map<String, String>> priceThanGiftSkuList = new ArrayList<Map<String, String>>();
					priceThanGiftSkuList.add(giftSkuMapTrans);
					PRICE_THAN_GIFT_SETTING_TRANS.put(mapKey, priceThanGiftSkuList);

				}

				Map<String, String> giftSkuMapExt = new HashMap<String, String>();
				giftSkuMapExt.put("giftSku", giftSku);
				giftSkuMapExt.put("priorNum", giftSkuPrior);
				giftSkuMapExt.put("inventory", giftSkuInventory);
				if (PRICE_THAN_GIFT_SETTING_EXT.containsKey(mapKey)) {
					List<Map<String, String>> priceThanGiftSkuList = PRICE_THAN_GIFT_SETTING_EXT.get(mapKey);
					priceThanGiftSkuList.add(giftSkuMapExt);

				} else {
					List<Map<String, String>> priceThanGiftSkuList = new ArrayList<Map<String, String>>();
					priceThanGiftSkuList.add(giftSkuMapExt);
					PRICE_THAN_GIFT_SETTING_EXT.put(mapKey, priceThanGiftSkuList);

				}

				String giftMapKey = orderChannelId + cartId;
				if (PRICE_THAN_GIFT_PRICE_SETTING.containsKey(giftMapKey)) {
					List<Double> priceList = PRICE_THAN_GIFT_PRICE_SETTING.get(giftMapKey);
					// 重复价格设定过滤
					if (!priceList.contains(priceDouble)) {
						priceList.add(priceDouble);
					}
					
				} else {
					List<Double> priceList = new ArrayList<Double>();
					priceList.add(priceDouble);
					PRICE_THAN_GIFT_PRICE_SETTING.put(giftMapKey, priceList);
				}
			}
			
			for (String key : PRICE_THAN_GIFT_PRICE_SETTING.keySet()) {
				List<Double> priceList = PRICE_THAN_GIFT_PRICE_SETTING.get(key);
				Collections.sort(priceList);
				Collections.reverse(priceList);
			}
		}
	}
	
	/**
	 * 老顾客送赠品设定
	 * 
	 * @return
	 */
	public void getRegularCustomerGiftSetting() {
		if (REGULAR_CUSTOMER_GIFT_SETTING == null) {
			REGULAR_CUSTOMER_GIFT_SETTING = new HashMap<String, List<String>>();
		}
		if (!REGULAR_CUSTOMER_GIFT_SETTING.isEmpty()) {
			REGULAR_CUSTOMER_GIFT_SETTING.clear();
		}
		
		List<Map<String, String>> regularCustomerGiftList = orderDao.getRegularCustomerGiftSetting();
		if (regularCustomerGiftList != null && regularCustomerGiftList.size() > 0) {
			for (Map<String, String> regularCustomerGift : regularCustomerGiftList) {
				String orderChannelId = StringUtils.null2Space2(String.valueOf(regularCustomerGift.get("orderChannelId")));
				String cartId = StringUtils.null2Space2(String.valueOf(regularCustomerGift.get("cartId")));
				String giftSku = StringUtils.null2Space2(String.valueOf(regularCustomerGift.get("giftSku")));
				
				String mapKey = orderChannelId + cartId;
				if (REGULAR_CUSTOMER_GIFT_SETTING.containsKey(mapKey)) {
					List<String> giftSkuList = REGULAR_CUSTOMER_GIFT_SETTING.get(mapKey);
					giftSkuList.add(giftSku.toUpperCase());
					
				} else {
					List<String> giftSkuList = new ArrayList<String>();
					giftSkuList.add(giftSku.toUpperCase());
					REGULAR_CUSTOMER_GIFT_SETTING.put(mapKey, giftSkuList);
				}
				
			}
		}
	}
	
	/**
	 * 优先下单前多少名赠品设定
	 * 
	 * @return
	 */
	public void getPriorCountGiftSetting() {
		if (PRIOR_COUNT_CUSTOMER_GIFT_SETTING == null) {
			PRIOR_COUNT_CUSTOMER_GIFT_SETTING = new HashMap<String, List<Map<String, String>>>();
		}
		if (!PRIOR_COUNT_CUSTOMER_GIFT_SETTING.isEmpty()) {
			PRIOR_COUNT_CUSTOMER_GIFT_SETTING.clear();
		}
		if (PRIOR_COUNT_CUSTOMER_GIFT_SETTING_TRANS == null) {
			PRIOR_COUNT_CUSTOMER_GIFT_SETTING_TRANS = new HashMap<String, List<Map<String, String>>>();
		}
		if (!PRIOR_COUNT_CUSTOMER_GIFT_SETTING_TRANS.isEmpty()) {
			PRIOR_COUNT_CUSTOMER_GIFT_SETTING_TRANS.clear();
		}
		if (PRIOR_COUNT_CUSTOMER_GIFT_SETTING_EXT == null) {
			PRIOR_COUNT_CUSTOMER_GIFT_SETTING_EXT = new HashMap<String, List<Map<String, String>>>();
		}
		if (!PRIOR_COUNT_CUSTOMER_GIFT_SETTING_EXT.isEmpty()) {
			PRIOR_COUNT_CUSTOMER_GIFT_SETTING_EXT.clear();
		}

		// 排除sku设置获得
		if (PRIOR_COUNT_CUSTOMER_GIFT_EXCEPT_SKU_SETTING == null) {
			PRIOR_COUNT_CUSTOMER_GIFT_EXCEPT_SKU_SETTING = new HashMap<String, List<String>>();
		}
		if (!PRIOR_COUNT_CUSTOMER_GIFT_EXCEPT_SKU_SETTING.isEmpty()) {
			PRIOR_COUNT_CUSTOMER_GIFT_EXCEPT_SKU_SETTING.clear();
		}

		List<Map<String, String>> priorCountGiftList = orderDao.getPriorCountGiftSetting();
		if (priorCountGiftList != null && priorCountGiftList.size() > 0) {
			for (Map<String, String> priorCountGift : priorCountGiftList) {
				String orderChannelId = StringUtils.null2Space2(String.valueOf(priorCountGift.get("orderChannelId")));
				String cartId = StringUtils.null2Space2(String.valueOf(priorCountGift.get("cartId")));

				// 赠品sku
				String giftSku = StringUtils.null2Space2(String.valueOf(priorCountGift.get("giftSku")));
				giftSku = giftSku.toUpperCase();
				// 赠品sku指定顺序
				String giftSkuPrior = StringUtils.null2Space2(String.valueOf(priorCountGift.get("priorNum")));
				// 赠品sku库存
				String giftSkuInventory = StringUtils.null2Space2(String.valueOf(priorCountGift.get("inventory")));

				String mapKey = orderChannelId + Constants.COMMA_CHAR + cartId;

				Map<String, String> giftSkuMap = new HashMap<String, String>();
				giftSkuMap.put("giftSku", giftSku);
				giftSkuMap.put("priorNum", giftSkuPrior);
				giftSkuMap.put("inventory", giftSkuInventory);
				if (PRIOR_COUNT_CUSTOMER_GIFT_SETTING.containsKey(mapKey)) {
					List<Map<String, String>> giftSkuList = PRIOR_COUNT_CUSTOMER_GIFT_SETTING.get(mapKey);
					giftSkuList.add(giftSkuMap);
					
				} else {
					List<Map<String, String>> giftSkuList = new ArrayList<Map<String, String>>();
					giftSkuList.add(giftSkuMap);
					PRIOR_COUNT_CUSTOMER_GIFT_SETTING.put(mapKey, giftSkuList);
				}

				Map<String, String> giftSkuMapTrans = new HashMap<String, String>();
				giftSkuMapTrans.put("giftSku", giftSku);
				giftSkuMapTrans.put("priorNum", giftSkuPrior);
				giftSkuMapTrans.put("inventory", giftSkuInventory);
				if (PRIOR_COUNT_CUSTOMER_GIFT_SETTING_TRANS.containsKey(mapKey)) {
					List<Map<String, String>> giftSkuList = PRIOR_COUNT_CUSTOMER_GIFT_SETTING_TRANS.get(mapKey);
					giftSkuList.add(giftSkuMapTrans);

				} else {
					List<Map<String, String>> giftSkuList = new ArrayList<Map<String, String>>();
					giftSkuList.add(giftSkuMapTrans);
					PRIOR_COUNT_CUSTOMER_GIFT_SETTING_TRANS.put(mapKey, giftSkuList);
				}

				Map<String, String> giftSkuMapExt = new HashMap<String, String>();
				giftSkuMapExt.put("giftSku", giftSku);
				giftSkuMapExt.put("priorNum", giftSkuPrior);
				giftSkuMapExt.put("inventory", giftSkuInventory);
				if (PRIOR_COUNT_CUSTOMER_GIFT_SETTING_EXT.containsKey(mapKey)) {
					List<Map<String, String>> giftSkuList = PRIOR_COUNT_CUSTOMER_GIFT_SETTING_EXT.get(mapKey);
					giftSkuList.add(giftSkuMapExt);

				} else {
					List<Map<String, String>> giftSkuList = new ArrayList<Map<String, String>>();
					giftSkuList.add(giftSkuMapExt);
					PRIOR_COUNT_CUSTOMER_GIFT_SETTING_EXT.put(mapKey, giftSkuList);
				}
				
			}
		}

		List<Map<String, String>> priorCountExceptSkuList = orderDao.getPriorCountExceptSkuSetting();
		if (priorCountExceptSkuList != null && priorCountExceptSkuList.size() > 0) {
			for (Map<String, String> priorCountExceptSku : priorCountExceptSkuList) {
				String orderChannelId = StringUtils.null2Space2(String.valueOf(priorCountExceptSku.get("orderChannelId")));
				String cartId = StringUtils.null2Space2(String.valueOf(priorCountExceptSku.get("cartId")));

				// 排除的sku
				String exceptSku = StringUtils.null2Space2(String.valueOf(priorCountExceptSku.get("exceptSku")));
				exceptSku = exceptSku.toUpperCase();

				String mapKey = orderChannelId + Constants.COMMA_CHAR + cartId;

				if (PRIOR_COUNT_CUSTOMER_GIFT_EXCEPT_SKU_SETTING.containsKey(mapKey)) {
					List<String> exceptSkuList = PRIOR_COUNT_CUSTOMER_GIFT_EXCEPT_SKU_SETTING.get(mapKey);
					exceptSkuList.add(exceptSku);

				} else {
					List<String> exceptSkuList = new ArrayList<String>();
					exceptSkuList.add(exceptSku);
					PRIOR_COUNT_CUSTOMER_GIFT_EXCEPT_SKU_SETTING.put(mapKey, exceptSkuList);
				}
			}
		}
	}
	
	/**
	 * 已送赠品顾客信息获得
	 * 
	 * @return
	 */
	public void getHavingGiftedCustomerInfo() {
		if (HAVING_GIFTED_CUSTOMER_INFO == null) {
			HAVING_GIFTED_CUSTOMER_INFO = new HashMap<String, Object>();
		}
		if (!HAVING_GIFTED_CUSTOMER_INFO.isEmpty()) {
			HAVING_GIFTED_CUSTOMER_INFO.clear();
		}
		if (HAVING_GIFTED_CUSTOMER_INFO_LOCAL == null) {
			HAVING_GIFTED_CUSTOMER_INFO_LOCAL = new HashMap<String, Object>();
		}
		if (!HAVING_GIFTED_CUSTOMER_INFO_LOCAL.isEmpty()) {
			HAVING_GIFTED_CUSTOMER_INFO_LOCAL.clear();
		}
		
		// 获得已获赠品的顾客列表
		List<Map<String, String>> havingGiftedCustomerInfoList = orderDao.getHavingGiftedCustomerInfo();
		if (havingGiftedCustomerInfoList != null && havingGiftedCustomerInfoList.size() > 0) {
			for (Map<String, String> havingGiftedCustomerInfo : havingGiftedCustomerInfoList) {
				// 店铺
				String orderChannelId = StringUtils.null2Space2(String.valueOf(havingGiftedCustomerInfo.get("orderChannelId")));
				// 销售平台
				String platformId = StringUtils.null2Space2(String.valueOf(havingGiftedCustomerInfo.get("platformId")));
				// 下单顾客
				String buyerNick = StringUtils.null2Space2(String.valueOf(havingGiftedCustomerInfo.get("buyerNick")));
				// 赠品类型
				String type = StringUtils.null2Space2(String.valueOf(havingGiftedCustomerInfo.get("type")));
				
				String mapKeyName = orderChannelId + Constants.COMMA_CHAR + platformId + Constants.COMMA_CHAR + type + Constants.COMMA_CHAR + OmsConstants.HAVING_GIFTED_CUSTOMER_NAME;
				String mapKeyCount = orderChannelId + Constants.COMMA_CHAR + platformId + Constants.COMMA_CHAR + type + Constants.COMMA_CHAR + OmsConstants.HAVING_GIFTED_CUSTOMER_COUNT;
				
				if (HAVING_GIFTED_CUSTOMER_INFO.containsKey(mapKeyName)) {
					List<String> giftedCustomerList = (List<String>) HAVING_GIFTED_CUSTOMER_INFO.get(mapKeyName);
					giftedCustomerList.add(buyerNick);
					HAVING_GIFTED_CUSTOMER_INFO.put(mapKeyCount, giftedCustomerList.size());
					
				} else {
					List<String> giftedCustomerList = new ArrayList<String>();
					giftedCustomerList.add(buyerNick);
					HAVING_GIFTED_CUSTOMER_INFO.put(mapKeyName, giftedCustomerList);
					HAVING_GIFTED_CUSTOMER_INFO.put(mapKeyCount, 1);
				}
			}
		}
	}
	
	/**
	 * 赠品类型信息设置表获得
	 * 
	 * @return
	 */
	public void getGiftedPropertySetting() {
		if (GIFT_PROPERTY_SETTING == null) {
			GIFT_PROPERTY_SETTING = new HashMap<String, String>();
		}
		if (!GIFT_PROPERTY_SETTING.isEmpty()) {
			GIFT_PROPERTY_SETTING.clear();
		}
		
		// 获得赠品类型信息设置
		List<Map<String, String>> giftedPropertyInfoList = orderDao.getGiftedPropertySetting();
		
		if (giftedPropertyInfoList != null && giftedPropertyInfoList.size() > 0) {
			for (Map<String, String> giftedPropertyInfo : giftedPropertyInfoList) {
				String orderChannelId = StringUtils.null2Space2(String.valueOf(giftedPropertyInfo.get("orderChannelId")));
				String cartPlatformId = StringUtils.null2Space2(String.valueOf(giftedPropertyInfo.get("cartPlatformId")));
				String type = StringUtils.null2Space2(String.valueOf(giftedPropertyInfo.get("type")));
				String value = StringUtils.null2Space2(String.valueOf(giftedPropertyInfo.get("value")));
				
				String mapKey = orderChannelId + cartPlatformId + type;
				
				if (!GIFT_PROPERTY_SETTING.containsKey(mapKey)) {
					GIFT_PROPERTY_SETTING.put(mapKey, value);
				}
			}
		}
	}
	
//	/**
//	 * 满就送金额设定
//	 * 
//	 * @return
//	 */
//	public void getPriceThanGiftPriceSetting(Map<String, String> map) {
//		if (map == null) {
//			map = new HashMap<String, String>();
//		}
//		if (!map.isEmpty()) {
//			map.clear();
//		}
//		
//		List<Map<String, String>> priceThanGiftPriceSettingList = orderDao.getPriceThanGiftPriceSetting();
//		if (priceThanGiftPriceSettingList != null && priceThanGiftPriceSettingList.size() > 0) {
//			for (Map<String, String> priceThanGift : priceThanGiftPriceSettingList) {
//				String orderChannelId = StringUtils.null2Space2(String.valueOf(priceThanGift.get("orderChannelId")));
//				String cartId = StringUtils.null2Space2(String.valueOf(priceThanGift.get("cartId")));
//				
//				String price = String.valueOf(priceThanGift.get("price"));
//				double priceDouble = Double.parseDouble(price);
//				DecimalFormat df = new DecimalFormat("#.00");
//				price = df.format(priceDouble);
//				
//				String giftSku = StringUtils.null2Space2(String.valueOf(priceThanGift.get("giftSku")));
//				
//				map.put(orderChannelId + cartId + price, giftSku.toUpperCase());
//			}
//		}
//	}

	/**
	 * 获取套装设定信息
	 *
	 * @return
	 */
	public void getSuitSkuSetting() {
		if (SUIT_SKU_SETTING == null) {
			SUIT_SKU_SETTING = new HashMap<String, List<String>>();
		}
		if (!SUIT_SKU_SETTING.isEmpty()) {
			SUIT_SKU_SETTING.clear();
		}

		List<Map<String, String>> suitSkuSettingList = orderDao.getSuitSkuSetting();
		if (suitSkuSettingList != null && suitSkuSettingList.size() > 0) {
			for (Map<String, String> suitSku : suitSkuSettingList) {
				String orderChannelId = StringUtils.null2Space2(String.valueOf(suitSku.get("orderChannelId")));
				String cartId = StringUtils.null2Space2(String.valueOf(suitSku.get("cartId")));
				String sku = StringUtils.null2Space2(String.valueOf(suitSku.get("sku")));
				String realSku = StringUtils.null2Space2(String.valueOf(suitSku.get("realSku")));
				String realSkuPrice = StringUtils.null2Space2(String.valueOf(suitSku.get("realSkuPrice")));
				String realSkuName = StringUtils.null2Space2(String.valueOf(suitSku.get("realSkuName")));

				String mapKey = orderChannelId + cartId + sku.toUpperCase();
				if (SUIT_SKU_SETTING.containsKey(mapKey)) {
					List<String>  realSkuList = SUIT_SKU_SETTING.get(mapKey);
					realSkuList.add(realSku.toUpperCase() + Constants.SPLIT_CHAR_ADD + realSkuPrice + Constants.SPLIT_CHAR_ADD + realSkuName);

				} else {
					List<String> realSkuList = new ArrayList<String>();
					realSkuList.add(realSku.toUpperCase() + Constants.SPLIT_CHAR_ADD + realSkuPrice + Constants.SPLIT_CHAR_ADD + realSkuName);
					SUIT_SKU_SETTING.put(mapKey, realSkuList);
				}

			}
		}
	}

	/**
	 * 获取需批处理新订单信息(手工)
	 * 
	 * @return
	 */
	public List<NewOrderInfo4Log> getManualNewOrderInfo4BatchFromLog() {
		List<NewOrderInfo4Log> newOrderInfo = orderDao.getManualNewOrderInfo4BatchFromLog();
		return newOrderInfo;
	}
	
	/**
	 * 批处理顾客表所需数据拼装
	 * 
	 * @return
	 */
	public String getBatchCustomerSqlData(List<NewOrderInfo4Log> newOrderInfoList, String taskName) {
		StringBuilder sqlBuffer = new StringBuilder();
		
		int size = newOrderInfoList.size();
		for (int i = 0; i < size; i++) {
			sqlBuffer.append(prepareCustomerData(newOrderInfoList.get(i), taskName));
			
			if (i < (size - 1)) {
				sqlBuffer.append(Constants.COMMA_CHAR);
			}
		}
		
		return sqlBuffer.toString();
		
	}
	
	/**
	 * 一条顾客信息的插入语句values部分
	 * 
	 * @param newOrderInfo
	 * @param taskName
	 * @return
	 */
	private String prepareCustomerData(NewOrderInfo4Log newOrderInfo, String taskName) {
		StringBuilder sqlValueBuffer = new StringBuilder();
		
		sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
		
		String lastName = transferStr(newOrderInfo.getBillingBuyerNick());
		// full_name
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(lastName);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// last_name
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(lastName);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// email
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getBillingBuyerEmail()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// address
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getBillingAddressStreet1()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// address2
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getBillingAddressStreet2()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// city
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getBillingAddressCity()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// state
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getBillingAddressState()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// zip
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getBillingAddressZip()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// country
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getBillingAddressCountry()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// platform_id
		CartBean cartBean = ShopConfigs.getCart(newOrderInfo.getCartId());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(cartBean.getPlatform_id());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// order_channel_id
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(newOrderInfo.getOrderChannelId());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// created
		sqlValueBuffer.append(Constants.NOW_MYSQL);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// creater
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(taskName);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// modified
		sqlValueBuffer.append(Constants.NOW_MYSQL);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// modifier
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(taskName);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		
		sqlValueBuffer.append(Constants.RIGHT_BRACKET_CHAR);
		
		return sqlValueBuffer.toString();
	}
	
	
	/**
	 * 标注属于老顾客的订单
	 * 
	 * @return
	 */
	public void getRegularCustomer(List<NewOrderInfo4Log> newOrderInfoList) {
		int size = newOrderInfoList.size();
		
		for (int i = 0; i < size; i++) {
			NewOrderInfo4Log orderInfo = newOrderInfoList.get(i);
			
			Map<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("lastName", orderInfo.getBillingBuyerNick());
			dataMap.put("orderChannelId", orderInfo.getOrderChannelId());
			dataMap.put("cartId", orderInfo.getCartId());
			
			boolean isRegularCustomer = customerDao.getRegularCustomer(dataMap);
			orderInfo.setRegularCustomer(isRegularCustomer);
		}
	}
	
	/**
	 * 批处理插入顾客表数据（字符串拼装）
	 * 
	 * @param customerSqlValue
	 * @return
	 */
	public boolean insertCustomerBatchData(String customerSqlValue) {
		return customerDao.insertCustomerBatchData(customerSqlValue);
	}
	
	/**
	 * 批处理插入顾客表数据（集合）
	 * 
	 * @param newOrderInfoList
	 * @return
	 */
	public boolean insertCustomerBatchData(List<NewOrderInfo4Log> newOrderInfoList) {
		return customerDao.insertCustomerBatchData(newOrderInfoList);
	}
	
	/**
	 * 单条插入顾客表数据
	 * 
	 * @param newOrderInfo
	 * @param taskName
	 * @return
	 */
	public boolean insertCustomerData(NewOrderInfo4Log newOrderInfo, String taskName) {
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("billingBuyerNick", newOrderInfo.getBillingBuyerNick());
		dataMap.put("billingBuyerEmail", newOrderInfo.getBillingBuyerEmail());
		dataMap.put("billingAddressStreet1", newOrderInfo.getBillingAddressStreet1());
		dataMap.put("billingAddressStreet2", newOrderInfo.getBillingAddressStreet2());
		dataMap.put("billingAddressCity", newOrderInfo.getBillingAddressCity());
		dataMap.put("billingAddressState", newOrderInfo.getBillingAddressState());
		dataMap.put("billingAddressZip", newOrderInfo.getBillingAddressZip());
		dataMap.put("billingAddressCountry", newOrderInfo.getBillingAddressCountry());
		CartBean cartBean = ShopConfigs.getCart(newOrderInfo.getCartId());
		dataMap.put("platformId", cartBean.getPlatform_id());
		dataMap.put("taskName", taskName);
		
		return customerDao.insertCustomerData(dataMap);
	}
	
	/**
	 * 一次性取出要插入的订单对应的customerId列表
	 * 
	 * @param newOrderInfoList
	 * @return
	 */
	public List<String> getOrderCustomerIdList(List<NewOrderInfo4Log> newOrderInfoList) {
		List<Map<String, String>> customerMapList = new ArrayList<Map<String, String>>();
		for (NewOrderInfo4Log newOrder : newOrderInfoList) {
			Map<String, String> customerMap = new HashMap<String, String>();
			// last_name
			String lastName = newOrder.getBillingBuyerNick();
			// cartid -> platformId 
			CartBean cartBean = ShopConfigs.getCart(newOrder.getCartId());
			String platformId = cartBean.getPlatform_id();
			// order_channel_id
			String orderChannelId = newOrder.getOrderChannelId();
			
			customerMap.put("lastName", lastName);
			customerMap.put("platformId", platformId);
			customerMap.put("orderChannelId", orderChannelId);
			customerMapList.add(customerMap);
		}
		
		List<String> orderCustomerIdList = customerDao.selectCustomerIdList(customerMapList);
		return orderCustomerIdList;
	}
	
	/**
	 * 批处理新订单信息所需数据拼装
	 * 
	 * @param newOrderInfoList
	 * @param orderCustomerIdList
	 * @param taskName
	 * @return
	 */
	public String getBatchOrdersSqlData(List<NewOrderInfo4Log> newOrderInfoList, 
			List<String> orderCustomerIdList, String taskName) {
		StringBuilder sqlBuffer = new StringBuilder();
		
		int size = newOrderInfoList.size();
		for (int i = 0; i < size; i++) {
			// 取得最新订单号采番
			long orderNumber = orderDao.getOrderNumber();
			
			// 新订单信息
			NewOrderInfo4Log newOrderInfo = newOrderInfoList.get(i);
			// 保存预处理订单号
			newOrderInfo.setPreOrderNumber(String.valueOf(orderNumber));
			
			// customerId
			String customerId = orderCustomerIdList.get(i);
			// 拼装SQL values 部分
			sqlBuffer.append(prepareOrderData(newOrderInfo, customerId, taskName));
			
			if (i < (size - 1)) {
				sqlBuffer.append(Constants.COMMA_CHAR);
			}
		}
		
		return sqlBuffer.toString();
		
	}
	
	/**
	 * 单条处理新订单信息所需数据拼装
	 * 
	 * @param newOrderInfoList
	 * @param orderCustomerIdList
	 * @param taskName
	 * @return
	 */
	public String getEachOrdersSqlData(List<NewOrderInfo4Log> newOrderInfoList, 
			List<String> orderCustomerIdList, String taskName) {
		StringBuilder sqlBuffer = new StringBuilder();
		
		int size = newOrderInfoList.size();
		for (int i = 0; i < size; i++) {
			
			// 新订单信息
			NewOrderInfo4Log newOrderInfo = newOrderInfoList.get(i);
			String preOrderNumber = newOrderInfo.getPreOrderNumber();
			
			if (StringUtils.isNullOrBlank2(preOrderNumber)) {
				// 取得最新订单号采番
				long orderNumber = orderDao.getOrderNumber();
				// 保存预处理订单号
				newOrderInfo.setPreOrderNumber(String.valueOf(orderNumber));
			}
			
			// customerId
			String customerId = orderCustomerIdList.get(i);
			// 拼装SQL values 部分
			sqlBuffer.append(prepareOrderData(newOrderInfo, customerId, taskName));
			
			if (i < (size - 1)) {
				sqlBuffer.append(Constants.COMMA_CHAR);
			}
		}
		
		return sqlBuffer.toString();
		
	}
	
	/**
	 * 一条订单的插入orders表语句values部分
	 * 
	 * @param newOrderInfo
	 * @param customerId
	 * @param taskName
	 * @return
	 */
	private String prepareOrderData(NewOrderInfo4Log newOrderInfo, String customerId, String taskName) {
		StringBuilder sqlValueBuffer = new StringBuilder();
		
		sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
		
		// order_number
		sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// order_date_time
		String payTime = newOrderInfo.getPayTime();
		// 时区转换
		String gmtPayTime = DateTimeUtil.getGMTTime(payTime, OmsConstants.TIME_ZONE_8);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(gmtPayTime);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// customer_id
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(customerId);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// name
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getBillingBuyerNick()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// company
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getBillingBuyerCompany()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// email
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getBillingBuyerEmail()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// address
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getBillingAddressStreet1()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// address2
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getBillingAddressStreet2()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// city
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		String city = transferStr(newOrderInfo.getBillingAddressCity());
		sqlValueBuffer.append(city);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// state
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		String state = transferStr(newOrderInfo.getBillingAddressState());
		sqlValueBuffer.append(state);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// zip
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		String zip = getZipFromAddress(StringUtils.null2Space2(state), StringUtils.null2Space2(city));
		if (Constants.EMPTY_STR.equals(zip)) {
			zip = newOrderInfo.getBillingAddressZip();
		}
		sqlValueBuffer.append(transferStr(zip));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// country
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getBillingAddressCountry()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// phone
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getBillingBuyerPhone()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// ship_name
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getShippingReceiverName()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// ship_company
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getShippingReceiverCompany()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// ship_address
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getShippingAddressStreet1()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// ship_address2
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getShippingAddressStreet2()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// ship_city
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getShippingAddressCity()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// ship_state
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getShippingAddressState()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// ship_zip
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(zip));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// ship_country
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getShippingAddressCountry()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// ship_phone
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getShippingReceiverPhone()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// pay_type
		String payTypeName = newOrderInfo.getPaymentGeneric1Name();
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Type.getValue(MastType.paymentMethod.getId(), payTypeName, com.voyageone.common.Constants.LANGUAGE.EN));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// pay_no
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getPaymentGeneric1Field1()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// pay_account
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getPaymentGeneric1Field2()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// customer_comments
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getOtherComments()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// product_total
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(newOrderInfo.getTotalsTotalFee());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// shipping_total
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(newOrderInfo.getTotalsPostFee());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// grand_total
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(newOrderInfo.getTotalsPayment());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// shipping
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getTotalsShippingType()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// discount
		String disCountStrs = newOrderInfo.getDiscountAmount();
		double disCount = getTotalAmountFromStr(disCountStrs);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(-disCount);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// revised_discount
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(-disCount);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// surcharge
		String surchargeStrs = newOrderInfo.getSurchargeAmount();
		double surcharge = getTotalAmountFromStr(surchargeStrs);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(surcharge);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// revised_surcharge
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(surcharge);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// 店铺渠道
		String orderChannelId = newOrderInfo.getOrderChannelId();
		// approved
		boolean isApproved = false;
		// 斯伯丁
		if (OmsConstants.CHANNEL_SPALDING.equals(orderChannelId)) {
			isApproved = isAutoApprovedSpalding(newOrderInfo);
		} else {
			isApproved = isAutoApproved(newOrderInfo);
		}
		newOrderInfo.setApproved(isApproved);
		sqlValueBuffer.append(isApproved);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// cancelled
		sqlValueBuffer.append(false);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// final_product_total
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(newOrderInfo.getTotalsTotalFee());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// final_shipping_total
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(newOrderInfo.getTotalsPostFee());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// final_grand_total
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(newOrderInfo.getTotalsPayment());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// balance_due
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(newOrderInfo.getTotalsPayment());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// discount_type
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(disCount > 0 ? Constants.DISCOUNT_NORMAL : Constants.DISCOUNT_NO);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// lock_ship
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append("NO");
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// source_order_id
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(newOrderInfo.getTid());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// origin_source_order_id
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(newOrderInfo.getTid());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// order_status
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(isApproved ? 
				Type.getValue(MastType.orderStatus.getId(), Constants.ORDER_STATUS_APPROVED, com.voyageone.common.Constants.LANGUAGE.EN) : 
				Type.getValue(MastType.orderStatus.getId(), Constants.ORDER_STATUS_IN_PROCESSING, com.voyageone.common.Constants.LANGUAGE.EN));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// cart_id
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(newOrderInfo.getCartId());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// approval_date
		sqlValueBuffer.append(isApproved ? Constants.NOW_MYSQL : Constants.NULL_STR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// order_channel_id
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(orderChannelId);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// invoice_info
		String invoiceInfo = transferStr(newOrderInfo.getOtherInvoiceInfo());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(invoiceInfo);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// need_invoice
		String needInvoice = Constants.EMPTY_STR;
		if (!StringUtils.isNullOrBlank2(invoiceInfo)) {
			if (invoiceInfo.contains("不需要") || invoiceInfo.contains(":否")) {
				needInvoice = "NO";
			} else {
				needInvoice = "YES";
			}
		}
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(needInvoice);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// order_kind
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		// 原始订单
		String orderType = Type.getValue(MastType.orderType.getId(), Constants.ORDER_TYPE_ORIGINAL, com.voyageone.common.Constants.LANGUAGE.EN);
		// 是手工订单的话，要看具体设定类型
		if (!StringUtils.isNullOrBlank2(newOrderInfo.getOrderKind())) {
			orderType = Type.getValue(MastType.orderType.getId(), newOrderInfo.getOrderKind(), com.voyageone.common.Constants.LANGUAGE.EN);
		}
		sqlValueBuffer.append(orderType);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// price_difference_flag 差价标志
		boolean priceDifferenceFlag = false;
		if (isPriceGap(newOrderInfo)) {
			priceDifferenceFlag = true;
		}
		sqlValueBuffer.append(priceDifferenceFlag);
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
	 * 根据省市获得邮编
	 *
	 * @param state
	 * @param city
	 */
	private String getZipFromAddress(String state, String city) {
		String zip = orderDao.getZip(state, city);
		if (StringUtils.isNullOrBlank2(zip)) {
			zip = Constants.EMPTY_STR;
		}

		return zip;
	}
	
	/**
	 * 判断是否差价订单
	 * 
	 * @param newOrderInfo
	 * @return
	 */
	private boolean isPriceGap(NewOrderInfo4Log newOrderInfo) {
		boolean isPriceGap = false;
		
		String skus = newOrderInfo.getProductSku();
		if (!StringUtils.isNullOrBlank2(skus)) {
			String[] skuArray = skus.split(Constants.SPLIT_CHAR_RESOLVE);
			if (skuArray != null && skuArray.length > 0) {
				
				for (int i = 0; i < skuArray.length; i++) {
					String sku = skuArray[i];
					
					// 补差价订单
					if (OmsConstants.PRICES_GAP.equalsIgnoreCase(sku)) {
						isPriceGap = true;
						break;
					}
				}
			}
		}
		
		return isPriceGap;
	}
	
	/**
	 * 自动Apporved判断
	 * 
	 * @param newOrderInfo
	 * @return
	 */
	private boolean isAutoApproved(NewOrderInfo4Log newOrderInfo) {
		boolean isAutoApproved = false;
		
		String skusGap = newOrderInfo.getProductSku();
		// sku种类数
		int skuKinds = 0;
		if (!StringUtils.isNullOrBlank2(skusGap)) {
			
			String[] skuArray = skusGap.split(Constants.SPLIT_CHAR_RESOLVE);
			if (skuArray != null && skuArray.length > 0) {
				
				skuKinds = skuArray.length;
				
				// 含有补差价订单、没有sku错误商品的订单不允许自动Approved
				for (String sku : skuArray) {
					if (OmsConstants.PRICES_GAP.equalsIgnoreCase(sku) || OmsConstants.PRICES_GAP_SELF.equalsIgnoreCase(sku) || 
						OmsConstants.NO_SKU_PRODUCT.equals(sku)) {
						
						return isAutoApproved;
					}
				}
			}
		}
		
		// 获取该渠道自动approved配置
		String isApproved = ChannelConfigs.getVal1(newOrderInfo.getOrderChannelId(), Name.auto_approved);
		// 允许自动Approved
		if (Constants.ONE_CHAR.equals(isApproved)) {
			
			// 买家留言订单是否不允许自动Approved
			isApproved = ChannelConfigs.getVal1(newOrderInfo.getOrderChannelId(), Name.approved_except_buyer_message);
			if (Constants.ONE_CHAR.equals(isApproved)) {
				// 买家留言
				String buyerMessage = newOrderInfo.getOtherComments();
				if (!StringUtils.isNullOrBlank2(buyerMessage)) {
					return isAutoApproved;
				}
			}
			
			// 获取该笔订单件数
			int orderQuantity = 0;
			String skus = newOrderInfo.getProductSku();
			if (!StringUtils.isNullOrBlank2(skus)) {
				
				String[] skuArray = skus.split(Constants.SPLIT_CHAR_RESOLVE);
				if (skuArray != null && skuArray.length > 0) {
					// 数量
					String quantitys = newOrderInfo.getProductQuantity();
					String[] quantityArray = quantitys.split(Constants.SPLIT_CHAR_RESOLVE);
					
					for (int i = 0; i < skuArray.length; i++) {
						int quantity = Integer.valueOf(quantityArray[i]);
						orderQuantity += quantity;
					}
				}
			}
			
			// 订单物品数1件, 无需拆单
			if (orderQuantity <= 1) {
				isAutoApproved = true;
			
			// 订单物品数1件以上 
			} else {
				// 最大允许Approve件数( 例：<=7)
				String max_amount = ChannelConfigs.getVal1(newOrderInfo.getOrderChannelId(), Name.approved_max_amount);
				int maxAmount = 0;
				if (!StringUtils.isEmpty(max_amount)) {
					maxAmount = Integer.valueOf(max_amount);
					
					// 物品数在范围之内
					if (orderQuantity <= maxAmount) {
						// 最大允许Approve金额（人民币）
						String maxGrandTotal = ChannelConfigs.getVal1(newOrderInfo.getOrderChannelId(), Name.approved_max_grand_total);
						if (!StringUtils.isEmpty(maxGrandTotal)) {
							String grandTotal = newOrderInfo.getTotalsPayment();
							if (Double.valueOf(grandTotal) <= Double.valueOf(maxGrandTotal)) {
								isAutoApproved = true;
							}
						} else {
							isAutoApproved = true;
						}
					}
				} else {
					// 最大允许Approve金额（人民币）
					String maxGrandTotal = ChannelConfigs.getVal1(newOrderInfo.getOrderChannelId(), Name.approved_max_grand_total);
					if (!StringUtils.isEmpty(maxGrandTotal)) {
						String grandTotal = newOrderInfo.getTotalsPayment();
						if (Double.valueOf(grandTotal) <= Double.valueOf(maxGrandTotal)) {
							isAutoApproved = true;
						}
					} else {
						isAutoApproved = true;
					}
				}
				
				// sku种类数
				String skuKindNumber = ChannelConfigs.getVal1(newOrderInfo.getOrderChannelId(), Name.approved_max_sku_kind_number);
				if (!StringUtils.isEmpty(skuKindNumber)) {
					if (skuKinds > Integer.valueOf(skuKindNumber)) {
						isAutoApproved = false;
					}
				}
			}

			// 非真实姓名的限制
			if (isAutoApproved) {
				String isTrueNameCheck = ChannelConfigs.getVal1(newOrderInfo.getOrderChannelId(), Name.is_true_name_check);
				if (Constants.ONE_CHAR.equals(isTrueNameCheck)) {
					String shipName = newOrderInfo.getShippingReceiverName();
					isAutoApproved = checkShipName(shipName);
				}
			}

			// 地址中包含菜鸟驿站的订单是否允许自动Arrpoved
			if (isAutoApproved) {
				String isCaiNiaoYiZhanCheck = ChannelConfigs.getVal1(newOrderInfo.getOrderChannelId(), Name.is_cainiaoyizhan_check);
				if (Constants.ONE_CHAR.equals(isCaiNiaoYiZhanCheck)) {
					String shipAddressStreet2 = transferStr(newOrderInfo.getShippingAddressStreet2());
					if (shipAddressStreet2.contains("菜鸟驿站")) {
						isAutoApproved = false;
					}
				}
			}
		}
		
		return isAutoApproved;
	}
	
	/**
	 * spalding自动Apporved判断
	 * 
	 * @param newOrderInfo
	 * @return
	 */
	private boolean isAutoApprovedSpalding(NewOrderInfo4Log newOrderInfo) {
		boolean isAutoApproved = false;
		
		String skusGap = newOrderInfo.getProductSku();
		if (!StringUtils.isNullOrBlank2(skusGap)) {
			
			String[] skuArray = skusGap.split(Constants.SPLIT_CHAR_RESOLVE);
			if (skuArray != null && skuArray.length > 0) {
				
				// 含有补差价订单、没有sku错误商品的订单不允许自动Approved
				for (String sku : skuArray) {
					if (OmsConstants.PRICES_GAP.equalsIgnoreCase(sku) || OmsConstants.PRICES_GAP_SELF.equalsIgnoreCase(sku) || 
						OmsConstants.NO_SKU_PRODUCT.equals(sku)) {
						
						return isAutoApproved;
					}
				}
			}
		}
		
		// 获取该渠道自动approved配置
		String isApproved = ChannelConfigs.getVal1(newOrderInfo.getOrderChannelId(), Name.auto_approved);
		// 允许自动Approved
		if (Constants.ONE_CHAR.equals(isApproved)) {
			
			// 买家留言订单是否不允许自动Approved
			isApproved = ChannelConfigs.getVal1(newOrderInfo.getOrderChannelId(), Name.approved_except_buyer_message);
			if (Constants.ONE_CHAR.equals(isApproved)) {
				// 买家留言
				String buyerMessage = newOrderInfo.getOtherComments();
				if (!StringUtils.isNullOrBlank2(buyerMessage)) {
					return isAutoApproved;
				}
			}
			
			CartBean cartBean = ShopConfigs.getCart(newOrderInfo.getCartId());
			String platFormId = cartBean.getPlatform_id();
			
			// 独立域名
			if (OmsConstants.PLATFORM_ID_SELF.equals(platFormId)) {
				isAutoApproved = true;
			} else {
				// 最大允许Approve金额（人民币）(<=398)
				String maxGrandTotal = ChannelConfigs.getVal1(newOrderInfo.getOrderChannelId(), Name.approved_max_grand_total);
				if (!StringUtils.isEmpty(maxGrandTotal)) {
					String grandTotal = newOrderInfo.getTotalsPayment();
					if (Double.valueOf(grandTotal) <= Double.valueOf(maxGrandTotal)) {
						isAutoApproved = true;
					}
				} else {
					isAutoApproved = true;
				}
			}
		}

		// 非真实姓名的限制
		if (isAutoApproved) {
			String isTrueNameCheck = ChannelConfigs.getVal1(newOrderInfo.getOrderChannelId(), Name.is_true_name_check);
			if (Constants.ONE_CHAR.equals(isTrueNameCheck)) {
				String shipName = newOrderInfo.getShippingReceiverName();
				isAutoApproved = checkShipName(shipName);
			}
		}

		// 地址中包含菜鸟驿站的订单是否允许自动Arrpoved
		if (isAutoApproved) {
			String isCaiNiaoYiZhanCheck = ChannelConfigs.getVal1(newOrderInfo.getOrderChannelId(), Name.is_cainiaoyizhan_check);
			if (Constants.ONE_CHAR.equals(isCaiNiaoYiZhanCheck)) {
				String shipAddressStreet2 = transferStr(newOrderInfo.getShippingAddressStreet2());
				if (shipAddressStreet2.contains("菜鸟驿站")) {
					isAutoApproved = false;
				}
			}
		}
		
		return isAutoApproved;
	}

	/**
	 * 检查收件人姓名是否真实
	 * @param shipName
	 * @return
	 */
	private boolean checkShipName(String shipName) {
		boolean isSuccess = false;

		// 收件人姓名为空
		if (StringUtils.isNullOrBlank2(shipName)) {
			return isSuccess;
		}

		// 全英文判断
		int byteLength = shipName.getBytes().length;
		int strLength = shipName.length();
		if (byteLength == strLength) {
			return isSuccess;
		}

		// 只有一个字判断
		if (strLength == 1 && byteLength >= 2) {
			return isSuccess;
		}

		// 预定义关键词过滤检查
		String checkShipName = Codes.getCodeName(Constants.name_check_options, "ShipName");
		String[] checkShipNameArray = checkShipName.split(",");
		if (checkShipNameArray != null && checkShipNameArray.length > 0) {
			for (String checkName : checkShipNameArray) {
				if (shipName.contains(checkName)) {
					return isSuccess;
				}
			}
		}

		// 前面检查都通过了
		isSuccess = true;

		return isSuccess;
	}
	
	/**
	 * 批处理订单详细表所需数据拼装
	 * 
	 * @param newOrderInfoList
	 * @param taskName
	 * @return
	 */
	public String[] getBatchOrdersDetailSqlData(List<NewOrderInfo4Log> newOrderInfoList, String taskName) {
		StringBuilder sqlBuffer = new StringBuilder();
		
		int size = newOrderInfoList.size();
		int numberTotal = 0;
		for (int i = 0; i < size; i++) {
			// 新订单信息
			NewOrderInfo4Log newOrderInfo = newOrderInfoList.get(i);
			
			// 拼装SQL values 部分
			String[] sqlValueAndCount = prepareOrderDetailData(newOrderInfo, taskName);
			String sqlValue = sqlValueAndCount[0];
			sqlBuffer.append(sqlValue);
			
			// 记录detail总条数
			int number = Integer.valueOf(sqlValueAndCount[1]);
			numberTotal += number;
		}
		
		String sqlValues = sqlBuffer.toString();
		return new String[] { sqlValues.substring(0, sqlValues.length() - 1), String.valueOf(numberTotal) };
		
	}
	
	/**
	 * 一条订单的插入orderDetails表语句values部分
	 * 
	 * @param newOrderInfo
	 * @param taskName
	 * @return
	 */
	private String[] prepareOrderDetailData(NewOrderInfo4Log newOrderInfo, String taskName) {
		StringBuilder sqlValueBuffer = new StringBuilder();
		
		int itemNumber = 0;
		
		String orderChannelId = newOrderInfo.getOrderChannelId();
		String cartId = newOrderInfo.getCartId();
		
		// 折扣对应于具体orderDetail的itemNumber保持用
		List<Map<String,Integer>> subItemNumberList = new ArrayList<Map<String,Integer>>();
		
		// detail_date
		String payTime = newOrderInfo.getPayTime();
		// 时区转换
		String gmtPayTime = DateTimeUtil.getGMTTime(payTime, OmsConstants.TIME_ZONE_8);
		
		String skus = newOrderInfo.getProductSku();
		if (!StringUtils.isNullOrBlank2(skus)) {

			// sku转换配置
			String barcode2Sku = ShopConfigs.getVal1(orderChannelId, cartId, ShopConfigEnums.Name.barcode_2_sku);

			String[] realSkuArray = skus.split(Constants.SPLIT_CHAR_RESOLVE);
			// 商品名
			String names = newOrderInfo.getProductName();
			String[] realNameArray = names.split(Constants.SPLIT_CHAR_RESOLVE);
			// 单项价格
			String itemPrices = newOrderInfo.getProductItemPrice();
			String[] realItemPriceArray = itemPrices.split(Constants.SPLIT_CHAR_RESOLVE);
			// 数量
			String quantitys = newOrderInfo.getProductQuantity();
			String[] realQuantityArray = quantitys.split(Constants.SPLIT_CHAR_RESOLVE);

			// 判断是否套装
			boolean isSuit = false;

			String realSkus = "";
			String realItemPrices = "";
			String realSkuNames = "";
			String realQuantitys = "";

			if (realSkuArray != null && realSkuArray.length > 0) {
				for (int i = 0; i < realSkuArray.length; i++ ) {
					// 需要barcode转sku
					if (Constants.ONE_CHAR.equals(barcode2Sku)) {
						realSkuArray[i] = getSkuByBarcode(orderChannelId, realSkuArray[i]);
					}

					String realSku = realSkuArray[i];
					String realItemPrice = realItemPriceArray[i];
					String realSkuName = realNameArray[i];
					String realQuantity = realQuantityArray[i];

					if (SUIT_SKU_SETTING.containsKey(orderChannelId + cartId + realSku.toUpperCase())) {
						isSuit = true;

						List<String> realSkuPriceNameList = SUIT_SKU_SETTING.get(orderChannelId + cartId + realSku.toUpperCase());

						if (realSkuPriceNameList != null && realSkuPriceNameList.size() > 0) {

							double suitItemPrice = Double.parseDouble(realItemPrice);
							double sumItemPriceTmp = 0;
							int index = 0;
							for (String realSkuPriceName : realSkuPriceNameList) {
								String[] realSkuPriceNameArray = realSkuPriceName.split(Constants.SPLIT_CHAR_RESOLVE);

								// 套装中单品配置价格
								String skuPrice =  realSkuPriceNameArray[1];
								// 最后一件的价格用实际总价 - 其余物品的配置价格
								if (index == (realSkuPriceNameList.size() - 1)) {
									skuPrice = String.valueOf(suitItemPrice - sumItemPriceTmp);
								} else {
									sumItemPriceTmp += Double.parseDouble(skuPrice);
								}

								if (StringUtils.isNullOrBlank2(realSkus)) {
									realSkus = realSkuPriceNameArray[0];
									realItemPrices = skuPrice;
									realSkuNames = realSkuPriceNameArray[2];
									realQuantitys = realQuantity;
								} else {
									realSkus += Constants.SPLIT_CHAR_ADD + realSkuPriceNameArray[0];
									realItemPrices += Constants.SPLIT_CHAR_ADD + skuPrice;
									realSkuNames += Constants.SPLIT_CHAR_ADD + realSkuPriceNameArray[2];
									realQuantitys += Constants.SPLIT_CHAR_ADD + realQuantity;
								}

								index++;
							}
						}
					} else {
						if (StringUtils.isNullOrBlank2(realSkus)) {
							realSkus = realSku;
							realItemPrices = realItemPrice;
							realSkuNames = realSkuName;
							realQuantitys = realQuantity;
						} else {
							realSkus += Constants.SPLIT_CHAR_ADD + realSku;
							realItemPrices += Constants.SPLIT_CHAR_ADD + realItemPrice;
							realSkuNames += Constants.SPLIT_CHAR_ADD + realSkuName;
							realQuantitys += Constants.SPLIT_CHAR_ADD + realQuantity;
						}
					}
				}
			}

			String[] skuArray = realSkus.split(Constants.SPLIT_CHAR_RESOLVE);
			String[] nameArray = realSkuNames.split(Constants.SPLIT_CHAR_RESOLVE);
			String[] itemPriceArray = realItemPrices.split(Constants.SPLIT_CHAR_RESOLVE);
			String[] quantityArray = realQuantitys.split(Constants.SPLIT_CHAR_RESOLVE);

			newOrderInfo.setProductSku(realSkus);
			newOrderInfo.setProductName(realSkuNames);
			newOrderInfo.setProductItemPrice(realItemPrices);
			newOrderInfo.setProductQuantity(realQuantitys);

			if (skuArray != null && skuArray.length > 0) {

				//总价
				double dblPayment = Double.parseDouble(newOrderInfo.getTotalsPayment());
				//满就送排除的商品
				List<String> exceptOfPriceThanGift = PRICE_THAN_GIFT_EXCEPT_SETTING.get(orderChannelId + cartId);

				List<String> skuTotalList = new ArrayList<String>();

				for (int i = 0; i < skuArray.length; i++) {

					String name = nameArray[i];
					name = transferStr(name);

					String itemPrice = itemPriceArray[i];
					String sku = skuArray[i];
					sku = transferStr(sku);

					int quantity = Integer.valueOf(quantityArray[i]);

					//满就送总价中减掉排除商品的价格
					if (exceptOfPriceThanGift != null && exceptOfPriceThanGift.size() > 0) {
						for (String tmp : exceptOfPriceThanGift) {
							if (sku.toUpperCase().startsWith(tmp.toUpperCase())) {
								// 该订单折扣描述
								String disCounts = newOrderInfo.getDiscountAmount();
								String disCountDescriptions = newOrderInfo.getDiscountDescription();
								// 该sku实际价格
								double itemPriceReal = getRealItemPrice(Double.parseDouble(itemPrice), sku, disCounts, disCountDescriptions);
								dblPayment = dblPayment - (itemPriceReal * quantity);

								break;
							}
						}
					}

					// 补差价订单
					if (OmsConstants.PRICES_GAP.equalsIgnoreCase(sku)) {

						skuTotalList.add(sku.toUpperCase());

						sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);

						// order_number
						sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
						sqlValueBuffer.append(Constants.COMMA_CHAR);

						// item_number
						itemNumber++;
						sqlValueBuffer.append(itemNumber);
						sqlValueBuffer.append(Constants.COMMA_CHAR);

						// detail_date
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(gmtPayTime);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);

						// adjustment
						sqlValueBuffer.append(Constants.ADJUSTMENT_0);
						sqlValueBuffer.append(Constants.COMMA_CHAR);

						// product
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(name);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);

						// sub_item_number
						sqlValueBuffer.append(Constants.ZERO_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);

						// price_per_unit
						double itemPriceDouble = 0;
						if (StringUtils.isNumeric(itemPrice)) {
							itemPriceDouble = Double.parseDouble(itemPrice) * quantity;
						}
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(itemPriceDouble);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);

						// quantity_ordered
						sqlValueBuffer.append(Constants.ONE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);

						// quantity_shipped
						sqlValueBuffer.append(Constants.ONE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);

						// quantity_returned
						sqlValueBuffer.append(Constants.ZERO_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);

						// sku
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(sku);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);

						// status
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Type.getValue(MastType.orderStatus.getId(),
								Constants.ORDER_STATUS_IN_PROCESSING, com.voyageone.common.Constants.LANGUAGE.EN));
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);

						// res_allot_flg
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.ONE_CHAR);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);

						// synship_flg
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.ONE_CHAR);
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

						sqlValueBuffer.append(Constants.COMMA_CHAR);
					} else {
						for (int j = 0; j < quantity; j++) {

							skuTotalList.add(sku.toUpperCase());

							sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);

							// order_number
							sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
							sqlValueBuffer.append(Constants.COMMA_CHAR);

							// item_number
							itemNumber++;
							Map<String, Integer> skuItemMap = new HashMap<String, Integer>();
							skuItemMap.put(sku, itemNumber);
							subItemNumberList.add(skuItemMap);

							sqlValueBuffer.append(itemNumber);
							sqlValueBuffer.append(Constants.COMMA_CHAR);

							// detail_date
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(gmtPayTime);
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(Constants.COMMA_CHAR);

							// adjustment
							sqlValueBuffer.append(Constants.ADJUSTMENT_0);
							sqlValueBuffer.append(Constants.COMMA_CHAR);

							// product
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(name);
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(Constants.COMMA_CHAR);

							// sub_item_number
							sqlValueBuffer.append(Constants.ZERO_CHAR);
							sqlValueBuffer.append(Constants.COMMA_CHAR);

							// price_per_unit
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(itemPrice);
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(Constants.COMMA_CHAR);

							// quantity_ordered
							sqlValueBuffer.append(Constants.ONE_CHAR);
							sqlValueBuffer.append(Constants.COMMA_CHAR);

							// quantity_shipped
							sqlValueBuffer.append(Constants.ONE_CHAR);
							sqlValueBuffer.append(Constants.COMMA_CHAR);

							// quantity_returned
							sqlValueBuffer.append(Constants.ZERO_CHAR);
							sqlValueBuffer.append(Constants.COMMA_CHAR);

							// sku
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(sku);
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(Constants.COMMA_CHAR);

							// status
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(newOrderInfo.getApproved() ?
									Type.getValue(MastType.orderStatus.getId(), Constants.ORDER_STATUS_APPROVED, com.voyageone.common.Constants.LANGUAGE.EN) :
									Type.getValue(MastType.orderStatus.getId(), Constants.ORDER_STATUS_IN_PROCESSING, com.voyageone.common.Constants.LANGUAGE.EN));
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(Constants.COMMA_CHAR);

							// res_allot_flg
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(Constants.ZERO_CHAR);
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(Constants.COMMA_CHAR);

							// synship_flg
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(Constants.ZERO_CHAR);
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

							sqlValueBuffer.append(Constants.COMMA_CHAR);
						}
					}
				}

				// 平台ID
				CartBean cartBean = ShopConfigs.getCart(cartId);
				String platformId = cartBean.getPlatform_id();
				// ----------------------------------------------------------------- 满就送赠品 START ---------------------------------------------------------------------------
				boolean blnGift = false;
				// 同一家店满就送规则是以平台还是以渠道来进行赠品
				boolean isFlatform = false;
				String platformOrCart = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRICE_THAN_SELECT_PLATFORM_CART,
						com.voyageone.common.Constants.LANGUAGE.EN);
				String platformOrCartValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + platformOrCart);
				// 平台
				if (Constants.ONE_CHAR.equals(platformOrCartValue)) {
					isFlatform = true;
				}

				// 获得该 渠道/平台 的满就送赠品设置
				List<Double> priceOfPriceThanGift = PRICE_THAN_GIFT_PRICE_SETTING.get(orderChannelId + (isFlatform ? platformId : cartId));
				if (priceOfPriceThanGift != null && priceOfPriceThanGift.size() > 0) {
					for (int i = 0; i < priceOfPriceThanGift.size(); i++) {
						if (!blnGift) {
							double dblPrice = priceOfPriceThanGift.get(i);
							if (dblPayment >= dblPrice) {
								// 获取赠品
								DecimalFormat df = new DecimalFormat("#.00");
								String price = df.format(dblPrice);

								String mapKey = orderChannelId + Constants.COMMA_CHAR + (isFlatform ? platformId : cartId) + Constants.COMMA_CHAR + price;
								List<Map<String, String>> giftMaps = PRICE_THAN_GIFT_SETTING.get(mapKey);
								if (giftMaps != null && giftMaps.size() > 0) {

									// 可重复
									String giftPropertyRepeat = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRICE_THAN_REPEAT,
											com.voyageone.common.Constants.LANGUAGE.EN);
									// 前最大数
									String giftPropertyMaxsum = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRICE_THAN_MAXSUM,
											com.voyageone.common.Constants.LANGUAGE.EN);
									// 随机选一个
									String giftPropertySelectOne = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRICE_THAN_SELECTONE,
											com.voyageone.common.Constants.LANGUAGE.EN);
									// 选择优先顺序且有库存的赠品
									String giftPropertyAppointOneWithInventory = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRICE_THAN_SELECT_APPOINT_ONE_WITH_INVENTORY,
											com.voyageone.common.Constants.LANGUAGE.EN);

									// 满就送赠品列表
									List<String> gifts = new ArrayList<String>();

									// 任选择一款sku
									String giftPropertySelectOneValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertySelectOne);
									if (Constants.ONE_CHAR.equals(giftPropertySelectOneValue)) {
										int giftSize = giftMaps.size();
										int index = new Random().nextInt(giftSize);
										String skuSelect = giftMaps.get(index).get("giftSku");

										if (!StringUtils.isNullOrBlank2(skuSelect)) {
											gifts.add(skuSelect);
										}

										// 任选一款以外设置
									} else {

										// 按优先顺序选择一款赠品，当且仅当有库存
										String giftPropertySelectAppointOneWithInventoryValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertyAppointOneWithInventory);
										if (Constants.ONE_CHAR.equals(giftPropertySelectAppointOneWithInventoryValue)) {
											// 找出本次赠品选哪个及库存处理
											String giftSku = getSelectAppointOneWithInventorySku(giftMaps);
											// 多个sku
											if (giftSku.contains(";")) {
												String[] giftSkuSplitArray = giftSku.split(";");
												for (String giftSkuSplit : giftSkuSplitArray) {
													if (!StringUtils.isNullOrBlank2(giftSkuSplit)) {
														gifts.add(giftSkuSplit);
													}
												}
											} else {
												if (!StringUtils.isNullOrBlank2(giftSku)) {
													gifts.add(giftSku);
												}
											}

											// 以上情况以外满就送赠品设定
										} else {
											for (Map<String, String> giftMap : giftMaps) {
												String giftSku = giftMap.get("giftSku");
												if (!StringUtils.isNullOrBlank2(giftSku)) {
													gifts.add(giftSku);
												}
											}
										}
									}

									// 有满足条件赠品
									if (gifts.size() > 0) {
										// 可以送赠品
										if (giftRulerJudge(newOrderInfo, giftPropertyRepeat, OmsConstants.GIFT_PROPERTY_PRICE_THAN, giftPropertyMaxsum)) {
											itemNumber = getGiftSql(newOrderInfo, gifts, sqlValueBuffer, itemNumber, taskName);
											blnGift = true;
											break;
										}
									}
								}
							}
						}
					}
				}
				// ----------------------------------------------------------------- 满就送赠品 END -----------------------------------------------------------------------------


				// ----------------------------------------------------------------- 买就送赠品 START ---------------------------------------------------------------------------
				// 同一家店买就送规则是以平台还是以渠道来进行赠品
				isFlatform = false;
				platformOrCart = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_BUY_THAN_SELECT_PLATFORM_CART,
						com.voyageone.common.Constants.LANGUAGE.EN);
				platformOrCartValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + platformOrCart);
				// 平台
				if (Constants.ONE_CHAR.equals(platformOrCartValue)) {
					isFlatform = true;
				}

				for (String sku : skuTotalList) {
					sku = sku.toUpperCase();

					String mapKeyPart = orderChannelId + Constants.COMMA_CHAR + (isFlatform ? platformId : cartId) + Constants.COMMA_CHAR;

					// 对应买就送有优先顺及库存设置
					Set<String> buyKeySet = BUY_THAN_GIFT_SETTING.keySet();
					if (buyKeySet != null && buyKeySet.size() > 0) {
						for (String buyKey : buyKeySet) {
							if (buyKey.contains(mapKeyPart)) {
								String buySkuKey = buyKey.replace(mapKeyPart, "");
								// 买赠购买的sku可能配置多个对应一个赠品
								if (buySkuKey.contains(sku) && buySkuKey.contains(";")) {
									sku = buySkuKey;

									break;
								}
							}
						}
					}

					String mapKey = mapKeyPart + sku;
					List<Map<String, String>> giftMaps = BUY_THAN_GIFT_SETTING.get(mapKey);

					if (giftMaps != null && giftMaps.size() > 0) {
						// 可重复
						String giftPropertyRepeat = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_BUY_THAN_REPEAT,
								com.voyageone.common.Constants.LANGUAGE.EN);
						// 前最大数
						String giftPropertyMaxsum = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_BUY_THAN_MAXSUM,
								com.voyageone.common.Constants.LANGUAGE.EN);
						// 随机选一个
						String giftPropertySelectOne = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_BUY_THAN_SELECTONE,
								com.voyageone.common.Constants.LANGUAGE.EN);
						// 选择优先顺序且有库存的赠品
						String giftPropertyAppointOneWithInventory = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_BUY_THAN_SELECT_APPOINT_ONE_WITH_INVENTORY,
								com.voyageone.common.Constants.LANGUAGE.EN);

						// 买就送赠品列表
						List<String> gifts = new ArrayList<String>();

						String giftPropertySelectOneValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertySelectOne);
						// 任选择一款sku
						if (Constants.ONE_CHAR.equals(giftPropertySelectOneValue)) {
							int giftSize = giftMaps.size();
							int index = new Random().nextInt(giftSize);
							String skuSelect = giftMaps.get(index).get("giftSku");

							if (!StringUtils.isNullOrBlank2(skuSelect)) {
								gifts.add(skuSelect);
							}

							// 任选一款以外设置
						} else {
							// 按优先顺序选择一款赠品，当且仅当有库存
							String giftPropertySelectAppointOneWithInventoryValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertyAppointOneWithInventory);
							if (Constants.ONE_CHAR.equals(giftPropertySelectAppointOneWithInventoryValue)) {
								// 找出本次赠品选哪个及库存处理
								String giftSku = getSelectAppointOneWithInventorySku(giftMaps);
								// 多个sku
								if (giftSku.contains(";")) {
									String[] giftSkuSplitArray = giftSku.split(";");
									for (String giftSkuSplit : giftSkuSplitArray) {
										if (!StringUtils.isNullOrBlank2(giftSkuSplit)) {
											gifts.add(giftSkuSplit);
										}
									}
								} else {
									if (!StringUtils.isNullOrBlank2(giftSku)) {
										gifts.add(giftSku);
									}
								}

								// 以上情况以外满就送赠品设定
							} else {
								for (Map<String, String> giftMap : giftMaps) {
									String giftSku = giftMap.get("giftSku");
									if (!StringUtils.isNullOrBlank2(giftSku)) {
										gifts.add(giftSku);
									}
								}
							}
						}

						// 有满足条件赠品
						if (gifts.size() > 0) {
							// 可以送赠品
							if (giftRulerJudge(newOrderInfo, giftPropertyRepeat, OmsConstants.GIFT_PROPERTY_BUY_THAN, giftPropertyMaxsum)) {
								itemNumber = getGiftSql(newOrderInfo, gifts, sqlValueBuffer, itemNumber, taskName);
							}
						}
					}
				}
				// ----------------------------------------------------------------- 买就送赠品 END -----------------------------------------------------------------------------

				List<String> gifts = null;
				// ----------------------------------------------------------------- 老顾客送赠品 START --------------------------------------------------------------------------
				if (newOrderInfo.isRegularCustomer()) {
					gifts = REGULAR_CUSTOMER_GIFT_SETTING.get(orderChannelId + cartId);
					if (gifts != null && gifts.size() > 0) {

						String giftPropertyRepeat = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_REGULAR_CUSTOMER_REPEAT,
								com.voyageone.common.Constants.LANGUAGE.EN);
						String giftPropertyMaxsum = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_REGULAR_CUSTOMER_MAXSUM,
								com.voyageone.common.Constants.LANGUAGE.EN);
						String giftPropertySelectOne = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_REGULAR_CUSTOMER_SELECTONE,
								com.voyageone.common.Constants.LANGUAGE.EN);

						String giftPropertySelectOneValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertySelectOne);

						// 任选择一款sku
						if (Constants.ONE_CHAR.equals(giftPropertySelectOneValue)) {
							int giftSize = gifts.size();
							int index = new Random().nextInt(giftSize);
							String skuSelect = gifts.get(index);

							if (!StringUtils.isNullOrBlank2(skuSelect)) {
								gifts = new ArrayList<String>();
								gifts.add(skuSelect);
							}
						}

						// 可以送赠品
						if (giftRulerJudge(newOrderInfo, giftPropertyRepeat, OmsConstants.GIFT_PROPERTY_REGULAR_CUSTOMER, giftPropertyMaxsum)) {
							itemNumber = getGiftSql(newOrderInfo, gifts, sqlValueBuffer, itemNumber, taskName);
						}
					}
				}
				// ----------------------------------------------------------------- 老顾客送赠品 END -----------------------------------------------------------------------------


				// ----------------------------------------------------------------- 前多少名送赠品 START --------------------------------------------------------------------------
				// 同一家店前多少名规则是以平台还是以渠道来进行赠品
				isFlatform = false;
				platformOrCart = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRIOR_COUNT_CUSTOMER_SELECT_PLATFORM_CART,
						com.voyageone.common.Constants.LANGUAGE.EN);
				platformOrCartValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + platformOrCart);
				// 平台
				if (Constants.ONE_CHAR.equals(platformOrCartValue)) {
					isFlatform = true;
				}

				// 前多少名排除sku
				boolean isPriorCheck = true;
				String mapKey = orderChannelId + Constants.COMMA_CHAR + (isFlatform ? platformId : cartId);
				List<String> exceptSkuList = PRIOR_COUNT_CUSTOMER_GIFT_EXCEPT_SKU_SETTING.get(mapKey);
				if (exceptSkuList != null && exceptSkuList.size() > 0) {
					for (String sku : skuTotalList) {
						// 包含排除的sku则不送赠品
						if (exceptSkuList.contains(sku)) {
							isPriorCheck = false;

							break;
						}
					}
				}

				// 可以进行前多少名送赠品
				if (isPriorCheck) {
					List<Map<String, String>> giftMaps = PRIOR_COUNT_CUSTOMER_GIFT_SETTING.get(mapKey);
					if (giftMaps != null && giftMaps.size() > 0) {
						// 可重复
						String giftPropertyRepeat = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_REPEAT,
								com.voyageone.common.Constants.LANGUAGE.EN);
						// 前最大数
						String giftPropertyMaxsum = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_MAXSUM,
								com.voyageone.common.Constants.LANGUAGE.EN);
						// 随机选一个
						String giftPropertySelectOne = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_SELECTONE,
								com.voyageone.common.Constants.LANGUAGE.EN);
						// 选择优先顺序且有库存的赠品
						String giftPropertyAppointOneWithInventory = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRIOR_COUNT_CUSTOMER_SELECT_APPOINT_ONE_WITH_INVENTORY,
								com.voyageone.common.Constants.LANGUAGE.EN);

						// 前多少名送赠品列表
						gifts = new ArrayList<String>();

						String giftPropertySelectOneValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertySelectOne);
						// 任选择一款sku
						if (Constants.ONE_CHAR.equals(giftPropertySelectOneValue)) {
							int giftSize = giftMaps.size();
							int index = new Random().nextInt(giftSize);
							String skuSelect = giftMaps.get(index).get("giftSku");

							if (!StringUtils.isNullOrBlank2(skuSelect)) {
								gifts.add(skuSelect);
							}

							// 任选一款以外设置
						} else {
							// 按优先顺序选择一款赠品，当且仅当有库存
							String giftPropertySelectAppointOneWithInventoryValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertyAppointOneWithInventory);
							if (Constants.ONE_CHAR.equals(giftPropertySelectAppointOneWithInventoryValue)) {
								// 找出本次赠品选哪个及库存处理
								String giftSku = getSelectAppointOneWithInventorySku(giftMaps);
								// 多个sku
								if (giftSku.contains(";")) {
									String[] giftSkuSplitArray = giftSku.split(";");
									for (String giftSkuSplit : giftSkuSplitArray) {
										if (!StringUtils.isNullOrBlank2(giftSkuSplit)) {
											gifts.add(giftSkuSplit);
										}
									}
								} else {
									if (!StringUtils.isNullOrBlank2(giftSku)) {
										gifts.add(giftSku);
									}
								}

								// 以上情况以外满就送赠品设定
							} else {
								for (Map<String, String> giftMap : giftMaps) {
									String giftSku = giftMap.get("giftSku");
									if (!StringUtils.isNullOrBlank2(giftSku)) {
										gifts.add(giftSku);
									}
								}
							}
						}

						// 有满足条件赠品
						if (gifts.size() > 0) {
							// 可以送赠品
							if (giftRulerJudge(newOrderInfo, giftPropertyRepeat, OmsConstants.GIFT_PROPERTY, giftPropertyMaxsum)) {
								itemNumber = getGiftSql(newOrderInfo, gifts, sqlValueBuffer, itemNumber, taskName);
							}
						}
					}
				}
				// ----------------------------------------------------------------- 前多少名送赠品 END ----------------------------------------------------------------------------
				
			}
		}
		
		// discount
		itemNumber = getDisCountSurchargeShipingSql(subItemNumberList, newOrderInfo, Constants.DISCOUNT_TYPE, sqlValueBuffer, itemNumber, taskName);
		// surcharge
		itemNumber = getDisCountSurchargeShipingSql(null, newOrderInfo, Constants.SURCHARGE_TYPE, sqlValueBuffer, itemNumber, taskName);
		// shipping
		itemNumber = getDisCountSurchargeShipingSql(null, newOrderInfo, Constants.SHIPPING_TYPE, sqlValueBuffer, itemNumber, taskName);
		
		return new String[] { sqlValueBuffer.toString() , String.valueOf(itemNumber) };
	}

	/**
	 * 找出本次赠品选哪个及库存处理
	 *
	 * @param giftMaps
	 * @return
	 */
	private String getSelectAppointOneWithInventorySku(List<Map<String, String>> giftMaps) {
		// 本次所选赠品sku
		String selectSku = "";

		// 优先顺列表
		List<Integer> priorNumList = new ArrayList<Integer>();
		for (Map<String, String> giftMap : giftMaps) {
			String priorNum = giftMap.get("priorNum");
			priorNumList.add(Integer.valueOf(priorNum));
		}

		// 优先顺列表有设置
		if (priorNumList.size() > 0) {
			// 优先顺排序
			Collections.sort(priorNumList);

			// 按排过序的优先顺序循环
			for (int priorSort : priorNumList) {

				// 找到赠品需跳出标志
				boolean isBreak = false;

				for (Map<String, String> giftMap : giftMaps) {
					String priorNum = giftMap.get("priorNum");
					String giftSku = giftMap.get("giftSku");
					String inventory = giftMap.get("inventory");

					// 设置的优先顺
					int priorNumInt = Integer.valueOf(priorNum);

					// 找到符合条件优先顺的赠品
					if ((priorSort == priorNumInt) && (Integer.valueOf(inventory) > 0)) {
						selectSku = giftSku;

						// 送完赠品之后计算剩余库存并保存
						int inventoryInt = Integer.valueOf(inventory);
						giftMap.put("inventory", String.valueOf(--inventoryInt));

						isBreak = true;

						break;
					}
				}

				if (isBreak) {
					break;
				}
			}
		}

		// 返回本次所选赠品sku
		return selectSku;
	}
	
	/**
	 * 各维度赠品规则判断
	 * 
	 * @param newOrderInfo
	 * @param giftPropertyRepeat
	 * @param giftProperty
	 * @param giftPropertyMaxsum
	 * @return
	 */
	private boolean giftRulerJudge(NewOrderInfo4Log newOrderInfo, String giftPropertyRepeat, String giftProperty, String giftPropertyMaxsum) {
		boolean isJudge = false;
		
		String orderChannelId = newOrderInfo.getOrderChannelId();
		String cartId = newOrderInfo.getCartId();
		CartBean cartBean = ShopConfigs.getCart(cartId);
		String platformId = cartBean.getPlatform_id();
		
		// 某规则重复顾客送赠品不可以
		if (Constants.ZERO_CHAR.equals(GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertyRepeat))) {
			// 顾客名获得
			String buyNick = newOrderInfo.getBillingBuyerNick();
			
			// 已获赠品顾客
			String mapKeyName = orderChannelId + Constants.COMMA_CHAR + platformId + Constants.COMMA_CHAR + giftProperty + Constants.COMMA_CHAR + OmsConstants.HAVING_GIFTED_CUSTOMER_NAME;
			
			// 历史已经获得某规则赠品的顾客或本轮中已获某规则赠品的顾客排除
			List<String> giftedCustomerList = (List<String>) HAVING_GIFTED_CUSTOMER_INFO.get(mapKeyName);
			List<String> giftedCustomerListLocal = (List<String>) HAVING_GIFTED_CUSTOMER_INFO_LOCAL.get(mapKeyName);
			if (giftedCustomerList != null && giftedCustomerList.size() > 0) {
				if (giftedCustomerList.contains(buyNick)) {
					return isJudge;
					
				}
			}
			
			if (giftedCustomerListLocal != null && giftedCustomerListLocal.size() > 0) {
				if (giftedCustomerListLocal.contains(buyNick)) {
					return isJudge;
					
				} else {
					// 某规则前多少名也满足
					int judgeNumber = giftRulerJudge(newOrderInfo, giftProperty, giftPropertyMaxsum);
					if (judgeNumber == 1) {
						
						giftedCustomerListLocal.add(buyNick);
						
						HAVING_GIFTED_CUSTOMER_INFO_LOCAL.put(mapKeyName, giftedCustomerListLocal);
					}

					if (judgeNumber == 1 || judgeNumber == 0) {
						isJudge = true;
					}
					
					return isJudge;
				}
			} else {
				// 某规则前多少名也满足
				int judgeNumber = giftRulerJudge(newOrderInfo, giftProperty, giftPropertyMaxsum);
				if (judgeNumber == 1) {
					
					giftedCustomerListLocal = new ArrayList<String>();
					giftedCustomerListLocal.add(buyNick);
					
					HAVING_GIFTED_CUSTOMER_INFO_LOCAL.put(mapKeyName, giftedCustomerListLocal);
				}

				if (judgeNumber == 1 || judgeNumber == 0) {
					isJudge = true;
				}

				return isJudge;
			}
			
		// 某规则重复顾客送赠品可以
		} else {
			int judgeNumber = giftRulerJudge(newOrderInfo, giftProperty, giftPropertyMaxsum);
			if (judgeNumber == 1) {
				// 顾客名获得
				String buyNick = newOrderInfo.getBillingBuyerNick();
				
				// 已获赠品顾客
				String mapKeyName = orderChannelId + Constants.COMMA_CHAR + platformId + Constants.COMMA_CHAR + giftProperty + Constants.COMMA_CHAR + OmsConstants.HAVING_GIFTED_CUSTOMER_NAME;
				List<String> giftedCustomerListLocal = (List<String>) HAVING_GIFTED_CUSTOMER_INFO_LOCAL.get(mapKeyName);
				if (giftedCustomerListLocal != null && giftedCustomerListLocal.size() > 0) {
					giftedCustomerListLocal.add(buyNick);
				} else {
					giftedCustomerListLocal = new ArrayList<String>();
					giftedCustomerListLocal.add(buyNick);
					
					HAVING_GIFTED_CUSTOMER_INFO_LOCAL.put(mapKeyName, giftedCustomerListLocal);
				}
			}

			if (judgeNumber == 1 || judgeNumber == 0) {
				isJudge = true;
			}

			return isJudge;
		}
		
	}
	
	/**
	 * 各维度赠品规则判断
	 * 
	 * @param newOrderInfo
	 * @param giftProperty
	 * @param giftPropertyMaxsum
	 * @return
	 */
	private int giftRulerJudge(NewOrderInfo4Log newOrderInfo, String giftProperty, String giftPropertyMaxsum) {
		String orderChannelId = newOrderInfo.getOrderChannelId();
		String cartId = newOrderInfo.getCartId();
		CartBean cartBean = ShopConfigs.getCart(cartId);
		String platformId = cartBean.getPlatform_id();
		
		// 已获某规则赠品顾客前多少名
		String buyThanMaxSumStr = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertyMaxsum);

		// 没有配置
		if (buyThanMaxSumStr == null) {
			return 0;
		}

		int buyThanMaxSum = 0;
		if (!StringUtils.isNullOrBlank2(buyThanMaxSumStr) && StringUtils.isDigit(buyThanMaxSumStr.trim())) {
			buyThanMaxSum = Integer.valueOf(buyThanMaxSumStr);
		}
		if (buyThanMaxSum > 0) {
			// 获得已获某规则赠品顾客数
			String mapKeyCount = orderChannelId + Constants.COMMA_CHAR + platformId + Constants.COMMA_CHAR + giftProperty + Constants.COMMA_CHAR + OmsConstants.HAVING_GIFTED_CUSTOMER_COUNT;
			
			// 获得历史已获某规则赠品顾客数、本轮中已获某规则赠品的顾客数
			Integer giftedCustomerCount = (Integer) HAVING_GIFTED_CUSTOMER_INFO.get(mapKeyCount);
			Integer giftedCustomerCountLocal = (Integer) HAVING_GIFTED_CUSTOMER_INFO_LOCAL.get(mapKeyCount);
			int count = 0;
			int countLocal = 0;
			if (giftedCustomerCount != null) {
				count = giftedCustomerCount;
			}
			if (giftedCustomerCountLocal != null) {
				countLocal = giftedCustomerCountLocal;
			}
			
			if (count >= buyThanMaxSum) {
				return -1;
					
			} else {
				if ((count + countLocal) >= buyThanMaxSum) {
					return -1;
				} else {
					HAVING_GIFTED_CUSTOMER_INFO_LOCAL.put(mapKeyCount, ++countLocal);
				}
			}
		}
		
		return 1;
	}
	
	/**
	 * 各维度赠品规则判断
	 * 
	 * @param newOrderInfo
	 * @param giftPropertyRepeat
	 * @param giftProperty
	 * @param giftPropertyMaxsum
	 * @return
	 */
	private boolean giftRulerJudge2(NewOrderInfo4Log newOrderInfo, String giftPropertyRepeat, String giftProperty, String giftPropertyMaxsum) {
		boolean isJudge = false;
		
		String orderChannelId = newOrderInfo.getOrderChannelId();
		String cartId = newOrderInfo.getCartId();
		CartBean cartBean = ShopConfigs.getCart(cartId);
		String platformId = cartBean.getPlatform_id();
		
		// 某规则重复顾客送赠品不可以
		if (Constants.ZERO_CHAR.equals(GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertyRepeat))) {
			// 顾客名获得
			String buyNick = newOrderInfo.getBillingBuyerNick();
			
			// 已获赠品顾客
			String mapKeyName = orderChannelId + Constants.COMMA_CHAR + platformId + Constants.COMMA_CHAR + giftProperty + Constants.COMMA_CHAR + OmsConstants.HAVING_GIFTED_CUSTOMER_NAME;
			
			// 历史已经获得某规则赠品的顾客或本轮中已获某规则赠品的顾客排除
			List<String> giftedCustomerList = (List<String>) HAVING_GIFTED_CUSTOMER_INFO.get(mapKeyName);
			List<String> giftedCustomerListLocal = (List<String>) HAVING_GIFTED_CUSTOMER_INFO_LOCAL.get(mapKeyName);
			if (giftedCustomerList != null && giftedCustomerList.size() > 0) {
				if (giftedCustomerList.contains(buyNick)) {
					return isJudge;
					
				}
			}
			
			if (giftedCustomerListLocal != null && giftedCustomerListLocal.size() > 0) {
				if (giftedCustomerListLocal.contains(buyNick)) {
					return isJudge;
					
				} else {
					// 某规则前多少名也满足
					isJudge = giftRulerJudge2(newOrderInfo, giftProperty, giftPropertyMaxsum);
					
					return isJudge;
				}
			} else {
				// 某规则前多少名也满足
				isJudge = giftRulerJudge2(newOrderInfo, giftProperty, giftPropertyMaxsum);
				return isJudge;
			}
			
		// 某规则重复顾客送赠品可以
		} else {
			isJudge = giftRulerJudge2(newOrderInfo, giftProperty, giftPropertyMaxsum);
			
			return isJudge;
		}
		
	}
	
	/**
	 * 各维度赠品规则判断
	 * 
	 * @param newOrderInfo
	 * @param giftProperty
	 * @param giftPropertyMaxsum
	 * @return
	 */
	private boolean giftRulerJudge2(NewOrderInfo4Log newOrderInfo, String giftProperty, String giftPropertyMaxsum) {
		String orderChannelId = newOrderInfo.getOrderChannelId();
		String cartId = newOrderInfo.getCartId();
		CartBean cartBean = ShopConfigs.getCart(cartId);
		String platformId = cartBean.getPlatform_id();
		
		// 已获某规则赠品顾客前多少名
		String buyThanMaxSumStr = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertyMaxsum);
		int buyThanMaxSum = 0;
		if (!StringUtils.isNullOrBlank2(buyThanMaxSumStr) && StringUtils.isDigit(buyThanMaxSumStr.trim())) {
			buyThanMaxSum = Integer.valueOf(buyThanMaxSumStr);
		}
		if (buyThanMaxSum > 0) {
			// 获得已获某规则赠品顾客数
			String mapKeyCount = orderChannelId + Constants.COMMA_CHAR + platformId + Constants.COMMA_CHAR + giftProperty + Constants.COMMA_CHAR + OmsConstants.HAVING_GIFTED_CUSTOMER_COUNT;
			
			// 获得历史已获某规则赠品顾客数、本轮中已获某规则赠品的顾客数
			Integer giftedCustomerCount = (Integer) HAVING_GIFTED_CUSTOMER_INFO.get(mapKeyCount);
			Integer giftedCustomerCountLocal = (Integer) HAVING_GIFTED_CUSTOMER_INFO_LOCAL.get(mapKeyCount);
			int count = 0;
			int countLocal = 0;
			if (giftedCustomerCount != null) {
				count = giftedCustomerCount;
			}
			if (giftedCustomerCountLocal != null) {
				countLocal = giftedCustomerCountLocal;
			}
			
			if (count >= buyThanMaxSum) {
				return false;
					
			} else {
				if ((count + countLocal) >= buyThanMaxSum) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * 批处理Notes信息所需数据拼装
	 * 
	 * @param newOrderInfoList
	 * @param taskName
	 * @return
	 */
	public String getBatchNoteSqlData(List<NewOrderInfo4Log> newOrderInfoList, String taskName) {
		StringBuilder sqlBuffer = new StringBuilder();
		
		int size = newOrderInfoList.size();
		for (int i = 0; i < size; i++) {
			// 新订单信息
			NewOrderInfo4Log newOrderInfo = newOrderInfoList.get(i);
			
			// 拼装SQL values 部分
			String entryDateTime = DateTimeUtil.getNow(DateTimeUtil.DEFAULT_DATETIME_FORMAT);
			
			sqlBuffer.append(prepareNotesData(newOrderInfo, entryDateTime, entryDateTime, taskName));
			
			if (i < (size - 1)) {
				sqlBuffer.append(Constants.COMMA_CHAR);
			}
		}
		
		return sqlBuffer.toString();
		
	}
	
	/**
	 * 一条订单的插入notes表语句values部分
	 * 
	 * @param newOrderInfo
	 * @param entryDate
	 * @param entryTime
	 * @param taskName
	 * @return
	 */
	private String prepareNotesData(NewOrderInfo4Log newOrderInfo, String entryDate, String entryTime, String taskName) {
		StringBuilder sqlValueBuffer = new StringBuilder();
		
		sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
		
		// type
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.NOTES_SYSTEM);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// numeric_key
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
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
		String note = "Order Status changed to: ";
		if (isAutoApproved(newOrderInfo)) {
			note += Type.getValue(MastType.orderStatus.getId(), Constants.ORDER_STATUS_APPROVED, com.voyageone.common.Constants.LANGUAGE.EN);
		} else {
			note += Type.getValue(MastType.orderStatus.getId(), Constants.ORDER_STATUS_IN_PROCESSING, com.voyageone.common.Constants.LANGUAGE.EN);
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
		sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// source_order_id
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(newOrderInfo.getTid());
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
	 * 批处理交易明细表所需数据拼装
	 * 
	 * @param newOrderInfoList
	 * @param taskName
	 * @return
	 */
	public Map<String, Object> getBatchTransactionsSqlData(List<NewOrderInfo4Log> newOrderInfoList, String taskName) {
		StringBuilder sqlBuffer = new StringBuilder();
		
		int size = newOrderInfoList.size();
		int count = 0;
		for (int i = 0; i < size; i++) {
			// 新订单信息
			NewOrderInfo4Log newOrderInfo = newOrderInfoList.get(i);
			
			// 拼装SQL values 部分
			String[] prepareTransactions = prepareTransactionsData(newOrderInfo, taskName);
			
			if (prepareTransactions != null && prepareTransactions.length == 2) {
				String sql = prepareTransactions[0];
				if (!StringUtils.isEmpty(sql)) {
					sqlBuffer.append(sql);
				}
				
				String itemNumber = prepareTransactions[1];
				if (!StringUtils.isEmpty(itemNumber)) {
					count += Integer.valueOf(itemNumber);
				}
				
			}
		}
		String sqlValues = sqlBuffer.toString();
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("sqlData", sqlValues.substring(0, sqlValues.length() - 1));
		dataMap.put("count", count);
		
		return dataMap;
		
	}
	
	/**
	 * 一条订单的插入交易明细表语句values部分
	 * 
	 * @param newOrderInfo
	 * @param taskName
	 * @return
	 */
	private String[] prepareTransactionsData(NewOrderInfo4Log newOrderInfo, String taskName) {
		StringBuilder sqlValueBuffer = new StringBuilder();
		
		int itemNumber = 0;
		
		String orderChannelId = newOrderInfo.getOrderChannelId();
		String cartId = newOrderInfo.getCartId();

		// sku转换配置
		String barcode2Sku = ShopConfigs.getVal1(orderChannelId, cartId, ShopConfigEnums.Name.barcode_2_sku);
		
		String skus = newOrderInfo.getProductSku();
		if (!StringUtils.isNullOrBlank2(skus)) {
			
			String[] skuArray = skus.split(Constants.SPLIT_CHAR_RESOLVE);
			if (skuArray != null && skuArray.length > 0) {
				// 单项价格
				String itemPrices = newOrderInfo.getProductItemPrice();
				String[] itemPriceArray = itemPrices.split(Constants.SPLIT_CHAR_RESOLVE);
				// 数量
				String quantitys = newOrderInfo.getProductQuantity();
				String[] quantityArray = quantitys.split(Constants.SPLIT_CHAR_RESOLVE);
				
				// -------------------------------赠品相关--------------------------
				//总价
				double dblPayment = Double.parseDouble(newOrderInfo.getTotalsPayment());
				//满就送排除的商品
				List<String> exceptOfPriceThanGift = PRICE_THAN_GIFT_EXCEPT_SETTING.get(orderChannelId + cartId);
				// -------------------------------赠品相关--------------------------
				
				List<String> skuTotalList = new ArrayList<String>();
				
				for (int i = 0; i < skuArray.length; i++) {
					
					String itemPrice = itemPriceArray[i];

					// 需要barcode转sku
					if (Constants.ONE_CHAR.equals(barcode2Sku)) {
						skuArray[i] = getSkuByBarcode(orderChannelId, skuArray[i]);
					}

					String sku = skuArray[i];
					sku = transferStr(sku);
					
					int quantity = Integer.valueOf(quantityArray[i]);
					double debit = 0;
					if (StringUtils.isNumeric(itemPrice)) {
						debit = Double.parseDouble(itemPrice) * quantity;
					}
					
					// -------------------------------赠品相关--------------------------
					//满就送总价中减掉排除商品的价格
					if (exceptOfPriceThanGift != null && exceptOfPriceThanGift.size() > 0) {
						for (String tmp : exceptOfPriceThanGift) {
							if (sku.toUpperCase().startsWith(tmp.toUpperCase())) {
								// 该订单折扣描述
								String disCounts = newOrderInfo.getDiscountAmount();
								String disCountDescriptions = newOrderInfo.getDiscountDescription();
								
								// 该sku实际价格
								double itemPriceReal = getRealItemPrice(Double.parseDouble(itemPrice), sku, disCounts, disCountDescriptions);
								dblPayment = dblPayment - (itemPriceReal * quantity);
								
								break;
							}
						}
					}
					// -------------------------------赠品相关--------------------------
					
					// 补差价订单
					if (OmsConstants.PRICES_GAP.equalsIgnoreCase(sku)) {
						
						skuTotalList.add(sku.toUpperCase());
						
						sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
						
						// order_number
						sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// transaction_time
						String payTime = newOrderInfo.getPayTime();
						// 时区转换
						String gmtPayTime = DateTimeUtil.getGMTTime(payTime, OmsConstants.TIME_ZONE_8);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(gmtPayTime);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// sku
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(sku);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// description
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(OmsConstants.TRANSACTION_PRODUCT);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// item_number
						itemNumber++;
						sqlValueBuffer.append(itemNumber);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// debit
						sqlValueBuffer.append(debit);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// credit
						sqlValueBuffer.append(Constants.ZERO_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// source_order_id
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(newOrderInfo.getTid());
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// origin_source_order_id
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(newOrderInfo.getTid());
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// type
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(OmsConstants.TRANSACTIONS_TYPE_ORDER);
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
						
						sqlValueBuffer.append(Constants.COMMA_CHAR);
					} else {
						for (int j = 0; j < quantity; j++) {
							
							skuTotalList.add(sku.toUpperCase());
							
							sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
							
							// order_number
							sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
							sqlValueBuffer.append(Constants.COMMA_CHAR);
							
							// transaction_time
							String payTime = newOrderInfo.getPayTime();
							// 时区转换
							String gmtPayTime = DateTimeUtil.getGMTTime(payTime, OmsConstants.TIME_ZONE_8);
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(gmtPayTime);
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(Constants.COMMA_CHAR);
							
							// sku
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(sku);
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(Constants.COMMA_CHAR);
							
							// description
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(OmsConstants.TRANSACTION_PRODUCT);
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(Constants.COMMA_CHAR);
							
							// item_number
							itemNumber++;
							sqlValueBuffer.append(itemNumber);
							sqlValueBuffer.append(Constants.COMMA_CHAR);
							
							// debit
							sqlValueBuffer.append(itemPrice);
							sqlValueBuffer.append(Constants.COMMA_CHAR);
							
							// credit
							sqlValueBuffer.append(Constants.ZERO_CHAR);
							sqlValueBuffer.append(Constants.COMMA_CHAR);
							
							// source_order_id
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(newOrderInfo.getTid());
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(Constants.COMMA_CHAR);
							
							// origin_source_order_id
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(newOrderInfo.getTid());
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(Constants.COMMA_CHAR);
							
							// type
							sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
							sqlValueBuffer.append(OmsConstants.TRANSACTIONS_TYPE_ORDER);
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
							
							sqlValueBuffer.append(Constants.COMMA_CHAR);
						}
					}
				}

				// 平台ID
				CartBean cartBean = ShopConfigs.getCart(cartId);
				String platformId = cartBean.getPlatform_id();
				// ----------------------------------------------------------------- 满就送赠品 START ---------------------------------------------------------------------------
				boolean blnGift = false;
				// 同一家店满就送规则是以平台还是以渠道来进行赠品
				boolean isFlatform = false;
				String platformOrCart = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRICE_THAN_SELECT_PLATFORM_CART,
						com.voyageone.common.Constants.LANGUAGE.EN);
				String platformOrCartValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + platformOrCart);
				// 平台
				if (Constants.ONE_CHAR.equals(platformOrCartValue)) {
					isFlatform = true;
				}

				// 获得该 渠道/平台 的满就送赠品设置
				List<Double> priceOfPriceThanGift = PRICE_THAN_GIFT_PRICE_SETTING.get(orderChannelId + (isFlatform ? platformId : cartId));
				if (priceOfPriceThanGift != null && priceOfPriceThanGift.size() > 0) {
					for (int i = 0; i < priceOfPriceThanGift.size(); i++){
						if (!blnGift) {
							double dblPrice = priceOfPriceThanGift.get(i);
							if (dblPayment >= dblPrice) {
								// 获取赠品
								DecimalFormat df = new DecimalFormat("#.00");
								String price = df.format(dblPrice);

								String mapKey = orderChannelId + Constants.COMMA_CHAR + (isFlatform ? platformId : cartId) + Constants.COMMA_CHAR + price;
								List<Map<String, String>> giftMaps = PRICE_THAN_GIFT_SETTING_TRANS.get(mapKey);
								if (giftMaps != null && giftMaps.size() > 0) {

									// 可重复
									String giftPropertyRepeat = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRICE_THAN_REPEAT, 
											com.voyageone.common.Constants.LANGUAGE.EN);
									// 前最大数
									String giftPropertyMaxsum = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRICE_THAN_MAXSUM, 
											com.voyageone.common.Constants.LANGUAGE.EN);
									// 随机选一个
									String giftPropertySelectOne = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRICE_THAN_SELECTONE, 
											com.voyageone.common.Constants.LANGUAGE.EN);
									// 选择优先顺序且有库存的赠品
									String giftPropertyAppointOneWithInventory = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRICE_THAN_SELECT_APPOINT_ONE_WITH_INVENTORY,
											com.voyageone.common.Constants.LANGUAGE.EN);

									// 满就送赠品列表
									List<String> gifts = new ArrayList<String>();

									// 任选择一款sku
									String giftPropertySelectOneValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertySelectOne);
									if (Constants.ONE_CHAR.equals(giftPropertySelectOneValue)) {
										int giftSize = giftMaps.size();
										int index = new Random().nextInt(giftSize);
										String skuSelect = giftMaps.get(index).get("giftSku");
										
										if (!StringUtils.isNullOrBlank2(skuSelect)) {
											gifts.add(skuSelect);
										}
										// 任选一款以外设置
									} else {
										// 按优先顺序选择一款赠品，当且仅当有库存
										String giftPropertySelectAppointOneWithInventoryValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertyAppointOneWithInventory);
										if (Constants.ONE_CHAR.equals(giftPropertySelectAppointOneWithInventoryValue)) {
											// 找出本次赠品选哪个及库存处理
											String giftSku = getSelectAppointOneWithInventorySku(giftMaps);
											// 多个sku
											if (giftSku.contains(";")) {
												String[] giftSkuSplitArray = giftSku.split(";");
												for (String giftSkuSplit : giftSkuSplitArray) {
													if (!StringUtils.isNullOrBlank2(giftSkuSplit)) {
														gifts.add(giftSkuSplit);
													}
												}
											} else {
												if (!StringUtils.isNullOrBlank2(giftSku)) {
													gifts.add(giftSku);
												}
											}

											// 以上情况以外满就送赠品设定
										} else {
											for (Map<String, String> giftMap : giftMaps) {
												String giftSku = giftMap.get("giftSku");
												if (!StringUtils.isNullOrBlank2(giftSku)) {
													gifts.add(giftSku);
												}
											}
										}
									}

									// 有满足条件赠品
									if (gifts.size() > 0) {
										// 可以送赠品
										if (giftRulerJudge2(newOrderInfo, giftPropertyRepeat, OmsConstants.GIFT_PROPERTY_PRICE_THAN, giftPropertyMaxsum)) {
											itemNumber = getGiftSqlTransaction(newOrderInfo, gifts, sqlValueBuffer, itemNumber, taskName);
											blnGift = true;
											break;
										}
									}
								}
							}
						}
					}
				}
				// ----------------------------------------------------------------- 满就送赠品 END -----------------------------------------------------------------------------
				
				
				// ----------------------------------------------------------------- 买就送赠品 START ---------------------------------------------------------------------------
				// 同一家店买就送规则是以平台还是以渠道来进行赠品
				isFlatform = false;
				platformOrCart = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_BUY_THAN_SELECT_PLATFORM_CART,
						com.voyageone.common.Constants.LANGUAGE.EN);
				platformOrCartValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + platformOrCart);
				// 平台
				if (Constants.ONE_CHAR.equals(platformOrCartValue)) {
					isFlatform = true;
				}

				for (String sku : skuTotalList) {
					sku = sku.toUpperCase();

					String mapKeyPart = orderChannelId + Constants.COMMA_CHAR + (isFlatform ? platformId : cartId) + Constants.COMMA_CHAR;

					// 对应买就送有优先顺及库存设置
					Set<String> buyKeySet = BUY_THAN_GIFT_SETTING_TRANS.keySet();
					if (buyKeySet != null && buyKeySet.size() > 0) {
						for (String buyKey : buyKeySet) {
							if (buyKey.contains(mapKeyPart)) {
								String buySkuKey = buyKey.replace(mapKeyPart, "");
								// 买赠购买的sku可能配置多个对应一个赠品
								if (buySkuKey.contains(sku) && buySkuKey.contains(";")) {
									sku = buySkuKey;

									break;
								}
							}
						}
					}

					String mapKey = mapKeyPart + sku;
					List<Map<String, String>> giftMaps = BUY_THAN_GIFT_SETTING_TRANS.get(mapKey);

					if (giftMaps != null && giftMaps.size() > 0) {
						// 可重复
						String giftPropertyRepeat = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_BUY_THAN_REPEAT,
								com.voyageone.common.Constants.LANGUAGE.EN);
						// 前最大数
						String giftPropertyMaxsum = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_BUY_THAN_MAXSUM,
								com.voyageone.common.Constants.LANGUAGE.EN);
						// 随机选一个
						String giftPropertySelectOne = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_BUY_THAN_SELECTONE,
								com.voyageone.common.Constants.LANGUAGE.EN);
						// 选择优先顺序且有库存的赠品
						String giftPropertyAppointOneWithInventory = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_BUY_THAN_SELECT_APPOINT_ONE_WITH_INVENTORY,
								com.voyageone.common.Constants.LANGUAGE.EN);

						// 买就送赠品列表
						List<String> gifts = new ArrayList<String>();

						String giftPropertySelectOneValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertySelectOne);
						// 任选择一款sku
						if (Constants.ONE_CHAR.equals(giftPropertySelectOneValue)) {
							int giftSize = giftMaps.size();
							int index = new Random().nextInt(giftSize);
							String skuSelect = giftMaps.get(index).get("giftSku");

							if (!StringUtils.isNullOrBlank2(skuSelect)) {
								gifts.add(skuSelect);
							}

						// 任选一款以外设置
						} else {
							// 按优先顺序选择一款赠品，当且仅当有库存
							String giftPropertySelectAppointOneWithInventoryValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertyAppointOneWithInventory);
							if (Constants.ONE_CHAR.equals(giftPropertySelectAppointOneWithInventoryValue)) {
								// 找出本次赠品选哪个及库存处理
								String giftSku = getSelectAppointOneWithInventorySku(giftMaps);
								// 多个sku
								if (giftSku.contains(";")) {
									String[] giftSkuSplitArray = giftSku.split(";");
									for (String giftSkuSplit : giftSkuSplitArray) {
										if (!StringUtils.isNullOrBlank2(giftSkuSplit)) {
											gifts.add(giftSkuSplit);
										}
									}
								} else {
									if (!StringUtils.isNullOrBlank2(giftSku)) {
										gifts.add(giftSku);
									}
								}

								// 以上情况以外满就送赠品设定
							} else {
								for (Map<String, String> giftMap : giftMaps) {
									String giftSku = giftMap.get("giftSku");
									if (!StringUtils.isNullOrBlank2(giftSku)) {
										gifts.add(giftSku);
									}
								}
							}
						}

						// 有满足条件赠品
						if (gifts.size() > 0) {
							// 可以送赠品
							if (giftRulerJudge2(newOrderInfo, giftPropertyRepeat, OmsConstants.GIFT_PROPERTY_BUY_THAN, giftPropertyMaxsum)) {
								itemNumber = getGiftSqlTransaction(newOrderInfo, gifts, sqlValueBuffer, itemNumber, taskName);
							}
						}
					}
				}
				// ----------------------------------------------------------------- 买就送赠品 END -----------------------------------------------------------------------------


				List<String> gifts = null;
				// ----------------------------------------------------------------- 老顾客送赠品 START --------------------------------------------------------------------------
				// 老顾客是否送赠品
				if (newOrderInfo.isRegularCustomer()) {
					gifts = REGULAR_CUSTOMER_GIFT_SETTING.get(orderChannelId + cartId);
					if (gifts != null && gifts.size() > 0) {

						String giftPropertyRepeat = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_REGULAR_CUSTOMER_REPEAT, 
								com.voyageone.common.Constants.LANGUAGE.EN);
						String giftPropertyMaxsum = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_REGULAR_CUSTOMER_MAXSUM, 
								com.voyageone.common.Constants.LANGUAGE.EN);
						String giftPropertySelectOne = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_REGULAR_CUSTOMER_SELECTONE, 
								com.voyageone.common.Constants.LANGUAGE.EN);
						
						String giftPropertySelectOneValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertySelectOne);
						
						// 任选择一款sku
						if (Constants.ONE_CHAR.equals(giftPropertySelectOneValue)) {
							int giftSize = gifts.size();
							int index = new Random().nextInt(giftSize);
							String skuSelect = gifts.get(index);
							
							if (!StringUtils.isNullOrBlank2(skuSelect)) {
								gifts = new ArrayList<String>();
								gifts.add(skuSelect);
							}
						}
						
						// 可以送赠品
						if (giftRulerJudge2(newOrderInfo, giftPropertyRepeat, OmsConstants.GIFT_PROPERTY_REGULAR_CUSTOMER, giftPropertyMaxsum)) {
							itemNumber = getGiftSqlTransaction(newOrderInfo, gifts, sqlValueBuffer, itemNumber, taskName);
						}
					}
				}
				// ----------------------------------------------------------------- 老顾客送赠品 END -----------------------------------------------------------------------------
				
				
				// ----------------------------------------------------------------- 前多少名送赠品 START --------------------------------------------------------------------------
				// 同一家店前多少名规则是以平台还是以渠道来进行赠品
				isFlatform = false;
				platformOrCart = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRIOR_COUNT_CUSTOMER_SELECT_PLATFORM_CART,
						com.voyageone.common.Constants.LANGUAGE.EN);
				platformOrCartValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + platformOrCart);
				// 平台
				if (Constants.ONE_CHAR.equals(platformOrCartValue)) {
					isFlatform = true;
				}

				// 前多少名排除sku
				boolean isPriorCheck = true;
				String mapKey = orderChannelId + Constants.COMMA_CHAR + (isFlatform ? platformId : cartId);
				List<String> exceptSkuList = PRIOR_COUNT_CUSTOMER_GIFT_EXCEPT_SKU_SETTING.get(mapKey);
				if (exceptSkuList != null && exceptSkuList.size() > 0) {
					for (String sku : skuTotalList) {
						// 包含排除的sku则不送赠品
						if (exceptSkuList.contains(sku)) {
							isPriorCheck = false;

							break;
						}
					}
				}

				// 可以进行前多少名送赠品
				if (isPriorCheck) {
					List<Map<String, String>> giftMaps = PRIOR_COUNT_CUSTOMER_GIFT_SETTING_TRANS.get(mapKey);
					if (giftMaps != null && giftMaps.size() > 0) {

						// 可重复
						String giftPropertyRepeat = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_REPEAT,
								com.voyageone.common.Constants.LANGUAGE.EN);
						// 前最大数
						String giftPropertyMaxsum = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_MAXSUM,
								com.voyageone.common.Constants.LANGUAGE.EN);
						// 随机选一个
						String giftPropertySelectOne = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_SELECTONE,
								com.voyageone.common.Constants.LANGUAGE.EN);
						// 选择优先顺序且有库存的赠品
						String giftPropertyAppointOneWithInventory = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRIOR_COUNT_CUSTOMER_SELECT_APPOINT_ONE_WITH_INVENTORY,
								com.voyageone.common.Constants.LANGUAGE.EN);

						// 前多少名送赠品列表
						gifts = new ArrayList<String>();

						String giftPropertySelectOneValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertySelectOne);
						// 任选择一款sku
						if (Constants.ONE_CHAR.equals(giftPropertySelectOneValue)) {
							int giftSize = giftMaps.size();
							int index = new Random().nextInt(giftSize);
							String skuSelect = giftMaps.get(index).get("giftSku");

							if (!StringUtils.isNullOrBlank2(skuSelect)) {
								gifts.add(skuSelect);
							}

							// 任选一款以外设置
						} else {
							// 按优先顺序选择一款赠品，当且仅当有库存
							String giftPropertySelectAppointOneWithInventoryValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertyAppointOneWithInventory);
							if (Constants.ONE_CHAR.equals(giftPropertySelectAppointOneWithInventoryValue)) {
								// 找出本次赠品选哪个及库存处理
								String giftSku = getSelectAppointOneWithInventorySku(giftMaps);
								// 多个sku
								if (giftSku.contains(";")) {
									String[] giftSkuSplitArray = giftSku.split(";");
									for (String giftSkuSplit : giftSkuSplitArray) {
										if (!StringUtils.isNullOrBlank2(giftSkuSplit)) {
											gifts.add(giftSkuSplit);
										}
									}
								} else {
									if (!StringUtils.isNullOrBlank2(giftSku)) {
										gifts.add(giftSku);
									}
								}

								// 以上情况以外满就送赠品设定
							} else {
								for (Map<String, String> giftMap : giftMaps) {
									String giftSku = giftMap.get("giftSku");
									if (!StringUtils.isNullOrBlank2(giftSku)) {
										gifts.add(giftSku);
									}
								}
							}
						}

						// 有满足条件赠品
						if (gifts.size() > 0) {
							// 可以送赠品
							if (giftRulerJudge2(newOrderInfo, giftPropertyRepeat, OmsConstants.GIFT_PROPERTY, giftPropertyMaxsum)) {
								itemNumber = getGiftSqlTransaction(newOrderInfo, gifts, sqlValueBuffer, itemNumber, taskName);
							}
						}
					}
				}
				// ----------------------------------------------------------------- 前多少名送赠品 END ----------------------------------------------------------------------------
			}
		}
		
		// discount
		itemNumber = getDisCountSurchargeShipingSql(newOrderInfo, Constants.DISCOUNT_TYPE, sqlValueBuffer, itemNumber, taskName);
		// surcharge
		itemNumber = getDisCountSurchargeShipingSql(newOrderInfo, Constants.SURCHARGE_TYPE, sqlValueBuffer, itemNumber, taskName);
		// shipping
		itemNumber = getDisCountSurchargeShipingSql(newOrderInfo, Constants.SHIPPING_TYPE, sqlValueBuffer, itemNumber, taskName);
		
		return new String[] { sqlValueBuffer.toString() , String.valueOf(itemNumber) };
	}
	
	/**
	 * 批处理交易明细表所需数据拼装
	 * 
	 * @param newOrderInfoList
	 * @param taskName
	 * @return
	 */
	public String getBatchGroupSqlData(List<NewOrderInfo4Log> newOrderInfoList, String taskName) {
		StringBuilder sqlBuffer = new StringBuilder();
		
		int size = newOrderInfoList.size();
		for (int i = 0; i < size; i++) {
			// 新订单信息
			NewOrderInfo4Log newOrderInfo = newOrderInfoList.get(i);
			
//			String sku = newOrderInfo.getProductSku();
//			if (OmsConstants.PRICES_GAP.equals(sku)) {
//				continue;
//			}
			
			// 拼装SQL values 部分
			sqlBuffer.append(prepareGroupData(newOrderInfo, taskName));
			
			if (i < (size - 1)) {
				sqlBuffer.append(Constants.COMMA_CHAR);
			}
		}
		
		return sqlBuffer.toString();
		
	}
	
	/**
	 * 一条订单的插入oms_bt_group_orders表语句values部分
	 * 
	 * @param newOrderInfo
	 * @param taskName
	 * @return
	 */
	private String prepareGroupData(NewOrderInfo4Log newOrderInfo, String taskName) {
		StringBuilder sqlValueBuffer = new StringBuilder();
		
		sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
		
		// source_order_id
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(newOrderInfo.getTid());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// final_product_total
		sqlValueBuffer.append(newOrderInfo.getTotalsTotalFee());
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// final_shipping_total
		sqlValueBuffer.append(newOrderInfo.getTotalsPostFee());
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// revised_discount
		String disCountStrs = newOrderInfo.getDiscountAmount();
		double disCount = getTotalAmountFromStr(disCountStrs);
		sqlValueBuffer.append(-disCount);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// revised_surcharge
		String surchargeStrs = newOrderInfo.getSurchargeAmount();
		double surcharge = getTotalAmountFromStr(surchargeStrs);
		sqlValueBuffer.append(surcharge);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// final_grand_total
		String paymentTotal = newOrderInfo.getTotalsPayment();
		if (StringUtils.isNullOrBlank2(paymentTotal)) {
			paymentTotal = "0.0";
		}
		sqlValueBuffer.append(paymentTotal);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// expected
		sqlValueBuffer.append(paymentTotal);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// 默认值
		String payment_total = "0.0";
		CartBean cartBean = ShopConfigs.getCart(newOrderInfo.getCartId());
		String platFormId = cartBean.getPlatform_id();
		// 独立域名
		if (OmsConstants.PLATFORM_ID_SELF.equals(platFormId)) {
			payment_total = paymentTotal;
		}
		// payment_total
		sqlValueBuffer.append(payment_total);
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
	 * 批处理支付明细表所需数据拼装
	 * 
	 * @param newOrderInfoList
	 * @param taskName
	 * @return
	 */
	public String[] getBatchPaymentSqlData(List<NewOrderInfo4Log> newOrderInfoList, String taskName) {
		StringBuilder sqlBuffer = new StringBuilder();
		
		int size = newOrderInfoList.size();
		int paymentSize = 0;
		for (int i = 0; i < size; i++) {
			// 新订单信息
			NewOrderInfo4Log newOrderInfo = newOrderInfoList.get(i);
			
			CartBean cartBean = ShopConfigs.getCart(newOrderInfo.getCartId());
			String platFormId = cartBean.getPlatform_id();
			
			// 独立域名
			if (OmsConstants.PLATFORM_ID_SELF.equals(platFormId)) {
			
				// 拼装SQL values 部分
				sqlBuffer.append(preparePaymentData(newOrderInfo, taskName));
				
				paymentSize++;
				
				if (i < (size - 1)) {
					sqlBuffer.append(Constants.COMMA_CHAR);
				}
			}
		}
		
		String paymentBatchStr = sqlBuffer.toString();
		if (paymentBatchStr.endsWith(Constants.COMMA_CHAR)) {
			paymentBatchStr = paymentBatchStr.substring(0, paymentBatchStr.length() - 1);
		}
		
		String[] paymentArray = {paymentBatchStr, String.valueOf(paymentSize)};
		return paymentArray;
		
	}
	
	/**
	 * 一条订单的插入oms_bt_payments表语句values部分
	 * 
	 * @param newOrderInfo
	 * @param taskName
	 * @return
	 */
	private String preparePaymentData(NewOrderInfo4Log newOrderInfo, String taskName) {
		StringBuilder sqlValueBuffer = new StringBuilder();
		
		sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
		
		// order_number
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// source_order_id
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(newOrderInfo.getTid());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// origin_source_order_id
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(newOrderInfo.getTid());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// payment_time
		String paymentTime = newOrderInfo.getPayTime();
		paymentTime = DateTimeUtil.getGMTTime(paymentTime, OmsConstants.TIME_ZONE_8);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(paymentTime);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// description
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append("Finished");
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// pay_type
		String payTypeName = newOrderInfo.getPaymentGeneric1Name();
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Type.getValue(MastType.paymentMethod.getId(), payTypeName, com.voyageone.common.Constants.LANGUAGE.EN));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// pay_no
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getPaymentGeneric1Field1()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// pay_account
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(newOrderInfo.getPaymentGeneric1Field2()));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// debit
		sqlValueBuffer.append(newOrderInfo.getTotalsPayment());
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
	 * 批处理扩展订单表所需数据拼装
	 * 
	 * @param newOrderInfoList
	 * @param taskName
	 * @return
	 */
	public String[] getBatchExtOrdersSqlData(List<NewOrderInfo4Log> newOrderInfoList, String taskName) {
		StringBuilder sqlBuffer = new StringBuilder();
		
		int size = newOrderInfoList.size();
		int extOrdersSize = 0;
		
		for (int i = 0; i < size; i++) {
			// 新订单信息
			NewOrderInfo4Log newOrderInfo = newOrderInfoList.get(i);
			
			// 该渠道订单是否要插扩展表
			String isExtOrders = ChannelConfigs.getVal1(newOrderInfo.getOrderChannelId(), Name.ext_order_insert);
			// 需要插入扩展订单表
			if (Constants.ONE_CHAR.equals(isExtOrders)) {
			
				// 拼装SQL values 部分
				sqlBuffer.append(prepareExtOrderData(newOrderInfo, taskName));
				
				extOrdersSize++;
				
				if (i < (size - 1)) {
					sqlBuffer.append(Constants.COMMA_CHAR);
				}
			}
		}
		
		String extOrdersBatchStr = sqlBuffer.toString();
		if (extOrdersBatchStr.endsWith(Constants.COMMA_CHAR)) {
			extOrdersBatchStr = extOrdersBatchStr.substring(0, extOrdersBatchStr.length() - 1);
		}
		
		String[] extOrdersArray = {extOrdersBatchStr, String.valueOf(extOrdersSize)};
		return extOrdersArray;
		
	}
	
	/**
	 * 一条订单的插入oms_bt_ext_orders表语句values部分
	 * 
	 * @param newOrderInfo
	 * @param taskName
	 * @return
	 */
	private String prepareExtOrderData(NewOrderInfo4Log newOrderInfo, String taskName) {
		StringBuilder sqlValueBuffer = new StringBuilder();
		
		sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
		
		// origin_source_order_id
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(newOrderInfo.getTid());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// order_channel_id
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(newOrderInfo.getOrderChannelId());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// cart_id
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(newOrderInfo.getCartId());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// order_number
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
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
	 * 批处理扩展订单详细表所需数据拼装
	 * 
	 * @param newOrderInfoList
	 * @param taskName
	 * @return
	 */
	public String[] getBatchExtOrderDetailsSqlData(List<NewOrderInfo4Log> newOrderInfoList, String taskName) {
		StringBuilder sqlBuffer = new StringBuilder();
		
		int size = newOrderInfoList.size();
		int extOrderDetailsSize = 0;
		for (int i = 0; i < size; i++) {
			// 新订单信息
			NewOrderInfo4Log newOrderInfo = newOrderInfoList.get(i);
			
			// 该渠道订单是否要插扩展表
			String isExtOrders = ChannelConfigs.getVal1(newOrderInfo.getOrderChannelId(), Name.ext_order_insert);
			// 需要插入扩展订单表
			if (Constants.ONE_CHAR.equals(isExtOrders)) {
			
				// 拼装SQL values 部分
				String[] sqlValueAndCount = prepareExtOrderDetailData(newOrderInfo, taskName);
				
				String sqlValue = sqlValueAndCount[0];
				sqlBuffer.append(sqlValue);
				
				// 记录detail总条数
				int number = Integer.valueOf(sqlValueAndCount[1]);
				extOrderDetailsSize += number;
			}
		}
		
		String extOrderDetailsBatchStr = sqlBuffer.toString();
		if (extOrderDetailsBatchStr.endsWith(Constants.COMMA_CHAR)) {
			extOrderDetailsBatchStr = extOrderDetailsBatchStr.substring(0, extOrderDetailsBatchStr.length() - 1);
		}
		
		return new String[] { extOrderDetailsBatchStr, String.valueOf(extOrderDetailsSize) };
		
	}
	
	/**
	 * 一条订单的插入oms_bt_ext_order_details表语句values部分
	 * 
	 * @param newOrderInfo
	 * @param taskName
	 * @return
	 */
	private String[] prepareExtOrderDetailData(NewOrderInfo4Log newOrderInfo, String taskName) {
		StringBuilder sqlValueBuffer = new StringBuilder();
		
		int itemNumber = 0;
		
		String orderChannelId = newOrderInfo.getOrderChannelId();
		String cartId = newOrderInfo.getCartId();

		// sku转换配置
		String barcode2Sku = ShopConfigs.getVal1(orderChannelId, cartId, ShopConfigEnums.Name.barcode_2_sku);

		String skus = newOrderInfo.getProductSku();
		if (!StringUtils.isNullOrBlank2(skus)) {
			
			String[] skuArray = skus.split(Constants.SPLIT_CHAR_RESOLVE);
			if (skuArray != null && skuArray.length > 0) {
				// 商品名
				String names = newOrderInfo.getProductName();
				String[] nameArray = names.split(Constants.SPLIT_CHAR_RESOLVE);
				// 单项价格
				String itemPrices = newOrderInfo.getProductItemPrice();
				String[] itemPriceArray = itemPrices.split(Constants.SPLIT_CHAR_RESOLVE);
				// 数量
				String quantitys = newOrderInfo.getProductQuantity();
				String[] quantityArray = quantitys.split(Constants.SPLIT_CHAR_RESOLVE);
				
				//总价
				double dblPayment = Double.parseDouble(newOrderInfo.getTotalsPayment());
				//满就送排除的商品
				List<String> exceptOfPriceThanGift = PRICE_THAN_GIFT_EXCEPT_SETTING.get(orderChannelId + cartId);
				
				List<String> skuTotalList = new ArrayList<String>();
				
				for (int i = 0; i < skuArray.length; i++) {
					
					String name = nameArray[i];
					name = transferStr(name);
					
					String itemPrice = itemPriceArray[i];

					// 需要barcode转sku
					if (Constants.ONE_CHAR.equals(barcode2Sku)) {
						skuArray[i] = getSkuByBarcode(orderChannelId, skuArray[i]);
					}

					String sku = skuArray[i];
					sku = transferStr(sku);
					
					int quantity = Integer.valueOf(quantityArray[i]);
					
					//满就送总价中减掉排除商品的价格
					if (exceptOfPriceThanGift != null && exceptOfPriceThanGift.size() > 0) {
						for (String tmp : exceptOfPriceThanGift) {
							if (sku.toUpperCase().startsWith(tmp.toUpperCase())) {
								// 该订单折扣描述
								String disCounts = newOrderInfo.getDiscountAmount();
								String disCountDescriptions = newOrderInfo.getDiscountDescription();
								// 该sku实际价格
								double itemPriceReal = getRealItemPrice(Double.parseDouble(itemPrice), sku, disCounts, disCountDescriptions);
								dblPayment = dblPayment - (itemPriceReal * quantity);
								
								break;
							}
						}
					}
					
					// 补差价订单
					if (OmsConstants.PRICES_GAP.equalsIgnoreCase(sku)) {
						
						skuTotalList.add(sku.toUpperCase());
						
						sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
						
						// order_number
						sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// item_number
						itemNumber++;
						sqlValueBuffer.append(itemNumber);
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
						
						sqlValueBuffer.append(Constants.COMMA_CHAR);
					} else {
						for (int j = 0; j < quantity; j++) {
							
							skuTotalList.add(sku.toUpperCase());
							
							sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
							
							// order_number
							sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
							sqlValueBuffer.append(Constants.COMMA_CHAR);
							
							// item_number
							itemNumber++;
							sqlValueBuffer.append(itemNumber);
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
							
							sqlValueBuffer.append(Constants.COMMA_CHAR);
						}
					}
				}

				// 平台ID
				CartBean cartBean = ShopConfigs.getCart(cartId);
				String platformId = cartBean.getPlatform_id();
				// ----------------------------------------------------------------- 满就送赠品 START ---------------------------------------------------------------------------
				boolean blnGift = false;
				// 同一家店满就送规则是以平台还是以渠道来进行赠品
				boolean isFlatform = false;
				String platformOrCart = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRICE_THAN_SELECT_PLATFORM_CART,
						com.voyageone.common.Constants.LANGUAGE.EN);
				String platformOrCartValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + platformOrCart);
				// 平台
				if (Constants.ONE_CHAR.equals(platformOrCartValue)) {
					isFlatform = true;
				}

				// 获得该 渠道/平台 的满就送赠品设置
				List<Double> priceOfPriceThanGift = PRICE_THAN_GIFT_PRICE_SETTING.get(orderChannelId + (isFlatform ? platformId : cartId));
				if (priceOfPriceThanGift != null && priceOfPriceThanGift.size() > 0) {
					for (int i = 0; i < priceOfPriceThanGift.size(); i++) {
						if (!blnGift) {
							double dblPrice = priceOfPriceThanGift.get(i);
							if (dblPayment >= dblPrice) {
								// 获取赠品
								DecimalFormat df = new DecimalFormat("#.00");
								String price = df.format(dblPrice);

								String mapKey = orderChannelId + Constants.COMMA_CHAR + (isFlatform ? platformId : cartId) + Constants.COMMA_CHAR + price;
								List<Map<String, String>> giftMaps = PRICE_THAN_GIFT_SETTING_EXT.get(mapKey);
								if (giftMaps != null && giftMaps.size() > 0) {

									// 可重复
									String giftPropertyRepeat = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRICE_THAN_REPEAT, 
											com.voyageone.common.Constants.LANGUAGE.EN);
									// 前最大数
									String giftPropertyMaxsum = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRICE_THAN_MAXSUM,
											com.voyageone.common.Constants.LANGUAGE.EN);
									// 随机选一个
									String giftPropertySelectOne = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRICE_THAN_SELECTONE,
											com.voyageone.common.Constants.LANGUAGE.EN);
									// 选择优先顺序且有库存的赠品
									String giftPropertyAppointOneWithInventory = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRICE_THAN_SELECT_APPOINT_ONE_WITH_INVENTORY,
											com.voyageone.common.Constants.LANGUAGE.EN);

									// 满就送赠品列表
									List<String> gifts = new ArrayList<String>();

									// 任选择一款sku
									String giftPropertySelectOneValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertySelectOne);
									if (Constants.ONE_CHAR.equals(giftPropertySelectOneValue)) {
										int giftSize = giftMaps.size();
										int index = new Random().nextInt(giftSize);
										String skuSelect = giftMaps.get(index).get("giftSku");
										
										if (!StringUtils.isNullOrBlank2(skuSelect)) {
											gifts.add(skuSelect);
										}

									// 任选一款以外设置
									} else {

										// 按优先顺序选择一款赠品，当且仅当有库存
										String giftPropertySelectAppointOneWithInventoryValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertyAppointOneWithInventory);
										if (Constants.ONE_CHAR.equals(giftPropertySelectAppointOneWithInventoryValue)) {
											// 找出本次赠品选哪个及库存处理
											String giftSku = getSelectAppointOneWithInventorySku(giftMaps);
											// 多个sku
											if (giftSku.contains(";")) {
												String[] giftSkuSplitArray = giftSku.split(";");
												for (String giftSkuSplit : giftSkuSplitArray) {
													if (!StringUtils.isNullOrBlank2(giftSkuSplit)) {
														gifts.add(giftSkuSplit);
													}
												}
											} else {
												if (!StringUtils.isNullOrBlank2(giftSku)) {
													gifts.add(giftSku);
												}
											}

											// 以上情况以外满就送赠品设定
										} else {
											for (Map<String, String> giftMap : giftMaps) {
												String giftSku = giftMap.get("giftSku");
												if (!StringUtils.isNullOrBlank2(giftSku)) {
													gifts.add(giftSku);
												}
											}
										}
									}

									// 有满足条件赠品
									if (gifts.size() > 0) {
										// 可以送赠品
										if (giftRulerJudge2(newOrderInfo, giftPropertyRepeat, OmsConstants.GIFT_PROPERTY_PRICE_THAN, giftPropertyMaxsum)) {
											itemNumber = getGiftSqlExt(newOrderInfo, gifts, sqlValueBuffer, itemNumber, taskName);
											blnGift = true;
											break;
										}
									}
								}
							}
						}
					}
				}
				// ----------------------------------------------------------------- 满就送赠品 END -----------------------------------------------------------------------------

				
				// ----------------------------------------------------------------- 买就送赠品 START ---------------------------------------------------------------------------
				// 同一家店买就送规则是以平台还是以渠道来进行赠品
				isFlatform = false;
				platformOrCart = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_BUY_THAN_SELECT_PLATFORM_CART,
						com.voyageone.common.Constants.LANGUAGE.EN);
				platformOrCartValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + platformOrCart);
				// 平台
				if (Constants.ONE_CHAR.equals(platformOrCartValue)) {
					isFlatform = true;
				}

				for (String sku : skuTotalList) {
					sku = sku.toUpperCase();

					String mapKeyPart = orderChannelId + Constants.COMMA_CHAR + (isFlatform ? platformId : cartId) + Constants.COMMA_CHAR;

					// 对应买就送有优先顺及库存设置
					Set<String> buyKeySet = BUY_THAN_GIFT_SETTING_EXT.keySet();
					if (buyKeySet != null && buyKeySet.size() > 0) {
						for (String buyKey : buyKeySet) {
							if (buyKey.contains(mapKeyPart)) {
								String buySkuKey = buyKey.replace(mapKeyPart, "");
								// 买赠购买的sku可能配置多个对应一个赠品
								if (buySkuKey.contains(sku) && buySkuKey.contains(";")) {
									sku = buySkuKey;

									break;
								}
							}
						}
					}

					String mapKey = mapKeyPart + sku;
					List<Map<String, String>> giftMaps = BUY_THAN_GIFT_SETTING_EXT.get(mapKey);
					if (giftMaps != null && giftMaps.size() > 0) {

						// 可重复
						String giftPropertyRepeat = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_BUY_THAN_REPEAT,
								com.voyageone.common.Constants.LANGUAGE.EN);
						// 前最大数
						String giftPropertyMaxsum = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_BUY_THAN_MAXSUM,
								com.voyageone.common.Constants.LANGUAGE.EN);
						// 随机选一个
						String giftPropertySelectOne = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_BUY_THAN_SELECTONE,
								com.voyageone.common.Constants.LANGUAGE.EN);
						// 选择优先顺序且有库存的赠品
						String giftPropertyAppointOneWithInventory = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_BUY_THAN_SELECT_APPOINT_ONE_WITH_INVENTORY,
								com.voyageone.common.Constants.LANGUAGE.EN);

						// 买就送赠品列表
						List<String> gifts = new ArrayList<String>();

						String giftPropertySelectOneValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertySelectOne);
						// 任选择一款sku
						if (Constants.ONE_CHAR.equals(giftPropertySelectOneValue)) {
							int giftSize = giftMaps.size();
							int index = new Random().nextInt(giftSize);
							String skuSelect = giftMaps.get(index).get("giftSku");

							if (!StringUtils.isNullOrBlank2(skuSelect)) {
								gifts.add(skuSelect);
							}

						// 任选一款以外设置
						} else {
							// 按优先顺序选择一款赠品，当且仅当有库存
							String giftPropertySelectAppointOneWithInventoryValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertyAppointOneWithInventory);
							if (Constants.ONE_CHAR.equals(giftPropertySelectAppointOneWithInventoryValue)) {
								// 找出本次赠品选哪个及库存处理
								String giftSku = getSelectAppointOneWithInventorySku(giftMaps);
								// 多个sku
								if (giftSku.contains(";")) {
									String[] giftSkuSplitArray = giftSku.split(";");
									for (String giftSkuSplit : giftSkuSplitArray) {
										if (!StringUtils.isNullOrBlank2(giftSkuSplit)) {
											gifts.add(giftSkuSplit);
										}
									}
								} else {
									if (!StringUtils.isNullOrBlank2(giftSku)) {
										gifts.add(giftSku);
									}
								}

								// 以上情况以外满就送赠品设定
							} else {
								for (Map<String, String> giftMap : giftMaps) {
									String giftSku = giftMap.get("giftSku");
									if (!StringUtils.isNullOrBlank2(giftSku)) {
										gifts.add(giftSku);
									}
								}
							}
						}

						// 有满足条件赠品
						if (gifts.size() > 0) {
							// 可以送赠品
							if (giftRulerJudge2(newOrderInfo, giftPropertyRepeat, OmsConstants.GIFT_PROPERTY_BUY_THAN, giftPropertyMaxsum)) {
								itemNumber = getGiftSqlExt(newOrderInfo, gifts, sqlValueBuffer, itemNumber, taskName);
							}
						}
					}
				}
				// ----------------------------------------------------------------- 买就送赠品 END -----------------------------------------------------------------------------

				List<String> gifts = null;
				// ----------------------------------------------------------------- 老顾客送赠品 START --------------------------------------------------------------------------
				if (newOrderInfo.isRegularCustomer()) {
					gifts = REGULAR_CUSTOMER_GIFT_SETTING.get(orderChannelId + cartId);
					if (gifts != null && gifts.size() > 0) {
						
						String giftPropertyRepeat = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_REGULAR_CUSTOMER_REPEAT, 
								com.voyageone.common.Constants.LANGUAGE.EN);
						String giftPropertyMaxsum = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_REGULAR_CUSTOMER_MAXSUM,
								com.voyageone.common.Constants.LANGUAGE.EN);
						String giftPropertySelectOne = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_REGULAR_CUSTOMER_SELECTONE,
								com.voyageone.common.Constants.LANGUAGE.EN);

						String giftPropertySelectOneValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertySelectOne);
						
						// 任选择一款sku
						if (Constants.ONE_CHAR.equals(giftPropertySelectOneValue)) {
							int giftSize = gifts.size();
							int index = new Random().nextInt(giftSize);
							String skuSelect = gifts.get(index);
							
							if (!StringUtils.isNullOrBlank2(skuSelect)) {
								gifts = new ArrayList<String>();
								gifts.add(skuSelect);
							}
						}
						
						// 可以送赠品
						if (giftRulerJudge2(newOrderInfo, giftPropertyRepeat, OmsConstants.GIFT_PROPERTY_REGULAR_CUSTOMER, giftPropertyMaxsum)) {
							itemNumber = getGiftSqlExt(newOrderInfo, gifts, sqlValueBuffer, itemNumber, taskName);
						}
					}
				}
				// ----------------------------------------------------------------- 老顾客送赠品 END -----------------------------------------------------------------------------
				
				// ----------------------------------------------------------------- 前多少名送赠品 START --------------------------------------------------------------------------
				// 同一家店前多少名规则是以平台还是以渠道来进行赠品
				isFlatform = false;
				platformOrCart = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRIOR_COUNT_CUSTOMER_SELECT_PLATFORM_CART,
						com.voyageone.common.Constants.LANGUAGE.EN);
				platformOrCartValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + platformOrCart);
				// 平台
				if (Constants.ONE_CHAR.equals(platformOrCartValue)) {
					isFlatform = true;
				}

				// 前多少名排除sku
				boolean isPriorCheck = true;
				String mapKey = orderChannelId + Constants.COMMA_CHAR + (isFlatform ? platformId : cartId);
				List<String> exceptSkuList = PRIOR_COUNT_CUSTOMER_GIFT_EXCEPT_SKU_SETTING.get(mapKey);
				if (exceptSkuList != null && exceptSkuList.size() > 0) {
					for (String sku : skuTotalList) {
						// 包含排除的sku则不送赠品
						if (exceptSkuList.contains(sku)) {
							isPriorCheck = false;

							break;
						}
					}
				}

				// 可以进行前多少名送赠品
				if (isPriorCheck) {
					List<Map<String, String>> giftMaps = PRIOR_COUNT_CUSTOMER_GIFT_SETTING_EXT.get(mapKey);
					if (giftMaps != null && giftMaps.size() > 0) {
						// 可重复
						String giftPropertyRepeat = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_REPEAT,
								com.voyageone.common.Constants.LANGUAGE.EN);
						// 前最大数
						String giftPropertyMaxsum = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_MAXSUM,
								com.voyageone.common.Constants.LANGUAGE.EN);
						// 随机选一个
						String giftPropertySelectOne = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_SELECTONE,
								com.voyageone.common.Constants.LANGUAGE.EN);
						// 选择优先顺序且有库存的赠品
						String giftPropertyAppointOneWithInventory = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRIOR_COUNT_CUSTOMER_SELECT_APPOINT_ONE_WITH_INVENTORY,
								com.voyageone.common.Constants.LANGUAGE.EN);

						// 前多少名送赠品列表
						gifts = new ArrayList<String>();

						String giftPropertySelectOneValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertySelectOne);
						// 任选择一款sku
						if (Constants.ONE_CHAR.equals(giftPropertySelectOneValue)) {
							int giftSize = giftMaps.size();
							int index = new Random().nextInt(giftSize);
							String skuSelect = giftMaps.get(index).get("giftSku");

							if (!StringUtils.isNullOrBlank2(skuSelect)) {
								gifts.add(skuSelect);
							}

							// 任选一款以外设置
						} else {
							// 按优先顺序选择一款赠品，当且仅当有库存
							String giftPropertySelectAppointOneWithInventoryValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertyAppointOneWithInventory);
							if (Constants.ONE_CHAR.equals(giftPropertySelectAppointOneWithInventoryValue)) {
								// 找出本次赠品选哪个及库存处理
								String giftSku = getSelectAppointOneWithInventorySku(giftMaps);
								// 多个sku
								if (giftSku.contains(";")) {
									String[] giftSkuSplitArray = giftSku.split(";");
									for (String giftSkuSplit : giftSkuSplitArray) {
										if (!StringUtils.isNullOrBlank2(giftSkuSplit)) {
											gifts.add(giftSkuSplit);
										}
									}
								} else {
									if (!StringUtils.isNullOrBlank2(giftSku)) {
										gifts.add(giftSku);
									}
								}

								// 以上情况以外满就送赠品设定
							} else {
								for (Map<String, String> giftMap : giftMaps) {
									String giftSku = giftMap.get("giftSku");
									if (!StringUtils.isNullOrBlank2(giftSku)) {
										gifts.add(giftSku);
									}
								}
							}
						}

						// 有满足条件赠品
						if (gifts.size() > 0) {
							// 可以送赠品
							if (giftRulerJudge2(newOrderInfo, giftPropertyRepeat, OmsConstants.GIFT_PROPERTY, giftPropertyMaxsum)) {
								itemNumber = getGiftSqlExt(newOrderInfo, gifts, sqlValueBuffer, itemNumber, taskName);
							}
						}
					}
				}
				// ----------------------------------------------------------------- 前多少名送赠品 END ----------------------------------------------------------------------------
			}
		}
		
		// discount
		itemNumber = getDisCountSurchargeShipingSqlExt(newOrderInfo, Constants.DISCOUNT_TYPE, sqlValueBuffer, itemNumber, taskName);
		// surcharge
		itemNumber = getDisCountSurchargeShipingSqlExt(newOrderInfo, Constants.SURCHARGE_TYPE, sqlValueBuffer, itemNumber, taskName);
		// shipping
		itemNumber = getDisCountSurchargeShipingSqlExt(newOrderInfo, Constants.SHIPPING_TYPE, sqlValueBuffer, itemNumber, taskName);
		
		return new String[] { sqlValueBuffer.toString() , String.valueOf(itemNumber) };
	}
	
	/**
	 * 获得扩展表用 DisCount Surcharge Shiping sql value部分
	 * 
	 * @param newOrderInfo
	 * @param flag 1:disCount/2:surcharge/3:shipping
	 * @param sqlValueBuffer
	 * @param itemNumber
	 * @param taskName
	 * 
	 * @return
	 */
	private int getDisCountSurchargeShipingSqlExt(NewOrderInfo4Log newOrderInfo, 
			int flag, StringBuilder sqlValueBuffer, int itemNumber, String taskName) {
		// disCount
		if (flag == 1) {
			String disCounts = newOrderInfo.getDiscountAmount();
			if (!StringUtils.isNullOrBlank2(disCounts)) {
				String[] disCountArray = disCounts.split(Constants.SPLIT_CHAR_RESOLVE);
				
				if (disCountArray != null && disCountArray.length > 0) {
					for (int i = 0; i < disCountArray.length; i++) {
						String disCountStr = disCountArray[i];
						double disCount = Double.valueOf(disCountStr);
						if (disCount == 0) {
							continue;
						}
						
						sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
						
						// order_number
						sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// item_number
						itemNumber++;
						sqlValueBuffer.append(itemNumber);
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
						
						sqlValueBuffer.append(Constants.COMMA_CHAR);
					}
				}
			}
			
		// surcharge
		} else if (flag == 2) {
			String surcharges = newOrderInfo.getSurchargeAmount();
			if (!StringUtils.isNullOrBlank2(surcharges)) {
				String[] surchargeArray = surcharges.split(Constants.SPLIT_CHAR_RESOLVE);
				
				if (surchargeArray != null && surchargeArray.length > 0) {
					for (int i = 0; i< surchargeArray.length; i++) {
						String surchargeStr = surchargeArray[i];
						double surcharge = Double.valueOf(surchargeStr);
						if (surcharge == 0) {
							continue;
						}
						
						sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
						
						// order_number
						sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// item_number
						itemNumber++;
						sqlValueBuffer.append(itemNumber);
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
						
						sqlValueBuffer.append(Constants.COMMA_CHAR);
					}
				}
			}
			
		// shipping
		} else if (flag == 3) {
			String shippingFeeStr = newOrderInfo.getTotalsPostFee();
			if (!StringUtils.isNullOrBlank2(shippingFeeStr)) {
				double shippingFee = Double.valueOf(shippingFeeStr);
				if (shippingFee == 0) {
					return itemNumber;
				}
				
				sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
				
				// order_number
				sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// item_number
				itemNumber++;
				sqlValueBuffer.append(itemNumber);
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
				
				sqlValueBuffer.append(Constants.COMMA_CHAR);
			}
		}
		
		return itemNumber;
	}
	
	/**
	 * 获取需每条处理新订单信息
	 * 
	 * @return
	 */
	public List<NewOrderInfo4Log> getNewOrderInfo4EachFromLog() {
		List<NewOrderInfo4Log> newOrderInfo = orderDao.getNewOrderInfo4EachFromLog();
		return newOrderInfo;
	}
	
	/**
	 * 开启事物批量导入新订单
	 * 
	 * @param ordersBatchStr
	 * @param orderDetailsStrAndCount
	 * @param notesStr
	 * @param transactionsStr
	 * @param countNoGap
	 * @param ordersGroupBatchStr
	 * @param newOrderInfoList
	 * @param paymentBatchStr
	 * @param paymentSize
	 * @param extOrdersBatchStr
	 * @param extOrdersSize
	 * @param extOrderDetailsBatchStr
	 * @param extOrderDetailsSize
	 * @param taskName
	 */
	public boolean importNewOrders(String ordersBatchStr, String[] orderDetailsStrAndCount, String notesStr, 
			String transactionsStr, int countNoGap, String ordersGroupBatchStr, List<NewOrderInfo4Log> newOrderInfoList, 
			String paymentBatchStr, int paymentSize, String extOrdersBatchStr, int extOrdersSize, String extOrderDetailsBatchStr, 
			int extOrderDetailsSize, String taskName) {
		boolean isSuccess = false;
		
		int size = newOrderInfoList.size();
		
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			// 批量导入orders表
			isSuccess = insertOrdersBatchData(ordersBatchStr, size);
			
			// 成功
			if (isSuccess) {
				logger.info("批量导入orders表 insertOrdersBatchData is success");
				
				// 批量导入orderDetails表
				isSuccess = insertOrderDetailsBatchData(orderDetailsStrAndCount[0], Integer.valueOf(orderDetailsStrAndCount[1]));
				
				// 成功
				if (isSuccess) {
					logger.info("批量导入orderDetails表 insertOrderDetailsBatchData is success");
					
					// 批量导入notes表
					isSuccess = insertNotesBatchData(notesStr, size);
					
					// 成功
					if (isSuccess) {
						logger.info("批量导入notes表 insertNotesBatchData is success");
						
						// 批量插入oms_bt_transactions表
						isSuccess = insertTransactionsBatchData(transactionsStr, countNoGap);
						
						// 成功
						if (isSuccess) {
							logger.info("批量导入oms_bt_transactions表 insertTransactionsBatchData is success");
							
							isSuccess = insertGroupOrderBatchData(ordersGroupBatchStr, size);
							
							if (isSuccess) {
								logger.info("批量导入oms_bt_group_orders表 insertGroupOrderBatchData is success");
								
								// 有独立域名订单需插入payment表的场合
								if (!StringUtils.isEmpty(paymentBatchStr) && paymentSize > 0) {
									
									isSuccess = insertPaymentBatchData(paymentBatchStr, paymentSize);
									
									if (isSuccess) {
										logger.info("批量导入oms_bt_payments表insertPaymentBatchData is success");
										
									} else {
										logger.info("批量导入oms_bt_payments表insertPaymentBatchData is failure");
									}
								} 
								
								if (isSuccess) {
									// 有订单需插入扩展订单表的场合
									if (!StringUtils.isEmpty(extOrdersBatchStr) && extOrdersSize > 0) {
									
										isSuccess = insertExtOrdersBatchData(extOrdersBatchStr, extOrdersSize);
										
										if (isSuccess) {
											logger.info("批量导入oms_bt_ext_orders表insertExtOrdersBatchData is success");
											
										} else {
											logger.info("批量导入oms_bt_ext_orders表insertExtOrdersBatchData is failure");
											
										}
									}
									
									if (isSuccess) {
										// 有订单需插入扩展订单详细表的场合
										if (!StringUtils.isEmpty(extOrderDetailsBatchStr) && extOrderDetailsSize > 0) {
										
											isSuccess = insertExtOrderDetailsBatchData(extOrderDetailsBatchStr, extOrderDetailsSize);
											
											if (isSuccess) {
												logger.info("批量导入oms_bt_ext_order_details表insertExtOrderDetailsBatchData is success");
												
											} else {
												logger.info("批量导入oms_bt_ext_order_details表insertExtOrderDetailsBatchData is failure");
												
											}
										}
										
										if (isSuccess) {
											// 批处理置位历史临时表发送标志（->1）
											isSuccess = resetNewHistoryOrders(newOrderInfoList, size, taskName);
											
											// 成功
											if (isSuccess) {
												logger.info("批处理置位历史临时表发送标志（->1） resetNewHistoryOrders is success");
											} else {
												logger.info("批处理置位历史临时表发送标志（->1） resetNewHistoryOrders is failure");
											}
										}
									}
								}
							} else {
								logger.info("批量导入oms_bt_group_orders表 insertGroupOrderBatchData is failure");
							}
						} else {
							logger.info("批量导入oms_bt_transactions表 insertTransactionsBatchData is failure");
						}
					} else {
						logger.info("批量导入notes表 insertNotesBatchData is failure");
					}
				} else {
					logger.info("批量导入orderDetails表 insertOrderDetailsBatchData is failure");
				}
			} else {
				logger.info("批量导入orders表 insertOrdersBatchData is failure");
			}
			
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			
			issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
			
			isSuccess = false;
			
		} finally {
			if (isSuccess) {
				transactionManager.commit(status);
				logger.info("批量导入新订单importNewOrders is commit");
				
			} else {
				transactionManager.rollback(status);
				logger.info("批量导入新订单importNewOrders is rollback");
			}
		}
		
		return isSuccess;
	}
	
	/**
	 * 开启事物批量导入手工新订单
	 * 
	 * @param ordersBatchStr
	 * @param orderDetailsStrAndCount
	 * @param notesStr
	 * @param transactionsStr
	 * @param countNoGap
	 * @param newOrderInfoList
	 * @param paymentBatchStr
	 * @param paymentSize
	 * @param extOrdersBatchStr
	 * @param extOrdersSize
	 * @param taskName
	 */
	public boolean importNewManualOrders(String ordersBatchStr, String[] orderDetailsStrAndCount, String notesStr, 
			String transactionsStr, int countNoGap, List<NewOrderInfo4Log> newOrderInfoList, 
			String paymentBatchStr, int paymentSize, String extOrdersBatchStr, int extOrdersSize, String taskName) {
		boolean isSuccess = false;
		
		int size = newOrderInfoList.size();
		
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			// 批量导入orders表
			isSuccess = insertOrdersBatchData(ordersBatchStr, size);
			
			// 成功
			if (isSuccess) {
				logger.info("批量导入orders表 insertOrdersBatchData is success");
				
				// 批量导入orderDetails表
				isSuccess = insertOrderDetailsBatchData(orderDetailsStrAndCount[0], Integer.valueOf(orderDetailsStrAndCount[1]));
				
				// 成功
				if (isSuccess) {
					logger.info("批量导入orderDetails表 insertOrderDetailsBatchData is success");
					
					// 批量导入notes表
					isSuccess = insertNotesBatchData(notesStr, size);
					
					// 成功
					if (isSuccess) {
						logger.info("批量导入notes表 insertNotesBatchData is success");
						
						// 批量插入oms_bt_transactions表
						isSuccess = insertTransactionsBatchData(transactionsStr, countNoGap);
						
						// 成功
						if (isSuccess) {
							logger.info("批量导入oms_bt_transactions表 insertTransactionsBatchData is success");
							
							// 有独立域名订单需插入payment表的场合
							if (!StringUtils.isEmpty(paymentBatchStr) && paymentSize > 0) {
								
								isSuccess = insertPaymentBatchData(paymentBatchStr, paymentSize);
								
								if (isSuccess) {
									logger.info("批量导入oms_bt_payments表insertPaymentBatchData is success");
									
								} else {
									logger.info("批量导入oms_bt_payments表insertPaymentBatchData is failure");
								}
							} 
							
							if (isSuccess) {
								// 有订单需插入扩展订单表的场合
								if (!StringUtils.isEmpty(extOrdersBatchStr) && extOrdersSize > 0) {
								
									isSuccess = insertExtOrdersBatchData(extOrdersBatchStr, extOrdersSize);
									
									if (isSuccess) {
										logger.info("批量导入oms_bt_ext_orders表insertExtOrdersBatchData is success");
										
									} else {
										logger.info("批量导入oms_bt_ext_orders表insertExtOrdersBatchData is failure");
										
									}
								}
								
								if (isSuccess) {
									// 批处理置位历史临时表发送标志（->1）
									isSuccess = resetNewManualHistoryOrders(newOrderInfoList, size, taskName);
									
									// 成功
									if (isSuccess) {
										logger.info("批处理置位历史临时表发送标志（->1） resetNewManualHistoryOrders is success");
									} else {
										logger.info("批处理置位历史临时表发送标志（->1） resetNewManualHistoryOrders is failure");
									}
								}
							}
						} else {
							logger.info("批量导入oms_bt_transactions表 insertTransactionsBatchData is failure");
						}
					} else {
						logger.info("批量导入notes表 insertNotesBatchData is failure");
					}
				} else {
					logger.info("批量导入orderDetails表 insertOrderDetailsBatchData is failure");
				}
			} else {
				logger.info("批量导入orders表 insertOrdersBatchData is failure");
			}
			
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			
			issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
			
			isSuccess = false;
			
		} finally {
			if (isSuccess) {
				transactionManager.commit(status);
				logger.info("批量导入新订单importNewManualOrders is commit");
				
			} else {
				transactionManager.rollback(status);
				logger.info("批量导入新订单importNewManualOrders is rollback");
			}
		}
		
		return isSuccess;
	}
	
	/**
	 * 批处理插入订单表数据（字符串拼装）
	 * 
	 * @param ordersBatchStr
	 * @param size
	 * @return
	 */
	public boolean insertOrdersBatchData(String ordersBatchStr, int size) {
		return orderDao.insertOrdersBatchData(ordersBatchStr, size);
	}
	
	/**
	 * 批处理插入订单详细表数据（字符串拼装）
	 * 
	 * @param orderDetailsStr
	 * @param size
	 * @return
	 */
	public boolean insertOrderDetailsBatchData(String orderDetailsStr, int size) {
		return orderDao.insertOrderDetailsBatchData(orderDetailsStr, size);
	}
	
	/**
	 * 批处理插入notes表数据（字符串拼装）
	 * 
	 * @param orderNotesStr
	 * @param size
	 * @return
	 */
	public boolean insertNotesBatchData(String orderNotesStr, int size) {
		return orderDao.insertNotesBatchData(orderNotesStr, size);
	}
	
	/**
	 * 批处理插入交易明细表数据（字符串拼装）
	 * 
	 * @param transactionsStr
	 * @param size
	 * @return
	 */
	public boolean insertTransactionsBatchData(String transactionsStr, int size) {
		if (!StringUtils.isEmpty(transactionsStr) && size > 0) {
			return orderDao.insertTransactionsBatchData(transactionsStr, size);
		} else {
			return true;
		}
	}
	
	/**
	 * 批处理插入oms_bt_group_orders数据（字符串拼装）
	 * 
	 * @param ordersGroupBatchStr
	 * @param size
	 * @return
	 */
	public boolean insertGroupOrderBatchData(String ordersGroupBatchStr, int size) {
		if (!StringUtils.isEmpty(ordersGroupBatchStr) && size > 0) {
			return orderDao.insertGroupOrderBatchData(ordersGroupBatchStr, size);
		} else {
			return true;
		}
	}
	
	/**
	 * 批处理插入oms_bt_payments数据（字符串拼装）
	 * 
	 * @param paymentBatchStr
	 * @param size
	 * @return
	 */
	public boolean insertPaymentBatchData(String paymentBatchStr, int size) {
		if (!StringUtils.isEmpty(paymentBatchStr) && size > 0) {
			return orderDao.insertPaymentBatchData(paymentBatchStr, size);
		} else {
			return true;
		}
	}
	
	/**
	 * 批处理插入oms_bt_ext_orders数据（字符串拼装）
	 * 
	 * @param extOrdersBatchStr
	 * @param size
	 * @return
	 */
	public boolean insertExtOrdersBatchData(String extOrdersBatchStr, int size) {
		if (!StringUtils.isEmpty(extOrdersBatchStr) && size > 0) {
			return orderDao.insertExtOrdersBatchData(extOrdersBatchStr, size);
		} else {
			return true;
		}
	}
	
	/**
	 * 批处理插入oms_bt_ext_order_details数据（字符串拼装）
	 * 
	 * @param extOrderDetailsBatchStr
	 * @param size
	 * @return
	 */
	public boolean insertExtOrderDetailsBatchData(String extOrderDetailsBatchStr, int size) {
		if (!StringUtils.isEmpty(extOrderDetailsBatchStr) && size > 0) {
			return orderDao.insertExtOrderDetailsBatchData(extOrderDetailsBatchStr, size);
		} else {
			return true;
		}
	}
	
	/**
	 * 批处理置位历史临时表发送标志（1：正常结束）
	 * 
	 * @param newOrderInfoList
	 * @param size
	 * @param taskName
	 * @return
	 */
	public boolean resetNewHistoryOrders(List<NewOrderInfo4Log> newOrderInfoList, int size, String taskName) {
		StringBuilder subSqlBuilder = new StringBuilder(" select ");
		for (NewOrderInfo4Log newOrderInfo : newOrderInfoList) {
			subSqlBuilder = subSqlBuilder.append(Constants.APOSTROPHE_CHAR)
					.append(newOrderInfo.getOrderChannelId())
					.append(Constants.APOSTROPHE_CHAR)
					.append(" order_channel_id, ")
					.append(Constants.APOSTROPHE_CHAR)
					.append(newOrderInfo.getCartId())
					.append(Constants.APOSTROPHE_CHAR)
					.append(" cart_id, ")
					.append(Constants.APOSTROPHE_CHAR)
					.append(newOrderInfo.getTid())
					.append(Constants.APOSTROPHE_CHAR)
					.append(" tid union all select ");
		}
		String subSql =  subSqlBuilder.substring(0, subSqlBuilder.lastIndexOf("union"));
		return orderDao.resetNewHistoryOrders(subSql, size, taskName);
	}
	
	/**
	 * 批处理置位历史临时表发送标志（1：正常结束）
	 * 
	 * @param newOrderInfoList
	 * @param size
	 * @param taskName
	 * @return
	 */
	public boolean resetNewManualHistoryOrders(List<NewOrderInfo4Log> newOrderInfoList, int size, String taskName) {
		StringBuilder subSqlBuilder = new StringBuilder(" select ");
		for (NewOrderInfo4Log newOrderInfo : newOrderInfoList) {
			subSqlBuilder = subSqlBuilder.append(Constants.APOSTROPHE_CHAR)
					.append(newOrderInfo.getOrderChannelId())
					.append(Constants.APOSTROPHE_CHAR)
					.append(" order_channel_id, ")
					.append(Constants.APOSTROPHE_CHAR)
					.append(newOrderInfo.getCartId())
					.append(Constants.APOSTROPHE_CHAR)
					.append(" cart_id, ")
					.append(Constants.APOSTROPHE_CHAR)
					.append(newOrderInfo.getTid())
					.append(Constants.APOSTROPHE_CHAR)
					.append(" tid union all select ");
		}
		String subSql =  subSqlBuilder.substring(0, subSqlBuilder.lastIndexOf("union"));
		return orderDao.resetNewManualHistoryOrders(subSql, size, taskName);
	}
	
	/**
	 * 批处理置位历史临时表发送标志（2：批处理失败，转单条处理）
	 * 
	 * @param newOrderInfoList
	 * @param size
	 * @param taskName
	 * @return
	 */
	public boolean resetHistoryOrdersForeach(List<NewOrderInfo4Log> newOrderInfoList, int size, String taskName) {
		StringBuilder subSqlBuilder = new StringBuilder(" select ");
		for (NewOrderInfo4Log newOrderInfo : newOrderInfoList) {
			subSqlBuilder = subSqlBuilder.append(Constants.APOSTROPHE_CHAR)
					.append(newOrderInfo.getPreOrderNumber())
					.append(Constants.APOSTROPHE_CHAR)
					.append(" pre_order_number, ")
					.append(Constants.APOSTROPHE_CHAR)
					.append(newOrderInfo.getOrderChannelId())
					.append(Constants.APOSTROPHE_CHAR)
					.append(" order_channel_id, ")
					.append(Constants.APOSTROPHE_CHAR)
					.append(newOrderInfo.getCartId())
					.append(Constants.APOSTROPHE_CHAR)
					.append(" cart_id, ")
					.append(Constants.APOSTROPHE_CHAR)
					.append(newOrderInfo.getTid())
					.append(Constants.APOSTROPHE_CHAR)
					.append(" tid union all select ");
		}
		String subSql =  subSqlBuilder.substring(0, subSqlBuilder.lastIndexOf("union"));
		return orderDao.resetHistoryOrdersForeach(subSql, size, taskName);
	}
	
	/**
	 * 批处理置位手工历史临时表发送标志（2：批处理失败，转单条处理）
	 * 
	 * @param newOrderInfoList
	 * @param size
	 * @param taskName
	 * @return
	 */
	public boolean resetManualHistoryOrdersForeach(List<NewOrderInfo4Log> newOrderInfoList, int size, String taskName) {
		StringBuilder subSqlBuilder = new StringBuilder(" select ");
		for (NewOrderInfo4Log newOrderInfo : newOrderInfoList) {
			subSqlBuilder = subSqlBuilder.append(Constants.APOSTROPHE_CHAR)
					.append(newOrderInfo.getPreOrderNumber())
					.append(Constants.APOSTROPHE_CHAR)
					.append(" pre_order_number, ")
					.append(Constants.APOSTROPHE_CHAR)
					.append(newOrderInfo.getOrderChannelId())
					.append(Constants.APOSTROPHE_CHAR)
					.append(" order_channel_id, ")
					.append(Constants.APOSTROPHE_CHAR)
					.append(newOrderInfo.getCartId())
					.append(Constants.APOSTROPHE_CHAR)
					.append(" cart_id, ")
					.append(Constants.APOSTROPHE_CHAR)
					.append(newOrderInfo.getTid())
					.append(Constants.APOSTROPHE_CHAR)
					.append(" tid union all select ");
		}
		String subSql =  subSqlBuilder.substring(0, subSqlBuilder.lastIndexOf("union"));
		return orderDao.resetManualHistoryOrdersForeach(subSql, size, taskName);
	}
	
	/**
	 * 批处理置位历史临时表发送标志（3：单条处理失败，转人工处理）
	 * 
	 * @param newOrderInfoList
	 * @param taskName
	 * @return
	 */
	public boolean resetHistoryOrdersForManual(List<NewOrderInfo4Log> newOrderInfoList, String taskName) {
		StringBuilder subSqlBuilder = new StringBuilder(" select ");
		for (NewOrderInfo4Log newOrderInfo : newOrderInfoList) {
			subSqlBuilder = subSqlBuilder.append(Constants.APOSTROPHE_CHAR)
					.append(newOrderInfo.getPreOrderNumber())
					.append(Constants.APOSTROPHE_CHAR)
					.append(" pre_order_number, ")
					.append(Constants.APOSTROPHE_CHAR)
					.append(newOrderInfo.getOrderChannelId())
					.append(Constants.APOSTROPHE_CHAR)
					.append(" order_channel_id, ")
					.append(Constants.APOSTROPHE_CHAR)
					.append(newOrderInfo.getCartId())
					.append(Constants.APOSTROPHE_CHAR)
					.append(" cart_id, ")
					.append(Constants.APOSTROPHE_CHAR)
					.append(newOrderInfo.getTid())
					.append(Constants.APOSTROPHE_CHAR)
					.append(" tid union all select ");
		}
		String subSql =  subSqlBuilder.substring(0, subSqlBuilder.lastIndexOf("union"));
		return orderDao.resetHistoryOrdersForManual(subSql, taskName);
	}

	/**
	 * 获得带^@^分割的字符串数值之和
	 * @param amountStrs
	 * @return
	 */
	private double getTotalAmountFromStr(String amountStrs) {
		BigDecimal disCountBigDecimal = new BigDecimal(0);
		if (!StringUtils.isNullOrBlank2(amountStrs)) {
			String[] disCountArray = amountStrs.split(Constants.SPLIT_CHAR_RESOLVE);
			if (disCountArray != null && disCountArray.length > 0) {
				
				for (String disCountStr : disCountArray) {
					disCountBigDecimal = disCountBigDecimal.add(new BigDecimal(disCountStr));
				}
			}
		}
		
		return disCountBigDecimal.doubleValue();
	}
	
	/**
	 * 获得DisCount Surcharge Shiping sql value部分
	 * 
	 * @param subItemNumberList
	 * @param newOrderInfo
	 * @param flag 1:disCount/2:surcharge/3:shipping
	 * @param sqlValueBuffer
	 * @param itemNumber
	 * @param taskName
	 * 
	 * @return
	 */
	private int getDisCountSurchargeShipingSql(List<Map<String,Integer>> subItemNumberList, NewOrderInfo4Log newOrderInfo, 
			int flag, StringBuilder sqlValueBuffer, int itemNumber, String taskName) {
		// disCount
		if (flag == 1) {
			String disCounts = newOrderInfo.getDiscountAmount();
			String disCountDescriptions = newOrderInfo.getDiscountDescription();
			if (!StringUtils.isNullOrBlank2(disCounts)) {
				String[] disCountArray = disCounts.split(Constants.SPLIT_CHAR_RESOLVE);
				String[] disCountDescriptionArray = disCountDescriptions.split(Constants.SPLIT_CHAR_RESOLVE);
				
				if (disCountArray != null && disCountArray.length > 0) {
					for (int i = 0; i < disCountArray.length; i++) {
						String disCountStr = disCountArray[i];
						double disCount = Double.valueOf(disCountStr);
						if (disCount == 0) {
							continue;
						}
						
						sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
						
						// order_number
						sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// item_number
						itemNumber++;
						sqlValueBuffer.append(itemNumber);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// detail_date
						String payTime = newOrderInfo.getPayTime();
						// 时区转换
						String gmtPayTime = DateTimeUtil.getGMTTime(payTime, OmsConstants.TIME_ZONE_8);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(gmtPayTime);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// adjustment
						sqlValueBuffer.append(Constants.ADJUSTMENT_1);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// product
						String product = disCountDescriptionArray[i];
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(transferStr(product));
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						/** 获取折扣对应的实际sku **/
						String[] skuArray = product.split(Constants.DOLLAR_CHAR_RESOLVE);
						String sku = Constants.EMPTY_STR;
						if (skuArray != null && skuArray.length > 1) {
							sku = skuArray[1];
						}
						
						int subItemNum = 0;
						if (!StringUtils.isEmpty(sku)) {
							for (int j = 0; j < subItemNumberList.size(); j++) {
								Map<String,Integer> subItemNumMap = subItemNumberList.get(j);
								if (subItemNumMap.containsKey(sku)) {
									subItemNum = subItemNumMap.get(sku);
									subItemNumberList.remove(j);
									
									break;
								}
							}
						}
//						if (subItemNum == 0) {
//							logger.info("折扣对应的实际sku没找到！");
//						}
						
						// sub_item_number
						sqlValueBuffer.append(subItemNum);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// price_per_unit
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(-disCount);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// quantity_ordered
						sqlValueBuffer.append(Constants.ONE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// quantity_shipped
						sqlValueBuffer.append(Constants.ONE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// quantity_returned
						sqlValueBuffer.append(Constants.ZERO_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// sku
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.DISCOUNT_STR);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// status
						sqlValueBuffer.append(Constants.NULL_STR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// res_allot_flg
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.ONE_CHAR);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// synship_flg
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.ONE_CHAR);
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
						
						sqlValueBuffer.append(Constants.COMMA_CHAR);
					}
				}
			}
			
		// surcharge
		} else if (flag == 2) {
			String surcharges = newOrderInfo.getSurchargeAmount();
			String surchargeDescriptions = newOrderInfo.getSurchargeDescription();
			if (!StringUtils.isNullOrBlank2(surcharges)) {
				String[] surchargeArray = surcharges.split(Constants.SPLIT_CHAR_RESOLVE);
				String[] surchargeDescriptionArray = surchargeDescriptions.split(Constants.SPLIT_CHAR_RESOLVE);
				
				if (surchargeArray != null && surchargeArray.length > 0) {
					for (int i = 0; i< surchargeArray.length; i++) {
						String surchargeStr = surchargeArray[i];
						double surcharge = Double.valueOf(surchargeStr);
						if (surcharge == 0) {
							continue;
						}
						
						sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
						
						// order_number
						sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// item_number
						itemNumber++;
						sqlValueBuffer.append(itemNumber);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// detail_date
						String payTime = newOrderInfo.getPayTime();
						// 时区转换
						String gmtPayTime = DateTimeUtil.getGMTTime(payTime, OmsConstants.TIME_ZONE_8);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(gmtPayTime);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// adjustment
						sqlValueBuffer.append(Constants.ADJUSTMENT_1);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// product
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(transferStr(surchargeDescriptionArray[i]));
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// sub_item_number
						sqlValueBuffer.append(Constants.ZERO_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// price_per_unit
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(surcharge);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// quantity_ordered
						sqlValueBuffer.append(Constants.ONE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// quantity_shipped
						sqlValueBuffer.append(Constants.ONE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// quantity_returned
						sqlValueBuffer.append(Constants.ZERO_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// sku
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.SURCHARGE_STR);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// status
						sqlValueBuffer.append(Constants.NULL_STR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// res_allot_flg
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.ONE_CHAR);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// synship_flg
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.ONE_CHAR);
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
						
						sqlValueBuffer.append(Constants.COMMA_CHAR);
					}
				}
			}
			
		// shipping
		} else if (flag == 3) {
			String shippingFeeStr = newOrderInfo.getTotalsPostFee();
			if (!StringUtils.isNullOrBlank2(shippingFeeStr)) {
				double shippingFee = Double.valueOf(shippingFeeStr);
				if (shippingFee == 0) {
					return itemNumber;
				}
				
				sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
				
				// order_number
				sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// item_number
				itemNumber++;
				sqlValueBuffer.append(itemNumber);
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// detail_date
				String payTime = newOrderInfo.getPayTime();
				// 时区转换
				String gmtPayTime = DateTimeUtil.getGMTTime(payTime, OmsConstants.TIME_ZONE_8);
				sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
				sqlValueBuffer.append(gmtPayTime);
				sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// adjustment
				sqlValueBuffer.append(Constants.ADJUSTMENT_1);
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// product
				sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
				sqlValueBuffer.append("Shipping charge");
				sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// sub_item_number
				sqlValueBuffer.append(Constants.ZERO_CHAR);
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// price_per_unit
				sqlValueBuffer.append(shippingFee);
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// quantity_ordered
				sqlValueBuffer.append(Constants.ONE_CHAR);
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// quantity_shipped
				sqlValueBuffer.append(Constants.ONE_CHAR);
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// quantity_returned
				sqlValueBuffer.append(Constants.ZERO_CHAR);
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// sku
				sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
				sqlValueBuffer.append(Constants.SHIPPING_STR);
				sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// status
				sqlValueBuffer.append(Constants.NULL_STR);
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// res_allot_flg
				sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
				sqlValueBuffer.append(Constants.ONE_CHAR);
				sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// synship_flg
				sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
				sqlValueBuffer.append(Constants.ONE_CHAR);
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
				
				sqlValueBuffer.append(Constants.COMMA_CHAR);
			}
		}
		
		return itemNumber;
	}
	
	/**
	 * 获得transaction表用 DisCount Surcharge Shiping sql value部分
	 * 
	 * @param newOrderInfo
	 * @param flag 1:disCount/2:surcharge/3:shipping
	 * @param sqlValueBuffer
	 * @param itemNumber
	 * @param taskName
	 * 
	 * @return
	 */
	private int getDisCountSurchargeShipingSql(NewOrderInfo4Log newOrderInfo, 
			int flag, StringBuilder sqlValueBuffer, int itemNumber, String taskName) {
		// disCount
		if (flag == 1) {
			String disCounts = newOrderInfo.getDiscountAmount();
			String disCountDescriptions = newOrderInfo.getDiscountDescription();
			if (!StringUtils.isNullOrBlank2(disCounts)) {
				String[] disCountArray = disCounts.split(Constants.SPLIT_CHAR_RESOLVE);
				String[] disCountDescriptionArray = disCountDescriptions.split(Constants.SPLIT_CHAR_RESOLVE);
				
				if (disCountArray != null && disCountArray.length > 0) {
					for (int i = 0; i < disCountArray.length; i++) {
						String disCountStr = disCountArray[i];
						double disCount = Double.valueOf(disCountStr);
						if (disCount == 0) {
							continue;
						}
						
						sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
						
						// order_number
						sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// transaction_time
						String payTime = newOrderInfo.getPayTime();
						// 时区转换
						String gmtPayTime = DateTimeUtil.getGMTTime(payTime, OmsConstants.TIME_ZONE_8);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(gmtPayTime);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						/** 获取折扣对应的实际sku **/
						// product
						String product = disCountDescriptionArray[i];
						String[] skuArray = product.split(Constants.DOLLAR_CHAR_RESOLVE);
						String sku = OmsConstants.TRANSACTION_DISCOUNT;
						if (skuArray != null && skuArray.length > 1) {
							sku = skuArray[1];
						}
						// sku
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(sku);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// description
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(OmsConstants.TRANSACTION_DISCOUNT);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// item_number
						itemNumber++;
						sqlValueBuffer.append(itemNumber);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// debit
						sqlValueBuffer.append(Constants.ZERO_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// credit
						sqlValueBuffer.append(disCount);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// source_order_id
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(newOrderInfo.getTid());
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// origin_source_order_id
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(newOrderInfo.getTid());
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// type
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(OmsConstants.TRANSACTIONS_TYPE_ORDER);
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
						
						sqlValueBuffer.append(Constants.COMMA_CHAR);
					}
				}
			}
			
		// surcharge
		} else if (flag == 2) {
			String surcharges = newOrderInfo.getSurchargeAmount();
			if (!StringUtils.isNullOrBlank2(surcharges)) {
				String[] surchargeArray = surcharges.split(Constants.SPLIT_CHAR_RESOLVE);
				
				if (surchargeArray != null && surchargeArray.length > 0) {
					for (int i = 0; i< surchargeArray.length; i++) {
						String surchargeStr = surchargeArray[i];
						double surcharge = Double.valueOf(surchargeStr);
						if (surcharge == 0) {
							continue;
						}
						
						sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
						
						// order_number
						sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// transaction_time
						String payTime = newOrderInfo.getPayTime();
						// 时区转换
						String gmtPayTime = DateTimeUtil.getGMTTime(payTime, OmsConstants.TIME_ZONE_8);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(gmtPayTime);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// sku
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(OmsConstants.TRANSACTION_SURCHARGE);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// description
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(OmsConstants.TRANSACTION_SURCHARGE);
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// item_number
						itemNumber++;
						sqlValueBuffer.append(itemNumber);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// debit
						sqlValueBuffer.append(surcharge);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// credit
						sqlValueBuffer.append(Constants.ZERO_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// source_order_id
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(newOrderInfo.getTid());
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// origin_source_order_id
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(newOrderInfo.getTid());
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(Constants.COMMA_CHAR);
						
						// type
						sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
						sqlValueBuffer.append(OmsConstants.TRANSACTIONS_TYPE_ORDER);
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
						
						sqlValueBuffer.append(Constants.COMMA_CHAR);
					}
				}
			}
			
		// shipping
		} else if (flag == 3) {
			String shippingFeeStr = newOrderInfo.getTotalsPostFee();
			if (!StringUtils.isNullOrBlank2(shippingFeeStr)) {
				double shippingFee = Double.valueOf(shippingFeeStr);
				if (shippingFee == 0) {
					return itemNumber;
				}
				
				sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
				
				// order_number
				sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// transaction_time
				String payTime = newOrderInfo.getPayTime();
				// 时区转换
				String gmtPayTime = DateTimeUtil.getGMTTime(payTime, OmsConstants.TIME_ZONE_8);
				sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
				sqlValueBuffer.append(gmtPayTime);
				sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// sku
				sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
				sqlValueBuffer.append(OmsConstants.TRANSACTION_SHIPPING);
				sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// description
				sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
				sqlValueBuffer.append(OmsConstants.TRANSACTION_SHIPPING);
				sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// item_number
				itemNumber++;
				sqlValueBuffer.append(itemNumber);
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// debit
				sqlValueBuffer.append(shippingFee);
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// credit
				sqlValueBuffer.append(Constants.ZERO_CHAR);
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// source_order_id
				sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
				sqlValueBuffer.append(newOrderInfo.getTid());
				sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// origin_source_order_id
				sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
				sqlValueBuffer.append(newOrderInfo.getTid());
				sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
				sqlValueBuffer.append(Constants.COMMA_CHAR);
				
				// type
				sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
				sqlValueBuffer.append(OmsConstants.TRANSACTIONS_TYPE_ORDER);
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
				
				sqlValueBuffer.append(Constants.COMMA_CHAR);
			}
		}
		
		return itemNumber;
	}
	
	/**
	 * 获取需处理状态发生变化订单信息
	 * 
	 * @return
	 */
	public List<ChangedOrderInfo4Log> getChangedOrderInfoFromLog() {
		List<ChangedOrderInfo4Log> changedOrderInfo = orderDao.getChangedOrderInfoFromLog();
		return changedOrderInfo;
	}
	
	/**
	 * 根据原始订单号tid 获取内部订单号及订单状态
	 * 
	 * @return
	 */
	public List<Map> getOrderNumberAndStatusByTid(ChangedOrderInfo4Log changedOrderInfo) {
		return orderDao.getOrderNumberAndStatusByTid(changedOrderInfo);
		
	}
	
	/**
	 * 返回该订单状态及确认时间
	 * 
	 * @param changedOrderInfo
	 * @return
	 */
	public String[] getOrderStatusAndConfirmDate(ChangedOrderInfo4Log changedOrderInfo) {
		String changedStatus = changedOrderInfo.getStatus();
		
		String orderStatus = changedStatus;
		String confirmDate = Constants.EMPTY_STR;
		String now = DateTimeUtil.getNow(DateTimeUtil.DEFAULT_DATETIME_FORMAT);
		
		if (Constants.ALIBABA_STATUS_TRADE_SUCCESS.equalsIgnoreCase(changedStatus) || Constants.ALIBABA_STATUS_TRADE_CLOSE.equalsIgnoreCase(changedStatus)) {
//			orderStatus = Type.getValue(MastType.orderStatus.getId(), Constants.ORDER_STATUS_CONFIRMED, com.voyageone.common.Constants.LANGUAGE.EN);
			confirmDate = now;
			
		} else if (Constants.ALIBABA_STATUS_REFUND_CLOSED.equalsIgnoreCase(changedStatus)) {
			orderStatus = Type.getValue(MastType.orderStatus.getId(), Constants.ORDER_STATUS_RETURN_UNSUCCESS_CLOSED, com.voyageone.common.Constants.LANGUAGE.EN);
			confirmDate = now;
			
		} else if (Constants.ALIBABA_STATUS_REFUND_CREATED.equalsIgnoreCase(changedStatus) || Constants.ALIBABA_STATUS_REFUND_BUYER_MODIFY_AGREEMENT.equalsIgnoreCase(changedStatus)) {
			orderStatus = Type.getValue(MastType.orderStatus.getId(), Constants.ORDER_STATUS_RETURN_REQUESTED, com.voyageone.common.Constants.LANGUAGE.EN);
			
		} else if (Constants.ALIBABA_STATUS_REFUND_SELLER_AGREE_AGREEMENT.equalsIgnoreCase(changedStatus)) {
			orderStatus = Type.getValue(MastType.orderStatus.getId(), Constants.ORDER_STATUS_RETURN_APPROVED, com.voyageone.common.Constants.LANGUAGE.EN);
			
		} else if (Constants.ALIBABA_STATUS_REFUND_SELLER_REFUSE_AGREEMENT.equalsIgnoreCase(changedStatus)) {
			orderStatus = Type.getValue(MastType.orderStatus.getId(), Constants.ORDER_STATUS_RETURN_REFUSED, com.voyageone.common.Constants.LANGUAGE.EN);
			
		} else if (Constants.ALIBABA_STATUS_REFUND_SUCCESS.equalsIgnoreCase(changedStatus)) {
			orderStatus = getRefundStatus(changedOrderInfo);
		}

		return new String[] {orderStatus, confirmDate};
	}
	
	/**
	 * tradeSuccess状态消息重复判断
	 * 
	 * @param changedOrderInfo
	 * @return
	 */
	public boolean isRepeatTradeSuccess(ChangedOrderInfo4Log changedOrderInfo) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("originSourceOrderId", changedOrderInfo.getTid());
		
		return orderDao.isRepeatTradeSuccess(dataMap);
	}
	
	/**
	 * 退款类型判断
	 * 
	 * @param changedOrderInfo
	 * @return
	 */
	private String getRefundStatus(ChangedOrderInfo4Log changedOrderInfo) {
		String orderStatus = Constants.EMPTY_STR;
		
		String feeStr = changedOrderInfo.getFee();
		double fee = 0;
		if (!StringUtils.isNullOrBlank2(feeStr)) {
			fee = Double.valueOf(feeStr);
		}
		
		double[] refundOrderPrice = orderDao.getOrderPrice4Refund(changedOrderInfo);
		double finalProductTotal = refundOrderPrice[0];
		double pricePerUnit = refundOrderPrice[1];
		double finalGrandTotal = refundOrderPrice[2];
		
		if (fee > finalProductTotal) {
			orderStatus = Type.getValue(MastType.orderStatus.getId(), Constants.ORDER_STATUS_RETURNED, com.voyageone.common.Constants.LANGUAGE.EN);
		} else if (finalGrandTotal - fee == pricePerUnit) {
			orderStatus = Type.getValue(MastType.orderStatus.getId(), Constants.ORDER_STATUS_RETURNED, com.voyageone.common.Constants.LANGUAGE.EN);
		} else if (finalGrandTotal == pricePerUnit) {
			orderStatus = Type.getValue(MastType.orderStatus.getId(), Constants.ORDER_STATUS_RETURNED, com.voyageone.common.Constants.LANGUAGE.EN);
		}
		
		return orderStatus;
	}
	
	/**
	 * 开启事物插入Notes表、payments表、置位oms_changed_orders_import_history表发送标志
	 * 
	 * @param changedOrderInfo
	 * @param orderStatus
	 * @param orderInfoMapList
	 * @param taskName
	 * @return
	 */
	public boolean tradeSuccessUpdate(ChangedOrderInfo4Log changedOrderInfo, String orderStatus, 
			List<Map> orderInfoMapList, String taskName) {
		boolean isSuccess = false;
		
		List<String> orderNumberList = new ArrayList<String>();
		List<String> orderKindList = new ArrayList<String>();
		List<String> payTypeList = new ArrayList<String>();
		List<String> payNoList = new ArrayList<String>();
		List<String> payAccountList = new ArrayList<String>();
		List<String> sourceOrderIdList = new ArrayList<String>();
		for (Map dataMap : orderInfoMapList) {
			orderNumberList.add(String.valueOf(dataMap.get("orderNumber")));
			orderKindList.add(String.valueOf(dataMap.get("orderKind")));
			payTypeList.add(String.valueOf(dataMap.get("payType")));
			payNoList.add(String.valueOf(dataMap.get("payNo")));
			payAccountList.add(String.valueOf(dataMap.get("payAccount")));
			sourceOrderIdList.add(String.valueOf(dataMap.get("sourceOrderId")));
		}
		
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			int size = orderNumberList.size();
			if (size > 0) {
				
				// 差价订单类型
				String priceDifferenceOrderType = Type.getValue(MastType.orderType.getId(), Constants.ORDER_TYPE_PRICE_DIFFERENCE, com.voyageone.common.Constants.LANGUAGE.EN);
				boolean isPriceDifferenceHandle = false;
				// 已经绑定的差价订单
				if (size == 1 && !sourceOrderIdList.get(0).equals(changedOrderInfo.getTid()) && priceDifferenceOrderType.equals(orderKindList.get(0))) {
					isPriceDifferenceHandle = true;
				}
				
				// 插入notes表
				String originalOrderType = Type.getValue(MastType.orderType.getId(), Constants.ORDER_TYPE_ORIGINAL, com.voyageone.common.Constants.LANGUAGE.EN);
				int indexOriginal = -1;
				
				// 已经绑定的差价订单
				if (isPriceDifferenceHandle) {
					indexOriginal = 0;
				} else {
					for (int i = 0; i < orderKindList.size(); i++) {
						String orderKind = orderKindList.get(i);
						if (originalOrderType.equals(orderKind)) {
							indexOriginal = i;
							break;
						}
					}
				}
				
				if (indexOriginal > -1) {
					String orderNumberOriginal = orderNumberList.get(indexOriginal);
					// 原始订单号
					String originSourceOrderId = changedOrderInfo.getTid();
					// 组订单号
					String sourceOrderId = sourceOrderIdList.get(indexOriginal);
					
					isSuccess = insertChangedNotesData(originSourceOrderId, orderNumberOriginal, orderStatus, taskName);
					
					if (isSuccess) {
						logger.info("插入notes表 insertChangedNotesData is success. orderNumberOriginal:" + orderNumberOriginal + " originSourceOrderId:" + originSourceOrderId);
						
						// 获得实际金额（如有售中退款，需要减掉退款申请金额）
						double fee = getTradeSuccessFee(changedOrderInfo, sourceOrderId);
						logger.info("实际金额:" + fee);
						
						if (fee != 0) {
							// 插入payment表
							String paymentTime = changedOrderInfo.getModifiedTime();
							paymentTime = DateTimeUtil.getGMTTime(paymentTime, OmsConstants.TIME_ZONE_8);
							
							isSuccess = insertPayments(paymentTime, originSourceOrderId, sourceOrderId, orderNumberOriginal, payTypeList.get(indexOriginal), 
									payNoList.get(indexOriginal), payAccountList.get(indexOriginal), fee, taskName);
						}
						
						// 成功
						if (isSuccess) {
							if (fee == 0) {
								logger.info("插入Payments表 insertPayments is skip");
							} else {
								logger.info("插入Payments表 insertPayments is success");
							}
							
							// 更新oms_bt_group_orders表payment_total
							isSuccess = updateGroupPaymentTotal(changedOrderInfo, sourceOrderId, taskName);
							
							if (isSuccess) {
								logger.info("更新oms_bt_group_orders表payment_total updateGroupPaymentTotal is success");
								
								// 置位订单状态变化历史临时表发送标志（->1）
								isSuccess = resetChangedHistoryOrders(changedOrderInfo, taskName);
								
								if (isSuccess) {
									logger.info("置位订单状态变化历史临时表发送标志 resetChangedHistoryOrders is success");
									
								} else {
									logger.info("置位订单状态变化历史临时表发送标志 resetChangedHistoryOrders is failure");
								}
							} else {
								logger.info("更新oms_bt_group_orders表payment_total updateGroupPaymentTotal is failure");
							}
							
						} else {
							logger.info("插入Payments表 insertPayments is failure");
						}
					} else {
						logger.info("插入notes表 insertChangedNotesData is failure. orderNumberOriginal:" + orderNumberOriginal);
					}
				} else {
					logger.info("tradeSuccessUpdate 没有找到原始订单 tid:" + changedOrderInfo.getTid());
				}
			} else {
				// 订单号不存在
				//logger.info("tid:" + changedOrderInfo.getTid() + "在订单表里不存在, 等待下次执行");
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			
			isSuccess = false;
			issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
		} finally {
		
			if (isSuccess) {
				transactionManager.commit(status);
				
				logger.info("tradeSuccessUpdate is commit");
			} else {
				transactionManager.rollback(status);
				
				logger.info("tradeSuccessUpdate is rollback");
				issueLog.log("OrderInfoImportService.tradeSuccessUpdate", 
						"tradeSuccessUpdate is rollback, sourceOrderId:" + changedOrderInfo.getTid() + " orderStatus:" + orderStatus, 
						ErrorType.BatchJob, SubSystem.OMS, "数据可能不整合导致");
			}
		}
		return isSuccess;
	}
	
	/**
	 * 开启事物插入Notes表、payments表、更新oms_bt_group_orders表payment_total、置位oms_changed_orders_import_history表发送标志
	 * 
	 * @param changedOrderInfo
	 * @param orderStatus
	 * @param orderInfoMapList
	 * @param taskName
	 * @return
	 */
	public boolean refundSuccessUpdate(ChangedOrderInfo4Log changedOrderInfo, String orderStatus, 
			List<Map> orderInfoMapList, String taskName) {
		boolean isSuccess = false;
		
		List<String> orderNumberList = new ArrayList<String>();
		List<String> orderKindList = new ArrayList<String>();
		List<String> payTypeList = new ArrayList<String>();
		List<String> payNoList = new ArrayList<String>();
		List<String> payAccountList = new ArrayList<String>();
		List<String> sourceOrderIdList = new ArrayList<String>();
		for (Map dataMap : orderInfoMapList) {
			orderNumberList.add(String.valueOf(dataMap.get("orderNumber")));
			orderKindList.add(String.valueOf(dataMap.get("orderKind")));
			payTypeList.add(String.valueOf(dataMap.get("payType")));
			payNoList.add(String.valueOf(dataMap.get("payNo")));
			payAccountList.add(String.valueOf(dataMap.get("payAccount")));
			sourceOrderIdList.add(String.valueOf(dataMap.get("sourceOrderId")));
		}
		
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			int size = orderNumberList.size();
			if (size > 0) {
				// 差价订单类型
				String priceDifferenceOrderType = Type.getValue(MastType.orderType.getId(), Constants.ORDER_TYPE_PRICE_DIFFERENCE, com.voyageone.common.Constants.LANGUAGE.EN);
				boolean isPriceDifferenceHandle = false;
				// 已经绑定的差价订单
				if (size == 1 && !sourceOrderIdList.get(0).equals(changedOrderInfo.getTid()) && priceDifferenceOrderType.equals(orderKindList.get(0))) {
					isPriceDifferenceHandle = true;
				}
				
				// 插入notes表
				String originalOrderType = Type.getValue(MastType.orderType.getId(), Constants.ORDER_TYPE_ORIGINAL, com.voyageone.common.Constants.LANGUAGE.EN);
				int indexOriginal = -1;
				
				// 已经绑定的差价订单
				if (isPriceDifferenceHandle) {
					indexOriginal = 0;
				} else {
					for (int i = 0; i < orderKindList.size(); i++) {
						String orderKind = orderKindList.get(i);
						if (originalOrderType.equals(orderKind)) {
							indexOriginal = i;
							break;
						}
					}
				}
				
				if (indexOriginal > -1) {
					String orderNumberOriginal = orderNumberList.get(indexOriginal);
					
					// 原始订单号
					String originSourceOrderId = changedOrderInfo.getTid();
					// 组订单号
					String sourceOrderId = sourceOrderIdList.get(indexOriginal);
					
					isSuccess = insertChangedNotesData(originSourceOrderId, orderNumberOriginal, orderStatus, taskName);
					
					if (isSuccess) {
						logger.info("插入notes表 insertChangedNotesData is success. orderNumberOriginal:" + orderNumberOriginal);
						
						// 插入payment表
						String paymentTime = changedOrderInfo.getModifiedTime();
						paymentTime = DateTimeUtil.getGMTTime(paymentTime, OmsConstants.TIME_ZONE_8);
						double fee = Double.valueOf(changedOrderInfo.getFee());
						// credit
						fee = -fee;
						
						isSuccess = insertPayments(paymentTime, originSourceOrderId, sourceOrderId, orderNumberOriginal, payTypeList.get(indexOriginal), 
								payNoList.get(indexOriginal), payAccountList.get(indexOriginal), fee, taskName);
						// 成功
						if (isSuccess) {
							logger.info("插入Payments表 insertPayments is success");
							
							// 更新oms_bt_group_orders表payment_total
							isSuccess = updateGroupPaymentTotal(changedOrderInfo, sourceOrderId, taskName);
							
							if (isSuccess) {
								logger.info("更新oms_bt_group_orders表payment_total updateGroupPaymentTotal is success");
								
								// 置位订单状态变化历史临时表发送标志（->1）
								isSuccess = resetChangedHistoryOrders(changedOrderInfo, taskName);
								
								if (isSuccess) {
									logger.info("置位订单状态变化历史临时表发送标志 resetChangedHistoryOrders is success");
									
								} else {
									logger.info("置位订单状态变化历史临时表发送标志 resetChangedHistoryOrders is failure");
								}
							} else {
								logger.info("更新oms_bt_group_orders表payment_total updateGroupPaymentTotal is failure");
							}
							
						} else {
							logger.info("插入Payments表 insertPayments is failure");
						}
					} else {
						logger.info("插入notes表 insertChangedNotesData is failure. orderNumberOriginal:" + orderNumberOriginal);
					}
				} else {
					logger.info("refundSuccessUpdate 没有找到原始订单 tid:" + changedOrderInfo.getTid());
				}
			} else {
				// 订单号不存在
				//logger.info("tid:" + changedOrderInfo.getTid() + "在订单表里不存在, 等待下次执行");
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			
			isSuccess = false;
			issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
		} finally {
		
			if (isSuccess) {
				transactionManager.commit(status);
				
				logger.info("refundSuccessUpdate is commit");
			} else {
				transactionManager.rollback(status);
				
				logger.info("refundSuccessUpdate is rollback");
				issueLog.log("OrderInfoImportService.refundSuccessUpdate", 
						"refundSuccessUpdate is rollback, sourceOrderId:" + changedOrderInfo.getTid() + " orderStatus:" + orderStatus, 
						ErrorType.BatchJob, SubSystem.OMS, "数据可能不整合导致");
			}
		}
		return isSuccess;
	}
	
	/**
	 * 开启事物插入Notes表、更新rufund表process_flag、置位oms_changed_orders_import_history表发送标志
	 * 
	 * @param changedOrderInfo
	 * @param orderStatus
	 * @param orderInfoMapList
	 * @param taskName
	 * @return
	 */
	public boolean refundClosedUpdate(ChangedOrderInfo4Log changedOrderInfo, String orderStatus, 
			List<Map> orderInfoMapList, String taskName) {
		boolean isSuccess = false;
		
		List<String> orderNumberList = new ArrayList<String>();
		List<String> orderKindList = new ArrayList<String>();
		List<String> sourceOrderIdList = new ArrayList<String>();
		for (Map dataMap : orderInfoMapList) {
			orderNumberList.add(String.valueOf(dataMap.get("orderNumber")));
			orderKindList.add(String.valueOf(dataMap.get("orderKind")));
			sourceOrderIdList.add(String.valueOf(dataMap.get("sourceOrderId")));
		}
		
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			int size = orderNumberList.size();
			if (size > 0) {
				// 差价订单类型
				String priceDifferenceOrderType = Type.getValue(MastType.orderType.getId(), Constants.ORDER_TYPE_PRICE_DIFFERENCE, com.voyageone.common.Constants.LANGUAGE.EN);
				boolean isPriceDifferenceHandle = false;
				// 已经绑定的差价订单
				if (size == 1 && !sourceOrderIdList.get(0).equals(changedOrderInfo.getTid()) && priceDifferenceOrderType.equals(orderKindList.get(0))) {
					isPriceDifferenceHandle = true;
				}
				
				// 插入notes表
				String originalOrderType = Type.getValue(MastType.orderType.getId(), Constants.ORDER_TYPE_ORIGINAL, com.voyageone.common.Constants.LANGUAGE.EN);
				int indexOriginal = -1;
				
				// 已经绑定的差价订单
				if (isPriceDifferenceHandle) {
					indexOriginal = 0;
				} else {
					for (int i = 0; i < orderKindList.size(); i++) {
						String orderKind = orderKindList.get(i);
						if (originalOrderType.equals(orderKind)) {
							indexOriginal = i;
							break;
						}
					}
				}
				
				if (indexOriginal > -1) {
					String orderNumberOriginal = orderNumberList.get(indexOriginal);
					
					// 原始订单号
					String originSourceOrderId = changedOrderInfo.getTid();
					// 组订单号
					String sourceOrderId = sourceOrderIdList.get(indexOriginal);
					
					isSuccess = insertChangedNotesData(originSourceOrderId, orderNumberOriginal, orderStatus, taskName);
					
					if (isSuccess) {
						logger.info("插入notes表 insertChangedNotesData is success. orderNumberOriginal:" + orderNumberOriginal);
						
						// 更新oms_bt_order_refunds表process_flag-->1、refund_status-->CLOSED
						isSuccess = updateRefundProcessFlag(changedOrderInfo, sourceOrderId, taskName);
						
						if (isSuccess) {
							logger.info("更新oms_bt_order_refunds表process_flag updateRefundProcessFlag is success");
							
							// 更新oms_bt_group_orders表refund_total - (该次Refund Closed 对应的 refund_fee)
							isSuccess = updateGroupRefundTotal4Closed(changedOrderInfo, sourceOrderId, taskName);
							
							if (isSuccess) {
								logger.info("更新oms_bt_group_orders表refund_total - (该次Refund Closed 对应的 refund_fee) is success");
							
								// 置位订单状态变化历史临时表发送标志（->1）
								isSuccess = resetChangedHistoryOrders(changedOrderInfo, taskName);
								
								if (isSuccess) {
									logger.info("置位订单状态变化历史临时表发送标志 resetChangedHistoryOrders is success");
									
								} else {
									logger.info("置位订单状态变化历史临时表发送标志 resetChangedHistoryOrders is failure");
								}
							} else {
								logger.info("更新oms_bt_group_orders表refund_total - (该次Refund Closed 对应的 refund_fee) is failure");
							}
						} else {
							logger.info("更新oms_bt_order_refunds表process_flag updateRefundProcessFlag is failure");
						}
							
					} else {
						logger.info("插入notes表 insertChangedNotesData is failure. orderNumberOriginal:" + orderNumberOriginal);
					}
				} else {
					logger.info("refundClosedUpdate 没有找到原始订单 tid:" + changedOrderInfo.getTid());
				}
			} else {
				// 订单号不存在
				//logger.info("tid:" + changedOrderInfo.getTid() + "在订单表里不存在, 等待下次执行");
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			
			isSuccess = false;
			issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
		} finally {
		
			if (isSuccess) {
				transactionManager.commit(status);
				
				logger.info("refundClosedUpdate is commit");
			} else {
				transactionManager.rollback(status);
				
				logger.info("refundClosedUpdate is rollback");
				
				issueLog.log("OrderInfoImportService.refundClosedUpdate", 
						"refundClosedUpdate is rollback, sourceOrderId:" + changedOrderInfo.getTid() + " orderStatus:" + orderStatus, 
						ErrorType.BatchJob, SubSystem.OMS, "数据可能不整合导致");
			}
		}
		return isSuccess;
	}
	
	/**
	 * 开启事物插入Notes表、oms_alibaba_refund表、置位oms_changed_orders_import_history表发送标志
	 * 
	 * @param changedOrderInfo
	 * @param orderStatus
	 * @param orderInfoMapList
	 * @param taskName
	 * @param sourceOrderStatus
	 * @return
	 */
	public boolean refundCreatedUpdate(ChangedOrderInfo4Log changedOrderInfo, String orderStatus, 
			List<Map> orderInfoMapList, String taskName, String sourceOrderStatus) {
		boolean isSuccess = false;
		
		List<String> orderNumberList = new ArrayList<String>();
		List<String> orderKindList = new ArrayList<String>();
		List<String> sourceOrderIdList = new ArrayList<String>();
		for (Map dataMap : orderInfoMapList) {
			orderNumberList.add(String.valueOf(dataMap.get("orderNumber")));
			orderKindList.add(String.valueOf(dataMap.get("orderKind")));
			sourceOrderIdList.add(String.valueOf(dataMap.get("sourceOrderId")));
		}
		
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			int size = orderNumberList.size();
			if (size > 0) {
				// 差价订单类型
				String priceDifferenceOrderType = Type.getValue(MastType.orderType.getId(), Constants.ORDER_TYPE_PRICE_DIFFERENCE, com.voyageone.common.Constants.LANGUAGE.EN);
				boolean isPriceDifferenceHandle = false;
				// 已经绑定的差价订单
				if (size == 1 && !sourceOrderIdList.get(0).equals(changedOrderInfo.getTid()) && priceDifferenceOrderType.equals(orderKindList.get(0))) {
					isPriceDifferenceHandle = true;
				}
				
				// 插入notes表
				String originalOrderType = Type.getValue(MastType.orderType.getId(), Constants.ORDER_TYPE_ORIGINAL, com.voyageone.common.Constants.LANGUAGE.EN);
				int indexOriginal = -1;
				
				// 已经绑定的差价订单
				if (isPriceDifferenceHandle) {
					indexOriginal = 0;
				} else {
					for (int i = 0; i < orderKindList.size(); i++) {
						String orderKind = orderKindList.get(i);
						if (originalOrderType.equals(orderKind)) {
							indexOriginal = i;
							break;
						}
					}
				}
				
				if (indexOriginal > -1) {
					String orderNumberOriginal = orderNumberList.get(indexOriginal);
					
					// 原始订单号
					String originSourceOrderId = changedOrderInfo.getTid();
					// 组订单号
					String sourceOrderId = sourceOrderIdList.get(indexOriginal);
					
					String notes = "";
					if (Constants.ALIBABA_STATUS_REFUND_CREATED.equals(sourceOrderStatus)) {
						notes = "Order Status changed to: " + sourceOrderStatus + "(Locked) updated by batchJob.";
					} else {
						notes = "Order Status changed to: " + sourceOrderStatus + " updated by batchJob.";
					}
					isSuccess = insertChangedNotesData(originSourceOrderId, orderNumberOriginal, orderStatus, notes, taskName);
				
					if (isSuccess) {
						logger.info("插入notes表 insertChangedNotesData is success");
						
						// 插入更新Refund表
						String refundTime = changedOrderInfo.getModifiedTime();
						refundTime = DateTimeUtil.getGMTTime(refundTime, OmsConstants.TIME_ZONE_8);
						String orderChannelId = changedOrderInfo.getOrderChannelId();
						String cartId = changedOrderInfo.getCartId();
						String refundId = changedOrderInfo.getRid();
						String refundPhase = changedOrderInfo.getRefundPhase();
						
						Map<String, String> dataMap = new HashMap<String, String>();
						dataMap.put("sourceOrderId", sourceOrderId);
						dataMap.put("originSourceOrderId", originSourceOrderId);
						dataMap.put("orderChannelId", orderChannelId);
						dataMap.put("cartId", cartId);
						dataMap.put("refundTime", refundTime);
						dataMap.put("orderStatus", orderStatus);
						dataMap.put("refundId", refundId);
						dataMap.put("refundFee", changedOrderInfo.getFee());
						dataMap.put("refundPhase", refundPhase);
						dataMap.put("taskName", taskName);
						
						logger.info("退款处理开始。originSourceOrderId:" + originSourceOrderId + " sourceOrderId:" + sourceOrderId + " orderStatus:" + orderStatus + " refundId:" + refundId);
						
						// 退款创建时
						if (Constants.ALIBABA_STATUS_REFUND_CREATED.equals(sourceOrderStatus)) {
							// 插入退款表
							isSuccess = insertRefundData(dataMap);
							// 成功
							if (isSuccess) {
								logger.info("插入oms_bt_order_refunds表 insertRefundData is success");
								
								// 更新一组订单表锁定
								isSuccess = lockOrders(sourceOrderId, orderChannelId, cartId, taskName);
								
								// 订单锁定成功
								if (isSuccess) {
									logger.info("订单表锁定 lockOrders is success");
									
									// 更新oms_bt_group_orders表refund_total
									isSuccess = updateGroupRefundTotal(changedOrderInfo, sourceOrderId, taskName);
									
									if (isSuccess) {
										logger.info("更新oms_bt_group_orders表refund_total is success");
										
										// 置位订单状态变化历史临时表发送标志（->1）
										isSuccess = resetChangedHistoryOrders(changedOrderInfo, taskName);
										
										if (isSuccess) {
											logger.info("置位订单状态变化历史临时表发送标志 resetChangedHistoryOrders is success");
											
										} else {
											logger.info("置位订单状态变化历史临时表发送标志 resetChangedHistoryOrders is failure");
										}
									} else {
										logger.info("更新oms_bt_group_orders表refund_total is failure");
									}
								} else {
									logger.info("订单表锁定 lockOrders is failure");
								}
									
							} else {
								logger.info("插入oms_bt_order_refunds表 insertRefundData is failure");
							}
						
						// 买家修改退款协议
						} else {
							// 更新退款表
							isSuccess = updateRefundData(dataMap);
							// 成功
							if (isSuccess) {
								logger.info("插入oms_bt_order_refunds表 insertRefundData is success");
								
								// 更新oms_bt_group_orders表refund_total
								isSuccess = updateGroupRefundTotal(changedOrderInfo, sourceOrderId, taskName);
								
								if (isSuccess) {
									logger.info("更新oms_bt_group_orders表refund_total is success");
									
									// 置位订单状态变化历史临时表发送标志（->1）
									isSuccess = resetChangedHistoryOrders(changedOrderInfo, taskName);
									
									if (isSuccess) {
										logger.info("置位订单状态变化历史临时表发送标志 resetChangedHistoryOrders is success");
										
									} else {
										logger.info("置位订单状态变化历史临时表发送标志 resetChangedHistoryOrders is failure");
									}
								} else {
									logger.info("更新oms_bt_group_orders表refund_total is failure");
								}
							} else {
								logger.info("更新oms_bt_order_refunds表 updateRefundData is failure");
							}
						}
					} else {
						logger.info("插入notes表 insertChangedNotesData is failure");
					}
				} else {
					logger.info("refundCreatedUpdate 没有找到原始订单 tid:" + changedOrderInfo.getTid());
				}
			} else {
				// 订单号不存在
				logger.info("tid:" + changedOrderInfo.getTid() + "在订单表里不存在, 等待下次执行");
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			
			isSuccess = false;
			issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
			
		} finally {
		
			if (isSuccess) {
				transactionManager.commit(status);
				
				logger.info("refundCreatedUpdate is commit");
			} else {
				transactionManager.rollback(status);
				
				logger.info("refundCreatedUpdate is rollback");
				issueLog.log("OrderInfoImportService.refundCreatedUpdate", 
						"refundCreatedUpdate is rollback, sourceOrderId:" + changedOrderInfo.getTid() + 
						" sourceOrderStatus:" + sourceOrderStatus + " orderStatus:" + orderStatus, 
						ErrorType.BatchJob, SubSystem.OMS, "数据可能不整合导致");
			}
		}
		return isSuccess;
	}
	
	/**
	 * 开启事物更新 订单表、订单详细表，插入Notes表、置位oms_changed_orders_import_history表发送标志
	 * 
	 * @param orderNumber
	 * @param orderStatus
	 * @param confirmDate
	 * @param changedOrderInfo
	 * @param taskName
	 * @return
	 */
	public boolean importChangedOrders(String orderNumber, String orderStatus, String confirmDate, ChangedOrderInfo4Log changedOrderInfo, String taskName) {
		boolean isSuccess = false;
		
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			// 更新orders表
			isSuccess = updateChangedOrdersData(orderNumber, orderStatus, confirmDate, changedOrderInfo, taskName);
			
			// 成功
			if (isSuccess) {
				logger.info("更新orders表 updateChangedOrdersData is success");
				
				// 更新orderDetails表
				isSuccess = updateChangedOrderDetailsData(orderNumber, orderStatus, taskName);
				
				// 成功
				if (isSuccess) {
					logger.info("更新orderDetails表 updateChangedOrderDetailsData is success");
					
					// 插入notes表
					isSuccess = insertChangedNotesData(changedOrderInfo.getTid(), orderNumber, orderStatus, taskName);
					
					// 成功
					if (isSuccess) {
						logger.info("插入notes表 insertChangedNotesData is success");
						
						// 置位订单状态变化历史临时表发送标志（->1）
						isSuccess = resetChangedHistoryOrders(changedOrderInfo, taskName);
						
						// 更新synship tt_orders表
						if (isSuccess) {
							logger.info("置位订单状态变化历史临时表发送标志 resetChangedHistoryOrders is success");
							
//							isSuccess = updateSynShipOrdersStatus(orderNumber, orderStatus, changedOrderInfo, taskName);
//							
//							if (isSuccess) {
//								logger.info("同步synship订单状态变化 updateSynShipOrdersStatus is success");
//							} else {
//								logger.info("同步synship订单状态变化 updateSynShipOrdersStatus is failure");
//							}
						} else {
							logger.info("置位订单状态变化历史临时表发送标志 resetChangedHistoryOrders is failure");
						}
						
					} else {
						logger.info("插入notes表 insertChangedNotesData is failure");
					}
				} else {
					logger.info("更新orderDetails表 updateChangedOrderDetailsData is failure");
				}
			} else {
				logger.info("更新orders表 updateChangedOrdersData is failure");
			}
			
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			
			isSuccess = false;
		} finally {
		
			if (isSuccess) {
				transactionManager.commit(status);
				
				logger.info("importChangedOrders is commit");
			} else {
				transactionManager.rollback(status);
				
				logger.info("importChangedOrders is rollback");
			}
		}
		return isSuccess;
	}
	
	/**
	 * 根据状态变化信息更新orders表
	 * 
	 * @param orderNumber
	 * @param orderStatus
	 * @param confirmDate
	 * @param changedOrderInfo
	 * @param taskName
	 * 
	 * @return
	 */
	public boolean updateChangedOrdersData(String orderNumber, String orderStatus, String confirmDate, ChangedOrderInfo4Log changedOrderInfo, String taskName) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("orderChannelId", changedOrderInfo.getOrderChannelId());
		dataMap.put("cartId", changedOrderInfo.getCartId());
		dataMap.put("sourceOrderId", changedOrderInfo.getTid());
		dataMap.put("orderNumber", orderNumber);
		dataMap.put("orderStatus", orderStatus);
		dataMap.put("sourceOrderStatus", changedOrderInfo.getStatus());
		dataMap.put("confirmDate", confirmDate);
		dataMap.put("taskName", taskName);
		
		return orderDao.updateChangedOrdersData(dataMap);
	}
	
	/**
	 * 根据状态变化信息更新orderDetails表
	 * 
	 * @param orderNumber
	 * @param orderStatus
	 * @param taskName
	 * 
	 * @return
	 */
	public boolean updateChangedOrderDetailsData(String orderNumber, String orderStatus, String taskName) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("orderNumber", orderNumber);
		dataMap.put("orderStatus", orderStatus);
		dataMap.put("taskName", taskName);
		
		dataMap.put("statusReturned", Type.getValue(MastType.orderStatus.getId(), Constants.ORDER_STATUS_RETURNED, com.voyageone.common.Constants.LANGUAGE.EN));
		dataMap.put("statusCanceled", Type.getValue(MastType.orderStatus.getId(), Constants.ORDER_STATUS_CANCELED, com.voyageone.common.Constants.LANGUAGE.EN));
		dataMap.put("statusConfirmed", Type.getValue(MastType.orderStatus.getId(), Constants.ORDER_STATUS_CONFIRMED, com.voyageone.common.Constants.LANGUAGE.EN));
		
		return orderDao.updateChangedOrderDetailsData(dataMap);
	}
	
	/**
	 * 根据状态变化信息插入Notes表
	 * 
	 * @param sourceOrderId
	 * @param orderNumber
	 * @param orderStatus
	 * @param taskName
	 * 
	 * @return
	 */
	public boolean insertChangedNotesData(String sourceOrderId, String orderNumber, String orderStatus, String taskName) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("type", Constants.NOTES_SYSTEM);
		dataMap.put("orderNumber", orderNumber);
		String entryDateTime = DateTimeUtil.getNow(DateTimeUtil.DEFAULT_DATETIME_FORMAT);
		dataMap.put("itemNumber", Constants.ZERO_CHAR);
		dataMap.put("entryDate", entryDateTime);
		dataMap.put("entryTime", entryDateTime);
		dataMap.put("notes", "Order Status changed to: " + orderStatus + " updated by batchJob.");
		dataMap.put("enteredBy", taskName);
		dataMap.put("parentType", Constants.NOTES_SYSTEM);
		dataMap.put("parentKey", orderNumber);
		dataMap.put("sourceOrderId", sourceOrderId);
		dataMap.put("taskName", taskName);
		
		return orderDao.insertChangedNotesData(dataMap);
	}
	
	/**
	 * 根据状态变化信息插入Notes表
	 * 
	 * @param sourceOrderId
	 * @param orderNumber
	 * @param orderStatus
	 * @param notes
	 * @param taskName
	 * 
	 * @return
	 */
	public boolean insertChangedNotesData(String sourceOrderId, String orderNumber, String orderStatus, String notes, String taskName) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("type", Constants.NOTES_SYSTEM);
		dataMap.put("orderNumber", orderNumber);
		String entryDateTime = DateTimeUtil.getNow(DateTimeUtil.DEFAULT_DATETIME_FORMAT);
		dataMap.put("itemNumber", Constants.ZERO_CHAR);
		dataMap.put("entryDate", entryDateTime);
		dataMap.put("entryTime", entryDateTime);
		dataMap.put("notes", notes);
		dataMap.put("enteredBy", taskName);
		dataMap.put("parentType", Constants.NOTES_SYSTEM);
		dataMap.put("parentKey", orderNumber);
		dataMap.put("sourceOrderId", sourceOrderId);
		dataMap.put("taskName", taskName);
		
		return orderDao.insertChangedNotesData(dataMap);
	}
	
	/**
	 * 付款成功状态变化信息插入payments表
	 * 
	 * @param changedOrderInfo
	 * @param sourceOrderId
	 * 
	 * @return
	 */
	public double getTradeSuccessFee(ChangedOrderInfo4Log changedOrderInfo, String sourceOrderId) {
		// 付款成功消息金额
		double tradeSuccessFee = 0;
		String tradeSuccessFeeStr = changedOrderInfo.getFee();
		if (StringUtils.isNumeric(tradeSuccessFeeStr)) {
			tradeSuccessFee = Double.parseDouble(tradeSuccessFeeStr);
		}
		logger.info("tradesuccess fee. tid:" + changedOrderInfo.getTid() + " fee:" + tradeSuccessFee);
		
		// 售中退款金额合计
		double refundFee = orderDao.getOnSaleRefundTotalFee(changedOrderInfo, sourceOrderId);
		logger.info("onsale refundFee. tid:" + changedOrderInfo.getTid() + " sourceOrderId:" + sourceOrderId + " fee:" + refundFee);
		
		return (tradeSuccessFee - refundFee);
	}
	
	/**
	 * 付款成功状态变化信息插入payments表
	 * 
	 * @param paymentTime
	 * @param originSourceOrderId
	 * @param sourceOrderId
	 * @param orderNumber
	 * @param payType
	 * @param payNo
	 * @param payAccount
	 * @param fee
	 * @param taskName
	 * 
	 * @return
	 */
	public boolean insertPayments(String paymentTime, String originSourceOrderId, String sourceOrderId, String orderNumber, 
			String payType, String payNo, String payAccount, double fee, String taskName) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("orderNumber", orderNumber);
		dataMap.put("sourceOrderId", sourceOrderId);
		dataMap.put("originSourceOrderId", originSourceOrderId);
		dataMap.put("paymentTime", paymentTime);
		dataMap.put("description", "Finished");
		dataMap.put("payType", StringUtils.null2Space2(payType));
		dataMap.put("payNo", StringUtils.null2Space2(payNo));
		dataMap.put("payAccount", StringUtils.null2Space2(payAccount));
		if (fee >= 0) {
			dataMap.put("abs", "0");
			dataMap.put("debit", String.valueOf(fee));
		} else {
			dataMap.put("abs", "1");
			dataMap.put("credit", String.valueOf(Math.abs(fee)));
		}
		dataMap.put("taskName", taskName);
		
		return orderDao.insertPaymentsData(dataMap);
	}
	
	/**
	 * 更新组订单PaymentTotal
	 * 
	 * @param changedOrderInfo
	 * @param sourceOrderId
	 * @param taskName
	 * 
	 * @return
	 */
	public boolean updateGroupPaymentTotal(ChangedOrderInfo4Log changedOrderInfo, String sourceOrderId, String taskName) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("sourceOrderId", sourceOrderId);
		dataMap.put("fee", changedOrderInfo.getFee());
		dataMap.put("taskName", taskName);
		
		return orderDao.updateGroupPaymentTotal(dataMap);
	}
	
	/**
	 * 更新oms_bt_order_refunds表的process_flag
	 * 
	 * @param changedOrderInfo
	 * @param sourceOrderId
	 * @param taskName
	 * 
	 * @return
	 */
	public boolean updateRefundProcessFlag(ChangedOrderInfo4Log changedOrderInfo, String sourceOrderId, String taskName) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("sourceOrderId", sourceOrderId);
		dataMap.put("originSourceOrderId", changedOrderInfo.getTid());
		dataMap.put("orderChannelId", changedOrderInfo.getOrderChannelId());
		dataMap.put("cartId", changedOrderInfo.getCartId());
		dataMap.put("refundId", changedOrderInfo.getRid());
		dataMap.put("taskName", taskName);
		
		return orderDao.updateRefundProcessFlag(dataMap);
	}
	
	/**
	 * 更新组订单refund_total
	 * 
	 * @param changedOrderInfo
	 * @param sourceOrderId
	 * @param taskName
	 * 
	 * @return
	 */
	public boolean updateGroupRefundTotal(ChangedOrderInfo4Log changedOrderInfo, String sourceOrderId, String taskName) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("sourceOrderId", sourceOrderId);
		dataMap.put("fee", changedOrderInfo.getFee());
		dataMap.put("taskName", taskName);
		
		return orderDao.updateGroupRefundTotal(dataMap);
	}
	
	/**
	 * 关闭退款时更新组订单refund_total
	 * 
	 * @param changedOrderInfo
	 * @param sourceOrderId
	 * @param taskName
	 * 
	 * @return
	 */
	public boolean updateGroupRefundTotal4Closed(ChangedOrderInfo4Log changedOrderInfo, String sourceOrderId, String taskName) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("sourceOrderId", sourceOrderId);
		dataMap.put("fee", changedOrderInfo.getFee());
		dataMap.put("taskName", taskName);
		
		return orderDao.updateGroupRefundTotal4Closed(dataMap);
	}
	
	/**
	 * 退款创建信息插入oms_bt_order_refunds表
	 * 
	 * @param dataMap
	 * 
	 * @return
	 */
	public boolean insertRefundData(Map<String, String> dataMap) {
		return orderDao.insertRefundData(dataMap);
	}
	
	/**
	 * 买家修改退款协议信息更新oms_bt_order_refunds表
	 * 
	 * @param dataMap
	 * 
	 * @return
	 */
	public boolean updateRefundData(Map<String, String> dataMap) {
		return orderDao.updateRefundData(dataMap);
	}
	
	/**
	 * 锁单操作
	 * 
	 * @param sourceOrderId
	 * @param orderChannelId
	 * @param cartId
	 * @param taskName
	 * @return
	 */
	public boolean lockOrders(String sourceOrderId, String orderChannelId, String cartId, String taskName) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("sourceOrderId", sourceOrderId);
		dataMap.put("orderChannelId", orderChannelId);
		dataMap.put("cartId", cartId);
		dataMap.put("taskName", taskName);
		
		return orderDao.lockOrders(dataMap);
	}
	
	/**
	 * 置位订单状态变化历史临时表发送标志
	 * 
	 * @param changedOrderInfo
	 * @param taskName
	 * @return
	 */
	public boolean resetChangedHistoryOrders(ChangedOrderInfo4Log changedOrderInfo, String taskName) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("orderChannelId", changedOrderInfo.getOrderChannelId());
		dataMap.put("cartId", changedOrderInfo.getCartId());
		dataMap.put("tid", changedOrderInfo.getTid());
		dataMap.put("status", changedOrderInfo.getStatus());
		dataMap.put("rid", changedOrderInfo.getRid());
		dataMap.put("modifiedTime", changedOrderInfo.getModifiedTime());
		dataMap.put("taskName", taskName);
		
		return orderDao.resetChangedHistoryOrders(dataMap);
	}
	
	/**
	 * 锁定synship tt_orders表
	 * 
	 * @param orderInfoMapList
	 * @param changedOrderInfo
	 * @param taskName
	 * 
	 * @return
	 */
	public boolean lockSynShipOrders(List<Map> orderInfoMapList, ChangedOrderInfo4Log changedOrderInfo, String taskName) {
		Map<String, String> dataMap = new HashMap<String, String>();
		
		StringBuilder sb = new StringBuilder();
		sb.append(Constants.LEFT_BRACKET_CHAR);
		int size = orderInfoMapList.size();
		List<String> sourceOrderIdList = new ArrayList<String>();
		List<String> orderKindList = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			Map orderMap = orderInfoMapList.get(i);
			sb.append(Constants.APOSTROPHE_CHAR);
			sb.append(String.valueOf(orderMap.get("orderNumber")));
			sb.append(Constants.APOSTROPHE_CHAR);
			if (i < (size - 1)) {
				sb.append(Constants.COMMA_CHAR);
			}
			sourceOrderIdList.add(String.valueOf(orderMap.get("sourceOrderId")));
			orderKindList.add(String.valueOf(orderMap.get("orderKind")));
		}
		sb.append(Constants.RIGHT_BRACKET_CHAR);
		
		dataMap.put("orderNumber", sb.toString());
		dataMap.put("taskName", taskName);
		
		
		// 差价订单类型
		String priceDifferenceOrderType = Type.getValue(MastType.orderType.getId(), Constants.ORDER_TYPE_PRICE_DIFFERENCE, com.voyageone.common.Constants.LANGUAGE.EN);
		boolean isPriceDifferenceHandle = false;
		// 已经绑定的差价订单
		if (size == 1 && !sourceOrderIdList.get(0).equals(changedOrderInfo.getTid()) && priceDifferenceOrderType.equals(orderKindList.get(0))) {
			isPriceDifferenceHandle = true;
		}
		
		String originalOrderType = Type.getValue(MastType.orderType.getId(), Constants.ORDER_TYPE_ORIGINAL, com.voyageone.common.Constants.LANGUAGE.EN);
		int indexOriginal = 0;
		
		// 已经绑定的差价订单
		if (isPriceDifferenceHandle) {
			indexOriginal = 0;
		} else {
			for (int i = 0; i < orderKindList.size(); i++) {
				String orderKind = orderKindList.get(i);
				if (originalOrderType.equals(orderKind)) {
					indexOriginal = i;
					break;
				}
			}
		}
		
		String sourceOrderId = sourceOrderIdList.get(indexOriginal);
		dataMap.put("sourceOrderId", sourceOrderId);
		
		return orderDao.updateSynShipOrdersLock(dataMap);
	}
	
	/**
	 * 根据状态变化信息更新synship tt_orders表
	 * 
	 * @param orderNumber
	 * @param orderStatus
	 * @param changedOrderInfo
	 * @param taskName
	 * 
	 * @return
	 */
	public boolean updateSynShipOrdersStatus(String orderNumber, String orderStatus, ChangedOrderInfo4Log changedOrderInfo, String taskName) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("orderChannelId", changedOrderInfo.getOrderChannelId());
		dataMap.put("cartId", changedOrderInfo.getCartId());
		dataMap.put("orderNumber", orderNumber);
		dataMap.put("orderStatus", orderStatus);
		dataMap.put("sourceOrderStatus", changedOrderInfo.getStatus());
		dataMap.put("taskName", taskName);
		
		return orderDao.updateSynShipOrdersStatus(dataMap);
	}
	
	/**
	 * 从OMS取出需要同步到synship的订单记录
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getSynShipNewOrderInfo() {
		return orderDao.getSynShipNewOrderInfo();
	}
	
	/**
	 * 记录本轮已送赠品的各维度规则的顾客信息
	 * 
	 * @return
	 */
	public void recordHavingGiftedCustomerInfo() {
		if (HAVING_GIFTED_CUSTOMER_INFO_LOCAL != null && HAVING_GIFTED_CUSTOMER_INFO_LOCAL.size() > 0) {
			for (String mapKey : HAVING_GIFTED_CUSTOMER_INFO_LOCAL.keySet()) {
				String[] mapKeyArray = mapKey.split(Constants.COMMA_CHAR);
				if (mapKeyArray != null && mapKeyArray.length >= 4) {
					if (OmsConstants.HAVING_GIFTED_CUSTOMER_NAME.equals(mapKeyArray[3])) {
						String orderChannelId = mapKeyArray[0];
						String platformId = mapKeyArray[1];
						String type = mapKeyArray[2];
						
						List<String> buyerNickList = (List<String>)HAVING_GIFTED_CUSTOMER_INFO_LOCAL.get(mapKey);
						if (buyerNickList != null && buyerNickList.size() > 0) {
							int size = buyerNickList.size();
							
							StringBuilder sqlBuffer = new StringBuilder();
							for (int i = 0; i < size; i++) {
								String buyerNick = buyerNickList.get(i);
								
								// 拼装SQL values 部分
								sqlBuffer.append(prepareGiftedCustomerData(platformId, orderChannelId, buyerNick, type));
								
								if (i < (size - 1)) {
									sqlBuffer.append(Constants.COMMA_CHAR);
								}
							}
							
							orderDao.recordHavingGiftedCustomerInfo(sqlBuffer.toString(), size);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 一条已获得赠品顾客的插入oms_having_gifted_customer_setting表语句values部分
	 * 
	 * @param platformId
	 * @param orderChannelId
	 * @param buyerNick
	 * @param type
	 * @return
	 */
	private String prepareGiftedCustomerData(String platformId, String orderChannelId, String buyerNick, String type) {
		StringBuilder sqlValueBuffer = new StringBuilder();
		
		sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
		
		// platform_id
		sqlValueBuffer.append(platformId);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// order_channel_id
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(orderChannelId);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// buyer_nick
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(transferStr(buyerNick));
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// type
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(type);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		
		sqlValueBuffer.append(Constants.RIGHT_BRACKET_CHAR);
		
		return sqlValueBuffer.toString();
	}

	/**
	 * 本轮订单插入结束之后回写满就送配置表里的赠品剩余库存
	 *
	 * @return
	 */
	public void recordPriceThanGiftInventory() {
		if (PRICE_THAN_GIFT_SETTING != null && PRICE_THAN_GIFT_SETTING.size() > 0) {
			String giftPropertyAppointOneWithInventory = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRICE_THAN_SELECT_APPOINT_ONE_WITH_INVENTORY,
					com.voyageone.common.Constants.LANGUAGE.EN);

			for (String mapKey : PRICE_THAN_GIFT_SETTING.keySet()) {

				String[] mapKeyArray = mapKey.split(Constants.COMMA_CHAR);

				if (mapKeyArray != null && mapKeyArray.length == 3) {
					String orderChannelId = mapKeyArray[0];
					String cartId = mapKeyArray[1];
					String price = mapKeyArray[2];

					String giftPropertySelectAppointOneWithInventoryValue = GIFT_PROPERTY_SETTING.get(orderChannelId + cartId + giftPropertyAppointOneWithInventory);
					if (giftPropertySelectAppointOneWithInventoryValue == null) {
						CartBean cartBean = ShopConfigs.getCart(cartId);
						String platformId = cartBean.getPlatform_id();

						if (!StringUtils.isNullOrBlank2(platformId)) {
							giftPropertySelectAppointOneWithInventoryValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertyAppointOneWithInventory);
						}
					}
					// 有满就送根据库存优先送的配置
					if (Constants.ONE_CHAR.equals(giftPropertySelectAppointOneWithInventoryValue)) {
						List<Map<String, String>> giftMapList = PRICE_THAN_GIFT_SETTING.get(mapKey);
						if (giftMapList != null && giftMapList.size() > 0) {
							for (Map<String, String> giftMap : giftMapList) {
								Map<String, String> paraMap = new HashMap<String, String>();
								paraMap.put("orderChannelId", orderChannelId);
								paraMap.put("cartId", cartId);
								paraMap.put("price", price.split("\\.")[0]);
								paraMap.put("giftSku", giftMap.get("giftSku"));
								paraMap.put("inventory", giftMap.get("inventory"));

								orderDao.recordPriceThanGiftInventory(paraMap);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 本轮订单插入结束之后回写买就送配置表里的赠品剩余库存
	 *
	 * @return
	 */
	public void recordBuyThanGiftInventory() {
		if (BUY_THAN_GIFT_SETTING != null && BUY_THAN_GIFT_SETTING.size() > 0) {
			String giftPropertyAppointOneWithInventory = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_BUY_THAN_SELECT_APPOINT_ONE_WITH_INVENTORY,
					com.voyageone.common.Constants.LANGUAGE.EN);

			for (String mapKey : BUY_THAN_GIFT_SETTING.keySet()) {

				String[] mapKeyArray = mapKey.split(Constants.COMMA_CHAR);

				if (mapKeyArray != null && mapKeyArray.length == 3) {
					String orderChannelId = mapKeyArray[0];
					String cartId = mapKeyArray[1];
					String sku = mapKeyArray[2];

					String giftPropertySelectAppointOneWithInventoryValue = GIFT_PROPERTY_SETTING.get(orderChannelId + cartId + giftPropertyAppointOneWithInventory);
					if (giftPropertySelectAppointOneWithInventoryValue == null) {
						CartBean cartBean = ShopConfigs.getCart(cartId);
						String platformId = cartBean.getPlatform_id();

						if (!StringUtils.isNullOrBlank2(platformId)) {
							giftPropertySelectAppointOneWithInventoryValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertyAppointOneWithInventory);
						}
					}
					// 有买就送根据库存优先送的配置
					if (Constants.ONE_CHAR.equals(giftPropertySelectAppointOneWithInventoryValue)) {
						List<Map<String, String>> giftMapList = BUY_THAN_GIFT_SETTING.get(mapKey);
						if (giftMapList != null && giftMapList.size() > 0) {
							for (Map<String, String> giftMap : giftMapList) {
								Map<String, String> paraMap = new HashMap<String, String>();
								paraMap.put("orderChannelId", orderChannelId);
								paraMap.put("cartId", cartId);
								paraMap.put("sku", sku);
								paraMap.put("giftSku", giftMap.get("giftSku"));
								paraMap.put("inventory", giftMap.get("inventory"));

								orderDao.recordBuyThanGiftInventory(paraMap);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 本轮订单插入结束之后回写前多少名配置表里的赠品剩余库存
	 *
	 * @return
	 */
	public void recordPriorCountCustomerGiftInventory() {
		if (PRIOR_COUNT_CUSTOMER_GIFT_SETTING != null && PRIOR_COUNT_CUSTOMER_GIFT_SETTING.size() > 0) {
			String giftPropertyAppointOneWithInventory = Type.getValue(MastType.giftRuleType.getId(), OmsConstants.GIFT_PROPERTY_PRIOR_COUNT_CUSTOMER_SELECT_APPOINT_ONE_WITH_INVENTORY,
					com.voyageone.common.Constants.LANGUAGE.EN);

			for (String mapKey : PRIOR_COUNT_CUSTOMER_GIFT_SETTING.keySet()) {

				String[] mapKeyArray = mapKey.split(Constants.COMMA_CHAR);

				if (mapKeyArray != null && mapKeyArray.length == 2) {
					String orderChannelId = mapKeyArray[0];
					String cartId = mapKeyArray[1];

					String giftPropertySelectAppointOneWithInventoryValue = GIFT_PROPERTY_SETTING.get(orderChannelId + cartId + giftPropertyAppointOneWithInventory);
					if (giftPropertySelectAppointOneWithInventoryValue == null) {
						CartBean cartBean = ShopConfigs.getCart(cartId);
						String platformId = cartBean.getPlatform_id();

						if (!StringUtils.isNullOrBlank2(platformId)) {
							giftPropertySelectAppointOneWithInventoryValue = GIFT_PROPERTY_SETTING.get(orderChannelId + platformId + giftPropertyAppointOneWithInventory);
						}
					}
					// 有前多少名就送根据库存优先送的配置
					if (Constants.ONE_CHAR.equals(giftPropertySelectAppointOneWithInventoryValue)) {
						List<Map<String, String>> giftMapList = PRIOR_COUNT_CUSTOMER_GIFT_SETTING.get(mapKey);
						if (giftMapList != null && giftMapList.size() > 0) {
							for (Map<String, String> giftMap : giftMapList) {
								Map<String, String> paraMap = new HashMap<String, String>();
								paraMap.put("orderChannelId", orderChannelId);
								paraMap.put("cartId", cartId);
								paraMap.put("giftSku", giftMap.get("giftSku"));
								paraMap.put("inventory", giftMap.get("inventory"));

								orderDao.recordPriorCountCustomerGiftInventory(paraMap);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 检查收件人信息完整性检查，如果信息不全会发警告邮件
	 * 
	 * @return
	 */
	public void checkReceiverInfo(List<NewOrderInfo4Log> newOrderInfoList) {
		if (newOrderInfoList != null && newOrderInfoList.size() > 0) {
			
			StringBuilder tbody = new StringBuilder();
			
			for (NewOrderInfo4Log orderInfo : newOrderInfoList) {
				// 收件人姓名
				String shipName = orderInfo.getShippingReceiverName();
				// 收件人电话
				String shipPhone = orderInfo.getShippingReceiverPhone();
				// 收件人地址:国家
				String shipCountry = orderInfo.getShippingAddressCountry();
				// 收件人地址:省
				String shipState = orderInfo.getShippingAddressState();
				// 收件人地址:市
				String shipCity = orderInfo.getShippingAddressCity();
				// 收件人地址:邮编
				String shipZip = orderInfo.getShippingAddressZip();
				// 收件人地址：地区
				String shipAddress1 = orderInfo.getShippingAddressStreet1();
				// 收件人地址：地址
				String shipAddress2 = orderInfo.getShippingAddressStreet2();
				// 收件人地址: 合并地址
				String shipAddress = shipAddress1 + shipAddress2;
				// 快递方式
				String shippingType = orderInfo.getTotalsShippingType();
				
				// 国内电商收件人信息不需要邮编
				if (Sale.CN.getType().equals(ChannelConfigs.getVal1(orderInfo.getOrderChannelId(), Name.sale_type))) {
					if (StringUtils.isNullOrBlank2(shipZip)) {
						shipZip = Constants.BLANK_STR;
					}
				}
				
				// 收件人信息不全
				if (StringUtils.isNullOrBlank2(shipName) ||
					StringUtils.isNullOrBlank2(shipPhone) ||
					StringUtils.isNullOrBlank2(shipCountry) ||
					StringUtils.isNullOrBlank2(shipState) ||
					StringUtils.isNullOrBlank2(shipCity) ||
					StringUtils.isNullOrBlank2(shipZip) ||
					StringUtils.isNullOrBlank2(shipAddress) ||
					StringUtils.isNullOrBlank2(shippingType)) {
					
					String orderChannelId = orderInfo.getOrderChannelId();
					OrderChannelBean orderChannelBean = ChannelConfigs.getChannel(orderChannelId);
					String orderNumber = orderInfo.getPreOrderNumber();
					String sourceOrderId = orderInfo.getTid();
					
					// 邮件每行正文
					String mailTextLine = 
						String.format(OmsConstants.SHIP_INFO_CHECK_ROW, orderChannelBean.getFull_name(), orderNumber, sourceOrderId, shipName, 
								shipPhone, shipCountry, shipState, shipCity, shipZip, shipAddress, shippingType);
					tbody.append(mailTextLine);
				}
			}
			
			// 收件人信息不全，需要发警告邮件
			if (tbody.length() > 0) {
				// 拼接table
				String body = String.format(OmsConstants.SHIP_INFO_CHECK_TABLE, OmsConstants.SHIP_INFO_CHECK_HEAD, tbody.toString());
				
				// 拼接邮件正文
				StringBuilder emailContent = new StringBuilder();
				emailContent.append(Constants.EMAIL_STYLE_STRING).append(body);
				try {
					Mail.sendAlert("ITOMS", OmsConstants.SHIP_INFO_CHECK_SUBJECT, emailContent.toString(), true);
					logger.info("邮件发送成功!");
					
				} catch (MessagingException e) {
					logger.info("邮件发送失败！" + e);
				}
			}
		}
	}
	
	/**
	 * 检查没有自动approved的订单
	 * 
	 * @return
	 */
	public void checkNotApprovedInfo(List<NewOrderInfo4Log> newOrderInfoList) {
		if (newOrderInfoList != null && newOrderInfoList.size() > 0) {
			
			StringBuilder tbody = new StringBuilder();
			
			for (NewOrderInfo4Log orderInfo : newOrderInfoList) {
				
				// 没有自动approved的订单该店铺是否需要发邮件
				String notApprovedMail = ChannelConfigs.getVal1(orderInfo.getOrderChannelId(), Name.not_approved_mail);
				if (Constants.ONE_CHAR.equals(notApprovedMail)) {
					// 店铺渠道
					String orderChannelId = orderInfo.getOrderChannelId();
					// approved
					boolean isApproved = false;
					// 斯伯丁
					if (OmsConstants.CHANNEL_SPALDING.equals(orderChannelId)) {
						isApproved = isAutoApprovedSpalding(orderInfo);
					} else {
						isApproved = isAutoApproved(orderInfo);
					}
					
					if (!isApproved) {
						OrderChannelBean orderChannelBean = ChannelConfigs.getChannel(orderChannelId);
						String orderNumber = orderInfo.getPreOrderNumber();
						String sourceOrderId = orderInfo.getTid();
						
						// 邮件每行正文
						String mailTextLine = 
							String.format(OmsConstants.NOT_APPROVED_CHECK_ROW, orderChannelBean.getFull_name(), orderNumber, sourceOrderId);
						tbody.append(mailTextLine);
					}
				}
			}
			
			// 需要发警告邮件
			if (tbody.length() > 0) {
				// 拼接table
				String body = String.format(OmsConstants.NOT_APPROVED_CHECK_TABLE, OmsConstants.NOT_APPROVED_CHECK_HEAD, tbody.toString());
				
				// 拼接邮件正文
				StringBuilder emailContent = new StringBuilder();
				emailContent.append(Constants.EMAIL_STYLE_STRING).append(body);
				try {
					// TODO 目前只有皇马配了没有自动Approved要发邮件，如果以后别的渠道也有这个配置，需要改代码
					String receiver = "CUSTOMER_SERVICE_REALMADRID";
					Mail.sendAlert(receiver, OmsConstants.NOT_APPROVED_CHECK_SUBJECT, emailContent.toString(), true);
					logger.info("邮件发送成功!");
					
				} catch (MessagingException e) {
					logger.info("邮件发送失败！" + e);
				}
			}
		}
	}
	
	/**
	 * 取得新订单history表中最新订单信息的时间, 以此时间作为向京东请求的开始时间
	 * 
	 * @return
	 */
	public String getLastHistoryNewOrderTime(int preTime, String orderChannelId, String cartId) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("orderChannelId",  orderChannelId);
		dataMap.put("cartId", cartId);
		
		String lastOrderTime = orderDao.getLastHistoryNewOrderTime(dataMap);
		// 如果没取到时间
		if (StringUtils.isNullOrBlank2(lastOrderTime)) {
			Date date = DateTimeUtil.addMinutes(DateTimeUtil.getDate(), -preTime);
			lastOrderTime = DateTimeUtil.format(date, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
			
			// 格林威治时间转北京时间
			lastOrderTime = DateTimeUtil.getLocalTime(lastOrderTime, OmsConstants.TIME_ZONE_8);
			
		// 数据库表本来就是北京时间
		} else {
			Date date = DateTimeUtil.parse(lastOrderTime, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
			date = DateTimeUtil.addMinutes(date, -preTime);
			lastOrderTime = DateTimeUtil.format(date, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
		}
		
		logger.info("新订单history表中最新订单信息的往前推" + preTime + "分钟之后的北京时间为：" + lastOrderTime);
		
		return lastOrderTime;
	}
	
	/**
	 * 取得新订单history表中最新订单信息的时间, 以此时间作为向聚美请求的开始时间
	 * 
	 * @return
	 */
	public String getLastHistoryNewOrderTime4Jumei(int preTime, String cartId) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("cartId", cartId);
		
		String lastOrderTime = orderDao.getLastHistoryNewOrderTime4Jumei(dataMap);
		// 如果没取到时间
		if (StringUtils.isNullOrBlank2(lastOrderTime)) {
			Date date = DateTimeUtil.addMinutes(DateTimeUtil.getDate(), -preTime);
			lastOrderTime = DateTimeUtil.format(date, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
			
			// 格林威治时间转北京时间
			lastOrderTime = DateTimeUtil.getLocalTime(lastOrderTime, OmsConstants.TIME_ZONE_8);
			
		// 数据库表本来就是北京时间
		} else {
			Date date = DateTimeUtil.parse(lastOrderTime, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
			date = DateTimeUtil.addMinutes(date, -preTime);
			lastOrderTime = DateTimeUtil.format(date, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
		}
		
		logger.info("新订单history表中最新订单信息的往前推" + preTime + "分钟之后的北京时间为：" + lastOrderTime);
		
		return lastOrderTime;
	}
	
	/**
	 * 取得订单状态变化history表中最新订单信息的时间, 以此时间作为向京东请求的开始时间
	 * 
	 * @return
	 */
	public String getLastHistoryChangedOrderTime(int preTime, String orderChannelId, String cartId) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("orderChannelId",  orderChannelId);
		dataMap.put("cartId", cartId);
		
		String lastOrderTime = orderDao.getLastHistoryChangedOrderTime(dataMap);
		// 如果没取到时间
		if (StringUtils.isNullOrBlank2(lastOrderTime)) {
			Date date = DateTimeUtil.addMinutes(DateTimeUtil.getDate(), -preTime);
			lastOrderTime = DateTimeUtil.format(date, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
			
			// 格林威治时间转北京时间
			lastOrderTime = DateTimeUtil.getLocalTime(lastOrderTime, OmsConstants.TIME_ZONE_8);
			
		// 数据库表本来就是北京时间
		} else {
			Date date = DateTimeUtil.parse(lastOrderTime, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
			date = DateTimeUtil.addMinutes(date, -preTime);
			lastOrderTime = DateTimeUtil.format(date, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
		}
		
		logger.info("订单状态变化history表中最新订单信息的往前推" + preTime + "分钟之后的北京时间为：" + lastOrderTime);
		
		return lastOrderTime;
	}
	
	/**
	 * 获得赠品sql
	 * @param newOrderInfo
	 * @param gifts
	 * @param sqlValueBuffer
	 * @param itemNumber
	 * @param taskName
	 * @return
	 */
	private int getGiftSql(NewOrderInfo4Log newOrderInfo, List<String> gifts, StringBuilder sqlValueBuffer,
						   int itemNumber, String taskName){
		for (int i = 0; i < gifts.size(); i++) {
			sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);

			// order_number
			sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
			sqlValueBuffer.append(Constants.COMMA_CHAR);

			// item_number
			itemNumber++;
			sqlValueBuffer.append(itemNumber);
			sqlValueBuffer.append(Constants.COMMA_CHAR);

			// detail_date
			String payTime = newOrderInfo.getPayTime();
			// 时区转换
			String gmtPayTime = DateTimeUtil.getGMTTime(payTime, OmsConstants.TIME_ZONE_8);
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append(gmtPayTime);
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append(Constants.COMMA_CHAR);

			// adjustment
			sqlValueBuffer.append(Constants.ADJUSTMENT_0);
			sqlValueBuffer.append(Constants.COMMA_CHAR);

			// product
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append("赠品");
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append(Constants.COMMA_CHAR);

			// sub_item_number
			sqlValueBuffer.append(Constants.ZERO_CHAR);
			sqlValueBuffer.append(Constants.COMMA_CHAR);

			// price_per_unit
			sqlValueBuffer.append(Constants.ZERO_CHAR);
			sqlValueBuffer.append(Constants.COMMA_CHAR);

			// quantity_ordered
			sqlValueBuffer.append(Constants.ONE_CHAR);
			sqlValueBuffer.append(Constants.COMMA_CHAR);

			// quantity_shipped
			sqlValueBuffer.append(Constants.ONE_CHAR);
			sqlValueBuffer.append(Constants.COMMA_CHAR);

			// quantity_returned
			sqlValueBuffer.append(Constants.ZERO_CHAR);
			sqlValueBuffer.append(Constants.COMMA_CHAR);

			// sku
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append(gifts.get(i));
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append(Constants.COMMA_CHAR);

			// status
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append(newOrderInfo.getApproved() ? 
					Type.getValue(MastType.orderStatus.getId(), Constants.ORDER_STATUS_APPROVED, com.voyageone.common.Constants.LANGUAGE.EN) : 
					Type.getValue(MastType.orderStatus.getId(), Constants.ORDER_STATUS_IN_PROCESSING, com.voyageone.common.Constants.LANGUAGE.EN));
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append(Constants.COMMA_CHAR);
			
			// res_allot_flg
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append(Constants.ZERO_CHAR);
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append(Constants.COMMA_CHAR);
			
			// synship_flg
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append(Constants.ZERO_CHAR);
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

			sqlValueBuffer.append(Constants.COMMA_CHAR);
		}
		return itemNumber;
	}
	
	/**
	 * 获得赠品sql--Transaction用
	 * @param newOrderInfo
	 * @param gifts
	 * @param sqlValueBuffer
	 * @param itemNumber
	 * @param taskName
	 * @return
	 */
	private int getGiftSqlTransaction(NewOrderInfo4Log newOrderInfo ,List<String> gifts, StringBuilder sqlValueBuffer,
						int itemNumber, String taskName) {
		
		for (int i = 0; i < gifts.size(); i++) {
			sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);

			// order_number
			sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
			sqlValueBuffer.append(Constants.COMMA_CHAR);

			// transaction_time
			String payTime = newOrderInfo.getPayTime();
			// 时区转换
			String gmtPayTime = DateTimeUtil.getGMTTime(payTime, OmsConstants.TIME_ZONE_8);
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append(gmtPayTime);
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append(Constants.COMMA_CHAR);
			
			// sku
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append(gifts.get(i));
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append(Constants.COMMA_CHAR);
			
			// description
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append(OmsConstants.TRANSACTION_PRODUCT);
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append(Constants.COMMA_CHAR);
			
			// item_number
			itemNumber++;
			sqlValueBuffer.append(itemNumber);
			sqlValueBuffer.append(Constants.COMMA_CHAR);
			
			// debit
			sqlValueBuffer.append(Constants.ZERO_CHAR);
			sqlValueBuffer.append(Constants.COMMA_CHAR);
			
			// credit
			sqlValueBuffer.append(Constants.ZERO_CHAR);
			sqlValueBuffer.append(Constants.COMMA_CHAR);
			
			// source_order_id
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append(newOrderInfo.getTid());
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append(Constants.COMMA_CHAR);
			
			// origin_source_order_id
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append(newOrderInfo.getTid());
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append(Constants.COMMA_CHAR);
			
			// type
			sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
			sqlValueBuffer.append(OmsConstants.TRANSACTIONS_TYPE_ORDER);
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

			sqlValueBuffer.append(Constants.COMMA_CHAR);
		}
		return itemNumber;
	}
	
	/**
	 * 获得赠品sql--oms_bt_ext_order_details扩展表用
	 * @param newOrderInfo
	 * @param gifts
	 * @param sqlValueBuffer
	 * @param itemNumber
	 * @param taskName
	 * @return
	 */
	private int getGiftSqlExt(NewOrderInfo4Log newOrderInfo ,List<String> gifts, StringBuilder sqlValueBuffer,
						int itemNumber, String taskName) {
		
		for (int i = 0; i < gifts.size(); i++) {
			sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
			
			// order_number
			sqlValueBuffer.append(newOrderInfo.getPreOrderNumber());
			sqlValueBuffer.append(Constants.COMMA_CHAR);
			
			// item_number
			itemNumber++;
			sqlValueBuffer.append(itemNumber);
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

			sqlValueBuffer.append(Constants.COMMA_CHAR);
		}
		return itemNumber;
	}
	
	/**
	 * 获取独立域名已发货订单信息
	 * 
	 * @return
	 */
	public List<SelfOrderInfo4Post> getSelfShippedOrderInfo(String orderChannelId, String cartId) {
		List<SelfOrderInfo4Post> shippedOrderInfo = new ArrayList<SelfOrderInfo4Post>();
		List<Map> listMap = orderDao.getSelfShippedOrderInfo(orderChannelId, cartId);
		
		if (listMap != null && listMap.size() > 0) {
			String sourceOrderIdSame = Constants.EMPTY_STR;
			List<SelfOrderDetailInfo4Post> detailList = null;
			SelfOrderInfo4Post order = null;
			StringBuilder sbTrackingNumber = null;
			
			for (int i = 0; i < listMap.size(); i++) {
				SelfOrderDetailInfo4Post detailBean = new SelfOrderDetailInfo4Post();
				
				String orderNumber = String.valueOf(listMap.get(i).get("order_number"));
				String itemNumber = String.valueOf(listMap.get(i).get("item_number"));
				String sku = String.valueOf(listMap.get(i).get("sku"));
				
				detailBean.setOrderNumber(orderNumber);
				detailBean.setItemNumber(itemNumber);
				detailBean.setSku(sku);
				detailBean.setStatus(Constants.ORDER_STATUS_IN_PROCESSING);
				detailBean.setQuantityOrdered(Constants.ONE_CHAR);
				detailBean.setQuantityShipped(Constants.ONE_CHAR);
				detailBean.setQuantityReturned(Constants.ZERO_CHAR);
				detailBean.setQuantityNeeded(Constants.ZERO_CHAR);
				
				String sourceOrderId =  String.valueOf(listMap.get(i).get("source_order_id"));
				String carrier = String.valueOf(listMap.get(i).get("tracking_type"));
				String trackingNumber = String.valueOf(listMap.get(i).get("tracking_no"));
				
				if (!sourceOrderIdSame.equals(sourceOrderId)) {
					sourceOrderIdSame = sourceOrderId;
					
					if (i == 0) {
						detailList = new ArrayList<SelfOrderDetailInfo4Post>();
						order = new SelfOrderInfo4Post();
						sbTrackingNumber = new StringBuilder();
						
						sbTrackingNumber.append(trackingNumber);
						sbTrackingNumber.append(Constants.COMMA_CHAR);
						
						order.setSourceOrderId(sourceOrderId);
						order.setCarrier(carrier);
						order.setOrderNumber(orderNumber);
						order.setStatus(Constants.ORDER_STATUS_SHIPPED);
						
					} else {
						order.setItems(detailList);
						String trackingNo = sbTrackingNumber.toString();
						if (trackingNo.length() > 1) {
							trackingNo = trackingNo.substring(0, trackingNo.length() - 1);
						} else {
							trackingNo = Constants.EMPTY_STR;
						}
						order.setTrackingNumber(trackingNo);
						shippedOrderInfo.add(order);
						
						detailList = new ArrayList<SelfOrderDetailInfo4Post>();
						order = new SelfOrderInfo4Post();
						sbTrackingNumber = new StringBuilder();
						
						sbTrackingNumber.append(trackingNumber);
						sbTrackingNumber.append(Constants.COMMA_CHAR);
						
						order.setSourceOrderId(sourceOrderId);
						order.setCarrier(carrier);
						order.setOrderNumber(orderNumber);
						order.setStatus(Constants.ORDER_STATUS_SHIPPED);
					}
				}
				
				detailList.add(detailBean);
				if (i == (listMap.size() - 1)) {
					order.setItems(detailList);
					String trackingNo = sbTrackingNumber.toString();
					if (trackingNo.length() > 1) {
						trackingNo = trackingNo.substring(0, trackingNo.length() - 1);
					} else {
						trackingNo = Constants.EMPTY_STR;
					}
					order.setTrackingNumber(trackingNumber);
					shippedOrderInfo.add(order);
				}
			}
		}
		return shippedOrderInfo;
	}
	
	/**
	 * 获取独立域名已取消原始订单信息
	 * 
	 * @return
	 */
	public List<SelfOrderInfo4Post> getSelfCanceledOriginalOrderInfo(String orderChannelId, String cartId) {
		List<SelfOrderInfo4Post> canceledOriginalOrderInfo = new ArrayList<SelfOrderInfo4Post>();
		
		List<Map> listMap = orderDao.getSelfCanceledOriginalOrderInfo(orderChannelId, cartId);
		if (listMap != null && listMap.size() > 0) {
			String sourceOrderIdSame = Constants.EMPTY_STR;
			List<SelfOrderDetailInfo4Post> detailList = null;
			SelfOrderInfo4Post order = null;
			
			for (int i = 0; i < listMap.size(); i++) {
				SelfOrderDetailInfo4Post detailBean = new SelfOrderDetailInfo4Post();
				
				String orderNumber = String.valueOf(listMap.get(i).get("order_number"));
				String itemNumber = String.valueOf(listMap.get(i).get("item_number"));
				String sku = String.valueOf(listMap.get(i).get("sku"));
				
				detailBean.setOrderNumber(orderNumber);
				detailBean.setItemNumber(itemNumber);
				detailBean.setSku(sku);
				detailBean.setStatus(Constants.ORDER_STATUS_CANCELED);
				detailBean.setQuantityOrdered(Constants.ONE_CHAR);
				detailBean.setQuantityShipped(Constants.ZERO_CHAR);
				detailBean.setQuantityReturned(Constants.ZERO_CHAR);
				detailBean.setQuantityNeeded(Constants.ZERO_CHAR);
				
				String sourceOrderId = String.valueOf(listMap.get(i).get("source_order_id"));
				String orderDateTime = String.valueOf(listMap.get(i).get("order_date_time"));
				
				if (!sourceOrderIdSame.equals(sourceOrderId)) {
					sourceOrderIdSame = sourceOrderId;
					
					if (i == 0) {
						detailList = new ArrayList<SelfOrderDetailInfo4Post>();
						order = new SelfOrderInfo4Post();
						
						order.setSourceOrderId(sourceOrderId);
						order.setOrderNumber(orderNumber);
						order.setStatus(Constants.ORDER_STATUS_CANCELED);
						order.setOrderDateTime(orderDateTime);
						
					} else {
						order.setItems(detailList);
						canceledOriginalOrderInfo.add(order);
						
						detailList = new ArrayList<SelfOrderDetailInfo4Post>();
						order = new SelfOrderInfo4Post();
						
						order.setSourceOrderId(sourceOrderId);
						order.setOrderNumber(orderNumber);
						order.setStatus(Constants.ORDER_STATUS_CANCELED);
						order.setOrderDateTime(orderDateTime);
					}
				}
				
				detailList.add(detailBean);
				if (i == (listMap.size() - 1)) {
					order.setItems(detailList);
					canceledOriginalOrderInfo.add(order);
				}
			}
		}
		return canceledOriginalOrderInfo;
	}
	
	/**
	 * 获取独立域名已取消原始订单对应 子订单没有取消信息
	 * 
	 * @return
	 */
	public List<SelfOrderInfo4Post> getSelfCanceledChildOrderInfo(String orderChannelId, String cartId) {
		List<SelfOrderInfo4Post> canceledChildOrderInfo = new ArrayList<SelfOrderInfo4Post>();
		
		List<Map> listMap = orderDao.getSelfCanceledChildOrderInfo(orderChannelId, cartId);
		if (listMap != null && listMap.size() > 0) {
			for (int i = 0; i < listMap.size(); i++) {
				SelfOrderInfo4Post order = new SelfOrderInfo4Post();
				String orderNumber = String.valueOf(listMap.get(i).get("order_number"));
				String sourceOrderId = String.valueOf(listMap.get(i).get("source_order_id"));
				order.setOrderNumber(orderNumber);
				order.setSourceOrderId(sourceOrderId);
				
				canceledChildOrderInfo.add(order);
			}
		}
		return canceledChildOrderInfo;
	}
	
	/**
	 * 判断及保留是否的确是独立域名已取消订单信息
	 * 
	 * @return
	 */
	public void getReallySelfCanceledOrderInfoList(List<SelfOrderInfo4Post> selfCanceledOriginalOrderInfoList,
			List<SelfOrderInfo4Post> selfCanceledChildOrderInfoList) {
		if (selfCanceledOriginalOrderInfoList == null || selfCanceledOriginalOrderInfoList.size() <= 0) {
			return;
		}
		
		// 系统当前时间
		String now = DateTimeUtil.getNow();
		logger.info("系统当前时间:" + now);
		for (int i = 0; i < selfCanceledOriginalOrderInfoList.size(); i++) {
			SelfOrderInfo4Post orderInfo = selfCanceledOriginalOrderInfoList.get(i);
			// 该订单时间
			String orderDateTimeDb = orderInfo.getOrderDateTime();
			logger.info("该订单时间:" + orderDateTimeDb);
			
			try {
				// 获得系统当前时间和订单时间间隔的小时数
				long interval = DateTimeUtil.getInterVal(orderDateTimeDb, now, 1);
				
				// 没有超过设定时间间隔，还不能认为的确是取消了
				if (interval < OmsConstants.CANCEL_TIME_INTERVAL) {
					selfCanceledOriginalOrderInfoList.remove(i);
					i--;
				} else {
					logger.info("系统当前时间和订单" + orderInfo.getSourceOrderId() + "时间间隔的小时数:" + interval);
				}
			} catch (Exception ex) {
				issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
			}
		}
		logger.info("超过时间间隔的已取消订单数:" + selfCanceledOriginalOrderInfoList.size());
		
		// 取消的原始订单有并且对应的子订单没有取消的也有，也不能认为是取消了
		if (selfCanceledOriginalOrderInfoList.size() > 0 && selfCanceledChildOrderInfoList.size() > 0) {
			for (SelfOrderInfo4Post child : selfCanceledChildOrderInfoList) {
				String sourceOrderId = child.getSourceOrderId();
				
				for (int i = 0; i < selfCanceledOriginalOrderInfoList.size(); i++) {
					SelfOrderInfo4Post original = selfCanceledOriginalOrderInfoList.get(i);
					
					String sourceOrderIdOriginal = original.getSourceOrderId();
					if (sourceOrderIdOriginal.equals(sourceOrderId)) {
						logger.info("订单" + sourceOrderIdOriginal + "存在没有取消的子订单");
						
						selfCanceledOriginalOrderInfoList.remove(i);
						
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String postOrder(String data, String postUrl) throws Exception {
		String response = CommonUtil.HttpPost(data, "UTF-8", postUrl);
		return response;
	}
	
	/**
	 * 置位订单状态变化历史临时表发送标志
	 * 
	 * @param bean
	 * @param taskName
	 * @return
	 */
	public boolean resetGroupSelfSendFlag(SelfOrderInfo4Post bean, String taskName) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("source_order_id", bean.getSourceOrderId());
		dataMap.put("taskName", taskName);
		
		return orderDao.resetGroupSelfSendFlag(dataMap);
	}
	
	/**
	 * 开启事物插入Notes表、锁定订单表、置位oms_changed_orders_import_history表发送标志
	 * 
	 * @param changedOrderInfo
	 * @param orderInfoMapList
	 * @param taskName
	 * @param sourceOrderStatus
	 * @return
	 */
	public boolean lockFromJdUpdate(ChangedOrderInfo4Log changedOrderInfo, 
			List<Map> orderInfoMapList, String taskName, String sourceOrderStatus) {
		boolean isSuccess = false;
		
		List<String> orderNumberList = new ArrayList<String>();
		List<String> orderKindList = new ArrayList<String>();
		List<String> sourceOrderIdList = new ArrayList<String>();
		for (Map dataMap : orderInfoMapList) {
			orderNumberList.add(String.valueOf(dataMap.get("orderNumber")));
			orderKindList.add(String.valueOf(dataMap.get("orderKind")));
			sourceOrderIdList.add(String.valueOf(dataMap.get("sourceOrderId")));
		}
		
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			int size = orderNumberList.size();
			if (size > 0) {
				// 差价订单类型
				String priceDifferenceOrderType = Type.getValue(MastType.orderType.getId(), Constants.ORDER_TYPE_PRICE_DIFFERENCE, com.voyageone.common.Constants.LANGUAGE.EN);
				boolean isPriceDifferenceHandle = false;
				// 已经绑定的差价订单
				if (size == 1 && !sourceOrderIdList.get(0).equals(changedOrderInfo.getTid()) && priceDifferenceOrderType.equals(orderKindList.get(0))) {
					isPriceDifferenceHandle = true;
				}
				
				// 插入notes表
				String originalOrderType = Type.getValue(MastType.orderType.getId(), Constants.ORDER_TYPE_ORIGINAL, com.voyageone.common.Constants.LANGUAGE.EN);
				int indexOriginal = -1;
				
				// 已经绑定的差价订单
				if (isPriceDifferenceHandle) {
					indexOriginal = 0;
				} else {
					for (int i = 0; i < orderKindList.size(); i++) {
						String orderKind = orderKindList.get(i);
						if (originalOrderType.equals(orderKind)) {
							indexOriginal = i;
							break;
						}
					}
				}
				
				if (indexOriginal > -1) {
					String orderNumberOriginal = orderNumberList.get(indexOriginal);
					
					// 原始订单号
					String originSourceOrderId = changedOrderInfo.getTid();
					// 组订单号
					String sourceOrderId = sourceOrderIdList.get(indexOriginal);
					
					isSuccess = insertChangedNotesData(originSourceOrderId, orderNumberOriginal, sourceOrderStatus, taskName);
				
					if (isSuccess) {
						logger.info("插入notes表 insertChangedNotesData is success");
						
						String orderChannelId = changedOrderInfo.getOrderChannelId();
						String cartId = changedOrderInfo.getCartId();
						
						logger.info("京东锁定订单状态开始。originSourceOrderId:" + originSourceOrderId + " sourceOrderId:" + sourceOrderId + " orderStatus:" + sourceOrderStatus);
						
								
						// 更新一组订单表锁定
						isSuccess = lockOrders(sourceOrderId, orderChannelId, cartId, taskName);
						
						// 订单锁定成功
						if (isSuccess) {
							logger.info("订单表锁定 lockOrders is success");
							
							// 置位订单状态变化历史临时表发送标志（->1）
							isSuccess = resetChangedHistoryOrders(changedOrderInfo, taskName);
							
							if (isSuccess) {
								logger.info("置位订单状态变化历史临时表发送标志 resetChangedHistoryOrders is success");
								
							} else {
								logger.info("置位订单状态变化历史临时表发送标志 resetChangedHistoryOrders is failure");
							}
						} else {
							logger.info("订单表锁定 lockOrders is failure");
						}
						
					} else {
						logger.info("插入notes表 insertChangedNotesData is failure");
					}
				} else {
					logger.info("lockFromJdUpdate 没有找到原始订单 tid:" + changedOrderInfo.getTid());
				}
			} else {
				// 订单号不存在
				logger.info("tid:" + changedOrderInfo.getTid() + "在订单表里不存在, 等待下次执行");
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			
			isSuccess = false;
			issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
			
		} finally {
		
			if (isSuccess) {
				transactionManager.commit(status);
				
				logger.info("lockFromJdUpdate is commit");
			} else {
				transactionManager.rollback(status);
				
				logger.info("lockFromJdUpdate is rollback");
				issueLog.log("OrderInfoImportService.lockFromJdUpdate", 
						"lockFromJdUpdate is rollback, sourceOrderId:" + changedOrderInfo.getTid() + 
						" sourceOrderStatus:" + sourceOrderStatus, 
						ErrorType.BatchJob, SubSystem.OMS, "数据可能不整合导致");
			}
		}
		return isSuccess;
	}
	
	/**
	 * 获得某sku在订单中实际售价
	 * 
	 * @param itemPrice
	 * @param sku
	 * @param disCounts
	 * @param disCountDescriptions
	 * 
	 * @return
	 */
	private double getRealItemPrice(double itemPrice, String sku, String disCounts, String disCountDescriptions) {
		double realPrice = itemPrice;
		
		if (!StringUtils.isNullOrBlank2(disCounts)) {
			String[] disCountArray = disCounts.split(Constants.SPLIT_CHAR_RESOLVE);
			String[] disCountDescriptionArray = disCountDescriptions.split(Constants.SPLIT_CHAR_RESOLVE);
			
			if (disCountArray != null && disCountArray.length > 0) {
				for (int i = 0; i < disCountArray.length; i++) {
					// 某个折扣
					String disCountStr = disCountArray[i];
					double disCount = Double.valueOf(disCountStr);
					if (disCount == 0) {
						continue;
					}
					
					// 该折扣对应产品描述
					String product = disCountDescriptionArray[i];
					String[] skuArray = product.split(Constants.DOLLAR_CHAR_RESOLVE);
					String skuIn = Constants.EMPTY_STR;
					if (skuArray != null && skuArray.length > 1) {
						skuIn = skuArray[1];
					}
					
					if (!StringUtils.isEmpty(skuIn)) {
						// 找到折扣对应sku
						if (skuIn.equalsIgnoreCase(sku)) {
							realPrice = realPrice - disCount;
							
							break;
						}
					}
				}
			}
		}
		
		return realPrice;
	}

	/**
	 * 根据barcode获得sku
	 * @param orderChannelId
	 * @param barcode
	 * @return
	 */
	private String getSkuByBarcode(String orderChannelId, String barcode) {
		String sku = "";

		if (!StringUtils.isNullOrBlank2(barcode)) {
			sku = orderDao.getSkuByBarcode(orderChannelId, barcode);
		}

		return sku;
	}
	
	public static void main(String[] args) {
		String giftSku = "8306sr-OneSize;40001-02-24*26";
		String[] giftSkuArray = giftSku.split(";");
//		String lastOrderTime = "2015-06-18 10:30:45";
//		int preTime = 30;
//		// 如果没取到时间
//		if (StringUtils.isNullOrBlank2(lastOrderTime)) {
//			Date date = DateTimeUtil.addMinutes(DateTimeUtil.getDate(), -preTime);
//			lastOrderTime = DateTimeUtil.getDate(date, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
//			// 格林威治时间转北京时间
//			lastOrderTime = DateTimeUtil.getLocalTime(lastOrderTime, OmsConstants.TIME_ZONE_8);
//		} else {
//			Date date = DateTimeUtil.getDate(lastOrderTime, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
//			date = DateTimeUtil.addMinutes(date, -preTime);
//			lastOrderTime = DateTimeUtil.getDate(date, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
//		}
		
		int giftSize = 1;
		
		for (int i = 0; i < 100; i++) {
			int index = new Random().nextInt(giftSize);
			System.out.println(index);
		}
		
//		List<Double> priceList = new ArrayList<Double>();
//		priceList.add(219d);
//		priceList.add(199.5d);
//		priceList.add(199d);
//		priceList.add(99d);
//		priceList.add(100d);
//		priceList.add(38d);
//		
//		double priceDoubles = 199.50;
//		// 重复价格设定过滤
//				if (!priceList.contains(priceDoubles)) {
//					priceList.add(priceDoubles);
//				}
//		
//		Collections.sort(priceList);
//		Collections.reverse(priceList);
//		
//		String price = String.valueOf("399.296");
//		double priceDouble = Double.parseDouble(price);
//		DecimalFormat df = new DecimalFormat("#.00");
//		price = df.format(priceDouble);
//		
//		OrderInfoImportService thiss = new OrderInfoImportService();
//		String data = "abc\r\nccdd\nkk\rsdf\\u200";
//		String result = thiss.transferStr(data);
//		
//		double dd = 0.00;
//		if (dd == 0) {
//			System.out.println();
//		}
//		String test = Sale.CN.getType();
		System.out.println();
	}
}
