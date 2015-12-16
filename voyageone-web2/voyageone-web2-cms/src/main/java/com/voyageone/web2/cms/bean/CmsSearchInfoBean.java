package com.voyageone.web2.cms.bean;

import com.voyageone.common.util.StringUtils;

/**
 * @author Edward
 * @version 2.0.0, 15/12/15
 */
public class CmsSearchInfoBean {

    private String[] productStatus;

    private String[] publishStatus;

    private String[] labelType;

    private String priceType;

    private String priceTypeStart;

    private String priceTypeEnd;

    private String createTimeStart;

    private String createTimeTo;

    private String publishTimeStart;

    private String publishTimeTo;

    private String compareType;

    private String inventory;

    private String brand;

    private String promotion;

    private String[] codeList;

    private String sortOneName;

    private String sortOneType;

    private String sortTwoName;

    private String sortTwoType;

    private String sortThreeName;

    private String sortThreeType;

    private String groupPageNum;

    private String groupPageSize;

    private String productPageNum;

    private String productPageSize;

    public String[] getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String[] productStatus) {
        this.productStatus = productStatus;
    }

    public String[] getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String[] publishStatus) {
        this.publishStatus = publishStatus;
    }

    public String[] getLabelType() {
        return labelType;
    }

    public void setLabelType(String[] labelType) {
        this.labelType = labelType;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getPriceTypeStart() {
        return priceTypeStart;
    }

    public void setPriceTypeStart(String priceTypeStart) {
        this.priceTypeStart = priceTypeStart;
    }

    public String getPriceTypeEnd() {
        return priceTypeEnd;
    }

    public void setPriceTypeEnd(String priceTypeEnd) {
        this.priceTypeEnd = priceTypeEnd;
    }

    public String getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(String createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public String getCreateTimeTo() {
        return createTimeTo;
    }

    public void setCreateTimeTo(String createTimeTo) {
        this.createTimeTo = createTimeTo;
    }

    public String getPublishTimeStart() {
        return publishTimeStart;
    }

    public void setPublishTimeStart(String publishTimeStart) {
        this.publishTimeStart = publishTimeStart;
    }

    public String getPublishTimeTo() {
        return publishTimeTo;
    }

    public void setPublishTimeTo(String publishTimeTo) {
        this.publishTimeTo = publishTimeTo;
    }

    public String getCompareType() {
        return compareType;
    }

    public void setCompareType(String compareType) {
        this.compareType = compareType;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public String[] getCodeList() {
        return codeList;
    }

    public void setCodeList(String[] codeList) {
        this.codeList = codeList;
    }

    public String getSortOneName() {
        return sortOneName;
    }

    public void setSortOneName(String sortOneName) {
        this.sortOneName = sortOneName;
    }

    public String getSortOneType() {
        return sortOneType;
    }

    public void setSortOneType(String sortOneType) {
        this.sortOneType = sortOneType;
    }

    public String getSortTwoName() {
        return sortTwoName;
    }

    public void setSortTwoName(String sortTwoName) {
        this.sortTwoName = sortTwoName;
    }

    public String getSortTwoType() {
        return sortTwoType;
    }

    public void setSortTwoType(String sortTwoType) {
        this.sortTwoType = sortTwoType;
    }

    public String getSortThreeName() {
        return sortThreeName;
    }

    public void setSortThreeName(String sortThreeName) {
        this.sortThreeName = sortThreeName;
    }

    public String getSortThreeType() {
        return sortThreeType;
    }

    public void setSortThreeType(String sortThreeType) {
        this.sortThreeType = sortThreeType;
    }

    public String getGroupPageNum() {
        return groupPageNum;
    }

    public void setGroupPageNum(String groupPageNum) {
        this.groupPageNum = StringUtils.isEmpty(groupPageNum) ? "1" : groupPageNum;
    }

    public String getGroupPageSize() {
        return groupPageSize;
    }

    public void setGroupPageSize(String groupPageSize) {
        this.groupPageSize = StringUtils.isEmpty(groupPageSize) ? "1" : groupPageSize;
    }

    public String getProductPageNum() {
        return productPageNum;
    }

    public void setProductPageNum(String productPageNum) {
        this.productPageNum = StringUtils.isEmpty(productPageNum) ? "1" : productPageNum;
    }

    public String getProductPageSize() {
        return productPageSize;
    }

    public void setProductPageSize(String productPageSize) {
        this.productPageSize = StringUtils.isEmpty(productPageSize) ? "1" : productPageSize;
    }
}
