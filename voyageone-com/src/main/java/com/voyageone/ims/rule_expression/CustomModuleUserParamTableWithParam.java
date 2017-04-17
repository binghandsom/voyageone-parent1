package com.voyageone.ims.rule_expression;

import java.util.List;

/**
 * @tableTemplate html模板，如果该值为空，那么parse直接返回图片的url, 如果不为空，那么图片套用这个html模板，如果有
 *               多张图，那么每个图片都套用这个html模板，并拼接好返回
 * @trTemplate 图片模板，该值不能为空
 *
 */
public class CustomModuleUserParamTableWithParam extends CustomModuleUserParam {
    //user param
    private RuleExpression tableTemplate;

    private int columnCount;

    private List<RuleExpression> tableParams;



    public CustomModuleUserParamTableWithParam() {}

    public CustomModuleUserParamTableWithParam(RuleExpression tableTemplate, int columnCount, List<RuleExpression> tableParams) {
        this.tableTemplate = tableTemplate;
        this.columnCount = columnCount;
        this.tableParams = tableParams;
    }

    public RuleExpression getTableTemplate() {
        return tableTemplate;
    }

    public void setTableTemplate(RuleExpression tableTemplate) {
        this.tableTemplate = tableTemplate;
    }

    public List<RuleExpression> getTableParams() {
        return tableParams;
    }

    public void setTableParams(List<RuleExpression> tableParams) {
        this.tableParams = tableParams;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }
}
