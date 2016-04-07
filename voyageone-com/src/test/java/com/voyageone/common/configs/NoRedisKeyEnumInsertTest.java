package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CarrierEnums;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

/**
 * @author aooer 2016/3/30.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class NoRedisKeyEnumInsertTest {

    @Test
    public void testInsert() throws Exception {
        CacheHelper.reFreshSSB("dasdfsda",new HashMap(){{ put("a",123);}});
    }

    @Test
    public void testDelete() throws Exception {
        CacheHelper.delete("dasdfsda");
    }



}
