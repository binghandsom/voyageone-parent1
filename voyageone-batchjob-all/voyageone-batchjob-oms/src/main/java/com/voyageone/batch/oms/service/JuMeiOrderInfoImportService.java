package com.voyageone.batch.oms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.batch.core.Constants;
import com.voyageone.batch.oms.OmsConstants;
import com.voyageone.batch.oms.bean.JasonCommentBean;
import com.voyageone.batch.oms.bean.JsonVerificationBean;
import com.voyageone.batch.oms.dao.OrderDao;
import com.voyageone.batch.oms.modelbean.TaobaoOrderBean;
import com.voyageone.batch.oms.modelbean.TaobaoPromotionBean;
import com.voyageone.batch.oms.modelbean.TaobaoTradeBean;
import com.voyageone.batch.oms.utils.Verification;
import com.voyageone.batch.oms.utils.WebServiceUtil;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.jumei.JumeiService;
import com.voyageone.common.components.jumei.Bean.GetOrderDetailRes;
import com.voyageone.common.components.jumei.Bean.GetOrderIdsReq;
import com.voyageone.common.components.jumei.Bean.GetOrderIdsRes;
import com.voyageone.common.components.jumei.Bean.JmOrderInfo;
import com.voyageone.common.components.jumei.Bean.Product_Infos;
import com.voyageone.common.components.jumei.Bean.Receiver_Infos;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;

@Service
public class JuMeiOrderInfoImportService {
	
	private static Log logger = LogFactory.getLog(JuMeiOrderInfoImportService.class);
	
	private static int repeatTimes = 3;
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	OrderInfoImportService orderInfoImportService;
	
	@Autowired
	IssueLog issueLog;
	
	/**
	 * 获得聚美bhfo某个时间点开始->当前时间结束的新订单数据
	 * 
	 * @param startOrderTime
	 * @param cartId
	 * @param logTitle
	 * @return
	 */
	public List<TaobaoTradeBean> getJumeiNewOrder(String startOrderTime, String cartId, String logTitle) {
		// 获得当前时间
		String endOrderTime = DateTimeUtil.getNow();
		// 格林威治时间转北京时间
		endOrderTime = DateTimeUtil.getLocalTime(endOrderTime, OmsConstants.TIME_ZONE_8);
		
		// 获得调用聚美API所需信息
		List<ShopBean> shopBeanList = ShopConfigs.getCartShopList(cartId);
		ShopBean shopBean = new ShopBean();
		if (shopBeanList != null && shopBeanList.size() > 0) {
			shopBean = shopBeanList.get(0);
		} else {
			issueLog.log("", "聚美Appkey配置取得失败 ", ErrorType.BatchJob, SubSystem.OMS);
			
			return new ArrayList<TaobaoTradeBean>();
		}
		
		List<String> orderNumberList = null;
		List<JmOrderInfo> orderInfoList = new ArrayList<JmOrderInfo>();
		
		JumeiService jumeiService = new JumeiService();
		GetOrderIdsReq reqIds = new GetOrderIdsReq();
		
		Map<String, String> startAndEndTimeMap = getJumeiStartEndTime(startOrderTime, endOrderTime);
		
		// 订单开始时间
		reqIds.setStart_date(startAndEndTimeMap.get("startDateTime"));
		// 订单结束时间
		reqIds.setEnd_date(startAndEndTimeMap.get("endDateTime"));
		// 已付款、备货中（都是新订单）
		reqIds.setStatus("2,7");
		
		int count = 0;
		try {
			// 获取新订单号列表
			GetOrderIdsRes orderIdsRes = jumeiService.getOrderIds(shopBean, reqIds);
			count++;
			
			if (orderIdsRes != null) {
				// 返回结果正常
				while (!Constants.ZERO_CHAR.equals(orderIdsRes.getError())) {
					if (count > repeatTimes) {
						break;
					}
					orderIdsRes = jumeiService.getOrderIds(shopBean, reqIds);
					count++;
				}
				
				if (orderIdsRes != null) {
					if (!Constants.ZERO_CHAR.equals(orderIdsRes.getError())) {
						logger.info("聚美取得订单号列表失败 " + orderIdsRes.getError());
						
						issueLog.log("", "聚美取得订单号列表失败 " + orderIdsRes.getError(), ErrorType.BatchJob, SubSystem.OMS);
						
						return null;
					}
					
					// 订单号列表
					orderNumberList = orderIdsRes.getResult();
					
				} else {
					logger.info("本次" + logTitle + "调用没有取到新订单");
				}
			} else {
				logger.info("本次" + logTitle + "调用没有取到新订单");
			}
			
		} catch (Exception ex) {
			logger.info(logTitle + "取得聚美新订单号列表失败");
			
			issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
		}
		
		// ---------------------------------------------------------------------------------------------------------
		
		if (orderNumberList != null && orderNumberList.size() > 0) {
			// 过滤已经取过的新订单
			getRealNewOrderNumber(orderNumberList, cartId);
			
			// 取得订单详情
			if (orderNumberList != null && orderNumberList.size() > 0) {
				logger.info("聚美新订单数：" + orderNumberList.size());
				
				for (String orderId : orderNumberList) {
					count = 0;
					
					try {
						// 获取新订单详情
						GetOrderDetailRes orderDetailRes = jumeiService.getOrderDetailByOrderId(shopBean, orderId);
						count++;
						
						if (orderDetailRes != null) {
							// 返回结果正常
							while (!Constants.ZERO_CHAR.equals(orderDetailRes.getError())) {
								if (count > repeatTimes) {
									break;
								}
								orderDetailRes = jumeiService.getOrderDetailByOrderId(shopBean, orderId);
								count++;
							}
							
							if (orderDetailRes != null) {
								if (!Constants.ZERO_CHAR.equals(orderDetailRes.getError())) {
									logger.info("聚美取得订单详情失败 " + orderDetailRes.getError() + " orderId:" + orderId);
									
									issueLog.log("", "聚美取得订单详情失败 " + orderDetailRes.getError() + " orderId:" + orderId, ErrorType.BatchJob, SubSystem.OMS);
									
									continue;
								}
								
								// 解析订单详情
								JmOrderInfo jmOrderInfo = orderDetailRes.getResult();
								orderInfoList.add(jmOrderInfo);
								
							} else {
								logger.info("本次" + logTitle + "调用没有取到新订单");
							}
						} else {
							logger.info("本次" + logTitle + "调用没有取到新订单");
						}
						
					} catch (Exception ex) {
						logger.info(logTitle + "取得聚美新订单号列表失败");
						
						issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
					}
				}
				
			} else {
				logger.info("本次" + logTitle + "调用没有取到新订单");
			}
		} else {
			logger.info("本次" + logTitle + "调用没有取到新订单");
		}
		
		return transJmData2TaobaoTradeBean(orderInfoList, cartId);
		
	}
	
	/**
	 * 校准开始时间（聚美开始终了时间不超过24小时）
	 * 
	 * @param startOrderTime
	 * @param endOrderTime
	 * @return
	 */
	private Map<String, String> getJumeiStartEndTime(String startOrderTime, String endOrderTime) {
		Map<String, String> startAndEndTimeMap = new HashMap<String, String>();
		try {
			// 间隔分钟
			long interval = DateTimeUtil.getInterVal(startOrderTime, endOrderTime, 2);
			long jumei24Hour = 24 * 60;
			while (interval >= jumei24Hour) {
				Date startDateTime = DateTimeUtil.addMinutes(DateTimeUtil.parse(startOrderTime, DateTimeUtil.DEFAULT_DATETIME_FORMAT), 10);
				startOrderTime = DateTimeUtil.getDateTime(startDateTime, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
				
				interval = DateTimeUtil.getInterVal(startOrderTime, endOrderTime, 2);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			
			issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
		}
		
		startAndEndTimeMap.put("startDateTime", startOrderTime);
		startAndEndTimeMap.put("endDateTime", endOrderTime);
		
		return startAndEndTimeMap;
	}
	
	/**
	 * 过滤已经取过的新订单
	 * 
	 * @param orderNumberList
	 * @param cartId
	 * @return
	 */
	private void getRealNewOrderNumber(List<String> orderNumberList, String cartId) {
		if (orderNumberList != null && orderNumberList.size() > 0) {
			for (int i = 0; i < orderNumberList.size(); i++) {
				String orderNumber = orderNumberList.get(i);
				boolean isExist = orderDao.isJuMeiOrderExist(orderNumber, cartId);
				if (isExist) {
					orderNumberList.remove(i);
					i--;
				}
			}
		}
	}

	
	/**
	 * 聚美新订单数据bean转换成histroy表用的数据bean
	 * 
	 * @param orderInfoList
	 * @param cartId
	 * @return
	 */
	private List<TaobaoTradeBean> transJmData2TaobaoTradeBean(List<JmOrderInfo> orderInfoList, String cartId) {
		if (orderInfoList != null && orderInfoList.size() > 0) {
			List<TaobaoTradeBean> transOrderList = new ArrayList<TaobaoTradeBean>();
			for (JmOrderInfo jmOrderInfo : orderInfoList) {
				TaobaoTradeBean transBean = new TaobaoTradeBean();
				
				String shippingSystemId = jmOrderInfo.getShipping_system_id();
				String orderChannelId = Codes.getCodeName("JUMEI", shippingSystemId);
				logger.info("shipping_system_id：" + shippingSystemId + " orderChannelId：" + orderChannelId);
				
				if (StringUtils.isNullOrBlank2(orderChannelId)) {
					logger.info("聚美取得订单店铺区分失败，源订单号：" + jmOrderInfo.getOrder_sn() + "， shipping_system_id：" + shippingSystemId);
					
					issueLog.log("", "聚美取得订单店铺区分失败，源订单号：" + jmOrderInfo.getOrder_sn() + "， shipping_system_id：" + shippingSystemId, ErrorType.BatchJob, SubSystem.OMS);
					continue;
				}
				
				// 订单店铺id
				transBean.setChannelId(orderChannelId);
				// 订单渠道id
				transBean.setCartId(cartId);
				// 源订单号
				transBean.setTid(StringUtils.null2Space2(jmOrderInfo.getOrder_sn()));
				
				// 订单支付时间
				String paymentTime = jmOrderInfo.getPayment_time();
				paymentTime = DateTimeUtil.getDateTimeFromUnixTime(paymentTime);
				// 格林威治时间转北京时间
				paymentTime = DateTimeUtil.getLocalTime(paymentTime, OmsConstants.TIME_ZONE_8);
				
				// 订单更新时间 (聚美用订单支付时间)
				transBean.setModified(StringUtils.null2Space2(paymentTime));
				// 订单支付时间
				transBean.setPay_time(StringUtils.null2Space2(paymentTime));
				
				// 收货人基本信息
				Receiver_Infos receiverInfo = jmOrderInfo.getReceiver_infos();
				if (receiverInfo != null) {
					String detailAddress = StringUtils.null2Space2(receiverInfo.getAddress());
					String[] detailAddressArray = detailAddress.split("-");
					String state = Constants.EMPTY_STR;
					String city = Constants.EMPTY_STR;
					String addressDetail = Constants.EMPTY_STR;
					String distinct = Constants.EMPTY_STR;
					String address = Constants.EMPTY_STR;
					if (detailAddressArray != null && detailAddressArray.length >= 3) {
						state = detailAddressArray[0];
						city = detailAddressArray[1];
						for (int i = 2; i < detailAddressArray.length; i++) {
							if (i > 2) {
								addressDetail += "-";
							}
							addressDetail += detailAddressArray[i];
						}
						
						if (!StringUtils.isNullOrBlank2(addressDetail)) {
							String[] addressDetailArray = addressDetail.split(Constants.BLANK_STR);
							if (addressDetailArray != null && addressDetailArray.length > 1) {
								distinct = StringUtils.null2Space2(addressDetailArray[0]);
							}
							address = addressDetail.substring(distinct.length() + 1);
						}
					}
					// 收货人县
					transBean.setReceiver_district(StringUtils.null2Space2(distinct));
					// 收货人地址
					transBean.setReceiver_address(StringUtils.null2Space2(address));
					// 收货人城市
					transBean.setReceiver_city(StringUtils.null2Space2(city));
					// 收货人省
					transBean.setReceiver_state(StringUtils.null2Space2(state));
					// 收件人邮编
					String zip = receiverInfo.getPostalcode();
					if ("000000".equals(zip) || zip == null) {
						zip = getZipFromAddress(StringUtils.null2Space2(state), StringUtils.null2Space2(city));
					}
					transBean.setReceiver_zip(StringUtils.null2Space2(zip));
					// 收货人姓名
					transBean.setReceiver_name(StringUtils.null2Space2(receiverInfo.getReceiver_name()));
					// 买家的账号昵称
					transBean.setBuyer_nick(StringUtils.null2Space2(receiverInfo.getReceiver_name() + "-聚美"));
					// 收货人手机
					transBean.setReceiver_mobile(StringUtils.null2Space2(receiverInfo.getHp()));
					// 收货人固定电话 
					transBean.setReceiver_phone(StringUtils.null2Space2(receiverInfo.getPhone()));
					// 收件人身份证号
					transBean.setId_card(StringUtils.null2Space2(receiverInfo.getId_card_num()));
				}
				
				// 订单内容信息
				List<Product_Infos> itemInfoList = jmOrderInfo.getProduct_infos();
				if (itemInfoList != null && itemInfoList.size() > 0) {
					List<TaobaoOrderBean> detailBeanList = new ArrayList<TaobaoOrderBean>();
					for (Product_Infos item : itemInfoList) {
						if (item != null) {
							TaobaoOrderBean detailBean = new TaobaoOrderBean();
							
							// SKU外部ID
							String outerSkuId = StringUtils.null2Space2(item.getSupplier_code());
							// [*]-->[.]转换
							if (outerSkuId != null && outerSkuId.length() > 0) {
								if (outerSkuId.contains("*")) {
									outerSkuId = outerSkuId.replace("*", ".");
								}
							}
							detailBean.setOuter_sku_id(outerSkuId);
							detailBean.setOuter_iid(outerSkuId);
							
							// 商品的名称+SKU规格
							String title = StringUtils.null2Space2(item.getDeal_short_name());
							detailBean.setTitle(title);
							
							// SKU的聚美价
							String price = StringUtils.null2Space2(item.getDeal_price());
							detailBean.setPrice(price);
							
							// 数量
							String num = StringUtils.null2Space2(item.getQuantity());
							detailBean.setNum(num);
							
							// 聚美内部商品ID
							String numIid = StringUtils.null2Space2(item.getSku_no());
							detailBean.setNum_iid(numIid);
							
							detailBeanList.add(detailBean);
						}
					}
					transBean.setOrder(detailBeanList);
				}
				
				transBean.setPaymentMethod(jmOrderInfo.getPayment_method());
				
				transBean.setAlipay_no(jmOrderInfo.getTrade_no());
				transBean.setBuyer_alipay_no(Constants.EMPTY_STR);
				
				// 订单商品总额
				String orderTotalPrice = jmOrderInfo.getTotal_products_price();
				double dblOrderTotalPrice = 0.00;
				if (!StringUtils.isEmpty(orderTotalPrice)) {
					transBean.setTotal_fee(orderTotalPrice);
					dblOrderTotalPrice = Double.parseDouble(orderTotalPrice);
				}
				
				// 运费
				String freightPrice = jmOrderInfo.getDelivery_fee();
				double dblFreightPrice = 0.00;
				if (!StringUtils.isEmpty(freightPrice)) {
					transBean.setPost_fee(freightPrice);
					dblFreightPrice = Double.parseDouble(freightPrice);
				}
				
				// 快递方式
				String logisticsId = "SF Standard";
				if (!StringUtils.isEmpty(logisticsId)) {
					transBean.setShipping_type(logisticsId);
				}
				
				// 折扣
				List<TaobaoPromotionBean> promotionlist = new ArrayList<TaobaoPromotionBean>();
				double coupon = 0;
				// 优惠详细信息 聚美的jane说没有
				// String couponStr = jmOrderInfo.getOrder_discount_price();
				String couponStr = "0"; 
				
				TaobaoPromotionBean bean = new TaobaoPromotionBean();
				
				bean.setName(Constants.DISCOUNT_STR);
				bean.setDescription(Constants.DISCOUNT_STR);
				bean.setDiscount(BigDecimal.valueOf(Double.valueOf(couponStr)));
				promotionlist.add(bean);
				
				coupon = coupon + Double.valueOf(couponStr);
				
				transBean.setPromotion(promotionlist);
				
				// 应该收到的金额=订单商品总额+运费-店铺折扣
				String payMent = String.valueOf(dblOrderTotalPrice + dblFreightPrice - coupon);
				transBean.setPayment(payMent);
				
				// 发票信息
				StringBuilder invoiceinfoSb  = new StringBuilder();
				String needInvoice = jmOrderInfo.getNeed_invoice();
				String invoiceMedium = jmOrderInfo.getInvoice_medium();
				String invoiceHeader = jmOrderInfo.getInvoice_header();
				String invoiceContents = jmOrderInfo.getInvoice_contents();
				
				if (needInvoice != null && needInvoice.length() > 0) {
					invoiceinfoSb.append("是否需要发票:");
					invoiceinfoSb.append(needInvoice);
					invoiceinfoSb.append(Constants.BLANK_STR);
				}
				
				if (invoiceMedium != null && invoiceMedium.length() > 0) {
					invoiceinfoSb.append("发票介质:");
					invoiceinfoSb.append(invoiceMedium);
					invoiceinfoSb.append(Constants.BLANK_STR);
				}
				
				if (invoiceHeader != null && invoiceHeader.length() > 0) {
					invoiceinfoSb.append("发票抬头:");
					invoiceinfoSb.append(invoiceHeader);
					invoiceinfoSb.append(Constants.BLANK_STR);
				}
				
				if (invoiceContents != null && invoiceContents.length() > 0) {
					invoiceinfoSb.append("发票内容:");
					invoiceinfoSb.append(invoiceContents);
				}
				transBean.setInvoice_info(invoiceinfoSb.toString());
				
				// 买家下单时订单备注
//				String orderRemark = orderSearchInfo.getOrderRemark();
//				transBean.setBuyer_message(StringUtils.null2Space2(orderRemark));
				
				transOrderList.add(transBean);
			}
			return transOrderList;
		} else {
			return new ArrayList<TaobaoTradeBean>();
		}
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
	 * 聚美系新订单插入临时表
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
	 * 获得聚美没有推送过短链接的订单记录
	 * 
	 * @param cartIdList
	 * @return
	 */
	public List<TaobaoTradeBean> getNeedShortUrlJmNewOrder(List<String> cartIdList) {
		List<TaobaoTradeBean> tradeInfoOrderList = null;
		try {
			tradeInfoOrderList = orderDao.getNeedShortUrlJmNewOrder(cartIdList);
			
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
		// 追加身份证信息
		rec.setId_card(StringUtils.null2Space2(trade.getId_card()));
		String orderComment = JsonUtil.getJsonString(rec);
		
		// 调用wsdl验证数据拼装
		JsonVerificationBean jsonVerificationBean = new JsonVerificationBean();
		jsonVerificationBean.setJsonData(orderComment);
		
		// 验证用KEY
		String dateTime = DateTimeUtil.getNow();
		jsonVerificationBean.setTimeStamp(dateTime);
		jsonVerificationBean.setSignature(Verification.getSynShipVerificationString(dateTime));
		
		// 调用WSDL
		logger.info("聚美推送到SynShip的短链接表");
		String shortUrl = Properties.readValue("SHIP_WEBSERVICE_URL_SHORT_URL");
		String response = WebServiceUtil.postByJsonStr(shortUrl, JsonUtil.getJsonString(jsonVerificationBean));
		
		// 推送成功的场合，更新send_flg
		JSONObject paraJson = new JSONObject(response);
		if (OmsConstants.RESULT_OK.equals(String.valueOf(paraJson.get("result")))) {
			logger.info("聚美订单数据推送到synship的短链接表成功：" + trade.getTid());
			
			boolean isResetSuccess = resetShortUrlSendFlag(trade, taskName);
			// 置位发送标志成功
			if (isResetSuccess) {
				logger.info("聚美 updateShortUrlSendFlag Success.");
				isPushSuccess = true;
			} else {
				logger.error("聚美 updateShortUrlSendFlag Failed.");
				errorMessage.add("oms_new_orders_import_history：聚美更新short_url_send_flag失败");
			}
		} else {
			logger.info("聚美订单数据推送到synship的短链接表失败：" + trade.getTid());
			
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
		// [*]-->[.]转换
		String outerSkuId = StringUtils.null2Space2("P108615*7-4503-H6*5");
		if (outerSkuId != null && outerSkuId.length() > 0) {
			if (outerSkuId.contains("*")) {
				outerSkuId = outerSkuId.replace("*", ".");
			}
		}
		
		
		// [H]-->[.5]转换
		if (outerSkuId != null && outerSkuId.length() > 0) {
			int length = outerSkuId.length();
			if (outerSkuId.endsWith("H")) {
				outerSkuId = outerSkuId.substring(0, length - 1) + ".5";
			}
		}
		
		
		String startOrderTime = "2015-08-02 10:21:56";
		
		Date startDateTime = DateTimeUtil.addMinutes(DateTimeUtil.parse(startOrderTime, DateTimeUtil.DEFAULT_DATETIME_FORMAT), -10);
		startOrderTime = DateTimeUtil.getDateTime(startDateTime, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
		System.out.println();
	}
}
