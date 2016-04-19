package com.voyageone.components.jd.bean;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 京东商品对象结构
 * 属性项目是从com.jd.open.api.sdk.domain.ware.sku中取得
 *
 * Created on 2016/04/18
 *
 * @author desmond
 * @version 2.0.0
 * @since 2.0.0
 */
public class JdSkuBean {
    private Long skuId;              // Skuid
    private long wareId;             // 商品id
    private long shopId;             // 店铺id
    private String status;           // 状态
    private String attributes;       // Sku属性(100041:150041^1000046:15844 )
    private String jdPrice;          // 京东价格
    private String costPrice;        // 成本价
    private String marketPrice;      // 市场价
    private long stockNum;           // 库存
    private String outerId;          // sku外部id
    private String created;          // 创建时间
    private String modified;         // 修改时间
    private String colorValue;       // 颜色
    private String sizeValue;        // 尺寸
    private String trade_no;         // 流水号(新加的，商品也有)

    public JdSkuBean() {
    }

    @JsonProperty("sku_id")
    public Long getSkuId() {
        return this.skuId;
    }

    @JsonProperty("sku_id")
    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    @JsonProperty("ware_id")
    public long getWareId() {
        return this.wareId;
    }

    @JsonProperty("ware_id")
    public void setWareId(long wareId) {
        this.wareId = wareId;
    }

    @JsonProperty("shop_id")
    public long getShopId() {
        return this.shopId;
    }

    @JsonProperty("shop_id")
    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    @JsonProperty("status")
    public String getStatus() {
        return this.status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("attributes")
    public String getAttributes() {
        return this.attributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    @JsonProperty("jd_price")
    public String getJdPrice() {
        return this.jdPrice;
    }

    @JsonProperty("jd_price")
    public void setJdPrice(String jdPrice) {
        this.jdPrice = jdPrice;
    }

    @JsonProperty("cost_price")
    public String getCostPrice() {
        return this.costPrice;
    }

    @JsonProperty("cost_price")
    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    @JsonProperty("market_price")
    public String getMarketPrice() {
        return this.marketPrice;
    }

    @JsonProperty("market_price")
    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    @JsonProperty("stock_num")
    public long getStockNum() {
        return this.stockNum;
    }

    @JsonProperty("stock_num")
    public void setStockNum(long stockNum) {
        this.stockNum = stockNum;
    }

    @JsonProperty("outer_id")
    public String getOuterId() {
        return this.outerId;
    }

    @JsonProperty("outer_id")
    public void setOuterId(String outerId) {
        this.outerId = outerId;
    }

    @JsonProperty("created")
    public String getCreated() {
        return this.created;
    }

    @JsonProperty("created")
    public void setCreated(String created) {
        this.created = created;
    }

    @JsonProperty("modified")
    public String getModified() {
        return this.modified;
    }

    @JsonProperty("modified")
    public void setModified(String modified) {
        this.modified = modified;
    }

    @JsonProperty("color_value")
    public String getColorValue() {
        return this.colorValue;
    }

    @JsonProperty("color_value")
    public void setColorValue(String colorValue) {
        this.colorValue = colorValue;
    }

    @JsonProperty("size_value")
    public String getSizeValue() {
        return this.sizeValue;
    }

    @JsonProperty("size_value")
    public void setSizeValue(String sizeValue) {
        this.sizeValue = sizeValue;
    }

    @JsonProperty("trade_no")
    public String getTrade_no() {
        return this.trade_no;
    }

    @JsonProperty("trade_no")
    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

}
