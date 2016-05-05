package com.voyageone.cacheable;

import com.voyageone.cacheable.service.TCacheableService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author aooer 2016/5/5.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TCacheableServiceTest {

    @Autowired
    private TCacheableService tCacheableService;

    @Test
    public void testQueryByName() throws Exception {
        System.out.println();
        System.out.println("测试CacheAble缓存：");
        for (int i=0;i<3;i++)
            System.out.println("QUERY:\t"+tCacheableService.queryByName("asdsadf"));
        System.out.println("测试CachePut缓存：");
        for (int i=0;i<3;i++) {
            System.out.println("UPDATE:\t"+tCacheableService.updateByName("asdsadf"));
            System.out.println("QUERY:\t"+tCacheableService.queryByName("asdsadf"));
        }
        System.out.println("测试CacheEvict缓存：");
        for (int i=0;i<3;i++) {
            System.out.println("REMOVE:\t"+tCacheableService.removeByName("asdsadf"));
            System.out.println("QUERY:\t"+tCacheableService.queryByName("asdsadf"));
        }
    }
}