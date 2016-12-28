package com.voyageone.common.masterdate.schema.field;

import com.voyageone.common.masterdate.schema.enums.FieldValueTypeEnum;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.masterdate.schema.utils.XmlUtils;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.enums.TopSchemaErrorCodeEnum;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaFactory;
import org.dom4j.Element;

import java.util.Map;

public class InputField extends Field {
    protected String value;

    public InputField() {
        super.type = FieldTypeEnum.INPUT;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public String getDefaultValue() {
        if(super.defaultValueField == null) {
            this.initDefaultField();
        }

        InputField defaultField = (InputField)super.defaultValueField;
        return defaultField.getValue();
    }

    public void setDefaultValue(String value) {
        if(super.defaultValueField == null) {
            this.initDefaultField();
        }

        InputField defaultField = (InputField)super.defaultValueField;
        defaultField.setValue(value);
    }

    public Element toParamElement() throws TopSchemaException {
        Element fieldNode = XmlUtils.createRootElement("field");
        if(StringUtil.isEmpty(this.id)) {
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30001, null);
        } else if(this.type != null && !StringUtil.isEmpty(this.type.value())) {
            FieldTypeEnum fieldEnum = FieldTypeEnum.getEnum(this.type.value());
            if(fieldEnum == null) {
                throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30003, this.id);
            } else {
                fieldNode.addAttribute("id", this.id);
                fieldNode.addAttribute("name", this.name);
                fieldNode.addAttribute("type", this.type.value());
                Element valueNode = XmlUtils.appendElement(fieldNode, "value");
                if(!StringUtil.isEmpty(this.value)) {
                    valueNode.setText(this.value);
                }

                return fieldNode;
            }
        } else {
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30002, this.id);
        }
    }

    public Element toDefaultValueElement() throws TopSchemaException {
        InputField defaultField = (InputField)super.defaultValueField;
        Element valueNode = XmlUtils.createRootElement("default-value");
        String dvalue = defaultField.getValue();
        if(StringUtil.isEmpty(dvalue)) {
            return null;
        } else {
            valueNode.setText(dvalue);
            return valueNode;
        }
    }

    public void initDefaultField() {
        super.defaultValueField = SchemaFactory.createField(FieldTypeEnum.INPUT);
    }

    @Override
    public void setFieldValueFromMap(Map<String, Object> valueMap) {
        setValue(getFieldValueFromMap(valueMap));
    }

    @Override
    public String getFieldValueFromMap(Map<String, Object> valueMap) {
        return StringUtil.getStringValue(valueMap.get(id));
    }

    @Override
    public void getFieldValueToMap(Map<String,Object> valueMap) {
        valueMap.put(id, getValue(value, fieldValueType));
    }

    public static Object getValue(String value, FieldValueTypeEnum fieldValueType) {
        Object result = null;
        if (value == null) {
            return null;
        }
        if (fieldValueType == null) {
            return value;
        }
        switch(fieldValueType) {
            case NONE:
                result = value;
                break;
            case INT:
                if (value.length() > 0) {
                    result = value.indexOf(".")==-1?new Integer(value):new Double(value).intValue();
                }
                break;
            case DOUBLE:
                if (value.length() > 0) {
                    result = new Double(value);
                }
                break;
            default:
                result = value;
                break;
        }
        return result;
    }
}
