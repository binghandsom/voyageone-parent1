package com.voyageone.batch.cms.model;

import java.util.Date;
import java.util.List;

public class JmBtDealImportModel {
    private Integer seq;

    private String channelId;

    private String productCode;

    private String partnerDealId;

    private String startTime;

    private String endTime;

    private Integer userPurchaseLimit;

    private Integer shippingSystemId;

    private String productLongName;

    private String productMediumName;

    private String productShortName;

    private String addressOfProduce;

    private String searchMetaTextCustom;

    private String jumeiHashId;

    private Integer synFlg ;

    private Date created;

    private String creater;

    private Date modified;

    private String modifier;

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode == null ? null : productCode.trim();
    }

    public String getPartnerDealId() {
        return partnerDealId;
    }

    public void setPartnerDealId(String partnerDealId) {
        this.partnerDealId = partnerDealId == null ? null : partnerDealId.trim();
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime == null ? null : startTime.trim();
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime == null ? null : endTime.trim();
    }

    public Integer getUserPurchaseLimit() {
        return userPurchaseLimit;
    }

    public void setUserPurchaseLimit(Integer userPurchaseLimit) {
        this.userPurchaseLimit = userPurchaseLimit;
    }

    public Integer getShippingSystemId() {
        return shippingSystemId;
    }

    public void setShippingSystemId(Integer shippingSystemId) {
        this.shippingSystemId = shippingSystemId;
    }

    public String getProductLongName() {
        return productLongName;
    }

    public void setProductLongName(String productLongName) {
        this.productLongName = productLongName == null ? null : productLongName.trim();
    }

    public String getProductMediumName() {
        return productMediumName;
    }

    public void setProductMediumName(String productMediumName) {
        this.productMediumName = productMediumName == null ? null : productMediumName.trim();
    }

    public String getProductShortName() {
        return productShortName;
    }

    public void setProductShortName(String productShortName) {
        this.productShortName = productShortName == null ? null : productShortName.trim();
    }

    public String getAddressOfProduce() {
        return addressOfProduce;
    }

    public void setAddressOfProduce(String addressOfProduce) {
        this.addressOfProduce = addressOfProduce == null ? null : addressOfProduce.trim();
    }

    public String getSearchMetaTextCustom() {
        return searchMetaTextCustom;
    }

    public void setSearchMetaTextCustom(String searchMetaTextCustom) {
        this.searchMetaTextCustom = searchMetaTextCustom == null ? null : searchMetaTextCustom.trim();
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

    public Integer getSynFlg() {
        return synFlg;
    }

    public void setSynFlg(Integer synFlg) {
        this.synFlg = synFlg;
    }

    public String getJumeiHashId() {
        return jumeiHashId;
    }

    public void setJumeiHashId(String jumeiHashId) {
        this.jumeiHashId = jumeiHashId;
    }
}