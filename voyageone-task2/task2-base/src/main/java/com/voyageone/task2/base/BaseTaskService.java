package com.voyageone.task2.base;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.dao.TaskDao;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

/**
 * job 所使用的 service 基类
 * <p>
 * Created by neil on 2015-05-26.
 */
public abstract class BaseTaskService extends LoggedService {

    @Autowired
    protected TaskDao taskDao;

    /**
     * 获取子系统
     */
    public abstract SubSystem getSubSystem();

    /**
     * 获取任务名称
     */
    public abstract String getTaskName();

    /**
     * 获取 job 配置
     */
    List<TaskControlBean> getControls() {
        return taskDao.getTaskControlList(getTaskName());
    }

    /**
     * 默认公开的启动入口
     */
    public void startup() {
        // 先获取配置
        List<TaskControlBean> taskControlList = getControls();

        if (taskControlList.size() < 1) {
            $info("没有找到任何配置。");
            logIssue("没有找到任何配置！！！", getTaskName());
            return;
        }

        // 是否可以运行的判断
        if (!TaskControlUtils.isRunnable(taskControlList)) {
            return;
        }

        String taskID = TaskControlUtils.getTaskId(taskControlList);

        TaskControlEnums.Status status = TaskControlEnums.Status.START;

        // 任务监控历史记录添加:启动
        taskDao.insertTaskHistory(taskID, status.getIs());

        try {
            onStartup(taskControlList);
            status = TaskControlEnums.Status.SUCCESS;

        } catch (BusinessException be) {
            logIssue(be, be.getInfo());
            status = TaskControlEnums.Status.ERROR;
            $info("出现业务异常，任务退出");
        } catch (Exception e) {
            logIssue(e);
            status = TaskControlEnums.Status.ERROR;
            logger.error("出现异常，任务退出", e);
        }

        // 任务监控历史记录添加:结束
        taskDao.insertTaskHistory(taskID, status.getIs());
    }

    /**
     * 必须实现的，具体的任务内容
     *
     * @param taskControlList job 配置
     */
    protected abstract void onStartup(List<TaskControlBean> taskControlList) throws Exception;

    /**
     * 使用配置数量的线程运行传入的任务
     *
     * @param threads         要运行的任务
     * @param taskControlList job 配置
     * @throws InterruptedException
     */
    protected void runWithThreadPool(List<Runnable> threads, List<TaskControlBean> taskControlList) throws InterruptedException {
        runWithThreadPool(threads, taskControlList, 1);
    }

    /**
     * 使用配置数量的线程运行传入的任务
     *
     * @param threads            要运行的任务
     * @param taskControlList    job 配置
     * @param defaultThreadCount 当配置中没有配置线程数量时，默认的数量
     * @throws InterruptedException
     */
    protected void runWithThreadPool(List<Runnable> threads, List<TaskControlBean> taskControlList, int defaultThreadCount) throws InterruptedException {

        // 如果没有任务，则直接返回
        if (threads == null || threads.size() < 1)
            return;

        String threadCount = TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.thread_count);
        int intThreadCount = defaultThreadCount;

        if (!StringUtils.isNullOrBlank2(threadCount)) {
            intThreadCount = Integer.valueOf(threadCount);
        }

        // 如果最终计算获得线程数量无效，则提示错误
        if (intThreadCount < 1) {
            throw new IllegalArgumentException("thread count error.");
        }

        // 如果计算出来的开放线程数偏多了。则使用最少的线程数
        if (intThreadCount > threads.size()) {
            intThreadCount = threads.size();
        }

        ExecutorService pool = Executors.newFixedThreadPool(intThreadCount);

        threads.forEach(pool::execute);

        pool.shutdown();

        // 等待子线程结束，再继续执行下面的代码
        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    }

    /**
     * 错误信息记录
     */
    public void logIssue(Exception ex) {
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
    public void logIssue(Exception ex, Object attJson) {
        issueLog.log(ex, ErrorType.BatchJob, getSubSystem(), format("<p>出现未处理异常的任务是: [ %s ]</p>%s", getTaskName(), makeIssueAttach(attJson)));
    }


    /**
     * 错误信息记录
     */
    public void logIssue(String message) {
        issueLog.log(getTaskName(), message, ErrorType.BatchJob, getSubSystem());
    }

    /**
     * 错误信息记录
     */
    public void logIssue(String msg, String attach) {
        issueLog.log(getTaskName(), msg, ErrorType.BatchJob, getSubSystem(), attach);
    }

    private String makeIssueAttach(Object attach) {
        if (attach == null) return com.voyageone.common.Constants.EmptyString;

        if (attach instanceof String) return (String) attach;

        return JacksonUtil.bean2Json(attach);
    }
}
