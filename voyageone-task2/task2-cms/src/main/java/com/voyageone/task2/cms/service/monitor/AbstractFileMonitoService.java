package com.voyageone.task2.cms.service.monitor;

import com.voyageone.common.util.StringUtils;
import com.voyageone.task2.base.dao.TaskDao;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 抽象文件监控服务，文件监控服务的父类
 *
 * @author aooer 2016/6/2.
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class AbstractFileMonitoService implements ApplicationListener {

    /* 监控 */
    protected final static String INOTIFY_WAIT_CMD = "/usr/bin/inotifywait";

    /* 日志 */
    protected final Logger LOG = LoggerFactory.getLogger(getClass());


    @Autowired
    protected TaskDao taskDao;

    /**
     * onApplicationEvent
     *
     * @param applicationEvent ApplicationEvent
     */
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ContextRefreshedEvent) {
            new Thread(this::run).start();
        }
    }

    /**
     * run
     */
    private void run() {
        LOG.info("AbstractFileMonitoService.run start");

        // get taskControlList
        List<TaskControlBean> taskControlList = taskDao.getTaskControlList(getTaskName());

        // 线程List
        List<Runnable> threads = new ArrayList<>();
        // 循环处理批量图片给上传
        for (TaskControlBean taskControl : taskControlList) {
            if ("order_channel_id".equals(taskControl.getCfg_name())) {
                String finalPath = taskControl.getCfg_val2();
                String channelId = taskControl.getCfg_val1();
                LOG.info(String.format("AbstractFileMonitoService.run channelId:=%s;finalPath=%s", channelId, finalPath));

                if (StringUtils.isEmpty(finalPath)) {
                    continue;
                }

                File filePath = new File(finalPath);
                if (!filePath.exists()) {
                    LOG.warn(String.format("AbstractFileMonitoService.run filePath not found channelId:=%s;finalPath=%s", channelId, finalPath));
                    continue;
                }
                if (!filePath.isDirectory()) {
                    continue;
                }

                threads.add(() -> executeCmd(channelId, finalPath));
            }
        }
        // check threads
        if (!threads.isEmpty()) {
            ExecutorService pool = Executors.newFixedThreadPool(threads.size());
            threads.forEach(pool::execute);
            pool.shutdown();
            // 等待子线程结束，再继续执行下面的代码
            try {
                pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                LOG.error("AbstractFileMonitoService.awaitTermination error", e);
            }
        }

        LOG.info("AbstractFileMonitoService.run end");
    }

    /**
     * getTaskName
     */
    protected abstract String getTaskName();

    /**
     * getInotifyEvents
     */
    protected abstract String[] getInotifyEvents();

    /**
     * 业务Check
     */
    protected abstract boolean eventCheck(String event, String watchPath, String filePath, String fileName, String channelId);

    /**
     * onEvent
     */
    protected void onEvent(String event, String watchPath, String filePath, String fileName, String channelId) {
        try {
            if (eventCheck(event, watchPath, filePath, fileName, channelId)) {
                doEvent(event, filePath, fileName, channelId);
            }
        } catch (Exception ex) {
            LOG.error(String.format("onEvent error event=%s filePath=%s fileName=%s channelId=%s", event, filePath, fileName, channelId), ex);
        }
    }

    /**
     * doEvent
     */
    protected abstract void doEvent(String event, String filePath, String fileName, String channelId);

    private void executeCmd(String channelId, String watchPath) {

        File workFolder = new File(System.getProperty("user.dir"));

        // kill process
        processKill(watchPath);

        String cmd = buildCmd(watchPath);
        LOG.info("AbstractFileMonitoService.executeCmd cmd:=" + cmd);

        BufferedReader reader = null;
        try {
            // start process
            Process process = Runtime.getRuntime().exec(cmd, null, workFolder);

            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    continue;
                }

                String[] result = line.split(":");
                if (result.length != 3) {
                    continue;
                }
                //LOG.info(result[0] + "->" + result[1] + "->" + result[2]);

                String strPath = result[0];
                String strFileName = result[1];
                String event = result[2];
                onEvent(event, watchPath, strPath, strFileName, channelId);
            }
        } catch (Exception e) {
            LOG.error("AbstractFileMonitoService.executeCmd error:", e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private String buildCmd(String watchPath) {
        StringBuilder bf = new StringBuilder(INOTIFY_WAIT_CMD);
        bf.append(" -mr");

        String[] events = getInotifyEvents();
        for (String event : events) {
            bf.append(" -e ");
            bf.append(event);
        }

        bf.append(" --format %w:%f:%e ");
        bf.append(watchPath);
        return bf.toString();
    }

    private boolean processKill(String cmdPath) {
        try {
            String cmd = String.format("ps -ef|grep %s |grep -v grep|cut -c 9-15|xargs kill -9", cmdPath);
            LOG.info("AbstractFileMonitoService.processKill cmd:=" + cmd);
            Process process = Runtime.getRuntime().exec(cmd);
            // 进程的出口值。根据惯例，0 表示正常终止。
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                return true;
            }
        } catch (Exception e) {
            LOG.error("AbstractFileMonitoService.executeKill error:", e.getMessage());
        }
        return false;
    }
}
