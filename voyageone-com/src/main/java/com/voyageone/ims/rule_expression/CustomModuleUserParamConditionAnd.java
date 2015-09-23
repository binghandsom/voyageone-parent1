package com.voyageone.ims.rule_expression;

/**
 * Created by Leo on 15-9-18.
 */
public class CustomModuleUserParamConditionAnd extends CustomModuleUserParam {
    private RuleExpression conditionListExpression;

    public CustomModuleUserParamConditionAnd() {
    }

    public CustomModuleUserParamConditionAnd(RuleExpression conditionListExpression) {
        this.conditionListExpression = conditionListExpression;
    }

    public RuleExpression getConditionListExpression() {
        return conditionListExpression;
    }

    public void setConditionListExpression(RuleExpression conditionListExpression) {
        this.conditionListExpression = conditionListExpression;
    }
}
