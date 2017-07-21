package com.voyageone.web2.cms.views.search;

import com.voyageone.web2.core.bean.UserSessionBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by dell on 2017/7/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:META-INF/context-web2.xml","classpath*:META-INF/context-web2-mvc.xml"})
public class CmsAdvSearchOtherServiceTest {
    @Autowired
    CmsAdvSearchOtherService cmsAdvSearchOtherService;

    @Test
    public void testUpdateOnePrice() throws Exception {

        HashMap<String, Object> map = new HashMap<>();
        map.put("code","609973-460");
        map.put("cartId","23");
        map.put("clientMsrpPrice","555");
        map.put("clientRetailPrice","666");
        map.put("prodId","4361021");
        UserSessionBean user = new UserSessionBean();
        user.setSelChannelId("001");
        user.setUserName("xusong");
       // cmsAdvSearchOtherService.updateOnePrice(map,user);
    }

    @Test
    public void testGetAllPlatformsPrice() throws Exception {
        UserSessionBean user = new UserSessionBean();
        user.setSelChannelId("001");
        user.setUserName("xusong");
        //cmsAdvSearchOtherService.getAllPlatformsPrice(0,user);
    }
}