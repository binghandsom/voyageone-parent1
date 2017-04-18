package com.voyageone.task2.base;

import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.logger.VOAbsIssueLoggable;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.task2.base.dao.TaskDao;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.lang.String.format;

/**
 * 基础 任务 服务类
 * <p>
 * Created by Chuanyu Liang 2016/10/12
 */
public abstract class BaseTaskService extends VOAbsIssueLoggable implements BeanNameAware {

    /**
     * Bean Name
     */
    protected String beanName;

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
    /**
     * 获取子系统
     */
    protected abstract String getTaskName();

    /**
     * 获取任务名称
     */
    protected abstract SubSystem getSubSystem();

    /**
     * 默认公开的启动入口
     */
    public abstract void startup();


    @Autowired
    protected TaskDao taskDao;

    /**
     * 获取 job 配置
     */
    List<TaskControlBean> getControls() {
        return taskDao.getTaskControlList(getTaskName());
    }

    /**
     * 获取 job 配置 中的指定 TaskControlBean
     */
    protected TaskControlBean getTaskControlBean(List<TaskControlBean> taskControlList, String cfg_name) {
        for (TaskControlBean taskControlBean : taskControlList) {
            if (taskControlBean.getCfg_name().equals(cfg_name)) {
                return taskControlBean;
            }
        }
        return null;
    }

    /**
     * 错误信息记录
     */
    protected void logIssue(Throwable ex) {
        logIssue(ex, null);
    }

    /**
     * 错误信息记录
     */
    public void logIssue(String msg, Object attJson) {
        if (attJson == null) {
            logIssue(msg);
        } else {
            logIssue(msg, makeIssueAttach(attJson));
        }
    }

    /**
     * 错误信息记录
     */
    protected void logIssue(Throwable ex, Object attJson) {
        issueLog.log(ex, ErrorType.BatchJob, getSubSystem(), format("<p>出现未处理异常的任务是: [ %s ]</p>%s", getTaskName(), makeIssueAttach(attJson)));
    }

    /**
     * 错误信息记录
     */
    protected void logIssue(String message) {
        issueLog.log(getTaskName(), message, ErrorType.BatchJob, getSubSystem());
    }

    /**
     * 错误信息记录
     */
    protected void logIssue(String msg, String attach) {
        issueLog.log(getTaskName(), msg, ErrorType.BatchJob, getSubSystem(), attach);
    }

    private String makeIssueAttach(Object attach) {

        if (attach == null) return com.voyageone.common.Constants.EmptyString;

        if (attach instanceof String) return (String) attach;

        return JacksonUtil.bean2Json(attach);
    }
}
