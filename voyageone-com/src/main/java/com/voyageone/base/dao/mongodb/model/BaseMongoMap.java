package com.voyageone.base.dao.mongodb.model;

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
}
