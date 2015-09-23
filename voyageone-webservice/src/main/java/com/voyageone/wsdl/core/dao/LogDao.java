package com.voyageone.wsdl.core.dao;

import com.voyageone.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import com.voyageone.wsdl.core.Constants;
import com.voyageone.wsdl.core.modelbean.ExceptionLogBean;

@Repository
public class LogDao extends BaseDao {
	
	public int insertException(ExceptionLogBean exceptionBean) {
		return updateTemplate.update(Constants.DAO_NAME_SPACE_CORE + "insertException", exceptionBean);
	}
	
}
