package com.voyageone.ims.rule_expression;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Created by Leo on 15-7-7.
 */
public class CustomWordValueTableWithParam extends CustomWordValue{
    private CustomModuleUserParamTableWithParam userParam;
    @JsonIgnore
    public final static String moduleName = "TableWithParam";

    public CustomWordValueTableWithParam() {
    }

    public CustomWordValueTableWithParam(RuleExpression tableTemplate, int columnCount, List<RuleExpression> tableParams) {
        this.userParam = new CustomModuleUserParamTableWithParam(tableTemplate, columnCount, tableParams);
    }

    public CustomModuleUserParamTableWithParam getUserParam() {
        return userParam;
    }

    public void setUserParam(CustomModuleUserParamTableWithParam userParam) {
        this.userParam = userParam;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }
}
