package com.voyageone.oms.dao;

import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.oms.formbean.OutFormOrderdetailOrders;
import com.voyageone.oms.modelbean.OrdersBean;

@Repository
public class SynShipSyncDao extends BaseDao {
	
	
	/**
	 * 更改地址
	 * @param bean 订单信息
	 * @return
	 */
	public boolean updateAddress(OutFormOrderdetailOrders bean){
		
		updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "tt_orders_updateAddressInfo", bean);
		// 存在synship 不存在的情况，始终返回  true
		return true;
	}
	
	/**
	 * 更改Shipping
	 * @param bean 订单信息
	 * @return
	 */
	public boolean updateShipping(OutFormOrderdetailOrders bean){

		updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "tt_orders_updateShippingInfo", bean);
		// 存在synship 不存在的情况，始终返回  true
		return true;
	}
	
	/**
	 * 更改订单锁定状态 
	 * @param bean 订单信息
	 * @return
	 */
	public boolean updateLockStatus(OrdersBean bean){

		updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "tt_orders_updateIsLockedInfo", bean);
		// 存在synship 不存在的情况，始终返回  true
		return true;
	}
	
	/**
	 * 更改订单OrderInst
	 * @param bean 订单信息
	 * @return
	 */
	public boolean updateCustomerComment(OutFormOrderdetailOrders bean) {
		
		updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "tt_orders_updateCustomerComment", bean);
		// 存在synship 不存在的情况，始终返回  true
		return true;
	}
	
	/**
	 * 更改订单Comments
	 * @param bean 订单信息
	 * @return
	 */
	public boolean updateInternalMessage(OutFormOrderdetailOrders bean) {
		
		updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "tt_orders_updateInternalMessage", bean);
		// 存在synship 不存在的情况，始终返回  true
		return true;
	}
	
	/**
	 * 更改订单invoiceInfo
	 * @param bean 订单信息
	 * @return
	 */
	public boolean updateNoteToCust(OutFormOrderdetailOrders bean) {
		
		updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "tt_orders_updateNoteToCustInfo", bean);
		// 存在synship 不存在的情况，始终返回  true
		return true;
	}
	
	/**
	 * 更改订单FinalGrandTotal
	 * @param bean 订单信息
	 * @return
	 */
	public boolean updateFinalGrandTotal(OutFormOrderdetailOrders bean) {
		
		updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "tt_orders_updateFinalGrandTotalInfo", bean);
		// 存在synship 不存在的情况，始终返回  true
		return true;
	}
	
	/**
	 * 更改订单状态
	 * @param bean 订单信息
	 * @return
	 */
	public boolean updateOrderStatus(OrdersBean bean) {
		
		updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "tt_orders_updateOrderStatus", bean);
		// 存在synship 不存在的情况，始终返回  true
		return true;
	}
	
	/**
	 * 更改freight_collect
	 * @param bean 订单信息
	 * @return
	 */
	public boolean updateFreightCollect(OutFormOrderdetailOrders bean){

		updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "tt_orders_updateFreightCollectInfo", bean);
		// 存在synship 不存在的情况，始终返回  true
		return true;
	}
}
