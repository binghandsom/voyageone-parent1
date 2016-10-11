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
        private Integer shipping_system_id; // 发货仓库
        private String product_long_name; // 产品长标题
        private String product_medium_name; // 产品中标题
        private String product_short_name; // 产品短标题
        private String before_date; // 保质期限
        private String suit_people; // 适用人群
        private String special_explain; // 特殊说明
        private String search_meta_text_custom; // 自定义搜索词
        private String description_properties; // 本单详情
        private String description_usage; // 使用方法
        private String description_images; // 商品实拍

        private UpdateDataInfo(){}

        public Integer getShipping_system_id() {
            return shipping_system_id;
        }

        public void setShipping_system_id(Integer shipping_system_id) {
            this.shipping_system_id = shipping_system_id;
        }

        public String getProduct_long_name() {
            return product_long_name;
        }

        public void setProduct_long_name(String product_long_name) {
            this.product_long_name = product_long_name;
        }

        public String getProduct_medium_name() {
            return product_medium_name;
        }

        public void setProduct_medium_name(String product_medium_name) {
            this.product_medium_name = product_medium_name;
        }

        public String getProduct_short_name() {
            return product_short_name;
        }

        public void setProduct_short_name(String product_short_name) {
            this.product_short_name = product_short_name;
        }

        public String getBefore_date() {
            return before_date;
        }

        public void setBefore_date(String before_date) {
            this.before_date = before_date;
        }

        public String getSuit_people() {
            return suit_people;
        }

        public void setSuit_people(String suit_people) {
            this.suit_people = suit_people;
        }

        public String getSpecial_explain() {
            return special_explain;
        }

        public void setSpecial_explain(String special_explain) {
            this.special_explain = special_explain;
        }

        public String getSearch_meta_text_custom() {
            return search_meta_text_custom;
        }

        public void setSearch_meta_text_custom(String search_meta_text_custom) {
            this.search_meta_text_custom = search_meta_text_custom;
        }

        public String getDescription_properties() {
            return description_properties;
        }

        public void setDescription_properties(String description_properties) {
            this.description_properties = description_properties;
        }

        public String getDescription_usage() {
            return description_usage;
        }

        public void setDescription_usage(String description_usage) {
            this.description_usage = description_usage;
        }

        public String getDescription_images() {
            return description_images;
        }

        public void setDescription_images(String description_images) {
            this.description_images = description_images;
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
