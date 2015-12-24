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

import java.util.*;

public class ComplexValue {
    protected Map<String, Field> fieldMap = new LinkedHashMap<>();
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

    public List<Value> getMultiCheckFieldValues(String fieldId) {
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

    public List<ComplexValue> getMultiComplexFieldValues(String fieldId) {
        Field field = (Field)this.fieldMap.get(fieldId);
        if(field != null && field.getType().equals(FieldTypeEnum.MULTICOMPLEX)) {
            MultiComplexField cfield = (MultiComplexField)field;
            return cfield.getComplexValues();
        } else {
            return null;
        }
    }

    public void setFieldValue(String fieldId, Object value, FieldTypeEnum fieldType) {
        if (value == null) {
            return;
        }

        List valueList = null;
        switch (fieldType){
            case INPUT:
                setInputFieldValue(fieldId, (String)value);
                break;
            case MULTIINPUT:
                valueList = (List)value;
                List<String> objValuesStr = new ArrayList<>();
                for (Object cellValue : valueList) {
                    if (cellValue instanceof String) {
                        objValuesStr.add((String)cellValue);
                    }
                }
                setMultiInputFieldValues(fieldId, objValuesStr);
                break;
            case SINGLECHECK:
                if (value instanceof String) {
                    setSingleCheckFieldValue(fieldId, new Value((String)value));
                } else if (value instanceof Value) {
                    setSingleCheckFieldValue(fieldId, (Value)value);
                }
                break;
            case MULTICHECK: {
                valueList = (List)value;
                List<Value> objValuesValue = new ArrayList<>();
                for (Object cellValue : valueList) {
                    if (cellValue instanceof String) {
                        objValuesValue.add(new Value((String)cellValue));
                    } else if (cellValue instanceof Value) {
                        objValuesValue.add((Value)cellValue);
                    }
                }
                setMultiCheckFieldValues(fieldId, objValuesValue);
                break;
            }
            case COMPLEX: {
                ComplexValue complexValue = (ComplexValue)value;
                setComplexFieldValue(fieldId, complexValue);
                break;
            }
            case MULTICOMPLEX: {
                List<ComplexValue> complexValues = new ArrayList<>();
                valueList = (List)value;
                for (Object cellValue : valueList) {
                    if (cellValue instanceof ComplexValue) {
                        complexValues.add((ComplexValue) cellValue);
                    }
                }
                setMultiComplexFieldValues(fieldId, complexValues);
                break;
            }
        }
    }


    public Object getFieldValue(String fieldId, FieldTypeEnum fieldType) {
        Object result = null;

        switch (fieldType){
            case INPUT:
                result = getInputFieldValue(fieldId);
                break;
            case MULTIINPUT:
                result = convertEmptyList(getMultiInputFieldValues(fieldId));
                break;
            case SINGLECHECK:
                Value valueSingleChec = getSingleCheckFieldValue(fieldId);
                if (valueSingleChec != null) {
                    result = valueSingleChec.getValue();
                }
                break;
            case MULTICHECK: {
                List<String> objValuesStr = new ArrayList<>();
                List<Value> valueList = getMultiCheckFieldValues(fieldId);
                if (valueList != null) {
                    for (Value cellValue : valueList) {
                        objValuesStr.add(cellValue.getValue() != null ? cellValue.getValue() : "");
                    }
                }
                result = objValuesStr;
                break;
            }
            case COMPLEX: {
                ComplexValue subComplexValue = getComplexFieldValue(fieldId);
                result = getFieldComplexSubMap(subComplexValue);
                break;
            }
            case MULTICOMPLEX: {
                List<ComplexValue> subComplexValues = getMultiComplexFieldValues(fieldId);
                List<Map<String, Object>> valueMapList = new ArrayList<>();
                for (ComplexValue subComplexValue : subComplexValues) {
                    valueMapList.add(getFieldComplexSubMap(subComplexValue));
                }
                result = valueMapList;
                break;
            }
            case LABEL:
                break;
        }

        return result;
    }

    private Map<String, Object> getFieldComplexSubMap(ComplexValue complexValue) {
        Map<String, Object> subvalueMapCell = new LinkedHashMap<>();
        Map<String, Field> fieldMap = complexValue.getFieldMap();
        if (fieldMap != null) {
            for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
                if (entry.getValue() != null) {
                    entry.getValue().getFieldValueToMap(subvalueMapCell);
                }
            }
        }
        return subvalueMapCell;
    }

    private List<String> convertEmptyList(List<String> inputList) {
        List<String> result = new ArrayList<>();
        if (inputList != null) {
            for (String cell : inputList) {
                result.add(cell != null ? cell:"");
            }
        }
        return result;
    }
}
