package com.voyageone.oms.service.impl;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.TypeConfigEnums.MastType;
import com.voyageone.common.configs.Type;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.core.modelbean.MasterInfoBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.oms.OmsCodeConstants;
import com.voyageone.oms.OmsConstants;
import com.voyageone.oms.dao.ChannelDao;
import com.voyageone.oms.dao.MasterInfoDao;
import com.voyageone.oms.dao.OrderDao;
import com.voyageone.oms.formbean.InFormSearch;
import com.voyageone.oms.formbean.OutFormSearch;
import com.voyageone.oms.service.OmsOrdersSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public class OmsOrdersSearchServiceImpl implements OmsOrdersSearchService {

	@Autowired
	private ChannelDao channelDao;
	
	@Autowired
	private MasterInfoDao masterInfoDao;
	
	@Autowired
	private OrderDao orderDao;
	
	@Override
	public List<MasterInfoBean> getShoppingCarts(String propertyId) {
		return channelDao.getShoppingCarts(propertyId);
	}
	
	@Override
	public Map<Integer, List<MasterInfoBean>> getMasterInfoForOrderSearch() {
		List<MasterInfoBean> masterInfoList = masterInfoDao.getMasterInfoForOrderSearch();
		Map<Integer, List<MasterInfoBean>> masterInfoListMap = new HashMap<Integer, List<MasterInfoBean>>();
		
		if (masterInfoList != null && masterInfoList.size() > 0) {
			// -----standard fields-----
			// quickFilterList
			List<MasterInfoBean> quickFilterList = new ArrayList<MasterInfoBean>();
			// shippingMethodList
			List<MasterInfoBean> shippingMethodList = new ArrayList<MasterInfoBean>();
			// paymentMethodList
			List<MasterInfoBean> paymentMethodList = new ArrayList<MasterInfoBean>();
			// itemStatusList
			List<MasterInfoBean> itemStatusList = new ArrayList<MasterInfoBean>();
			// orderStatusList
			List<MasterInfoBean> orderStatusList = new ArrayList<MasterInfoBean>();
			
			// -----custom order&customer detail fields-----
			// invoice
			List<MasterInfoBean> invoiceList = new ArrayList<MasterInfoBean>();
			// local ship on hold
			List<MasterInfoBean> localShipOnHoldList = new ArrayList<MasterInfoBean>();
			// freight by customer
			List<MasterInfoBean> freightByCustomerList = new ArrayList<MasterInfoBean>();
			
			for (MasterInfoBean master : masterInfoList) {
				int type = master.getType();
				switch (type) {
				case OmsConstants.TYPE_QUICK_FILTER :
					quickFilterList.add(master);
					break;
				case OmsConstants.TYPE_SHIPPING_METHOD :
					shippingMethodList.add(master);
					break;
				case OmsConstants.TYPE_PAYMENT_METHOD :
					paymentMethodList.add(master);
					break;
				case OmsConstants.TYPE_ITEM_STATUS :
					itemStatusList.add(master);
					break;
				case OmsConstants.TYPE_ORDER_STATUS :
					orderStatusList.add(master);
					break;
				case OmsConstants.TYPE_INVOICE :
					invoiceList.add(master);
					break;
				case OmsConstants.TYPE_LOCAL_SHIP_ON_HOLD :
					localShipOnHoldList.add(master);
					break;
				case OmsConstants.TYPE_FREIGHT_BY_CUSTOMER :
					freightByCustomerList.add(master);
					break;
				}
			}
			
			masterInfoListMap.put(OmsConstants.TYPE_QUICK_FILTER, quickFilterList);
			masterInfoListMap.put(OmsConstants.TYPE_SHIPPING_METHOD, shippingMethodList);
			masterInfoListMap.put(OmsConstants.TYPE_PAYMENT_METHOD, paymentMethodList);
			masterInfoListMap.put(OmsConstants.TYPE_ITEM_STATUS, itemStatusList);
			masterInfoListMap.put(OmsConstants.TYPE_ORDER_STATUS, orderStatusList);
			masterInfoListMap.put(OmsConstants.TYPE_INVOICE, invoiceList);
			masterInfoListMap.put(OmsConstants.TYPE_LOCAL_SHIP_ON_HOLD, localShipOnHoldList);
			masterInfoListMap.put(OmsConstants.TYPE_FREIGHT_BY_CUSTOMER, freightByCustomerList);
		}
		return masterInfoListMap;
	}
	
	@Override
	public int getOrderCount(InFormSearch bean) {
		return orderDao.getOrderCount(bean);
	}
	
	@Override
	public List<String> getOrderNumbers(InFormSearch bean) {
		return orderDao.getOrderNumbers(bean);
	}
	
	@Override
	public List<OutFormSearch> getOrdersInfo(InFormSearch bean, UserSessionBean user) {
		List<OutFormSearch> ordersInfo = orderDao.getOrdersInfo(bean);
		if (ordersInfo != null && ordersInfo.size() > 0) {
			DecimalFormat df = new DecimalFormat("0.00");
			
			for (OutFormSearch order : ordersInfo) {
				String finalGrandTotal = order.getFinalGrandTotal();
				if (StringUtils.isNullOrBlank2(finalGrandTotal)) {
					finalGrandTotal = "0.00";
				}
				finalGrandTotal = df.format(Double.valueOf(finalGrandTotal));
				order.setFinalGrandTotal(finalGrandTotal);
				
//				String balanceDue = order.getBalanceDue();
//				if (StringUtils.isNullOrBlank2(balanceDue)) {
//					balanceDue = "0.00";
//				}
//				balanceDue = df.format(Double.valueOf(balanceDue));
//				order.setBalanceDue(balanceDue);
				
				//	支付差额
				String paymentBalanceDue = order.getPaymentBalanceDue();
				if (StringUtils.isNullOrBlank2(paymentBalanceDue)) {
					paymentBalanceDue = "0.00";
				}
				paymentBalanceDue = df.format(Double.valueOf(paymentBalanceDue));
				order.setPaymentBalanceDue(paymentBalanceDue);
				
				//	交易差额
				String transactionBalanceDue = order.getTransactionBalanceDue();
				if (StringUtils.isNullOrBlank2(transactionBalanceDue)) {
					transactionBalanceDue = "0.00";
				}
				transactionBalanceDue = df.format(Double.valueOf(transactionBalanceDue));
				order.setTransactionBalanceDue(transactionBalanceDue);
				
				String orderDateTime = order.getOrderDate();
				if (!StringUtils.isNullOrBlank2(orderDateTime)) {
					orderDateTime = DateTimeUtil.getLocalTime(orderDateTime, user.getTimeZone());
					if (orderDateTime.length() > 10) {
						orderDateTime = orderDateTime.substring(0, 10);
					}
					order.setOrderDate(orderDateTime);
				}
			}
		}
		return ordersInfo;
	}
	
    /**
     * 获取当前订单数量
     */
	@Override
	public List<Map<String,Object>> getCurrentDayOrderCounts(List<String> channelId,String fromTime,String endTime) {
		
		return orderDao.getCurrentDayOrderCounts(channelId,fromTime,endTime);
	}
    /**
     * 获取未Approved的数量
     */
	@Override
	public List<Map<String,Object>> getUnApprovedCounts(List<String> channelId,String orderStatusInProcessing) {
		
		return orderDao.getUnApprovedCounts(channelId,orderStatusInProcessing);
	}
    /**
     * 获取退款未处理的订单数量
     */
	@Override
	public List<Map<String,Object>> gerRefundsCounts(List<String> channelId,String orderStatusReturnRequested) {
		
		return orderDao.gerRefundsCounts(channelId,orderStatusReturnRequested);
	}

	@Override
	public Map<String, Object> getOrderIndexCount(List<String> channelId,
			String fromTime, String endTime) {
		Map<String, Object> orderCountInfoMap = new HashMap<String, Object>();
		//存放TodayOrderCount
		List<Map<String,Object>> todayCount;
		//存放RefundOrderCount
		List<Map<String,Object>> reCount;
		//存放UnApprovedCount
		List<Map<String,Object>> uaCount;
		//从Master取InProcessing状态的Value
		String orderStatusInProcessing = Type.getValue(MastType.orderStatus.getId(), OmsCodeConstants.OrderStatus.INPROCESSING, com.voyageone.common.Constants.LANGUAGE.EN);
		//从Master取ReturnRequested状态的Value
		String orderStatusReturnRequested = Type.getValue(MastType.orderStatus.getId(), OmsCodeConstants.RefundStatus.RETURN_REQUESTED, com.voyageone.common.Constants.LANGUAGE.EN);
		
		List<Map<String,Object>> currentDayCount = getCurrentDayOrderCounts(channelId, fromTime, endTime);
		if (currentDayCount.size() > 0) {
			 todayCount=count(currentDayCount, channelId);
			 orderCountInfoMap.put("todayCount", todayCount);
		}
		List<Map<String,Object>> refundCount = gerRefundsCounts(channelId, orderStatusReturnRequested);
		if (refundCount.size() > 0) {
			 reCount = count(refundCount, channelId);
			 orderCountInfoMap.put("reCount", reCount);
			
		}
		List<Map<String,Object>> UnApprovedCount = getUnApprovedCounts(channelId, orderStatusInProcessing);
		if (UnApprovedCount.size() > 0) {
			uaCount = count(UnApprovedCount, channelId);
			orderCountInfoMap.put("uaCount", uaCount);
		}
		return orderCountInfoMap;
	}
	/**
	 * 拼接返回的Listmap
	 * @param listmap
	 * @param channelId
	 * @return
	 */
	public List<Map<String,Object>> count(List<Map<String,Object>> listmap, List<String> channelId) {
		for (Map<String, Object> map: listmap) {
		  for (int i = 0; i < channelId.size(); i++) {
			  if (map.get("channelId").equals(channelId.get(i))) {
				  map.put("name", ChannelConfigs.getChannel(channelId.get(i)).getName());
			     }
			  }
		}
		return listmap;
	}
	/**
	 * 数据为空时的返回结果
	 * @param listmap
	 * @param channelId
	 * @return
	 *//*
	public List<Map<String,Object>> emptycount(List<Map<String,Object>> listmap,List<String> channelId ){
	
	    for(int i=0;i<channelId.size();i++){
    	Map<String,Object> map=new HashMap<String,Object>();
	    	map.put("name",ChannelConfigs.getChannel(channelId.get(i)).getName());
	    	map.put("channelId",channelId.get(i));
	    	map.put("count", "0");
	    	listmap.add(map);
    }
		return listmap;
	}*/

	@Override
	public List<String> getOrderNumbersByTrackingNo(InFormSearch bean) {
		return orderDao.getOrderNumbersByTrackingNo(bean);
	}
}
