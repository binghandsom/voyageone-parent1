package com.voyageone.batch.oms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.jd.open.api.sdk.domain.order.CouponDetail;
import com.jd.open.api.sdk.domain.order.ItemInfo;
import com.jd.open.api.sdk.domain.order.OrderSearchInfo;
import com.jd.open.api.sdk.domain.order.UserInfo;
import com.jd.open.api.sdk.domain.order.VatInvoiceInfo;
import com.voyageone.batch.core.Constants;
import com.voyageone.batch.oms.OmsConstants;
import com.voyageone.batch.oms.bean.JasonCommentBean;
import com.voyageone.batch.oms.bean.JsonVerificationBean;
import com.voyageone.batch.oms.dao.OrderDao;
import com.voyageone.batch.oms.modelbean.TaobaoOrderBean;
import com.voyageone.batch.oms.modelbean.TaobaoPromotionBean;
import com.voyageone.batch.oms.modelbean.TaobaoTradeBean;
import com.voyageone.batch.oms.modelbean.TradeInfoBean;
import com.voyageone.batch.oms.utils.Verification;
import com.voyageone.batch.oms.utils.WebServiceUtil;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.jd.JdOrderService;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;

@Service
public class JingdongOrderInfoImportService {
	
	private static Log logger = LogFactory.getLog(JingdongOrderInfoImportService.class);
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	OrderInfoImportService orderInfoImportService;
	
	@Autowired
	IssueLog issueLog;
	
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	private DefaultTransactionDefinition def = new DefaultTransactionDefinition();
	
	/**
	 * 获得京东某个时间点开始->当前时间结束的新订单数据
	 * 
	 * @param startOrderTime
	 * @param orderChannelId
	 * @param cartId
	 * @param targetId
	 * @param logTitle
	 * @return
	 */
	public List<TaobaoTradeBean> getJdNewOrder(String startOrderTime, String orderChannelId, String cartId, String targetId, String logTitle) {
		// 获得当前时间
		String endOrderTime = DateTimeUtil.getNow();
		// 格林威治时间转北京时间
		endOrderTime = DateTimeUtil.getLocalTime(endOrderTime, OmsConstants.TIME_ZONE_8);
		
		// 获得调用sneakerhead京东API所需信息
		ShopBean shopBean = ShopConfigs.getShop(orderChannelId, cartId);
		
		List<OrderSearchInfo> orderList = null;
		List<TaobaoTradeBean> transedOrderList = null;
		try {
			JdOrderService jdOrderService = new JdOrderService();
			orderList = jdOrderService.getNewOrderPage(startOrderTime, endOrderTime, shopBean);
			
			if (orderList != null && orderList.size() > 0) {
				transedOrderList = transJdData2TaobaoTradeBean(orderList, orderChannelId, cartId, targetId);
			} else {
				logger.info("本次" + logTitle + "调用没有取到新订单");
			}
			
		} catch (Exception ex) {
			logger.info(logTitle + "取得新订单信息失败");
			
			issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
		}
		
		return transedOrderList;
	}
	
	/**
	 * 获得京东某个时间点开始->当前时间结束的订单状态变化数据
	 * 
	 * @param startOrderTime
	 * @param orderChannelId
	 * @param cartId
	 * @param targetId
	 * @param logTitle
	 * @return
	 */
	public List<TradeInfoBean> getJdChangedOrder(String startOrderTime, String orderChannelId, String cartId, String targetId, String logTitle) {
		// 获得当前时间
		String endOrderTime = DateTimeUtil.getNow();
		// 格林威治时间转北京时间
		endOrderTime = DateTimeUtil.getLocalTime(endOrderTime, OmsConstants.TIME_ZONE_8);
		
		// 获得调用sneakerhead京东API所需信息
		ShopBean shopBean = ShopConfigs.getShop(orderChannelId, cartId);
		
		List<OrderSearchInfo> orderList = null;
		List<TradeInfoBean> tradeInfoOrderList = null;
		try {
			JdOrderService jdOrderService = new JdOrderService();
			orderList = jdOrderService.getChangedOrderPage(startOrderTime, endOrderTime, shopBean);
			
			if (orderList != null && orderList.size() > 0) {
				tradeInfoOrderList = transJdData2TradeInfoBean(orderList, orderChannelId, cartId, targetId);
			} else {
				logger.info("本次" + logTitle + "调用没有取到状态变化订单");
			}
			
		} catch (Exception ex) {
			logger.info(logTitle + "取得状态变化订单信息失败");
			
			issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
		}
		
		return tradeInfoOrderList;
	}
	
	/**
	 * 京东新订单数据bean转换成histroy表用的数据bean
	 * 
	 * @param orderList
	 * @param orderChannelId
	 * @param cartId
	 * @param targetId
	 * @return
	 */
	private List<TaobaoTradeBean> transJdData2TaobaoTradeBean(List<OrderSearchInfo> orderList, 
			String orderChannelId, String cartId, String targetId) {
		if (orderList != null && orderList.size() > 0) {
			List<TaobaoTradeBean> transOrderList = new ArrayList<TaobaoTradeBean>();
			for (OrderSearchInfo orderSearchInfo : orderList) {
				TaobaoTradeBean transBean = new TaobaoTradeBean();
				
				// 订单店铺id
				transBean.setChannelId(orderChannelId);
				// 订单渠道id
				transBean.setCartId(cartId);
				// 源订单号
				transBean.setTid(StringUtils.null2Space2(orderSearchInfo.getOrderId()));
				
				// 订单更新时间 
				transBean.setModified(StringUtils.null2Space2(orderSearchInfo.getModified()));
				// 订单支付时间
				transBean.setPay_time(StringUtils.null2Space2(orderSearchInfo.getPaymentConfirmTime()));
				// 买家的账号昵称
				transBean.setBuyer_nick(StringUtils.null2Space2(orderSearchInfo.getPin()));
				
				// 收货人基本信息
				UserInfo userInfo = orderSearchInfo.getConsigneeInfo();
				if (userInfo != null) {
					// 收货人县
					transBean.setReceiver_district(StringUtils.null2Space2(userInfo.getCounty()));
					// 收货人地址
					transBean.setReceiver_address(StringUtils.null2Space2(userInfo.getFullAddress()));
					// 收货人城市
					transBean.setReceiver_city(StringUtils.null2Space2(userInfo.getCity()));
					// 收货人省
					transBean.setReceiver_state(StringUtils.null2Space2(userInfo.getProvince()));
					// 收货人姓名
					transBean.setReceiver_name(StringUtils.null2Space2(userInfo.getFullname()));
					// 收货人手机
					transBean.setReceiver_mobile(StringUtils.null2Space2(userInfo.getMobile()));
					// 收货人固定电话 
					transBean.setReceiver_phone(StringUtils.null2Space2(userInfo.getTelephone()));
				}
				
				// 订单内容信息
				List<ItemInfo> itemInfoList = orderSearchInfo.getItemInfoList();
				if (itemInfoList != null && itemInfoList.size() > 0) {
					List<TaobaoOrderBean> detailBeanList = new ArrayList<TaobaoOrderBean>();
					for (ItemInfo item : itemInfoList) {
						if (item != null) {
							TaobaoOrderBean detailBean = new TaobaoOrderBean();
							
							// SKU外部ID
							String outerSkuId = StringUtils.null2Space2(item.getOuterSkuId());
							detailBean.setOuter_sku_id(outerSkuId);
							detailBean.setOuter_iid(outerSkuId);
							
							// 商品的名称+SKU规格
							String title = StringUtils.null2Space2(item.getSkuName());
							detailBean.setTitle(title);
							
							// SKU的京东价
							String price = StringUtils.null2Space2(item.getJdPrice());
							detailBean.setPrice(price);
							
							// 数量
							String num = StringUtils.null2Space2(item.getItemTotal());
							detailBean.setNum(num);
							
							// 京东内部商品ID
							String numIid = StringUtils.null2Space2(item.getWareId());
							detailBean.setNum_iid(numIid);
							
							detailBeanList.add(detailBean);
						}
					}
					transBean.setOrder(detailBeanList);
				}
				
				String paymentMethod = "Jdpay";
				if (OmsConstants.TARGET_JD.equals(targetId)) {
					paymentMethod = "Jdpay";
				} else if (OmsConstants.TARGET_JDG.equals(targetId)) {
					paymentMethod = "JdpayInterNational";
				}
				transBean.setPaymentMethod(paymentMethod);
				
				transBean.setAlipay_no(Constants.EMPTY_STR);
				transBean.setBuyer_alipay_no(Constants.EMPTY_STR);
				
				// 订单总金额
				String orderTotalPrice = orderSearchInfo.getOrderTotalPrice();
				double dblOrderTotalPrice = 0.00;
				if (!StringUtils.isEmpty(orderTotalPrice)) {
					transBean.setTotal_fee(orderTotalPrice);
					dblOrderTotalPrice = Double.parseDouble(orderTotalPrice);
				}
				
				// 运费
				String freightPrice = orderSearchInfo.getFreightPrice();
				double dblFreightPrice = 0.00;
				if (!StringUtils.isEmpty(freightPrice)) {
					transBean.setPost_fee(freightPrice);
					dblFreightPrice = Double.parseDouble(freightPrice);
				}
				
				// 快递方式
				String logisticsId = orderSearchInfo.getLogisticsId();
				if (!StringUtils.isEmpty(logisticsId)) {
					transBean.setShipping_type(logisticsId);
				}
				
				// 折扣
				List<TaobaoPromotionBean> promotionlist = new ArrayList<TaobaoPromotionBean>();
				double coupon = 0;
				// 优惠详细信息
				List<CouponDetail> couponDetailList = orderSearchInfo.getCouponDetailList();
				if (couponDetailList != null && couponDetailList.size() > 0) {
					for (CouponDetail couponDetail : couponDetailList) {
						// 优惠金额
						String couponPrice = couponDetail.getCouponPrice();
						
						// 折扣信息(优惠金额)非空的场合（京东折扣信息是有空字符串的场合）
						if (!StringUtils.isEmpty(couponPrice)) {
							// 判断类型：只有本店铺出资提供的优惠才会被计入
							String strType = couponDetail.getCouponType();
							if ("30-单品促销优惠".equals(strType) || 
								"35-满返满送(返现)".equals(strType) || 
								"100-店铺优惠".equals(strType)) {
								
								TaobaoPromotionBean bean = new TaobaoPromotionBean();
	
								bean.setName(Constants.DISCOUNT_STR);
								bean.setDescription(Constants.DISCOUNT_STR);
								bean.setDiscount(BigDecimal.valueOf(Double.valueOf(couponPrice)));
								promotionlist.add(bean);
								
								coupon = coupon + Double.valueOf(couponPrice);
							}
						}
					}
				}
				transBean.setPromotion(promotionlist);
				
				// 应该收到的金额=订单总金额+运费-店铺折扣
				String payMent = String.valueOf(dblOrderTotalPrice + dblFreightPrice - coupon);
				transBean.setPayment(payMent);
				
				// 发票信息
				StringBuilder invoiceinfoSb  = new StringBuilder();
				String invoiceinfo = orderSearchInfo.getInvoiceInfo();
				if (invoiceinfo != null) {
					invoiceinfoSb.append("是否需要发票:");
					invoiceinfoSb.append(invoiceinfo);
				}
				VatInvoiceInfo vatInvoiceInfo = orderSearchInfo.getVatInvoiceInfo();
				if (vatInvoiceInfo != null) {
					invoiceinfoSb.append("\r\n");
					invoiceinfoSb.append("纳税人识别号:");
					String taxpayerIdent = vatInvoiceInfo.getTaxpayerIdent();
					if (taxpayerIdent != null) {
						invoiceinfoSb.append(taxpayerIdent);
					}
					
					invoiceinfoSb.append("\r\n");
					invoiceinfoSb.append("注册地址:");
					String registeredAddress = vatInvoiceInfo.getRegisteredAddress();
					if (registeredAddress != null) {
						invoiceinfoSb.append(registeredAddress);
					}
					
					invoiceinfoSb.append("\r\n");
					invoiceinfoSb.append("注册电话:");
					String registeredPhone = vatInvoiceInfo.getRegisteredPhone();
					if (registeredPhone != null) {
						invoiceinfoSb.append(registeredPhone);
					}
					
					invoiceinfoSb.append("\r\n");
					invoiceinfoSb.append("开户银行:");
					String depositBank = vatInvoiceInfo.getDepositBank();
					if (depositBank != null) {
						invoiceinfoSb.append(depositBank);
					}
					
					invoiceinfoSb.append("\r\n");
					invoiceinfoSb.append("银行账户:");
					String bankAccount = vatInvoiceInfo.getBankAccount();
					if (bankAccount != null) {
						invoiceinfoSb.append(bankAccount);
					}
				}
				transBean.setInvoice_info(invoiceinfoSb.toString());
				
				// 买家下单时订单备注
				String orderRemark = orderSearchInfo.getOrderRemark();
				transBean.setBuyer_message(StringUtils.null2Space2(orderRemark));
				
				transOrderList.add(transBean);
			}
			return transOrderList;
		} else {
			return new ArrayList<TaobaoTradeBean>();
		}
	}
	
	/**
	 * 京东状态变化数据bean转换成histroy表用的数据bean
	 * 
	 * @param orderList
	 * @param orderChannelId
	 * @param cartId
	 * @param targetId
	 * @return
	 */
	private List<TradeInfoBean> transJdData2TradeInfoBean(List<OrderSearchInfo> orderList, 
			String orderChannelId, String cartId, String targetId) {
		if (orderList != null && orderList.size() > 0) {
			List<TradeInfoBean> tradeOrderList = new ArrayList<TradeInfoBean>();
			for (OrderSearchInfo orderSearchInfo : orderList) {
				TradeInfoBean tradeBean = new TradeInfoBean();
				
				// 订单店铺id
				tradeBean.setChannelId(orderChannelId);
				
				// 订单渠道id
				tradeBean.setCartId(cartId);
				
				// 源订单号
				tradeBean.setTid(StringUtils.null2Space2(orderSearchInfo.getOrderId()));
				
				// target
				tradeBean.setTarget(targetId);
				
				// 订单状态
				String orderStatus = StringUtils.null2Space2(orderSearchInfo.getOrderState());
				if ("LOCKED".equals(orderStatus)) {
					// 顾客售前退款申请
					orderStatus = Constants.JD_STATUS_LOCKED;
				} else if ("FINISHED_L".equals(orderStatus)) {
					// 交易成功
					orderStatus = Constants.ALIBABA_STATUS_TRADE_SUCCESS;
				}
				tradeBean.setStatus(orderStatus);
				
				// 买家的账号昵称
				tradeBean.setBuyer_nick(StringUtils.null2Space2(orderSearchInfo.getPin()));
				
				// 订单总金额
				String orderTotalPrice = orderSearchInfo.getOrderTotalPrice();
				double dblOrderTotalPrice = 0.00;
				if (!StringUtils.isEmpty(orderTotalPrice)) {
					dblOrderTotalPrice = Double.parseDouble(orderTotalPrice);
				}
				// 运费
				String freightPrice = orderSearchInfo.getFreightPrice();
				double dblFreightPrice = 0.00;
				if (!StringUtils.isEmpty(freightPrice)) {
					dblFreightPrice = Double.parseDouble(freightPrice);
				}
				// 折扣
				double coupon = 0;
				// 优惠详细信息
				List<CouponDetail> couponDetailList = orderSearchInfo.getCouponDetailList();
				if (couponDetailList != null && couponDetailList.size() > 0) {
					for (CouponDetail couponDetail : couponDetailList) {
						// 优惠金额
						String couponPrice = couponDetail.getCouponPrice();
						
						// 折扣信息(优惠金额)非空的场合（京东折扣信息是有空字符串的场合）
						if (!StringUtils.isEmpty(couponPrice)) {
							// 判断类型：只有本店铺出资提供的优惠才会被计入
							String strType = couponDetail.getCouponType();
							if ("30-单品促销优惠".equals(strType) || 
								"35-满返满送(返现)".equals(strType) || 
								"100-店铺优惠".equals(strType)) {
								
								coupon = coupon + Double.valueOf(couponPrice);
							}
						}
					}
				}
				// 应该收到的金额=订单总金额+运费-店铺折扣
				String payMent = String.valueOf(dblOrderTotalPrice + dblFreightPrice - coupon);
				tradeBean.setFee(payMent);
				
				// 订单更新时间 
				tradeBean.setModified(StringUtils.null2Space2(orderSearchInfo.getModified()));
				
				// 京东没有天猫以下属性，设固定值
				tradeBean.setOid(Constants.ZERO_CHAR);
				tradeBean.setRid(Constants.ZERO_CHAR);
				tradeBean.setRefundPhase(Constants.EMPTY_STR);
				
				tradeOrderList.add(tradeBean);
			}
			
			return tradeOrderList;
		} else {
			return new ArrayList<TradeInfoBean>();
		}
	}
	
	/**
	 * 京东系新订单插入临时表
	 * 
	 * @param orderInfoTotalList
	 * @param taskName
	 * @return
	 */
	public boolean importNewOrderToHistory(List<TaobaoTradeBean> orderInfoTotalList, String taskName){
		
		String sqlBatch = orderInfoImportService.getBatchNewOrderHistorySqlData(orderInfoTotalList, taskName);
		return orderDao.insertNewOrdersInfo(sqlBatch, orderInfoTotalList.size());
		
	}
	
	/**
	 * 京东系状态变化订单插入临时表
	 * 
	 * @param orderInfoTotalList
	 * @param taskName
	 * @return
	 */
	public boolean importChangedOrderToHistory(List<TradeInfoBean> orderInfoTotalList, String taskName){
		
		String sqlBatch = orderInfoImportService.getBatchChangedOrderHistorySqlData(orderInfoTotalList, taskName);
		return orderDao.insertChangedOrdersInfo(sqlBatch, orderInfoTotalList.size());
		
	}
	
	/**
	 * 获得京东和京东国际没有推送过短链接的订单记录
	 * 
	 * @param orderChannelId
	 * @param cartIdList
	 * @return
	 */
	public List<TaobaoTradeBean> getNeedShortUrlJdNewOrder(String orderChannelId, List<String> cartIdList) {
		List<TaobaoTradeBean> tradeInfoOrderList = null;
		try {
			tradeInfoOrderList = orderDao.getNeedShortUrlJdNewOrder(orderChannelId, cartIdList);
			
		} catch (Exception ex) {
			issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
		}
		
		return tradeInfoOrderList;
	}
	
	/**
	 * 拼装短链接需要数据并且推送synship短链接wsdl
	 * 
	 * @param trade
	 * @param errorMessage
	 * @param taskName
	 * @return
	 */
	public boolean pushShortUrl(TaobaoTradeBean trade, List<String> errorMessage, String taskName) throws Exception {
		boolean isPushSuccess = false;
		
		// json数据拼装
		JasonCommentBean rec = new JasonCommentBean();
		
		rec.setTid(StringUtils.null2Space2(trade.getTid()));
		rec.setReceiver_name(StringUtils.null2Space2(trade.getReceiver_name()));
		rec.setReceiver_mobile(StringUtils.null2Space2(trade.getReceiver_mobile()));
		rec.setPay_time(StringUtils.null2Space2(trade.getPay_time()));
		
		String orderChannelId = trade.getOrder_channel_id();
		rec.setOrder_channel_id(StringUtils.null2Space2(orderChannelId));
		
		ShopBean shopBean = ShopConfigs.getShop(orderChannelId, trade.getCartId());
		rec.setShop_name(StringUtils.null2Space2(shopBean.getShop_name()));
		
		rec.setPayment(StringUtils.null2Space2(trade.getPayment()));
		// 产品数量
		int product_num = 0;
		String productSkus = trade.getProduct_sku();
		String productQuantity = trade.getProduct_quantity();
		if (!StringUtils.isNullOrBlank2(productSkus) && !StringUtils.isNullOrBlank2(productQuantity)) {
			String[] skuArray = productSkus.split(Constants.SPLIT_CHAR_RESOLVE);
			if (skuArray != null && skuArray.length > 0) {
				// 数量
				String[] quantityArray = productQuantity.split(Constants.SPLIT_CHAR_RESOLVE);
				for (int i = 0; i < skuArray.length; i++) {
					int quantity = Integer.valueOf(quantityArray[i]);
					product_num += quantity;
				}
			}
		}
		rec.setProduct_num(String.valueOf(product_num));
		rec.setBuyer_nick(StringUtils.null2Space2(trade.getBuyer_nick()));
		// cartId
		rec.setCartId(trade.getCartId());
		String orderComment = JsonUtil.getJsonString(rec);
		
		// 调用wsdl验证数据拼装
		JsonVerificationBean jsonVerificationBean = new JsonVerificationBean();
		jsonVerificationBean.setJsonData(orderComment);
		
		// 验证用KEY
		String dateTime = DateTimeUtil.getNow();
		jsonVerificationBean.setTimeStamp(dateTime);
		jsonVerificationBean.setSignature(Verification.getSynShipVerificationString(dateTime));
		
		// 调用WSDL
		logger.info("推送到SynShip的短链接表");
		String shortUrl = Properties.readValue("SHIP_WEBSERVICE_URL_SHORT_URL");
		String response = WebServiceUtil.postByJsonStr(shortUrl, JsonUtil.getJsonString(jsonVerificationBean));
		
		// 推送成功的场合，更新send_flg
		JSONObject paraJson = new JSONObject(response);
		if (OmsConstants.RESULT_OK.equals(String.valueOf(paraJson.get("result")))) {
			logger.info("订单数据推送到synship的短链接表成功：" + trade.getTid());
			
			boolean isResetSuccess = resetShortUrlSendFlag(trade, taskName);
			// 置位发送标志成功
			if (isResetSuccess) {
				logger.info("updateShortUrlSendFlag Success.");
				isPushSuccess = true;
			} else {
				logger.error("updateShortUrlSendFlag Failed.");
				errorMessage.add("oms_new_orders_import_history：更新short_url_send_flag失败");
			}
		} else {
			logger.info("订单数据推送到synship的短链接表失败：" + trade.getTid());
			
			if (!StringUtils.isNullOrBlank2(String.valueOf(paraJson.get("errorInfo")))) {
				JSONObject errorInfo = paraJson.getJSONObject("errorInfo");
				if (errorInfo != null) {
					if (!StringUtils.isNullOrBlank2(String.valueOf(errorInfo.get("errorMessageCn")))) {
						errorMessage.add(errorInfo.get("errorMessageCn").toString());
					} else {
						errorMessage.add(errorInfo.get("errorMessage").toString());
					}
				}
			}
		}
		
		return isPushSuccess;
	}
	
	/**
	 * 置位oms_new_orders_import_history发送短域名标志
	 * 
	 * @param trade
	 * @param taskName
	 * @return
	 */
	private boolean resetShortUrlSendFlag(TaobaoTradeBean trade, String taskName) {
		return orderDao.resetShortUrlSendFlag(trade, taskName);
	}
	
	public static void main(String[] args) {
	}
}
