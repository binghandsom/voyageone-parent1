package com.voyageone.common.masterdate.schema.factory;

import com.voyageone.common.masterdate.schema.Util.JsonUtil;
import com.voyageone.common.masterdate.schema.depend.DependExpress;
import com.voyageone.common.masterdate.schema.depend.DependGroup;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.label.Label;
import com.voyageone.common.masterdate.schema.label.LabelGroup;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.masterdate.schema.property.Property;
import com.voyageone.common.masterdate.schema.rule.MaxLengthRule;
import com.voyageone.common.masterdate.schema.rule.Rule;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.masterdate.schema.value.Value;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2015/12/4.
 */
public class SchemaJsonReaderTest {

    @Test
     public void tesMapToInputField() throws Exception {
        List<Field> fieldsList = new ArrayList<>();
        InputField inputField = createInputFiled(null);
        fieldsList.add(inputField);

        String jsonStr = JsonUtil.bean2Json(fieldsList);
        System.out.println(jsonStr);

        List<Field> newFieldsList = SchemaJsonReader.readJsonForList(jsonStr);
        String newjsonStr = JsonUtil.bean2Json(newFieldsList);
        System.out.println(newjsonStr);

        if (newjsonStr.equals(jsonStr)) {
            System.out.println("equal");
        }
    }

    private InputField createInputFiled(String id) {
        InputField inputField = (InputField) SchemaFactory.createField(FieldTypeEnum.INPUT);
        if (id == null) {
            inputField.setId("InputFiled1");
        } else {
            inputField.setId(id);
        }
        inputField.setName("InputFiled1");
        inputField.setValue("Value1");
        inputField.setDefaultValue("DefaultValue1");
        //inputField.setFieldRequired();
        List<Property> properties = new ArrayList();
        properties.add(new Property("property_key1", "property_value1"));
        properties.add(new Property("property_key2", "property_value2"));
        inputField.setProperties(properties);

        List<Rule> rules = new ArrayList();

        RuleTypeEnum ruleEnum = RuleTypeEnum.getEnum("maxLengthRule");
        MaxLengthRule rule = (MaxLengthRule)SchemaFactory.createRule(ruleEnum);
        rule.setUnit("mm");
        rule.setValue("15");
        rule.setExProperty("MaxLengthRule ExProperty 1");

        DependGroup dg_result = new DependGroup();
        dg_result.setOperator("and");

        List<DependExpress> dependExpressList = new ArrayList();
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

        List<DependGroup> dependGroupList = new ArrayList();

        dependGroupList.add(new DependGroup());


        dg_result.setDependGroupList(dependGroupList);

        rule.setDependGroup(dg_result);

        rules.add(rule);

        inputField.setRules(rules);
        return inputField;
    }

    @Test
    public void testMapToMultiInputField() throws Exception {
        List<Field> fieldsList = new ArrayList<>();
        MultiInputField inputField = createMutiInputField();
        fieldsList.add(inputField);

        String jsonStr = JsonUtil.bean2Json(fieldsList);
        System.out.println(jsonStr);

        List<Field> newFieldsList = SchemaJsonReader.readJsonForList(jsonStr);
        String newjsonStr = JsonUtil.bean2Json(newFieldsList);
        System.out.println(newjsonStr);

        if (newjsonStr.equals(jsonStr)) {
            System.out.println("equal");
        }
    }

    private MultiInputField createMutiInputField() {
        MultiInputField inputField = (MultiInputField) SchemaFactory.createField(FieldTypeEnum.MULTIINPUT);
        inputField.setId("MultiInputField1");
        inputField.setName("MultiInputField11");
        inputField.addValue("Value1");

        inputField.addValue(new Value("key1", "value1"));
        inputField.addValue(new Value("key2", "value2"));

        inputField.addDefaultValue("DefaultValue1");
        inputField.addDefaultValue("DefaultValue2");

        //inputField.setFieldRequired();
        List<Property> properties = new ArrayList();
        properties.add(new Property("property_key1", "property_value1"));
        properties.add(new Property("property_key2", "property_value2"));
        inputField.setProperties(properties);

        List<Rule> rules = new ArrayList();

        RuleTypeEnum ruleEnum = RuleTypeEnum.getEnum("maxLengthRule");
        MaxLengthRule rule = (MaxLengthRule)SchemaFactory.createRule(ruleEnum);
        rule.setUnit("mm");
        rule.setValue("15");
        rule.setExProperty("MaxLengthRule ExProperty 1");

        DependGroup dg_result = new DependGroup();
        dg_result.setOperator("and");

        List<DependExpress> dependExpressList = new ArrayList();
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

        List<DependGroup> dependGroupList = new ArrayList();

        dependGroupList.add(new DependGroup());


        dg_result.setDependGroupList(dependGroupList);

        rule.setDependGroup(dg_result);

        rules.add(rule);

        inputField.setRules(rules);
        return inputField;
    }

    @Test
    public void testmapToSingleCheckField() throws TopSchemaException {
        List<Field> fieldsList = new ArrayList<>();
        SingleCheckField singleCheckField = createSingleCheckField();
        fieldsList.add(singleCheckField);
        String jsonStr = JsonUtil.bean2Json(fieldsList);
        System.out.println(jsonStr);

        List<Field> newFieldsList = SchemaJsonReader.readJsonForList(jsonStr);
        String newjsonStr = JsonUtil.bean2Json(newFieldsList);
        System.out.println(newjsonStr);

        if (newjsonStr.equals(jsonStr)) {
            System.out.println("equal");
        }
    }

    private SingleCheckField createSingleCheckField() {
        SingleCheckField singleCheckField = (SingleCheckField) SchemaFactory.createField(FieldTypeEnum.SINGLECHECK);
        singleCheckField.setId("SingleCheckField1");
        singleCheckField.setName("SingleCheckField11");
        //inputField.setFieldRequired();
        List<Property> properties = new ArrayList();
        properties.add(new Property("property_key1", "property_value1"));
        properties.add(new Property("property_key2", "property_value2"));
        singleCheckField.setProperties(properties);

        List<Rule> rules = new ArrayList();
        RuleTypeEnum ruleEnum = RuleTypeEnum.getEnum("maxLengthRule");
        MaxLengthRule rule = (MaxLengthRule)SchemaFactory.createRule(ruleEnum);
        rule.setUnit("mm");
        rule.setValue("15");
        rule.setExProperty("MaxLengthRule ExProperty 1");
        DependGroup dg_result = new DependGroup();
        dg_result.setOperator("and");
        List<DependExpress> dependExpressList = new ArrayList();
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
        List<DependGroup> dependGroupList = new ArrayList();
        dependGroupList.add(new DependGroup());
        dg_result.setDependGroupList(dependGroupList);
        rule.setDependGroup(dg_result);
        rules.add(rule);
        singleCheckField.setRules(rules);


        Option opResult = new Option();
        opResult.setDisplayName("DisplayName1");
        opResult.setValue("value11");
        opResult.setDependGroup(dg_result);
        singleCheckField.add(opResult);
        opResult = new Option();
        opResult.setDisplayName("DisplayName2");
        opResult.setValue("value12");
        opResult.setDependGroup(dg_result);
        singleCheckField.add(opResult);

        Value defaultValue = new Value("defaultValue_key1", "defaultValue_value1");
        singleCheckField.setDefaultValueDO(defaultValue);

        Value value = new Value("key1", "value1");
        singleCheckField.setValue(value);
        return singleCheckField;
    }

    @Test
    public void testMapToMultiCheckField() throws TopSchemaException {
        List<Field> fieldsList = new ArrayList<>();
        MultiCheckField multiCheckField = createMultiCheckField();
        fieldsList.add(multiCheckField);
        String jsonStr = JsonUtil.bean2Json(fieldsList);
        System.out.println(jsonStr);

        List<Field> newFieldsList = SchemaJsonReader.readJsonForList(jsonStr);
        String newjsonStr = JsonUtil.bean2Json(newFieldsList);
        System.out.println(newjsonStr);

        if (newjsonStr.equals(jsonStr)) {
            System.out.println("equal");
        }
    }

    private MultiCheckField createMultiCheckField() {
        MultiCheckField multiCheckField = (MultiCheckField) SchemaFactory.createField(FieldTypeEnum.MULTICHECK);
        multiCheckField.setId("MultiCheckField1");
        multiCheckField.setName("MultiCheckField11");
        //inputField.setFieldRequired();
        List<Property> properties = new ArrayList();
        properties.add(new Property("property_key1", "property_value1"));
        properties.add(new Property("property_key2", "property_value2"));
        multiCheckField.setProperties(properties);

        List<Rule> rules = new ArrayList();
        RuleTypeEnum ruleEnum = RuleTypeEnum.getEnum("maxLengthRule");
        MaxLengthRule rule = (MaxLengthRule)SchemaFactory.createRule(ruleEnum);
        rule.setUnit("mm");
        rule.setValue("15");
        rule.setExProperty("MaxLengthRule ExProperty 1");
        DependGroup dg_result = new DependGroup();
        dg_result.setOperator("and");
        List<DependExpress> dependExpressList = new ArrayList();
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
        List<DependGroup> dependGroupList = new ArrayList();
        dependGroupList.add(new DependGroup());
        dg_result.setDependGroupList(dependGroupList);
        rule.setDependGroup(dg_result);
        rules.add(rule);
        multiCheckField.setRules(rules);


        Option opResult = new Option();
        opResult.setDisplayName("DisplayName1");
        opResult.setValue("value11");
        opResult.setDependGroup(dg_result);
        multiCheckField.add(opResult);
        opResult = new Option();
        opResult.setDisplayName("DisplayName2");
        opResult.setValue("value12");
        opResult.setDependGroup(dg_result);
        multiCheckField.add(opResult);

        multiCheckField.addDefaultValue("defaultValue_key0");
        multiCheckField.addDefaultValueDO(new Value("defaultValue_key1", "defaultValue_value1"));
        multiCheckField.addDefaultValueDO(new Value("defaultValue_key2", "defaultValue_value2"));

        multiCheckField.addValue("key0");
        multiCheckField.addValue(new Value("key1", "value1"));
        multiCheckField.addValue(new Value("key2", "value2"));
        return multiCheckField;
    }

    @Test
    public void testMapToComplexField() throws TopSchemaException {
        List<Field> fieldsList = new ArrayList<>();
        ComplexField complexField = createComplexField();
        fieldsList.add(complexField);
        String jsonStr = JsonUtil.bean2Json(fieldsList);
        System.out.println(jsonStr);

        List<Field> newFieldsList = SchemaJsonReader.readJsonForList(jsonStr);
        String newjsonStr = JsonUtil.bean2Json(newFieldsList);
        System.out.println(newjsonStr);

        if (newjsonStr.equals(jsonStr)) {
            System.out.println("equal");
        }
    }

    private ComplexField createComplexField() {
        ComplexField complexField = (ComplexField) SchemaFactory.createField(FieldTypeEnum.COMPLEX);
        complexField.setId("createComplexField1");
        complexField.setName("createComplexField11");
        //inputField.setFieldRequired();
        List<Property> properties = new ArrayList();
        properties.add(new Property("property_key1", "property_value1"));
        properties.add(new Property("property_key2", "property_value2"));
        complexField.setProperties(properties);

        List<Rule> rules = new ArrayList();
        RuleTypeEnum ruleEnum = RuleTypeEnum.getEnum("maxLengthRule");
        MaxLengthRule rule = (MaxLengthRule)SchemaFactory.createRule(ruleEnum);
        rule.setUnit("mm");
        rule.setValue("15");
        rule.setExProperty("MaxLengthRule ExProperty 1");
        DependGroup dg_result = new DependGroup();
        dg_result.setOperator("and");
        List<DependExpress> dependExpressList = new ArrayList();
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
        List<DependGroup> dependGroupList = new ArrayList();
        dependGroupList.add(new DependGroup());
        dg_result.setDependGroupList(dependGroupList);
        rule.setDependGroup(dg_result);
        rules.add(rule);
        complexField.setRules(rules);

        complexField.add(createInputFiled("InputFiled11"));
        complexField.add(createSingleCheckField());
        complexField.add(createMultiCheckField());

        ComplexValue defaultValue = new ComplexValue();
        defaultValue.put(createInputFiled(null));
        defaultValue.put(createSingleCheckField());
        complexField.setDefaultValue(defaultValue);

        ComplexValue complexValue = new ComplexValue();
        complexValue.put(createInputFiled(null));
        complexValue.put(createSingleCheckField());
        complexField.setComplexValue(complexValue);


        return complexField;
    }

    @Test
    public void testMapToMultiComplexField() throws TopSchemaException {
        List<Field> fieldsList = new ArrayList<>();
        MultiComplexField complexField = createMultiComplexField();
        fieldsList.add(complexField);
        String jsonStr = JsonUtil.bean2Json(fieldsList);
        System.out.println(jsonStr);

        List<Field> newFieldsList = SchemaJsonReader.readJsonForList(jsonStr);
        String newjsonStr = JsonUtil.bean2Json(newFieldsList);
        System.out.println(newjsonStr);

        Field field = newFieldsList.get(0).getChildFieldById("InputFiled11");
        System.out.println(field);

        if (newjsonStr.equals(jsonStr)) {
            System.out.println("equal");
        }
    }

    private MultiComplexField createMultiComplexField() {
        MultiComplexField complexField = (MultiComplexField) SchemaFactory.createField(FieldTypeEnum.MULTICOMPLEX);
        complexField.setId("aa1");
        complexField.setName("A11");
        //inputField.setFieldRequired();
        List<Property> properties = new ArrayList();
        properties.add(new Property("property_key1", "property_value1"));
        properties.add(new Property("property_key2", "property_value2"));
        complexField.setProperties(properties);

        List<Rule> rules = new ArrayList();
        RuleTypeEnum ruleEnum = RuleTypeEnum.getEnum("maxLengthRule");
        MaxLengthRule rule = (MaxLengthRule)SchemaFactory.createRule(ruleEnum);
        rule.setUnit("mm");
        rule.setValue("15");
        rule.setExProperty("MaxLengthRule ExProperty 1");
        DependGroup dg_result = new DependGroup();
        dg_result.setOperator("and");
        List<DependExpress> dependExpressList = new ArrayList();
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
        List<DependGroup> dependGroupList = new ArrayList();
        dependGroupList.add(new DependGroup());
        dg_result.setDependGroupList(dependGroupList);
        rule.setDependGroup(dg_result);
        rules.add(rule);
        complexField.setRules(rules);

        complexField.add(createInputFiled(null));
        complexField.add(createSingleCheckField());
        complexField.add(createMultiCheckField());
        complexField.add(createComplexField());

        ComplexValue defaultValue = new ComplexValue();
        defaultValue.put(createInputFiled(null));
        defaultValue.put(createSingleCheckField());
        complexField.addDefaultComplexValue(defaultValue);
        defaultValue = new ComplexValue();
        defaultValue.put(createInputFiled(null));
        defaultValue.put(createMultiCheckField());
        complexField.addDefaultComplexValue(defaultValue);

        ComplexValue complexValue = new ComplexValue();
        complexValue.put(createInputFiled(null));
        complexValue.put(createSingleCheckField());
        complexField.addComplexValue(complexValue);
        complexValue = new ComplexValue();
        complexValue.put(createInputFiled(null));
        complexValue.put(createMultiCheckField());
        complexField.addComplexValue(complexValue);


        return complexField;
    }

    @Test
    public void tesMapToLabelField() throws Exception {
        List<Field> fieldsList = new ArrayList<>();

        LabelField labelField = (LabelField) SchemaFactory.createField(FieldTypeEnum.LABEL);
        labelField.setId("a1");
        labelField.setName("A11");
        //inputField.setFieldRequired();
        List<Property> properties = new ArrayList();
        properties.add(new Property("property_key1", "property_value1"));
        properties.add(new Property("property_key2", "property_value2"));
        labelField.setProperties(properties);

        List<Rule> rules = new ArrayList();

        RuleTypeEnum ruleEnum = RuleTypeEnum.getEnum("maxLengthRule");
        MaxLengthRule rule = (MaxLengthRule)SchemaFactory.createRule(ruleEnum);
        rule.setUnit("mm");
        rule.setValue("15");
        rule.setExProperty("MaxLengthRule ExProperty 1");

        DependGroup dg_result = new DependGroup();
        dg_result.setOperator("and");

        List<DependExpress> dependExpressList = new ArrayList();
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

        List<DependGroup> dependGroupList = new ArrayList();

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

        fieldsList.add(labelField);
        String jsonStr = JsonUtil.bean2Json(fieldsList);
        System.out.println(jsonStr);

        List<Field> newFieldsList = SchemaJsonReader.readJsonForList(jsonStr);
        String newjsonStr = JsonUtil.bean2Json(newFieldsList);
        System.out.println(newjsonStr);

        if (newjsonStr.equals(jsonStr)) {
            System.out.println("equal");
        }
    }


}
