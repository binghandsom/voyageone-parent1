package com.voyageone.task2.cms.service.search;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.CursorType;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.spring.SpringContext;
import com.voyageone.components.solr.bean.SolrUpdateBean;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Cms Product Data 增量导入收索服务器 Service
 *
 * @author chuanyu.liang 2016/9/30.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class CmsProductIncrImportToSearchService extends BaseListenService {

    private static final String OPLOG_FILENAME = "mongo_oplog_timestamp";

    private final Gson gson = new GsonBuilder().create();

    private BSONTimestamp lastTimeStamp = null;
    private boolean isNeedCommit = false;

    @Autowired
    private CmsProductSearchService cmsProductSearchService;

    private RedisTemplate cacheTemplate;

    /**
     * for test
     */
    protected void onStartup(List<TaskControlBean> taskControlList) {
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
        // get MongoClient
        MongoClient client = getMongoClient();
        // get MongoCollection
        MongoCollection<Document> fromCollection = client.getDatabase("local").getCollection("oplog.rs");
        // get Query
        BasicDBObject queryDBObject = getQueryDBObject();
        $info("CmsProductIncrImportToSearchService Start tailing with query:" + queryDBObject);
        return fromCollection.find(queryDBObject)
                .sort(new BasicDBObject("$natural", 1))
                .cursorType(CursorType.TailableAwait)
                .noCursorTimeout(true).iterator();
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
        return (MongoClient) SpringContext.getBean("mongo");
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
        String valueStr = (String)cacheTemplate.opsForValue().get(OPLOG_FILENAME);
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
        switch ((String) op.get("op")) { // usually op looks like {"op": "i"} or {"op": "u"}
            case "i":
                handleInsert(op); // insert event in mongodb
                break;
            case "u": // update event in mongodb
                if (!"repl.time".equals(op.getString("ns"))) {
                    handleUpdate(op);
                }
                break;
            case "d":
                handleDelete(op); // delete event in mongodb
                break;
            default:
                $error("CmsProductIncrImportToSearchService Non-handled operation: " + op);
                break;
        }
    }

    /**
     * handleInsert
     */
    private void handleInsert(Document document) {
        $debug("CmsProductIncrImportToSearchService.handleInsert" + document.toJson());
        Document objectDoc = (Document) document.get("o");
        if (objectDoc == null) {
            return;
        }

        String id = objectDoc.get("_id").toString();
        String productChannel = (String) objectDoc.get("channelId");
        String productCode = null;
        String productModel = null;

        Document commonDoc = (Document) objectDoc.get("common");
        List<String> skuCodeList = new ArrayList<>();
        //noinspection Duplicates
        if (commonDoc != null) {
            Document fieldsDoc = (Document) commonDoc.get("fields");
            if (fieldsDoc != null) {
                productCode = (String) fieldsDoc.get("code");
                productModel = (String) fieldsDoc.get("model");
            }
            Object skusObj = commonDoc.get("skus");
            if (skusObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<Document> skuListDoc = (List<Document>) commonDoc.get("skus");
                if (skuListDoc != null) {
                    skuListDoc.stream().filter(sku -> sku != null && sku.get("skuCode") != null).forEach(sku -> {
                        String skuCode = (String) sku.get("skuCode");
                        if (skuCode.length() > 0) {
                            skuCodeList.add(skuCode);
                        }
                    });
                }
            }
        }

        SolrUpdateBean update = cmsProductSearchService.createUpdate(id, productChannel, productCode, productModel, skuCodeList, null, null);
        if (update != null) {
            String response = cmsProductSearchService.saveBean(update);
            $debug("CmsProductIncrImportToSearchService.handleInsert commit ; response:" + response);
            isNeedCommit = true;
            //cmsProductSolrTemplate.commit();
        }
    }

    /**
     * handleUpdate
     */
    private void handleUpdate(Document document) {
        $debug("CmsProductIncrImportToSearchService.handleUpdate:" + document.toJson());
        Document objectDoc = (Document) document.get("o");
        if (objectDoc == null) {
            return;
        }

        String id = null;
        Document object2Doc = ((Document) document.get("o2"));
        if (object2Doc != null) {
            Object idObject = object2Doc.get("_id");
            if (idObject != null) {
                id = idObject.toString();
            }
        }
        if (id == null) {
            return;
        }

        String productChannel = null;
        String nsStr = ((String) document.get("ns"));
        if (nsStr != null && nsStr.length() > 4) {
            productChannel = nsStr.substring(nsStr.length() - 3, nsStr.length());
        }
        if (productChannel == null) {
            return;
        }

        String productCode = null;
        String productModel = null;
        List<String> skuCodeListTmp = new ArrayList<>();
        List<String> skuCodeAddListTmp = new ArrayList<>();

        if (objectDoc.containsKey("$set")) {
            Document setDoc = (Document) objectDoc.get("$set");
            if (setDoc != null) {
                // common.fields.code
                if (setDoc.containsKey("common.fields.code")) {
                    productCode = (String) setDoc.get("common.fields.code");
                } else if (setDoc.containsKey("common.fields")) {
                    Document fieldsDoc = (Document) setDoc.get("common.fields");
                    if (fieldsDoc != null) {
                        productCode = (String) fieldsDoc.get("code");
                    }
                } else if (setDoc.containsKey("common")) {
                    Document commonDoc = (Document) objectDoc.get("common");
                    if (commonDoc != null) {
                        Document fieldsDoc = (Document) commonDoc.get("fields");
                        if (fieldsDoc != null) {
                            productCode = (String) fieldsDoc.get("code");
                        }
                    }
                }
                // common.fields.model
                if (setDoc.containsKey("common.fields.model")) {
                    productModel = (String) setDoc.get("common.fields.model");
                } else if (setDoc.containsKey("common.fields")) {
                    Document fieldsDoc = (Document) setDoc.get("common.fields");
                    if (fieldsDoc != null) {
                        productModel = (String) fieldsDoc.get("model");
                    }
                } else if (setDoc.containsKey("common")) {
                    Document commonDoc = (Document) objectDoc.get("common");
                    if (commonDoc != null) {
                        Document fieldsDoc = (Document) commonDoc.get("fields");
                        if (fieldsDoc != null) {
                            productModel = (String) fieldsDoc.get("model");
                        }
                    }
                }

                // common.fields.skus
                if (setDoc.containsKey("common.skus")) {
                    @SuppressWarnings("unchecked")
                    List<Document> skuListDoc = (List<Document>) setDoc.get("common.skus");
                    //noinspection Duplicates
                    if (skuListDoc != null) {
                        skuListDoc.stream().filter(sku -> sku != null && sku.get("skuCode") != null).forEach(sku -> {
                            String skuCode = (String) sku.get("skuCode");
                            if (skuCode.length() > 0) {
                                skuCodeListTmp.add(skuCode);
                            }
                        });
                    }
                } else if (setDoc.containsKey("common")) {
                    Document commonDoc = (Document) objectDoc.get("common");
                    if (commonDoc != null) {
                        @SuppressWarnings("unchecked")
                        List<Document> skusDoc = (List<Document>) commonDoc.get("skus");
                        //noinspection Duplicates
                        if (skusDoc != null) {
                            skusDoc.stream().filter(sku -> sku != null && sku.get("skuCode") != null).forEach(sku -> {
                                String skuCode = (String) sku.get("skuCode");
                                if (skuCode.length() > 0) {
                                    skuCodeListTmp.add(skuCode);
                                }
                            });
                        }
                    }
                } else {
                    for (Map.Entry<String, Object> entry : setDoc.entrySet()) {
                        String key = entry.getKey();
                        if (key != null && key.startsWith("common.skus.") && key.endsWith(".skuCode")) {
                            skuCodeAddListTmp.add(String.valueOf(entry.getValue()));
                        }
                    }
                }
            }
        } else {
            productChannel = (String) objectDoc.get("channelId");
            productCode = null;
            productModel = null;

            Document commonDoc = (Document) objectDoc.get("common");
            //noinspection Duplicates
            if (commonDoc != null) {
                Document fieldsDoc = (Document) commonDoc.get("fields");
                if (fieldsDoc != null) {
                    productCode = (String) fieldsDoc.get("code");
                    productModel = (String) fieldsDoc.get("model");
                }

                Object skusObj = commonDoc.get("skus");
                if (skusObj instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<Document> skusDoc = (List<Document>) commonDoc.get("skus");
                    //noinspection Duplicates
                    if (skusDoc != null) {
                        skusDoc.stream().filter(sku -> sku != null && sku.get("skuCode") != null).forEach(sku -> {
                            String skuCode = (String) sku.get("skuCode");
                            if (skuCode.length() > 0) {
                                skuCodeListTmp.add(skuCode);
                            }
                        });
                    }
                }
            }
        }

        List<String> skuCodeList = null;
        if (!skuCodeListTmp.isEmpty()) {
            skuCodeList = new ArrayList<>();
            skuCodeList.addAll(skuCodeListTmp);
        }
        List<String> skuCodeAddList = null;
        if (!skuCodeAddListTmp.isEmpty()) {
            skuCodeAddList = new ArrayList<>();
            skuCodeAddList.addAll(skuCodeAddListTmp);
        }

        SolrUpdateBean update = cmsProductSearchService.createUpdate(id, productChannel, productCode, productModel, skuCodeList, skuCodeAddList, null);
        if (update != null) {
            String response = cmsProductSearchService.saveBean(update);
            $debug("CmsProductIncrImportToSearchService.handleUpdate commit ; response" + response);
            //cmsProductSolrTemplate.commit();
            isNeedCommit = true;
        }
    }

    /**
     * handleDelete
     */
    private void handleDelete(Document document) {
        $debug("CmsProductIncrImportToSearchService.handleDelete:" + document.toJson());
        Document objectDoc = (Document) document.get("o");
        if (objectDoc == null) {
            return;
        }

        String id = null;
        Object idObject = objectDoc.get("_id");
        if (idObject != null) {
            id = idObject.toString();
        }
        if (id == null) {
            return;
        }

        String response = cmsProductSearchService.deleteById(id);
        $debug("CmsProductIncrImportToSearchService.handleDelete commit ; response:" + response);
//        cmsProductSearchService.commit();
        isNeedCommit = true;
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
