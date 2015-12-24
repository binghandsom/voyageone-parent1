package com.voyageone.common.masterdate.schema.value;

import com.voyageone.common.masterdate.schema.util.ISPUtil;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.ComplexField;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.masterdate.schema.field.MultiCheckField;
import com.voyageone.common.masterdate.schema.field.MultiComplexField;
import com.voyageone.common.masterdate.schema.field.MultiInputField;
import com.voyageone.common.masterdate.schema.field.SingleCheckField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ComplexValue {
    protected Map<String, Field> fieldMap = new HashMap();
    public Map<String, Field> getFieldMap() {
        return fieldMap;
    }
    public void setFieldMap(Map<String, Field> fieldMap) {
        this.fieldMap = fieldMap;
    }


    public ComplexValue() {
    }

    public Set<String> getFieldKeySet() {
        return this.fieldMap.keySet();
    }

    public Field getValueField(String fieldId) {
        return (Field)this.fieldMap.get(fieldId);
    }

    public void put(Field field) {
        if(field != null && field.getId() != null) {
            this.fieldMap.put(field.getId(), field);
        }
    }

    public Value getValue(String fieldId) {
        Field field = (Field)this.fieldMap.get(fieldId);
        return field == null?null:ISPUtil.getFieldValue(field);
    }

    public String getStringValue(String fieldId) {
        Field field = (Field)this.fieldMap.get(fieldId);
        return field == null?null:ISPUtil.getFieldStringValue(field);
    }

    public void setInputFieldValue(String fieldId, String value) {
        InputField field = new InputField();
        field.setId(fieldId);
        field.setValue(value);
        this.fieldMap.put(fieldId, field);
    }

    public String getInputFieldValue(String fieldId) {
        Field field = (Field)this.fieldMap.get(fieldId);
        return field != null && field.getType().equals(FieldTypeEnum.INPUT)?ISPUtil.getFieldStringValue(field):null;
    }

    public void setSingleCheckFieldValue(String fieldId, Value value) {
        SingleCheckField field = new SingleCheckField();
        field.setId(fieldId);
        field.setValue(value);
        this.fieldMap.put(fieldId, field);
    }

    public Value getSingleCheckFieldValue(String fieldId) {
        Field field = (Field)this.fieldMap.get(fieldId);
        return field != null && field.getType().equals(FieldTypeEnum.SINGLECHECK)?ISPUtil.getFieldValue(field):null;
    }

    public void setMultiInputFieldValues(String fieldId, List<String> values) {
        MultiInputField field = new MultiInputField();
        field.setId(fieldId);
        field.setValues(values);
        this.fieldMap.put(fieldId, field);
    }

    public List<String> getMultiInputFieldValues(String fieldId) {
        Field field = (Field)this.fieldMap.get(fieldId);
        return field != null && field.getType().equals(FieldTypeEnum.MULTIINPUT)?ISPUtil.getFieldStringValues(field):null;
    }

    public void setMultiCheckFieldValues(String fieldId, List<Value> values) {
        MultiCheckField field = new MultiCheckField();
        field.setId(fieldId);
        field.setValues(values);
        this.fieldMap.put(fieldId, field);
    }

    public List<Value> getMultiCheckValues(String fieldId) {
        Field field = (Field)this.fieldMap.get(fieldId);
        return field != null && field.getType().equals(FieldTypeEnum.MULTICHECK)?ISPUtil.getFieldValues(field):null;
    }

    public void setComplexFieldValue(String fieldId, ComplexValue complexValue) {
        ComplexField field = new ComplexField();
        field.setId(fieldId);
        field.setComplexValue(complexValue);
        this.fieldMap.put(fieldId, field);
    }

    public ComplexValue getComplexFieldValue(String fieldId) {
        Field field = (Field)this.fieldMap.get(fieldId);
        if(field != null && field.getType().equals(FieldTypeEnum.COMPLEX)) {
            ComplexField cfield = (ComplexField)field;
            return cfield.getComplexValue();
        } else {
            return null;
        }
    }

    public void setMultiComplexFieldValues(String fieldId, List<ComplexValue> complexValueList) {
        MultiComplexField field = new MultiComplexField();
        field.setId(fieldId);
        field.setComplexValues(complexValueList);
        this.fieldMap.put(fieldId, field);
    }

    public List<ComplexValue> getComplexFieldValues(String fieldId) {
        Field field = (Field)this.fieldMap.get(fieldId);
        if(field != null && field.getType().equals(FieldTypeEnum.MULTICOMPLEX)) {
            MultiComplexField cfield = (MultiComplexField)field;
            return cfield.getComplexValues();
        } else {
            return null;
        }
    }
}
