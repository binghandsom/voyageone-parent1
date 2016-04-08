package com.voyageone.components.jumei.bean;

/**
 * @author james.li on 2016/1/23.
 * @version 2.0.0
 */
public class JmGetProductInfo_Spus_Sku extends JmBaseBean {
    private String sale_on_this_deal;

    private String sku_no;

    private String customs_product_number;

    private String businessman_code;

    private String stocks;

    private String deal_price;

    private String market_price;


    public String getSale_on_this_deal() {
        return sale_on_this_deal;
    }

    public void setSale_on_this_deal(String sale_on_this_deal) {
        this.sale_on_this_deal = sale_on_this_deal;
    }

    public String getCustoms_product_number() {
        return customs_product_number;
    }

    public void setCustoms_product_number(String customs_product_number) {
        this.customs_product_number = customs_product_number;
    }


    public String getStocks() {
        return stocks;
    }

    public void setStocks(String stocks) {
        this.stocks = stocks;
    }

    public String getDeal_price() {
        return deal_price;
    }

    public void setDeal_price(String deal_price) {
        this.deal_price = deal_price;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getSku_no() {
        return sku_no;
    }

    public void setSku_no(String sku_no) {
        this.sku_no = sku_no;
    }

    public String getBusinessman_code() {
        return businessman_code;
    }

    public void setBusinessman_code(String businessman_code) {
        this.businessman_code = businessman_code;
    }
}
