package com.voyageone.web2.cms.views.setting.mapping.platform;

import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Jonas, 1/12/16.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsPlatformMappingServiceTest {

    @Autowired
    private CmsPlatformMappingService platformMappingService;

    @Test
    public void testGetPlatformMap() throws Exception {

        platformMappingService.getPlatformMap(
                ChannelConfigEnums.Channel.SN,
                Integer.valueOf(CartEnums.Cart.TG.getId()));
    }
}