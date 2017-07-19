package com.voyageone.service.model.cms.mongo.product;

import java.util.Date;

/**
 * Created by yangjindong on 2017/6/21.
 */
public class CmsBtPriceLogModel_History {

    private Double msrpPrice;
    private Double retailPrice;
    private Double salePrice;
    private Double clientMsrpPrice;
    private Double clientRetailPrice;
    private Double clientNetPrice;
    private String comment;
    private Date created;
    private String creater;
    private Date modified;
    private String modifier;

    public Double getMsrpPrice() {
        return msrpPrice;
    }

    public void setMsrpPrice(Double msrpPrice) {
        this.msrpPrice = msrpPrice;
    }

    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Double getClientMsrpPrice() {
        return clientMsrpPrice;
    }

    public void setClientMsrpPrice(Double clientMsrpPrice) {
        this.clientMsrpPrice = clientMsrpPrice;
    }

    public Double getClientRetailPrice() {
        return clientRetailPrice;
    }

    public void setClientRetailPrice(Double clientRetailPrice) {
        this.clientRetailPrice = clientRetailPrice;
    }

    public Double getClientNetPrice() {
        return clientNetPrice;
    }

    public void setClientNetPrice(Double clientNetPrice) {
        this.clientNetPrice = clientNetPrice;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
        this.creater = creater;
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
        this.modifier = modifier;
    }
}
