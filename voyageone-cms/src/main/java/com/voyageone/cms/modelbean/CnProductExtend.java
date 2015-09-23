package com.voyageone.cms.modelbean;

import java.math.BigDecimal;
import java.util.Date;

public class CnProductExtend  {
    private String cnAbstract;

    private String cnShortDescription;

    private String displayImages;

    private BigDecimal referenceMsrp;

    private BigDecimal referencePrice;

    private Integer hsCodeId;

    private Integer hsCodePuId;

    private Date created;

    private String creater;

    private Date modified;

    private String modifier;

    private String cnLongDescription;

    public String getCnAbstract() {
        return cnAbstract;
    }

    public void setCnAbstract(String cnAbstract) {
        this.cnAbstract = cnAbstract == null ? null : cnAbstract.trim();
    }

    public String getCnShortDescription() {
        return cnShortDescription;
    }

    public void setCnShortDescription(String cnShortDescription) {
        this.cnShortDescription = cnShortDescription == null ? null : cnShortDescription.trim();
    }

    public String getDisplayImages() {
        return displayImages;
    }

    public void setDisplayImages(String displayImages) {
        this.displayImages = displayImages == null ? null : displayImages.trim();
    }

    public BigDecimal getReferenceMsrp() {
        return referenceMsrp;
    }

    public void setReferenceMsrp(BigDecimal referenceMsrp) {
        this.referenceMsrp = referenceMsrp;
    }

    public BigDecimal getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(BigDecimal referencePrice) {
        this.referencePrice = referencePrice;
    }

    public Integer getHsCodeId() {
        return hsCodeId;
    }

    public void setHsCodeId(Integer hsCodeId) {
        this.hsCodeId = hsCodeId;
    }

    public Integer getHsCodePuId() {
        return hsCodePuId;
    }

    public void setHsCodePuId(Integer hsCodePuId) {
        this.hsCodePuId = hsCodePuId;
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

    public String getCnLongDescription() {
        return cnLongDescription;
    }

    public void setCnLongDescription(String cnLongDescription) {
        this.cnLongDescription = cnLongDescription == null ? null : cnLongDescription.trim();
    }
}