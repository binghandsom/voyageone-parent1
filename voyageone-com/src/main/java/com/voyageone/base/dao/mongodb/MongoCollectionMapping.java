package com.voyageone.base.dao.mongodb;

import java.util.HashMap;
import java.util.Map;

public class MongoCollectionMapping {
    private Map<String, String> collectionNameMap;
    private Map<String, String> _collectionNameMap = new HashMap<>();
    public Map<String, String> getCollectionNameMap() {
        return collectionNameMap;
    }

    public void setCollectionNameMap(Map<String, String> collectionNameMap) {
        this.collectionNameMap = collectionNameMap;
        for (Map.Entry<String, String> entry : collectionNameMap.entrySet()) {
            _collectionNameMap.put(entry.getKey().toLowerCase(), entry.getValue());
        }
    }

    public String getCollectionName(Class<?> entityClass) {
        String collectionName = entityClass.getSimpleName().toLowerCase();
        if (_collectionNameMap.containsKey(collectionName)) {
            return _collectionNameMap.get(collectionName);
        }
        return null;
    }

    private static String getPartitionValue(String channelId) {
        String result = "";
        if (channelId != null) {
            result = "_c" + channelId;
        }
        return result;
    }

    public String getCollectionName(Class<?> entityClass, String channelId) {
        String collectionName = getCollectionName(entityClass);
        if (collectionName != null) {
            return collectionName + getPartitionValue(channelId);
        }
        return null;
    }
}
