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
 * Created by yangjindong on 2017/5/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2-sh.xml")
public class ProductGroupServiceTest {
    @Autowired
    private ProductGroupService service;


    @Test
    public void testGetList() throws Exception {
        JongoQuery jq = new JongoQuery();
        String query = "cartId:" + 27;
        query = query + ",productCodes:" + "\"75584\"";
        jq.setQuery("{" + query + "}");
        long start = System.currentTimeMillis();
        List<CmsBtProductGroupModel> grpList = service.getList("928", jq);
        long end = System.currentTimeMillis();
        System.out.println("getList cost:" + (end - start));
        Assert.assertTrue(grpList.size() > 0);
    }

    @Test
    public void testGetProductGroupByGroupId() throws Exception {
        long start = System.currentTimeMillis();
        CmsBtProductGroupModel grp = service.getProductGroupByGroupId("928", 864370L);
        long end = System.currentTimeMillis();
        System.out.println("getProductGroupByGroupId cost:" + (end - start));
        Assert.assertNotNull(grp);
        Assert.assertEquals(grp.getGroupId().longValue(), 864370L);
    }


    @Test
    public void testGetProductGroupByQuery() throws Exception {
        long start = System.currentTimeMillis();
        String q = "{'cartId':" + 27 + ",'productCodes':\"75584\"}";
        CmsBtProductGroupModel grp = service.getProductGroupByQuery("928", q);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(grp);
        System.out.println("testGetProductGroupByQuery cost:" + (end - start));
    }

    @Test
    public void testGetProductGroupByQuery2() throws Exception {
        long start = System.currentTimeMillis();
        JongoQuery jq = new JongoQuery();
        jq.setQuery("{\"productCodes\": #, \"cartId\": #}");
        jq.setParameters("75584", 27);
        CmsBtProductGroupModel grp = service.getProductGroupByQuery("928", jq);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(grp);
        System.out.println("testGetProductGroupByQuery2 cost:" + (end - start));
    }

    @Test
    public void testSelectProductGroupByCode() throws Exception {
        long start = System.currentTimeMillis();
        CmsBtProductGroupModel grp = service.selectProductGroupByCode("928", "75584", 27);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(grp);
        System.out.println("selectProductGroupByCode cost:" + (end - start));
    }

    @Test
    public void testSelectProductGroupListByCode() throws Exception {
        long start = System.currentTimeMillis();
        List<CmsBtProductGroupModel> grpList = service.selectProductGroupListByCode("928", "75584");
        long end = System.currentTimeMillis();
        Assert.assertNotNull(grpList);
        Assert.assertTrue(grpList.size() > 0);
        System.out.println("selectProductGroupListByCode cost:" + (end - start));
    }

    @Test
    public void testSelectMainProductGroupByCode() throws Exception {
        long start = System.currentTimeMillis();
        CmsBtProductGroupModel grp = service.selectMainProductGroupByCode("928", "75584", 27);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(grp);
        System.out.println("selectMainProductGroupByCode cost:" + (end - start));
    }

    @Test
    public void testSelectProductGroupByNumIId() throws Exception {
        long start = System.currentTimeMillis();
        CmsBtProductGroupModel grp = service.selectProductGroupByNumIId("928", 28, "1954753032");
        long end = System.currentTimeMillis();
        Assert.assertNotNull(grp);
        System.out.println("selectProductGroupByNumIId cost:" + (end - start));
    }

    @Test
    public void testUpdate() throws Exception {
        CmsBtProductGroupModel grp = service.getProductGroupByGroupId("928", 864370L);
        grp.setModifier(grp.getModifier() + "-sh-test123");
        long start = System.currentTimeMillis();
        service.update(grp);
        long end = System.currentTimeMillis();
        System.out.println("update cost:" + (end - start));
    }

    @Test
    public void testInsert() throws Exception {
        CmsBtProductGroupModel grp = service.getProductGroupByGroupId("928", 864370L);
        long start = System.currentTimeMillis();
        grp.set_id(null);
        long grpId = grp.getGroupId() * Math.round(10 * Math.random());
        grp.setGroupId(grpId * -1);
        service.insert(grp);
        long end = System.currentTimeMillis();
        System.out.println("insert cost:" + (end - start));
    }

    @Test
    public void testUpdateMainProduct() throws Exception {
        long start = System.currentTimeMillis();
        service.updateMainProduct("928", "806580_010", 834940L, "sh-test1234");
        long end = System.currentTimeMillis();
        System.out.println("updateMainProduct cost:" + (end - start));
    }

    @Test
    public void testSelectProductGroupByModelCodeAndCartId() throws Exception {
        long start = System.currentTimeMillis();
        CmsBtProductGroupModel grp = service.selectProductGroupByModelCodeAndCartId("928", "10004913", "29");
        long end = System.currentTimeMillis();
//        Assert.assertNotNull(grp);
        System.out.println("selectProductGroupByModelCodeAndCartId cost:" + (end - start));
    }

    @Test
    public void testSelectProductGroupByModelCodeAndCartId2() throws Exception {
        long start = System.currentTimeMillis();
        CmsBtProductGroupModel grp = service.selectProductGroupByModelCodeAndCartId("928", "10004913", "29", "023");
        long end = System.currentTimeMillis();
//        Assert.assertNotNull(grp);
        System.out.println("selectProductGroupByModelCodeAndCartId cost:" + (end - start));
    }

    @Test
    public void testUpdateGroupsPlatformStatus() throws Exception {
        CmsBtProductGroupModel grp = service.getProductGroupByGroupId("928", 834977L);
        List<String> listSxCode = new ArrayList<>();
        listSxCode.add("B40353");
        long start = System.currentTimeMillis();
        CmsBtProductGroupModel grp1 = service.updateGroupsPlatformStatus(grp, listSxCode);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(grp1);
        System.out.println("updateGroupsPlatformStatus cost:" + (end - start));
    }

    @Test
    public void testUpdateGroupsPlatformStatus2() throws Exception {
        CmsBtProductGroupModel grp = service.getProductGroupByGroupId("928", 834977L);
        List<String> listSxCode = new ArrayList<>();
        listSxCode.add("B40353");
        Map<String, String> pCatInfoMap = new HashMap<>();
        pCatInfoMap.put("pCatId", "9527");
        pCatInfoMap.put("pCatPath", "大类>中类>小类");
        long start = System.currentTimeMillis();
        CmsBtProductGroupModel grp1 = service.updateGroupsPlatformStatus(grp, listSxCode, pCatInfoMap);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(grp1);
        System.out.println("updateGroupsPlatformStatus cost:" + (end - start));
    }

    @Test
    public void testUpdateUploadErrorStatus() throws Exception {
        CmsBtProductGroupModel grp = service.getProductGroupByGroupId("928", 834977L);
        List<String> listSxCode = new ArrayList<>();
        listSxCode.add("B40353");
        long start = System.currentTimeMillis();
        boolean res = service.updateUploadErrorStatus(grp, listSxCode, "err000111");
        Assert.assertTrue(res);
        long end = System.currentTimeMillis();
        System.out.println("updateUploadErrorStatus cost:" + (end - start));

    }


    @Test
    public void testGetUnPublishedProducts() throws Exception {
        CmsBtProductGroupModel grp = service.getProductGroupByGroupId("928", 834977L);
        long start = System.currentTimeMillis();
        List<String> codes = service.getUnPublishedProducts(grp);
        long end = System.currentTimeMillis();
        Assert.assertTrue(codes.size() > 0);
        System.out.println("updateUploadErrorStatus cost:" + (end - start));
    }

    @Test
    public void testSplitJmProductGroup() throws Exception {
        long start = System.currentTimeMillis();
        String rr = service.splitJmProductGroup("928");
        long end = System.currentTimeMillis();
        Assert.assertNotNull(rr);
        System.out.println("splitJmProductGroup cost:" + (end - start));
    }

    @Test
    public void testUpdateFirst() throws Exception {
        JongoUpdate ju = new JongoUpdate();
        ju.setQuery("{'mainProductCode':#,'cartId':#}");
        ju.setQueryParameters("ML501VIB_D", 28);
        ju.setUpdate("{$set:{'modified':#,'modifier':#}}");
        ju.setUpdateParameters(DateTimeUtil.getNowTimeStamp(), "test-sh-upd");
        long start = System.currentTimeMillis();
        service.updateFirst(ju, "928");
        long end = System.currentTimeMillis();
        System.out.println("splitJmProductGroup cost:" + (end - start));
    }

    @Test
    public void testAddCodeToGroup() throws Exception {
        long start = System.currentTimeMillis();
        CmsBtProductGroupModel grp = service.addCodeToGroup("928", 28, "724940_006_sh_test", "724940_006", false);
        long end = System.currentTimeMillis();
        Assert.assertNotNull(grp);
        System.out.println("addCodeToGroup cost:" + (end - start));
    }

    @Test
    public void testDeleteGroup() throws Exception {
        CmsBtProductGroupModel grp = service.getProductGroupByGroupId("928", 864370L);
        grp.set_id(null);
        long grpId = grp.getGroupId() * Math.round(-10 * Math.random()) * -1;
        grp.setGroupId(grpId);
        service.insert(grp);

        CmsBtProductGroupModel grpD = service.getProductGroupByGroupId("928", grpId);
        long start = System.currentTimeMillis();
        service.deleteGroup(grpD);
        long end = System.currentTimeMillis();
        System.out.println("deleteGroup cost:" + (end - start));

    }
}
