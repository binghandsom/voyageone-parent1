package com.voyageone.oms.dao;

import java.util.List;

import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.oms.formbean.InFormAddNewOrderCustomer;
import com.voyageone.oms.formbean.OutFormCustomer;
import com.voyageone.oms.formbean.OutFormCustomerNotes;
import com.voyageone.oms.formbean.OutFormCustomerOrders;
import com.voyageone.oms.formbean.OutFormCustomerTransactions;
import com.voyageone.oms.modelbean.NotesBean;

@Repository
public class CustomerSearchDao extends BaseDao {

	/**
	 * 获得客户数量
	 * @return
	 */
	public int getCustomerCount(InFormAddNewOrderCustomer inFormAddNewOrderCustomer) {
		int customersCount = (int) selectOne(
				Constants.DAO_NAME_SPACE_OMS + "oms_customer_getCustomerCount",
				inFormAddNewOrderCustomer);
		return customersCount;
	}
	
	/**
	 * 获得客户信息
	 * @return
	 */
	public List<OutFormCustomer> getCustomerList(InFormAddNewOrderCustomer inFormAddNewOrderCustomer) {
		List<OutFormCustomer> customersList = (List) selectList(
				Constants.DAO_NAME_SPACE_OMS + "oms_customer_getCustomerInfo",
				inFormAddNewOrderCustomer);
		return customersList;
	}

	/**
	 * 获得客户订单信息
	 * @return
	 */
	public List<OutFormCustomerOrders> getCustomerOrderList(String customerId) {
		List<OutFormCustomerOrders> customersOrderList = (List) selectList(
				Constants.DAO_NAME_SPACE_OMS + "oms_customer_getCustomerOrders", customerId);
		return customersOrderList;
    }

	/**
	 * 获得客户交易信息
	 * @return
	 */
	public List<OutFormCustomerTransactions> getCustomerTransactionList(String customerId) {
		List<OutFormCustomerTransactions> customersOrderList = (List) selectList(
				Constants.DAO_NAME_SPACE_OMS + "oms_customer_getCustomerTransactions", customerId);
		return customersOrderList;
	}
	
	/**
	 * 获得客户Notes
	 * @return
	 */
	public List<OutFormCustomerNotes> getCustomerNotesList(String customerId) {
		List<OutFormCustomerNotes> customersOrderList = (List) selectList(
				Constants.DAO_NAME_SPACE_OMS + "oms_customer_getCustomerNotes", customerId);
		return customersOrderList;
	}

	/**
	 * 添加客户Notes
	 * @return
	 */
	public boolean saveNotes(NotesBean notesBean) {
		boolean ret = false;
		int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_customer_addNotes", notesBean);
		if (retCount > 0) {
			ret = true;
		}
		return ret;
	}

	/**
	 * 编辑客户Notes
	 * @return
	 */
	public boolean updateNotes(NotesBean notesBean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_customer_updateNotes", notesBean);
		if (retCount > 0) {
			ret = true;
		}
		return ret;
	}
}
