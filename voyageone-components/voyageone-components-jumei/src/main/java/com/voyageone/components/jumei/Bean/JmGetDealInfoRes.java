package com.voyageone.components.jumei.bean;

/**
 * @author aooer 2016/1/28.
 * @version 2.0.0
 * @since 2.0.0
 */
public class JmGetDealInfoRes extends JmBaseBean {

    private String hash_id;	//	Hash_id.
    private Integer on_sale_status;	//	可售状态，可售返回 1..
    private Integer product_id;	//	商品 ID.
    private String product_long_name;	//	长标题.
    private String product_medium_name;	//	中标题.
    private String product_short_name;	//	短标题.
    private String url;	//	详情页 url. ####待定
    private String wapurl;	//	详情页 wap 地址. ####待定
    private Double original_price;	//	原价
    private Double discounted_price;	//	折扣价
    private Double discount;	//	折扣
    private Integer stocks;	//	库存
    private Integer real_buyer_number;	//	真实购买人数
    private Long brand_id;	//	品牌ID
    private Long start_time;	//	开始时间.
    private Long end_time;	//	结束时间.
    private Integer user_purchase_limit;	//	限购数量.
    private Long shipping_system_id;	//	仓库ID.
    private String address_of_produce;	//	生产地区.
    private String before_date;	//	保质期限.
    private String suit_people;	//	适用人群.
    private String special_explain;	//	特殊说明.
    private String search_meta_text_custom;	//	自定义搜索词.
    private String description_properties;	//	本单详情.
    private String description_usage;	//	使用方法.
    private String description_images;	//	商品实拍.

    public String getHash_id() {
        return hash_id;
    }

    public void setHash_id(String hash_id) {
        this.hash_id = hash_id;
    }

    public Integer getOn_sale_status() {
        return on_sale_status;
    }

    public void setOn_sale_status(Integer on_sale_status) {
        this.on_sale_status = on_sale_status;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWapurl() {
        return wapurl;
    }

    public void setWapurl(String wapurl) {
        this.wapurl = wapurl;
    }

    public Double getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(Double original_price) {
        this.original_price = original_price;
    }

    public Double getDiscounted_price() {
        return discounted_price;
    }

    public void setDiscounted_price(Double discounted_price) {
        this.discounted_price = discounted_price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getStocks() {
        return stocks;
    }

    public void setStocks(Integer stocks) {
        this.stocks = stocks;
    }

    public Integer getReal_buyer_number() {
        return real_buyer_number;
    }

    public void setReal_buyer_number(Integer real_buyer_number) {
        this.real_buyer_number = real_buyer_number;
    }

    public Long getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(Long brand_id) {
        this.brand_id = brand_id;
    }

    public Long getStart_time() {
        return start_time;
    }

    public void setStart_time(Long start_time) {
        this.start_time = start_time;
    }

    public Long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Long end_time) {
        this.end_time = end_time;
    }

    public Integer getUser_purchase_limit() {
        return user_purchase_limit;
    }

    public void setUser_purchase_limit(Integer user_purchase_limit) {
        this.user_purchase_limit = user_purchase_limit;
    }

    public Long getShipping_system_id() {
        return shipping_system_id;
    }

    public void setShipping_system_id(Long shipping_system_id) {
        this.shipping_system_id = shipping_system_id;
    }

    public String getAddress_of_produce() {
        return address_of_produce;
    }

    public void setAddress_of_produce(String address_of_produce) {
        this.address_of_produce = address_of_produce;
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
