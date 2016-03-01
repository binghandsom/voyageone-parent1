package com.voyageone.web2.cms.views.translate;

import static org.junit.Assert.*;
import com.voyageone.cms.service.model.CmsFeedCategoryModel;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.web2.cms.views.mapping.feed.CmsFeedMappingService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * @author Jonas, 12/10/15
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsFeedCustPropServiceTest {

    @Autowired
    private CmsFeedCustPropService cmsFeedCustPropService;

    @Test
    public void testGetMasterData() throws Exception {
        UserSessionBean userInfo = new UserSessionBean();
        userInfo.setSelChannelId("010");

        List<CmsFeedCategoryModel> resultInfo = cmsFeedCustPropService.getTopCategories(userInfo);
        assertNotNull(resultInfo);
    }
}