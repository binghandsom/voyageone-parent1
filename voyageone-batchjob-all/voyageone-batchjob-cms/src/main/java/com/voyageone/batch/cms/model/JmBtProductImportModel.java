package com.voyageone.batch.cms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JmBtProductImportModel {
    private Integer seq;

    private String channelId;

    private String dealId;

    private String attribute;

    private String addressOfProduce;

    private String productCode;

    private String productDes;

    private Integer categoryLv4Id;

    private Integer brandId;

    private String brandName;

    private String sizeType;

    private String productName;

    private String foreignLanguageName;

    private String functionIds;

    private String uploadErrorInfo;

    private String synFlg;

    private List<JmBtSkuImportModel> skuImportModelList;

    private JmBtDealImportModel jmBtDealImportModel;

    private String jumeiProductId;

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

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getAddressOfProduce() {
        return addressOfProduce;
    }

    public void setAddressOfProduce(String addressOfProduce) {
        this.addressOfProduce = addressOfProduce;
    }

    public String getProductDes() {
        return productDes;
    }

    public void setProductDes(String productDes) {
        this.productDes = productDes == null ? null : productDes.trim();
    }

    public Integer getCategoryLv4Id() {
        return categoryLv4Id;
    }

    public void setCategoryLv4Id(Integer categoryLv4Id) {
        this.categoryLv4Id = categoryLv4Id;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getForeignLanguageName() {
        return foreignLanguageName;
    }

    public void setForeignLanguageName(String foreignLanguageName) {
        this.foreignLanguageName = foreignLanguageName == null ? null : foreignLanguageName.trim();
    }

    public String getFunctionIds() {
        return functionIds;
    }

    public void setFunctionIds(String functionIds) {
        this.functionIds = functionIds == null ? null : functionIds.trim();
    }

    public String getSynFlg() {
        return synFlg;
    }

    public void setSynFlg(String synFlg) {
        this.synFlg = synFlg == null ? null : synFlg.trim();
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

    public String getJumeiProductId() {
        return jumeiProductId;
    }

    public void setJumeiProductId(String jumeiProductId) {
        this.jumeiProductId = jumeiProductId;
    }

    public List<JmBtSkuImportModel> getSkuImportModelList() {
        if(skuImportModelList == null)
        {
            this.skuImportModelList = new ArrayList<>();
        }
        return skuImportModelList;
    }

    public void setSkuImportModelList(List<JmBtSkuImportModel> skuImportModelList) {
        this.skuImportModelList = skuImportModelList;
    }

    public JmBtDealImportModel getJmBtDealImportModel() {
        return jmBtDealImportModel;
    }

    public void setJmBtDealImportModel(JmBtDealImportModel jmBtDealImportModel) {
        this.jmBtDealImportModel = jmBtDealImportModel;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSizeType() {
        return sizeType;
    }

    public void setSizeType(String sizeType) {
        this.sizeType = sizeType;
    }

    public String getUploadErrorInfo() {
        return uploadErrorInfo;
    }

    public void setUploadErrorInfo(String uploadErrorInfo) {
        this.uploadErrorInfo = uploadErrorInfo;
    }
}