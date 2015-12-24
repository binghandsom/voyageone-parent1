package com.voyageone.common.masterdate.schema.field;

import com.voyageone.common.masterdate.schema.util.StringUtil;
import com.voyageone.common.masterdate.schema.util.XmlUtils;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.enums.TopSchemaErrorCodeEnum;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaFactory;
import com.voyageone.common.masterdate.schema.value.Value;
import org.dom4j.Element;

import java.util.Map;

public class SingleCheckField extends OptionsField {
    protected Value value = new Value();

    public SingleCheckField() {
        super.type = FieldTypeEnum.SINGLECHECK;
    }

    @Override
    public Value getValue() {
        return this.value;
    }

    public void setValue(String value) {
        if(value != null) {
            this.value.setValue(value);
        }
    }

    public void setValue(Value value) {
        if(value != null) {
            this.value = value;
        }
    }

    public String getDefaultValue() {
        if(super.defaultValueField == null) {
            this.initDefaultField();
        }

        SingleCheckField defaultField = (SingleCheckField)super.defaultValueField;
        return defaultField.getValue() == null?null:defaultField.getValue().getValue();
    }

    public void setDefaultValue(String value) {
        if(value != null) {
            if(super.defaultValueField == null) {
                this.initDefaultField();
            }

            SingleCheckField defaultField = (SingleCheckField)super.defaultValueField;
            defaultField.setValue(value);
        }
    }

    public Value getDefaultValueDO() {
        if(super.defaultValueField == null) {
            return null;
        } else {
            SingleCheckField defaultField = (SingleCheckField)super.defaultValueField;
            return defaultField.getValue();
        }
    }

    public void setDefaultValueDO(Value value) {
        if(value != null) {
            if(super.defaultValueField == null) {
                this.initDefaultField();
            }

            SingleCheckField defaultField = (SingleCheckField)super.defaultValueField;
            defaultField.setValue(value);
        }
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
                if(!StringUtil.isEmpty(this.value.getValue())) {
                    valueNode.setText(this.value.getValue());
                }

                return fieldNode;
            }
        } else {
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30002, this.id);
        }
    }

    public Element toDefaultValueElement() throws TopSchemaException {
        SingleCheckField defaultField = (SingleCheckField)super.defaultValueField;
        Element valueNode = XmlUtils.createRootElement("default-value");
        Value dvalue = defaultField.getValue();
        if(dvalue != null && !StringUtil.isEmpty(dvalue.getValue())) {
            valueNode.setText(dvalue.getValue());
            return valueNode;
        } else {
            return null;
        }
    }

    public void initDefaultField() {
        super.defaultValueField = SchemaFactory.createField(FieldTypeEnum.SINGLECHECK);
    }

    @Override
    public void setFieldValueFromMap(Map<String, Object> valueMap) {
        Object valueObj = valueMap.get(id);
        if (valueObj != null) {
            setValue(valueObj.toString());
        }
    }
}
