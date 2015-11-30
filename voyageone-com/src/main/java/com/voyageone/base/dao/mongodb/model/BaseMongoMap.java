package com.voyageone.base.dao.mongodb.model;

import java.util.HashMap;
import java.util.Map;

public class BaseMongoMap<K,V> extends HashMap<K,V> implements Map<K,V> {
    public V getAttribute(String key) {
        if (key == null) {
            return null;
        } else {
            return  super.get((K)key);
        }
    }

    public void setAttribute(String key, Object value) {
        if (value == null) {
            super.remove(key);
        } else {
            super.put((K)key, (V)value);
        }
    }
}
