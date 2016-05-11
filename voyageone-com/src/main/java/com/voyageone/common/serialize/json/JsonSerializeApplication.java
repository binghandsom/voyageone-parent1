package com.voyageone.common.serialize.json;

import com.taobao.top.schema.enums.FieldTypeEnum;
import com.taobao.top.schema.enums.RuleTypeEnum;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.InputField;
import com.taobao.top.schema.rule.Rule;
import com.voyageone.common.serialize.SerializeType;
import com.voyageone.common.serialize.SerializerFactory;

public class JsonSerializeApplication {

    public static Field constructTmallField() {
        InputField inputField = (InputField) FieldTypeEnum.createField(FieldTypeEnum.INPUT);
        inputField.setId("prop_13021751");
        inputField.setName("款号");
        Rule rule = RuleTypeEnum.createRule(RuleTypeEnum.REQUIRED_RULE);
        rule.setValue("true");
        inputField.addRule(RuleTypeEnum.REQUIRED_RULE, "true");
        inputField.setValue("123456");
        return inputField;
    }

//    public static void main(String[] args) throws Exception {
//
//        JsonSerializer serializer = SerializerFactory.getSerializer(SerializeType.JSON);
//
//        Field inputField = constructTmallField();
//
//        if (serializer != null) {
//            String jsonStr = serializer.serialize(inputField);
//            Field f = (Field) serializer.deserialize(jsonStr);
//        }
//    }

}
