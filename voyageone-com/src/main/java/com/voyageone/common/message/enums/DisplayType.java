package com.voyageone.common.message.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 预定义提示信息的提示类型,用于控制提示的显示方式
 * @author Jonas
 * @version 2.0.0, 12/5/15
 */
public enum DisplayType {
    /**
     * 弹出提示框
     */
    ALERT(1),
    /**
     * 顶端弹出自动关闭
     */
    NOTIFY(2),
    /**
     * 右下弹出自动关闭
     */
    POP(3),
    /**
     * 用户自定义处理
     */
    CUSTOM(4);

    private final int id;

    DisplayType(int id) {
        this.id = id;
    }

    @JsonValue
    public int getId() {
        return id;
    }

    public static DisplayType valueOf(int id) {
        for (DisplayType type: values()) {
            if (type.getId() == id) return type;
        }
        return null;
    }
}
