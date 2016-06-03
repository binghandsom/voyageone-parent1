package com.voyageone.web2.cms.views.channel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.HashMapChangeSet;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by Ethan Shi on 2016/5/25.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:META-INF/context-web2.xml","classpath*:META-INF/context-web2-mvc.xml"})
public class CmsChannelCategoryControllerTest {

    @Autowired
    private CmsChannelCategoryController cmsChannelCategoryController;

    @Test
    public void testGetSellerCat() throws Exception {

        HashMap<String, Object> map = new HashMap();
        map.put("cartId", 20);
        cmsChannelCategoryController.getSellerCat(map);

    }
}