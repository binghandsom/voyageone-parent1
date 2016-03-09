package com.voyageone.task2.cms.model;

import java.util.Date;
import java.util.List;

public class JmBtDealImportModel {
    private Integer seq;

    private String channelId;

    private String productCode;

    private String dealId;

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

    private String specialActivityId1;
    private String shelfId1;
    private String specialActivityId2;
    private String shelfId2;
    private String specialActivityId3;
    private String shelfId3;


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

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
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

    public String getSpecialActivityId1() {
        return specialActivityId1;
    }

    public void setSpecialActivityId1(String specialActivityId1) {
        this.specialActivityId1 = specialActivityId1;
    }

    public String getShelfId1() {
        return shelfId1;
    }

    public void setShelfId1(String shelfId1) {
        this.shelfId1 = shelfId1;
    }

    public String getSpecialActivityId2() {
        return specialActivityId2;
    }

    public void setSpecialActivityId2(String specialActivityId2) {
        this.specialActivityId2 = specialActivityId2;
    }

    public String getShelfId2() {
        return shelfId2;
    }

    public void setShelfId2(String shelfId2) {
        this.shelfId2 = shelfId2;
    }

    public String getSpecialActivityId3() {
        return specialActivityId3;
    }

    public void setSpecialActivityId3(String specialActivityId3) {
        this.specialActivityId3 = specialActivityId3;
    }

    public String getShelfId3() {
        return shelfId3;
    }

    public void setShelfId3(String shelfId3) {
        this.shelfId3 = shelfId3;
    }
}