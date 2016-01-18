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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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
//		String userName = ThirdPartyConfigs.getVal1(this.orderChannelId, "userName");
//		// magento api调用 密钥
//		String apiKey = ThirdPartyConfigs.getVal1(this.orderChannelId, "apiKey");

			loginParam = new LoginParam();
//		loginParam.setUsername(userName);
//		loginParam.setApiKey(apiKey);
			loginParam.setUsername("juicy_couture");
			loginParam.setApiKey("abc.123");

//		loginParam.setUsername("VOYAGEONE_API_USER");
//		loginParam.setApiKey("hjA=fs2H0n+%PFd,b4wB");

//		String customerId = ThirdPartyConfigs.getVal1(this.orderChannelId, "customerId");
//		String customerMode = ThirdPartyConfigs.getVal1(this.orderChannelId, "customerMode");
//		String storeId = ThirdPartyConfigs.getVal1(this.orderChannelId, "storeId");
//		String webSiteId = ThirdPartyConfigs.getVal1(this.orderChannelId, "webSiteId");
//		String storeName = ThirdPartyConfigs.getVal1(this.orderChannelId, "storeName");

			String customerId = "20";
			String customerMode = "customer";
//		String customerMode = "guest";
			String storeId = "1";
			String webSiteId = "1";
			String storeName = "juicy_cn";

			customer = new CustomerBean();
			customer.setCustomerId(Integer.valueOf(customerId));
			customer.setMode(customerMode);
			customer.setStoreId(Integer.valueOf(storeId));
			customer.setWebsiteId(Integer.valueOf(webSiteId));
			customer.setStore(storeName);
		}
	}
	
	/**
	 * Login to Magento
	 *
	 * @return
	 */
	private String login() throws Exception {
		// magento api调用 URL
//		String url = ThirdPartyConfigs.getVal1(this.orderChannelId, "url");
		String url = "https://api.juicycouture.asia/api/v2_soap";
		stub = new MagentoServiceStub(url);
//		stub = new MagentoServiceStub("http://www.wmf.com/api/v2_soap");
		System.setProperty("javax.net.ssl.trustStore", "D:/tmp/trustStore");
		
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
				request.setStore(customer.getStore());
				
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
						customerEntity.setStore_id(customer.getStoreId());
						customerEntity.setWebsite_id(customer.getWebsiteId());
						customerEntity.setCustomer_id(customer.getCustomerId());
						
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
							productAddRequest.setStore(customer.getStore());
							
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
							
								// 购物车中顾客账单地址和收货地址设置参数准备
								ShoppingCartCustomerAddressesRequestParam addressRequest = new ShoppingCartCustomerAddressesRequestParam();
								addressRequest.setQuoteId(shoppingCartId);
								addressRequest.setSessionId(sessionId);
								addressRequest.setStore(customer.getStore());
								ShoppingCartCustomerAddressEntityArray addressEntity = new ShoppingCartCustomerAddressEntityArray();
								
								// 账单人信息
								ShoppingCartCustomerAddressEntity billingAddress = new ShoppingCartCustomerAddressEntity();
								billingAddress.setMode("billing");
								billingAddress.setFirstname(order.getBillingName());
								billingAddress.setCity(order.getBillingCity());
								billingAddress.setRegion(order.getBillingState());
								billingAddress.setStreet(order.getBillingAddress());
								billingAddress.setTelephone(order.getBillingTelephone());
								billingAddress.setPostcode(order.getBillingPostcode());
								billingAddress.setCountry_id(order.getBillingCountry());
								
								// 收货人信息
								ShoppingCartCustomerAddressEntity shippingAddress = new ShoppingCartCustomerAddressEntity();
								shippingAddress.setMode("shipping");
								shippingAddress.setFirstname(order.getShippingName());
								shippingAddress.setCity(order.getShippingCity());
								shippingAddress.setRegion(order.getShippingState());
								shippingAddress.setStreet(order.getShippingAddress());
								shippingAddress.setTelephone(order.getShippingTelephone());
								shippingAddress.setPostcode(order.getShippingPostcode());
								shippingAddress.setCountry_id(order.getShippingCountry());
								
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
									shippingMethodRequest.setShippingMethod(order.getShippingMethod());
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
										paymentMethodRequest.setStore(customer.getStore());
										ShoppingCartPaymentMethodEntity paymentMethodEntity = new ShoppingCartPaymentMethodEntity();
										paymentMethodEntity.setMethod(order.getPaymentMethod());
										paymentMethodRequest.setPaymentData(paymentMethodEntity);
										
										// 支付方式设定
										ShoppingCartPaymentMethodResponseParam paymentMethodResponse = stub.shoppingCartPaymentMethod(paymentMethodRequest);
										boolean isPaymentMethodSuccess = paymentMethodResponse.getResult();
										
										// 支付方式设定成功
										if (isPaymentMethodSuccess) {
											
											// 下订单参数准备
											ShoppingCartOrderRequestParam shoppingCartOrderRequestParam = new ShoppingCartOrderRequestParam();
											shoppingCartOrderRequestParam.setStore(customer.getStore());
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
				request.setStore(customer.getStore());

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

						// 顾客对象
						ShoppingCartCustomerEntity customerEntity = new ShoppingCartCustomerEntity();
						customerEntity.setMode(customer.getMode());
						customerEntity.setStore_id(customer.getStoreId());
						customerEntity.setWebsite_id(customer.getWebsiteId());
						customerEntity.setCustomer_id(customer.getCustomerId());

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
							productAddRequest.setStore(customer.getStore());

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

								// 购物车中顾客账单地址和收货地址设置参数准备
								ShoppingCartCustomerAddressesRequestParam addressRequest = new ShoppingCartCustomerAddressesRequestParam();
								addressRequest.setQuoteId(shoppingCartId);
								addressRequest.setSessionId(sessionId);
								addressRequest.setStore(customer.getStore());
								ShoppingCartCustomerAddressEntityArray addressEntity = new ShoppingCartCustomerAddressEntityArray();

								// 账单人信息
								ShoppingCartCustomerAddressEntity billingAddress = new ShoppingCartCustomerAddressEntity();
								billingAddress.setMode("billing");
								billingAddress.setFirstname(order.getBillingName());
								billingAddress.setCity(order.getBillingCity());
								billingAddress.setRegion(order.getBillingState());
								billingAddress.setStreet(order.getBillingAddress());
								billingAddress.setTelephone(order.getBillingTelephone());
								billingAddress.setPostcode(order.getBillingPostcode());
								billingAddress.setCountry_id(order.getBillingCountry());

								// 收货人信息
								ShoppingCartCustomerAddressEntity shippingAddress = new ShoppingCartCustomerAddressEntity();
								shippingAddress.setMode("shipping");
								shippingAddress.setFirstname(order.getShippingName());
								shippingAddress.setCity(order.getShippingCity());
								shippingAddress.setRegion(order.getShippingState());
								shippingAddress.setStreet(order.getShippingAddress());
								shippingAddress.setTelephone(order.getShippingTelephone());
								shippingAddress.setPostcode(order.getShippingPostcode());
								shippingAddress.setCountry_id(order.getShippingCountry());

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
									shippingMethodRequest.setShippingMethod(order.getShippingMethod());
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
										paymentMethodRequest.setStore(customer.getStore());
										ShoppingCartPaymentMethodEntity paymentMethodEntity = new ShoppingCartPaymentMethodEntity();
										paymentMethodEntity.setMethod(order.getPaymentMethod());
										paymentMethodRequest.setPaymentData(paymentMethodEntity);

										// 支付方式设定
										ShoppingCartPaymentMethodResponseParam paymentMethodResponse = stub.shoppingCartPaymentMethod(paymentMethodRequest);
										boolean isPaymentMethodSuccess = paymentMethodResponse.getResult();

										// 支付方式设定成功
										if (isPaymentMethodSuccess) {

											// 下订单参数准备
											ShoppingCartOrderRequestParam shoppingCartOrderRequestParam = new ShoppingCartOrderRequestParam();
											shoppingCartOrderRequestParam.setStore(customer.getStore());
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
	 * 获得支付方式列表
	 *
	 * @return
	 * @throws Exception
	 */
	public ShoppingCartPaymentListResponseParam getShoppingCartPaymentList() throws Exception {
		// 获得magento订单信息时是否发生异常
		boolean isException = false;

		String sessionId = "";
		try {
			// login
			sessionId = login();

			// 创建购物车参数准备
			ShoppingCartCreateRequestParam request = new ShoppingCartCreateRequestParam();
			request.setSessionId(sessionId);
			request.setStore(customer.getStore());

			// 创建购物车
			ShoppingCartCreateResponseParam cartCreateResponse = stub.shoppingCartCreate(request);
			// 新创建购物车ID
			int shoppingCartId = cartCreateResponse.getResult();

			// 购物车创建成功
			ShoppingCartPaymentListRequestParam request1 = new ShoppingCartPaymentListRequestParam();
			request1.setSessionId(sessionId);
			request1.setQuoteId(shoppingCartId);
			request1.setStore(customer.getStore());

			ShoppingCartPaymentListResponseParam response = stub.shoppingCartPaymentList(request1);

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
	 * 获得发货方式列表
	 *
	 * @return
	 * @throws Exception
	 */
	public ShoppingCartShippingListResponseParam getShoppingCartShippingList() throws Exception {
		// 获得magento订单信息时是否发生异常
		boolean isException = false;

		String sessionId = "";
		try {
			// login
			sessionId = login();

			ShoppingCartCreateRequestParam request = new ShoppingCartCreateRequestParam();
			request.setSessionId(sessionId);
			request.setStore(customer.getStore());
			// 创建购物车
			ShoppingCartCreateResponseParam cartCreateResponse = stub.shoppingCartCreate(request);
			// 新创建购物车ID
			int shoppingCartId = cartCreateResponse.getResult();

			// 顾客对象
			ShoppingCartCustomerEntity customerEntity = new ShoppingCartCustomerEntity();
			customerEntity.setMode(customer.getMode());
			customerEntity.setStore_id(customer.getStoreId());
			customerEntity.setWebsite_id(customer.getWebsiteId());
			customerEntity.setCustomer_id(customer.getCustomerId());

			// 购物车中顾客对象设置参数准备
			ShoppingCartCustomerSetRequestParam customer = new ShoppingCartCustomerSetRequestParam();
			customer.setSessionId(sessionId);
			customer.setCustomerData(customerEntity);
			customer.setQuoteId(shoppingCartId);
			// 购物车中顾客对象设置
			ShoppingCartCustomerSetResponseParam responseCustomer = stub.shoppingCartCustomerSet(customer);

			ShoppingCartShippingListRequestParam request1 = new ShoppingCartShippingListRequestParam();
			request1.setStore(customer.getStore());
			request1.setSessionId(sessionId);
			request1.setQuoteId(shoppingCartId);

			ShoppingCartShippingListResponseParam response = stub.shoppingCartShippingList(request1);

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
