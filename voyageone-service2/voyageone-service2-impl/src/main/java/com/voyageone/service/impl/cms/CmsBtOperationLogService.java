package com.voyageone.service.impl.cms;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ExceptionUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.IMQMessageBody;
import com.voyageone.service.bean.cms.enumcms.OperationLog_Type;
import com.voyageone.service.dao.cms.mongo.CmsBtOperationLogDao;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by dell on 2016/12/27.
 */
@Service
public class CmsBtOperationLogService {
    @Autowired
    CmsBtOperationLogDao dao;

    @Autowired
    MongoSequenceService mongoSequenceService;

    /**
     * @param messageBody
     * @param ex
     */
    public void log(IMQMessageBody messageBody, Exception ex) {
        final VOMQQueue voQueue = AnnotationUtils.findAnnotation(messageBody.getClass(), VOMQQueue.class);
        String operationName = voQueue.value();
        String msg = "";
        String stackTrace = "";
        String msgBody = JsonUtil.bean2Json(messageBody);
        if (ex != null) {
            stackTrace = ExceptionUtil.getStackTrace(ex);
            msg = ex.getMessage();
        }
        log(operationName, OperationLog_Type.unknownException, msgBody, msg, stackTrace, messageBody.getSender());
    }

    /**
     * @param messageBody
     * @param operationLog_type
     * @param ex
     */
    public void log(IMQMessageBody messageBody, OperationLog_Type operationLog_type, Exception ex) {
        final VOMQQueue voQueue = AnnotationUtils.findAnnotation(messageBody.getClass(), VOMQQueue.class);
        String operationName = voQueue.value();
        String msg = "";
        String stackTrace = "";
        String msgBody = JsonUtil.bean2Json(messageBody);
        if (ex != null) {
            stackTrace = ExceptionUtil.getStackTrace(ex);
            msg = ex.getMessage();
        }
        log(operationName, operationLog_type, msgBody, msg, stackTrace, messageBody.getSender());
    }

    /**
     * @param messageBody
     * @param operationLog_type
     * @param msg
     */
    public void log(IMQMessageBody messageBody, OperationLog_Type operationLog_type, String msg) {
        final VOMQQueue voQueue = AnnotationUtils.findAnnotation(messageBody.getClass(), VOMQQueue.class);
        log(voQueue.value(), operationLog_type, JsonUtil.bean2Json(messageBody), msg, "", messageBody.getSender());
    }

    /**
     * @param operationName
     * @param operationLog_type
     * @param msg
     * @param creator
     */
    public void log(String operationName, OperationLog_Type operationLog_type, String msg, String creator) {
        log(operationName, operationLog_type, "", msg, "", creator);
    }

    /**
     * @param operationName
     * @param operationLog_type
     * @param messageBody
     * @param msg
     * @param stackTrace
     * @param creater
     */
    private void log(String operationName, OperationLog_Type operationLog_type, String messageBody, String msg, String stackTrace, String creater) {
        CmsBtOperationLogModel model = new CmsBtOperationLogModel();
        model.setName(operationName);
        model.setMessageBody(messageBody);
        model.setMsg(msg);
        model.setComment("");
        model.setStackTrace(stackTrace);
        model.setCreated(DateTimeUtil.getNow());
        model.setCreater(creater);
        model.setModified(DateTimeUtil.getNow());
        model.setModifier(creater);
        model.setType(operationLog_type.getId());
        dao.insert(model);
    }
}
