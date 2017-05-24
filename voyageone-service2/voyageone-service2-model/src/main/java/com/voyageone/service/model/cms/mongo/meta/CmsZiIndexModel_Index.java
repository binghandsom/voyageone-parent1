package com.voyageone.service.model.cms.mongo.meta;

/**
 * {@link CmsZiIndexModel_Index} 的商品Model
 *
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsZiIndexModel_Index {

    private String name;
    private String key;
    private Boolean unique;
    private Boolean required;
    private Boolean sparse;
    private String partialFilterExpression;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    public Boolean getSparse() {
        return sparse;
    }

    public void setSparse(Boolean sparse) {
        this.sparse = sparse;
    }

    public String getPartialFilterExpression() {
        return partialFilterExpression;
    }

    public void setPartialFilterExpression(String partialFilterExpression) {
        this.partialFilterExpression = partialFilterExpression;
    }
}