package com.voyageone.web2.cms.views.system;

import com.voyageone.web2.cms.views.system.category.CmsCategoryListService;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2015/12/29.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsCategoryListServiceTest {

    @Autowired
    CmsCategoryListService cmsCategoryListService;

    @Test
    public void testGetCategoryList() throws Exception {
        Map<String,Object> param = new HashMap<>();
        param.put("skip",0);
        param.put("limit", 1);
        List<JSONObject>ret= cmsCategoryListService.getCategoryList(param);
        System.out.println("");
    }
}