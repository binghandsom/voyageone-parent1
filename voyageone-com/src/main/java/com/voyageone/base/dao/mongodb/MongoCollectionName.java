package com.voyageone.base.dao.mongodb;

import com.voyageone.base.dao.mongodb.support.MongoCollection;
import com.voyageone.common.util.StringUtils;

import static java.util.stream.Collectors.joining;

/**
 * MongoCollectionName
 *
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
class MongoCollectionName {
    private static final String SEPARATOR = "_";

    public static String getCollectionName(Class<?> entityClass) {
        String className = entityClass.getSimpleName();
        String result = null;
        MongoCollection mongoCollection = entityClass.getAnnotation(MongoCollection.class);
        if (mongoCollection != null && !StringUtils.isEmpty(mongoCollection.value())) {
            result = mongoCollection.value();
        }

        if (result == null) {
            result = toUnderlineName(className);
            if (result.endsWith("_model")) {
                result = result.substring(0, result.length() - 6);
            }
        }
        return result;
    }

    public static String getCollectionName(Class<?> entityClass, String partStr, String split) {
        String collectionName = getCollectionName(entityClass);
        return getCollectionName(collectionName, partStr, split);
    }

    public static String getCollectionName(String collectionName, String partStr, String split) {
        if (collectionName != null) {
            return collectionName + getPartitionValue(partStr, split);
        }
        return null;
    }

    private static String getPartitionValue(String partStr, String split) {
        String result = "";
        if (partStr != null) {
            result = "_" + split + partStr;
        }
        return result;
    }

    private static String toUnderlineName(String s) {
        if (s == null) {
            return null;
        }
        return s.chars()
                .boxed()
                .map(boxedCodePoint -> {
                    int codePoint = boxedCodePoint;
                    if (Character.isUpperCase(codePoint)) {
                        return SEPARATOR + Character.toString((char) Character.toLowerCase(codePoint));
                    } else {
                        return Character.toString((char) codePoint);
                    }
                })
                .collect(joining());
    }
}
