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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Element;

public class MultiComplexField extends Field {
    protected List<ComplexValue> values = new ArrayList();
    protected List<com.voyageone.common.masterdate.schema.field.Field> fields = new ArrayList();

    public MultiComplexField() {
        super.type = FieldTypeEnum.MULTICOMPLEX;
    }

    public com.voyageone.common.masterdate.schema.field.Field addField(FieldTypeEnum fieldEnum) {
        com.voyageone.common.masterdate.schema.field.Field field = SchemaFactory.createField(fieldEnum);
        this.add(field);
        return field;
    }

    public void add(com.voyageone.common.masterdate.schema.field.Field field) {
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

    public List<com.voyageone.common.masterdate.schema.field.Field> getFieldList() {
        return this.fields;
    }

    public Map<String, com.voyageone.common.masterdate.schema.field.Field> getFieldMap() {
        HashMap map = new HashMap();
        Iterator i$ = this.fields.iterator();

        while(i$.hasNext()) {
            com.voyageone.common.masterdate.schema.field.Field field = (com.voyageone.common.masterdate.schema.field.Field)i$.next();
            map.put(field.getId(), field);
        }

        return map;
    }

    public Element toElement() throws TopSchemaException {
        Element fieldNode = super.toElement();
        Element fieldsNode = XmlUtils.appendElement(fieldNode, "fields");
        if(this.fields != null && !this.fields.isEmpty()) {
            Iterator i$ = this.fields.iterator();

            while(i$.hasNext()) {
                com.voyageone.common.masterdate.schema.field.Field field = (com.voyageone.common.masterdate.schema.field.Field)i$.next();
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
                Iterator i$ = this.values.iterator();

                while(i$.hasNext()) {
                    ComplexValue complexValue = (ComplexValue)i$.next();
                    Element complexValuesNode = XmlUtils.appendElement(fieldNode, "complex-values");
                    ComplexValue cValue = complexValue;
                    Iterator i$1 = complexValue.getFieldKeySet().iterator();

                    while(i$1.hasNext()) {
                        String keyFieldId = (String)i$1.next();
                        com.voyageone.common.masterdate.schema.field.Field field = cValue.getValueField(keyFieldId);
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
            Iterator i$ = multiComplexField.getComplexValues().iterator();

            while(i$.hasNext()) {
                ComplexValue complexValue = (ComplexValue)i$.next();
                Element complexValuesNode = XmlUtils.appendElement(fieldNode, "default-complex-values");
                ComplexValue cValue = complexValue;
                Iterator i$1 = complexValue.getFieldKeySet().iterator();

                while(i$1.hasNext()) {
                    String keyFieldId = (String)i$1.next();
                    com.voyageone.common.masterdate.schema.field.Field field = cValue.getValueField(keyFieldId);
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
}
