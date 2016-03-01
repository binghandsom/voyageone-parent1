package com.voyageone.oms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.oms.formbean.InFormSearch;
import com.voyageone.oms.formbean.OutFormOrderdetailOrders;
import com.voyageone.oms.formbean.OutFormSearch;
import com.voyageone.oms.modelbean.OrdersBean;

@Repository
public class OrderDao extends BaseDao {
	
	/**
	 * 获得订单数
	 * 
	 * @return
	 */
	public int getOrderCounts() {
		int count = (int) selectOne(Constants.DAO_NAME_SPACE_OMS + "omst_orders_getCounts");
		
		return count;
	}
	
	/**
	 * 获得订单总数
	 * 
	 * @return
	 */
	public List<String> getOrders(InFormSearch bean) {
		List<String> orderList = (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_getOrdersCount", bean);
		
		return orderList;
	}
	
	/**
	 * 获得订单数量
	 * 
	 * @return
	 */
	public int getOrderCount(InFormSearch bean) {
		int count = (int) selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_getOrderCount", bean);
		
		return count;
	}
	
	/**
	 * 获得订单号列表
	 * 
	 * @return
	 */
	public List<String> getOrderNumbers(InFormSearch bean) {
		List<String> orderNumberList = (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_getOrderNumbers", bean);
		
		return orderNumberList;
	}
	
	/**
	 * 获得订单信息
	 * 
	 * @return
	 */
	public List<OutFormSearch> getOrdersInfo(InFormSearch bean) {
		List<OutFormSearch> ordersInfo = (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_getOrdersInfo", bean);
		
		return ordersInfo;
	}
	
	/**
	 * 订单信息追加
	 * 
	 * @return
	 */
	public boolean insertOrdersInfo(OrdersBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_insertOrdersInfo", bean);
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 更改地址
	 * @param bean
	 * @return
	 */
	public boolean updateAddress(OutFormOrderdetailOrders bean){
		boolean ret = false;
		int count = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_updateAddress", bean);
		if (count > 0) {
			ret = true;
		}
		return ret;
	}
	
	/**
	 * 更改Sold to地址
	 * @param bean
	 * @return
	 */
	public boolean updateSoldToAddress(OutFormOrderdetailOrders bean){
		boolean ret = false;
		int count = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_updateSoldToAddress", bean);
		if (count > 0) {
			ret = true;
		}
		return ret;
	}
	
	/**
	 * 更改Ship to地址
	 * @param bean
	 * @return
	 */
	public boolean updateShipToAddress(OutFormOrderdetailOrders bean){
		boolean ret = false;
		int count = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_updateShipToAddress", bean);
		if (count > 0) {
			ret = true;
		}
		return ret;
	}

	/**
	 * 更改Invoice
	 * @param bean
	 * @return
	 */
	public boolean updateInvoice(OutFormOrderdetailOrders bean){
		boolean ret = false;
		int count = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_updateInvoice", bean);
		if (count > 0) {
			ret = true;
		}
		return ret;
	}
	
	/**
	 * 更改GiftMessage
	 * @param bean
	 * @return
	 */
	public boolean updateGiftMessage(OutFormOrderdetailOrders bean){
		boolean ret = false;
		int count = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_updateGiftMessage", bean);
		if (count > 0) {
			ret = true;
		}
		return ret;
	}
	
	/**
	 * 获取当天订单的数量
	 * @return
	 */
	public List<Map<String,Object>> getCurrentDayOrderCounts(List<String> channelId,String fromTime,String endTime){
		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("channelId", channelId);
		paraIn.put("fromTime", fromTime);
		paraIn.put("endTime", endTime);
		
		List<Map<String,Object>> count=(List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_getOrderCurrentDayCount", paraIn);
		
		return count;
	}
	/**
	 * 获取未approved订单数量
	 * @return
	 */
	public List<Map<String,Object>> getUnApprovedCounts(List<String> channelId,String orderStatusInProcessing){
		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("channelId", channelId);
		paraIn.put("orderStatusInProcessing", orderStatusInProcessing);
		List<Map<String,Object>> count=(List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_getOrdersUnApprovedCount", paraIn);
		
		return count;
	}
	/**
	 * 获取退款未处理的数量
	 * @return
	 */
	public List<Map<String,Object>> gerRefundsCounts(List<String> channelId,String orderStatusReturnRequested){
		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("channelId", channelId);
		paraIn.put("orderStatusReturnRequested", orderStatusReturnRequested);
		List<Map<String,Object>> count=(List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_getRefundOrdersCount",paraIn);
		
		return count;
	}

	/**
	 * 根据物流单号获得订单号列表
	 *
	 * @return
	 */
	public List<String> getOrderNumbersByTrackingNo(InFormSearch bean) {
		List<String> orderNumberList = (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_getOrderNumbersByTrackingNo", bean);

		return orderNumberList;
	}
}
