package com.voyageone.core.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.core.modelbean.ExceptionLogBean;
import org.springframework.stereotype.Repository;

@Repository
public class LogDao extends BaseDao {
	
	public int insertException(ExceptionLogBean exceptionBean) {
		return updateTemplate.update(Constants.DAO_NAME_SPACE_CORE + "insertException", exceptionBean);
	}
	
}
