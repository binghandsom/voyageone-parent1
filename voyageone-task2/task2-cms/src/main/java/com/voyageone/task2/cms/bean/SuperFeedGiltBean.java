package com.voyageone.task2.cms.bean;

import com.voyageone.common.util.MD5;

/**
 * @author Jonas, 2/3/16.
 * @version 2.0.0
 * @since 2.0.0
 */
public class SuperFeedGiltBean {

    public final static String INSERTING = "1";

    public final static String INSERTED = "3";

    public final static String UPDATING = "5";

    public final static String UPDATED = "7";

    private String id;

    private String product_id;

    private String product_look_id;

    private String locale;

    private String name;

    private String description;

    private String country_code;

    private String brand_id;

    private String brand_name;

    private String images_url;

    private String attributes_color_nfr_code;

    private String attributes_color_name;

    private String attributes_style_name;

    private String attributes_material_value;

    private String attributes_size_size_chart_id;

    private String attributes_size_type;

    private String attributes_size_value;

    private String prices_retail_currency;

    private String prices_retail_value;

    private String prices_sale_currencty;

    private String prices_sale_value;

    private String prices_cost_currency;

    private String prices_cost_value;

    private String product_codes;

    private String product_codes_first;

    private String categories_id;

    private String categories_name;

    private String categories_key;

    private String updateFlag = "0";

    private String md5;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getImages_url() {
        return images_url;
    }

    public void setImages_url(String images_url) {
        this.images_url = images_url;
    }

    public String getAttributes_color_nfr_code() {
        return attributes_color_nfr_code;
    }

    public void setAttributes_color_nfr_code(String attributes_color_nfr_code) {
        this.attributes_color_nfr_code = attributes_color_nfr_code;
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

    public String getPrices_retail_currency() {
        return prices_retail_currency;
    }

    public void setPrices_retail_currency(String prices_retail_currency) {
        this.prices_retail_currency = prices_retail_currency;
    }

    public String getPrices_retail_value() {
        return prices_retail_value;
    }

    public void setPrices_retail_value(String prices_retail_value) {
        this.prices_retail_value = prices_retail_value;
    }

    public String getPrices_sale_currencty() {
        return prices_sale_currencty;
    }

    public void setPrices_sale_currencty(String prices_sale_currencty) {
        this.prices_sale_currencty = prices_sale_currencty;
    }

    public String getPrices_sale_value() {
        return prices_sale_value;
    }

    public void setPrices_sale_value(String prices_sale_value) {
        this.prices_sale_value = prices_sale_value;
    }

    public String getPrices_cost_currency() {
        return prices_cost_currency;
    }

    public void setPrices_cost_currency(String prices_cost_currency) {
        this.prices_cost_currency = prices_cost_currency;
    }

    public String getPrices_cost_value() {
        return prices_cost_value;
    }

    public void setPrices_cost_value(String prices_cost_value) {
        this.prices_cost_value = prices_cost_value;
    }

    public String getProduct_codes() {
        return product_codes;
    }

    public void setProduct_codes(String product_codes) {
        this.product_codes = product_codes;
    }

    public String getProduct_codes_first() {
        return product_codes_first;
    }

    public void setProduct_codes_first(String product_codes_first) {
        this.product_codes_first = product_codes_first;
    }

    public String getCategories_id() {
        return categories_id;
    }

    public void setCategories_id(String categories_id) {
        this.categories_id = categories_id;
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

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }

    public String getMd5() {
        StringBuffer temp = new StringBuffer();
        temp.append(this.id);
        temp.append(this.product_id);
        temp.append(this.product_look_id);
        temp.append(this.locale);
        temp.append(this.name);
        temp.append(this.description);
        temp.append(this.country_code);
        temp.append(this.brand_id);
        temp.append(this.brand_name);
        temp.append(this.images_url);
        temp.append(this.attributes_color_nfr_code);
        temp.append(this.attributes_color_name);
        temp.append(this.attributes_style_name);
        temp.append(this.attributes_material_value);
        temp.append(this.attributes_size_size_chart_id);
        temp.append(this.attributes_size_type);
        temp.append(this.attributes_size_value);
        temp.append(this.prices_retail_currency);
        temp.append(this.prices_retail_value);
        temp.append(this.prices_sale_currencty);
        temp.append(this.prices_sale_value);
        temp.append(this.prices_cost_currency);
        temp.append(this.prices_cost_value);
        temp.append(this.product_codes);
        temp.append(this.product_codes_first);
        temp.append(this.categories_id);
        temp.append(this.categories_name);
        temp.append(this.categories_key);

        return MD5.getMD5(temp.toString());
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}






























