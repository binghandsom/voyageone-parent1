package com.voyageone.common.configscache;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.redis.CacheTemplateFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.redis.core.HashOperations;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @deprecated 本地配置不同，建议不用此类
 *  KeyValue 配置文件的专用配置访问类
 * Created by Tester on 4/16/2015.
 */
public class PropertiesConfigs {

    private static Log logger = LogFactory.getLog(PropertiesConfigs.class);

    /* redis key */
    private static final String KEY = CacheKeyEnums.ConfigData_Properties.toString();

    private static HashOperations<String, String, String> hashOperations = CacheTemplateFactory.getHashOperation();

    static {
        if (!CacheTemplateFactory.getCacheTemplate().hasKey(KEY)) {
            try {
                Map<String, String> propertyBeanMap = new HashMap<>();
                Resource[] resources = resources = new PathMatchingResourcePatternResolver().getResources("classpath*:config/*_keyvalue.properties");
                Arrays.asList(resources).forEach(resource -> {
                    Properties props = new Properties();
                    try {
                        InputStream stream = resource.getInputStream();
                        InputStreamReader streamReader = new InputStreamReader(stream, "UTF-8");
                        props.load(streamReader);
                        props.stringPropertyNames().forEach(pname -> {
                            propertyBeanMap.put(buildKey(pname), props.getProperty(pname));
                        });
                    } catch (Exception e) {
                        logger.error("读取KeyValue配置出现异常：" + e);
                    }
                });
                CacheHelper.reFreshSSS(KEY, propertyBeanMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(String key) {
        return key+CacheHelper.SKIP;
    }

    /**
     * 读取配置值
     */
    public static String readValue(String key) {
        return hashOperations.get(KEY, buildKey(key));
    }
}