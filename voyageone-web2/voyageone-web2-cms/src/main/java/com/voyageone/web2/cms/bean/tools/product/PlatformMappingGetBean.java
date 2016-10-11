package com.voyageone.web2.cms.bean.tools.product;

import com.voyageone.common.masterdate.schema.field.Field;

import java.util.List;

/**
 * 返回给前端的单行模型
 * <p>
 * Created by jonas on 8/15/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
public class PlatformMappingGetBean {

    private Integer cartId;

    private Integer categoryType;

    private String categoryPath;

    private Schema schema;

    private String modified;

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

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public static class Schema {

        private List<Field> product;

        private List<Field> item;

        public List<Field> getProduct() {
            return product;
        }

        public void setProduct(List<Field> product) {
            this.product = product;
        }

        public List<Field> getItem() {
            return item;
        }

        public void setItem(List<Field> item) {
            this.item = item;
        }
    }
}
