package com.voyageone.web2.cms.views.search;

import com.voyageone.service.bean.com.UserConfigBean;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 15/12/15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsSearchIndexServiceTest {

    @Autowired
    private CmsSearchAdvanceService cmsSearchIndexService;

    @Test
    public void testGetMasterData() throws Exception {

        UserSessionBean userInfo = new UserSessionBean();
        userInfo.setSelChannelId("001");

        UserConfigBean userConfigModel = new UserConfigBean();
        userConfigModel.setUser_id(3);
        userConfigModel.setCfg_name("language_id");
        userConfigModel.setCfg_val1("en");

        CmsSessionBean cmsSession = new CmsSessionBean();

        Map<String, Object> resultInfo = cmsSearchIndexService.getMasterData(userInfo, cmsSession, "en");

        System.out.println(resultInfo.get("productStatusList"));
        System.out.println(resultInfo.get("publishStatusList"));
        System.out.println(resultInfo.get("labelTypeList"));
        System.out.println(resultInfo.get("priceTypeList"));
        System.out.println(resultInfo.get("compareTypeList"));
        System.out.println(resultInfo.get("brandList"));
        System.out.println(resultInfo.get("promotionList"));

    }
}