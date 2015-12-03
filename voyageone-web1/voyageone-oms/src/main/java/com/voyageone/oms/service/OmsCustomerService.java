package com.voyageone.oms.service;

import java.util.List;

import com.voyageone.oms.formbean.InFormAddNewOrderCustomer;
import com.voyageone.oms.formbean.OutFormCustomer;
import com.voyageone.oms.formbean.OutFormCustomerNotes;
import com.voyageone.oms.formbean.OutFormCustomerOrders;
import com.voyageone.oms.formbean.OutFormCustomerTransactions;
import com.voyageone.oms.modelbean.NotesBean;

/**
 * OMS 客户明细检索service
 * 
 * @author sky
 *
 */
public interface OmsCustomerService {

	/**
	 * 检索客户信息
	 * 
	 * @return
	 */
	public int getCustomersCount(InFormAddNewOrderCustomer inFormAddNewOrderCustomer);

	/**
	 * 检索客户信息
	 * 
	 * @return
	 */
	public List<OutFormCustomer> getCustomersList(InFormAddNewOrderCustomer inFormAddNewOrderCustomer);

	/**
	 * 检索客户订单信息
	 * 
	 * @return
	 */
	public List<OutFormCustomerOrders> getCustomerOrderList(String customerNumber);

	/**
	 * 检索客户交易信息
	 * 
	 * @return
	 */
	public List<OutFormCustomerTransactions> getCustomerTransactionList(String customerId);

	/**
	 * 添加客户Notes
	 * 
	 * @return
	 */
	public boolean saveCustomerNotes(NotesBean notesBean);

	/**
	 * 获得客户Notes
	 * 
	 * @return
	 */
	public List<OutFormCustomerNotes> getCustomerNotesList(String customerId);

	/**
	 * 编辑客户Notes
	 * 
	 * @return
	 */
	public boolean updateCustomerNotes(NotesBean notesBean);

}
