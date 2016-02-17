package com.voyageone.batch.cms.bean;


import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 15-6-10.
 */
public class UploadProductRunState {
    private boolean is_dalwin;
    private List<PlatformPropBean> productPlatformProps;
    private Map<PlatformPropBean, List<String>> productPlatformPropValueMap;

    private List<Object> productFields;
    // 产品编号
    private String product_code;

    public List<Object> getProductFields() {
        return productFields;
    }

    public void setProductFields(List<Object> productFields) {
        this.productFields = productFields;
    }

    public boolean is_dalwin() {
        return is_dalwin;
    }

    public void setIs_dalwin(boolean is_dalwin) {
        this.is_dalwin = is_dalwin;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public List<PlatformPropBean> getProductPlatformProps() {
        return productPlatformProps;
    }

    public void setProductPlatformProps(List<PlatformPropBean> productPlatformProps) {
        this.productPlatformProps = productPlatformProps;
    }

    public Map<PlatformPropBean, List<String>> getProductPlatformPropValueMap() {
        return productPlatformPropValueMap;
    }

    public void setProductPlatformPropValueMap(Map<PlatformPropBean, List<String>> productPlatformPropValueMap) {
        this.productPlatformPropValueMap = productPlatformPropValueMap;
    }

    @Override
    public String toString() {
        return "["
                + "super: " + super.toString()
                + ", product_code: " + product_code
                + "]";
    }
}
