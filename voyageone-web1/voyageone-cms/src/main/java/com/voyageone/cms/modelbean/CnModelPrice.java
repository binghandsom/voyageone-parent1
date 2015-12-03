package com.voyageone.cms.modelbean;

import java.math.BigDecimal;
import java.util.Date;

public class CnModelPrice  {
    private Integer basePriceId;

    private BigDecimal pricingFactor;

    private BigDecimal exchangeRate;

    private String overHear1;

    private String overHear2;

    private String overHear3;

    private String overHear4;

    private String overHear5;

    private BigDecimal shippingCompensation;

    private Date created;

    private String creater;

    private Date modified;

    private String modifier;

    public Integer getBasePriceId() {
        return basePriceId;
    }

    public void setBasePriceId(Integer basePriceId) {
        this.basePriceId = basePriceId;
    }

    public BigDecimal getPricingFactor() {
        return pricingFactor;
    }

    public void setPricingFactor(BigDecimal pricingFactor) {
        this.pricingFactor = pricingFactor;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getOverHear1() {
        return overHear1;
    }

    public void setOverHear1(String overHear1) {
        this.overHear1 = overHear1 == null ? null : overHear1.trim();
    }

    public String getOverHear2() {
        return overHear2;
    }

    public void setOverHear2(String overHear2) {
        this.overHear2 = overHear2 == null ? null : overHear2.trim();
    }

    public String getOverHear3() {
        return overHear3;
    }

    public void setOverHear3(String overHear3) {
        this.overHear3 = overHear3 == null ? null : overHear3.trim();
    }

    public String getOverHear4() {
        return overHear4;
    }

    public void setOverHear4(String overHear4) {
        this.overHear4 = overHear4 == null ? null : overHear4.trim();
    }

    public String getOverHear5() {
        return overHear5;
    }

    public void setOverHear5(String overHear5) {
        this.overHear5 = overHear5 == null ? null : overHear5.trim();
    }

    public BigDecimal getShippingCompensation() {
        return shippingCompensation;
    }

    public void setShippingCompensation(BigDecimal shippingCompensation) {
        this.shippingCompensation = shippingCompensation;
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