package com.voyageone.cms.service.bean;

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

    public static class MultiComplexCustomMappingValue {
        private List<MappingBean> subMappings;

        public MultiComplexCustomMappingValue() {
            subMappings = new ArrayList<>();
        }

        public List<MappingBean> getSubMappings() {
            return subMappings;
        }

        public void setSubMappings(List<MappingBean> subMappings) {
            this.subMappings = subMappings;
        }

        public void addSubMapping(MappingBean mappingBean) {
            this.subMappings.add(mappingBean);
        }
    }

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

        RuleExpression summaryExpression = new RuleExpression();
        summaryExpression.addRuleWord(new TextWord("summary desc"));
        SimpleMappingBean simpleMappingBean = new SimpleMappingBean("wap_desc_summary", summaryExpression);
        complexMappingBean.addSubMapping(simpleMappingBean);

        MultiComplexCustomMappingBean mappingBean = new MultiComplexCustomMappingBean();
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
