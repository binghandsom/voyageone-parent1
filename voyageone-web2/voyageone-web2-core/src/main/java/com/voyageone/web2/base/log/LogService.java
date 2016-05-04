package com.voyageone.web2.base.log;

import com.voyageone.common.configs.beans.ExceptionLogBean;
import com.voyageone.common.logger.VOAbsLoggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 记录异常的日志信息
 * Created on 11/26/15.
 *
 * @author Jonas
 * @version 2.0.0
 */
@Service
public class LogService extends VOAbsLoggable {
    @Autowired
    private LogDao logDao;

    public void addExceptionLog(ExceptionLogBean exceptionBean) {
        try {
            logDao.insert(exceptionBean);
        } catch (Exception e) {
            $error(e);
        }
    }
}
