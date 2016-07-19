package com.voyageone.task2.cms.service.monitor;

import com.voyageone.common.util.StringUtils;
import com.voyageone.task2.base.dao.TaskDao;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.apache.commons.net.ftp.FTPClient;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 抽象文件监控服务，文件监控服务的父类
 *
 * @author aooer 2016/6/2.
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class AbstractFileMonitoService {

    /* 监控 */
    private final static String INOTIFY_WAIT_CMD = "/usr/bin/inotifywait";

    /* 日志 */
    protected final Logger LOG = LoggerFactory.getLogger(getClass());
    // 锁
    private Lock lock = new ReentrantLock();
    @Autowired
    protected TaskDao taskDao;

    /**
     * onApplicationEvent
     *
     * @param applicationEvent ApplicationEvent
     */
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

        // 线程List
        List<Runnable> threads = new ArrayList<>();
        // 循环处理批量图片给上传
        for (FileMonitorBean fileMonitorBean : getFilePaths()) {
            String finalPath = fileMonitorBean.getFilePath();
            LOG.info(String.format("AbstractFileMonitoService.run finalPath=%s", finalPath));

            if (StringUtils.isEmpty(finalPath)) {
                continue;
            }

            File filePath = new File(finalPath);
            if (!filePath.exists()) {
                LOG.warn(String.format("AbstractFileMonitoService.run filePath not found finalPath=%s", finalPath));
                continue;
            }
            if (!filePath.isDirectory()) {
                continue;
            }

            threads.add(() -> executeCmd(fileMonitorBean));
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

    private void executeCmd(FileMonitorBean fileMonitorBean) {
        String watchPath = fileMonitorBean.getFilePath();
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
                    Thread.sleep(5000);
                    continue;
                }

                String[] result = line.split(":");
                if (result.length != 3) {
                    Thread.sleep(5000);
                    continue;
                }
                //LOG.info(result[0] + "->" + result[1] + "->" + result[2]);

                String strPath = result[0];
                String strFileName = result[1];
                String event = result[2];
                onEvent(event, new File(strPath+strFileName), fileMonitorBean);
            }
        } catch (Exception e) {
            LOG.error("AbstractFileMonitoService.executeCmd error:" + e.getMessage());
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
            //获得锁
            lock.lock();
            String cmd = String.format("ps -ef|grep %s |grep -v grep|cut -c 9-15|xargs kill -9", cmdPath);
            LOG.info("AbstractFileMonitoService.processKill cmd:=" + cmd);
            Process process = Runtime.getRuntime().exec(cmd);
            // 进程的出口值。根据惯例，0 表示正常终止。
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                return true;
            }
        } catch (Exception e) {
            LOG.error("AbstractFileMonitoService.executeKill error:" + e.getMessage());
        } finally {
            //释放锁
            lock.unlock();
        }
        return false;
    }

    /**
     * onEvent
     */
    protected void onEvent(String event, File file, FileMonitorBean fileMonitorBean) {
        try {
            if (eventCheck(event, file, fileMonitorBean)) {
                doEvent(event, file, fileMonitorBean);
            }
        } catch (Exception ex) {
            LOG.error(String.format("onEvent error event=%s filePath=%s fileName=%s", event, file.getPath(), file.getName()), ex);
        }
    }

    /**
     * getTaskName
     */
    protected abstract String getTaskName();

    /**
     * getFinalPaths
     */
    protected abstract FileMonitorBean[] getFilePaths();

    /**
     * getInotifyEvents
     */
    protected abstract String[] getInotifyEvents();

    /**
     * 业务Check
     */
    protected abstract boolean eventCheck(String event, File file, FileMonitorBean fileMonitorBean);
    /**
     * doEvent
     */
    protected abstract void doEvent(String event, File file, FileMonitorBean fileMonitorBean);

}
