package com.voyageone.web2.cms.views.channel;

import com.voyageone.web2.core.bean.UserSessionBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

//        List<CmsMtFeedCategoryModel> resultInfo = cmsFeedCustPropService.getTopFeedCategories(userInfo);
//        assertNotNull(resultInfo);
    }
}