package com.voyageone.common.masterdate.schema.enums;

public enum ValueTypeEnum {
    TEXT("text"),
    DECIMAL("decimal"),
    INTEGER("integer"),
    LONG("long"),
    DATE("date"),
    TIME("time"),
    URL("url"),
    TEXTAREA("textarea"),
    HTML("html");

    private final String type;

    private ValueTypeEnum(String type) {
        this.type = type;
    }

    public String toString() {
        return this.type;
    }

    public String value() {
        return this.type;
    }

    public static ValueTypeEnum getEnum(String name) {
        ValueTypeEnum[] values = values();
        ValueTypeEnum[] arr$ = values;
        int len$ = values.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            ValueTypeEnum value = arr$[i$];
            if(value.value().equals(name)) {
                return value;
            }
        }

        return null;
    }
}