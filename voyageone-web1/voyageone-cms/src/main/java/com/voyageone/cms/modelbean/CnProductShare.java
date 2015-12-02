package com.voyageone.cms.modelbean;

import java.math.BigDecimal;
import java.util.Date;

public class CnProductShare  {
    private Integer freeShippingTypeId;

    private BigDecimal firstSalePrice;

    private BigDecimal cnPrice;

    private BigDecimal cnPriceDiscount;

    private BigDecimal effectivePrice;

    private BigDecimal cnPriceRmb;

    private BigDecimal cnPriceAdjustmentRmb;

    private BigDecimal cnPriceFinalRmb;

    private BigDecimal cnPriceFinalRmbDiscount;

    private Date prePublishDatetime;

    private String isOnSale;

    private String isApproved;

    private Date approvedDatetime;

    private Date created;

    private String creater;

    private Date modified;

    private String modifier;

    public Integer getFreeShippingTypeId() {
        return freeShippingTypeId;
    }

    public void setFreeShippingTypeId(Integer freeShippingTypeId) {
        this.freeShippingTypeId = freeShippingTypeId;
    }

    public BigDecimal getFirstSalePrice() {
        return firstSalePrice;
    }

    public void setFirstSalePrice(BigDecimal firstSalePrice) {
        this.firstSalePrice = firstSalePrice;
    }

    public BigDecimal getCnPrice() {
        return cnPrice;
    }

    public void setCnPrice(BigDecimal cnPrice) {
        this.cnPrice = cnPrice;
    }

    public BigDecimal getCnPriceDiscount() {
        return cnPriceDiscount;
    }

    public void setCnPriceDiscount(BigDecimal cnPriceDiscount) {
        this.cnPriceDiscount = cnPriceDiscount;
    }

    public BigDecimal getEffectivePrice() {
        return effectivePrice;
    }

    public void setEffectivePrice(BigDecimal effectivePrice) {
        this.effectivePrice = effectivePrice;
    }

    public BigDecimal getCnPriceRmb() {
        return cnPriceRmb;
    }

    public void setCnPriceRmb(BigDecimal cnPriceRmb) {
        this.cnPriceRmb = cnPriceRmb;
    }

    public BigDecimal getCnPriceAdjustmentRmb() {
        return cnPriceAdjustmentRmb;
    }

    public void setCnPriceAdjustmentRmb(BigDecimal cnPriceAdjustmentRmb) {
        this.cnPriceAdjustmentRmb = cnPriceAdjustmentRmb;
    }

    public BigDecimal getCnPriceFinalRmb() {
        return cnPriceFinalRmb;
    }

    public void setCnPriceFinalRmb(BigDecimal cnPriceFinalRmb) {
        this.cnPriceFinalRmb = cnPriceFinalRmb;
    }

    public BigDecimal getCnPriceFinalRmbDiscount() {
        return cnPriceFinalRmbDiscount;
    }

    public void setCnPriceFinalRmbDiscount(BigDecimal cnPriceFinalRmbDiscount) {
        this.cnPriceFinalRmbDiscount = cnPriceFinalRmbDiscount;
    }

    public Date getPrePublishDatetime() {
        return prePublishDatetime;
    }

    public void setPrePublishDatetime(Date prePublishDatetime) {
        this.prePublishDatetime = prePublishDatetime;
    }

    public String getIsOnSale() {
        return isOnSale;
    }

    public void setIsOnSale(String isOnSale) {
        this.isOnSale = isOnSale == null ? null : isOnSale.trim();
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved == null ? null : isApproved.trim();
    }

    public Date getApprovedDatetime() {
        return approvedDatetime;
    }

    public void setApprovedDatetime(Date approvedDatetime) {
        this.approvedDatetime = approvedDatetime;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater == null ? null : creater.trim();
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }
}