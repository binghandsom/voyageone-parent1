package com.voyageone.service.bean.cms;

import com.voyageone.ims.rule_expression.MasterWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.TextWord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 15-12-9.
 */
public class MultiComplexCustomMappingBean extends MappingBean {
    private List<MultiComplexCustomMappingValue> values;

    public List<MultiComplexCustomMappingValue> getValues() {
        return values;
    }

    public void setValues(List<MultiComplexCustomMappingValue> values) {
        this.values = values;
    }

    public MultiComplexCustomMappingBean() {
        mappingType = MAPPING_MULTICOMPLEX_CUSTOM;
        values = new ArrayList<>();
    }

    public void addValue(MultiComplexCustomMappingValue value) {
        values.add(value);
    }

    public static void main(String[] args) {
        ComplexMappingBean complexMappingBean = new ComplexMappingBean();
        complexMappingBean.setPlatformPropId("wap_desc");

        RuleExpression summaryExpression = new RuleExpression();
        summaryExpression.addRuleWord(new TextWord("summary desc"));
        SimpleMappingBean simpleMappingBean = new SimpleMappingBean("wap_desc_summary", summaryExpression);
        complexMappingBean.addSubMapping(simpleMappingBean);

        MultiComplexCustomMappingBean mappingBean = new MultiComplexCustomMappingBean();
        mappingBean.setPlatformPropId("wap_desc_content");
        MultiComplexCustomMappingValue value1 = new MultiComplexCustomMappingValue();
        RuleExpression ruleExpression11 = new RuleExpression();
        RuleExpression ruleExpression12 = new RuleExpression();
        ruleExpression11.addRuleWord(new TextWord("image"));
        ruleExpression12.addRuleWord(new TextWord("https://img.alicdn.com/imgextra/i4/2183719539/TB2WnY7iVXXXXXSXpXXXXXXXXXX_!!2183719539.jpg"));
        value1.addSubMapping(new SimpleMappingBean("wap_desc_content_type", ruleExpression11));
        value1.addSubMapping(new SimpleMappingBean("wap_desc_content_content", ruleExpression12));

        MultiComplexCustomMappingValue value2 = new MultiComplexCustomMappingValue();
        RuleExpression ruleExpression21 = new RuleExpression();
        RuleExpression ruleExpression22 = new RuleExpression();
        ruleExpression21.addRuleWord(new TextWord("text"));
        ruleExpression22.addRuleWord(new MasterWord("longTitle"));
        value2.addSubMapping(new SimpleMappingBean("wap_desc_content_type", ruleExpression21));
        value2.addSubMapping(new SimpleMappingBean("wap_desc_content_content", ruleExpression22));
        mappingBean.addValue(value1);
        mappingBean.addValue(value2);

        complexMappingBean.addSubMapping(mappingBean);
    }
}
