package com.voyageone.service.model.cms.mongo.feed;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by james.li on 2015/11/27.
 */
public class CmsBtFeedInfoModel_Sku {
    private Double priceCurrent;
    private Double priceMsrp;
    private Double priceNet;
    private Double priceClientRetail;
    private Double priceClientMsrp;
    private String sku;
    private String size;
    private String barcode;
    private String clientSku;
    private List<String> image;
    private Integer qty;
    private String relationshipType;
    private String variationTheme;
    private String weightOrg;
    private String weightOrgUnit;
    private String weightCalc;
    private Integer isSale;
    private String mainVid;
    private Map<String, String> attribute = new HashMap<>();
    private String errInfo;

    public Integer getIsSale() {
        return isSale == null?1:isSale;
    }

    public void setIsSale(Integer isSale) {
        this.isSale = isSale;
    }

    public Double getPriceCurrent() {
        return priceCurrent;
    }

    public void setPriceCurrent(Double priceCurrent) {
        this.priceCurrent = priceCurrent;
    }

    public Double getPriceMsrp() {
        return priceMsrp;
    }

    public void setPriceMsrp(Double priceMsrp) {
        this.priceMsrp = priceMsrp;
    }

    public Double getPriceNet() {
        return priceNet;
    }

    public void setPriceNet(Double priceNet) {
        this.priceNet = priceNet;
    }

    public Double getPriceClientRetail() {
        return priceClientRetail;
    }

    public void setPriceClientRetail(Double priceClientRetail) {
        this.priceClientRetail = priceClientRetail;
    }

    public Double getPriceClientMsrp() {
        return priceClientMsrp;
    }

    public void setPriceClientMsrp(Double priceClientMsrp) {
        this.priceClientMsrp = priceClientMsrp;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getClientSku() {
        return clientSku;
    }

    public void setClientSku(String clientSku) {
        this.clientSku = clientSku;
    }

    public Integer getQty() {
        if (qty == null) return 0;
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public List<String> getImage() { return image; }

    public void setImage(List<String> image) { this.image = image; }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public String getVariationTheme() {
        return variationTheme;
    }

    public void setVariationTheme(String variationTheme) {
        this.variationTheme = variationTheme;
    }

    @Override
    public boolean equals(Object obj) {
         return  this.sku.equalsIgnoreCase(((CmsBtFeedInfoModel_Sku)obj).getSku());
    }

    public String getWeightOrg() {
        return weightOrg;
    }

    public void setWeightOrg(String weightOrg) {
        this.weightOrg = weightOrg;
    }

    public String getWeightOrgUnit() {
        return weightOrgUnit;
    }

    public void setWeightOrgUnit(String weightOrgUnit) {
        this.weightOrgUnit = weightOrgUnit;
    }

    public String getWeightCalc() {
        return weightCalc;
    }

    public void setWeightCalc(String weightCalc) {
        this.weightCalc = weightCalc;
    }

    public Map<String, String> getAttribute() {
        return attribute;
    }

    public void setAttribute(Map<String, String> attribute) {
        this.attribute = attribute;
    }

    public String getErrInfo() {
        return errInfo;
    }

    public void setErrInfo(String errInfo) {
        this.errInfo = errInfo;
    }

    public String getMainVid() {
        return mainVid;
    }

    public void setMainVid(String mainVid) {
        this.mainVid = mainVid;
    }
}
