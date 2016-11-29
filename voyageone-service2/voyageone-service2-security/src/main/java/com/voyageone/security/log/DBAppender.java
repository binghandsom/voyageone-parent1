package com.voyageone.security.log;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import com.voyageone.common.spring.SpringContext;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.security.dao.ComLogDao;
import com.voyageone.security.model.ComLogModel;

/**
 * Created by Ethan Shi on 2016-08-23.
 */

public class DBAppender extends AppenderSkeleton {
    @Override
    protected void append(LoggingEvent loggingEvent)  {
//        String  msg = loggingEvent.getMessage().toString();
//        Map<String, Object> record = JacksonUtil.jsonToMap(msg);
        try {
            ComLogModel model = new ComLogModel();
//            BeanUtils.populate(model,  record);
//            model.setRequest(JacksonUtil.bean2Json(record.get("request")));
//            model.setResponse(JacksonUtil.bean2Json(record.get("response")));


            model.setApplication(loggingEvent.getMDC("application").toString());
            model.setIp(loggingEvent.getMDC("ip").toString());
            model.setUrl(loggingEvent.getMDC("url").toString());
            model.setAction(loggingEvent.getMDC("action").toString());
            model.setCreater(loggingEvent.getMDC("creater").toString());
            model.setExecutionTime(Integer.valueOf(loggingEvent.getMDC("executionTime").toString()));
            model.setRequest(JacksonUtil.bean2Json(loggingEvent.getMDC("request")));
            model.setResponse(JacksonUtil.bean2Json(loggingEvent.getMDC("response")));

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
