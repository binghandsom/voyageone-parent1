package com.voyageone.service.impl.cms.RedisLua;

import org.apache.velocity.Template;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by dell on 2016/4/28.
 */
@Component
public class ImagePathCacheLua {
    private static HashOperations<String, Long, String> hashOperation;
    private static ZSetOperations<String,Object> ZSetOperation;
//    private  static StringRedisTemplate stringRedisTemplate;
    public static final String HashtableName = "voyageone_image_create_hashcode_file";
    public static final String ZSetName = "voyageone_image_create_hashcode_zset";
    public static  RedisTemplate<Object,Object> redisTemplate;
   @Autowired
    public  void setTemplate(RedisTemplate redisTemplate) {
        hashOperation = redisTemplate.opsForHash();
        ZSetOperation=redisTemplate.opsForZSet();
        ImagePathCacheLua.redisTemplate= redisTemplate;
        //template.execute()
    }

    /**
     *
     * @param key      hashCode
     * @param value    图片路径
     */
    public void set(long key, String value) {
      //  hashOperation.put(HashtableName, key, value);
      //  ZSetOperation.add(ZSetName, key, System.currentTimeMillis());
//        if (ZSetOperation.size(ZSetName) > 10000) {
//            Set<Long> hashCodeList = ZSetOperation.range(ZSetName, 1, 100);
//            Object[] keys = hashCodeList.toArray();
//            hashOperation.delete(HashtableName, keys);
//            ZSetOperation.remove(ZSetName,keys);
//        }
       // redisTemplate.execute()
        List<Object> KEYS = new ArrayList<>();
        KEYS.add(HashtableName);
        KEYS.add(key);
        KEYS.add(ZSetName);
        List<Object> ARGV=new ArrayList<>();
        ARGV.add(value);
        ARGV.add(System.currentTimeMillis());
        RedisScript<Long> script;
        script = new DefaultRedisScript<Long>(
                "redis.call('HSET',KEYS[1],KEYS[2],ARGV[1]);" +
                        "redis.call('ZADD',KEYS[3],ARGV[2],KEYS[2]);" +
                        "local size=redis.call('ZCARD',KEYS[3]);return size;" +
                        "if size >1000 then\n" +
                        "local keyList= redis.call('ZRANGE',KEYS[3],1,100)"+
                        "    redis.call('expire',KEYS[1], ARGV[1])\n" +
                        "end", Long.class);
        Long result= redisTemplate.execute(script,KEYS,ARGV.toArray());
        System.out.println("sha1:" + script.getSha1());
        System.out.println("Lua:" + script.getScriptAsString());
        System.out.println("dbsize:" + result);
    }
    public String get(Long key) {
        return hashOperation.get(HashtableName, key);


    }


    public void test71(long key,String value) {

//        ZSetOperation.add(ZSetName, key, System.currentTimeMillis());
//        if (ZSetOperation.size(ZSetName) > 10000) {
//            Set<Long> hashCodeList = ZSetOperation.range(ZSetName, 1, 100);
//            Object[] keys = hashCodeList.toArray();
//            hashOperation.delete(HashtableName, keys);
//            ZSetOperation.remove(ZSetName,keys);
        List<Object> keys = new ArrayList<>();
        keys.add(HashtableName);
        keys.add(key);
        keys.add(value);
        List<Object> args=new ArrayList<>();

        RedisScript<String> script;
        script = new DefaultRedisScript<String>(
                "redis.call('HSET',KEYS[1],KEYS[2],KEYS[3]); ", String.class);
        redisTemplate.execute(script,keys,args.toArray());

//
    }
    public  void testHashSet(long key,String value)
    {
        //HSET key field value
        //local list = redis.call("ZRANGE", KEYS[1], 1, -1)
       // redis.call('expire',KEYS[1], ARGV[1])
        //HSET key field value
        List<Object> keys = new ArrayList<>();
        keys.add(HashtableName);
        keys.add(key);
        keys.add(value);
      List<Object> args=new ArrayList<>();

        RedisScript<String> script;
        script = new DefaultRedisScript<String>(
                "redis.call('HSET',KEYS[1],KEYS[2],KEYS[3]); ", String.class);
         redisTemplate.execute(script,keys,args.toArray());

        script = new DefaultRedisScript<String>(
                "local value=redis.call('HGET',KEYS[1],KEYS[2]);return value; ", String.class);
       String result=   redisTemplate.execute(script,keys,args.toArray());
        System.out.println("sha1:" + script.getSha1());
        System.out.println("Lua:" + script.getScriptAsString());
        System.out.println("dbsize:" + result);
    }
    public void test72() {
        DefaultRedisScript<Boolean> script = new DefaultRedisScript<Boolean>();
        /**
         * isexistskey.lua内容如下：
         *
         * return tonumber(redis.call("exists",KEYS[1])) == 1;
         */
//        script.setScriptSource(new ResourceScriptSource(new ClassPathResource(
//                "/isexistskey.lua")));
//
//        script.setResultType(Boolean.class);// Must Set
//
//        System.out.println("script:" + script.getScriptAsString());
//        Boolean isExist = redisTemplate.execute(script,
//                Collections.singletonList("k2"), new Object[] {});
//        Assert.assertTrue(isExist);
    }
}
