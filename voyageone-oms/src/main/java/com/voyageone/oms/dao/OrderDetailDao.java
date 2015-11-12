package com.voyageone.oms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.base.exception.SystemException;
import com.voyageone.oms.formbean.OutFormOrderDetailOrderDetail;
import com.voyageone.oms.formbean.OutFormOrderdetailOrders;
import com.voyageone.oms.formbean.OutFormOrderdetailShipping;
import com.voyageone.oms.modelbean.OrderDetailsBean;
import com.voyageone.oms.modelbean.OrdersBean;

@Repository
public class OrderDetailDao extends BaseDao {
	
	/**
	 * 获得订单信息，根据（order_number）
	 * 
	 * @return
	 */
	public OutFormOrderdetailOrders getOrdersInfo(String orderNumber) {
		OutFormOrderdetailOrders ordersInfo = (OutFormOrderdetailOrders) selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_getOrdersInfo", orderNumber);
		
		return ordersInfo;
	}
	
	/**
	 * 获得订单信息，根据（source_order_id）
	 * 
	 * @return
	 */
	public OutFormOrderdetailOrders getOrdersInfoBySourceOrderId(String sourceOrderId) {
		OutFormOrderdetailOrders ordersInfo = (OutFormOrderdetailOrders) selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_getOrdersInfoBySourceOrderId", sourceOrderId);
		
		return ordersInfo;
	}

	/**
	 * 获得订单信息，根据（origin_source_order_id）
	 *
	 * @return
	 */
	public OutFormOrderdetailOrders getOrdersInfoByOrigSourceOrderId(String originSourceOrderId) {
		OutFormOrderdetailOrders ordersInfo = (OutFormOrderdetailOrders) selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_getOrdersInfoByOrigSourceOrderId", originSourceOrderId);

		return ordersInfo;
	}

	/**
	 * 获得订单信息，根据（Pay Number）
	 *
	 * @return
	 */
	public OutFormOrderdetailOrders getOrdersInfoByPayNo(String payNum) {
		OutFormOrderdetailOrders ordersInfo = (OutFormOrderdetailOrders) selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_getOrdersInfoByPayNo", payNum);

		return ordersInfo;
	}

	/**
	 * 获得订单信息，根据（source_order_id）
	 * 
	 * @return
	 */
	public List<OutFormOrderdetailOrders> getOrdersListBySourceOrderId(String sourceOrderId) {
		List<OutFormOrderdetailOrders> ordersList = (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_getOrdersListBySourceOrderId", sourceOrderId);
		
		return ordersList;
	}
	
	/**
	 * 获得订单详细信息
	 * 
	 * @return
	 */
	public List<OutFormOrderDetailOrderDetail> getOrderDetailsInfo(String orderNumber) {
		 List<OutFormOrderDetailOrderDetail> orderDetailsList =  (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_getOrderDetailsInfo", orderNumber);
		
		return orderDetailsList;
	}
	
	/**
	 * 获得订单详细信息，根据一组（order_number）
	 * 
	 * @return
	 */
	public List<OutFormOrderDetailOrderDetail> getOrderDetailsInfo(List<String> orderNumberList) {
		
		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("orderNumberList", orderNumberList);
		
		List<OutFormOrderDetailOrderDetail> orderDetailsList =  (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_getOrderDetailsInfoByOrderNumList", paraIn);
		
		return orderDetailsList;
	}
	
	/**
	 * 获得订单详细信息最大番号
	 * 
	 * @return
	 */
	public int getOrderDetailsMaxItemNumber(String orderNumber) {
		 int ret =  (int) selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_getOrderDetailsMaxItemNumber", orderNumber);
		
		return ret;
	}
	
	/**
	 * 获得订单历史信息
	 * 
	 * @return
	 */
	public List<OrdersBean> getOrderHistoryInfo(String orderNumber) {
		 List<OrdersBean> orderHistoryList =  (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_getOrderHistoryInfo", orderNumber);
		
		return orderHistoryList;
	}
	
	/**
	 * 获得订单历史信息，根据元订单号
	 * 
	 * @return
	 */
	public List<OrdersBean> getOrderHistoryInfoBySourceOrderId(String sourceOrderId) {
		 List<OrdersBean> orderHistoryList =  (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_getOrderHistoryInfoBySourceOrderId", sourceOrderId);
		
		return orderHistoryList;
	}
	
	/**
	 * 获得子订单历史信息
	 * 
	 * @return
	 */
	public List<OrdersBean> getSonOrderHistoryInfo(List<String> sourceOrderId) {
		
		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("sourceOrderId", sourceOrderId);
		
		 List<OrdersBean> orderHistoryList =  (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_getSonOrderHistoryInfo", paraIn);
		
		return orderHistoryList;
	}
	
	/**
	 * 订单明细信息追加
	 * 
	 * @return
	 */
	public boolean insertOrderDetailsInfo(OrderDetailsBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_insertOrderDetailsInfo", bean);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 订单信息更新
	 * 
	 * @return
	 */
	public boolean updateOrdersInfo(OrdersBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_updateOrdersInfo", bean);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 订单信息更新（差价订单绑定）
	 * 
	 * @return
	 */
	public boolean updateOrdersInfoForBindPriceDiffOrder(OrdersBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_updateOrdersInfoForBindPriceDiffOrder", bean);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 订单锁定信息更新
	 * 
	 * @return
	 */
	public boolean updateOrdersLockInfo(OrdersBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_updateOrdersLockInfo", bean);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 一组订单锁定信息更新
	 * 
	 * @return
	 */
	public boolean updateGroupOrdersLockInfo(OrdersBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_updateGroupOrdersLockInfo", bean);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 订单Approved信息更新
	 * 
	 * @return
	 */
	public boolean updateOrdersApprovedInfo(OrdersBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_updateOrdersApprovedInfo", bean);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}

	/**
	 * 订单明细Approved信息更新
	 *
	 * @return
	 */
	public boolean updateOrderDetailsApprovedInfo(OrderDetailsBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_updateOrdersApprovedInfo", bean);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 订单锁定信息更新
	 * 
	 * @return
	 */
	public boolean updateOrdersCancelInfo(OrdersBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_updateOrdersCancelInfo", bean);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}	
	
	/**
	 * 获得订单详细信息Notes中最大的番号
	 * 
	 * @return
	 */
	public int getOrderDetailsNotesMaxItemNumber(String orderNumber) {
		Object ret = selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_bt_notes_getNotesMaxItemNumber", orderNumber);
		if (ret == null) {
			return 0;
		} else {
			return (int)ret;
		}
	}
	
	/**
	 * 订单明细信息删除
	 * 
	 * @return
	 */
	public boolean deleteOrderDetailsInfo(OrderDetailsBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.delete(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_deleteOrderDetailsInfo", bean);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 订单明细信息更新
	 * 
	 * @return
	 */
	public boolean updateOrderDetailsInfo(OrderDetailsBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_updateOrderDetailsInfo", bean);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 订单明细信息更新（状态）
	 * 
	 * @return
	 */
	public boolean updateOrderDetailsStatusInfo(OrderDetailsBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_updateOrderDetailsStatusInfo", bean);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 订单信息更新（状态）
	 * 
	 * @return
	 */
	public boolean updateOrdersStatusInfo(OrdersBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_updateOrdersStatusInfo", bean);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 订单信息更新（取消订单）
	 * 
	 * @return
	 */
	public boolean updateOrdersStatusInfoForCanceOrder(OrdersBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_updateOrdersStatusInfoForCanceOrder", bean);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 订单信息更新（其他属性，[freight_collect]）
	 * 
	 * @return
	 */
	public boolean updateOrdersOtherPropInfo(OutFormOrderdetailOrders bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_updateOrdersOtherPropInfo", bean);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}

	/**
	 * 订单信息更新（客户拒收，[customer_refused]）
	 *
	 * @return
	 */
	public boolean updateOrdersCustomerRefusedInfo(OutFormOrderdetailOrders bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_updateOrdersCustomerRefusedInfo", bean);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 第三方订单取消更新（其他属性，[ext_flg1]）
	 *
	 * @return
	 */
	public boolean cancelClientOrder(OutFormOrderdetailOrders bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_ext_orders_updateExtFlg1", bean);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 获得最新订单号
	 * 
	 * @return
	 */
	public long getOrderNumber() {
		Long orderNumber = 0L;
		Map<String, Long> orderNumberMap = new HashMap<String, Long>();
		int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_sequence_order_number_getOrderNumber", orderNumberMap);
		
		if (retCount > 0) {
			orderNumber = orderNumberMap.get("orderNumber");
		} else {
			throw new SystemException("获得最新订单号失败");
		}
		
		return orderNumber;
	}
	
	/**
	 * 获得订单最大SubSourceOrderId
	 * 
	 * @return
	 */
	public String getOrdersMaxSubSourceOrderId(String sourceOrderId) {
		String ret =  (String) selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_getOrdersMaxSubSourceOrderId", sourceOrderId);
		
		return ret;
	}
	
	/**
	 * 物品单位 货运信息取得
	 * 
	 * @return
	 */
	public List<OutFormOrderdetailShipping> getOrderDetailsShippingInfo(String orderNumber) {
		
		 List<OutFormOrderdetailShipping> orderDetailsShippingList =  (List) selectList(Constants.DAO_NAME_SPACE_OMS + "viw_syn_tracking_getShippingInfo", orderNumber);
		
		return orderDetailsShippingList;
	}
	
	/**
	 * 物品单位 货运信息取得
	 * 
	 * @return
	 */
	public List<OutFormOrderdetailShipping> getOrderDetailsShippingInfo(List<String> orderNumberList) {
		
		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("orderNumberList", orderNumberList);
		
		 List<OutFormOrderdetailShipping> orderDetailsShippingList =  (List) selectList(Constants.DAO_NAME_SPACE_OMS + "viw_syn_tracking_getShippingInfoByOrderNumList", paraIn);
		
		return orderDetailsShippingList;
	}
	
	/**
	 * 获得一组订单金额信息，根据（source_order_id）
	 * 
	 * @return
	 */
	public OutFormOrderdetailOrders getGroupOrdersInfo(String source_order_id) {
		OutFormOrderdetailOrders ordersInfo = (OutFormOrderdetailOrders) selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_bt_group_orders_getGroupOrdersInfo", source_order_id);
		
		return ordersInfo;
	}
	
	/**
	 * 一组订单信息更新
	 * 
	 * @return
	 */
	public boolean updateGroupOrdersInfo(OrdersBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_group_orders_updateGroupOrdersInfo", bean);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 一组订单信息更新（差价订单绑定）
	 * 
	 * @return
	 */
	public boolean updateGroupOrdersInfoForBindPriceDiffOrder(OrdersBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_group_orders_updateGroupOrdersInfoForBindPriceDiffOrder", bean);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 一组订单信息更新（同意退差价）
	 * 
	 * @return
	 */
	public boolean updateGroupOrdersInfoForRefundAgree(OrdersBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_group_orders_updateGroupOrdersInfoForRefundAgree", bean);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}

	/**
	 * 一组订单信息更新（同意退差价独立域名）
	 *
	 * @return
	 */
	public boolean updateGroupOrdersInfoForRefundAgreeCN(OrdersBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_group_orders_updateGroupOrdersInfoForRefundAgreeCN", bean);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 一组订单信息更新（同意退差价独立域名）
	 *
	 * @return
	 */
	public boolean updateGroupOrdersInfoForRefundAgreeCNOnsale(OrdersBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_group_orders_updateGroupOrdersInfoForRefundAgreeCNOnsale", bean);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 一组订单信息更新（拒绝退差价）
	 *
	 * @return
	 */
	public boolean updateGroupOrdersInfoForRefundRefuse(OrdersBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_group_orders_updateGroupOrdersInfoForRefundRefuse", bean);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 一组订单信息追加
	 * 
	 * @return
	 */
	public boolean insertGroupOrdersInfo(OrdersBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_group_orders_insertGroupOrdersInfo", bean);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}

	/**
	 * 扩展订单信息追加
	 *
	 * @return
	 */
	public boolean insertExtOrdersInfo(OrdersBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_ext_orders_insertExtOrdersInfo", bean);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 扩展订单明细信息追加
	 *Detail
	 * @return
	 */
	public boolean insertExtOrderDetailsInfo(OrderDetailsBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_ext_order_details_insertExtOrderDetailsInfo", bean);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 订单信息更新（差价订单绑定）
	 *
	 * @return
	 */
//	public boolean updatePaymentSettleInfo(List<String> originSourceOrderList, String settlementFileId) {
//		boolean ret = false;
//
//		HashMap<String, Object> paraIn = new HashMap<String, Object>();
//		paraIn.put("originSourceOrderList", originSourceOrderList);
//		paraIn.put("settlementFileId", settlementFileId);
//
//		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_group_orders_updatePaymentSettleInfo", paraIn);
//
//		if (retCount > 0) {
//			ret = true;
//		}
//
//		return ret;
//	}
	public boolean updatePaymentSettleInfo(String settlementFileId) {
		boolean ret = false;

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("settlementFileId", settlementFileId);

		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_group_orders_updatePaymentSettleInfo", paraIn);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 订单汇率更新
	 *
	 * @return
	 */
	public boolean updateOrderRateInfo(String settlementFileId) {
		boolean ret = false;

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("settlementFileId", settlementFileId);

		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_updateOrderRateInfo", paraIn);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}
}
