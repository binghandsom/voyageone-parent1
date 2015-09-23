package com.voyageone.batch.oms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;

@Repository
public class RefundDao extends BaseDao {
	private Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * 获得未关闭退款订单
	 * @param orderChannelId
	 * @param cartId
	 * @return
	 */
	public List<String> getUnclosedRefundId(String orderChannelId, String cartId){
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("orderChannelId", orderChannelId);
		dataMap.put("cartId", cartId);
		
		List<String> refundOrders = selectList(Constants.DAO_NAME_SPACE_OMS + "oms_changed_orders_import_history_getUnclosedRefund", dataMap);
		
		return refundOrders;
	}
	
	/**
	 *  获得已经导入订单history表最新订单时间
	 * @return
	 */
	public String getJdLastHistoryRefundOrderTime(Map<String, String> paraMap) {
		String lastOrderTime = selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_changed_orders_import_history_getJdLastHistoryRefundOrderTime", paraMap);
		
		return lastOrderTime;
	}

	public boolean insertRefundOrdersInfo(String ordersSqlValue, int size) {
		boolean ret = false;
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("values", ordersSqlValue);
		
		try {
			int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_changed_orders_import_history_insertRefundOrdersInfo", dataMap);
			if (retCount == size) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		return ret;
	}
}
