package com.voyageone.common.masterdate.schema.field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.masterdate.schema.utils.XmlUtils;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.enums.TopSchemaErrorCodeEnum;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaFactory;
import com.voyageone.common.masterdate.schema.value.ComplexValue;

import java.util.*;

import org.dom4j.Element;

public class MultiComplexField extends Field {
    protected List<ComplexValue> values = new ArrayList<>();
    protected List<Field> fields = new ArrayList<>();
    // added by morse.lu 2016/06/21 start
    private Map<String, Field> mapField = null;
    // added by morse.lu 2016/06/21 end

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
            // added by morse.lu 2016/06/21 start
            if (mapField != null) {
                mapField.putIfAbsent(field.getId(), field);
            }
            // added by morse.lu 2016/06/21 end
        }
    }

    // added by morse.lu 2016/06/22 start
    public void clear() {
        values = new ArrayList<>();
        fields = new ArrayList<>();
        mapField = null;
    }

    public void clearMapField() {
        mapField = null;
    }
    // added by morse.lu 2016/06/22 end

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
    @JsonIgnore
    public List<ComplexValue> getValue() {
        return this.values;
    }

    @Override
    public void setFieldValueFromMap(Map<String, Object> valueMap) {
        List<ComplexValue> value = getFieldValueFromMap(valueMap);
        if (value != null) {
            setComplexValues(value);
        }
    }

    @Override
    public List<ComplexValue> getFieldValueFromMap(Map<String, Object> valueMap) {
        List<ComplexValue> result = null;

        Object valueObj = valueMap.get(id);
        if (valueObj != null && valueObj instanceof List) {
            result = new ArrayList<>();

            List valueObjList = (List)valueObj;
            for (Object valueCellObjTmp : valueObjList) {
                if (valueCellObjTmp != null && valueCellObjTmp instanceof Map) {
                    Map<String, Object> valueCellMap = new LinkedHashMap<>();

                    Map valueCellObjMap = (Map) valueCellObjTmp;
                    for (Object valueTmp : valueCellObjMap.entrySet()) {
                        Map.Entry entry = (Map.Entry) valueTmp;
                        valueCellMap.put(entry.getKey().toString(), entry.getValue());
                    }

                    ComplexValue complexValue = new ComplexValue();
                    if (fields != null && fields.size() > 0) {
                        for (Field field : fields) {
                            Object value = field.getFieldValueFromMap(valueCellMap);
                            complexValue.setFieldValue(field.getId(), value, field.getType());
                        }
                    }
                    result.add(complexValue);
                }
            }
        }
        return result;
    }

    @Override
    public void getFieldValueToMap(Map<String,Object> valueMap) {
        if (values != null && values.size() > 0
                && fields != null && fields.size() > 0) {
            if (!valueMap.containsKey(id)) {
                valueMap.put(id, new ArrayList());
            }
            List currObj = (List)valueMap.get(id);
            for (ComplexValue complexValue : values) {
                Map<String, Object> valueMapCell = new LinkedHashMap<>();

                for (Field field : fields) {
                    if (complexValue != null) {
                        Object subValue = complexValue.getFieldValue(field.id, field.getType(), field.getFieldValueType());
                        if (subValue != null) {
                            valueMapCell.put(field.id, subValue);
                        }
                    }
                }

                currObj.add(valueMapCell);
            }
        }
    }
}
