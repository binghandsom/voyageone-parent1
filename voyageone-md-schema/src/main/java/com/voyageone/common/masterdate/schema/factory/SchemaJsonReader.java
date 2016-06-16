package com.voyageone.common.masterdate.schema.factory;

import com.voyageone.common.masterdate.schema.enums.FieldValueTypeEnum;
import com.voyageone.common.masterdate.schema.utils.JsonUtil;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
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

import java.util.*;

public class SchemaJsonReader {

    /**
     * readJsonForMap
     */
    public static Map<String, Field> readJsonForMap(String jsonStirng) {
        List<Map<String, Object>> rootList = JsonUtil.jsonToMapList(jsonStirng);
        return readJsonForMap(rootList);
    }

    /**
     * readJsonForList
     */
    public static List<Field> readJsonForList(String jsonStirng) {
        List<Map<String, Object>> rootList = JsonUtil.jsonToMapList(jsonStirng);
        return readJsonForList(rootList);
    }

    /**
     * readJsonForList
     */
    public static List<Field> readJsonForList(List<Map<String, Object>> rootList) {
        List<Field> fieldList = SchemaFactory.createEmptyFieldList();
        for (Map<String, Object> fieldElm : rootList) {
            Field field = mapToField(fieldElm);
            fieldList.add(field);
        }

        return fieldList;
    }

    /**
     * readJsonForMap
     */
    public static Map<String, Field> readJsonForMap(List<Map<String, Object>> rootList) {
        Map<String, Field> fieldMap = new HashMap<>();
        for (Map<String, Object> fieldElm : rootList) {
            Field field = mapToField(fieldElm);
            fieldMap.put(field.getId(), field);
        }

        return fieldMap;
    }

    /**
     * mapToField from Object
     */
    @SuppressWarnings("unchecked")
    public static Field mapToField(Object fieldMap) {
        return mapToField((Map<String, Object>)fieldMap);
    }

    /**
     * mapToField from Map
     */
    public static Field mapToField(Map<String, Object> fieldMap) {
        if(fieldMap == null) {
            return null;
        } else {
            String fieldId = (String)fieldMap.get("id");
            if(StringUtil.isEmpty(fieldId)) {
                throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30001, null);
            } else {
                String fieldType = (String)fieldMap.get("type");
                if(StringUtil.isEmpty(fieldType)) {
                    throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30002, fieldId);
                } else {
                    String fieldName = (String)fieldMap.get("name");
                    FieldTypeEnum fieldEnum = FieldTypeEnum.getEnum(fieldType);
                    if(fieldEnum == null) {
                        throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30003, fieldId);
                    } else {
                        Object field_result = null;
                        switch(fieldEnum) {
                            case INPUT:
                                field_result = mapToInputField(fieldMap, fieldId, fieldName);
                                break;
                            case MULTIINPUT:
                                field_result = mapToMultiInputField(fieldMap, fieldId, fieldName);
                                break;
                            case SINGLECHECK:
                                field_result = mapToSingleCheckField(fieldMap, fieldId, fieldName);
                                break;
                            case MULTICHECK:
                                field_result = mapToMultiCheckField(fieldMap, fieldId, fieldName);
                                break;
                            case COMPLEX:
                                field_result = mapToComplexField(fieldMap, fieldId, fieldName);
                                break;
                            case MULTICOMPLEX:
                                field_result = mapToMultiComplexField(fieldMap, fieldId, fieldName);
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

    @SuppressWarnings("unchecked")
    private static Rule mapToRule(Map<String, Object> ruleMap, String fieldId) {
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
                    Rule rule;
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

    @SuppressWarnings("unchecked")
    private static DependGroup mapToDependGroup(Map<String, Object> dependGroupMap, String fieldId) {
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
            Iterator<Map<String, Object>> deListIt = deList.iterator();
            List<DependExpress> dependExpressList = new ArrayList<>();
            while(deListIt.hasNext()) {
                Map<String, Object> deMap = deListIt.next();
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
            Iterator<Map<String, Object>> dgListIt = dgList.iterator();
            List<DependGroup> dependGroupList = new ArrayList<>();
            while(dgListIt.hasNext()) {
                Map<String, Object> dgMap = dgListIt.next();
                //new DependGroup();
                DependGroup dependGroup = mapToDependGroup(dgMap, fieldId);
                dependGroupList.add(dependGroup);
            }
            dg_result.setDependGroupList(dependGroupList);

            return dg_result;
        }
    }

    @SuppressWarnings("unchecked")
    private static LabelGroup mapToLabelGroup(Map<String, Object> labelGroupMap, String fieldId) {
        if(labelGroupMap == null) {
            return null;
        } else {
            LabelGroup lg_result = new LabelGroup();
            String name = (String)labelGroupMap.get("name");
            lg_result.setName(name);

            List<Map<String, Object>> labelList = (List<Map<String, Object>>)labelGroupMap.get("labelList");
            if (labelList != null) {
                for (Map<String, Object> labelMap : labelList) {
                    Label subLabelGroupEle = new Label();
                    String subGroup = (String)labelMap.get("name");
                    String labelValue = (String)labelMap.get("value");
                    String labelDesc = (String)labelMap.get("desc");
                    subLabelGroupEle.setName(subGroup);
                    subLabelGroupEle.setValue(labelValue);
                    subLabelGroupEle.setDesc(labelDesc);
                    lg_result.add(subLabelGroupEle);
                }
            }

            List<Map<String, Object>> labelGroupList = (List<Map<String, Object>>)labelGroupMap.get("labelGroupList");
            if (labelGroupList != null) {
                for(Map<String, Object> subLabelGroup : labelGroupList) {
                    LabelGroup subGroup1 = mapToLabelGroup(subLabelGroup, fieldId);
                    lg_result.add(subGroup1);
                }
            }

            return lg_result;
        }
    }

    @SuppressWarnings("unchecked")
    private static Option mapToOption(Map<String, Object> optionMap, String fieldId) {
        Option opResult = new Option();
        String displayName = (String)optionMap.get("displayName");
        if(StringUtil.isEmpty(displayName)) {
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_33001, fieldId);
        } else {
            // 前台传进来的参数可能会是Integer类型，不能直接转为String
            Object value = optionMap.get("value");
            if (value == null) {
                throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_33002, fieldId);
            } else {
                String strValue = value.toString();
                if (StringUtil.isEmpty(strValue)) {
                    throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_33002, fieldId);
                }
                opResult.setDisplayName(displayName);
                opResult.setValue(strValue);
                Map<String, Object> dependGroupMap = (Map<String, Object>)optionMap.get("dependGroup");
                DependGroup dependGroup = mapToDependGroup(dependGroupMap, fieldId);
                opResult.setDependGroup(dependGroup);
                return opResult;
            }
        }
    }

    private static Property mapToProperty(Map<String, Object> propertyItMap, String fieldId) {
        String key = (String)propertyItMap.get("key");
        String value = (String)propertyItMap.get("value");
        return new Property(key, value);
    }

    private static void setComColumn(Field field, Map<String, Object> fieldMap) {
        field.setId((String) fieldMap.get("id"));
        field.setName((String) fieldMap.get("name"));
        if (fieldMap.containsKey("inputLevel")) {
            field.setInputLevel((int) fieldMap.get("inputLevel"));
        }
        if (fieldMap.containsKey("inputOrgId")) {
            field.setInputOrgId((String) fieldMap.get("inputOrgId"));
        }
        if (fieldMap.containsKey("dataSource")) {
            field.setDataSource((String) fieldMap.get("dataSource"));
        }
        if (fieldMap.containsKey("isDisplay")) {
            field.setIsDisplay((int) fieldMap.get("isDisplay"));
        }
        String fieldValueTypeStr = (String)fieldMap.get("fieldValueType");
        if(fieldValueTypeStr != null) {
            FieldValueTypeEnum fieldValueTypeEnum = FieldValueTypeEnum.getEnum(fieldValueTypeStr);
            field.setFieldValueType(fieldValueTypeEnum);
        }
    }

    @SuppressWarnings("unchecked")
    private static InputField mapToInputField(Map<String, Object> fieldMap, String fieldId, String fieldName) {
        if(fieldMap == null) {
            return null;
        } else {
            InputField inputField = (InputField) SchemaFactory.createField(FieldTypeEnum.INPUT);
            setComColumn(inputField, fieldMap);

            if(fieldMap.containsKey("rules")) {
                List<Map<String, Object>> rulesMap = (List<Map<String, Object>>)fieldMap.get("rules");
                Iterator<Map<String, Object>> rulesIt = rulesMap.iterator();

                List<Rule> rules = new ArrayList<>();
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

                List<Property> properties = new ArrayList<>();
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

            Object valueObj = fieldMap.get("value");

            if (valueObj != null) {
                String valueStr = String.valueOf(valueObj);
                inputField.setValue(valueStr);
            }

            return inputField;
        }
    }

    @SuppressWarnings("unchecked")
    private static LabelField mapToLabelField(Map<String, Object> fieldMap, String fieldId, String fieldName) {
        if(fieldMap == null) {
            return null;
        } else {
            LabelField labelField = (LabelField) SchemaFactory.createField(FieldTypeEnum.LABEL);
            setComColumn(labelField, fieldMap);

            if (fieldMap.containsKey("rules")) {
                List<Map<String, Object>> rulesMap = (List<Map<String, Object>>)fieldMap.get("rules");
                Iterator<Map<String, Object>> rulesIt = rulesMap.iterator();

                List<Rule> rules = new ArrayList<>();
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

                List<Property> properties = new ArrayList<>();
                while(propertyIt.hasNext()) {
                    Map<String, Object> propertyItMap = (Map<String, Object>)propertyIt.next();
                    Property property = mapToProperty(propertyItMap, labelField.getId());
                    properties.add(property);
                }
                labelField.setProperties(properties);
            }

            if(fieldMap.containsKey("labelGroup")) {
                Map<String, Object> labelGroupMap = (Map<String, Object>)fieldMap.get("labelGroup");
                LabelGroup labelGroup = mapToLabelGroup(labelGroupMap, fieldId);
                labelField.setLabelGroup(labelGroup);
            }

            return labelField;
        }
    }

    @SuppressWarnings("unchecked")
    private static MultiInputField mapToMultiInputField(Map<String, Object> fieldMap, String fieldId, String fieldName) {
        if(fieldMap == null) {
            return null;
        } else {
            MultiInputField multiInputField = (MultiInputField) SchemaFactory.createField(FieldTypeEnum.MULTIINPUT);
            setComColumn(multiInputField, fieldMap);

            if(fieldMap.containsKey("rules")) {
                List<Map<String, Object>> rulesMap = (List<Map<String, Object>>)fieldMap.get("rules");
                Iterator<Map<String, Object>> rulesIt = rulesMap.iterator();

                List<Rule> rules = new ArrayList<>();
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

                List<Property> properties = new ArrayList<>();
                while(propertyIt.hasNext()) {
                    Map<String, Object> propertyItMap = (Map<String, Object>)propertyIt.next();
                    Property property = mapToProperty(propertyItMap, multiInputField.getId());
                    properties.add(property);
                }
                multiInputField.setProperties(properties);
            }

            Map<String, Object> defaultValueMap = (Map<String, Object>)fieldMap.get("defaultValueField");
            if(defaultValueMap != null) {
                List<Map<String, Object>> defaultValues = (List<Map<String, Object>>)defaultValueMap.get("values");
                if (defaultValues != null) {
                    for(Map<String, Object> defaultValueSubMap : defaultValues) {
                        String defaultValueStr = (String)defaultValueSubMap.get("value");
                        multiInputField.addDefaultValue(defaultValueStr);
                    }
                }
            }

            List<Map<String, Object>> valuesMapList = (List<Map<String, Object>>)fieldMap.get("values");
            if(valuesMapList != null) {
                for(Map<String, Object> valueMap : valuesMapList) {
                    String id = (String)valueMap.get("id");
                    String value = (String)valueMap.get("value");
                    Value valueObj = new Value(id, value);
                    multiInputField.addValue(valueObj);
                }
            }

            return multiInputField;
        }
    }

    @SuppressWarnings("unchecked")
    private static SingleCheckField mapToSingleCheckField(Map<String, Object>  fieldMap, String fieldId, String fieldName) {
        if(fieldMap == null) {
            return null;
        } else {
            SingleCheckField singleCheckField = (SingleCheckField) SchemaFactory.createField(FieldTypeEnum.SINGLECHECK);
            setComColumn(singleCheckField, fieldMap);

            if(fieldMap.containsKey("rules")) {
                List<Map<String, Object>> rules = (List<Map<String, Object>>)fieldMap.get("rules");
                for(Map<String, Object> ruleMap : rules) {
                    Rule valueEle = mapToRule(ruleMap, singleCheckField.getId());
                    singleCheckField.add(valueEle);
                }
            }

            if(fieldMap.containsKey("options")) {
                List<Map<String, Object>> options = (List<Map<String, Object>>)fieldMap.get("options");
                for(Map<String, Object> optionMap : options) {
                    Option value = mapToOption(optionMap, singleCheckField.getId());
                    singleCheckField.add(value);
                }
            }

            if(fieldMap.containsKey("properties")) {
                List<Map<String, Object>> propertiesMap = (List<Map<String, Object>>)fieldMap.get("properties");
                Iterator propertyIt = propertiesMap.iterator();

                List<Property> properties = new ArrayList<>();
                while(propertyIt.hasNext()) {
                    Map<String, Object> propertyItMap = (Map<String, Object>)propertyIt.next();
                    Property property = mapToProperty(propertyItMap, singleCheckField.getId());
                    properties.add(property);
                }
                singleCheckField.setProperties(properties);
            }

            Map<String, Object> defaultValueMap = (Map<String, Object>)fieldMap.get("defaultValueField");
            if(defaultValueMap != null) {
                Map<String, Object> valueMap = (Map<String, Object>)defaultValueMap.get("value");
                if (valueMap != null) {
                    Value value3 = new Value();
                    value3.setId((String)valueMap.get("id"));
                    value3.setValue((String) valueMap.get("value"));
                    singleCheckField.setDefaultValueDO(value3);
                }
            }

            Map<String, Object> valueMap = (Map<String, Object>)fieldMap.get("value");
            if(valueMap != null) {
                Value value3 = new Value();
                value3.setId((String)valueMap.get("id"));
                value3.setValue((String)valueMap.get("value"));
                singleCheckField.setValue(value3);
            }

            return singleCheckField;
        }
    }

    @SuppressWarnings("unchecked")
    private static MultiCheckField mapToMultiCheckField(Map<String, Object>  fieldMap, String fieldId, String fieldName)  {
        if(fieldMap == null) {
            return null;
        } else {
            MultiCheckField multiCheckField = (MultiCheckField) SchemaFactory.createField(FieldTypeEnum.MULTICHECK);
            setComColumn(multiCheckField, fieldMap);

            if(fieldMap.containsKey("rules")) {
                List<Map<String, Object>> rulesMap = (List<Map<String, Object>>)fieldMap.get("rules");
                Iterator<Map<String, Object>> rulesIt = rulesMap.iterator();

                List<Rule> rules = new ArrayList<>();
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

                List<Option> options = new ArrayList<>();
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

                List<Property> properties = new ArrayList<>();
                while(propertyIt.hasNext()) {
                    Map<String, Object> propertyItMap = (Map<String, Object>)propertyIt.next();
                    Property property = mapToProperty(propertyItMap, multiCheckField.getId());
                    properties.add(property);
                }
                multiCheckField.setProperties(properties);
            }

            Map<String, Object> defaultValueMap = (Map<String, Object>)fieldMap.get("defaultValueField");
            if(defaultValueMap != null) {
                List<Map<String, Object>> defaultValues = (List<Map<String, Object>>)defaultValueMap.get("values");
                if (defaultValues != null) {
                    for(Map<String, Object> defaultValueSubMap : defaultValues) {
                        String id = (String)defaultValueSubMap.get("id");
                        String value = (String)defaultValueSubMap.get("value");
                        Value valueObj = new Value();
                        valueObj.setId(id);
                        valueObj.setValue(value);
                        multiCheckField.addDefaultValueDO(valueObj);
                    }
                }
            }

            List<Map<String, Object>> valuesMapList = (List<Map<String, Object>>)fieldMap.get("values");
            if(valuesMapList != null) {
                for(Map<String, Object> valueMap : valuesMapList) {
                    String id = (String)valueMap.get("id");
                    String value = (String)valueMap.get("value");
                    Value valueObj = new Value(id, value);
                    multiCheckField.addValue(valueObj);
                }
            }

            return multiCheckField;
        }
    }

    @SuppressWarnings("unchecked")
    private static ComplexField mapToComplexField(Map<String, Object>  fieldMap, String fieldId, String fieldName)  {
        if(fieldMap == null) {
            return null;
        } else {
            ComplexField complexField = (ComplexField) SchemaFactory.createField(FieldTypeEnum.COMPLEX);
            setComColumn(complexField, fieldMap);

            if (fieldMap.containsKey("fields")) {
                List<Map<String, Object>> fieldList = (List<Map<String, Object>>)fieldMap.get("fields");
                for(Map<String, Object> subFieldMap : fieldList) {
                    Field complexSubField = mapToField(subFieldMap);
                    complexField.add(complexSubField);
                }
            }

            if(fieldMap.containsKey("rules")) {
                List<Map<String, Object>> rulesMap = (List<Map<String, Object>>)fieldMap.get("rules");
                Iterator<Map<String, Object>> rulesIt = rulesMap.iterator();

                List<Rule> rules = new ArrayList<>();
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

                List<Property> properties = new ArrayList<>();
                while(propertyIt.hasNext()) {
                    Map<String, Object> propertyItMap = (Map<String, Object>)propertyIt.next();
                    Property property = mapToProperty(propertyItMap, complexField.getId());
                    properties.add(property);
                }
                complexField.setProperties(properties);
            }

            Map<String, Object> defaultValue = (Map<String, Object>)fieldMap.get("defaultValueField");
            if(defaultValue != null) {
                Map<String, Object> complexValueMap = (Map<String, Object>)defaultValue.get("complexValue");
                if(complexValueMap != null) {
                    ComplexValue defaultComplexValue = new ComplexValue();
                    Map<String, Object> complexValueFieldMap = (Map<String, Object>)complexValueMap.get("fieldMap");
                    if (complexValueFieldMap != null) {
                        for (Object o : complexValueFieldMap.values()) {
                            Map<String, Object> complexValueSubFieldMap = (Map<String, Object>) o;
                            Field subFiledValue = mapToField(complexValueSubFieldMap);
                            defaultComplexValue.put(subFiledValue);
                        }
                    }
                    complexField.setDefaultValue(defaultComplexValue);
                }
            }

            Map<String, Object> complexValue = (Map<String, Object>)fieldMap.get("complexValue");
            if(complexValue != null) {
                ComplexValue defaultComplexValue = new ComplexValue();
                Map<String, Object> complexValueFieldMap = (Map<String, Object>)complexValue.get("fieldMap");
                if (complexValueFieldMap != null) {
                    for (Object o : complexValueFieldMap.values()) {
                        Map<String, Object> complexValueSubFieldMap = (Map<String, Object>) o;
                        Field subFiledValue = mapToField(complexValueSubFieldMap);
                        defaultComplexValue.put(subFiledValue);
                    }
                }
                complexField.setComplexValue(defaultComplexValue);
            }

            return complexField;
        }
    }

    @SuppressWarnings("unchecked")
    private static MultiComplexField mapToMultiComplexField(Map<String, Object> fieldMap, String fieldId, String fieldName)  {
        if(fieldMap == null) {
            return null;
        } else {
            MultiComplexField multiComplexField = (MultiComplexField) SchemaFactory.createField(FieldTypeEnum.MULTICOMPLEX);
            setComColumn(multiComplexField, fieldMap);

            if (fieldMap.containsKey("fields")) {
                List<Map<String, Object>> fieldList = (List<Map<String, Object>>)fieldMap.get("fields");

                for (Map<String, Object> subFieldMap : fieldList) {
                    Field complexSubField = mapToField(subFieldMap);
                    multiComplexField.add(complexSubField);
                }
            }

            if(fieldMap.containsKey("rules")) {
                List<Map<String, Object>> rulesMap = (List<Map<String, Object>>)fieldMap.get("rules");
                Iterator<Map<String, Object>> rulesIt = rulesMap.iterator();

                List<Rule> rules = new ArrayList<>();
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

                List<Property> properties = new ArrayList<>();
                while(propertyIt.hasNext()) {
                    Map<String, Object> propertyItMap = (Map<String, Object>)propertyIt.next();
                    Property property = mapToProperty(propertyItMap, multiComplexField.getId());
                    properties.add(property);
                }
                multiComplexField.setProperties(properties);
            }

            Map<String, Object> defaultValue = (Map<String, Object>)fieldMap.get("defaultValueField");
            if(defaultValue != null) {
                List<Map<String, Object>> valuesMapList = (List<Map<String, Object>>)defaultValue.get("values");
                if (valuesMapList != null) {
                    for (Map<String, Object> complexValueMap : valuesMapList) {
                        ComplexValue defaultComplexValue = new ComplexValue();
                        Map<String, Object> complexValueFieldMap = (Map<String, Object>) complexValueMap.get("fieldMap");
                        if (complexValueFieldMap != null) {
                            for (Object o : complexValueFieldMap.values()) {
                                Map<String, Object> complexValueSubFieldMap = (Map<String, Object>) o;
                                Field subFiledValue = mapToField(complexValueSubFieldMap);
                                defaultComplexValue.put(subFiledValue);
                            }
                        }
                        multiComplexField.addDefaultComplexValue(defaultComplexValue);
                    }
                }
            }

            List<Map<String, Object>> valuesMapList = (List<Map<String, Object>>)fieldMap.get("values");
            if (valuesMapList == null || valuesMapList.size() == 0) {
                valuesMapList = (List<Map<String, Object>>)fieldMap.get("complexValues");
            }
            if (valuesMapList != null) {
                for (Map<String, Object> complexValueMap : valuesMapList) {
                    ComplexValue complexValue = new ComplexValue();
                    Map<String, Object> complexValueFieldMap = (Map<String, Object>) complexValueMap.get("fieldMap");
                    if (complexValueFieldMap != null) {
                        for (Object o : complexValueFieldMap.values()) {
                            Map<String, Object> complexValueSubFieldMap = (Map<String, Object>) o;
                            Field subFiledValue = mapToField(complexValueSubFieldMap);
                            complexValue.put(subFiledValue);
                        }
                    }
                    multiComplexField.addComplexValue(complexValue);
                }
            }

            return multiComplexField;
        }
    }

}
