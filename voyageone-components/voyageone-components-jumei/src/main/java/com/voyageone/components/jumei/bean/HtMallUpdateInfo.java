package com.voyageone.components.jumei.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyageone.common.util.JacksonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 聚美商城 编辑商城属性[MALL]
 *
 * @author morse on 2016/8/29
 * @version 2.5.0
 */
public class HtMallUpdateInfo {

    private String jumeiMallId;  // 聚美Mall Id.

    private UpdateDataInfo updateDataInfo;

    public class UpdateDataInfo {
        private Integer shippingSystemId; // 发货仓库
        private String productLongName; // 产品长标题
        private String productMediumName; // 产品中标题
        private String productShortName; // 产品短标题
        private String beforeDate; // 保质期限
        private String suitPeople; // 适用人群
        private String specialExplain; // 特殊说明
        private String searchMetaTextCustom; // 自定义搜索词
        private String descriptionProperties; // 本单详情
        private String descriptionUsage; // 使用方法
        private String descriptionImages; // 商品实拍

        private UpdateDataInfo(){}

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
            this.productLongName = productLongName;
        }

        public String getProductMediumName() {
            return productMediumName;
        }

        public void setProductMediumName(String productMediumName) {
            this.productMediumName = productMediumName;
        }

        public String getProductShortName() {
            return productShortName;
        }

        public void setProductShortName(String productShortName) {
            this.productShortName = productShortName;
        }

        public String getBeforeDate() {
            return beforeDate;
        }

        public void setBeforeDate(String beforeDate) {
            this.beforeDate = beforeDate;
        }

        public String getSuitPeople() {
            return suitPeople;
        }

        public void setSuitPeople(String suitPeople) {
            this.suitPeople = suitPeople;
        }

        public String getSpecialExplain() {
            return specialExplain;
        }

        public void setSpecialExplain(String specialExplain) {
            this.specialExplain = specialExplain;
        }

        public String getSearchMetaTextCustom() {
            return searchMetaTextCustom;
        }

        public void setSearchMetaTextCustom(String searchMetaTextCustom) {
            this.searchMetaTextCustom = searchMetaTextCustom;
        }

        public String getDescriptionProperties() {
            return descriptionProperties;
        }

        public void setDescriptionProperties(String descriptionProperties) {
            this.descriptionProperties = descriptionProperties;
        }

        public String getDescriptionUsage() {
            return descriptionUsage;
        }

        public void setDescriptionUsage(String descriptionUsage) {
            this.descriptionUsage = descriptionUsage;
        }

        public String getDescriptionImages() {
            return descriptionImages;
        }

        public void setDescriptionImages(String descriptionImages) {
            this.descriptionImages = descriptionImages;
        }
    }

    public String getJumeiMallId() {
        return jumeiMallId;
    }

    public void setJumeiMallId(String jumeiMallId) {
        this.jumeiMallId = jumeiMallId;
    }

    public UpdateDataInfo getUpdateDataInfo() {
        if (updateDataInfo == null) {
            updateDataInfo = new UpdateDataInfo();
        }
        return updateDataInfo;
    }

    public void setUpdateDataInfo(UpdateDataInfo updateDataInfo) {
        this.updateDataInfo = updateDataInfo;
    }

    @JsonIgnore
    public String getUpdateDataString() {
        return JacksonUtil.bean2JsonNotNull(updateDataInfo);
    }

}
