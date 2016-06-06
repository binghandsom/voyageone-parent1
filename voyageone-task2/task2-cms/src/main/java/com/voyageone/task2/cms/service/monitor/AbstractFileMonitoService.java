package com.voyageone.task2.cms.service.monitor;

import com.voyageone.common.configs.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * 抽象文件监控服务，文件监控服务的父类
 *
 * @author aooer 2016/6/2.
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class AbstractFileMonitoService implements ApplicationListener{

    /* 日志 */
    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    /* 监控路径 */
    private final String MONITOR_HOME_PATH = getClass().getSimpleName() + "_monitor_home_path";

    protected final String MODIFIER=getClass().getSimpleName();

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof ContextRefreshedEvent) run();
    }

    private void run() {
        try {
            final String finalPath = raisePath();
            final Path watchPath= Paths.get(finalPath);
            WatchService watchService = watchPath.getFileSystem().newWatchService();
            LOG.info("文件监听程序：操作系统"+watchPath.getFileSystem());
            /* 注册监听“创建”、“删除”、“更新”事件 */
            watchPath.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            while (true) {
                WatchKey key = watchService.take();
                key.pollEvents().forEach(e -> {
                    String absPath= finalPath + "/"+e.context().toString();
                    if (e.kind().equals(ENTRY_CREATE)) onCreate(absPath);
                    else if (e.kind().equals(ENTRY_DELETE)) onDelete(absPath);
                    else if (e.kind().equals(ENTRY_MODIFY)) onModify(absPath);
                    else LOG.warn("文件监控服务监控到未处理的事件：" + absPath + "\t" + e.kind());
                });
                if (!key.reset()) break;
            }
        } catch (IOException e) {
            LOG.error("文件监控服务启动严重异常", e);
        } catch (InterruptedException e) {
            LOG.error("文件监控服务线程中断异常", e);
        }
    }

    private String raisePath() throws IOException {
        String path ;
        try {
            path = Properties.readValue(MONITOR_HOME_PATH);
        }catch (NullPointerException e){
            Properties.init();
        }
        return Properties.readValue(MONITOR_HOME_PATH);
    }

    protected abstract void onCreate(String filePath);

    protected abstract void onDelete(String filePath);

    protected abstract void onModify(String filePath);

}
