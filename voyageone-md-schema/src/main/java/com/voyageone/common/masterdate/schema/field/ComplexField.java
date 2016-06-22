package com.voyageone.common.masterdate.schema.field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.masterdate.schema.utils.XmlUtils;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.enums.TopSchemaErrorCodeEnum;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaFactory;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import org.dom4j.Element;

import java.util.*;

public class ComplexField extends Field {
    protected ComplexValue complexValue = new ComplexValue();
    protected List<Field> fields = new ArrayList<>();
    // added by morse.lu 2016/06/21 start
    private Map<String, Field> mapField = null;
    // added by morse.lu 2016/06/21 end

    public ComplexField() {
        super.type = FieldTypeEnum.COMPLEX;
    }

    public void initDefaultField() {
        super.defaultValueField = SchemaFactory.createField(FieldTypeEnum.COMPLEX);
    }

    public Field addField(FieldTypeEnum fieldEnum) {
        Field field = SchemaFactory.createField(fieldEnum);
        this.add(field);
        return field;
    }

    public void add(Field field) {
        this.fields.add(field);
        // added by morse.lu 2016/06/21 start
        if (mapField != null) {
            mapField.putIfAbsent(field.getId(), field);
        }
        // added by morse.lu 2016/06/21 end
    }

    // added by morse.lu 2016/06/22 start
    public void clear() {
        complexValue = new ComplexValue();
        fields = new ArrayList<>();
        mapField = null;
    }
    // added by morse.lu 2016/06/22 end

    public ComplexValue getComplexValue() {
        return this.complexValue;
    }

    public ComplexValue getDefaultComplexValue() {
        if(this.defaultValueField == null) {
            this.initDefaultField();
        }

        ComplexField complexField = (ComplexField)this.defaultValueField;
        return complexField.getComplexValue();
    }

    public void setComplexValue(ComplexValue complexValue) {
        if(complexValue != null) {
            this.complexValue = complexValue;
        }
    }

    public void setDefaultValue(ComplexValue complexValue) {
        if(this.defaultValueField == null) {
            this.initDefaultField();
        }

        ComplexField complexField = (ComplexField)this.defaultValueField;
        complexField.setComplexValue(complexValue);
    }

    public List<Field> getFields() {
        return this.fields;
    }

    @JsonIgnore
    public Map<String, Field> getFieldMap() {
        // modified by morse.lu 2016/06/21 start
        // 性能优化
//        Map<String, Field> map = new HashMap<>();
//
//        for (Field field : this.fields) {
//            map.put(field.getId(), field);
//        }
//
//        return map;
        if (mapField == null) {
            mapField = new HashMap<>();
            for (Field field : this.fields) {
                mapField.put(field.getId(), field);
            }
        }

        return mapField;
        // modified by morse.lu 2016/06/21 end
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
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30001, null);
        } else if(this.type != null && !StringUtil.isEmpty(this.type.value())) {
            FieldTypeEnum fieldEnum = FieldTypeEnum.getEnum(this.type.value());
            if(fieldEnum == null) {
                throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30003, this.id);
            } else {
                fieldNode.addAttribute("id", this.id);
                fieldNode.addAttribute("name", this.name);
                fieldNode.addAttribute("type", this.type.value());
                Element complexValuesNode = XmlUtils.appendElement(fieldNode, "complex-values");
                ComplexValue cValue = this.complexValue;

                for (String keyFieldId : cValue.getFieldKeySet()) {
                    Field field = cValue.getValueField(keyFieldId);
                    Element valueNode = field.toParamElement();
                    XmlUtils.appendElement(complexValuesNode, valueNode);
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
            ComplexField complexField = (ComplexField)this.defaultValueField;
            Element defaultComplexValuesNode = XmlUtils.createRootElement("default-complex-values");
            ComplexValue cValue = complexField.getComplexValue();

            for (String keyFieldId : cValue.getFieldKeySet()) {
                Field field = cValue.getValueField(keyFieldId);
                Element valueNode = field.toParamElement();
                XmlUtils.appendElement(defaultComplexValuesNode, valueNode);
            }

            return defaultComplexValuesNode;
        }
    }

    @Override
    @JsonIgnore
    public ComplexValue getValue() {
        return this.complexValue;
    }

    @Override
    public void setFieldValueFromMap(Map<String, Object> valueMap) {
        ComplexValue value = getFieldValueFromMap(valueMap);
        if (value != null) {
            setComplexValue(value);
        }
    }

    @Override
    public ComplexValue getFieldValueFromMap(Map<String, Object> valueMap) {
        ComplexValue result = null;

        Object valueObj = valueMap.get(id);
        if (valueObj != null && valueObj instanceof Map) {
            Map<String, Object> values = new LinkedHashMap<>();

            Map valuesTmp = (Map)valueObj;
            for (Object valueTmp : valuesTmp.entrySet()) {
                Map.Entry entry = (Map.Entry)valueTmp;
                values.put(entry.getKey().toString(), entry.getValue());
            }

            result = new ComplexValue();

            if (fields != null && fields.size() > 0) {
                for (Field field : fields) {
                    Object value = field.getFieldValueFromMap(values);
                    result.setFieldValue(field.getId(), value, field.getType());
                }
            }
        }

        return result;
    }

    @Override
    public void getFieldValueToMap(Map<String,Object> valueMap) {
        if (complexValue != null && fields != null && fields.size() > 0) {
            if (!valueMap.containsKey(id)) {
                valueMap.put(id, new LinkedHashMap());
            }
            Map currObj = (Map)valueMap.get(id);
            for (Field field : fields) {
                Object subValue = complexValue.getFieldValue(field.id, field.getType(), field.getFieldValueType());
                if (subValue != null) {
                    currObj.put(field.id, subValue);
                }
            }
        }
    }
}
