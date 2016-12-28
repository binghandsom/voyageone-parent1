package com.voyageone.service.impl.cms;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ExceptionUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.IMQMessageBody;
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
        log(operationName, msgBody, msg, stackTrace, messageBody.getSender());
    }

    public void log(IMQMessageBody messageBody, String msg) {
        final VOMQQueue voQueue = AnnotationUtils.findAnnotation(messageBody.getClass(), VOMQQueue.class);
        log(voQueue.value(), JsonUtil.bean2Json(messageBody), msg, "", messageBody.getSender());
    }

    public void log(String operationName, String msg, String creater) {
        log(operationName, "", msg, "", creater);
    }

    public void log(String operationName, String messageBody, String msg, String stackTrace, String creater) {
        CmsBtOperationLogModel model = new CmsBtOperationLogModel();
        model.setName(operationName);
        model.setOperationId(mongoSequenceService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_IMAGE_TEMPLATE_ID));
        model.setMessageBody("");
        model.setMsg(msg);
        model.setComment("");
        model.setStackTrace(stackTrace);
        model.setCreated(DateTimeUtil.getNow());
        model.setCreater(creater);
        model.setModified(DateTimeUtil.getNow());
        model.setModifier(creater);
        dao.insert(model);
    }
}
