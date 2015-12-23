package com.voyageone.wsdl.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.wsdl.core.dao.LogDao;
import com.voyageone.wsdl.core.modelbean.ExceptionLogBean;
import com.voyageone.wsdl.core.service.LogService;

@Service
public class LogServiceImpl implements LogService {

	@Autowired
	private LogDao logDao;
	
	@Override
	public void insertExceptionLog(ExceptionLogBean exceptionBean) {
		logDao.insertException(exceptionBean);
	}
}
