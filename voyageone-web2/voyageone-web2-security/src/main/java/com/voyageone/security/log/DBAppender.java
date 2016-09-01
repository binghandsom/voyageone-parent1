package com.voyageone.security.log;

import com.voyageone.common.spring.SpringContext;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.security.dao.ComLogDao;
import com.voyageone.security.model.ComLogModel;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by Ethan Shi on 2016-08-23.
 */

public class DBAppender extends AppenderSkeleton {
    @Override
    protected void append(LoggingEvent loggingEvent)  {
        String  msg = loggingEvent.getMessage().toString();
        Map<String, Object> record = JacksonUtil.jsonToMap(msg);
        ComLogModel model = new ComLogModel();
        try {
            BeanUtils.populate(model,  record);
            model.setRequest(JacksonUtil.bean2Json(record.get("request")));
            model.setResponse(JacksonUtil.bean2Json(record.get("response")));

            ComLogDao comLogDao = SpringContext.getBean(ComLogDao.class);
            comLogDao.insert(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
