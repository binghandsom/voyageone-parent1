package com.voyageone.web2.cms.views.pop.tag.promotion;

import com.voyageone.web2.cms.model.CmsBtTagModel;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author gubuchun 15/12/9
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsPromotionSelectServiceTest {

    @Autowired
    private CmsPromotionSelectService cmsPromotionSelectService;

    @Test
    public void testGetPromotionTags() throws Exception {
        JSONObject params = new JSONObject();
        params.put("refTagId", 1);
        List<CmsBtTagModel> resultList = cmsPromotionSelectService.getPromotionTags(params);
        System.out.println(resultList);
        assert resultList.size() > 0;
    }

    @Test
    public void testAddToPromotion() throws Exception {
        JSONObject params = new JSONObject();
        JSONArray productIds = new JSONArray();
        productIds.add((long)1);
        params.put("productIds", productIds);
        params.put("tagPath", "-8-");
        cmsPromotionSelectService.addToPromotion(params, "001", "testcc");
        assert true;
    }

}
