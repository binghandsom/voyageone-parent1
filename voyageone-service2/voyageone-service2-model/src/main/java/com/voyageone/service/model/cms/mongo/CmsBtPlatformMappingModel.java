package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

import java.util.List;
import java.util.Map;

/**
 * 平台类目的属性匹配
 * <p>
 * Created by jonas on 8/13/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
public class CmsBtPlatformMappingModel extends ChannelPartitionModel {

    private Integer cartId;

    private Integer categoryType;

    private String categoryPath;

    private Map<String, FieldMapping> mappings;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public String getCategoryPath() {
        return categoryPath;
    }

    public void setCategoryPath(String categoryPath) {
        this.categoryPath = categoryPath;
    }

    public Map<String, FieldMapping> getMappings() {
        return mappings;
    }

    public void setMappings(Map<String, FieldMapping> mappings) {
        this.mappings = mappings;
    }

    public static class FieldMapping {

        private String fieldId;

        private Object value;

        private Map<String, FieldMapping> children;

        private List<FieldMappingExpression> expressions;

        public String getFieldId() {
            return fieldId;
        }

        public void setFieldId(String fieldId) {
            this.fieldId = fieldId;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Map<String, FieldMapping> getChildren() {
            return children;
        }

        public void setChildren(Map<String, FieldMapping> children) {
            this.children = children;
        }

        public List<FieldMappingExpression> getExpressions() {
            return expressions;
        }

        public void setExpressions(List<FieldMappingExpression> expressions) {
            this.expressions = expressions;
        }
    }

    public static class FieldMappingExpression {

        private String type;

        private String value;

        private String append;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getAppend() {
            return append;
        }

        public void setAppend(String append) {
            this.append = append;
        }
    }
}
