package com.voyageone.components.solr.bean;

import java.util.List;

/**
 * CmsProductSearchModel
 *
 * @author chuanyu.liang 2016/10/8
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsProductSearchModel {

    private String id;
    private Long lastVer;
    private String productModel;
    private String productCode;
    private String productChannel;
    private List<String> skuCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getLastVer() {
        return lastVer;
    }

    public void setLastVer(Long lastVer) {
        this.lastVer = lastVer;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductChannel() {
        return productChannel;
    }

    public void setProductChannel(String productChannel) {
        this.productChannel = productChannel;
    }

    public List<String> getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(List<String> skuCode) {
        this.skuCode = skuCode;
    }
}
