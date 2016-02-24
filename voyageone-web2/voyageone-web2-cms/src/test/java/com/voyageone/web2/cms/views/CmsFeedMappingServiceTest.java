package com.voyageone.web2.cms.views;

import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.web2.cms.views.mapping.feed.CmsFeedMappingService;
import com.voyageone.web2.core.bean.UserSessionBean;
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
public class CmsFeedMappingServiceTest {

    @Autowired
    private CmsFeedMappingService cmsFeedMappingService;

    private UserSessionBean userSessionBean = new UserSessionBean() {{
        setUserName("wms");
        setSelChannel(ChannelConfigEnums.Channel.SN);
    }};
}