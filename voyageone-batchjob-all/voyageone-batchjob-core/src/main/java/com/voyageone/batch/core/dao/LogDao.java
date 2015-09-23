package com.voyageone.batch.core.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import com.voyageone.batch.core.modelbean.ExceptionLogBean;

@Repository
public class LogDao extends BaseDao {
	
	public int insertException(ExceptionLogBean exceptionBean) {
		return updateTemplate.update(Constants.DAO_NAME_SPACE_CORE + "insertException", exceptionBean);
	}
	
}
