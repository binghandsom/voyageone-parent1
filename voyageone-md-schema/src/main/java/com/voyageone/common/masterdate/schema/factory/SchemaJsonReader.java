package com.voyageone.common.masterdate.schema.factory;

import com.voyageone.common.masterdate.schema.Util.StringUtil;
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

import java.util.*;

public class SchemaJsonReader {

    /**
     * readJsonForMap
     * @param jsonStirng
     * @return
     * @throws TopSchemaException
     */
    public static Map<String, Field> readJsonForMap(String jsonStirng) throws TopSchemaException {
        List<Map<String, Object>> rootList = JsonUtil.jsonToMapList(jsonStirng);
        return readJsonForMap(rootList);
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

    private static LabelGroup mapToLabelGroup(Map<String, Object> labelGroupMap, String fieldId) throws TopSchemaException {
        if(labelGroupMap == null) {
            return null;
        } else {
            LabelGroup lg_result = new LabelGroup();
            String name = (String)labelGroupMap.get("name");
            lg_result.setName(name);

            List<Map<String, Object>> labelList = (List<Map<String, Object>>)labelGroupMap.get("labelList");
            if (labelList != null) {
                Iterator<Map<String, Object>> labelListIt = labelList.iterator();

                while(labelListIt.hasNext()) {
                    Map<String, Object> labelMap = labelListIt.next();
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
                Iterator labelGroupListIt = labelGroupList.iterator();
                while(labelGroupListIt.hasNext()) {
                    Map<String, Object> subLabelGroup = (Map<String, Object>)labelGroupListIt.next();
//                    new LabelGroup();
                    LabelGroup subGroup1 = mapToLabelGroup(subLabelGroup, fieldId);
                    lg_result.add(subGroup1);
                }
            }

            return lg_result;
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

            if(fieldMap.containsKey("labelGroup")) {
                Map<String, Object> labelGroupMap = (Map<String, Object>)fieldMap.get("labelGroup");
                LabelGroup labelGroup = mapToLabelGroup(labelGroupMap, fieldId);
                labelField.setLabelGroup(labelGroup);
            }

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

            Map<String, Object> defaultValueMap = (Map<String, Object>)fieldMap.get("defaultValueField");
            if(defaultValueMap != null) {
                List<Map<String, Object>> defaultValues = (List<Map<String, Object>>)defaultValueMap.get("values");
                if (defaultValues != null) {
                    Iterator<Map<String, Object>> defaultValuesIt = defaultValues.iterator();
                    while(defaultValuesIt.hasNext()) {
                        Map<String, Object> defaultValueSubMap = defaultValuesIt.next();
                        String defaultValueStr = (String)defaultValueSubMap.get("value");
                        multiInputField.addDefaultValue(defaultValueStr);
                    }
                }
            }

            List<Map<String, Object>> valuesMapList = (List<Map<String, Object>>)fieldMap.get("values");
            if(valuesMapList != null) {
                Iterator<Map<String, Object>> valuesMapListIt = valuesMapList.iterator();
                while(valuesMapListIt.hasNext()) {
                    Map<String, Object> valueMap = valuesMapListIt.next();
                    String id = (String)valueMap.get("id");
                    String value = (String)valueMap.get("value");
                    Value valueObj = new Value(id, value);
                    multiInputField.addValue(valueObj);
                }
            }

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

            Map<String, Object> defaultValueMap = (Map<String, Object>)fieldMap.get("defaultValueField");
            if(defaultValueMap != null) {
                List<Map<String, Object>> defaultValues = (List<Map<String, Object>>)defaultValueMap.get("values");
                if (defaultValues != null) {
                    Iterator<Map<String, Object>> defaultValuesIt = defaultValues.iterator();
                    while(defaultValuesIt.hasNext()) {
                        Map<String, Object> defaultValueSubMap = defaultValuesIt.next();
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
                Iterator<Map<String, Object>> valuesMapListIt = valuesMapList.iterator();
                while(valuesMapListIt.hasNext()) {
                    Map<String, Object> valueMap = valuesMapListIt.next();
                    String id = (String)valueMap.get("id");
                    String value = (String)valueMap.get("value");
                    Value valueObj = new Value(id, value);
                    multiCheckField.addValue(valueObj);
                }
            }

            return multiCheckField;
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

            Map<String, Object> defaultValue = (Map<String, Object>)fieldMap.get("defaultValueField");
            if(defaultValue != null) {
                Map<String, Object> complexValueMap = (Map<String, Object>)defaultValue.get("complexValue");
                if(complexValueMap != null) {
                    ComplexValue defaultComplexValue = new ComplexValue();
                    Map<String, Object> complexValueFieldMap = (Map<String, Object>)complexValueMap.get("fieldMap");
                    if (complexValueFieldMap != null) {
                        Iterator complexValueFieldMapIt = complexValueFieldMap.values().iterator();
                        while(complexValueFieldMapIt.hasNext()) {
                            Map<String, Object> complexValueSubFieldMap = (Map<String, Object>)complexValueFieldMapIt.next();
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
                    Iterator complexValueFieldMapIt = complexValueFieldMap.values().iterator();
                    while(complexValueFieldMapIt.hasNext()) {
                        Map<String, Object> complexValueSubFieldMap = (Map<String, Object>)complexValueFieldMapIt.next();
                        Field subFiledValue = mapToField(complexValueSubFieldMap);
                        defaultComplexValue.put(subFiledValue);
                    }
                }
                complexField.setComplexValue(defaultComplexValue);
            }

            return complexField;
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

            Map<String, Object> defaultValue = (Map<String, Object>)fieldMap.get("defaultValueField");
            if(defaultValue != null) {
                List<Map<String, Object>> valuesMapList = (List<Map<String, Object>>)defaultValue.get("values");
                if (valuesMapList != null) {
                    Iterator<Map<String, Object>> valuesMapIt = valuesMapList.iterator();
                    while(valuesMapIt.hasNext()) {
                        Map<String, Object> complexValueMap = (Map<String, Object>)valuesMapIt.next();
                        ComplexValue defaultComplexValue = new ComplexValue();
                        Map<String, Object> complexValueFieldMap = (Map<String, Object>)complexValueMap.get("fieldMap");
                        if (complexValueFieldMap != null) {
                            Iterator complexValueFieldMapIt = complexValueFieldMap.values().iterator();
                            while(complexValueFieldMapIt.hasNext()) {
                                Map<String, Object> complexValueSubFieldMap = (Map<String, Object>)complexValueFieldMapIt.next();
                                Field subFiledValue = mapToField(complexValueSubFieldMap);
                                defaultComplexValue.put(subFiledValue);
                            }
                        }
                        multiComplexField.addDefaultComplexValue(defaultComplexValue);
                    }
                }
            }

            List<Map<String, Object>> valuesMapList = (List<Map<String, Object>>)fieldMap.get("values");
            if (valuesMapList != null) {
                Iterator<Map<String, Object>> valuesMapIt = valuesMapList.iterator();
                while(valuesMapIt.hasNext()) {
                    Map<String, Object> complexValueMap = (Map<String, Object>)valuesMapIt.next();
                    ComplexValue complexValue = new ComplexValue();
                    Map<String, Object> complexValueFieldMap = (Map<String, Object>)complexValueMap.get("fieldMap");
                    if (complexValueFieldMap != null) {
                        Iterator complexValueFieldMapIt = complexValueFieldMap.values().iterator();
                        while(complexValueFieldMapIt.hasNext()) {
                            Map<String, Object> complexValueSubFieldMap = (Map<String, Object>)complexValueFieldMapIt.next();
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
