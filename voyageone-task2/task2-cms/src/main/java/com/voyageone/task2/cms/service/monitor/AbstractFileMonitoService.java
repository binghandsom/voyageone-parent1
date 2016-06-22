package com.voyageone.task2.cms.service.monitor;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.dao.TaskDao;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 抽象文件监控服务，文件监控服务的父类
 *
 * @author aooer 2016/6/2.
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class AbstractFileMonitoService implements ApplicationListener {

    /* 监控 */
    protected final static String INOTIFYWAIT_CMD = "/usr/bin/inotifywait";
    protected final static String[] events = {"close_write"};

    /* 日志 */
    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    protected final String MODIFIER = getClass().getSimpleName();

    protected final String TASK_NAME = "CmsBulkUploadImageToS7Job";

    @Autowired
    private TaskDao taskDao;

    private void run() {

        LOG.info("AbstractFileMonitoService.run");

        List<TaskControlBean> taskControlList = taskDao.getTaskControlList(TASK_NAME);

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
                    continue;
                }

                File[] files = filePath.listFiles();
                if (files == null) {
                    continue;
                }

                for (File childFile : files) {
                    new Thread(() -> {
                        try {
                            LOG.info(String.format("AbstractFileMonitoService.run channelId=%s;path=%s", channelId, childFile.getPath()));
                            executeCmd(channelId, childFile.getPath(), taskControlList, 1);
                        } catch (IOException e) {
                            LOG.error("文件监控服务启动严重异常", e);
                        }

                    }).start();
                }
            }
        }
    }


    protected abstract void onCreate(String filePath, String fileName, String channelId);

    protected abstract void onDelete(String filePath, String fileName);

    protected abstract void onModify(String filePath, String fileName, String channelId);

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        LOG.info("AbstractFileMonitoService.start");
        if (applicationEvent instanceof ContextRefreshedEvent) {
            run();
        }
    }

    private void executeCmd(String channelId, String watchPath, List<TaskControlBean> taskControlList, int defaultThreadCount) throws IOException {

        String threadCount = TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.thread_count);
        int intThreadCount = defaultThreadCount;

        if (!StringUtils.isNullOrBlank2(threadCount)) {
            intThreadCount = Integer.valueOf(threadCount);
        }

        // 如果最终计算获得线程数量无效，则提示错误
        if (intThreadCount < 1) {
            throw new IllegalArgumentException("thread count error.");
        }

        ExecutorService pool = Executors.newFixedThreadPool(intThreadCount);

        File workFolder = new File(System.getProperty("user.dir"));

        String cmd = buildCmd(watchPath);
        LOG.info("AbstractFileMonitoService.executeCmd cmd:=" + cmd);

        Process process = Runtime.getRuntime().exec(cmd, null, workFolder);

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (true) {
                String line = reader.readLine();
                if(StringUtil.isEmpty(line)) continue;
                String[] result = line.split(":");

                if (result.length != 3) {
                    continue;
                }
                LOG.info(result[0] + "->" + result[1] + "->" + result[2]);

                String strPath = result[0];
                String strFileName = result[1];
                try {
                    pool.execute(() -> onModify(strPath, strFileName, channelId));
                } catch (Exception e) {
                    LOG.error("AbstractFileMonitoService.executeCmd", e);
                }
            }
        } finally {
            if (null != reader)
                reader.close();
        }
    }

    private String buildCmd(String watchPath) {
        StringBuilder bf = new StringBuilder(INOTIFYWAIT_CMD);
        bf.append(" -mr");

        for (String event : events) {
            bf.append(" -e ");
            bf.append(event);
        }
        bf.append(" --format %w:%f:%e ");
        bf.append(watchPath);
        return bf.toString();
    }
}
