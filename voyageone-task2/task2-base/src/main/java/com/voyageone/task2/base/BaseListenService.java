package com.voyageone.task2.base;

import com.mongodb.MongoClient;
import com.mongodb.MongoQueryException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCursor;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.spring.SpringContext;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.apache.log4j.MDC;
import org.bson.Document;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
            //　如果数据库已分片，取得所有的分片连接
            MongoClient mongoClient = (MongoClient) SpringContext.getBean("mongoClient");

            MongoCursor<Document> shardsCur = mongoClient.getDatabase("config").getCollection("shards").find().iterator();
            List<Runnable> threads = new ArrayList<>();
            Map<String, MongoClient> shardMap = new HashMap<>();
            if (shardsCur.hasNext()) {
                while (shardsCur.hasNext()) {
                    Document shardSet = shardsCur.next();
                    String host = shardSet.get("host", String.class);
                    String shardId = shardSet.get("_id", String.class);
                    List<ServerAddress> shardSeeds = buildMongoConnUri(host);
                    MongoClient shardClient = new MongoClient(shardSeeds, mongoClient.getCredentialsList());
                    shardMap.put(shardId, shardClient);
                }
                Iterator<String> shardIterator = shardMap.keySet().iterator();
                while (shardIterator.hasNext()) {
                    String shardId = shardIterator.next();
                    threads.add(() -> onStartup(shardMap, taskControlList, shardId));
                }
            } else {
                shardMap.put(null, mongoClient);
                threads.add(() -> onStartup(shardMap, taskControlList, null));
            }

            ExecutorService pool = Executors.newFixedThreadPool(threads.size());
            threads.forEach(pool::execute);
            pool.shutdown();
            // 等待子线程结束，再继续执行下面的代码
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
            running = false;
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

    private List<ServerAddress> buildMongoConnUri(String host) {
        int idx = host.indexOf('/');
        String[] hostArr = host.substring(idx + 1).split(",");
        List<ServerAddress> serverAddressList = new ArrayList<>();
        for (String shardHost : hostArr) {
            serverAddressList.add(new ServerAddress(shardHost));
        }

        return serverAddressList;
    }

    /**
     * 必须实现的，具体的任务内容
     *
     * @param taskControlList job 配置
     */
    protected void onStartup(Map<String, MongoClient> shardMap, List<TaskControlBean> taskControlList, String shardId) {
        initStartup(shardMap, taskControlList, shardId);
        Object eventObj = onListen(taskControlList, shardId);
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
            } catch (MongoQueryException e) {
                $error(" MongoQueryException 出现异常，任务退出", e);
                eventObj = onListen(taskControlList, shardId);
            } catch (Exception e) {
                logIssue(e);
                $error("出现异常，任务退出", e);
                throw e;
            }
        }
    }

    protected void initStartup(Map<String, MongoClient> shardMap, List<TaskControlBean> taskControlList, String shardId) {
    }

    protected abstract Object onListen(List<TaskControlBean> taskControlList, String shardId);

    protected abstract void doEvent(List<TaskControlBean> taskControlList, Object eventObj);

}
