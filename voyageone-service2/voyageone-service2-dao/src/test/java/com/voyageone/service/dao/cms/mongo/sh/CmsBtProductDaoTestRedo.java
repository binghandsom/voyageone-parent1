package com.voyageone.service.dao.cms.mongo.sh;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_UsPlatform_Cart;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangjindong on 2017/5/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-sh.xml")
public class CmsBtProductDaoTestRedo {

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Test
    public void testSelectProductByIds() throws Exception {

        List<Long> ids = new ArrayList<>();
        ids.add(3048950L); // in shard s5
        ids.add(4691810L); // in shard s7
        long start = System.currentTimeMillis();
        List<CmsBtProductModel> products = cmsBtProductDao.selectProductByIds(ids, "928");
        long end = System.currentTimeMillis();
        System.out.println("selectProductByIds cost:" + (end - start));
        Assert.assertEquals(products.size(), 2);
        Assert.assertEquals(products.get(0).getProdId().longValue(), 3048950L);

    }

    @Test
    public void testSelectProductByCodes() throws Exception {
        List<String> codes = new ArrayList<>();
        codes.add("14906076-17Inch-SterlingSilver-White-s5"); // s7
        codes.add("15365591-Silver-Gold-Brass-Two-Tone-Two-Tone-s6"); // s3
        long start = System.currentTimeMillis();
        List<CmsBtProductModel> products = cmsBtProductDao.selectProductByCodes(codes, "928");
        Assert.assertEquals(products.size(), 2);
        long end = System.currentTimeMillis();
        System.out.println("selectProductByCodes cost:" + (end - start));
    }

    @Test
    public void testSelectByCode() throws Exception {
        long start = System.currentTimeMillis();
        CmsBtProductModel product = cmsBtProductDao.selectByCode("15257165-Silver-White-SterlingSilver-White-Holiday-OtherThemes-s5", "928"); // s4
        long end = System.currentTimeMillis();
        Assert.assertEquals(product.getCommon().getFields().getCode(), "15257165-Silver-White-SterlingSilver-White-Holiday-OtherThemes-s5");
        System.out.println("selectByCode cost:" + (end - start));

    }

    //    @Test
    public void testSelectBySkuIgnoreCase() throws Exception {
        long start = System.currentTimeMillis();
        CmsBtProductModel product = cmsBtProductDao.selectBySkuIgnoreCase("not-a-sku", "928");
        long end = System.currentTimeMillis();
        System.out.println("selectBySkuIgnoreCase cost:" + (end - start));
    }

    @Test
    public void testSelectBean() throws Exception {
        // TODO
        JongoQuery jq = new JongoQuery();
        List<String> codes = new ArrayList<>();
        codes.add("120809-s10"); // s4 skip 20000
        codes.add("149868-s1"); // s7 skip 20000
        jq.setQuery("{'common.fields.code':{$in:#}}");
        jq.setParameters(codes);

        long start = System.currentTimeMillis();
        List<CmsBtProductBean> prods = cmsBtProductDao.selectBean(jq, "928");
        Assert.assertEquals(prods.size(), 2);
        long end = System.currentTimeMillis();
        System.out.println("testSelectBean cost:" + (end - start));
    }

    @Test
    public void testBulkUpdateWithMap() throws Exception {
        List<BulkUpdateModel> updList = new ArrayList<>();
        BulkUpdateModel upd1 = new BulkUpdateModel();
        HashMap<String, Object> queryMap = new HashMap<>();
        HashMap<String, Object> updMap = new HashMap<>();
        queryMap.put("common.fields.code", "138832");  // s6 skip 20000
        updMap.put("platforms.P32.modifier", "sh_test_inner1-redo");


        upd1.setQueryMap(queryMap);
        upd1.setUpdateMap(updMap);

        HashMap<String, Object> query2Map = new HashMap<>();
        HashMap<String, Object> upd2Map = new HashMap<>();
        query2Map.put("common.fields.code", "1285653-002-s8");  // s4 desc skip 10000
        upd2Map.put("platforms.P32.modifier", "sh_test_inner2-redo");

        BulkUpdateModel upd2 = new BulkUpdateModel();
        upd2.setQueryMap(query2Map);
        upd2.setUpdateMap(upd2Map);
        updList.add(upd1);
        updList.add(upd2);
        long start = System.currentTimeMillis();
        cmsBtProductDao.bulkUpdateWithMap("928", updList, "sh_test", "$set");
        long end = System.currentTimeMillis();
        System.out.println("bulkUpdateWithMap cost:" + (end - start));
    }

    @Test
    public void testCheckProductDataIsReady() throws Exception {
        long start = System.currentTimeMillis();
        boolean ready = cmsBtProductDao.checkProductDataIsReady("928", 464163002L); // s4 $natural -1 skipp 10000
        System.out.println("ready:" + ready);
        long end = System.currentTimeMillis();
        System.out.println("checkProductDataIsReady cost:" + (end - start));
    }

    @Test
    public void testDeleteSellerCat() throws Exception {
        CmsBtSellerCatModel cat = new CmsBtSellerCatModel();
        cat.setCatId("5109999"); // TODO 测试前先追加
        long start = System.currentTimeMillis();
        cmsBtProductDao.deleteSellerCat("928", cat, 32, "test-redo");
        long end = System.currentTimeMillis();
        System.out.println("deleteSellerCat cost:" + (end - start));
    }


    @Test
    public void testUpdateSellerCat() throws Exception {
        CmsBtSellerCatModel catModel = new CmsBtSellerCatModel();
        catModel.setChannelId("928");
        catModel.setCartId(32);
        catModel.setCatId("507");  // 大约216条
        catModel.setCatName("已更新111。。");

        ArrayList<CmsBtSellerCatModel> list = new ArrayList<>();
        list.add(catModel);
        long start = System.currentTimeMillis();
        cmsBtProductDao.updateSellerCat("928", list, 32, "sh_test");
        long end = System.currentTimeMillis();
        System.out.println("updateSellerCat cost:" + (end - start));
    }

    @Test
    public void testSelectListCodeBySellerCat() throws Exception {
        long start = System.currentTimeMillis();
        // {"platforms.P32.sellerCats":{$exists:true}}).skip(30000)
        // {"platforms.P32.sellerCats":{$exists:true}}).skip(40000)
        List<String> codes = cmsBtProductDao.selectListCodeBySellerCat("928", 32, "497", null, null, null);
        System.out.println("size:" + codes.size());
        long end = System.currentTimeMillis();
        System.out.println("selectListCodeBySellerCat cost:" + (end - start));
    }

    @Test
    public void testRemoveTagByCodes() throws Exception {
        List<String> codes = new ArrayList<>();
        codes.add("1154772375-s3");
        long start = System.currentTimeMillis();
        // s3 tags.0 exists skipp 10000
        cmsBtProductDao.removeTagByCodes("928", codes, 11123);
        long end = System.currentTimeMillis();
        System.out.println("removeTagByCodes cost:" + (end - start));
    }

    @Test
    public void testUpdateUsPlatforms() throws Exception {
        long start = System.currentTimeMillis();
        Map<String, CmsBtProductModel_UsPlatform_Cart> usPlatforms = new HashMap<>();
        CmsBtProductModel_UsPlatform_Cart p1Cart = new CmsBtProductModel_UsPlatform_Cart();
        p1Cart.setCartId(1);
        p1Cart.setStatus("ssss1111ss");
        usPlatforms.put("P1", p1Cart);
        // s4 $natural -1 skip 1000
        cmsBtProductDao.updateUsPlatforms("928", "1285653-002-s8", usPlatforms, "CmsSynInventoryToCmsJob");
        long end = System.currentTimeMillis();
        System.out.println("updateUsPlatforms cost:" + (end - start));
    }

}