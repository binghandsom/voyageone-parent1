package com.voyageone.bi.commonutils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

public class PropertiesUtils {

    private static Logger logger = Logger.getLogger(PropertiesUtils.class.getName());

    private static final String filePath = "keyvalue.properties";

    //根据key读取value
    public String readValue(String key) {
        Properties props = new Properties();
        try {
            InputStreamReader in = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(filePath), "UTF-8");
            props.load(in);
            String value = props.getProperty(key);
            //logger.info(key + ":" + value);
            return value;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    //根据key读取value
    public HashMap<String , String> readConfigFile(String file) {
        HashMap<String , String> fileMap = new HashMap<String , String>();
        Properties props = new Properties();
        try {
            InputStreamReader in = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(file), "UTF-8");
            props.load(in);
            Iterator<Entry<Object, Object>> it = props.entrySet().iterator();
            while (it.hasNext()) {
                Entry<Object, Object> entry = it.next();
                String key = entry.getKey().toString();
                String value = entry.getValue().toString();
                fileMap.put(key, value);
            }

            return fileMap;
        } catch (IOException  e) {
            logger.error(e.getMessage());
            return fileMap;
        }
    }
}
