package com.voyageone.service.model.cms.mongo.search;

/**
 * 检索项-Mongodb属性和值表达式
 *
 * @Author rex.wu
 * @Create 2017-05-15 13:45
 */
public class CmsBtSearchItemModel_SearchItem {

    /**
     * 查询的Mongodb的属性
     */
    private String field;
    /**
     * 查询时值表达式
     */
    private String valExpression;


    public CmsBtSearchItemModel_SearchItem() {

    }

    public CmsBtSearchItemModel_SearchItem(String field, String valExpression) {
        this.field = field;
        this.valExpression = valExpression;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValExpression() {
        return valExpression;
    }

    public void setValExpression(String valExpression) {
        this.valExpression = valExpression;
    }
}
