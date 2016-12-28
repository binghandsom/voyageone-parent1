package com.voyageone.service.impl.com.log;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ExceptionUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.IMQMessageBody;
import com.voyageone.components.rabbitmq.service.IMQJobLog;
import com.voyageone.service.enums.com.EnumTaskLog_LogType;
import com.voyageone.service.enums.com.EnumTaskLog_Status;
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

    /**
     *
     * @param jobName
     * @param messageBody
     * @param ex
     * @param beginDate
     * @param endDate
     */
    public void log(String jobName,IMQMessageBody messageBody, Exception ex, Date beginDate, Date endDate) {
        ComBtTaskLogModel model = new ComBtTaskLogModel();
        model.setTaskName(jobName);
        if (messageBody != null) {
            final VOMQQueue voQueue = AnnotationUtils.findAnnotation(messageBody.getClass(), VOMQQueue.class);
            model.setQueueName(voQueue.value());
            model.setMessageBody(JsonUtil.bean2Json(messageBody));
        } else {
            model.setQueueName("");
            model.setMessageBody("");
        }
        model.setBeginDate(beginDate);
        model.setEndDate(endDate);
        model.setSendDate(DateTimeUtil.getCreatedDefaultDate());
        model.setLogType(EnumTaskLog_LogType.MQJob.getId());
        if (ex != null) {
            model.setStackTrace(ExceptionUtil.getStackTrace(ex));
            String errorMsg = ex.getMessage();
            model.setComment(errorMsg);
            model.setStatus(EnumTaskLog_Status.failed.getId());
        } else {
            model.setStatus(EnumTaskLog_Status.success.getId());
            model.setComment("");
            model.setStackTrace("");
        }
        if (StringUtils.isEmpty(model.getComment())) {
            model.setComment("");
        }
        model.setCreated(new Date());
        model.setCreater(messageBody.getSender());
        model.setModified(new Date());
        model.setModifier(messageBody.getSender());
        dao.insert(model);
    }
}
