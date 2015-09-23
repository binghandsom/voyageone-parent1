package com.voyageone.batch.oms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.oms.modelbean.CustomerBean;
import com.voyageone.batch.oms.modelbean.NewOrderInfo4Log;
import com.voyageone.common.Constants;

@Repository
public class CustomerDao extends BaseDao {
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * 批处理插入顾客表数据(字符串拼接)
	 * 
	 * @param customerSqlValue
	 * @return
	 */
	public boolean insertCustomerBatchData(String customerSqlValue) {
		boolean ret = true;
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("values", customerSqlValue);
		
		try {
			updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_customer_insertCustomerBatchData_Str", dataMap);
		
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			
			ret = false;
		}
		
		return ret;
	}
	
	/**
	 * 批处理插入顾客表数据(集合)
	 * 
	 * @param newOrderInfoList
	 * @return
	 */
	public boolean insertCustomerBatchData(List<NewOrderInfo4Log> newOrderInfoList) {
		boolean ret = false;
		
		int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_customer_insertCustomerBatchData", newOrderInfoList);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 插入顾客表数据
	 * 
	 * @param dataMap
	 * @return
	 */
	public boolean insertCustomerData(Map<String, String> dataMap) {
		boolean ret = true;
		
		try {
			updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_customer_insertCustomerData", dataMap);
		
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			
			ret = false;
		}
		
		return ret;
	}
	
	/**
	 * 获得一批订单对应的顾客Id列表
	 * 
	 * @param customerMapList
	 * @return
	 */
	public List<String> selectCustomerIdList(List<Map<String, String>> customerMapList) {
		
		List<String> customerIdList = (List)selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_customer_selectCustomerIdList", customerMapList);
		
		return customerIdList;
	}
	
	/**
	 * 获得订单对应的顾客Id
	 * 
	 * @param customerMap
	 * @return
	 */
	public String selectCustomerId(Map<String, String> customerMap) {
		String customerId = null;
		try {
			customerId = (String)selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_bt_customer_selectCustomerId", customerMap);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		
		if (customerId == null) {
			customerId = "";
		}
		
		return customerId;
	}
	
	/**
	 * 获取订单的顾客在顾客表中是否已经存在
	 * 
	 * @param customer
	 * @return
	 */
	public boolean getRegularCustomer(Map<String, String> dataMap) {
		boolean ret = false;
		
		try {
			int size = (Integer)selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_bt_customer_getRegularCustomer", dataMap);
			if (size >= 1) {
				ret = true;
			}
		
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			
			ret = false;
		}
		
		return ret;
	}
	
}
