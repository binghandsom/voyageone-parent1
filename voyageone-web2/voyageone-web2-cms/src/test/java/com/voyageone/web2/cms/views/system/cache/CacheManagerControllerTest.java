package com.voyageone.web2.cms.views.system.cache;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.redis.CacheTemplateFactory;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.web2.base.ajax.AjaxResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

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
        keys.forEach(k->Assert.notNull(CacheKeyEnums.valueOf(k),"参数校验未通过，"+k+"不是CacheKeyEnums实例"));
        CacheTemplateFactory.getCacheTemplate().delete(keys);
        System.out.println(cacheKeySet());
        System.out.println(cacheKeySet().size());
    }

    private Set<String> cacheKeySet(){
        return CacheTemplateFactory.getCacheTemplate().keys("ConfigData_*");
    }
}