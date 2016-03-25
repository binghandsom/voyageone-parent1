package com.voyageone.common.configs;

import com.voyageone.common.util.JsonUtil;
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
public class ImsCategoryConfigsTest {

    @Test
    public void testGetMtCategoryBeanById() throws Exception {
        System.out.println(JsonUtil.getJsonString(ImsCategorys.getMtCategoryBeanById(35866)));
    }
}