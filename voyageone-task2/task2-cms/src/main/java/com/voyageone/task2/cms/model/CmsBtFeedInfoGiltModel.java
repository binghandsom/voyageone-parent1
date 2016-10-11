package com.voyageone.task2.cms.model;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;

/**
 * @author morse.lu
 * @version 0.0.1, 16/3/9
 */
public class CmsBtFeedInfoGiltModel extends CmsBtFeedInfoModel {
    private String product_id;
    private String product_look_id;
    private String locale;
    private String description;
    private String brand_name;
    private String attributes_color_name;
    private String attributes_style_name;
    private String attributes_material_value;
    private String attributes_size_size_chart_id;
    private String attributes_size_type;
    private String attributes_size_value;
    private String categories_name;
    private String categories_key;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_look_id() {
        return product_look_id;
    }

    public void setProduct_look_id(String product_look_id) {
        this.product_look_id = product_look_id;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getAttributes_color_name() {
        return attributes_color_name;
    }

    public void setAttributes_color_name(String attributes_color_name) {
        this.attributes_color_name = attributes_color_name;
    }

    public String getAttributes_style_name() {
        return attributes_style_name;
    }

    public void setAttributes_style_name(String attributes_style_name) {
        this.attributes_style_name = attributes_style_name;
    }

    public String getAttributes_material_value() {
        return attributes_material_value;
    }

    public void setAttributes_material_value(String attributes_material_value) {
        this.attributes_material_value = attributes_material_value;
    }

    public String getAttributes_size_size_chart_id() {
        return attributes_size_size_chart_id;
    }

    public void setAttributes_size_size_chart_id(String attributes_size_size_chart_id) {
        this.attributes_size_size_chart_id = attributes_size_size_chart_id;
    }

    public String getAttributes_size_type() {
        return attributes_size_type;
    }

    public void setAttributes_size_type(String attributes_size_type) {
        this.attributes_size_type = attributes_size_type;
    }

    public String getAttributes_size_value() {
        return attributes_size_value;
    }

    public void setAttributes_size_value(String attributes_size_value) {
        this.attributes_size_value = attributes_size_value;
    }

    public String getCategories_name() {
        return categories_name;
    }

    public void setCategories_name(String categories_name) {
        this.categories_name = categories_name;
    }

    public String getCategories_key() {
        return categories_key;
    }

    public void setCategories_key(String categories_key) {
        this.categories_key = categories_key;
    }
}
