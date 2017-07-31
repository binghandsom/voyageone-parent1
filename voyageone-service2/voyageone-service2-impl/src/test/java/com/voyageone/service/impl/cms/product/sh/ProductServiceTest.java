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
 * Created by yangjindong on 2017/5/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2-sh.xml")
public class ProductServiceTest {
    @Autowired
    ProductService productService;

    @Test
    public void testGetProductByObjectId() throws Exception {
        CmsBtProductModel prodOld = productService.getProductById("928", 3034547L);
        long start = System.currentTimeMillis();
        CmsBtProductModel prod = productService.getProductByObjectId("928", prodOld.get_id());
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prod);
        System.out.println("getProductByObjectId cost:" + (end - start));
    }


    @Test
    public void testGetProductById() throws Exception {
        long start = System.currentTimeMillis();
        CmsBtProductModel prod = productService.getProductById("928", 3034547L);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prod);
        System.out.println("getProductById cost:" + (end - start));
    }

    @Test
    public void testGetProductByNumIid() throws Exception {
        long start = System.currentTimeMillis();
        List<CmsBtProductModel> prods = productService.getProductByNumIid("928", "806579_001", 33);
        long end = System.currentTimeMillis();
        System.out.println("getProductByNumIid cost:" + (end - start));
        Assert.assertTrue(prods.size() > 0);
    }

    @Test
    public void testGetProductByCode() throws Exception {
        long start = System.currentTimeMillis();
        CmsBtProductModel prod = productService.getProductByCode("928", "806579_001");
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prod);
        System.out.println("getProductByCode cost:" + (end - start));
    }

    @Test
    public void testGetProductByCode2() throws Exception {
        long start = System.currentTimeMillis();
        CmsBtProductModel prod = productService.getProductByCode("928", "023", "806579_001");
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prod);
        System.out.println("getProductByCode cost:" + (end - start));
    }

    @Test
    public void testGetProductSingleSku() throws Exception {
        long start = System.currentTimeMillis();
        CmsBtProductModel prod = productService.getProductSingleSku("928", "806579_001", "806579_001");
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prod);
        System.out.println("getProductSingleSku cost:" + (end - start));
    }

    @Test
    public void testGetProductSingleSku2() throws Exception {
        long start = System.currentTimeMillis();
        CmsBtProductModel prod = productService.getProductSingleSku("928", "023", "806579_001", "806579_001");
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prod);
        System.out.println("getProductSingleSku2 cost:" + (end - start));
    }

    @Test
    public void testGetProductByOriginalCode() throws Exception {
        long start = System.currentTimeMillis();
        List<CmsBtProductModel> prods = productService.getProductByOriginalCode("928", "806579_001");
        long end = System.currentTimeMillis();
        System.out.println("getProductByOriginalCode cost:" + (end - start));
        Assert.assertTrue(prods.size() > 0);
    }

    @Test
    public void testGetProductByOriginalCode2() throws Exception {
        long start = System.currentTimeMillis();
        List<CmsBtProductModel> prods = productService.getProductByOriginalCode("928", "023", "806579_001");
        long end = System.currentTimeMillis();
        System.out.println("getProductByOriginalCode2 cost:" + (end - start));
        Assert.assertTrue(prods.size() > 0);
    }

    @Test
    public void testGetProductBySkuCode() throws Exception {
        long start = System.currentTimeMillis();
        List<CmsBtProductModel> prods = productService.getProductBySkuCode("928", "023-806579_001-5.5");
        long end = System.currentTimeMillis();
        System.out.println("getProductBySkuCode cost:" + (end - start));
        Assert.assertTrue(prods.size() > 0);
    }

    @Test
    public void testGetProductBySku() throws Exception {
        long start = System.currentTimeMillis();
        CmsBtProductModel prod = productService.getProductBySku("928", "023-806579_001-5.5");
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prod);
        System.out.println("getProductBySku cost:" + (end - start));
    }

    @Test
    public void testGetProductByCondition() throws Exception {
        JongoQuery jq = new JongoQuery();
        jq.setQuery("{'prodId':#,'platforms.P#.skus':{$exists:true}}");
        jq.setParameters(3034547L, 33);
        long start = System.currentTimeMillis();
        CmsBtProductModel prod = productService.getProductByCondition("928", jq);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prod);
        System.out.println("getProductByCondition cost:" + (end - start));
    }

    @Test
    public void testGetProductByGroupId() throws Exception {
        long start = System.currentTimeMillis();
        List<CmsBtProductBean> pbs = productService.getProductByGroupId("928", 864370L, false);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(pbs);
        System.out.println("getProductByGroupId cost:" + (end - start));
    }

    @Test
    public void testGetList() throws Exception {
        JongoQuery jq = new JongoQuery();
        jq.setQuery("{\"common.fields.code\": #}");
        jq.setParameters("806579_001");
        long start = System.currentTimeMillis();
        List<CmsBtProductModel> prods = productService.getList("928", jq);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prods);
        System.out.println("getList cost:" + (end - start));
    }

    @Test
    public void testGetBeanList() throws Exception {
        JongoQuery jq = new JongoQuery();
        jq.setQuery("{\"common.fields.code\": #}");
        jq.setParameters("806579_001");
        long start = System.currentTimeMillis();
        List<CmsBtProductBean> prods = productService.getBeanList("928", jq);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prods);
        System.out.println("getBeanList cost:" + (end - start));
    }

    @Test
    public void testGetListWithGroup() throws Exception {
        JongoQuery jq = new JongoQuery();
        jq.setQuery("{\"common.fields.code\": #}");
        jq.setParameters("806579_001");
        long start = System.currentTimeMillis();
        List<CmsBtProductBean> prods = productService.getListWithGroup("928", 33, jq);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(prods);
        System.out.println("testGetListWithGroup cost:" + (end - start));
    }

    @Test
    public void testCheckProductDataIsReady() throws Exception {
        long start = System.currentTimeMillis();
        boolean res = productService.checkProductDataIsReady("928", 3034547L);
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
        long start = System.currentTimeMillis();
        prod.set_id(null);
        prod.setProdId(103034547L);
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
        prod.setProdId(103034547L);
        List<CmsBtProductModel> prods = new ArrayList<>();
        prods.add(prod);
        productService.insert(prods);
        long end = System.currentTimeMillis();
        System.out.println("insert2 cost:" + (end - start));
    }

    // @Test 不用测
    public void testCreateProduct() throws Exception {
        productDao.deleteWithQuery("{\"prodId\":103034547}", "928");
        CmsBtProductModel prod = productService.getProductById("928", 3034547L);
        long start = System.currentTimeMillis();
        prod.set_id(null);
        prod.setProdId(103034547L);
        String code = prod.getCommon().getFields().getCode();
        prod.getCommon().getFields().setCode(code + "-sh");
        productService.createProduct("928", prod, "sh-test");
        long end = System.currentTimeMillis();
        System.out.println("createProduct cost:" + (end - start));

    }


    @Test
    public void testUpdateProduct() throws Exception {
        Map<String, Object> rsMap = new HashMap<>();
        rsMap.put("modifier", "sh-test");
        rsMap.put("modified", DateTimeUtil.getNowTimeStamp());

        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("$set", rsMap);

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", 3034547L);

        long start = System.currentTimeMillis();
        productService.updateProduct("928", queryMap, updateMap);
        long end = System.currentTimeMillis();
        System.out.println("updateProduct cost:" + (end - start));
    }

    @Test
    public void testUpdateFirstProduct() throws Exception {
        JongoUpdate ju = new JongoUpdate();
        ju.setQuery("{\"common.fields.code\": #}");
        ju.setQueryParameters("806579_001");
        ju.setUpdate("{$set: {\"platforms.P#.lock\": #}}");
        ju.setUpdateParameters(33, "1");

        long start = System.currentTimeMillis();
        productService.updateFirstProduct(ju, "928");
        long end = System.currentTimeMillis();
        System.out.println("updateFirstProduct cost:" + (end - start));
    }

    @Test
    public void testUpdateTags() throws Exception {
        long start = System.currentTimeMillis();
        List<String> tags = new ArrayList<>();
        tags.add("防水");
        tags.add("男士");
        productService.updateTags("928", 3034547L, tags, "sh-test-tags");
        long end = System.currentTimeMillis();
        System.out.println("updateTags cost:" + (end - start));
    }

    @Test
    public void testRemoveTagByCodes() throws Exception {
        List<String> codes = new ArrayList<>();
        codes.add("022-GU7262TO153");
        long start = System.currentTimeMillis();
        productService.removeTagByCodes("928", codes, 9806);
        long end = System.currentTimeMillis();
        System.out.println("updateTags cost:" + (end - start));
    }
}
