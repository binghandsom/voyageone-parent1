package com.voyageone.service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * SKU 分割枚举
 * Created by jonasvlag on 16/7/14.
 *
 * @version 2.3.0
 * @since 2.3.0
 */
public enum SkuSplit {

    NONE(0),

    CODE(1),

    MODEL(2);

    private Integer id;

    SkuSplit(Integer id) {
        this.id = id;
    }

    public static SkuSplit valueOf(Integer id) {
        for (SkuSplit item : values()) {
            if (item.id.equals(id))
                return item;
        }
        return null;
    }

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.id);
        map.put("name", name());
        return map;
    }

    @JsonCreator
    public static SkuSplit valueOf(Map<String, Object> map) {
        return valueOf((Integer) map.get("id"));
    }
}
