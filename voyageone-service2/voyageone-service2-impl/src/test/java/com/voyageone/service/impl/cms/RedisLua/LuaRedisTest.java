package com.voyageone.service.impl.cms.RedisLua;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

/**
 * Created by dell on 2016/5/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class LuaRedisTest {
    @Autowired
    ImagePathCacheLua imagePathCache;
//    @Autowired
//     StringRedisTemplate stringRedisTemplate;
    // 测试Lua脚本



@Test
public  void  testHash()
{
    imagePathCache.set(123,"8888888888888");
}
    @Test
    public void test72() {
        StringRedisTemplate stringRedisTemplate=new StringRedisTemplate();
        DefaultRedisScript<Boolean> script = new DefaultRedisScript<Boolean>();
        /**
         * isexistskey.lua内容如下：
         *
         * return tonumber(redis.call("exists",KEYS[1])) == 1;
         */
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource(
                "/isexistskey.lua")));

        script.setResultType(Boolean.class);// Must Set

        System.out.println("script:" + script.getScriptAsString());
        Boolean isExist = stringRedisTemplate.execute(script,
                Collections.singletonList("k2"), new Object[] {});
        Assert.assertTrue(isExist);
    }
}
