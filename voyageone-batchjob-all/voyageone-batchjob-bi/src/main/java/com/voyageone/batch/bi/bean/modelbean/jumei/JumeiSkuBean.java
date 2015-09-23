package com.voyageone.batch.bi.bean.modelbean.jumei;

import java.math.BigDecimal;

/**
 * Created by Kylin on 2015/9/2.
 */
public class JumeiSkuBean {

    private String channel_id;
    private String task_id;
    private String product_code;
    private String sku;
    private String cms_size;
    private String jumei_size;
    private String cms_color;
    private String jumei_color;
    private BigDecimal cms_oversea_official_price;
    private BigDecimal trade_price;
    private BigDecimal market_price;
    private int inventory;
    private String status;

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCms_size() {
        return cms_size;
    }

    public void setCms_size(String cms_size) {
        this.cms_size = cms_size;
    }

    public String getJumei_size() {
        return jumei_size;
    }

    public void setJumei_size(String jumei_size) {
        this.jumei_size = jumei_size;
    }

    public String getCms_color() {
        return cms_color;
    }

    public void setCms_color(String cms_color) {
        this.cms_color = cms_color;
    }

    public String getJumei_color() {
        return jumei_color;
    }

    public void setJumei_color(String jumei_color) {
        this.jumei_color = jumei_color;
    }

    public BigDecimal getCms_oversea_official_price() {
        return cms_oversea_official_price;
    }

    public void setCms_oversea_official_price(BigDecimal cms_oversea_official_price) {
        this.cms_oversea_official_price = cms_oversea_official_price;
    }

    public BigDecimal getTrade_price() {
        return trade_price;
    }

    public void setTrade_price(BigDecimal trade_price) {
        this.trade_price = trade_price;
    }

    public BigDecimal getMarket_price() {
        return market_price;
    }

    public void setMarket_price(BigDecimal market_price) {
        this.market_price = market_price;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
