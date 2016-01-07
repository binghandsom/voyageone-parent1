package com.voyageone.cms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 属性匹配中,属性的类型(已针对 Jackson 优化)
 *
 * @author Jonas, 1/6/16.
 * @version 2.0.0
 * @since 2.0.0
 */
public enum MappingPropType {
    /**
     * 通用属性
     */
    COMMON,
    /**
     * 类目属性
     */
    FIELD,
    /**
     * SKU 属性
     */
    SKU;

    @JsonValue
    private String toJson() {
        return name();
    }

    @JsonCreator
    private static MappingPropType fromJson(String value) {
        return valueOf(value);
    }
}
