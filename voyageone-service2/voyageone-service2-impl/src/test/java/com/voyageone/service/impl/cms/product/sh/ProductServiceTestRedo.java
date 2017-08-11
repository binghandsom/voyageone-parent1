package com.voyageone.service.impl.cms.product.sh;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
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
 * Created by yangjindong on 2017/5/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2-sh.xml")
public class ProductServiceTestRedo {
    @Autowired
    ProductService productService;

    @Test
    public void testGetProductByObjectId() throws Exception {
        // s3 ({$natural:1}).skip(20000)
        long start = System.currentTimeMillis();
        CmsBtProductModel prod = productService.getProductByObjectId("928", "5913e45341a82a1c205f66bc");
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prod);
        System.out.println("getProductByObjectId cost:" + (end - start));
    }


    @Test
    public void testGetProductById() throws Exception {
        long start = System.currentTimeMillis();
        // s4 ({$natural:1}).skip(20000)
        CmsBtProductModel prod = productService.getProductById("928", 313419310L);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prod);
        System.out.println("getProductById cost:" + (end - start));
    }

    @Test
    public void testGetProductByNumIid() throws Exception {
        long start = System.currentTimeMillis();
        // s5 ({$natural:1}).skip(20000)
        List<CmsBtProductModel> prods = productService.getProductByNumIid("928", "020-332V103958", 33);
        long end = System.currentTimeMillis();
        System.out.println("getProductByNumIid cost:" + (end - start));
        Assert.assertTrue(prods.size() > 0);
    }

    @Test
    public void testGetProductByCode() throws Exception {
        long start = System.currentTimeMillis();
        // s6 ({$natural:1}).skip(20000)
        CmsBtProductModel prod = productService.getProductByCode("928", "138832");
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prod);
        System.out.println("getProductByCode cost:" + (end - start));
    }

    @Test
    public void testGetProductByCode2() throws Exception {
        long start = System.currentTimeMillis();
        // s7 ({$natural:1}).skip(20000)
        CmsBtProductModel prod = productService.getProductByCode("928", "017", "149868-s1");
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prod);
        System.out.println("getProductByCode cost:" + (end - start));
    }

    @Test
    public void testGetProductSingleSku() throws Exception {
        long start = System.currentTimeMillis();
        // s0 ({$natural:1}).skip(20000)
        CmsBtProductModel prod = productService.getProductSingleSku("928", "029-1-4663-061-s6", "029-1-4663-061");
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prod);
        System.out.println("getProductSingleSku cost:" + (end - start));
    }

    @Test
    public void testGetProductSingleSku2() throws Exception {
        long start = System.currentTimeMillis();
        // s1 ({$natural:1}).skip(20000)
        CmsBtProductModel prod = productService.getProductSingleSku("928", "017", "156083-s7", "156083");
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prod);
        System.out.println("getProductSingleSku2 cost:" + (end - start));
    }

    @Test
    public void testGetProductByOriginalCode() throws Exception {
        long start = System.currentTimeMillis();
        // s2 ({$natural:1}).skip(20000)
        List<CmsBtProductModel> prods = productService.getProductByOriginalCode("928", "107041");
        long end = System.currentTimeMillis();
        System.out.println("getProductByOriginalCode cost:" + (end - start));
        Assert.assertTrue(prods.size() > 0);
    }

    @Test
    public void testGetProductByOriginalCode2() throws Exception {
        long start = System.currentTimeMillis();
        // s3 ({$natural:1}).skip(30000)
        List<CmsBtProductModel> prods = productService.getProductByOriginalCode("928", "023", "028-ps3058694");
        long end = System.currentTimeMillis();
        System.out.println("getProductByOriginalCode2 cost:" + (end - start));
        Assert.assertTrue(prods.size() > 0);
    }

    @Test
    public void testGetProductBySkuCode() throws Exception {
        long start = System.currentTimeMillis();
        // s4 ({$natural:1}).skip(30000)
        List<CmsBtProductModel> prods = productService.getProductBySkuCode("928", "009-LP-10588-RG-11");
        long end = System.currentTimeMillis();
        System.out.println("getProductBySkuCode cost:" + (end - start));
        Assert.assertTrue(prods.size() > 0);
    }

    @Test
    public void testGetProductBySku() throws Exception {
        long start = System.currentTimeMillis();
        // s5 ({$natural:1}).skip(30000)
        CmsBtProductModel prod = productService.getProductBySku("928", "5893592");
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prod);
        System.out.println("getProductBySku cost:" + (end - start));
    }

    @Test
    public void testGetProductByCondition() throws Exception {
        JongoQuery jq = new JongoQuery();
        jq.setQuery("{'prodId':#,'platforms.P#.skus':{$exists:true}}");
        jq.setParameters(318661308L, 33);
        long start = System.currentTimeMillis();
        // s6 ({$natural:1}).skip(30000)
        CmsBtProductModel prod = productService.getProductByCondition("928", jq);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prod);
        System.out.println("getProductByCondition cost:" + (end - start));
    }

    @Test
    public void testGetProductByGroupId() throws Exception {
        long start = System.currentTimeMillis();
        // s7 ({$natural:1}).skip(40000) aaa
        List<CmsBtProductBean> pbs = productService.getProductByGroupId("928", 134287004L, false);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(pbs);
        System.out.println("getProductByGroupId cost:" + (end - start));
    }

    @Test
    public void testGetList() throws Exception {
        // s0 ({$natural:1}).skip(30000)
        JongoQuery jq = new JongoQuery();
        jq.setQuery("{\"common.fields.code\": #}");
        jq.setParameters("028-ps5827757-s2");
        long start = System.currentTimeMillis();
        List<CmsBtProductModel> prods = productService.getList("928", jq);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prods);
        System.out.println("getList cost:" + (end - start));
    }

    @Test
    public void testGetBeanList() throws Exception {
        // s1 ({$natural:1}).skip(30000)
        JongoQuery jq = new JongoQuery();
        jq.setQuery("{\"common.fields.code\": #}");
        jq.setParameters("028-ps6824130-s8");
        long start = System.currentTimeMillis();
        List<CmsBtProductBean> prods = productService.getBeanList("928", jq);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prods);
        System.out.println("getBeanList cost:" + (end - start));
    }

    @Test
    public void testGetListWithGroup() throws Exception {
        // s2 ({$natural:1}).skip(30000)
        JongoQuery jq = new JongoQuery();
        jq.setQuery("{\"common.fields.code\": #}");
        jq.setParameters("028-ps6840386-s2");
        long start = System.currentTimeMillis();
        List<CmsBtProductBean> prods = productService.getListWithGroup("928", 33, jq);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prods);
        System.out.println("testGetListWithGroup cost:" + (end - start));
    }

    @Test
    public void testCheckProductDataIsReady() throws Exception {
        // s3 ({$natural:1}).skip(40000)
        long start = System.currentTimeMillis();
        boolean res = productService.checkProductDataIsReady("928", 319413909L);
        long end = System.currentTimeMillis();
        Assert.assertTrue(res);
        System.out.println("checkProductDataIsReady cost:" + (end - start));
    }

    @Autowired
    CmsBtProductDao productDao;

    @Test
    public void testInsert() throws Exception {
        productDao.deleteWithQuery("{\"prodId\":103034547}", "928");
        CmsBtProductModel prod = productService.getProductById("928", 3034547L);
        prod.set_id(null);
        // 20W以下的随机数
        long prodId = Math.round(Math.random() * 200000L);
        prod.setProdId(prodId);
        long start = System.currentTimeMillis();
        productService.insert(prod);
        long end = System.currentTimeMillis();
        System.out.println("insert cost:" + (end - start));

    }

    @Test
    public void testInsert2() throws Exception {
        productDao.deleteWithQuery("{\"prodId\":103034547}", "928");
        CmsBtProductModel prod = productService.getProductById("928", 3034547L);
        long start = System.currentTimeMillis();
        prod.set_id(null);
        // 20W以下的随机数
        long prodId = Math.round(Math.random() * 200000L);
        prod.setProdId(prodId);
        List<CmsBtProductModel> prods = new ArrayList<>();
        prods.add(prod);
        productService.insert(prods);
        long end = System.currentTimeMillis();
        System.out.println("insert2 cost:" + (end - start));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        // s4 ({$natural:1}).skip(40000)
        Map<String, Object> rsMap = new HashMap<>();
        rsMap.put("modifier", "sh-test");
        rsMap.put("modified", DateTimeUtil.getNowTimeStamp());

        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("$set", rsMap);

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", 319202010L);
        long start = System.currentTimeMillis();
        productService.updateProduct("928", queryMap, updateMap);
        long end = System.currentTimeMillis();
        System.out.println("updateProduct cost:" + (end - start));
    }

    @Test
    public void testUpdateFirstProduct() throws Exception {
        // s5 ({$natural:1}).skip(40000)
        JongoUpdate ju = new JongoUpdate();
        ju.setQuery("{\"common.fields.code\": #}");
        ju.setQueryParameters("1124575845-s9");
        ju.setUpdate("{$set: {\"platforms.P#.lock\": #}}");
        ju.setUpdateParameters(33, "1");

        long start = System.currentTimeMillis();
        productService.updateFirstProduct(ju, "928");
        long end = System.currentTimeMillis();
        System.out.println("updateFirstProduct cost:" + (end - start));
    }

    @Test
    public void testUpdateTags() throws Exception {
        // s6 ({$natural:1}).skip(40000)
        long start = System.currentTimeMillis();
        List<String> tags = new ArrayList<>();
        tags.add("防水");
        tags.add("男士");
        productService.updateTags("928", 319440203L, tags, "sh-test-tags");
        long end = System.currentTimeMillis();
        System.out.println("updateTags cost:" + (end - start));
    }

    @Test
    public void testRemoveTagByCodes() throws Exception {
        // s7 ({$natural:1}).skip(50000)
        List<String> codes = new ArrayList<>();
        codes.add("1147597053-s7");
        long start = System.currentTimeMillis();
        productService.removeTagByCodes("928", codes, 9806);
        long end = System.currentTimeMillis();
        System.out.println("updateTags cost:" + (end - start));
    }
}
