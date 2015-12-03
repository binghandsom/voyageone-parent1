package com.voyageone.oms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.voyageone.common.Constants;
import com.voyageone.oms.dao.OrderDao;
import com.voyageone.oms.service.OmsDefaultService;

@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public class OmsDefaultServiceImpl implements OmsDefaultService {

	@Autowired
	private OrderDao orderDao;
	
	@Override
	public int getTotalOrderCount() {
		// TODO 
		return orderDao.getOrderCounts();
	}
}
