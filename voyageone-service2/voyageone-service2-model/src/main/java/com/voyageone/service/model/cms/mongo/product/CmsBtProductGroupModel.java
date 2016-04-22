package com.voyageone.service.model.cms.mongo.product;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.CartPartitionModel;
import com.voyageone.common.CmsConstants;

import java.util.List;
import java.util.Map;

/**
 * 商品Model Group>Platform
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductGroupModel extends CartPartitionModel {

    private long groupId = 0;
    private String numIId = null;
    private String platformPid = null;
    private int displayOrder = 0;
    private String publishTime = null;
    private String onSaleTime = null;
    private String instockTime = null;
    private String platformStatus = null;
    private String platformActive = null;

    private String mainProductCode = null;
    private List<String> productCodes = null;
    private int qty = 0;
    private double priceMsrpSt = 0;
    private double priceMsrpEd = 0;
    private double priceRetailSt = 0;
    private double priceRetailEd = 0;
    private double priceSaleSt = 0;
    private double priceSaleEd = 0;

    public double getPriceSaleEd() {
        return priceSaleEd;
    }

    public void setPriceSaleEd(double priceSaleEd) {
        this.priceSaleEd = priceSaleEd;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
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

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
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

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPriceMsrpSt() {
        return priceMsrpSt;
    }

    public void setPriceMsrpSt(double priceMsrpSt) {
        this.priceMsrpSt = priceMsrpSt;
    }

    public double getPriceMsrpEd() {
        return priceMsrpEd;
    }

    public void setPriceMsrpEd(double priceMsrpEd) {
        this.priceMsrpEd = priceMsrpEd;
    }

    public double getPriceRetailSt() {
        return priceRetailSt;
    }

    public void setPriceRetailSt(double priceRetailSt) {
        this.priceRetailSt = priceRetailSt;
    }

    public double getPriceRetailEd() {
        return priceRetailEd;
    }

    public void setPriceRetailEd(double priceRetailEd) {
        this.priceRetailEd = priceRetailEd;
    }

    public double getPriceSaleSt() {
        return priceSaleSt;
    }

    public void setPriceSaleSt(double priceSaleSt) {
        this.priceSaleSt = priceSaleSt;
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