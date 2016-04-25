package com.voyageone.service.model.cms.mongo.product;


import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;
import com.voyageone.common.CmsConstants;

import java.util.List;
import java.util.Map;

/**
 * 商品Model Group Channel
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductGroupModel extends ChannelPartitionModel {

    private long groupId = 0;
    private int cartId = 0;
    private String numIId;
    private String platformPid;
    private int displayOrder = 0;
    private String publishTime;
    private String onSaleTime;
    private String instockTime;
    private String platformStatus;
    private String platformActive;

    private String mainProductCode;
    private List<String> productCodes;
    private int qty = 0;
    private double priceMsrpSt = 0;
    private double priceMsrpEd = 0;
    private double priceRetailSt = 0;
    private double priceRetailEd = 0;
    private double priceSaleSt = 0;
    private double priceSaleEd = 0;

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

    public String getInstockTime() {
        return instockTime;
    }

    public void setInstockTime(String instockTime) {
        this.instockTime = instockTime;
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

    public double getPriceSaleEd() {
        return priceSaleEd;
    }

    public void setPriceSaleEd(double priceSaleEd) {
        this.priceSaleEd = priceSaleEd;
    }

    // platform status 等待上新/在售/在库
    public CmsConstants.PlatformStatus getPlatformStatus() {
        return (platformStatus == null) ? null : CmsConstants.PlatformStatus.valueOf(platformStatus);
    }
    public void setPlatformStatus(CmsConstants.PlatformStatus platformStatus) {
        this.platformStatus = platformStatus.name();
    }

    //"Instock"(在库)/"OnSale"(在售)
    public CmsConstants.PlatformActive getPlatformActive() {
        return (platformActive == null) ? null : CmsConstants.PlatformActive.valueOf(platformActive);
    }
    public void setPlatformActive(CmsConstants.PlatformActive platformActive) {
        this.platformActive = platformActive.name();
    }

}