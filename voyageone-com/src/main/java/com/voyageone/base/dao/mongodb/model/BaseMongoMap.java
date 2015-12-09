package com.voyageone.base.dao.mongodb.model;

import com.mongodb.BasicDBObject;
import com.voyageone.common.util.JsonUtil;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaseMongoMap<K,V> extends LinkedHashMap<K,V> implements Map<K,V> {
    public V getAttribute(K key) {
        if (key == null) {
            return null;
        } else {
            return super.get(key);
        }
    }

    public void setAttribute(K key, V value) {
        if (value == null) {
            super.remove(key);
        } else {
            super.put(key, value);
        }
    }

    @Override
    public String toString() {
        return JsonUtil.getJsonString(this);
    }

    public String toUpdateString(String preStr) {
        Map<String, Object> changeKeyMap = new LinkedHashMap<>();
        Iterator iterator = this.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            changeKeyMap.put(preStr + entry.getKey(), entry.getValue());
        }
        return JsonUtil.getJsonString(changeKeyMap);
    }

    public BasicDBObject toUpdateBasicDBObject(String preStr) {
        BasicDBObject result = new BasicDBObject();
        Iterator iterator = this.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            result.append(preStr + entry.getKey(), entry.getValue());
        }
        return result;
    }
}
