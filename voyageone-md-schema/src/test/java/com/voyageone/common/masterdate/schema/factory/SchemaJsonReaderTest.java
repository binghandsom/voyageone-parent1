package com.voyageone.common.masterdate.schema.factory;

import com.voyageone.common.masterdate.schema.depend.DependExpress;
import com.voyageone.common.masterdate.schema.depend.DependGroup;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.masterdate.schema.property.Property;
import com.voyageone.common.masterdate.schema.rule.MaxLengthRule;
import com.voyageone.common.masterdate.schema.rule.Rule;
import com.voyageone.common.util.JsonUtil;
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

        InputField inputField = (InputField) SchemaFactory.createField(FieldTypeEnum.INPUT);
        inputField.setId("a1");
        inputField.setName("A11");
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
        dependGroupList.add(dg_result);
        dg_result.setDependGroupList(dependGroupList);

        rule.setDependGroup(dg_result);

        rules.add(rule);

        inputField.setRules(rules);

        fieldsList.add(inputField);
        String jsonStr = JsonUtil.bean2Json(fieldsList);
        System.out.println(jsonStr);

        List<Field> newFieldsList = SchemaJsonReader.readJsonForList(jsonStr);
        String newjsonStr = JsonUtil.bean2Json(newFieldsList);
        System.out.println(newjsonStr);

        if (newjsonStr.equals(jsonStr)) {
            System.out.print("equal");
        }
    }
}
