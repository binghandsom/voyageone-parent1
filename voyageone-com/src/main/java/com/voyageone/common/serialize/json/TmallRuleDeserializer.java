package com.voyageone.common.serialize.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.taobao.top.schema.depend.DependGroup;
import com.taobao.top.schema.enums.RuleTypeEnum;
import com.taobao.top.schema.rule.Rule;

public class TmallRuleDeserializer extends JsonDeserializer<Rule> {

    public static final String RULE_NAME = "name";
    public static final String RULE_DEPEND_GROUP = "dependGroup";
    public static final String RULE_EX_PROPERTY = "exProperty";
    public static final String RULE_VALUE = "value";

    private TmallSerializeModule module;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Rule deserialize(JsonParser p,
                            DeserializationContext ctxt) throws IOException {
        TokenBuffer tb = new TokenBuffer(p);
        tb.copyCurrentStructure(p);

        Rule rule = getRuleInstance(ctxt, tb);

        JsonDeserializer ruleDeserializer = ctxt.findRootValueDeserializer(ctxt.getTypeFactory().constructType(rule.getClass()));
        rule = (Rule) ruleDeserializer.deserialize(p, ctxt, rule);
        return rule;
    }

    Rule getRuleInstance(DeserializationContext ctxt, TokenBuffer tb) throws IOException {
        String rule_name = "", rule_ex_property = "", rule_value = "";
        DependGroup dependGroup = null;
        Rule rule = null;

        JsonParser jp = tb.asParser();
        jp.nextToken();
        do {
            String fieldName = jp.getCurrentName();
            if (RULE_NAME.equals(fieldName)) {
                rule_name = jp.nextTextValue();
            } else if (RULE_DEPEND_GROUP.equals(fieldName)) {
                if (jp.nextValue() == JsonToken.VALUE_NULL) {
                    break;
                } else {
                    JsonDeserializer<Object> dependGroupDeserializer = ctxt.findRootValueDeserializer(ctxt.getTypeFactory().constructType(DependGroup.class));
                    dependGroup = (DependGroup) dependGroupDeserializer.deserialize(jp, ctxt);
                }
            } else if (RULE_EX_PROPERTY.equals(fieldName)) {
                rule_ex_property = jp.nextTextValue();
            } else if (RULE_VALUE.equals(fieldName)) {
                rule_value = jp.nextTextValue();
            }
        } while (jp.nextToken() != JsonToken.END_OBJECT);

        rule = RuleTypeEnum.createRule(RuleTypeEnum.getEnum(rule_name));

        if (rule != null) {
            rule.setDependGroup(dependGroup);
            rule.setExProperty(rule_ex_property);
            rule.setValue(rule_value);
        }
        return rule;
    }

    public TmallSerializeModule getModule() {
        return module;
    }

    public void setModule(TmallSerializeModule module) {
        this.module = module;
    }
}
