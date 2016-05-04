package com.voyageone.web2.base.log;

import com.voyageone.common.configs.beans.ExceptionLogBean;
import com.voyageone.web2.base.dao.WebBaseDao;
import com.voyageone.web2.base.dao.WebDaoNs;
import org.springframework.stereotype.Repository;

/**
 * 对 ct_exception_log 进行访问
 * Created by Jonas on 11/25/15.
 *
 * @author Jonas
 * @version 2.0.0
 */
@Repository
class LogDao extends WebBaseDao {
    @Override
    protected WebDaoNs webNs() {
        return WebDaoNs.CORE;
    }

    public int insert(ExceptionLogBean exceptionBean) {
        return updateTemplate.insert("ct_exception_log_insert", exceptionBean);
    }
}
