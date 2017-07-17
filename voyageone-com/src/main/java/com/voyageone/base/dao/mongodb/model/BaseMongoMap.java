package com.voyageone.base.dao.mongodb.model;

import com.mongodb.BasicDBObject;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.JsonUtil;

import java.util.LinkedHashMap;
import java.util.List;
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

    /**
     * 取得子节点的值
     * @param complexKey 节点路径，如 xx.yy.zz, 则调用时为：getSubNode(xx, yy, zz);
     */
    public Object getSubNode(String... complexKey) {
        if (complexKey == null) {
            return null;
        }
        int keyLvl = complexKey.length;
        Object subNode = null;

        for (int i = 0; i < keyLvl; i ++) {
            if (i == 0) {
                subNode = get(complexKey[0]);
            } else {
                if (subNode == null) {
                    return null;
                }
                if (subNode instanceof Map) {
                    subNode = ((Map) subNode).get(complexKey[i]);
                } else {
                    return null;
                }
            }
        }
        return subNode;
    }

    public int getIntAttribute(K key) {
        return convertToInt(getAttribute(key));
    }

    public double getDoubleAttribute(K key) {
        return convertToDoubel(getAttribute(key));
    }

    public String getStringAttribute(K key) {
        return convertToString(getAttribute(key));
    }

    public void setAttribute(K key, V value) {
        if (value == null) {
            super.remove(key);
        } else {
            super.put(key, value);
        }
    }

    public void setAttribute(K key, List value) {
        if (value == null) {
            super.remove(key);
        } else {
            super.put(key, (V) value.get(0));
        }
    }

    public void setStringAttribute(K key, Object value) {
        if (value == null) {
            super.remove(key);
        } else {
            String strValue = convertToString(value);
            super.put(key, (V) strValue);
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
        for (Map.Entry entry : this.entrySet()) {
            result.append(preStr + entry.getKey(), entry.getValue());
        }
        return result;
    }

    private int convertToInt(Object input) {
        int result = 0;
        if (input == null) {
            return result;
        }
        if (input instanceof Integer) {
            result = (Integer)input;
        } else {
            if(!StringUtil.isEmpty(input.toString())){
                result = Double.valueOf(input.toString()).intValue();
            }
        }
        return result;
    }

    private double convertToDoubel(Object input) {
        double result = 0.00;
        if (input == null) {
            return result;
        }
        if (input instanceof Double) {
            result = (Double)input;
        } else {
            if(!StringUtil.isEmpty(input.toString())){
                result = Double.parseDouble(input.toString());
            }
        }
        return result;
    }

    private String convertToString(Object input) {
        if (input == null) {
            return null;
        }
        if (input instanceof String) {
            return (String)input;
        } else {
            return input.toString();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }
}
