package com.voyageone.task2.base;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.util.List;

/**
 * 基础 Application Listener任务 服务类
 * <p>
 * Created by jonas on 15/6/6.
 */
public abstract class BaseListenService extends BaseTaskService implements ApplicationListener {
//public abstract class BaseListenService extends BaseTaskService {

    private boolean running = false;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        startup();
    }

    @Override
    public void startup() {

        String taskName = getTaskName();
        MDC.put("taskName", taskName);
        MDC.put("subSystem", getSubSystem().name().toLowerCase());

        if (running) {
            $info(getTaskName() + "正在运行，忽略");
            return;
        }

        running = true;

        $info(getTaskName() + "任务开始");

        // 先获取配置
        List<TaskControlBean> taskControlList = getControls();

        if (taskControlList.isEmpty()) {
            $info("没有找到任何配置。");
            logIssue("没有找到任何配置！！！", getTaskName());
            return;
        }

        // 是否可以运行的判断
        if (!TaskControlUtils.isRunnable(taskControlList)) {
            return;
        }

        try {

            new Thread(() -> onStartup(taskControlList)).start();

        } catch (BusinessException be) {
            logIssue(be, be.getInfo());
            $error("出现业务异常，任务退出");
        } catch (BeanCreationException bce) {
            $error("出现业务异常，任务退出");
        } catch (Exception e) {
            logIssue(e);
            $error("出现异常，任务退出", e);
        }
        MDC.remove("taskName");
        MDC.remove("subSystem");
    }

    /**
     * 必须实现的，具体的任务内容
     *
     * @param taskControlList job 配置
     */
    protected void onStartup(List<TaskControlBean> taskControlList) {
        initStartup(taskControlList);
        Object eventObj = onListen(taskControlList);
        //noinspection InfiniteLoopStatement
        while (true) {
            if (eventObj == null) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                continue;
            }
            try {
                doEvent(taskControlList, eventObj);
            } catch (BusinessException be) {
                logIssue(be, be.getInfo());
                $error("出现业务异常，任务退出");
                throw be;
            } catch (BeanCreationException bce) {
                $error("出现业务异常，任务退出 " + bce.getMessage());
                throw bce;
            } catch (Exception e) {
                logIssue(e);
                $error("出现异常，任务退出", e);
                throw e;
            }
        }
    }

    protected void initStartup(List<TaskControlBean> taskControlList) {}

    protected abstract Object onListen(List<TaskControlBean> taskControlList);

    protected abstract void doEvent(List<TaskControlBean> taskControlList, Object eventObj);

}
