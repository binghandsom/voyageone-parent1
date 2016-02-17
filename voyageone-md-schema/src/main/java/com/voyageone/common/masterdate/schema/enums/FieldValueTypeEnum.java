package com.voyageone.common.masterdate.schema.enums;

public enum FieldValueTypeEnum {
    NONE(""),
    INT("INT"),
    DOUBLE("DOUBLE")
    ;

    private final String type;

    private FieldValueTypeEnum(String type) {
        this.type = type;
    }

    public String toString() {
        return this.type;
    }

    public String value() {
        return this.type;
    }

    public static FieldValueTypeEnum getEnum(String name) {
        FieldValueTypeEnum[] values = values();
        for (FieldValueTypeEnum value : values) {
            if (value.value().equalsIgnoreCase(name)) {
                return value;
            }
        }

        return FieldValueTypeEnum.NONE;
    }
}
