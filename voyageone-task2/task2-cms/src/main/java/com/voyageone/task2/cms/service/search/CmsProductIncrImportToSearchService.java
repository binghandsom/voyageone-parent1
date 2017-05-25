package com.voyageone.task2.cms.service.search;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.CursorType;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
    private static final String NON_SHARD = "NON_SHARD";
    private final Gson gson = new GsonBuilder().create();

    private ThreadLocal<String> shardId = new ThreadLocal<>();
    //    private ThreadLocal<BSONTimestamp> lastTimeStamp = new ThreadLocal<>();
//    private ThreadLocal<Boolean> isNeedCommit = new ThreadLocal<>();
    private ThreadLocal<Map<String, MongoClient>> clientMap = new ThreadLocal<>();
    private ConcurrentHashMap<String, Boolean> shardNeedCommitMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, BSONTimestamp> shardLastTimestampMap = new ConcurrentHashMap<>();

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
    protected void onStartup(Map<String, MongoClient> shardMap, List<TaskControlBean> taskControlList, String shardId) {
        importSubServiceBeanArray = new CmsBaseIncrImportSearchSubService[]{
                cmsProductIncrImportToSearchService
//                ,cmsProductIncrImportToDistSearchService
        };
        super.onStartup(shardMap, taskControlList, shardId);
    }

    @Override
    protected void initStartup(Map<String, MongoClient> shardMap, List<TaskControlBean> taskControlList, String shardId) {
        cacheTemplate = CacheHelper.getCacheTemplate();
        this.clientMap.set(shardMap);
        this.shardId.set(shardId);
        BSONTimestamp ts = readTimestamp();
        if (shardId == null) {
            shardNeedCommitMap.put(NON_SHARD, false);
            if (ts != null) {
                shardLastTimestampMap.put(NON_SHARD, readTimestamp());
            }
            handleCommit(null);
        } else {
            Iterator<String> shardIterator = shardMap.keySet().iterator();
            while (shardIterator.hasNext()) {
                String curShardId = shardIterator.next();
                shardNeedCommitMap.put(curShardId, false);
                if (ts != null) {
                    shardLastTimestampMap.put(curShardId, readTimestamp());
                }
                handleCommit(curShardId);
            }
        }
    }

    @Override
    protected Object onListen(List<TaskControlBean> taskControlList, String shardId) {
        try {
            // get MongoClient
            MongoClient mongoClient = clientMap.get().get(shardId);
            // get MongoCollection
//            MongoCollection<Document> fromCollection = client.getDatabase("local").getCollection("oplog.rs");
            MongoCollection<Document> fromCollection = mongoClient.getDatabase("local").getCollection("oplog.rs");            // get Query
            BasicDBObject queryDBObject = getQueryDBObject();
            if (this.shardId.get() != null) {
                $info("processing shard:" + this.shardId.get());
            }
            $info("CmsProductIncrImportToSearchService Start tailing with query:" + queryDBObject);
            try {
                return fromCollection.find(queryDBObject)
                        .sort(new BasicDBObject("$natural", 1))
                        .cursorType(CursorType.TailableAwait)
                        .noCursorTimeout(true).iterator();
            } catch (Exception e) {
                e.printStackTrace();
                $info(e.getMessage());
                issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                return onListen(taskControlList, shardId);
            }
        } catch (Exception e) {
            $info(e.getMessage());
            throw e;
        }
    }

    @Override
    protected void doEvent(List<TaskControlBean> taskControlList, Object eventObj) {
        System.out.print("in doEvent");
        @SuppressWarnings("unchecked")
        MongoCursor<Document> opCursor = (MongoCursor<Document>) eventObj;
        if (opCursor.hasNext()) {
            Document nextOp = opCursor.next();
            BsonTimestamp ts = (BsonTimestamp) nextOp.get("ts");
//            lastTimeStamp.set(new BSONTimestamp(ts.getTime(), ts.getInc()));
            if (this.shardId.get() != null) {
                $info("正在处理shard(" + this.shardId.get() + ") : " + nextOp.toJson() + "  ts:" + ts.toString());
                shardLastTimestampMap.put(this.shardId.get(), new BSONTimestamp(ts.getTime(), ts.getInc()));
            } else {
                $info("正在处理非shard : " + nextOp.toJson() + "  ts:" + ts.toString());
                shardLastTimestampMap.put(NON_SHARD, new BSONTimestamp(ts.getTime(), ts.getInc()));
            }
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
        if (getTimeQuery() != null) {
            obj.add(getTimeQuery());
        }
        obj.add(getCollectNameRegexQuery());
        if (this.shardId.get() != null) {
            obj.add(getShNonMigrateQuery());
        }
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
        BSONTimestamp ts;
        if (this.shardId.get() != null) {
            ts = shardLastTimestampMap.get(this.shardId.get());
        } else {
            ts = shardLastTimestampMap.get(NON_SHARD);
        }
        if (ts != null) {
            timeQuery.put("ts", BasicDBObjectBuilder.start("$gt", ts).get());
            return timeQuery;
        }
        return null;
    }

    /**
     * getCollectNameRegexQuery
     */
    private BasicDBObject getCollectNameRegexQuery() {
        final BasicDBObject collectNameRegexQuery = new BasicDBObject();
        collectNameRegexQuery.put("ns", new BasicDBObject("$regex", "cms_bt_product_c\\d{3}$"));
        return collectNameRegexQuery;
    }

    private BasicDBObject getShNonMigrateQuery() {
        final BasicDBObject shNonMigrateQuery = new BasicDBObject();
        shNonMigrateQuery.put("fromMigrate", new BasicDBObject("$exists", false));
        return shNonMigrateQuery;
    }

    /**
     * readTimestamp
     */
    private BSONTimestamp readTimestamp() {
        String oplogKey = OPLOG_FILENAME;
        if (this.shardId.get() != null) {
            oplogKey = oplogKey + "-" + this.shardId.get();
        }
        String valueStr = (String) cacheTemplate.opsForValue().get(oplogKey);
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
     * handleOp
     */
    private void handleOp(Document op) {
        try {
            String shardKey = this.shardId.get() == null ? NON_SHARD : this.shardId.get();
            for (CmsBaseIncrImportSearchSubService service : importSubServiceBeanArray) {
                switch ((String) op.get("op")) { // usually op looks like {"op": "i"} or {"op": "u"}
                    case "i":
                        // insert event in mongodb
                        if (service.handleInsert(op)) {
                            shardNeedCommitMap.put(shardKey, true);
                        }
                        break;
                    case "u":
                        // update event in mongodb
                        if (!"repl.time".equals(op.getString("ns"))) {
                            if (service.handleUpdate(op)) {
                                shardNeedCommitMap.put(shardKey, true);
                            }
                        }
                        break;
                    case "d":
                        // delete event in mongodb
                        if (service.handleDelete(op)) {
                            shardNeedCommitMap.put(shardKey, true);
                        }
                        break;
                    default:
                        $error("CmsProductIncrImportToSearchService Non-handled operation: " + op);
                        break;
                }
                $debug(String.format("op:%s,isNeedCommit:%s", op.get("op"), shardNeedCommitMap.get(shardKey)));
            }
        } catch (Exception e) {
            $error(op.toJson(), e);
        }
    }


    /**
     * handleCommit
     */

    private void handleCommit(String shardId) {
        $info("CmsProductIncrImportToSearchService.handleCommit");
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        long initialDelay1 = 1;
        long period1 = 5;
        // 从现在开始1秒钟之后，每隔5秒钟执行一次job1
//        executor.submit(new HandleCommitRunnable());
        executor.scheduleAtFixedRate(
                new HandleCommitRunnable(shardId), initialDelay1,
                period1, TimeUnit.SECONDS);
    }

    /**
     * HandleCommitRunnable
     */
    private class HandleCommitRunnable implements Runnable {

        private String shardId;
        private final static String CORE_NAME_PRODUCT = "cms_product";

        public HandleCommitRunnable(String shardId) {
            this.shardId = shardId;
        }

        @Override
        public void run() {
            String shardKey = this.shardId == null ? NON_SHARD : this.shardId;
            boolean isNeeded = shardNeedCommitMap.get(shardKey);
            $debug("CmsProductIncrImportToSearchService.HandleCommitRunnable isNeedCommit:" + shardKey + "->" + isNeeded);
            try {
                if (isNeeded) {
                    shardNeedCommitMap.put(shardKey, false);
                    cmsProductSearchService.commit(CORE_NAME_PRODUCT);
                    BSONTimestamp lastTs = shardLastTimestampMap.get(shardKey);
                    persistTimeStamp(lastTs);
                    $info("CmsProductIncrImportToSearchService.HandleCommitRunnable lastTimeStamp:" + lastTs.toString());
                }
            } catch (Exception e) {
                $error("commit error:" + shardKey);
                e.printStackTrace();
            }
        }

        /**
         * persistTimeStamp
         */
        private void persistTimeStamp(BSONTimestamp timestamp) {
            try {
                if (timestamp != null) {
                    String oplogKey = OPLOG_FILENAME;
                    if (this.shardId != null) {
                        oplogKey = oplogKey + "-" + this.shardId;
                    }
                    //noinspection unchecked
                    cacheTemplate.opsForValue().set(oplogKey, gson.toJson(timestamp));
                }
            } catch (Exception e) {
                $error("CmsProductIncrImportToSearchService.persistTimeStamp", e);
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
