package com.voyageone.base.dao.mongodb;

import com.voyageone.base.dao.mongodb.support.MongoCollection;
import com.voyageone.common.util.StringUtils;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.Map;

/**
 * MongoCollectionMapping
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class MongoCollectionMapping {
    private static final Map<String, String> collectionCache = new ConcurrentReferenceHashMap<>();

    public static String getCollectionName(Class<?> entityClass) {

        String className = entityClass.getSimpleName();

        if (collectionCache.containsKey(className)) {
            return collectionCache.get(className);
        }
        
        String result = null;
        MongoCollection mongoCollection = entityClass.getAnnotation(MongoCollection.class);
        if (mongoCollection != null && !StringUtils.isEmpty(mongoCollection.value())) {
            result = mongoCollection.value();
        }
		
        if (result == null) {
            result = toUnderlineName(className);
            if (result.endsWith("_model")) {
                result = result.substring(0, result.length()-6);
            }
        }
        collectionCache.put(className, result);
        return result;

    }

    private static String getPartitionValue(String channelId) {
        String result = "";
        if (channelId != null) {
            result = "_c" + channelId;
        }
        return result;
    }

    public static String getCollectionName(Class<?> entityClass, String channelId) {
        String collectionName = getCollectionName(entityClass);
        return getCollectionName(collectionName, channelId);
    }

    public static String getCollectionName(String collectionName, String channelId) {
        if (collectionName != null) {
            return collectionName + getPartitionValue(channelId);
        }
        return null;
    }

    private static final char SEPARATOR = '_';
    public static String toUnderlineName(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i >= 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    if (i > 0) sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }
}
