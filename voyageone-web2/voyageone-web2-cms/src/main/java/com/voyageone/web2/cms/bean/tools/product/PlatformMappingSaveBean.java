package com.voyageone.web2.cms.bean.tools.product;

import java.util.List;
import java.util.Map;

/**
 * 接收前端保存的数据类型
 * <p>
 * Created by jonas on 8/15/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
public class PlatformMappingSaveBean {

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

        private List<Map<String, Object>> product;

        private List<Map<String, Object>> item;

        public List<Map<String, Object>> getProduct() {
            return product;
        }

        public void setProduct(List<Map<String, Object>> product) {
            this.product = product;
        }

        public List<Map<String, Object>> getItem() {
            return item;
        }

        public void setItem(List<Map<String, Object>> item) {
            this.item = item;
        }
    }
}
