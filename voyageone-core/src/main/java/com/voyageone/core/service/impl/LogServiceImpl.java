package com.voyageone.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.core.dao.LogDao;
import com.voyageone.core.modelbean.ExceptionLogBean;
import com.voyageone.core.service.LogService;

@Service
public class LogServiceImpl implements LogService {

	@Autowired
	private LogDao logDao;
	
	@Override
	public void insertExceptionLog(ExceptionLogBean exceptionBean) {
		logDao.insertException(exceptionBean);
	}
}
