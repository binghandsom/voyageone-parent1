package com.voyageone.oms.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taobao.api.response.*;
import com.voyageone.common.components.channelAdvisor.bean.orders.*;
import com.voyageone.common.components.channelAdvisor.service.OrderRefundService;
import com.voyageone.common.components.channelAdvisor.service.OrderService;
import com.voyageone.common.components.channelAdvisor.webservices.APIResultOfArrayOfOrderResponseItem;
import com.voyageone.common.components.channelAdvisor.webservices.APIResultOfRefundOrderResponse;
import com.voyageone.common.components.channelAdvisor.webservices.ArrayOfOrderResponseItem;
import com.voyageone.common.components.channelAdvisor.webservices.SubmitOrderRefundResponse;
import com.voyageone.common.components.tmall.TbWLBService;
import com.voyageone.common.configs.*;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.mail.Mail;
import com.voyageone.common.Constants;
import com.voyageone.oms.dao.*;
import com.voyageone.oms.modelbean.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import sun.misc.BASE64Decoder;

import com.taobao.api.ApiException;
import com.taobao.api.domain.Refund;
import com.taobao.api.domain.RefundMessage;
import com.voyageone.common.components.tmall.TbRefundService;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Name;
import com.voyageone.common.configs.beans.OrderChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ImgUtils;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.PermissionBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.oms.OmsCodeConstants;
import com.voyageone.oms.OmsConstants;
import com.voyageone.oms.OmsConstants.LockShip;
import com.voyageone.oms.OmsConstants.OrderDetailSkuDsp;
import com.voyageone.oms.OmsConstants.PropKey;
import com.voyageone.oms.OmsMessageConstants;
import com.voyageone.oms.OmsUrlConstants;
import com.voyageone.oms.formbean.InFormOrderdetailAddLineItem;
import com.voyageone.oms.formbean.InFormOrderdetailAdjustmentItem;
import com.voyageone.oms.formbean.InFormOrderdetailReturn;
import com.voyageone.oms.formbean.InFormServiceSearchSKU;
import com.voyageone.oms.formbean.OutFormAddNewOrderCustomer;
import com.voyageone.oms.formbean.OutFormOrderDetailOrderDetail;
import com.voyageone.oms.formbean.OutFormOrderdetailNotes;
import com.voyageone.oms.formbean.OutFormOrderdetailOrderHistory;
import com.voyageone.oms.formbean.OutFormOrderdetailOrderHistoryItem;
import com.voyageone.oms.formbean.OutFormOrderdetailOrders;
import com.voyageone.oms.formbean.OutFormOrderdetailPayments;
import com.voyageone.oms.formbean.OutFormOrderdetailRefunds;
import com.voyageone.oms.formbean.OutFormOrderdetailRefundsStatus;
import com.voyageone.oms.formbean.OutFormOrderdetailShipping;
import com.voyageone.oms.formbean.OutFormOrderdetailTracking;
import com.voyageone.oms.formbean.OutFormOrderdetailTransactions;
import com.voyageone.oms.formbean.OutFormServiceSearchSKU;
import com.voyageone.oms.service.OmsCommonService;
import com.voyageone.oms.service.OmsOrderDetailsSearchService;
import com.voyageone.oms.utils.Verification;
import com.voyageone.oms.utils.WebServiceUtil;

@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public class OmsOrderDetailsSearchServiceImpl implements OmsOrderDetailsSearchService {
	
	private static Log logger = LogFactory.getLog(OmsOrderDetailsSearchServiceImpl.class);
	
	@Autowired
	private OrderDetailDao orderDetailDao;
	
	@Autowired
	private NotesDao notesDao;
	
	@Autowired
	private TrackingDao trackingDao;
	
	@Autowired
	private TransactionsDao transactionsDao;
	
	@Autowired
	private PaymentsDao paymentsDao;	
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private SynShipSyncDao synShipSyncDao;
	
	@Autowired
	private OmsCommonService omsCommonService;
	
	@Autowired
	private CartDao cartDao;
	
	@Autowired
	private RefundDao refundDao;

	@Autowired
	private CainiaoDao cainiaoDao;
	
    // 淘宝退款申请API
	@Autowired
    TbRefundService tbRefundService;
	// 物流宝API
	@Autowired
	TbWLBService tbWLBService;

	// CA退货API
	@Autowired
	OrderRefundService orderRefundService;
	// CA订单API
	@Autowired
	OrderService orderService;
	
	@Autowired
	private DataSourceTransactionManager transactionManager;	
	DefaultTransactionDefinition def =new DefaultTransactionDefinition();
	
	// 中国店铺
//	private final static String CN_CARTIDS = "20,21,23,24,25,26";
	private static List<CartBean> CN_CARTIDS = null;
	
	// pay Title
	private final static String PAID_IN_FULL = "Paid in full";
	private final static String BALANCE_DUE = "Balance Due";
	private final static String CREDIT_DUE = "Credit Due";
	
	// pay Value
	private final static String PAID_IN_FULL_VAL = "1";
	private final static String BALANCE_DUE_VAL = "2";
	private final static String CREDIT_DUE_VAL = "3";
	
	// index
	//		product title index
	private final static int PRODUCT_TITLE_INDEX = 0;
	//		SKU title index
	private final static int SKU_TITLE_INDEX = 1;
	//		note index
	private final static int NOTE_INDEX = 2;	
	
	// 订单操作类型
	private enum Operation {
		// 订单明细取消
		CancelOrderDetail("1"),
		// 订单明细Return
		ReturnOrderDetail("2"),
		// 订单明细追加
		AddOrderDetail("3"),
		
		// 订单取消
		CancelOrder("4"),
		// 订单调整（地址变更）
		ChangeShipAddress("5"),
		// 第三方订单取消
		CancelThirdPartnerOrder("6");
		
		private final String value;
		
		Operation(String value) {
            this.value = value;
        }
		
		public String getValue() {
            return value;
        }
	}
	
	// PA 子账号（可退款）
//	private final static String appkey = "23004167";
//	private final static String appsecret = "8b1ab21e4fde6c71fcd177e4ebe89147";
//	private final static String sessionkey = "6201d239a699c339549ebd803e0d9baced16562c5649cd22205145934";
	
//	// PA 主账号（SQL Server）
//	private final static String appkey = "23004167";
//	private final static String appsecret = "8b1ab21e4fde6c71fcd177e4ebe89147";
//	private final static String sessionkey = "6200027b2368d6b6e385a84cba141be5654ZZ1ce61300612183719539";
	
	// sneakerhead
//	private final static String appkey = "21008948";
//	private final static String appsecret = "0a16bd08019790b269322e000e52a19f";
//	private final static String sessionkey = "6200a23ce180124c66248fa2bd50420ZZf0df31db94bd5a907029661";
	
	// sneakerhead 新
//	private final static String appkey = "21008948";
//	private final static String appsecret = "0a16bd08019790b269322e000e52a19f";
//	private final static String sessionkey = "6202025a31c3b530f680c4457bf74913egiee3a557504632183719539";
//
//	private final static String url = "http://gw.api.taobao.com/router/rest";

	// 聚石塔
	//appkey:21008948
	//appsecret:0a16bd08019790b269322e000e52a19f
	//sessionkey:6200f026eace3a22978a33e4f2b397616f760debcc3f8092205145934

	// 卖家收货地址编号（taobao.rp.returngoods.agree 卖家同意退货）
//	long sellerAddressId = 139324185;
	
	/**
	 * 订单信息取得
	 * 
	 * @param orderNumber 订单号
	 * @return
	 */
	@Override
	public OutFormOrderdetailOrders getOrdersInfo(String orderNumber, UserSessionBean user){
		// 当前时区
		int timezone = user.getTimeZone();
		
		// 订单信息取得
		OutFormOrderdetailOrders ordersInfo = orderDetailDao.getOrdersInfo(orderNumber);
		// 订单信息中，信息再设定
		setCustomOrderFields(ordersInfo, timezone);
		
		// 订单详情取得
		List<OutFormOrderDetailOrderDetail> orderDetailsList = orderDetailDao.getOrderDetailsInfo(orderNumber);
		// 订单明细信息中，信息再设定
		setCustomOrderDetailFields(ordersInfo, orderDetailsList);
		
		if (ordersInfo != null) {
			// 订单信息中，详情设定
			ordersInfo.setOrderDetailsList(orderDetailsList);
		}

		
		return ordersInfo;
	}
	
	/**
	 * 获得订单列表信息（含明细）
	 * 
	 * @param sourceOrderId 网络订单号
	 * @param user 当前用户
	 * @return
	 */
	@Override
	public List<OutFormOrderdetailOrders> getOrdersList(String sourceOrderId, UserSessionBean user){
		// 当前时区
		int timezone = user.getTimeZone();
		
		// 订单信息取得
		List<OutFormOrderdetailOrders> ordersList = orderDetailDao.getOrdersListBySourceOrderId(sourceOrderId);
		// 订单信息中，信息再设定
		setCustomOrderListFields(ordersList, timezone);
		
		// 订单详情取得
		List<OutFormOrderDetailOrderDetail> orderDetailsListForOrdersList = orderDetailDao.getOrderDetailsInfo(getOrderNumberList(ordersList));
//		// 订单明细信息中，信息再设定
//		setCustomOrderDetailFields(orderDetailsListForOrdersList);
		
		// 订单Tracking信息
		List<OutFormOrderdetailShipping> orderShippingListForOrdersList = getOrderShippingInfoByOrderNumberList(getOrderNumberList(ordersList));
		
		if (ordersList != null) {
//			// 订单信息中，详情设定
//			ordersInfo.setOrderDetailsList(orderDetailsList);
			
			for (int i = 0; i < ordersList.size(); i++) {
				OutFormOrderdetailOrders orderInfo = ordersList.get(i);
				//	订单明细信息取得
				List<OutFormOrderDetailOrderDetail> orderDetailsList = getOrderDetailsList(orderDetailsListForOrdersList, orderInfo.getOrderNumber());
				//	订单明细信息中，信息再设定
				setCustomOrderDetailFields(orderInfo, orderDetailsList);
				//	订单明细设定
				orderInfo.setOrderDetailsList(orderDetailsList);
				
				// 订单Shipping信息取得
				List<OutFormOrderdetailShipping> orderShippingList = getOrderShippingList(orderShippingListForOrdersList, orderInfo.getOrderNumber());				
				orderInfo.setOrderShippingList(orderShippingList);
			}
		}
		
		return ordersList;
	}
	
	/**
	 * 获得订单明细信息，根据订单号
	 * 
	 * @param orderDetailsListForOrdersList 根据原订单号获得的一组订单明细信息
	 * @param orderNumber 订单号
	 * 
	 * @return
	 */
	private List<OutFormOrderDetailOrderDetail> getOrderDetailsList(List<OutFormOrderDetailOrderDetail> orderDetailsListForOrdersList, String orderNumber) {
		List<OutFormOrderDetailOrderDetail> orderDetailsList = new ArrayList<OutFormOrderDetailOrderDetail>();
		
		for (int i = 0; i < orderDetailsListForOrdersList.size(); i++) {
			OutFormOrderDetailOrderDetail orderDetailsInfo = orderDetailsListForOrdersList.get(i);
			
			if (orderNumber.equals(orderDetailsInfo.getOrderNumber())) {
				orderDetailsList.add(orderDetailsInfo);
			}
		}
		
		return orderDetailsList;
	}
	
	/**
	 * 获得订单shipping信息，根据订单号
	 * 
	 * @param orderShippingListForOrdersList 根据原订单号获得的一组Shipping信息
	 * @param orderNumber 订单号
	 * @return Shipping信息
	 */
	private List<OutFormOrderdetailShipping> getOrderShippingList(List<OutFormOrderdetailShipping> orderShippingListForOrdersList, String orderNumber) {
		List<OutFormOrderdetailShipping> orderShippingList = new ArrayList<OutFormOrderdetailShipping>();
		
		for (int i = 0; i < orderShippingListForOrdersList.size(); i++) {
			OutFormOrderdetailShipping orderShippingInfo = orderShippingListForOrdersList.get(i);
			
			if (orderNumber.equals(orderShippingInfo.getOrderNumber())) {
				orderShippingList.add(orderShippingInfo);
			}
		}
		
		return orderShippingList;
	}
	
	/**
	 * 获得订单号一览
	 * 
	 * @param ordersList 订单一览
	 * @return 订单号一览
	 */
	@Override
	public List<String> getOrderNumberList(List<OutFormOrderdetailOrders> ordersList) {
		ArrayList<String> orderNumberList = new ArrayList<String>();
		
		for (int i = 0; i < ordersList.size(); i++) {
			orderNumberList.add(ordersList.get(i).getOrderNumber());
		}
		
		return orderNumberList;
	}
	
	/**
	 * 设置主订单信息
	 * 
	 * @param mainOrderInfo 主订单信息
	 * @param orderInfo 订单信息
	 * @param orderTransactionsList 订单交易信息
	 * @return
	 */
	@Override
	public void setMainOrderInfo(OutFormOrderdetailOrders mainOrderInfo, OutFormOrderdetailOrders orderInfo, List<OutFormOrderdetailTransactions> orderTransactionsList) {
//		// order_channel_name（SN等）
//		mainOrderInfo.setStore(orderInfo.getStore());
//		// cart_name(TmallG 等)
//		mainOrderInfo.setChannel(orderInfo.getChannel());
		
		// wangWangId
		mainOrderInfo.setWangwangId(orderInfo.getName());
		// order_channel_name（SN等）
        mainOrderInfo.setOrderChannelId(orderInfo.getOrderChannelId());
		mainOrderInfo.setOrderChannelName(orderInfo.getOrderChannelName());
		// cart_name(TmallG 等)
        mainOrderInfo.setCartId(orderInfo.getCartId());
		mainOrderInfo.setCartName(orderInfo.getCartName());
		// platformId
		mainOrderInfo.setPlatformId(getCartInfo(orderInfo.getCartId()).getPlatformId());
		// localShipOnHold
		mainOrderInfo.setLocalShipOnHold(orderInfo.isLocalShipOnHold());
		
		// transaction 信息合计
		float debitTotal = 0f;
		float creditTotal = 0f;
		for (int i = 0; i < orderTransactionsList.size(); i++) {
			OutFormOrderdetailTransactions transactionInfo = orderTransactionsList.get(i);
			
			debitTotal = add2Digits(debitTotal, Float.valueOf(transactionInfo.getDebit()));
			creditTotal = add2Digits(creditTotal, Float.valueOf(transactionInfo.getCredit()));
		}
		// debtTotal
		mainOrderInfo.setDebitTotal(String.valueOf(debitTotal));
		// creditTotal
		mainOrderInfo.setCreditTotal(String.valueOf(creditTotal));
		
		// customerRefund
//		float customerRefund = sub2Digits(Float.valueOf(mainOrderInfo.getExpected()), Float.valueOf(mainOrderInfo.getFinalGrandTotal()));
//		mainOrderInfo.setCustomerRefund(String.valueOf(customerRefund));
		mainOrderInfo.setCustomerRefund(mainOrderInfo.getRefundTotal());
		
		// payTitleValue
		float balancedue = sub2Digits(Float.valueOf(mainOrderInfo.getPaymentTotal()), Float.valueOf(mainOrderInfo.getExpected()));
		mainOrderInfo.setBalanceDue(String.valueOf(balancedue));
		// payTitleText
		String payTitleText = "";
		if (balancedue == 0) {
			payTitleText = Type.getTypeName(OmsCodeConstants.PayInfoType, OmsCodeConstants.PayInfo.PAY_IN_FULL, "en");
		// Credit Due
		} else if (balancedue > 0) {
			payTitleText = Type.getTypeName(OmsCodeConstants.PayInfoType, OmsCodeConstants.PayInfo.CREDIT_DUE, "en");
		// Blance Due
		} else if (balancedue < 0) {
			payTitleText = Type.getTypeName(OmsCodeConstants.PayInfoType, OmsCodeConstants.PayInfo.BALANCE_DUE, "en");
		}
		
		mainOrderInfo.setPayTitleText(payTitleText);

		// price_difference_flag
		mainOrderInfo.setPriceDifferenceFlag(orderInfo.isPriceDifferenceFlag());

        // 是否存在退款履历
        boolean isHaveRefundHistory = isHaveRefunds(mainOrderInfo.getSourceOrderId(), true);
        mainOrderInfo.setIsHaveRefundHistory(isHaveRefundHistory);
	}
	
	/**
	 * 获得主订单信息
	 *
	 * @param sourceOrderId 网络订单号
	 * @return 订单信息
	 */
	@Override
	public OutFormOrderdetailOrders getMainOrderInfo(String sourceOrderId) {
		OutFormOrderdetailOrders mainOrderInfo = orderDetailDao.getGroupOrdersInfo(sourceOrderId);
		
		return mainOrderInfo;
	}
	
	/**
	 * 订单历史信息取得
	 * 
	 * @param orderNumber 订单号
	 * @return
	 */
	@Override
	public List<OutFormOrderdetailOrderHistory> getOrdersHistoryInfo(String orderNumber) {		
		
		List<OutFormOrderdetailOrderHistory> ret = new ArrayList<OutFormOrderdetailOrderHistory>();
		
		// 订单历史信息
		List<OrdersBean> orderHistoryList = orderDetailDao.getOrderHistoryInfo(orderNumber);
		
		// 网络原订单号缓存
		List<String> sourceOrderIdList = new ArrayList<String>();
		
		for (int i = 0; i < orderHistoryList.size(); i++) {
			OrdersBean ordersInfo = orderHistoryList.get(i);
			
			// 网络原订单号缓存
			sourceOrderIdList.add(ordersInfo.getSourceOrderId());
			
			int existIndex = getExistHistoryIndex(ret, trimDBDateTime(ordersInfo.getOrderDate()));
			// 当前日期存在判定
			if (existIndex == -1) {
				
				// 历史订单信息
				OutFormOrderdetailOrderHistory ordersHistoryInfo = new OutFormOrderdetailOrderHistory();
				// 订单日期
				ordersHistoryInfo.setOrderDate(trimDBDateTime(ordersInfo.getOrderDate()));
				
				// 节点
				OutFormOrderdetailOrderHistoryItem note = new OutFormOrderdetailOrderHistoryItem();
				// 		订单号
				note.setOrderNumber(ordersInfo.getOrderNumber());	
				//		网络订单号
				note.setSourceOrderId(ordersInfo.getSourceOrderId());
				
				ordersHistoryInfo.getOrderNumbers().add(note);
				
				ret.add(ordersHistoryInfo);
			} else {
				// 历史订单信息
				OutFormOrderdetailOrderHistory OrdersHistoryInfo = ret.get(existIndex);
				
				// 节点
				OutFormOrderdetailOrderHistoryItem note = new OutFormOrderdetailOrderHistoryItem();
				// 		订单号
				note.setOrderNumber(ordersInfo.getOrderNumber());	
				//		网络订单号
				note.setSourceOrderId(ordersInfo.getSourceOrderId());
				
				OrdersHistoryInfo.getOrderNumbers().add(note);
			}
		}
		
		// 网络原订单号获取
//		String sourceOrderIds = arrayListToString(sourceOrderIdList);
//		if (!StringUtils.isEmpty(sourceOrderIds)) {
		if (sourceOrderIdList.size() > 0) {
			// 订单历史信息
			List<OrdersBean> sonOrderHistoryList = orderDetailDao.getSonOrderHistoryInfo(sourceOrderIdList);
			
			for (int i = 0; i < sonOrderHistoryList.size(); i++) {
				OrdersBean ordersInfo = sonOrderHistoryList.get(i);
				
				// 子订单与原订单绑定
				setSonOrderHistory(ret, ordersInfo);
			}
		}
		
		return ret;
	}	

	/**
	 * 子订单与原订单绑定
	 * 
	 * @param originalOrdersList 原订单信息
	 * @param sonOrdersInfo 子订单信息
	 * 
	 * @return
	 */
	private void setSonOrderHistory(List<OutFormOrderdetailOrderHistory> originalOrdersList, OrdersBean sonOrdersInfo) {
		for (int j = 0; j < originalOrdersList.size(); j++) {
			OutFormOrderdetailOrderHistory orderHistoryInfo = originalOrdersList.get(j);
			
			// 当日期下订单号遍历
			for (int k = 0; k < orderHistoryInfo.getOrderNumbers().size(); k++) {
				OutFormOrderdetailOrderHistoryItem note = orderHistoryInfo.getOrderNumbers().get(k);
				
				if (note.getSourceOrderId().equals(sonOrdersInfo.getSourceOrderId())) {
					note.getSonOrderNumbers().add(sonOrdersInfo.getOrderNumber());
					
					return;
				}
			}
		}
	}
	
	/**
	 * 订单历史信息取得
	 * 
	 * @param sourceOrderId 网络订单号
	 * @return
	 */
	public List<OutFormOrderdetailOrderHistory> getOrdersHistoryInfoBySourceOrderId(String sourceOrderId, UserSessionBean user) {
		List<OutFormOrderdetailOrderHistory> ret = new ArrayList<OutFormOrderdetailOrderHistory>();
		
		// 订单历史信息
		List<OrdersBean> orderHistoryList = orderDetailDao.getOrderHistoryInfoBySourceOrderId(sourceOrderId);
		
		// 本地时间对应（时区转化）
		for (int i = 0; i < orderHistoryList.size(); i++) {
			int timezone = user.getTimeZone();
			OrdersBean ordersInfo = orderHistoryList.get(i);
			
			//	秒去除
			String orderDate = StringUtils.trimDBDateTimeMs(ordersInfo.getOrderDate());
			//	本地时间转化
			orderDate = DateTimeUtil.getLocalTime(DateTimeUtil.parseStr(orderDate, DateTimeUtil.DEFAULT_DATETIME_FORMAT), timezone);			
			//	日期时间去除
			ordersInfo.setOrderDate(trimDBDateTime(orderDate));
		}
		
		for (int i = 0; i < orderHistoryList.size(); i++) {
			OrdersBean ordersInfo = orderHistoryList.get(i);
			
			int existIndex = getExistHistoryIndex(ret, ordersInfo.getOrderDate());
			// 当前日期存在判定
			if (existIndex == -1) {
				
				// 历史订单信息
				OutFormOrderdetailOrderHistory ordersHistoryInfo = new OutFormOrderdetailOrderHistory();
				// 订单日期
				ordersHistoryInfo.setOrderDate(ordersInfo.getOrderDate());
				
				// 节点
				OutFormOrderdetailOrderHistoryItem note = new OutFormOrderdetailOrderHistoryItem();
				// 		订单号
				note.setOrderNumber(ordersInfo.getOrderNumber());	
				//		网络订单号
				note.setSourceOrderId(ordersInfo.getSourceOrderId());
				
				ordersHistoryInfo.getOrderNumbers().add(note);
				
				ret.add(ordersHistoryInfo);
			} else {
				// 历史订单信息
				OutFormOrderdetailOrderHistory OrdersHistoryInfo = ret.get(existIndex);
				
				// 节点
				OutFormOrderdetailOrderHistoryItem note = new OutFormOrderdetailOrderHistoryItem();
				// 		订单号
				note.setOrderNumber(ordersInfo.getOrderNumber());	
				//		网络订单号
				note.setSourceOrderId(ordersInfo.getSourceOrderId());
				
				OrdersHistoryInfo.getOrderNumbers().add(note);
			}
		}
		
		return ret;
	}
	
	/**
	 * 既存历史记录Index取得
	 * 
	 * @param orderHistoryList 历史记录
	 * @param orderDate 订单日期
	 * @return
	 */
	private int getExistHistoryIndex(List<OutFormOrderdetailOrderHistory> orderHistoryList, String orderDate) {
		int ret = -1;
		
		if (orderHistoryList.size() > 0) {
			for (int i = 0; i < orderHistoryList.size(); i++) {
				if (orderDate.equals(orderHistoryList.get(i).getOrderDate())) {
					ret = i;
					break;
				}
			}
		}
		
		return ret;
	}
	
//	/**
//	 * 获得订单Notes信息
//	 * 
//	 * @return
//	 */
//	@Override
//	public List<OutFormOrderdetailNotes> getOrderNotesInfo(String orderNumber) {
//		// 订单Notes信息
//		List<OutFormOrderdetailNotes> orderNotesList = notesDao.getOrderNotesInfo(orderNumber);
//		// 订单Notes信息，信息再设定
//		setCustomNotesFields(orderNotesList);
//		
//		return orderNotesList;
//	}
	
	/**
	 * 获得订单Notes信息
	 * 
	 * @return
	 */
	@Override
	public List<OutFormOrderdetailNotes> getOrderNotesInfoBySourceOrderId(String sourceOrderId, UserSessionBean user) {
		int timeZone = user.getTimeZone();
		
		// 订单Notes信息
		List<OutFormOrderdetailNotes> orderNotesList = notesDao.getOrderNotesInfoBySourceOrderId(sourceOrderId);
		// 订单Notes信息，信息再设定
		setCustomNotesFields(orderNotesList, timeZone);
		
		return orderNotesList;
	}
	
	/**
	 * 获得订单Tracking信息
	 * 
	 * @return
	 */
	@Override
	public List<OutFormOrderdetailTracking> getOrderTrackingInfo(String orderNumber) {
		// 订单历史信息
		List<OutFormOrderdetailTracking> orderTrackingList = trackingDao.getOrderTrackingInfo(orderNumber);
		
		return orderTrackingList;
	}
	
	/**
	 * 获得订单Shipping信息
	 * 
	 * @return
	 */
	@Override
	public List<OutFormOrderdetailShipping> getOrderShippingInfo(String orderNumber) {
		// 订单历史信息
		List<OutFormOrderdetailShipping> orderShippingList = orderDetailDao.getOrderDetailsShippingInfo(orderNumber);
		
		return orderShippingList;
	}
	
	/**
	 * 获得订单Shipping信息（根据一组订单号）
	 * 
	 * @return
	 */
	@Override
	public List<OutFormOrderdetailShipping> getOrderShippingInfoByOrderNumberList(List<String> orderNumberList) {
		// 订单历史信息
		List<OutFormOrderdetailShipping> orderShippingList = orderDetailDao.getOrderDetailsShippingInfo(orderNumberList);
		
		return orderShippingList;
	}
	
	/**
	 * 获得订单Transactions信息，根据一组（order_number）
	 * 
	 * @return
	 */
	@Override
	public List<OutFormOrderdetailTransactions> getOrderTransactionsInfo(String sourceOrderId, UserSessionBean user) {
		// 当前时区
		int timezone = user.getTimeZone();
		
		// 订单Transaction信息
		List<OutFormOrderdetailTransactions> orderTransactionsList = transactionsDao.getOrderTransactionsInfoBySourceOrderId(sourceOrderId);
		// 订单Transaction信息中，自定义信息设定
		setCustomTransactionFields(orderTransactionsList, timezone);
		
		return orderTransactionsList;
	}
	
	/**
	 * 获得订单Transactions信息，根据一组（order_number）
	 * 
	 * @return
	 */
	@Override
	public List<OutFormOrderdetailPayments> getOrderPaymentsInfo(String sourceOrderId, UserSessionBean user) {
		// 当前时区
		int timezone = user.getTimeZone();
		
		// 订单Transaction信息
		List<OutFormOrderdetailPayments> orderPaymentsList = paymentsDao.getOrdePaymentsInfo(sourceOrderId);
		// 订单Transaction信息中，自定义信息设定
		setCustomPaymentsFields(orderPaymentsList, timezone);
		
		return orderPaymentsList;
	}

    /**
     * 是否存在退款信息
     *
     * @return
     */
    private boolean isHaveRefunds(String sourceOrderId, boolean processFlag) {
        boolean ret = false;

        int retCount = refundDao.getOrderRefundsCount(sourceOrderId, processFlag);
        if (retCount > 0) {
            ret = true;
        }

        return ret;
    }

	/**
	 * 订单列表字段再设定
	 * 
	 * @param ordersList 订单信息
	 * @param timezone 当前时区
	 * @return
	 */
	private void setCustomOrderListFields(List<OutFormOrderdetailOrders> ordersList, int timezone) {
		for (int i = 0; i < ordersList.size(); i++) {			
			OutFormOrderdetailOrders ordersInfo = ordersList.get(i);
			setCustomOrderFields(ordersInfo, timezone);
		}
	}
	
	/**
	 * 订单字段再设定
	 * 
	 * @param ordersInfo 订单信息
	 * @param timezone 当前时区
	 * @return
	 */
	private void setCustomOrderFields(OutFormOrderdetailOrders ordersInfo, int timezone) {
		
		if (ordersInfo != null) {
			
			// 前端页面展示用
			if (LockShip.YES.equals(ordersInfo.getLockShip())) {
				ordersInfo.setLocalShipOnHold(true);
			} else {
				ordersInfo.setLocalShipOnHold(false);
			}
			
			// 订单日期时间
			String orderDateTime = StringUtils.trimDBDateTimeMs(ordersInfo.getOrderDateTime());
			orderDateTime = DateTimeUtil.getLocalTime(DateTimeUtil.parseStr(orderDateTime, DateTimeUtil.DEFAULT_DATETIME_FORMAT), timezone);
			ordersInfo.setOrderDateTime(orderDateTime);
			// approval_date(含时间)
			ordersInfo.setApprovalDate(StringUtils.trimDBDateTimeMs(ordersInfo.getApprovalDate()));
			
			// main orderInfo 移动
//			// productTotal
//			ordersInfo.setProductTotal(StringUtils.getFormatedMoney(ordersInfo.getProductTotal()));
//			// shippingTotal
//			ordersInfo.setShippingTotal(StringUtils.getFormatedMoney(ordersInfo.getShippingTotal()));
//			// grandTotal
//			ordersInfo.setGrandTotal(StringUtils.getFormatedMoney(ordersInfo.getGrandTotal()));
//			// discount
//			ordersInfo.setDiscount(StringUtils.getFormatedMoney(ordersInfo.getDiscount()));
//			// revisedDiscount
//			ordersInfo.setRevisedDiscount(StringUtils.getFormatedMoney(ordersInfo.getRevisedDiscount()));
//			// surcharge
//			ordersInfo.setSurcharge(StringUtils.getFormatedMoney(ordersInfo.getSurcharge()));
//			// revisedSurcharge
//			ordersInfo.setRevisedSurcharge(StringUtils.getFormatedMoney(ordersInfo.getRevisedSurcharge()));
//			// finalProductTotal
//			ordersInfo.setFinalProductTotal(StringUtils.getFormatedMoney(ordersInfo.getFinalProductTotal()));
//			// finalShippingTotal
//			ordersInfo.setFinalShippingTotal(StringUtils.getFormatedMoney(ordersInfo.getFinalShippingTotal()));
//			// finalGrandTotal
//			ordersInfo.setFinalGrandTotal(StringUtils.getFormatedMoney(ordersInfo.getFinalGrandTotal()));
//			// balanceDue
//			ordersInfo.setBalanceDue(StringUtils.getFormatedMoney(ordersInfo.getBalanceDue()));
//			// useTmallPointFee
//			ordersInfo.setUseTmallPointFee(StringUtils.getFormatedMoney(ordersInfo.getUseTmallPointFee()));
////			// expectedNet
////			ordersInfo.setExpectedNet(StringUtils.getFormatedMoney(ordersInfo.getExpectedNet()));
////			// actualNet
////			ordersInfo.setActualNet(StringUtils.getFormatedMoney(ordersInfo.getActualNet()));
			
			
//			// 自定义字段
//			// 		PayTitle 设定
//			double balanceDue = Double.valueOf(ordersInfo.getBalanceDue());
//			if (balanceDue == 0) {
//				ordersInfo.setPayTitleText(PAID_IN_FULL);
//				ordersInfo.setPayTitleValue(PAID_IN_FULL_VAL);
//			} else if (balanceDue > 0) {
//				ordersInfo.setPayTitleText(BALANCE_DUE);
//				ordersInfo.setPayTitleValue(BALANCE_DUE_VAL);
//			} else {
//				ordersInfo.setPayTitleText(CREDIT_DUE);	
//				ordersInfo.setPayTitleValue(CREDIT_DUE_VAL);
//			}
//			
			// 		钱款符号 设定
//			if (CN_CARTIDS.contains(ordersInfo.getCartId())) {
//				ordersInfo.setCurrencyType("￥");
//			} else {
//				ordersInfo.setCurrencyType("$");
//			}
			if (isCNCarts(ordersInfo.getCartId())) { 
				ordersInfo.setCurrencyType(OmsConstants.CurrencyType.RMB);
			} else {
				ordersInfo.setCurrencyType(OmsConstants.CurrencyType.DOLLAR);
			}
			
//			
//			//		旺旺ID（同SoldTo Name）
//			ordersInfo.setWangwangId(ordersInfo.getName());
		}
	}
	
	/**
	 * 订单明细字段再设定
	 * 
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * @return
	 */
	private void setCustomOrderDetailFields(OutFormOrderdetailOrders ordersInfo, 
											List<OutFormOrderDetailOrderDetail> orderDetailsList) {
		if (orderDetailsList != null) {
			
			String trackingPath = Properties.readValue(PropKey.TRACKING_PATH);
			
			// 当前订单sku缓存
			List<String> skuList = new ArrayList<String>();
			// 订单运输渠道（根据订单明细取得）
			String shipChannel = "";
			
			// 初次设定
			for (int i = 0; i < orderDetailsList.size(); i++) {
				OutFormOrderDetailOrderDetail orderDetailsInfo = orderDetailsList.get(i);
				
				// 运输渠道设定
				if (!StringUtils.isEmpty(orderDetailsInfo.getShipChannel())) {
					shipChannel = orderDetailsInfo.getShipChannel();
				}
				
				// pricePerUnit
				orderDetailsInfo.setPricePerUnit(StringUtils.getFormatedMoney(orderDetailsInfo.getPricePerUnit()));
//				// usPricing
//				orderDetailsInfo.setUsPricing(StringUtils.getFormatedMoney(orderDetailsInfo.getUsPricing()));
				
//				// 发货日期
//				orderDetailsInfo.setDateShipped(StringUtils.trimDBDateTimeMs(orderDetailsInfo.getDateShipped()));
//				// 更新日
//				orderDetailsInfo.setModified(StringUtils.getDate(orderDetailsInfo.getModified()));
				
				// 真实物品的场合
				if (isTrueSKU(orderDetailsInfo.getSku())) {
					
					// 路由信息
					if (!StringUtils.isEmpty(orderDetailsInfo.getSynShipNo())) {
						orderDetailsInfo.setSynShipPath(trackingPath + orderDetailsInfo.getSynShipNo());
					}
					
					// 当前订单sku缓存
					if (!skuList.contains(orderDetailsInfo.getSku())) {
						skuList.add(orderDetailsInfo.getSku());
					}
					
//					// 根据 WebService 获得
//	//				String imgPath = "http://image.sneakerhead.com/is/image/sneakerhead/tmall-imgn?$460$&$img=nike-women-dunk-sky-hi-sneaker-boot-616738001-1&layer=2&originN=0,.5&pos=0,105";
//					String imgPath = "http://gi4.md.alicdn.com/bao/uploaded/i4/TB15bt0HpXXXXbuXVXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg";
//					orderDetailsInfo.setImgPath(OmsUrlConstants.URL_OMS_ORDER_DETAILS_SEARCH + "/doGetDetailPic.html?imgPath=" + imgPath);
//					
//					String skuTmallPath = "http://detail.tmall.hk/hk/item.htm?spm=a1z10.3-b.w4011-3568037628.140.XIEbhB&id=44839276086&rn=ae9a7f033e50b4a8a46ab6e0244a8e20&abbucket=16";
//					orderDetailsInfo.setSkuTmallPath(skuTmallPath);
				}
				
				if (OrderDetailSkuDsp.PRODUCT_TITLE.endsWith(orderDetailsInfo.getSku())) {
					//  return 明细信息不显示（例：Returned (1) 11821313-6: Dr. Martens 1460 W 马丁大夫经典八孔女子真皮马丁靴 美国正品） 
					orderDetailsInfo.setShowFlag(false);
					
				} else if (StringUtils.isEmpty(orderDetailsInfo.getSubItemNumber()) || "0".equals(orderDetailsInfo.getSubItemNumber())) {
					// 普通明细信息
					orderDetailsInfo.setShowFlag(true);
				} else {
					// 关联打折信息
					orderDetailsInfo.setShowFlag(false);
				}
				
				// 物品的场合
				if (!orderDetailsInfo.isAdjustment()) {
					// 打折信息加算
					float discount = 0;
					for (int j = 0; j < orderDetailsList.size(); j++) {
						OutFormOrderDetailOrderDetail orderDetailsDiscountInfo = orderDetailsList.get(j);
						if (orderDetailsInfo.getItemNumber().equals(orderDetailsDiscountInfo.getSubItemNumber())) {
							discount = add2Digits(discount, Float.valueOf(orderDetailsDiscountInfo.getPricePerUnit()));
						}
					}
					
					// 打折信息设定
					if (discount != 0) {
						orderDetailsInfo.setDiscount(String.valueOf(discount));
					}
					
					// 售价设定（该处discount 已经 *-1）
					float price = add2Digits(Float.valueOf(orderDetailsInfo.getPricePerUnit()), discount);
					orderDetailsInfo.setPrice(String.valueOf(price));
				}
			}
			
			// 订单运输渠道设定
			ordersInfo.setShipChannel(shipChannel);
			
			// WebService 调用
			InFormServiceSearchSKU inFormServiceSearchSKU = new InFormServiceSearchSKU();
			inFormServiceSearchSKU.setChannelId(ordersInfo.getOrderChannelId());
			inFormServiceSearchSKU.setCartId(ordersInfo.getCartId());
			String skuJson = "";
			skuJson = JsonUtil.getJsonString(skuList);
			inFormServiceSearchSKU.setSkuJson(skuJson);
			inFormServiceSearchSKU.setSkuList(skuList);
			List<OutFormServiceSearchSKU> skuInfoList = omsCommonService.getSKUList(inFormServiceSearchSKU,OmsConstants.SKU_TYPE_ORDERDETAIL);
			
			if (skuInfoList.size() > 0) {
				// 明细再次设定（sku 信息）
				for (int i = 0; i < orderDetailsList.size(); i++) {
					OutFormOrderDetailOrderDetail orderDetailsInfo = orderDetailsList.get(i);
					
					OutFormServiceSearchSKU skuInfo = getSkuInfo(orderDetailsInfo.getSku(), skuInfoList);
					if (skuInfo != null) {
						// 根据 WebService 获得
						// 产品图片
						String imgPath = skuInfo.getImgPath();
						orderDetailsInfo.setImgPath(imgPath);
						
						// 天猫路径
						String skuTmallPath = skuInfo.getSkuTmallUrl();
						orderDetailsInfo.setSkuTmallPath(skuTmallPath);
						
						// 库存
						orderDetailsInfo.setInventory(skuInfo.getInventory());
					}
				}				
			}
		}
	}
	
	/**
	 * 对应sku信息取得
	 * 
	 * @param sku 待取得sku
	 * @param skuInfoList sku信息一览 
	 * 
	 * @return
	 */
	private OutFormServiceSearchSKU getSkuInfo(String sku, List<OutFormServiceSearchSKU> skuInfoList) {
		OutFormServiceSearchSKU ret = null;

		for (int i = 0; i < skuInfoList.size(); i++) {
			OutFormServiceSearchSKU skuInfo = skuInfoList.get(i);
			
			// 找到对应sku信息的场合
			if (skuInfo.getSku().equalsIgnoreCase(sku)) {
				ret = skuInfo;
				
				break;
			}
		}
		
		return ret;
	}
	
	/**
	 * Transaction自定义字段设定
	 * 
	 * @param orderTransactionsList 订单交易信息
	 * @param timezone 时区
	 * @return
	 */
	private void setCustomTransactionFields(List<OutFormOrderdetailTransactions> orderTransactionsList, int timezone) {
		
		if (orderTransactionsList != null) {
			
			for (int i = 0; i < orderTransactionsList.size(); i++) {
				OutFormOrderdetailTransactions transactionsInfo = orderTransactionsList.get(i);

				String transactionTime = StringUtils.trimDBDateTimeMs(transactionsInfo.getTransactionTime());
				transactionTime = DateTimeUtil.getLocalTime(DateTimeUtil.parseStr(transactionTime, DateTimeUtil.DEFAULT_DATETIME_FORMAT), timezone);
				
				transactionsInfo.setTransactionTime(transactionTime);
			}
		}
	}
	
	/**
	 * Payments自定义字段设定
	 * 
	 * @param orderPaymentsList 订单付款信息
	 * @param timezone 时区
	 * @return
	 */
	private void setCustomPaymentsFields(List<OutFormOrderdetailPayments> orderPaymentsList, int timezone) {
		
		if (orderPaymentsList != null) {
			
			for (int i = 0; i < orderPaymentsList.size(); i++) {
				OutFormOrderdetailPayments paymentsInfo = orderPaymentsList.get(i);
				
				String paymentTime = StringUtils.trimDBDateTimeMs(paymentsInfo.getPaymentTime());
				paymentTime = DateTimeUtil.getLocalTime(DateTimeUtil.parseStr(paymentTime, DateTimeUtil.DEFAULT_DATETIME_FORMAT), timezone);
				
				// 时间去除
				paymentsInfo.setPaymentTime(paymentTime);
			}
		}
	}
	
	/**
	 * 订单明细字段再设定
	 * 
	 * @param orderNotesList 订单Notes信息
	 * @param timezone 时区
	 * @return
	 */
	private void setCustomNotesFields(List<OutFormOrderdetailNotes> orderNotesList, int timezone) {
		if (orderNotesList != null) {
			for (int i = 0; i < orderNotesList.size(); i++) {
				OutFormOrderdetailNotes notesInfo = orderNotesList.get(i);
				
//				// TODO imgPath
//				notesInfo.setImgPath(OmsUrlConstants.URL_OMS_ORDER_DETAILS_SEARCH + "/doGetNotesPic.html?imgPath=panda");
//				// 输入时间
//				notesInfo.setEntryTime(StringUtils.getDateTime(StringUtils.trimDBDateTimeMs(notesInfo.getEntryDate()), StringUtils.trimDBDateTimeMs(notesInfo.getEntryTime())));				
//				// 输入日
//				notesInfo.setEntryDate(StringUtils.getDate(StringUtils.trimDBDateTimeMs(notesInfo.getEntryDate())));

				// 文件路径
				if (!StringUtils.isEmpty(notesInfo.getFilePath())) {
					String fileName = String.format("%s_%02d", String.valueOf(notesInfo.getNumericKey()), Integer.valueOf(notesInfo.getItemNumber()));
					// imgPath
					notesInfo.setImgPath(OmsUrlConstants.URL_OMS_ORDER_DETAILS_SEARCH + "/doGetNotesPic.html?imgPath=" + notesInfo.getFilePath() + File.separator + fileName);					
				}
				
				String entryDate = StringUtils.trimDBDateTimeMs(notesInfo.getEntryDate());
				entryDate = DateTimeUtil.getLocalTime(DateTimeUtil.parseStr(entryDate, DateTimeUtil.DEFAULT_DATETIME_FORMAT), timezone);
				// 输入时间
				notesInfo.setEntryTime(StringUtils.getDateTime(entryDate, entryDate));				
				// 输入日
				notesInfo.setEntryDate(StringUtils.getDate(entryDate));

			}			
		}
	}
	
	/**
	 * 订单明细字段再设定
	 * 
	 * @param user 登陆用户信息
	 * @param orderInfo 订单信息
	 * @return
	 */
	@Override
	public boolean isAuthorized(UserSessionBean user, OutFormOrderdetailOrders orderInfo) {
		
		boolean ret = false;
		
		if (user != null && orderInfo != null) {
			HashMap<String, PermissionBean> propertyPermissions = user.getPropertyPermissions();
			Iterator<String> it = propertyPermissions.keySet().iterator();

			while (it.hasNext()) {
				String propertyId = it.next();
				
				if (propertyId.equals(orderInfo.getOrderChannelId())) {

					ret = true;
					
					break;
				}
			}			
		}
		
		return ret;
	}
	
	/**
	 * Notes图片取得
	 * 
	 * @return
	 * @throws Exception 
	 */
	@Override
	public void getNotesPic(HttpServletRequest request,	HttpServletResponse response, String imgPath) throws Exception {
    	String filePath = Properties.readValue(PropKey.NOTE_IMG_PATH) + imgPath + ".jpg";
    	
		ImgUtils.getPicStream(request, response, filePath);
	}
	
	/**
	 * SKU图片取得
	 * 
	 * @return
	 */
	@Override
	public void getSKUPic(HttpServletRequest request, HttpServletResponse response, String imgPath) throws Exception {
		
		ImgUtils.getPicStream(request, response, imgPath);
	}
	
	/**
	 * 订单调整保存
	 * 
	 * @param inFormOrderdetailAdjustmentItem 输入调整信息
	 * @param user 当前用户
	 * @return
	 */
	@Override
	public boolean saveAdjustment(InFormOrderdetailAdjustmentItem inFormOrderdetailAdjustmentItem, UserSessionBean user) {
		boolean ret = true;
		
		logger.info("saveAdjustment start");
		
		TransactionStatus status=transactionManager.getTransaction(def);
		
		try {
			String orderNumber = inFormOrderdetailAdjustmentItem.getOrderNumber();
			// 		item_number 取得（当前最大item_number + 1）
			int itemNumber = orderDetailDao.getOrderDetailsMaxItemNumber(orderNumber) + 1;
			
			// 当前订单信息取得
			// 		订单信息取得
			OutFormOrderdetailOrders ordersInfo = orderDetailDao.getOrdersInfo(orderNumber);
			// 		订单详情取得
			List<OutFormOrderDetailOrderDetail> orderDetailsList = orderDetailDao.getOrderDetailsInfo(orderNumber);
			
			// 订单价格计算
			OrderPrice orderPrice = getOrderPriceMain(inFormOrderdetailAdjustmentItem, ordersInfo, orderDetailsList);
			
			// 主订单价格取得
			OrderPrice mainOrderPrice = getMainOrderPrice(inFormOrderdetailAdjustmentItem.getSourceOrderId(), orderPrice);
			
			// 订单明细保存
			logger.info("saveOrderDetail");
			ret = saveOrderDetail(inFormOrderdetailAdjustmentItem, itemNumber, orderPrice, ordersInfo.getOrderChannelId(), user);
			
			// 订单金额变更
			if (ret) {
				logger.info("saveOrder");
				ret = saveOrder(inFormOrderdetailAdjustmentItem, orderPrice, user);
			}
			
			// 一组订单价格更新
			if (ret) {
				logger.info("saveGroupOrderPrice");
				ret = saveGroupOrderPrice(mainOrderPrice, ordersInfo, user);
			}
			
			// 订单Notes保存
			if (ret) {
				logger.info("saveOrderNotes");
				String noteId = saveOrderNotes(inFormOrderdetailAdjustmentItem, itemNumber, user);
				if (StringUtils.isEmpty(noteId)) {
					ret = false;
				} else {
					// product 信息
					logger.info("getAdjustmentProduct");
					String[] productSkuInsert = getAdjustmentProduct(inFormOrderdetailAdjustmentItem);
					// transactions 信息更新
					// orderNumber, description, debt, credit, noteId, user
					ret = saveOrderTransactionsInfo(inFormOrderdetailAdjustmentItem.getSourceOrderId(),
													ordersInfo.getOriginSourceOrderId(),
													orderNumber,
													productSkuInsert[SKU_TITLE_INDEX],
													String.valueOf(itemNumber),
													productSkuInsert[SKU_TITLE_INDEX],
													inFormOrderdetailAdjustmentItem.getAdjustmentNumber(),
													noteId,
													OmsConstants.Transaction_Type.REFUND,
													false,
													user);
				}
			}
			
			//	synShip 同步
			if (ret) {
				logger.info("syncSynshipFinalGrandTotal");
				ret = syncSynshipFinalGrandTotal(orderPrice, user);
			}
			
			if (ret) {
				logger.info("saveAdjustment success");
				transactionManager.commit(status);
			} else {
				logger.info("saveAdjustment error");
				transactionManager.rollback(status);
			}
			
		} catch (Exception e) {
			ret = false;
			
			logger.error("saveAdjustment", e);
			
			transactionManager.rollback(status);
		}
		
		return ret;
	}
	
	/**
	 * 同步synShip final_grand_total
	 * 
	 * @param orderPrice 订单价格
	 * @param user 当前用户 
	 * 
	 * @return 订单价格
	 */
	private boolean syncSynshipFinalGrandTotal(OrderPrice orderPrice, UserSessionBean user) {
		boolean ret = true;
		
		OutFormOrderdetailOrders orderInfo = new OutFormOrderdetailOrders();
		
		//	订单号		
		orderInfo.setOrderNumber(orderPrice.getOrderNumber());
		//	final_grand_total
		orderInfo.setFinalGrandTotal(String.valueOf(orderPrice.getFinalGrandTotal()));
		//	更新用户
		orderInfo.setModifier(user.getUserName());
		
		ret = synShipSyncDao.updateFinalGrandTotal(orderInfo);
		
		return ret;
	}
	
	/**
	 * 同步synShip final_grand_total
	 * 
	 * @param orderPrice 订单价格
	 * @param orderStatus 订单状态
	 * @param user 当前用户 
	 * 
	 * @return 订单价格
	 */
	private boolean syncSynshipFinalGrandTotalAndOrderStatus(OrderPrice orderPrice, String orderStatus, UserSessionBean user) {
		boolean ret = true;
		
		OutFormOrderdetailOrders orderInfo = new OutFormOrderdetailOrders();
		
		//	订单号		
		orderInfo.setOrderNumber(orderPrice.getOrderNumber());
		//	订单状态
		orderInfo.setOrderStatus(orderStatus);
		//	final_grand_total
		orderInfo.setFinalGrandTotal(String.valueOf(orderPrice.getFinalGrandTotal()));
		//	更新用户
		orderInfo.setModifier(user.getUserName());
		
		ret = synShipSyncDao.updateFinalGrandTotal(orderInfo);
		
		return ret;
	}
	
	/**
	 * 同步synShip se_order_status
	 * 
	 * @param orderNumber 订单号
	 * @param orderStatus 订单状态
	 * @param user 当前用户 
	 * 
	 * @return 订单价格
	 */
	private boolean syncSynshipSEOrderStatus(String orderNumber, String orderStatus, UserSessionBean user) {
		boolean ret = true;
		
		OrdersBean orderInfo = new OrdersBean();
		
		//	订单号		
		orderInfo.setOrderNumber(orderNumber);
		//	orderStatus
		orderInfo.setOrderStatus(orderStatus);
		//	更新用户
		orderInfo.setModifier(user.getUserName());
		
		ret = synShipSyncDao.updateOrderStatus(orderInfo);
		
		return ret;
	}
	
	/**
	 * 取得订单金额 主处理
	 * 
	 * @param inFormOrderdetailAdjustmentItem 输入调整信息
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * 
	 * @return 订单价格
	 */
	private OrderPrice getOrderPriceMain(InFormOrderdetailAdjustmentItem inFormOrderdetailAdjustmentItem, OutFormOrderdetailOrders ordersInfo, List<OutFormOrderDetailOrderDetail> orderDetailsList) {
		
//		String orderNumber = inFormOrderdetailAdjustmentItem.getOrderNumber();
		
		// 订单价格
		OrderPrice orderPrice = getOrderPrice(ordersInfo);
		
		// 设定调整价格
		setAdjustPrice(inFormOrderdetailAdjustmentItem, orderPrice);
		
		// 价格再计算
		priceReCalculate(orderPrice);
		
		// 明细价格再计算
//		setItemAdjustPrice(orderDetailsList, orderPrice);
		//	本次明细价格不需要再计算
		orderPrice.setItemAdjustPrice(Float.valueOf(inFormOrderdetailAdjustmentItem.getAdjustmentNumber()));
		
		return orderPrice;
	}
	
	/**
	 * 主订单金额 取得
	 * 
	 * @param sourceOrderId 网络订单号
	 * @param orderPrice 订单价格
	 * 
	 * @return 订单价格
	 */
	private OrderPrice getMainOrderPrice(String sourceOrderId, OrderPrice orderPrice) {
		// 一组订单金额检索
		OutFormOrderdetailOrders mainOrdersInfo = orderDetailDao.getGroupOrdersInfo(sourceOrderId);
		
		// 一组订单价格取得
		OrderPrice mainOrderPrice = getOrderPrice(mainOrdersInfo);
		
		// final_grand_total
		float finalGrandTotal = sub2Digits(mainOrderPrice.getFinalGrandTotal(), orderPrice.getOrigFinalGrandTotal());
		finalGrandTotal = add2Digits(finalGrandTotal, orderPrice.getFinalGrandTotal());
		mainOrderPrice.setFinalGrandTotal(finalGrandTotal);
		
		// final_shipping_total
		float finalShippingTotal = sub2Digits(mainOrderPrice.getFinalShippingTotal(), orderPrice.getOrigFinalShippingTotal());
		finalShippingTotal = add2Digits(finalShippingTotal, orderPrice.getFinalShippingTotal());
		mainOrderPrice.setFinalShippingTotal(finalShippingTotal);
		
		// revised_surcharge
		float revisedSurcharge = sub2Digits(mainOrderPrice.getRevisedSurcharge(), orderPrice.getOrigRevisedSurcharge());
		revisedSurcharge = add2Digits(revisedSurcharge, orderPrice.getRevisedSurcharge());
		mainOrderPrice.setRevisedSurcharge(revisedSurcharge);
		
		// revised_discount
		float revisedDiscount = sub2Digits(mainOrderPrice.getRevisedDiscount(), orderPrice.getOrigRevisedDiscount());
		revisedDiscount = add2Digits(revisedDiscount, orderPrice.getRevisedDiscount());
		mainOrderPrice.setRevisedDiscount(revisedDiscount);
		
		// final_product_total
		float finalProductTotal = sub2Digits(mainOrderPrice.getFinalProductTotal(), orderPrice.getOrigFinalProductTotal());
		finalProductTotal = add2Digits(finalProductTotal, orderPrice.getFinalProductTotal());
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
		
		if (ordersInfo.getSourceOrderId() != null) {
			// source_order_id
			orderPrice.setSourceOrderId(ordersInfo.getSourceOrderId());
		}
		
		if (ordersInfo.getOrderNumber() != null) {
			// order number
			orderPrice.setOrderNumber(ordersInfo.getOrderNumber());
		}
		
		// product Total
		if (ordersInfo.getProductTotal() != null) {
			orderPrice.setProductTotal(Float.valueOf(ordersInfo.getProductTotal()));			
		}
		// revised product Total
		orderPrice.setFinalProductTotal(Float.valueOf(ordersInfo.getFinalProductTotal()));
		
		// surcharge
		if (ordersInfo.getSurcharge() != null) {
			orderPrice.setSurcharge(Float.valueOf(ordersInfo.getSurcharge()));
		}
		// revised surcharge
		orderPrice.setRevisedSurcharge(Float.valueOf(ordersInfo.getRevisedSurcharge()));
		
		// discount
		if (ordersInfo.getDiscount() != null) {
			orderPrice.setDiscount(Float.valueOf(ordersInfo.getDiscount()));			
		}
		// revised discount
		orderPrice.setRevisedDiscount(Float.valueOf(ordersInfo.getRevisedDiscount()));
		
		// shipping Total
		if (ordersInfo.getShippingTotal() != null) {
			orderPrice.setShippingTotal(Float.valueOf(ordersInfo.getShippingTotal()));	
		}
		// revised shipping
		orderPrice.setFinalShippingTotal(Float.valueOf(ordersInfo.getFinalShippingTotal()));
		
		// grand total
		if (ordersInfo.getGrandTotal() != null) {
			orderPrice.setGrandTotal(Float.valueOf(ordersInfo.getGrandTotal()));			
		}
		// revised grand total
		orderPrice.setFinalGrandTotal(Float.valueOf(ordersInfo.getFinalGrandTotal()));
		
		// 删除
//		// expected_net
//		orderPrice.setExpectedNet(Float.valueOf(ordersInfo.getExpectedNet()));
//		// actual_net
//		orderPrice.setActualNet(Float.valueOf(ordersInfo.getActualNet()));
//		// balanceDue
//		if (ordersInfo.getBalanceDue() != null) {
//			orderPrice.setBalanceDue(Float.valueOf(ordersInfo.getBalanceDue()));	
//		}
		// expected 主订单用
		if (ordersInfo.getExpected() != null) {
			orderPrice.setExpected(Float.valueOf(ordersInfo.getExpected()));
		}
		
		// 修正前价格
		orderPrice.setOrigFinalGrandTotal(orderPrice.getFinalGrandTotal());
		// product
		orderPrice.setOrigFinalProductTotal(orderPrice.getFinalProductTotal());
		// shipping
		orderPrice.setOrigFinalShippingTotal(orderPrice.getFinalShippingTotal());
		// discount
		orderPrice.setOrigRevisedDiscount(orderPrice.getRevisedDiscount());
		// surcharge
		orderPrice.setOrigRevisedSurcharge(orderPrice.getRevisedSurcharge());
		
		return orderPrice;
	}
	
	/**
	 * 设定调整价格
	 * 
	 * @param inAdjustmentItem 输入调整信息
	 * @param orderPrice 订单价格（I/O）
	 * @return
	 */
	private void setAdjustPrice(InFormOrderdetailAdjustmentItem inAdjustmentItem, OrderPrice  orderPrice) {
		
		Float adjustPrice = 0f;
		
		// surcharge
		if (OmsConstants.AdjustmentType.SURCHARGE.equals(inAdjustmentItem.getAdjustmentType())) {
//			adjustPrice = orderPrice.getRevisedSurcharge() + Float.valueOf(inAdjustmentItem.getAdjustmentNumber());
			adjustPrice = add2Digits(orderPrice.getRevisedSurcharge() , Float.valueOf(inAdjustmentItem.getAdjustmentNumber()));
			
			orderPrice.setRevisedSurcharge(adjustPrice);			
		}
		
		// discount
		if (OmsConstants.AdjustmentType.DISCOUNT.equals(inAdjustmentItem.getAdjustmentType())) {
			// 不打折
			if (OmsConstants.DiscountType.NODISCOUNT.equals(inAdjustmentItem.getAdjustmentDiscountType())) {
				
				orderPrice.setRevisedDiscount(0);
			
			// 手工输入
			} else if (OmsConstants.DiscountType.MANUAL.equals(inAdjustmentItem.getAdjustmentDiscountType())) {
//				adjustPrice = orderPrice.getRevisedDiscount() + Float.valueOf(inAdjustmentItem.getAdjustmentNumber()) * -1;
				adjustPrice = add2Digits(orderPrice.getRevisedDiscount(), Float.valueOf(inAdjustmentItem.getAdjustmentNumber()));
				
				orderPrice.setRevisedDiscount(adjustPrice);
				
			// 百分比
			} else if (OmsConstants.DiscountType.PERCENT.equals(inAdjustmentItem.getAdjustmentDiscountType())) {
//				adjustPrice = orderPrice.getFinalProductTotal() * Float.valueOf(inAdjustmentItem.getAdjustmentNumber()) * -1;
				adjustPrice = mult2Digits(orderPrice.getFinalProductTotal(), (Float.valueOf(inAdjustmentItem.getAdjustmentNumber())/100) * -1);
				
				orderPrice.setRevisedDiscount(adjustPrice);
			}
		}
		
		// coupon
		if (OmsConstants.AdjustmentType.COUPON.equals(inAdjustmentItem.getAdjustmentType())) {
			
		}
		
		// shipping
		if (OmsConstants.AdjustmentType.SHIPPING.equals(inAdjustmentItem.getAdjustmentType())) {
//			adjustPrice = orderPrice.getFinalShippingTotal() + Float.valueOf(inAdjustmentItem.getAdjustmentNumber());
			adjustPrice = add2Digits(orderPrice.getFinalShippingTotal() , Float.valueOf(inAdjustmentItem.getAdjustmentNumber()));
			
			orderPrice.setFinalShippingTotal(adjustPrice);
		}
	}
	
	/**
	 * 调整价格计算
	 *
	 * @param orderPrice 订单价格（I/O）
	 * @return
	 *
	 *
	 */
	private void priceReCalculate(OrderPrice  orderPrice) {
		
		// revised grand total
//		float finalGrandTotal = orderPrice.getFinalProductTotal() + 
//								orderPrice.getRevisedSurcharge() + 
//								orderPrice.getRevisedDiscount() + 
//								orderPrice.getFinalShippingTotal();
		float finalGrandTotal = add2Digits(add2Digits(add2Digits(orderPrice.getFinalProductTotal(), orderPrice.getRevisedSurcharge()), orderPrice.getRevisedDiscount()), orderPrice.getFinalShippingTotal());
		orderPrice.setFinalGrandTotal(finalGrandTotal);
		
//		// expected_net
//		orderPrice.setExpectedNet(finalGrandTotal);
		
		// group_orders 移动预订
//		// balancedue
//		float balancedue = sub2Digits(orderPrice.getExpectedNet() , orderPrice.getActualNet());
//		orderPrice.setBalanceDue(balancedue);
	}
	
	/**
	 * 明细调整价格计算
	 * @param orderDetailsList 订单明细 
	 * @param orderPrice 订单价格（I/O）
	 * 
	 */
	private void setItemAdjustPrice(List<OutFormOrderDetailOrderDetail> orderDetailsList, OrderPrice  orderPrice) {
		float orgPrice = 0f;
		float itemPrice = 0f; 
		
		for (int i = 0; i < orderDetailsList.size(); i++) {
//			orgPrice = orgPrice + Float.valueOf(orderDetailsList.get(i).getPricePerUnit());
			
			// cancelled 以外项目的场合
//			if (!OmsCodeConstants.OrderStatus.CANCELED.equals(orderDetailsList.get(i).getStatus())) {
			if (!isCanceledProduct(orderDetailsList.get(i))) {
				orgPrice = add2Digits(orgPrice , Float.valueOf(orderDetailsList.get(i).getPricePerUnit()));
			}
		}
		
//		itemPrice = orderPrice.getFinalGrandTotal() -  orgPrice;
		itemPrice = sub2Digits(orderPrice.getFinalGrandTotal() ,  orgPrice);
		
		orderPrice.setItemAdjustPrice(itemPrice);
	}
	
	/**
	 * 订单保存（订单修正）
	 * 
	 * @param inFormOrderdetailAdjustment 输入调整信息
	 * @param orderPrice 订单价格
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrder(InFormOrderdetailAdjustmentItem inFormOrderdetailAdjustment, OrderPrice orderPrice, UserSessionBean user) {
		boolean ret = true;
		
		// 订单
		OrdersBean ordersInfo = setOrderPrice(orderPrice);

		// 打折修正的场合
		if (OmsConstants.AdjustmentType.DISCOUNT.equals(inFormOrderdetailAdjustment.getAdjustmentType())) {
			//		discount_type
			ordersInfo.setDiscountType(inFormOrderdetailAdjustment.getAdjustmentDiscountType());
			
			// 		百分比打折的场合
			if (OmsConstants.DiscountType.PERCENT.equals(inFormOrderdetailAdjustment.getAdjustmentDiscountType())) {
				//		discount_percent
//				ordersInfo.setDiscountPercent(String.valueOf(Float.valueOf(inFormOrderdetailAdjustment.getAdjustmentNumber())/100));
				ordersInfo.setDiscountPercent(String.valueOf(div4Digits(Float.valueOf(inFormOrderdetailAdjustment.getAdjustmentNumber()) , 100)));
			} else {
				//		discount_percent
				ordersInfo.setDiscountPercent("0");
			}
		}
		
		// 		modifier
		ordersInfo.setModifier(user.getUserName());
		
		ret = orderDetailDao.updateOrdersInfo(ordersInfo);
		
		return ret;
	}
		
	/**
	 * 订单金额设定
	 * 
	 * @param orderPrice 订单价格
	 * 
	 * @return 订单Bean
	 */
	private OrdersBean setOrderPrice(OrderPrice orderPrice) {
		OrdersBean ordersInfo = new OrdersBean();
		
		//		source_order_id（一组订单时，设定）
		ordersInfo.setSourceOrderId(orderPrice.getSourceOrderId());
		
		// 		order_number（单个订单时，设定）
		ordersInfo.setOrderNumber(orderPrice.getOrderNumber());
		
		//		product_total
		ordersInfo.setProductTotal(String.valueOf(orderPrice.getProductTotal()));
		//		final_product_total
		ordersInfo.setFinalProductTotal(String.valueOf(orderPrice.getFinalProductTotal()));
		//		surcharge
		ordersInfo.setSurcharge(String.valueOf(orderPrice.getSurcharge()));
		//		revised_surcharge
		ordersInfo.setRevisedSurcharge(String.valueOf(orderPrice.getRevisedSurcharge()));
		//		discount
		ordersInfo.setDiscount(String.valueOf(orderPrice.getDiscount()));
		//		revised_discount
		ordersInfo.setRevisedDiscount(String.valueOf(orderPrice.getRevisedDiscount()));
		//		shipping_total
		ordersInfo.setShippingTotal(String.valueOf(orderPrice.getShippingTotal()));
		//		final_shipping_total
		ordersInfo.setFinalShippingTotal(String.valueOf(orderPrice.getFinalShippingTotal()));
		//		grand_total
		ordersInfo.setGrandTotal(String.valueOf(orderPrice.getGrandTotal()));
		//		final_grand_total
		ordersInfo.setFinalGrandTotal(String.valueOf(orderPrice.getFinalGrandTotal()));
//		//		expected_net
//		ordersInfo.setExpectedNet(String.valueOf(orderPrice.getExpectedNet()));
//		//		actual_net
//		ordersInfo.setActualNet(String.valueOf(orderPrice.getActualNet()));
//		//		balance_due
//		ordersInfo.setBalanceDue(String.valueOf(orderPrice.getBalanceDue()));
		
		return ordersInfo;
	}
	
	/**
	 * 订单明细保存
	 * 
	 * @param inFormOrderdetailAdjustmentItem 输入调整信息
	 * @param itemNumber 明细番号
	 * @param orderPrice 调整价格
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrderDetail(InFormOrderdetailAdjustmentItem inFormOrderdetailAdjustmentItem, int itemNumber, OrderPrice orderPrice, String orderChannelId, UserSessionBean user) {
		boolean ret = true;
		
		// 订单明细
		OrderDetailsBean orderDetailsInfo = new OrderDetailsBean();
		
		// Insert Product sku取得
		String[] productSkuInsert = getAdjustmentProduct(inFormOrderdetailAdjustmentItem);
		
		// 		order_number
		orderDetailsInfo.setOrderNumber(inFormOrderdetailAdjustmentItem.getOrderNumber());
		// 		item_number
		orderDetailsInfo.setItemNumber(String.valueOf((itemNumber)));
		// 		adjustment
		orderDetailsInfo.setAdjustment(true);
		// 		product
		orderDetailsInfo.setProduct(productSkuInsert[PRODUCT_TITLE_INDEX]);
		// 		price_per_unit
		orderDetailsInfo.setPricePerUnit(String.valueOf(orderPrice.getItemAdjustPrice()));
		// 		quantity_ordered
		orderDetailsInfo.setQuantityOrdered("1");
		//		quantity_shippped
		orderDetailsInfo.setQuantityShipped("1");
		//		quantity_returned
		orderDetailsInfo.setQuantityReturned("0");
		//		resAllotFlg
		orderDetailsInfo.setResAllotFlg(true);
		//		syncSynship
		orderDetailsInfo.setSyncSynship(true);
		// 		sku
		orderDetailsInfo.setSku(productSkuInsert[SKU_TITLE_INDEX]);
		//		res_id
		orderDetailsInfo.setResId("0");
		// 		creater
		orderDetailsInfo.setCreater(user.getUserName());
		// 		modifier
		orderDetailsInfo.setModifier(user.getUserName());
		
		ret = orderDetailDao.insertOrderDetailsInfo(orderDetailsInfo);

		if (ret) {
			ret = insertExtTable(orderDetailsInfo, orderChannelId);
		}

		return ret;
	}

	/**
	 * 订单明细保存
	 *
	 * @param orderDetailsInfo 订单明细
	 * @param itemNumber 明细番号
	 * @param orderChannelId 订单渠道
	 *
	 * @return
	 */
	private boolean insertExtTable(OrderDetailsBean orderDetailsInfo, String orderChannelId) {
		boolean ret = true;

		boolean isInsertIntoExtTable = isInsertIntoExtTable(orderChannelId);
		if (isInsertIntoExtTable) {
			ret = insertExtOrderDetailsInfo(orderDetailsInfo);
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
	 * Ext order details info 保存
	 *
	 * @param orderDetailsInfo 订单明细
	 *
	 * @return
	 */
	private boolean insertExtOrderDetailsInfo(OrderDetailsBean orderDetailsInfo) {
		boolean ret = true;

		ret = orderDetailDao.insertExtOrderDetailsInfo(orderDetailsInfo);

		return ret;
	}

	/**
	 * 订单Note保存
	 * 
	 * @param inFormOrderdetailAdjustmentItem 输入调整信息
	 * @param itemNumber 明细番号
	 * @param user 当前用户
	 * @return
	 */
	private String saveOrderNotes(InFormOrderdetailAdjustmentItem inFormOrderdetailAdjustmentItem, int itemNumber, UserSessionBean user) {
		String ret = "";
		
		// Insert Product sku notes取得
		String[] productSkuInsert = getAdjustmentProduct(inFormOrderdetailAdjustmentItem);
		String notes = "item #" + itemNumber + " " + productSkuInsert[NOTE_INDEX];
		
//		// 订单Notes
//		NotesBean notesInfo = new NotesBean();
		//		//		type
//		notesInfo.setType(OmsConstants.NotesType.SYSTEM);
//		// 		order_number
//		notesInfo.setNumericKey(inFormOrderdetailAdjustmentItem.getOrderNumber());
//		//		item_number
//		notesInfo.setItemNumber(String.valueOf(itemNumber));
//		//		notes
//		notesInfo.setNotes(notes);
//		//		entered_by
//		notesInfo.setEnteredBy(OmsConstants.SYS_USER);
//		//		modifier
//		notesInfo.setModifier(user.getUserName());
//		
//		ret = notesDao.insertOrdersInfo(notesInfo);
		
		ret = addNotes(inFormOrderdetailAdjustmentItem.getSourceOrderId(),
				OmsConstants.NotesType.SYSTEM, 
				inFormOrderdetailAdjustmentItem.getOrderNumber(),
				notes,
				user.getUserName(),
				user.getUserName());
		
		return ret;
	}
	
	/**
	 * 调整名称取得
	 * 
	 * @param inFormOrderdetailAdjustmentItem 画面输入调整数据
	 * 
	 * @return String[0]  调整Product
	 * 			String[1] 调整Sku
	 * 			String[2]  notes
	 */
	private String[] getAdjustmentProduct(InFormOrderdetailAdjustmentItem inFormOrderdetailAdjustmentItem) {
		String[] ret = {"", "", ""};
		
		// 调整类型
		String adjustmentType = inFormOrderdetailAdjustmentItem.getAdjustmentType();
		// 调整原因
		String adjustmentReason = inFormOrderdetailAdjustmentItem.getAdjustmentReason();
		// 打折类型
		String adjustmentDiscountType = inFormOrderdetailAdjustmentItem.getAdjustmentDiscountType();
		
		// surcharge
		if (OmsConstants.AdjustmentType.SURCHARGE.equals(adjustmentType)) {
			ret[PRODUCT_TITLE_INDEX] = OmsConstants.OrderDetailProductDsp.SURCHARGE_ADJUSTMENT_TITLE + adjustmentReason;
			ret[SKU_TITLE_INDEX] = OmsConstants.OrderDetailSkuDsp.SURCHARGE_TITLE;
			ret[NOTE_INDEX] = ret[0];
		
		// discount
		} else if (OmsConstants.AdjustmentType.DISCOUNT.equals(adjustmentType)) {
			// 不打折的场合
			if (OmsConstants.DiscountType.NODISCOUNT.equals(adjustmentDiscountType)) {
				ret[PRODUCT_TITLE_INDEX] = OmsConstants.OrderDetailProductDsp.DISCOUNT_CANCELLED_TITLE + adjustmentReason;
			} else {
			// 打折的场合
				ret[PRODUCT_TITLE_INDEX] = OmsConstants.OrderDetailProductDsp.DISCOUNT_DJUSTMENT_TITLE + adjustmentReason;
			}
			
			ret[SKU_TITLE_INDEX] = OmsConstants.OrderDetailSkuDsp.DISCOUNT_TITLE;
			ret[NOTE_INDEX] = ret[0];
		
		// shipping
		} else if (OmsConstants.AdjustmentType.SHIPPING.equals(adjustmentType)) {
			ret[PRODUCT_TITLE_INDEX] = OmsConstants.OrderDetailProductDsp.SHIPPING_ADJUSTMENT_TITLE + adjustmentReason;
			ret[SKU_TITLE_INDEX] = OmsConstants.OrderDetailSkuDsp.SHIPPING_TITLE;
			ret[NOTE_INDEX] = ret[0];
		}	
		
		return ret;
	}
	
	/**
	 * 删除LineItem主函数	（该机能已废止）
	 * 
	 * @param outFormOrderDetailOrderDetail 画面选中明细行
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return
	 */
	@Override
	public void delLineItemMain(OutFormOrderDetailOrderDetail outFormOrderDetailOrderDetail, AjaxResponseBean result, UserSessionBean user) {
		
		// 当前订单信息取得
		// 		订单信息取得
		OutFormOrderdetailOrders ordersInfo = orderDetailDao.getOrdersInfo(outFormOrderDetailOrderDetail.getOrderNumber());
		// 		订单详情取得
		List<OutFormOrderDetailOrderDetail> orderDetailsList = orderDetailDao.getOrderDetailsInfo(outFormOrderDetailOrderDetail.getOrderNumber());
		
		//	操作许可检查
		if (delLineItemPermitChk(outFormOrderDetailOrderDetail.getItemNumber(), ordersInfo, orderDetailsList, result)) {
			
			//	订单明细删除
			if (delLineItem(outFormOrderDetailOrderDetail, ordersInfo, orderDetailsList, user)) {				

				// 订单刷新
				//		订单信息返回
				OutFormOrderdetailOrders orderInfo = getOrdersInfo(outFormOrderDetailOrderDetail.getOrderNumber(), user);
				//（该机能已废止）
//				//		订单Notes信息
//				List<OutFormOrderdetailNotes> orderNotesList = getOrderNotesInfo(outFormOrderDetailOrderDetail.getOrderNumber());
				
				// 设置返回结果
				Map<String, Object> ordersListMap = new HashMap<String, Object>();			
				// 		订单
				ordersListMap.put("orderInfo", orderInfo);
//				// 		订单NotesList
//				ordersListMap.put("orderNotesList", orderNotesList);
				
				result.setResultInfo(ordersListMap);
				
				//		正常
				result.setResult(true);
			} else {
				// 异常返回
//				result.setResult(false, MessageConstants.MESSAGE_CODE_200005, 
//									MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210001, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
		}
	}
	
	/**
	 * 追加LineItem主函数（该机能已废止）
	 * 
	 * @param inFormOrderdetailAddLineItem 追加SKU
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return
	 */
	@Override
	public void addLineItemMain(InFormOrderdetailAddLineItem inFormOrderdetailAddLineItem, AjaxResponseBean result, UserSessionBean user) {
		
		// 当前订单信息取得
		// 		订单信息取得
		OutFormOrderdetailOrders ordersInfo = orderDetailDao.getOrdersInfo(inFormOrderdetailAddLineItem.getOrderNumber());
		// 		订单详情取得
		List<OutFormOrderDetailOrderDetail> orderDetailsList = orderDetailDao.getOrderDetailsInfo(inFormOrderdetailAddLineItem.getOrderNumber());
		
		// 操作许可检查
		if (addLineItemPermitChk(ordersInfo, orderDetailsList, result, user)) {
		
			//	订单明细追加
			if (addLineItem(inFormOrderdetailAddLineItem, ordersInfo, user)) {
				
				// 订单刷新
				//		订单信息返回
				OutFormOrderdetailOrders orderInfo = getOrdersInfo(inFormOrderdetailAddLineItem.getOrderNumber(), user);
//				//		订单Notes信息
//				List<OutFormOrderdetailNotes> orderNotesList = getOrderNotesInfo(inFormOrderdetailAddLineItem.getOrderNumber());
				
				// 设置返回结果
				Map<String, Object> ordersListMap = new HashMap<String, Object>();			
				// 		订单
				ordersListMap.put("orderInfo", orderInfo);
//				// 		订单NotesList
//				ordersListMap.put("orderNotesList", orderNotesList);
				
				result.setResultInfo(ordersListMap);
				
				//		正常
				result.setResult(true);
				
			} else {
				// 订单明细追加异常
	//			result.setResult(false, "200007", 2);
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210003, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
		}
	}

	/**
	 * 追加LineItem操作许可检查
	 * 
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单详细信息
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean addLineItemPermitChk(OutFormOrderdetailOrders ordersInfo, List<OutFormOrderDetailOrderDetail> orderDetailsList, AjaxResponseBean result, UserSessionBean user) {
		boolean ret = true;

//		String add_order_detail_permit = ChannelConfigs.getVal1(ordersInfo.getOrderChannelId(), Name.add_order_detail_permit);
//		if (!StringUtils.isEmpty(add_order_detail_permit)) {
//			// 操作不许可的场合
//			if (OmsConstants.PERMIT_NG.equals(add_order_detail_permit)) {
//				ret = false;
//				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210028, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
//			}
//		}
		for (int i = 0; i < orderDetailsList.size(); i++) {
			OutFormOrderDetailOrderDetail orderDetailInfo = orderDetailsList.get(i);
			
			ret = chkLineItemOptPermit(Operation.AddOrderDetail, ordersInfo, orderDetailInfo, result);
			if (!ret) {
				break;
			}
		}
			
		
		return ret;
	}
	
	/**
	 * 追加LineItem
	 * 
	 * @param inFormOrderdetailAddLineItem 追加SKU
	 * @param ordersInfo 订单信息
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean addLineItem(InFormOrderdetailAddLineItem inFormOrderdetailAddLineItem,
								OutFormOrderdetailOrders ordersInfo,
								UserSessionBean user) {
		
		boolean ret = true;
		
		TransactionStatus status=transactionManager.getTransaction(def);
		
		try {
			
			// 订单价格计算
			OrderPrice orderPrice = getOrderPriceMainForAddLineItem(inFormOrderdetailAddLineItem, ordersInfo);
			
			// 订单明细保存
			ret = saveOrderDetailInfoForAddLineItem(inFormOrderdetailAddLineItem, ordersInfo, user);
			
			// 订单金额保存
			if (ret) {
				ret = saveOrderPrice(orderPrice, user);
			}
			
			// 订单Notes保存
			if (ret) {
				ret = saveOrderNotesForAddOrderDetail(inFormOrderdetailAddLineItem, user);
			}
			
			//	synShip 同步
			if (ret) {
				ret = syncSynshipFinalGrandTotal(orderPrice, user);
			}
			
			transactionManager.commit(status);
			
		} catch (Exception e) {
			ret = false;
			
			logger.error("addLineItem", e);
			
			transactionManager.rollback(status);
		}
		
		return ret;
	}

	/**
	 * 取得订单金额 （订单明细追加）
	 * 
	 * @param inFormOrderdetailAddLineItem 追加SKU
	 * @param ordersInfo 订单信息
	 *
	 * @return 订单价格
	 */
	private OrderPrice getOrderPriceMainForAddLineItem(InFormOrderdetailAddLineItem inFormOrderdetailAddLineItem,
											OutFormOrderdetailOrders ordersInfo) {
		
		// 订单价格
		OrderPrice orderPrice = getOrderPrice(ordersInfo);
		
		// 设定调整价格
		setAdjustPriceAddItem(inFormOrderdetailAddLineItem, ordersInfo, orderPrice);
		
		// 价格再计算
		priceReCalculate(orderPrice);
		
		return orderPrice;
	}
	
	/**
	 * 追加LineItem，DB保存
	 * 
	 * @param insertOrderDetailInfo 追加SKU
	 * @param ordersInfo 订单信息
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrderDetailInfoForAddLineItem(InFormOrderdetailAddLineItem insertOrderDetailInfo,
										OutFormOrderdetailOrders ordersInfo,
										UserSessionBean user) {
		
		boolean ret = true;
		// 追加明细连番
		String detailLineItem = "";
		
		// 明细连番
		int itemNumber = orderDetailDao.getOrderDetailsMaxItemNumber(insertOrderDetailInfo.getOrderNumber()) + 1;
		
		OrderDetailsBean orderDetailsInfo = new OrderDetailsBean();
		
		for (int j = 0; j < Integer.valueOf(insertOrderDetailInfo.getInventory()); j++) {
			// 再设定字段
			//	订单号
			orderDetailsInfo.setOrderNumber(insertOrderDetailInfo.getOrderNumber());
			//	明细番号
			orderDetailsInfo.setItemNumber(String.valueOf(itemNumber));
			//	adjustment　无
			orderDetailsInfo.setAdjustment(false);
			//	product
			orderDetailsInfo.setProduct(insertOrderDetailInfo.getItemName());			
			//	price_per_unit
			orderDetailsInfo.setPricePerUnit(insertOrderDetailInfo.getPrice());			
			//  订单数量（本次处理复数件的场合，拆开）
			orderDetailsInfo.setQuantityOrdered("1");
			//		quantity_shipped
			orderDetailsInfo.setQuantityShipped("1");
			//		quantity_returned
			orderDetailsInfo.setQuantityReturned("0");
			//	sku
			orderDetailsInfo.setSku(insertOrderDetailInfo.getSku());
			// 	明细状态
			orderDetailsInfo.setStatus(OmsCodeConstants.OrderStatus.INPROCESSING);
			//	synship 同步状态
			orderDetailsInfo.setSyncSynship(false);
			//	resId
			orderDetailsInfo.setResId("0");
			//		modifier
			orderDetailsInfo.setModifier(user.getUserName());
			
			ret = orderDetailDao.insertOrderDetailsInfo(orderDetailsInfo);
			if (!ret) {
				break;
			}
			
			// 追加项目番号保存
			if (StringUtils.isEmpty(detailLineItem)) {
				detailLineItem = String.valueOf(itemNumber);
			} else {
				detailLineItem = detailLineItem + "," + String.valueOf(itemNumber);
			}
			
			// 明细番号
			itemNumber = itemNumber + 1;
		}
		
		// discount更新
		// 		百分比打折的场合		
		if (ret && OmsConstants.DiscountType.PERCENT.equals(ordersInfo.getDiscountType())) {
			//	追加项目价格
			float addItemProduct = mult2Digits(Float.valueOf(insertOrderDetailInfo.getInventory()), Float.valueOf(insertOrderDetailInfo.getPrice()));
			//	打折金额设定
			float discount = mult2Digits(addItemProduct, Float.valueOf(ordersInfo.getDiscountPercent()));
			discount = mult2Digits(discount, -1);
			
			//	明细番号
			orderDetailsInfo.setItemNumber(String.valueOf(itemNumber));
			//	adjustment　无
			orderDetailsInfo.setAdjustment(true);
			//	product
			orderDetailsInfo.setProduct(OmsConstants.OrderDetailProductDsp.DISCOUNT_DJUSTMENT_TITLE_DELlINEITEM);
			//	price_per_unit
			orderDetailsInfo.setPricePerUnit(String.valueOf(discount));
			//	sku
			orderDetailsInfo.setSku(OmsConstants.OrderDetailSkuDsp.DISCOUNT_TITLE);
			// 	明细状态
			orderDetailsInfo.setStatus("");

			ret = orderDetailDao.insertOrderDetailsInfo(orderDetailsInfo);
		}
		
		// Note 输出用itemNumber 退避
		if (ret) {
			insertOrderDetailInfo.setDetailLineItem(detailLineItem);
		}
		
		return ret;
	}
	
	/**
	 * 订单Note保存	（该机能废止）
	 * 
	 * @param insertOrderDetailInfo 追加SKU
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrderNotesForAddOrderDetail(InFormOrderdetailAddLineItem insertOrderDetailInfo,
													UserSessionBean user) {
		boolean ret = true;
		
//		// 订单号
//		String orderNumber = insertOrderDetailInfo.getOrderNumber();
////		// 明细行番号
////		int itemNumber = orderDetailDao.getOrderDetailsNotesMaxItemNumber(orderNumber) + 1;
//		
//		//	Insert Product sku notes取得
//		String notes = "";
//
//		//	"item #%s SKU=%s add. Quantity: %s"
//		notes = String.format(OmsConstants.ADD_LINEITEM_NOTES_FORMAT_FORPRODUCT, 
//				insertOrderDetailInfo.getDetailLineItem(),
//				insertOrderDetailInfo.getSku(),
//				insertOrderDetailInfo.getInventory());
//		
////		// 订单Notes
////		NotesBean notesInfo = new NotesBean();
////		//		type
////		notesInfo.setType(OmsConstants.NotesType.SYSTEM);
////		// 		order_number
////		notesInfo.setNumericKey(orderNumber);
////		//		item_number
////		notesInfo.setItemNumber(String.valueOf(itemNumber));
////		//		notes
////		notesInfo.setNotes(notes);
////		//		entered_by
////		notesInfo.setEnteredBy(OmsConstants.SYS_USER);
////		//		modifier
////		notesInfo.setModifier(user.getUserName());		
////		ret = notesDao.insertOrdersInfo(notesInfo);
//		
//		String noteId = addNotes(OmsConstants.NotesType.SYSTEM, 
//				orderNumber,
//				notes,
//				OmsConstants.SYS_USER,
//				user.getUserName());
//		if (StringUtils.isEmpty(noteId)) {
//			ret = false;
//		}
		
		return ret;
	}
	
	/**
	 * Return LineItem主函数
	 * 
	 * @param inFormOrderdetailReturn Return项目
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return
	 */
	@Override
	public void returnOrderDetailMain(InFormOrderdetailReturn inFormOrderdetailReturn, AjaxResponseBean result, UserSessionBean user) {
		
		// 当前订单信息取得
		// 		订单信息取得
		OutFormOrderdetailOrders ordersInfo = orderDetailDao.getOrdersInfo(inFormOrderdetailReturn.getOrderNumber());
		// 		订单详情取得
		List<OutFormOrderDetailOrderDetail> orderDetailsList = orderDetailDao.getOrderDetailsInfo(inFormOrderdetailReturn.getOrderNumber());

		// 操作许可检查
		if (returnLineItemPermitChk(inFormOrderdetailReturn, ordersInfo, orderDetailsList, result, user)) {
		
			//	订单明细Return
			if (returnOrderDetail(inFormOrderdetailReturn, ordersInfo, orderDetailsList, result, user)) {
				//	正常返回
				setSuccessReturn(inFormOrderdetailReturn.getSourceOrderId(), inFormOrderdetailReturn.getOrderNumber(), result, user);
			}
		}
	}
	
	/**
	 * Return LineItem操作许可检查
	 * 
	 * @param inFormOrderdetailReturn 画面输入return项目
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean returnLineItemPermitChk(InFormOrderdetailReturn inFormOrderdetailReturn, 
												OutFormOrderdetailOrders ordersInfo, 
												List<OutFormOrderDetailOrderDetail> orderDetailsList, 
												AjaxResponseBean result,
												UserSessionBean user) {
		boolean ret = true;

//		String return_order_detail_permit = ChannelConfigs.getVal1(ordersInfo.getOrderChannelId(), Name.return_order_detail_permit);
//		if (!StringUtils.isEmpty(return_order_detail_permit)) {
//			// 操作不许可的场合
//			if (OmsConstants.PERMIT_NG.equals(return_order_detail_permit)) {
//				ret = false;
//				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210030, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
//			}
//		}

		// 客人拒收的场合，可以退货
		if (ordersInfo.isCustomerRefused()) {
			return true;
		}

		// 客人拒收以外的场合
		// 取得DB中Returned OrderDetail
		List<OutFormOrderDetailOrderDetail> returnedOrderDetails = getSelectedItems(inFormOrderdetailReturn, orderDetailsList);
		for (int i = 0; i < returnedOrderDetails.size(); i++) {
			OutFormOrderDetailOrderDetail orderDetailInfo = returnedOrderDetails.get(i);
			
			// Return LineItem 允许检查
			ret = chkLineItemOptPermit(Operation.ReturnOrderDetail, ordersInfo, orderDetailInfo, result);
			
			if (ret) {
				// 已Return项目检查
				if ("1".equals(orderDetailInfo.getQuantityReturned())) {
					result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210060, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);

					ret = false;
				}

				if (!ret) {
					break;
				}
			} else {
				break;
			}
		}
		
		return ret;
	}
	
	/**
	 * Return LineItem
	 * 
	 * @param inFormOrderdetailReturn Return项目
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean returnOrderDetail(InFormOrderdetailReturn inFormOrderdetailReturn, 
											OutFormOrderdetailOrders ordersInfo, 
											List<OutFormOrderDetailOrderDetail> orderDetailsList,
									  		AjaxResponseBean result,
											UserSessionBean user) {
		
		boolean ret = true;
		
		logger.info("returnOrderDetail start");
		TransactionStatus status=transactionManager.getTransaction(def);
		
		try {
			
			// 取得DB中Returned OrderDetail
			List<OutFormOrderDetailOrderDetail> returnedOrderDetails = getSelectedItems(inFormOrderdetailReturn, orderDetailsList);
			
			// 订单价格计算
			OrderPrice orderPrice = getOrderPriceMainForReturnLineItem(inFormOrderdetailReturn, ordersInfo, returnedOrderDetails);
			
			// 主订单价格取得
			OrderPrice mainOrderPrice = getMainOrderPrice(inFormOrderdetailReturn.getSourceOrderId(), orderPrice);
			
			// 订单明细保存
			logger.info("saveOrderDetailInfoForReturn");
			ret = saveOrderDetailInfoForReturn(inFormOrderdetailReturn, ordersInfo, returnedOrderDetails, orderPrice, user);
			
			// 订单金额保存
			if (ret) {
				logger.info("saveOrderPriceForUpdateStatus");
				ret = saveOrderPriceForUpdateStatus(orderPrice, user);
			}
			
			// 一组订单价格更新
			if (ret) {
				logger.info("saveGroupOrderPrice");
				ret = saveGroupOrderPrice(mainOrderPrice, ordersInfo, user);
			}			
			
			// 订单Notes保存
			if (ret) {
				logger.info("saveOrderNotesForReturn");
				ret = saveOrderNotesForReturn(inFormOrderdetailReturn, ordersInfo, returnedOrderDetails, user);
			}
			
			//	synShip 同步
			if (ret) {
				logger.info("syncSynshipFinalGrandTotal");
				ret = syncSynshipFinalGrandTotal(orderPrice, user);
			}
			
			if (ret) {
				//	订单Return 状态判定
				ArrayList<Object> judgeOrderReturnedRet = judgeOrderReturned(inFormOrderdetailReturn, orderDetailsList, returnedOrderDetails, user);
				ret = (boolean)judgeOrderReturnedRet.get(0);
			}

			if (!ret) {
				// 订单Return异常
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210010, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}

			//	CA订单取消
			if (ret) {
				if (isNeedCancelCAOrder(ordersInfo.getOrderChannelId())) {
					logger.info("cancelOrderDetailInfoForCA");
					ret = cancelOrderDetailInfoForCA(ordersInfo, returnedOrderDetails, user, result, true);
				}
			}

			if (ret) {
				logger.info("returnOrderDetail success");
				transactionManager.commit(status);
			} else {
				logger.info("returnOrderDetail error");
				transactionManager.rollback(status);
			}
			
			
		} catch (Exception e) {
			ret = false;
			
			logger.error("returnOrderDetail", e);
			
			transactionManager.rollback(status);
		}
		
		return ret;
		
	}
	
	/**
	 * 取得Return OrderDetail
	 * 
	 * @param inFormOrderdetailReturn Return项目
	 * @param orderDetailsList 订单明细信息
	 * 
	 * @return 选中行关联明细
	 */
	private List<OutFormOrderDetailOrderDetail> getSelectedItems(InFormOrderdetailReturn inFormOrderdetailReturn, List<OutFormOrderDetailOrderDetail> orderDetailsList) {
		// 返回值
		List<OutFormOrderDetailOrderDetail> returnedOrderDetails = new ArrayList<OutFormOrderDetailOrderDetail>();
		
		// 画面输入Return 项目
		List<OrderDetailsBean> inputOrderDetailsReturnList = inFormOrderdetailReturn.getOrderDetailsList();
		
		for (int i = 0; i < inputOrderDetailsReturnList.size(); i++) {
			String returnItemNumber =  inputOrderDetailsReturnList.get(i).getItemNumber();
			
			for (int j = 0; j < orderDetailsList.size(); j++) {
				// 物品的场合
				if (returnItemNumber.equals(orderDetailsList.get(j).getItemNumber())) {
					
					returnedOrderDetails.add(orderDetailsList.get(j));
					
				// 关联打折信息的场合
				} else if(returnItemNumber.equals(orderDetailsList.get(j).getSubItemNumber())) {
					
					returnedOrderDetails.add(orderDetailsList.get(j));
				}
			}
		}
		
		return returnedOrderDetails;
	}

	/**
	 * 取得订单金额 （订单明细Return）
	 * 
	 * @param inFormOrderdetailReturn Return项目
	 * @param ordersInfo 订单信息
	 * @param returnedOrderDetailsList 订单明细信息
	 * 
	 * @return 订单价格
	 */
	private OrderPrice getOrderPriceMainForReturnLineItem(InFormOrderdetailReturn inFormOrderdetailReturn,
											OutFormOrderdetailOrders ordersInfo,
											List<OutFormOrderDetailOrderDetail> returnedOrderDetailsList) {
		
		// 订单价格
		OrderPrice orderPrice = getOrderPrice(ordersInfo);
		
		// 设定调整价格
		setAdjustPriceReturnItem(inFormOrderdetailReturn, ordersInfo, returnedOrderDetailsList, orderPrice);
		
		// 价格再计算
		priceReCalculate(orderPrice);
		
		return orderPrice;
	}
	
	/**
	 * Return LineItem订单价格调整
	 * 
	 * @param inFormOrderdetailReturn 输入Return OrderDetail
	 * @param ordersInfo 订单信息
	 * @param returnedOrderDetailsList returned明细信息
	 * @param orderPrice 订单价格
	 * 
	 * @return
	 */
	private void setAdjustPriceReturnItem(InFormOrderdetailReturn inFormOrderdetailReturn,
										OutFormOrderdetailOrders ordersInfo,
										List<OutFormOrderDetailOrderDetail> returnedOrderDetailsList,
										OrderPrice  orderPrice) {
		
		
		// final_product_total
		//		return 物品 product total 取得
		float returnItemProduct = 0f;
		//		return 物品discount 取得
		float returnItemDiscount = 0f;
		
		for (int j = 0; j < returnedOrderDetailsList.size(); j++) {
			OutFormOrderDetailOrderDetail returnOrderDetailInfo = returnedOrderDetailsList.get(j);
			
			// discount 的场合
			if (returnOrderDetailInfo.isAdjustment()) {
				returnItemDiscount = add2Digits(returnItemDiscount, Float.valueOf(returnOrderDetailInfo.getPricePerUnit()));
			// 物品的场合
			} else {
				returnItemProduct = add2Digits(returnItemProduct, Float.valueOf(returnOrderDetailInfo.getPricePerUnit()));	
			}
		}
		
		//	return 后物品价格
		float finalProductTotal = sub2Digits(orderPrice.getFinalProductTotal(), returnItemProduct);
		orderPrice.setFinalProductTotal(finalProductTotal);
		
		//	return 后打折价格
		float revisedDiscount = sub2Digits(orderPrice.getRevisedDiscount(), returnItemDiscount);
		orderPrice.setRevisedDiscount(revisedDiscount);
		
		// reserved discount（本次不对应 20150507）
		// 		百分比打折的场合
		if (OmsConstants.DiscountType.PERCENT.equals(ordersInfo.getDiscountType())) {
			// revised_discount = 产品价格 * 订单打折率 * -1
			float reservedDiscount = mult2Digits(mult2Digits(finalProductTotal, Float.valueOf(ordersInfo.getDiscountPercent())), -1);
			
			orderPrice.setRevisedDiscount(reservedDiscount);
		}
		
		// finalShippingTotal
		//		退运费的场合
		if (inFormOrderdetailReturn.isReturnShipping()) {
			// 	被退运费退避
			orderPrice.setFinalShippingTotal_beforeReturnShipping(orderPrice.getFinalShippingTotal());
			//	运费清空
			orderPrice.setFinalShippingTotal(0f);
		}
	}

//	/**
//	 * Return LineItem，DB保存
//	 * 
//	 * @param InFormOrderdetailReturn Return Line Item
//	 * @param ordersInfo 订单信息
//	 * @param returnedOrderDetailsList returned明细信息
//	 * @param user 当前用户
//	 * 
//	 * @return
//	 */
//	private boolean saveOrderDetailInfoForReturn(InFormOrderdetailReturn inFormOrderdetailReturn,
//										OutFormOrderdetailOrders ordersInfo,
//										List<OutFormOrderDetailOrderDetail> returnedOrderDetailsList,
//										OrderPrice orderPrice,
//										UserSessionBean user) {
//		
//		boolean ret = true;
//		
//		// 明细连番
//		int itemNumber = orderDetailDao.getOrderDetailsMaxItemNumber(inFormOrderdetailReturn.getOrderNumber()) + 1;
//		
//		// Return product total
//		float returnItemProduct = 0f;
//		
//		// Order Detail 追加项目Bean
//		OrderDetailsBean orderDetailsInfo = new OrderDetailsBean();
//		
//		for (int i = 0; i < returnedOrderDetailsList.size(); i++) {
//			// return orderDetail item
//			OutFormOrderDetailOrderDetail orderDetailInfo = returnedOrderDetailsList.get(i);			
//			// 订单Discount明细删除
//			if (orderDetailInfo.isAdjustment()) {
//				// 订单明细Bean
//				OrderDetailsBean orderDetailInfoForDel = new OrderDetailsBean();
//				//		订单号
//				orderDetailInfoForDel.setOrderNumber(orderDetailInfo.getOrderNumber());
//				//		订单明细番号
//				orderDetailInfoForDel.setItemNumber(orderDetailInfo.getItemNumber());
//				ret = orderDetailDao.deleteOrderDetailsInfo(orderDetailInfoForDel);
//			} else {
//				// Return product total 加算
//				returnItemProduct = add2Digits(returnItemProduct, Float.valueOf(orderDetailInfo.getPricePerUnit()));
//				
//				// 再设定字段
//				//	订单号
//				orderDetailsInfo.setOrderNumber(inFormOrderdetailReturn.getOrderNumber());
//				//	明细番号
//				orderDetailsInfo.setItemNumber(String.valueOf(itemNumber));
//				//	adjustment　有
//				orderDetailsInfo.setAdjustment(true);
//				//	product
//				//		"Returned(1)%s:%s";
//				String product = String.format(OmsConstants.OrderDetailProductDsp.RETURN_TITLE, orderDetailInfo.getSku(), orderDetailInfo.getProduct());
//				orderDetailsInfo.setProduct(product);			
//				//	price_per_unit
//				float pricePerUnit = mult2Digits(Float.valueOf(orderDetailInfo.getPricePerUnit()), -1);
//				orderDetailsInfo.setPricePerUnit(String.valueOf(pricePerUnit));			
//				//  quantity_ordered
//				orderDetailsInfo.setQuantityOrdered("1");
//				//	quantity_shipped
//				orderDetailsInfo.setQuantityShipped("1");
//				//	quantity_returned
//				orderDetailsInfo.setQuantityReturned("0");
//				//	sku
//				orderDetailsInfo.setSku(OmsConstants.OrderDetailSkuDsp.PRODUCT_TITLE);
//				// 	明细状态
//				orderDetailsInfo.setStatus("");
//				//		resAllotFlg
//				orderDetailsInfo.setResAllotFlg(true);
//				//	synship 同步状态
//				orderDetailsInfo.setSyncSynship(true);
//				//	resId
//				orderDetailsInfo.setResId("0");
//				//		creater
//				orderDetailsInfo.setCreater(user.getUserName());
//				//		modifier
//				orderDetailsInfo.setModifier(user.getUserName());
//				
//				ret = orderDetailDao.insertOrderDetailsInfo(orderDetailsInfo);
//				if (!ret) {
//					return false;
//				}
//			}
//			
//			
//			// 明细番号
//			itemNumber = itemNumber + 1;
//		}
//		
//		//	退运费的场合
//		if (inFormOrderdetailReturn.isReturnShipping()) {
//			
//			//	被退运费 〉0 的场合
//			if (orderPrice.getFinalShippingTotal_beforeReturnShipping() > 0) {
//				//	明细番号
//				orderDetailsInfo.setItemNumber(String.valueOf(itemNumber));
//				//	adjustment　无
//				orderDetailsInfo.setAdjustment(true);
//				//	product
//				orderDetailsInfo.setProduct(OmsConstants.OrderDetailProductDsp.SHIPPING_ADJUSTMENT_TITLE);
//				//	price_per_unit
//				float pricePerUnit = mult2Digits(orderPrice.getFinalShippingTotal_beforeReturnShipping(), -1);
//				orderDetailsInfo.setPricePerUnit(String.valueOf(pricePerUnit));
//				//  quantity_ordered
//				orderDetailsInfo.setQuantityOrdered("1");
//				//	quantity_shipped
//				orderDetailsInfo.setQuantityShipped("1");
//				//	quantity_returned
//				orderDetailsInfo.setQuantityReturned("0");
//				//	sku
//				orderDetailsInfo.setSku(OmsConstants.OrderDetailSkuDsp.SHIPPING_TITLE);
//				// 	明细状态
//				orderDetailsInfo.setStatus("");
//
//				ret = orderDetailDao.insertOrderDetailsInfo(orderDetailsInfo);
//				if (!ret) {
//					return false;
//				}
//				
//				// 明细番号
//				itemNumber = itemNumber + 1;				
//			}
//		}
//		
//		
//		// discount更新
//		// 		百分比打折的场合		
//		if (ret && OmsConstants.DiscountType.PERCENT.equals(ordersInfo.getDiscountType())) {
//			//	打折金额设定
//			float discount = mult2Digits(returnItemProduct, Float.valueOf(ordersInfo.getDiscountPercent()));
//			
//			//	明细番号
//			orderDetailsInfo.setItemNumber(String.valueOf(itemNumber));
//			//	adjustment　无
//			orderDetailsInfo.setAdjustment(true);
//			//	product
//			orderDetailsInfo.setProduct(OmsConstants.OrderDetailProductDsp.DISCOUNT_DJUSTMENT_TITLE_DELlINEITEM);
//			//	price_per_unit
//			orderDetailsInfo.setPricePerUnit(String.valueOf(discount));
//			//  quantity_ordered
//			orderDetailsInfo.setQuantityOrdered("1");
//			//	quantity_shipped
//			orderDetailsInfo.setQuantityShipped("1");
//			//	quantity_returned
//			orderDetailsInfo.setQuantityReturned("0");
//			//	sku
//			orderDetailsInfo.setSku(OmsConstants.OrderDetailSkuDsp.DISCOUNT_TITLE);
//			// 	明细状态
//			orderDetailsInfo.setStatus("");
//
//			ret = orderDetailDao.insertOrderDetailsInfo(orderDetailsInfo);
//			if (!ret) {
//				return false;
//			}
//		}
//		
//		// OrderDetail Status Change
//		for (int i = 0; i < returnedOrderDetailsList.size(); i++) {
//			// return orderDetail item
//			OutFormOrderDetailOrderDetail orderDetailInfo = returnedOrderDetailsList.get(i);
//			
//			if (!orderDetailInfo.isAdjustment()){
//
//				OrderDetailsBean updateOrderDetailsInfo = new OrderDetailsBean();			
//	
//				//	orderNumber
//				updateOrderDetailsInfo.setOrderNumber(orderDetailInfo.getOrderNumber());
//				//	itemNumber
//				updateOrderDetailsInfo.setItemNumber(orderDetailInfo.getItemNumber());
//				// 	quantity_returned
//				updateOrderDetailsInfo.setQuantityReturned("1");
//				// 	status
//				updateOrderDetailsInfo.setStatus(OmsCodeConstants.OrderStatus.RETURNED);
//				//	modifier
//				updateOrderDetailsInfo.setModifier(user.getUserName());
//				
//				ret = orderDetailDao.updateOrderDetailsStatusInfo(updateOrderDetailsInfo);
//				if (!ret) {
//					return false;
//				}
//			}
//		}
//		
//		return ret;
//	}
	
	/**
	 * Return LineItem，DB保存（本次Return，不新追加记录，百分比打折，退运费不对应）
	 * 
	 * @param inFormOrderdetailReturn  Return项目
	 * @param ordersInfo 订单信息
	 * @param returnedOrderDetailsList returned明细信息
	 * @param orderPrice 订单价格
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrderDetailInfoForReturn(InFormOrderdetailReturn inFormOrderdetailReturn,
										OutFormOrderdetailOrders ordersInfo,
										List<OutFormOrderDetailOrderDetail> returnedOrderDetailsList,
										OrderPrice orderPrice,
										UserSessionBean user) {
		
		boolean ret = true;
		
		// OrderDetail Status Change
		for (int i = 0; i < returnedOrderDetailsList.size(); i++) {
			// return orderDetail item
			OutFormOrderDetailOrderDetail orderDetailInfo = returnedOrderDetailsList.get(i);
			

			OrderDetailsBean updateOrderDetailsInfo = new OrderDetailsBean();			

			//	orderNumber
			updateOrderDetailsInfo.setOrderNumber(orderDetailInfo.getOrderNumber());
			//	itemNumber
			updateOrderDetailsInfo.setItemNumber(orderDetailInfo.getItemNumber());
			// 	quantity_returned
			updateOrderDetailsInfo.setQuantityReturned("1");
			// 	status
			updateOrderDetailsInfo.setStatus(OmsCodeConstants.OrderStatus.RETURNED);
			//	modifier
			updateOrderDetailsInfo.setModifier(user.getUserName());
			
			ret = orderDetailDao.updateOrderDetailsStatusInfo(updateOrderDetailsInfo);
			if (!ret) {
				return false;
			}

		}
		
		return ret;
	}
	
	/**
	 * 订单价格保存（状态同步更新）
	 * 
	 * @param orderPrice 订单价格
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrderPriceForUpdateStatus(OrderPrice orderPrice, UserSessionBean user) {
		
		boolean ret = true;
		
		// 订单
		OrdersBean ordersInfo = setOrderPrice(orderPrice);
		
		// 订单状态（本处不发生变化，要根据剩余的明细来判断）
		ordersInfo.setOrderStatus("");
		
		// 		modifier
		ordersInfo.setModifier(user.getUserName());
		
		ret = orderDetailDao.updateOrdersInfo(ordersInfo);
		
		return ret;
	}
	
	/**
	 * 订单Note保存
	 * 
	 * @param inFormOrderdetailReturn 画面输入Return项目
	 * @param ordersInfo 当前订单
	 * @param returnedOrderDetails Return项目
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrderNotesForReturn(InFormOrderdetailReturn inFormOrderdetailReturn,
													OutFormOrderdetailOrders ordersInfo,
													List<OutFormOrderDetailOrderDetail> returnedOrderDetails,
													UserSessionBean user) {
		boolean ret = true;
		
		// 订单号
		String orderNumber = inFormOrderdetailReturn.getOrderNumber();
		// 明细行番号
		int itemNumber = orderDetailDao.getOrderDetailsNotesMaxItemNumber(orderNumber) + 1;

		// 追加订单Notes		
		NotesBean notesInfo = new NotesBean();
		//		共通设定
		//		source_order_id
		notesInfo.setSourceOrderId(inFormOrderdetailReturn.getSourceOrderId());
		//		type
		notesInfo.setType(OmsConstants.NotesType.SYSTEM);
		// 		order_number
		notesInfo.setNumericKey(orderNumber);
		//		entered_by
		notesInfo.setEnteredBy(user.getUserName());
		//		Creater
		notesInfo.setCreater(user.getUserName());
		//		modifier
		notesInfo.setModifier(user.getUserName());
		
		// Return 项目Notes
		for (int i = 0; i < returnedOrderDetails.size(); i++) {
			OutFormOrderDetailOrderDetail returnOrderDetailInfo = returnedOrderDetails.get(i);
			
			//	"Returned(1)%s:%s";
			String notes = String.format(OmsConstants.OrderDetailProductDsp.RETURN_TITLE,
					returnOrderDetailInfo.getSku(),
					returnOrderDetailInfo.getProduct());

			//		item_number
			notesInfo.setItemNumber(String.valueOf(itemNumber));
			//		notes
			notesInfo.setNotes(notes);
			
			ret = notesDao.insertNotesInfo(notesInfo);
			
			// transactons 信息更新
			if (ret) {
				
				// return 只存在  物品 和 关联打折信息
				String description = "";
				if (returnOrderDetailInfo.isAdjustment()) {
					description = OrderDetailSkuDsp.DISCOUNT_TITLE;
				} else {
					description = OrderDetailSkuDsp.PRODUCT_TITLE;
				}
				
				// 订单明细, debt, credit, noteId, user
				ret = saveOrderTransactionsInfo(inFormOrderdetailReturn.getSourceOrderId(),
												ordersInfo.getOriginSourceOrderId(),
												returnOrderDetailInfo.getOrderNumber(),
												getTransactionSku(returnOrderDetailInfo, returnedOrderDetails),
												returnOrderDetailInfo.getItemNumber(),
												description, 
												returnOrderDetailInfo.getPricePerUnit(), 
												notesInfo.getId(),
												OmsConstants.Transaction_Type.REFUND,
												true,
												user);
				
				if (!ret) {
					break;
				}
			}
			
			itemNumber = itemNumber + 1;
		}
		
//		 订单状态变化Notes
//		if (ret) {
//			// OrderStatus变化Notes
//			//		item_number
//			notesInfo.setItemNumber(String.valueOf(itemNumber));
//			//		notes
//			notesInfo.setNotes(OmsConstants.RETURN_ORDER);
//			ret = notesDao.insertNotesInfo(notesInfo);
//			itemNumber = itemNumber + 1;
//		}

		if (ret) {
			// OrderDetail Status变化Notes
			for (int i = 0; i < returnedOrderDetails.size(); i++) {
				OutFormOrderDetailOrderDetail returnOrderDetailInfo = returnedOrderDetails.get(i);
				
				//	"Item #%S SKU=%S Status changed to: Returned. Reason: %s"
				String notes = String.format(OmsConstants.RETURN_ORDERDETAIL_FORMAT,
						returnOrderDetailInfo.getItemNumber(),
						returnOrderDetailInfo.getSku(),
						inFormOrderdetailReturn.getReason());

				//		item_number
				notesInfo.setItemNumber(String.valueOf(itemNumber));
				//		notes
				notesInfo.setNotes(notes);
				
				ret = notesDao.insertNotesInfo(notesInfo);
				if (!ret) {
					break;
				}
				
				itemNumber = itemNumber + 1;
			}
		}
		
		return ret;
	}
	
	/**
	 * unReturn LineItem主函数（已废止）
	 * 
	 * @param inFormOrderdetailReturn 画面输入Return项目
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return
	 */
	@Override
	public void unReturnOrderDetailMain(InFormOrderdetailReturn inFormOrderdetailReturn, AjaxResponseBean result, UserSessionBean user) {
		
		// 当前订单信息取得
		// 		订单信息取得
		OutFormOrderdetailOrders ordersInfo = orderDetailDao.getOrdersInfo(inFormOrderdetailReturn.getOrderNumber());
		// 		订单详情取得
		List<OutFormOrderDetailOrderDetail> orderDetailsList = orderDetailDao.getOrderDetailsInfo(inFormOrderdetailReturn.getOrderNumber());
		
		//	订单明细Return
		if (unReturnOrderDetail(inFormOrderdetailReturn, ordersInfo, orderDetailsList, user)) {
			
			// 订单刷新
			//		订单信息返回
			OutFormOrderdetailOrders orderInfo = getOrdersInfo(inFormOrderdetailReturn.getOrderNumber(), user);
//			//		订单Notes信息
//			List<OutFormOrderdetailNotes> orderNotesList = getOrderNotesInfo(inFormOrderdetailReturn.getOrderNumber());
			
			// 设置返回结果
			Map<String, Object> ordersListMap = new HashMap<String, Object>();			
			// 		订单
			ordersListMap.put("orderInfo", orderInfo);
//			// 		订单NotesList
//			ordersListMap.put("orderNotesList", orderNotesList);
			
			result.setResultInfo(ordersListMap);
			
			//		正常
			result.setResult(true);
			
		} else {
			// 订单明细追加异常
//			result.setResult(false, "200016", 2);
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210011, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}
		
	}

	/**
	 * Return LineItem
	 * 
	 * @param inFormOrderdetailReturn Return项目
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean unReturnOrderDetail(InFormOrderdetailReturn inFormOrderdetailReturn, 
											OutFormOrderdetailOrders ordersInfo, 
											List<OutFormOrderDetailOrderDetail> orderDetailsList, 
											UserSessionBean user) {
		
		boolean ret = true;
		
		TransactionStatus status=transactionManager.getTransaction(def);
		
		try {
			
			// 取得DB中Returned OrderDetail
			List<OutFormOrderDetailOrderDetail> unReturnedOrderDetails = getSelectedItems(inFormOrderdetailReturn, orderDetailsList);
			
			// 订单价格计算
			OrderPrice orderPrice = getOrderPriceMainForunReturnLineItem(inFormOrderdetailReturn, ordersInfo, unReturnedOrderDetails);
			
			// 订单明细保存
			ret = saveOrderDetailInfoForUnReturn(inFormOrderdetailReturn, ordersInfo, unReturnedOrderDetails, orderDetailsList, orderPrice, user);
			
			// 订单金额保存
			if (ret) {
				ret = saveOrderPriceForUpdateStatus(orderPrice, user);
			}
			
			// 订单Notes保存
			if (ret) {
				ret = saveOrderNotesForUnReturn(inFormOrderdetailReturn, unReturnedOrderDetails, user);
			}
			
			//	synShip 同步
			if (ret) {
				ret = syncSynshipFinalGrandTotal(orderPrice, user);
			}
			
			transactionManager.commit(status);
			
		} catch (Exception e) {
			ret = false;
			
			logger.error("unReturnOrderDetail", e);
			
			transactionManager.rollback(status);
		}
		
		return ret;
	}
	
	/**
	 * 取得订单金额 （订单明细unReturn）
	 * 
	 * @param inFormOrderdetailReturn Return项目
	 * @param ordersInfo 订单信息
	 * @param unReturnedOrderDetailsList unReturn订单明细信息
	 * 
	 * @return 订单价格
	 */
	private OrderPrice getOrderPriceMainForunReturnLineItem(InFormOrderdetailReturn inFormOrderdetailReturn,
											OutFormOrderdetailOrders ordersInfo,
											List<OutFormOrderDetailOrderDetail> unReturnedOrderDetailsList) {
		
		// 订单价格
		OrderPrice orderPrice = getOrderPrice(ordersInfo);
		
		// 设定调整价格
		setAdjustPriceUnReturnItem(inFormOrderdetailReturn, ordersInfo, unReturnedOrderDetailsList, orderPrice);
		
		// 价格再计算
		priceReCalculate(orderPrice);
		
		return orderPrice;
	}
	
	/**
	 * unReturn LineItem订单价格调整
	 * 
	 * @param inFormOrderdetailReturn 输入Return项目
	 * @param ordersInfo 订单信息
	 * @param returnedOrderDetailsList returned明细信息
	 * @param orderPrice 订单价格
	 * 
	 * @return
	 */
	private void setAdjustPriceUnReturnItem(InFormOrderdetailReturn inFormOrderdetailReturn,
										OutFormOrderdetailOrders ordersInfo,
										List<OutFormOrderDetailOrderDetail> returnedOrderDetailsList,
										OrderPrice  orderPrice) {
		
		
		// final_product_total
		//		return 物品 product total 取得
		float unReturnItemProduct = 0f;
		for (int j = 0; j < returnedOrderDetailsList.size(); j++) {
			unReturnItemProduct = add2Digits(unReturnItemProduct, Float.valueOf(returnedOrderDetailsList.get(j).getPricePerUnit()));
		}
		
		//		unReturn后价格
		float finalProductTotal = add2Digits(orderPrice.getFinalProductTotal(), unReturnItemProduct);
		orderPrice.setFinalProductTotal(finalProductTotal);
		
		// reserved discount
		// 		百分比打折的场合
		if (OmsConstants.DiscountType.PERCENT.equals(ordersInfo.getDiscountType())) {
			// revised_discount = 产品价格 * 订单打折率 * -1
			float reservedDiscount = mult2Digits(mult2Digits(finalProductTotal, Float.valueOf(ordersInfo.getDiscountPercent())), -1);
			
			orderPrice.setRevisedDiscount(reservedDiscount);
		}
	}

	/**
	 * unReturn LineItem，DB保存
	 * 
	 * @param inFormOrderdetailReturn Return项目
	 * @param ordersInfo 订单信息
	 * @param unReturnedOrderDetailsList unReturned明细信息
	 * @param orderDetailsList 订单明细
	 * @param orderPrice 订单价格
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrderDetailInfoForUnReturn(InFormOrderdetailReturn inFormOrderdetailReturn,
										OutFormOrderdetailOrders ordersInfo,
										List<OutFormOrderDetailOrderDetail> unReturnedOrderDetailsList,
										List<OutFormOrderDetailOrderDetail> orderDetailsList,
										OrderPrice orderPrice,
										UserSessionBean user) {
		
		boolean ret = true;
		
		// 明细连番
		int itemNumber = orderDetailDao.getOrderDetailsMaxItemNumber(inFormOrderdetailReturn.getOrderNumber()) + 1;
		
		// Return product total
		float unReturnItemProduct = 0f;
		
		//	订单明细删除
		//		待删除订单明细退避（item_number）
		List<String> delOrderDetailsList = new ArrayList<String>();
		//		待删除订单明细取得
		for (int i = 0; i < unReturnedOrderDetailsList.size(); i++) {
			// return orderDetail item
			OutFormOrderDetailOrderDetail unRetOrderDetailInfo = unReturnedOrderDetailsList.get(i);			
			
			// unReturn product total 加算
			unReturnItemProduct = add2Digits(unReturnItemProduct, Float.valueOf(unRetOrderDetailInfo.getPricePerUnit()));

			//	product
			//		"Returned(1)%s:%s";
			String product = String.format(OmsConstants.OrderDetailProductDsp.RETURN_TITLE, unRetOrderDetailInfo.getSku(), unRetOrderDetailInfo.getProduct());
			//	price_per_unit
			float pricePerUnit = mult2Digits(Float.valueOf(unRetOrderDetailInfo.getPricePerUnit()), -1);

			//	待删除item_number取得
			for (int j = 0; j < orderDetailsList.size(); j++) {
				OutFormOrderDetailOrderDetail orderDetailInfo = orderDetailsList.get(j);
				// Product 的场合
				if (OmsConstants.OrderDetailSkuDsp.PRODUCT_TITLE.equals(orderDetailInfo.getSku())) {
					// product && price 一致的场合
					if (product.equals(orderDetailInfo.getProduct()) && pricePerUnit == Float.valueOf(orderDetailInfo.getPricePerUnit())) {
						
						// 该 ItemNumber 未退避的场合
						if (!delOrderDetailsList.contains(orderDetailInfo.getItemNumber())) {
							delOrderDetailsList.add(orderDetailInfo.getItemNumber());
							break;
						}
					}
				}
			}
		}
		//		订单明细删除
		for (int i = 0; i < delOrderDetailsList.size(); i++) {
			// Order Detail 删除项目Bean
			OrderDetailsBean orderDetailsInfo = new OrderDetailsBean();
			//		订单号
			orderDetailsInfo.setOrderNumber(inFormOrderdetailReturn.getOrderNumber());
			//		订单明细相番
			orderDetailsInfo.setItemNumber(delOrderDetailsList.get(i));
			
			ret = orderDetailDao.deleteOrderDetailsInfo(orderDetailsInfo);
			if (!ret) {
				return false;
			}
		}
		
		// discount更新
		// 		百分比打折的场合		
		if (ret && OmsConstants.DiscountType.PERCENT.equals(ordersInfo.getDiscountType())) {
			// Order Detail 追加项目Bean
			OrderDetailsBean orderDetailsInfo = new OrderDetailsBean();
			
			//	打折金额设定
			float discount = mult2Digits(unReturnItemProduct, Float.valueOf(ordersInfo.getDiscountPercent()));
			discount = mult2Digits(discount, -1);

			//	订单号
			orderDetailsInfo.setOrderNumber(inFormOrderdetailReturn.getOrderNumber());
			//	明细番号
			orderDetailsInfo.setItemNumber(String.valueOf(itemNumber));
			//	adjustment　有
			orderDetailsInfo.setAdjustment(true);
			//	product
			orderDetailsInfo.setProduct(OmsConstants.OrderDetailProductDsp.DISCOUNT_DJUSTMENT_TITLE_DELlINEITEM);
			//	price_per_unit
			orderDetailsInfo.setPricePerUnit(String.valueOf(discount));
			
			// 	quantity_ordered
			orderDetailsInfo.setQuantityOrdered("1");
			//	quantity_shipped
			orderDetailsInfo.setQuantityShipped("1");
			// 	quantity_returned
			orderDetailsInfo.setQuantityReturned("0");
			
			//	sku
			orderDetailsInfo.setSku(OmsConstants.OrderDetailSkuDsp.DISCOUNT_TITLE);
			// 	明细状态
			orderDetailsInfo.setStatus("");
			
			//	synship 同步状态
			orderDetailsInfo.setSyncSynship(false);
			//	resId
			orderDetailsInfo.setResId("0");
			//		modifier
			orderDetailsInfo.setModifier(user.getUserName());

			ret = orderDetailDao.insertOrderDetailsInfo(orderDetailsInfo);
			if (!ret) {
				return false;
			}
		}
		
		// OrderDetail Status Change
		for (int i = 0; i < unReturnedOrderDetailsList.size(); i++) {
			// return orderDetail item
			OutFormOrderDetailOrderDetail orderDetailInfo = unReturnedOrderDetailsList.get(i);			

			OrderDetailsBean updateOrderDetailsInfo = new OrderDetailsBean();			

			//	orderNumber
			updateOrderDetailsInfo.setOrderNumber(orderDetailInfo.getOrderNumber());
			//	itemNumber
			updateOrderDetailsInfo.setItemNumber(orderDetailInfo.getItemNumber());
			//	quantity_shipped
			updateOrderDetailsInfo.setQuantityShipped("1");
			// 	quantity_returned
			updateOrderDetailsInfo.setQuantityReturned("0");
			// 	status
			updateOrderDetailsInfo.setStatus(OmsCodeConstants.OrderStatus.RETURNED);
			//	synship 同步状态
			updateOrderDetailsInfo.setSyncSynship(false);
			//	resId
			updateOrderDetailsInfo.setResId("0");
			//		modifier
			updateOrderDetailsInfo.setModifier(user.getUserName());
			
			ret = orderDetailDao.updateOrderDetailsStatusInfo(updateOrderDetailsInfo);
			if (!ret) {
				return false;
			}
		}
		
		return ret;
	}	
	
	/**
	 * 订单Note保存
	 * 
	 * @param inFormOrderdetailReturn 画面Return项目
	 * @param returnedOrderDetails Return项目
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrderNotesForUnReturn(InFormOrderdetailReturn inFormOrderdetailReturn,
													List<OutFormOrderDetailOrderDetail> returnedOrderDetails,
													UserSessionBean user) {
		boolean ret = true;
		
		// 订单号
		String orderNumber = inFormOrderdetailReturn.getOrderNumber();
		// 明细行番号
		int itemNumber = orderDetailDao.getOrderDetailsNotesMaxItemNumber(orderNumber) + 1;

		// 追加订单Notes		
		NotesBean notesInfo = new NotesBean();
		//		共通设定
		//		type
		notesInfo.setType(OmsConstants.NotesType.SYSTEM);
		// 		order_number
		notesInfo.setNumericKey(orderNumber);
		//		entered_by
		notesInfo.setEnteredBy(user.getUserName());
		//		creater
		notesInfo.setCreater(user.getUserName());
		//		modifier
		notesInfo.setModifier(user.getUserName());
		
		// Return 项目Notes
		for (int i = 0; i < returnedOrderDetails.size(); i++) {
			OutFormOrderDetailOrderDetail returnOrderDetailInfo = returnedOrderDetails.get(i);
			
			//	"Canceled the Return of (1) %s:%";
			String notes = String.format(OmsConstants.UNRETURN_ORDER,
					returnOrderDetailInfo.getSku(),
					returnOrderDetailInfo.getProduct());

			//		item_number
			notesInfo.setItemNumber(String.valueOf(itemNumber));
			//		notes
			notesInfo.setNotes(notes);
			
			ret = notesDao.insertNotesInfo(notesInfo);
			if (!ret) {
				return false;
			}
			
			itemNumber = itemNumber + 1;
		}
		
		return ret;
	}
	
	/**
	 * 追加LineItem订单价格调整
	 * 
	 * @param inFormOrderdetailAddLineItem 追加
	 * @param orderPrice 订单价格
	 * 
	 * @return
	 */
	private void setAdjustPriceAddItem(InFormOrderdetailAddLineItem inFormOrderdetailAddLineItem,
										OutFormOrderdetailOrders ordersInfo,
										OrderPrice  orderPrice) {
		
		
		// final_product_total
		//		追加项目价格
		float addItemProduct = mult2Digits(Float.valueOf(inFormOrderdetailAddLineItem.getInventory()), Float.valueOf(inFormOrderdetailAddLineItem.getPrice()));
		//		追加后价格
		float finalProductTotal = add2Digits(orderPrice.getFinalProductTotal(), addItemProduct);
		orderPrice.setFinalProductTotal(finalProductTotal);
		
		// 百分比打折的场合
		if (OmsConstants.DiscountType.PERCENT.equals(ordersInfo.getDiscountType())) {
			// revised_discount = 产品价格 * 订单打折率 * -1
			float reservedDiscount = mult2Digits(mult2Digits(finalProductTotal, Float.valueOf(ordersInfo.getDiscountPercent())), -1);
			
			orderPrice.setRevisedDiscount(reservedDiscount);
		}
	}	
	
	/**
	 * 订单锁定状态变更
	 * 
	 * @param sourceOrderId 网络订单号
	 * @param lockFlag 需变更订单状态 
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return
	 */
	@Override
	public void changeLockStatusMain(String sourceOrderId, boolean lockFlag, AjaxResponseBean result, UserSessionBean user) {
		
		// 当前订单信息取得
		// 		主订单信息取得 
		OutFormOrderdetailOrders mainOrdersInfo = orderDetailDao.getGroupOrdersInfo(sourceOrderId);
		
		// Cancel 订单许可检查
		if (chkChangeLockStatusPermit(mainOrdersInfo, lockFlag, result)) {
			
			//	订单明细追加
			if (changeLockStatus(sourceOrderId, lockFlag, user)) {			
	
				//		订单Notes信息
				List<OutFormOrderdetailNotes> orderNotesList = getOrderNotesInfoBySourceOrderId(sourceOrderId, user);
				
				// 设置返回结果
				Map<String, Object> ordersListMap = new HashMap<String, Object>();
				// 		订单NotesList
				ordersListMap.put("orderNotesList", orderNotesList);
				
				result.setResultInfo(ordersListMap);
				
				//		正常
				result.setResult(true);
				
			} else {
				// 订单锁定状态更新异常
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210004, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
		}
	}
	
	/**
	 * 订单锁定许可判定
	 * 
	 * @param mainOrdersInfo 主订单信息
	 * @param result 返回结果
	 * 
	 * @return
	 */
	private boolean chkChangeLockStatusPermit(OutFormOrderdetailOrders mainOrdersInfo, boolean lockFlag, AjaxResponseBean result) {
		boolean ret = true;
		
		//	解锁的场合
		if (!lockFlag) {
			// 预收款项 < 实际价值
			if (Float.valueOf(mainOrdersInfo.getExpected()) < Float.valueOf(mainOrdersInfo.getFinalGrandTotal())) {
				//	该订单存在Return明细，必须先Unreturn明细后，再进行订单Cancel
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210043, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
				
				ret = false;
			}			
		}
		
		return ret;
	}
	
	/**
	 * 订单取消
	 * 
	 * @param sourceOrderId 网络订单号
	 * @param orderNumber 订单号
	 * @param reason 取消原因 
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return
	 */
	@Override
	public void cancelOrderMain(String sourceOrderId, String orderNumber, String reason, AjaxResponseBean result, UserSessionBean user) {
		
		// 当前订单信息取得
		// 		订单信息取得
		OutFormOrderdetailOrders ordersInfo = orderDetailDao.getOrdersInfo(orderNumber);
		// 		订单详情取得
		List<OutFormOrderDetailOrderDetail> orderDetailsList = orderDetailDao.getOrderDetailsInfo(orderNumber);
		
		// Cancel 订单许可检查
		if (chkCancelOrder(ordersInfo, orderDetailsList, result)) {

			// 订单取消
			if (cancelOrder(sourceOrderId, orderNumber, reason, ordersInfo, orderDetailsList, user, result)) {
				//		正常
				setSuccessReturn(sourceOrderId, orderNumber, result, user);
			}
		}
	}
	
	/**
	 * 订单取消 许可检查
	 *
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * @param result 返回结果
	 * 
	 * @return
	 */
	private boolean chkCancelOrder(OutFormOrderdetailOrders ordersInfo, List<OutFormOrderDetailOrderDetail> orderDetailsList, AjaxResponseBean result) {
		boolean ret = true;
		
		for (int i = 0; i < orderDetailsList.size(); i++) {
			OutFormOrderDetailOrderDetail orderDetailInfo = orderDetailsList.get(i);
			
			// Cancel LineItem 允许检查
			ret = chkLineItemOptPermit(Operation.CancelOrder, ordersInfo, orderDetailInfo, result);
			
			if (ret) {
				// return 物品检查
				if ("1".equals(orderDetailInfo.getQuantityReturned())) {
					
					//	该订单存在Return明细，必须先Unreturn明细后，再进行订单Cancel
					result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210019, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);				
					ret = false;
					
					break;
				}
			} else {
				break;
			}

		}		
		return ret;
	}
	
	/**
	 * 订单取消 
	 *
	 * @param orderNumber 订单号
	 * @param reason 原因
	 * @param ordersInfo 当前订单信息 
	 * @param orderDetailsList 订单明细信息
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean cancelOrder(String sourceOrderId,
								String orderNumber, 
								String reason,
								OutFormOrderdetailOrders ordersInfo,
								List<OutFormOrderDetailOrderDetail> orderDetailsList,
								UserSessionBean user,
								AjaxResponseBean result) {
		boolean ret = true;

		logger.info("cancelOrder start");
		TransactionStatus status=transactionManager.getTransaction(def);
		
		try {
			// 被删除明细项目缓存
			ArrayList<OutFormOrderDetailOrderDetail> cancelledItems = new ArrayList<OutFormOrderDetailOrderDetail>();
			
			// 订单价格清0
			OrderPrice orderPrice = getOrderPriceMainForCancelOrder(ordersInfo);
			
			// 主订单价格取得
			OrderPrice mainOrderPrice = getMainOrderPrice(ordersInfo.getSourceOrderId(), orderPrice);
			
			// 订单明细保存
			logger.info("saveOrderDetailInfoForCancelOrder");
			ret = saveOrderDetailInfoForCancelOrder(orderDetailsList, cancelledItems, user);
			
			// 订单取消
			if (ret) {
				logger.info("saveOrderPriceForCancelOrder");
				ret = saveOrderPriceForCancelOrder(orderPrice, true, user);
			}
			
			// 一组订单价格更新
			if (ret) {
				logger.info("saveGroupOrderPrice");
				ret = saveGroupOrderPrice(mainOrderPrice, ordersInfo, user);
			}
			
			// 订单Notes保存
			if (ret) {
				logger.info("saveOrderNotesForCancelOrder");
				ret = saveOrderNotesForCancelOrder(sourceOrderId, orderNumber, true, reason, ordersInfo, cancelledItems, user);
			}
			
			//	synShip 同步
			if (ret) {
				logger.info("syncSynshipFinalGrandTotalAndOrderStatus");
				ret = syncSynshipFinalGrandTotalAndOrderStatus(orderPrice, OmsCodeConstants.OrderStatus.CANCELED, user);
			}

			if (!ret) {
				// 订单取消异常
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210005, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}

			//	菜鸟订单取消
			if (ret) {
				if (OmsConstants.ShipChannel.SYB.equals(orderDetailsList.get(0).getShipChannel())) {
					logger.info("cancelSYBOrder");
					ret = cancelSYBOrder(ordersInfo, user, result);
				}
			}

			//	CA订单取消
			if (ret) {
				if (isNeedCancelCAOrder(ordersInfo.getOrderChannelId())) {
					//	CA 订单取消与明细取消相同
					logger.info("cancelOrderDetailInfoForCA");
					ret = cancelOrderDetailInfoForCA(ordersInfo, orderDetailsList, user, result, false);
				}
			}

			//	KitBag订单取消
			if (ret) {
				if (isNeedCancelKitbagOrder(ordersInfo.getOrderChannelId())) {
					logger.info("cancelOrderForKitBag");
					ret = cancelOrderForKitBag(ordersInfo, result, user);
				}
			}

			//	第三方订单取消
			if (ret) {
				if (isNeedCancelThirdPartyOrder(ordersInfo.getOrderChannelId())) {
					logger.info("cancelOrderForThirdParty");
					ret = cancelOrderForThirdParty(ordersInfo, result, user);
				}
			}
			
			if (ret) {
				logger.info("cancelOrder success");
				transactionManager.commit(status);
			} else {
				logger.error("cancelOrder error");
				transactionManager.rollback(status);
			}			
			
		} catch (Exception e) {
			ret = false;
			
			logger.error("cancelOrder", e);
			
			transactionManager.rollback(status);

			// 订单取消异常
			result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, "cancelOrder error : " + e.getMessage());
		}
		
		return ret;
	}

	/**
	 * 速邮宝订单取消
	 *
	 * @param bean 订单信息
	 * @param result 执行结果
	 *
	 * @return 订单价格
	 */
	private boolean cancelSYBOrder(OutFormOrderdetailOrders bean, UserSessionBean user, AjaxResponseBean result) throws ApiException {
		boolean ret = true;

		String lgOrderCode = "";
		lgOrderCode = cainiaoDao.getLogisticsId(bean.getSourceOrderId());

		if (!StringUtils.isEmpty(lgOrderCode)) {
			// 物流宝订单取消
			WlbImportsOrderCancelResponse tmallResponse = callTmallApiWLBOrderCancel(bean, lgOrderCode);
			boolean tmallResponseRet = tmallResponse.getIsSuccess();

			if (!tmallResponseRet) {
				// 物流宝订单状态查询
				WlbImportsOrderGetResponse tmallOrderGetResponse = callTmallApiWLBOrderGet(bean);
				if (tmallOrderGetResponse.isSuccess()) {
					if (!OmsConstants.WLBOrderStatusCode.ORDER_CANCELED.equals(tmallOrderGetResponse.getOrders().get(0).getStatusCode())) {
						ret = false;

						String errorMsg = String.format(OmsMessageConstants.MessageContent.WLB_ORDER_GET_INFO, tmallOrderGetResponse.getOrders().get(0).getStatusCode());
						result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, errorMsg);
					}
				} else {
					ret = false;

					String errorMsg = String.format(OmsMessageConstants.MessageContent.WLB_ORDER_CANCEL_ERROR, tmallResponse.getResultErrorCode(), tmallResponse.getResultErrorMsg());
					result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, errorMsg);
				}
			} else {

				String note = OmsConstants.SYB_ORDER_CANCELLED;

				// 订单Notes
				String noteId = addNotes(bean.getSourceOrderId(),
						OmsConstants.NotesType.SYSTEM,
						bean.getOrderNumber(),
						note,
						user.getUserName(),
						user.getUserName());
				if (StringUtils.isEmpty(noteId)) {
					ret = false;
				}
			}
		}


		return ret;
	}

	/**
	 * 是否需要取消CA的订单
	 *
	 * @param orderChannelId 订单渠道ID
	 *
	 * @return 订单价格
	 */
	private boolean isNeedCancelCAOrder(String orderChannelId) {
		boolean ret = false;
		String needCancelCA = ChannelConfigs.getVal1(orderChannelId, Name.need_cancel_CA);
		if (!StringUtils.isEmpty(needCancelCA) && OmsConstants.PERMIT_OK.equals(needCancelCA)) {
			ret = true;
		}

		return  ret;
	}

	/**
	 * 是否需要取消Kitbag的订单
	 *
	 * @param orderChannelId 订单渠道ID
	 *
	 * @return 订单价格
	 */
	private boolean isNeedCancelKitbagOrder(String orderChannelId) {
		boolean ret = false;
		String needCancelKitBag = ChannelConfigs.getVal1(orderChannelId, Name.need_cancel_KitBag);
		if (!StringUtils.isEmpty(needCancelKitBag) && OmsConstants.PERMIT_OK.equals(needCancelKitBag)) {
			ret = true;
		}

		return  ret;
	}

	/**
	 * 是否需要取消第三方的订单
	 *
	 * @param orderChannelId 订单渠道ID
	 *
	 * @return 订单价格
	 */
	private boolean isNeedCancelThirdPartyOrder(String orderChannelId) {
		boolean ret = false;
		String needCancelThirdParty = ChannelConfigs.getVal1(orderChannelId, Name.need_cancel_ThirdParty);
		if (!StringUtils.isEmpty(needCancelThirdParty) && OmsConstants.PERMIT_OK.equals(needCancelThirdParty)) {
			ret = true;
		}

		return  ret;
	}

	/**
	 * 取得订单金额 （订单取消）
	 *
	 * @param ordersInfo 订单信息
	 *
	 * @return 订单价格
	 */
	private OrderPrice getOrderPriceMainForCancelOrder(OutFormOrderdetailOrders ordersInfo) {
		
		// 订单价格
		OrderPrice orderPrice = getOrderPrice(ordersInfo);
		
		// 设定调整价格 清0
		orderPrice.setFinalProductTotal(0f);
		orderPrice.setRevisedSurcharge(0f);
		orderPrice.setRevisedDiscount(0f);
		orderPrice.setFinalShippingTotal(0f);
		orderPrice.setFinalGrandTotal(0f);
//		orderPrice.setExpectedNet(0f);
		
		// 价格再计算
		priceReCalculate(orderPrice);
		
		return orderPrice;
	}
	
	/**
	 * 订单明细保存 （订单取消）
	 *
	 * @param orderDetailsList 订单明细信息
	 * @param cancelledItems 删除明细缓存（O）
	 * 
	 * @return
	 */
	private boolean saveOrderDetailInfoForCancelOrder(List<OutFormOrderDetailOrderDetail> orderDetailsList, ArrayList<OutFormOrderDetailOrderDetail> cancelledItems, UserSessionBean user) {
		boolean ret = true;
		
		for (int i = 0; i < orderDetailsList.size(); i++) {
			OutFormOrderDetailOrderDetail orderDetailInfoFromDB = orderDetailsList.get(i);
			
			// 所有物品都可以取消 20150510
			// 产品的场合
//			if ("0".equals(orderDetailInfoFromDB.getAdjustment())) {
//			if (!orderDetailInfoFromDB.isAdjustment()) {
				// cancelled 以外的场合
//				if (!OmsCodeConstants.OrderStatus.CANCELED.equals(orderDetailInfoFromDB.getStatus())) {
				if ("1".equals(orderDetailInfoFromDB.getQuantityShipped())) {
					// 订单明细Bean
					OrderDetailsBean orderDetailInfo = new OrderDetailsBean();
					//		订单号
					orderDetailInfo.setOrderNumber(orderDetailInfoFromDB.getOrderNumber());
					//		订单明细番号
					orderDetailInfo.setItemNumber(orderDetailInfoFromDB.getItemNumber());
					//		产品
					orderDetailInfo.setProduct(orderDetailInfoFromDB.getProduct());
					//		quantity_shipped
					orderDetailInfo.setQuantityShipped("0");
					//		订单明细状态
					orderDetailInfo.setStatus(OmsCodeConstants.OrderStatus.CANCELED);
					//  	更新者
					orderDetailInfo.setModifier(user.getUserName());
					ret = orderDetailDao.updateOrderDetailsInfo(orderDetailInfo);
					
					if(!ret) {
						break;
					}
					
					// 被删除项目缓存
					OutFormOrderDetailOrderDetail cancelledItem = new OutFormOrderDetailOrderDetail();
					//		订单号
					cancelledItem.setOrderNumber(orderDetailInfoFromDB.getOrderNumber());
					//		订单明细番号
					cancelledItem.setItemNumber(orderDetailInfoFromDB.getItemNumber());
					//		关联明细番号
					cancelledItem.setSubItemNumber(orderDetailInfoFromDB.getSubItemNumber());
					//		adjustment
					cancelledItem.setAdjustment(orderDetailInfoFromDB.isAdjustment());
					//		SKU
					cancelledItem.setSku(orderDetailInfoFromDB.getSku());
					//		price_per_unit
					cancelledItem.setPricePerUnit(orderDetailInfoFromDB.getPricePerUnit());
					cancelledItems.add(cancelledItem);
				}
//			}
		}
		
		return ret;
	}

	/**
	 * 订单明细取消　（channeladvisor）
	 *
	 * @param orderDetailsList 订单明细信息
	 *
	 * @return
	 */
	private boolean cancelOrderDetailInfoForCA(OutFormOrderdetailOrders ordersInfo, List<OutFormOrderDetailOrderDetail> orderDetailsList, UserSessionBean user, AjaxResponseBean result, boolean isReturn) throws Exception {
		boolean ret = false;

		// 绑定折扣处理
		for (int i = 0; i < orderDetailsList.size(); i++) {
			OutFormOrderDetailOrderDetail orderDetailsInfo = orderDetailsList.get(i);
			// 物品的场合
			if (!orderDetailsInfo.isAdjustment()) {
				// 打折信息加算
				float discount = 0;
				for (int j = 0; j < orderDetailsList.size(); j++) {
					OutFormOrderDetailOrderDetail orderDetailsDiscountInfo = orderDetailsList.get(j);
					if (orderDetailsInfo.getItemNumber().equals(orderDetailsDiscountInfo.getSubItemNumber())) {
						discount = add2Digits(discount, Float.valueOf(orderDetailsDiscountInfo.getPricePerUnit()));
					}
				}

				// 打折信息设定
				if (discount != 0) {
					orderDetailsInfo.setDiscount(String.valueOf(discount));
				}

				// 售价设定（该处discount 已经 *-1）
				float price = add2Digits(Float.valueOf(orderDetailsInfo.getPricePerUnit()), discount);
				orderDetailsInfo.setPrice(String.valueOf(price));
			}
		}

		// CA订单取消预处理
		ArrayOfRefundItem refundItems = new ArrayOfRefundItem();
		for (int i = 0; i < orderDetailsList.size(); i++) {
			OutFormOrderDetailOrderDetail orderDetailInfoFromDB = orderDetailsList.get(i);

			if ("1".equals(orderDetailInfoFromDB.getQuantityShipped()) && !orderDetailInfoFromDB.isAdjustment()) {
				RefundItem refundItem = new RefundItem();
				refundItem.setSKU(orderDetailInfoFromDB.getSku());
//				refundItem.setAmount(new BigDecimal(0.00));
//				refundItem.setAmount(new BigDecimal(orderDetailInfoFromDB.getPricePerUnit()));
				refundItem.setAmount(new BigDecimal(orderDetailInfoFromDB.getPrice()));

				refundItem.setShippingAmount(new BigDecimal(0.00));
				refundItem.setShippingTaxAmount(new BigDecimal(0.00));
				refundItem.setTaxAmount(new BigDecimal(0.00));
				refundItem.setQuantity(1);

				refundItems.getRefundItem().add(refundItem);
			}
		}

		RefundOrderRequest refundOrderRequest = new RefundOrderRequest();
		// 原订单的场合
		if (StringUtils.isEmpty(ordersInfo.getSubSourceOrderId())) {
			refundOrderRequest.setClientOrderIdentifier(ordersInfo.getSourceOrderId());
		// 子订单的场合
		} else {
			refundOrderRequest.setClientOrderIdentifier(ordersInfo.getSubSourceOrderId());
		}

		refundOrderRequest.setRefundItems(refundItems);

		//获取当前销售渠道的第三方配置信息
		HashMap<String, ThirdPartyConfigBean> configs = ThirdPartyConfigs.getThirdPartyConfigMap(ordersInfo.getOrderChannelId());

		APIResultOfRefundOrderResponse apiResultOfRefundOrderResponse = orderRefundService.submitOrderRefund(refundOrderRequest, configs);

		// 正常返回的场合
//		if (OmsConstants.CAResult.Status_Success.equals(apiResultOfRefundOrderResponse.getStatus())) {
		if (apiResultOfRefundOrderResponse.getMessageCode() == 0) {
			ret = true;
		} else {
			// return 的场合
			if (isReturn) {
				result.setResult(false,
						MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION,
						String.format(OmsMessageConstants.MessageContent.CA_CANCEL_ORDER_REQ_ERROR, apiResultOfRefundOrderResponse.getMessageCode(), apiResultOfRefundOrderResponse.getMessage()));
			} else {
			// cancel 的场合
				List<Object> retGetOrderStatusInfoForCA = getOrderStatusInfoForCA(ordersInfo, result);
				// 执行结果
				boolean getOrderStatusRet = (boolean)retGetOrderStatusInfoForCA.get(0);
				// CheckoutStatus
				String orderStatus = (String)retGetOrderStatusInfoForCA.get(1);
				if (getOrderStatusRet) {
					if (OmsConstants.CACheckoutStatus.Cancelled.equals(orderStatus)) {
						ret = true;
					} else {
						result.setResult(false,
								MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION,
								String.format(OmsMessageConstants.MessageContent.CA_GET_ORDER_INFO_REQ_CHECKOUTSTATUS, orderStatus));
					}
				} else {
					result.setResult(false,
							MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION,
							String.format(OmsMessageConstants.MessageContent.CA_CANCEL_ORDER_REQ_ERROR, apiResultOfRefundOrderResponse.getMessageCode(), apiResultOfRefundOrderResponse.getMessage()));
				}
			}
		}

		return ret;
	}

	/**
	 * 订单取得　（channeladvisor）
	 *
	 * @param ordersInfo 订单信息
	 *
	 * @return
	 */
	private List<Object> getOrderStatusInfoForCA(OutFormOrderdetailOrders ordersInfo, AjaxResponseBean result) throws Exception {
		List<Object> ret = new ArrayList<>();

		// 执行结果
		boolean isSuccess = true;
		//	订单状态
		String orderStatusForCA = "";

		OrderCriteria orderCriteria = new OrderCriteria();
		ArrayOfString clientOrderIdentifierList = new ArrayOfString();
		// 原订单的场合
		if (StringUtils.isEmpty(ordersInfo.getSubSourceOrderId())) {
			clientOrderIdentifierList.getString().add(ordersInfo.getSourceOrderId());
			// 子订单的场合
		} else {
			clientOrderIdentifierList.getString().add(ordersInfo.getSubSourceOrderId());
		}

		orderCriteria.setClientOrderIdentifierList(clientOrderIdentifierList);

		//获取当前销售渠道的第三方配置信息
		HashMap<String, ThirdPartyConfigBean> configs = ThirdPartyConfigs.getThirdPartyConfigMap(ordersInfo.getOrderChannelId());
//		HashMap<String, ThirdPartyConfigBean> configs = ThirdPartyConfigs.getThirdPartyConfigMap("010");

		APIResultOfArrayOfOrderResponseItem apiResultOfArrayOfOrderResponseItem = orderService.getOrderList(orderCriteria, configs);
		if (apiResultOfArrayOfOrderResponseItem.getMessageCode() == 0) {
			ArrayOfOrderResponseItem arrayOfOrderResponseItem = apiResultOfArrayOfOrderResponseItem.getResultData();

			List<OrderResponseDetailHigh> orderResponseDetailHighList = arrayOfOrderResponseItem.getOrderResponseItem();
			OrderResponseDetailHigh orderResponseDetailHigh = orderResponseDetailHighList.get(0);

			OrderStatus orderStatus = orderResponseDetailHigh.getOrderStatus();
			orderStatusForCA = orderStatus.getCheckoutStatus();

		} else {
			isSuccess = false;
			result.setResult(false,
					MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION,
					String.format(OmsMessageConstants.MessageContent.CA_GET_ORDER_INFO_REQ_ERROR, apiResultOfArrayOfOrderResponseItem.getMessageCode(), apiResultOfArrayOfOrderResponseItem.getMessage()));
		}

		ret.add(isSuccess);
		ret.add(orderStatusForCA);
		return ret;
	}

	/**
	 * 订单价格保存
	 * 
	 * @param orderPrice 订单价格
	 * @param cancelFlag 订单取消标志
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrderPriceForCancelOrder(OrderPrice orderPrice, boolean cancelFlag, UserSessionBean user) {
		
		boolean ret = true;
		
		// 订单
		//		订单价格取得
		OrdersBean ordersInfo = setOrderPrice(orderPrice);
		//		cancelledFalg
		ordersInfo.setCancelled(cancelFlag);
		
		//	订单状态
		if (cancelFlag) {
			//	Cancel 的场合		
			ordersInfo.setOrderStatus(OmsCodeConstants.OrderStatus.CANCELED);
		} else {
			//	TODO 暂时approved
			//	恢复的场合
			ordersInfo.setOrderStatus(OmsCodeConstants.OrderStatus.APPROVED);
		}

		
		// 		modifier
		ordersInfo.setModifier(user.getUserName());
		
		ret = orderDetailDao.updateOrdersCancelInfo(ordersInfo);
		
		return ret;
	}
	
	/**
	 * 订单Note保存（取消订单）
	 *
	 * @param sourceOrderId 网络订单号
	 * @param orderNumber 订单号
	 * @param cancelFlag 取消状态
	 * @param reason 原因
	 * @param cancelledItems 被删除明细
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrderNotesForCancelOrder(String sourceOrderId,
													String orderNumber,
													boolean cancelFlag,
													String reason,
												 	OutFormOrderdetailOrders ordersInfo,
													ArrayList<OutFormOrderDetailOrderDetail> cancelledItems,
													UserSessionBean user) {
		boolean ret = true;		

		// 明细行番号
		int itemNumber = orderDetailDao.getOrderDetailsNotesMaxItemNumber(orderNumber) + 1;		

		String notes = "";
		if (cancelFlag) {
			// "Order Status changed to: Canceled. Reason: %s";
			notes = String.format(OmsConstants.CANCEL_ORDER, reason);
		} else  {
			// "Canceled order restored. Reason: %s";
			notes = String.format(OmsConstants.RESTORED_ORDER, reason);
		}
		
		//	订单更新Notes
		//	订单Notes
		NotesBean notesInfo = new NotesBean();
		// 		sourceOrderId
		notesInfo.setSourceOrderId(sourceOrderId);
		//		type
		notesInfo.setType(OmsConstants.NotesType.SYSTEM);
		// 		order_number
		notesInfo.setNumericKey(orderNumber);
		//		item_number
		notesInfo.setItemNumber(String.valueOf(itemNumber));
		//		notes
		notesInfo.setNotes(notes);
		//		entered_by
		notesInfo.setEnteredBy(user.getUserName());
		//		creater
		notesInfo.setCreater(user.getUserName());
		//		modifier
		notesInfo.setModifier(user.getUserName());
		
		ret = notesDao.insertNotesInfo(notesInfo);
		
		//	订单明细更新
		if (ret) {
			itemNumber = itemNumber + 1;
			
			for (int i = 0; i < cancelledItems.size(); i++) {
				
				// "item #%s SKU=%s Status changed to: Canceled."
				notes = String.format(OmsConstants.CANCEL_ORDER_LINEITEM_NOTES_FORMAT_FORPRODUCT, 
										cancelledItems.get(i).getItemNumber(),
										cancelledItems.get(i).getSku());
				// 		sourceOrderId
				notesInfo.setSourceOrderId(sourceOrderId);
				//		type
				notesInfo.setType(OmsConstants.NotesType.SYSTEM);
				// 		order_number
				notesInfo.setNumericKey(orderNumber);
				//		item_number
				notesInfo.setItemNumber(String.valueOf(itemNumber));
				//		notes
				notesInfo.setNotes(notes);
				//		entered_by
				notesInfo.setEnteredBy(user.getUserName());
				//		modifier
				notesInfo.setModifier(user.getUserName());
				
				ret = notesDao.insertNotesInfo(notesInfo);
				
				if (!ret) {
					break;
				} else {					
					// transactions 信息更新
					String description = cancelledItems.get(i).getSku();
					if (!cancelledItems.get(i).isAdjustment()) {
						description = OrderDetailSkuDsp.PRODUCT_TITLE;
					}
					
					// orderNumber, description, debt, credit, noteId, user
					ret = saveOrderTransactionsInfo(sourceOrderId,
													ordersInfo.getOriginSourceOrderId(),
													cancelledItems.get(i).getOrderNumber(),
													getTransactionSku(cancelledItems.get(i), cancelledItems),
													cancelledItems.get(i).getItemNumber(),
													description,
													cancelledItems.get(i).getPricePerUnit(),
													notesInfo.getId(),
													OmsConstants.Transaction_Type.REFUND,
													true,
													user);
					if (!ret) {
						break;
					}
				}
				
				itemNumber = itemNumber + 1;
			}
		}
		
		return ret;
	}

	/**
	 * 明细sku取得（transaction 用，含关联ＳＫＵ）
	 *
	 * @param orderDetailInfo　待处理订单明细
	 * @param orderDetailList 订单明细一览
	 * @return
	 */
	private String getTransactionSku(OutFormOrderDetailOrderDetail orderDetailInfo, List<OutFormOrderDetailOrderDetail> orderDetailList) {
		String ret = "";
		
		// 无关联项目的场合
		if (StringUtils.isEmpty(orderDetailInfo.getSubItemNumber()) || "0".equals(orderDetailInfo.getSubItemNumber())) {
			ret = orderDetailInfo.getSku();
		} else {
		// 有关联项目的场合
			for (int i = 0; i < orderDetailList.size(); i++) {
				if (orderDetailInfo.getSubItemNumber().equals(orderDetailList.get(i).getItemNumber())) {
//					ret = orderDetailInfo.getSku();
					ret = orderDetailList.get(i).getSku();
					break;
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * 订单恢复（该机能已废止）
	 * 
	 * @return
	 */
	@Override
	public void revertOrderMain(String orderNumber, AjaxResponseBean result, UserSessionBean user) {
		// 当前订单信息取得
		// 		订单信息取得
		OutFormOrderdetailOrders ordersInfo = orderDetailDao.getOrdersInfo(orderNumber);
		// 		订单详情取得
		List<OutFormOrderDetailOrderDetail> orderDetailsList = orderDetailDao.getOrderDetailsInfo(orderNumber);
		
		// 订单取消
		if (revertOrder(orderNumber, ordersInfo, orderDetailsList, user)) {
		
			// 订单刷新
			//		订单信息返回
			OutFormOrderdetailOrders orderInfo = getOrdersInfo(orderNumber, user);
//			//		订单Notes信息
//			List<OutFormOrderdetailNotes> orderNotesList = getOrderNotesInfo(orderNumber);
			
			// 设置返回结果
			Map<String, Object> ordersListMap = new HashMap<String, Object>();			
			// 		订单
			ordersListMap.put("orderInfo", orderInfo);
//			// 		订单NotesList
//			ordersListMap.put("orderNotesList", orderNotesList);
			
			result.setResultInfo(ordersListMap);
			
			//		正常
			result.setResult(true);
		} else {
			// 订单取消异常
//			result.setResult(false, "200009", 2);
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210022, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}
	}
	
	/**
	 * 订单恢复
	 * 
	 * @param orderNumber 订单号
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean revertOrder(String orderNumber, OutFormOrderdetailOrders ordersInfo, List<OutFormOrderDetailOrderDetail> orderDetailsList, UserSessionBean user) {
		boolean ret = true;
		
		TransactionStatus status=transactionManager.getTransaction(def);
		
		try {
			
			// 订单价格再计算
			OrderPrice orderPrice = getOrderPriceMainForRevertOrder(ordersInfo, orderDetailsList);
			
			// 订单恢复
			ret = saveOrderPriceForCancelOrder(orderPrice, false, user);
			
			if (ret) {
				// 订单明细保存
				ret = saveOrderDetailInfoForRevert(orderDetailsList, user);
			}
			
			// 订单Notes保存
			if (ret) {
				ret = saveOrderNotesForRevertOrder(orderNumber, user);
			}
			
			//	synShip 同步
			if (ret) {
				ret = syncSynshipFinalGrandTotal(orderPrice, user);
			}
			
			transactionManager.commit(status);			
			
		} catch (Exception e) {
			ret = false;
			
			logger.error("revertOrder", e);
			
			transactionManager.rollback(status);
		}		
		
		return ret;
	}

	/**
	 * 订单价格再计算（订单恢复）
	 *
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * 
	 * @return 订单价格
	 */
	private OrderPrice getOrderPriceMainForRevertOrder(OutFormOrderdetailOrders ordersInfo, List<OutFormOrderDetailOrderDetail> orderDetailsList) {
		
		// 订单价格（Actual Net）取得
		OrderPrice orderPrice = getOrderPrice(ordersInfo);
		
		// 设定调整价格
		for (int i = 0; i < orderDetailsList.size(); i++) {
			OutFormOrderDetailOrderDetail orderDetailInfo = orderDetailsList.get(i);
			String sku = orderDetailInfo.getSku();			
			
			// Surcharge
			if (OmsConstants.OrderDetailSkuDsp.SURCHARGE_TITLE.equals(sku)) {
				float surcharge = add2Digits(orderPrice.getRevisedSurcharge(), Float.valueOf(orderDetailInfo.getPricePerUnit()));
				orderPrice.setRevisedSurcharge(surcharge);
				
			// Discount
			} else if (OmsConstants.OrderDetailSkuDsp.DISCOUNT_TITLE.equals(sku)) {
				float discount = add2Digits(orderPrice.getRevisedDiscount(), Float.valueOf(orderDetailInfo.getPricePerUnit()));
				orderPrice.setRevisedDiscount(discount);
				
			// Shipping
			} else if (OmsConstants.OrderDetailSkuDsp.SHIPPING_TITLE.equals(sku)) {
				float shipping = add2Digits(orderPrice.getFinalShippingTotal(), Float.valueOf(orderDetailInfo.getPricePerUnit()));
				orderPrice.setFinalShippingTotal(shipping);
				
			} else {
			// product total
				String product = orderDetailInfo.getProduct();
				//	cancelled product 去除
//				if (product.length() < 9 || !OmsConstants.PRODUCT_CANCELLED_TITLE.equals(product.substring(0, 9))) {
				if (!isCanceledProduct(orderDetailInfo))	{
					float productTotal = add2Digits(orderPrice.getFinalProductTotal(), Float.valueOf(orderDetailInfo.getPricePerUnit()));
					orderPrice.setFinalProductTotal(productTotal);
				}
			}
		}
		
		// 价格再计算
		priceReCalculate(orderPrice);
		
		return orderPrice;
	}
	
	/**
	 * 订单明细保存 （订单恢复）
	 *
	 * @param orderDetailsList 订单明细信息
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrderDetailInfoForRevert(List<OutFormOrderDetailOrderDetail> orderDetailsList, UserSessionBean user) {
		boolean ret = true;
		
		for (int i = 0; i < orderDetailsList.size(); i++) {
			OutFormOrderDetailOrderDetail orderDetailInfoFromDB = orderDetailsList.get(i);
			
			// 产品的场合
//			if ("0".equals(orderDetailInfoFromDB.getAdjustment())) {
			if (!orderDetailInfoFromDB.isAdjustment()) {
				
				// cancelled 以外的场合
				if (!isCanceledProduct(orderDetailInfoFromDB)) {
					// 订单明细Bean
					OrderDetailsBean orderDetailInfo = new OrderDetailsBean();
					//		订单号
					orderDetailInfo.setOrderNumber(orderDetailInfoFromDB.getOrderNumber());
					//		订单明细番号
					orderDetailInfo.setItemNumber(orderDetailInfoFromDB.getItemNumber());
					//		产品
					orderDetailInfo.setProduct(orderDetailInfoFromDB.getProduct());
					//		quantity_shipped
					orderDetailInfo.setQuantityShipped("1");
					//  	更新者
					orderDetailInfo.setModifier(user.getUserName());
					ret = orderDetailDao.updateOrderDetailsInfo(orderDetailInfo);
					
					if(!ret) {
						break;
					}
				}
			}
			
		}
		
		return ret;
	}
	
	/**
	 * 被Canceled订单明细判定 （例：CANCELED: The North Face Gotham 550 Fill Down Jacket）
	 *
	 * @param orderDetailInfo 订单明细
	 * 
	 * @return
	 */
	private boolean isCanceledProduct(OutFormOrderDetailOrderDetail orderDetailInfo) {
		boolean ret = false;
		
		String product = orderDetailInfo.getProduct();
		
		if (product.length() > 9 && OmsConstants.PRODUCT_CANCELLED_TITLE.substring(0, 9).equals(product.substring(0, 9))) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 订单Note保存
	 *
	 * @param orderNumber 订单号 
	 * @param user 订单明细信息
	 * 
	 * @return
	 */
	private boolean saveOrderNotesForRevertOrder(String orderNumber, UserSessionBean user) {
		boolean ret = true;
		
		int itemNumber = orderDetailDao.getOrderDetailsNotesMaxItemNumber(orderNumber) + 1;
		
		// notes取得
		String notes = OmsConstants.CANCEL_ORDER_RESTORE;
		
		// 订单Notes
		NotesBean notesInfo = new NotesBean();
		
		//		type
		notesInfo.setType(OmsConstants.NotesType.SYSTEM);
		// 		order_number
		notesInfo.setNumericKey(orderNumber);
		//		item_number
		notesInfo.setItemNumber(String.valueOf(itemNumber));
		//		notes
		notesInfo.setNotes(notes);
		//		entered_by
		notesInfo.setEnteredBy(user.getUserName());
		//		creater
		notesInfo.setCreater(user.getUserName());
		//		modifier
		notesInfo.setModifier(user.getUserName());
		
		ret = notesDao.insertNotesInfo(notesInfo);
		
		return ret;
	}
	
	/**
	 * 改变订单状态 
	 * 
	 * @param sourceOrderId 网络订单号
	 * @param lockFlag 需变更订单状态
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean changeLockStatus(String sourceOrderId, boolean lockFlag, UserSessionBean user) {
		boolean ret = true;
		
		TransactionStatus status=transactionManager.getTransaction(def);
		
		try {
			
			// 锁单子函数
			ret = changeLockStatusSub(sourceOrderId, lockFlag, user);
			
			if (ret) {
				transactionManager.commit(status);
			} else {
				transactionManager.rollback(status);
			}

		} catch (Exception e) {
			ret = false;
			
			logger.error("changeLockStatus", e);
			
			transactionManager.rollback(status);
		}
		
		return ret;
	}
	
	/**
	 * 锁单子函数（不含事务）
	 * 
	 * @param sourceOrderId 网络订单号
	 * @param lockFlag 需变更订单状态
	 * @param user 当前用户
	 * 
	 * @return
	 */
	public boolean changeLockStatusSub(String sourceOrderId, boolean lockFlag, UserSessionBean user) {
		
		boolean ret = true;
		
		// 订单
		OrdersBean ordersInfo = new OrdersBean();		
		// 		网络订单号
		ordersInfo.setSourceOrderId(sourceOrderId);;
		//		锁定状态
		//		synship 更新用
		ordersInfo.setLocalShipOnHold(lockFlag);
		//		OMS 更新用
		if (lockFlag) {
			ordersInfo.setLockShip(LockShip.YES);
		} else {
			ordersInfo.setLockShip(LockShip.No);
		}

		// 		modifier
		ordersInfo.setModifier(user.getUserName());
		
		ret = orderDetailDao.updateGroupOrdersLockInfo(ordersInfo);
		
		// 关联订单号取得
		List<OutFormOrderdetailOrders> ordersList = orderDetailDao.getOrdersListBySourceOrderId(sourceOrderId);
		
		for (int i = 0; i < ordersList.size(); i++) {
			String orderNumber = ordersList.get(i).getOrderNumber();
			
			// 订单Notes保存
			if (ret) {				
				ret = saveOrderNotesForChangeLockStatus(sourceOrderId, orderNumber, lockFlag, user);
				
				if (!ret) {
					break;
				}
			}
			
			// synship 同步
			if (ret) {
				ordersInfo.setOrderNumber(orderNumber);
				ret = synShipSyncDao.updateLockStatus(ordersInfo);
				
				if (!ret) {
					break;
				}
			}				
		}
		
		return ret;
	}
	
	/**
	 * 订单状态更新
	 * 
	 * @return
	 */
	@Override
	public void setOrderStatus(OrdersBean orderInfo, AjaxResponseBean result, UserSessionBean user) {
		
		boolean ret = orderDetailDao.updateOrdersStatusInfo(orderInfo);
		
		if (ret) {
			//		正常
			result.setResult(true);
		} else  {
			// 订单取消异常
//			result.setResult(false, "200012", 2);
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210007, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}
	}
	
	/**
	 * 订单明细状态更新
	 * 
	 * @return
	 */
	@Override
	public void setOrderDetailStatus(OrderDetailsBean orderInfo, AjaxResponseBean result, UserSessionBean user) {
		
		boolean ret = orderDetailDao.updateOrderDetailsStatusInfo(orderInfo);
		
		if (ret) {
			//		正常
			result.setResult(true);
		} else  {
			// 订单取消异常
//			result.setResult(false, "200013", 2);
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210008, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}
	}
	
	/**
	 * 订单其他属性更新主函数
	 * 
	 * @return
	 */
	@Override
	public void updateOrderOtherPropMain(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {
		
		if (updateOrderOtherPropSub(bean, result, user)) {
			// Notes 结果返回
			setNotesToResult(bean.getSourceOrderId(), user, result);
			
			//	正常
			result.setResult(true);
		}
	}
	
	/**
	 * 订单其他属性更新子函数
	 * 
	 * @return
	 */
	private boolean updateOrderOtherPropSub(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {		
		boolean ret = true;

		TransactionStatus status=transactionManager.getTransaction(def);
		
		try {
			//	更新者设定
			bean.setModifier(user.getUserName());
			
			ret = orderDetailDao.updateOrdersOtherPropInfo(bean);
			
			if (ret) {
				
				String note = String.format(OmsConstants.FREIGHT_COLLECT, bean.isOrigFreightCollect());
				
				// 订单Notes
				String noteId = addNotes(bean.getSourceOrderId(),
											OmsConstants.NotesType.SYSTEM, 
											bean.getOrderNumber(),
											note,
											user.getUserName(),
											user.getUserName());
				if (StringUtils.isEmpty(noteId)) {
					ret = false;
				}
			}
			
			if (ret) {
				
				// synship 同步
				ret = synShipSyncDao.updateFreightCollect(bean);
			}
			
			// 执行结果判定
			if (ret) {
				logger.info("updateOrderOtherPropSub success");
				
				transactionManager.commit(status);				
				
				//	正常
				result.setResult(true);
			} else {
				logger.info("updateOrderOtherPropSub failure");
				
				transactionManager.rollback(status);
				
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210009,
						 MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
		} catch (Exception ex) {
			
			logger.error("updateOrderOtherPropSub", ex);
			
			ret = false;
			
			transactionManager.rollback(status);
			
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210009,
					 MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}
		
		return ret;
	}

	/**
	 * 客户拒收更新主函数
	 *
	 * @return
	 */
	@Override
	public void updateCustomerRefusedMain(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {

		if (updateCustomerRefusedSub(bean, result, user)) {
			// Notes 结果返回
			setNotesToResult(bean.getSourceOrderId(), user, result);

			//	正常
			result.setResult(true);
		}
	}

	/**
	 * 客户拒收更新子函数
	 *
	 * @return
	 */
	private boolean updateCustomerRefusedSub(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {
		boolean ret = true;

		TransactionStatus status=transactionManager.getTransaction(def);

		try {
			//	更新者设定
			bean.setModifier(user.getUserName());

			ret = orderDetailDao.updateOrdersCustomerRefusedInfo(bean);

			if (ret) {

				String note = String.format(OmsConstants.CUSTOMER_REFUSED, bean.isOrigCustomerRefused());

				// 订单Notes
				String noteId = addNotes(bean.getSourceOrderId(),
						OmsConstants.NotesType.SYSTEM,
						bean.getOrderNumber(),
						note,
						user.getUserName(),
						user.getUserName());
				if (StringUtils.isEmpty(noteId)) {
					ret = false;
				}
			}

			// 执行结果判定
			if (ret) {
				logger.info("updateCustomerRefusedSub success");

				transactionManager.commit(status);

				//	正常
				result.setResult(true);
			} else {
				logger.info("updateCustomerRefusedSub failure");

				transactionManager.rollback(status);

				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210062,
						MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
		} catch (Exception ex) {

			logger.error("updateCustomerRefusedSub", ex);

			ret = false;

			transactionManager.rollback(status);

			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210062,
					MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}

		return ret;
	}

	/**
	 * 第三方订单取消主函数
	 *
	 * @return
	 */
	@Override
	public void cancelClientOrderMain(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {

		// 当前订单信息取得
		// 		订单信息取得
		OutFormOrderdetailOrders ordersInfo = orderDetailDao.getOrdersInfo(bean.getOrderNumber());
		// 		订单详情取得
		List<OutFormOrderDetailOrderDetail> orderDetailsList = orderDetailDao.getOrderDetailsInfo(bean.getOrderNumber());

		// Cancel 订单许可检查
		if (chkCancelThirdPartnerOrder(ordersInfo, orderDetailsList, result)) {
			if (cancelClientOrderSub(bean, result, user)) {
				// Notes 结果返回
				setNotesToResult(bean.getSourceOrderId(), user, result);

				//	正常
				result.setResult(true);
			}
		}
	}

	/**
	 * 第三方订单取消 许可检查
	 *
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * @param result 返回结果
	 *
	 * @return
	 */
	private boolean chkCancelThirdPartnerOrder(OutFormOrderdetailOrders ordersInfo, List<OutFormOrderDetailOrderDetail> orderDetailsList, AjaxResponseBean result) {
		boolean ret = true;

		// 订单未推送的场合，取消申请失败
		if (!ordersInfo.isClientOrderSendFlag()) {
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210059, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			ret  = false;
		} else {
			for (int i = 0; i < orderDetailsList.size(); i++) {
				OutFormOrderDetailOrderDetail orderDetailInfo = orderDetailsList.get(i);

				// Cancel LineItem 允许检查
				ret = chkLineItemOptPermit(Operation.CancelThirdPartnerOrder, ordersInfo, orderDetailInfo, result);

				if (ret) {
					// return 物品检查
					if ("1".equals(orderDetailInfo.getQuantityReturned())) {

						//	该订单存在Return明细，必须先Unreturn明细后，再进行订单Cancel
						result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210019, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
						ret = false;

						break;
					}
				} else {
					break;
				}
			}
		}


		return ret;
	}

	/**
	 * 第三方订单取消子函数
	 *
	 * @return
	 */
	private boolean cancelClientOrderSub(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {
		boolean ret = true;

		TransactionStatus status=transactionManager.getTransaction(def);

		try {
			//	更新者设定
			bean.setModifier(user.getUserName());

			ret = orderDetailDao.cancelClientOrder(bean);

			if (ret) {

				String note = String.format(OmsConstants.THIRD_PARTY_ORDER_CANCELLED, bean.isOrigFreightCollect());

				// 订单Notes
				String noteId = addNotes(bean.getSourceOrderId(),
						OmsConstants.NotesType.SYSTEM,
						bean.getOrderNumber(),
						note,
						user.getUserName(),
						user.getUserName());
				if (StringUtils.isEmpty(noteId)) {
					ret = false;
				}
			}

			// 执行结果判定
			if (ret) {
				logger.info("cancelClientOrderSub success");

				transactionManager.commit(status);

				//	正常
				result.setResult(true);
			} else {
				logger.info("cancelClientOrderSub failure");

				transactionManager.rollback(status);

				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210057,
						MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
		} catch (Exception ex) {

			logger.error("cancelClientOrderSub", ex);

			ret = false;

			transactionManager.rollback(status);

			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210057,
					MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}

		return ret;
	}

	/**
	 * KitBag订单取消子函数
	 *
	 * @return
	 */
	private boolean cancelOrderForKitBag(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {
		boolean ret = true;

		logger.info("	cancelOrderForKitBag isPushed = " + bean.isClientOrderSendFlag());

		if (bean.isClientOrderSendFlag()) {
			logger.info("	cancelOrderForKitBag");

			//	更新者设定
			bean.setModifier(user.getUserName());

			ret = orderDetailDao.cancelClientOrder(bean);

			if (ret) {

				String note = String.format(OmsConstants.THIRD_PARTY_ORDER_CANCELLED, bean.isOrigFreightCollect());

				// 订单Notes
				String noteId = addNotes(bean.getSourceOrderId(),
						OmsConstants.NotesType.SYSTEM,
						bean.getOrderNumber(),
						note,
						user.getUserName(),
						user.getUserName());
				if (StringUtils.isEmpty(noteId)) {
					ret = false;
				}
			}
		}

		return ret;
	}

	/**
	 * ThirdParty订单取消子函数
	 *
	 * @return
	 */
	private boolean cancelOrderForThirdParty(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {
		boolean ret = true;

		logger.info("	cancelOrderForThirdParty");

		//	更新者设定
		bean.setModifier(user.getUserName());

		ret = orderDetailDao.cancelClientOrder(bean);

		if (ret) {

			String note = String.format(OmsConstants.THIRD_PARTY_ORDER_CANCELLED, bean.isOrigFreightCollect());

			// 订单Notes
			String noteId = addNotes(bean.getSourceOrderId(),
					OmsConstants.NotesType.SYSTEM,
					bean.getOrderNumber(),
					note,
					user.getUserName(),
					user.getUserName());
			if (StringUtils.isEmpty(noteId)) {
				ret = false;
			}
		}

		return ret;
	}

	/**
	 * 订单Approve 主函数
	 * 
	 * @return
	 */
	@Override
	public void approveOrderMain(String  sourceOrderId, String orderNumber, AjaxResponseBean result, UserSessionBean user) {
		
		// 当前订单信息取得
		// 		订单信息取得
		OutFormOrderdetailOrders ordersInfo = orderDetailDao.getOrdersInfo(orderNumber);
		// 		订单详情取得
		List<OutFormOrderDetailOrderDetail> orderDetailsList = orderDetailDao.getOrderDetailsInfo(orderNumber);

		if (chkApproveOrder(ordersInfo, orderDetailsList, result)) {
			//	订单Approve
			if (approveOrder(sourceOrderId, orderNumber, ordersInfo, orderDetailsList, result, user)) {

//			// 订单刷新
//			//		订单Notes信息
//			List<OutFormOrderdetailNotes> orderNotesList = getOrderNotesInfoBySourceOrderId(sourceOrderId, user);
//
//			// 设置返回结果
//			Map<String, Object> ordersListMap = new HashMap<String, Object>();
//			// 		订单
//			ordersListMap.put("approved", true);
//			// 		订单NotesList
//			ordersListMap.put("orderNotesList", orderNotesList);
//
//			result.setResultInfo(ordersListMap);

				// 正常返回
				setSuccessReturnForPriceNoChange(sourceOrderId, orderNumber, result, user);
			}
		}
	}

	/**
	 * Approve 许可检查
	 *
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * @param result 返回结果
	 *
	 * @return
	 */
	private boolean chkApproveOrder(OutFormOrderdetailOrders ordersInfo, List<OutFormOrderDetailOrderDetail> orderDetailsList, AjaxResponseBean result) {
		boolean ret = true;
		// surcharg需要调整标志位
		boolean isNeedSurchargeAdjustment = true;

		//	surcharg需要调整标志位（配置项）
		String needSurchargeAdjustment = "";
		needSurchargeAdjustment = ChannelConfigs.getVal1(ordersInfo.getOrderChannelId(), Name.need_surcharge_adjustment);

		if (StringUtils.isEmpty(needSurchargeAdjustment) || OmsConstants.PERMIT_NG.equals(needSurchargeAdjustment)) {
			isNeedSurchargeAdjustment = false;
		}

		// 需要调整的场合
		if (isNeedSurchargeAdjustment) {

			for (int i = 0; i < orderDetailsList.size(); i++) {
				OutFormOrderDetailOrderDetail orderDetailInfo = orderDetailsList.get(i);

				// surcharge 的场合
				if (OrderDetailSkuDsp.SURCHARGE_TITLE.equals(orderDetailInfo.getSku().trim())) {
					// 状态检查
					if (!OmsCodeConstants.OrderStatus.CANCELED.equals(orderDetailInfo.getStatus())) {
						//	surcharge 未调整
						result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210061, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
						ret = false;

						break;
					}
				}
			}
		}

		return ret;
	}
	
	/**
	 * 订单Approve
	 * @param orderNumber 订单号
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * @param result 结果返回
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean approveOrder(String  sourceOrderId, String orderNumber, OutFormOrderdetailOrders ordersInfo, List<OutFormOrderDetailOrderDetail> orderDetailsList, AjaxResponseBean result, UserSessionBean user) {
		boolean ret = true;
		
		// 最大允许Approve件数( 例：<=7)
		String max_amount = ChannelConfigs.getVal1(ordersInfo.getOrderChannelId(), Name.manual_approved_max_amount);
		// 最大允许Approve金额（人民币）(<=4000)
		String max_grand_total = ChannelConfigs.getVal1(ordersInfo.getOrderChannelId(), Name.manual_approved_max_grand_total);
		// 最大允许sku数
		String max_sku_kind_number = ChannelConfigs.getVal1(ordersInfo.getOrderChannelId(), Name.manual_approved_max_sku_kind_number);

		// 手动Approve 检查取消
		// 件数检查
		int productCount = getOrderProductCount(orderDetailsList);
		if (!StringUtils.isEmpty(max_amount)) {
			//  件数超过临界值
			if (productCount > Integer.valueOf(max_amount)) {
				ret = false;
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210023, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
		}

		if (ret && productCount > 1) {
			// 金额检查
			if (!StringUtils.isEmpty(max_grand_total)) {
				String grand_total = ordersInfo.getFinalGrandTotal();

				//	金额超过临界值
				if (Float.valueOf(grand_total) > Float.valueOf(max_grand_total)) {
					ret = false;
					result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210024, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
				}
			}
		}

		if (ret) {
			// 最大允许sku数
			if (!StringUtils.isEmpty(max_sku_kind_number)) {
				int skuCount = getOrderSkuCount(orderDetailsList);
				if (skuCount > Integer.valueOf(max_sku_kind_number)) {
					ret = false;
					result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210070, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
				}
			}
		}

		if (ret) {
			// 订单Approve
			ret = changeApprovedStatus(sourceOrderId, orderNumber, user);
			
			if (!ret) {
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210016, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
		}
		
		return ret;
	}

	/**
	 * 订单中sku数量取得（相同Sku不重复计算）
	 *
	 * @param orderDetailsList 订单明细行
	 *
	 * @return	sku数量
	 */
	private int getOrderSkuCount(List<OutFormOrderDetailOrderDetail> orderDetailsList) {
		ArrayList<String> skuList = new ArrayList<String>();

		for (int i = 0; i < orderDetailsList.size(); i++) {
			OutFormOrderDetailOrderDetail orderDetailInfo = orderDetailsList.get(i);

			if (!orderDetailInfo.isAdjustment()) {
				if (!skuList.contains(orderDetailInfo.getSku())) {
					skuList.add(orderDetailInfo.getSku());
				}
			}
		}

		return skuList.size();
	}

	/**
	 * 改变订单状态 
	 * 
	 * @param sourceOrderId 网络订单号
	 * @param orderNumber 订单号
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean changeApprovedStatus(String sourceOrderId, String orderNumber, UserSessionBean user) {
		boolean ret = true;
		
		TransactionStatus status=transactionManager.getTransaction(def);
		
		try {
			//	订单状态
			// 订单
			OrdersBean ordersInfo = new OrdersBean();		
			// 		订单号
			ordersInfo.setOrderNumber(orderNumber);
			//		Approve状态
			ordersInfo.setApproved(true);
			//		OrderStatus
			ordersInfo.setOrderStatus(OmsCodeConstants.OrderStatus.APPROVED);
			// 		modifier
			ordersInfo.setModifier(user.getUserName());
			
			ret = orderDetailDao.updateOrdersApprovedInfo(ordersInfo);

			if (ret) {
				//	订单明细状态
				OrderDetailsBean orderDetailsInfo = new OrderDetailsBean();
				// 		订单号
				orderDetailsInfo.setOrderNumber(orderNumber);
				//		status
				orderDetailsInfo.setStatus(OmsCodeConstants.OrderStatus.APPROVED);
				// 		modifier
				orderDetailsInfo.setModifier(user.getUserName());

				ret = orderDetailDao.updateOrderDetailsApprovedInfo(orderDetailsInfo);
			}
			
			// 订单Notes保存
			if (ret) {
				
				String noteId = addNotes(sourceOrderId,
						OmsConstants.NotesType.SYSTEM, 
						orderNumber,
						OmsConstants.CHANGE_ORDER_APPROVED_STATUS,
						user.getUserName(),
						user.getUserName());
				if (StringUtils.isEmpty(noteId)) {
					ret = false;
				}
			}
			
			if (ret) {
				transactionManager.commit(status);
			} else {
				transactionManager.rollback(status);
			}
			

		} catch (Exception e) {
			ret = false;
			
			logger.error("changeApprovedStatus", e);
			
			transactionManager.rollback(status);
		}
		
		return ret;
	} 
	
	/**
	 * 订单物品件数
	 * @param orderDetailsList 订单明细信息
	 *
	 * @return
	 */
	private int getOrderProductCount(List<OutFormOrderDetailOrderDetail> orderDetailsList) {
		int count = 0;
		
		for (int i = 0; i < orderDetailsList.size(); i++) {
			OutFormOrderDetailOrderDetail orderDetailInfo = orderDetailsList.get(i);
			
			//	物品的场合
//			if ("0".equals(orderDetailInfo.getAdjustment())) {
			if (!orderDetailInfo.isAdjustment()) {
				//	未Cancel的场合
				if (!isCanceledProduct(orderDetailInfo)) {
					//	未Return的场合
					if ("0".equals(orderDetailInfo.getQuantityReturned())) {
						count = count + 1;
					}
				}
			}
		}
		
		return count;
	}
	
	/**
	 * 订单Note保存（锁定）
	 *
	 * @param sourceOrderId 网络订单号
	 * @param orderNumber 订单号
	 * @param lockFlag 锁定状态
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrderNotesForChangeLockStatus(String sourceOrderId,
													String orderNumber,
													boolean lockFlag, 
													UserSessionBean user) {
		boolean ret = true;		

		// 明细行番号
		int itemNumber = orderDetailDao.getOrderDetailsNotesMaxItemNumber(orderNumber) + 1;
		
		String lockStatus = "unlock";
		if (lockFlag) {
			lockStatus = "lock";
		}
		
		//	Insert Product sku notes取得
		String notes = "";
		//	"Changed lock status to %s."
		notes = String.format(OmsConstants.CHANGE_ORDER_LOCK_STATUS, lockStatus);
		
		// 订单Notes
		NotesBean notesInfo = new NotesBean();
		//		source_order_id
		notesInfo.setSourceOrderId(sourceOrderId);
		//		type
		notesInfo.setType(OmsConstants.NotesType.SYSTEM);
		// 		order_number
		notesInfo.setNumericKey(orderNumber);
		//		item_number
		notesInfo.setItemNumber(String.valueOf(itemNumber));
		//		notes
		notesInfo.setNotes(notes);
		//		entered_by
		notesInfo.setEnteredBy(user.getUserName());
		//		creater
		notesInfo.setCreater(user.getUserName());
		//		modifier
		notesInfo.setModifier(user.getUserName());
		
		ret = notesDao.insertNotesInfo(notesInfo);
		
		return ret;
	}
	
	/**
	 * 订单价格保存
	 * 
	 * @param orderPrice 订单价格
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrderPrice(OrderPrice orderPrice, UserSessionBean user) {
		
		boolean ret = true;
		
		// 订单
		OrdersBean ordersInfo = setOrderPrice(orderPrice);
		
		// 		modifier
		ordersInfo.setModifier(user.getUserName());
		
		ret = orderDetailDao.updateOrdersInfo(ordersInfo);
		
		return ret;
	}

	/**
	 * 校验LineItem
	 * 
	 * @param itemNumber 订单明细编号
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息 
	 * @param result 返回结果
	 * 
	 * @return
	 */
	public boolean delLineItemPermitChk(String itemNumber,
								OutFormOrderdetailOrders ordersInfo, 
								List<OutFormOrderDetailOrderDetail> orderDetailsList,
								AjaxResponseBean result) {
		boolean ret = true;
		
		// 获取选中LineItem
//		OutFormOrderDetailOrderDetail selectedOrderDetailInfo = getSelectedLineItem(orderDetailsList, itemNumber);
		// 获取订单明细一览（含关联订单明细）
		List<OutFormOrderDetailOrderDetail> selectedOrderDetailList = getSelectedLineItems(orderDetailsList, itemNumber);
		
		for (int i = 0; i < selectedOrderDetailList.size(); i++) {
			OutFormOrderDetailOrderDetail selectedOrderDetailInfo = selectedOrderDetailList.get(i);
			
			// 选中行product
			String selectedProduct = selectedOrderDetailInfo.getProduct();
			// 选中行SKU
			String selectedSKU = selectedOrderDetailInfo.getSku();
			// 选中行adjustment
//			String adjustment = selectedOrderDetailInfo.getAdjustment();
			boolean adjustment = selectedOrderDetailInfo.isAdjustment();
			
			// 删除Title 长度
			int canceledTitleLength = OmsConstants.PRODUCT_CANCELLED_TITLE.length();
			
			// Cancel LineItem 允许检查
			ret = chkLineItemOptPermit(Operation.CancelOrderDetail, ordersInfo, selectedOrderDetailInfo, result);
			
			if (ret) {
			
				// 本次原始运费不处理（20150506）START
//				// 原始运费删除校验
//				if (OmsConstants.OrderDetailProductDsp.SHIPPING_CHARGE_TITLE.equals(selectedProduct) ||
//						OmsConstants.OrderDetailProductDsp.SHIPPING_CHARGE_ADJUSTMENT_TITLE.equals(selectedProduct)) {
//					
//		//			result.setResult(false, "200006", 2);
//					result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210002, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
//					
//					ret = false;
//				// 已被删除检查
//				} else if (selectedProduct.length() > canceledTitleLength && OmsConstants.PRODUCT_CANCELLED_TITLE.equals(selectedProduct.substring(0, canceledTitleLength))){
				
				// 已被删除检查
				if (selectedProduct.length() > canceledTitleLength && OmsConstants.PRODUCT_CANCELLED_TITLE.equals(selectedProduct.substring(0, canceledTitleLength))){
				// 本次原始运费不处理（20150506）END
					
		//			result.setResult(false, "200020", 2);
					result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210015, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					
					ret = false;
				// product 校验
				} else if (OmsConstants.OrderDetailSkuDsp.PRODUCT_TITLE.equals(selectedSKU)) {
					
					result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210021, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					ret = false;
					
				// 已Return检查 
//				} else if ("0".equals(adjustment) && "1".equals(selectedOrderDetailInfo.getQuantityReturned())) {
				} else if (!adjustment && "1".equals(selectedOrderDetailInfo.getQuantityReturned())) {
					
					result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210020, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					ret = false;
				}
				
				// 检查异常的场合
				if (!ret) {
					break;
				}
			} else {
				break;
			}
		}
		
		return ret;
	}
	
	/**
	 * LineItem 操作允许检查
	 *
	 * @param operation 操作类型
	 * @param ordersInfo 订单信息
	 * @param orderDetailInfo 订单明细信息
	 * @param result 返回结果
	 * 
	 * @return
	 */
	private boolean chkLineItemOptPermit(Operation operation, OutFormOrderdetailOrders ordersInfo, OutFormOrderDetailOrderDetail orderDetailInfo, AjaxResponseBean result) {

		logger.info("chkLineItemOptPermit start");
		logger.info("	order_number = " + ordersInfo.getOrderNumber());
		logger.info("	shipChannel = " + orderDetailInfo.getShipChannel());
		logger.info("	resStatus = " + orderDetailInfo.getResStatus());

		// 结果返回
		boolean ret = true;
		
		// 渠道许可结果
		boolean chkChannelPermit = true;
		//	状态许可结果
		boolean chkStatusPermit = true;
		//	第三方状态检查
		boolean chkPartnerPermit = true;
		
		// 操作许可判定
		//		真实物品的场合
//		if (OmsConstants.ADJUSTMENT_NO.equals(orderDetailInfo.getAdjustment())) {
		if (!orderDetailInfo.isAdjustment()) {
			
			//	操作许可	允许配置（order_channel）
			String order_detail_permit = "";
			//	第三方状态检查
			String order_chk_parten_status = "";
			//	可处理状态  配置（ship_channel）
			List<OrderChannelConfigBean> permitStatusList = null;
			//	可处理状态  配置（port）
			List<OrderChannelConfigBean> permitPortStatusList = null;
			
			//	配置信息取得
			switch (operation) {
				case CancelThirdPartnerOrder:
					order_detail_permit = ChannelConfigs.getVal1(ordersInfo.getOrderChannelId(), Name.cancel_order_permit);
					permitStatusList = ChannelConfigs.getConfigs(ordersInfo.getOrderChannelId(), Name.cancel_order_detail_permit_status);
					break;
				case CancelOrder:
					order_detail_permit = ChannelConfigs.getVal1(ordersInfo.getOrderChannelId(), Name.cancel_order_permit);
					order_chk_parten_status = ChannelConfigs.getVal1(ordersInfo.getOrderChannelId(), Name.cancel_order_chk_partner_status);
					permitStatusList = ChannelConfigs.getConfigs(ordersInfo.getOrderChannelId(), Name.cancel_order_detail_permit_status);
					break;
				case CancelOrderDetail:
//					order_detail_permit = ChannelConfigs.getVal1(ordersInfo.getOrderChannelId(), Name.cancel_order_detail_permit);
					// 按渠道取明细删除许可
					order_detail_permit = getConfigureInfoByShipChannel(ordersInfo, orderDetailInfo, Name.cancel_order_detail_permit);
					permitStatusList = ChannelConfigs.getConfigs(ordersInfo.getOrderChannelId(), Name.cancel_order_detail_permit_status);
					break;
				case ReturnOrderDetail:
					order_detail_permit = ChannelConfigs.getVal1(ordersInfo.getOrderChannelId(), Name.return_order_detail_permit);
					permitStatusList = ChannelConfigs.getConfigs(ordersInfo.getOrderChannelId(), Name.return_order_detail_permit_status);
					permitPortStatusList = ChannelConfigs.getConfigs(ordersInfo.getOrderChannelId(), Name.return_order_detail_permit_status_port);
					break;
				case AddOrderDetail:
					order_detail_permit = ChannelConfigs.getVal1(ordersInfo.getOrderChannelId(), Name.add_order_detail_permit);
					permitStatusList = ChannelConfigs.getConfigs(ordersInfo.getOrderChannelId(), Name.add_order_detail_permit_status);
					break;
				case ChangeShipAddress:
					order_detail_permit = ChannelConfigs.getVal1(ordersInfo.getOrderChannelId(), Name.change_ship_address_permit);
					permitStatusList = ChannelConfigs.getConfigs(ordersInfo.getOrderChannelId(), Name.change_ship_address_permit_status);
					permitPortStatusList = ChannelConfigs.getConfigs(ordersInfo.getOrderChannelId(), Name.change_ship_address_permit_status_port);
					break;
			}
			
			// 操作许可配置的场合
			if (!StringUtils.isEmpty(order_detail_permit)) {
				
				// 操作不许可的场合
				if (OmsConstants.PERMIT_NG.equals(order_detail_permit)) {
					ret = false;					
					chkChannelPermit = false;
					
				// 操作许可的场合
				} else {
//					// 速邮宝取消对应，该检查删除
//					// CancelOrder时
//					// 第三方状态检查
//					if (!StringUtils.isEmpty(order_chk_parten_status) && OmsConstants.PERMIT_OK.equals(order_chk_parten_status)) {
//						// 已推送 && 未取消
//						if (ordersInfo.isClientOrderSendFlag() && !ordersInfo.isThirdPartyCancelOrderFlag()) {
//							ret = false;
//							chkPartnerPermit = false;
//						}
//					}

					if (chkPartnerPermit) {
						//	按渠道
						//	状态判定用配置Bean
						//		val1：shipChannel
						//		val2：许可状态
						OrderChannelConfigBean chkOrderChannelConfigInfo = null;
						//	可处理状态  判定
						if (permitStatusList != null) {
							for (int i = 0; i < permitStatusList.size(); i++) {
								OrderChannelConfigBean permitStatusInfo = permitStatusList.get(i);
								// 根据ShipChannel（TM，GZ）取得，状态判定用配置Bean取得
								if (permitStatusInfo.getCfg_val1().equals(orderDetailInfo.getShipChannel())) {
									chkOrderChannelConfigInfo = permitStatusInfo;
									break;
								}
							}

							// 可处理状态判定
							//		shipChannel 确定的场合（Res已导入）
							if (chkOrderChannelConfigInfo != null){
								// reservation Status 判定
								if (!chkOrderChannelConfigInfo.getCfg_val2().contains(orderDetailInfo.getResStatus())) {
									ret = false;
									chkStatusPermit = false;
								}
							//      shipChannel 未确定的场合（Res未导入）
							} else {
								switch (operation) {
									case ReturnOrderDetail:
										ret = false;
										chkStatusPermit = false;
										break;
								}
							}
						}

						//	按港口
						//	状态判定用配置Bean
						//		val1：port
						//		val2：许可状态
						OrderChannelConfigBean chkPortConfigInfo = null;
						//	可处理状态  判定
						if (permitPortStatusList != null) {
							for (int i = 0; i < permitPortStatusList.size(); i++) {
								OrderChannelConfigBean permitStatusInfo = permitPortStatusList.get(i);
								// 根据Port（LA）取得，状态判定用配置Bean取得
								if (permitStatusInfo.getCfg_val1().equals(orderDetailInfo.getPort())) {
									chkPortConfigInfo = permitStatusInfo;
									break;
								}
							}

							// 可处理状态判定
							//		shipChannel 确定的场合（Res已导入）
							if (chkPortConfigInfo != null){
								// reservation Status 判定
								if (!chkPortConfigInfo.getCfg_val2().contains(orderDetailInfo.getResStatus())) {
									ret = false;
									chkStatusPermit = false;
								}
							//      shipChannel 未确定的场合（Res未导入）
							} else {
								switch (operation) {
									case ReturnOrderDetail:
										ret = false;
										chkStatusPermit = false;
										break;
								}
							}
						}
					}
				}
			}
			
			// 结果返回设定
			switch (operation) {
				case CancelOrderDetail:
					//	渠道许可结果
					if (!chkChannelPermit) {
						result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210029, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					}
					
					//	状态许可结果
					if (!chkStatusPermit) {
						result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210031, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					}
					break;
				case CancelThirdPartnerOrder:
				case CancelOrder:
					//	渠道许可结果
					if (!chkChannelPermit) {
						result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210032, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					}
					
					//	状态许可结果
					if (!chkStatusPermit) {
						result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210033, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					}

					//	第三方状态检查
					if (!chkPartnerPermit) {
						result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210058, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					}
					break;
				case ChangeShipAddress:
					//	渠道许可结果
					if (!chkChannelPermit) {
						result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210034, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					}
					
					//	状态许可结果
					if (!chkStatusPermit) {
						result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210035, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					}	
					break;
				case ReturnOrderDetail:
					//	渠道许可结果
					if (!chkChannelPermit) {
						result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210030, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					}
					
					//	状态许可结果
					if (!chkStatusPermit) {
						result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210036, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					}	
					break;
				case AddOrderDetail:
					//	渠道许可结果
					if (!chkChannelPermit) {
						result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210028, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					}
					
					//	状态许可结果
					if (!chkStatusPermit) {
						result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210037, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					}	
					break;
			}
		}

		logger.info("ret = " + ret);
		logger.info("chkLineItemOptPermit end");

		return ret;
	}

	/**
	 * 按渠道配置信息取得
	 *
	 * @param ordersInfo 订单信息
	 * @param configureInfo 配置项
	 *
	 * @return
	 */
	private String getConfigureInfoByShipChannel(OutFormOrderdetailOrders ordersInfo, OutFormOrderDetailOrderDetail orderDetailInfo, ChannelConfigEnums.Name configureInfo) {
		String ret = "";

		//	可处理状态  配置（ship_channel）
		List<OrderChannelConfigBean> permitStatusList = ChannelConfigs.getConfigs(ordersInfo.getOrderChannelId(), configureInfo);

		if (permitStatusList != null) {
			for (int i = 0; i < permitStatusList.size(); i++) {
				OrderChannelConfigBean permitStatusInfo = permitStatusList.get(i);
				// 根据ShipChannel（TM，GZ）取得，状态判定用配置Bean取得
				if (permitStatusInfo.getCfg_val1().equals(orderDetailInfo.getShipChannel())) {
					ret = permitStatusInfo.getCfg_val2();
					break;
				}
			}
		}

		return ret;
	}

	/**
	 * 删除LineItem（该机能已废止）
	 * 
	 * @param outFormOrderDetailOrderDetail 画面选中明细行
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * @param user 当前用户
	 * 
	 * @return
	 */
	public boolean delLineItem(OutFormOrderDetailOrderDetail outFormOrderDetailOrderDetail, 
								OutFormOrderdetailOrders ordersInfo, 
								List<OutFormOrderDetailOrderDetail> orderDetailsList, 
								UserSessionBean user) {
		boolean ret = true;
		
		TransactionStatus status=transactionManager.getTransaction(def);
		
		try {

			
			// 订单价格计算
			OrderPrice orderPrice = getOrderPriceMain(outFormOrderDetailOrderDetail, ordersInfo, orderDetailsList);
			
			// 主订单价格取得
			OrderPrice mainOrderPrice = getMainOrderPrice(ordersInfo.getSourceOrderId(), orderPrice);
			
			// 订单明细保存
			ret = saveOrderDetailDelOrderDetail(outFormOrderDetailOrderDetail, ordersInfo, orderDetailsList, orderPrice, user);
			
			// 订单金额变更
			if (ret) {
				ret = saveOrderForDelOrderDetail(orderPrice, user);
			}
			
			// 订单Notes保存
			if (ret) {
//				ret = saveOrderNotesForDelOrderDetail(outFormOrderDetailOrderDetail, orderDetailsList ,user);
			}
			
			//	synShip 同步
			if (ret) {
				ret = syncSynshipFinalGrandTotal(orderPrice, user);
			}
			
			transactionManager.commit(status);
			
		} catch (Exception e) {
			ret = false;
			
			logger.error("delLineItem", e);
			
			transactionManager.rollback(status);
		}
		
		return ret;
	}	
	
	/**
	 * 取得订单金额 主处理
	 * 
	 * @param outFormOrderDetailOrderDetail 选中订单明细
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * 
	 * @return 订单价格
	 */
	private OrderPrice getOrderPriceMain(OutFormOrderDetailOrderDetail outFormOrderDetailOrderDetail,
											OutFormOrderdetailOrders ordersInfo,
											List<OutFormOrderDetailOrderDetail> orderDetailsList) {
		
		// 获取选中LineItem
		OutFormOrderDetailOrderDetail selectedOrderDetailInfo = getSelectedLineItem(orderDetailsList, outFormOrderDetailOrderDetail.getItemNumber());
		
		// 订单价格
		OrderPrice orderPrice = getOrderPrice(ordersInfo);
		
		// 设定调整价格
		setAdjustPriceDelItem(ordersInfo, selectedOrderDetailInfo, orderDetailsList, orderPrice);
		
		// 价格再计算
		priceReCalculate(orderPrice);
		
//		// 明细价格再计算
//		setItemAdjustPrice(orderDetailsList, orderPrice);
		
		return orderPrice;
	}
	
	/**
	 * 订单明细保存（订单明细 删除）
	 * 
	 * @param outFormOrderDetailOrderDetail 当前选中明细行（画面传入）
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * @param orderPrice 订单价格
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrderDetailDelOrderDetail(OutFormOrderDetailOrderDetail outFormOrderDetailOrderDetail, 
													OutFormOrderdetailOrders ordersInfo, 
													List<OutFormOrderDetailOrderDetail> orderDetailsList,
													OrderPrice orderPrice,
													UserSessionBean user) {
		boolean ret = true;
		
		// 获取选中LineItem
		OutFormOrderDetailOrderDetail selectedOrderDetailInfo = getSelectedLineItem(orderDetailsList, outFormOrderDetailOrderDetail.getItemNumber());
		
		// 订单明细Bean
		OrderDetailsBean orderDetailInfo = new OrderDetailsBean();
		//		订单号
		orderDetailInfo.setOrderNumber(outFormOrderDetailOrderDetail.getOrderNumber());
		//		订单明细番号
		orderDetailInfo.setItemNumber(outFormOrderDetailOrderDetail.getItemNumber());
		
		// surcharge 的场合
		if (OmsConstants.OrderDetailSkuDsp.SURCHARGE_TITLE.equals(selectedOrderDetailInfo.getSku())) {
			
			ret = orderDetailDao.deleteOrderDetailsInfo(orderDetailInfo);
			
		// shipping 的场合
		} else if (OmsConstants.OrderDetailSkuDsp.SHIPPING_TITLE.equals(selectedOrderDetailInfo.getSku())) {
			
			//	调整shipping的场合 
			if (OmsConstants.OrderDetailProductDsp.SHIPPING_ADJUSTMENT_TITLE.equals(selectedOrderDetailInfo.getProduct())) {
				ret = orderDetailDao.deleteOrderDetailsInfo(orderDetailInfo);
			}
			
		// discount 的场合 
		} else if (OmsConstants.OrderDetailSkuDsp.DISCOUNT_TITLE.equals(selectedOrderDetailInfo.getSku())) {
			//	不打折 或  百分比打折的场合
			if (OmsConstants.DiscountType.NODISCOUNT.equals(ordersInfo.getDiscountType()) ||
					OmsConstants.DiscountType.PERCENT.equals(ordersInfo.getDiscountType())) {
			
			} else {
			//  手工打折的场合
				ret = orderDetailDao.deleteOrderDetailsInfo(orderDetailInfo);
			}
			
		// sku 的场合
		} else {
			//	订单明细更新
			//		订单明细 product
			orderDetailInfo.setProduct(OmsConstants.PRODUCT_CANCELLED_TITLE + selectedOrderDetailInfo.getProduct());
			//		quantity_shipped
			orderDetailInfo.setQuantityShipped("0");
			//		订单明细状态
			orderDetailInfo.setStatus(OmsCodeConstants.OrderStatus.CANCELED);
			//  	更新者
			orderDetailInfo.setModifier(user.getUserName());			
			ret = orderDetailDao.updateOrderDetailsInfo(orderDetailInfo);
			
			//	当前最大明细番号
			int maxItemNumber = getOrderDetailsMaxItemNumber(orderDetailsList);
			
			//  插入项目共通设定
			OrderDetailsBean orderDetailsInfoForInsert = new OrderDetailsBean();
			//	订单号
			orderDetailsInfoForInsert.setOrderNumber(outFormOrderDetailOrderDetail.getOrderNumber());
			//	adjustment
			orderDetailsInfoForInsert.setAdjustment(true);
			//	quantity_ordered
			orderDetailsInfoForInsert.setQuantityOrdered("1");
			//	quantity_shipped
			orderDetailsInfoForInsert.setQuantityShipped("1");
			//	quantity_returned
			orderDetailsInfoForInsert.setQuantityReturned("0");
			//	status
			orderDetailsInfoForInsert.setStatus("");
			//	syncSynship
			orderDetailsInfoForInsert.setSyncSynship(false);
			//	resId
			orderDetailsInfoForInsert.setResId("0");
			//	modifier
			orderDetailsInfoForInsert.setModifier(user.getUserName());
			
			//	shipping更新
			//		订单物品价格为0的场合
			if (ret && orderPrice.getFinalProductTotal() == 0){
				//	原始订单价格
				float originalShipping = getOriginalShipping(orderDetailsList);
				
				if (originalShipping > 0) {					

					//	明细番号
					maxItemNumber = maxItemNumber + 1;
					orderDetailsInfoForInsert.setItemNumber(String.valueOf(maxItemNumber));

					//	product
					orderDetailsInfoForInsert.setProduct(OmsConstants.OrderDetailProductDsp.SHIPPING_CHARGE_ADJUSTMENT_TITLE);
					//	price_per_unit
					orderDetailsInfoForInsert.setPricePerUnit(String.valueOf(mult2Digits(originalShipping, -1f)));

					//	sku
					orderDetailsInfoForInsert.setSku(OmsConstants.OrderDetailSkuDsp.SHIPPING_TITLE);

					ret = orderDetailDao.insertOrderDetailsInfo(orderDetailsInfoForInsert);
				}
			}
			
			// discount更新
			// 		百分比打折的场合
			if (ret && OmsConstants.DiscountType.PERCENT.equals(ordersInfo.getDiscountType())) {
				//	打折金额设定
				float discount = mult2Digits(Float.valueOf(selectedOrderDetailInfo.getPricePerUnit()), Float.valueOf(ordersInfo.getDiscountPercent()));
				
				//	明细番号
				maxItemNumber = maxItemNumber + 1;
				orderDetailsInfoForInsert.setItemNumber(String.valueOf(maxItemNumber));

				//	product
				orderDetailsInfoForInsert.setProduct(OmsConstants.OrderDetailProductDsp.DISCOUNT_DJUSTMENT_TITLE_DELlINEITEM);
				//	price_per_unit
				orderDetailsInfoForInsert.setPricePerUnit(String.valueOf(discount));

				//	sku
				orderDetailsInfoForInsert.setSku(OmsConstants.OrderDetailSkuDsp.DISCOUNT_TITLE);

				ret = orderDetailDao.insertOrderDetailsInfo(orderDetailsInfoForInsert);
			}
		}
		
		return ret;
	}
		
	/**
	 * 订单保存（订单明细 删除）
	 * 
	 * @param orderPrice 订单价格
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrderForDelOrderDetail(OrderPrice orderPrice, UserSessionBean user) {
		
		boolean ret = true;
		
		// 订单
		OrdersBean ordersInfo = setOrderPrice(orderPrice);
		
		// 		modifier
		ordersInfo.setModifier(user.getUserName());
		
		ret = orderDetailDao.updateOrdersInfo(ordersInfo);
		
		return ret;
	}

	/**
	 * 订单Note保存
	 *
	 * @param ordersInfo 当前订单信息
	 * @param outFormOrderDetailOrderDetail 选中行
	 * @param orderDetailsList 订单明细信息
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrderNotesForDelOrderDetail(String sourceOrderId,
													OutFormOrderdetailOrders ordersInfo,
													OutFormOrderDetailOrderDetail outFormOrderDetailOrderDetail, 
													List<OutFormOrderDetailOrderDetail> orderDetailsList, 
													UserSessionBean user) {
		boolean ret = true;
		
		// 获取选中LineItem
		OutFormOrderDetailOrderDetail selectedOrderDetailInfo = getSelectedLineItem(orderDetailsList, outFormOrderDetailOrderDetail.getItemNumber());
		
		// 订单号
		String orderNumber = outFormOrderDetailOrderDetail.getOrderNumber();
		// 明细行番号
		int itemNumber = orderDetailDao.getOrderDetailsNotesMaxItemNumber(orderNumber) + 1;
		
		//	Insert Product sku notes取得
		String notes = "";
		if (isTrueSKU(selectedOrderDetailInfo.getSku())) {
			//	"item #%s SKU=%s Status changed to: Canceled. Reason: %s";
			notes = String.format(OmsConstants.DELETE_LINEITEM_NOTES_FORMAT_FORPRODUCT, 
					outFormOrderDetailOrderDetail.getItemNumber(),
					selectedOrderDetailInfo.getSku(),
					outFormOrderDetailOrderDetail.getReason());
		} else {
			//	"item #%s SKU=%s Delete. Reason: %s";
			notes = String.format(OmsConstants.DELETE_LINEITEM_NOTES_FORMAT_FOROTHER, 
					outFormOrderDetailOrderDetail.getItemNumber(),
					selectedOrderDetailInfo.getSku(),
					outFormOrderDetailOrderDetail.getReason());
		}

		
		// 订单Notes
		NotesBean notesInfo = new NotesBean();
		// 		sourceOrderId
		notesInfo.setSourceOrderId(sourceOrderId);
		//		type
		notesInfo.setType(OmsConstants.NotesType.SYSTEM);
		// 		order_number
		notesInfo.setNumericKey(orderNumber);
		//		item_number
		notesInfo.setItemNumber(String.valueOf(itemNumber));
		//		notes
		notesInfo.setNotes(notes);
		//		entered_by
		notesInfo.setEnteredBy(user.getUserName());
		//		creater
		notesInfo.setCreater(user.getUserName());
		//		modifier
		notesInfo.setModifier(user.getUserName());
		
		ret = notesDao.insertNotesInfo(notesInfo);
		
		// transactons 信息更新
		if (ret) {
			// transactions 信息更新
			String description = selectedOrderDetailInfo.getSku();
			if (!selectedOrderDetailInfo.isAdjustment()) {
				description = OrderDetailSkuDsp.PRODUCT_TITLE;
			}
			
			// 订单明细, debt, credit, noteId, user
			ret = saveOrderTransactionsInfo(sourceOrderId,
											ordersInfo.getOriginSourceOrderId(),
											selectedOrderDetailInfo.getOrderNumber(),
											getTransactionSku(selectedOrderDetailInfo, orderDetailsList),
											selectedOrderDetailInfo.getItemNumber(),
											description,
											outFormOrderDetailOrderDetail.getPricePerUnit(),
											notesInfo.getId(),
											OmsConstants.Transaction_Type.REFUND,
											true,
											user);
		}
		
		return ret;
	}
	
	/**
	 * 获取选中LineItem
	 * 
	 * @param orderDetailsList 订单明细信息
	 * @param selectedItemNumber 选中
	 * 
	 * @return 选中明细行
	 */
	private OutFormOrderDetailOrderDetail getSelectedLineItem(List<OutFormOrderDetailOrderDetail> orderDetailsList, String selectedItemNumber) {
		
		OutFormOrderDetailOrderDetail selectedOrderDetailInfo = null;
		
		for (int i = 0; i < orderDetailsList.size(); i++) {
			OutFormOrderDetailOrderDetail orderDetailInfo = orderDetailsList.get(i);
			
			if (selectedItemNumber.equals(orderDetailInfo.getItemNumber())) {
				selectedOrderDetailInfo = orderDetailInfo;
				
				break;
			}
		}
		
		return selectedOrderDetailInfo;
	}
	
	/**
	 * 获取当前订单明细最大番号
	 * 
	 * @param orderDetailsList 订单明细信息
	 * 
	 * @return 选中明细行
	 */
	private int getOrderDetailsMaxItemNumber(List<OutFormOrderDetailOrderDetail> orderDetailsList) {
		int ret = 0;
		
		for (int i = 0; i < orderDetailsList.size(); i++){
			OutFormOrderDetailOrderDetail orderDetailsInfo = orderDetailsList.get(i);
			int itemNumber = Integer.valueOf(orderDetailsInfo.getItemNumber()); 
			
			if (itemNumber > ret) {
				ret = itemNumber;
			}
		}
		
		return ret;
	}
	
	/**
	 * 删除LineItem订单价格调整
	 * 
	 * @param ordersInfo 订单信息
	 * @param orderDetailInfo 选中订单明细信息
	 * @param orderDetailsList 订单明细信息
	 * @param orderPrice 订单价格
	 * 
	 * @return
	 */
	private void setAdjustPriceDelItem(OutFormOrderdetailOrders ordersInfo, OutFormOrderDetailOrderDetail orderDetailInfo, List<OutFormOrderDetailOrderDetail> orderDetailsList, OrderPrice  orderPrice) {
		
		// surcharge 的场合
		if (OmsConstants.OrderDetailSkuDsp.SURCHARGE_TITLE.equals(orderDetailInfo.getSku())) {
			// revised_surcharge
			float revisedSurcharge = sub2Digits(orderPrice.getRevisedSurcharge() ,Float.valueOf(orderDetailInfo.getPricePerUnit()));
			
			orderPrice.setRevisedSurcharge(revisedSurcharge);
			
		// shipping 的场合
		} else if (OmsConstants.OrderDetailSkuDsp.SHIPPING_TITLE.equals(orderDetailInfo.getSku())) {
			// final_shipping_total
			float finalShippingTotal = sub2Digits(orderPrice.getFinalShippingTotal() ,Float.valueOf(orderDetailInfo.getPricePerUnit()));
			
			orderPrice.setFinalShippingTotal(finalShippingTotal);
			
		// discount 的场合
		} else if (OmsConstants.OrderDetailSkuDsp.DISCOUNT_TITLE.equals(orderDetailInfo.getSku())) {
			
			// 手工打折的场合（百分比打折，不打折的场合，不处理）
			if (OmsConstants.DiscountType.MANUAL.equals(ordersInfo.getDiscountType())) {
				// revised_discount
				float revisedDiscount = sub2Digits(orderPrice.getRevisedDiscount(), Float.valueOf(orderDetailInfo.getPricePerUnit()));
				
				orderPrice.setRevisedDiscount(revisedDiscount);
			}
			
		// sku 的场合
		} else {			
			// final_product_total
			float finalProductTotal = sub2Digits(orderPrice.getFinalProductTotal(), Float.valueOf(orderDetailInfo.getPricePerUnit()));
			orderPrice.setFinalProductTotal(finalProductTotal);
			
			// 百分比打折的场合
			if (OmsConstants.DiscountType.PERCENT.equals(ordersInfo.getDiscountType())) {
				// revised_discount = 产品价格 * 订单打折率 * -1
				float reservedDiscount = mult2Digits(mult2Digits(finalProductTotal, Float.valueOf(ordersInfo.getDiscountPercent())), -1);
				
				orderPrice.setRevisedDiscount(reservedDiscount);
			}
			
			// original shipping
			float originalShipping = getOriginalShipping(orderDetailsList);
			
			// 订单物品金额 == 0 && 原始运费有的场合
			if (finalProductTotal == 0  && originalShipping > 0) {
				
				// final_shipping_total
				float finalShippingTotal = sub2Digits(orderPrice.getFinalShippingTotal() ,originalShipping);
				
				orderPrice.setFinalShippingTotal(finalShippingTotal);
			}
		}
	}
	
	/**
	 * 是否真实物品
	 * 
	 * @param sku SKU信息
	 * 
	 * @return
	 */
	private boolean isTrueSKU(String sku) {
		
		// surcharge 的场合 || discount 的场合 || shipping 的场合  || product 的场合
		if (OmsConstants.OrderDetailSkuDsp.SURCHARGE_TITLE.equals(sku) ||
				OmsConstants.OrderDetailSkuDsp.DISCOUNT_TITLE.equals(sku) ||
				OmsConstants.OrderDetailSkuDsp.SHIPPING_TITLE.equals(sku) ||
				OmsConstants.OrderDetailSkuDsp.PRODUCT_TITLE.equals(sku)) {
			return false;
		} else {
			return true;
		}
	}

	
	/**
	 * 获取选中LineItem
	 * 
	 * @param orderDetailsList 订单明细信息
	 * 
	 * @return 原始运费
	 */
	private float getOriginalShipping(List<OutFormOrderDetailOrderDetail> orderDetailsList) {
		float ret = 0f;

		// 原始运费
		float shippingCharge = 0f;
		// 调整后原始运费
		float autoShippingCharge = 0f;
		
		for (int i = 0; i < orderDetailsList.size(); i++) {
			OutFormOrderDetailOrderDetail orderDetailInfo = orderDetailsList.get(i);
			
			// 原始运费
			if (OmsConstants.OrderDetailProductDsp.SHIPPING_CHARGE_TITLE.equals(orderDetailInfo.getProduct())) {				
				shippingCharge = Float.valueOf(orderDetailInfo.getPricePerUnit());
			}
			
			// 调整后原始运费
			if (OmsConstants.OrderDetailProductDsp.SHIPPING_CHARGE_ADJUSTMENT_TITLE.equals(orderDetailInfo.getProduct())) {
				autoShippingCharge = Float.valueOf(orderDetailInfo.getPricePerUnit());
			}
		}
		
		ret = sub2Digits(shippingCharge, autoShippingCharge);
		
		return ret;
	}
	
	/**
	 * 保存Notes
	 * 
	 * @param map 输入数据
	 * @param user 用户信息
	 * 
	 * @return boolean
	 */
	@Override
	public boolean saveNotes(Map<String, Object> map, UserSessionBean user) {
		
		try {
			// 图片文件
			String fileBase64 = (String) map.get("fileBase64");
			
			logger.info("fileBase64=" + fileBase64);
			
			
			// 网络订单号
			String sourceOrderId = (String)map.get("sourceOrderId");
			// 订单号
			String orderNumber = (String) map.get("orderNumber");
			// Notes
			String notes = (String) map.get("notes");
			// 设置日期格式
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");	
			// 文件路径
			String filePath = "";
			
			// 文件名
			String imagePath = Properties.readValue(PropKey.NOTE_IMG_PATH);
			if (!imagePath.endsWith(File.separator)) {
				imagePath = imagePath + File.separator;
			} 
			String dirname = null;
			
			// 获得订单详细信息Notes中最大的番号
			int itemNumber = orderDetailDao.getOrderDetailsNotesMaxItemNumber(orderNumber) + 1;

			String rawFilename = String.format("%s_%02d.jpg", orderNumber, itemNumber);
			
			if (fileBase64 != null) {
				// 当前时间为文件路径
				filePath = df.format(new Date());
				dirname = imagePath + filePath + File.separator;
				// 文件上传
				if (0 != uploadImage(dirname, rawFilename, fileBase64)) {
					return false;
				}
			} 
			// 保存Notes
			if (saveOrderNotes(sourceOrderId, orderNumber, itemNumber, notes, filePath, user)) {
				return true;
			}
			
			// 删除已保存的文件
			File file = new File(dirname + rawFilename);
			if (file != null) {
				file.delete();
				file = null;
			}
		}
		catch (Exception e) {
			logger.error("saveNotes ex :", e);
		}
		
		return false;
	}
	
	
	/**
	 * 订单Note保存
	 * 
	 * @param orderNumber 订单号
	 * @param itemNumber 明细番号
	 * @param notes Notes
	 * @param filePath 文件路径
	 * @param user 用户信息
	 * 
	 * @return boolean
	 */
	private boolean saveOrderNotes(String source_order_id, String orderNumber, int itemNumber, String notes, String filePath, UserSessionBean user) {
		boolean ret = true;
		
		// 订单Notes
		NotesBean notesInfo = new NotesBean();
		//		source_order_id
		notesInfo.setSourceOrderId(source_order_id);
		//		type
		notesInfo.setType(OmsConstants.NotesType.SYSTEM);
		// 		order_number
		notesInfo.setNumericKey(orderNumber);
		//		item_number
		notesInfo.setItemNumber(String.valueOf(itemNumber));
		//		notes
		notesInfo.setNotes(notes);
		//		entered_by
		notesInfo.setEnteredBy(user.getUserName());
		//		file path
		notesInfo.setFilePath(filePath);
		//		Creater
		notesInfo.setCreater(user.getUserName());
		//		modifer
		notesInfo.setModifier(user.getUserName());
		ret = notesDao.insertNotesInfo(notesInfo);
		
		return ret;
	}

	/**
	 * 图片的上传
	 * 
	 * @param dirname 		保存目录
	 * @param rawFilename	保存文件名
	 * @param rawFile64		文件内容
	 * 
	 * @return int
	 */
	private int uploadImage(String dirname, String rawFilename, String rawFile64) {
		int uploadResult = 0;
		try {
			  // 目标文件夹
			 File needDir = new File(dirname);
			 // 目标文件夹不存在的场合，新建文件夹
			 if (!needDir.exists()) {
			 	needDir.mkdirs();
			 }

			// 前端未压缩过的上传方法
			String saveFileName = dirname + rawFilename;

			try (OutputStream out = new FileOutputStream(saveFileName)) {
				BASE64Decoder decoder = new BASE64Decoder();
				// Base64解码(开始一段图片信息说明要去除)
				byte[] b = decoder.decodeBuffer(rawFile64.substring(rawFile64
						.indexOf(",") + 1));
				for (int i = 0; i < b.length; ++i) {
					if (b[i] < 0) {// 调整异常数据
						b[i] += 256;
					}
				}
				// 生成jpeg图片
				out.write(b);
				out.flush();
				logger.info("Notes中的图片(前端压缩过)：" + (b.length / 1024) + "KB");
				logger.info("Notes中的图片上传成功");
			} catch (Exception e) {
				uploadResult = 1;
				logger.error("Notes中的图片解码失败", e);
			}
		} catch (Exception e) {
			uploadResult = 2;
			logger.error("Notes中的图片上传失败：", e);
		}

		return uploadResult;
	}
	
	
	/**
	 * Sold to 地址保存主函数
	 * 
	 * @return
	 */
	@Override
	public void updateAddressMain(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {

		// 地址保存
		if (updateAddress(bean, result, user)) {
			
			// 设置返回结果
			setNotesToResult(bean.getSourceOrderId(), user, result);
			
			//	正常
			result.setResult(true);
		}

	}
	
	/**
	 * 订单编辑 许可检查
	 *
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * @param result 返回结果
	 * 
	 * @return
	 */
	private boolean chkChangeOrderShipAddressPermit(OutFormOrderdetailOrders ordersInfo, List<OutFormOrderDetailOrderDetail> orderDetailsList, AjaxResponseBean result) {
		boolean ret = true;
		
		for (int i = 0; i < orderDetailsList.size(); i++) {
			OutFormOrderDetailOrderDetail orderDetailInfo = orderDetailsList.get(i);
			
			// Cancel LineItem 允许检查
			ret = chkLineItemOptPermit(Operation.ChangeShipAddress, ordersInfo, orderDetailInfo, result);
			
			if (!ret) {				
				break;
			}

		}		
		return ret;
	}
	
	/**
	 * 地址保存
	 * 
	 * @return
	 */
	private boolean updateAddress(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {
		boolean ret = true;	
		
		TransactionStatus status=transactionManager.getTransaction(def);
		
		int updateStep = 0;
		
		try {	
			
			bean.setModifier(user.getUserName());			
			// 地址信息更新
			updateStep = 1;
			ret = orderDao.updateSoldToAddress(bean);
			
			if (ret) {
				String customerId = bean.getCustomerId();			
				// 客户信息更新
				if(customerId != null) {
					CustomerBean customerbean=new CustomerBean();
					customerbean.setLastName(bean.getName());
					customerbean.setCompany(bean.getCompany());
					customerbean.setAddress(bean.getAddress());
					customerbean.setAddress2(bean.getAddress2());
					customerbean.setCity(bean.getCity());
					customerbean.setCountry(bean.getCountry());
					customerbean.setEmail(bean.getEmail());
					customerbean.setState(bean.getState());
					customerbean.setZip(bean.getZip());
					customerbean.setPhone(bean.getPhone());
					customerbean.setCustomerId(customerId);
					customerbean.setModifier(user.getUserName());
					
					updateStep = 2;
					ret = customerDao.updateCustomerInfo(customerbean);
				}
			}

			// notes 追加
			if (ret) {
				StringBuffer noteBuf = new StringBuffer();
				
				if(isChanged(bean.getName(), bean.getOrigName())){
					noteBuf.append(getOutputNote(OmsConstants.BEFORE_EDIT_TITLE.NAME, bean.getOrigName())).append(";");
				}

				if(isChanged(bean.getEmail(), bean.getOrigEmail())){
					noteBuf.append(getOutputNote(OmsConstants.BEFORE_EDIT_TITLE.EMAIL, bean.getOrigEmail())).append(";");
				}
				
				if(isChanged(bean.getPhone(), bean.getOrigPhone())){
					noteBuf.append(getOutputNote(OmsConstants.BEFORE_EDIT_TITLE.PHONE, bean.getOrigPhone())).append(";");
				}
				
				if(isChanged(bean.getCompany(), bean.getOrigCompany())){
					noteBuf.append(getOutputNote(OmsConstants.BEFORE_EDIT_TITLE.COMPANY, bean.getOrigCompany())).append(";");
				}
				
				if(isChanged(bean.getAddress(), bean.getOrigAddress())){
					noteBuf.append(getOutputNote(OmsConstants.BEFORE_EDIT_TITLE.ADDRESS, bean.getOrigAddress())).append(";");
				}
				
				if(isChanged(bean.getAddress2(), bean.getOrigAddress2())){					
					noteBuf.append(getOutputNote(OmsConstants.BEFORE_EDIT_TITLE.ADDRESS2, bean.getOrigAddress2())).append(";");
				}
				
				if(isChanged(bean.getCity(), bean.getOrigCity())){
					noteBuf.append(getOutputNote(OmsConstants.BEFORE_EDIT_TITLE.CITY, bean.getOrigCity())).append(";");					
				}
				
				if(isChanged(bean.getState(), bean.getOrigState())){
					noteBuf.append(getOutputNote(OmsConstants.BEFORE_EDIT_TITLE.STATE, bean.getOrigState())).append(";");
				}
				
				if(isChanged(bean.getZip(), bean.getOrigZip())){
					noteBuf.append(getOutputNote(OmsConstants.BEFORE_EDIT_TITLE.ZIP, bean.getOrigZip())).append(";");
				}
				
				if(isChanged(bean.getCountry(), bean.getOrigCountry())){
					noteBuf.append(getOutputNote(OmsConstants.BEFORE_EDIT_TITLE.COUNTRY, bean.getOrigCountry())).append(";");
				}
				
				String note=String.format(OmsConstants.SOLD_TO_ADDRESS_CHANGED, noteBuf);
				
				if (note.length() > 1) {
					note = note.substring(0,note.length() - 1);	
				}
				
				updateStep = 3;
				
				// 订单Notes
				String noteId = addNotes(bean.getSourceOrderId(),
								OmsConstants.NotesType.SYSTEM, 
								bean.getOrderNumber(),
								note,
								user.getUserName(),
								user.getUserName());
				if (StringUtils.isEmpty(noteId)) {
					ret = false;
				}
			}
			
			// 执行结果判定
			if (ret) {
				transactionManager.commit(status);
				
			} else {
				
				transactionManager.rollback(status);
				
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210012,
						 MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
		}catch (DuplicateKeyException ex){			
			logger.error("updateAddressMain", ex);
			
			ret = false;
			
			transactionManager.rollback(status);
			
			// 客户信息更新的场合
			if (updateStep == 2) {
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210025,
						 MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			} else {
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210012,
						 MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
			
		} catch (Exception ex) {
			logger.error("updateAddressMain", ex);
			
			ret = false;
			
			transactionManager.rollback(status);
			
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210012,
					 MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}
		
		return ret;
	}
	
	/**
	 * notes 输出格式化 
	 * 
	 * @return
	 */
	private String getOutputNote(String formatInfo, String output) {
		String ret = "";
		
		ret = String.format(formatInfo, StringUtils.null2Space(output));
		
		return ret; 
	}
	
	/**
	 * 内容变化判定
	 * 
	 *  @param content 变更后内容
	 *  @param orgContent 变更前内容
	 * 
	 * @return
	 */
	private boolean isChanged(String content, String orgContent) {
		boolean ret = true;
		
		String compContent = StringUtils.null2Space(content);
		String compOrgContent = StringUtils.null2Space(orgContent);
		
		if (compContent.equals(compOrgContent)) {
			ret = false;
		}
		
		return ret;
	}
	/**
	 * synship 同步 字段设定 
	 * 
	 * @return
	 */
	private void setSynShipFields(OutFormOrderdetailOrders bean) {
		
		// ship_district
		//		当ShipAddress2不为空时，设置ShipAddress
		if (!StringUtils.isEmpty(bean.getShipAddress2())) {
			bean.setSynShipShipDistrict(bean.getShipAddress());
		} else  {
			bean.setSynShipShipDistrict("");
		}
		
		// ship_address
		//		设置ShipAddress+ShipAddress2
		String shipAddress = bean.getShipAddress() + bean.getShipAddress2();
		bean.setSynShipShipAddress(shipAddress);
		
	}
	
	/**
	 * 注释保存主函数
	 * 
	 * @return
	 */
	@Override
	public void updateInternalMessage(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {
		
		if (updateInternalMessageSub(bean, result, user)) {
			
			// Notes 结果返回
			setNotesToResult(bean.getSourceOrderId(), user, result);
			
			//	正常
			result.setResult(true);
		}
	}
	
	/**
	 * 注释保存子函数
	 * 
	 * @return
	 */
	private boolean updateInternalMessageSub(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {
		boolean ret = true;
		
		TransactionStatus status=transactionManager.getTransaction(def);
		
		try {
			
			// 更新用户设定
			bean.setModifier(user.getUserName());

			// Comment信息更新
			ret = orderDao.updateAddress(bean);
		
			if (ret) {
				String note=String.format(OmsConstants.INTERNAL_MESSAGE_CHANGED, bean.getOrigInternalMessage());
				
				// 订单Notes
				String noteId = addNotes(bean.getSourceOrderId(),
								OmsConstants.NotesType.SYSTEM, 
								bean.getOrderNumber(),
								note,
								user.getUserName(),
								user.getUserName());
				if (StringUtils.isEmpty(noteId)) {
					ret = false;
				}
			}
			
			if (ret) {
				// synship 同步
				ret = synShipSyncDao.updateInternalMessage(bean);
			}
			
			// 执行结果判定
			if (ret) {
				transactionManager.commit(status);
				
				// 设置返回结果
				setNotesToResult(bean.getSourceOrderId(), user, result);
				
				//	正常 
				result.setResult(true);
			} else {
				
				transactionManager.rollback(status);
				
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210013,
						 MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
		} catch (Exception ex) {
			
			ret = false;
			
			transactionManager.rollback(status);
			
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210013,
					 MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}
		
		return ret;
	}

	/**
	 * GiftMessage保存主函数
	 * 
	 * @return
	 */
	@Override
	public void updateGiftMessage(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {
		
		if (updateGiftMessageSub(bean, result, user)) {
			// Notes 结果返回
			setNotesToResult(bean.getSourceOrderId(), user, result);
			
			//	正常
			result.setResult(true);
		}
	}
	
	/**
	 * GiftMessage保存子函数
	 * 
	 * @return
	 */
	private boolean updateGiftMessageSub(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {
		
		boolean ret = true;
		
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			
			// 更新用户设定
			bean.setModifier(user.getUserName());
			
			// GiftMessage信息更新
			ret = orderDao.updateGiftMessage(bean);
		
			if (ret) {
				String note = String.format(OmsConstants.GIFT_MESSAGE_CHANGED, bean.getOrigGiftMessage());
				
				// 订单Notes
				String noteId = addNotes(bean.getSourceOrderId(),
								OmsConstants.NotesType.SYSTEM, 
								bean.getOrderNumber(),
								note,
								user.getUserName(),
								user.getUserName());
				if (StringUtils.isEmpty(noteId)) {
					ret = false;
				}
			}
			
			//SynShip暂时不同步
			/*if (ret) {
				// synship 同步
				ret = synShipSyncDao.updateCustComments(bean);
			}*/
			
			// 执行结果判定
			if (ret) {
				transactionManager.commit(status);
				
				// 设置返回结果
				setNotesToResult(bean.getSourceOrderId(), user, result);
				
				//	正常 
				result.setResult(true);
			} else {
				
				transactionManager.rollback(status);
				
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210013,
						 MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
		} catch (Exception ex) {
			ret = false;
			
			transactionManager.rollback(status);
			
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210013,
					 MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}
		
		return ret;
	}
	
	/**
	 * Shipping保存主函数
	 * 
	 * @return
	 */
	@Override
	public void updateShippingMain(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {
		
		if (updateShippingSub(bean, result, user)) {
			
			// Notes 结果返回
			setNotesToResult(bean.getSourceOrderId(), user, result);
			
			//	正常
			result.setResult(true);
		}
	}
	
	/**
	 * Shipping保存函数
	 * 
	 * @return
	 */
	private boolean updateShippingSub(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {
		
		boolean ret = true;
		
		TransactionStatus status=transactionManager.getTransaction(def);
		
		try {
			// 更新用户设定
			bean.setModifier(user.getUserName());			
			
			// shipping信息更新
			ret = orderDao.updateAddress(bean);
			
			if (ret) {
				
				String shipping = "";
				if (!StringUtils.isEmpty(bean.getOrigShipping())) {
					shipping = Type.getTypeName(OmsCodeConstants.SHIPPINGMETHOD_TYPE, bean.getOrigShipping(), "en");
				}
				
				String note = String.format(OmsConstants.SHIPPING_CHANGED, shipping);
				
				// 订单Notes
				String noteId = addNotes(bean.getSourceOrderId(),
											OmsConstants.NotesType.SYSTEM, 
											bean.getOrderNumber(),
											note,
											user.getUserName(),
											user.getUserName());
				if (StringUtils.isEmpty(noteId)) {
					ret = false;
				}
			}
			
			if (ret) {
				bean.setShippingName(Type.getTypeName(OmsCodeConstants.SHIPPINGMETHOD_TYPE, bean.getShipping(), "en"));
				// synship 同步
				ret = synShipSyncDao.updateShipping(bean);
			}
			
			// 执行结果判定
			if (ret) {				
				logger.info("updateShippingSub success");
				
				transactionManager.commit(status);				
				
				//	正常
				result.setResult(true);
			} else {
				logger.info("updateShippingSub failure");
				
				transactionManager.rollback(status);
				
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210014,
						 MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
		} catch (Exception ex) {
			
			logger.error("updateShippingSub", ex);
			
			ret = false;
			
			transactionManager.rollback(status);
			
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210014,
					 MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}
		
		return ret;
		
	}
	
	
	/**
	 * 更改地址
	 */
	@Override
	public int updateAddress(OutFormOrderdetailOrders bean) {
//      int count=orderDao.updateAddress(bean);
//		return count;
		
		return 1;
	}
    /**
     * 查询客户信息
     */
	@Override
	public OutFormAddNewOrderCustomer getCustomerInfoforSold(String customerId) {
		OutFormAddNewOrderCustomer  cuntomer=customerDao.getcustomerInfoforSold(customerId);
		return cuntomer;
	}

	@Override
	public boolean updateCustomerInfo(CustomerBean bean) {
		boolean  ret=true;
		ret=customerDao.updateCustomerInfo(bean);
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
	 * 乘法 不保留小数
	 * 
	 * @param addPara1
	 * @param addPara2
	 * @return ret
	 */
	public static int mult0Digits(float addPara1, float addPara2) {
		int ret = 0;
		
		BigDecimal bd1 = new BigDecimal(addPara1);
		BigDecimal bd2 = new BigDecimal(addPara2);
		
		ret = bd1.multiply(bd2).setScale(0, BigDecimal.ROUND_HALF_UP).intValue(); 
		
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
		
		ret = bd1.divide(bd2).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
		
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
	 * 更改Comment
	 * @param bean
	 * @return
	 */
	@Override
	public int updateComment(OutFormOrderdetailOrders bean){
//		 int count=orderDao.updateAddress(bean);
//			return count;
		
		return 1;
	}

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
		
		//		网络订单号
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
	 * Notes 前端返回
	 * @param sourceOrderId 网络订单号
	 * @param user 当前用户
	 * @param result 前端返回结果
	 * 
	 * @return
	 */
	private void setNotesToResult(String sourceOrderId, UserSessionBean user, AjaxResponseBean result) {
		// 设置返回结果
		Map<String, Object> ordersListMap = new HashMap<String, Object>();				
		// 		订单Notes信息
		List<OutFormOrderdetailNotes> orderNotesList = getOrderNotesInfoBySourceOrderId(sourceOrderId, user);
		// 		订单NotesList
		ordersListMap.put("orderNotesList", orderNotesList);
		// 结果返回
		result.setResultInfo(ordersListMap);
	}
	
	/**
	 * DB日期，时间去除
	 * @param dbDateTime 
	 * 
	 * @return
	 */
	private String trimDBDateTime(String dbDateTime) {
		String ret = dbDateTime;
		
		String[] arrDateTimes = dbDateTime.split(" ");
		if (arrDateTimes.length == 2) {
			// 日期返回
			ret = arrDateTimes[0];
		}
		
		return ret;
	}
	/**
	 * CustomerComment保存主函数
	 * 
	 * @return
	 */
	@Override
	public void updateCustomerComment(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {
		
		if (updateCustomerCommentSub(bean, result, user)) {
			// Notes 结果返回
			setNotesToResult(bean.getSourceOrderId(), user, result);
			
			//	正常
			result.setResult(true);
		}
	}
	
	/**
	 * CustomerComment保存子函数
	 * 
	 * @return
	 */
	private boolean updateCustomerCommentSub(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {
		boolean ret = true;
		
		TransactionStatus status=transactionManager.getTransaction(def);
		
		try {
			
			// 更新用户设定
			bean.setModifier(user.getUserName());
			
			// Comment信息更新
			ret = orderDao.updateAddress(bean);
		
			if (ret) {
				String note = String.format(OmsConstants.CUSTOMER_COMMENT_CHANGED, bean.getOrigCustomerComment());
				
				// 订单Notes
				String noteId = addNotes(bean.getSourceOrderId(),
								OmsConstants.NotesType.SYSTEM, 
								bean.getOrderNumber(),
								note,
								user.getUserName(),
								user.getUserName());
				if (StringUtils.isEmpty(noteId)) {
					ret = false;
				}
			}
			
			// synship 同步
			if (ret) {
				ret = synShipSyncDao.updateCustomerComment(bean);
			}
			
			// 执行结果判定
			if (ret) {
				transactionManager.commit(status);
				
				// 设置返回结果
				setNotesToResult(bean.getSourceOrderId(), user, result);
				
				//	正常 
				result.setResult(true);
			} else {
				
				transactionManager.rollback(status);
				
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210040,
						 MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
		} catch (Exception ex) {
			
			ret = false;
			
			transactionManager.rollback(status);
			
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210040,
					 MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}
		
		return ret;
	}
	
	/**
	 * Invoice保存主函数
	 * 
	 * @return
	 */
	@Override
	public void updateInvoiceMain(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {
		
		if (updateInvoiceSub(bean, result, user)) {
			// Notes 结果返回
			setNotesToResult(bean.getSourceOrderId(), user, result);
			
			//	正常
			result.setResult(true);
		}
	}
	
	/**
	 * Invoice保存子函数
	 * 
	 * @return
	 */
	public boolean updateInvoiceSub(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {
		
		boolean ret = true;
		
		TransactionStatus status=transactionManager.getTransaction(def);
		
		try {
			
			// 更新用户设定
			bean.setModifier(user.getUserName());
			
			// Comment信息更新
			ret = orderDao.updateInvoice(bean);
		
			if (ret) {
				
				String invoice = "";
				if (!StringUtils.isEmpty(bean.getOrigInvoice())) {
					invoice = Type.getTypeName(OmsCodeConstants.INVOICE, bean.getOrigInvoice(), "en");
				}
				
				String invoiceKind = "";
				if (!StringUtils.isEmpty(bean.getOrigInvoiceKind())) {
					invoiceKind = Type.getTypeName(OmsCodeConstants.INVOICE_KIND, bean.getOrigInvoiceKind(), "cn");
				}
				
				String noteContent = String.format(OmsConstants.BEFORE_INVOICE_TITLE.INVOICE_INFO, bean.getInvoiceInfo())
									+ " ; " +
									String.format(OmsConstants.BEFORE_INVOICE_TITLE.INVOICE, invoice)	
									+ " ; " +
									String.format(OmsConstants.BEFORE_INVOICE_TITLE.INVOICE_KIND, invoiceKind);
									
				String note = String.format(OmsConstants.INVOICE_CHANGED, noteContent);
				
				// 订单Notes
				String noteId = addNotes(bean.getSourceOrderId(),
								OmsConstants.NotesType.SYSTEM, 
								bean.getOrderNumber(),
								note,
								user.getUserName(),
								user.getUserName());
				if (StringUtils.isEmpty(noteId)) {
					ret = false;
				}
			}
			
			// 执行结果判定
			if (ret) {
				transactionManager.commit(status);
				
				// 设置返回结果
				setNotesToResult(bean.getSourceOrderId(), user, result);
				
				//	正常 
				result.setResult(true);
			} else {
				
				transactionManager.rollback(status);
				
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210041,
						 MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
		} catch (Exception ex) {
			ret = false;
			
			transactionManager.rollback(status);
			
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210041,
					 MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}
		
		return ret;
	}
	
	/**
	 * InvoiceInfo保存主函数
	 * 
	 * @return
	 */
	@Override
	public void updateInvoiceInfoMain(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {
		
		TransactionStatus status=transactionManager.getTransaction(def);
		
		try {
			boolean ret = true;
			
			// 更新用户设定
			bean.setModifier(user.getUserName());
			
			String note=String.format(OmsConstants.INVOICEINFO_CHANGED, bean.getOrigInvoiceInfo());
			// Comment信息更新
			ret = orderDao.updateAddress(bean);
		
			if (ret) {
				// 订单Notes
				String noteId = addNotes(bean.getSourceOrderId(),
								OmsConstants.NotesType.SYSTEM, 
								bean.getOrderNumber(),
								note,
								user.getUserName(),
								user.getUserName());
				if (StringUtils.isEmpty(noteId)) {
					ret = false;
				}
			}
			
			// synship 同步
			if (ret) {
				ret = synShipSyncDao.updateNoteToCust(bean);
			}
			
			// 执行结果判定
			if (ret) {
				transactionManager.commit(status);
				
				// 设置返回结果
				setNotesToResult(bean.getSourceOrderId(), user ,result);
				
				//	正常 
				result.setResult(true);
			} else {
				
				transactionManager.rollback(status);
				
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210042,
						 MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
		} catch (Exception ex) {
			transactionManager.rollback(status);
			
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210042,
					 MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}
	}
	
	/**
	 * Ship to 地址保存主函数
	 * 
	 * @return
	 */
	@Override
	public void updateShipAddressMain(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {
		
		// 当前订单信息取得
		// 		订单信息取得
		OutFormOrderdetailOrders ordersInfo = orderDetailDao.getOrdersInfo(bean.getOrderNumber());
		// 		订单详情取得
		List<OutFormOrderDetailOrderDetail> orderDetailsList = orderDetailDao.getOrderDetailsInfo(bean.getOrderNumber());
		
		// 订单编辑 许可检查
		if (chkChangeOrderShipAddressPermit(ordersInfo, orderDetailsList, result)) {
		
			if (updateShipToAddress(bean, result, user)) {
				// 设置返回结果
				setNotesToResult(bean.getSourceOrderId(), user, result);
				
				//	正常
				result.setResult(true);
			}
		}

	}
	
	/**
	 * Ship to 地址保存
	 * 
	 * @return
	 */
	private boolean updateShipToAddress(OutFormOrderdetailOrders bean, AjaxResponseBean result, UserSessionBean user) {
		boolean ret = true;
		
		// synship 同步用字段设定
		setSynShipFields(bean);
		
		TransactionStatus status=transactionManager.getTransaction(def);
		
		try {
			bean.setModifier(user.getUserName());			
			// 地址信息更新
			ret = orderDao.updateShipToAddress(bean);
			
			StringBuffer noteBuf= new StringBuffer();
			if(isChanged(bean.getShipName(), bean.getOrigShipName())){
				noteBuf.append(getOutputNote(OmsConstants.BEFORE_EDIT_TITLE.NAME, bean.getOrigShipName())).append(";");
			}

			if(isChanged(bean.getShipEmail(), bean.getOrigShipEmail())){
				noteBuf.append(getOutputNote(OmsConstants.BEFORE_EDIT_TITLE.EMAIL, bean.getOrigShipEmail())).append(";");
			}
			
			if(isChanged(bean.getShipPhone(), bean.getOrigShipPhone())){
				noteBuf.append(getOutputNote(OmsConstants.BEFORE_EDIT_TITLE.PHONE, bean.getOrigShipPhone())).append(";");
			}
			
			if(isChanged(bean.getShipCompany(), bean.getOrigShipCompany())){
				noteBuf.append(getOutputNote(OmsConstants.BEFORE_EDIT_TITLE.COMPANY, bean.getOrigShipCompany())).append(";");
			}
			
			if(isChanged(bean.getShipAddress(), bean.getOrigShipAddress())){
				noteBuf.append(getOutputNote(OmsConstants.BEFORE_EDIT_TITLE.ADDRESS, bean.getOrigShipAddress())).append(";");
			}
			
			if(isChanged(bean.getShipAddress2(), bean.getOrigShipAddress2())){					
				noteBuf.append(getOutputNote(OmsConstants.BEFORE_EDIT_TITLE.ADDRESS2, bean.getOrigShipAddress2())).append(";");
			}
			
			if(isChanged(bean.getShipCity(), bean.getOrigShipCity())){
				noteBuf.append(getOutputNote(OmsConstants.BEFORE_EDIT_TITLE.CITY, bean.getOrigShipCity())).append(";");					
			}
			
			if(isChanged(bean.getShipState(), bean.getOrigShipState())){
				noteBuf.append(getOutputNote(OmsConstants.BEFORE_EDIT_TITLE.STATE, bean.getOrigShipState())).append(";");
			}
			
			if(isChanged(bean.getShipZip(), bean.getOrigShipZip())){
				noteBuf.append(getOutputNote(OmsConstants.BEFORE_EDIT_TITLE.ZIP, bean.getOrigShipZip())).append(";");
			}
			
			if(isChanged(bean.getShipCountry(), bean.getOrigShipCountry())){
				noteBuf.append(getOutputNote(OmsConstants.BEFORE_EDIT_TITLE.COUNTRY, bean.getOrigShipCountry())).append(";");
			}

			String note=String.format(OmsConstants.SHIP_TO_ADDRESS_CHANGED, noteBuf);
			
			if (note.length() > 1) {
				note = note.substring(0,note.length() - 1);	
			}			

			// notes 追加
			if (ret) {
				// 订单Notes
				String noteId = addNotes(bean.getSourceOrderId(),
								OmsConstants.NotesType.SYSTEM, 
								bean.getOrderNumber(),
								note,
								user.getUserName(),
								user.getUserName());
				if (StringUtils.isEmpty(noteId)) {
					ret = false;
				}
			}
			
			if (ret) {
				// synship 同步
				ret = synShipSyncDao.updateAddress(bean);
			}
			
			// 执行结果判定
			if (ret) {
				transactionManager.commit(status);
				
			} else {
				
				transactionManager.rollback(status);
				
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210012,
						 MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
		} catch (Exception ex) {
			logger.error("updateShipAddress", ex);
			
			ret = false;
			
			transactionManager.rollback(status);
			
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210012,
					 MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}
		
		return ret;
	}
	
	/**
	 * 一组订单价格保存（含锁单判定）
	 * 
	 * @param mainOrderPrice 主订单价格
	 * @param ordersInfo 订单信息
	 * @param user 当前用户
	 * 
	 * @return
	 */
	public boolean saveGroupOrderPrice(OrderPrice mainOrderPrice, OutFormOrderdetailOrders ordersInfo, UserSessionBean user) {
		
		boolean ret = true;
		
		// 一组订单
		//		订单价格取得
		OrdersBean mainOrdersInfo = setOrderPrice(mainOrderPrice);
		
		// 		modifier
		mainOrdersInfo.setModifier(user.getUserName());
		
		ret = orderDetailDao.updateGroupOrdersInfo(mainOrdersInfo);
		
		if (ret) {
			//	未锁定的场合
//			if (StringUtils.isEmpty(ordersInfo.getLockShip()) || OmsConstants.LockShip.No.equals(ordersInfo.getLockShip())) {
			if (!OmsConstants.LockShip.YES.equals(ordersInfo.getLockShip())) {
				//	expected < final_grand_total
				if (mainOrderPrice.getExpected() < mainOrderPrice.getFinalGrandTotal()) {
					ret = changeLockStatusSub(mainOrderPrice.getSourceOrderId(), true, user);
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * 订单Transactions信息保存
	 * 
	 * @param sourceOrderId 网络订单号
	 * @param orderNumber 订单号
	 * @param sku
	 * @param description 描述
	 * @param money 金额
	 * @param noteId
	 * @param type 类型
	 * @param isCanceled 是否取消操作
	 * 
	 * @return
	 */
	public boolean saveOrderTransactionsInfo(String sourceOrderId, String originSourceOrderId, String orderNumber, String sku, String itemNumber, String description, String money, String noteId, String type, boolean isCanceled, UserSessionBean user) {
		boolean ret = true;		

//		//	Notes 的 Comment
//		String description = orderDetailInfo.getSku();
//		if (!orderDetailInfo.isAdjustment()) {
//			description = OrderDetailSkuDsp.PRODUCT_TITLE;
//		}
		
		//	收入
		String debit = "0";
		//	支出
		String credit = "0";
		
		// 取消的场合
		if (isCanceled) {
			// 产品的场合
			if (OrderDetailSkuDsp.PRODUCT_TITLE.equals(description)) {
				credit = money;
			// shipping 的场合
			} else if (OrderDetailSkuDsp.SHIPPING_TITLE.equals(description)) {
				if (Float.valueOf(money) > 0) {
					credit = money;
				} else {
					debit = String.valueOf(mult2Digits(Float.valueOf(money), -1));
				}
			// surcharge 的场合
			} else if (OrderDetailSkuDsp.SURCHARGE_TITLE.equals(description)) {
				if (Float.valueOf(money) > 0) {
					credit = money;
				} else {
					debit = String.valueOf(mult2Digits(Float.valueOf(money), -1));
				}
			// discount 的场合
			} else if (OrderDetailSkuDsp.DISCOUNT_TITLE.equals(description)) {
				//	该处money为DB的值，（存入DB时 *-1）
					//	表示追加打折
				if (Float.valueOf(money) > 0) {
					credit = money;
				} else {
					// 表示取消打折
					debit = String.valueOf(mult2Digits(Float.valueOf(money), -1));
				}
			// 价差订单 的场合
			} else if (OrderDetailSkuDsp.PRICE_DIFF_TITLE.equals(description)) {
				credit = money;
			}	
		} else {
			// 产品的场合
			if (OrderDetailSkuDsp.PRODUCT_TITLE.equals(description)) {
				debit = money;
			// shipping 的场合
			} else if (OrderDetailSkuDsp.SHIPPING_TITLE.equals(description)) {
				if (Float.valueOf(money) > 0) {
					debit = money;
				} else {
					credit = String.valueOf(mult2Digits(Float.valueOf(money), -1));
				}
			// surcharge 的场合
			} else if (OrderDetailSkuDsp.SURCHARGE_TITLE.equals(description)) {
				if (Float.valueOf(money) > 0) {
					debit = money;
				} else {
					credit = String.valueOf(mult2Digits(Float.valueOf(money), -1));
				}
			// discount 的场合
			} else if (OrderDetailSkuDsp.DISCOUNT_TITLE.equals(description)) {
				//	该处money为画面输入的值，（存入DB时 *-1）
				//	表示打折
				if (Float.valueOf(money) > 0) {
					debit = money;
				//	表示不打折
				} else {
					credit = String.valueOf(mult2Digits(Float.valueOf(money), -1));
				}
			// 价差订单 的场合
			} else if (OrderDetailSkuDsp.PRICE_DIFF_TITLE.equals(description)) {
				debit = money;
			}			
		}

		
		TransactionsBean transactionsInfo = new TransactionsBean();
		//	网络订单号
		transactionsInfo.setSourceOrderId(sourceOrderId);
		//	原始网络订单号
		transactionsInfo.setOriginSourceOrderId(originSourceOrderId);
		//	订单号
		transactionsInfo.setOrderNumber(orderNumber);
		//	sku
		transactionsInfo.setSku(sku);
		//	itemNumber
		transactionsInfo.setItemNumber(itemNumber);
		//  description
		transactionsInfo.setDescription(description);
		//	应收金额
		transactionsInfo.setDebit(debit);
		//	应付金额 
		transactionsInfo.setCredit(credit);
		//	noteId
		transactionsInfo.setNoteId(noteId);
		//	类型
		transactionsInfo.setType(type);
		//	当前用户
		transactionsInfo.setCreater(user.getUserName());
		//	当前用户
		transactionsInfo.setModifier(user.getUserName());
		
		ret = transactionsDao.insertTransactionsInfo(transactionsInfo);
		if (!ret) {
			ret = false;
		}
		
		return ret;
	}
	
	/**
	 * 订单明细Cancel
	 * 
	 * @param inFormOrderdetailReturn 待删除明细项目
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return
	 */
	@Override
	public void cancelLineItemsMain(InFormOrderdetailReturn inFormOrderdetailReturn, AjaxResponseBean result, UserSessionBean user) {
		// 当前订单信息取得
		// 		订单信息取得
		OutFormOrderdetailOrders ordersInfo = orderDetailDao.getOrdersInfo(inFormOrderdetailReturn.getOrderNumber());
		// 		订单详情取得
		List<OutFormOrderDetailOrderDetail> orderDetailsList = orderDetailDao.getOrderDetailsInfo(inFormOrderdetailReturn.getOrderNumber());
		
		//	操作许可检查
		if (cancelLineItemPermitChkMain(inFormOrderdetailReturn, ordersInfo, orderDetailsList, result)) {
			
			//	订单明细删除
			if (cancelLineItem(inFormOrderdetailReturn, ordersInfo, orderDetailsList, user, result)) {
				
				//	正常返回
				setSuccessReturn(inFormOrderdetailReturn.getSourceOrderId(), inFormOrderdetailReturn.getOrderNumber(), result, user);
				
			}
		}
	}
	
	/**
	 * 校验多件订单LineItem
	 * 
	 * @param inFormOrderdetailReturn 待删除明细项目
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息 
	 * @param result 返回结果
	 * 
	 * @return
	 */
	private boolean cancelLineItemPermitChkMain(InFormOrderdetailReturn inFormOrderdetailReturn,
												OutFormOrderdetailOrders ordersInfo, 
												List<OutFormOrderDetailOrderDetail> orderDetailsList,
												AjaxResponseBean result) {
		boolean ret = true;
		
		// 待删除明细一览
		List<OrderDetailsBean> inOrderDetailsList = inFormOrderdetailReturn.getOrderDetailsList();
		
		for (int i = 0; i < inOrderDetailsList.size(); i++) {
			ret = delLineItemPermitChk(inOrderDetailsList.get(i).getItemNumber(), ordersInfo, orderDetailsList, result);
			if (!ret) {
				break;
			}
		}
		
		return ret;
	}
	
	/**
	 * 获取选中LineItem 及 关联折扣信息
	 * 
	 * @param orderDetailsList 订单明细信息
	 * @param selectedItemNumber 选中
	 * 
	 * @return 选中明细行
	 */
	private List<OutFormOrderDetailOrderDetail> getSelectedLineItems(List<OutFormOrderDetailOrderDetail> orderDetailsList, String selectedItemNumber) {
		
		List<OutFormOrderDetailOrderDetail> selectedOrderDetailList = new ArrayList<OutFormOrderDetailOrderDetail>();
		
		for (int i = 0; i < orderDetailsList.size(); i++) {
			OutFormOrderDetailOrderDetail orderDetailInfo = orderDetailsList.get(i);
			
			// 订单明细取得
			if (selectedItemNumber.equals(orderDetailInfo.getItemNumber())) {
				selectedOrderDetailList.add(orderDetailInfo);
			}
			
			// 关联订单明细取得
			if (selectedItemNumber.equals(orderDetailInfo.getSubItemNumber())) {
				selectedOrderDetailList.add(orderDetailInfo);
			}
		}
		
		return selectedOrderDetailList;
	}

	/**
	 * 获取选中LineItem 及 关联折扣信息
	 *
	 * @param orderDetailsList 订单明细信息
	 * @param inOrderDetailsList 选中明细行
	 *
	 * @return 选中明细行
	 */
	private List<OutFormOrderDetailOrderDetail> getSelectedLineItems(List<OutFormOrderDetailOrderDetail> orderDetailsList, List<OrderDetailsBean> inOrderDetailsList) {
		List<OutFormOrderDetailOrderDetail> selectedOrderDetailList = new ArrayList<OutFormOrderDetailOrderDetail>();

		for (int i = 0; i < inOrderDetailsList.size(); i++) {
			OrderDetailsBean inOrderDetailInfo = inOrderDetailsList.get(i);
			for (int j = 0; j < orderDetailsList.size(); j++) {
				OutFormOrderDetailOrderDetail orderDetailInfo = orderDetailsList.get(j);

				// 订单明细取得
				if (inOrderDetailInfo.getItemNumber().equals(orderDetailInfo.getItemNumber())) {
					selectedOrderDetailList.add(orderDetailInfo);
				}

				// 关联订单明细取得
				if (inOrderDetailInfo.getItemNumber().equals(orderDetailInfo.getSubItemNumber())) {
					selectedOrderDetailList.add(orderDetailInfo);
				}
			}
		}

		return selectedOrderDetailList;
	}
	
	/**
	 * 删除LineItem
	 * 
	 * @param inFormOrderdetailReturn 画面选中明细行
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * @param user 当前用户
	 * 
	 * @return
	 */
	public boolean cancelLineItem(InFormOrderdetailReturn inFormOrderdetailReturn,
									OutFormOrderdetailOrders ordersInfo, 
									List<OutFormOrderDetailOrderDetail> orderDetailsList,
									UserSessionBean user,
								  	AjaxResponseBean result) {
		boolean ret = true;
		
		logger.info("cancelLineItem start");
		
		TransactionStatus status=transactionManager.getTransaction(def);
		
		try {

			// 待删除明细一览
			List<OrderDetailsBean> inOrderDetailsList = inFormOrderdetailReturn.getOrderDetailsList();
			
			// 选中明细缓存用
			ArrayList<OutFormOrderDetailOrderDetail> selectedItems = new ArrayList<OutFormOrderDetailOrderDetail>();
			
			// 订单价格取得
			OrderPrice orderPrice = getOrderPrice(ordersInfo);
			
			for (int i = 0; i < inOrderDetailsList.size(); i++) {
				OrderDetailsBean inOrderDetailsInfo = inOrderDetailsList.get(i);
				
				// 待删除LineItem 及 关联折扣信息
				List<OutFormOrderDetailOrderDetail> selectedOrderDetailList = getSelectedLineItems(orderDetailsList, inOrderDetailsInfo.getItemNumber());
				
				for (int j = 0; j < selectedOrderDetailList.size(); j++) {
					
					OutFormOrderDetailOrderDetail selectedOrderDetailInfo = selectedOrderDetailList.get(j);
					
					// 选中明细缓存
					selectedItems.add(selectedOrderDetailInfo);
					
					// 原因设定
					selectedOrderDetailInfo.setReason(inFormOrderdetailReturn.getReason());
					
					// 订单价格计算
//					OrderPrice orderPrice = getOrderPriceMain(selectedOrderDetailInfo.getItemNumber(), ordersInfo, orderDetailsList);
					getOrderPriceMainForCancelLineItems(orderPrice, selectedOrderDetailInfo.getItemNumber(), ordersInfo, orderDetailsList);
					
//					// 主订单价格取得
//					OrderPrice mainOrderPrice = getMainOrderPrice(ordersInfo.getSourceOrderId(), orderPrice);
					
					// 订单明细保存
					logger.info("saveOrderDetailCancelOrderDetail");
					ret = saveOrderDetailCancelOrderDetail(selectedOrderDetailInfo, ordersInfo, orderDetailsList, orderPrice, user);
					
//					// 订单金额变更
//					if (ret) {
//						ret = saveOrderForDelOrderDetail(orderPrice, user);
//					}
					
//					// 一组订单价格更新
//					if (ret) {
//						ret = saveGroupOrderPrice(mainOrderPrice, user);
//					}
					
					// 订单Notes保存
					if (ret) {
						logger.info("saveOrderNotesForDelOrderDetail");
						ret = saveOrderNotesForDelOrderDetail(inFormOrderdetailReturn.getSourceOrderId(), ordersInfo, selectedOrderDetailInfo, orderDetailsList ,user);
					}
					
					//	synShip 同步
					if (ret) {
						logger.info("syncSynshipFinalGrandTotal");
						ret = syncSynshipFinalGrandTotal(orderPrice, user);
					}
				}
			}
			
			// 主订单价格取得
			logger.info("getMainOrderPrice");
			OrderPrice mainOrderPrice = getMainOrderPrice(ordersInfo.getSourceOrderId(), orderPrice);
			
			// 订单金额变更
			if (ret) {
				logger.info("saveOrderForDelOrderDetail");
				ret = saveOrderForDelOrderDetail(orderPrice, user);
			}
			
			// 一组订单价格更新
			if (ret) {
				logger.info("saveGroupOrderPrice");
				ret = saveGroupOrderPrice(mainOrderPrice, ordersInfo, user);
			}
			
			// 判断订单是否Canceled
			ArrayList<Object> judgeOrderCanceledRet = null;
			if (ret) {
				judgeOrderCanceledRet = judgeOrderCanceled(inFormOrderdetailReturn, orderDetailsList, selectedItems, user);
				
				ret = (boolean)judgeOrderCanceledRet.get(0);
			}
			
			// 判断订单是否Returned
			if (ret) {
				// 订单状态未Canceled的场合
				if (StringUtils.isEmpty((String)judgeOrderCanceledRet.get(1))) {

					ArrayList<Object> judgeOrderReturnedRet = judgeOrderReturned(inFormOrderdetailReturn, orderDetailsList, selectedItems, user);
					
					ret = (boolean)judgeOrderReturnedRet.get(0);
				}
			}

			if (!ret) {
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210001, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}

			//	CA订单取消
			if (ret) {
				if (isNeedCancelCAOrder(ordersInfo.getOrderChannelId())) {

					if (isHaveGoods(getSelectedLineItems(orderDetailsList, inOrderDetailsList))) {
						logger.info("cancelOrderDetailInfoForCA");
						ret = cancelOrderDetailInfoForCA(ordersInfo, getSelectedLineItems(orderDetailsList, inOrderDetailsList), user, result, false);
					}
				}
			}
			
			if (ret) {
				logger.info("cancelLineItem success");
				transactionManager.commit(status);
			} else {
				logger.error("cancelLineItem error");
				transactionManager.rollback(status);
			}
			
		} catch (Exception e) {
			ret = false;
			
			logger.error("cancelLineItem", e);
			
			transactionManager.rollback(status);

			// 订单取消异常
			result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, "cancelLineItem error : " + e.getMessage());
		}
		
		return ret;
	}

	/**
	 * 物品有无判定
	 *
	 * @param judgeItems 待判定订单明细信息
	 *
	 * @return 判定结果
	 */
	private boolean isHaveGoods(List<OutFormOrderDetailOrderDetail> judgeItems) {
		boolean ret = false;

		for (int i = 0; i < judgeItems.size(); i++) {
			OutFormOrderDetailOrderDetail itemInfo = judgeItems.get(i);

			if (!itemInfo.getSku().equals("Shipping") && !itemInfo.getSku().equals("Discount")) {
				ret = true;
				break;
			}
		}

		return ret;
	}
	
	/**
	 * 根据未选中行，判断订单是否Canceled
	 * 
	 * @param inFormOrderdetailReturn 画面输入项目
	 * @param orderDetailsList 订单明细信息
	 * @param selectedItems 选中明细行
	 * @param user 当前用户
	 * 
	 * @return	ArrayList[0]	执行结果
	 * 			ArrayList[1]	订单状态
	 */
	private ArrayList<Object> judgeOrderCanceled(InFormOrderdetailReturn inFormOrderdetailReturn,
										List<OutFormOrderDetailOrderDetail> orderDetailsList,
										ArrayList<OutFormOrderDetailOrderDetail> selectedItems,
										UserSessionBean user) {
		logger.info("judgeOrderCanceled");
		
		ArrayList<Object> retResult = new ArrayList<Object>();
		
		//	执行结果
		boolean ret = true;
		//	订单状态
		String orderStatus = "";
		
//		未选中项，全部删除的场合				
		//		待判定状态
		ArrayList<String> judgeStatusList = new ArrayList<String>();
		//		取消
		judgeStatusList.add(OmsCodeConstants.OrderStatus.CANCELED);
		
		if (isAllUnSelectedItemSameStatusList(orderDetailsList, selectedItems, judgeStatusList)) {
			
			orderStatus = OmsCodeConstants.OrderStatus.CANCELED;
			
			//	订单状态变化（订单Cancel）
			OrdersBean orderInfo = new OrdersBean();
			
			orderInfo.setOrderNumber(inFormOrderdetailReturn.getOrderNumber());
			orderInfo.setCancelled(true);
			orderInfo.setOrderStatus(OmsCodeConstants.OrderStatus.CANCELED);
			orderInfo.setModifier(user.getUserName());
			
			logger.info("	updateOrdersStatusInfo");
			ret = orderDetailDao.updateOrdersStatusInfoForCanceOrder(orderInfo);
			
			if (ret) {
				//	Notes 追加
				String notes = "";					
				// "Order Status changed to: Canceled. Reason: %s";
				notes = String.format(OmsConstants.CANCEL_ORDER, inFormOrderdetailReturn.getReason());
				
				// 明细行番号
				int itemNumber = orderDetailDao.getOrderDetailsNotesMaxItemNumber(inFormOrderdetailReturn.getOrderNumber()) + 1;		
				
				//	订单更新Notes
				//	订单Notes
				NotesBean notesInfo = new NotesBean();
				// 		sourceOrderId
				notesInfo.setSourceOrderId(inFormOrderdetailReturn.getSourceOrderId());
				//		type
				notesInfo.setType(OmsConstants.NotesType.SYSTEM);
				// 		order_number
				notesInfo.setNumericKey(inFormOrderdetailReturn.getOrderNumber());
				//		item_number
				notesInfo.setItemNumber(String.valueOf(itemNumber));
				//		notes
				notesInfo.setNotes(notes);
				//		entered_by
				notesInfo.setEnteredBy(user.getUserName());
				//		creater
				notesInfo.setCreater(user.getUserName());
				//		modifier
				notesInfo.setModifier(user.getUserName());
				
				logger.info("	insertNotesInfo");
				ret = notesDao.insertNotesInfo(notesInfo);						
			}
			
			// synship 状态同步
			if (ret) {
				logger.info("	syncSynshipSEOrderStatus");
				ret = syncSynshipSEOrderStatus(inFormOrderdetailReturn.getOrderNumber(), OmsCodeConstants.OrderStatus.CANCELED, user);
			}
		}		
		
		// 执行结果异常，状态清空
		if (!ret) {
			orderStatus = "";
		}
		
		//	执行结果
		retResult.add(ret);
		//	订单状态
		retResult.add(orderStatus);
		
		return retResult;
	}
	
	/**
	 * 根据未选中行，判断订单是否Returned
	 * 
	 * @param inFormOrderdetailReturn 画面输入项目
	 * @param orderDetailsList 订单明细信息
	 * @param selectedItems 选中明细行
	 * @param user 当前用户
	 * 
	 * @return	ArrayList[0]	执行结果
	 * 			ArrayList[1]	订单状态
	 */
	private ArrayList<Object> judgeOrderReturned(InFormOrderdetailReturn inFormOrderdetailReturn,
										List<OutFormOrderDetailOrderDetail> orderDetailsList,
										List<OutFormOrderDetailOrderDetail> selectedItems,
										UserSessionBean user) {
		logger.info("judgeOrderReturned");
		
		ArrayList<Object> retResult = new ArrayList<Object>();
		
		//	执行结果
		boolean ret = true;
		//	订单状态
		String orderStatus = "";
		
		//	待判定状态
		ArrayList<String> judgeStatusList = new ArrayList<String>();
		judgeStatusList.add(OmsCodeConstants.OrderStatus.RETURNED);
		judgeStatusList.add(OmsCodeConstants.OrderStatus.CANCELED);
		//	adjustment 状态对应
		judgeStatusList.add("");
		
		if (isAllUnSelectedItemSameStatusList(orderDetailsList, selectedItems, judgeStatusList)) {
			
			orderStatus = OmsCodeConstants.OrderStatus.RETURNED;
			
			//	订单状态变化
			OrdersBean orderInfo = new OrdersBean();
			
			orderInfo.setOrderNumber(inFormOrderdetailReturn.getOrderNumber());
			orderInfo.setOrderStatus(OmsCodeConstants.OrderStatus.RETURNED);
			orderInfo.setModifier(user.getUserName());	
			
			logger.info("	updateOrdersStatusInfo");
			ret = orderDetailDao.updateOrdersStatusInfo(orderInfo);
			
			if (ret) {
				//	Notes 追加
				String notes = "";					
				// "Order Status changed to: Returned";
				notes = String.format(OmsConstants.RETURN_ORDER, inFormOrderdetailReturn.getReason());
				
				// 明细行番号
				int itemNumber = orderDetailDao.getOrderDetailsNotesMaxItemNumber(inFormOrderdetailReturn.getOrderNumber()) + 1;		
				
				//	订单更新Notes
				//	订单Notes
				NotesBean notesInfo = new NotesBean();
				// 		sourceOrderId
				notesInfo.setSourceOrderId(inFormOrderdetailReturn.getSourceOrderId());
				//		type
				notesInfo.setType(OmsConstants.NotesType.SYSTEM);
				// 		order_number
				notesInfo.setNumericKey(inFormOrderdetailReturn.getOrderNumber());
				//		item_number
				notesInfo.setItemNumber(String.valueOf(itemNumber));
				//		notes
				notesInfo.setNotes(notes);
				//		entered_by
				notesInfo.setEnteredBy(user.getUserName());
				//		Creater
				notesInfo.setCreater(user.getUserName());
				//		modifier
				notesInfo.setModifier(user.getUserName());
				
				logger.info("	insertNotesInfo");
				ret = notesDao.insertNotesInfo(notesInfo);
			}
			
			// synship 状态同步
			if (ret) {
				logger.info("	syncSynshipSEOrderStatus");
				ret = syncSynshipSEOrderStatus(inFormOrderdetailReturn.getOrderNumber(), OmsCodeConstants.OrderStatus.RETURNED, user);
			}
		}
		
		// 执行结果异常，状态清空
		if (!ret) {
			orderStatus = "";
		}
		
		//	执行结果
		retResult.add(ret);
		//	订单状态
		retResult.add(orderStatus);
		
		return retResult;
	}
	
//	/**
//	 * 未选中明细是否相同状态
//	 * 
//	 * @param orderDetailsList 全体明细
//	 * @param selectedItems 选中明细
//	 * @param status 待判定状态
//	 * 
//	 * @return 
//	 */
//	private boolean isAllUnSelectedItemSameStatus(List<OutFormOrderDetailOrderDetail> orderDetailsList, List<OutFormOrderDetailOrderDetail> selectedItems, String status) {
//		boolean ret = true;
//		
//		for (int i = 0; i < orderDetailsList.size(); i++) {
//			OutFormOrderDetailOrderDetail orderDetailsInfo = orderDetailsList.get(i);
//			
//			// 调整项目跳过
//			if (orderDetailsInfo.isAdjustment()) {
//				continue;
//			}
//			
//			boolean isSelected = false;
//			
//			for (int j = 0; j < selectedItems.size(); j++) {
//				if (orderDetailsInfo.getItemNumber().equals(selectedItems.get(j).getItemNumber())) {
//					isSelected = true;
//					break;
//				}
//			}
//			
//			// 该项目未选中的场合
//			if (!isSelected) {
//				if (!status.equals(orderDetailsInfo.getStatus())) {
//					ret = false;
//					break;
//				}
//			}
//		}
//		
//		return ret;
//	}	
	
	/**
	 * 未选中明细是否相同状态
	 * 
	 * @param orderDetailsList 全体明细
	 * @param selectedItems 选中明细
	 * 
	 * @return 
	 */
	private boolean isAllUnSelectedItemSameStatusList(List<OutFormOrderDetailOrderDetail> orderDetailsList, List<OutFormOrderDetailOrderDetail> selectedItems, List<String> statusList) {
		boolean ret = true;
		
		for (int i = 0; i < orderDetailsList.size(); i++) {
			OutFormOrderDetailOrderDetail orderDetailsInfo = orderDetailsList.get(i);
			
			// 明细所有项目全部判定（不跳过）
//			// 调整项目跳过
//			if (orderDetailsInfo.isAdjustment()) {
//				continue;
//			}
			
			boolean isSelected = false;
			
			for (int j = 0; j < selectedItems.size(); j++) {
				if (orderDetailsInfo.getItemNumber().equals(selectedItems.get(j).getItemNumber())) {
					isSelected = true;
					break;
				}
			}
			
			// 该项目未选中的场合
			if (!isSelected) {
				if (!statusList.contains(orderDetailsInfo.getStatus())) {
					ret = false;
					break;
				}
				
			}
		}
		
		return ret;
	}
	
	/**
	 * 取得订单金额 主处理
	 * 
	 * @param itemNumber 明细番号
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * 
	 * @return 订单价格
	 */
	private OrderPrice getOrderPriceMain(String itemNumber,
											OutFormOrderdetailOrders ordersInfo,
											List<OutFormOrderDetailOrderDetail> orderDetailsList) {
		
		// 获取选中LineItem
		OutFormOrderDetailOrderDetail selectedOrderDetailInfo = getSelectedLineItem(orderDetailsList, itemNumber);
		
		// 订单价格
		OrderPrice orderPrice = getOrderPrice(ordersInfo);
		
		// 设定调整价格
		setAdjustPriceCancelItem(ordersInfo, selectedOrderDetailInfo, orderDetailsList, orderPrice);
		
		// 价格再计算
		priceReCalculate(orderPrice);
		
//		// 明细价格再计算
//		setItemAdjustPrice(orderDetailsList, orderPrice);
		
		return orderPrice;
	}
	
	/**
	 * 取得订单金额 主处理
	 * 
	 * @param orderPrice 订单价格
	 * @param itemNumber 明细番号
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * 
	 * @return 订单价格
	 */
	private void getOrderPriceMainForCancelLineItems(OrderPrice orderPrice,
															String itemNumber,
															OutFormOrderdetailOrders ordersInfo,
															List<OutFormOrderDetailOrderDetail> orderDetailsList) {
		
		// 获取选中LineItem
		OutFormOrderDetailOrderDetail selectedOrderDetailInfo = getSelectedLineItem(orderDetailsList, itemNumber);
		
//		// 订单价格
//		OrderPrice orderPrice = getOrderPrice(ordersInfo);
		
		// 设定调整价格
		setAdjustPriceCancelItem(ordersInfo, selectedOrderDetailInfo, orderDetailsList, orderPrice);
		
		// 价格再计算
		priceReCalculate(orderPrice);
		
//		// 明细价格再计算
//		setItemAdjustPrice(orderDetailsList, orderPrice);
		
		return;
	}
	
	/**
	 * Cancel 多条 LineItem订单价格调整
	 * 
	 * @param ordersInfo 订单信息
	 * @param orderDetailInfo 选中订单明细信息
	 * @param orderDetailsList 订单明细信息
	 * @param orderPrice 订单价格
	 * 
	 * @return
	 */
	private void setAdjustPriceCancelItem(OutFormOrderdetailOrders ordersInfo, OutFormOrderDetailOrderDetail orderDetailInfo, List<OutFormOrderDetailOrderDetail> orderDetailsList, OrderPrice  orderPrice) {
		
		// surcharge 的场合
		if (OmsConstants.OrderDetailSkuDsp.SURCHARGE_TITLE.equals(orderDetailInfo.getSku())) {
			// revised_surcharge
			float revisedSurcharge = sub2Digits(orderPrice.getRevisedSurcharge() ,Float.valueOf(orderDetailInfo.getPricePerUnit()));
			
			orderPrice.setRevisedSurcharge(revisedSurcharge);
			
		// shipping 的场合
		} else if (OmsConstants.OrderDetailSkuDsp.SHIPPING_TITLE.equals(orderDetailInfo.getSku())) {
			// final_shipping_total
			float finalShippingTotal = sub2Digits(orderPrice.getFinalShippingTotal() ,Float.valueOf(orderDetailInfo.getPricePerUnit()));
			
			orderPrice.setFinalShippingTotal(finalShippingTotal);
			
		// discount 的场合
		} else if (OmsConstants.OrderDetailSkuDsp.DISCOUNT_TITLE.equals(orderDetailInfo.getSku())) {
			
//			// 手工打折的场合
//			if (OmsConstants.DiscountType.MANUAL.equals(ordersInfo.getDiscountType())) {
//				// revised_discount
//				float revisedDiscount = sub2Digits(orderPrice.getRevisedDiscount(), Float.valueOf(orderDetailInfo.getPricePerUnit()));
//				
//				orderPrice.setRevisedDiscount(revisedDiscount);
//			}
			
			// 本次不区分手工打折，百分比打折（20150511）
			// revised_discount
			float revisedDiscount = sub2Digits(orderPrice.getRevisedDiscount(), Float.valueOf(orderDetailInfo.getPricePerUnit()));
			
			orderPrice.setRevisedDiscount(revisedDiscount);
			
		// sku 的场合
		} else {			
			// final_product_total
			float finalProductTotal = sub2Digits(orderPrice.getFinalProductTotal(), Float.valueOf(orderDetailInfo.getPricePerUnit()));
			orderPrice.setFinalProductTotal(finalProductTotal);
			
			// 本次原始运费不处理（20150506）
//			// original shipping
//			float originalShipping = getOriginalShipping(orderDetailsList);
//			
//			// 订单物品金额 == 0 && 原始运费有的场合
//			if (finalProductTotal == 0  && originalShipping > 0) {
//				
//				// final_shipping_total
//				float finalShippingTotal = sub2Digits(orderPrice.getFinalShippingTotal() ,originalShipping);
//				
//				orderPrice.setFinalShippingTotal(finalShippingTotal);
//			}
		}
	}
	
//	/**
//	 * 订单明细保存（订单明细 删除）
//	 * 
//	 * @param outFormOrderDetailOrderDetail 当前选中明细行（画面传入）
//	 * @param ordersInfo 订单信息
//	 * @param orderDetailsList 订单明细信息
//	 * @param orderPrice 订单价格
//	 * @param user 当前用户
//	 * 
//	 * @return
//	 */
//	private boolean saveOrderDetailCancelOrderDetail(OutFormOrderDetailOrderDetail outFormOrderDetailOrderDetail, 
//													OutFormOrderdetailOrders ordersInfo, 
//													List<OutFormOrderDetailOrderDetail> orderDetailsList,
//													OrderPrice orderPrice,
//													UserSessionBean user) {
//		boolean ret = true;
//		
//		// 获取选中LineItem
//		OutFormOrderDetailOrderDetail selectedOrderDetailInfo = getSelectedLineItem(orderDetailsList, outFormOrderDetailOrderDetail.getItemNumber());
//		
//		// 订单明细Bean
//		OrderDetailsBean orderDetailInfo = new OrderDetailsBean();
//		//		订单号
//		orderDetailInfo.setOrderNumber(outFormOrderDetailOrderDetail.getOrderNumber());
//		//		订单明细番号
//		orderDetailInfo.setItemNumber(outFormOrderDetailOrderDetail.getItemNumber());
//		
//		// surcharge 的场合
//		if (OmsConstants.OrderDetailSkuDsp.SURCHARGE_TITLE.equals(selectedOrderDetailInfo.getSku())) {
//			
//			ret = orderDetailDao.deleteOrderDetailsInfo(orderDetailInfo);
//			
//		// shipping 的场合
//		} else if (OmsConstants.OrderDetailSkuDsp.SHIPPING_TITLE.equals(selectedOrderDetailInfo.getSku())) {
//			
//			// 	本次原始运费不处理
////			//	调整shipping的场合 
////			if (OmsConstants.OrderDetailProductDsp.SHIPPING_ADJUSTMENT_TITLE.equals(selectedOrderDetailInfo.getProduct())) {
////				ret = orderDetailDao.deleteOrderDetailsInfo(orderDetailInfo);
////			}
//			
//			ret = orderDetailDao.deleteOrderDetailsInfo(orderDetailInfo);
//			
//		// discount 的场合 
//		} else if (OmsConstants.OrderDetailSkuDsp.DISCOUNT_TITLE.equals(selectedOrderDetailInfo.getSku())) {
////			//	不打折 或  百分比打折的场合
////			if (OmsConstants.DiscountType.NODISCOUNT.equals(ordersInfo.getDiscountType()) ||
////					OmsConstants.DiscountType.PERCENT.equals(ordersInfo.getDiscountType())) {
////			
////			} else {
////			//  手工打折的场合
////				ret = orderDetailDao.deleteOrderDetailsInfo(orderDetailInfo);
////			}
//			
//			//	本次不区分百分比打折，手工打折（20150511）
//			ret = orderDetailDao.deleteOrderDetailsInfo(orderDetailInfo);
//			
//		// sku 的场合
//		} else {
//			//	订单明细更新
//			//		订单明细 product
//			orderDetailInfo.setProduct(OmsConstants.PRODUCT_CANCELLED_TITLE + selectedOrderDetailInfo.getProduct());
//			//		quantity_shipped
//			orderDetailInfo.setQuantityShipped("0");
//			//		订单明细状态
//			orderDetailInfo.setStatus(OmsCodeConstants.OrderStatus.CANCELED);
//			//  	更新者
//			orderDetailInfo.setModifier(user.getUserName());			
//			ret = orderDetailDao.updateOrderDetailsInfo(orderDetailInfo);
//			
////			//	当前最大明细番号
////			int maxItemNumber = getOrderDetailsMaxItemNumber(orderDetailsList);
////			
////			//  插入项目共通设定
////			OrderDetailsBean orderDetailsInfoForInsert = new OrderDetailsBean();
////			//	订单号
////			orderDetailsInfoForInsert.setOrderNumber(outFormOrderDetailOrderDetail.getOrderNumber());
////			//	adjustment
////			orderDetailsInfoForInsert.setAdjustment(true);
////			//	quantity_ordered
////			orderDetailsInfoForInsert.setQuantityOrdered("1");
////			//	quantity_shipped
////			orderDetailsInfoForInsert.setQuantityShipped("1");
////			//	quantity_returned
////			orderDetailsInfoForInsert.setQuantityReturned("0");
////			//	status
////			orderDetailsInfoForInsert.setStatus("");
////			//	syncSynship
////			orderDetailsInfoForInsert.setSyncSynship(false);
////			//	resId
////			orderDetailsInfoForInsert.setResId("0");
////			//	modifier
////			orderDetailsInfoForInsert.setModifier(user.getUserName());
//			
//			// 本次运费不自动处理 20150507
////			//	shipping更新
////			//		订单物品价格为0的场合
////			if (ret && orderPrice.getFinalProductTotal() == 0){
////				//	原始订单价格
////				float originalShipping = getOriginalShipping(orderDetailsList);
////				
////				if (originalShipping > 0) {					
////
////					//	明细番号
////					maxItemNumber = maxItemNumber + 1;
////					orderDetailsInfoForInsert.setItemNumber(String.valueOf(maxItemNumber));
////
////					//	product
////					orderDetailsInfoForInsert.setProduct(OmsConstants.OrderDetailProductDsp.SHIPPING_CHARGE_ADJUSTMENT_TITLE);
////					//	price_per_unit
////					orderDetailsInfoForInsert.setPricePerUnit(String.valueOf(mult2Digits(originalShipping, -1f)));
////
////					//	sku
////					orderDetailsInfoForInsert.setSku(OmsConstants.OrderDetailSkuDsp.SHIPPING_TITLE);
////
////					ret = orderDetailDao.insertOrderDetailsInfo(orderDetailsInfoForInsert);
////				}
////			}
//			
//			// 本次只有手动打折
////			// discount更新
////			// 		百分比打折的场合
////			if (ret && OmsConstants.DiscountType.PERCENT.equals(ordersInfo.getDiscountType())) {
////				//	打折金额设定
////				float discount = mult2Digits(Float.valueOf(selectedOrderDetailInfo.getPricePerUnit()), Float.valueOf(ordersInfo.getDiscountPercent()));
////				
////				//	明细番号
////				maxItemNumber = maxItemNumber + 1;
////				orderDetailsInfoForInsert.setItemNumber(String.valueOf(maxItemNumber));
////
////				//	product
////				orderDetailsInfoForInsert.setProduct(OmsConstants.OrderDetailProductDsp.DISCOUNT_DJUSTMENT_TITLE_DELlINEITEM);
////				//	price_per_unit
////				orderDetailsInfoForInsert.setPricePerUnit(String.valueOf(discount));
////
////				//	sku
////				orderDetailsInfoForInsert.setSku(OmsConstants.OrderDetailSkuDsp.DISCOUNT_TITLE);
////
////				ret = orderDetailDao.insertOrderDetailsInfo(orderDetailsInfoForInsert);
////			}
//		}
//		
//		return ret;
//	}
	
	/**
	 * 订单明细保存（订单明细  非物理删除变更）
	 * 
	 * @param outFormOrderDetailOrderDetail 当前选中明细行（画面传入）
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * @param orderPrice 订单价格
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrderDetailCancelOrderDetail(OutFormOrderDetailOrderDetail outFormOrderDetailOrderDetail, 
													OutFormOrderdetailOrders ordersInfo, 
													List<OutFormOrderDetailOrderDetail> orderDetailsList,
													OrderPrice orderPrice,
													UserSessionBean user) {
		boolean ret = true;
		
		// 获取选中LineItem
		OutFormOrderDetailOrderDetail selectedOrderDetailInfo = getSelectedLineItem(orderDetailsList, outFormOrderDetailOrderDetail.getItemNumber());
		
		//	订单明细更新（surcharge，shipping，discount，sku 的场合，共通处理）
		OrderDetailsBean orderDetailInfo = new OrderDetailsBean();
		//		订单号
		orderDetailInfo.setOrderNumber(outFormOrderDetailOrderDetail.getOrderNumber());
		//		订单明细番号
		orderDetailInfo.setItemNumber(outFormOrderDetailOrderDetail.getItemNumber());
		//		订单明细 product
		orderDetailInfo.setProduct(OmsConstants.PRODUCT_CANCELLED_TITLE + selectedOrderDetailInfo.getProduct());
		//		quantity_shipped
		orderDetailInfo.setQuantityShipped("0");
		//		订单明细状态
		orderDetailInfo.setStatus(OmsCodeConstants.OrderStatus.CANCELED);
		//  	更新者
		orderDetailInfo.setModifier(user.getUserName());
		
		ret = orderDetailDao.updateOrderDetailsInfo(orderDetailInfo);
	
		return ret;
	}
	
	/**
	 * 订单明细打折主函数
	 * 
	 * @return
	 */
	public void saveOrderDetailDiscountMain(InFormOrderdetailReturn inFormOrderdetailReturn, AjaxResponseBean result, UserSessionBean user) {
		
		// 订单明细打折
		if (saveOrderDetailDiscount(inFormOrderdetailReturn, result, user)) {
			
//			// 订单信息返回
//			OutFormOrderdetailOrders orderInfo = getOrdersInfo(inFormOrderdetailReturn.getOrderNumber(), user);
//			// 	订单Notes信息
//			List<OutFormOrderdetailNotes> orderNotesList = getOrderNotesInfoBySourceOrderId(inFormOrderdetailReturn.getSourceOrderId(), user);
//			
//			// 设置返回结果
//			Map<String, Object> ordersListMap = new HashMap<String, Object>();			
//			// 		订单
//			ordersListMap.put("orderInfo", orderInfo);
//			// 		订单NotesList
//			ordersListMap.put("orderNotesList", orderNotesList);
//			
//			result.setResultInfo(ordersListMap);
//			
//			//		正常
//			result.setResult(true);
			
			//		正常返回
			setSuccessReturn(inFormOrderdetailReturn.getSourceOrderId(), inFormOrderdetailReturn.getOrderNumber(), result, user);			
		} else {
			
			// 异常返回（订单号不存在）
			result.setResult(false, MessageConstants.MESSAGE_CODE_200004, 
								MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}
	}

	/**
	 * 订单明细打折
	 * 
	 * @return
	 */
	private boolean saveOrderDetailDiscount(InFormOrderdetailReturn inFormOrderdetailReturn, AjaxResponseBean result, UserSessionBean user) {
		boolean ret = true;
		
		logger.info("saveOrderDetailDiscount start");
		TransactionStatus status=transactionManager.getTransaction(def);
		
		try {
			String orderNumber = inFormOrderdetailReturn.getOrderNumber();
			// 		item_number 取得（当前最大item_number + 1）
			int itemNumber = orderDetailDao.getOrderDetailsMaxItemNumber(orderNumber) + 1;
			
			// 当前订单信息取得
			// 		订单信息取得
			OutFormOrderdetailOrders ordersInfo = orderDetailDao.getOrdersInfo(orderNumber);
			// 		订单详情取得
			List<OutFormOrderDetailOrderDetail> orderDetailsList = orderDetailDao.getOrderDetailsInfo(orderNumber);
			
			// 订单价格计算
			OrderPrice orderPrice = getOrderPriceMain(inFormOrderdetailReturn, ordersInfo, orderDetailsList);
			
			// 主订单价格取得
			OrderPrice mainOrderPrice = getMainOrderPrice(ordersInfo.getSourceOrderId(), orderPrice);
			
			// 订单明细保存
			logger.info("saveOrderDetail");
			ret = saveOrderDetail(inFormOrderdetailReturn, itemNumber, ordersInfo.getOrderChannelId(),user);
			
			// 订单金额变更
			if (ret) {
				logger.info("saveOrder");
				ret = saveOrder(orderPrice, user);
			}
			
			// 一组订单价格更新
			if (ret) {
				logger.info("saveGroupOrderPrice");
				ret = saveGroupOrderPrice(mainOrderPrice, ordersInfo, user);
			}
			
			// 订单Notes保存
			if (ret) {
				logger.info("saveOrderNotes");
				ret = saveOrderNotes(inFormOrderdetailReturn, ordersInfo, itemNumber, user);
			}
			
			//	synShip 同步
			if (ret) {
				logger.info("syncSynshipFinalGrandTotal");
				ret = syncSynshipFinalGrandTotal(orderPrice, user);
			}
			
			if (ret) {
				logger.info("saveOrderDetailDiscount success");
				transactionManager.commit(status);
			} else {
				logger.info("saveOrderDetailDiscount error");
				transactionManager.rollback(status);
			}
			
		} catch (Exception e) {
			ret = false;
			
			logger.error("saveOrderDetailDiscount", e);
			
			transactionManager.rollback(status);
		}
		
		return ret;
	}
	
	/**
	 * 取得订单金额 主处理
	 * 
	 * @param inFormOrderdetailReturn 输入订单明细打折信息
	 * @param ordersInfo 订单信息
	 * @param orderDetailsList 订单明细信息
	 * 
	 * @return 订单价格
	 */
	private OrderPrice getOrderPriceMain(InFormOrderdetailReturn inFormOrderdetailReturn, OutFormOrderdetailOrders ordersInfo, List<OutFormOrderDetailOrderDetail> orderDetailsList) {
		
		// 订单价格
		OrderPrice orderPrice = getOrderPrice(ordersInfo);
		
		// 设定调整价格
		setAdjustPrice(inFormOrderdetailReturn, orderPrice);
		
		// 价格再计算
		priceReCalculate(orderPrice);
		
		// 手动调整不需要再计算
//		// 明细价格再计算
//		setItemAdjustPrice(orderDetailsList, orderPrice);
		
		return orderPrice;
	}

	/**
	 * 设定调整价格（物品折扣信息）
	 * 
	 * @param inFormOrderdetailReturn 输入调整信息
	 * @param orderPrice 订单价格（I/O）
	 * 
	 */
	private void setAdjustPrice(InFormOrderdetailReturn inFormOrderdetailReturn, OrderPrice  orderPrice) {
		
		Float adjustPrice = orderPrice.getRevisedDiscount();
		
		List<OrderDetailsBean> orderDetailsList = inFormOrderdetailReturn.getOrderDetailsList();
		
		for (int i = 0; i < orderDetailsList.size(); i++) {
			
			adjustPrice = add2Digits(adjustPrice, Float.valueOf(orderDetailsList.get(i).getPricePerUnit()));
		}
		orderPrice.setRevisedDiscount(adjustPrice);
	}
	
	/**
	 * 订单明细保存
	 * 
	 * @param inFormOrderdetailReturn 输入调整信息
	 * @param itemNumber 明细番号
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrderDetail(InFormOrderdetailReturn inFormOrderdetailReturn, int itemNumber, String orderChannelId, UserSessionBean user) {
		boolean ret = true;
		
		// product 信息
		String product = OmsConstants.OrderDetailProductDsp.DISCOUNT_DJUSTMENT_TITLE + inFormOrderdetailReturn.getReason();

		List<OrderDetailsBean> orderDetailsList = inFormOrderdetailReturn.getOrderDetailsList();
		
		for (int i = 0; i < orderDetailsList.size(); i++) {
			// 订单明细
			OrderDetailsBean orderDetailsInfo = new OrderDetailsBean();
			
			// 		order_number
			orderDetailsInfo.setOrderNumber(inFormOrderdetailReturn.getOrderNumber());
			// 		item_number
			orderDetailsInfo.setItemNumber(String.valueOf((itemNumber)));
			// 		adjustment
			orderDetailsInfo.setAdjustment(true);
			// 		product
			orderDetailsInfo.setProduct(product);
			//		sub_item_number
			orderDetailsInfo.setSubItemNumber(orderDetailsList.get(i).getItemNumber());
			// 		price_per_unit
			orderDetailsInfo.setPricePerUnit(orderDetailsList.get(i).getPricePerUnit());
			// 		quantity_ordered
			orderDetailsInfo.setQuantityOrdered("1");
			//		quantity_shipped
			orderDetailsInfo.setQuantityShipped("1");
			//		quantity_returned
			orderDetailsInfo.setQuantityReturned("0");
			
			//		resAllotFlg
			orderDetailsInfo.setResAllotFlg(true);
			//		syncSynship
			orderDetailsInfo.setSyncSynship(true);
			
			// 		sku
			orderDetailsInfo.setSku(OmsConstants.OrderDetailSkuDsp.DISCOUNT_TITLE);
			//		res_id
			orderDetailsInfo.setResId("0");
			// 		creater
			orderDetailsInfo.setCreater(user.getUserName());
			// 		modifier
			orderDetailsInfo.setModifier(user.getUserName());
			
			ret = orderDetailDao.insertOrderDetailsInfo(orderDetailsInfo);

			if (ret) {
				ret = insertExtTable(orderDetailsInfo, orderChannelId);
			}
			
			if (!ret) {
				break;
			}
			
			itemNumber = itemNumber + 1;
			
		}

		return ret;
	}
	
	/**
	 * 订单保存（订单修正）
	 * 
	 * @param orderPrice 订单价格
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrder(OrderPrice orderPrice, UserSessionBean user) {
		boolean ret = true;
		
		// 订单
		OrdersBean ordersInfo = setOrderPrice(orderPrice);		
		// 		modifier
		ordersInfo.setModifier(user.getUserName());
		
		ret = orderDetailDao.updateOrdersInfo(ordersInfo);
		
		return ret;
	}
	
	/**
	 * 订单Note保存
	 * 
	 * @param inFormOrderdetailReturn 输入调整信息
	 * @param ordersInfo 当前订单信息
	 * @param itemNumber 明细番号
	 * @param user 当前用户
	 *
	 * @return
	 */
	private boolean saveOrderNotes(InFormOrderdetailReturn inFormOrderdetailReturn, OutFormOrderdetailOrders ordersInfo, int itemNumber, UserSessionBean user) {
		boolean ret = true;
		
		List<OrderDetailsBean> orderDetailsList = inFormOrderdetailReturn.getOrderDetailsList();
		
		for (int i = 0; i < orderDetailsList.size(); i++) {
			// Insert Product sku notes取得
			String notes = "item #" + itemNumber + " " + OmsConstants.OrderDetailProductDsp.DISCOUNT_DJUSTMENT_TITLE + inFormOrderdetailReturn.getReason();
			
			String noteId = addNotes(inFormOrderdetailReturn.getSourceOrderId(),
										OmsConstants.NotesType.SYSTEM, 
										inFormOrderdetailReturn.getOrderNumber(),
										notes,
										user.getUserName(),
										user.getUserName());
			if (StringUtils.isEmpty(noteId)) {
				ret = false;
			}
			
			// 订单明细, debt, credit, noteId, user
			if (ret) {
//				ret = saveOrderTransactionsInfo(inFormOrderdetailReturn.getSourceOrderId(), inFormOrderdetailReturn.getOrderNumber(), OrderDetailSkuDsp.DISCOUNT_TITLE, "0", orderDetailsList.get(i).getPricePerUnit(), noteId, user);
				ret = saveOrderTransactionsInfo(inFormOrderdetailReturn.getSourceOrderId(),
												ordersInfo.getOriginSourceOrderId(),
												inFormOrderdetailReturn.getOrderNumber(),
												orderDetailsList.get(i).getSku(),
												String.valueOf(itemNumber),
												OrderDetailSkuDsp.DISCOUNT_TITLE,
												orderDetailsList.get(i).getPricePerUnit(), 
												noteId,
												OmsConstants.Transaction_Type.REFUND,
												false,
												user);
			}
			
			if (!ret) {
				break;
			}
			
			itemNumber = itemNumber + 1;
		}
		
		return ret;
	}
	
	/**
	 * 订单Transactions信息保存
	 * 
	 * @param sourceOrderId 网络订单号
	 * @param orderNumber 订单号
	 * @param description 描述
	 * @param debit 应收金额
	 * @param credit 应付金额 
	 * @param noteId　NoteId
	 * @param user 当前用户
	 *
	 * @return
	 */
	private boolean saveOrderTransactionsInfo(String sourceOrderId, String orderNumber, String description, String debit, String credit, String noteId, UserSessionBean user) {
		boolean ret = true;
		
		TransactionsBean transactionsInfo = new TransactionsBean();
		
		transactionsInfo.setSourceOrderId(sourceOrderId);
		//	订单号
		transactionsInfo.setOrderNumber(orderNumber);
		//  description
		transactionsInfo.setDescription(description);
		//	应收金额
		transactionsInfo.setDebit(debit);
		//	应付金额 
		transactionsInfo.setCredit(credit);
		//	noteId
		transactionsInfo.setNoteId(noteId);
		//	当前用户
		transactionsInfo.setCreater(user.getUserName());
		//	当前用户
		transactionsInfo.setModifier(user.getUserName());
		
		ret = transactionsDao.insertTransactionsInfo(transactionsInfo);
		if (!ret) {
			ret = false;
		}
		
		return ret;
	}
	
	/**
	 * 操作正常返回
	 * 
	 * @param sourceOrderId 网络订单号
	 * @param orderNumber 订单号
	 * @param user 当前用户
	 * 
	 * @return
	 */
	@Override
	public void setSuccessReturn(String sourceOrderId, String orderNumber, AjaxResponseBean result, UserSessionBean user) {
		// 订单刷新
		//		主订单信息取得
		OutFormOrderdetailOrders mainOrderInfo = getMainOrderInfo(sourceOrderId);
		//		订单信息返回
		OutFormOrderdetailOrders orderInfo = getOrdersInfo(orderNumber, user);
		//		订单Notes信息
		List<OutFormOrderdetailNotes> orderNotesList = getOrderNotesInfoBySourceOrderId(sourceOrderId, user);
		// 		订单Transaction信息
		List<OutFormOrderdetailTransactions> orderTransactionsList = getOrderTransactionsInfo(sourceOrderId, user);
		
		// 设置返回结果
		Map<String, Object> ordersListMap = new HashMap<String, Object>();
		// 		主订单
		setMainOrderInfo(mainOrderInfo, orderInfo, orderTransactionsList);
		ordersListMap.put("mainOrderInfo", mainOrderInfo);
		// 		订单
		ordersListMap.put("orderInfo", orderInfo);
		// 		订单NotesList
		ordersListMap.put("orderNotesList", orderNotesList);
		// 		订单TransactionList
		ordersListMap.put("orderTransactionsList", orderTransactionsList);
		
		result.setResultInfo(ordersListMap);
		
		//		正常
		result.setResult(true);
	}

	/**
	 * 操作正常返回（退款）
	 *
	 * @param sourceOrderId 网络订单号
	 * @param user 当前用户
	 *
	 * @return
	 */
	public void setSuccessReturnForRefund(String sourceOrderId, AjaxResponseBean result, UserSessionBean user) {
		// 订单刷新
		//		主订单信息取得
		OutFormOrderdetailOrders mainOrderInfo = getMainOrderInfo(sourceOrderId);
		//		订单Notes信息
		List<OutFormOrderdetailNotes> orderNotesList = getOrderNotesInfoBySourceOrderId(sourceOrderId, user);

		// 设置返回结果
		Map<String, Object> ordersListMap = new HashMap<String, Object>();
		// 		主订单
		setMainOrderInfoForRefund(mainOrderInfo);
		ordersListMap.put("mainOrderInfo", mainOrderInfo);
		// 		订单NotesList
		ordersListMap.put("orderNotesList", orderNotesList);

		result.setResultInfo(ordersListMap);

		//		正常
		result.setResult(true);
	}

	/**
	 * 操作正常返回（退款 独立域名）
	 *
	 * @param sourceOrderId 网络订单号
	 * @param user 当前用户
	 *
	 * @return
	 */
	public void setSuccessReturnForRefundCN(String sourceOrderId, AjaxResponseBean result, UserSessionBean user) {
		// 订单刷新
		//		主订单信息取得
		OutFormOrderdetailOrders mainOrderInfo = getMainOrderInfo(sourceOrderId);
		//		订单Notes信息
		List<OutFormOrderdetailNotes> orderNotesList = getOrderNotesInfoBySourceOrderId(sourceOrderId, user);
		//		订单Payments信息
		List<OutFormOrderdetailPayments> orderPaymentsList = getOrderPaymentsInfo(sourceOrderId, user);

		// 设置返回结果
		Map<String, Object> ordersListMap = new HashMap<String, Object>();
		// 		主订单
		setMainOrderInfoForRefund(mainOrderInfo);
		ordersListMap.put("mainOrderInfo", mainOrderInfo);
		// 		订单NotesList
		ordersListMap.put("orderNotesList", orderNotesList);
		//		订单paymentsList
		ordersListMap.put("orderPaymentsList", orderPaymentsList);

		result.setResultInfo(ordersListMap);

		//		正常
		result.setResult(true);
	}

	/**
	 * 设置主订单信息
	 *
	 * @param mainOrderInfo 主订单信息
	 * @return
	 */
	public void setMainOrderInfoForRefund(OutFormOrderdetailOrders mainOrderInfo) {

		mainOrderInfo.setCustomerRefund(mainOrderInfo.getRefundTotal());

		// payTitleValue
		float balancedue = sub2Digits(Float.valueOf(mainOrderInfo.getPaymentTotal()), Float.valueOf(mainOrderInfo.getExpected()));
		mainOrderInfo.setBalanceDue(String.valueOf(balancedue));
		// payTitleText
		String payTitleText = "";
		if (balancedue == 0) {
			payTitleText = Type.getTypeName(OmsCodeConstants.PayInfoType, OmsCodeConstants.PayInfo.PAY_IN_FULL, "en");
			// Credit Due
		} else if (balancedue > 0) {
			payTitleText = Type.getTypeName(OmsCodeConstants.PayInfoType, OmsCodeConstants.PayInfo.CREDIT_DUE, "en");
			// Blance Due
		} else if (balancedue < 0) {
			payTitleText = Type.getTypeName(OmsCodeConstants.PayInfoType, OmsCodeConstants.PayInfo.BALANCE_DUE, "en");
		}

		mainOrderInfo.setPayTitleText(payTitleText);

        // 是否存在退款履历
        boolean isHaveRefundHistory = isHaveRefunds(mainOrderInfo.getSourceOrderId(), true);
        mainOrderInfo.setIsHaveRefundHistory(isHaveRefundHistory);
	}
	
	/**
	 * 操作正常返回（金额未发生变化）
	 * 
	 * @param sourceOrderId 网络订单号
	 * @param orderNumber 订单号
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private void setSuccessReturnForPriceNoChange(String sourceOrderId, String orderNumber, AjaxResponseBean result, UserSessionBean user) {
		// 订单刷新
		//		订单信息返回
		OutFormOrderdetailOrders orderInfo = getOrdersInfo(orderNumber, user);
		//		订单Notes信息
		List<OutFormOrderdetailNotes> orderNotesList = getOrderNotesInfoBySourceOrderId(sourceOrderId, user);
		
		// 设置返回结果
		Map<String, Object> ordersListMap = new HashMap<String, Object>();

		// 		订单
		ordersListMap.put("orderInfo", orderInfo);
		// 		订单NotesList
		ordersListMap.put("orderNotesList", orderNotesList);
		
		result.setResultInfo(ordersListMap);
		
		//		正常
		result.setResult(true);
	}
	
	/**
	 * 差价订单绑定预处理
	 * 
	 * @param orderNumber 待处理订单号
	 * @param bindNumber 绑定订单号
	 * @param bindNumberKind 绑定订单号类型区分     "0"：(sourceOrderId)；"1"：(orderNumber)
	 * @param result 返回值
	 * @param user 当前用户
	 * 
	 * @return
	 */
	@Override
	public void preApprovePriceDiffOrderMain(String orderNumber, String bindNumber, String bindNumberKind, AjaxResponseBean result, UserSessionBean user) {

		// 被绑定订单信息
		OutFormOrderdetailOrders bindOrderInfo = null;
		
		// 绑定订单号类型区分
		if ("0".equals(bindNumberKind)) {
		//		"0"：(sourceOrderId)
			bindOrderInfo = orderDetailDao.getOrdersInfoBySourceOrderId(bindNumber);
		} else {
		//		"1"：(orderNumber)
			bindOrderInfo = orderDetailDao.getOrdersInfo(bindNumber);
		}

		// 		订单信息取得
		OutFormOrderdetailOrders ordersInfo = orderDetailDao.getOrdersInfo(orderNumber);

		//	操作许可检查
		if (approvePriceDiffOrderPermitChkMain(ordersInfo, bindOrderInfo, result)) {
		
			// 设置返回结果
			Map<String, Object> ordersListMap = new HashMap<String, Object>();		
			// 		被绑定订单
			ordersListMap.put("bindOrderInfo", bindOrderInfo);
			//		自身订单
			ordersListMap.put("orderInfo", ordersInfo);
			
			result.setResultInfo(ordersListMap);
			
			//		正常
			result.setResult(true);
		}
	}
	
	/**
	 * 差价订单绑定
	 * 
	 * @param orderNumber 待处理订单号
	 * @param bindNumber 绑定订单号
	 * @param bindNumberKind 绑定订单号类型区分     "0"：(sourceOrderId)；"1"：(orderNumber)
	 * @param result 返回值
	 * @param user 当前用户
	 * 
	 * @return
	 */
	@Override
	public void approvePriceDiffOrderMain(String orderNumber, String bindNumber, String bindNumberKind, AjaxResponseBean result, UserSessionBean user) {

		// 绑定订单信息
		OutFormOrderdetailOrders bindOrdersInfo = null;
		
		// 绑定订单号类型区分
		if ("0".equals(bindNumberKind)) {
		//		"0"：(sourceOrderId)
			bindOrdersInfo = orderDetailDao.getOrdersInfoBySourceOrderId(bindNumber);
		} else {
		//		"1"：(orderNumber)
			bindOrdersInfo = orderDetailDao.getOrdersInfo(bindNumber);
		}

		// 待处理订单信息
		// 		订单信息取得
		OutFormOrderdetailOrders ordersInfo = orderDetailDao.getOrdersInfo(orderNumber);

		//	操作许可检查
		if (approvePriceDiffOrderPermitChkMain(ordersInfo, bindOrdersInfo, result)) {
			
			// 待处理订单信息
			// 		订单详情取得
			List<OutFormOrderDetailOrderDetail> orderDetailsList = orderDetailDao.getOrderDetailsInfo(orderNumber);
			
			//	价差订单绑定
			if (approvePriceDiffOrder(ordersInfo, bindOrdersInfo, orderDetailsList, user)) {
				
				// 设置返回结果
				Map<String, Object> ordersListMap = new HashMap<String, Object>();			
				// 		订单
				ordersListMap.put("sourceOrderId", bindOrdersInfo.getSourceOrderId());
				
				result.setResultInfo(ordersListMap);
				
				//		正常
				result.setResult(true);
			} else {
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210038, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
		}
	}
	
	/**
	 * 差价订单绑定操作检查
	 * 
	 * @param ordersInfo 待处理单
	 * @param bindOrdersInfo 绑定订单信息
	 * @param result 返回值
	 * 
	 * @return
	 */
	private boolean approvePriceDiffOrderPermitChkMain(OutFormOrderdetailOrders ordersInfo, OutFormOrderdetailOrders bindOrdersInfo, AjaxResponseBean result) {
		boolean ret = true;
		
		//	被绑定订单不存在
		if (bindOrdersInfo == null) {
			
			ret = false;
			
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210039, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			
		//	价差订单不能绑定
		} else if(bindOrdersInfo.isPriceDifferenceFlag()) {

			ret = false;

			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210044, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);

//		//	未来订单绑定检查（待绑定订单号  < 被绑定订单号）
//		} else if (Double.valueOf(orderNumber) < Double.valueOf(bindOrdersInfo.getOrderNumber())) {
//
//			ret = false;
//
//			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210045, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
//		}
			//	下单人检查
		} else {
			if (!StringUtils.null2Space(ordersInfo.getName()).equals(bindOrdersInfo.getName()) ||
					!StringUtils.null2Space(ordersInfo.getPhone()).equals(bindOrdersInfo.getPhone())) {
				ret = false;

				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210068, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
		}

		return ret;
	}
	
	/**
	 * 差价订单绑定操作
	 * 
	 * @param ordersInfo 待处理订单信息
	 * @param bindOrdersInfo 绑定订单信息
	 * @param orderDetailsList 待处理订单明细信息
	 * @param user 当前用户
	 * 
	 * @return
	 */
	public boolean approvePriceDiffOrder(OutFormOrderdetailOrders ordersInfo,
										OutFormOrderdetailOrders bindOrdersInfo,
										List<OutFormOrderDetailOrderDetail> orderDetailsList,
										UserSessionBean user) {
		boolean ret = true;
		
		TransactionStatus status=transactionManager.getTransaction(def);
		
		try {
			// 价差订单金额
			OutFormOrderdetailOrders priceDiffOrderGroupInfo = orderDetailDao.getGroupOrdersInfo(ordersInfo.getSourceOrderId());

			// 订单信息更新
//			ret = savePriceDiffOrder(ordersInfo, bindOrdersInfo, user);
			ret = savePriceDiffUpdateDB(ordersInfo, bindOrdersInfo, user);
			
			// 一组订单价格更新（Expect +）
			if (ret) {
//				ret = saveGroupOrderPriceForBindPriceDiffOrder(bindOrdersInfo.getSourceOrderId(), ordersInfo.getFinalGrandTotal(), priceDiffOrderGroupInfo.getRefundTotal(), priceDiffOrderGroupInfo.getPaymentTotal(), user);
				ret = saveGroupOrderPriceForBindPriceDiffOrder(bindOrdersInfo.getSourceOrderId(), priceDiffOrderGroupInfo.getExpected(), priceDiffOrderGroupInfo.getRefundTotal(), priceDiffOrderGroupInfo.getPaymentTotal(), user);
			}
			
			// 订单Notes保存
			if (ret) {
				ret = saveOrderNotesForPriceDiffOrder(bindOrdersInfo.getSourceOrderId(), ordersInfo.getOrderNumber(), orderDetailsList, user);
			}
			
			if (ret) {
				logger.info("approvePriceDiffOrder success");

				transactionManager.commit(status);
			} else {
				logger.info("approvePriceDiffOrder error");

				transactionManager.rollback(status);
			}
			
		} catch (Exception e) {
			ret = false;
			
			logger.error("approvePriceDiffOrder", e);
			
			transactionManager.rollback(status);
		}
		
		return ret;
	}

	/**
	 * 差价订单绑定，表更新操作（Order表，Transaction表，Payment表，Refund表）
	 *
	 * @param ordersInfo 待处理订单信息
	 * @param bindOrdersInfo 绑定订单信息
	 * @param user 当前用户
	 *
	 * @return
	 */
	private boolean savePriceDiffUpdateDB(OutFormOrderdetailOrders ordersInfo,
										  OutFormOrderdetailOrders bindOrdersInfo,
										  UserSessionBean user) {
		boolean ret = true;

		// Order 表更新
		ret = savePriceDiffOrder(ordersInfo, bindOrdersInfo, user);

		// 价差订单transaction 表不发生变更　20150807
//		if (ret) {
//			// Transaction 表更新
//			ret = savePriceDiffOrderForTransaction(ordersInfo, bindOrdersInfo, user);
//		}

		if (ret) {
			// Payment 表更新
			ret = savePriceDiffOrderForPayment(ordersInfo, bindOrdersInfo, user);
		}

		if (ret) {
			// Refund 表更新
			ret = savePriceDiffOrderForRefund(ordersInfo, bindOrdersInfo, user);
		}

		return ret;
	}

	/**
	 * 差价订单保存（Order 表）
	 * 
	 * @param ordersInfo 待处理订单信息
	 * @param bindOrdersInfo 绑定订单信息
	 * @param user 当前用户
	 *
	 */
	private boolean savePriceDiffOrder(OutFormOrderdetailOrders ordersInfo,
										OutFormOrderdetailOrders bindOrdersInfo,
										UserSessionBean user) {
		boolean ret = true;
		
		OrdersBean bean = new OrdersBean();
		//	订单号
		bean.setOrderNumber(ordersInfo.getOrderNumber());
		//	绑定订单号
		bean.setSourceOrderId(bindOrdersInfo.getSourceOrderId());
		//	原始订单号
		bean.setOriginSourceOrderId(ordersInfo.getSourceOrderId());
		//	approved 标志位
		bean.setApproved(true);
		//  订单状态
		bean.setOrderStatus(OmsCodeConstants.OrderStatus.APPROVED);
		//	订单类型
		bean.setOrderKind(OmsCodeConstants.OrderKind.PRICE_DIFF_ORDER);
		//	当前用户
		bean.setModifier(user.getUserName());
		
		ret = orderDetailDao.updateOrdersInfoForBindPriceDiffOrder(bean);
		
		return ret;
	}

	private boolean savePriceDiffOrderForTransaction(OutFormOrderdetailOrders ordersInfo,
													 OutFormOrderdetailOrders bindOrdersInfo,
													 UserSessionBean user) {
		boolean ret = true;

		TransactionsBean bean = new TransactionsBean();
		//	订单号
		bean.setOrderNumber(ordersInfo.getOrderNumber());
		//	绑定订单号
		bean.setSourceOrderId(bindOrdersInfo.getSourceOrderId());
		//	原始订单号
		bean.setOriginSourceOrderId(ordersInfo.getSourceOrderId());
		//	当前用户
		bean.setModifier(user.getUserName());

		ret = transactionsDao.updateTransactionsInfoForBindPriceDiffOrder(bean);

		return ret;
	}

	private boolean savePriceDiffOrderForPayment(OutFormOrderdetailOrders ordersInfo,
													 OutFormOrderdetailOrders bindOrdersInfo,
													 UserSessionBean user) {
		boolean ret = true;

		OrderPaymentsBean bean = new OrderPaymentsBean();
		//	订单号
		bean.setOrderNumber(ordersInfo.getOrderNumber());
		//	绑定订单号
		bean.setSourceOrderId(bindOrdersInfo.getSourceOrderId());
		//	原始订单号
		bean.setOriginSourceOrderId(ordersInfo.getSourceOrderId());
		//	当前用户
		bean.setModifier(user.getUserName());

		ret = paymentsDao.updatePaymentsInfoForBindPriceDiffOrder(bean);

		return ret;
	}

	private boolean savePriceDiffOrderForRefund(OutFormOrderdetailOrders ordersInfo,
												 OutFormOrderdetailOrders bindOrdersInfo,
												 UserSessionBean user) {
		boolean ret = true;

		OrderRefundsBean bean = new OrderRefundsBean();

		//	绑定订单号
		bean.setSourceOrderId(bindOrdersInfo.getSourceOrderId());
		//	原始订单号
		bean.setOriginSourceOrderId(ordersInfo.getSourceOrderId());
		//	当前用户
		bean.setModifier(user.getUserName());

		ret = refundDao.updateRefundsInfoForBindPriceDiffOrder(bean);

		return ret;
	}
	
	/**
	 * 一组订单Expected保存
	 * 
	 * @param sourceOrderId 网络订单号
	 * @param expected 
	 * @param user 当前用户
	 *
	 */
	private boolean saveGroupOrderPriceForBindPriceDiffOrder(String sourceOrderId, String expected, String refundTotal, String paymentTotal, UserSessionBean user){
		boolean ret = true;
		
		// 一组订单
		//		订单价格取得
		OrdersBean ordersInfo = new OrdersBean();
		//		sourceOrderId
		ordersInfo.setSourceOrderId(sourceOrderId);
		// 		expected
		ordersInfo.setExpected(expected);
		//		退款金额
		ordersInfo.setRefund(refundTotal);
		//		支付金额
		ordersInfo.setPaymentTotal(paymentTotal);
		// 		modifier
		ordersInfo.setModifier(user.getUserName());
		
		ret = orderDetailDao.updateGroupOrdersInfoForBindPriceDiffOrder(ordersInfo);
		
		return ret;
	}
	
	/**
	 * 订单Note保存（差价订单）
	 * 
	 * @param sourceOrderId 网络订单号
	 * @param orderNumber 订单号
	 * @param orderDetailsList 订单明细
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean saveOrderNotesForPriceDiffOrder(String sourceOrderId,
													String orderNumber,
													List<OutFormOrderDetailOrderDetail> orderDetailsList,
													UserSessionBean user) {
		boolean ret = true;		

		// 明细行番号
		int itemNumber = orderDetailDao.getOrderDetailsNotesMaxItemNumber(orderNumber) + 1;		

		String notes = OmsConstants.PRICE_DIFFERENCE_ORDER_APPROVED;
				
		//	订单更新Notes
		//	订单Notes
		NotesBean notesInfo = new NotesBean();
		// 		sourceOrderId
		notesInfo.setSourceOrderId(sourceOrderId);
		//		type
		notesInfo.setType(OmsConstants.NotesType.SYSTEM);
		// 		order_number
		notesInfo.setNumericKey(orderNumber);
		//		item_number
		notesInfo.setItemNumber(String.valueOf(itemNumber));
		//		notes
		notesInfo.setNotes(notes);
		//		entered_by
		notesInfo.setEnteredBy(user.getUserName());
		//		Creater
		notesInfo.setCreater(user.getUserName());
		//		modifier
		notesInfo.setModifier(user.getUserName());
		
		ret = notesDao.insertNotesInfo(notesInfo);

		// 差价定单 origin_source_order_id 对应 此处删除 20150722
//		if (ret) {
//			// 价差Description
//			String description = OrderDetailSkuDsp.PRICE_DIFF_TITLE;
//
//			//	价差订单，item_number 始终为1
//			ret = saveOrderTransactionsInfo(sourceOrderId,
//											orderNumber,
//											description,
//											"1",
//											description,
//											orderDetailsList.get(0).getPricePerUnit(),
//											notesInfo.getId(),
//											OmsConstants.Transaction_Type.REFUND,
//											false,
//											user);
//		}
		
		return ret;
	}
	
	/**
	 * 判定是否国内Cart
	 * 
	 * @param cartId cartId
	 * 
	 * @return
	 */ 
	private boolean isCNCarts(String cartId) {
		boolean ret = false;
		
		if (CN_CARTIDS == null) {
			CN_CARTIDS = cartDao.getCartsListByType(OmsConstants.CartType.CART_CN);
		}
		
		if (CN_CARTIDS != null) {
			for (int i = 0; i < CN_CARTIDS.size(); i++) {
				if (CN_CARTIDS.get(i).getCartId().equals(cartId)) {
					ret = true;
					break;
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * 订单退款信息取得主函数
	 * 
	 * @param sourceOrderId 网络订单号
     * @param cartId
     * @param isShowHistoryOnly 仅显示履历
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return
	 */
	@Override
	public void getOrderRefundsMain(String sourceOrderId, String cartId, boolean isShowHistoryOnly, AjaxResponseBean result, UserSessionBean user) {

        // 阿里的场合
		if (OmsConstants.Platform.ALI.equals(getCartInfo(cartId).getPlatformId())) {
			//	订单退款信息取得（阿里）
			getOrderRefunds(sourceOrderId, isShowHistoryOnly, result, user);
        // 其他的场合
		} else {
			// 订单退款信息取得函数（阿里以外）
			getOrderRefundsforOther(sourceOrderId, isShowHistoryOnly, result, user);
		}

			
	}
	
	/**
	 * 订单退款信息取得函数（阿里）
	 * 
	 * @param sourceOrderId 网络订单号
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean getOrderRefunds(String sourceOrderId, boolean isShowHistoryOnly, AjaxResponseBean result, UserSessionBean user) {
		
		boolean ret = true;
		
		try {
			// 订单退款历史信息取得
            List<OutFormOrderdetailRefunds> orderRefundsList = getRefundsList(sourceOrderId, isShowHistoryOnly, user);
			
			// 当前退款信息
			RefundGetResponse refundGetResponse = null;
			
			// 订单退款状态
			OutFormOrderdetailRefundsStatus refundsStatus = null;
			
			// 订单退款信息
			OutFormOrderdetailRefunds refundInfo = null;
			
			// 订单退款信息取得
			List<RefundMessage> orderRefundsMessagesList = new ArrayList<RefundMessage>();
			if (orderRefundsList.size() > 0) {
				orderRefundsMessagesList = getRefundMessagesList(orderRefundsList.get(0), result);
				
				if (orderRefundsMessagesList == null) {
					ret = false;
				}
				
				// 当前退款信息查询
				if (ret) {					
					refundGetResponse = getRefund(orderRefundsList.get(0));
					
					if (refundGetResponse == null) {
						ret = false;
						
						result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210048, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					} else {
						if (refundGetResponse.getErrorCode() != null) {
							ret = false;
							
							String errorMsg = String.format(OmsMessageConstants.MessageContent.REFUND_GET_ERROR, refundGetResponse.getErrorCode(), refundGetResponse.getMsg(), refundGetResponse.getSubMsg());
							result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, errorMsg);
						}
					}
				}
				
				// 退款信息设定
				if (ret) {
					//	DB退款信息
					OutFormOrderdetailRefunds dbRefundInfo = orderRefundsList.get(0);
					//	画面返回退款信息
					refundInfo = getRefundInfo(dbRefundInfo);
					setRefundsTmallInfo(refundInfo, refundGetResponse.getRefund());
				}
				
				// 订单退款状态设定
				if (ret) {
					refundsStatus = getRefundsStatus(orderRefundsList.get(0).getRefundId(), refundGetResponse.getRefund());
				}
			}
			
			// 天猫接口调用正常的场合
			if (ret) {
				// 设置返回结果
				Map<String, Object> ordersRefundMap = new HashMap<String, Object>();		
				//		订单退款历史信息设置
				ordersRefundMap.put("orderRefundsList", orderRefundsList);
				// 		退款信息
				ordersRefundMap.put("refundInfo", refundInfo);
				//		订单退款信息设置
				ordersRefundMap.put("refundMessagesList", orderRefundsMessagesList);
				//		订单是否发货
				ordersRefundMap.put("refundsStatus", refundsStatus);
				
				result.setResultInfo(ordersRefundMap);
				
				//		正常
				result.setResult(true);
			}
		} catch (ApiException e) {
			ret = false;
			
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210048, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					
			logger.error("getOrderRefunds", e);
			
		} catch (Exception e) {
			
			ret = false;
			
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210046, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					
			logger.error("getOrderRefunds", e);
		}
		
		return ret;
	}

	/**
	 * 订单退款信息取得函数（阿里以外）
	 *
	 * @param sourceOrderId 网络订单号
	 * @param result 返回结果
	 * @param user 当前用户
	 *
	 * @return
	 */
	private boolean getOrderRefundsforOther(String sourceOrderId, boolean isShowHistoryOnly, AjaxResponseBean result, UserSessionBean user) {

		boolean ret = true;

		try {
			// 订单退款历史信息取得
			List<OutFormOrderdetailRefunds> orderRefundsList = getRefundsList(sourceOrderId, isShowHistoryOnly, user);

			// 订单退款信息取得
			List<RefundMessage> orderRefundsMessagesList = new ArrayList<RefundMessage>();

			// 订单退款状态
			OutFormOrderdetailRefundsStatus refundsStatus = null;

			// 订单退款信息
			OutFormOrderdetailRefunds refundInfo = null;

			if (orderRefundsList.size() > 0) {

				//	DB退款信息
				OutFormOrderdetailRefunds dbRefundInfo = orderRefundsList.get(0);
				//	画面返回退款信息
				refundInfo = getRefundInfo(dbRefundInfo);

				// 订单退款状态设定
				if (ret) {
					refundsStatus = new OutFormOrderdetailRefundsStatus();
                    //  是否处理结束
                    refundsStatus.setIsEndProcess(refundInfo.isProcessFlag());
				}
			}

			// 天猫接口调用正常的场合
			if (ret) {
				// 设置返回结果
				Map<String, Object> ordersRefundMap = new HashMap<String, Object>();
				//		订单退款历史信息设置
				ordersRefundMap.put("orderRefundsList", orderRefundsList);
				// 		退款信息
				ordersRefundMap.put("refundInfo", refundInfo);
				//		订单退款信息设置
				ordersRefundMap.put("refundMessagesList", orderRefundsMessagesList);
				//		订单是否发货
				ordersRefundMap.put("refundsStatus", refundsStatus);

				result.setResultInfo(ordersRefundMap);

				//		正常
				result.setResult(true);
			}
		} catch (Exception e) {

			ret = false;

			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210046, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);

			logger.error("getOrderRefunds", e);
		}

		return ret;
	}

	/**
	 * 订单退款信息设定
	 * 
	 * @param refundsInfo 本地订单退款信息
	 * @param tmallRefundInfo 天猫退款信息
	 * 
	 * @return
	 */
	private void setRefundsTmallInfo(OutFormOrderdetailRefunds refundsInfo, Refund tmallRefundInfo) {
		
		//	退款状态
		String refundStatus = Type.getTypeName(OmsCodeConstants.TMALL_REFUND_STATUS, tmallRefundInfo.getStatus(), "cn");
		refundsInfo.setRefundStatus(refundStatus);
		
		//	退款类型Code
		String refundKindCode = "";
		//	退款类型
		String refundKind = "";

		//	退款类型Code赋值
		if (tmallRefundInfo.getHasGoodReturn()) {
			refundKindCode = OmsCodeConstants.TmallRefundKindInfo.HAS_GOODS_RETURN_YES;
		} else {
			refundKindCode = OmsCodeConstants.TmallRefundKindInfo.HAS_GOODS_RETURN_NO;
		}

		//	退款类型赋值
		refundKind = Type.getTypeName(OmsCodeConstants.TMALL_REFUND_KIND, refundKindCode, "cn");
		refundsInfo.setRefundKind(refundKind);
		
		//	退款原因
		refundsInfo.setRefundReason(tmallRefundInfo.getReason());
		
		//	退款说明
		refundsInfo.setRefundComment(tmallRefundInfo.getDesc());
	}
	
	/**
	 * 订单退款历史信息取得
	 * 
	 * @param sourceOrderId 网络订单号
	 * @param isShowHistoryOnly 是否仅显示历史记录
	 * @param user 当前用户
	 * @return
	 */
	public List<OutFormOrderdetailRefunds> getRefundsList(String sourceOrderId, boolean isShowHistoryOnly, UserSessionBean user) {
        List<OutFormOrderdetailRefunds> ret = null;
		// DB数据取得
            // 仅历史退款记录
        if (isShowHistoryOnly) {
            ret = refundDao.getOrderRefundsListByProcesFlag(sourceOrderId, true);
            // 全体退款记录
        } else {
            ret = refundDao.getOrderRefundsListBySourceOrderId(sourceOrderId);
        }
		
		// 金额符号设定
		if (ret.size() > 0) {
			for (int i = 0; i < ret.size(); i++) {
				OutFormOrderdetailRefunds refundInfo = ret.get(i);

				// 金额符号
				if (isCNCarts(refundInfo.getCartId())) { 
					refundInfo.setCurrencyType(OmsConstants.CurrencyType.RMB);
				} else {
					refundInfo.setCurrencyType(OmsConstants.CurrencyType.DOLLAR);
				}

				// 申请时间
				//		用户时区
				int timezone = user.getTimeZone();
				//		秒去除
				String refundTime = StringUtils.trimDBDateTimeMs(refundInfo.getRefundTime());
				//		本地时间转化
				refundTime = DateTimeUtil.getLocalTime(DateTimeUtil.parseStr(refundTime, DateTimeUtil.DEFAULT_DATETIME_FORMAT), timezone);
				refundInfo.setRefundTime(refundTime);
			}
		}
		
		return ret;
	}
	
	/**
	 * 订单退款信息取得
	 * 
	 * @param bean 退款信息
	 * @param result 结果返回
	 * @return　退款留言/凭证列表
	 * @throws ApiException 
	 */
	public List<RefundMessage> getRefundMessagesList(OutFormOrderdetailRefunds bean, AjaxResponseBean result) throws ApiException {
				
		List<RefundMessage> retBuff = new ArrayList<RefundMessage>();
		
		boolean retTmall = true;
		
		RefundMessagesGetResponse tmallResponse = null;
		
		String fields = "id,refund_id,owner_id,owner_nick,content,pic_urls,created,message_type,refund_phase,owner_role";
		long pageNo = 1L;
		long pageSize = OmsConstants.GET_TMALL_REFUND_MESSAGES_PAGESIZE;
		
		while (true) {
			tmallResponse = callTmallApiGetRefundMessages(bean, pageNo, pageSize, fields);
			
			if (tmallResponse.getErrorCode() == null) {
				if (tmallResponse.getRefundMessages().size() > 0) {
					
//					// Tmall故障 重复取出对应
//					if (isBufferedMessage(retBuff, tmallResponse.getRefundMessages().get(0))) {
//						break;
//					}
					
					retBuff.addAll(tmallResponse.getRefundMessages());
					
					if (retBuff.size() >= Long.valueOf(tmallResponse.getTotalResults()).longValue()) {
						break;
					}
				} else {
					break;
				}
			//	返回Code 异常
			} else {
				retTmall = false;
				break;
			}
			
			pageNo = pageNo + 1;
		}
		
		if (retTmall) {
			for (int i = 0; i < retBuff.size(); i++) {
				
				RefundMessage retItem = retBuff.get(i);
				
				//	created 北京时间转化
				//		Taobao Created 默认本地时间（部署环境RackSpace 默认GMT时间），转成北京时间 与content时间 保持一致。
				String localDateTimeStr = DateTimeUtil.getLocalTime(getFormatedCreated(retItem.getCreated()), 8);
				Date localDateTime = DateTimeUtil.parse(localDateTimeStr, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
				retItem.setCreated(localDateTime);
				
				//	conent \n -> <br> 变化
				String content = retItem.getContent();
                if (!StringUtils.isEmpty(content)) {
                    retItem.setContent(content.replace("\n", "<br />"));
                }
			}
		} else {
			// todo throw bussiness Exception
			logger.info("RefundMessagesGetRequest error");
			logger.info("	ErrorCode = " + tmallResponse.getErrorCode());
			logger.info("	Msg = " + tmallResponse.getMsg());
			logger.info("	SubMsg = " + tmallResponse.getSubMsg());
			
			String errorMsg = String.format(OmsMessageConstants.MessageContent.REFUND_MESSAGES_GET_ERROR, tmallResponse.getErrorCode(), tmallResponse.getMsg(), tmallResponse.getSubMsg());
			result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, errorMsg);
			
			retBuff = null;
		}
		
		return retBuff;
	}

	/**
	 * 天猫退款信息时间格式化
	 * 
	 * @param tmallCreated 天猫时间
	 * @return
	 */
	private String getFormatedCreated(Date tmallCreated) {
		String formatedTmallCreated = DateTimeUtil.format(tmallCreated, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
		return formatedTmallCreated;
	}

	
	/**
	 * Message是否已被取出
	 * 
	 * @param messageList 缓存信息
	 * @param newMessage 新信息
	 * 
	 * @return
	 */
	private boolean isBufferedMessage(List<RefundMessage> messageList, RefundMessage newMessage) {
		boolean ret = false;
		
		for (int i = 0; i < messageList.size(); i++) {
			if (messageList.get(i).getId().longValue() == newMessage.getId().longValue()) {
				ret = true;
				break;
			}
		}
		
		return ret;
	}
	
	/**
	 * 订单退款Message信息取得主函数
	 *
	 * @param bean 退款信息
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return
	 */
	@Override
	public void getOrderRefundMessagesMain(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {

        if (OmsConstants.Platform.ALI.equals(getCartInfo(bean.getCartId()).getPlatformId())) {
            getOrderRefundMessages(bean, result, user);
        } else {
            getOrderRefundMessagesForOther(bean, result, user);
        }
	}

	/**
	 * 订单退款Message信息取得函数（阿里）
	 *
	 * @param bean 退款信息
	 * @param result 返回结果
	 * @param user 当前用户
	 *
	 * @return
	 */
	private boolean getOrderRefundMessages(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {
		boolean ret = true;
		
		try {
			// 当前退款信息
			RefundGetResponse refundGetResponse = null;
			
			// 订单退款状态
			OutFormOrderdetailRefundsStatus refundsStatus = null;
			
			// 订单退款信息取得
			List<RefundMessage> orderRefundsMessagesList = getRefundMessagesList(bean, result);
				
			// 当前退款信息查询
			if (ret) {
				refundGetResponse = getRefund(bean);
				if (refundGetResponse.getErrorCode() != null) {
					ret = false;
					
					String errorMsg = String.format(OmsMessageConstants.MessageContent.REFUND_GET_ERROR, refundGetResponse.getErrorCode(), refundGetResponse.getMsg(), refundGetResponse.getSubMsg());
					result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, errorMsg);
				}
			}
			
			// 退款信息
			OutFormOrderdetailRefunds refundInfo = null;
			if (ret) {
				refundInfo = getRefundInfo(bean);
				setRefundsTmallInfo(refundInfo, refundGetResponse.getRefund());
			}
			
			// 订单退款状态设定
			if (ret) {
				refundsStatus = getRefundsStatus(bean.getRefundId(), refundGetResponse.getRefund());
			}
			
			// 设置返回结果
			if (ret) {
				Map<String, Object> ordersRefundMap = new HashMap<String, Object>();
				
				//		订单退款信息
				ordersRefundMap.put("refundInfo", refundInfo);
				//		订单退款信息设置
				ordersRefundMap.put("refundMessagesList", orderRefundsMessagesList);
				//		订单是否发货
				ordersRefundMap.put("refundsStatus", refundsStatus);
				
				result.setResultInfo(ordersRefundMap);
				
				//		正常
				result.setResult(true);
			}
		} catch (ApiException e) {
			ret = false;

			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210048, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);

			logger.error("getOrderRefundMessages", e);
			
		} catch (Exception e) {
			
			ret = false;
			
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210046, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			
			logger.error("getOrderRefundMessages", e);
		}
		
		return ret;
	}

    /**
     * 订单退款Message信息取得函数（阿里以外）
     *
     * @param bean 退款信息
     * @param result 返回结果
     * @param user 当前用户
     *
     * @return
     */
    private boolean getOrderRefundMessagesForOther(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {
        boolean ret = true;

        try {
            // 当前退款信息
            RefundGetResponse refundGetResponse = null;

            // 订单退款信息取得
            List<RefundMessage> orderRefundsMessagesList = new ArrayList<RefundMessage>();

            // 退款信息
            OutFormOrderdetailRefunds refundInfo = getRefundInfo(bean);

            // 订单退款状态
            OutFormOrderdetailRefundsStatus refundsStatus = new OutFormOrderdetailRefundsStatus();
            refundsStatus.setIsEndProcess(bean.isProcessFlag());

            // 设置返回结果
            if (ret) {
                Map<String, Object> ordersRefundMap = new HashMap<String, Object>();

                //		订单退款信息
                ordersRefundMap.put("refundInfo", refundInfo);
                //		订单退款信息设置
                ordersRefundMap.put("refundMessagesList", orderRefundsMessagesList);
                //		订单是否发货
                ordersRefundMap.put("refundsStatus", refundsStatus);

                result.setResultInfo(ordersRefundMap);

                //		正常
                result.setResult(true);
            }

        } catch (Exception e) {

            ret = false;

            result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210046, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);

            logger.error("getOrderRefundMessagesForOther", e);
        }

        return ret;
    }

	/**
	 * 订单退款Bean 生成
	 * 
	 * @param dbRefundInfo DB保存退款信息
	 *
	 * @return　生成后Bean
	 */
	private OutFormOrderdetailRefunds getRefundInfo(OutFormOrderdetailRefunds dbRefundInfo) {
		OutFormOrderdetailRefunds refundInfo = new OutFormOrderdetailRefunds();
		
		refundInfo.setSourceOrderId(dbRefundInfo.getSourceOrderId());
		refundInfo.setOrderChannelId(dbRefundInfo.getOrderChannelId());
		refundInfo.setCartId(dbRefundInfo.getCartId());
		refundInfo.setRefundId(dbRefundInfo.getRefundId());
		refundInfo.setRefundPhase(dbRefundInfo.getRefundPhase());
		refundInfo.setRefundFee(dbRefundInfo.getRefundFee());
		refundInfo.setRefundTime(dbRefundInfo.getRefundTime());
		refundInfo.setProcessFlag(dbRefundInfo.isProcessFlag());
		refundInfo.setCurrencyType(dbRefundInfo.getCurrencyType());
		
		return refundInfo;
	}
	
	/**
	 * 订单退款Message信息追加主函数
	 * 
	 * @param refundId 退款编号
	 * @param content 留言内容
	 * @param image 图片
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return
	 */
	@Override
	public void addOrderRefundMessageMain(String refundId, String content, String image, AjaxResponseBean result, UserSessionBean user) {
		
		if (!addOrderRefundMessages(refundId, content, image, result, user)) {
			
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210047, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}
	}
	
	/**
	 * 订单退款Message信息取得函数
	 * 
	 * @param refundId 退款编号
	 * @param content 留言内容
	 * @param image 图片
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean addOrderRefundMessages(String refundId, String content, String image, AjaxResponseBean result, UserSessionBean user) {
		boolean ret = true;
		
		try {
			// 订单退款信息取得
			RefundMessageAddResponse tmallResponse = callTmallApiAddRefundMessages(Long.valueOf(refundId), content, "D:\\jiming\\work\\test\\test.jpg");
			
			RefundMessage orderRefundsMessage = null;
			
			if (tmallResponse.getErrorCode() != null) {
				orderRefundsMessage = tmallResponse.getRefundMessage();
			}
			
			// 设置返回结果
			Map<String, Object> ordersRefundMap = new HashMap<String, Object>();
		
			//		订单退款信息设置
			ordersRefundMap.put("refundMessage", orderRefundsMessage);
			
			result.setResultInfo(ordersRefundMap);
			
			//		正常
			result.setResult(true);
		} catch (Exception e) {
			
			ret = false;
			
			logger.error("addOrderRefundMessages", e);
		}
		
		return ret;
	}
	
	/**
	 * 拒绝订单退款主函数
	 * 
	 * @param bean 退款信息
	 * @param result 返回结果
	 * @param user 当前用户
	 *
	 * @return
	 */
	@Override
	public void refundRefuseMain(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {

		//	阿里的场合
		if (OmsConstants.Platform.ALI.equals(getCartInfo(bean.getCartId()).getPlatformId())) {
			if (refundRefuse(bean, result, user)) {
				// notes追加
				String notes = OmsConstants.REFUND_REFUSE;
				addNotes(bean.getSourceOrderId(),
						OmsConstants.NotesType.SYSTEM,
						bean.getOrderNumber(),
						notes,
						user.getUserName(),
						user.getUserName());

				setSuccessReturnForRefund(bean.getSourceOrderId(), result, user);
			}
		//	其他的场合
		} else {
			if (refundRefuseForOther(bean, result, user)) {
				// notes追加
				String notes = OmsConstants.REFUND_REFUSE;
				addNotes(bean.getSourceOrderId(),
						OmsConstants.NotesType.SYSTEM,
						bean.getOrderNumber(),
						notes,
						user.getUserName(),
						user.getUserName());

				setSuccessReturnForRefund(bean.getSourceOrderId(), result, user);
			}
		}
	}

	private boolean refundRefuseForOther(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {
		boolean ret = true;

		TransactionStatus status=transactionManager.getTransaction(def);

		try {

			// 一组订单价格更新（refundTotal -）
			logger.info("saveGroupOrderPriceForRefundRefuse");
			ret = saveGroupOrderPriceForRefundRefuse(bean.getSourceOrderId(), bean.getRefundFee(), user);

			if (ret) {
				// 退款处理标志位更新
				logger.info("updateProcessFlag");
				ret = updateRefundProcessFlag(bean.getRefundId(), OmsCodeConstants.RefundStatus.CLOSE, user);
			}

			if (ret) {
				logger.info("refundRefuseForOther success");

				transactionManager.commit(status);
			} else {
				logger.info("refundRefuseForOther error");

				transactionManager.rollback(status);
			}

		} catch (Exception e) {
			ret = false;

			logger.error("refundRefuseForOther", e);

			transactionManager.rollback(status);
		}

		return ret;
	}

	/**
	 * 拒绝订单退款
	 *
	 * @param bean 退款信息
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean refundRefuse(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {
		boolean ret = true;

		logger.info("refundRefuse Start");
		
		// 上传文件本地保存
		String uploadFileName = "";
		
		try {
			// 当前退款信息查询
			RefundGetResponse refundInfo = getRefund(bean);
			if (refundInfo.getErrorCode() != null) {
				ret = false;
				
				String errorMsg = String.format(OmsMessageConstants.MessageContent.REFUND_GET_ERROR, refundInfo.getErrorCode(), refundInfo.getMsg(), refundInfo.getSubMsg());
				result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, errorMsg);
			}

			if (ret) {
				// 上传文件缓存
				uploadFileName = uploadRefundFile(bean.getRefundId(), bean.getImage());
				
				if (StringUtils.isEmpty(uploadFileName)) {
					ret = false;
					result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210054, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
				}
			}
			
			// Call天猫接口（拒绝退款）
			if (ret) {
				RefundRefuseResponse tmallResponse = callTmallApiRefundRefuse(bean, refundInfo.getRefund().getRefundVersion(), uploadFileName);

				// 异常Code返回
				if (tmallResponse.getErrorCode() != null) {
					ret = false;

					String errorMsg = String.format(OmsMessageConstants.MessageContent.REFUND_REFUSE_ERROR, tmallResponse.getErrorCode(), tmallResponse.getMsg(), tmallResponse.getSubMsg());
					result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, errorMsg);
				// 返回值判定
				} else  {
					if (!tmallResponse.getIsSuccess()) {
						ret = false;
						result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210047, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					}
				}

				logger.info("RefundRefuseResponse = " + JsonUtil.getJsonString(tmallResponse));
			}
			logger.info("refundRefuse end");
		} catch (ApiException e) {
			ret = false;

			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210048, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);

			logger.error("refundRefuse", e);
			
		} catch (Exception ex) {
			ret = false;
			
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210047, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			
			logger.error("refundRefuse", ex);
			
		} finally {
			if (!StringUtils.isEmpty(uploadFileName)) {
				FileUtil.deleteContents(new File(uploadFileName));
			}	
		}
		
		return ret;
	}
	
	/**
	 * 同意订单退款主函数
	 * 
	 * @param bean 画面输入信息
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return
	 */
	@Override
	public void refundsAgreeMain(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {

//		//	阿里的场合
//        if (OmsConstants.Platform.ALI.equals(getCartInfo(bean.getCartId()).getPlatformId())) {
//			refundsAgreeMainForAli(bean, result, user);
//		// 独立域名的场合
//		} else if(OmsConstants.Platform.CN.equals(getCartInfo(bean.getCartId()).getPlatformId())) {
//			refundsAgreeMainForCN(bean, result, user);
//		//	其他的场合
//        } else {
//            refundsAgreeMainForOther(bean, result, user);
//        }

//		//	阿里的场合
//		if (OmsConstants.Platform.ALI.equals(getCartInfo(bean.getCartId()).getPlatformId())) {
//			refundsAgreeMainForAli(bean, result, user);
//			// 独立域名的场合
//		} else {
//			refundsAgreeMainForCN(bean, result, user);
//		}
		// URL 分开对应
		refundsAgreeMainForAli(bean, result, user);
	}

	/**
	 * 同意订单退款主函数（同步OMS）
	 *
	 * @param bean 画面输入信息
	 * @param result 返回结果
	 * @param user 当前用户
	 *
	 * @return
	 */
	@Override
	public void refundsAgreeMainSynOMS(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {

		// URL 分开对应
		refundsAgreeMainForSynOMS(bean, result, user);
	}

	/**
	 * 同意订单退款主函数(独立域名)
	 *
	 * @param bean 画面输入信息
	 * @param result 返回结果
	 * @param user 当前用户
	 *
	 * @return
	 */
	@Override
	public void refundsAgreeMainCN(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {

//		//	阿里的场合
//        if (OmsConstants.Platform.ALI.equals(getCartInfo(bean.getCartId()).getPlatformId())) {
//			refundsAgreeMainForAli(bean, result, user);
//		// 独立域名的场合
//		} else if(OmsConstants.Platform.CN.equals(getCartInfo(bean.getCartId()).getPlatformId())) {
//			refundsAgreeMainForCN(bean, result, user);
//		//	其他的场合
//        } else {
//            refundsAgreeMainForOther(bean, result, user);
//        }

//		//	阿里的场合
//		if (OmsConstants.Platform.ALI.equals(getCartInfo(bean.getCartId()).getPlatformId())) {
//			refundsAgreeMainForAli(bean, result, user);
//			// 独立域名的场合
//		} else {
//			refundsAgreeMainForCN(bean, result, user);
//		}
		// URL 分开对应
		refundsAgreeMainForCN(bean, result, user);
	}

    /**
     * 同意订单退款主函数（阿里）
     *
     * @param bean 画面输入信息
     * @param result 返回结果
     * @param user 当前用户
     *
     * @return
     */
    private void  refundsAgreeMainForAli(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {
        logger.info("refundsAgreeMain start");
        logger.info("	refund_id = " + bean.getRefundId());
        logger.info("	refund_phase = " + bean.getRefundPhase());

        // 验证码预处理
        if (StringUtils.isEmpty(bean.getCode())) {
            bean.setCode("");;
        }

        //	同意订单退款天猫调用
        List refundsAgreeRet = refundsAgree (bean, result, user);

        //	list[0] 调用结果
        //	list[1] code返回
        boolean refundsAgreeResult = (Boolean)refundsAgreeRet.get(0);
        String  refundsAgreeResultRetCode = (String)refundsAgreeRet.get(1);

        if (refundsAgreeResult) {
            // 发送二次验证短信成功 的场合
            if (OmsConstants.TmallRefundAgreeMsgCode.MSG_CODE_10000.equals(refundsAgreeResultRetCode)) {
                //	正常结果返回
                setRefundsAgreeSuccessRet(refundsAgreeResultRetCode, result);
                // 天猫退款操作成功
            } else {
                logger.info("	天猫退款成功 notes追加");
                // 天猫退款成功 notes追加
                String noteId = addNotes(bean.getSourceOrderId(),
                        OmsConstants.NotesType.SYSTEM,
                        bean.getOrderNumber(),
                        OmsConstants.REFUND_AGREE,
                        user.getUserName(),
                        user.getUserName());

                if (StringUtils.isEmpty(noteId)) {
                    logger.error("refundsAgreeMain addNotes error");
                }

                logger.info("	天猫退款成功 OMS DB 更新");
                // 本地DB更新
                if (refundsAgreeInside(bean, user)) {
                    //	正常结果返回
//					setRefundsAgreeSuccessRet(refundsAgreeResultRetCode, result);

                    setSuccessReturnForRefund(bean.getSourceOrderId(), result, user);
                } else {
                    //	天猫请求成功，但内部失败 邮件发送
                    boolean ret = sendMail(bean);

                    String errorMsg = String.format(OmsMessageConstants.MessageContent.REFUND_AGREE_SUCCESS_OMSDB_UPDATE_ERROR, bean.getRefundId());
                    result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, errorMsg);
                }
            }
        }

        logger.info("refundsAgreeMain end");
    }

	/**
	 * 同意订单退款主函数（同步OMS，后台已退款）
	 *
	 * @param bean 画面输入信息
	 * @param result 返回结果
	 * @param user 当前用户
	 *
	 * @return
	 */
	private void  refundsAgreeMainForSynOMS(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {
		logger.info("refundsAgreeMainForSynOMS start");
		logger.info("	refund_id = " + bean.getRefundId());
		logger.info("	refund_phase = " + bean.getRefundPhase());

		boolean ret = true;
		try {
			logger.info("	当前退款信息查询");
			// 当前退款信息查询
			RefundGetResponse refundInfo = getRefund(bean);
			if (refundInfo.getErrorCode() != null) {
				ret = false;

				String errorMsg = String.format(OmsMessageConstants.MessageContent.REFUND_GET_ERROR, refundInfo.getErrorCode(), refundInfo.getMsg(), refundInfo.getSubMsg());
				result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, errorMsg);
			}

			logger.info("	退款金额检查");
			logger.info("		本地缓存天猫金额，天猫实时取得退款金额");
			logger.info("		本地缓存天猫金额 = " + bean.getRefundFee());
			logger.info("		天猫实时取得退款金额 = " + refundInfo.getRefund().getRefundFee());
			// 退款金额检查
			if (ret) {
				// 本地退款金额(oms_bt_order_refunds.refund_fee)	!=	天猫退款金额
				if (Float.valueOf(refundInfo.getRefund().getRefundFee()).floatValue() != Float.valueOf(bean.getRefundFee()).floatValue()) {
					ret = false;
					// 明细退货金额与天猫不一致，不能退款。等待天猫推送。
					result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210050, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
				}
			}

			if (ret) {
				logger.info("		总退款金额，OMS算出金额，天猫实时取得退款金额");
				//	主订单信息取得
				OutFormOrderdetailOrders mainOrderInfo = getMainOrderInfo(bean.getSourceOrderId());

				// 客户申请退款
				float customerReqRefund = Float.valueOf(mainOrderInfo.getRefundTotal());
				// OMS算出退款
				float omsRefund = sub2Digits(Float.valueOf(mainOrderInfo.getExpected()), Float.valueOf(mainOrderInfo.getFinalGrandTotal()));
				logger.info("		总退款金额 = " + customerReqRefund);
				logger.info("		OMS算出金额 = " + omsRefund);
				logger.info("		客户申请退款金额（单次） = " + refundInfo.getRefund().getRefundFee());

				// 分批退款对应
				if ( Float.valueOf(refundInfo.getRefund().getRefundFee()).floatValue() > omsRefund) {
					ret = false;
					// 客户申请退款金额 > 系统算出退款金额，不能退款。确认客户申请退款金额是否正确。
					result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210056, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
				}
			}

			if (ret) {
				logger.info("	天猫退款成功 OMS DB 更新");
				// 本地DB更新
				if (refundsAgreeInside(bean, user)) {

					setSuccessReturnForRefund(bean.getSourceOrderId(), result, user);
				} else {
					//	天猫请求成功，但内部失败 邮件发送
					ret = sendMail(bean);

					String errorMsg = String.format(OmsMessageConstants.MessageContent.REFUND_AGREE_SUCCESS_OMSDB_UPDATE_ERROR, bean.getRefundId());
					result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, errorMsg);
				}
			}
		} catch (ApiException e) {
			ret = false;

			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210048, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);

			logger.error("refundsAgreeMainForSynOMS", e);
		}

		logger.info("refundsAgreeMainForSynOMS end");
	}

	/**
	 * 同意订单退款主函数（独立域名）
	 *
	 * @param bean 画面输入信息
	 * @param result 返回结果
	 * @param user 当前用户
	 *
	 * @return
	 */
	private void  refundsAgreeMainForCN(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {
		logger.info("refundsAgreeMainForCN start");

		boolean ret = true;

		//	主订单信息取得
		OutFormOrderdetailOrders mainOrderInfo = getMainOrderInfo(bean.getSourceOrderId());
		//	订单预收金额
		float omsExpected = Float.valueOf(mainOrderInfo.getExpected());
		// 	退款金额
		float refundFee = Float.valueOf(bean.getRefundFee());

		if (refundFee > omsExpected) {
			ret = false;

			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210069, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}

		if (ret) {
			if (refundsAgreeInsideForCN(bean, user)) {
				//	正常结果返回
				setSuccessReturnForRefundCN(bean.getSourceOrderId(), result, user);
			}
		}

		logger.info("refundsAgreeMainForCN end");
	}

    /**
     * 同意订单退款主函数（阿里以外）
     *
     * @param bean 画面输入信息
     * @param result 返回结果
     * @param user 当前用户
     *
     * @return
     */
    private void  refundsAgreeMainForOther(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {
        logger.info("refundsAgreeMainForOther start");
        logger.info("	refund_id = " + bean.getRefundId());
        logger.info("	refund_phase = " + bean.getRefundPhase());

        if (refundsAgreeInside(bean, user)) {
            //	正常结果返回
            setSuccessReturnForRefund(bean.getSourceOrderId(), result, user);
        }
        logger.info("refundsAgreeMainForOther end");
    }

	/**
	 * 同意订单正常结果返回
	 *
	 * @param retMsgCode 天猫返回Code
	 * @param result 返回结果
	 *
	 * @return
	 */
	private void setRefundsAgreeSuccessRet (String retMsgCode, AjaxResponseBean result) {
		// 结果返回
		Map<String, Object> ordersRefundMap = new HashMap<String, Object>();
		//		订单退款信息
		ordersRefundMap.put("result", true);
		//		返回Code
		OutFormOrderdetailRefundsStatus refundsStatus = new OutFormOrderdetailRefundsStatus();
		refundsStatus.setMsgCode(retMsgCode);
		ordersRefundMap.put("refundsStatus", refundsStatus);
		result.setResultInfo(ordersRefundMap);

		//		正常
		result.setResult(true);
	}

	/**
	 * 同意订单退款函数
	 * 		taobao.rp.refund.review 审核退款单
	 * 		taobao.rp.refunds.agree 同意退款（聚石塔内调用）
	 * 
	 * @param bean 画面输入信息
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return list[0] 调用结果
	 * 			list[1] code返回
	 */
	private List<Object> refundsAgree(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {
		List<Object> retList = new ArrayList<Object>();
		
		boolean ret = true;
		String msgCode = "";
		
		logger.info("refundsAgree Start");
		logger.info("	refund_id = " + bean.getRefundId());
		try {
			logger.info("	当前退款信息查询");
			// 当前退款信息查询
			RefundGetResponse refundInfo = getRefund(bean);
			if (refundInfo.getErrorCode() != null) {
				ret = false;

				String errorMsg = String.format(OmsMessageConstants.MessageContent.REFUND_GET_ERROR, refundInfo.getErrorCode(), refundInfo.getMsg(), refundInfo.getSubMsg());
				result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, errorMsg);
			}

			logger.info("	退款金额检查");
			logger.info("		本地缓存天猫金额，天猫实时取得退款金额");
			logger.info("		本地缓存天猫金额 = " + bean.getRefundFee());
			logger.info("		天猫实时取得退款金额 = " + refundInfo.getRefund().getRefundFee());
			// 退款金额检查
			if (ret) {
				// 本地退款金额(oms_bt_order_refunds.refund_fee)	!=	天猫退款金额
				if (Float.valueOf(refundInfo.getRefund().getRefundFee()).floatValue() != Float.valueOf(bean.getRefundFee()).floatValue()) {
					ret = false;
					// 明细退货金额与天猫不一致，不能退款。等待天猫推送。
					result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210050, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
				}
			}

			logger.info("		总退款金额，OMS算出金额，天猫实时取得退款金额");
			//	主订单信息取得
			OutFormOrderdetailOrders mainOrderInfo = getMainOrderInfo(bean.getSourceOrderId());
			if (ret) {
				// 客户申请退款
				float customerReqRefund = Float.valueOf(mainOrderInfo.getRefundTotal());
				// OMS算出退款
				float omsRefund = sub2Digits(Float.valueOf(mainOrderInfo.getExpected()), Float.valueOf(mainOrderInfo.getFinalGrandTotal()));
				logger.info("		总退款金额 = " + customerReqRefund);
				logger.info("		OMS算出金额 = " + omsRefund);
				logger.info("		客户申请退款金额（单次） = " + refundInfo.getRefund().getRefundFee());
//				if (customerReqRefund != omsRefund) {
//					ret = false;
//					// 总退款金额与系统算出不一致，不能退款。确认客户申请退款金额是否正确。
//					result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210055, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
//				} else {
					// 分批退款对应
					if ( Float.valueOf(refundInfo.getRefund().getRefundFee()).floatValue() > omsRefund) {
						ret = false;
						// 客户申请退款金额 > 系统算出退款金额，不能退款。确认客户申请退款金额是否正确。
						result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210056, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					}
//				}
			}

			logger.info("	Call天猫接口（审核退款）");
			// Call天猫接口（审核退款）
			if (ret) {
				// 审批通过（taobao.rp.refund.review 不单独调用）
				bean.setContent("同意退款");
				bean.setReviewResult(true);
				RpRefundReviewResponse tmallResponse = callTmallApiRefundReview(bean, refundInfo.getRefund().getRefundVersion(), user);

				if (tmallResponse.getErrorCode() == null) {
					// 结果返回
					if (!tmallResponse.getIsSuccess()) {
						ret = false;

						result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210053, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					}
				} else {
					// 已经同意过，再次同意发生（errorCode = 15  && subCode = 2101）
					if (!"15".equals(tmallResponse.getErrorCode()) && !"2101".equals(tmallResponse.getSubCode())) {
						// 返回值异常
						ret = false;

						String errorMsg = String.format(OmsMessageConstants.MessageContent.RETURN_GOODS_AGREE_ERROR, tmallResponse.getErrorCode(), tmallResponse.getMsg(), tmallResponse.getSubMsg());
						result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, errorMsg);
					}
				}
				
				logger.info("RpRefundReviewResponse = " + JsonUtil.getJsonString(tmallResponse));
			}
			
			logger.info("	Call天猫接口（同意退款）");
			// Call天猫接口（同意退款）
			if (ret) {
				
				String response = callTmallApiRefundsAgreeByWebservice(bean, refundInfo.getRefund().getRefundVersion(), user);
				
				// 结果取得
				Map<String, Object> responseMap = JsonUtil.jsonToMap(response);
				
				// 调用结果
				String resultWSDL = responseMap.get(OmsConstants.WSDLRet.resultKey).toString();
				if (OmsConstants.WSDLRet.resultOK.equals(resultWSDL)) {
					// 返回Code
					msgCode = responseMap.get(OmsConstants.WSDLRet.resultInfoKey).toString();
					
				} else {
					ret = false;
					// 返回异常信息
					String errorMsg = responseMap.get(OmsConstants.WSDLRet.messageKey).toString();
					result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, errorMsg);
				}
			}

			logger.info("refundsAgree end");
		} catch (ApiException e) {
			ret = false;

			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210048, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);

			logger.error("refundsAgree", e);
			
		} catch (Exception ex) {
			ret = false;
			
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210051, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			
			logger.error("refundsAgree", ex);
		}
		
		//	结果返回		
		retList.add(ret);
		//	Code返回
		//		10000：发送二次验证短信成功 
		//		40000： 操作成功
		retList.add(msgCode);
		
		return retList;
	}
	
	/**
	 * 同意订单退款函数（内部）
	 * 
	 * @param bean 画面输入信息
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean refundsAgreeInside(OutFormOrderdetailRefunds bean, UserSessionBean user) {
		boolean ret = true;
		
		logger.info("refundsAgreeInside start");

		TransactionStatus status=transactionManager.getTransaction(def);
		
		try {
			
			// 一组订单价格更新（Expect -）
			logger.info("saveGroupOrderPriceForRefundAgree");
			ret = saveGroupOrderPriceForRefundAgree(bean.getSourceOrderId(), bean.getRefundFee(), user);			
			
			if (ret) {
				// 退款处理标志位更新
				logger.info("updateProcessFlag");
				ret = updateRefundProcessFlag(bean.getRefundId(), OmsCodeConstants.RefundStatus.SUCCESS, user);
			}
			
			// 订单Notes保存
			if (ret) {
				logger.info("saveOrderNotes");

				// OMSDB更新成功 notes追加
				String notes = OmsConstants.REFUND_AGREE_INSIDE;
				String noteId = addNotes(bean.getSourceOrderId(),
									OmsConstants.NotesType.SYSTEM, 
									bean.getOrderNumber(),
									notes,
									user.getUserName(),
									user.getUserName());
				
				if (StringUtils.isEmpty(noteId)) {
					ret = false;
				}
			}
			
			if (ret) {
				logger.info("refundsAgreeInside success");
				
				transactionManager.commit(status);
			} else {
				logger.info("refundsAgreeInside error");
				
				transactionManager.rollback(status);
			}
				
		} catch (Exception e) {
			ret = false;
			
			logger.error("refundsAgreeInside", e);
			
			transactionManager.rollback(status);
		}	
				
		return ret;
	}

	/**
	 * 同意订单退款函数（独立域名）
	 *
	 * @param bean 画面输入信息
	 * @param user 当前用户
	 *
	 * @return
	 */
	private boolean refundsAgreeInsideForCN(OutFormOrderdetailRefunds bean, UserSessionBean user) {
		boolean ret = true;

		logger.info("refundsAgreeInsideForCN start");

		TransactionStatus status=transactionManager.getTransaction(def);

		try {
			// 退款记录生成
			logger.info("insertRefundInfoForCN");
			ret = insertRefundInfoForCN(bean.getSourceOrderId(), bean.getOrderChannelId(), bean.getCartId(), bean.getRefundFee(), user);

			if (ret) {
				// 售后的场合
				if (isAfterSalePhase(bean.getSourceOrderId())) {
					// 付款记录生成
					logger.info("insertPaymentInfoForCN");
					ret = insertPaymentInfoForCN(bean.getSourceOrderId(), bean.getOrderNumber(), bean.getCartId(), bean.getRefundFee(), user);

					if (ret) {
						// 一组订单价格更新（Expect -）
						logger.info("saveGroupOrderPriceForRefundAgreeCN");
						ret = saveGroupOrderPriceForRefundAgreeCN(bean.getSourceOrderId(), bean.getRefundFee(), user);
					}
				// 售中的场合
				} else {
					// 一组订单价格更新（Expect -）
					logger.info("saveGroupOrderPriceForRefundAgreeCNOnsale");
					ret = saveGroupOrderPriceForRefundAgreeCNOnsale(bean.getSourceOrderId(), bean.getRefundFee(), user);
				}
			}

			// 订单Notes保存
			if (ret) {
				logger.info("saveOrderNotes");

				// OMSDB更新成功 notes追加
				String notes = OmsConstants.REFUND_AGREE_INSIDE;
				String noteId = addNotes(bean.getSourceOrderId(),
						OmsConstants.NotesType.SYSTEM,
						bean.getOrderNumber(),
						notes,
						user.getUserName(),
						user.getUserName());

				if (StringUtils.isEmpty(noteId)) {
					ret = false;
				}
			}

			if (ret) {
				logger.info("refundsAgreeInsideForCN success");

				transactionManager.commit(status);
			} else {
				logger.info("refundsAgreeInsideForCN error");

				transactionManager.rollback(status);
			}

		} catch (Exception e) {
			ret = false;

			logger.error("refundsAgreeInsideForCN", e);

			transactionManager.rollback(status);
		}

		return ret;
	}

	/**
	 * 售后判定
	 *
	 * @param sourceOrderId 网络订单号
	 *
	 */
	private boolean isAfterSalePhase(String sourceOrderId) {
		boolean ret = false;

		List<OutFormOrderdetailPayments> paymentList = paymentsDao.getOrdePaymentsInfo(sourceOrderId);
		if (paymentList.size() > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 一组订单Expected保存（同意退差价）
	 * 
	 * @param sourceOrderId 网络订单号
	 * @param refund 退款 
	 * @param user 当前用户
	 *
	 */
	private boolean saveGroupOrderPriceForRefundAgree(String sourceOrderId, String refund, UserSessionBean user){
		boolean ret = true;
		
		// 一组订单
		//		订单价格取得
		OrdersBean ordersInfo = new OrdersBean();
		//		sourceOrderId
		ordersInfo.setSourceOrderId(sourceOrderId);
		// 		refund
		ordersInfo.setRefund(refund);
		// 		modifier
		ordersInfo.setModifier(user.getUserName());
		
		ret = orderDetailDao.updateGroupOrdersInfoForRefundAgree(ordersInfo);
		
		return ret;
	}

	/**
	 * 一组订单Expected保存（同意退差价 独立域名）
	 *
	 * @param sourceOrderId 网络订单号
	 * @param refund 退款
	 * @param user 当前用户
	 *
	 */
	private boolean saveGroupOrderPriceForRefundAgreeCN(String sourceOrderId, String refund, UserSessionBean user){
		boolean ret = true;

		// 一组订单
		//		订单价格取得
		OrdersBean ordersInfo = new OrdersBean();
		//		sourceOrderId
		ordersInfo.setSourceOrderId(sourceOrderId);
		// 		refund
		ordersInfo.setRefund(refund);
		// 		modifier
		ordersInfo.setModifier(user.getUserName());

		ret = orderDetailDao.updateGroupOrdersInfoForRefundAgreeCN(ordersInfo);

		return ret;
	}

	/**
	 * 一组订单Expected保存（同意退差价 独立域名）
	 *
	 * @param sourceOrderId 网络订单号
	 * @param refund 退款
	 * @param user 当前用户
	 *
	 */
	private boolean saveGroupOrderPriceForRefundAgreeCNOnsale(String sourceOrderId, String refund, UserSessionBean user){
		boolean ret = true;

		// 一组订单
		//		订单价格取得
		OrdersBean ordersInfo = new OrdersBean();
		//		sourceOrderId
		ordersInfo.setSourceOrderId(sourceOrderId);
		// 		refund
		ordersInfo.setRefund(refund);
		// 		modifier
		ordersInfo.setModifier(user.getUserName());

		ret = orderDetailDao.updateGroupOrdersInfoForRefundAgreeCNOnsale(ordersInfo);

		return ret;
	}

	/**
	 * 一组订单Expected保存（拒绝退差价）
	 *
	 * @param sourceOrderId 网络订单号
	 * @param refund 退款
	 * @param user 当前用户
	 *
	 */
	private boolean saveGroupOrderPriceForRefundRefuse(String sourceOrderId, String refund, UserSessionBean user){
		boolean ret = true;

		// 一组订单
		//		订单价格取得
		OrdersBean ordersInfo = new OrdersBean();
		//		sourceOrderId
		ordersInfo.setSourceOrderId(sourceOrderId);
		// 		refund
		ordersInfo.setRefund(refund);
		// 		modifier
		ordersInfo.setModifier(user.getUserName());

		ret = orderDetailDao.updateGroupOrdersInfoForRefundRefuse(ordersInfo);

		return ret;
	}
	
	/**
	 * 订单退款处理标志更新
	 * 
	 * @param refundId 退款编号
	 * @param refundStatus 退款状态
	 * @param user 当前用户
	 *
	 */
	private boolean updateRefundProcessFlag(String refundId, String refundStatus, UserSessionBean user){
		boolean ret = true;
		
		// 一组订单
		//		订单价格取得
		OrderRefundsBean orderRefundsInfo = new OrderRefundsBean();
		//		refundId
		orderRefundsInfo.setRefundId(refundId);
		//		refundStatus
		orderRefundsInfo.setRefundStatus(refundStatus);
		//		processFlag
		orderRefundsInfo.setProcessFlag(true);
		// 		modifier
		orderRefundsInfo.setModifier(user.getUserName());
		
		ret = refundDao.updateProcessFlag(orderRefundsInfo);
		
		return ret;
	}

	/**
	 * 订单退款处理退款记录追加（独立域名）
	 *
	 * @param sourceOrderId
	 * @param orderChannelId
	 * @param cartId
	 * @param refundFee
	 * @param user 当前用户
	 *
	 */
	private boolean insertRefundInfoForCN(String sourceOrderId, String orderChannelId, String cartId, String refundFee, UserSessionBean user){
		boolean ret = true;

		// 退款记录
		OrderRefundsBean orderRefundsInfo = new OrderRefundsBean();
		// 		source_order_id
		orderRefundsInfo.setSourceOrderId(sourceOrderId);
		//		origin_source_order_id
		orderRefundsInfo.setOriginSourceOrderId(sourceOrderId);
		//		orderChannelId
		orderRefundsInfo.setOrderChannelId(orderChannelId);
		//		cartId
		orderRefundsInfo.setCartId(cartId);
		//		refundId
		orderRefundsInfo.setRefundId(DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_2));
		//		refundStatus
		orderRefundsInfo.setRefundStatus(OmsCodeConstants.RefundStatus.SUCCESS);
		//		refundFee
		orderRefundsInfo.setRefundFee(refundFee);
		//		refundPhase
		orderRefundsInfo.setRefundPhase(OmsCodeConstants.RefundPhase.AFTERSALE);
		//		processFlag
		orderRefundsInfo.setProcessFlag(true);
		//		creater
		orderRefundsInfo.setCreater(user.getUserName());
		// 		modifier
		orderRefundsInfo.setModifier(user.getUserName());

		ret = refundDao.insertRefundInfo(orderRefundsInfo);

		return ret;
	}

	/**
	 * 订单退款处理 付款记录追加（独立域名）
	 *
	 * @param sourceOrderId
	 * @param orderNumber
	 * @param cartId
	 * @param refundFee
	 * @param user 当前用户
	 *
	 */
	private boolean insertPaymentInfoForCN(String sourceOrderId, String orderNumber, String cartId, String refundFee, UserSessionBean user){
		boolean ret = true;

		// 退款记录
		OrderPaymentsBean orderPaymentsInfo = new OrderPaymentsBean();
		//		orderNumber
		orderPaymentsInfo.setOrderNumber(orderNumber);
		// 		source_order_id
		orderPaymentsInfo.setSourceOrderId(sourceOrderId);
		//		origin_source_order_id
		orderPaymentsInfo.setOriginSourceOrderId(sourceOrderId);
		//		description
		orderPaymentsInfo.setDescription(OmsCodeConstants.PaymentDescription.FINISHED);
		//		payType
		orderPaymentsInfo.setPayType(OmsCodeConstants.PaymentMethod.CASH);
		//		payNo
		orderPaymentsInfo.setPayNo("");
		//		payAccount
		orderPaymentsInfo.setPayAccount("");
		//		credit
		orderPaymentsInfo.setCredit(refundFee);
		//		creater
		orderPaymentsInfo.setCreater(user.getUserName());
		// 		modifier
		orderPaymentsInfo.setModifier(user.getUserName());

		ret = paymentsDao.insertPaymentInfo(orderPaymentsInfo);

		return ret;
	}
	
	/**
	 * 退款凭证保存
	 * 
	 * @param refundId 退款单号
	 * @param fileBase64 退款凭证（Base64格式）
	 * 
	 * @return 文件名
	 */
	private String uploadRefundFile(String refundId, String fileBase64) {
		logger.info("uploadRefundFile");
		
		String ret = "";
		
		// 文件路径
		String imagePath = Properties.readValue(PropKey.TEMP_IMG_PATH);

		// 文件名
		String rawFilenameSub = DateTimeUtil.getNow("yyyyMMddHHmmssSSS");
		String rawFilename = refundId + rawFilenameSub + ".jpg";
		
		if (fileBase64 != null) {
			// 文件上传
			if (0 == uploadImage(imagePath, rawFilename, fileBase64)) {
				ret = imagePath + rawFilename;
			}
		}
		
		return ret;
	}
	
	/**
	 * 获得退款信息
	 * 
	 * @param bean 画面输入退款信息
	 * 
	 * @return	退款信息
	 * @throws ApiException 
	 */
	private RefundGetResponse getRefund(OutFormOrderdetailRefunds bean) throws ApiException {
	
		RefundGetResponse tmallResponse = null;
//		tmallResponse = callTmallApiGetRefund(Long.valueOf(refundId));
		tmallResponse = callTmallApiGetRefund(bean);
		
		return tmallResponse;
	}
	
	/**
	 * 获得退款状态取得
	 * 
	 * @param refundId 退款编号
	 * @param refundInfo 退款信息
	 * 
	 * @return 退款状态
	 */
	private OutFormOrderdetailRefundsStatus getRefundsStatus(String refundId, Refund refundInfo) {
		OutFormOrderdetailRefundsStatus ret = new OutFormOrderdetailRefundsStatus();
		ret.setRefundId(refundId);
		
		//	是否发货判定（订单交易状态）
		if (OmsCodeConstants.TmallOrderStatus.WAIT_BUYER_CONFIRM_GOODS.equalsIgnoreCase(refundInfo.getOrderStatus()) ||
				OmsCodeConstants.TmallOrderStatus.TRADE_BUYER_SIGNED.equalsIgnoreCase(refundInfo.getOrderStatus()) ||
				OmsCodeConstants.TmallOrderStatus.TRADE_FINISHED.equalsIgnoreCase(refundInfo.getOrderStatus()) ||
				OmsCodeConstants.TmallOrderStatus.TRADE_CLOSED.equalsIgnoreCase(refundInfo.getOrderStatus()) ||
				OmsCodeConstants.TmallOrderStatus.TRADE_CLOSED_BY_TAOBAO.equalsIgnoreCase(refundInfo.getOrderStatus()) ||
				OmsCodeConstants.TmallOrderStatus.ALL_CLOSED.equalsIgnoreCase(refundInfo.getOrderStatus())) {
			ret.setShipped(true);
		} else {
			ret.setShipped(false);
		}
		
		//	是否需要退货
		ret.setHasGoodReturn(refundInfo.getHasGoodReturn());
		
		//	退款状态
		//	WAIT_SELLER_AGREE : 买家已经申请退款，等待卖家同意
		//	WAIT_BUYER_RETURN_GOODS : 卖家已经同意退款，等待买家退货
		//	WAIT_SELLER_CONFIRM_GOODS : 买家已经退货，等待卖家确认收货
		//	SELLER_REFUSE_BUYER : 卖家拒绝退款
		//	CLOSED : 退款关闭
		//	SUCCESS : 退款成功
		ret.setStatus(refundInfo.getStatus());
		
		return ret;
	}
	
	/**
	 * 同意退货主函数
	 * 
	 * @param bean 画面输入退款信息
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return
	 */
	@Override
	public void returnGoodsAgreeMain(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {
		
		if (returnGoodsAgree(bean, result, user)) {
			
			// notes追加
			String notes = OmsConstants.RETURN_GOODS_AGREE;
			addNotes(bean.getSourceOrderId(),
						OmsConstants.NotesType.SYSTEM, 
						bean.getOrderNumber(),
						notes,
						user.getUserName(),
						user.getUserName());
			
//			// 返回值设定
//			Map<String, Object> ordersRefundMap = new HashMap<String, Object>();
//			//		订单退款信息
//			ordersRefundMap.put("result", true);
//			result.setResultInfo(ordersRefundMap);
//
//			//		正常
//			result.setResult(true);
			setSuccessReturnForRefund(bean.getSourceOrderId(), result, user);
		}
	}
	
	/**
	 * 同意退货函数
	 *
	 * @param bean 画面输入退款信息
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean returnGoodsAgree(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {
		boolean ret = true;
		
		logger.info("returnGoodsAgree Start");
		
		try {
			// 当前退款信息查询
			RefundGetResponse refundGetResponse = getRefund(bean);
			if (refundGetResponse.getErrorCode() != null) {
				ret = false;

				String errorMsg = String.format(OmsMessageConstants.MessageContent.REFUND_GET_ERROR, refundGetResponse.getErrorCode(), refundGetResponse.getMsg(), refundGetResponse.getSubMsg());
				result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, errorMsg);
			}
			
			// Call天猫接口同意退货
			if (ret) {
				// 卖家收货地址编号
				String sellerAddressIdStr = ChannelConfigs.getVal2(bean.getOrderChannelId(), Name.seller_address_id, bean.getCartId());
				Long sellerAddressId = Long.valueOf(sellerAddressIdStr);
				RpReturngoodsAgreeResponse tmallResponse = callTmallApiReturnGoodsAgree(bean,
																							refundGetResponse.getRefund().getRefundVersion(),
																							sellerAddressId);

				// 异常Code返回
				if (tmallResponse.getErrorCode() != null) {
					ret = false;

					String errorMsg = String.format(OmsMessageConstants.MessageContent.RETURN_GOODS_AGREE_ERROR, tmallResponse.getErrorCode(), tmallResponse.getMsg(), tmallResponse.getSubMsg());
					result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, errorMsg);
				// 返回值判定
				} else {
					if (!tmallResponse.getIsSuccess()) {
						ret = false;
						result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210049, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					}
				}

				logger.info("RpReturngoodsAgreeResponse = " + JsonUtil.getJsonString(tmallResponse));
			}
			
		logger.info("returnGoodsAgree End");
		
		} catch (ApiException e) {
			ret = false;

			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210048, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);

			logger.error("returnGoodsAgree", e);
			
		} catch (Exception ex) {
			ret = false;
			
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210049, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					
			logger.error("returnGoodsAgree", ex);
		}
		
		return ret;
	}
	
	/**
	 * 拒绝退货函数
	 *
	 * @param bean 画面输入退款信息
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return
	 */
	@Override
	public void returnGoodsRefuseMain(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {
		
		if (returnGoodsRefuse(bean, result, user)) {
			
			// notes追加
			String notes = OmsConstants.RETURN_GOODS_REFUSE;
			addNotes(bean.getSourceOrderId(),
						OmsConstants.NotesType.SYSTEM, 
						bean.getOrderNumber(),
						notes,
						user.getUserName(),
						user.getUserName());
			
			
//			//	结果返回
//			Map<String, Object> ordersRefundMap = new HashMap<String, Object>();
//			//		订单退款信息
//			ordersRefundMap.put("result", true);
//
//			result.setResultInfo(ordersRefundMap);
//
//			//		正常
//			result.setResult(true);

			setSuccessReturnForRefund(bean.getSourceOrderId(), result, user);
		}
	}
	
	/**
	 * 拒绝退货函数
	 *
	 * @param bean 画面输入退款信息
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return
	 */
	private boolean returnGoodsRefuse(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {
		boolean ret = true;
		
		logger.info("returnGoodsRefuse Start");
		
		try {
			// 上传文件本地保存
			String uploadFileName = ""; 
			
			// 当前退款信息查询
			RefundGetResponse refundGetResponse = getRefund(bean);
			if (refundGetResponse.getErrorCode() != null) {
				ret = false;

				String errorMsg = String.format(OmsMessageConstants.MessageContent.REFUND_GET_ERROR, refundGetResponse.getErrorCode(), refundGetResponse.getMsg(), refundGetResponse.getSubMsg());
				result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, errorMsg);
			}

			if (ret) {
				// 上传文件缓存
				uploadFileName = uploadRefundFile(bean.getRefundId(), bean.getImage());
				
				if (StringUtils.isEmpty(uploadFileName)) {
					ret = false;
					result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210054, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
				}
			}
			
			// Call天猫接口拒绝退货
			if (ret) {
				RpReturngoodsRefuseResponse tmallResponse = callTmallApiReturnGoodsRefuse(bean, refundGetResponse.getRefund().getRefundVersion(), uploadFileName);

				// 异常Code返回
				if (tmallResponse.getErrorCode() != null) {
					ret = false;

					String errorMsg = String.format(OmsMessageConstants.MessageContent.RETURN_GOODS_AGREE_ERROR, tmallResponse.getErrorCode(), tmallResponse.getMsg(), tmallResponse.getSubMsg());
					result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, errorMsg);
				// 返回值判定
				} else {
					if (!tmallResponse.getResult()) {
						ret = false;

						result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210049, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					}
				}

				logger.info("RpReturngoodsRefuseResponse = " + JsonUtil.getJsonString(tmallResponse));
			}

			logger.info("returnGoodsRefuse end");
		} catch (ApiException e) {
			ret = false;

			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210048, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);

			logger.error("returnGoodsRefuse", e);
			
		} catch (Exception ex) {
			ret = false;
			
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210049, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					
			logger.error("returnGoodsRefuse", ex);
		}
		
		return ret;
	}
	
	/**
	 * 卖家回填物流信息主函数
	 *
	 * @param bean 画面输入退款信息
	 * @param result 返回结果
	 * @param user 当前用户
	 * 
	 * @return
	 */
	@Override
	public void returnGoodsRefillMain(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {
		if (returnGoodsRefill(bean, result, user)) {
			
			// notes追加
			String notes = OmsConstants.RETURN_GOODS_REFILL;
			addNotes(bean.getSourceOrderId(),
						OmsConstants.NotesType.SYSTEM, 
						bean.getOrderNumber(),
						notes,
						user.getUserName(),
						user.getUserName());
			
			
//			//	结果返回
//			Map<String, Object> ordersRefundMap = new HashMap<String, Object>();
//			//		订单退款信息
//			ordersRefundMap.put("result", true);
//
//			result.setResultInfo(ordersRefundMap);
//
//			//		正常
//			result.setResult(true);
			setSuccessReturnForRefund(bean.getSourceOrderId(), result, user);
		}
	}
	
	private boolean returnGoodsRefill(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {
		boolean ret = true;
		
		logger.info("returnGoodsRefill Start");
		
		try {
			
			// 当前退款信息查询
			RefundGetResponse refundGetResponse = getRefund(bean);
			if (refundGetResponse.getErrorCode() != null) {
				ret = false;
				
				String errorMsg = String.format(OmsMessageConstants.MessageContent.REFUND_GET_ERROR, refundGetResponse.getErrorCode(), refundGetResponse.getMsg(), refundGetResponse.getSubMsg());
				result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, errorMsg);
			}
		
			// Call卖家回填物流信息
			if (ret) {
				RpReturngoodsRefillResponse tmallResponse = callTmallApiReturnGoodsRefill(bean, refundGetResponse.getRefund().getRefundVersion());

				// 异常Code返回
				if (tmallResponse.getErrorCode() != null) {
					ret = false;

					String errorMsg = String.format(OmsMessageConstants.MessageContent.RETURN_GOODS_AGREE_ERROR, tmallResponse.getErrorCode(), tmallResponse.getMsg(), tmallResponse.getSubMsg());
					result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, errorMsg);
				// 返回值判定
				} else  {
					if (!tmallResponse.getIsSuccess()) {
						ret = false;

						result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210052, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					}
				}
				
				logger.info("RpReturngoodsRefillResponse = " + JsonUtil.getJsonString(tmallResponse));
			}

			logger.info("returnGoodsRefill end");
		} catch (ApiException e) {
			ret = false;

			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210048, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);

			logger.error("returnGoodsRefill", e);
			
		} catch (Exception ex) {
			ret = false;
			
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210052, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					
			logger.error("returnGoodsRefill", ex);
		}
		
		return ret;
	}
	
	/**
	 * 审核退款单
	 * 
	 * @return
	 */
	@Override
	public void refundReviewMain(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {
		if (refundReview(bean, result, user)) {
			
			// notes追加
			String notes = OmsConstants.REFUND_REVIEW;
			addNotes(bean.getSourceOrderId(),
						OmsConstants.NotesType.SYSTEM, 
						bean.getOrderNumber(),
						notes,
						user.getUserName(),
						user.getUserName());
			
			
			//	结果返回
			Map<String, Object> ordersRefundMap = new HashMap<String, Object>();
			//		订单退款信息
			ordersRefundMap.put("result", true);

			result.setResultInfo(ordersRefundMap);
			
			//		正常
			result.setResult(true);
		}
	}
	
	private boolean refundReview(OutFormOrderdetailRefunds bean, AjaxResponseBean result, UserSessionBean user) {
		boolean ret = true;
		
		logger.info("refundReview Start");
		
		try {
			
			// 当前退款信息查询
			RefundGetResponse refundGetResponse = getRefund(bean);
			if (refundGetResponse.getErrorCode() != null) {
				ret = false;
				
				String errorMsg = String.format(OmsMessageConstants.MessageContent.REFUND_GET_ERROR, refundGetResponse.getErrorCode(), refundGetResponse.getMsg(), refundGetResponse.getSubMsg());
				result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, errorMsg);
			}
		
			// Call审核退款单
			if (ret) {
				RpRefundReviewResponse tmallResponse = callTmallApiRefundReview(bean, refundGetResponse.getRefund().getRefundVersion(), user);

				if (tmallResponse.getErrorCode() == null) {
					// 结果返回
					if (!tmallResponse.getIsSuccess()) {
						ret = false;

						result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210053, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					}
				} else {
					// 返回值异常
					ret = false;

					String errorMsg = String.format(OmsMessageConstants.MessageContent.RETURN_GOODS_AGREE_ERROR, tmallResponse.getErrorCode(), tmallResponse.getMsg(), tmallResponse.getSubMsg());
					result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, errorMsg);
				}
			}

			logger.info("refundReview end");

		} catch (ApiException e) {
			ret = false;

			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210048, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);

			logger.error("refundReview", e);
			
		} catch (Exception ex) {
			ret = false;
			
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210053, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
					
			logger.error("refundReview", ex);
		}
		
		return ret;
	}
	
	/**
	 * 天猫API调用（taobao.refund.messages.get）
	 * 
	 * @param bean 退款信息
	 * @param pageNo 页码
	 * @param pageSize 每页条数
	 * @param fields 需返回的字段列表
	 * 
	 * @return
	 * @throws ApiException 
	 */
	private RefundMessagesGetResponse callTmallApiGetRefundMessages(OutFormOrderdetailRefunds bean, long pageNo, long pageSize, String fields) throws ApiException {

		logger.info("callTmallApiGetRefundMessages");
		
//		TaobaoClient client=new DefaultTaobaoClient(url, appkey, appsecret);
//		RefundMessagesGetRequest req=new RefundMessagesGetRequest();
//		req.setFields(fields);
//		req.setRefundId(refundId);
//		req.setPageNo(pageNo);
//		req.setPageSize(pageSize);
//		req.setRefundPhase(refundPhase);
//		RefundMessagesGetResponse response = client.execute(req , sessionkey);
		
		ShopBean shopInfo = ShopConfigs.getShop(bean.getOrderChannelId(), bean.getCartId());
		RefundMessagesGetResponse response = tbRefundService.getRefundMessages(shopInfo, Long.valueOf(bean.getRefundId()), pageNo, pageSize, fields, bean.getRefundPhase());
		
		return response;
	}
	
	/**
	 * 天猫API调用（taobao.refund.message.add） 天猫不支持
	 * 
	 * @param refundId 退款编号
	 * @param content 留言内容
	 * @param fileLocation 文件路径
	 * 
	 * @return
	 * @throws ApiException 
	 */
	private RefundMessageAddResponse callTmallApiAddRefundMessages(long refundId, String content, String fileLocation) throws ApiException {
		
//		logger.info("callTmallApiAddRefundMessages");
//
//		TaobaoClient client=new DefaultTaobaoClient(url, appkey, appsecret);
//		RefundMessageAddRequest req=new RefundMessageAddRequest();
//		req.setRefundId(refundId);
//		req.setContent(content);
//		FileItem fItem = new FileItem(new File(fileLocation));
//		req.setImage(fItem);
//		RefundMessageAddResponse response = client.execute(req, sessionkey);
//
//		return response;

		return null;
	}
	
	/**
	 * 天猫API调用（taobao.refund.refuse）
	 * 
	 * @param bean 画面输入退款信息
	 * @param refundVersion 退款版本号
	 * @param fileLocation 拒绝退货凭证图片
	 * 
	 * @return
	 */
	private RefundRefuseResponse callTmallApiRefundRefuse(OutFormOrderdetailRefunds bean, long refundVersion, String fileLocation) throws ApiException {
		
		logger.info("callTmallApiRefundRefuse");
		
//		TaobaoClient client=new DefaultTaobaoClient(url, appkey, appsecret);
//		RefundRefuseRequest req=new RefundRefuseRequest();
//		req.setRefundId(Long.valueOf(bean.getRefundId()));
//		req.setRefuseMessage(bean.getContent());
//		FileItem fItem = new FileItem(new File(fileLocation));
//		req.setRefuseProof(fItem);
//		req.setRefundPhase(bean.getRefundPhase());
//		req.setRefundVersion(refundVersion);
//		RefundRefuseResponse response = client.execute(req , sessionkey);
		
		ShopBean shopInfo = ShopConfigs.getShop(bean.getOrderChannelId(), bean.getCartId());
		RefundRefuseResponse response = tbRefundService.doRefundRefuse(shopInfo, Long.valueOf(bean.getRefundId()), bean.getContent(), fileLocation, bean.getRefundPhase(), refundVersion);
		
		if (response.getErrorCode() != null) {
			logger.info("	errcode = " + response.getErrorCode());
			logger.info("	msg = " + response.getMsg());
			logger.info("	sub errcode = " + response.getSubCode());
			logger.info("	sub msg = " + response.getSubMsg());
		}
		
		return response;
	}
	
	/**
	 * 天猫API调用（taobao.rp.refunds.agree） 聚石塔调用变更
	 * 
	 * @param bean 画面输入退款信息
	 * @param refundVersion 退款版本号
	 *
	 * @return
	 * @throws ApiException 
	 */
	private RpRefundsAgreeResponse callTmallApiRefundsAgree(OutFormOrderdetailRefunds bean, long refundVersion) throws ApiException {

//		logger.info("callTmallApiRefundsAgree");
//
//		TaobaoClient client=new DefaultTaobaoClient(url, appkey, appsecret);
//		RpRefundsAgreeRequest req=new RpRefundsAgreeRequest();
//		req.setCode(bean.getCode());
//		// refund_id|amount|version|phase
//		String refundInfos = bean.getRefundId() + "|"
//							+ bean.getRefundFee() + "|"
//							+ refundVersion + "|"
//							+ bean.getRefundPhase();
//		req.setRefundInfos(refundInfos);
//		RpRefundsAgreeResponse response = client.execute(req, sessionkey);
//
//		if (response.getErrorCode() != null) {
//			logger.info("	errcode = " + response.getErrorCode());
//			logger.info("	msg = " + response.getMsg());
//			logger.info("	sub errcode = " + response.getSubCode());
//			logger.info("	sub msg = " + response.getSubMsg());
//		}
//
//		return response;

		return null;
	}
	
	/**
	 * 通过聚石塔天猫API调用（taobao.rp.refunds.agree）
	 * 
	 * @param bean 画面输入退款信息
	 * @param refundVersion 退款版本
	 * 
	 * @return
	 * @throws Exception 
	 */
	private String callTmallApiRefundsAgreeByWebservice(OutFormOrderdetailRefunds bean, long refundVersion, UserSessionBean user) throws Exception {
		
		logger.info("callTmallApiRefundsAgreeByWebservice start");
		
		Map<String, Object> orderInfoMap = new HashMap<String, Object>();
		String now = DateTimeUtil.getNow(DateTimeUtil.DEFAULT_DATETIME_FORMAT);//"yyyy-MM-dd HH:mm:ss"
		orderInfoMap.put("timeStamp", now);// 校验用
		orderInfoMap.put("signature", Verification.getVoyageOneVerificationString(now));// 校验用
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("orderChannelId", bean.getOrderChannelId());
		dataMap.put("cartId", bean.getCartId());
		dataMap.put("refundId", bean.getRefundId());
		// 同意退款时，金额以分为单位
		int amount = mult0Digits(Float.valueOf(bean.getRefundFee()), 100);
		dataMap.put("amount", String.valueOf(amount));
		dataMap.put("version", String.valueOf(refundVersion));
		dataMap.put("phase", bean.getRefundPhase());
		dataMap.put("code", bean.getCode());
		dataMap.put("userName", user.getUserName());
		
		logger.info("callTmallApiRefundsAgreeByWebservice in param");
		logger.info("	orderChannelId = " + bean.getOrderChannelId());
		logger.info("	cartId = " + bean.getCartId());
		logger.info("	refundId = " + bean.getRefundId());
		logger.info("	amount = " + String.valueOf(amount));
		logger.info("	version = " + String.valueOf(refundVersion));
		logger.info("	phase = " + bean.getRefundPhase());
		logger.info("	code = " + bean.getCode());
		logger.info("	userName = " + user.getUserName());
		
		// 退款相关信息		
		orderInfoMap.put(OmsConstants.REFUNDS_INFO_WSDL_PARAM, dataMap); // 数据体
		// 转换成json字符串用来传输
		String orderInfoJson = JsonUtil.getJsonString(orderInfoMap);
		
		// 退款同意（聚石塔调用URL）
		String refundsAgreePath = Properties.readValue(PropKey.REFUNDS_AGREE_PATH);;
		
		// 调用WSDL
		String response = "";
		
		response = WebServiceUtil.postByJsonStr(refundsAgreePath, orderInfoJson);
		
		logger.info("callTmallApiRefundsAgreeByWebservice	response = " + response);
		
		return response;
	}
	
	/**
	 * 天猫API调用（taobao.refund.get）
	 * 
	 * @param bean 退款信息
	 *
	 * @return
	 * @throws ApiException 
	 */
	private RefundGetResponse callTmallApiGetRefund(OutFormOrderdetailRefunds bean) throws ApiException {

		logger.info("callTmallApiGetRefund");
		
//		TaobaoClient client=new DefaultTaobaoClient(url, appkey, appsecret);
//		RefundGetRequest req=new RefundGetRequest();
//		//	订单交易状态,退款状态,货物状态,退款版本号,买家是否需要退货,退还金额(退还给买家的金额),退款原因,退款说明
//		req.setFields("refund_id,order_status,status,good_status,refund_version,has_good_return,refund_fee,refund_phase,operation_contraint,reason,desc ");
//		req.setRefundId(refundId);
//		RefundGetResponse response = client.execute(req , sessionkey);
		
		ShopBean shopInfo = ShopConfigs.getShop(bean.getOrderChannelId(), bean.getCartId());
		
		String fields = "refund_id,order_status,status,good_status,refund_version,has_good_return,refund_fee,refund_phase,operation_contraint,reason,desc";
		RefundGetResponse response = tbRefundService.getRefundInfo(shopInfo, fields, Long.valueOf(bean.getRefundId()));
		
		if (response.getErrorCode() == null) {
			logger.info("	refund_id = " + response.getRefund().getRefundId());
			logger.info("	refund_version = " + response.getRefund().getRefundVersion());
			logger.info("	order_status = " + response.getRefund().getOrderStatus());
			logger.info("	status = " + response.getRefund().getStatus());
			logger.info("	good_status = " + response.getRefund().getGoodStatus());
			logger.info("	has_good_return = " + response.getRefund().getHasGoodReturn());
			logger.info("	refund_phase = " + response.getRefund().getRefundPhase());
			logger.info("	operation_contraint = " + response.getRefund().getOperationContraint());			
		}
		
		return response;
	}
	
	/**
	 * 天猫API调用（taobao.refund.get）
	 * 
	 * @param bean 退款信息
	 * @param refundVersion 退款版本号
	 * @param sellerAddressId 卖家收货地址编号
	 * 
	 * @return
	 * @throws ApiException 
	 */
	private RpReturngoodsAgreeResponse callTmallApiReturnGoodsAgree(OutFormOrderdetailRefunds bean,
																long refundVersion,
																long sellerAddressId) throws ApiException {
		
		logger.info("callTmallApiReturnGoodsAgree");
		
//		TaobaoClient client=new DefaultTaobaoClient(url, appkey, appsecret);
//		RpReturngoodsAgreeRequest req=new RpReturngoodsAgreeRequest();
//		req.setRefundId(Long.valueOf(bean.getRefundId()));
//		req.setName("");
//		req.setAddress("");
//		req.setPost("");
//		req.setTel("");
//		req.setMobile("");
//		req.setRemark(bean.getContent());
//		req.setRefundPhase(bean.getRefundPhase());
//		req.setRefundVersion(refundVersion);
//		req.setSellerAddressId(sellerAddressId);
//		RpReturngoodsAgreeResponse response = client.execute(req , sessionkey);
		
		ShopBean shopInfo = ShopConfigs.getShop(bean.getOrderChannelId(), bean.getCartId());
		RpReturngoodsAgreeResponse response = tbRefundService.doReturnGoodsAgree(shopInfo, Long.valueOf(bean.getRefundId()), bean.getContent(), bean.getRefundPhase(), refundVersion, sellerAddressId);
		
		if (response.getErrorCode() != null) {
			logger.info("	errcode = " + response.getErrorCode());
			logger.info("	msg = " + response.getMsg());
			logger.info("	sub errcode = " + response.getSubCode());
			logger.info("	sub msg = " + response.getSubMsg());
		}
		
		return response;
	}
	
	/**
	 * 天猫API调用（taobao.rp.returngoods.refuse）
	 * 
	 * @param bean 退款信息
	 * @param refundVersion 退款版本号
	 * @param fileLocation 拒绝退货凭证图片
	 * 
	 * @return
	 * @throws ApiException 
	 */
	private RpReturngoodsRefuseResponse callTmallApiReturnGoodsRefuse(OutFormOrderdetailRefunds bean,
																	long refundVersion,
																	String fileLocation) throws ApiException {
		logger.info("callTmallApiReturnGoodsRefuse");
		
//		TaobaoClient client=new DefaultTaobaoClient(url, appkey, appsecret);
//		RpReturngoodsRefuseRequest req=new RpReturngoodsRefuseRequest();
//		req.setRefundId(Long.valueOf(bean.getRefundId()));
//		req.setRefundPhase(bean.getRefundPhase());
//		req.setRefundVersion(refundVersion);
//		FileItem fItem = new FileItem(new File(fileLocation));
//		req.setRefuseProof(fItem);
//		RpReturngoodsRefuseResponse response = client.execute(req , sessionkey);
		
		ShopBean shopInfo = ShopConfigs.getShop(bean.getOrderChannelId(), bean.getCartId());
		RpReturngoodsRefuseResponse response = tbRefundService.doReturnGoodsRefuse(shopInfo, Long.valueOf(bean.getRefundId()), bean.getRefundPhase(), refundVersion, fileLocation);
		
		if (response.getErrorCode() != null) {
			logger.info("	errcode = " + response.getErrorCode());
			logger.info("	msg = " + response.getMsg());
			logger.info("	sub errcode = " + response.getSubCode());
			logger.info("	sub msg = " + response.getSubMsg());
		}
		
		return response;
	}

	/**
	 * 天猫API调用（taobao.rp.returngoods.refill）
	 * 
	 * @param bean 退款信息
	 * @param refundVersion 退款版本号
	 *
	 * @return
	 * @throws ApiException 
	 */
	private RpReturngoodsRefillResponse callTmallApiReturnGoodsRefill(OutFormOrderdetailRefunds bean,
																	long refundVersion) throws ApiException {
		logger.info("callTmallApiReturnGoodsRefill");
		
//		TaobaoClient client=new DefaultTaobaoClient(url, appkey, appsecret);
//		RpReturngoodsRefillRequest req=new RpReturngoodsRefillRequest();
//		req.setRefundId(Long.valueOf(bean.getRefundId()));
//		req.setRefundPhase(bean.getRefundPhase());
//		req.setLogisticsWaybillNo(bean.getLogisticsWaybillNo());
//		req.setLogisticsCompanyCode(bean.getLogisticsCompanyCode());
//		RpReturngoodsRefillResponse response = client.execute(req , sessionkey);
		
		ShopBean shopInfo = ShopConfigs.getShop(bean.getOrderChannelId(), bean.getCartId());
		RpReturngoodsRefillResponse response = tbRefundService.doReturnGoodsRefill(shopInfo, Long.valueOf(bean.getRefundId()), bean.getRefundPhase(), bean.getLogisticsWaybillNo(), bean.getLogisticsCompanyCode());
		
		if (response.getErrorCode() != null) {
			logger.info("	errcode = " + response.getErrorCode());
			logger.info("	msg = " + response.getMsg());
			logger.info("	sub errcode = " + response.getSubCode());
			logger.info("	sub msg = " + response.getSubMsg());
		}
		
		return response;
	}
	
	/**
	 * 天猫API调用（taobao.rp.refund.review）
	 *
	 * @param bean 退款信息
	 * @param refundVersion 退款版本号
	 * @param user 当前用户
	 * 
	 * @return
	 * @throws ApiException 
	 */
	private RpRefundReviewResponse callTmallApiRefundReview(OutFormOrderdetailRefunds bean,
																	long refundVersion,
																	UserSessionBean user) throws ApiException {
		logger.info("callTmallApiRefundReview");
		
//		TaobaoClient client=new DefaultTaobaoClient(url, appkey, appsecret);
//		RpRefundReviewRequest req=new RpRefundReviewRequest();
//		req.setRefundId(Long.valueOf(bean.getRefundId()));
//		req.setOperator(user.getUserName());
//		req.setRefundPhase(bean.getRefundPhase());
//		req.setRefundVersion(refundVersion);
//		req.setResult(bean.isReviewResult());
//		req.setMessage(bean.getContent());
//		RpRefundReviewResponse response = client.execute(req , sessionkey);
		
		ShopBean shopInfo = ShopConfigs.getShop(bean.getOrderChannelId(), bean.getCartId());
		RpRefundReviewResponse response = tbRefundService.doRefundReview(shopInfo, Long.valueOf(bean.getRefundId()), user.getUserName(), bean.getRefundPhase(), refundVersion, bean.isReviewResult(), bean.getContent());
		
		if (response.getErrorCode() != null) {
			logger.info("	errcode = " + response.getErrorCode());
			logger.info("	msg = " + response.getMsg());
			logger.info("	sub errcode = " + response.getSubCode());
			logger.info("	sub msg = " + response.getSubMsg());
		}
		
		return response;
	}

	/**
	 * 天猫API调用（taobao.wlb.imports.order.cancel）
	 *
	 * @param bean 订单信息
	 * @param lgOrderCode 菜鸟物流单号
	 *
	 * @return
	 * @throws ApiException
	 */
	private WlbImportsOrderCancelResponse callTmallApiWLBOrderCancel(OutFormOrderdetailOrders bean, String lgOrderCode) throws ApiException {
		logger.info("callTmallApiWLBOrderCancel");

		ShopBean shopInfo = ShopConfigs.getShop(bean.getOrderChannelId(), bean.getCartId());
		WlbImportsOrderCancelResponse response = tbWLBService.cancelOrder(shopInfo, lgOrderCode);

		if (response.getErrorCode() != null) {
			logger.info("	errcode = " + response.getErrorCode());
			logger.info("	msg = " + response.getMsg());
			logger.info("	sub errcode = " + response.getSubCode());
			logger.info("	sub msg = " + response.getSubMsg());
		}

		return response;
	}

	/**
	 * 天猫API调用（taobao.wlb.imports.order.get）
	 *
	 * @param bean 订单信息
	 *
	 * @return
	 * @throws ApiException
	 */
	private WlbImportsOrderGetResponse callTmallApiWLBOrderGet(OutFormOrderdetailOrders bean) throws ApiException {
		logger.info("callTmallApiWLBOrderGet");

		ShopBean shopInfo = ShopConfigs.getShop(bean.getOrderChannelId(), bean.getCartId());
		WlbImportsOrderGetResponse response = tbWLBService.orderGet(shopInfo, bean.getSourceOrderId());
//		WlbImportsOrderGetResponse response = tbWLBService.orderGet(shopInfo, "1326471399347183");

		if (response.getErrorCode() != null) {
			logger.info("	errcode = " + response.getErrorCode());
			logger.info("	msg = " + response.getMsg());
			logger.info("	sub errcode = " + response.getSubCode());
			logger.info("	sub msg = " + response.getSubMsg());
		}

		return response;
	}

	/**
	 * 天猫退款成功，OMS DB更新异常 邮件发送
	 *
	 * @param bean 画面输入信息
	 *
	 * @return
	 */
	private boolean sendMail(OutFormOrderdetailRefunds bean) {

		boolean ret = true;

		StringBuilder tbody = new StringBuilder();

		/**
		 * 收件人信息检查邮件ROW格式
		 */
		String SHIP_INFO_CHECK_ROW =
				"<tr>" +
						"<td>%s</td>" +
						"<td>%s</td>" +
						"<td>%s</td>" +
						"<td>%s</td>" +
						"<td>%s</td>" +
						"</tr>";
		// 邮件样式
		String EMAIL_STYLE_STRING = "<style>"
				+  "body{font-family:'微软雅黑',sans-serif}"
				+ "table,th,td{border:1px solid silver;border-collapse:collapse;font-size:15px}"
				+ "th,td{padding:3px 5px}"
				+  "th{background:gray;color:white;border:1px solid #514e3a}"
				+ "</style>";

		/**
		 * 收件人信息检查邮件TABLE格式
		 */
		String SHIP_INFO_CHECK_TABLE =
				"<div><span>%s</span>" +
						"<table><tr>" +
						"<th>网络订单号</th>" +
						"<th>订单渠道编号</th>" +
						"<th>CartId</th>" +
						"<th>退款编号</th>" +
						"<th>退款金额</th>" +
						"</tr>%s</table></div>";

		String SHIP_INFO_CHECK_HEAD = "天猫退款成功，OMS DB更新异常，请IT即时处理";
		String SHIP_INFO_CHECK_SUBJECT = "OMS DB更新异常，请IT即时处理";

		// 邮件每行正文
		String mailTextLine = String.format(SHIP_INFO_CHECK_ROW,
												bean.getSourceOrderId(),
												bean.getOrderChannelId(),
												bean.getCartId(),
												bean.getRefundId(),
												bean.getRefundFee());
		tbody.append(mailTextLine);

		// 拼接table
		String body = String.format(SHIP_INFO_CHECK_TABLE, SHIP_INFO_CHECK_HEAD, tbody.toString());

		// 拼接邮件正文
		StringBuilder emailContent = new StringBuilder();
		emailContent.append(EMAIL_STYLE_STRING).append(body);
		try {
			Mail.sendAlert("ITOMS", SHIP_INFO_CHECK_SUBJECT, emailContent.toString(), true);
			logger.info("邮件发送成功!");

		} catch (MessagingException e) {
			ret = false;
			logger.error("邮件发送失败！" ,e);
		}

		return ret;
	}

    /**
     * Cart信息取得
     *
     * @param cartId cartId
     *
     * @return
     */
    private CartBean getCartInfo(String cartId) {
        CartBean cartInfo = cartDao.getCartInfo(cartId);
        return cartInfo;
    }
}
