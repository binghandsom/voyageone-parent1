package com.voyageone.web2.cms.views.mapping.feed.brand;

import com.voyageone.web2.cms.bean.BrandMappingBean;
import com.voyageone.web2.cms.views.mapping.brand.BrandMappingService;
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
public class BrandMappingServiceTest {


    @Autowired
    BrandMappingService brandMappingService;


    @Test
    public void testSynchronizePlatformBrands() {
        BrandMappingBean brandMapping = new BrandMappingBean();
        UserSessionBean userInfo = new UserSessionBean();

        brandMapping.setChannelId("001");
        brandMapping.setCartId(34);

        userInfo.setSelChannelId("001");
        userInfo.setUserName("Edward");

        brandMappingService.synchronizePlatformBrands(brandMapping, userInfo);
    }
}