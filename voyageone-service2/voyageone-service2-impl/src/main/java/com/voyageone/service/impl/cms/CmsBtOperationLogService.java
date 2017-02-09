package com.voyageone.service.impl.cms;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.util.*;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.IMQMessageBody;
import com.voyageone.service.dao.cms.mongo.CmsBtOperationLogDao;
import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
     * @param jobName
     * @param jobTitle
     * @param messageBody
     * @param ex
     */
    public void log(String jobName, String jobTitle, IMQMessageBody messageBody, Exception ex) {
        final VOMQQueue voQueue = AnnotationUtils.findAnnotation(messageBody.getClass(), VOMQQueue.class);
        String msg = "";
        String stackTrace = "";
        String msgBody = JsonUtil.bean2Json(messageBody);
        if (ex != null) {
            stackTrace = ExceptionUtil.getStackTrace(ex);
            msg = ex.getMessage();
        }
        log(jobName, jobTitle, OperationLog_Type.unknownException, msgBody, msg, stackTrace, voQueue.value(), messageBody.getSender());
    }

    /**
     * @param jobName
     * @param jobTitle
     * @param messageBody
     * @param operationLog_type
     * @param ex
     */
    public void log(String jobName, String jobTitle, IMQMessageBody messageBody, OperationLog_Type operationLog_type, Exception ex) {
        final VOMQQueue voQueue = AnnotationUtils.findAnnotation(messageBody.getClass(), VOMQQueue.class);
        String msg = "";
        String stackTrace = "";
        String msgBody = JsonUtil.bean2Json(messageBody);
        if (ex != null) {
            stackTrace = ExceptionUtil.getStackTrace(ex);
            msg = ex.getMessage();
        }
        log(jobName, jobTitle, operationLog_type, msgBody, msg, stackTrace, voQueue.value(), messageBody.getSender());
    }

    /**
     * @param jobName
     * @param jobTitle
     * @param messageBody
     * @param operationLog_type
     * @param msg
     */
    public void log(String jobName, String jobTitle, IMQMessageBody messageBody, OperationLog_Type operationLog_type, String msg) {
        final VOMQQueue voQueue = AnnotationUtils.findAnnotation(messageBody.getClass(), VOMQQueue.class);
        log(jobName, jobTitle, operationLog_type, JsonUtil.bean2Json(messageBody), msg, "", voQueue.value(), messageBody.getSender());
    }

    /**
     * @param operationName
     * @param title
     * @param operationLog_type
     * @param msg
     * @param creator
     */
    public void log(String operationName, String title, OperationLog_Type operationLog_type, String msg, String creator) {
        log(operationName, title, operationLog_type, "", msg, "", "", creator);
    }

    /**
     * @param name
     * @param title
     * @param operationLog_type
     * @param messageBody
     * @param msg
     * @param stackTrace
     * @param comment
     * @param creater
     */
    private void log(String name, String title, OperationLog_Type operationLog_type, String messageBody, String msg, String stackTrace, String comment, String creater) {
        CmsBtOperationLogModel model = new CmsBtOperationLogModel();
        model.setName(name);
        model.setTitle(title);
        model.setMessageBody(messageBody);
        model.setMsg(msg);
        model.setComment(comment);
        model.setStackTrace(stackTrace);
        model.setCreated(DateTimeUtil.getNow());
        model.setCreater(creater);
        model.setModified(DateTimeUtil.getNow());
        model.setModifier(creater);
        model.setType(operationLog_type.getId());
        dao.insert(model);
    }

    /**
     * 数据初始化
     *
     * @param params
     */
    public List<CmsBtOperationLogModel> searchMqCmsBtOperationLogData(Map params) {
        JongoQuery queryObject = new JongoQuery();
        String parameter = getSearchQuery(params);
        queryObject.setQuery(parameter);
        queryObject.setSort("{\"modified\": -1}");
        int pageNum = (Integer) params.get("curr");
        int pageSize = (Integer) params.get("size");
        queryObject.setSkip((pageNum - 1) * pageSize);
        queryObject.setLimit(pageSize);
        return dao.select(queryObject);
    }

    /**
     * 取得参数
     */
    private String getSearchQuery(Map params) {
        StringBuilder sbQuery = new StringBuilder();
        //title
        String title = String.valueOf(params.get("title"));
        if (!StringUtils.isEmpty(title)) {
            sbQuery.append(MongoUtils.splicingValue("title", title, "$regex"));
            sbQuery.append(",");
        }
        //name
        String name = String.valueOf(params.get("name"));
        if (!StringUtils.isEmpty(name)) {
            sbQuery.append(MongoUtils.splicingValue("name", name, "$regex"));
            sbQuery.append(",");
        }
        //type
        List type = (List) params.get("typeValue");
        if (type.size() > 0) {
            sbQuery.append(MongoUtils.splicingValue("type", type.toArray(), "$in"));
            sbQuery.append(",");
        }
        //type
        String userName = String.valueOf(params.get("userName"));
        if (!StringUtils.isEmpty(userName) && !"0".equals(userName)) {
            sbQuery.append(MongoUtils.splicingValue("modifier", userName, "$regex"));
            sbQuery.append(",");
        }
        return "{" + sbQuery.toString() + "}";
    }

    /**
     *
     * @param params
     */
    public long searchMqCmsBtOperationLogDataCnt(Map params) {
        String parameter = getSearchQuery(params);
        return dao.countByQuery(parameter);
    }
}
