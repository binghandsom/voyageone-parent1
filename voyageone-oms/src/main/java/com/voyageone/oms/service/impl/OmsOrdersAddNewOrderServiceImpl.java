package com.voyageone.oms.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.voyageone.common.util.StringUtils;
import com.voyageone.common.Constants;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.oms.OmsCodeConstants;
import com.voyageone.oms.OmsCodeConstants.OrderKind;
import com.voyageone.oms.OmsConstants;
import com.voyageone.oms.OmsMessageConstants;
import com.voyageone.oms.dao.ChannelDao;
import com.voyageone.oms.dao.CustomerDao;
import com.voyageone.oms.dao.NotesDao;
import com.voyageone.oms.dao.OrderDao;
import com.voyageone.oms.dao.OrderDetailDao;
import com.voyageone.oms.dao.TransactionsDao;
import com.voyageone.oms.formbean.InFormAddNewOrderCustomer;
import com.voyageone.oms.formbean.InFormAddNewOrderOrder;
import com.voyageone.oms.formbean.OutFormAddNewOrderCustomer;
import com.voyageone.oms.formbean.OutFormAddNewOrderOrderHistory;
import com.voyageone.oms.formbean.OutFormOrderdetailOrders;
import com.voyageone.oms.modelbean.CustomerBean;
import com.voyageone.oms.modelbean.NotesBean;
import com.voyageone.oms.modelbean.OrderDetailsBean;
import com.voyageone.oms.modelbean.OrderPrice;
import com.voyageone.oms.modelbean.OrdersBean;
import com.voyageone.oms.service.OmsOrdersAddNewOrderService;

@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public class OmsOrdersAddNewOrderServiceImpl implements OmsOrdersAddNewOrderService {
	private static Log logger = LogFactory.getLog(OmsOrdersAddNewOrderServiceImpl.class);
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private OrderDetailDao orderDetailDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private NotesDao notesDao;
	
	@Autowired
	@Qualifier("OmsChannelDao")
	private ChannelDao omsChannelDao;
	
	@Autowired
	private TransactionsDao transactionsDao;
	
	@Autowired
	private OmsOrderDetailsSearchServiceImpl omsOrderDetailsSearchService;
	
	@Autowired
	private DataSourceTransactionManager transactionManager;	
	DefaultTransactionDefinition def =new DefaultTransactionDefinition();
	
	/**
	 * 订单历史信息取得
	 * 
	 * @param orderNumber 订单号
	 * @return
	 */
	@Override
	public List<OutFormAddNewOrderOrderHistory> getOrdersHistoryInfo(String orderNumber) {		
		
		List<OutFormAddNewOrderOrderHistory> ret = new ArrayList<OutFormAddNewOrderOrderHistory>();
		
		// 订单历史信息取得
		List<OrdersBean> orderHistoryList = orderDetailDao.getOrderHistoryInfo(orderNumber);
		
		// 输出订单历史信息设定
		for (int i = 0; i < orderHistoryList.size(); i++) {
			OrdersBean ordersInfo = orderHistoryList.get(i);			
			 
			OutFormAddNewOrderOrderHistory outFormOrderHistory = new OutFormAddNewOrderOrderHistory();
			outFormOrderHistory.setOrderNumber(ordersInfo.getOrderNumber());
			outFormOrderHistory.setOrderDate(StringUtils.getDate(ordersInfo.getOrderDate()));
			outFormOrderHistory.setName(ordersInfo.getShipName());
			outFormOrderHistory.setCompany(ordersInfo.getShipCompany());
			outFormOrderHistory.setEmail(ordersInfo.getShipEmail());			
			outFormOrderHistory.setAddress(ordersInfo.getShipAddress());
			outFormOrderHistory.setAddress2(ordersInfo.getShipAddress2());
			outFormOrderHistory.setCity(ordersInfo.getShipCity());
			outFormOrderHistory.setState(ordersInfo.getShipState());
			outFormOrderHistory.setZip(ordersInfo.getShipZip());
			outFormOrderHistory.setCountry(ordersInfo.getShipCountry());
			outFormOrderHistory.setPhone(ordersInfo.getShipPhone());
			outFormOrderHistory.setEmail(ordersInfo.getShipEmail());
			
			ret.add(outFormOrderHistory);
		}
		
		return ret;
	}
	
	/**
	 * 获得当前订单信息
	 * 
	 * @return
	 */
	@Override
	public OutFormAddNewOrderOrderHistory getCurOrderShipInfo(List<OutFormAddNewOrderOrderHistory> orderHistoryList, String orderNumber) {
		OutFormAddNewOrderOrderHistory ret = null;
		
		if (orderHistoryList != null && orderHistoryList.size() > 0) {
			for (int i = 0; i < orderHistoryList.size(); i++) {
				OutFormAddNewOrderOrderHistory outFormAddNewOrderOrderHistory = orderHistoryList.get(i);
				
				if (outFormAddNewOrderOrderHistory.getOrderNumber().equals(orderNumber)) {
					ret = outFormAddNewOrderOrderHistory;
					
					break;
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * 获得客户信息
	 * 
	 * @return
	 */
	@Override
	public OutFormAddNewOrderCustomer getCustomerInfo(String orderNumber) {
		
		OutFormAddNewOrderCustomer customerInfo = customerDao.getCustomerInfo(orderNumber);
		
		return customerInfo;
	}
	
	/**
	 * 订单信息保存
	 * 
	 * @return
	 */
	@Override
	public boolean doSaveOrderInfo(OrdersBean orderInfo) {
		boolean ret = false;
		
		return ret;
	}
	
	/**
	 * 订单明细信息保存
	 * 
	 * @return
	 */
	@Override
	public boolean doSaveOrderDetailsInfo(List<OrderDetailsBean> orderDetailsList) {
		boolean ret = true;		
		
		return ret;
	}
	
	/**
	 * 订单信息保存（含明细）
	 * 
	 * @return
	 */
	@Override
	public void doSaveOrderAndDetailsInfo(InFormAddNewOrderOrder inFormAddNewOrderOrder, AjaxResponseBean result, UserSessionBean user) {
		boolean ret = true;
		
		// 原始订单信息取得
		OutFormOrderdetailOrders ordersInfo = orderDetailDao.getOrdersInfoBySourceOrderId(inFormAddNewOrderOrder.getOrderInfo().getSourceOrderId());
		
		// 原订单检查
		if (!StringUtils.isEmpty(inFormAddNewOrderOrder.getOrderInfo().getSourceOrderId())) {
//			// 原始订单信息取得
//			OutFormOrderdetailOrders ordersInfo = orderDetailDao.getOrdersInfoBySourceOrderId(inFormAddNewOrderOrder.getOrderInfo().getSourceOrderId());
			
			// 	子订单的场合，原定单检查
			if (!OmsCodeConstants.OrderKind.ORIGINAL_ORDER.equals(inFormAddNewOrderOrder.getOrderInfo().getOrderKind())) {
				if (ordersInfo == null) {
					// 异常返回（插入异常）
					result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210018,
							MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					
					return;
				}
			}
			
			// 拆分订单 && 原始订单未Cancel 的场合
			if (OmsCodeConstants.OrderKind.SPLIT_ORDER.equals(inFormAddNewOrderOrder.getOrderInfo().getOrderKind()) &&
					!OmsCodeConstants.OrderStatus.CANCELED.equals(ordersInfo.getOrderStatus())) {
				// 异常返回（插入异常）
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210017,
						MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
				
				return;
			}			
		}
			
		// 客户信息保存
		ret = saveCustomer(inFormAddNewOrderOrder.getOrderInfo());

		// 订单信息保存（含明细）
		if (ret) {
			ret = doSaveOrderAndDetailsInfoSub(inFormAddNewOrderOrder, ordersInfo, user);
		}		
		
		if (ret) {
			
			// 设置返回结果
			Map<String, Object> ordersListMap = new HashMap<String, Object>();
			ordersListMap.put("sourceOrderId", inFormAddNewOrderOrder.getOrderInfo().getSourceOrderId());
			result.setResultInfo(ordersListMap);
			
			//	正常
			result.setResult(true);
		} else {
			// 异常返回（插入异常）
			result.setResult(false, MessageConstants.MESSAGE_CODE_200003,
					MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}
	}
	
//	@Transactional(rollbackFor={Exception.class})
	private boolean doSaveOrderAndDetailsInfoSub(InFormAddNewOrderOrder inFormAddNewOrderOrder, OutFormOrderdetailOrders ordersInfo, UserSessionBean user){
		boolean ret = true;
		
		logger.info("doSaveOrderAndDetailsInfoSub start");

		// 扩展表追加判定
		boolean isInsertExtTable = isInsertIntoExtTable(inFormAddNewOrderOrder.getOrderInfo().getOrderChannelId());
		logger.info("doSaveOrderAndDetailsInfoSub isInsertIntoExtTable = " + isInsertExtTable);

		//	未设定字段预处理
		logger.info("	setOrderInfoFields");
		setOrderInfoFields(inFormAddNewOrderOrder, ordersInfo);
		
		//	画面输入新追加订单信息
		OrdersBean newOrderInfo = inFormAddNewOrderOrder.getOrderInfo();
		
		
		//	订单信息保存
		TransactionStatus status=transactionManager.getTransaction(def);
		
		try {
			
			// 折扣在处理（订单折扣（合计）= 物品折扣	+ 订单折扣 ）
			logger.info("	setOrderInfoDiscount");
			setOrderInfoDiscount(inFormAddNewOrderOrder);
			
			// 订单信息保存
			logger.info("	saveOrderInfo");
			ret = saveOrderInfo(newOrderInfo, user);
			
			// 订单group_orders保存
			if (ret) {
				// 原始订单的场合
				if (OrderKind.ORIGINAL_ORDER.equals(newOrderInfo.getOrderKind())) {
					logger.info("	saveGroupOrdersInfo");
					ret = saveGroupOrdersInfo(newOrderInfo, user);
					
				// 其他订单的场合
				} else {
					OrderPrice mainOrderPrice = getMainOrderPrice(newOrderInfo);
					
					// 一组订单价格更新
					if (ret) {
						logger.info("	saveGroupOrderPrice");
						ret = omsOrderDetailsSearchService.saveGroupOrderPrice(mainOrderPrice, ordersInfo, user);
					}
				}
			}

			// oms_bt_ext_orders 保存
			if (ret) {
				// 扩展表需要追加的场合
				if (isInsertExtTable) {
					logger.info("	insertExtOrdersInfo");
					ret = insertExtOrdersInfo(newOrderInfo, user);
				}
			}
			
			if (ret) {
				// Notes 追加
				String noteId = addNotes(newOrderInfo.getSourceOrderId(),
											OmsConstants.NotesType.SYSTEM,
											newOrderInfo.getOrderNumber(),
											OmsConstants.NEW_ORDER_CREATED,
											user.getUserName(),
											user.getUserName());
				
				if (StringUtils.isEmpty(noteId)) {
					ret = false;
				} else {
					if (ret) {
						// 订单明细保存（含Transaction 信息）
						logger.info("	saveOrderDetailsInfo");
						ret = saveOrderDetailsInfo(newOrderInfo, inFormAddNewOrderOrder.getOrderDetailsList(), noteId, isInsertExtTable, user);
					}
				}
			}
			
			if (ret) {
				logger.info("doSaveOrderAndDetailsInfoSub success");
				
				transactionManager.commit(status);
			} else {
				logger.info("doSaveOrderAndDetailsInfoSub error");
				
				transactionManager.rollback(status);
			}
			
		}
		catch (DuplicateKeyException dbEx){
			
			logger.error("doSaveOrderAndDetailsInfoSub", dbEx);
			
			transactionManager.rollback(status);
			
			ret = false;
			
		} catch (Exception e) {
			logger.error("doSaveOrderAndDetailsInfoSub", e);
			
			transactionManager.rollback(status);
			
			ret = false;
		}
		
		return ret;
	}

	/**
	 * 扩展表是否追加判定
	 *
	 * @param orderChannelId 订单渠道
	 *
	 * @return
	 */
	private boolean isInsertIntoExtTable(String orderChannelId) {
		boolean ret = false;

		String extOrderInsert = ChannelConfigs.getVal1(orderChannelId, ChannelConfigEnums.Name.ext_order_insert);
		// 扩展表需要追加的场合
		if (!StringUtils.isEmpty(extOrderInsert) && "1".equals(extOrderInsert)) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 订单折扣信息处理
	 * 
	 * @param inFormAddNewOrderOrder 画面传入信息
	 * 
	 * @return
	 */
	private void setOrderInfoDiscount(InFormAddNewOrderOrder inFormAddNewOrderOrder) {
		//	画面输入订单信息
		OrdersBean newOrderInfo = inFormAddNewOrderOrder.getOrderInfo();
		//	画面输入订单明细信息		
		List<OrderDetailsBean> newOrderDetailsList = inFormAddNewOrderOrder.getOrderDetailsList();
		
		//	订单折扣缓存
		newOrderInfo.setOrderDiscount(newOrderInfo.getDiscount());
		
		//	物品折扣
		float detailsDiscount = 0f;
		
		//	物品折扣合计处理
		for (int i = 0; i < newOrderDetailsList.size(); i++) {
			OrderDetailsBean newOrderDetailInfo = newOrderDetailsList.get(i);
			
			// 物品折扣有的场合
			if (!"0".equals(newOrderDetailInfo.getDiscount())) {
				float itemDiscount = mult2Digits(Float.valueOf(newOrderDetailInfo.getQuantityOrdered()), Float.valueOf(newOrderDetailInfo.getDiscount()));
				detailsDiscount = add2Digits(detailsDiscount, itemDiscount);
			}
		}
		
		//	订单折扣（合计）= 物品折扣  + 订单折扣
		if (detailsDiscount != 0) {
			
			String orderDiscount = newOrderInfo.getDiscount();
			
			//	订单折扣不存在的场合
			if (StringUtils.isEmpty(orderDiscount)) {
				
				//	订单折扣
				newOrderInfo.setDiscount(String.valueOf(detailsDiscount));
				//	修正后订单折扣
				newOrderInfo.setRevisedDiscount(String.valueOf(detailsDiscount));
				
			//	订单折扣存在的场合
			} else {
				orderDiscount = String.valueOf(add2Digits(Float.valueOf(orderDiscount), detailsDiscount));
				
				//	订单折扣
				newOrderInfo.setDiscount(orderDiscount);
				//	修正后订单折扣
				newOrderInfo.setRevisedDiscount(orderDiscount);
			}
		}
	}
	
	/**
	 * 主订单价格取得
	 * 
	 * @param orderInfo 订单号
	 *
	 * @return
	 */
	private OrderPrice getMainOrderPrice(OrdersBean orderInfo) {
		// 一组订单金额检索
		OutFormOrderdetailOrders mainOrdersInfo = orderDetailDao.getGroupOrdersInfo(orderInfo.getSourceOrderId());
		
		// 一组订单价格取得
		OrderPrice mainOrderPrice = getOrderPrice(mainOrdersInfo);
		
		// final_grand_total
		float finalGrandTotal = add2Digits(mainOrderPrice.getFinalGrandTotal(), Float.valueOf(orderInfo.getFinalGrandTotal()));
		mainOrderPrice.setFinalGrandTotal(finalGrandTotal);
		
		// final_shipping_total
		float finalShippingTotal = add2Digits(mainOrderPrice.getFinalShippingTotal(), Float.valueOf(orderInfo.getFinalShippingTotal()));
		mainOrderPrice.setFinalShippingTotal(finalShippingTotal);
		
		// revised_surcharge
		float revisedSurcharge = add2Digits(mainOrderPrice.getRevisedSurcharge(), Float.valueOf(orderInfo.getRevisedSurcharge()));
		mainOrderPrice.setRevisedSurcharge(revisedSurcharge);
		
		// revised_discount
		float revisedDiscount = add2Digits(mainOrderPrice.getRevisedDiscount(), Float.valueOf(orderInfo.getRevisedDiscount()));
		mainOrderPrice.setRevisedDiscount(revisedDiscount);
		
		// final_product_total
		float finalProductTotal = add2Digits(mainOrderPrice.getFinalProductTotal(), Float.valueOf(orderInfo.getFinalProductTotal()));
		mainOrderPrice.setFinalProductTotal(finalProductTotal);
		
		return mainOrderPrice;
	}
	
	/**
	 * 订单金额取得
	 * 
	 * @param ordersInfo 订单信息
	 * 
	 * @return 订单价格
	 * 
	 */
	private OrderPrice getOrderPrice(OutFormOrderdetailOrders ordersInfo) {
		OrderPrice orderPrice = new OrderPrice();
		
		// source_order_id
		orderPrice.setSourceOrderId(ordersInfo.getSourceOrderId());

		// order number
		orderPrice.setOrderNumber(ordersInfo.getOrderNumber());

		// revised product Total
		orderPrice.setFinalProductTotal(Float.valueOf(ordersInfo.getFinalProductTotal()));
		
		// revised surcharge
		orderPrice.setRevisedSurcharge(Float.valueOf(ordersInfo.getRevisedSurcharge()));
		
		// revised discount
		orderPrice.setRevisedDiscount(Float.valueOf(ordersInfo.getRevisedDiscount()));
		
		// revised shipping
		orderPrice.setFinalShippingTotal(Float.valueOf(ordersInfo.getFinalShippingTotal()));
		
		// revised grand total
		orderPrice.setFinalGrandTotal(Float.valueOf(ordersInfo.getFinalGrandTotal()));
		
		if (ordersInfo.getExpected() != null) {
			orderPrice.setExpected(Float.valueOf(ordersInfo.getExpected()));
		}
		
		return orderPrice;
	}

//	omsOrderDetailsSearchService 调用变更	
//	/**
//	 * 一组订单价格保存
//	 * 
//	 * @param orderPrice 订单价格
//	 * @param user 当前用户
//	 * 
//	 * @return
//	 */
//	private boolean saveGroupOrderPrice(OrderPrice orderPrice, OutFormOrderdetailOrders ordersInfo, UserSessionBean user) {
//		
//		boolean ret = true;
//		
//		// 一组订单
//		//		订单价格取得
//		OrdersBean mainOrdersInfo = setOrderPrice(orderPrice);
//		
//		// 		modifier
//		mainOrdersInfo.setModifier(user.getUserName());
//		
//		ret = orderDetailDao.updateGroupOrdersInfo(mainOrdersInfo);
//		
//		if (ret) {
//			//	未锁定的场合
//			if (OmsConstants.LockShip.No.equals(ordersInfo.getLockShip())) {
//				//	expected < final_grand_total
//				if (orderPrice.getExpected() < orderPrice.getFinalGrandTotal()) {
//					ret = omsOrderDetailsSearchService.changeLockStatusSub(orderPrice.getSourceOrderId(), true, user);
//				}
//			}
//		}
//		
//		return ret;
//	}
	
	/**
	 * Group order info 保存
	 * 
	 * @param orderInfo 订单号
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveGroupOrdersInfo(OrdersBean orderInfo, UserSessionBean user) {
		boolean ret = true;
		
		// expected 设定
		orderInfo.setExpected(orderInfo.getFinalGrandTotal());
		//	payment 设定（初期没有 payment 信息）
		orderInfo.setPaymentTotal("0");
		
		//	creater
		orderInfo.setCreater(user.getUserName());
		
		//	modifier
		orderInfo.setModifier(user.getUserName());
		
		ret = orderDetailDao.insertGroupOrdersInfo(orderInfo);
		
		return ret;
	}

	/**
	 * Ext order info 保存
	 *
	 * @param orderInfo 订单号
	 * @param user 当前用户
	 *
	 * @return
	 */
	private boolean insertExtOrdersInfo(OrdersBean orderInfo, UserSessionBean user) {
		boolean ret = true;

		//	creater
		orderInfo.setCreater(user.getUserName());

		//	modifier
		orderInfo.setModifier(user.getUserName());

		ret = orderDetailDao.insertExtOrdersInfo(orderInfo);

		return ret;
	}

	/**
	 * Ext order details info 保存
	 *
	 * @param orderDetailsInfo 订单明细
	 * @param user 当前用户
	 *
	 * @return
	 */
	private boolean insertExtOrderDetailsInfo(OrderDetailsBean orderDetailsInfo, UserSessionBean user) {
		boolean ret = true;

		//	creater
		orderDetailsInfo.setCreater(user.getUserName());

		//	modifier
		orderDetailsInfo.setModifier(user.getUserName());

		ret = orderDetailDao.insertExtOrderDetailsInfo(orderDetailsInfo);

		return ret;
	}

//	omsOrderDetailsSearchService 调用变更	
//	/**
//	 * 订单金额设定
//	 * 
//	 * @param orderPrice 订单价格
//	 * 
//	 * @return 订单Bean
//	 */
//	private OrdersBean setOrderPrice(OrderPrice orderPrice) {
//		OrdersBean ordersInfo = new OrdersBean();
//		
//		//		source_order_id（一组订单时，设定）
//		ordersInfo.setSourceOrderId(orderPrice.getSourceOrderId());
//		
//		// 		order_number（单个订单时，设定）
//		ordersInfo.setOrderNumber(orderPrice.getOrderNumber());
//		
//		//		product_total
//		ordersInfo.setProductTotal(String.valueOf(orderPrice.getProductTotal()));
//		//		final_product_total
//		ordersInfo.setFinalProductTotal(String.valueOf(orderPrice.getFinalProductTotal()));
//		//		surcharge
//		ordersInfo.setSurcharge(String.valueOf(orderPrice.getSurcharge()));
//		//		revised_surcharge
//		ordersInfo.setRevisedSurcharge(String.valueOf(orderPrice.getRevisedSurcharge()));
//		//		discount
//		ordersInfo.setDiscount(String.valueOf(orderPrice.getDiscount()));
//		//		revised_discount
//		ordersInfo.setRevisedDiscount(String.valueOf(orderPrice.getRevisedDiscount()));
//		//		shipping_total
//		ordersInfo.setShippingTotal(String.valueOf(orderPrice.getShippingTotal()));
//		//		final_shipping_total
//		ordersInfo.setFinalShippingTotal(String.valueOf(orderPrice.getFinalShippingTotal()));
//		//		grand_total
//		ordersInfo.setGrandTotal(String.valueOf(orderPrice.getGrandTotal()));
//		//		final_grand_total
//		ordersInfo.setFinalGrandTotal(String.valueOf(orderPrice.getFinalGrandTotal()));
//		
//		return ordersInfo;
//	}

	
//	omsOrderDetailsSearchService 调用变更	
//	/**
//	 * 订单Transactions信息保存
//	 * 
//	 * @param sourceOrderId 元订单号
//	 * @param orderNumber 订单号
//	 * @param description 内容
//	 * @param money 金额
//	 * @param user 当前用户
//	 * 
//	 * @return
//	 */
//	private boolean saveOrderTransactionsInfo(String sourceOrderId, String orderNumber, String sku, String itemNumber, String description, String money, String noteId, boolean isCanceled, UserSessionBean user) {
//		boolean ret = true;
//		
//		//	收入
//		String debt = "0";
//		//	支出
//		String credit = "0";
//		
//		// 取消的场合
//		if (isCanceled) {
//			// 产品的场合
//			if (OrderDetailSkuDsp.PRODUCT_TITLE.equals(description)) {
//				credit = money;
//			// shipping 的场合
//			} else if (OrderDetailSkuDsp.SHIPPING_TITLE.equals(description)) {
//				if (Float.valueOf(money) > 0) {
//					credit = money;
//				} else {
//					debt = String.valueOf(mult2Digits(Float.valueOf(money), -1));
//				}
//			// discount 的场合
//			} else if (OrderDetailSkuDsp.DISCOUNT_TITLE.equals(description)) {
//				//	该处money为DB的值，（存入DB时 *-1）
//					//	表示追加打折
//				if (Float.valueOf(money) > 0) {
//					credit = money;
//				} else {
//					// 表示取消打折
//					debt = String.valueOf(mult2Digits(Float.valueOf(money), -1));
//				}
//			// 价差订单 的场合
//			} else if (OrderDetailSkuDsp.PRICE_DIFF_TITLE.equals(description)) {
//				credit = money;
//			}	
//		} else {
//			// 产品的场合
//			if (OrderDetailSkuDsp.PRODUCT_TITLE.equals(description)) {
//				debt = money;
//			// shipping 的场合
//			} else if (OrderDetailSkuDsp.SHIPPING_TITLE.equals(description)) {
//				if (Float.valueOf(money) > 0) {
//					debt = money;
//				} else {
//					credit = String.valueOf(mult2Digits(Float.valueOf(money), -1));
//				}
//			// discount 的场合
//			} else if (OrderDetailSkuDsp.DISCOUNT_TITLE.equals(description)) {
//				//	该处money为画面输入的值，（存入DB时 *-1）
//				//	表示打折
//				if (Float.valueOf(money) > 0) {
//					debt = money;
//				//	表示不打折
//				} else {
//					credit = String.valueOf(mult2Digits(Float.valueOf(money), -1));
//				}
//			// 价差订单 的场合
//			} else if (OrderDetailSkuDsp.PRICE_DIFF_TITLE.equals(description)) {
//				debt = money;
//			}			
//		}
//		
//		TransactionsBean transactionsInfo = new TransactionsBean();
//		//	元订单号
//		transactionsInfo.setSourceOrderId(sourceOrderId);
//		//	订单号
//		transactionsInfo.setOrderNumber(orderNumber);
//		//	sku
//		transactionsInfo.setSku(sku);
//		//	itemNumber
//		transactionsInfo.setItemNumber(itemNumber);
//		//  description
//		transactionsInfo.setDescription(description);
//		//	应收金额
//		transactionsInfo.setDebt(debt);
//		//	应付金额 
//		transactionsInfo.setCredit(credit);
//		//	noteId
//		transactionsInfo.setNoteId(noteId);
//		//	当前用户
//		transactionsInfo.setCreater(user.getUserName());
//		//	当前用户
//		transactionsInfo.setModifier(user.getUserName());
//		
//		ret = transactionsDao.insertTransactionsInfo(transactionsInfo);
//		if (!ret) {
//			ret = false;
//		}
//		
//		return ret;
//	}
	
	/**
	 * Notes 追加
	 * @param type 类型
	 * @param orderNumber 订单号
	 * @param Notes Notes
	 * @param enteredBy 输入者
	 * @param modifier 更新人
	 * 
	 * @return
	 */
	private String addNotes(String sourceOrderId, String type, String orderNumber, String Notes, String enteredBy, String modifier) {
		
		// 订单Notes
		NotesBean notesInfo = new NotesBean();
		
		// 明细行番号
		int itemNumber = orderDetailDao.getOrderDetailsNotesMaxItemNumber(orderNumber) + 1;
		
		//		元订单号
		notesInfo.setSourceOrderId(sourceOrderId);
		//		type
		notesInfo.setType(type);
		// 		order_number
		notesInfo.setNumericKey(orderNumber);
		//		item_number
		notesInfo.setItemNumber(String.valueOf(itemNumber));
		//		notes
		notesInfo.setNotes(Notes);
		//		entered_by
		notesInfo.setEnteredBy(enteredBy);
		//		creater
		notesInfo.setCreater(modifier);
		//		modifier
		notesInfo.setModifier(modifier);
		
		notesDao.insertNotesInfo(notesInfo);
		
		return notesInfo.getId();
	}
	
	/**
	 * 订单信息OrderNumber设定
	 * 
	 * @param inFormAddNewOrderOrder 订单信息
	 * @param orderNumber 订单号
	 * 
	 * @return
	 */
	private void setInputInfoOrderNumber(InFormAddNewOrderOrder inFormAddNewOrderOrder, String orderNumber) {
		// 订单信息
		OrdersBean ordersBean = inFormAddNewOrderOrder.getOrderInfo();
		ordersBean.setOrderNumber(orderNumber);
		
		// 订单明细信息
		List<OrderDetailsBean> orderDetailsList = inFormAddNewOrderOrder.getOrderDetailsList();
		for (int i = 0; i < orderDetailsList.size(); i++) {
			OrderDetailsBean orderDetailsInfo = orderDetailsList.get(i);
			orderDetailsInfo.setOrderNumber(orderNumber);			
		}
		
//		// 订单Transactions信息
//		List<TransactionsBean> transactionsList = inFormAddNewOrderOrder.getTransactionsList();
//		for (int i = 0; i < transactionsList.size(); i++) {
//			TransactionsBean transactionsInfo = transactionsList.get(i);
//			transactionsInfo.setOrderNumber(orderNumber);
//		}
	}
	
	/**
	 * 订单信息其他字段设定
	 * 
	 * @param inFormAddNewOrderOrder 新订单信息
	 * @param ordersInfo 关联订单信息
	 * 
	 * @return
	 */
	private void setOrderInfoFields(InFormAddNewOrderOrder inFormAddNewOrderOrder, OutFormOrderdetailOrders ordersInfo) {
		OrdersBean newOrderInfo = inFormAddNewOrderOrder.getOrderInfo();
		
		// order_number採番
		String orderNumber = String.valueOf(orderDetailDao.getOrderNumber());
		//	输入信息 OrderNumber 设定
		setInputInfoOrderNumber(inFormAddNewOrderOrder, orderNumber);

		logger.info("		setOrderInfoFields insert orderNumber = " + orderNumber);

		//	订单CartId再设定
		if (StringUtils.isEmpty(newOrderInfo.getCartId())) {
			newOrderInfo.setCartId(OmsCodeConstants.CartId.OFFLINE);
		}
		
		// 	source_order_id, subsource_order_id 再设定
		//		普通订单的场合
		if (OmsCodeConstants.OrderKind.ORIGINAL_ORDER.equals(newOrderInfo.getOrderKind())) {
			// 		source_order_id 再设定
			newOrderInfo.setSourceOrderId(newOrderInfo.getOrderNumber());
			//		origin_source_order_id 再设定
			newOrderInfo.setOriginSourceOrderId(newOrderInfo.getOrderNumber());
			//		sub_source_order_id 清空
			newOrderInfo.setSubSourceOrderId("");
		} else {
			//		source_order_id 保持不变
			//		origin_source_order_id 同 source_order_id
			newOrderInfo.setOriginSourceOrderId(newOrderInfo.getSourceOrderId());
			//		sub_source_order_id 採番
			String subSourceOrderId = getOrdersSubSourceOrderId(newOrderInfo.getSourceOrderId());
			newOrderInfo.setSubSourceOrderId(subSourceOrderId);
		}
		
		//		order_status
		newOrderInfo.setOrderStatus(OmsCodeConstants.OrderStatus.APPROVED);
		//		approved
		newOrderInfo.setApproved(true);
		//		cancelled
		newOrderInfo.setCancelled(false);
		//		freightCollect
		newOrderInfo.setFreightCollect(false);

		// 原始订单的场合
		if (OmsCodeConstants.OrderKind.ORIGINAL_ORDER.equals(inFormAddNewOrderOrder.getOrderInfo().getOrderKind())) {
			//	未锁定
			newOrderInfo.setLockShip(OmsConstants.LockShip.No);
			//	快递方式
			newOrderInfo.setShipping(ordersInfo.getShipping());
		} else {
			//	锁定状态，同主订单状态
			newOrderInfo.setLockShip(ordersInfo.getLockShip());
			//	快递方式
			newOrderInfo.setShipping(ordersInfo.getShipping());
		}
	}
	
	/**
	 * 订单信息保存
	 * 
	 * @param orderInfo 订单信息
	 * 
	 * @return
	 */
	private boolean saveOrderInfo(OrdersBean orderInfo, UserSessionBean user) {
		boolean ret = true;
		
//		// 	再设定字段
//		//		普通订单的场合
//		if (OmsCodeConstants.OrderKind.ORIGINAL_ORDER.equals(orderInfo.getOrderKind())) {
//			// 		source_order_id 再设定
//			orderInfo.setSourceOrderId(orderInfo.getOrderNumber());
//			//		sub_source_order_id 清空
//			orderInfo.setSubSourceOrderId("");
//		} else {
//			//		source_order_id 保持不变
//			//		sub_source_order_id 採番
//			String subSourceOrderId = getOrdersSubSourceOrderId(orderInfo.getSourceOrderId());
//			orderInfo.setSubSourceOrderId(subSourceOrderId);
//		}
		
//		//		order_status
//		orderInfo.setOrderStatus(OmsCodeConstants.OrderStatus.APPROVED);
//		//		approved
//		orderInfo.setApproved(true);
//		//		cancelled
//		orderInfo.setCancelled(false);
		
		// 实际值换算
		if (OmsConstants.DiscountType.PERCENT.equals(orderInfo.getDiscountType())) {
			float discountPercent = div4Digits(Float.valueOf(orderInfo.getDiscountPercent()), 100);
			orderInfo.setDiscountPercent(String.valueOf(discountPercent));
		}
		
		//		creater
		orderInfo.setCreater(user.getUserName());
		//		modifier
		orderInfo.setModifier(user.getUserName());
		
		//	CustomerId
		if (StringUtils.isEmpty(orderInfo.getCustomerId())) {
			CustomerBean customerinfoExist = customerDao.getCustomerInfo(orderInfo.getName(), orderInfo.getPhone(), orderInfo.getCartId());
			
			if (customerinfoExist != null) {
				orderInfo.setCustomerId(customerinfoExist.getCustomerId());
			}
		}
		
		if (StringUtils.isEmpty(orderInfo.getCustomerId())) {
			// 客户ID取得失败
			ret = false;
			
			logger.info("CustomerId is null. ordernum = " + orderInfo.getOrderNumber());
		} else {
			ret = orderDao.insertOrdersInfo(orderInfo);
		}		
		
		return ret;
	}
	
	/**
	 * 除法 保留两位小数
	 * 
	 * @param addPara1
	 * @param addPara2
	 * @return ret
	 */
	private float div4Digits(float addPara1, float addPara2) {
		float ret = 0f;
		
		BigDecimal bd1 = new BigDecimal(addPara1);
		BigDecimal bd2 = new BigDecimal(addPara2);
		
		ret = bd1.divide(bd2).setScale(4, BigDecimal.ROUND_HALF_UP).floatValue(); 
		
		return ret;
	}
	
	/**
	 * 根据sourceOrderId取得subSourceOrderId
	 * 
	 * @param sourceOrderId 网络订单号
	 * 
	 * @return
	 */
	private String getOrdersSubSourceOrderId(String sourceOrderId) {
		String ret = "";

		// DB 既存subSourceOrderId
		String existSubSourceOrderId = orderDetailDao.getOrdersMaxSubSourceOrderId(sourceOrderId);
		if (StringUtils.isEmpty(existSubSourceOrderId)) {
			ret = sourceOrderId + "1";
		} else {
			int no = Integer.valueOf(existSubSourceOrderId.substring(sourceOrderId.length()));
			no = no + 1;
			
			ret = sourceOrderId + no;
		}
		
		return ret;		
	}
	
	/**
	 * 订单明细信息保存
	 * 
	 * @param orderInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * @param noteId transaction追加用
	 * @param isInsertExtTable 扩展表是否追加
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrderDetailsInfo(OrdersBean orderInfo, List<OrderDetailsBean> orderDetailsList, String noteId, boolean isInsertExtTable, UserSessionBean user) {
		boolean ret = true;
		//	订单明细番号
		int itemNumber = 1;
		//	订单明细打折关联番号
		int subItemNumber = 0; 
		
		// 订单明细信息
		for (int i = 0; i < orderDetailsList.size(); i++) {
			OrderDetailsBean orderDetailsInfo = orderDetailsList.get(i);
			//	输入明细SKU缓存
			String sku = orderDetailsInfo.getSku();
			//	输入明细Product缓存
			String product = orderDetailsInfo.getProduct();
			//	输入金额单价
			String pricePerUnit = orderDetailsInfo.getPricePerUnit();
			
			// 	订单数量
			int quantityOrdered = Integer.valueOf(orderDetailsInfo.getQuantityOrdered());

			for (int j = 0; j < quantityOrdered; j++) {
				
				subItemNumber = itemNumber;
				
				// 再设定字段
				//	明细番号
				orderDetailsInfo.setItemNumber(String.valueOf(itemNumber));
				//	关联明细番号
				orderDetailsInfo.setSubItemNumber("0");
				//	sku
				orderDetailsInfo.setSku(sku);					
				//	product
				orderDetailsInfo.setProduct(product);
				//	pricePerUnit
				orderDetailsInfo.setPricePerUnit(pricePerUnit);
				//	adjustment　无
				orderDetailsInfo.setAdjustment(false);
				//  订单数量（本次处理复数件的场合，拆开）
				//		quantity_ordered
				orderDetailsInfo.setQuantityOrdered("1");
				//		quantity_shipped
				orderDetailsInfo.setQuantityShipped("1");
				//		quantity_returned
				orderDetailsInfo.setQuantityReturned("0");
				// 	明细状态
				orderDetailsInfo.setStatus(OmsCodeConstants.OrderStatus.APPROVED);
				//	库存分配 状态
				orderDetailsInfo.setResAllotFlg(false);
				//	synship 同步状态
				orderDetailsInfo.setSyncSynship(false);
				//	resId
				orderDetailsInfo.setResId("0");
				//		creater
				orderDetailsInfo.setCreater(user.getUserName());
				//		modifier
				orderDetailsInfo.setModifier(user.getUserName());
				
				ret = orderDetailDao.insertOrderDetailsInfo(orderDetailsInfo);
				if (!ret) {
					break;
				}
				
				// Transactions 信息更新
				// sourceOrderId, orderNumber, sku, itemNumber, description,  money, noteId, isCanceled, user
				ret = omsOrderDetailsSearchService.saveOrderTransactionsInfo(orderInfo.getSourceOrderId(),
						orderInfo.getOriginSourceOrderId(),
						orderInfo.getOrderNumber(),
						orderDetailsInfo.getSku(),
						String.valueOf(itemNumber),
						OmsConstants.OrderDetailSkuDsp.PRODUCT_TITLE,
						orderDetailsInfo.getPricePerUnit(),
						noteId,
						OmsConstants.Transaction_Type.ORDER,
						false,
						user);
				if (!ret) {
					break;
				}

				if (isInsertExtTable) {
					// oms_bt_ext_order_details追加
					ret = insertExtOrderDetailsInfo(orderDetailsInfo, user);
					if (!ret) {
						break;
					}
				}

				// 明细番号
				itemNumber = itemNumber + 1;
				
				// 打折信息保存
				if (!"0".equals(orderDetailsInfo.getDiscount())) {
					// 再设定字段
					//	明细番号
					orderDetailsInfo.setItemNumber(String.valueOf(itemNumber));	
					//	关联明细番号
					orderDetailsInfo.setSubItemNumber(String.valueOf(subItemNumber));
					//	sku
					orderDetailsInfo.setSku(OmsConstants.OrderDetailSkuDsp.DISCOUNT_TITLE);					
					//	product
					orderDetailsInfo.setProduct(OmsConstants.OrderDetailProductDsp.DISCOUNT_TITLE);
					//	adjustment　有
					orderDetailsInfo.setAdjustment(true);
					//	pricePerUnit
					orderDetailsInfo.setPricePerUnit(orderDetailsInfo.getDiscount());
					//  订单数量（本次处理复数件的场合，拆开）
					//		quantity_ordered
					orderDetailsInfo.setQuantityOrdered("1");
					//		quantity_shipped
					orderDetailsInfo.setQuantityShipped("1");
					//		quantity_returned
					orderDetailsInfo.setQuantityReturned("0");
					// 	明细状态
					orderDetailsInfo.setStatus("");
					//	库存分配 状态
					orderDetailsInfo.setResAllotFlg(true);
					//	synship 同步状态
					orderDetailsInfo.setSyncSynship(true);
					//	resId
					orderDetailsInfo.setResId("0");
					//		creater
					orderDetailsInfo.setCreater(user.getUserName());
					//		modifier
					orderDetailsInfo.setModifier(user.getUserName());
					
					ret = orderDetailDao.insertOrderDetailsInfo(orderDetailsInfo);
					if (!ret) {
						break;
					}
					
					// Transactions 信息更新
					// sourceOrderId, orderNumber, sku, itemNumber, description,  money, noteId, isCanceled, user
					ret = omsOrderDetailsSearchService.saveOrderTransactionsInfo(orderInfo.getSourceOrderId(),
													orderInfo.getOriginSourceOrderId(),
													orderInfo.getOrderNumber(),
													sku,
													String.valueOf(itemNumber),
													OmsConstants.OrderDetailSkuDsp.DISCOUNT_TITLE,
													orderDetailsInfo.getPricePerUnit(),
													noteId,
													OmsConstants.Transaction_Type.ORDER,
													false,
													user);
					if (!ret) {
						break;
					}

					if (isInsertExtTable) {
						// oms_bt_ext_order_details追加
						ret = insertExtOrderDetailsInfo(orderDetailsInfo, user);
						if (!ret) {
							break;
						}
					}

					// 明细番号
					itemNumber = itemNumber + 1;
				}	
			}
		}
		
		// 折扣等信息追加
		// 		订单明细（复制元）
		OrderDetailsBean orderDetailsInfo = orderDetailsList.get(0);
		//		订单明细插入
		OrderDetailsBean orderDetailsInfoFromOther = new OrderDetailsBean();
		//		共通设定
		//			adjustment　有
		orderDetailsInfoFromOther.setAdjustment(true);
		//			订单号
		orderDetailsInfoFromOther.setOrderNumber(orderDetailsInfo.getOrderNumber());
		//			quantity_ordered
		orderDetailsInfoFromOther.setQuantityOrdered("1");
		//			quantity_shipped
		orderDetailsInfoFromOther.setQuantityShipped("1");
		//			quantity_returned
		orderDetailsInfoFromOther.setQuantityReturned("0");
		// 	明细状态
		orderDetailsInfoFromOther.setStatus("");
		//		库存分配 状态（非正常物品不需分配）
		orderDetailsInfoFromOther.setResAllotFlg(true);
		//		synship 同步状态
		orderDetailsInfoFromOther.setSyncSynship(true);
		//		res_id
		orderDetailsInfoFromOther.setResId("0");
		//		creater
		orderDetailsInfoFromOther.setCreater(user.getUserName());
		//		modifier
		orderDetailsInfoFromOther.setModifier(user.getUserName());
		
		//		surcharge保存
		if (ret && Float.valueOf(orderInfo.getSurcharge()) !=0 ) {
			orderDetailsInfoFromOther.setItemNumber(String.valueOf(itemNumber));
			// product
			orderDetailsInfoFromOther.setProduct(OmsConstants.OrderDetailProductDsp.SURCHARGE_TITLE);
			orderDetailsInfoFromOther.setPricePerUnit(orderInfo.getSurcharge());
			orderDetailsInfoFromOther.setQuantityOrdered("1");
			// sku
			orderDetailsInfoFromOther.setSku(OmsConstants.OrderDetailSkuDsp.SURCHARGE_TITLE);
			
			ret = orderDetailDao.insertOrderDetailsInfo(orderDetailsInfoFromOther);

			if (ret) {
				// Transactions 信息更新
				// sourceOrderId, orderNumber, description, money, noteId, user
				ret = omsOrderDetailsSearchService.saveOrderTransactionsInfo(orderInfo.getSourceOrderId(),
						orderInfo.getOriginSourceOrderId(),
						orderInfo.getOrderNumber(),
						orderDetailsInfoFromOther.getSku(),
						String.valueOf(itemNumber),
						OmsConstants.OrderDetailSkuDsp.SURCHARGE_TITLE,
						orderDetailsInfoFromOther.getPricePerUnit(),
						noteId,
						OmsConstants.Transaction_Type.ORDER,
						false,
						user);
			}

			if (ret && isInsertExtTable) {
				// oms_bt_ext_order_details追加
				ret = insertExtOrderDetailsInfo(orderDetailsInfoFromOther, user);
			}
			
			// 明细番号
			itemNumber = itemNumber + 1;
		}
		
		//		discount保存
		if (ret && Float.valueOf(orderInfo.getOrderDiscount()) !=0 ) {
			orderDetailsInfoFromOther.setItemNumber(String.valueOf(itemNumber));
			// product
			orderDetailsInfoFromOther.setProduct(OmsConstants.OrderDetailProductDsp.DISCOUNT_TITLE);
			orderDetailsInfoFromOther.setPricePerUnit(orderInfo.getOrderDiscount());
			orderDetailsInfoFromOther.setQuantityOrdered("1");
			// sku
			orderDetailsInfoFromOther.setSku(OmsConstants.OrderDetailSkuDsp.DISCOUNT_TITLE);
			
			ret = orderDetailDao.insertOrderDetailsInfo(orderDetailsInfoFromOther);

			if (ret) {
				// Transactions 信息更新
				// sourceOrderId, orderNumber, description, money, noteId, user
				ret = omsOrderDetailsSearchService.saveOrderTransactionsInfo(orderInfo.getSourceOrderId(),
												orderInfo.getOriginSourceOrderId(),
												orderInfo.getOrderNumber(),
												orderDetailsInfoFromOther.getSku(),
												String.valueOf(itemNumber),
												OmsConstants.OrderDetailSkuDsp.DISCOUNT_TITLE,
												orderDetailsInfoFromOther.getPricePerUnit(),
												noteId,
												OmsConstants.Transaction_Type.ORDER,
												false,
												user);
			}

			if (ret && isInsertExtTable) {
				// oms_bt_ext_order_details追加
				ret = insertExtOrderDetailsInfo(orderDetailsInfoFromOther, user);
			}
			
			// 明细番号
			itemNumber = itemNumber + 1;
		}
		
		//		shipping保存
		if (ret && Float.valueOf(orderInfo.getShippingTotal()) != 0) {
			orderDetailsInfoFromOther.setItemNumber(String.valueOf(itemNumber));
			// product
			orderDetailsInfoFromOther.setProduct(OmsConstants.OrderDetailProductDsp.SHIPPING_CHARGE_TITLE);
			orderDetailsInfoFromOther.setPricePerUnit(orderInfo.getShippingTotal());
			orderDetailsInfoFromOther.setQuantityOrdered("1");
			// sku
			orderDetailsInfoFromOther.setSku(OmsConstants.OrderDetailSkuDsp.SHIPPING_TITLE);
			
			ret = orderDetailDao.insertOrderDetailsInfo(orderDetailsInfoFromOther);

			if (ret) {
				// Transactions 信息更新
				// sourceOrderId, orderNumber, description, money, noteId, user
				ret = omsOrderDetailsSearchService.saveOrderTransactionsInfo(orderInfo.getSourceOrderId(),
						orderInfo.getOriginSourceOrderId(),
						orderInfo.getOrderNumber(),
						orderDetailsInfoFromOther.getSku(),
						String.valueOf(itemNumber),
						OmsConstants.OrderDetailSkuDsp.SHIPPING_TITLE,
						orderDetailsInfoFromOther.getPricePerUnit(),
						noteId,
						OmsConstants.Transaction_Type.ORDER,
						false,
						user);
			}

			boolean preRet = ret && isInsertExtTable;
			if (preRet) {
				// oms_bt_ext_order_details追加
				ret	 = insertExtOrderDetailsInfo(orderDetailsInfoFromOther, user);
			}
			
			// 明细番号
			itemNumber = itemNumber + 1;
		}
		
		return ret;
	}
	
	// 已废止
//	/**
//	 * 订单Transactions信息保存
//	 * 
//	 * @param orderTransactionsList 订单Transactions信息
//	 * 
//	 * @return
//	 */
//	private boolean saveOrderTransactionsInfo(List<TransactionsBean> orderTransactionsList, UserSessionBean user) {
//		boolean ret = true;
//		
//		for (int i = 0; i < orderTransactionsList.size(); i++) {
//			TransactionsBean transactionsInfo = orderTransactionsList.get(i);
//			transactionsInfo.setCreater(user.getUserName());
//			transactionsInfo.setModifier(user.getUserName());
//			
//			ret = transactionsDao.insertTransactionsInfo(transactionsInfo);
//			if (!ret) {
//				ret = false;
//				break;
//			}
//		}
//		
//		return ret;
//	}
	
	/**
	 * 客户信息保存
	 * 
	 * @param orderInfo 订单信息
	 * 
	 * @return
	 */
	private boolean saveCustomer(OrdersBean orderInfo) {
		boolean ret = true;
		String customerId = "";
		
		// 客户ID取得
		customerId = orderInfo.getCustomerId();
		
		if (StringUtils.isEmpty(customerId)) {
			
			CustomerBean customerinfoExist = customerDao.getCustomerInfo(orderInfo.getName(), orderInfo.getPhone(), orderInfo.getCartId());
			
			if (customerinfoExist != null) {
				customerId = customerinfoExist.getCustomerId();
			}
		}
		
		// 客户Bean设定
		CustomerBean customerinfo = new CustomerBean();
		customerinfo.setCustomerId(customerId);
		customerinfo.setFullName(orderInfo.getName()); 
		customerinfo.setLastName(orderInfo.getName());
		customerinfo.setCompany(orderInfo.getCompany());
		customerinfo.setEmail(orderInfo.getEmail());
		customerinfo.setAddress(orderInfo.getAddress());
		customerinfo.setAddress2(orderInfo.getAddress2());
		customerinfo.setCity(orderInfo.getCity());
		customerinfo.setState(orderInfo.getState());
		customerinfo.setZip(orderInfo.getZip());
		customerinfo.setCountry(orderInfo.getCountry());
		customerinfo.setPhone(orderInfo.getPhone());
		customerinfo.setOrderChannelId(orderInfo.getOrderChannelId());
		
		if (StringUtils.isEmpty(customerId)) {
			int sourceId = omsChannelDao.getSourceId(Integer.valueOf(orderInfo.getCartId()));
			customerinfo.setSourceId(String.valueOf(sourceId));
			
			ret = customerDao.insertCustomerInfo(customerinfo);
			
		} else {
			
			ret = customerDao.updateCustomerInfo(customerinfo);
			
		}
		return ret;
	}
	
	/**
	 * 检索客户信息
	 * 
	 * @return
	 */
	public List<OutFormAddNewOrderCustomer> getCustomersList(InFormAddNewOrderCustomer inFormAddNewOrderCustomer) {
		List<OutFormAddNewOrderCustomer> ret = customerDao.getCustomersList(inFormAddNewOrderCustomer);
		
		return ret;
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
}
