package com.voyageone.common.configscache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author aooer 2016/3/23.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class DiscountRateConfigsTest {

    @Test
    public void testGetDiscountRate() throws Exception {
        System.out.println(DiscountRateConfigs.getDiscountRate("001","GZ"));
    }
}