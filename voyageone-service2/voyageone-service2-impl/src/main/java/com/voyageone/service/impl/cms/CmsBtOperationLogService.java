package com.voyageone.service.impl.cms;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ExceptionUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.IMQMessageBody;
import com.voyageone.service.dao.cms.mongo.CmsBtOperationLogDao;
import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     * 插入job/mq的执行履历
     * @param jobName job名称
     * @param jobTitle job标题
     * @param messageBody 参数或者消息内容
     * @param operationLog_type 类型
     * @param comment comment
     */
    public void log(String jobName, String jobTitle, IMQMessageBody messageBody, OperationLog_Type operationLog_type, String comment) {
        final VOMQQueue voQueue = AnnotationUtils.findAnnotation(messageBody.getClass(), VOMQQueue.class);

        String stackTrace = "";
        String msgBody = JsonUtil.bean2Json(messageBody);
        comment = voQueue.value() + ":" + comment;
        log(jobName, jobTitle, operationLog_type, msgBody, null, stackTrace, comment, messageBody.getSender(), messageBody.getChannelId());
    }

    /**
     * 插入job/mq的执行履历
     * @param jobName job名称
     * @param jobTitle job标题
     * @param messageBody 参数或者消息内容
     * @param ex Exception
     */
    public void log(String jobName, String jobTitle, IMQMessageBody messageBody, Exception ex) {
        final VOMQQueue voQueue = AnnotationUtils.findAnnotation(messageBody.getClass(), VOMQQueue.class);

        String stackTrace = "";
        String comment = "";
        String msgBody = JsonUtil.bean2Json(messageBody);
        if (ex != null) {
            stackTrace = ExceptionUtil.getStackTrace(ex);
            comment = voQueue.value() + ":" + ex.getMessage();
        }
        log(jobName, jobTitle, OperationLog_Type.unknownException, msgBody, null, stackTrace, comment, messageBody.getSender(), messageBody.getChannelId());
    }

    /**
     * 插入job/mq的执行履历
     * @param jobName job名称
     * @param jobTitle job标题
     * @param messageBody 参数或者消息内容
     * @param operationLog_type OperationLog_Type
     * @param ex Exception
     */
    public void log(String jobName, String jobTitle, IMQMessageBody messageBody, OperationLog_Type operationLog_type, Exception ex) {
        final VOMQQueue voQueue = AnnotationUtils.findAnnotation(messageBody.getClass(), VOMQQueue.class);
        List<Map<String, String>> msgList = new ArrayList<>();
        String stackTrace = "";
        String comment = "";
        String msgBody = JsonUtil.bean2Json(messageBody);
        if (ex != null) {
            stackTrace = ExceptionUtil.getStackTrace(ex);
            comment = voQueue.value() + ":" + ex.getMessage();
        }
        log(jobName, jobTitle, operationLog_type, msgBody, null, stackTrace, comment, messageBody.getSender(), messageBody.getChannelId());
    }

    /**
     * 插入job/mq的执行履历
     * @param jobName job名称
     * @param jobTitle job标题
     * @param messageBody 参数或者消息内容
     * @param operationLog_type OperationLog_Type
     * @param comment 备注
     * @param msg 错误信息列表
     */
    public void log(String jobName, String jobTitle, IMQMessageBody messageBody, OperationLog_Type operationLog_type, String comment, List<CmsBtOperationLogModel_Msg> msg) {
        final VOMQQueue voQueue = AnnotationUtils.findAnnotation(messageBody.getClass(), VOMQQueue.class);
        log(jobName, jobTitle, operationLog_type, JsonUtil.bean2Json(messageBody), msg, "", voQueue.value() + ":" + comment, messageBody.getSender(), messageBody.getChannelId());
    }

    /**
     * 插入job/mq的执行履历
     * @param jobName job名称
     * @param jobTitle job标题
     * @param operationLog_type OperationLog_Type
     * @param msg 错误信息列表
     * @param creator 操作者
     */
    public void log(String jobName, String jobTitle, OperationLog_Type operationLog_type, List<CmsBtOperationLogModel_Msg> msg, String creator, String channelId) {
        log(jobName, jobTitle, operationLog_type, "", msg, "", "", creator, channelId);
    }

    /**
     * 插入job/mq的执行履历
     * @param name 名称
     * @param title 标题
     * @param operationLog_type OperationLog_Type
     * @param messageBody 消息内容
     * @param msg 错误信息列表
     * @param stackTrace 堆栈错误信息
     * @param comment 备注
     * @param creator 操作者
     */
    private void log(String name, String title, OperationLog_Type operationLog_type, String messageBody, List<CmsBtOperationLogModel_Msg> msg
            , String stackTrace, String comment, String creator, String channelId) {
        CmsBtOperationLogModel model = new CmsBtOperationLogModel();
        model.setName(name);
        model.setTitle(title);
        model.setChannelId(channelId);
        model.setMessageBody(messageBody);
        model.setMsg(msg);
        model.setComment(comment);
        model.setStackTrace(stackTrace);
        model.setCreated(DateTimeUtil.getNow());
        model.setCreater(creator);
        model.setModified(DateTimeUtil.getNow());
        model.setModifier(creator);
        model.setType(operationLog_type.getId());
        dao.insert(model);
    }

    /**
     * 数据初始化
     *
     * @param params
     */
    public List<CmsBtOperationLogModel> searchMqCmsBtOperationLogData(Map params) {
        JongoQuery queryObject = getSearchQuery(params);
        queryObject.setSort("{\"modified\": -1}");
        int pageNum = (Integer) params.get("curr");
        int pageSize = (Integer) params.get("size");
        queryObject.setSkip((pageNum - 1) * pageSize);
        queryObject.setLimit(pageSize);
        return dao.select(queryObject);
    }

    /**
     *
     * @param params
     */
    public long searchMqCmsBtOperationLogDataCnt(Map params) {
        JongoQuery parameter = getSearchQuery(params);
        return dao.countByQuery(parameter.getQuery(), parameter.getParameters());
    }

    /**
     * 取得参数
     * @param params 参数
     * @return 检索条件
     */
    private JongoQuery getSearchQuery(Map params) {
        JongoQuery queryObject = new JongoQuery();

        String title = params.get("title") != null ? String.valueOf(params.get("title")) : "";
        String name = params.get("name") != null ? String.valueOf(params.get("name")) : "";
        String userName = params.get("userName") != null ? String.valueOf(params.get("userName")) : "";
        String channelId = params.get("channelId") != null ? String.valueOf(params.get("channelId")) : "";

        Criteria criteria = new Criteria("channelId").is(channelId);
        criteria = criteria.and("name").regex(name);
        criteria = criteria.and("title").regex(title);
        if (!"0".equals(userName))
            criteria = criteria.and("modifier").is(userName);
        else
            criteria = criteria.and("modifier").exists(true);
        List<Integer> newType = new ArrayList<>();
        for (String s : (ArrayList<String>) params.get("typeValue")) {
            newType.add(Integer.valueOf(s));
        }
        if (newType != null && newType.size() > 0) {
            criteria = criteria.and("type").in(newType);
        }
        queryObject.setQuery(criteria);
        return queryObject;
    }
}
