package com.voyageone.web2.cms.views.system.cache;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.redis.CacheHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author aooer 2016/3/25.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CacheManagerControllerTest {


    @Test
    public void testInit() throws Exception {
        System.out.println(cacheKeySet());
        System.out.println(cacheKeySet().size());
    }

    @Test
    public void testClear() throws Exception {
        List<String> keys=Arrays.asList("ConfigData_CartConfigs");
        keys.forEach(k->Assert.notNull(CacheKeyEnums.KeyEnum.valueOf(k), "参数校验未通过，" + k + "不是CacheKeyEnums实例"));
        CacheHelper.getCacheTemplate().delete(keys);
        System.out.println(cacheKeySet());
        System.out.println(cacheKeySet().size());
    }

    private Set<String> cacheKeySet(){
        return CacheHelper.getCacheTemplate().keys(CacheKeyEnums.CONFIG_ALL_KEY_REGEX);
    }
}