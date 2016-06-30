package com.voyageone.common.components.issueLog;

import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.beans.IssueLogBean;
import com.voyageone.common.components.issueLog.dao.IssueLogDao;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Tester on 5/6/2015.
 */
@Component
public final class IssueLog {
    @Autowired
    private IssueLogDao issueLogDao;

    /**
     * 记录一个错误
     *
     * @param exception 目标异常
     * @param errorType 错误类型
     * @param subSystem 发生的子系统
     */
    public void log(Exception exception, ErrorType errorType, SubSystem subSystem) {
        log(exception, errorType, subSystem, Constants.EmptyString);
    }

    /**
     * 记录一个错误
     *
     * @param location    发生的位置
     * @param description 错误的信息描述
     * @param errorType   错误类型
     * @param subSystem   发生的子系统
     */
    public void log(String location, String description, ErrorType errorType, SubSystem subSystem) {
        log(location, description, errorType, subSystem, Constants.EmptyString);
    }

    /**
     * 记录一个错误
     *
     * @param exception 目标异常
     * @param errorType 错误类型
     * @param subSystem 发生的子系统
     * @param add       附加的信息
     */
    public void log(Exception exception, ErrorType errorType, SubSystem subSystem, String add) {

        // 有的异常 message 是 null 或 空，此处处理这种状况
        String message = exception.getMessage();

        if (StringUtils.isEmpty(message)) message = CommonUtil.getMessages(exception);

        log(CommonUtil.getExceptionSimpleContent(exception), StringUtils.isEmpty(message) ? Constants.EmptyString : message, errorType, subSystem, add);
    }

    /**
     * 记录一个错误
     *
     * @param location    发生的位置
     * @param description 错误的信息描述
     * @param errorType   错误类型
     * @param subSystem   发生的子系统
     * @param add         附加的信息
     */
    public void log(String location, String description, ErrorType errorType, SubSystem subSystem, String add) {
        IssueLogBean issueLogBean = new IssueLogBean();

        issueLogBean.setError_location(location);
        issueLogBean.setSub_system(subSystem);
        issueLogBean.setError_type(errorType);
        issueLogBean.setDescription(description);
        issueLogBean.setDescription_add(add);
        issueLogBean.setDate_time(DateTimeUtil.getNow());
        issueLogBean.setSend_flg(0);

        issueLogDao.insert(issueLogBean);
    }
}
