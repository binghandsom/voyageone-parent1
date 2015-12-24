package com.voyageone.common.masterdate.schema.field;

import com.voyageone.common.masterdate.schema.util.StringUtil;
import com.voyageone.common.masterdate.schema.util.XmlUtils;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.enums.TopSchemaErrorCodeEnum;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaFactory;
import com.voyageone.common.masterdate.schema.value.Value;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

public class MultiCheckField extends OptionsField {
    protected List<Value> values = new ArrayList<>();

    public MultiCheckField() {
        super.type = FieldTypeEnum.MULTICHECK;
    }

    public void addValue(Value value) {
        if(value != null) {
            this.values.add(value);
        }
    }

    public void addValue(String value) {
        if(value != null) {
            Value v = new Value();
            v.setValue(value);
            this.addValue(v);
        }
    }

    public List<Value> getValues() {
        return this.values;
    }

    public void setValues(List<Value> values) {
        if(values != null) {
            this.values = values;
        }
    }

    public void addDefaultValueDO(Value value) {
        if(value != null) {
            if(super.defaultValueField == null) {
                this.initDefaultField();
            }

            MultiCheckField defaultField = (MultiCheckField)super.defaultValueField;
            defaultField.addValue(value);
        }
    }

    public void setDefaultValueDO(List<Value> values) {
        if(values != null) {
            if(super.defaultValueField == null) {
                this.initDefaultField();
            }

            MultiCheckField defaultField = (MultiCheckField)super.defaultValueField;
            if(defaultField.getValues() != null && !defaultField.getValues().isEmpty()) {
                defaultField.getValues().clear();
            }

            for (Value v : values) {
                defaultField.addValue(v);
            }

        }
    }

    public List<Value> getDefaultValuesDO() {
        if(super.defaultValueField == null) {
            this.initDefaultField();
        }

        MultiCheckField defaultField = (MultiCheckField)super.defaultValueField;
        return defaultField.getValues();
    }

    public void addDefaultValue(String value) {
        if(value != null) {
            if(super.defaultValueField == null) {
                this.initDefaultField();
            }

            MultiCheckField defaultField = (MultiCheckField)super.defaultValueField;
            defaultField.addValue(value);
        }
    }

    public void setDefaultValue(List<String> values) {
        if(values != null) {
            if(super.defaultValueField == null) {
                this.initDefaultField();
            }

            MultiCheckField defaultField = (MultiCheckField)super.defaultValueField;
            if(defaultField.getValues() != null && !defaultField.getValues().isEmpty()) {
                defaultField.getValues().clear();
            }

            for (String v : values) {
                defaultField.addValue(v);
            }

        }
    }

    public List<String> getDefaultValues() {
        List<String> result = new ArrayList<>();
        if(super.defaultValueField == null) {
            this.initDefaultField();
        }

        MultiCheckField defaultField = (MultiCheckField)super.defaultValueField;
        List<Value> vList = defaultField.getValues();

        for (Value aVList : vList) {
            result.add(aVList.getValue());
        }

        return result;
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
                Element valuesNode = XmlUtils.appendElement(fieldNode, "values");

                for (Value value : this.values) {
                    Element valueNode = XmlUtils.appendElement(valuesNode, "value");
                    if (!StringUtil.isEmpty(value.getValue())) {
                        valueNode.setText(value.getValue());
                    }
                }

                return fieldNode;
            }
        } else {
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30002, this.id);
        }
    }

    public Element toDefaultValueElement() throws TopSchemaException {
        MultiCheckField defaultField = (MultiCheckField)super.defaultValueField;
        Element valuesNode = XmlUtils.createRootElement("default-values");
        List<Value> defaultValues = defaultField.getValues();
        if(defaultValues != null && !defaultValues.isEmpty()) {

            for (Value defaultValue : defaultValues) {
                Element valueNode = XmlUtils.appendElement(valuesNode, "default-value");
                if (!StringUtil.isEmpty(defaultValue.getValue())) {
                    valueNode.setText(defaultValue.getValue());
                }
            }

            return valuesNode;
        } else {
            return null;
        }
    }

    public void initDefaultField() {
        super.defaultValueField = SchemaFactory.createField(FieldTypeEnum.MULTICHECK);
    }

    @Override
    public List<Value> getValue() {
        return this.values;
    }

    @Override
    public void setFieldValueFromMap(Map<String, Object> valueMap) {
        List<Value> values = getFieldValueFromMap(valueMap);
        setValues(values);
    }

    @Override
    public List<Value> getFieldValueFromMap(Map<String, Object> valueMap) {
        List<Value> values = new ArrayList<>();
        Object valueObj = valueMap.get(id);
        if (valueObj != null && valueObj instanceof List) {
            List valuesTmp = (List)valueObj;
            for (Object value : valuesTmp) {
                values.add(new Value(value != null?(String)value:""));
            }
        }
        return values;
    }

    @Override
    public void getFieldValueToMap(Map<String,Object> valueMap) {
        if (values != null) {
            List<String> valueStrList = new ArrayList<>();
            for (Value valueObj : values) {
                valueStrList.add(valueObj != null && valueObj.getValue()!=null?valueObj.getValue():"");
            }
            valueMap.put(id, valueStrList);
        }
    }
}
