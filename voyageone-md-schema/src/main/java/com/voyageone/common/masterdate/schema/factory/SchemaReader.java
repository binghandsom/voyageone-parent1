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
import org.dom4j.Element;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SchemaReader {
    public SchemaReader() {
    }

    public static Map<String, Field> readXmlForMap(File file) throws TopSchemaException {
        Element rootEle = XmlUtils.getRootElementFromFile(file);
        return readXmlForMap(rootEle);
    }

    public static Map<String, Field> readXmlForMap(String xmlStirng) throws TopSchemaException {
        Element rootEle = XmlUtils.getRootElementFromString(xmlStirng);
        return readXmlForMap(rootEle);
    }

    public static List<Field> readXmlForList(File file) throws TopSchemaException {
        Element rootEle = XmlUtils.getRootElementFromFile(file);
        return readXmlForList(rootEle);
    }

    public static List<Field> readXmlForList(String xmlStirng) throws TopSchemaException {
        Element rootEle = XmlUtils.getRootElementFromString(xmlStirng);
        return readXmlForList(rootEle);
    }

    public static List<Field> readXmlForList(Element rootEle) throws TopSchemaException {
        List fieldList = com.voyageone.common.masterdate.schema.factory.SchemaFactory.createEmptyFieldList();
        List fieldElmList = XmlUtils.getChildElements(rootEle, "field");
        Iterator i$ = fieldElmList.iterator();

        while(i$.hasNext()) {
            Element fieldElm = (Element)i$.next();
            Field field = elementToField(fieldElm);
            fieldList.add(field);
        }

        return fieldList;
    }

    public static Map<String, Field> readXmlForMap(Element rootEle) throws TopSchemaException {
        HashMap fieldMap = new HashMap();
        List fieldElmList = XmlUtils.getChildElements(rootEle, "field");
        Iterator i$ = fieldElmList.iterator();

        while(i$.hasNext()) {
            Element fieldElm = (Element)i$.next();
            Field field = elementToField(fieldElm);
            fieldMap.put(field.getId(), field);
        }

        return fieldMap;
    }

    public static Field elementToField(Element fieldElm) throws TopSchemaException {
        if(fieldElm == null) {
            return null;
        } else {
            String fieldId = XmlUtils.getAttributeValue(fieldElm, "id");
            if(StringUtil.isEmpty(fieldId)) {
                throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30001, (String)null);
            } else {
                String fieldType = XmlUtils.getAttributeValue(fieldElm, "type");
                if(StringUtil.isEmpty(fieldType)) {
                    throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30002, fieldId);
                } else {
                    String fieldName = XmlUtils.getAttributeValue(fieldElm, "name");
                    FieldTypeEnum fieldEnum = FieldTypeEnum.getEnum(fieldType);
                    if(fieldEnum == null) {
                        throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_30003, fieldId);
                    } else {
                        Object field_result = null;
                        switch(fieldEnum) {
                            case INPUT:
                                field_result = elementToInputField(fieldElm, fieldId, fieldName);
                                break;
                            case SINGLECHECK:
                                field_result = elementToSingleCheckField(fieldElm, fieldId, fieldName);
                                break;
                            case COMPLEX:
                                field_result = elementToComplexField(fieldElm, fieldId, fieldName);
                                break;
                            case MULTICHECK:
                                field_result = elementToMultiCheckField(fieldElm, fieldId, fieldName);
                                break;
                            case MULTICOMPLEX:
                                field_result = elementToMultiComplexField(fieldElm, fieldId, fieldName);
                                break;
                            case MULTIINPUT:
                                field_result = elementToMultiInputField(fieldElm, fieldId, fieldName);
                                break;
                            case LABEL:
                                field_result = elementToLabelField(fieldElm, fieldId, fieldName);
                        }

                        return (Field)field_result;
                    }
                }
            }
        }
    }

    private static Rule elementToRule(Element ruleEle, String fieldId) throws TopSchemaException {
        if(ruleEle == null) {
            return null;
        } else {
            String ruleName = XmlUtils.getAttributeValue(ruleEle, "name");
            if(StringUtil.isEmpty(ruleName)) {
                throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_31001, fieldId);
            } else {
                String ruleValue = XmlUtils.getAttributeValue(ruleEle, "value");
                if(StringUtil.isEmpty(ruleValue)) {
                    throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_31002, fieldId);
                } else {
                    Rule rule = null;
                    RuleTypeEnum ruleEnum = RuleTypeEnum.getEnum(ruleName);
                    if(ruleEnum != null) {
                        rule = com.voyageone.common.masterdate.schema.factory.SchemaFactory.createRule(ruleEnum);
                    } else {
                        rule = com.voyageone.common.masterdate.schema.factory.SchemaFactory.createCustomRule(ruleName, ruleValue);
                    }

                    String unit;
                    if(ruleName.equals(RuleTypeEnum.TIP_RULE.value()) && !StringUtil.isEmpty(ruleValue)) {
                        unit = XmlUtils.getAttributeValue(ruleEle, "url");
                        ((TipRule)rule).setUrl(unit);
                    }

                    if(ruleName.equals(RuleTypeEnum.DEV_TIP_RULE.value()) && !StringUtil.isEmpty(ruleValue)) {
                        unit = XmlUtils.getAttributeValue(ruleEle, "url");
                        ((DevTipRule)rule).setUrl(unit);
                    }

                    unit = XmlUtils.getAttributeValue(ruleEle, "unit");
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

                    String exProperty4 = XmlUtils.getAttributeValue(ruleEle, "exProperty");
                    if(!StringUtil.isEmpty(exProperty4)) {
                        rule.setExProperty(exProperty4);
                    }

                    rule.setValue(ruleValue);
                    Element dependGroupEle = XmlUtils.getChildElement(ruleEle, "depend-group");
                    DependGroup dependGroup = elementToDependGroup(dependGroupEle, fieldId);
                    rule.setDependGroup(dependGroup);
                    return rule;
                }
            }
        }
    }

    private static DependGroup elementToDependGroup(Element dependGroupEle, String fieldId) throws TopSchemaException {
        if(dependGroupEle == null) {
            return null;
        } else {
            String dependGroupOperator = XmlUtils.getAttributeValue(dependGroupEle, "operator");
            if(StringUtil.isEmpty(dependGroupOperator)) {
                dependGroupOperator = "and";
            }

            DependGroup dg_result = new DependGroup();
            dg_result.setOperator(dependGroupOperator);
            List deEleList = XmlUtils.getChildElements(dependGroupEle, "depend-express");
            Iterator dgEleList = deEleList.iterator();

            while(dgEleList.hasNext()) {
                Element i$ = (Element)dgEleList.next();
                DependExpress dgSubEle = new DependExpress();
                String subGroup = XmlUtils.getAttributeValue(i$, "fieldId");
                String deValue = XmlUtils.getAttributeValue(i$, "value");
                String deSymbol = XmlUtils.getAttributeValue(i$, "symbol");
                dgSubEle.setFieldId(subGroup);
                dgSubEle.setValue(deValue);
                dgSubEle.setSymbol(deSymbol);
                dg_result.add(dgSubEle);
            }

            List dgEleList1 = XmlUtils.getChildElements(dependGroupEle, "depend-group");
            Iterator i$1 = dgEleList1.iterator();

            while(i$1.hasNext()) {
                Element dgSubEle1 = (Element)i$1.next();
                new DependGroup();
                DependGroup subGroup1 = elementToDependGroup(dgSubEle1, fieldId);
                dg_result.add(subGroup1);
            }

            return dg_result;
        }
    }

    private static LabelGroup elementToLabelGroup(Element labelGroupEle, String fieldId) throws TopSchemaException {
        if(labelGroupEle == null) {
            return null;
        } else {
            String name = XmlUtils.getAttributeValue(labelGroupEle, "name");
            LabelGroup lg_result = new LabelGroup();
            lg_result.setName(name);
            List labelEleList = XmlUtils.getChildElements(labelGroupEle, "label");
            Iterator labelGroupEleList = labelEleList.iterator();

            while(labelGroupEleList.hasNext()) {
                Element i$ = (Element)labelGroupEleList.next();
                Label subLabelGroupEle = new Label();
                String subGroup = XmlUtils.getAttributeValue(i$, "name");
                String labelValue = XmlUtils.getAttributeValue(i$, "value");
                String labelDesc = XmlUtils.getAttributeValue(i$, "desc");
                subLabelGroupEle.setName(subGroup);
                subLabelGroupEle.setValue(labelValue);
                subLabelGroupEle.setDesc(labelDesc);
                lg_result.add(subLabelGroupEle);
            }

            List labelGroupEleList1 = XmlUtils.getChildElements(labelGroupEle, "label-group");
            Iterator i$1 = labelGroupEleList1.iterator();

            while(i$1.hasNext()) {
                Element subLabelGroupEle1 = (Element)i$1.next();
                new LabelGroup();
                LabelGroup subGroup1 = elementToLabelGroup(subLabelGroupEle1, fieldId);
                lg_result.add(subGroup1);
            }

            return lg_result;
        }
    }

    private static Option elementToOption(Element optionEle, String fieldId) throws TopSchemaException {
        Option opResult = new Option();
        String displayName = XmlUtils.getAttributeValue(optionEle, "displayName");
        if(StringUtil.isEmpty(displayName)) {
            throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_33001, fieldId);
        } else {
            String value = XmlUtils.getAttributeValue(optionEle, "value");
            if(StringUtil.isEmpty(value)) {
                throw new TopSchemaException(TopSchemaErrorCodeEnum.ERROR_CODE_33002, fieldId);
            } else {
                opResult.setDisplayName(displayName);
                opResult.setValue(value);
                Element dependGroupEle = XmlUtils.getChildElement(optionEle, "depend-group");
                DependGroup dependGroup = elementToDependGroup(dependGroupEle, fieldId);
                opResult.setDependGroup(dependGroup);
                return opResult;
            }
        }
    }

    private static Property elementToProperty(Element propertyEle, String fieldId) throws TopSchemaException {
        String key = XmlUtils.getAttributeValue(propertyEle, "key");
        String value = XmlUtils.getAttributeValue(propertyEle, "value");
        Property property = new Property(key, value);
        return property;
    }

    private static InputField elementToInputField(Element fieldElm, String fieldId, String fieldName) throws TopSchemaException {
        if(fieldElm == null) {
            return null;
        } else {
            InputField inputField = (InputField) com.voyageone.common.masterdate.schema.factory.SchemaFactory.createField(FieldTypeEnum.INPUT);
            inputField.setId(fieldId);
            inputField.setName(fieldName);
            Element rulesEle = XmlUtils.getChildElement(fieldElm, "rules");
            Element value;
            if(rulesEle != null) {
                List propertiesEle = XmlUtils.getChildElements(rulesEle, "rule");
                Iterator defaultValueEle = propertiesEle.iterator();

                while(defaultValueEle.hasNext()) {
                    value = (Element)defaultValueEle.next();
                    Rule propertyEle = elementToRule(value, inputField.getId());
                    inputField.add(propertyEle);
                }
            }

            Element propertiesEle1 = XmlUtils.getChildElement(fieldElm, "properties");
            if(propertiesEle1 != null) {
                List defaultValueEle1 = XmlUtils.getChildElements(propertiesEle1, "property");
                Iterator value1 = defaultValueEle1.iterator();

                while(value1.hasNext()) {
                    Element propertyEle1 = (Element)value1.next();
                    Property property = elementToProperty(propertyEle1, inputField.getId());
                    inputField.add(property);
                }
            }

            Element defaultValueEle2 = XmlUtils.getChildElement(fieldElm, "default-value");
            if(defaultValueEle2 != null) {
                String value2 = defaultValueEle2.getText();
                inputField.setDefaultValue(value2);
            }

            value = XmlUtils.getChildElement(fieldElm, "value");
            if(value != null) {
                inputField.setValue(XmlUtils.getElementValue(value));
            }

            return inputField;
        }
    }

    private static LabelField elementToLabelField(Element fieldElm, String fieldId, String fieldName) throws TopSchemaException {
        if(fieldElm == null) {
            return null;
        } else {
            LabelField labelField = (LabelField) com.voyageone.common.masterdate.schema.factory.SchemaFactory.createField(FieldTypeEnum.LABEL);
            labelField.setId(fieldId);
            labelField.setName(fieldName);
            Element rulesEle = XmlUtils.getChildElement(fieldElm, "rules");
            if(rulesEle != null) {
                List propertiesEle = XmlUtils.getChildElements(rulesEle, "rule");
                Iterator labelGroupEle = propertiesEle.iterator();

                while(labelGroupEle.hasNext()) {
                    Element labelGroup = (Element)labelGroupEle.next();
                    Rule propertyEle = elementToRule(labelGroup, labelField.getId());
                    labelField.add(propertyEle);
                }
            }

            Element propertiesEle1 = XmlUtils.getChildElement(fieldElm, "properties");
            if(propertiesEle1 != null) {
                List labelGroupEle1 = XmlUtils.getChildElements(propertiesEle1, "property");
                Iterator labelGroup1 = labelGroupEle1.iterator();

                while(labelGroup1.hasNext()) {
                    Element propertyEle1 = (Element)labelGroup1.next();
                    Property property = elementToProperty(propertyEle1, labelField.getId());
                    labelField.add(property);
                }
            }

            Element labelGroupEle2 = XmlUtils.getChildElement(fieldElm, "label-group");
            if(labelGroupEle2 != null) {
                LabelGroup labelGroup2 = elementToLabelGroup(labelGroupEle2, fieldId);
                labelField.setLabelGroup(labelGroup2);
            }

            return labelField;
        }
    }

    private static MultiInputField elementToMultiInputField(Element fieldElm, String fieldId, String fieldName) throws TopSchemaException {
        if(fieldElm == null) {
            return null;
        } else {
            MultiInputField multiInputField = (MultiInputField) com.voyageone.common.masterdate.schema.factory.SchemaFactory.createField(FieldTypeEnum.MULTIINPUT);
            multiInputField.setId(fieldId);
            multiInputField.setName(fieldName);
            Element rulesEle = XmlUtils.getChildElement(fieldElm, "rules");
            Element valuesEle;
            if(rulesEle != null) {
                List propertiesEle = XmlUtils.getChildElements(rulesEle, "rule");
                Iterator defaultValuesEle = propertiesEle.iterator();

                while(defaultValuesEle.hasNext()) {
                    valuesEle = (Element)defaultValuesEle.next();
                    Rule valueEleList = elementToRule(valuesEle, multiInputField.getId());
                    multiInputField.add(valueEleList);
                }
            }

            Element propertiesEle1 = XmlUtils.getChildElement(fieldElm, "properties");
            if(propertiesEle1 != null) {
                List defaultValuesEle1 = XmlUtils.getChildElements(propertiesEle1, "property");
                Iterator valuesEle1 = defaultValuesEle1.iterator();

                while(valuesEle1.hasNext()) {
                    Element valueEleList1 = (Element)valuesEle1.next();
                    Property i$ = elementToProperty(valueEleList1, multiInputField.getId());
                    multiInputField.add(i$);
                }
            }

            Element defaultValuesEle2 = XmlUtils.getChildElement(fieldElm, "default-values");
            if(defaultValuesEle2 != null) {
                List valuesEle2 = XmlUtils.getChildElements(defaultValuesEle2, "default-value");
                Iterator valueEleList2 = valuesEle2.iterator();

                while(valueEleList2.hasNext()) {
                    Element i$1 = (Element)valueEleList2.next();
                    String valueEle = i$1.getText();
                    multiInputField.addDefaultValue(valueEle);
                }
            }

            valuesEle = XmlUtils.getChildElement(fieldElm, "values");
            if(valuesEle != null) {
                List valueEleList3 = XmlUtils.getChildElements(valuesEle, "value");
                Iterator i$2 = valueEleList3.iterator();

                while(i$2.hasNext()) {
                    Element valueEle1 = (Element)i$2.next();
                    Value value = new Value();
                    value.setValue(XmlUtils.getElementValue(valueEle1));
                    multiInputField.addValue(value);
                }
            }

            return multiInputField;
        }
    }

    private static SingleCheckField elementToSingleCheckField(Element fieldElm, String fieldId, String fieldName) throws TopSchemaException {
        if(fieldElm == null) {
            return null;
        } else {
            SingleCheckField singleCheckField = (SingleCheckField) com.voyageone.common.masterdate.schema.factory.SchemaFactory.createField(FieldTypeEnum.SINGLECHECK);
            singleCheckField.setId(fieldId);
            singleCheckField.setName(fieldName);
            Element rulesEle = XmlUtils.getChildElement(fieldElm, "rules");
            Element defaultValueEle;
            if(rulesEle != null) {
                List optionsEle = XmlUtils.getChildElements(rulesEle, "rule");
                Iterator propertiesEle = optionsEle.iterator();

                while(propertiesEle.hasNext()) {
                    defaultValueEle = (Element)propertiesEle.next();
                    Rule valueEle = elementToRule(defaultValueEle, singleCheckField.getId());
                    singleCheckField.add(valueEle);
                }
            }

            Element optionsEle1 = XmlUtils.getChildElement(fieldElm, "options");
            Element valueEle1;
            if(optionsEle1 != null) {
                List propertiesEle1 = XmlUtils.getChildElements(optionsEle1, "option");
                Iterator defaultValueEle1 = propertiesEle1.iterator();

                while(defaultValueEle1.hasNext()) {
                    valueEle1 = (Element)defaultValueEle1.next();
                    Option value = elementToOption(valueEle1, singleCheckField.getId());
                    singleCheckField.add(value);
                }
            }

            Element propertiesEle2 = XmlUtils.getChildElement(fieldElm, "properties");
            if(propertiesEle2 != null) {
                List defaultValueEle2 = XmlUtils.getChildElements(propertiesEle2, "property");
                Iterator valueEle2 = defaultValueEle2.iterator();

                while(valueEle2.hasNext()) {
                    Element value1 = (Element)valueEle2.next();
                    Property property = elementToProperty(value1, singleCheckField.getId());
                    singleCheckField.add(property);
                }
            }

            defaultValueEle = XmlUtils.getChildElement(fieldElm, "default-value");
            if(defaultValueEle != null) {
                Value valueEle3 = new Value();
                String value2 = defaultValueEle.getText();
                valueEle3.setValue(value2);
                singleCheckField.setDefaultValueDO(valueEle3);
            }

            valueEle1 = XmlUtils.getChildElement(fieldElm, "value");
            if(valueEle1 != null) {
                Value value3 = new Value();
                value3.setValue(XmlUtils.getElementValue(valueEle1));
                singleCheckField.setValue(value3);
            }

            return singleCheckField;
        }
    }

    private static MultiCheckField elementToMultiCheckField(Element fieldElm, String fieldId, String fieldName) throws TopSchemaException {
        if(fieldElm == null) {
            return null;
        } else {
            MultiCheckField multiCheckField = (MultiCheckField) com.voyageone.common.masterdate.schema.factory.SchemaFactory.createField(FieldTypeEnum.MULTICHECK);
            multiCheckField.setId(fieldId);
            multiCheckField.setName(fieldName);
            Element rulesEle = XmlUtils.getChildElement(fieldElm, "rules");
            Element defaultValuesEle;
            if(rulesEle != null) {
                List optionsEle = XmlUtils.getChildElements(rulesEle, "rule");
                Iterator propertiesEle = optionsEle.iterator();

                while(propertiesEle.hasNext()) {
                    defaultValuesEle = (Element)propertiesEle.next();
                    Rule valuesEle = elementToRule(defaultValuesEle, multiCheckField.getId());
                    multiCheckField.add(valuesEle);
                }
            }

            Element optionsEle1 = XmlUtils.getChildElement(fieldElm, "options");
            Element valuesEle1;
            if(optionsEle1 != null) {
                List propertiesEle1 = XmlUtils.getChildElements(optionsEle1, "option");
                Iterator defaultValuesEle1 = propertiesEle1.iterator();

                while(defaultValuesEle1.hasNext()) {
                    valuesEle1 = (Element)defaultValuesEle1.next();
                    Option valueEleList = elementToOption(valuesEle1, multiCheckField.getId());
                    multiCheckField.add(valueEleList);
                }
            }

            Element propertiesEle2 = XmlUtils.getChildElement(fieldElm, "properties");
            if(propertiesEle2 != null) {
                List defaultValuesEle2 = XmlUtils.getChildElements(propertiesEle2, "property");
                Iterator valuesEle2 = defaultValuesEle2.iterator();

                while(valuesEle2.hasNext()) {
                    Element valueEleList1 = (Element)valuesEle2.next();
                    Property i$ = elementToProperty(valueEleList1, multiCheckField.getId());
                    multiCheckField.add(i$);
                }
            }

            defaultValuesEle = XmlUtils.getChildElement(fieldElm, "default-values");
            if(defaultValuesEle != null) {
                List valuesEle3 = XmlUtils.getChildElements(defaultValuesEle, "default-value");
                Iterator valueEleList2 = valuesEle3.iterator();

                while(valueEleList2.hasNext()) {
                    Element i$1 = (Element)valueEleList2.next();
                    Value valueEle = new Value();
                    String value = i$1.getText();
                    valueEle.setValue(value);
                    multiCheckField.addDefaultValueDO(valueEle);
                }
            }

            valuesEle1 = XmlUtils.getChildElement(fieldElm, "values");
            if(valuesEle1 != null) {
                List valueEleList3 = XmlUtils.getChildElements(valuesEle1, "value");
                Iterator i$2 = valueEleList3.iterator();

                while(i$2.hasNext()) {
                    Element valueEle1 = (Element)i$2.next();
                    Value value1 = new Value();
                    value1.setValue(XmlUtils.getElementValue(valueEle1));
                    multiCheckField.addValue(value1);
                }
            }

            return multiCheckField;
        }
    }

    private static MultiComplexField elementToMultiComplexField(Element fieldElm, String fieldId, String fieldName) throws TopSchemaException {
        if(fieldElm == null) {
            return null;
        } else {
            MultiComplexField multiComplexField = (MultiComplexField) com.voyageone.common.masterdate.schema.factory.SchemaFactory.createField(FieldTypeEnum.MULTICOMPLEX);
            multiComplexField.setId(fieldId);
            multiComplexField.setName(fieldName);
            Element fieldsEle = XmlUtils.getChildElement(fieldElm, "fields");
            Element defaultValuesEle;
            if(fieldsEle != null) {
                List rulesEle = XmlUtils.getChildElements(fieldsEle, "field");
                Iterator propertiesEle = rulesEle.iterator();

                while(propertiesEle.hasNext()) {
                    defaultValuesEle = (Element)propertiesEle.next();
                    Field complexValuesEle = elementToField(defaultValuesEle);
                    multiComplexField.add(complexValuesEle);
                }
            }

            Element rulesEle1 = XmlUtils.getChildElement(fieldElm, "rules");
            if(rulesEle1 != null) {
                List propertiesEle1 = XmlUtils.getChildElements(rulesEle1, "rule");
                Iterator defaultValuesEle1 = propertiesEle1.iterator();

                while(defaultValuesEle1.hasNext()) {
                    Element complexValuesEle1 = (Element)defaultValuesEle1.next();
                    Rule i$ = elementToRule(complexValuesEle1, multiComplexField.getId());
                    multiComplexField.add(i$);
                }
            }

            Element propertiesEle2 = XmlUtils.getChildElement(fieldElm, "properties");
            if(propertiesEle2 != null) {
                List defaultValuesEle2 = XmlUtils.getChildElements(propertiesEle2, "property");
                Iterator complexValuesEle2 = defaultValuesEle2.iterator();

                while(complexValuesEle2.hasNext()) {
                    Element i$2 = (Element)complexValuesEle2.next();
                    Property complexValue = elementToProperty(i$2, multiComplexField.getId());
                    multiComplexField.add(complexValue);
                }
            }

            defaultValuesEle = XmlUtils.getChildElement(fieldElm, "default-values");
            List defaultValuesSubFieldList;
            ComplexValue cvalue;
            Iterator i$1;
            Element subFiledValueEle;
            Field field;
            List complexValuesEle3;
            Iterator i$3;
            Element complexValue1;
            if(defaultValuesEle != null) {
                complexValuesEle3 = XmlUtils.getChildElements(defaultValuesEle, "default-complex-values");
                i$3 = complexValuesEle3.iterator();

                while(i$3.hasNext()) {
                    complexValue1 = (Element)i$3.next();
                    defaultValuesSubFieldList = XmlUtils.getChildElements(complexValue1, "field");
                    cvalue = new ComplexValue();
                    i$1 = defaultValuesSubFieldList.iterator();

                    while(i$1.hasNext()) {
                        subFiledValueEle = (Element)i$1.next();
                        field = elementToField(subFiledValueEle);
                        cvalue.put(field);
                    }

                    multiComplexField.addDefaultComplexValue(cvalue);
                }
            }

            complexValuesEle3 = XmlUtils.getChildElements(fieldElm, "complex-values");
            i$3 = complexValuesEle3.iterator();

            while(i$3.hasNext()) {
                complexValue1 = (Element)i$3.next();
                defaultValuesSubFieldList = XmlUtils.getChildElements(complexValue1, "field");
                cvalue = new ComplexValue();
                i$1 = defaultValuesSubFieldList.iterator();

                while(i$1.hasNext()) {
                    subFiledValueEle = (Element)i$1.next();
                    field = elementToField(subFiledValueEle);
                    cvalue.put(field);
                }

                multiComplexField.addComplexValue(cvalue);
            }

            return multiComplexField;
        }
    }

    private static ComplexField elementToComplexField(Element fieldElm, String fieldId, String fieldName) throws TopSchemaException {
        if(fieldElm == null) {
            return null;
        } else {
            ComplexField complexField = (ComplexField) com.voyageone.common.masterdate.schema.factory.SchemaFactory.createField(FieldTypeEnum.COMPLEX);
            complexField.setId(fieldId);
            complexField.setName(fieldName);
            Element fieldsEle = XmlUtils.getChildElement(fieldElm, "fields");
            Element defaultComplexValueEle;
            if(fieldsEle != null) {
                List rulesEle = XmlUtils.getChildElements(fieldsEle, "field");
                Iterator propertiesEle = rulesEle.iterator();

                while(propertiesEle.hasNext()) {
                    defaultComplexValueEle = (Element)propertiesEle.next();
                    Field complexValueEle = elementToField(defaultComplexValueEle);
                    complexField.add(complexValueEle);
                }
            }

            Element rulesEle1 = XmlUtils.getChildElement(fieldElm, "rules");
            Element complexValueEle1;
            if(rulesEle1 != null) {
                List propertiesEle1 = XmlUtils.getChildElements(rulesEle1, "rule");
                Iterator defaultComplexValueEle1 = propertiesEle1.iterator();

                while(defaultComplexValueEle1.hasNext()) {
                    complexValueEle1 = (Element)defaultComplexValueEle1.next();
                    Rule valuesSubFieldList = elementToRule(complexValueEle1, complexField.getId());
                    complexField.add(valuesSubFieldList);
                }
            }

            Element propertiesEle2 = XmlUtils.getChildElement(fieldElm, "properties");
            if(propertiesEle2 != null) {
                List defaultComplexValueEle2 = XmlUtils.getChildElements(propertiesEle2, "property");
                Iterator complexValueEle2 = defaultComplexValueEle2.iterator();

                while(complexValueEle2.hasNext()) {
                    Element valuesSubFieldList1 = (Element)complexValueEle2.next();
                    Property cvalue = elementToProperty(valuesSubFieldList1, complexField.getId());
                    complexField.add(cvalue);
                }
            }

            defaultComplexValueEle = XmlUtils.getChildElement(fieldElm, "default-complex-values");
            if(defaultComplexValueEle != null) {
                List complexValueEle3 = XmlUtils.getChildElements(defaultComplexValueEle, "field");
                ComplexValue valuesSubFieldList2 = new ComplexValue();
                Iterator cvalue1 = complexValueEle3.iterator();

                while(cvalue1.hasNext()) {
                    Element i$ = (Element)cvalue1.next();
                    Field subFiledValueEle = elementToField(i$);
                    valuesSubFieldList2.put(subFiledValueEle);
                }

                complexField.setDefaultValue(valuesSubFieldList2);
            }

            complexValueEle1 = XmlUtils.getChildElement(fieldElm, "complex-values");
            if(complexValueEle1 != null) {
                List valuesSubFieldList3 = XmlUtils.getChildElements(complexValueEle1, "field");
                ComplexValue cvalue2 = new ComplexValue();
                Iterator i$1 = valuesSubFieldList3.iterator();

                while(i$1.hasNext()) {
                    Element subFiledValueEle1 = (Element)i$1.next();
                    Field field = elementToField(subFiledValueEle1);
                    cvalue2.put(field);
                }

                complexField.setComplexValue(cvalue2);
            }

            return complexField;
        }
    }
}
