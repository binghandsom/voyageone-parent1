package com.voyageone.batch.configs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.voyageone.batch.bi.util.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * KeyValue 配置文件的专用配置访问类
 * Created by Tester on 4/16/2015.
 */
public class FileProperties {
    private static HashMap<String, String> keyValueMap;

    private static Log logger = LogFactory.getLog(FileProperties.class);

    public static void init() throws IOException {

        if (keyValueMap == null) keyValueMap = new HashMap<>();

        // 搜索所有配置文件
        String path = FileUtils.getConfPath();
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("file:"+path+"/config/*_keyvalue.properties");

        // 挨个读取
        for (Resource resource : resources) {
            loadProps(resource);
        }

        if (resources.length < 1) {
            logger.warn("没有读取任何 keyvalue 属性文件");
        }
    }

    private static void loadProps(Resource resource) throws IOException {
        java.util.Properties props = new java.util.Properties();

        try (InputStream stream = resource.getInputStream();

             InputStreamReader streamReader = new InputStreamReader(stream, "UTF-8")) {

            props.load(streamReader);

            logger.info("从 " + resource.getFilename() + " 文件读取数量：" + props.size() + " （未消除重复的数量）");

            for (String name : props.stringPropertyNames()) {

                if (keyValueMap.containsKey(name)) {
                    logger.warn("读入 Properties 时，发现了重复的元素：" + name);
                }

                keyValueMap.put(name, props.getProperty(name));
            }
        }
    }

    /**
     * 读取配置值
     */
    public static String readValue(String key) {
        return keyValueMap.get(key);
    }
}