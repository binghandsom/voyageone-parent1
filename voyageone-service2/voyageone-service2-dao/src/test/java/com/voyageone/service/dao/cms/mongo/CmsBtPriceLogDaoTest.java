package com.voyageone.service.dao.cms.mongo;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.model.cms.CmsBtPriceLogModel_Mysql;
import com.voyageone.service.model.cms.mongo.product.CmsBtPriceLogFlatModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtPriceLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtPriceLogModel_History;
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
@ContextConfiguration(locations = "classpath:test-context.xml")
public class CmsBtPriceLogDaoTest {
    @Autowired
    private CmsBtPriceLogDao priceLogDao;

    @Test
    public void testInsert() throws Exception {
        CmsBtPriceLogModel model = new CmsBtPriceLogModel();
        model.setChannelId("001");
        model.setCartId(23);
        model.setProductId(3316458L);
        model.setCode("1118913912");
        model.setSku("5089415");
        model.setModifier("yjd-price-ts");
        model.setCreater("yjd-price-ts");

        CmsBtPriceLogModel_History history = new CmsBtPriceLogModel_History();

        history.setMsrpPrice(1123.12);
        history.setRetailPrice(1221.43);
        history.setSalePrice(1321.43);
        history.setClientMsrpPrice(1421.43);
        history.setClientRetailPrice(1521.43);
        history.setClientNetPrice(1021.43);
        history.setComment("子店->USJOI主店导入");
        history.setCreater("yjd-price-ts");
        history.setModifier("yjd-price-ts");
        Date ts = new Date();
        history.setCreated(ts);
        history.setModified(ts);
        model.getList().add(history);

        priceLogDao.insert(model);
    }


    @Test
    public void testInsert2() throws Exception {
        CmsBtPriceLogModel model = new CmsBtPriceLogModel();
        model.setChannelId("928");
        model.setCartId(23);
        model.setProductId(3316458L);
        model.setCode("1118913912-2");
        model.setSku("5089415");
        model.setModifier("yjd-price-ts");
        model.setCreater("yjd-price-ts");

        CmsBtPriceLogModel_History history = new CmsBtPriceLogModel_History();

        history.setMsrpPrice(1123.12);
        history.setRetailPrice(1221.43);
        history.setSalePrice(1321.43);
        history.setClientMsrpPrice(1421.43);
        history.setClientRetailPrice(1521.43);
        history.setClientNetPrice(1021.43);
        history.setComment("子店->USJOI主店导入");
        history.setCreater("yjd-price-ts");
        history.setModifier("yjd-price-ts");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        history.setCreated(ts);
        history.setModified(ts);
        model.getList().add(history);

        priceLogDao.insert(model);
    }

    @Test
    public void testSelectListBySkuOnCart() throws Exception {
        priceLogDao.selectListBySkuOnCart(null, "14009927", "23", "018");
    }

    @Test
    public void testSelectPageBySkuOnCart() throws Exception {
        List<CmsBtPriceLogFlatModel> flatModelList = priceLogDao.selectPageBySkuOnCart("", "14009927", "23", "018", 2, 10);
        Assert.assertNotNull(flatModelList);
        Assert.assertEquals(10, flatModelList.size());
        CmsBtPriceLogFlatModel model0 = flatModelList.get(0);
        Assert.assertTrue(model0.getMsrpPrice().doubleValue() == 177d);
    }

    @Test
    public void testSelectCountBySkuOnCart() throws Exception {
        int count = priceLogDao.selectCountBySkuOnCart("", "14009927", "23", "018");
        Assert.assertEquals(23, count);
    }

    @Test
    public void testSelectCountBySkuOnCart2() throws Exception {
        int count = priceLogDao.selectCountBySkuOnCart("14009927", "14009927", "23", "018");
        Assert.assertEquals(3, count);
    }

    @Test
    public void testSelectLastOneBySkuOnCart() throws Exception {
        CmsBtPriceLogFlatModel flatModel = priceLogDao.selectLastOneBySkuOnCart("12082570", 23, "018");
        System.out.println(flatModel.getSku());
        Assert.assertEquals(flatModel.getSku(), "12082570");
        Assert.assertTrue(flatModel.getMsrpPrice().doubleValue() == 8);
    }


    @Test
    public void testUpdateCmsBtPriceLogForMove() throws Exception {
        List<String> skus = new ArrayList<>();
        skus.add("12311842");
        skus.add("12311841");
        skus.add("12311843");
        int cnt = priceLogDao.updateCmsBtPriceLogForMove("018", "10920275-Pink Ice", skus, "10920275-Pink Ice-2", "yjd-mod1112");
        System.out.println("updated: " + cnt);
    }


    /**
     * 从mysql导入mongo
     */
    @Test
    public void transferPriceHistoryMysql2Mongo() throws Exception {
//        String[] channelIdArr = new String[]{"001", "010", "012", "015", "017", "018", "023", "024", "928", "929"};
        String[] channelIdArr = new String[]{"001", "007", "008", "010", "012", "017", "018", "024", "030", "033", "034", "928", "014"};
        List<Runnable> threads = new ArrayList<>();

        for (String channelId : channelIdArr) {
            threads.add(() -> transferPriceLogHistoryByChannel(channelId));
        }

        ExecutorService pool = Executors.newFixedThreadPool(channelIdArr.length);
        threads.forEach(pool::execute);
        pool.shutdown();
        // 等待子线程结束，再继续执行下面的代码
        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
//        transferPriceLogByChannel("000");
    }

    @Test
    public void transferPriceMysql2Mongo() throws Exception {
//        String[] channelIdArr = new String[]{"001", "010", "012", "015", "017", "018", "023", "024", "928", "929"};
        String[] channelIdArr = new String[]{"001", "007", "008", "010", "012", "017", "018", "024", "030", "033", "034", "928", "014"};
        List<Runnable> threads = new ArrayList<>();

        for (String channelId : channelIdArr) {
            threads.add(() -> transferPriceLogByChannel(channelId));
        }

        ExecutorService pool = Executors.newFixedThreadPool(channelIdArr.length);
        threads.forEach(pool::execute);
        pool.shutdown();
        // 等待子线程结束，再继续执行下面的代码
        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
//        transferPriceLogByChannel("000");
    }

    @Autowired
    private com.voyageone.service.dao.cms.CmsBtPriceLogDao cmsBtPriceLogDaoMysql;

    private void transferPriceLogByChannel(String channelId) {
        Map<String, Object> param = new HashMap<>();
        int pageSize = 10000;
        param.put("pageRowCount", pageSize);
        param.put("channelId", channelId);
        long cnt = 0L;
        while (true) {
            long start = System.currentTimeMillis();
            long startId = findTransferProgress(channelId);
            param.put("startId", startId);
            List<CmsBtPriceLogModel_Mysql> priceLogModelMysqls = cmsBtPriceLogDaoMysql.selectList4Transfer(param);
            if (priceLogModelMysqls == null || priceLogModelMysqls.size() == 0) {
                System.out.println("channel finished:" + channelId + "!");
                break;
            }
            long id = 0L;
            for (CmsBtPriceLogModel_Mysql logModelMysql : priceLogModelMysqls) {
                convertMysql2Mongo(logModelMysql);
                id = Long.valueOf(logModelMysql.getId());
            }
            saveTransferProgress(channelId, id);
            System.out.println("price log processed records: " + channelId + "->" + priceLogModelMysqls.size());
            cnt += priceLogModelMysqls.size();
            long end = System.currentTimeMillis();
            System.out.println("cost in this loop(ms):" + (end - start));
            System.out.println("channel total processed:" + channelId + "-->" + cnt);
        }
    }

    private void transferPriceLogHistoryByChannel(String channelId) {
        Map<String, Object> param = new HashMap<>();
        int pageSize = 10000;
        param.put("pageRowCount", pageSize);
        param.put("channelId", channelId);
        long cnt = 0L;
        while (true) {
            long start = System.currentTimeMillis();
            long startId = findTransferProgress(channelId);
            param.put("startId", startId);
            List<CmsBtPriceLogModel_Mysql> priceLogModelMysqls = cmsBtPriceLogDaoMysql.selectList4TransferHistory(param);
            if (priceLogModelMysqls == null || priceLogModelMysqls.size() == 0) {
                System.out.println("channel finished:" + channelId + "!");
                break;
            }
            long id = 0L;
            for (CmsBtPriceLogModel_Mysql logModelMysql : priceLogModelMysqls) {
                convertMysql2Mongo(logModelMysql);
                id = Long.valueOf(logModelMysql.getId());
            }
            saveTransferProgress(channelId, id);
            System.out.println("price log processed records: " + channelId + "->" + priceLogModelMysqls.size());
            cnt += priceLogModelMysqls.size();
            long end = System.currentTimeMillis();
            System.out.println("cost in this loop(ms):" + (end - start));
            System.out.println("channel total processed:" + channelId + "-->" + cnt);
        }
    }

    private void convertMysql2Mongo(CmsBtPriceLogModel_Mysql logModelMysql) {
        CmsBtPriceLogModel model = new CmsBtPriceLogModel();
        model.setChannelId(logModelMysql.getChannelId());
        model.setProductId(logModelMysql.getProductId());
        model.setCartId(logModelMysql.getCartId());
        model.setCode(logModelMysql.getCode());
        model.setSku(logModelMysql.getSku());
        model.setCreater(logModelMysql.getCreater());
        model.setModifier(logModelMysql.getModifier());
        model.setCreated(DateTimeUtil.format(logModelMysql.getCreated(), DateTimeUtil.DEFAULT_DATETIME_FORMAT));
        model.setModified(DateTimeUtil.format(logModelMysql.getModified(), DateTimeUtil.DEFAULT_DATETIME_FORMAT));

        CmsBtPriceLogModel_History history = new CmsBtPriceLogModel_History();
        history.setMsrpPrice(logModelMysql.getMsrpPrice());
        history.setRetailPrice(logModelMysql.getRetailPrice());
        history.setSalePrice(logModelMysql.getSalePrice());
        history.setClientMsrpPrice(logModelMysql.getClientMsrpPrice());
        history.setClientRetailPrice(logModelMysql.getClientRetailPrice());
        history.setClientNetPrice(logModelMysql.getClientNetPrice());
        history.setComment(logModelMysql.getComment());
        history.setCreater(logModelMysql.getCreater());
        history.setModifier(logModelMysql.getModifier());
        history.setCreated(logModelMysql.getCreated());
        history.setModified(logModelMysql.getModified());
        model.getList().add(history);

        priceLogDao.insert(model);
    }


    @Autowired
    private MongoTemplate mongoTemplate;

    private long findTransferProgress(String channelId) {
        long id = -1L;
        Query query = new Query(where("channelId").is(channelId));
        Map<String, Object> progressmap = mongoTemplate.findOne(query, Map.class, "cms_bt_price_log_progress");
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

    private void saveTransferProgress(String channelId, long id) {
        Query query = new Query(where("channelId").is(channelId));
        Update update = Update.update("id", id);
        mongoTemplate.upsert(query, update, "cms_bt_price_log_progress");
    }

    @Test
    public void testSaveTransferProgress() throws Exception {
        saveTransferProgress("010", 120L);
//        saveTransferProgress("010",120L);
    }
}