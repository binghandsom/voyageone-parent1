package com.voyageone.core.service.impl;

import java.util.List;
import java.util.Map;

import com.voyageone.common.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.voyageone.core.CoreConstants;
import com.voyageone.core.dao.ManagerDao;
import com.voyageone.core.service.ManagerService;

@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public class ManagerServiceImpl implements ManagerService {

	private static Log logger = LogFactory.getLog(ManagerServiceImpl.class);
	
	@Autowired
	private ManagerDao managerDao;
	
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	private DefaultTransactionDefinition def = new DefaultTransactionDefinition();
	
	@Override
	public List<Map<String, Object>> getBatchJobList() {
		return managerDao.getBatchJobList();
	}

	@Override
	public int updateJobRun(Map data) {
		// TODO Auto-generated method stub
		return managerDao.updateJobRun(data);
	}

	@Override
	public int insertBatchJob(Map data) {
		TransactionStatus status = transactionManager.getTransaction(def);
		try{
			managerDao.insertComMtTask(data);
			managerDao.insertTmTaskControl(data);
		}catch(Exception e){
			logger.error(e.getMessage());
			transactionManager.rollback(status);
			return 0;
		};
		transactionManager.commit(status);
		return 1;
	}

}
