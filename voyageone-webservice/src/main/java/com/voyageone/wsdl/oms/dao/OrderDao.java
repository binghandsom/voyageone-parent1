package com.voyageone.wsdl.oms.dao;

import java.util.Map;

import com.voyageone.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import com.voyageone.wsdl.core.Constants;

@Repository
public class OrderDao extends BaseDao {
	
	/**
	 * 插入新付款订单json信息
	 * 
	 * @return
	 */
	public boolean insertNewOrdersJsonInfo(Map<String, Object> dataMap) {
		boolean ret = false;
		
		int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_new_order_info_json_insertNewOrdersJsonInfo", dataMap);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 插入订单状态变化json信息
	 * 
	 * @param dataMap
	 * @return
	 */
	public boolean insertChangedOrdersJsonInfo(Map<String, Object> dataMap) {
		boolean ret = false;
		
		int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_changed_order_info_json_insertChangedOrdersInfo", dataMap);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}
	
}
