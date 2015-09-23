package com.voyageone.oms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.oms.formbean.InFormAddNewOrderCustomer;
import com.voyageone.oms.formbean.OutFormAddNewOrderCustomer;
import com.voyageone.oms.modelbean.CustomerBean;

@Repository
public class CustomerDao extends BaseDao {
	
	/**
	 * 获得客户信息
	 * 
	 * @return
	 */
	public OutFormAddNewOrderCustomer getCustomerInfo(String orderNumber) {
		OutFormAddNewOrderCustomer customerInfo = (OutFormAddNewOrderCustomer) selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_bt_customer_getCustomerInfo", orderNumber);
		
		return customerInfo;
	}
	
	/**
	 * 获得客户信息
	 * 
	 * @return
	 */
	public CustomerBean getCustomerInfo(String last_name, String phone, String cart_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("last_name", last_name);
		params.put("phone", phone);
		params.put("cart_id", cart_id);
		
		CustomerBean customerInfo = (CustomerBean) selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_bt_customer_getCustomerInfoByKey", params);
		
		return customerInfo;
	}
	
	/**
	 * 客户信息追加
	 * 
	 * @return
	 */
	public boolean insertCustomerInfo(CustomerBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_customer_insertCustomerInfo", bean);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 客户信息更新
	 * 
	 * @return
	 */
	public boolean updateCustomerInfo(CustomerBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_customer_updateCustomerInfo", bean);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * 获得客户信息
	 * 
	 * @return
	 */
	public List<OutFormAddNewOrderCustomer> getCustomersList(InFormAddNewOrderCustomer inFormAddNewOrderCustomer) {
		List<OutFormAddNewOrderCustomer> customersList =  (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_customer_getCustomerInfoForSearch", inFormAddNewOrderCustomer);
		
		return customersList;
	}
	/**
	 * 
	 * @param customerId
	 * @return
	 */
	public OutFormAddNewOrderCustomer getcustomerInfoforSold(String customerId) {
		OutFormAddNewOrderCustomer customerInfo = (OutFormAddNewOrderCustomer) selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_bt_customer_customerInfo", customerId);
		
		return customerInfo;
	}
}
