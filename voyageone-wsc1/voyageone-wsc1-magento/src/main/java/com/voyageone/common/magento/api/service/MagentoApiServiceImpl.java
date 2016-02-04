package com.voyageone.common.magento.api.service;

import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.magento.api.bean.*;
import com.voyageone.common.magento.api.base.*;
import com.voyageone.common.magento.api.service.*;
import com.voyageone.common.util.StringUtils;
import magento.*;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class MagentoApiServiceImpl {
	private static Log logger = LogFactory.getLog(MagentoApiServiceImpl.class);

	/**
	 * session失效code
	 */
	private static String RELOGIN_CODE = "5";

//	@Autowired
//	IssueLog issueLog;
	/**
	 * 店铺渠道
	 */
	private String orderChannelId;
	/**
	 * 登陆用数据Bean
	 */
	private LoginParam loginParam;
	/**
	 * 顾客信息
	 */
	private CustomerBean customer;
	
	private MagentoServiceStub stub;

	private String sessionId;
	
	/**
	 * @return the orderChannelId
	 */
	public String getOrderChannelId() {
		return orderChannelId;
	}
	
	/**
	 * @param orderChannelId the orderChannelId to set
	 */
	public void setOrderChannelId(String orderChannelId) {

		if (StringUtils.isNullOrBlank2(this.orderChannelId)) {
			this.orderChannelId = orderChannelId;
			// magento api调用 userName
			String userName = ThirdPartyConfigs.getVal1(this.orderChannelId, "userName");
			// magento api调用 密钥
			String apiKey = ThirdPartyConfigs.getVal1(this.orderChannelId, "apiKey");

			loginParam = new LoginParam();
			loginParam.setUsername(userName);
			loginParam.setApiKey(apiKey);
//			loginParam.setUsername("juicy_couture");
//			loginParam.setApiKey("abc.123");

	//		loginParam.setUsername("VOYAGEONE_API_USER");
	//		loginParam.setApiKey("hjA=fs2H0n+%PFd,b4wB");

			String customerId = ThirdPartyConfigs.getVal1(this.orderChannelId, "customerId");
			String customerMode = ThirdPartyConfigs.getVal1(this.orderChannelId, "customerMode");
			String storeId = ThirdPartyConfigs.getVal1(this.orderChannelId, "storeId");
			String webSiteId = ThirdPartyConfigs.getVal1(this.orderChannelId, "webSiteId");
			String storeName = ThirdPartyConfigs.getVal1(this.orderChannelId, "storeName");
			String country = ThirdPartyConfigs.getVal1(this.orderChannelId, "country");
			String paymentMethod = ThirdPartyConfigs.getVal1(this.orderChannelId, "paymentMethod");
			String shippingMethod = ThirdPartyConfigs.getVal1(this.orderChannelId, "shippingMethod");
			String houseNo = ThirdPartyConfigs.getVal1(this.orderChannelId, "houseNo");

	//		String customerId = "20";
	//		String customerMode = "customer";
	//		String customerMode = "guest";
	//		String storeId = "1";
	//		String webSiteId = "1";
	//		String storeName = "juicy_cn";

			customer = new CustomerBean();
			customer.setCustomerId(Integer.valueOf(customerId));
			customer.setMode(customerMode);
			customer.setStoreId(storeId);
			customer.setWebsiteId(Integer.valueOf(webSiteId));
			customer.setStore(storeName);
			customer.setCountry(country);
			customer.setPaymentMethod(paymentMethod);
			customer.setShippingMethod(shippingMethod);
			customer.setHouseNo(houseNo);
		}
	}
	
	/**
	 * Login to Magento
	 *
	 * @return
	 */
	private String login() throws Exception {
		// magento api调用 URL
		String url = ThirdPartyConfigs.getVal1(this.orderChannelId, "url");
//		String url = "https://api.juicycouture.asia/api/v2_soap";
		stub = new MagentoServiceStub(url);
//		stub = new MagentoServiceStub("http://www.wmf.com/api/v2_soap");
//		System.setProperty("javax.net.ssl.trustStore", "D:/tmp/trustStore");

		String useBasicAuth = ThirdPartyConfigs.getVal1(this.orderChannelId, "useBasicAuth");
		if ("1".equals(useBasicAuth)) {
			HttpTransportProperties.Authenticator auth = new HttpTransportProperties.Authenticator();
			String basicAuthUserName = ThirdPartyConfigs.getVal1(this.orderChannelId, "basicAuthUserName");
			String basicAuthPassword = ThirdPartyConfigs.getVal1(this.orderChannelId, "basicAuthPassword");
			auth.setUsername(basicAuthUserName);
			auth.setPassword(basicAuthPassword);
			stub._getServiceClient().getOptions().setProperty(HTTPConstants.AUTHENTICATE, auth);
		}
		
		// 登陆
		LoginResponseParam response = stub.login(loginParam);

		// session ID
		String sessionId = response.getResult();

		return sessionId;
	}
	
	/**
	 * Close Session
	 *
	 * @return
	 */
	private void logout(String sessionId) throws Exception {
		EndSessionParam endSession = new EndSessionParam();
		endSession.setSessionId(sessionId);
		stub.endSession(endSession);
	}
	
	/**
	 * 往magento推定单
	 * 
	 * @throws Exception
	 */
	public String pushOrder(OrderDataBean order) throws Exception {
		// 在magento上生成订单时是否发生异常
		boolean isException = false;
		
		// 订单号
		String orderCreateResult = "";
		
		String sessionId = "";
		try {
			// login
			sessionId = login();
			
			// login成功
			if (!StringUtils.isNullOrBlank2(sessionId)) {
			
				// 创建购物车参数准备
				ShoppingCartCreateRequestParam request = new ShoppingCartCreateRequestParam();
				request.setSessionId(sessionId);
				request.setStore(customer.getStoreId());
				
				// 创建购物车
				ShoppingCartCreateResponseParam cartCreateResponse = stub.shoppingCartCreate(request);
				if (cartCreateResponse != null) {
					// 新创建购物车ID
					int shoppingCartId = cartCreateResponse.getResult();
					
					// 购物车创建成功
					if (shoppingCartId > 0) {
						
						// 顾客对象
						ShoppingCartCustomerEntity customerEntity = new ShoppingCartCustomerEntity();
						customerEntity.setMode(customer.getMode());
						customerEntity.setEmail(order.getBillingEmail());
						customerEntity.setFirstname(order.getBillingName());
						
						// 购物车中顾客对象设置参数准备
						ShoppingCartCustomerSetRequestParam customer = new ShoppingCartCustomerSetRequestParam();
						customer.setSessionId(sessionId);
						customer.setCustomerData(customerEntity);
						customer.setQuoteId(shoppingCartId);
						// 购物车中顾客对象设置
						ShoppingCartCustomerSetResponseParam responseCustomer = stub.shoppingCartCustomerSet(customer);
						boolean isCustomerSuccess = responseCustomer.getResult();
						
						// 购物车中顾客对象设置成功
						if (isCustomerSuccess) {
							
							// 购物车中商品追加参数设置
							ShoppingCartProductAddRequestParam productAddRequest = new ShoppingCartProductAddRequestParam();
							productAddRequest.setQuoteId(shoppingCartId);
							productAddRequest.setSessionId(sessionId);
							
							ShoppingCartProductEntityArray productEntityArrays = new ShoppingCartProductEntityArray();

							List<OrderDetailBean> detailList = order.getOrderDetails();
							if (detailList != null && detailList.size() > 0) {
								for (OrderDetailBean orderDetail : detailList) {
									ShoppingCartProductEntity productEntity = new ShoppingCartProductEntity();
									productEntity.setSku(orderDetail.getSku());
									productEntity.setQty(orderDetail.getQty());
									productEntityArrays.addComplexObjectArray(productEntity);
								}
							}
							
							productAddRequest.setProductsData(productEntityArrays);
							
							ShoppingCartProductAddResponseParam productAddResponse = stub.shoppingCartProductAdd(productAddRequest);
							boolean isProductAddSuccess = productAddResponse.getResult();
							
							// 购物车中商品追加成功
							if (isProductAddSuccess) {

								// 订单级折扣
								double orderDiscount = order.getDiscount();
								// 订单级补价
								double orderSurcharge = order.getSurcharge();
								// 订单级实际差价
								double orderPriceDiff = Math.abs(orderDiscount) - Math.abs(orderSurcharge);
								orderPriceDiff = -orderPriceDiff;
								// 订单物品实际总价
								double finalGrandTotal = order.getFinalGrandTotal();

								ShoppingCartProductCustomPriceRequestParam customerPriceRequest = new ShoppingCartProductCustomPriceRequestParam();
								if (detailList != null && detailList.size() > 0) {
									int detailSize = detailList.size();
									getPriceDiffPer(orderPriceDiff, finalGrandTotal, detailList);

									ShoppingCartProductCustomPriceEntityArray customPriceEntityArray = new ShoppingCartProductCustomPriceEntityArray();

									for (int i = 0; i < detailSize; i++) {
										OrderDetailBean orderDetail = detailList.get(i);

										ShoppingCartProductCustomPriceEntity param = new ShoppingCartProductCustomPriceEntity();
										param.setSku(orderDetail.getSku());
										param.setPrice(orderDetail.getRealPrice());
										customPriceEntityArray.addComplexObjectArray(param);

									}
									customerPriceRequest.setProductsData(customPriceEntityArray);
								}
								customerPriceRequest.setSessionId(sessionId);
								customerPriceRequest.setQuoteId(shoppingCartId);

								ShoppingCartProductCustomPriceResponseParam customPriceResponse = stub.shoppingCartProductSetCustomPrice(customerPriceRequest);
								boolean isCustomerPrice = customPriceResponse.getResult();

								if (isCustomerPrice) {

									// 购物车中顾客账单地址和收货地址设置参数准备
									ShoppingCartCustomerAddressesRequestParam addressRequest = new ShoppingCartCustomerAddressesRequestParam();
									addressRequest.setQuoteId(shoppingCartId);
									addressRequest.setSessionId(sessionId);
									ShoppingCartCustomerAddressEntityArray addressEntity = new ShoppingCartCustomerAddressEntityArray();

									// 账单人信息
									ShoppingCartCustomerAddressEntity billingAddress = new ShoppingCartCustomerAddressEntity();
									billingAddress.setMode("billing");
									// 账单人
									billingAddress.setFirstname(order.getBillingName());
									// 市
									billingAddress.setCity(order.getBillingCity());
									// 省
									billingAddress.setRegion(order.getBillingState());
									// 街道地址
									billingAddress.setStreet(order.getBillingAddress());
									// 电话
									billingAddress.setTelephone(order.getBillingTelephone());
									// 邮编
									billingAddress.setPostcode(order.getBillingPostcode());
									// 国家
									billingAddress.setCountry_id(order.getBillingCountry());
									billingAddress.setIs_default_billing(0);

									// 收货人信息
									ShoppingCartCustomerAddressEntity shippingAddress = new ShoppingCartCustomerAddressEntity();
									shippingAddress.setMode("shipping");
									// 收件人
									shippingAddress.setFirstname(order.getShippingName());
									// 市
									shippingAddress.setCity(order.getShippingCity());
									// 省
									shippingAddress.setRegion(order.getShippingState());
									// 街道地址
									shippingAddress.setStreet(order.getShippingAddress());
									// 电话
									shippingAddress.setTelephone(order.getShippingTelephone());
									// 邮编
									shippingAddress.setPostcode(order.getShippingPostcode());
									// 国家
									shippingAddress.setCountry_id(order.getShippingCountry());
									shippingAddress.setIs_default_shipping(0);

									addressEntity.addComplexObjectArray(billingAddress);
									addressEntity.addComplexObjectArray(shippingAddress);
									addressRequest.setCustomerAddressData(addressEntity);

									// 购物车中顾客账单地址和收货地址设置
									ShoppingCartCustomerAddressesResponseParam addressResponse = stub.shoppingCartCustomerAddresses(addressRequest);
									boolean isAddressSuccess = addressResponse.getResult();

									// 购物车中顾客账单地址和收货地址设置成功
									if (isAddressSuccess) {

										// 发货方式设定参数准备
										ShoppingCartShippingMethodRequestParam shippingMethodRequest = new ShoppingCartShippingMethodRequestParam();
										shippingMethodRequest.setQuoteId(shoppingCartId);
										shippingMethodRequest.setSessionId(sessionId);
										shippingMethodRequest.setShippingMethod(this.customer.getShippingMethod());

										// 发货方式设定
										ShoppingCartShippingMethodResponseParam shippingMethodResponse = stub.shoppingCartShippingMethod(shippingMethodRequest);
										boolean isShippingMethodSuccess = shippingMethodResponse.getResult();

										// 发货方式设定成功
										if (isShippingMethodSuccess) {

											// 支付方式设定参数准备
											ShoppingCartPaymentMethodRequestParam paymentMethodRequest = new ShoppingCartPaymentMethodRequestParam();
											paymentMethodRequest.setQuoteId(shoppingCartId);
											paymentMethodRequest.setSessionId(sessionId);
											ShoppingCartPaymentMethodEntity paymentMethodEntity = new ShoppingCartPaymentMethodEntity();
											paymentMethodEntity.setMethod(this.customer.getPaymentMethod());
											paymentMethodRequest.setPaymentData(paymentMethodEntity);

											// 支付方式设定
											ShoppingCartPaymentMethodResponseParam paymentMethodResponse = stub.shoppingCartPaymentMethod(paymentMethodRequest);
											boolean isPaymentMethodSuccess = paymentMethodResponse.getResult();

											// 支付方式设定成功
											if (isPaymentMethodSuccess) {

												// 下订单参数准备
												ShoppingCartOrderRequestParam shoppingCartOrderRequestParam = new ShoppingCartOrderRequestParam();
												shoppingCartOrderRequestParam.setSessionId(sessionId);
												shoppingCartOrderRequestParam.setQuoteId(shoppingCartId);

												// 下订单
												ShoppingCartOrderResponseParam orderResponse = stub.shoppingCartOrder(shoppingCartOrderRequestParam);
												// 订单号
												orderCreateResult = orderResponse.getResult();

											} else {
												logger.info("支付方式设定失败");
											}
										} else {
											logger.info("发货方式设定失败");
										}
									} else {
										logger.info("购物车中顾客账单地址和收货地址设置失败");
									}
								} else {
									logger.info("购物车中商品价格修改失败");
								}
							} else {
								logger.info("购物车中商品追加失败");
							}
						} else {
							logger.info("购物车中顾客对象设置失败");
						}
					} else {
						logger.info("购物车创建失败");
					}
					
				} else {
					logger.info("购物车创建失败");
				}
			} else {
				logger.info("login is failure");
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			
			isException = true;
			
			throw ex;
		} finally {
			// end session
			if (!StringUtils.isNullOrBlank2(sessionId)) {
				try {
					logout(sessionId);
				} catch (Exception ex) {
					logger.error(ex.getMessage(), ex);
					
					if (!isException) {
						throw ex;
					}
				}
			}
		}
		
		// 返回订单号
		return orderCreateResult;
	}

	/**
	 * 往magento推定单(上次登陆有效的话不用重新登陆)
	 *
	 * @throws Exception
	 */
	public String pushOrderWithOneSession(OrderDataBean order) throws Exception {
		// 订单号
		String orderCreateResult = "";

		String sessionId = this.sessionId;

		try {
			if (StringUtils.isNullOrBlank2(sessionId)) {
				// login
				sessionId = login();
				this.sessionId = sessionId;
			}

			// login成功
			if (!StringUtils.isNullOrBlank2(sessionId)) {

				// 创建购物车参数准备
				ShoppingCartCreateRequestParam request = new ShoppingCartCreateRequestParam();
				request.setSessionId(sessionId);
				request.setStore(customer.getStoreId());

				// 创建购物车
				ShoppingCartCreateResponseParam cartCreateResponse;
				try {
					cartCreateResponse = stub.shoppingCartCreate(request);

				// session失效
				} catch (AxisFault ex) {
					boolean isNotOtherAxisFault = false;
					try {
						isNotOtherAxisFault = RELOGIN_CODE.equals(ex.getFaultCode().getLocalPart());
					} catch (Exception e) {
						// 抛出原始异常
						throw ex;
					}
					if (isNotOtherAxisFault) {
						sessionId = login();
						this.sessionId = sessionId;
						request.setSessionId(sessionId);

						cartCreateResponse = stub.shoppingCartCreate(request);
					} else {
						throw ex;
					}
				} catch (Exception ex) {
					throw ex;
				}

				if (cartCreateResponse != null) {
					// 新创建购物车ID
					int shoppingCartId = cartCreateResponse.getResult();

					// 购物车创建成功
					if (shoppingCartId > 0) {

						// 创建外部ID（ExternalId）
						ShoppingCartExternalIdRequestParam externalIdRequest =  new ShoppingCartExternalIdRequestParam();
						externalIdRequest.setSessionId(sessionId);
						externalIdRequest.setQuoteId(shoppingCartId);
						String externalId = "cainiaoId=" + order.getTaobao_logistics_id() + ";sourceOrderId=" + order.getOrigin_source_order_id() + ";payNo=" + order.getPay_no();
						externalIdRequest.setExternalId(externalId);
						externalIdRequest.setStore(customer.getStoreId());
						ShoppingCartExternalIdResponseParam externalIdResponse = stub.shoppingCartExternalId(externalIdRequest);
						boolean isExternalIdSuccess = externalIdResponse.getResult();

						// 外部ID（ExternalId）创建成功
						if (isExternalIdSuccess) {

							// 顾客对象
							ShoppingCartCustomerEntity customerEntity = new ShoppingCartCustomerEntity();
							customerEntity.setMode(customer.getMode());
							customerEntity.setEmail(StringUtils.isNullOrBlank2(order.getBillingEmail())? "DummyEmail@dummy.com":order.getBillingEmail());
							customerEntity.setFirstname(StringUtils.isNullOrBlank2(order.getBillingName())? "DummyName":order.getBillingName());
							customerEntity.setLastname(StringUtils.isNullOrBlank2(order.getBillingName())? "DummyName":order.getBillingName());

							// 购物车中顾客对象设置参数准备
							ShoppingCartCustomerSetRequestParam customer = new ShoppingCartCustomerSetRequestParam();
							customer.setSessionId(sessionId);
							customer.setCustomerData(customerEntity);
							customer.setQuoteId(shoppingCartId);
							// 购物车中顾客对象设置
							ShoppingCartCustomerSetResponseParam responseCustomer = stub.shoppingCartCustomerSet(customer);
							boolean isCustomerSuccess = responseCustomer.getResult();

							// 购物车中顾客对象设置成功
							if (isCustomerSuccess) {

								// 购物车中商品追加参数设置
								ShoppingCartProductAddRequestParam productAddRequest = new ShoppingCartProductAddRequestParam();
								productAddRequest.setQuoteId(shoppingCartId);
								productAddRequest.setSessionId(sessionId);

								ShoppingCartProductEntityArray productEntityArrays = new ShoppingCartProductEntityArray();

								List<OrderDetailBean> detailList = order.getOrderDetails();
								if (detailList != null && detailList.size() > 0) {
									for (OrderDetailBean orderDetail : detailList) {
										ShoppingCartProductEntity productEntity = new ShoppingCartProductEntity();
										String clientSku = orderDetail.getClientSku();
										String sku = orderDetail.getSku();
										productEntity.setSku(StringUtils.isNullOrBlank2(clientSku) ? sku : clientSku);
										productEntity.setQty(orderDetail.getQty());
										productEntityArrays.addComplexObjectArray(productEntity);
									}
								}

								productAddRequest.setProductsData(productEntityArrays);

								ShoppingCartProductAddResponseParam productAddResponse = stub.shoppingCartProductAdd(productAddRequest);
								boolean isProductAddSuccess = productAddResponse.getResult();

								// 购物车中商品追加成功
								if (isProductAddSuccess) {

									// 订单级折扣
									double orderDiscount = order.getDiscount();
									// 订单级补价
									double orderSurcharge = order.getSurcharge();
									// 订单级实际差价
									double orderPriceDiff = Math.abs(orderDiscount) - Math.abs(orderSurcharge);
									orderPriceDiff = -orderPriceDiff;
									// 订单物品实际总价
									double finalGrandTotal = order.getFinalGrandTotal();

									ShoppingCartProductCustomPriceRequestParam customerPriceRequest = new ShoppingCartProductCustomPriceRequestParam();
									customerPriceRequest.setSessionId(sessionId);
									customerPriceRequest.setQuoteId(shoppingCartId);

									if (detailList != null && detailList.size() > 0) {
										int detailSize = detailList.size();
										getPriceDiffPer(orderPriceDiff, finalGrandTotal, detailList);

										ShoppingCartProductCustomPriceEntityArray customPriceEntityArray = new ShoppingCartProductCustomPriceEntityArray();

										for (int i = 0; i < detailSize; i++) {
											OrderDetailBean orderDetail = detailList.get(i);

											ShoppingCartProductCustomPriceEntity param = new ShoppingCartProductCustomPriceEntity();
											String clientSku = orderDetail.getClientSku();
											String sku = orderDetail.getSku();
											param.setSku(StringUtils.isNullOrBlank2(clientSku) ? sku : clientSku);
											param.setPrice(orderDetail.getRealPrice());
											customPriceEntityArray.addComplexObjectArray(param);
										}
										customerPriceRequest.setProductsData(customPriceEntityArray);
									}

									ShoppingCartProductCustomPriceResponseParam customPriceResponse = stub.shoppingCartProductSetCustomPrice(customerPriceRequest);
									boolean isCustomerPrice = customPriceResponse.getResult();

									if (isCustomerPrice) {
										// 购物车中顾客账单地址和收货地址设置参数准备
										ShoppingCartCustomerAddressesRequestParam addressRequest = new ShoppingCartCustomerAddressesRequestParam();
										addressRequest.setQuoteId(shoppingCartId);
										addressRequest.setSessionId(sessionId);
										ShoppingCartCustomerAddressEntityArray addressEntity = new ShoppingCartCustomerAddressEntityArray();

										// 账单人信息
										ShoppingCartCustomerAddressEntity billingAddress = new ShoppingCartCustomerAddressEntity();
										billingAddress.setMode("billing");
										billingAddress.setFirstname(StringUtils.isNullOrBlank2(order.getBillingName())? "DummyName" : order.getBillingName());
										billingAddress.setLastname(StringUtils.isNullOrBlank2(order.getBillingName())? "DummyName" : order.getBillingName());
										billingAddress.setCity(StringUtils.isNullOrBlank2(order.getBillingCity())? "DummyCity" : order.getBillingCity());
										billingAddress.setRegion(StringUtils.isNullOrBlank2(order.getBillingState())? "DummyState" : order.getBillingState());
										billingAddress.setStreet(StringUtils.isNullOrBlank2(order.getBillingAddress())? "DummyAddress" : order.getBillingAddress());
										billingAddress.setTelephone(StringUtils.isNullOrBlank2(order.getShippingTelephone())? "00000000" : order.getShippingTelephone());
										billingAddress.setFax(StringUtils.isNullOrBlank2(order.getShippingTelephone())? "00000000" : order.getShippingTelephone());
										billingAddress.setPostcode(StringUtils.isNullOrBlank2(order.getBillingPostcode())? "000000" : order.getBillingPostcode());

//										billingAddress.setFirstname("testFirstname");
//										billingAddress.setLastname("testLastname");
//										billingAddress.setCompany("testCompany");
//										billingAddress.setStreet("testStreet");
//										billingAddress.setCity("testCity");
//										billingAddress.setRegion("testRegion");
//										billingAddress.setPostcode("testPostcode");
										billingAddress.setCountry_id(this.customer.getCountry());
//										billingAddress.setTelephone("0123456789");
//										billingAddress.setFax("0123456789");
										billingAddress.setHouse_no(this.customer.getHouseNo());
										billingAddress.setIs_default_billing(0);
										billingAddress.setIs_default_shipping(0);

										// 收货人信息
										ShoppingCartCustomerAddressEntity shippingAddress = new ShoppingCartCustomerAddressEntity();
										shippingAddress.setMode("shipping");
										shippingAddress.setFirstname(StringUtils.isNullOrBlank2(order.getShippingName())? "DummyName" : order.getShippingName());
										shippingAddress.setLastname(StringUtils.isNullOrBlank2(order.getShippingName())? "DummyName" : order.getShippingName());
										shippingAddress.setCity(StringUtils.isNullOrBlank2(order.getShippingCity())? "DummyCity" : order.getShippingCity());
										shippingAddress.setRegion(StringUtils.isNullOrBlank2(order.getShippingState())? "DummyState" : order.getShippingState());
										shippingAddress.setStreet(StringUtils.isNullOrBlank2(order.getShippingAddress())? "DummyAddress" : order.getShippingAddress());
										shippingAddress.setTelephone(StringUtils.isNullOrBlank2(order.getShippingTelephone())? "00000000" : order.getShippingTelephone());
										shippingAddress.setFax(StringUtils.isNullOrBlank2(order.getShippingTelephone())? "00000000" : order.getShippingTelephone());
										shippingAddress.setPostcode(StringUtils.isNullOrBlank2(order.getShippingPostcode())? "000000" : order.getShippingPostcode());

//										shippingAddress.setFirstname("testFirstname");
//										shippingAddress.setLastname("testLastname");
//										shippingAddress.setCompany("testCompany");
//										shippingAddress.setStreet("testStreet");
//										shippingAddress.setCity("testCity");
//										shippingAddress.setRegion("testRegion");
//										shippingAddress.setPostcode("testPostcode");
										shippingAddress.setCountry_id(this.customer.getCountry());
//										shippingAddress.setTelephone("0123456789");
//										shippingAddress.setFax("0123456789");
										shippingAddress.setHouse_no(this.customer.getHouseNo());
										shippingAddress.setIs_default_billing(0);
										shippingAddress.setIs_default_shipping(0);

										addressEntity.addComplexObjectArray(billingAddress);
										addressEntity.addComplexObjectArray(shippingAddress);
										addressRequest.setCustomerAddressData(addressEntity);

										// 购物车中顾客账单地址和收货地址设置
										ShoppingCartCustomerAddressesResponseParam addressResponse = stub.shoppingCartCustomerAddresses(addressRequest);
										boolean isAddressSuccess = addressResponse.getResult();

										// 购物车中顾客账单地址和收货地址设置成功
										if (isAddressSuccess) {

											// 发货方式设定参数准备
											ShoppingCartShippingMethodRequestParam shippingMethodRequest = new ShoppingCartShippingMethodRequestParam();
											shippingMethodRequest.setQuoteId(shoppingCartId);
											shippingMethodRequest.setSessionId(sessionId);
											shippingMethodRequest.setShippingMethod(this.customer.getShippingMethod());
											shippingMethodRequest.setStore(customer.getStore());

											// 发货方式设定
											ShoppingCartShippingMethodResponseParam shippingMethodResponse = stub.shoppingCartShippingMethod(shippingMethodRequest);
											boolean isShippingMethodSuccess = shippingMethodResponse.getResult();

											// 发货方式设定成功
											if (isShippingMethodSuccess) {

												// 支付方式设定参数准备
												ShoppingCartPaymentMethodRequestParam paymentMethodRequest = new ShoppingCartPaymentMethodRequestParam();
												paymentMethodRequest.setQuoteId(shoppingCartId);
												paymentMethodRequest.setSessionId(sessionId);
												ShoppingCartPaymentMethodEntity paymentMethodEntity = new ShoppingCartPaymentMethodEntity();
												paymentMethodEntity.setMethod(this.customer.getPaymentMethod());
												paymentMethodRequest.setPaymentData(paymentMethodEntity);

												// 支付方式设定
												ShoppingCartPaymentMethodResponseParam paymentMethodResponse = stub.shoppingCartPaymentMethod(paymentMethodRequest);
												boolean isPaymentMethodSuccess = paymentMethodResponse.getResult();

												// 支付方式设定成功
												if (isPaymentMethodSuccess) {

													// 下订单参数准备
													ShoppingCartOrderRequestParam shoppingCartOrderRequestParam = new ShoppingCartOrderRequestParam();
													shoppingCartOrderRequestParam.setSessionId(sessionId);
													shoppingCartOrderRequestParam.setQuoteId(shoppingCartId);

													// 下订单
													ShoppingCartOrderResponseParam orderResponse = stub.shoppingCartOrder(shoppingCartOrderRequestParam);
													// 订单号
													orderCreateResult = orderResponse.getResult();

												} else {
													logger.info("支付方式设定失败");
												}
											} else {
												logger.info("发货方式设定失败");
											}
										} else {
											logger.info("购物车中顾客账单地址和收货地址设置失败");
										}
									} else {
										logger.info("购物车中商品价格修改失败");
									}
								} else {
									logger.info("购物车中商品追加失败");
								}
							} else {
								logger.info("购物车中顾客对象设置失败");
							}
						} else {
							logger.info("购物车中ExternalID创建失败");
						}
					} else {
						logger.info("购物车创建失败");
					}

				} else {
					logger.info("购物车创建失败");
				}
			} else {
				logger.info("login is failure");
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);

			throw ex;
		}

		// 返回订单号
		return orderCreateResult;
	}
	
	/**
	 * 库存API
	 * 
	 * @param skuList
	 * @return
	 * @throws Exception
	 */
	public List<InventoryStockItemBean> inventoryStockItemList(List<String> skuList) throws Exception {
		
		List<InventoryStockItemBean> inventoryList = new ArrayList<InventoryStockItemBean>();
		
		// 获得magento商品库存时是否发生异常
		boolean isException = false;
		
		if (skuList != null && skuList.size() > 0) {
			// magento api调用 库存sku数
			String inventoryMaxSize = ThirdPartyConfigs.getVal1(this.orderChannelId, "inventoryMaxSize");
			int inventoryMaxSizeInt = Integer.valueOf(inventoryMaxSize);

			List<List<String>> skuTotalList = new ArrayList<List<String>>();
			// 传递的sku数超过每次查询的上限
			if (skuList.size() > inventoryMaxSizeInt) {
				getTotalSkuList(skuTotalList, skuList, inventoryMaxSizeInt);

			} else {
				skuTotalList.add(skuList);
			}

			// login
			String sessionId = "";
			
			try {
				sessionId = login();

				// 入力参数初始化
				CatalogInventoryStockItemListRequestParam request = new CatalogInventoryStockItemListRequestParam();
				// 入力参数设置sessionId
				request.setSessionId(sessionId);
				
				for (List<String> skuListItem : skuTotalList) {
					if (skuListItem != null && skuListItem.size() > 0) {
						ArrayOfString productIds = new ArrayOfString();

						for (String sku : skuListItem) {
							if (!StringUtils.isNullOrBlank2(sku)) {
								// sku列表追加
								productIds.addComplexObjectArray(sku);
							}
						}

						// 请求的sku列表不为空
						if (productIds.getComplexObjectArray() != null && productIds.getComplexObjectArray().length > 0) {
							// 入力参数设置sku列表
							request.setProductIds(productIds);

							// 向magento请求sku列表的库存
							CatalogInventoryStockItemListResponseParam inventoryParam = stub.catalogInventoryStockItemList(request);

							// 解析返回值
							CatalogInventoryStockItemEntity[] inventoryArray = inventoryParam.getResult().getComplexObjectArray();
							if (inventoryArray != null && inventoryArray.length > 0) {

								for (CatalogInventoryStockItemEntity inventory : inventoryArray) {
									InventoryStockItemBean item = new InventoryStockItemBean();
									// Defines whether the product is in stock
									item.setIsInStock(inventory.getIs_in_stock());
									// Product ID
									item.setProductId(inventory.getProduct_id());
									// Product SKU
									item.setSku(inventory.getSku());
									// Product quantity
									item.setQty(inventory.getQty());

									inventoryList.add(item);
								}
							}

						}
					}
				}
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);

				isException = true;

				throw ex;
			} finally {
				// end session
				if (!StringUtils.isNullOrBlank2(sessionId)) {
					try {
						logout(sessionId);
					} catch (Exception ex) {
						logger.error(ex.getMessage(), ex);

						if (!isException) {
							throw ex;
						}
					}
				}
			}
		}
		
		// 返回库存列表信息
		return inventoryList;
	}

	/**
	 * 库存API
	 *
	 * @param skuList
	 * @return
	 * @throws Exception
	 */
	public List<InventoryStockItemBean> inventoryStockItemListWithOneSession(List<String> skuList) throws Exception {
		CatalogInventoryStockItemListResponseParam inventoryParam;

		List<InventoryStockItemBean> inventoryList = new ArrayList<InventoryStockItemBean>();

		// 获得magento商品库存时是否发生异常
		boolean isException = false;

		if (skuList != null && skuList.size() > 0) {
			// magento api调用 库存sku数
			String inventoryMaxSize = ThirdPartyConfigs.getVal1(this.orderChannelId, "inventoryMaxSize");
			int inventoryMaxSizeInt = Integer.valueOf(inventoryMaxSize);

			List<List<String>> skuTotalList = new ArrayList<List<String>>();
			// 传递的sku数超过每次查询的上限
			if (skuList.size() > inventoryMaxSizeInt) {
				getTotalSkuList(skuTotalList, skuList, inventoryMaxSizeInt);

			} else {
				skuTotalList.add(skuList);
			}

			// login
			String sessionId = this.sessionId;

			try {
				if (StringUtils.isNullOrBlank2(sessionId)) {
					// login
					sessionId = login();
					this.sessionId = sessionId;
				}

				// 入力参数初始化
				CatalogInventoryStockItemListRequestParam request = new CatalogInventoryStockItemListRequestParam();
				// 入力参数设置sessionId
				request.setSessionId(sessionId);

				for (List<String> skuListItem : skuTotalList) {
					if (skuListItem != null && skuListItem.size() > 0) {
						ArrayOfString productIds = new ArrayOfString();

						for (String sku : skuListItem) {
							if (!StringUtils.isNullOrBlank2(sku)) {
								// sku列表追加
								productIds.addComplexObjectArray(sku);
							}
						}

						// 请求的sku列表不为空
						if (productIds.getComplexObjectArray() != null && productIds.getComplexObjectArray().length > 0) {
							// 入力参数设置sku列表
							request.setProductIds(productIds);

							// 向magento请求sku列表的库存
							try {
								inventoryParam = stub.catalogInventoryStockItemList(request);
							} catch (AxisFault ex) {
								boolean isNotOtherAxisFault = false;
								try {
									isNotOtherAxisFault = RELOGIN_CODE.equals(ex.getFaultCode().getLocalPart());
								} catch (Exception e) {
									// 抛出原始异常
									throw ex;
								}
								// session失效
								if (isNotOtherAxisFault) {
									sessionId = login();
									this.sessionId = sessionId;
									request.setSessionId(sessionId);

									inventoryParam = stub.catalogInventoryStockItemList(request);
								} else {
									throw ex;
								}
							} catch (Exception ex) {
								throw ex;
							}


							// 解析返回值
							CatalogInventoryStockItemEntity[] inventoryArray = inventoryParam.getResult().getComplexObjectArray();
							if (inventoryArray != null && inventoryArray.length > 0) {

								for (CatalogInventoryStockItemEntity inventory : inventoryArray) {
									InventoryStockItemBean item = new InventoryStockItemBean();
									// Defines whether the product is in stock
									item.setIsInStock(inventory.getIs_in_stock());
									// Product ID
									item.setProductId(inventory.getProduct_id());
									// Product SKU
									item.setSku(inventory.getSku());
									// Product quantity
									item.setQty(inventory.getQty());

									inventoryList.add(item);
								}
							}

						}
					}
				}
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);

				throw ex;
			}
		}

		// 返回库存列表信息
		return inventoryList;
	}

	/**
	 * 过大时拆分sku列表
	 * @param skuTotalList
	 * @param skuList
	 * @param inventoryMaxSizeInt
	 */
	private void getTotalSkuList(List<List<String>> skuTotalList, List<String> skuList, int inventoryMaxSizeInt) {
		int skuListSize = skuList.size();
		int count = skuListSize / inventoryMaxSizeInt + 1;
		for (int i = 0; i < count; i++) {
			int start = i * inventoryMaxSizeInt;
			int end = (i + 1) * inventoryMaxSizeInt;
			if (end > skuListSize) {
				end = skuListSize;
			}
			List<String> skuListItem = skuList.subList(start, end);
			if (skuListItem != null && skuListItem.size() > 0) {
				skuTotalList.add(skuListItem);
			}
		}
	}
	
	/**
	 * 获得订单信息
	 * @param orderNumber
	 */
	public SalesOrderInfoResponseParam getSalesOrderInfo(String orderNumber) throws Exception {
		// 获得magento订单信息时是否发生异常
		boolean isException = false;
		
		String sessionId = "";
		try {
			// login
			sessionId = login();
			
			SalesOrderInfoRequestParam request = new SalesOrderInfoRequestParam();
			request.setOrderIncrementId(orderNumber);
			request.setSessionId(sessionId);
			
			SalesOrderInfoResponseParam response = stub.salesOrderInfo(request);
			
			return response;
			
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			
			isException = true;
			
			throw ex;
		} finally {
			// end session
			if (!StringUtils.isNullOrBlank2(sessionId)) {
				try {
					logout(sessionId);
				} catch (Exception ex) {
					logger.error(ex.getMessage(), ex);
					
					if (!isException) {
						throw ex;
					}
				}
			}
		}
	}

	/**
	 * 获得订单信息
	 * @param orderNumber
	 */
	public SalesOrderInfoResponseParam getSalesOrderInfoWithOneSession(String orderNumber) throws Exception {
		SalesOrderInfoResponseParam response;

		String sessionId = this.sessionId;
		try {
			if (StringUtils.isNullOrBlank2(sessionId)) {
				// login
				sessionId = login();
				this.sessionId = sessionId;
			}

			SalesOrderInfoRequestParam request = new SalesOrderInfoRequestParam();
			request.setOrderIncrementId(orderNumber);
			request.setSessionId(sessionId);

			try {
				response = stub.salesOrderInfo(request);

			} catch (AxisFault ex) {
				boolean isNotOtherAxisFault = false;
				try {
					isNotOtherAxisFault = RELOGIN_CODE.equals(ex.getFaultCode().getLocalPart());
				} catch (Exception e) {
					// 抛出原始异常
					throw ex;
				}
				// session失效
				if (isNotOtherAxisFault) {
					sessionId = login();
					this.sessionId = sessionId;
					request.setSessionId(sessionId);

					response = stub.salesOrderInfo(request);
				} else {
					throw ex;
				}
			} catch (Exception ex) {
				throw ex;
			}


		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);

			throw ex;
		}

		return response;
	}

	/**
	 * 取消订单
	 * @param orderNumber
	 */
	public SalesOrderCancelResponseParam cancelSalesOrder(String orderNumber) throws Exception {
		// 获得magento订单信息时是否发生异常
		boolean isException = false;

		String sessionId = "";
		try {
			// login
			sessionId = login();

			SalesOrderCancelRequestParam request = new SalesOrderCancelRequestParam();
			request.setOrderIncrementId(orderNumber);
			request.setSessionId(sessionId);

			SalesOrderCancelResponseParam response = stub.salesOrderCancel(request);

			return response;

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);

			isException = true;

			throw ex;
		} finally {
			// end session
			if (!StringUtils.isNullOrBlank2(sessionId)) {
				try {
					logout(sessionId);
				} catch (Exception ex) {
					logger.error(ex.getMessage(), ex);

					if (!isException) {
						throw ex;
					}
				}
			}
		}
	}

	/**
	 * 取消订单
	 * @param orderNumber
	 */
	public SalesOrderCancelResponseParam cancelSalesOrderWithOneSession(String orderNumber) throws Exception {
		SalesOrderCancelResponseParam response;

		String sessionId = this.sessionId;;
		try {
			if (StringUtils.isNullOrBlank2(sessionId)) {
				// login
				sessionId = login();
				this.sessionId = sessionId;
			}

			SalesOrderCancelRequestParam request = new SalesOrderCancelRequestParam();
			request.setOrderIncrementId(orderNumber);
			request.setSessionId(sessionId);

			try {
				response = stub.salesOrderCancel(request);
			} catch (AxisFault ex) {
				boolean isNotOtherAxisFault = false;
				try {
					isNotOtherAxisFault = RELOGIN_CODE.equals(ex.getFaultCode().getLocalPart());
				} catch (Exception e) {
					// 抛出原始异常
					throw ex;
				}
				if (isNotOtherAxisFault) {
					sessionId = login();
					this.sessionId = sessionId;
					request.setSessionId(sessionId);

					response = stub.salesOrderCancel(request);
				} else {
					throw ex;
				}
			} catch (Exception ex) {
				throw ex;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);

			throw ex;
		}

		return response;
	}

	/**
	 * 向wmf追加菜鸟id
	 *
	 * @param cainiaoMap
	 */
	public int addNewOrderTrack(Map<String, String> cainiaoMap) throws Exception {
		boolean isException = false;

		int result = 0;

		String clientOrderId = cainiaoMap.get("client_order_id");
		String sourceOrderId = cainiaoMap.get("origin_source_order_id");
		String payNo = cainiaoMap.get("pay_no");
		String cainiaoId = cainiaoMap.get("taobao_logistics_id");

		String sessionId = "";
		try {
			// login
			sessionId = login();

			SalesOrderAddCommentRequestParam requestParam = new SalesOrderAddCommentRequestParam();
			requestParam.setSessionId(this.sessionId);
			requestParam.setOrderIncrementId(clientOrderId);
			String comment = "cainiaoId=" + cainiaoId + ";sourceOrderId=" + sourceOrderId + ";payNo=" + payNo;
			requestParam.setComment(comment);
			requestParam.setStatus("pending");
			SalesOrderAddCommentResponseParam responseParam = stub.salesOrderAddComment(requestParam);
			result = responseParam.getResult();

//			SalesOrderShipmentCreateRequestParam request = new SalesOrderShipmentCreateRequestParam();
//			request.setOrderIncrementId(clientOrderId);
//			request.setSessionId(sessionId);
//
//			SalesOrderShipmentCreateResponseParam response = stub.salesOrderShipmentCreate(request);
//			String shipmentIncrementId = response.getResult();
//
//			if (!StringUtils.isNullOrBlank2(shipmentIncrementId)) {
//				SalesOrderShipmentAddCommentRequestParam request2 = new SalesOrderShipmentAddCommentRequestParam();
//				request2.setSessionId(sessionId);
//				request2.setShipmentIncrementId(shipmentIncrementId);
//				request2.setComment("sourceOrderId=" + sourceOrderId + ";payNo=" + payNo);
//
//				SalesOrderShipmentAddCommentResponseParam response2 = stub.salesOrderShipmentAddComment(request2);
//				int commentResult = response2.getResult();
//
//				if (commentResult > 0) {
//					SalesOrderShipmentAddTrackRequestParam request3 = new SalesOrderShipmentAddTrackRequestParam();
//					request3.setSessionId(sessionId);
//					request3.setShipmentIncrementId(shipmentIncrementId);
//					request3.setTrackNumber(cainiaoId);
//					request3.setCarrier(CAINIAO_CARRIER);
//					request3.setTitle(CAINIAO_TITLE);
//
//					SalesOrderShipmentAddTrackResponseParam response3 = stub.salesOrderShipmentAddTrack(request3);
//					trackingNumberId = response3.getResult();
//				}
//			} else {
//				logger.error("clientOrderId:" + clientOrderId + " salesOrderShipmentCreate failure!");
//			}
//
			return result;

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);

			isException = true;

			throw ex;
		} finally {
			// end session
			if (!StringUtils.isNullOrBlank2(sessionId)) {
				try {
					logout(sessionId);
				} catch (Exception ex) {
					logger.error(ex.getMessage(), ex);

					if (!isException) {
						throw ex;
					}
				}
			}
		}
	}

	/**
	 * 向wmf追加菜鸟id
	 *
	 * @param cainiaoMap
	 */
	public int addNewOrderTrackWithOneSession(Map<String, String> cainiaoMap) throws Exception {

		int result = 0;

		String clientOrderId = cainiaoMap.get("client_order_id");
		String sourceOrderId = cainiaoMap.get("origin_source_order_id");
		String payNo = cainiaoMap.get("pay_no");
		String cainiaoId = cainiaoMap.get("taobao_logistics_id");

		String sessionId = this.sessionId;
		try {
			if (StringUtils.isNullOrBlank2(sessionId)) {
				// login
				sessionId = login();
				this.sessionId = sessionId;
			}

			logger.info("WMF订单号：" + clientOrderId + " 处理开始");

			SalesOrderAddCommentRequestParam requestParam = new SalesOrderAddCommentRequestParam();
			requestParam.setSessionId(this.sessionId);
			requestParam.setOrderIncrementId(clientOrderId);
			String comment = "cainiaoId=" + cainiaoId + ";sourceOrderId=" + sourceOrderId + ";payNo=" + payNo;
			requestParam.setComment(comment);
			requestParam.setStatus("pending");
			SalesOrderAddCommentResponseParam responseParam;

			try {
				responseParam = stub.salesOrderAddComment(requestParam);
				result = responseParam.getResult();
			} catch (AxisFault ex) {
				int faultType = -1;
				try {
					if (RELOGIN_CODE.equals(ex.getFaultCode().getLocalPart())) {
						faultType = 0;
					}

				} catch (Exception e) {
					// 抛出原始异常
					throw ex;
				}
				// session失效
				if (faultType == 0) {
					sessionId = login();
					this.sessionId = sessionId;
					requestParam.setSessionId(sessionId);

					responseParam = stub.salesOrderAddComment(requestParam);
					result = responseParam.getResult();
				} else {
					throw ex;
				}
			}

//			SalesOrderShipmentCreateRequestParam request = new SalesOrderShipmentCreateRequestParam();
//			request.setOrderIncrementId(clientOrderId);
//
//			OrderItemIdQtyArray itemQty = new OrderItemIdQtyArray();
//			request.setItemsQty(itemQty);
//			request.setSessionId(sessionId);
//
//			SalesOrderShipmentCreateResponseParam response;
//			String shipmentIncrementId = "";
//			try {
//				response = stub.salesOrderShipmentCreate(request);
//				shipmentIncrementId = response.getResult();
//			} catch (AxisFault ex) {
//				int faultType = -1;
//				try {
//					if (RELOGIN_CODE.equals(ex.getFaultCode().getLocalPart())) {
//						faultType = 0;
//					} else if ("102".equals(ex.getFaultCode().getLocalPart())) {
//						faultType = 1;
//					}
//
//				} catch (Exception e) {
//					// 抛出原始异常
//					throw ex;
//				}
//				// session失效
//				if (faultType == 0) {
//					sessionId = login();
//					this.sessionId = sessionId;
//					request.setSessionId(sessionId);
//
//					response = stub.salesOrderShipmentCreate(request);
//				} else if (faultType == 1) {



//					SalesOrderInfoRequestParam requestParam = new SalesOrderInfoRequestParam();
//					requestParam.setSessionId(this.sessionId);
//					requestParam.setOrderIncrementId(clientOrderId);
//					SalesOrderInfoResponseParam responseParam = stub.salesOrderInfo(requestParam);
//					SalesOrderEntity salesOrderEntity = responseParam.getResult();
//
//					String shipmentId = salesOrderEntity.getIncrement_id();
//					System.out.println("");
//
//					SalesOrderShipmentListRequestParam requestParam = new SalesOrderShipmentListRequestParam();
//					requestParam.setSessionId(this.sessionId);
//
//					Filters filters = new Filters();
//					ComplexFilterArray filterArray = new ComplexFilterArray();
//					ComplexFilter complexFilter = new ComplexFilter();
//					complexFilter.setKey("increment_id");
//					AssociativeEntity associativeEntity = new AssociativeEntity();
//					associativeEntity.setKey("eq");
//					associativeEntity.setValue(clientOrderId);
//					complexFilter.setValue(associativeEntity);
//					filterArray.addComplexObjectArray(complexFilter);
//					filters.setComplex_filter(filterArray);
//					requestParam.setFilters(filters);
//
//					SalesOrderShipmentListResponseParam responseParam = stub.salesOrderShipmentList(requestParam);
//					SalesOrderShipmentEntityArray result = responseParam.getResult();
//					if (result != null) {
//						SalesOrderShipmentEntity[] complexObjectArray = result.getComplexObjectArray();
//						if (complexObjectArray != null && complexObjectArray.length >= 1) {
//							shipmentIncrementId = complexObjectArray[0].getShipment_id();
//						}
//					}
//				} else {
//					throw ex;
//				}
//			} catch (Exception ex) {
//				throw ex;
//			}

//			if (!StringUtils.isNullOrBlank2(shipmentIncrementId)) {
//				logger.info("shipmentIncrementId 创建成功：" + shipmentIncrementId);

//				SalesOrderShipmentAddCommentRequestParam request2 = new SalesOrderShipmentAddCommentRequestParam();
//				request2.setSessionId(sessionId);
//				request2.setShipmentIncrementId(shipmentIncrementId);
//				request2.setIncludeInEmail("order.test@voyageone.com");
//				request2.setEmail("order.test@voyageone.com");
//				request2.setComment("sourceOrderId=" + sourceOrderId + ";payNo=" + payNo);
//
//				SalesOrderShipmentAddCommentResponseParam response2 = stub.salesOrderShipmentAddComment(request2);
//				int commentResult = response2.getResult();
//
//				if (commentResult > 0) {
//					SalesOrderShipmentAddTrackRequestParam request3 = new SalesOrderShipmentAddTrackRequestParam();
//					request3.setSessionId(sessionId);
//					request3.setShipmentIncrementId(shipmentIncrementId);
//					request3.setTrackNumber(cainiaoId);
//					request3.setCarrier("ups");
//					request3.setTitle(CAINIAO_TITLE);
//
//					SalesOrderShipmentAddTrackResponseParam response3 = stub.salesOrderShipmentAddTrack(request3);
//					trackingNumberId = response3.getResult();
//				}
//			} else {
//				logger.error("clientOrderId:" + clientOrderId + " salesOrderShipmentCreate failure!");
//			}

			return result;

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);

			throw ex;
		}
	}

	/**
	 * 计算订单级的差价分配到每个物品上的价格差
	 *
	 * @param orderPriceDiff
	 * @param finalGrandTotal
	 * @param detailList
	 * @return
	 */
	private void getPriceDiffPer(double orderPriceDiff, double finalGrandTotal, List<OrderDetailBean> detailList) {
		// 物品实际总价（除去订单级折扣）
		double realPriceTotal = finalGrandTotal - orderPriceDiff;

		int detailSize = detailList.size();
		if (detailSize == 1) {
			OrderDetailBean detail = detailList.get(0);

			double detailPrice = detail.getPrice() + detail.getDiscount();
			detail.setRealPrice(detailPrice + orderPriceDiff);

		} else {
			// 已分配的订单折扣额
			double preDiscount = 0;
			// 按每个物品实际价格占物品总价百分比算出订单级折扣分配到物品上的部分
			for (int i = 0; i < detailSize; i++) {
				OrderDetailBean detail = detailList.get(i);

				double detailPrice = detail.getPrice() + detail.getDiscount();
				BigDecimal detailBigDecimal = new BigDecimal(detailPrice);

				if (i < detailSize - 1) {
					// 每个物品实际价格占物品总价百分比
					double rate = detailBigDecimal.divide(new BigDecimal(realPriceTotal), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
					// 订单级折扣分配到物品上的部分
					double detailRateDiscount = orderPriceDiff * rate;
					preDiscount += detailRateDiscount;

					detailPrice = detailPrice + detailRateDiscount;

					detail.setRealPrice(detailPrice);
				} else {
					double finalRealPrice = detailPrice + (orderPriceDiff - preDiscount);
					detail.setRealPrice(finalRealPrice);
				}
			}
		}
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		MagentoApiServiceImpl service = new MagentoApiServiceImpl();
		service.setOrderChannelId("014");
//		ShoppingCartPaymentListResponseParam responseParam = service.getShoppingCartPaymentList();

//		ShoppingCartShippingListResponseParam responseParam1 = service.getShoppingCartShippingList();
//		List<String> skuList = new ArrayList<String>();
//		skuList.add("WTKT34248-468-XS");
//		service.inventoryStockItemList(skuList);

		for (int i = 0; i < 2; i++) {
			OrderDataBean order = new OrderDataBean();
			order.setBillingAddress("天园街道科韵路16号广州信息港方圆E时光17楼");
			order.setBillingCity("广州市");
			order.setBillingCountry("CN");
			order.setBillingName("crimson_cc123456");
			order.setBillingPostcode("510000");
			order.setBillingState("广东省");
			order.setBillingTelephone("18027188866");
			order.setPaymentMethod("cashondelivery");
			order.setShippingMethod("freeshipping_freeshipping");
			order.setShippingAddress("天园街道科韵路16号广州信息港方圆E时光17楼");
			order.setShippingCity("广州市");
			order.setShippingCountry("CN");
			order.setShippingName("苏钺斐");
			order.setShippingPostcode("510000");
			order.setShippingState("广东省");
			order.setShippingTelephone("18027188866");
			List<OrderDetailBean> detailBeans = new ArrayList<OrderDetailBean>();
			OrderDetailBean detailBean = new OrderDetailBean();
			detailBean.setQty(2);
			detailBean.setSku("WTKT34248-468-XS");
			detailBeans.add(detailBean);
			order.setOrderDetails(detailBeans);

			String orderNumber = service.pushOrderWithOneSession(order);
			System.out.println(orderNumber);
		}


//		List<String> skuList = new ArrayList<String>();
//		skuList.add("0892179531");
//		service.inventoryStockItemList(skuList);
//		OrderDataBean order = new OrderDataBean();
//		String orderNumber = service.pushOrder();
//		order.setShippingMethod("freeshipping_freeshipping");
//		order.setPaymentMethod("cashondelivery");
//		service.getSalesOrderInfo("400000121");
		
//		System.out.println(orderNumber);
//		List<String> skuList= new ArrayList<String>();
//		skuList.add("wfwd32469-032-L");
//		skuList.add("WFWb32587-495-27");
//		skuList.add("WFWB32587-495-26");
//		skuList.add("WTKT34248-468-XS");
//		skuList.add("WTKT34248-468-S");
//		skuList.add("WTKT34247-160-L");
//		skuList.add("WTKT34248-419-L");
//		skuList.add("WTKT34244-519-XS");
//		skuList.add("WTKT34244-519-S");
//		skuList.add("WTKT31342-160-S");
//		skuList.add("WTKT31342-160-M");
//		skuList.add("WTKT31340-119-M");
//		skuList.add("WTKT31337-009-M");
//		skuList.add("WTKT31330-501-L");
//		skuList.add("WTKT31321-689-S");
//		skuList.add("WTKT25268-419-S");
//		List<InventoryStockItemBean> inventoryList = service.inventoryStockItemList(skuList);
//		System.out.print(inventoryList);
	}

}
