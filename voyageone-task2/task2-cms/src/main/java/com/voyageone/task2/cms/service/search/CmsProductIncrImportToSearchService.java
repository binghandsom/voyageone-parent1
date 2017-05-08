package com.voyageone.task2.cms.service.search;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.CursorType;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.spring.SpringContext;
import com.voyageone.components.solr.service.CmsProductSearchService;
import com.voyageone.task2.base.BaseListenService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.bson.BsonTimestamp;
import org.bson.Document;
import org.bson.types.BSONTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Cms OpLog Data 增量导入收索服务器 Service
 *
 * @author chuanyu.liang 2016/9/30.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
@SuppressWarnings("unused")
public class CmsProductIncrImportToSearchService extends BaseListenService {

    private static final String OPLOG_FILENAME = "mongo_oplog_timestamp";

    private final Gson gson = new GsonBuilder().create();

    private BSONTimestamp lastTimeStamp = null;
    private boolean isNeedCommit = false;

    @Autowired
    private CmsProductSearchService cmsProductSearchService;
    @Autowired
    private CmsProductIncrImportToCmsSearchService cmsProductIncrImportToSearchService;
    @Autowired
    private CmsProductIncrImportToDistSearchService cmsProductIncrImportToDistSearchService;


    private RedisTemplate cacheTemplate;
    private CmsBaseIncrImportSearchSubService[] importSubServiceBeanArray;

    /**
     * for test
     */
    protected void onStartup(List<TaskControlBean> taskControlList) {
        importSubServiceBeanArray = new CmsBaseIncrImportSearchSubService[]{
                cmsProductIncrImportToSearchService
//                ,cmsProductIncrImportToDistSearchService
        };
        super.onStartup(taskControlList);
    }

    @Override
    protected void initStartup(List<TaskControlBean> taskControlList) {
        cacheTemplate = CacheHelper.getCacheTemplate();
        this.lastTimeStamp = readTimestamp();
        handleCommit();
    }

    @Override
    protected Object onListen(List<TaskControlBean> taskControlList) {
        try {
            // get MongoClient
            MongoClient client = getMongoClient();
            // get MongoCollection
            MongoCollection<Document> fromCollection = client.getDatabase("local").getCollection("oplog.rs");
            // get Query
            BasicDBObject queryDBObject = getQueryDBObject();
            $info("CmsProductIncrImportToSearchService Start tailing with query:" + queryDBObject);
            try {
                return fromCollection.find(queryDBObject)
                        .sort(new BasicDBObject("$natural", 1))
                        .cursorType(CursorType.TailableAwait)
                        .noCursorTimeout(true).iterator();
            } catch (Exception e) {
                $info(e.getMessage());
                issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                return onListen(taskControlList);
            }
        }catch (Exception e){
            $info(e.getMessage());
            throw e;
        }
    }

    @Override
    protected void doEvent(List<TaskControlBean> taskControlList, Object eventObj) {
        @SuppressWarnings("unchecked")
        MongoCursor<Document> opCursor = (MongoCursor<Document>) eventObj;
        if (opCursor.hasNext()) {
            Document nextOp = opCursor.next();
            BsonTimestamp ts = (BsonTimestamp) nextOp.get("ts");
            lastTimeStamp = new BSONTimestamp(ts.getTime(), ts.getInc());
            handleOp(nextOp);
        }
    }

    /**
     * getMongoClient
     *
     * @return MongoClient
     */
    private MongoClient getMongoClient() {
        return (MongoClient) SpringContext.getBean("mongoClient");
    }

    /**
     * getQueryDBObject
     */
    private BasicDBObject getQueryDBObject() {
        BasicDBObject andQuery = new BasicDBObject();
        List<BasicDBObject> obj = new ArrayList<>();
        obj.add(getTimeQuery());
        obj.add(getCollectNameRegexQuery());
        andQuery.put("$and", obj);
        return andQuery;
    }

    /**
     * Return time query depending on lastTimeStamp
     * If lastTimeStamp is null, will return query that fetch ALL data from oplog
     * if lastTimeStamp is not null, return query that fetch data started from this time stamp
     */
    private BasicDBObject getTimeQuery() {
        final BasicDBObject timeQuery = new BasicDBObject();
        if (lastTimeStamp != null) {
            timeQuery.put("ts", BasicDBObjectBuilder.start("$gt", lastTimeStamp).get());
        }
        return timeQuery;
    }

    /**
     * getCollectNameRegexQuery
     */
    private BasicDBObject getCollectNameRegexQuery() {
        final BasicDBObject collectNameRegexQuery = new BasicDBObject();
        collectNameRegexQuery.put("ns", new BasicDBObject("$regex", "cms_bt_product_c\\d{3}$"));
        return collectNameRegexQuery;
    }

    /**
     * readTimestamp
     */
    private BSONTimestamp readTimestamp() {
        String valueStr = (String) cacheTemplate.opsForValue().get(OPLOG_FILENAME);
        if (valueStr != null) {
            try {
                return gson.fromJson(valueStr, BSONTimestamp.class);
            } catch (Exception e) {
                $error("CmsProductIncrImportToSearchService.readTimestamp", e);
            }
        }
        return null;
    }

    /**
     * persistTimeStamp
     */
    private void persistTimeStamp(BSONTimestamp timestamp) {
        try {
            if (timestamp != null) {
                //noinspection unchecked
                cacheTemplate.opsForValue().set(OPLOG_FILENAME, gson.toJson(timestamp));
            }
        } catch (Exception e) {
            $error("CmsProductIncrImportToSearchService.persistTimeStamp", e);
        }
    }

    /**
     * handleOp
     */
    private void handleOp(Document op) {
        try {
            for (CmsBaseIncrImportSearchSubService service : importSubServiceBeanArray) {
                switch ((String) op.get("op")) { // usually op looks like {"op": "i"} or {"op": "u"}
                    case "i":
                        // insert event in mongodb
                        if (service.handleInsert(op)) {
                            isNeedCommit = true;
                        }
                        break;
                    case "u":
                        // update event in mongodb
                        if (!"repl.time".equals(op.getString("ns"))) {
                            if (service.handleUpdate(op)) {
                                isNeedCommit = true;
                            }
                        }
                        break;
                    case "d":
                        // delete event in mongodb
                        if (service.handleDelete(op)) {
                            isNeedCommit = true;
                        }
                        break;
                    default:
                        $error("CmsProductIncrImportToSearchService Non-handled operation: " + op);
                        break;
                }
            }
        }catch (Exception e){
            $error(op.toJson(), e);
        }
    }


    /**
     * handleCommit
     */
    private void handleCommit() {
        $info("CmsProductIncrImportToSearchService.handleCommit");
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        long initialDelay1 = 1;
        long period1 = 5;
        // 从现在开始1秒钟之后，每隔5秒钟执行一次job1
        executor.scheduleAtFixedRate(
                new HandleCommitRunnable(), initialDelay1,
                period1, TimeUnit.SECONDS);
    }

    /**
     * HandleCommitRunnable
     */
    private class HandleCommitRunnable implements Runnable {
        @Override
        public void run() {
            if (isNeedCommit) {
                isNeedCommit = false;
                cmsProductSearchService.commit();
                persistTimeStamp(lastTimeStamp);
                $info("CmsProductIncrImportToSearchService.HandleCommitRunnable lastTimeStamp:" + lastTimeStamp.toString());
            }
        }
    }

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return getClass().getSimpleName();
    }
}
