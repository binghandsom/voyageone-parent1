package com.voyageone.service.impl.cms.product.sh;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
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
public class ProductGroupServiceTestRedo {
    @Autowired
    private ProductGroupService service;

    @Test
    public void testGetList() throws Exception {
        // s0 ({$natural:1}).skip(500000)
        JongoQuery jq = new JongoQuery();
        String query = "cartId:" + 28;
        query = query + ",productCodes:" + "\"1148233443\"";
        jq.setQuery("{" + query + "}");
        long start = System.currentTimeMillis();
        List<CmsBtProductGroupModel> grpList = service.getList("928", jq);
        long end = System.currentTimeMillis();
        System.out.println("getList cost:" + (end - start));
        Assert.assertTrue(grpList.size() > 0);
    }

    @Test
    public void testGetProductGroupByGroupId() throws Exception {
        // s1 ({$natural:1}).skip(500000)
        long start = System.currentTimeMillis();
        CmsBtProductGroupModel grp = service.getProductGroupByGroupId("928", 4041482L);
        long end = System.currentTimeMillis();
        System.out.println("getProductGroupByGroupId cost:" + (end - start));
        Assert.assertNotNull(grp);
        Assert.assertEquals(grp.getGroupId().longValue(), 4041482L);
    }


    @Test
    public void testGetProductGroupByQuery() throws Exception {
        // s2 ({$natural:1}).skip(500000)
        long start = System.currentTimeMillis();
        String q = "{'cartId':" + 29 + ",'productCodes':\"1128837328\"}";
        CmsBtProductGroupModel grp = service.getProductGroupByQuery("928", q);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(grp);
        System.out.println("testGetProductGroupByQuery cost:" + (end - start));
    }

    @Test
    public void testGetProductGroupByQuery2() throws Exception {
        // s3 ({$natural:1}).skip(500000)
        long start = System.currentTimeMillis();
        JongoQuery jq = new JongoQuery();
        jq.setQuery("{\"productCodes\": #, \"cartId\": #}");
        jq.setParameters("1116513293", 27);
        CmsBtProductGroupModel grp = service.getProductGroupByQuery("928", jq);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(grp);
        System.out.println("testGetProductGroupByQuery2 cost:" + (end - start));
    }

    @Test
    public void testSelectProductGroupByCode() throws Exception {
        // s4 ({$natural:1}).skip(500000)
        long start = System.currentTimeMillis();
        CmsBtProductGroupModel grp = service.selectProductGroupByCode("928", "1157773284", 32);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(grp);
        System.out.println("selectProductGroupByCode cost:" + (end - start));
    }

    @Test
    public void testSelectProductGroupListByCode() throws Exception {
        // s5 ({$natural:1}).skip(500000)
        long start = System.currentTimeMillis();
        List<CmsBtProductGroupModel> grpList = service.selectProductGroupListByCode("928", "1138385018");
        long end = System.currentTimeMillis();
        Assert.assertNotNull(grpList);
        Assert.assertTrue(grpList.size() > 0);
        System.out.println("selectProductGroupListByCode cost:" + (end - start));
    }

    @Test
    public void testSelectMainProductGroupByCode() throws Exception {
        // s6 ({$natural:1}).skip(500000)
        long start = System.currentTimeMillis();
        CmsBtProductGroupModel grp = service.selectMainProductGroupByCode("928", "1144535317", 28);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(grp);
        System.out.println("selectMainProductGroupByCode cost:" + (end - start));
    }

    @Test
    public void testSelectProductGroupByNumIId() throws Exception {
        // s7 ({$natural:1}).skip(500000)
        long start = System.currentTimeMillis();
        CmsBtProductGroupModel grp = service.selectProductGroupByNumIId("928", 28, "sss99092211000000");
        long end = System.currentTimeMillis();
        Assert.assertNotNull(grp);
        System.out.println("selectProductGroupByNumIId cost:" + (end - start));
    }

    @Test
    public void testUpdate() throws Exception {
        // s0 ({$natural:1}).skip(1,000,000)
        long start = System.currentTimeMillis();
        // get a productgroup from s1
        CmsBtProductGroupModel grp = service.getProductGroupByGroupId("928", 4041482L);
        grp.setModifier(grp.getModifier() + "-sh-test123");
        service.update(grp);
        long end = System.currentTimeMillis();
        System.out.println("update cost:" + (end - start));
    }

    @Test
    public void testInsert() throws Exception {
        // get a productgroup from s1
        CmsBtProductGroupModel grp = service.getProductGroupByGroupId("928", 4041482L);
        long start = System.currentTimeMillis();
        grp.set_id(null);
        long grpId = Math.round(Math.random() * 200000L);
        grp.setGroupId(grpId);
        service.insert(grp);
        long end = System.currentTimeMillis();
        System.out.println("insert cost:" + (end - start));
    }

    @Test
    public void testUpdateMainProduct() throws Exception {
        // s2 ({$natural:1}).skip(1,000,000)
        long start = System.currentTimeMillis();
        service.updateMainProduct("928", "1157159554-sh-upd-test", 189510901L, "sh-test1234");
        long end = System.currentTimeMillis();
        System.out.println("updateMainProduct cost:" + (end - start));
    }

    @Test
    public void testSelectProductGroupByModelCodeAndCartId() throws Exception {
        // s3 ({$natural:1}).skip(1,000,000)
        long start = System.currentTimeMillis();
        CmsBtProductGroupModel grp = service.selectProductGroupByModelCodeAndCartId("928", "1152863401", "29");
        long end = System.currentTimeMillis();
//        Assert.assertNotNull(grp);
        System.out.println("selectProductGroupByModelCodeAndCartId cost:" + (end - start));
    }

    @Test
    public void testSelectProductGroupByModelCodeAndCartId2() throws Exception {
        // s4 ({$natural:1}).skip(1,000,000)
        long start = System.currentTimeMillis();
        CmsBtProductGroupModel grp = service.selectProductGroupByModelCodeAndCartId("928", "1110685429", "27", "015");
        long end = System.currentTimeMillis();
//        Assert.assertNotNull(grp);
        System.out.println("selectProductGroupByModelCodeAndCartId cost:" + (end - start));
    }

    @Test
    public void testUpdateGroupsPlatformStatus() throws Exception {
        // s5 ({$natural:1}).skip(1,000,000)
        long start = System.currentTimeMillis();
        CmsBtProductGroupModel grp = service.getProductGroupByGroupId("928", 382177908L);
        List<String> listSxCode = new ArrayList<>();
        listSxCode.add("B40353");
        CmsBtProductGroupModel grp1 = service.updateGroupsPlatformStatus(grp, listSxCode);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(grp1);
        System.out.println("updateGroupsPlatformStatus cost:" + (end - start));
    }

    @Test
    public void testUpdateGroupsPlatformStatus2() throws Exception {
        // s6 ({$natural:1}).skip(1,000,000)
        long start = System.currentTimeMillis();
        CmsBtProductGroupModel grp = service.getProductGroupByGroupId("928", 189881808L);
        List<String> listSxCode = new ArrayList<>();
        listSxCode.add("B40353");
        Map<String, String> pCatInfoMap = new HashMap<>();
        pCatInfoMap.put("pCatId", "9527");
        pCatInfoMap.put("pCatPath", "大类>中类>小类");
        CmsBtProductGroupModel grp1 = service.updateGroupsPlatformStatus(grp, listSxCode, pCatInfoMap);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(grp1);
        System.out.println("updateGroupsPlatformStatus cost:" + (end - start));
    }

    @Test
    public void testUpdateUploadErrorStatus() throws Exception {
        // s7 ({$natural:1}).skip(1,000,000)
        long start = System.currentTimeMillis();
        CmsBtProductGroupModel grp = service.getProductGroupByGroupId("928", 189587510L);
        List<String> listSxCode = new ArrayList<>();
        listSxCode.add("B40353");
        boolean res = service.updateUploadErrorStatus(grp, listSxCode, "err000111");
        Assert.assertTrue(res);
        long end = System.currentTimeMillis();
        System.out.println("updateUploadErrorStatus cost:" + (end - start));

    }


    @Test
    public void testGetUnPublishedProducts() throws Exception {
        // s0 ({$natural:1}).skip(1,500,000)
        CmsBtProductGroupModel grp = service.getProductGroupByGroupId("928", 359975110L);
        long start = System.currentTimeMillis();
        List<String> codes = service.getUnPublishedProducts(grp);
        long end = System.currentTimeMillis();
        Assert.assertTrue(codes.size() > 0);
        System.out.println("updateUploadErrorStatus cost:" + (end - start));
    }

    @Test
    public void testSplitJmProductGroup() throws Exception {
        // s1 ({$natural:1}).skip(1,500,000) no diff
        long start = System.currentTimeMillis();
        String rr = service.splitJmProductGroup("928");
        long end = System.currentTimeMillis();
        Assert.assertNotNull(rr);
        System.out.println("splitJmProductGroup cost:" + (end - start));
    }

    @Test
    public void testUpdateFirst() throws Exception {
        // s2 ({$natural:1}).skip(1,500,000)
        JongoUpdate ju = new JongoUpdate();
        ju.setQuery("{'mainProductCode':#,'cartId':#}");
        ju.setQueryParameters("028-ps7361251", 32);
        ju.setUpdate("{$set:{'modified':#,'modifier':#}}");
        ju.setUpdateParameters(DateTimeUtil.getNowTimeStamp(), "test-sh-upd");
        long start = System.currentTimeMillis();
        service.updateFirst(ju, "928");
        long end = System.currentTimeMillis();
        System.out.println("splitJmProductGroup cost:" + (end - start));
    }

    @Test
    public void testAddCodeToGroup() throws Exception {
        // s3 ({$natural:1}).skip(1,500,000)
        long start = System.currentTimeMillis();
        CmsBtProductGroupModel grp = service.addCodeToGroup("928", 33, "028-ps7360889", "724940_006", false);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(grp);
        System.out.println("addCodeToGroup cost:" + (end - start));
    }

    @Test
    public void testDeleteGroup() throws Exception {
        // s4    ({$natural:1}).skip(1,500,000)
        long start = System.currentTimeMillis();
        CmsBtProductGroupModel grp = service.getProductGroupByGroupId("928", 3563147L);
        CmsBtProductGroupModel grpD = service.getProductGroupByGroupId("928", 3563147L);
        service.deleteGroup(grpD);
        long end = System.currentTimeMillis();
        System.out.println("deleteGroup cost:" + (end - start));

        grp.set_id(null);
        service.insert(grp);
    }
}
