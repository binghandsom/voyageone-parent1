package com.voyageone.base.dao.mongodb.model;

import com.mongodb.BasicDBObject;
import com.voyageone.common.util.JsonUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * BaseMongoMap
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class BaseMongoMap<K, V> extends LinkedHashMap<K, V> implements Map<K, V> {

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(K key) {
        if (key == null) {
            return null;
        } else {
            return (T) super.get(key);
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
        for (Object o : this.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            changeKeyMap.put(preStr + entry.getKey(), entry.getValue());
        }
        return JsonUtil.getJsonString(changeKeyMap);
    }

    public BasicDBObject toUpdateBasicDBObject(String preStr) {
        BasicDBObject result = new BasicDBObject();
        for (Object o : this.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            result.append(preStr + entry.getKey(), entry.getValue());
        }
        return result;
    }

    public double convertToDoubel(Object input) {
        double result = 0;
        if (input == null) {
            return result;
        }
        if (input instanceof Double) {
            result = (Double)input;
        } else {
            result = Double.parseDouble(input.toString());
        }
        return result;
    }
}
