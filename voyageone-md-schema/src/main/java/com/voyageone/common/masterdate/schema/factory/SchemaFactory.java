package com.voyageone.common.masterdate.schema.factory;

import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.rule.DefaultRule;
import com.voyageone.common.masterdate.schema.rule.Rule;
import java.util.ArrayList;
import java.util.List;

public class SchemaFactory {

    public SchemaFactory() {
    }

    public static List<Field> createEmptyFieldList() {
        return new ArrayList<>();
    }

    public static Field createField(FieldTypeEnum fieldEnum) {
        return FieldTypeEnum.createField(fieldEnum);
    }

    public static Rule createRule(RuleTypeEnum ruleType) {
        return RuleTypeEnum.createRule(ruleType);
    }

    public static Rule createCustomRule(String ruleName, String ruleValue) {
        return new DefaultRule(ruleName, ruleValue);
    }

    public static Rule createCustomRule(String ruleName, String ruleValue, String exProperty) {
        return new DefaultRule(ruleName, ruleValue, exProperty);
    }
}

