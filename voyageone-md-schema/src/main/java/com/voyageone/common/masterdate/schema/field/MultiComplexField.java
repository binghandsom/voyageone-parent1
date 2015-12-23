package com.voyageone.common.masterdate.schema.field;

import com.voyageone.common.masterdate.schema.Util.StringUtil;
import com.voyageone.common.masterdate.schema.Util.XmlUtils;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.enums.TopSchemaErrorCodeEnum;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaFactory;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.voyageone.common.masterdate.schema.value.Value;
import org.dom4j.Element;

public class MultiComplexField extends Field {
    protected List<ComplexValue> values = new ArrayList<>();
    protected List<Field> fields = new ArrayList<>();

    public MultiComplexField() {
        super.type = FieldTypeEnum.MULTICOMPLEX;
    }

    public Field addField(FieldTypeEnum fieldEnum) {
        Field field = SchemaFactory.createField(fieldEnum);
        this.add(field);
        return field;
    }

    public void add(Field field) {
        if(field != null) {
            this.fields.add(field);
        }
    }

    public void setComplexValues(List<ComplexValue> values) {
        if(values != null) {
            this.values = values;
        }
    }

    public ComplexValue addComplexValue() {
        ComplexValue complexValue = new ComplexValue();
        this.values.add(complexValue);
        return complexValue;
    }

    public ComplexValue addDefaultComplexValue() {
        if(super.defaultValueField == null) {
            this.initDefaultField();
        }

        ComplexValue complexValue = new ComplexValue();
        this.getDefaultComplexValues().add(complexValue);
        return complexValue;
    }

    public List<ComplexValue> getComplexValues() {
        return this.values;
    }

    public List<ComplexValue> getDefaultComplexValues() {
        if(super.defaultValueField == null) {
            this.initDefaultField();
        }

        MultiComplexField multiComplexField = (MultiComplexField)super.defaultValueField;
        return multiComplexField.getComplexValues();
    }

    public void addComplexValue(ComplexValue value) {
        if(value != null) {
            this.values.add(value);
        }
    }

    public void addDefaultComplexValue(ComplexValue value) {
        if(value != null) {
            if(super.defaultValueField == null) {
                this.initDefaultField();
            }

            this.getDefaultComplexValues().add(value);
        }
    }

    public List<Field> getFieldList() {
        return this.fields;
    }

    public Map<String, Field> getFieldMap() {
        Map<String, Field> map = new HashMap<>();

        for (Field field : this.fields) {
            map.put(field.getId(), field);
        }

        return map;
    }

    public Element toElement() throws TopSchemaException {
        Element fieldNode = super.toElement();
        Element fieldsNode = XmlUtils.appendElement(fieldNode, "fields");
        if(this.fields != null && !this.fields.isEmpty()) {

            for (Field field : this.fields) {
                Element fNode = field.toElement();
                XmlUtils.appendElement(fieldsNode, fNode);
            }
        }

        return fieldNode;
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

                for (ComplexValue complexValue : this.values) {
                    Element complexValuesNode = XmlUtils.appendElement(fieldNode, "complex-values");
                    for (String keyFieldId : complexValue.getFieldKeySet()) {
                        Field field = complexValue.getValueField(keyFieldId);
                        Element valueNode = field.toParamElement();
                        XmlUtils.appendElement(complexValuesNode, valueNode);
                    }
                }

                return fieldNode;
            }
        } else {
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30002, this.id);
        }
    }

    public Element toDefaultValueElement() throws TopSchemaException {
        if(this.defaultValueField == null) {
            return null;
        } else {
            Element fieldNode = XmlUtils.createRootElement("default-values");
            MultiComplexField multiComplexField = (MultiComplexField)this.defaultValueField;

            for (ComplexValue complexValue : multiComplexField.getComplexValues()) {
                Element complexValuesNode = XmlUtils.appendElement(fieldNode, "default-complex-values");
                for (String keyFieldId : complexValue.getFieldKeySet()) {
                    Field field = complexValue.getValueField(keyFieldId);
                    Element valueNode = field.toParamElement();
                    XmlUtils.appendElement(complexValuesNode, valueNode);
                }
            }

            return fieldNode;
        }
    }

    public void initDefaultField() {
        super.defaultValueField = SchemaFactory.createField(FieldTypeEnum.MULTICOMPLEX);
    }

    @Override
    public List<ComplexValue> getValue() {
        return this.values;
    }
}
