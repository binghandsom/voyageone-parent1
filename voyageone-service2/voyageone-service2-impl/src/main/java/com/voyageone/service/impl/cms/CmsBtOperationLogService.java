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

    public void log(IMQMessageBody messageBody, Exception ex) {
        final VOMQQueue voQueue = AnnotationUtils.findAnnotation(messageBody.getClass(), VOMQQueue.class);
        CmsBtOperationLogModel model = new CmsBtOperationLogModel();
        model.setName(voQueue.value());
        model.setMessageBody(JsonUtil.bean2Json(messageBody));
        if (ex != null) {
            model.setStackTrace(ExceptionUtil.getStackTrace(ex));
            String errorMsg = ex.getMessage();
            model.setMsg(errorMsg);
            model.setComment(errorMsg);
        } else {
            model.setMsg("");
            model.setComment("");
            model.setStackTrace("");
        }
        model.setCreated(DateTimeUtil.getNow());
        model.setCreater(messageBody.getSender());
        model.setModified(DateTimeUtil.getNow());
        model.setModifier(messageBody.getSender());
        dao.insert(model);
    }

    public void log(IMQMessageBody messageBody, String msg) {
        final VOMQQueue voQueue = AnnotationUtils.findAnnotation(messageBody.getClass(), VOMQQueue.class);
        CmsBtOperationLogModel model = new CmsBtOperationLogModel();
        model.setName(voQueue.value());
        model.setMessageBody(JsonUtil.bean2Json(messageBody));
        model.setMsg(msg);
        model.setComment("");
        model.setStackTrace("");
        model.setCreated(DateTimeUtil.getNow());
        model.setCreater(messageBody.getSender());
        model.setModified(DateTimeUtil.getNow());
        model.setModifier(messageBody.getSender());
        dao.insert(model);
    }
    public void log(String operationName, String msg,String creater) {
        CmsBtOperationLogModel model = new CmsBtOperationLogModel();
        model.setName(operationName);
        model.setMessageBody("");
        model.setMsg(msg);
        model.setComment("");
        model.setStackTrace("");
        model.setCreated(DateTimeUtil.getNow());
        model.setCreater(creater);
        model.setModified(DateTimeUtil.getNow());
        model.setModifier(creater);
        dao.insert(model);
    }
}
