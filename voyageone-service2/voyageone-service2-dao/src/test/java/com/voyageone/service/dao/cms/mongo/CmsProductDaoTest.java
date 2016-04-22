package com.voyageone.service.dao.cms.mongo;


import com.mongodb.*;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
//import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.UnknownHostException;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsProductDaoTest {
    @Autowired
    CmsBtProductDao cmsBtProductDao;

    @Test
    public void testSelectProductById() throws Exception {
        long start = System.currentTimeMillis();
        CmsBtProductModel model = cmsBtProductDao.selectById("565d97127b69bf04fdd12450", "100");
        System.out.println("time total:=" + (System.currentTimeMillis() - start));
        System.out.println(model.toString());
    }

//    @Test
//    public void testSelectProductByCode() throws Exception {
//        long start = System.currentTimeMillis();
//        CmsBtProductModel model = cmsBtProductDao.selectProductByCode("010", "51A0HC13E1-00LCNB0");
//        System.out.println("time total:=" + (System.currentTimeMillis() - start));
//        System.out.println(model.toString());
//    }

//    @Test
//    public void testSelectProductLikeCatIdPath() throws Exception {
//        long start = System.currentTimeMillis();
//        List<CmsBtProductModel> modelList = cmsBtProductDao.selectLeftLikeCatIdPath("100", "-100-10000-17");
//        System.out.println("time total:=" + (System.currentTimeMillis() - start) + "; size:=" + modelList.size());
//        for (CmsBtProductModel model : modelList) {
//            //System.out.println(model.toString());
//        }
//    }

    @Test
    public void selectAllReturnCursor() {
        long start = System.currentTimeMillis();
        Iterator<CmsBtProductModel> iterator = cmsBtProductDao.selectCursorAll("100");
        System.out.println("time total:=" + (System.currentTimeMillis() - start));
        int count = 0;
        while(iterator.hasNext()) {
            CmsBtProductModel model = iterator.next();
            System.out.println(model.toString());
            count++;
            if (count>10) {
                break;
            }
        }
    }

    @Test
    public void testUpsertTrue() throws UnknownHostException {
        DBCollection coll = cmsBtProductDao.getDBCollection("001");

        BasicDBObject o1 = new BasicDBObject();
        o1.append("$set", new BasicDBObject("name", "Innova1"));

        BasicDBObject query = new BasicDBObject().append("catId", "959");

        WriteResult c1 = coll.update(query, o1, true, false);
        DBCursor carcursor = coll.find();
        try {
            while (carcursor.hasNext()) {
                System.out.println(carcursor.next());
            }
        } finally {
            carcursor.close();
        }
    }

    @Test
    public void testUpsertBulkUnorderedDocsForUpdate() throws UnknownHostException {
        DBCollection coll = cmsBtProductDao.getDBCollection("001");
        // intialize and create a unordered bulk
        BulkWriteOperation b1 = coll.initializeUnorderedBulkOperation();

        BasicDBObject o1 = new BasicDBObject();

        o1.append("$setOnInsert", new BasicDBObject("name", "innova").append("speed", 54));
        o1.append("$set", new BasicDBObject("cno", "H456"));

        BasicDBObject query = new BasicDBObject().append("catId", "959");

        b1.find(query).upsert().update(o1);

        b1.execute();

        DBCursor c1 = coll.find();

        System.out.println("---------------------------------");

        try {
            while (c1.hasNext()) {
                System.out.println(c1.next());
            }
        } finally {
            c1.close();
        }

    }

    @Test
    public void upsertBulkUnordereDocsForUpdateOne() throws UnknownHostException {
        DBCollection coll = cmsBtProductDao.getDBCollection("001");

        // intialize and create a unordered bulk
        BulkWriteOperation b1 = coll.initializeUnorderedBulkOperation();

        BasicDBObject o1 = new BasicDBObject();

        o1.append(
                "$set",new BasicDBObject("name", "Xylo").append("speed", 67).append("cno", "H654"));

        BasicDBObject query = new BasicDBObject().append("catId", "959");
        b1.find(query).upsert().updateOne(o1);

        b1.execute();

        DBCursor c1 = coll.find();

        System.out.println("---------------------------------");

        try {
            while (c1.hasNext()) {
                System.out.println(c1.next());
            }
        } finally {
            c1.close();
        }

    }

    @Test
    public void upsertBulkForUpdateOneWithOperators() throws UnknownHostException {
        DBCollection coll = cmsBtProductDao.getDBCollection("001");
        // intialize and create a unordered bulk
        BulkWriteOperation b1 = coll.initializeOrderedBulkOperation();

        BasicDBObject o1 = new BasicDBObject();

        // insert if document not found and set the fields with updated value
        o1.append("$setOnInsert", new BasicDBObject("cno", "H123"));
        o1.append("$set", new BasicDBObject("speed", "63"));

        BasicDBObject query = new BasicDBObject().append("catId", "959");
        b1.find(query).upsert().updateOne(o1);

        b1.execute();

        DBCursor c1 = coll.find();

        System.out.println("---------------------------------");

        try {
            while (c1.hasNext()) {
                System.out.println(c1.next());
            }
        } finally {
            c1.close();
        }
    }

    @Test
    public void upsertBulkForUpdateOne() throws UnknownHostException {
        CmsBtProductModel_Sku skuModel = new CmsBtProductModel_Sku();
        skuModel.setSkuCode("100001-2");
        skuModel.setBarcode("12341111");
        skuModel.setPriceMsrp((double)187);
        skuModel.setPriceRetail((double)243);
        skuModel.setPriceSale((double)358);


        DBCollection coll = cmsBtProductDao.getDBCollection("100");
        // intialize and create a unordered bulk
        BulkWriteOperation b1 = coll.initializeOrderedBulkOperation();

        BasicDBObject updateObj = new BasicDBObject();
        // insert if document not found and set the fields with updated value
        updateObj.append("$set", skuModel.toUpdateBasicDBObject("skus.$."));

        BasicDBObject query = new BasicDBObject().append("skus.sku", skuModel.getSkuCode());

        b1.find(query).upsert().update(updateObj);

        b1.execute();

        DBCursor c1 = coll.find();

        System.out.println("---------------------------------");

        try {
            while (c1.hasNext()) {
                System.out.println(c1.next());
            }
        } finally {
            c1.close();
        }
    }

    @Test
    public void upsertBulkUnorderedDocsForReplaceOne() throws UnknownHostException {
        DBCollection coll = cmsBtProductDao.getDBCollection("001");

        // intialize and create a unordered bulk
        BulkWriteOperation b1 = coll.initializeOrderedBulkOperation();

        // insert query
        BasicDBObject o1 = new BasicDBObject("name", "Qualis").append("speed", null).append("color", "Palebrown");

        BasicDBObject query = new BasicDBObject().append("catId", "959");
        b1.find(query).upsert().replaceOne(o1);

        b1.execute();

        DBCursor c1 = coll.find();

        System.out.println("---------------------------------");

        try {
            while (c1.hasNext()) {
                System.out.println(c1.next());
            }
        } finally {
            c1.close();
        }

    }

//    @Test
//    public void testUpdateWithGroupPlatform() {
//        CmsBtProductModel_Group_Platform platformMode = new CmsBtProductModel_Group_Platform();
//        platformMode.setGroupId((long)45);
//        platformMode.setCartId(21);
//        platformMode.setNumIId("1000702");
//        platformMode.setIsMain(false);
//        platformMode.setDisplayOrder(27);
//        platformMode.setPublishTime("2015-10-12 16:19:00");
//        platformMode.setInstockTime("2015-10-18 16:19:00");
////        platformMode.setStatus("InStock1");
////        platformMode.setPublishStatus(" 等待上新1");
////        platformMode.setComment("1");
////        platformMode.setInventory(25);
//        cmsBtProductDao.updateWithPlatform("001", "2000702", platformMode);
//    }

    @Test
    public void testUpdateWithSku() {
        CmsBtProductModel_Sku skuModel = new CmsBtProductModel_Sku();

        skuModel.setSkuCode("100001-2");
        skuModel.setBarcode("12311111");
        skuModel.setPriceMsrp((double)387);
        skuModel.setPriceRetail((double)543);
        skuModel.setPriceSale((double)858);
        //skuModel.setPlatformArrCode("1110");

        System.out.println(JsonUtil.bean2Json(skuModel));
        cmsBtProductDao.updateWithSku("001", "001", skuModel);
    }

    @Test
    public void testUpdate10W() {
        DBCollection coll = cmsBtProductDao.getDBCollection("100");

        List<CmsBtProductModel> lst = new ArrayList<>();
        int index = 0;

        CmsBtProductModel_Sku skuModel = new CmsBtProductModel_Sku();
        skuModel.setPriceMsrp((double)187);
        skuModel.setPriceRetail((double)243);
        skuModel.setPriceSale((double)358);

        BulkWriteOperation b1 = coll.initializeUnorderedBulkOperation();

        long start = System.currentTimeMillis();
        for(int i=1; i<=100000; i++) {

            String sku = String.valueOf(100000 + i) + "-2";
            skuModel.setSkuCode(sku);

            BasicDBObject updateObj = new BasicDBObject();
            updateObj.append("$set", skuModel.toUpdateBasicDBObject("skus.$."));

            BasicDBObject query = new BasicDBObject().append("skus.sku", skuModel.getSkuCode());

            b1.find(query).upsert().update(updateObj);

            if (i%1000 == 0) {
                System.out.println("current count:=" + i);
                b1.execute();
                b1 = coll.initializeUnorderedBulkOperation();
            }
            index++;
        }
        if (lst.size()>0) {
            b1.execute();
        }
        long total = System.currentTimeMillis()-start;
        System.out.println("total count:=" + index + "; totalTime:=" + total);
    }

    @Test
    public void testDelete10W() {
        long start = System.currentTimeMillis();
        String commandStr = "{delete:\"cms_bt_product_c100\", deletes:[{ q: { }, limit: 0 }]}";
        cmsBtProductDao.executeCommand(commandStr);
        long total = System.currentTimeMillis()-start;
        System.out.println("total count:=10W; totalTime:=" + total);
    }

    @Test
    public void testDeleteAll() {
        long start = System.currentTimeMillis();
        cmsBtProductDao.deleteAll("001");
        long total = System.currentTimeMillis()-start;
        System.out.println("total count:=10W; totalTime:=" + total);
    }

    @Test
    public void testBathUpdateWithField() {
        long start = System.currentTimeMillis();

        CmsBtProductModel_Field field = new CmsBtProductModel_Field();
        field.setLock("1");

        List<String> codeList = new ArrayList<>();
        codeList.add("100001");
        codeList.add("100002");

//        cmsBtProductDao.bathUpdateWithField("001", codeList, field, "23232");

        long total = System.currentTimeMillis()-start;
        System.out.println("total count:=10W; totalTime:=" + total);
    }

    @Test
    public void testBathUpdateWithFields() {
        long start = System.currentTimeMillis();


        Map<String, CmsBtProductModel_Field> codeFieldMap = new LinkedHashMap<>();
        CmsBtProductModel_Field field = new CmsBtProductModel_Field();
        field.setLock("1");
        codeFieldMap.put("100001", field);
        field = new CmsBtProductModel_Field();
        field.setLock("1");
        field.setColor("BBB");
        codeFieldMap.put("100002", field);

//        cmsBtProductDao.bathUpdateWithFields("001", codeFieldMap, "23232");

        long total = System.currentTimeMillis()-start;
        System.out.println("total count:=10W; totalTime:=" + total);
    }

}