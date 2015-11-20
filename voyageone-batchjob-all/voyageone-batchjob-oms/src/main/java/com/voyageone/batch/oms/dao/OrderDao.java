package com.voyageone.batch.oms.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.voyageone.batch.oms.formbean.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.base.exception.SystemException;
import com.voyageone.batch.oms.formbean.OutFormOrderDetailOrderDetail;
import com.voyageone.batch.oms.formbean.OutFormOrderdetailOrders;
import com.voyageone.batch.oms.formbean.OutFormReservationInfo;
import com.voyageone.batch.oms.formbean.OutFormSendOrderMail;
import com.voyageone.batch.oms.modelbean.ChangedOrderInfo4Log;
import com.voyageone.batch.oms.modelbean.NewOrderInfo4Log;
import com.voyageone.batch.oms.modelbean.OrderExtend;
import com.voyageone.batch.oms.modelbean.TaobaoTradeBean;
import com.voyageone.common.Constants;

@Repository
public class OrderDao extends BaseDao {
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * 获得平台渠道信息
	 * 
	 * @return
	 */
	public Map<String, String> getPlatformId() {
		Map<String, String> platformIdMap = new HashMap<String, String>();
		
		List<Map> platformIdList = null;
		try {
			platformIdList = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "ct_cart_getPlatformId");
		} catch (Exception ex) {
			platformIdList = new ArrayList<Map>();
			
			logger.error(ex.getMessage(), ex);
		}
		
		if (platformIdList != null && platformIdList.size() > 0) {
			for (Map source : platformIdList) {
				platformIdMap.put(String.valueOf(source.get("cartId")), String.valueOf(source.get("platformId")));
			}
		}
		return platformIdMap;
	}
	
	/**
	 *  获得需批处理新付款订单json信息
	 * @return
	 */
	public List<Map<String, String>> getNewOrderInfoFromJson() {
		List<Map<String, String>> newOrderJsonList = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_new_order_info_json_getNewOrderInfoFromJson");
		
		return newOrderJsonList;
	}
	
	/**
	 * 插入新付款订单信息
	 * 
	 * @return
	 */
	public boolean insertNewOrdersInfo(String ordersSqlValue, int size) {
		boolean ret = true;
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("values", ordersSqlValue);
		
		try {
			updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_new_orders_import_history_insertNewOrdersInfo", dataMap);
			
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			
			ret = false;
		}
		
		return ret;
	}
	
	/**
	 * 置位新付款订单json信息表发送标志->1
	 * 
	 * @return
	 */
	public boolean updateNewSendFlag4Json(List<String> jsonIdList, List<String> targetList, int size, String taskName) {
		boolean ret = false;
		
		try {
			for (int i = 0; i < size; i++) {
				Map<String, String> dataMap = new HashMap<String, String>();
				dataMap.put("jsonId", jsonIdList.get(i));
				dataMap.put("target", targetList.get(i));
				dataMap.put("taskName", taskName);
				int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_new_order_info_json_updateNewSendFlag4Json", dataMap);
				
				if (retCount == 1) {
					ret = true;
				} else {
					ret = false;
					
					break;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 批处理异常时置位新付款订单json信息表发送标志->2
	 * 
	 * @return
	 */
	public boolean updateNewSendFlag4Json2(List<String> jsonIdList, List<String> targetList, int size, String taskName) {
		boolean ret = false;
		
		try {
			for (int i = 0; i < size; i++) {
				Map<String, String> dataMap = new HashMap<String, String>();
				dataMap.put("jsonId", jsonIdList.get(i));
				dataMap.put("target", targetList.get(i));
				dataMap.put("taskName", taskName);
				int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_new_order_info_json_updateNewSendFlag4Json2", dataMap);
				
				if (retCount == 1) {
					ret = true;
				} else {
					ret = false;
					
					break;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 *  获得需批处理状态变化订单json信息
	 * @return
	 */
	public List<Map<String, String>> getChangedOrderInfoFromJson() {
		List<Map<String, String>> changedOrderJsonList = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_changed_order_info_json_getChangedOrderInfoFromJson");
		
		return changedOrderJsonList;
	}
	
	/**
	 * 插入订单状态变化订单
	 * 
	 * @return
	 */
	public boolean insertChangedOrdersInfo(String ordersSqlValue, int size) {
		boolean ret = false;
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("values", ordersSqlValue);
		
		try {
			int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_changed_orders_import_history_insertChangedOrdersInfo", dataMap);
			
			if (retCount == size) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 置位状态变化订单json信息表发送标志->1
	 * 
	 * @return
	 */
	public boolean updateChangedSendFlag4Json(List<String> jsonIdList, List<String> targetList, int size, String taskName) {
		boolean ret = false;
		
		try {
			for (int i = 0; i < size; i++) {
				Map<String, String> dataMap = new HashMap<String, String>();
				dataMap.put("jsonId", jsonIdList.get(i));
				dataMap.put("target", targetList.get(i));
				dataMap.put("taskName", taskName);
				int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_changed_order_info_json_updateChangedSendFlag4Json", dataMap);
				
				if (retCount == 1) {
					ret = true;
				} else {
					ret = false;
					
					break;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 批处理异常时置位状态变化订单json信息表发送标志->2
	 * 
	 * @return
	 */
	public boolean updateChangedSendFlag4Json2(List<String> jsonIdList, List<String> targetList, int size, String taskName) {
		boolean ret = false;
		
		try {
			for (int i = 0; i < size; i++) {
				Map<String, String> dataMap = new HashMap<String, String>();
				dataMap.put("jsonId", jsonIdList.get(i));
				dataMap.put("target", targetList.get(i));
				dataMap.put("taskName", taskName);
				int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_changed_order_info_json_updateChangedSendFlag4Json2", dataMap);
				
				if (retCount == 1) {
					ret = true;
				} else {
					ret = false;
					
					break;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 *  获得需批处理新付款订单信息
	 * @return
	 */
	public List<NewOrderInfo4Log> getNewOrderInfo4BatchFromLog() {
		List<NewOrderInfo4Log> newOrderInfo = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_new_orders_import_history_getNewOrderInfo4BatchFromLog");
		
		return newOrderInfo;
	}
	
	/**
	 *  获得需批处理新付款订单信息（手工）
	 * @return
	 */
	public List<NewOrderInfo4Log> getManualNewOrderInfo4BatchFromLog() {
		List<NewOrderInfo4Log> newOrderInfo = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_manual_new_orders_import_history_getManualNewOrderInfo4BatchFromLog");
		
		return newOrderInfo;
	}
	
	/**
	 *  获得需每条处理新付款订单信息
	 * @return
	 */
	public List<NewOrderInfo4Log> getNewOrderInfo4EachFromLog() {
		List<NewOrderInfo4Log> newOrderInfo = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_new_orders_import_history_getNewOrderInfo4EachFromLog");
		
		return newOrderInfo;
	}
	
	/**
	 *  获得需处理状态变化订单信息
	 * @return
	 */
	public List<ChangedOrderInfo4Log> getChangedOrderInfoFromLog() {
		List<ChangedOrderInfo4Log> changedOrderInfo = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_changed_orders_import_history_getChangedOrderInfoFromLog");
		
		return changedOrderInfo;
	}
	
	/**
	 *  根据原始订单号tid 获取内部订单号及订单状态
	 * @return
	 */
	public List<Map> getOrderNumberAndStatusByTid(ChangedOrderInfo4Log changedOrderInfo) {
		List<Map> dataMapList = null;
		try {
			dataMapList = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_getOrderNumberAndStatusByTid", changedOrderInfo);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return dataMapList;
	}
	
	/**
	 *  tradeSuccess状态消息重复判断
	 * @return
	 */
	public boolean isRepeatTradeSuccess(Map<String, String> dataMap) {
		boolean isSuccess = false;
		
		List<Map> dataMapList = null;
		try {
			dataMapList = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_payments_isRepeatTradeSuccess", dataMap);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		if (dataMapList != null && dataMapList.size() > 0) {
			isSuccess = true;
		}
		
		return isSuccess;
	}
	
	/**
	 *  获得需退款订单价格信息
	 *  
	 * @return
	 */
	public double[] getOrderPrice4Refund(ChangedOrderInfo4Log changedOrderInfo) {
		double[] refundOrderPrice = new double[3];
		
		List<Map<String, String>> priceMapList = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_getOrderPrice4Refund", changedOrderInfo);
		
		if (priceMapList != null && priceMapList.size() > 0) {
			Map<String, String> priceMap = priceMapList.get(0);
			
			if (priceMap != null) {
				if (priceMap.get("finalProductTotal") != null && priceMap.get("pricePerUnit") != null && priceMap.get("finalGrandTotal") != null) {
					refundOrderPrice[0] = Double.valueOf(String.valueOf(priceMap.get("finalProductTotal")));
					refundOrderPrice[1] = Double.valueOf(String.valueOf(priceMap.get("pricePerUnit")));
					refundOrderPrice[2] = Double.valueOf(String.valueOf(priceMap.get("finalGrandTotal")));
				}
			}
		}
		
		return refundOrderPrice;
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
	 * 插入订单状态变化订单
	 * 
	 * @param dataMap
	 * @return
	 */
	public boolean importOrdersInfo(Map<String, String> dataMap) {
		boolean ret = false;
		
		try {
			int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_changed_orders_import_history_importOrdersInfo", dataMap);
			
			if (retCount > 0) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}

	/**
	 * 获取已经approved的订单信息
	 * @author sky
	 * @param dataMap
	 * @return boolean
	 */
	public List<OutFormReservationInfo> getReservationInfo() {
		List<OutFormReservationInfo> reservationInfo = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "wsdl_transfer_reservationInfo");
		return reservationInfo;
	}
	
	/**
	 * 批处理插入订单表数据
	 * 
	 * @param ordersSqlValue
	 * @param size
	 * @return
	 */
	public boolean insertOrdersBatchData(String ordersSqlValue, int size) {
		boolean ret = false;
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("values", ordersSqlValue);
		
		try {
			int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_insertOrdersBatchData", dataMap);
			
			if (retCount == size) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 批处理插入订单详细表数据
	 * 
	 * @param orderDetailsStr
	 * @param size
	 * @return
	 */
	public boolean insertOrderDetailsBatchData(String orderDetailsStr, int size) {
		boolean ret = false;
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("values", orderDetailsStr);
		
		try {
			int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_insertOrderDetailsBatchData", dataMap);
			
			if (retCount == size) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 批处理插入notes表数据
	 * 
	 * @param orderNotesStr
	 * @param size
	 * @return
	 */
	public boolean insertNotesBatchData(String orderNotesStr, int size) {
		boolean ret = false;
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("values", orderNotesStr);
		
		try {
			int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_notes_insertNotesBatchData", dataMap);
			
			if (retCount == size) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 批处理插入交易明细表数据
	 * 
	 * @param transactionsStr
	 * @param size
	 * @return
	 */
	public boolean insertTransactionsBatchData(String transactionsStr, int size) {
		boolean ret = false;
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("values", transactionsStr);
		
		try {
			int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_transactions_insertTransactionsBatchData", dataMap);
			
			if (retCount == size) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 批处理插入oms_bt_group_orders表数据
	 * 
	 * @param ordersGroupBatchStr
	 * @param size
	 * @return
	 */
	public boolean insertGroupOrderBatchData(String ordersGroupBatchStr, int size) {
		boolean ret = false;
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("values", ordersGroupBatchStr);
		
		try {
			int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_group_orders_insertGroupOrderBatchData", dataMap);
			
			if (retCount == size) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 批处理插入oms_bt_payments表数据
	 * 
	 * @param paymentBatchStr
	 * @param size
	 * @return
	 */
	public boolean insertPaymentBatchData(String paymentBatchStr, int size) {
		boolean ret = false;
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("values", paymentBatchStr);
		
		try {
			int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_payments_insertPaymentBatchData", dataMap);
			
			if (retCount == size) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 批处理插入oms_bt_ext_orders表数据
	 * 
	 * @param extOrdersBatchStr
	 * @param size
	 * @return
	 */
	public boolean insertExtOrdersBatchData(String extOrdersBatchStr, int size) {
		boolean ret = false;
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("values", extOrdersBatchStr);
		
		try {
			int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_ext_orders_insertExtOrdersBatchData", dataMap);
			
			if (retCount == size) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 批处理插入oms_bt_ext_order_details表数据
	 * 
	 * @param extOrderDetailsBatchStr
	 * @param size
	 * @return
	 */
	public boolean insertExtOrderDetailsBatchData(String extOrderDetailsBatchStr, int size) {
		boolean ret = false;
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("values", extOrderDetailsBatchStr);
		
		try {
			int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_ext_order_details_insertExtOrderDetailsBatchData", dataMap);
			
			if (retCount == size) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 批处理更新新订单历史临时表发送标志
	 * 
	 * @param subSql
	 * @param size
	 * @param taskName
	 * @return
	 */
	public boolean resetNewHistoryOrders(String subSql, int size, String taskName) {
		boolean ret = false;
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("subSql", subSql);
		dataMap.put("taskName", taskName);
		
		try {
			int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_new_orders_import_history_resetNewHistoryOrders", dataMap);
			
			if (retCount == size) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 批处理更新手工新订单历史临时表发送标志
	 * 
	 * @param subSql
	 * @param size
	 * @param taskName
	 * @return
	 */
	public boolean resetNewManualHistoryOrders(String subSql, int size, String taskName) {
		boolean ret = false;
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("subSql", subSql);
		dataMap.put("taskName", taskName);
		
		try {
			int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_manual_new_orders_import_history_resetNewHistoryOrders", dataMap);
			
			if (retCount == size) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 批处理更新新订单历史临时表发送标志
	 * 
	 * @param subSql
	 * @param size
	 * @param taskName
	 * @return
	 */
	public boolean resetHistoryOrdersForeach(String subSql, int size, String taskName) {
		boolean ret = false;
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("subSql", subSql);
		dataMap.put("taskName", taskName);
		
		try {
			int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_new_orders_import_history_resetHistoryOrdersForeach", dataMap);
			
			if (retCount == size) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 批处理更新新订单历史临时表发送标志
	 * 
	 * @param subSql
	 * @param size
	 * @param taskName
	 * @return
	 */
	public boolean resetManualHistoryOrdersForeach(String subSql, int size, String taskName) {
		boolean ret = false;
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("subSql", subSql);
		dataMap.put("taskName", taskName);
		
		try {
			int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_manual_new_orders_import_history_resetManualHistoryOrdersForeach", dataMap);
			
			if (retCount == size) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 更新新订单历史临时表发送标志
	 * 
	 * @param subSql
	 * @param taskName
	 * @return
	 */
	public boolean resetHistoryOrdersForManual(String subSql, String taskName) {
		boolean ret = false;
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("subSql", subSql);
		dataMap.put("taskName", taskName);
		
		try {
			int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_new_orders_import_history_resetHistoryOrdersForManual", dataMap);
			
			if (retCount == 1) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 批处理更新订单表数据
	 * 
	 * @param ordersSqlValue
	 * @param confirmDate
	 * @return
	 */
	public boolean updateOrdersBatchData(String ordersSqlValue, String confirmDate) {
		boolean ret = false;
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("subSql", ordersSqlValue);
		dataMap.put("confirmDate", confirmDate);
		
		try {
			int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_updateOrdersBatchData", dataMap);
			
			if (retCount >= 0) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
//	/**
//	 * 插入订单表数据
//	 * 
//	 * @param dataMap
//	 * @return
//	 */
//	public boolean insertOrdersData(Map<String, String> dataMap) {
//		boolean ret = false;
//		
//		int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_insertOrdersData", dataMap);
//		
//		if (retCount == 1) {
//			ret = true;
//		}
//		
//		return ret;
//	}
//	
//	/**
//	 * 插入订单详细表数据
//	 * 
//	 * @param dataMap
//	 * @return
//	 */
//	public boolean insertOrderDetailsData(Map<String, String> dataMap) {
//		boolean ret = false;
//		
//		int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_insertOrderDetailsData", dataMap);
//		
//		if (retCount == 1) {
//			ret = true;
//		}
//		
//		return ret;
//	}
	
	/**
	 * 根据状态变化信息更新orders表
	 * 
	 * @param dataMap
	 * @return
	 */
	public boolean updateChangedOrdersData(Map<String, String> dataMap) {
		boolean ret = false;
		
		try {
			int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_updateChangedOrdersData", dataMap);
			
			if (retCount == 1) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 根据状态变化信息更新orderDetails表
	 * 
	 * @param dataMap
	 * @return
	 */
	public boolean updateChangedOrderDetailsData(Map<String, String> dataMap) {
		boolean ret = false;
		
		try {
			int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_updateChangedOrderDetailsData", dataMap);
			
			if (retCount >= 1) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 根据状态变化信息插入Notes表
	 * 
	 * @param dataMap
	 * @return
	 */
	public boolean insertChangedNotesData(Map<String, String> dataMap) {
		boolean ret = false;
		
		int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_notes_insertChangedNotesData", dataMap);
		
		try {
			if (retCount == 1) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 售中退款金额合计
	 * 
	 * @param changedOrderInfo
	 * @param sourceOrderId
	 * @return
	 */
	public double getOnSaleRefundTotalFee(ChangedOrderInfo4Log changedOrderInfo, String sourceOrderId) {
		double refundTotalFee = 0;
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("orderChannelId", changedOrderInfo.getOrderChannelId());
		dataMap.put("cartId", changedOrderInfo.getCartId());
		dataMap.put("tid", changedOrderInfo.getTid());
		dataMap.put("sourceOrderId", sourceOrderId);
		
		refundTotalFee = (double)selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_refunds_getOnSaleRefundTotalFee", dataMap);
		
		return refundTotalFee;
	}
	
	/**
	 * 根据交易成功信息插入Payments表
	 * 
	 * @param dataMap
	 * @return
	 */
	public boolean insertPaymentsData(Map<String, String> dataMap) {
		boolean ret = false;
		
		int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_payments_insertPaymentsData", dataMap);
		
		try {
			if (retCount == 1) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 更新组订单PaymentTotal
	 * 
	 * @param dataMap
	 * @return
	 */
	public boolean updateGroupPaymentTotal(Map<String, String> dataMap) {
		boolean ret = false;
		
		try {
			int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_group_orders_updateGroupPaymentTotal", dataMap);
			
			if (retCount == 1) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 更新oms_bt_order_refunds表的process_flag
	 * 
	 * @param dataMap
	 * @return
	 */
	public boolean updateRefundProcessFlag(Map<String, String> dataMap) {
		boolean ret = false;
		
		try {
			int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_refunds_updateRefundProcessFlag", dataMap);
			
			if (retCount == 1) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 更新组订单refund_total
	 * 
	 * @param dataMap
	 * @return
	 */
	public boolean updateGroupRefundTotal(Map<String, String> dataMap) {
		boolean ret = false;
		
		try {
			int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_group_orders_updateGroupRefundTotal", dataMap);
			
			if (retCount == 1) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 关闭退款时更新组订单refund_total
	 * 
	 * @param dataMap
	 * @return
	 */
	public boolean updateGroupRefundTotal4Closed(Map<String, String> dataMap) {
		boolean ret = false;
		
		try {
			int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_group_orders_updateGroupRefundTotal4Closed", dataMap);
			
			if (retCount == 1) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 退款创建信息插入oms_bt_order_refunds表
	 * 
	 * @param dataMap
	 * @return
	 */
	public boolean insertRefundData(Map<String, String> dataMap) {
		boolean ret = false;
		
		int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_refunds_insertRefundData", dataMap);
		
		try {
			if (retCount == 1) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 买家修改退款协议信息更新oms_bt_order_refunds表
	 * 
	 * @param dataMap
	 * @return
	 */
	public boolean updateRefundData(Map<String, String> dataMap) {
		boolean ret = false;
		
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_refunds_updateRefundData", dataMap);
		
		try {
			if (retCount == 1) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 锁单操作
	 * 
	 * @param dataMap
	 * @return
	 */
	public boolean lockOrders(Map<String, String> dataMap) {
		boolean ret = false;
		
		try {
			int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_lockOrders", dataMap);
			
			if (retCount > 0) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 置位订单状态变化历史临时表发送标志
	 * 
	 * @param dataMap
	 * @return
	 */
	public boolean resetChangedHistoryOrders(Map<String, String> dataMap) {
		boolean ret = false;
		
		try {
			int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_changed_orders_import_history_resetChangedHistoryOrders", dataMap);
			
			if (retCount == 1) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 锁定synship tt_orders表
	 * 
	 * @param dataMap
	 * @return
	 */
	public boolean updateSynShipOrdersLock(Map<String, String> dataMap) {
		boolean ret = false;
		
		try {
			int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "tt_orders_updateSynShipOrdersLock", dataMap);
			
			if (retCount >= 1) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 根据状态变化信息更新synship tt_orders表
	 * 
	 * @param dataMap
	 * @return
	 */
	public boolean updateSynShipOrdersStatus(Map<String, String> dataMap) {
		boolean ret = false;
		
		try {
			int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "tt_orders_updateSynShipOrdersStatus", dataMap);
			
			if (retCount >= 1) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 从OMS取出需要同步到synship的订单记录
	 * 
	 * @param dataMap
	 * @return
	 */
	public List<Map<String, Object>> getSynShipNewOrderInfo() {
		List<Map<String, Object>> dataMapList = null;
		try {
			dataMapList = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_getSynShipNewOrderInfo");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return dataMapList;
	}
	
	/**
	 * 批处理插入oms_having_gifted_customer_setting表数据（字符串拼装）
	 * 
	 * @param giftedCustomerBatchStr
	 * @param size
	 * @return
	 */
	public boolean recordHavingGiftedCustomerInfo(String giftedCustomerBatchStr, int size) {
		boolean ret = false;
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("values", giftedCustomerBatchStr);
		
		try {
			int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_having_gifted_customer_setting_recordHavingGiftedCustomerInfo", dataMap);
			
			if (retCount == size) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}

	/**
	 * 本轮订单插入结束之后回写满就送配置表里的赠品剩余库存
	 *
	 * @param paraMap
	 * @return
	 */
	public boolean recordPriceThanGiftInventory(Map<String, String> paraMap) {
		boolean ret = false;

		try {
			int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_price_than_gift_setting_recordPriceThanGiftInventory", paraMap);
			ret = true;

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);

			ret = false;
		}

		return ret;
	}
	
	/**
	 * 置位synship发送标志
	 * 
	 * @param subSql
	 * @param size
	 * @param taskName
	 * @return
	 */
	public int resetSynShipFlag(Map<String, String> dataMap) {
		int retCount = 0;
		
		try {
			retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_resetSynShipFlag", dataMap);
			
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return retCount;
	}

	/**
	 *  获得已经导入新订单history表最新订单时间
	 * @return
	 */
	public String getLastHistoryNewOrderTime(Map<String, String> paraMap) {
		String lastOrderTime = selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_new_orders_import_history_getLastHistoryNewOrderTime", paraMap);
		
		return lastOrderTime;
	}
	
	/**
	 *  获得已经导入新订单history表最新订单时间(聚美)
	 * @return
	 */
	public String getLastHistoryNewOrderTime4Jumei(Map<String, String> paraMap) {
		String lastOrderTime = selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_new_orders_import_history_getLastHistoryNewOrderTime4Jumei", paraMap);
		
		return lastOrderTime;
	}
	
	/**
	 *  获得已经导入状态变化订单history表最新订单时间
	 * @return
	 */
	public String getLastHistoryChangedOrderTime(Map<String, String> paraMap) {
		String lastOrderTime = selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_new_orders_import_history_getLastHistoryChangedOrderTime", paraMap);
		
		return lastOrderTime;
	}
	
	/**
	 * Description 获取已经approved的订单信息
	 * @author sky
	 * @param dataMap
	 * @return List OutFormReservationInfo对象类型
	 */
	public List<OutFormReservationInfo> getReservationInfo(String channel,int intRowCount) {

		Map<String,Object> param=new HashMap<String,Object>();
		param.put("order_channel_id", channel);
		param.put("intRowCount", intRowCount);

		List<OutFormReservationInfo> reservationInfo = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "wsdl_transfer_reservationInfo", param);
		return reservationInfo;
	}

	/**
	 * Description 根据调用C#webServivce返回的ReservationID写到 oms_bt_order_details表的对应记录
	 * @author sky
	 * @param dataMap
	 * @return boolean
	 */
	public boolean setReservationId(String sql) {
		boolean ret = false;
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("strSql", sql);
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "wsdl_update_reservationId", dataMap);
		if (retCount > 0) {
			ret = true;
		}
		return ret;
	}
	 /**
     * 日报发送
     * @param fromTime
     * @param endTime
     * @return
     */
	public List<OutFormSendOrderMail> sendOrderMail(String fromTime, String endTime, String channelId, String cartId) {
		List<OutFormSendOrderMail>  list=new ArrayList<OutFormSendOrderMail>();
		Map<String,String> param=new HashMap<String,String>();
		param.put("fromTime", fromTime);
		param.put("endTime", endTime);
		param.put("channelId", channelId);
		param.put("cartId", cartId);
		list=(List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_sendOrderMail", param);
		return list;
	}
    /**
     * 订单日报sku统计
     * @param fromTime
     * @param endTime
     * @return
     */
	public List<Map<String,Object>> sendOrderMailTotal(String fromTime, String endTime,String channelId ) {
		List<Map<String,Object>>  list=new ArrayList<Map<String,Object>>();
		Map<String,String> param=new HashMap<String,String>();
		param.put("fromTime", fromTime);
		param.put("endTime", endTime);
		param.put("channelId", channelId);
		list=(List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_sendOrderMailTotal", param);
		return list;
	}
	/**
	 * 订单日报订单量统计
	 * @param fromTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String,Object>> sendOrderMailOrderTotal(String fromTime, String endTime, String channelId) {
		List<Map<String,Object>>  list=new ArrayList<Map<String,Object>>();
		Map<String,String> param=new HashMap<String,String>();
		param.put("fromTime", fromTime);
		param.put("endTime", endTime);
		param.put("channelId", channelId);
		list=(List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt-orders_sendOrderMailOrderTotal", param);
		return list;
	}
	/**
	 * 
	 * @param dataMap
	 * @return
	 */
	public List<Map<String, Object>> getOrderInfoByTid(Map<String, String> dataMap){
		List<Map<String, Object>> dataMapList = null;
		try {
			dataMapList = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_new_orders_import_history_getOrderInfoByTid", dataMap);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return dataMapList;
		
	}
	/**
	 * 根据cartId ChannelId 和时间找出改时间段的所有订单
	 * @param dataMap
	 * @return
	 */
	public List<String> getOrderByChannelId(Map<String, Object> dataMap){
		List<String> dataMapList = null;
		try {
			dataMapList = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_getSourceNumberByChannelId", dataMap);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return dataMapList;
		
	}
   public List<Map<String,Object>> getSameSourceOrderId(){
	 
	   List<Map<String, Object>> dataMapList = null;
		try {
			dataMapList = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_getSameSourceOrderId");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return dataMapList;
	   
   }
   
	/**
	 * 获取满就送中排除的买就送商品
	 * @param orderChannelId
	 * @param cartId
	 * @return
	 */
//	public List<String> getExceptItemOfBuyThanGift(String orderChannelId, String cartId){
//		Map<String, String> dataMap = new HashMap<String, String>();
//		dataMap.put("orderChannelId", orderChannelId);
//		dataMap.put("cartId", cartId);
//
//		List<String> itemOfBuyThanGift = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_getItemOfBuyThanGift", dataMap);
//
//		return itemOfBuyThanGift;
//	}

//	/**
//	 * 获取买就送赠品    废止
//	 * @param orderChannelId
//	 * @param cartId
//	 * @param skuTotalList
//	 * @return
//	 */
//	public List<String> getBuyThanGiftBySku(String orderChannelId, String cartId, List<String> skuTotalList) {
//		List<String> buyThanGiftSku = new ArrayList<String>();
//		
//		if (skuTotalList != null && skuTotalList.size() > 0) {
//			Map<String, Object> dataMap = new HashMap<String, Object>();
//			dataMap.put("orderChannelId", orderChannelId);
//			dataMap.put("cartId", cartId);
//			dataMap.put("skuTotalList", skuTotalList);
//			
//			buyThanGiftSku = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_buy_than_gift_setting_getBuyThanGiftBySku", dataMap);
//		}
//
//		return buyThanGiftSku;
//	}
	
	/**
	 * 获取买就送赠品
	 * 
	 * @return
	 */
	public List<Map<String, String>> getBuyThanGiftSetting() {
		List<Map<String, String>> buyThanGiftList = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_buy_than_gift_setting_getBuyThanGiftSetting");

		return buyThanGiftList;
	}
	
	/**
	 * 获取满就送排除商品
	 * 
	 * @return
	 */
	public List<Map<String, String>> getPriceThanGiftExceptSetting() {
		List<Map<String, String>> priceThanGiftExceptList = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_price_than_gift_except_setting_getPriceThanGiftExceptSetting");

		return priceThanGiftExceptList;
	}
	
	/**
	 * 获取满就送商品
	 * 
	 * @return
	 */
	public List<Map<String, String>> getPriceThanGiftSetting() {

		List<Map<String, String>> priceThanGiftSettingList = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_price_than_gift_setting_getPriceThanGiftSetting");

		return priceThanGiftSettingList;
	}
	
	/**
	 * 老顾客送赠品
	 * 
	 * @return
	 */
	public List<Map<String, String>> getRegularCustomerGiftSetting() {

		List<Map<String, String>> regularCustomerGiftList = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_regular_customer_gift_setting_getRegularCustomerGiftSetting");

		return regularCustomerGiftList;
	}
	
	/**
	 * 优先下单前多少名赠品设定
	 * 
	 * @return
	 */
	public List<Map<String, String>> getPriorCountGiftSetting() {

		List<Map<String, String>> priorCountGiftList = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_prior_count_gift_setting_getPriorCountGiftSetting");

		return priorCountGiftList;
	}
	
	/**
	 * 已获赠品顾客信息
	 * 
	 * @return
	 */
	public List<Map<String, String>> getHavingGiftedCustomerInfo() {

		List<Map<String, String>> havingGiftedCustomerInfoList = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_having_gifted_customer_setting_getHavingGiftedCustomerInfo");

		return havingGiftedCustomerInfoList;
	}
	
	/**
	 * 赠品类型信息设置表获得
	 * 
	 * @return
	 */
	public List<Map<String, String>> getGiftedPropertySetting() {

		List<Map<String, String>> giftPropertyInfoList = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_gifted_property_setting_getGiftedPropertySetting");

		return giftPropertyInfoList;
	}
	
//	/**
//	 * 获取满就送金额限制    废止
//	 * @param orderChannelId
//	 * @param cartId
//	 * @return
//	 */
//	public List<Double> getPriceOfPriceThanGift(String orderChannelId, String cartId){
//		Map<String, String> dataMap = new HashMap<String, String>();
//		dataMap.put("orderChannelId", orderChannelId);
//		dataMap.put("cartId", cartId);
//
//		List<Double> price = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_price_than_gift_setting_getPriceOfPriceThanGift", dataMap);
//
//		return price;
//	}

//	/**
//	 * 获取满就送商品      废止
//	 * @param orderChannelId
//	 * @param cartId
//	 * @param price
//	 * @return
//	 */
//	public List<String> getPriceThanGiftByPrice(String orderChannelId, String cartId, double price){
//		Map<String, String> dataMap = new HashMap<String, String>();
//		dataMap.put("orderChannelId", orderChannelId);
//		dataMap.put("cartId", cartId);
//		dataMap.put("price", Double.toString(price));
//
//		List<String> giftSku = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_price_than_gift_setting_getPriceThanGiftByPrice", dataMap);
//
//		return giftSku;
//	}

//	/**
//	 * 获取满就送排除商品   废止
//	 * @param orderChannelId
//	 * @param cartId
//	 * @return
//	 */
//	public List<String> getExceptOfPriceThanGift(String orderChannelId, String cartId){
//		Map<String, String> dataMap = new HashMap<String, String>();
//		dataMap.put("orderChannelId", orderChannelId);
//		dataMap.put("cartId", cartId);
//
//		List<String> exceptOfPriceThanGift = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_price_than_gift_except_setting_getExceptOfPriceThanGift", dataMap);
//
//		return exceptOfPriceThanGift;
//	}
	
//	/**
//	 * 获取老顾客赠品   废止
//	 * 
//	 * @param orderChannelId
//	 * @param cartId
//	 * @return
//	 */
//	public List<String> getRegularCustomerGifts(String orderChannelId, String cartId){
//		Map<String, String> dataMap = new HashMap<String, String>();
//		dataMap.put("orderChannelId", orderChannelId);
//		dataMap.put("cartId", cartId);
//
//		List<String> regularCustomerGiftSku = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_regular_customer_gift_setting_getRegularCustomerGifts", dataMap);
//
//		return regularCustomerGiftSku;
//	}

	/**
	 * 套装内容设置获得
	 *
	 * @return
	 */
	public List<Map<String, String>> getSuitSkuSetting() {

		List<Map<String, String>> suitInfoList = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_suit_sku_setting_getSuitSkuSetting");

		return suitInfoList;
	}

	/**
	 * 获取独立域名已发货订单信息
	 * 
	 * @return
	 */
	public List<Map> getSelfShippedOrderInfo(String orderChannelId, String cartId) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("orderChannelId", orderChannelId);
		dataMap.put("cartId", cartId);
		
		List<Map> selfShippedOrderInfo = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_getSelfShippedOrderInfo", dataMap);
		
		return selfShippedOrderInfo;
	}
	
	/**
	 * 获取独立域名已取消原始订单信息
	 * 
	 * @return
	 */
	public List<Map> getSelfCanceledOriginalOrderInfo(String orderChannelId, String cartId) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("orderChannelId", orderChannelId);
		dataMap.put("cartId", cartId);
		
		List<Map> selfCanceledOriginalOrderInfo = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_getSelfCanceledOriginalOrderInfo", dataMap);
		
		return selfCanceledOriginalOrderInfo;
	}
	
	/**
	 * 获取独立域名已取消原始订单对应 子订单没有取消信息
	 * 
	 * @return
	 */
	public List<Map> getSelfCanceledChildOrderInfo(String orderChannelId, String cartId) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("orderChannelId", orderChannelId);
		dataMap.put("cartId", cartId);
		
		List<Map> selfCanceledChildOrderInfo = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_getSelfCanceledChildOrderInfo", dataMap);
		
		return selfCanceledChildOrderInfo;
	}
	
	/**
	 * 置位oms_bt_group_orders发送独立域名标志
	 * 
	 * @param dataMap
	 * @return
	 */
	public boolean resetGroupSelfSendFlag(Map<String, String> dataMap) {
		boolean ret = false;
		
		try {
			int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_group_orders_resetGroupSelfSendFlag", dataMap);
			
			if (retCount == 1) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
	
	/**
	 * 获得京东、京东国际需推送短链接的订单信息
	 * 
	 * @param orderChannelId
	 * @param cartIdList
	 * @return
	 */
	public List<TaobaoTradeBean> getNeedShortUrlJdNewOrder(String orderChannelId, List<String> cartIdList) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("orderChannelId", orderChannelId);
		if (cartIdList != null && cartIdList.size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(com.voyageone.batch.core.Constants.LEFT_BRACKET_CHAR);
			for (int i = 0; i < cartIdList.size(); i++) {
				String cartId = cartIdList.get(i);
				sb.append(com.voyageone.batch.core.Constants.APOSTROPHE_CHAR);
				sb.append(cartId);
				sb.append(com.voyageone.batch.core.Constants.APOSTROPHE_CHAR);
				if (i < (cartIdList.size() - 1)) {
					sb.append(com.voyageone.batch.core.Constants.COMMA_CHAR);
				}
			}
			sb.append(com.voyageone.batch.core.Constants.RIGHT_BRACKET_CHAR);
			dataMap.put("cartId", sb.toString());
		}
		
		List<TaobaoTradeBean> needShortUrlJdNewOrderList = 
				(List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_new_orders_import_history_getNeedShortUrlJdNewOrder", dataMap);
		
		return needShortUrlJdNewOrderList;
	}
	
	/**
	 * 获得聚美需推送短链接的订单信息
	 * 
	 * @param cartIdList
	 * @return
	 */
	public List<TaobaoTradeBean> getNeedShortUrlJmNewOrder(List<String> cartIdList) {
		Map<String, String> dataMap = new HashMap<String, String>();
		if (cartIdList != null && cartIdList.size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(com.voyageone.batch.core.Constants.LEFT_BRACKET_CHAR);
			for (int i = 0; i < cartIdList.size(); i++) {
				String cartId = cartIdList.get(i);
				sb.append(com.voyageone.batch.core.Constants.APOSTROPHE_CHAR);
				sb.append(cartId);
				sb.append(com.voyageone.batch.core.Constants.APOSTROPHE_CHAR);
				if (i < (cartIdList.size() - 1)) {
					sb.append(com.voyageone.batch.core.Constants.COMMA_CHAR);
				}
			}
			sb.append(com.voyageone.batch.core.Constants.RIGHT_BRACKET_CHAR);
			dataMap.put("cartId", sb.toString());
		}
		
		List<TaobaoTradeBean> needShortUrlJdNewOrderList = 
				(List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_new_orders_import_history_getNeedShortUrlJmNewOrder", dataMap);
		
		return needShortUrlJdNewOrderList;
	}
	
	/**
	 * 置位oms_new_orders_import_history发送短域名标志
	 * 
	 * @param trade
	 * @param taskName
	 * @return
	 */
	public boolean resetShortUrlSendFlag(TaobaoTradeBean trade, String taskName) {
		boolean ret = false;
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("order_channel_id", trade.getOrder_channel_id());
		dataMap.put("cart_id", trade.getCartId());
		dataMap.put("tid", trade.getTid());
		dataMap.put("taskName", taskName);
		
		try {
			int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_new_orders_import_history_resetShortUrlSendFlag", dataMap);
			
			if (retCount == 1) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}

	/**
	 * 获得推送 正常订单信息
	 *
	 * @return
	 */
	public List<OrderExtend> getPushOrdersInfo(String orderChannelId) {
		List<OrderExtend> ordersInfo = (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_getPushNormalOrdersInfo", orderChannelId);

		return ordersInfo;
	}

	/**
	 * 获得推送 取消订单信息
	 *
	 * @return
	 */
	public List<OrderExtend> getPendingCancelOrdersInfo(String orderChannelId) {
		List<OrderExtend> ordersInfo = (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_getPushPendingCancelOrdersInfo", orderChannelId);

		return ordersInfo;
	}

	/**
	 * 获得推送 退货订单信息
	 *
	 * @return
	 */
	public List<OrderExtend> getPushReturnOrdersInfo(String orderChannelId) {
		List<OrderExtend> ordersInfo = (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_getPushReturnOrdersInfo", orderChannelId);

		return ordersInfo;
	}

	/**
	 * 获得推送 BCBG(not Shipped)订单信息
	 *
	 * @return
	 */
	public List<OrderExtend> getPushBCBGDemandsInfo(String orderChannelId, String beginSearchDate, String endSearchDate) {
		HashMap<String, String> inPara = new HashMap<String, String>();
		inPara.put("orderChannelId", orderChannelId);
		inPara.put("beginDate", beginSearchDate);
		inPara.put("endDate", endSearchDate);

		List<OrderExtend> ordersInfo = (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_getPushBCBGDemandsInfo", inPara);

		return ordersInfo;
	}

	/**
	 * 获得推送 BCBG(Shipped)订单信息
	 *
	 * @return
	 */
	public List<OrderExtend> getPushBCBGDailySalesInfo(String orderChannelId, String beginSearchDate, String endSearchDate) {
		HashMap<String, String> inPara = new HashMap<String, String>();
		inPara.put("orderChannelId", orderChannelId);
		inPara.put("beginDate", beginSearchDate);
		inPara.put("endDate", endSearchDate);

		List<OrderExtend> ordersInfo = (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_getPushBCBGDailySalesInfo", inPara);

		return ordersInfo;
	}

	/**
	 * 订单信息更新（发送标志）
	 *
	 * @return
	 */
	public boolean updateOrdersSendInfo(String taskName, List<String> orderNumberList) {
		boolean ret = false;

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("taskName", taskName);
		paraIn.put("orderNumberList", orderNumberList);

		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_ext_orders_updateSendFlag", paraIn);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 订单信息更新（取消订单发送标志）
	 *
	 * @return
	 */
	public boolean updatePendingCancelOrdersSendInfo(String taskName, List<String> orderNumberList) {
		boolean ret = false;

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("taskName", taskName);
		paraIn.put("orderNumberList", orderNumberList);

		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_ext_orders_updatePendingCancelSendFlag", paraIn);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 订单信息更新（ExtFlg1）
	 *
	 * @return
	 */
	public boolean updateOrderExtFlg1(String taskName, List<String> orderNumberList) {
		boolean ret = false;

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("taskName", taskName);
		paraIn.put("orderNumberList", orderNumberList);

		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_ext_orders_updateExtFlg1", paraIn);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 订单信息更新（Return订单发送标志）
	 *
	 * @return
	 */
	public boolean updateReturnOrdersSendInfo(String taskName, String orderNumber, String itemNumber) {
		boolean ret = false;

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("taskName", taskName);
		paraIn.put("orderNumber", orderNumber);
		paraIn.put("itemNumber", itemNumber);

		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_ext_order_details_updateReturnSendFlag", paraIn);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 订单信息更新（第三方取消订单标志）
	 *
	 * @return
	 */
	public boolean updatePaternCancelOrdersInfo(String taskName, List<String> sourceOrderIdList) {
		boolean ret = false;

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("taskName", taskName);
		paraIn.put("sourceOrderIdList", sourceOrderIdList);

		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_ext_orders_updatePaternCancelFlag", paraIn);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 获得订单详细信息，根据一组（order_number）
	 *
	 * @return
	 */
	public List<OutFormOrderDetailOrderDetail> getOrderDetailsInfo(String orderChannelId, List<String> orderNumberList) {

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("orderChannelId", orderChannelId);
		paraIn.put("orderNumberList", orderNumberList);

		List<OutFormOrderDetailOrderDetail> orderDetailsList =  (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_details_getOrderDetailsInfoByOrderNumList", paraIn);

		return orderDetailsList;
	}

	/**
	 * 获得订单信息，根据（source_order_id）
	 *
	 * @return
	 */
	public List<OutFormOrderdetailOrders> getOrdersListByOrderChannelId(String orderChannelId) {
		List<OutFormOrderdetailOrders> ordersList = (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_getOrdersListByOrderChannelId", orderChannelId);

		return ordersList;
	}

	/**
	 * 获得订单信息，根据一组（sourcer_order_id）
	 *
	 * @return
	 */
	public List<OrderExtend> getOrdersInfo(List<String> sourceOrderIdList) {

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("sourceOrderIdList", sourceOrderIdList);

		List<OrderExtend> ordersList =  (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_getOrderInfoBySourceOrderId", paraIn);

		return ordersList;
	}

	/**
	 * 获得订单信息，根据一组（sourcer_order_id）
	 *
	 * @return
	 */
	public List<OrderExtend> getOrdersInfoForShipping(List<InFormFile> sourceOrderIdList) {

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("sourceOrderIdList", sourceOrderIdList);

		List<OrderExtend> ordersList =  (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_getOrdersInfoForShipping", paraIn);

		return ordersList;
	}
	
	/**
	 *  聚美重复订单判断
	 *  
	 * @return
	 */
	public boolean isJuMeiOrderExist(String orderNumber, String cartId) {
		boolean isExist = false;
		
		int count = 0;
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("orderNumber", orderNumber + "%");
		dataMap.put("cartId", cartId);
		
		try {
			count = (int)selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_new_orders_import_history_isJuMeiOrderExist", dataMap);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		if (count > 0) {
			isExist = true;
		}
		
		return isExist;
	}
	
	/**
	 * 消费前count顾客
	 * 
	 * @return
	 */
	public List<Map<String, String>> getSneakerheadTopSpendingRanking(String orderChannelId, int count) {
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("orderChannelId", orderChannelId);
		paraMap.put("topCount", String.valueOf(count));
		List<Map<String, String>> topMapList = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_orders_getSpendingTop", paraMap);

		return topMapList;
	}
	
	/**
	 * 根据省市名获得邮编
	 * 
	 * @param state
	 * @param city
	 * @return
	 */
	public String getZip(String state, String city) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("state", state);
		dataMap.put("city", city);
		
		String zip = Constants.EmptyString;
		
		List<String> zipList = selectList(Constants.DAO_NAME_SPACE_OMS + "tm_zip_getZip", dataMap);
		if (zipList != null && zipList.size() > 0) {
			zip = zipList.get(0);
		}
		
		return zip;
	}
}
