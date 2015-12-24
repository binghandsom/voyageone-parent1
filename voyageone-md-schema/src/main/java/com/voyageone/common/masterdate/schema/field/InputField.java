package com.voyageone.common.masterdate.schema.field;

import com.voyageone.common.masterdate.schema.util.StringUtil;
import com.voyageone.common.masterdate.schema.util.XmlUtils;
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
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30001, (String)null);
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
        Object valueObj = valueMap.get(id);
        if (valueObj != null) {
            setValue(valueObj.toString());
        }
    }
}
