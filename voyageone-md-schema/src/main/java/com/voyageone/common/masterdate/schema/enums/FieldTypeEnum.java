package com.voyageone.common.masterdate.schema.enums;

import com.voyageone.common.masterdate.schema.field.ComplexField;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.masterdate.schema.field.LabelField;
import com.voyageone.common.masterdate.schema.field.MultiCheckField;
import com.voyageone.common.masterdate.schema.field.MultiComplexField;
import com.voyageone.common.masterdate.schema.field.MultiInputField;
import com.voyageone.common.masterdate.schema.field.SingleCheckField;

public enum FieldTypeEnum {
    INPUT("input"),
    MULTIINPUT("multiInput"),
    SINGLECHECK("singleCheck"),
    MULTICHECK("multiCheck"),
    COMPLEX("complex"),
    MULTICOMPLEX("multiComplex"),
    LABEL("label");

    private final String type;

    public static Field createField(FieldTypeEnum fieldEnum) {
        Object field = null;
        switch(fieldEnum) {
            case INPUT:
                field = new InputField();
                break;
            case MULTIINPUT:
                field = new MultiInputField();
                break;
            case SINGLECHECK:
                field = new SingleCheckField();
                break;
            case MULTICHECK:
                field = new MultiCheckField();
                break;
            case COMPLEX:
                field = new ComplexField();
                break;
            case MULTICOMPLEX:
                field = new MultiComplexField();
                break;
            case LABEL:
                field = new LabelField();
        }

        return (Field)field;
    }

    private FieldTypeEnum(String type) {
        this.type = type;
    }

    public String toString() {
        return this.type;
    }

    public String value() {
        return this.type;
    }

    public static FieldTypeEnum getEnum(String name) {
        FieldTypeEnum[] values = values();
        FieldTypeEnum[] arr$ = values;
        int len$ = values.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            FieldTypeEnum value = arr$[i$];
            if(value.value().equalsIgnoreCase(name)) {
                return value;
            }
        }

        return null;
    }
}
