package com.voyageone.service.impl.cms.jumei;

import com.voyageone.service.impl.cms.jumei2.JuMeiProductPlatform3Service;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class JuMeiProductPlatform3ServiceTest {
    @Autowired
    JuMeiProductPlatform3Service service;

    @Test
    public void testUpdateJmByPromotionId() throws Exception {
        int promotionId = 76;
        service.updateJmByPromotionId(1248);
    }
}
