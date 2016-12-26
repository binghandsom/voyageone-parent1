package com.voyageone.service.impl.cms.vomq;

import com.voyageone.common.util.ExceptionUtil;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.IMQMessageBody;
import com.voyageone.components.rabbitmq.service.IMQJobLog;
import com.voyageone.service.impl.BaseService;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by dell on 2016/12/26.
 */
@Service
public class CmsMQJobLogService  extends BaseService implements IMQJobLog {

    public void log(IMQMessageBody messageBody, Exception ex, Date beginDate, Date endDate) {
        final VOMQQueue voQueue = AnnotationUtils.findAnnotation(messageBody.getClass(), VOMQQueue.class);

        String routingKey=voQueue.value();
        String stackTrace = ExceptionUtil.getStackTrace(ex);
        String errorMsg = ex.getMessage();
        //votodo 待实现
        // id          主键
        // routingKey  队列名称
        // messageBody 消息体
        // logType     日志类型 ,
        // sendDate    消息发送时间
        // beginDate   消息处理开始时间
        // endDate   消息处理接收时间
        // errorMsg    错误消息
        // stackTrace  调用堆栈
    }
}
