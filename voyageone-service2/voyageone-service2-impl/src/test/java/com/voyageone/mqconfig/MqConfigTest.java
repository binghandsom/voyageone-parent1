package com.voyageone.mqconfig;

import com.voyageone.service.impl.com.mq.config.MqDisAllowConsumerIpConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author aooer 2016/5/9.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class MqConfigTest {

    @Autowired
    private MqDisAllowConsumerIpConfig mqDisAllowConsumerIpConfig;

    @Test
    public void testDisallowName() throws Exception {
        System.out.println(mqDisAllowConsumerIpConfig.getMqDisallowConsumerIpMap());
    }
}
