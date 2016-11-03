package com.voyageone.common.redis;

import com.voyageone.common.configs.Channels;
import com.voyageone.common.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author aooer 2016/3/23.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class CacheHelperTest {

    @Test
    public void testPushListOperation() throws Exception {
        ListOperations<String, Object> operations = CacheHelper.getListOperation();
        for (int i=0; i<10; i++) {
            operations.leftPush("testList", "a"+i);
            System.out.println("a"+i);
        }
    }

    @Test
    public void testPopListOperation() throws Exception {
        ListOperations<String, Object> operations = CacheHelper.getListOperation();
        for (int i=0; i<20; i++) {
            String key = (String)operations.rightPop("testList");
            System.out.println(key);
        }
    }
}