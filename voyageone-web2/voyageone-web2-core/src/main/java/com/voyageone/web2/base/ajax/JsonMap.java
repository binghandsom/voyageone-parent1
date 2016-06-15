package com.voyageone.web2.base.ajax;

import java.util.HashMap;

/**
 * 小型的辅助类，方便创建基于 Map 模拟的 bean 对象
 * Created by Jonas on 11/25/15.
 */
public class JsonMap extends HashMap<String, Object> {
    public JsonMap put(String name, Object value) {
        put(name, value);
        return this;
    }
}