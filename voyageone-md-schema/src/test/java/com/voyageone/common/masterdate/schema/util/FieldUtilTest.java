package com.voyageone.common.masterdate.schema.util;

import com.voyageone.common.masterdate.schema.depend.DependExpress;
import com.voyageone.common.masterdate.schema.depend.DependGroup;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;
import com.voyageone.common.masterdate.schema.factory.SchemaFactory;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReaderTest;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.label.Label;
import com.voyageone.common.masterdate.schema.label.LabelGroup;
import com.voyageone.common.masterdate.schema.property.Property;
import com.voyageone.common.masterdate.schema.rule.MaxLengthRule;
import com.voyageone.common.masterdate.schema.rule.Rule;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.masterdate.schema.value.Value;
import org.junit.Test;

import java.util.*;

/**
 * Created by DELL on 2015/12/24.
 */
public class FieldUtilTest {

    @Test
    public void testSetFieldsValueFromMapInputField() throws Exception {
        List<Field> fieldsList = new ArrayList<>();
        InputField inputField = SchemaJsonReaderTest.createInputFiled("input1");
        fieldsList.add(inputField);
        Map<String, Object> ValueMap = new LinkedHashMap<>();
        ValueMap.put("input1", "input1_value1");

        FieldUtil.setFieldsValueFromMap(fieldsList, ValueMap);
        String newjsonStr = JsonUtil.bean2Json(fieldsList);
        System.out.println(newjsonStr);

        List<Field> fieldsList2 = new ArrayList<>();
        InputField inputField2 = SchemaJsonReaderTest.createInputFiled("input1");
        fieldsList2.add(inputField2);
        setFieldsValue(fieldsList2, ValueMap);
        String newjsonStr2 = JsonUtil.bean2Json(fieldsList2);
        System.out.println(newjsonStr2);

        if (newjsonStr.equals(newjsonStr2)) {
            System.out.println("equal");
        }
    }


    @Test
    public void testSetFieldsValueFromMapLabelField() throws Exception {
        List<Field> fieldsList = new ArrayList<>();
        LabelField field = createLableGroup("input1");
        fieldsList.add(field);
        Map<String, Object> ValueMap = new LinkedHashMap<>();
        ValueMap.put("input1", "input1_value1");

        FieldUtil.setFieldsValueFromMap(fieldsList, ValueMap);
        String newjsonStr = JsonUtil.bean2Json(fieldsList);
        System.out.println(newjsonStr);

        List<Field> fieldsList2 = new ArrayList<>();
        LabelField field2 = createLableGroup("input1");
        fieldsList2.add(field2);
        setFieldsValue(fieldsList2, ValueMap);
        String newjsonStr2 = JsonUtil.bean2Json(fieldsList2);
        System.out.println(newjsonStr2);

        if (newjsonStr.equals(newjsonStr2)) {
            System.out.println("equal");
        }
    }

    public LabelField createLableGroup(String id) {
        LabelField labelField = (LabelField) SchemaFactory.createField(FieldTypeEnum.LABEL);
        labelField.setId(id);
        labelField.setName("A11");
        //inputField.setFieldRequired();
        List<Property> properties = new ArrayList<>();
        properties.add(new Property("property_key1", "property_value1"));
        properties.add(new Property("property_key2", "property_value2"));
        labelField.setProperties(properties);

        List<Rule> rules = new ArrayList<>();

        RuleTypeEnum ruleEnum = RuleTypeEnum.getEnum("maxLengthRule");
        MaxLengthRule rule = (MaxLengthRule)SchemaFactory.createRule(ruleEnum);
        rule.setUnit("mm");
        rule.setValue("15");
        rule.setExProperty("MaxLengthRule ExProperty 1");

        DependGroup dg_result = new DependGroup();
        dg_result.setOperator("and");

        List<DependExpress> dependExpressList = new ArrayList<>();
        DependExpress dgSubEle = new DependExpress();
        dgSubEle.setFieldId("dependExpressId1");
        dgSubEle.setValue("dependExpressValue1");
        dgSubEle.setSymbol("deSymbol1");
        dependExpressList.add(dgSubEle);
        dgSubEle = new DependExpress();
        dgSubEle.setFieldId("dependExpressId2");
        dgSubEle.setValue("dependExpressValue2");
        dgSubEle.setSymbol("deSymbol2");
        dependExpressList.add(dgSubEle);
        dg_result.setDependExpressList(dependExpressList);

        List<DependGroup> dependGroupList = new ArrayList<>();

        dependGroupList.add(new DependGroup());


        dg_result.setDependGroupList(dependGroupList);

        rule.setDependGroup(dg_result);

        rules.add(rule);

        labelField.setRules(rules);

        LabelGroup lg_result = new LabelGroup();
        lg_result.setName("lg_result");

        Label subLabelGroupEle = new Label();
        subLabelGroupEle.setName("subGroup");
        subLabelGroupEle.setValue("labelValue");
        subLabelGroupEle.setDesc("labelDesc");
        lg_result.add(subLabelGroupEle);
        lg_result.add(subLabelGroupEle);

        lg_result.add(new LabelGroup());

        labelField.setLabelGroup(lg_result);
        return labelField;
    }


    @Test
    public void testSetFieldsValueFromMapSingleCheck() throws Exception {
        List<Field> fieldsList = new ArrayList<>();
        SingleCheckField field = SchemaJsonReaderTest.createSingleCheckField("SingleCheck1");
        fieldsList.add(field);
        Map<String, Object> ValueMap = new LinkedHashMap<>();
        ValueMap.put("SingleCheck1", "SingleCheck1_value1");

        FieldUtil.setFieldsValueFromMap(fieldsList, ValueMap);
        String newjsonStr = JsonUtil.bean2Json(fieldsList);
        System.out.println(newjsonStr);

        List<Field> fieldsList2 = new ArrayList<>();
        SingleCheckField field2 = SchemaJsonReaderTest.createSingleCheckField("SingleCheck1");
        fieldsList2.add(field2);
        setFieldsValue(fieldsList2, ValueMap);
        String newjsonStr2 = JsonUtil.bean2Json(fieldsList2);
        System.out.println(newjsonStr2);

        if (newjsonStr.equals(newjsonStr2)) {
            System.out.println("equal");
        }
    }


    @Test
    public void testSetFieldsValueFromMapMutiInput() throws Exception {
        List<Field> fieldsList = new ArrayList<>();
        MultiInputField field = SchemaJsonReaderTest.createMutiInputField("MutiInput1");
        fieldsList.add(field);
        Map<String, Object> ValueMap = new LinkedHashMap<>();
        List<String> values = new ArrayList<>();
        values.add("MutiInput1_value1");
        values.add("MutiInput1_value2");
        ValueMap.put("MutiInput1", values);

        FieldUtil.setFieldsValueFromMap(fieldsList, ValueMap);
        String newjsonStr = JsonUtil.bean2Json(fieldsList);
        System.out.println(newjsonStr);

        List<Field> fieldsList2 = new ArrayList<>();
        MultiInputField field2 = SchemaJsonReaderTest.createMutiInputField("MutiInput1");
        fieldsList2.add(field2);
        setFieldsValue(fieldsList2, ValueMap);
        String newjsonStr2 = JsonUtil.bean2Json(fieldsList2);
        System.out.println(newjsonStr2);

        if (newjsonStr.equals(newjsonStr2)) {
            System.out.println("equal");
        }
    }


    @Test
    public void testSetFieldsValueFromMapMutiCheck() throws Exception {
        List<Field> fieldsList = new ArrayList<>();
        MultiCheckField field = SchemaJsonReaderTest.createMultiCheckField("MultiCheck1");
        fieldsList.add(field);
        Map<String, Object> ValueMap = new LinkedHashMap<>();
        List<String> values = new ArrayList<>();
        values.add("MutiInput1_value1");
        values.add("MutiInput1_value2");
        ValueMap.put("MultiCheck1", values);

        FieldUtil.setFieldsValueFromMap(fieldsList, ValueMap);
        String newjsonStr = JsonUtil.bean2Json(fieldsList);
        System.out.println(newjsonStr);

        List<Field> fieldsList2 = new ArrayList<>();
        MultiCheckField field2 = SchemaJsonReaderTest.createMultiCheckField("MultiCheck1");
        fieldsList2.add(field2);
        setFieldsValue(fieldsList2, ValueMap);
        String newjsonStr2 = JsonUtil.bean2Json(fieldsList2);
        System.out.println(newjsonStr2);

        if (newjsonStr.equals(newjsonStr2)) {
            System.out.println("equal");
        }
    }

    @Test
    public void testSetFieldsValueFromMapComplexField() throws Exception {
        List<Field> fieldsList = new ArrayList<>();
        ComplexField field = SchemaJsonReaderTest.createComplexField("Complex1");
        fieldsList.add(field);

        Map<String, Object> values = new LinkedHashMap<>();
        values.put("InputFiled11", "InputFiled11_value1");
        values.put("SingleCheck1", "SingleCheck1_value1");

        List<String> multiCheckValues = new ArrayList<>();
        multiCheckValues.add("MutiInput1_value1");
        multiCheckValues.add("MutiInput1_value2");

        values.put("MultiCheck1", multiCheckValues);

        Map<String, Object> ValueMap = new LinkedHashMap<>();
        ValueMap.put("Complex1", values);

        FieldUtil.setFieldsValueFromMap(fieldsList, ValueMap);
        String newjsonStr = JsonUtil.bean2Json(fieldsList);
        System.out.println(newjsonStr);

        List<Field> fieldsList2 = new ArrayList<>();
        ComplexField field2 = SchemaJsonReaderTest.createComplexField("Complex1");
        fieldsList2.add(field2);
        setFieldsValue(fieldsList2, ValueMap);
        String newjsonStr2 = JsonUtil.bean2Json(fieldsList2);
        System.out.println(newjsonStr2);

        if (newjsonStr.equals(newjsonStr2)) {
            System.out.println("equal");
        }

        Map<String, Object> valuesMap = FieldUtil.getFieldsValueToMap(fieldsList);
        String newjsonStr3 = JsonUtil.bean2Json(valuesMap);
        System.out.println(newjsonStr3);

        String newjsonStr4 = JsonUtil.bean2Json(ValueMap);
        System.out.println(newjsonStr4);
        if (newjsonStr3.equals(newjsonStr4)) {
            System.out.println("equal");
        }
    }

    @Test
    public void testSetFieldsValueFromMapMultiComplexField() throws Exception {
        List<Field> fieldsList = new ArrayList<>();
        MultiComplexField field = SchemaJsonReaderTest.createMultiComplexField("MultiComplex1");
        fieldsList.add(field);

        List<Map<String, Object>> valuesList = new ArrayList<>();

        Map<String, Object> values = new LinkedHashMap<>();
        values.put("InputFiled2", "InputFiled11_value1");
        values.put("SingleCheck2", "SingleCheck1_value1");

        List<String> multiCheckValues = new ArrayList<>();
        multiCheckValues.add("MutiInput1_value1");
        multiCheckValues.add("MutiInput1_value2");
        values.put("MultiCheck2", multiCheckValues);

        Map<String, Object> complexvalues = new LinkedHashMap<>();
        complexvalues.put("InputFiled11", "InputFiled11_value1_1");
        complexvalues.put("SingleCheck1", "SingleCheck1_value1_1");
        List<String> complexvaluesMultiCheckValues = new ArrayList<>();
        complexvaluesMultiCheckValues.add("MutiInput1_value1_1");
        complexvaluesMultiCheckValues.add("MutiInput1_value1_2");
        complexvalues.put("MultiCheck1", complexvaluesMultiCheckValues);
        values.put("Complex2", complexvalues);

        valuesList.add(values);
        valuesList.add(values);
        valuesList.add(values);


        Map<String, Object> ValueMap = new LinkedHashMap<>();
        ValueMap.put("MultiComplex1", valuesList);

        FieldUtil.setFieldsValueFromMap(fieldsList, ValueMap);
        String newjsonStr = JsonUtil.bean2Json(fieldsList);
        System.out.println(newjsonStr);

//        List<Field> fieldsList2 = new ArrayList<>();
//        MultiComplexField field2 = SchemaJsonReaderTest.createMultiComplexField("MultiComplex1");
//        fieldsList2.add(field2);
//        setFieldsValue(fieldsList2, ValueMap);
//        String newjsonStr2 = JsonUtil.bean2Json(fieldsList2);
//        System.out.println(newjsonStr2);
//
//        if (newjsonStr.equals(newjsonStr2)) {
//            System.out.println("equal");
//        }

        Map<String, Object> valuesMap = FieldUtil.getFieldsValueToMap(fieldsList);
        String newjsonStr3 = JsonUtil.bean2Json(valuesMap);
        System.out.println(newjsonStr3);

        String newjsonStr4 = JsonUtil.bean2Json(ValueMap);
        System.out.println(newjsonStr4);
        if (newjsonStr3.equals(newjsonStr4)) {
            System.out.println("equal");
        }


    }

    private void setFieldsValue(List<Field> schemaFields, Map<String, Object> valueFields){

        for (Field schemaField:schemaFields){
            FieldTypeEnum fieldType = schemaField.getType();
            switch (fieldType) {
                case INPUT: {
                    InputField inputField = (InputField) schemaField;
                    Object inputObj = valueFields.get(inputField.getId());
                    if (inputObj != null) {
                        inputField.setValue(inputObj.toString());
                    }
                    break;
                }
                case SINGLECHECK: {
                    SingleCheckField singleCheckField = (SingleCheckField) schemaField;
                    Object singleCheckObj = valueFields.get(singleCheckField.getId());
                    if (singleCheckObj != null) {
                        singleCheckField.setValue(singleCheckObj.toString());
                    }
                    break;
                }
                case MULTICHECK: {
                    MultiCheckField multiCheckField = (MultiCheckField) schemaField;
                    Object multiCheckObj = valueFields.get(multiCheckField.getId());
                    if (multiCheckObj != null) {
                        List<String> values = (List<String>) multiCheckObj;
                        for (String value : values) {
                            multiCheckField.addValue(value);
                        }
                    }
                    break;
                }
                case COMPLEX: {
                    ComplexField complexField = (ComplexField) schemaField;
                    Object complexObj = valueFields.get(complexField.getId());
                    if (complexObj != null){
                        Map<String, Object> valueMap = (Map<String, Object>) complexObj;
                        ComplexValue complexValue = new ComplexValue();
                        setComplexValue(complexField, complexValue, valueMap);
                        complexField.setComplexValue(complexValue);
                    }

                    break;
                }
                case MULTICOMPLEX:
                {
                    MultiComplexField multiComplexField = (MultiComplexField) schemaField;
                    Object multiComplexObj = valueFields.get(multiComplexField.getId());
                    if(multiComplexObj != null){
                        List<Map<String, Object>> valueMaps = (List<Map<String, Object>> )multiComplexObj ;
                        List<ComplexValue> complexValues = new ArrayList<>();
                        for (Map<String, Object> valueMap : valueMaps) {
                            ComplexValue complexValue = new ComplexValue();
                            setComplexValue(multiComplexField, complexValue, valueMap);
                            complexValues.add(complexValue);
                        }
                        multiComplexField.setComplexValues(complexValues);
                    }

                    break;
                }
            }
        }
    }

    private void setComplexValue(Field field, ComplexValue complexValue, Map<String, Object> valueMap) {
        List<Field> subFields = null;
        if (field.getType() == FieldTypeEnum.COMPLEX) {
            subFields = ((ComplexField)field).getFieldList();
        } else if (field.getType() == FieldTypeEnum.MULTICOMPLEX) {
            subFields = ((MultiComplexField)field).getFieldList();
        }

        for (Field subField : subFields){
            FieldTypeEnum fieldType = subField.getType();
            switch (fieldType){
                case INPUT:
                    if (valueMap.get(subField.getId()) != null){
                        complexValue.setInputFieldValue(subField.getId(), (String)valueMap.get(subField.getId()));
                    }
                    break;
                case SINGLECHECK:
                    if (valueMap.get(subField.getId()) !=null){
                        complexValue.setSingleCheckFieldValue(subField.getId(), new Value((String)valueMap.get(subField.getId())));
                    }
                    break;
                case MULTICHECK: {
                    if (valueMap.get(subField.getId()) != null){
                        List<String> values = (List<String>) valueMap.get(subField.getId());
                        List<Value> objValues = new ArrayList<>();
                        for (String value : values) {
                            objValues.add(new Value(value));
                        }
                        complexValue.setMultiCheckFieldValues(subField.getId(), objValues);
                    }
                    break;
                }
                case COMPLEX: {

                    if (valueMap.get(subField.getId()) != null){
                        Map<String, Object> subValueMap = (Map)valueMap.get(subField.getId());
                        ComplexValue subComplexValue = new ComplexValue();
                        setComplexValue(subField, subComplexValue, subValueMap);
                        complexValue.setComplexFieldValue(subField.getId(), complexValue);
                    }
                    break;
                }
                case MULTICOMPLEX: {

                    if (valueMap.get(subField.getId()) != null){
                        List<Map<String, Object>> subValueMaps = (List<Map<String, Object>>) valueMap.get(subField.getId());
                        List<ComplexValue> subComplexValues = new ArrayList<>();

                        for (Map<String, Object> subValueMap : subValueMaps) {
                            ComplexValue subComplexValue = new ComplexValue();
                            setComplexValue(subField, subComplexValue, subValueMap);
                            subComplexValues.add(subComplexValue);
                        }
                        complexValue.setMultiComplexFieldValues(subField.getId(), subComplexValues);
                    }
                    break;
                }
            }
        }
    }
}
