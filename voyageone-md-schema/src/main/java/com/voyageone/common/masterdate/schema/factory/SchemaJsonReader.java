package com.voyageone.common.masterdate.schema.factory;

import com.voyageone.common.masterdate.schema.Util.StringUtil;
import com.voyageone.common.masterdate.schema.Util.XmlUtils;
import com.voyageone.common.masterdate.schema.depend.DependExpress;
import com.voyageone.common.masterdate.schema.depend.DependGroup;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;
import com.voyageone.common.masterdate.schema.enums.TopSchemaErrorCodeEnum;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.label.Label;
import com.voyageone.common.masterdate.schema.label.LabelGroup;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.masterdate.schema.property.Property;
import com.voyageone.common.masterdate.schema.rule.*;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.JsonUtil;

import java.io.File;
import java.util.*;

public class SchemaJsonReader {
    public SchemaJsonReader() {
    }

    public static Map<String, Field> readJsonForMap(File file) throws TopSchemaException {
        //Element rootEle = XmlUtils.getRootElementFromFile(file);
        //return readXmlForMap(rootEle);
        return null;
    }

    public static Map<String, Field> readJsonForMap(String jsonStirng) throws TopSchemaException {
        List<Map<String, Object>> rootList = JsonUtil.jsonToMapList(jsonStirng);
        return readJsonForMap(rootList);
    }

    public static List<Field> readXmlForList(File file) throws TopSchemaException {
//        Element rootEle = XmlUtils.getRootElementFromFile(file);
//        return readXmlForList(rootEle);
        return null;
    }

    /**
     * readJsonForList
     * @param jsonStirng
     * @return
     * @throws TopSchemaException
     */
    public static List<Field> readJsonForList(String jsonStirng) throws TopSchemaException {
        List<Map<String, Object>> rootList = JsonUtil.jsonToMapList(jsonStirng);
        return readJsonForList(rootList);
    }

    public static List<Field> readJsonForList(List<Map<String, Object>> rootList) throws TopSchemaException {
        List fieldList = SchemaFactory.createEmptyFieldList();
        List fieldMapList = rootList;
        Iterator<Map<String, Object>> fieldMapListIt = fieldMapList.iterator();

        while(fieldMapListIt.hasNext()) {
            Map<String, Object> fieldElm = fieldMapListIt.next();
            Field field = mapToField(fieldElm);
            fieldList.add(field);
        }

        return fieldList;
    }

    public static Map<String, Field> readJsonForMap(List<Map<String, Object>> rootList) throws TopSchemaException {
        HashMap fieldMap = new HashMap();
        List fieldMapList = rootList;
        Iterator<Map<String, Object>> fieldMapListIt = fieldMapList.iterator();

        while(fieldMapListIt.hasNext()) {
            Map<String, Object> fieldElm = fieldMapListIt.next();
            Field field = mapToField(fieldElm);
            fieldMap.put(field.getId(), field);
        }

        return fieldMap;
    }

    public static Field mapToField(Map<String, Object> fieldMap) throws TopSchemaException {
        if(fieldMap == null) {
            return null;
        } else {
            String fieldId = (String)fieldMap.get("id");
            if(StringUtil.isEmpty(fieldId)) {
                throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30001, (String)null);
            } else {
                String fieldType = (String)fieldMap.get("type");
                if(StringUtil.isEmpty(fieldType)) {
                    throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30002, fieldId);
                } else {
                    String fieldName = (String)fieldMap.get("name");
                    FieldTypeEnum fieldEnum = FieldTypeEnum.getEnum(fieldType.toLowerCase());
                    if(fieldEnum == null) {
                        throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30003, fieldId);
                    } else {
                        Object field_result = null;
                        switch(fieldEnum) {
                            case INPUT:
                                field_result = mapToInputField(fieldMap, fieldId, fieldName);
                                break;
                            case SINGLECHECK:
                                field_result = mapToSingleCheckField(fieldMap, fieldId, fieldName);
                                break;
                            case COMPLEX:
                                field_result = mapToComplexField(fieldMap, fieldId, fieldName);
                                break;
                            case MULTICHECK:
                                field_result = mapToMultiCheckField(fieldMap, fieldId, fieldName);
                                break;
                            case MULTICOMPLEX:
                                field_result = mapToMultiComplexField(fieldMap, fieldId, fieldName);
                                break;
                            case MULTIINPUT:
                                field_result = mapToMultiInputField(fieldMap, fieldId, fieldName);
                                break;
                            case LABEL:
                                field_result = mapToLabelField(fieldMap, fieldId, fieldName);
                        }

                        return (Field)field_result;
                    }
                }
            }
        }
    }

    private static Rule mapToRule(Map<String, Object> ruleMap, String fieldId) throws TopSchemaException {
        if(ruleMap == null) {
            return null;
        } else {
            String ruleName = (String)ruleMap.get("name");
            if(StringUtil.isEmpty(ruleName)) {
                throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_31001, fieldId);
            } else {
                String ruleValue = (String)ruleMap.get("value");
                if(StringUtil.isEmpty(ruleValue)) {
                    throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_31002, fieldId);
                } else {
                    Rule rule = null;
                    RuleTypeEnum ruleEnum = RuleTypeEnum.getEnum(ruleName);
                    if(ruleEnum != null) {
                        rule = SchemaFactory.createRule(ruleEnum);
                    } else {
                        rule = SchemaFactory.createCustomRule(ruleName, ruleValue);
                    }

                    if(ruleName.equals(RuleTypeEnum.TIP_RULE.value()) && !StringUtil.isEmpty(ruleValue)) {
                        String url = (String)ruleMap.get("url");
                        ((TipRule)rule).setUrl(url);
                    }

                    if(ruleName.equals(RuleTypeEnum.DEV_TIP_RULE.value()) && !StringUtil.isEmpty(ruleValue)) {
                        String url = (String)ruleMap.get("url");
                        ((DevTipRule)rule).setUrl(url);
                    }

                    String unit = (String)ruleMap.get("unit");
                    if(ruleName.equals(RuleTypeEnum.MAX_TARGET_SIZE_RULE.value()) && !StringUtil.isEmpty(ruleValue)) {
                        MaxTargetSizeRule exProperty1 = (MaxTargetSizeRule)rule;
                        exProperty1.setUnit(unit);
                    } else if(ruleName.equals(RuleTypeEnum.MIN_TARGET_SIZE_RULE.value()) && !StringUtil.isEmpty(ruleValue)) {
                        MinTargetSizeRule exProperty = (MinTargetSizeRule)rule;
                        exProperty.setUnit(unit);
                    }

                    if(ruleName.equals(RuleTypeEnum.MAX_LENGTH_RULE.value()) && !StringUtil.isEmpty(ruleValue)) {
                        MaxLengthRule exProperty3 = (MaxLengthRule)rule;
                        exProperty3.setUnit(unit);
                    } else if(ruleName.equals(RuleTypeEnum.MIN_LENGTH_RULE.value()) && !StringUtil.isEmpty(ruleValue)) {
                        MinLengthRule exProperty2 = (MinLengthRule)rule;
                        exProperty2.setUnit(unit);
                    }

                    String exProperty = (String)ruleMap.get("exProperty");
                    if(!StringUtil.isEmpty(exProperty)) {
                        rule.setExProperty(exProperty);
                    }

                    rule.setValue(ruleValue);
                    Map<String, Object> dependGroupMap = (Map<String, Object>)ruleMap.get("dependGroup");
                    DependGroup dependGroup = mapToDependGroup(dependGroupMap, fieldId);
                    rule.setDependGroup(dependGroup);
                    return rule;
                }
            }
        }
    }

    private static DependGroup mapToDependGroup(Map<String, Object> dependGroupMap, String fieldId) throws TopSchemaException {
        if(dependGroupMap == null) {
            return null;
        } else {
            String dependGroupOperator = (String)dependGroupMap.get("operator");
            if(StringUtil.isEmpty(dependGroupOperator)) {
                dependGroupOperator = "and";
            }

            DependGroup dg_result = new DependGroup();
            dg_result.setOperator(dependGroupOperator);
            List<Map<String, Object>> deList = (List<Map<String, Object>>)dependGroupMap.get("dependExpressList");
            Iterator deListIt = deList.iterator();
            List<DependExpress> dependExpressList = new ArrayList();
            while(deListIt.hasNext()) {
                Map<String, Object> deMap = (Map<String, Object>)deListIt.next();
                String deFieldId = (String)deMap.get("fieldId");
                String deValue = (String)deMap.get("value");
                String deSymbol = (String)deMap.get("symbol");
                DependExpress dgSubEle = new DependExpress();
                dgSubEle.setFieldId(deFieldId);
                dgSubEle.setValue(deValue);
                dgSubEle.setSymbol(deSymbol);
                dependExpressList.add(dgSubEle);
            }
            dg_result.setDependExpressList(dependExpressList);

            List<Map<String, Object>> dgList = (List<Map<String, Object>>)dependGroupMap.get("dependGroupList");
            Iterator dgListIt = dgList.iterator();
            List<DependGroup> dependGroupList = new ArrayList();
            while(dgListIt.hasNext()) {
                Map<String, Object> dgMap = (Map<String, Object>)dgListIt.next();
                //new DependGroup();
                DependGroup dependGroup = mapToDependGroup(dgMap, fieldId);
                dependGroupList.add(dependGroup);
            }
            dg_result.setDependGroupList(dependGroupList);

            return dg_result;
        }
    }

    private static Option mapToOption(Map<String, Object> optionMap, String fieldId) throws TopSchemaException {
        Option opResult = new Option();
        String displayName = (String)optionMap.get("displayName");
        if(StringUtil.isEmpty(displayName)) {
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_33001, fieldId);
        } else {
            String value = (String)optionMap.get("value");
            if(StringUtil.isEmpty(value)) {
                throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_33002, fieldId);
            } else {
                opResult.setDisplayName(displayName);
                opResult.setValue(value);

                Map<String, Object> dependGroupMap = (Map<String, Object>)optionMap.get("dependGroup");
                DependGroup dependGroup = mapToDependGroup(dependGroupMap, fieldId);
                opResult.setDependGroup(dependGroup);
                return opResult;
            }
        }
    }

    private static Property mapToProperty(Map<String, Object> propertyItMap, String fieldId) throws TopSchemaException {
        String key = (String)propertyItMap.get("key");
        String value = (String)propertyItMap.get("value");
        Property property = new Property(key, value);
        return property;
    }

    private static InputField mapToInputField(Map<String, Object> fieldMap, String fieldId, String fieldName) throws TopSchemaException {
        if(fieldMap == null) {
            return null;
        } else {
            InputField inputField = (InputField) SchemaFactory.createField(FieldTypeEnum.INPUT);
            inputField.setId(fieldId);
            inputField.setName(fieldName);
            if(fieldMap.containsKey("rules")) {
                List<Map<String, Object>> rulesMap = (List<Map<String, Object>>)fieldMap.get("rules");
                Iterator<Map<String, Object>> rulesIt = rulesMap.iterator();

                List<Rule> rules = new ArrayList();
                while(rulesIt.hasNext()) {
                    Map<String, Object> ruleMap = rulesIt.next();
                    Rule rule = mapToRule(ruleMap, inputField.getId());
                    rules.add(rule);
                }
                inputField.setRules(rules);
            }

            if(fieldMap.containsKey("properties")) {
                List<Map<String, Object>> propertiesMap = (List<Map<String, Object>>)fieldMap.get("properties");
                Iterator propertyIt = propertiesMap.iterator();

                List<Property> properties = new ArrayList();
                while(propertyIt.hasNext()) {
                    Map<String, Object> propertyItMap = (Map<String, Object>)propertyIt.next();
                    Property property = mapToProperty(propertyItMap, inputField.getId());
                    properties.add(property);
                }
                inputField.setProperties(properties);
            }

            Map<String, Object> defaultValueMap = (Map<String, Object>)fieldMap.get("defaultValueField");
            if(defaultValueMap != null) {
                String defaultValue = (String)defaultValueMap.get("value");
                inputField.setDefaultValue(defaultValue);
            }

            String valueStr = (String)fieldMap.get("value");
            if(valueStr != null) {
                inputField.setValue(valueStr);
            }

            return inputField;
        }
    }

    private static LabelField mapToLabelField(Map<String, Object> fieldMap, String fieldId, String fieldName) throws TopSchemaException {
        if(fieldMap == null) {
            return null;
        } else {
            LabelField labelField = (LabelField) SchemaFactory.createField(FieldTypeEnum.LABEL);
            labelField.setId(fieldId);
            labelField.setName(fieldName);

            if(fieldMap.containsKey("rules")) {
                List<Map<String, Object>> rulesMap = (List<Map<String, Object>>)fieldMap.get("rules");
                Iterator<Map<String, Object>> rulesIt = rulesMap.iterator();

                List<Rule> rules = new ArrayList();
                while(rulesIt.hasNext()) {
                    Map<String, Object> ruleMap = rulesIt.next();
                    Rule rule = mapToRule(ruleMap, labelField.getId());
                    rules.add(rule);
                }
                labelField.setRules(rules);
            }

            if(fieldMap.containsKey("properties")) {
                List<Map<String, Object>> propertiesMap = (List<Map<String, Object>>)fieldMap.get("properties");
                Iterator propertyIt = propertiesMap.iterator();

                List<Property> properties = new ArrayList();
                while(propertyIt.hasNext()) {
                    Map<String, Object> propertyItMap = (Map<String, Object>)propertyIt.next();
                    Property property = mapToProperty(propertyItMap, labelField.getId());
                    properties.add(property);
                }
                labelField.setProperties(properties);
            }

//            Element labelGroupEle2 = XmlUtils.getChildElement(ruleMap, "label-group");
//            if(labelGroupEle2 != null) {
//                LabelGroup labelGroup2 = elementToLabelGroup(labelGroupEle2, fieldId);
//                labelField.setLabelGroup(labelGroup2);
//            }

            return labelField;
        }
    }

    private static MultiInputField mapToMultiInputField(Map<String, Object> fieldMap, String fieldId, String fieldName) throws TopSchemaException {
        if(fieldMap == null) {
            return null;
        } else {
            MultiInputField multiInputField = (MultiInputField) SchemaFactory.createField(FieldTypeEnum.MULTIINPUT);
            multiInputField.setId(fieldId);
            multiInputField.setName(fieldName);

            if(fieldMap.containsKey("rules")) {
                List<Map<String, Object>> rulesMap = (List<Map<String, Object>>)fieldMap.get("rules");
                Iterator<Map<String, Object>> rulesIt = rulesMap.iterator();

                List<Rule> rules = new ArrayList();
                while(rulesIt.hasNext()) {
                    Map<String, Object> ruleMap = rulesIt.next();
                    Rule rule = mapToRule(ruleMap, multiInputField.getId());
                    rules.add(rule);
                }
                multiInputField.setRules(rules);
            }

            if(fieldMap.containsKey("properties")) {
                List<Map<String, Object>> propertiesMap = (List<Map<String, Object>>)fieldMap.get("properties");
                Iterator propertyIt = propertiesMap.iterator();

                List<Property> properties = new ArrayList();
                while(propertyIt.hasNext()) {
                    Map<String, Object> propertyItMap = (Map<String, Object>)propertyIt.next();
                    Property property = mapToProperty(propertyItMap, multiInputField.getId());
                    properties.add(property);
                }
                multiInputField.setProperties(properties);
            }

//            Element defaultValuesEle2 = XmlUtils.getChildElement(fieldMap, "default-values");
//            if(defaultValuesEle2 != null) {
//                List valuesEle2 = XmlUtils.getChildElements(defaultValuesEle2, "default-value");
//                Iterator valueEleList2 = valuesEle2.iterator();
//
//                while(valueEleList2.hasNext()) {
//                    Element i$1 = (Element)valueEleList2.next();
//                    String valueEle = i$1.getText();
//                    multiInputField.addDefaultValue(valueEle);
//                }
//            }
//
//            valuesEle = XmlUtils.getChildElement(fieldMap, "values");
//            if(valuesEle != null) {
//                List valueEleList3 = XmlUtils.getChildElements(valuesEle, "value");
//                Iterator i$2 = valueEleList3.iterator();
//
//                while(i$2.hasNext()) {
//                    Element valueEle1 = (Element)i$2.next();
//                    Value value = new Value();
//                    value.setValue(XmlUtils.getElementValue(valueEle1));
//                    multiInputField.addValue(value);
//                }
//            }

            return multiInputField;
        }
    }

    private static SingleCheckField mapToSingleCheckField(Map<String, Object>  fieldMap, String fieldId, String fieldName) throws TopSchemaException {
        if(fieldMap == null) {
            return null;
        } else {
            SingleCheckField singleCheckField = (SingleCheckField) SchemaFactory.createField(FieldTypeEnum.SINGLECHECK);
            singleCheckField.setId(fieldId);
            singleCheckField.setName(fieldName);
            if(fieldMap.containsKey("rules")) {
                List<Map<String, Object>> rules = (List<Map<String, Object>>)fieldMap.get("rules");
                Iterator<Map<String, Object>> rulesIt = rules.iterator();

                while(rulesIt.hasNext()) {
                    Map<String, Object> ruleMap = rulesIt.next();
                    Rule valueEle = mapToRule(ruleMap, singleCheckField.getId());
                    singleCheckField.add(valueEle);
                }
            }

            if(fieldMap.containsKey("options")) {
                List<Map<String, Object>> options = (List<Map<String, Object>>)fieldMap.get("options");
                Iterator<Map<String, Object>> optionsIt = options.iterator();

                while(optionsIt.hasNext()) {
                    Map<String, Object> optionMap = optionsIt.next();
                    Option value = mapToOption(optionMap, singleCheckField.getId());
                    singleCheckField.add(value);
                }
            }

            if(fieldMap.containsKey("properties")) {
                List<Map<String, Object>> propertiesMap = (List<Map<String, Object>>)fieldMap.get("properties");
                Iterator propertyIt = propertiesMap.iterator();

                List<Property> properties = new ArrayList();
                while(propertyIt.hasNext()) {
                    Map<String, Object> propertyItMap = (Map<String, Object>)propertyIt.next();
                    Property property = mapToProperty(propertyItMap, singleCheckField.getId());
                    properties.add(property);
                }
                singleCheckField.setProperties(properties);
            }

            String defaultValueStr = (String)fieldMap.get("defaultValue");
            if(defaultValueStr != null) {
                Value valueEle3 = new Value();
                valueEle3.setValue(defaultValueStr);
                singleCheckField.setDefaultValueDO(valueEle3);
            }

            String valueStr = (String)fieldMap.get("value");
            if(valueStr != null) {
                Value value3 = new Value();
                value3.setValue(valueStr);
                singleCheckField.setValue(value3);
            }

            return singleCheckField;
        }
    }

    private static MultiCheckField mapToMultiCheckField(Map<String, Object>  fieldMap, String fieldId, String fieldName) throws TopSchemaException {
        if(fieldMap == null) {
            return null;
        } else {
            MultiCheckField multiCheckField = (MultiCheckField) SchemaFactory.createField(FieldTypeEnum.MULTICHECK);
            multiCheckField.setId(fieldId);
            multiCheckField.setName(fieldName);

            if(fieldMap.containsKey("rules")) {
                List<Map<String, Object>> rulesMap = (List<Map<String, Object>>)fieldMap.get("rules");
                Iterator<Map<String, Object>> rulesIt = rulesMap.iterator();

                List<Rule> rules = new ArrayList();
                while(rulesIt.hasNext()) {
                    Map<String, Object> ruleMap = rulesIt.next();
                    Rule rule = mapToRule(ruleMap, multiCheckField.getId());
                    rules.add(rule);
                }
                multiCheckField.setRules(rules);
            }

            if(fieldMap.containsKey("options")) {
                List<Map<String, Object>> optionsMap = (List<Map<String, Object>>)fieldMap.get("options");
                Iterator<Map<String, Object>> optionIt = optionsMap.iterator();

                List<Option> options = new ArrayList();
                while(optionIt.hasNext()) {
                    Map<String, Object> optionMap = optionIt.next();
                    Option option = mapToOption(optionMap, multiCheckField.getId());
                    options.add(option);
                }
                multiCheckField.setOptions(options);
            }

            if(fieldMap.containsKey("properties")) {
                List<Map<String, Object>> propertiesMap = (List<Map<String, Object>>)fieldMap.get("properties");
                Iterator propertyIt = propertiesMap.iterator();

                List<Property> properties = new ArrayList();
                while(propertyIt.hasNext()) {
                    Map<String, Object> propertyItMap = (Map<String, Object>)propertyIt.next();
                    Property property = mapToProperty(propertyItMap, multiCheckField.getId());
                    properties.add(property);
                }
                multiCheckField.setProperties(properties);
            }

            List<Map<String, Object>> defaultValue = (List<Map<String, Object>>)fieldMap.get("defaultValue");
            if(defaultValue != null) {
                Iterator<Map<String, Object>> defaultValueIt = defaultValue.iterator();
                ComplexValue defaultComplexValue = new ComplexValue();
                while(defaultValueIt.hasNext()) {
                    Map<String, Object> subDefaultValue = defaultValueIt.next();
                    Value valueEle = new Value();
                    String id = (String)subDefaultValue.get("id");
                    valueEle.setId(id);
                    String value = (String)subDefaultValue.get("value");
                    valueEle.setValue(value);
                    multiCheckField.addDefaultValueDO(valueEle);
                }
            }

            List<Map<String, Object>> values = (List<Map<String, Object>>)fieldMap.get("values");
            if(values != null) {
                Iterator<Map<String, Object>> valuesIt = values.iterator();

                while(valuesIt.hasNext()) {
                    Map<String, Object> valueSubMap = valuesIt.next();
                    Value valueSub = new Value();
                    String id = (String)valueSubMap.get("id");
                    valueSub.setId(id);
                    String value = (String)valueSubMap.get("value");
                    valueSub.setValue(value);
                    multiCheckField.addValue(valueSub);
                }
            }

            return multiCheckField;
        }
    }

    private static MultiComplexField mapToMultiComplexField(Map<String, Object> fieldMap, String fieldId, String fieldName) throws TopSchemaException {
        if(fieldMap == null) {
            return null;
        } else {
            MultiComplexField multiComplexField = (MultiComplexField) SchemaFactory.createField(FieldTypeEnum.MULTICOMPLEX);
            multiComplexField.setId(fieldId);
            multiComplexField.setName(fieldName);

            if (fieldMap.containsKey("fields")) {
                List<Map<String, Object>> fieldList = (List<Map<String, Object>>)fieldMap.get("fields");
                Iterator<Map<String, Object>> fieldListIt = fieldList.iterator();

                while(fieldListIt.hasNext()) {
                    Map<String, Object> subFieldMap = fieldListIt.next();
                    Field complexSubField = mapToField(subFieldMap);
                    multiComplexField.add(complexSubField);
                }
            }

            if(fieldMap.containsKey("rules")) {
                List<Map<String, Object>> rulesMap = (List<Map<String, Object>>)fieldMap.get("rules");
                Iterator<Map<String, Object>> rulesIt = rulesMap.iterator();

                List<Rule> rules = new ArrayList();
                while(rulesIt.hasNext()) {
                    Map<String, Object> ruleMap = rulesIt.next();
                    Rule rule = mapToRule(ruleMap, multiComplexField.getId());
                    rules.add(rule);
                }
                multiComplexField.setRules(rules);
            }

            if(fieldMap.containsKey("properties")) {
                List<Map<String, Object>> propertiesMap = (List<Map<String, Object>>)fieldMap.get("properties");
                Iterator propertyIt = propertiesMap.iterator();

                List<Property> properties = new ArrayList();
                while(propertyIt.hasNext()) {
                    Map<String, Object> propertyItMap = (Map<String, Object>)propertyIt.next();
                    Property property = mapToProperty(propertyItMap, multiComplexField.getId());
                    properties.add(property);
                }
                multiComplexField.setProperties(properties);
            }

            List<Map<String, Object>> defaultValue = (List<Map<String, Object>>)fieldMap.get("defaultValue");
            if(defaultValue != null) {
                Iterator<Map<String, Object>> defaultValueIt = defaultValue.iterator();
                ComplexValue defaultComplexValue = new ComplexValue();
                while(defaultValueIt.hasNext()) {
                    Map<String, Object> subDefaultValue = defaultValueIt.next();
                    Field subFiledValue = mapToField(subDefaultValue);
                    defaultComplexValue.put(subFiledValue);
                }
                multiComplexField.addDefaultComplexValue(defaultComplexValue);
            }

//            defaultValuesEle = XmlUtils.getChildElement(fieldMap, "default-values");
//            List defaultValuesSubFieldList;
//            ComplexValue cvalue;
//            Iterator i$1;
//            Element subFiledValueEle;
//            Field field;
//            List complexValuesEle3;
//            Iterator i$3;
//            Element complexValue1;
//            if(defaultValuesEle != null) {
//                complexValuesEle3 = XmlUtils.getChildElements(defaultValuesEle, "default-complex-values");
//                i$3 = complexValuesEle3.iterator();
//
//                while(i$3.hasNext()) {
//                    complexValue1 = (Element)i$3.next();
//                    defaultValuesSubFieldList = XmlUtils.getChildElements(complexValue1, "field");
//                    cvalue = new ComplexValue();
//                    i$1 = defaultValuesSubFieldList.iterator();
//
//                    while(i$1.hasNext()) {
//                        subFiledValueEle = (Element)i$1.next();
//                        field = elementToField(subFiledValueEle);
//                        cvalue.put(field);
//                    }
//
//                    multiComplexField.addDefaultComplexValue(cvalue);
//                }
//            }


            Map<String, Object> complexValue = (Map<String, Object>)fieldMap.get("complexValue");
            if(complexValue != null) {
                Iterator complexValueFieldsIt = complexValue.values().iterator();
                ComplexValue cvalue = new ComplexValue();

                while(complexValueFieldsIt.hasNext()) {
                    Map<String, Object> complexValueFieldMap = (Map<String, Object>)complexValueFieldsIt.next();
                    Field subFiledValueEle = mapToField(complexValueFieldMap);
                    cvalue.put(subFiledValueEle);
                }

                multiComplexField.addComplexValue(cvalue);
            }

//            complexValuesEle3 = XmlUtils.getChildElements(fieldMap, "complex-values");
//            i$3 = complexValuesEle3.iterator();
//
//            while(i$3.hasNext()) {
//                complexValue1 = (Element)i$3.next();
//                defaultValuesSubFieldList = XmlUtils.getChildElements(complexValue1, "field");
//                cvalue = new ComplexValue();
//                i$1 = defaultValuesSubFieldList.iterator();
//
//                while(i$1.hasNext()) {
//                    subFiledValueEle = (Element)i$1.next();
//                    field = elementToField(subFiledValueEle);
//                    cvalue.put(field);
//                }
//
//                multiComplexField.addComplexValue(cvalue);
//            }

            return multiComplexField;
        }
    }


    private static ComplexField mapToComplexField(Map<String, Object>  fieldMap, String fieldId, String fieldName) throws TopSchemaException {
        if(fieldMap == null) {
            return null;
        } else {
            ComplexField complexField = (ComplexField) SchemaFactory.createField(FieldTypeEnum.COMPLEX);
            complexField.setId(fieldId);
            complexField.setName(fieldName);
            if (fieldMap.containsKey("fields")) {
                List<Map<String, Object>> fieldList = (List<Map<String, Object>>)fieldMap.get("fields");
                Iterator<Map<String, Object>> fieldListIt = fieldList.iterator();

                while(fieldListIt.hasNext()) {
                    Map<String, Object> subFieldMap = fieldListIt.next();
                    Field complexSubField = mapToField(subFieldMap);
                    complexField.add(complexSubField);
                }
            }

            if(fieldMap.containsKey("rules")) {
                List<Map<String, Object>> rulesMap = (List<Map<String, Object>>)fieldMap.get("rules");
                Iterator<Map<String, Object>> rulesIt = rulesMap.iterator();

                List<Rule> rules = new ArrayList();
                while(rulesIt.hasNext()) {
                    Map<String, Object> ruleMap = rulesIt.next();
                    Rule rule = mapToRule(ruleMap, complexField.getId());
                    rules.add(rule);
                }
                complexField.setRules(rules);
            }

            if(fieldMap.containsKey("properties")) {
                List<Map<String, Object>> propertiesMap = (List<Map<String, Object>>)fieldMap.get("properties");
                Iterator propertyIt = propertiesMap.iterator();

                List<Property> properties = new ArrayList();
                while(propertyIt.hasNext()) {
                    Map<String, Object> propertyItMap = (Map<String, Object>)propertyIt.next();
                    Property property = mapToProperty(propertyItMap, complexField.getId());
                    properties.add(property);
                }
                complexField.setProperties(properties);
            }

            List<Map<String, Object>> defaultValue = (List<Map<String, Object>>)fieldMap.get("defaultValue");
            if(defaultValue != null) {
                Iterator<Map<String, Object>> defaultValueIt = defaultValue.iterator();
                ComplexValue defaultComplexValue = new ComplexValue();
                while(defaultValueIt.hasNext()) {
                    Map<String, Object> subDefaultValue = defaultValueIt.next();
                    Field subFiledValue = mapToField(subDefaultValue);
                    defaultComplexValue.put(subFiledValue);
                }
                complexField.setDefaultValue(defaultComplexValue);
            }

            Map<String, Object> complexValue = (Map<String, Object>)fieldMap.get("complexValue");
            if(complexValue != null) {
                Iterator complexValueFieldsIt = complexValue.values().iterator();
                ComplexValue cvalue = new ComplexValue();

                while(complexValueFieldsIt.hasNext()) {
                    Map<String, Object> complexValueFieldMap = (Map<String, Object>)complexValueFieldsIt.next();
                    Field subFiledValueEle = mapToField(complexValueFieldMap);
                    cvalue.put(subFiledValueEle);
                }

                complexField.setComplexValue(cvalue);
            }

            return complexField;
        }
    }
}
