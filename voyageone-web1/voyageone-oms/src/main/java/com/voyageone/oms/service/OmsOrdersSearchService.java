package com.voyageone.oms.service;

import java.util.List;
import java.util.Map;

import com.voyageone.core.modelbean.MasterInfoBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.oms.formbean.InFormSearch;
import com.voyageone.oms.formbean.OutFormSearch;


/**
 * OMS 订单检索service
 * 
 * @author jacky
 *
 */
public interface OmsOrdersSearchService {
	
	/**
	 * 获得检索条件下拉信息
	 * 
	 * @return
	 */
	public Map<Integer, List<MasterInfoBean>> getMasterInfoForOrderSearch();

	/**
	 * 获得channel信息
	 * 
	 * @return
	 */
	public List<MasterInfoBean> getShoppingCarts(String propertyId);
	
	/**
	 * 获得订单数量
	 * 
	 * @return
	 */
	public int getOrderCount(InFormSearch bean);
	
	/**
	 * 获得订单号列表
	 * 
	 * @return
	 */
	public List<String> getOrderNumbers(InFormSearch bean);
	
	/**
	 * 获得订单信息
	 * 
	 * @return
	 */
	public List<OutFormSearch> getOrdersInfo(InFormSearch bean, UserSessionBean user);
	/**
	 * 获取当前订单数量
	 * @return
	 */
	public List<Map<String,Object>> getCurrentDayOrderCounts(List<String> channelId,String fromTime,String endTime);
	/**
	 * 获取未Approved的订单数量
	 * @return
	 */
	public List<Map<String,Object>> getUnApprovedCounts(List<String> channelId,String orderStatusInProcessing);
	/**
	 * 获取退款未处理的数量
	 * @return
	 */
	public List<Map<String,Object>> gerRefundsCounts(List<String> channelId,String orderStatusReturnRequested);
	
	public Map<String,Object> getOrderIndexCount(List<String> channelId,String fromTime,String endTime);

	/**
	 * 根据物流单号获得订单号列表
	 *
	 * @return
	 */
	public List<String> getOrderNumbersByTrackingNo(InFormSearch bean);
}
