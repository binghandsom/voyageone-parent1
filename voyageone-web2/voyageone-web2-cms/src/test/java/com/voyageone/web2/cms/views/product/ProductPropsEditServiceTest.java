package com.voyageone.web2.cms.views.product;

import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lewis on 2016/2/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class ProductPropsEditServiceTest {

    @Autowired
    CmsProductDetailService productPropsEditService;

    @Test
    public void testConfirmChangeCategory() throws Exception {

        Map requestMap = new HashMap<>();
        requestMap.put("categoryId","a73c965b1150fb36a5ed73daec3e84ff");
        requestMap.put("categoryPath","饰品/流行首饰/时尚饰品新>手镯");
        requestMap.put("productId",161l);

        UserSessionBean userSession = new UserSessionBean();
        userSession.setSelChannelId("013");
        userSession.setUserName("lewis");

        Map<String,Object> resMap = productPropsEditService.changeProductCategory(requestMap,userSession, new CmsSessionBean());

        System.out.println();

    }
}