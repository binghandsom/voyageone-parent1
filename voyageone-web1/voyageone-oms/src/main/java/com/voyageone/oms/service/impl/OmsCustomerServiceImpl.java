package com.voyageone.oms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.voyageone.common.Constants;
import com.voyageone.oms.dao.CustomerSearchDao;
import com.voyageone.oms.formbean.InFormAddNewOrderCustomer;
import com.voyageone.oms.formbean.OutFormCustomer;
import com.voyageone.oms.formbean.OutFormCustomerNotes;
import com.voyageone.oms.formbean.OutFormCustomerOrders;
import com.voyageone.oms.formbean.OutFormCustomerTransactions;
import com.voyageone.oms.modelbean.NotesBean;
import com.voyageone.oms.service.OmsCustomerService;

@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public class OmsCustomerServiceImpl implements OmsCustomerService {

	@Autowired
	private CustomerSearchDao customerDao;
	
	@Override
	public int getCustomersCount(InFormAddNewOrderCustomer inFormAddNewOrderCustomer) {
		int ret = customerDao.getCustomerCount(inFormAddNewOrderCustomer);
		return ret;
	}

	@Override
	public List<OutFormCustomer> getCustomersList(InFormAddNewOrderCustomer inFormAddNewOrderCustomer) {
		List<OutFormCustomer> ret = customerDao.getCustomerList(inFormAddNewOrderCustomer);
		return ret;
	}

	@Override
	public List<OutFormCustomerOrders> getCustomerOrderList(String customerNumber) {
		List<OutFormCustomerOrders> ret = customerDao.getCustomerOrderList(customerNumber);
		return ret;
	}

	@Override
	public List<OutFormCustomerTransactions> getCustomerTransactionList(String customerId) {
		List<OutFormCustomerTransactions> ret = customerDao.getCustomerTransactionList(customerId);
		return ret;
	}

	@Override
	public boolean saveCustomerNotes(NotesBean notesBean) {
		boolean ret = true;
		ret = customerDao.saveNotes(notesBean);
		return ret;
	}

	@Override
	public List<OutFormCustomerNotes> getCustomerNotesList(String customerId) {
		List<OutFormCustomerNotes> ret = customerDao.getCustomerNotesList(customerId);
		return ret;
	}

	@Override
	public boolean updateCustomerNotes(NotesBean notesBean) {
		boolean ret = true;
		ret = customerDao.updateNotes(notesBean);
		return ret;
	}
	
}
