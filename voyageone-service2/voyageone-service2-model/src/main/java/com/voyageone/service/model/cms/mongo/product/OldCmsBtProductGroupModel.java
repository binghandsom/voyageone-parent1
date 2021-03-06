package com.voyageone.service.model.cms.mongo.product;


import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;
import com.voyageone.common.CmsConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品Model Group Channel
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class OldCmsBtProductGroupModel extends ChannelPartitionModel {

    private Long groupId;
    private Integer cartId;
    private String numIId;
    private String platformPid;
    private Integer displayOrder;
    private String publishTime;
    private String onSaleTime;
    private String inStockTime;
    private String platformStatus;
    private String platformActive;

    private String mainProductCode;
    private List<String> productCodes;
    private Integer qty;
    private Double priceMsrpSt;
    private Double priceMsrpEd;
    private Double priceRetailSt;
    private Double priceRetailEd;
    private Double priceSaleSt;
    private Double priceSaleEd;
    private Map sales = new HashMap<>();

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getNumIId() {
        return numIId;
    }

    public void setNumIId(String numIId) {
        this.numIId = numIId;
    }

    public String getPlatformPid() {
        return platformPid;
    }

    public void setPlatformPid(String platformPid) {
        this.platformPid = platformPid;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getOnSaleTime() {
        return onSaleTime;
    }

    public void setOnSaleTime(String onSaleTime) {
        this.onSaleTime = onSaleTime;
    }

    public String getInStockTime() {
        return inStockTime;
    }

    public void setInStockTime(String inStockTime) {
        this.inStockTime = inStockTime;
    }

    public String getMainProductCode() {
        return mainProductCode;
    }

    public void setMainProductCode(String mainProductCode) {
        this.mainProductCode = mainProductCode;
    }

    public List<String> getProductCodes() {
        return productCodes;
    }

    public void setProductCodes(List<String> productCodes) {
        this.productCodes = productCodes;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getPriceMsrpSt() {
        return priceMsrpSt;
    }

    public void setPriceMsrpSt(Double priceMsrpSt) {
        this.priceMsrpSt = priceMsrpSt;
    }

    public Double getPriceMsrpEd() {
        return priceMsrpEd;
    }

    public void setPriceMsrpEd(Double priceMsrpEd) {
        this.priceMsrpEd = priceMsrpEd;
    }

    public Double getPriceRetailSt() {
        return priceRetailSt;
    }

    public void setPriceRetailSt(Double priceRetailSt) {
        this.priceRetailSt = priceRetailSt;
    }

    public Double getPriceRetailEd() {
        return priceRetailEd;
    }

    public void setPriceRetailEd(Double priceRetailEd) {
        this.priceRetailEd = priceRetailEd;
    }

    public Double getPriceSaleSt() {
        return priceSaleSt;
    }

    public void setPriceSaleSt(Double priceSaleSt) {
        this.priceSaleSt = priceSaleSt;
    }

    public Double getPriceSaleEd() {
        return priceSaleEd;
    }

    public void setPriceSaleEd(Double priceSaleEd) {
        this.priceSaleEd = priceSaleEd;
    }

    // platform status 等待上新/在售/在库
    public CmsConstants.PlatformStatus getPlatformStatus() {
        CmsConstants.PlatformStatus rs = null;
        try {
            rs = (platformStatus == null || platformStatus.isEmpty()) ? null : CmsConstants.PlatformStatus.valueOf(platformStatus);
        } catch (IllegalArgumentException ignored) {
        }
        return rs;
    }
    public void setPlatformStatus(CmsConstants.PlatformStatus platformStatus) {
        this.platformStatus = platformStatus.name();
    }

    //"Instock"(在库)/"OnSale"(在售)
    public CmsConstants.PlatformActive getPlatformActive() {
        CmsConstants.PlatformActive rs = null;
        try {
            rs = (platformActive == null || platformActive.isEmpty()) ? null : CmsConstants.PlatformActive.valueOf(platformActive);
        } catch (IllegalArgumentException ignored) {
        }
        return rs;
    }

    public void setPlatformActive(CmsConstants.PlatformActive platformActive) {
        this.platformActive = platformActive.name();
    }

    public Map getSales() {
        return sales;
    }

    public void setSales(Map sales) {
        this.sales = sales;
    }
}