package com.voyageone.service.impl.com.log;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ExceptionUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.IMQMessageBody;
import com.voyageone.components.rabbitmq.service.IMQJobLog;
import com.voyageone.service.bean.com.enumcom.EnumTaskLog_LogType;
import com.voyageone.service.dao.com.ComBtTaskLogDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.ComBtTaskLogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by dell on 2016/12/26.
 */
@Service
public class ComBtTaskLogService extends BaseService implements IMQJobLog {

    @Autowired
    ComBtTaskLogDao dao;

    public void log(IMQMessageBody messageBody, Exception ex, Date beginDate, Date endDate) {
        final VOMQQueue voQueue = AnnotationUtils.findAnnotation(messageBody.getClass(), VOMQQueue.class);
        if (ex != null) {
            String stackTrace = ExceptionUtil.getStackTrace(ex);
            String errorMsg = ex.getMessage();
        }
        ComBtTaskLogModel model = new ComBtTaskLogModel();
        model.setTaskId(voQueue.value());
        model.setBeginDate(beginDate);
        model.setEndDate(endDate);
        model.setSendDate(DateTimeUtil.getCreatedDefaultDate());
        model.setComment("");
        model.setLogType(EnumTaskLog_LogType.success.getId());
        model.setMessageBody(JsonUtil.bean2Json(messageBody));
        model.setStacktrace("");
        model.setStatus((short) 1);
        model.setCreated(new Date());
        model.setCreater(messageBody.getSender());
        model.setModified(new Date());
        model.setModifier(messageBody.getSender());

        dao.insert(model);

    }
}
