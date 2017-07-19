package com.voyageone.service.dao.cms.mongo;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.daoext.cms.CmsBtProductStatusHistoryDaoExt;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductStatusHistoryModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductStatusHistoryModel_History;
import com.voyageone.service.model.util.MapModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.springframework.data.mongodb.core.query.Criteria.where;

//import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-mongo-transfer.xml")
public class CmsBtProductStatusHistoryDaoTest {
    @Autowired
    private CmsBtProductStatusHistoryDao productStatusHistoryDao;

    @Test
    public void testInsert() throws Exception {
        long start = System.currentTimeMillis();
        CmsBtProductStatusHistoryModel model = new CmsBtProductStatusHistoryModel();
        model.setChannelId("001");
        model.setCartId(23);
        model.setCode("014312-t22");
        model.setModifier("yjdaa");
        model.setCreater("yjdab");

        CmsBtProductStatusHistoryModel_History history = new CmsBtProductStatusHistoryModel_History();

        history.setOperationType(20);
        history.setStatus("Pending");
        history.setComment("高级检索 重新计算指导售价(未同步到最终售价11)");
        history.setCreater("yjd-inside1");
        history.setModifier("yjd-inside2");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        history.setCreated(ts);
        history.setModified(ts);
        model.getList().add(history);

        productStatusHistoryDao.insert(model);

    }

    @Test
    public void testSelectCount() throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("channelId", "001");
        param.put("cartId", "23");
        param.put("code", "aaa");
        param.put("operationType", 11);
        param.put("status", "PendingABC");

        int count = productStatusHistoryDao.selectCount(param);
        Assert.assertEquals(1, count);
    }

    @Test
    public void testSelectCount2() throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("channelId", "001");
        param.put("cartId", "23");
        param.put("code", "aaa");
        param.put("operationType", 10);
        param.put("status", "PendingABC_zz");

        int count = productStatusHistoryDao.selectCount(param);
        Assert.assertEquals(0, count);
    }

    @Test
    public void testSelectList() throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("channelId", "001");
        param.put("cartId", "23");
        param.put("code", "aaa");
        param.put("operationType", 10);
//        param.put("status", "PendingABC");

        Map<String, String> orderMap = new HashMap<>();
        orderMap.put("modified", "desc");
        param.put("orderBy", orderMap);
        param.put("pageRowCount", 10);
        param.put("skip", 1);

        List<MapModel> historyModelList = productStatusHistoryDao.selectPage(param);
        //  Assert.assertEquals(4, historyModelList.size());
        System.out.println(historyModelList.get(0).toString());
    }

    @Autowired
    private CmsBtProductStatusHistoryDaoExt cmsBtProductStatusHistoryDaoExt;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 从mysql导入mongo
     */
    @Test
    public void transferMysql2Mongo() throws Exception {
        String[] channelIdArr = new String[]{"001", "007", "008", "009", "010", "012", "014", "015", "016", "017", "018", "019", "020", "021", "022", "023",
                "024", "025", "026", "027", "028", "029", "030", "031", "033", "034", "035", "928"};
//        String[] channelIdArr = new String[]{"018"};
        List<Runnable> threads = new ArrayList<>();
        for (String channelId : channelIdArr) {
            threads.add(() -> transferDataByChannel(channelId));
        }

        ExecutorService pool = Executors.newFixedThreadPool(channelIdArr.length);
        threads.forEach(pool::execute);
        pool.shutdown();
        // 等待子线程结束，再继续执行下面的代码
        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    }

    private void transferDataByChannel(String channelId) {
        Map<String, Object> param = new HashMap<>();
        final int pageSize = 10000;
        param.put("pageRowCount", pageSize);
//        param.put("orderByColumn", "id");
        param.put("channelId", channelId);
        long cnt = 0L;
        while (true) {
            long start = System.currentTimeMillis();
            long startId = findTransferProgress(channelId);
            param.put("startId", startId);
            List<MapModel> mapList = cmsBtProductStatusHistoryDaoExt.selectPage4Transfer(param);
            if (mapList == null || mapList.size() == 0) {
                System.out.println("channel finished:" + channelId + "!");
                break;
            }
            long id = 0L;
            for (MapModel mapModel : mapList) {
                CmsBtProductStatusHistoryModel mongoModel = new CmsBtProductStatusHistoryModel();
                mongoModel.setChannelId((String) mapModel.get("channelId"));
                mongoModel.setCartId((Integer) mapModel.get("cartId"));
                mongoModel.setCode((String) mapModel.get("code"));
                mongoModel.setCreater((String) mapModel.get("creater"));
                mongoModel.setModifier((String) mapModel.get("modifier"));
                mongoModel.setCreated(DateTimeUtil.format((Date) mapModel.get("created"), DateTimeUtil.DEFAULT_DATETIME_FORMAT));
                mongoModel.setModified(DateTimeUtil.format((Date) mapModel.get("modified"), DateTimeUtil.DEFAULT_DATETIME_FORMAT));

                CmsBtProductStatusHistoryModel_History history = new CmsBtProductStatusHistoryModel_History();
                history.setOperationType((Integer) mapModel.get("operationType"));
                history.setStatus((String) mapModel.get("status"));
                history.setComment((String) mapModel.get("comment"));
                history.setCreater((String) mapModel.get("creater"));
                history.setModifier((String) mapModel.get("modifier"));
                history.setCreated((Timestamp) mapModel.get("created"));
                history.setModified((Timestamp) mapModel.get("modified"));
                mongoModel.getList().add(history);

                productStatusHistoryDao.insert(mongoModel);
                id = Long.valueOf(Long.valueOf(mapModel.get("id").toString()));
            }
            saveTransferProgress(channelId, id);
            System.out.println("product status history processed records: " + channelId + "->" + mapList.size());
            long end = System.currentTimeMillis();
            System.out.println("cost in this loop(ms):" + (end - start));
            cnt += mapList.size();
            System.out.println("channel total processed:" + channelId + "-->" + cnt);
        }
    }

    private void saveTransferProgress(String channelId, long id) {
        Query query = new Query(where("channelId").is(channelId));
        Update update = Update.update("id", id);
        mongoTemplate.upsert(query, update, "cms_bt_product_status_history_progress");
    }

    @Test
    public void testSaveTransferProgress() throws Exception {
        saveTransferProgress("010", 120L);
//        saveTransferProgress("010",120L);
    }

    private long findTransferProgress(String channelId) {
        long id = -1L;
        Query query = new Query(where("channelId").is(channelId));
        Map<String, Object> progressmap = mongoTemplate.findOne(query, Map.class, "cms_bt_product_status_history_progress");
        if (progressmap != null) {
            Long idGet = (Long) progressmap.get("id");
            if (idGet != null) {
                return idGet.longValue();
            }
        }
        return id;
    }

    @Test
    public void testFindTransferProgress() throws Exception {
        long id = findTransferProgress("010");
        System.out.println(id);
    }

}