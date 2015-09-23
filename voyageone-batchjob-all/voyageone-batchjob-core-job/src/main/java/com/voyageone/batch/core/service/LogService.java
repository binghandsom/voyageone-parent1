package com.voyageone.batch.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.batch.core.dao.LogDao;
import com.voyageone.batch.core.modelbean.ExceptionLogBean;

@Service
public class LogService {

	@Autowired
	private LogDao logDao;
	
	public void insertExceptionLog(ExceptionLogBean exceptionBean) {
		logDao.insertException(exceptionBean);
	}
}
